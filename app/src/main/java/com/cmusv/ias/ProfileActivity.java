package com.cmusv.ias;

import java.util.HashMap;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ProfileActivity extends Activity {
	private TextView txtViewUsername;
	private TextView txtViewContact;
	private TextView txtViewEngine;
	SessionManager session;

	Intent i;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d("ProfileActivity", "onCreate()");
		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_profile);
		
		session = new SessionManager(getApplicationContext());
		session.checkLogin();
        HashMap<String, String> user = session.getUserDetails();
        String name = user.get(SessionManager.KEY_NAME);
        String engine = user.get(SessionManager.KEY_ENGINE);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);   
        String contact = prefs.getString("MobileNumber", "");
        if (contact==null || contact.isEmpty()) {
			i = new Intent(this, SettingsActivity.class);
			startActivity(i);
		}
         
		if(!session.isLoggedIn()) {
			Log.d("ProfileActivity", "ERROR: not logged in");
			return;
		}
		
		
		IASIncident oIASIncident = new IASIncident();
		IncidentSessionManager inc_session = new IncidentSessionManager(getApplicationContext());
		HashMap<String, String> incident = inc_session.getIncidentDetails();
        String inc_commander = incident.get(IncidentSessionManager.KEY_INC_COMMANDER);
		
        if (inc_session.hasOpenIncident() && name.equals(inc_commander)) {
        	Log.d("ProfileActivity", "name.equals(inc_commander)");
			i = new Intent(this, CDashboardActivity.class);
			startActivity(i);
			return;
        }
        
		oIASIncident = IASHelper.getCurrentIncidentByUserName(this, name);
			
		if(oIASIncident != null && oIASIncident.getIncident_name() != null) {
			if(name.equals(oIASIncident.getCommander())) {
				String address = oIASIncident.getStreet() + ", " + oIASIncident.getCity() + ", " + oIASIncident.getState() + ", " + oIASIncident.getZip();
				inc_session.createIncidentSession(oIASIncident.getIncident_name(), oIASIncident.getCommander(), address);
				i = new Intent(this, CDashboardActivity.class);
				startActivity(i);
				return;
			} else {
				i = new Intent(this, ResponderCOPActivity.class);
				startActivity(i);
				return;
			}	
		}
					
		txtViewUsername = (TextView) findViewById(R.id.responder_username);
		txtViewContact = (TextView) findViewById(R.id.responder_contact);
		txtViewEngine = (TextView) findViewById(R.id.responder_engine);
		
		Log.d("ProfileActivity", "username = " + name);
		txtViewUsername.setText(name);
		Log.d("ProfileActivity", "contact = " + contact);
		txtViewContact.setText(contact);
		Log.d("ProfileActivity", "engine = " + engine);
		txtViewEngine.setText(engine);

		Button btnCreateIncident = (Button) findViewById(R.id.btnOpenCreateIncident);
		btnCreateIncident.setBackgroundColor(Color.rgb(52, 161, 201));
		Button btnLogout = (Button) findViewById(R.id.btnLogout);
		btnLogout.setBackgroundColor(Color.RED);

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
	
	public void initCreateIncident(View view) {
	    Intent intent = new Intent(this, CreateIncidentActivity.class);
	    startActivity(intent);
	}
	
	public void onLogoutClick(View view) {
		session.logoutUser();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.cdashboard, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.		
		Intent i = new Intent(this, SettingsActivity.class);
		startActivity(i);
		return true;
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK) {
	      return false;
	    }
	    return super.onKeyDown(keyCode, event);
	}
}
