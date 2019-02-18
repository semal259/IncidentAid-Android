package com.cmusv.ias;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.Toast;
import android.widget.ExpandableListView.OnChildClickListener;

public class CommTeamFragment extends Fragment{
	
	private static final String ARG_SECTION_NUMBER = "section_number";
	
	ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    Button btnAddMember;
    SessionManager session;
    IncidentSessionManager inc_session;
    String inc_name;
    String alert_ts;
    HashMap<String, List<String>> userAlert;
    DBAdapter db;
    HashMap<String, String> incident;
    HashMap<String, String> user;
    String currUser;
    BroadcastReceiver sent_reciever;
	BroadcastReceiver delivered_reciever;
    private Timer autoUpdate;
    
    public static CommTeamFragment newInstance(int sectionNumber) {
		CommTeamFragment fragment = new CommTeamFragment();
		Bundle args = new Bundle();
		args.putInt(ARG_SECTION_NUMBER, sectionNumber);
		fragment.setArguments(args);
		return fragment;
	}

	public CommTeamFragment() {
	}
    
    @Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_comm_team, container, false);
		session = new SessionManager(getActivity());
        inc_session = new IncidentSessionManager(getActivity());  
        incident = inc_session.getIncidentDetails();
        inc_name = incident.get(IncidentSessionManager.KEY_INC_NAME);  
        alert_ts = incident.get(IncidentSessionManager.KEY_ALERT_TS);
        user = session.getUserDetails();
        currUser = user.get(SessionManager.KEY_NAME);  
		expListView = (ExpandableListView) rootView.findViewById(R.id.lvExp);
		btnAddMember = (Button) rootView.findViewById(R.id.btnAddMember);
		btnAddMember.setBackgroundColor(Color.rgb(52, 161, 201));
		
	    expListView.setOnChildClickListener(new OnChildClickListener() {
	    	 
            //@Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
            	
            	List<String> alert_list = new ArrayList<String>();
            	db = new DBAdapter(getActivity());
            	String username = listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition);
            	db.open();
            	alert_list = db.getAlertsByUser(username, inc_name);
            	db.close();
            	
            	final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, alert_list);
				AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
				alert.setTitle("Messages");
				alert.setAdapter(adapter, null);
				alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int id) {
	                     dialog.cancel();
	                }
	            });
				alert.show();	
				
                return false;
            }
        });
	    
	    
		btnAddMember.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				
				final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, IASHelper.getAllEnginesNames(getActivity()));
				AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
				alert.setTitle("EngineList");
				alert.setAdapter(adapter, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {	

				        List<String> teamMembers = new ArrayList<String>();
				        List<String> all_team_members = new ArrayList<String>();
				        teamMembers = IASHelper.getUserNamesByEngineName(getActivity(), adapter.getItem(which));
				        if (teamMembers != null)
				        {
					        if(teamMembers.size() > 0)
					        {
					        	IncidentSessionManager inc_session = new IncidentSessionManager(getActivity());
					        	HashMap<String, String> incident = inc_session.getIncidentDetails();
					        	IASIncident oIASIncident = new IASIncident();
					        	try 
					        	{
						        	oIASIncident = IASHelper.getIncidentByIncidentName(getActivity(), incident.get(IncidentSessionManager.KEY_INC_NAME));
						    		all_team_members = oIASIncident.getFirefighters();					    		
						    		all_team_members.addAll(teamMembers);
							        //Fill Incident Object						        
						        	oIASIncident.setIncident_name(inc_name);				        	
						        	oIASIncident.setFirefighters(all_team_members);
						        	//Update new team members to incident
						        	if(IASHelper.updateIncidentByIncidentName(getActivity(), oIASIncident))
						        	{     
						        		createMenu();
						        		List<Directory> responder_list = IASUtilities.getUserContactsForIncident(IASHelper.getUsersByIncidentName(getActivity(), inc_name), currUser);
						    			MessagingUtility mu = new MessagingUtility(getActivity());
						    			for(Directory person:responder_list)
						    				mu.sendSMS(person.getContact(), mu.prepareMessage("INITINC"), sent_reciever, delivered_reciever);
						        	}
						        	else
						        		Toast.makeText(getActivity(), "Unable to add team", Toast.LENGTH_SHORT).show();
					        	}
					        	catch (Exception e) {
					        		Toast.makeText(getActivity(), "Unable to add team", Toast.LENGTH_SHORT).show();
									Log.d("Team Addition Exception", e.getClass().getName());
					        	}
					        }
				        }
					}
				});
	            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int id) {
	                     dialog.cancel();
	                }
	            });
				alert.show();
			} 
   	    });

		return rootView;
	}
    
    
    
    @Override
    public void onResume() {
	    super.onResume();
	    createMenu();
    }
    
    public void refreshView() {
    	Toast.makeText(getActivity(), "Fragment resumed", Toast.LENGTH_SHORT).show();
    }
    
    @Override
    public void onPause() {
    	//autoUpdate.cancel();
    	super.onPause();
    }
    
    @Override
	public void onDestroyView() {	    
		try {
			autoUpdate.cancel();
			if (delivered_reciever != null)
				getActivity().unregisterReceiver(delivered_reciever);
			if (sent_reciever != null)
				getActivity().unregisterReceiver(sent_reciever);
		}
		catch (Exception e) {
			Log.d("CommDashboard Exception", e.getClass().getName());
    	}
		super.onDestroyView();
	}
    
    public void createMenu()
    {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();
        List<IASUser> responderList = new ArrayList<IASUser>(); 
        responderList = IASHelper.getUsersByIncidentName(getActivity(), inc_name);
        listDataChild = IASUtilities.getUsersForIncident(listDataHeader, responderList);	     
        userAlert = new HashMap<String, List<String>>();
		
        autoUpdate = new Timer();
	    autoUpdate.schedule(new TimerTask() {
		    @Override
		    public void run() {
		    	if(getActivity()!=null){
				    getActivity().runOnUiThread(new Runnable() {
					    public void run() {
					    	try {
					    		Log.d("--MENU REFRESH--", "Menu refreshed in 10 seconds");
					        	db = new DBAdapter(getActivity());
					        	String inc = incident.get(IncidentSessionManager.KEY_ALERT_TS);
				    	        db.open();
				    	        userAlert = db.getLatestAlertStatus(inc, inc_name);
				    	        db.close();
					        }
					        catch (Exception e)
					        {
					        	Log.d("SQLite Error", e.getClass().getName());
					        } 
					    	ExpandableListView listView = (ExpandableListView) getActivity().findViewById(R.id.lvExp);
					    	listAdapter = new ExpandableListAdapter(getActivity(), listDataHeader, listDataChild, userAlert);
							listAdapter.notifyDataSetChanged();
							expListView.setAdapter(listAdapter);
							
							for (int position = 0; position < listDataHeader.size(); position++)
					    	    listView.expandGroup(position);
					    }
				    });
		    	}
		    }
	    }, 0, 10000);   
    }
}
