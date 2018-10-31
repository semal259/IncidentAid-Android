package com.cmusv.ias;

import java.util.Locale;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.net.ConnectivityManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;


public class CDashboardActivity extends Activity implements
		ActionBar.TabListener {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a {@link FragmentPagerAdapter}
	 * derivative, which will keep every loaded fragment in memory. If this
	 * becomes too memory intensive, it may be best to switch to a
	 * {@link android.support.v13.app.FragmentStatePagerAdapter}.
	 */
	SectionsStatePagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;
	
	String tab_position;
	TimerTasks timers;
	BroadcastReceiver sent_reciever;
	BroadcastReceiver delivered_reciever;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.activity_cdashboard);
		// Set up the action bar.
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the activity.
		mSectionsPagerAdapter = new SectionsStatePagerAdapter(getFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
	
		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			actionBar.addTab(actionBar.newTab()
					.setText(mSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}
		
		Intent intent = getIntent();
		tab_position = intent.getStringExtra("tab_position");
		if(tab_position != null)
			actionBar.setSelectedNavigationItem(Integer.parseInt(tab_position));
		timers = new TimerTasks(this);
		timers.StartPar(sent_reciever, delivered_reciever);
	}
	
	public void onResume() {
		try{
			IntentFilter ifBattery = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
			IntentFilter ifConnectivity = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
			this.registerReceiver(this.batteryReceiver, ifBattery);
			this.registerReceiver(this.connectivityReceiver, ifConnectivity);
		}
		catch (Exception e) {
			Log.d("Leacked Intent Reciever Exception", e.getClass().getName());
    	}
		super.onResume();
	}
	
	public void onPause()
	  {
	    unregisterReceiver(batteryReceiver);
	    unregisterReceiver(connectivityReceiver);
	    super.onPause();
	  }
	
	private BroadcastReceiver batteryReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			try{
				int batteryPercent = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
				Log.d("BATTERY", "batteryPercent = " + batteryPercent);
				if (getFragmentManager().findFragmentById(R.id.fgmtStatus) != null){
					StatusFragment statusFragment = (StatusFragment) getFragmentManager().findFragmentById(R.id.fgmtStatus);
					statusFragment.updateBatteryStatus(batteryPercent);
				}
			}
			catch (Exception e) {
				Log.d("Broadcast Battery Reciever Exception", e.getClass().getName());
				if (e.getLocalizedMessage() != null)
					Log.d("Broadcast Battery Reciever Exception", e.getLocalizedMessage());
        	}
		}
	};
	
	private BroadcastReceiver connectivityReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			try{
				ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
				boolean isConnected = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected();
				if (getFragmentManager().findFragmentById(R.id.fgmtStatus) != null){
				StatusFragment statusFragment = (StatusFragment) getFragmentManager().findFragmentById(R.id.fgmtStatus);
				statusFragment.updateConnectivityStatus(isConnected);
				}
			}
			catch (Exception e) {
				Log.d("Broadcast Connectivity Reciever Exception", e.getClass().getName());
				if (e.getLocalizedMessage() != null)
					Log.d("Broadcast Connectivity Reciever Exception", e.getLocalizedMessage());
        	}
			
		}
	};
	
	@Override
	protected void onStop()
	{
	    super.onStop();
	}
	
	@Override
	public void onDestroy()
	{
		timers.cancel();
	    super.onDestroy();
	}
		
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.cdashboard, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.		
		Intent i = new Intent(this, SettingsActivity.class);
		startActivity(i);
		return true;
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition(), true);
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
		FragmentTransaction fragmentTransaction) {
		//FragmentManager fragmentManager = getFragmentManager();
		//fragmentTransaction.remove(fragmentManager.findFragmentById(tab.getPosition())); 
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsStatePagerAdapter extends FragmentStatePagerAdapter {

		public SectionsStatePagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {

			try{
				switch (position) {
		        case 0:
		             // CommDashboard activity
		            return new CommDashboardFragment();
		        case 1:
		            // Alerts activity
		        	return new CommAlertFragment();
		        case 2:
		            // Teams activity
		            return new CommTeamFragment();
		        case 3:
		            // COP activity
		        	return new CommCOPFragment();
		        }
			}
			catch (Exception e) {
				Log.d("CDashboardActivity Exception", e.getClass().getName());
        	}
	        return null;
		}

		@Override
		public int getCount() {
			return 4;
		}
		
		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.title_comm_dashboard).toUpperCase(l);
			case 1:
				return getString(R.string.title_comm_alerts).toUpperCase(l);
			case 2:
				return getString(R.string.title_comm_teams).toUpperCase(l);
			case 3:
				return getString(R.string.title_comm_cop).toUpperCase(l);
			}
			return null;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK) {
	      return false;
	    }
	    return super.onKeyDown(keyCode, event);
	}
	
}
