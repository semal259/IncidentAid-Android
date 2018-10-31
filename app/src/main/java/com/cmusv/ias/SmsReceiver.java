package com.cmusv.ias;

import java.util.HashMap;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class SmsReceiver extends BroadcastReceiver
{
	public IASHelper globalEvents;
	
	@Override
	public void onReceive(Context context, Intent intent) 
	{
		Log.v("--Gerard--","SmsReceiver->onReceive()");

    	SharedData sd = (SharedData)context.getApplicationContext();
    	globalEvents = new IASHelper(sd.getLocalContact(), sd.getLocalRole()); //Global class now.
		
		//---get the SMS message passed in---
		Bundle bundle = intent.getExtras();        
		SmsMessage[] msgs = null;
		String msg = "";  
		String smsSenderNum = "";
		if (bundle != null)
		{
			//---retrieve the SMS message received---
			Object[] pdus = (Object[]) bundle.get("pdus");
			msgs = new SmsMessage[pdus.length];            
			for (int i=0; i<msgs.length; i++){
				msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);                
				msg += msgs[i].getMessageBody().toString();      
				smsSenderNum = msgs[i].getOriginatingAddress();
			}
				
			//---display the new SMS message---
			
			MessageParser mp  = new MessageParser(msg);
			Log.v("SMS Sender Number",smsSenderNum);
			Toast.makeText(context, smsSenderNum, Toast.LENGTH_SHORT).show();
			
			Log.v("--Gerard--","SmsReceiver->onReceive()"+"->"+mp.getMessage()+"->"+mp.getMessageStamp()+"->"+smsSenderNum);
			
			// only fire the flashing screen when it is an sms for app and it is not an ack sms
			if(mp.isAppMessage())
			{
				globalEvents.addBufferEvents("incident", smsSenderNum, mp.getMessageStamp(), "ACK");
				
				if(msg.contains("INITINC") || msg.contains("INITCOMM"))
				{
					Intent i = new Intent(context, ProfileActivity.class);
					i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					context.startActivity(i);
				}
				else if(msg.contains("MAYDAY"))
				{
					DBAdapter db = new DBAdapter(context);
					db.open();
					db.updateMayday(smsSenderNum);
					db.close();
					String[] str = msg.split(";");
					Intent i = new Intent(context, MaydayActivity.class);
					i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					i.putExtra("Sender", str[1]);
					i.putExtra("SenderNo", smsSenderNum);
					context.startActivity(i);
				}
				else if(msg.contains("STOPINC"))
				{
					String message[] = msg.split(";");
					Intent i = new Intent(context, ProfileActivity.class);
					i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					context.startActivity(i);
					Toast.makeText(context, "Fire was put out at " + message[1], Toast.LENGTH_SHORT).show();
				}
				else
				{
					if(!mp.isAcknowledgement())
					{
						Intent i = new Intent(context, CustomDialogActivity.class);
						i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
						i.putExtra("commanderNumber",smsSenderNum.toString());
						i.putExtra("msg",msg );		
						CustomDialog flashScreen = sd.getFlashScreen();
						context.startActivity(i);
						if(flashScreen != null) {
							flashScreen.getMsgToBeAck().add(msg);
							flashScreen.setCommanderNumber(smsSenderNum.toString());
							flashScreen.updateTextView();
						}
					}
					else 
					{					
						HashMap<String, String> message_hash = new HashMap<String, String>();
				    	message_hash = IASUtilities.getMessageText(msg);
						DBAdapter db = new DBAdapter(context);
						db.open();
				    	db.updateAcknowledgementStatus(smsSenderNum, message_hash.get("MSG_TOKEN"));
				    	db.close();
						Log.v("--Gerard--","SmsReceiver->MyCustomAdapter->getView()");
					}
				}
			}
			
			Log.v("--Gerard--","SmsReceiver->onReceive() JSON"+IASHelper.listToJSON(globalEvents.getBufferEvents()));
		}
	}

}