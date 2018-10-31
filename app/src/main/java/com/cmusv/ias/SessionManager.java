package com.cmusv.ias;

import java.util.HashMap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
 
public class SessionManager {
    // Shared Preferences
    SharedPreferences pref;
     
    // Editor for Shared preferences
    Editor editor;
     
    // Context
    Context _context;
     
    // Shared pref mode
    int PRIVATE_MODE = 0;
     
    // Sharedpref file name
    private static final String PREF_NAME = "IASLoginPref";
     
    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";
     
    // User name (make variable public to access from outside)
    public static final String KEY_NAME = "username";
     
    // Incident Role address (make variable public to access from outside)
    public static final String KEY_ROLE = "incident_role";
    
 // Incident Engine Number address (make variable public to access from outside)
    public static final String KEY_ENGINE = "engine_number";
     
    // Constructor
    @SuppressLint("CommitPrefEdits") public SessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }
    
    public void createLoginSession(String name, String role, String engine_number){
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);
         
        // Storing name in pref
        editor.putString(KEY_NAME, name);
         
        // Storing email in pref
        editor.putString(KEY_ROLE, role);
        
     // Storing email in pref
        editor.putString(KEY_ENGINE, engine_number);
         
        // commit changes
        editor.commit();
    }
    
    public void setUsername(String name)
    {
    	editor.putString(KEY_NAME, name);
    	editor.commit();
    }
    
    public void setRole(String role)
    {
    	editor.putString(KEY_ROLE, role);
    	editor.commit();
    }
    
    public void setEngine(String engine)
    {
    	editor.putString(KEY_ENGINE, engine);
    	editor.commit();
    }
    
    
    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        // user name
        user.put(KEY_NAME, pref.getString(KEY_NAME, null));
         
        // user role
        user.put(KEY_ROLE, pref.getString(KEY_ROLE, null));
         
        // user engine number
        user.put(KEY_ENGINE, pref.getString(KEY_ENGINE, null));
        
        // return user
        return user;
    }
    
    public void checkLogin(){
        // Check login status
    	Intent i;
        if(!this.isLoggedIn()){
            // user is not logged in redirect him to Login Activity
            i = new Intent(_context, LoginActivity.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
             
            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        
            _context.startActivity(i);
        }
    }
    
    public void logoutUser(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();
         
        // After logout redirect user to Loing Activity
        Intent i = new Intent(_context, LoginActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
         
        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
         
        // Staring Login Activity
        _context.startActivity(i);
    }
     
    // Get Login State
    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }
}