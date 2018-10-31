package com.cmusv.ias;

import java.util.HashMap;
import java.util.Random;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.SmsManager;
import android.widget.Toast;

public class MessagingUtility {
	private DBAdapter db;
	private Activity mActivity;
	String preparedmessage;
	//BroadcastReceiver sent_reciever;
	//BroadcastReceiver delivered_reciever;
	
	MessagingUtility(Activity activity) {
		db = new DBAdapter(activity);
		mActivity = activity;
	}
	
	public String prepareMessage(String messagecontent) {
		Random r = new Random();
		String token = Long.toString(Math.abs(r.nextLong()), 36).substring(0,3);
		preparedmessage = "*!=/" + "MES/" + token + "/" + messagecontent;	
		return preparedmessage;
	}
	
	public void sendSMS(final String phoneNumber, String message, BroadcastReceiver sent_reciever, BroadcastReceiver delivered_reciever) {
	   	String SENT = "SMS_SENT";
    	String DELIVERED = "SMS_DELIVERED";
    	
        PendingIntent sentPI = PendingIntent.getBroadcast(mActivity, 0,
            new Intent(SENT), 0);
        
        PendingIntent deliveredPI = PendingIntent.getBroadcast(mActivity, 0,
            new Intent(DELIVERED), 0);
    	
        //---when the SMS has been sent---
        mActivity.registerReceiver(sent_reciever = new BroadcastReceiver(){
			@Override
			public void onReceive(Context arg0, Intent arg1) {
				switch (getResultCode())
				{
				    case Activity.RESULT_OK:
					    Toast.makeText(mActivity.getBaseContext(), "SMS sent", 
					    		Toast.LENGTH_SHORT).show();
					    break;
				    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
					    Toast.makeText(mActivity.getBaseContext(), "Generic failure", 
					    		Toast.LENGTH_SHORT).show();
					    break;
				    case SmsManager.RESULT_ERROR_NO_SERVICE:
					    Toast.makeText(mActivity.getBaseContext(), "No service", 
					    		Toast.LENGTH_SHORT).show();
					    break;
				    case SmsManager.RESULT_ERROR_NULL_PDU:
					    Toast.makeText(mActivity.getBaseContext(), "Null PDU", 
					    		Toast.LENGTH_SHORT).show();
					    break;
				    case SmsManager.RESULT_ERROR_RADIO_OFF:
					    Toast.makeText(mActivity.getBaseContext(), "Radio off", 
					    		Toast.LENGTH_SHORT).show();
					    break;
				}
			}
        }, new IntentFilter(SENT));
        
        //---when the SMS has been delivered---
        mActivity.registerReceiver(delivered_reciever = new BroadcastReceiver(){
			@Override
			public void onReceive(Context arg0, Intent arg1) {
				switch (getResultCode())
				{
				    case Activity.RESULT_OK:
				    	HashMap<String, String> message_hash = new HashMap<String, String>();
				    	message_hash = IASUtilities.getMessageText(preparedmessage);
				    	db.open();
				    	db.updateDeliveryStatus(phoneNumber, message_hash.get("MSG_TOKEN"));
				    	db.close();
					    Toast.makeText(mActivity.getBaseContext(), "SMS delivered", 
					    		Toast.LENGTH_SHORT).show();
					    break;
				    case Activity.RESULT_CANCELED:
					    Toast.makeText(mActivity.getBaseContext(), "SMS not delivered", 
					    		Toast.LENGTH_SHORT).show();
					    break;					    
				}
			}
        }, new IntentFilter(DELIVERED));        
    	
        SmsManager sms = SmsManager.getDefault();
        String[] listOfContacts = phoneNumber.split(",");
        for(String contact:listOfContacts)
        {
        	sms.sendTextMessage(contact, null, message, sentPI, deliveredPI); 
        }	
	}
	
	/*public void disconnectReciever()
	{
		if (delivered_reciever != null)
			mActivity.unregisterReceiver(delivered_reciever);
		if (sent_reciever != null)
			mActivity.unregisterReceiver(sent_reciever);
	}*/
}

