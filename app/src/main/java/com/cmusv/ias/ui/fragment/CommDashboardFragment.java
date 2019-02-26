package com.cmusv.ias.ui.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.cmusv.ias.data.IASHelper;
import com.cmusv.ias.data.IASIncident;
import com.cmusv.ias.data.IASUser;
import com.cmusv.ias.data.IASUtilities;
import com.cmusv.ias.IncidentSessionManager;
import com.cmusv.ias.ui.DBAdapter;
import com.cmusv.ias.ui.activity.ProfileActivity;
import com.cmusv.ias.utils.MessagingUtility;
import com.cmusv.ias.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.StreetViewPanoramaFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class CommDashboardFragment extends Fragment {

	private static final String ARG_SECTION_NUMBER = "section_number";
	private String incident_address = "400 Castro St Mountain View, CA 94041";
    private String incident_name = null;
	private GoogleMap map;
    private StreetViewPanorama mSvp;
    private Button btnTransferOwnership;
    IncidentSessionManager inc_session;
    HashMap<String, String> incident;
    BroadcastReceiver sent_reciever;
	BroadcastReceiver delivered_reciever;

	public static CommDashboardFragment newInstance(int sectionNumber) {
		CommDashboardFragment fragment = new CommDashboardFragment();
		Bundle args = new Bundle();
		args.putInt(ARG_SECTION_NUMBER, sectionNumber);
		fragment.setArguments(args);
		return fragment;
	}

	public CommDashboardFragment() {
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_comm_dashboard, container, false);

		//Button to transfer ownership to incoming commander
		btnTransferOwnership = (Button) rootView.findViewById(R.id.btnTransferOwnership); 
        btnTransferOwnership.setBackgroundColor(Color.rgb(52, 161, 201));
        
        //Text view to display dispatcher incident creation notes 
        TextView incident_details_label = (TextView) rootView.findViewById(R.id.incident_details_label);
        
        inc_session = new IncidentSessionManager(getActivity());
        incident = inc_session.getIncidentDetails();
        incident_address = incident.get(IncidentSessionManager.KEY_INC_ADDRESS);
        incident_name = incident.get(IncidentSessionManager.KEY_INC_NAME);
        incident_details_label.setText(incident_name);
        	
        try {
        	//Add map with a marker for the address
            LatLng inc_location = IASUtilities.getGeoCoords(incident_address, getActivity());
    		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.mapCommander)).getMap();	
    		if (map != null){
    			Marker marker = map.addMarker(new MarkerOptions()
    				.position(inc_location)
    				.title(incident_address));
    			marker.showInfoWindow();
    			map.moveCamera(CameraUpdateFactory.newLatLngZoom(inc_location, 15));
    			map.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
    		}
    		//Street View
    		//if (mSvp == null) {
    			mSvp = ((StreetViewPanoramaFragment)getFragmentManager().findFragmentById(R.id.streetviewpanoramaCommander)).getStreetViewPanorama();
    			//if (mSvp != null) 
    			    mSvp.setPosition(inc_location);
    		//}
		}
		catch (Exception e) {
			Log.d("Comm Dashboard Exception", e.getClass().getName());
    	}
        
        
        btnTransferOwnership.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				List<IASUser> user_names_by_role = new ArrayList<IASUser>();
				user_names_by_role = IASHelper.getUsersByJobRole(getActivity(), "Commander");
				
				if(user_names_by_role != null) 
				{
					final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, IASUtilities.getUserNamesFromUser(user_names_by_role));
					AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
					alert.setTitle("Commander");
					alert.setAdapter(adapter, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							try {
								MessagingUtility mp = new MessagingUtility(getActivity());
								
								IASUser new_comm = new IASUser();
					        	new_comm = IASHelper.getUserByUserName(getActivity(), adapter.getItem(which));
								
					        	List<IASUser> users_in_commanders_engine = new ArrayList<IASUser>();
					        	users_in_commanders_engine = IASHelper.getUsersByEngineName(getActivity(), new_comm.getEngine_name());
					        						        				        	
								IASIncident oIASIncident = new IASIncident();
								oIASIncident.setIncident_name(incident_name);
						        oIASIncident.setCommander(adapter.getItem(which));
						        List<String> responders = new ArrayList<String>();
						        responders = IASUtilities.getUserNamesFromUser(IASHelper.getUsersByIncidentName(getActivity(), incident_name));					        
						        for(IASUser user : users_in_commanders_engine)
						        	responders.add(user.getUser_name());
						        oIASIncident.setFirefighters(responders);
						        if(IASHelper.updateIncidentByIncidentName(getActivity(), oIASIncident)) {
						        	for(IASUser user : users_in_commanders_engine)
						        	{
							        	if(user.getUser_name().equals(adapter.getItem(which)))
							        		mp.sendSMS(user.getContact(), mp.prepareMessage("INITCOMM"), sent_reciever, delivered_reciever);
							        	else
							        		mp.sendSMS(user.getContact(), mp.prepareMessage("INITINC"), sent_reciever, delivered_reciever);
						        	}
						        	inc_session.closeIncident();
						        	DBAdapter db = new DBAdapter(getActivity());
						        	db.open();
						        	db.destroyDB();
						        	db.close();
						        	Toast.makeText(getActivity(), "You will now be switched to Responder View", Toast.LENGTH_SHORT).show();
						        	Intent i = new Intent(getActivity(), ProfileActivity.class);
									startActivity(i);
						        }
						        else
						        	Toast.makeText(getActivity(), "Unable to transfer ownership", Toast.LENGTH_SHORT).show();
							}
							catch(Exception e){
								Toast.makeText(getActivity(), "Unable to transfer ownership", Toast.LENGTH_SHORT).show();
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
				else
					Toast.makeText(getActivity(), "No commanders available", Toast.LENGTH_SHORT).show();
			}
		});
        
		return rootView;
	}
	
	@Override
	public void onResume() {
		try {
			super.onResume();
		}
		catch (Exception e) {
			Log.d("Comm Dashboard Exception", e.getClass().getName());
    	}
	}
	
	@Override
	public void onDestroyView() {	    
		try {
			if (delivered_reciever != null)
				getActivity().unregisterReceiver(delivered_reciever);
			if (sent_reciever != null)
				getActivity().unregisterReceiver(sent_reciever);
		    MapFragment f = (MapFragment) getFragmentManager().findFragmentById(R.id.mapCommander);
		    StreetViewPanoramaFragment s = (StreetViewPanoramaFragment) getFragmentManager().findFragmentById(R.id.streetviewpanoramaCommander);
		    android.app.FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
		    if (f != null) 
		    	ft.remove(f); 
		    if (s != null) 
		    	ft.remove(s); 
		    ft.commit();   
		}
		catch (Exception e) {
			Log.d("Comm Dashboard Exception", e.getClass().getName());
    	}
		super.onDestroyView();
	}
}
