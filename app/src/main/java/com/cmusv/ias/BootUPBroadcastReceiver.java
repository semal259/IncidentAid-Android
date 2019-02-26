package com.cmusv.ias;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.cmusv.ias.MyService;

public class BootUPBroadcastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		// start the service ...
		Log.v("sms", "started !");
		Intent startServiceIntent = new Intent(context, MyService.class);
		context.startService(startServiceIntent);
	}

}
