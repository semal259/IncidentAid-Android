package com.cmusv.ias;

import java.util.HashMap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.cmusv.ias.ui.activity.ProfileActivity;

public class IncidentSessionManager {

	SharedPreferences pref;
    Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;
    
     
    private static final String PREF_NAME = "IASIncidentSessionPref";
    private static final String HAS_OPEN_INCIDENT = "HasOpenIncident";
    public static final String KEY_INC_NAME = "incident_name";
    public static final String KEY_INC_COMMANDER = "incident_commander";
    public static final String KEY_INC_ADDRESS = "incident_address";
    public static final String KEY_ALERT_TS = "alert_ts";
     
    // Constructor
    @SuppressLint("CommitPrefEdits") public IncidentSessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }
    
    public void createIncidentSession(String incident_name, String commander_name, String incident_address){  	
    	editor.putBoolean(HAS_OPEN_INCIDENT, true);
        editor.putString(KEY_INC_NAME, incident_name);
        editor.putString(KEY_INC_COMMANDER, commander_name);
        editor.putString(KEY_INC_ADDRESS, incident_address);
        editor.putString(KEY_ALERT_TS, "");
        editor.commit();
        
    }
    
    public HashMap<String, String> getIncidentDetails(){
        HashMap<String, String> incident = new HashMap<String, String>();
        incident.put(KEY_INC_NAME, pref.getString(KEY_INC_NAME, null));
        incident.put(KEY_INC_COMMANDER, pref.getString(KEY_INC_COMMANDER, null));
        incident.put(KEY_INC_ADDRESS, pref.getString(KEY_INC_ADDRESS, null));
        incident.put(KEY_ALERT_TS, pref.getString(KEY_ALERT_TS, null));
        return incident;
    }
    
    public void setAlertTS(String timestamp)
    {
    	editor.putString(KEY_ALERT_TS, timestamp);
    	editor.commit();
    }
    
    public void closeIncident(){
        editor.clear();
        editor.commit();
        Intent i = new Intent(_context, ProfileActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        _context.startActivity(i);
    }
     
    public boolean hasOpenIncident(){
        return pref.getBoolean(HAS_OPEN_INCIDENT, false);
    }
}
