package com.cmusv.ias.ui.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.cmusv.ias.data.Directory;
import com.cmusv.ias.data.IASEvent;
import com.cmusv.ias.data.IASHelper;
import com.cmusv.ias.data.IASUser;
import com.cmusv.ias.data.IASUtilities;
import com.cmusv.ias.IncidentSessionManager;
import com.cmusv.ias.ui.DBAdapter;
import com.cmusv.ias.ui.ImageAdapter;
import com.cmusv.ias.utils.MessagingUtility;
import com.cmusv.ias.R;
import com.cmusv.ias.utils.SessionManager;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class CommAlertFragment extends Fragment {
	GridView gridView;
	private static final String ARG_SECTION_NUMBER = "section_number";
	DBAdapter db;// = new DBAdapter(getActivity());
	BroadcastReceiver sent_reciever;
	BroadcastReceiver delivered_reciever;

	public static CommAlertFragment newInstance(int sectionNumber) {
		CommAlertFragment fragment = new CommAlertFragment();
		Bundle args = new Bundle();
		args.putInt(ARG_SECTION_NUMBER, sectionNumber);
		fragment.setArguments(args);
		return fragment;
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		View rootView = inflater.inflate(R.layout.fragment_comm_alert, container, false);	 
        GridView gridView = (GridView) rootView.findViewById(R.id.grid_view);
        
        int[] mThumbIds = getThumbs();
        
        gridView.setAdapter(new ImageAdapter(getActivity(), getResources(), mThumbIds, 350, 180));
        gridView.setOnItemClickListener(new OnItemClickListener()
        {
        	String selection;
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3)
            {
            	switch (position) 
            	{
	    			case 0: selection = "Vacate";
	    					break;
	    			case 1: selection = "Utilities On";
	    					break;
	    			case 2: selection = "Utilities Off";
	    					break;
	    			case 3: selection = "RIC";
	    					break;
	    			case 4: selection = "PAR";
	    					break;
	    			case 5: selection = "Life Haz";
	    					break;
	    			case 6: selection = "All Clear";
	    					break;
	    			case 7: selection = "Vertical Vent";
	    					break;
	    			case 8: selection = "Cross Vent";
	    					break;
            	}
            	AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
    		    alert.setMessage("Are you sure you want to send "+ selection+ " ?")
    	            .setCancelable(false)
    	            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
    	                public void onClick(DialogInterface dialog, int id) {
    	                	MessagingUtility msgUtility = new MessagingUtility(getActivity());
    	                	String tempmessage = msgUtility.prepareMessage(selection);
    	
    	                	IncidentSessionManager inc_session = new IncidentSessionManager(getActivity());
    	                	HashMap<String, String> incident = inc_session.getIncidentDetails();
    	                	SessionManager session = new SessionManager(getActivity());
    	                	HashMap<String, String> user = session.getUserDetails();
    	                	String userName = user.get(SessionManager.KEY_NAME);
    	                	db = new DBAdapter(getActivity());
    	                	List<IASUser> responderList = new ArrayList<IASUser>();
    	                	try 
    	                	{
	    	                    responderList = IASHelper.getUsersByIncidentName(getActivity(), incident.get(IncidentSessionManager.KEY_INC_NAME));
	    	                	if(responderList != null)
	    	                	{
	    	                		List<Directory> user_contact_list = IASUtilities.getUserContactsForIncident(responderList, userName);
		    	                	for(Directory userNo : user_contact_list) {
		    	                		msgUtility.sendSMS(userNo.getContact(), tempmessage, sent_reciever, delivered_reciever);
		    	                		IASEvent event = IASUtilities.parseToEvent(tempmessage, userNo.getName(), 1, 1);
		    	                		IASHelper.createEvent(getActivity(), event);
		    	                	}
	    	                		String timestamp = IASUtilities.getCurrentDateTime();
	    	                		inc_session.setAlertTS(timestamp);
		    	                	db.open();
	    	                		db.insertMessageLog(tempmessage, incident.get(IncidentSessionManager.KEY_INC_NAME), userName, user_contact_list, timestamp);
	    	                		db.close();
	    	                	}
    	                	}
    	                	catch(Exception e)
    	                	{
    	                		Toast.makeText(getActivity(), "Unable to send SMS", Toast.LENGTH_SHORT).show();
    	                	}
    	                }
    	            })
    	            .setNegativeButton("No", new DialogInterface.OnClickListener() {
    	                public void onClick(DialogInterface dialog, int id) {
    	                     dialog.cancel();
    	                }
    	            });
    	     	alert.show();
            }
        });

        return rootView;
	}
	
	private int[] getThumbs() {
		int[] ids = {
			R.drawable.vacate_icon,
			R.drawable.utilities_on_icon,
			R.drawable.utilities_off_icon,
			R.drawable.rescue_in_prog_icon,
			R.drawable.par_icon,
			R.drawable.life_haz_icon,
			R.drawable.all_clear_icon,
			R.drawable.vert_vent_icon,
			R.drawable.cross_vent_icon
		};
		
		return ids;
	}
	
	@Override
    public void onDestroy() {
		if (delivered_reciever != null)
			getActivity().unregisterReceiver(delivered_reciever);
		if (sent_reciever != null)
			getActivity().unregisterReceiver(sent_reciever);
		super.onDestroy();
	}
}

