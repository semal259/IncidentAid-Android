package com.cmusv.ias.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;

import com.cmusv.ias.ui.CustomDialog;

public class CustomDialogActivity extends Activity {
	/** Called when the activity is first created. */
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		/** Display Custom Dialog */
		Bundle extras = getIntent().getExtras(); 
		String commanderNumber = extras.getString("commanderNumber");
		String msg = extras.getString("msg");
		
		CustomDialog customizeDialog = new CustomDialog(this,commanderNumber, msg);
		customizeDialog.show();	
		
	}
	
}