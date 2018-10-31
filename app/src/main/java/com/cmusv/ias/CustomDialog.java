package com.cmusv.ias;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.AnimationDrawable;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/** Class Must extends with Dialog */

@SuppressLint("NewApi")
public class CustomDialog extends Dialog {
	private AnimationDrawable animation;
	private SoundManager mSoundManager = new SoundManager();
	private TextView msgTextView;
	Context context;
	String commanderNumber;
	private List<String> msgToBeAck = new ArrayList<String>();

	@SuppressWarnings("deprecation")
	public CustomDialog(Context context, String commanderNumber, String msg) {
		super(context);

		this.context = context;
		this.commanderNumber = commanderNumber;
		msgToBeAck.add(msg);

		/** 'Window.FEATURE_NO_TITLE' - Used to hide the title */
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.flashscreen);
		getWindow().setLayout(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT);

		RelativeLayout layout = (RelativeLayout) findViewById(R.id.RelativeLayout01);
		
		layout.setBackgroundResource(getMessageType(msg));	
		animation = (AnimationDrawable) layout.getBackground();	
		mSoundManager.initSounds(context);
		mSoundManager.addSound(1, R.raw.ring);
        msgTextView = (TextView) findViewById(R.id.FlashScreenTextView02);

        SharedData sd = (SharedData)context.getApplicationContext();
        sd.setFlashScreen(this);
        updateTextView();
	}
	
	private int getMessageType(String msg) {
		
		if(msg.contains("Vacate")) {
			return R.drawable.vacate_animation;
		} else if(msg.contains("Utilities On")) {
			return R.drawable.utilities_on_animation;
		} else if(msg.contains("Utilities Off")) {
			return R.drawable.utilities_off_animation;
		} else if(msg.contains("RIC")) {
			return R.drawable.rescue_in_prog_animation;
		} else if(msg.contains("PAR")) {
			return R.drawable.par_animation;
		} else if(msg.contains("Life Haz")) {
			return R.drawable.life_haz_animation;
		} else if(msg.contains("All Clear")) {
			return R.drawable.all_clear_animation;
		} else if(msg.contains("Vertical Vent")) {
			return R.drawable.vert_vent_animation;
		} else if(msg.contains("Cross Vent")) {
			return R.drawable.cross_vent_animation;
		} else if(msg.contains("Mayday Accepted")) {
			return R.drawable.mayday_acc_animation;
		} else {
			return R.drawable.color_animation;
		}

	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		 
        SharedData sd = (SharedData)context.getApplicationContext();
        sd.setFlashScreen(this);
        updateTextView();
	}

	public void updateTextView()
	{
		HashMap<String, String> message_hash = new HashMap<String, String>();
		for (String msg:msgToBeAck)
			message_hash = IASUtilities.getMessageText(msg);
		msgTextView.setText(message_hash.get("MSG_TEXT"));
	}

	public List<String> getMsgToBeAck() {
		return msgToBeAck;
	}

	public void setMsgToBeAck(List<String> msgToBeAck) {
		this.msgToBeAck = msgToBeAck;
	}

	public String getCommanderNumber() {
		return commanderNumber;
	}

	public void setCommanderNumber(String commanderNumber) {
		this.commanderNumber = commanderNumber;
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {

		animation.start();
		mSoundManager.playLoopedSound(1);
	}

	// ---sends a SMS message to another device---
	private void sendSMS(String phoneNumber, String message) {

		Log.v("--Gerard--", "CustomDialog->sendSMS()");

		String SENT = "SMS_SENT";
		String DELIVERED = "SMS_DELIVERED";

		PendingIntent sentPI = PendingIntent.getBroadcast(context, 0,
				new Intent(SENT), 0);

		PendingIntent deliveredPI = PendingIntent.getBroadcast(context, 0,
				new Intent(DELIVERED), 0);

		// ---when the SMS has been sent---
		context.registerReceiver(new BroadcastReceiver() {
			@Override
			public void onReceive(Context arg0, Intent arg1) {
				switch (getResultCode()) {
				case Activity.RESULT_OK:
					Toast.makeText(context, "SMS sent", Toast.LENGTH_SHORT)
							.show();
					break;
				case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
					Toast.makeText(context, "Generic failure",
							Toast.LENGTH_SHORT).show();
					break;
				case SmsManager.RESULT_ERROR_NO_SERVICE:
					Toast.makeText(context, "No service", Toast.LENGTH_SHORT)
							.show();
					break;
				case SmsManager.RESULT_ERROR_NULL_PDU:
					Toast.makeText(context, "Null PDU", Toast.LENGTH_SHORT)
							.show();
					break;
				case SmsManager.RESULT_ERROR_RADIO_OFF:
					Toast.makeText(context, "Radio off", Toast.LENGTH_SHORT)
							.show();
					break;
				}
			}
		}, new IntentFilter(SENT));

		// ---when the SMS has been delivered---
		context.registerReceiver(new BroadcastReceiver() {
			@Override
			public void onReceive(Context arg0, Intent arg1) {
				switch (getResultCode()) {
				case Activity.RESULT_OK:
					Toast.makeText(context, "SMS delivered", Toast.LENGTH_SHORT)
							.show();
					break;
				case Activity.RESULT_CANCELED:
					Toast.makeText(context, "SMS not delivered",
							Toast.LENGTH_SHORT).show();
					break;
				}
			}
		}, new IntentFilter(DELIVERED));

		SmsManager sms = SmsManager.getDefault();
		String[] listOfContacts = phoneNumber.split(",");
		for (String contact : listOfContacts) {
			sms.sendTextMessage(contact, null, message, sentPI, deliveredPI);
		}

	}

	public String createAcknowledgement(String messageStamp) {
		String ack = MessageParser.APPLICATION_STAMP + "/" + MessageParser.ACK_STAMP + "/"
				+ messageStamp + "/" + "OK";

		return ack;
	}
	 
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)  {
	   if ( keyCode == KeyEvent.KEYCODE_VOLUME_UP ) {
		   SoundManager.stopSound(1);
		   SoundManager.cleanup();
		   HashMap<String, String> message_hash = new HashMap<String, String>();
		   SessionManager session = new SessionManager(context);
		   HashMap<String, String> user = new HashMap<String, String>();
		   user = session.getUserDetails();	    	
		   Intent Intent = new Intent(this.getContext(), ResponderCOPActivity.class);
	       Intent.addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK); 
	        
	       for(String msg:msgToBeAck)
	       {
	    	   try {
	    		   IASEvent incident_event = IASUtilities.parseToEvent(msg, user.get(SessionManager.KEY_NAME), 2, 2);
	    		   IASHelper.createEvent(context, incident_event);
	    	   }
	    	   catch (Exception e){ Log.d("Create Event Exception", "Create Event Exception"); }
	    	   message_hash = IASUtilities.getMessageText(msg);
	    	   sendSMS(getCommanderNumber(), createAcknowledgement(message_hash.get("MSG_TOKEN")));	        	
	       }
	        
	       setMsgToBeAck(null);
	       SharedData sd = (SharedData)context.getApplicationContext();
	       sd.setFlashScreen(null);
	        
	       this.getContext().startActivity(Intent);
	        
	       dismiss();
	       return true;
	   }

	   // let the system handle all other key events
	   return super.onKeyDown(keyCode, event);
	}
	


}
