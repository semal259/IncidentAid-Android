package com.cmusv.ias;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

public class LoginActivity extends Activity {
	DBAdapter db = new DBAdapter(this);

	private Button btnLogin;
	private EditText txtUsername;
	private EditText txtPassword;
	private RadioButton radBtnFirefighter;
	private RadioButton radBtnCommander;
	private Spinner spnrEngine;
	private SharedPreferences prefs;
	ConnectivityManager connMgr;
	NetworkInfo networkInfo;
	AsyncHTTPRequest req;
	Context context = this;
	SessionManager session;
	String contact = null;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_login);
		btnLogin = (Button) findViewById(R.id.btnLogin);
		btnLogin.setBackgroundColor(Color.rgb(52, 161, 201));
		
		txtUsername = (EditText) findViewById(R.id.txtUsername);
		txtPassword = (EditText) findViewById(R.id.txtPassword);
		radBtnFirefighter = (RadioButton)findViewById(R.id.radBtnFirefighter);
		radBtnCommander = (RadioButton)findViewById(R.id.radBtnCommander);
		
		// Engine list
		spnrEngine = (Spinner) findViewById(R.id.engine_spinner);

		List<String> engineList = new ArrayList<String>();
		
		try {
			engineList = IASHelper.getAllEnginesNames(getApplicationContext());
			
			if (engineList != null)
			{
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, engineList);
				adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				spnrEngine.setAdapter(adapter);
			}
			else
				Toast.makeText(LoginActivity.this, "Unable to load engine list. Check network connectivity.", Toast.LENGTH_LONG).show();
		}
		catch(Exception e)
		{
			Toast.makeText(LoginActivity.this, "Unable to load engine list. Check network connectivity.", Toast.LENGTH_LONG).show();
		}
		
		connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		networkInfo = connMgr.getActiveNetworkInfo();
		req = new AsyncHTTPRequest();
		
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		
		btnLogin.setOnClickListener(new View.OnClickListener() 
        {
			
			public void onClick(View v) {
				
				String role;
				// CHECK IF ID IS SET
				if (txtUsername.getText().toString().matches("")) {
            		Toast.makeText(LoginActivity.this, "MUST ENTER ID!", Toast.LENGTH_LONG).show();
            		return;
            	}
				
				// CHECK IF ROLE IS SET
				if(radBtnFirefighter.isChecked())
					role = radBtnFirefighter.getText().toString();
				else if (radBtnCommander.isChecked())
					role = radBtnCommander.getText().toString();
				else 
				{
					Toast.makeText(LoginActivity.this, "MUST CHOOSE 'Firefighter' OR 'Commander'!", Toast.LENGTH_LONG).show();
					return;
				}
				
				// AUTHENTICATE USERNAME AND PASSWORD WHILE SETTING ENGINE AND CONTACT
				String username = txtUsername.getText().toString();
				String password = txtPassword.getText().toString();
				String engine = (String) spnrEngine.getSelectedItem();
				String contact = prefs.getString("MobileNumber", "");
				Log.d("Login", "authenticating: " + username + "/" + password);
				session = new SessionManager(context);
		        
				if(IASHelper.authenticateUser(context, username, password, contact, engine, role))
				{
					session.createLoginSession(username, "role", engine);
					Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
					startActivity(intent);
				}
				else
					Toast.makeText(LoginActivity.this, "COULD NOT LOGIN. CHECK USERNAME, PASSWORD, AND NETWORK CONNECTION.", Toast.LENGTH_LONG).show();

			}
        });    
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK) {
	      return false;
	    }
	    return super.onKeyDown(keyCode, event);
	}
}
