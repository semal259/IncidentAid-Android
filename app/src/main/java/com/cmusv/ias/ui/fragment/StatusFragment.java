package com.cmusv.ias.ui.fragment;

import java.util.HashMap;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import com.cmusv.ias.R;
import com.cmusv.ias.data.IASHelper;
import com.cmusv.ias.data.IASUser;
import com.cmusv.ias.ui.activity.ProfileActivity;
import com.cmusv.ias.utils.SessionManager;

public class StatusFragment extends Fragment {
	SessionManager session;
	ImageView imgBattery;
	ImageView imgConnectivity;
	Button btnEngine;
	//Button btnLogout;
	Spinner spinner;
	HashMap<String, String> curr_user;
	
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
		session = new SessionManager(getActivity());
		View statusView = inflater.inflate(R.layout.fragment_status, container);
		btnEngine = (Button) statusView.findViewById(R.id.btnEngine);
		//btnLogout = (Button) statusView.findViewById(R.id.btnLogout);		
		btnEngine.setBackgroundColor(Color.RED);
		curr_user = session.getUserDetails();
		
		// Update engine assignment
		btnEngine.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				
				final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, IASHelper.getAllEnginesNames(getActivity()));
				AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
				alert.setTitle("EngineList");
				alert.setAdapter(adapter, new DialogInterface.OnClickListener() {
					
					@Override
					// When engine selected, store for current user and update button text
					public void onClick(DialogInterface dialog, int which) {
						IASUser oIASUser = new IASUser();
						oIASUser.setEngine_name(adapter.getItem(which));
						oIASUser.setUser_name(curr_user.get(SessionManager.KEY_NAME));
						if(IASHelper.updateUserByUserName(getActivity(), oIASUser))
						{
							session.setEngine(adapter.getItem(which));
							btnEngine.setText(adapter.getItem(which));
							Intent i = new Intent(getActivity(), ProfileActivity.class);
							startActivity(i);
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
		
		
		/*btnLogout.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				IncidentSessionManager inc_session = new IncidentSessionManager(getActivity());
				inc_session.closeIncident();
				session.logoutUser();
			} 
   	    }); */
		
		return statusView;
	}

	public void onResume() {
		super.onResume();
		// hide the status bar - https://developer.android.com/training/system-ui/status.html
		View decorView = getActivity().getWindow().getDecorView();
		int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
		decorView.setSystemUiVisibility(uiOptions);
		session = new SessionManager(getActivity());
		curr_user = session.getUserDetails();
		btnEngine.setText(curr_user.get(SessionManager.KEY_ENGINE));
	}
	
	public void updateBatteryStatus(int batteryPercentage) {
		imgBattery = (ImageView) getView().findViewById(R.id.imgBattery);
		if (batteryPercentage > 80) {
			imgBattery.setImageResource(R.drawable.stat_sys_battery_100);
		} else if (batteryPercentage > 60) {
			imgBattery.setImageResource(R.drawable.stat_sys_battery_80);
		} else if (batteryPercentage > 40) {
			imgBattery.setImageResource(R.drawable.stat_sys_battery_60);
		} else if (batteryPercentage > 20) {
			imgBattery.setImageResource(R.drawable.stat_sys_battery_40);
		} else if (batteryPercentage > 10) {
			imgBattery.setImageResource(R.drawable.stat_sys_battery_20);
		} else {
			imgBattery.setImageResource(R.drawable.stat_sys_battery_10);
		}
		
	}
	
	public void updateConnectivityStatus(boolean isConnected) {
		imgConnectivity = (ImageView) getView().findViewById(R.id.imgConnectivity);
		if (isConnected) {
			imgConnectivity.setImageResource(R.drawable.connectivity_good);
		} else {
			imgConnectivity.setImageResource(R.drawable.connectivity_bad);
		}
	}
}
