package com.cmusv.ias;

import java.util.HashMap;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MaydayActivity extends Activity {

	String sender_no;
	Bitmap img;
	BroadcastReceiver sent_reciever;
	BroadcastReceiver delivered_reciever;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mayday);
		
		Intent intent = getIntent();
		TextView incident_details_label = (TextView) findViewById(R.id.lblMaydayUser);
		incident_details_label.setText(intent.getStringExtra("Sender"));
		sender_no = intent.getStringExtra("SenderNo");

		ImageView image = (ImageView) findViewById(R.id.mayday);
		
		try {
		final BitmapFactory.Options options = new BitmapFactory.Options();	    
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(getResources(), R.drawable.mayday, options);
        options.inJustDecodeBounds = false;
        img = BitmapFactory.decodeResource(getResources(), R.drawable.mayday, options);
        image.setImageBitmap(img);
		}
		catch (Exception e)
		{
			Log.d("OutOfMemory", "decodeResource() Out of memory exception Mayday");
		}
	}
	
	public void onOkClick(View view) {
		MessagingUtility mu = new MessagingUtility(this);
		
		SessionManager session = new SessionManager(this);
		HashMap<String, String> user = new HashMap<String, String>();
		user = session.getUserDetails();	
		String message = mu.prepareMessage("Mayday Accepted");
 	    try {
 		    IASEvent incident_event = IASUtilities.parseToEvent(message, user.get(SessionManager.KEY_NAME), 2, 1);
 		    IASHelper.createEvent(this, incident_event);
 	    }
 	    catch (Exception e){ Log.d("Create Event Exception", "Create Event Exception"); }
		
		mu.sendSMS(sender_no, mu.prepareMessage("Mayday Accepted"), sent_reciever, delivered_reciever);
		img.recycle();
		Intent intent = new Intent(this, CDashboardActivity.class);
		intent.putExtra("tab_position", "2");
		startActivity(intent);
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
}
