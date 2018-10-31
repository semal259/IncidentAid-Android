package com.cmusv.ias;

import java.util.HashMap;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.TextView;

public class ResponderCOPActivity extends Activity {
	private IASIncident oIASIncident;
	private TextView txtLastAlert;
	BroadcastReceiver sent_reciever;
	BroadcastReceiver delivered_reciever;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_responder_cop);
		txtLastAlert = (TextView) findViewById(R.id.txtLastAlert);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		
		
		try {
			SessionManager session = new SessionManager(getApplicationContext());
	        HashMap<String, String> user = session.getUserDetails();
	        setTitle(user.get(SessionManager.KEY_NAME));
	        oIASIncident = IASHelper.getCurrentIncidentByUserName(this, user.get(SessionManager.KEY_NAME));
	        txtLastAlert.setText(IASHelper.getLastAlertByIncidentName(this, oIASIncident.getIncident_name()));
        } catch (Exception e) {
			Log.d("ResponderCOPActivity Exception", e.getClass().getName());
			if (e.getLocalizedMessage() != null)
				Log.d("ResponderCOPActivity Exception", e.getLocalizedMessage());
    	}
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
	
	public void onResume() {
		super.onResume();
		IntentFilter ifBattery = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
		IntentFilter ifConnectivity = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
		this.registerReceiver(this.batteryReceiver, ifBattery);
		this.registerReceiver(this.connectivityReceiver, ifConnectivity);
	}

	public void onPause() {
		super.onPause();
		this.unregisterReceiver(this.batteryReceiver);
		this.unregisterReceiver(this.connectivityReceiver);
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
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK) {
	      return false;
	    }
	    else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) 
	    {
	    	SessionManager session = new SessionManager(this);
	    	HashMap<String, String> user = new HashMap<String, String>();
	    	user = session.getUserDetails();
	    	IASIncident inc = new IASIncident();
	    	inc = IASHelper.getCurrentIncidentByUserName(this, user.get(SessionManager.KEY_NAME));
	    	
	    	IASUser commander_info = IASHelper.getUserByUserName(this, inc.getCommander());
	    	
	    	MessagingUtility mu = new MessagingUtility(this);
	    	String message = mu.prepareMessage("INITMAYDAY;") + user.get(SessionManager.KEY_NAME);
	    	try {
		    	IASEvent incident_event = IASUtilities.parseToEvent(message, user.get(SessionManager.KEY_NAME), 1, 2);
	    		IASHelper.createEvent(this, incident_event);
	    	}
	    	catch (Exception e){ Log.d("Create Event Exception", "Create Event Exception"); }
	    	mu.sendSMS(commander_info.getContact(), mu.prepareMessage("INITMAYDAY;") + user.get(SessionManager.KEY_NAME), sent_reciever, delivered_reciever);
	    	return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}
}
