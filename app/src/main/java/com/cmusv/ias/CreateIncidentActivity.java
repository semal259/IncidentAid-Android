package com.cmusv.ias;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CreateIncidentActivity extends Activity {

	private Button btnCreateIncident;
	private String EMPTY = "empty";
	BroadcastReceiver sent_reciever;
	BroadcastReceiver delivered_reciever;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_incident);
        
		btnCreateIncident = (Button) findViewById(R.id.btnCreateIncident);
		btnCreateIncident.setBackgroundColor(Color.rgb(52, 161, 201));		
	}
	
	private BroadcastReceiver batteryReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			int batteryPercent = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
			Log.d("BATTERY", "batteryPercent = " + batteryPercent);
			StatusFragment statusFragment = (StatusFragment) getFragmentManager().findFragmentById(R.id.fgmtStatus);
			statusFragment.updateBatteryStatus(batteryPercent);
		}
	};
	
	private BroadcastReceiver connectivityReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			boolean isConnected = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected();
			StatusFragment statusFragment = (StatusFragment) getFragmentManager().findFragmentById(R.id.fgmtStatus);
			statusFragment.updateConnectivityStatus(isConnected);
		}
	};
	
	@Override
	public void onResume() {
		super.onResume();
		IntentFilter ifBattery = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
		IntentFilter ifConnectivity = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
		this.registerReceiver(this.batteryReceiver, ifBattery);
		this.registerReceiver(this.connectivityReceiver, ifConnectivity);
	}
	
	@Override
	public void onPause() {
		super.onPause();
		this.unregisterReceiver(this.batteryReceiver);
		this.unregisterReceiver(this.connectivityReceiver);
	}
	
	public void initCommDashboard(View view) {
		SessionManager session = new SessionManager(getApplicationContext());
		HashMap<String, String> user = session.getUserDetails();
        String currUser =  user.get(SessionManager.KEY_NAME);
		String incidentName = ((EditText) findViewById(R.id.txtincident_notes)).getText().toString();
		String incidentAddress = ((EditText) findViewById(R.id.txtincident_address)).getText().toString();
		
		List<String> fireFighters = new ArrayList<String>();
		fireFighters = IASHelper.getUserNamesByEngineName(getApplicationContext(), user.get(SessionManager.KEY_ENGINE));
		
		IASIncident oIASIncident = new IASIncident();
		oIASIncident.setCommander(currUser);
		oIASIncident.setStart_time(IASUtilities.getCurrentDateTime());
		oIASIncident.setIncident_name(incidentName);
		oIASIncident.setEnd_time(this.EMPTY);
		oIASIncident.setFirefighters(fireFighters);
		
		incidentAddress = IASUtilities.getResolvedAddress(incidentAddress, this, oIASIncident);		

		if(IASHelper.createIncident(getApplicationContext(), oIASIncident))
		{
			IncidentSessionManager inc_session = new IncidentSessionManager(getApplicationContext());
			inc_session.createIncidentSession(oIASIncident.getIncident_name(), currUser, incidentAddress);

			List<Directory> responder_list = IASUtilities.getUserContactsForIncident(IASHelper.getUsersByIncidentName(this, incidentName), currUser);
			MessagingUtility mu = new MessagingUtility(this);
			for(Directory person:responder_list)
				mu.sendSMS(person.getContact(), mu.prepareMessage("INITINC"), sent_reciever, delivered_reciever);
			
			Bundle bundle = new Bundle();
			bundle.putString("INCIDENTNAME", incidentName); 
			Intent intent = new Intent(this, CDashboardActivity.class);
			intent.putExtras(bundle);
			startActivity(intent);
		}
		else
		{
			Toast.makeText(this, "Unable to create incident", Toast.LENGTH_SHORT).show();
		}
	}
	
	@Override
    public void onDestroy() 
	{
		if (delivered_reciever != null)
			this.unregisterReceiver(delivered_reciever);
		if (sent_reciever != null)
			this.unregisterReceiver(sent_reciever);
		super.onDestroy();
	}
}