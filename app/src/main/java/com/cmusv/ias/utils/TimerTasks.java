package com.cmusv.ias.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.cmusv.ias.data.Directory;
import com.cmusv.ias.data.IASHelper;
import com.cmusv.ias.data.IASUser;
import com.cmusv.ias.data.IASUtilities;
import com.cmusv.ias.IncidentSessionManager;
import com.cmusv.ias.ui.DBAdapter;

public class TimerTasks extends Timer {

	private Timer autoUpdate;
	private Activity activity;
		
	public TimerTasks(Activity activity)
	{
		this.activity = activity;
	}
	
	public void StartPar(final BroadcastReceiver sent_reciever, final BroadcastReceiver delivered_reciever){
		try {
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
			String sSec = prefs.getString("PARInterval", "6000");
			Integer sec = Integer.parseInt(sSec);
			sec = sec * 60 * 1000;
			
			autoUpdate = new Timer();
		    autoUpdate.schedule(new TimerTask() {
			    @Override
			    public void run() {
			    	activity.runOnUiThread(new Runnable() {
					    public void run() {
					    	sendPAR(sent_reciever, delivered_reciever);
					    }
				    });
			    }
		    }, sec, sec);
		    
		}
		catch (Exception e)
		{
			
		}
	}
	
	public void sendPAR(BroadcastReceiver sent_reciever, BroadcastReceiver delivered_reciever)
	{
		MessagingUtility msgUtility = new MessagingUtility(activity);
        String parMessage = msgUtility.prepareMessage("PAR");
        DBAdapter db = new DBAdapter(activity);
        IncidentSessionManager inc_session = new IncidentSessionManager(activity);
    	HashMap<String, String> incident = inc_session.getIncidentDetails();
    	List<IASUser> responderList = new ArrayList<IASUser>();
    	SessionManager session = new SessionManager(activity);
    	HashMap<String, String> user = session.getUserDetails();
    	String userName = user.get(SessionManager.KEY_NAME);
    	
    	try 
    	{
            responderList = IASHelper.getUsersByIncidentName(activity, incident.get(IncidentSessionManager.KEY_INC_NAME));
        	if(responderList != null)
        	{
                List<Directory> user_contact_list = IASUtilities.getUserContactsForIncident(responderList, userName);
            	for(Directory userNo : user_contact_list)
            		msgUtility.sendSMS(userNo.getContact(), parMessage, sent_reciever, delivered_reciever);
            	 db.open();
        		 db.insertMessageLog(parMessage, incident.get(IncidentSessionManager.KEY_INC_NAME), userName, user_contact_list, IASUtilities.getCurrentDateTime());
        		 db.close();
        	}
    	}
    	catch(Exception e)
    	{
    		
    	}
	}
}
