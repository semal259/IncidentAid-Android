package com.cmusv.ias;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBAdapter {

	int id = 0;
	public static final String KEY_ROWID = "_id";
	public static final String KEY_NAME = "Name";
	public static final String KEY_NUMBER = "Number";
	public static final String KEY_ISACTIVE = "isActive";

	public static final String KEY_MESSAGE = "Message";
	public static final String KEY_MESSAGESTAMP = "MessageStamp";
	public static final String KEY_NUMBER_ARRAY = "NumberArray";

	public static final String KEY_MESSAGE_TS = "MessageTS";
	public static final String KEY_DELIVERED = "Delivered";
	public static final String KEY_ACKNOWLEDGED = "Acknowledged";
	public static final String KEY_ACKNOWLEDGED_TS = "AcknowledgedTS";
	public static final String KEY_SENDER = "Sender";
	public static final String KEY_RECIEVER = "Reciever";
	public static final String KEY_INCIDENT_NAME = "IncidentName";
	public static final String KEY_ISCURRENT_ALERT = "CurrentAlert";
	public static final String KEY_RECIEVER_NO = "RecieverNo";
	public static final String KEY_MSG_TOKEN = "Token";
	
	private static final String TAG = "DBAdapter";

	private static final String DATABASE_NAME = "IASDB";
	private static final String PERSONNEL_TABLE = "tblPersonnel";
	private static final String MESSAGE_TABLE = "tblMessage";
	private static final String MESSAGE_LOG_TABLE = "tblMessageLog";
	private static final int DATABASE_VERSION = 1;

	// 4 columns to keep personnel data
	// Column 0: Auto increment integer _id
	// Column 1 and 2: Personnel name and number respectively
	// Column 3: An integer field call isActive, which is 1 if the personnel is
	// on the scene of emergency otherwise 0
	private static final String PERSONNEL_TABLE_CREATE = "create table tblPersonnel (_id integer primary key autoincrement, "
			+ "Name text not null, "
			+ "Number text not null, "
			+ "isActive integer default '0');";

	private static final String MESSAGE_TABLE_CREATE = "create table tblMessage (_id integer primary key autoincrement, "
			+ "Message text not null, "
			+ "MessageStamp text not null,"
			+ "NumberArray text);";
	
	private static final String MESSAGE_LOG_TABLE_CREATE = "create table tblMessageLog (_id integer primary key autoincrement, "
			+ "Message text not null, "
			+ "MessageTS text not null,"
			+ "Delivered integer,"
			+ "Acknowledged integer,"
			+ "AcknowledgedTS text,"
			+ "Sender text,"
			+ "Reciever text,"
			+ "RecieverNo text,"
			+ "CurrentAlert integer,"
			+ "Token string,"
			+ "IncidentName text);";
	

	private static final String SETTING_TABLE_CREAT = "create table tblSetting (_id integer primary key autoincrement, "
			+ "Key text not null, " + "Value text not null);";

	private final Context context;

	private DatabaseHelper DBHelper;
	private SQLiteDatabase db;

	public DBAdapter(Context ctx) {
		this.context = ctx;
		DBHelper = new DatabaseHelper(context);

	}

	private static class DatabaseHelper extends SQLiteOpenHelper {
		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(PERSONNEL_TABLE_CREATE);
			db.execSQL(MESSAGE_TABLE_CREATE);
			db.execSQL(SETTING_TABLE_CREAT);
			db.execSQL(MESSAGE_LOG_TABLE_CREATE);

		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
					+ newVersion + ", which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS tblPersonnel");
			db.execSQL("DROP TABLE IF EXISTS tblMessage");
			db.execSQL("DROP TABLE IF EXISTS tblSetting");
			db.execSQL("DROP TABLE IF EXISTS tblMessageLog");
			onCreate(db);
		}
	}

	// ---opens the database---
	public DBAdapter open() throws SQLException {
		db = DBHelper.getWritableDatabase();
		return this;
	}

	// ---closes the database---
	public void close() {
		DBHelper.close();
	}

	public void destroyDB() {
		db.execSQL("DELETE FROM tblPersonnel");
		db.execSQL("DELETE FROM tblMessage");
		db.execSQL("DELETE FROM tblSetting");
		db.execSQL("DELETE FROM tblMessageLog");
	}
	
	// METHODS RELATED WITH THE PERSONNEL TABLE

	// ---insert a personnel into the personnel table---
	public long insertPersonnel(String Name, String Number) {
		Log.v("--Gerard--", "DBAdapter->insertPersonnel()");
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_NAME, Name);
		initialValues.put(KEY_NUMBER, Number);
		return db.insert(PERSONNEL_TABLE, null, initialValues);
	}

	// ---get names for all personnel in the database---
	public String[] getAllNames() {
		Log.v("--Gerard--", "DBAdapter->getAllNames()");

		ArrayList<String> arraylist = new ArrayList<String>();
		Cursor cursor = db.rawQuery("SELECT Name FROM tblPersonnel", null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			arraylist.add(cursor.getString(0));
			cursor.moveToNext();
		}
		String[] array = new String[arraylist.size()];
		arraylist.toArray(array);
		return array;

	}

	// ---recreates tables/ doesn't populate them----
	public void resetTables() {
		Log.v("--Gerard--", "DBAdapter->resetTables()");
		DBHelper.onUpgrade(db, 1, 1);
	}

	// ---sets isActive attribute of the personnel with given name to 1---
	public void activatePersonnel(String name) {
		Log.v("--Gerard--", "DBAdapter->activatePersonnel()");

		db.execSQL("UPDATE tblPersonnel SET isActive='1' WHERE Name='" + name
				+ "'");
	}

	// ---sets isActive attribute of the personnel with given name to 0---
	public void deactivatePersonnel(String name) {

		Log.v("--Gerard--", "DBAdapter->deactivatePersonnel()");

		db.execSQL("UPDATE tblPersonnel SET isActive='0' WHERE Name='" + name
				+ "'");
	}

	// gets numbers of all personnel with isActive=1
	public String[] getAllActiveNumbers() {
		Log.v("--Gerard--", "DBAdapter->getAllActiveNumbers()");
		ArrayList<String> arraylist = new ArrayList<String>();
		Cursor cursor = db.rawQuery(
				"SELECT Number FROM tblPersonnel WHERE isActive='1'", null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			arraylist.add(cursor.getString(0));
			cursor.moveToNext();
		}
		String[] array = new String[arraylist.size()];
		arraylist.toArray(array);
		return array;

	}

	// checks if a personnel isActive or not isActive=1 means active isActive=0
	// means not active
	public int isActive(String name) {
		Log.v("--Gerard--", "DBAdapter->isActive()");

		Cursor cursor = db.rawQuery(
				"SELECT isActive FROM tblPersonnel WHERE Name='" + name + "'",
				null);
		cursor.moveToFirst();
		int temp = cursor.getInt(0);
		return temp;
	}

	// METHODS RELATED WITH THE MESSAGE TABLE

	// ---insert a message into the message table---
	public long insertMessage(String Message, String MessageStamp,
			String NumberArray) {
		Log.v("--Gerard--", "DBAdapter->insertMessage()");

		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_MESSAGE, Message);
		initialValues.put(KEY_MESSAGESTAMP, MessageStamp);
		initialValues.put(KEY_NUMBER_ARRAY, NumberArray);
		return db.insert(MESSAGE_TABLE, null, initialValues);
	}

	// ---get all messages in the database---
	public String[] getAllMessages() {
		Log.v("--Gerard--", "DBAdapter->getAllMessages()");

		ArrayList<String> arraylist = new ArrayList<String>();
		Log.v("--- GA Deb--", "In AllMEssages");
		Cursor cursor = db.rawQuery(
				"SELECT Message, MessageStamp,NumberArray FROM tblMessage",
				null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			String acks = cursor.getString(2);
			String[] acksArr = acks.split(";");
			String temp = cursor.getString(0) + "-" + cursor.getString(1) + "-"
					+ acksArr.length;
			arraylist.add(temp);
			Log.v("--Gerard--", "DBAdapter->getAllMessages()" + "->" + temp);
			cursor.moveToNext();
		}
		String[] array = new String[arraylist.size()];
		arraylist.toArray(array);
		return array;

	}

	// ---get all Missing Acks in the database---
	public List<String> getAllMissingAcks(String messagestamp) {
		Log.v("--Gerard--", "DBAdapter->getAllMissingAcks()");

		Cursor cursor = db.rawQuery(
				"SELECT NumberArray FROM tblMessage WHERE MessageStamp='"
						+ messagestamp + "'", null);
		cursor.moveToFirst();
		String numberString = cursor.getString(0);
		Log.v("---GA NEW---", "--- before : " + numberString);
		String[] numArr = numberString.split(";");
		List<String> nameArr = new ArrayList<String>();
		for (int i = 0; i < numArr.length; i++) {
			cursor = db.rawQuery("SELECT Name FROM tblPersonnel WHERE Number='"
					+ numArr[i].toString() + "'", null);
			if (cursor.moveToFirst())
				nameArr.add(cursor.getString(0));
		}
		return nameArr;

	}

	/*public void updateNumberArray(String messageStamp, String ackContactNumber) {
		Log.v("--Gerard--", "DBAdapter->updateNumberArray()");

		Cursor cursor = db.rawQuery(
				"SELECT NumberArray FROM tblMessage WHERE MessageStamp='"
						+ messageStamp + "'", null);
		cursor.moveToFirst();
		String numberString = cursor.getString(0);
		Log.v("---GA2---", "--- before : " + numberString);
		String[] numArr = numberString.split(";");

		ArrayList<String> numList = new ArrayList<String>(Arrays.asList(numArr));

		numList.remove(ackContactNumber);
		Log.v("---GA2---", "Size : " + numList.size() + " ackContactNumber :"
				+ ackContactNumber);

		String[] array = new String[numList.size()];
		numList.toArray(array);
		numberString = MessageTab.arrayToString2(array, ";");
		if (numberString.trim().length() > 0)
			db.execSQL("UPDATE tblMessage SET NumberArray ='" + numberString
					+ "' WHERE MessageStamp='" + messageStamp + "'");
		else
			db.execSQL("DELETE FROM tblMessage  WHERE MessageStamp='"
					+ messageStamp + "'");

		// debugging
		Cursor cursor2 = db.rawQuery(
				"SELECT NumberArray FROM tblMessage WHERE MessageStamp='"
						+ messageStamp + "'", null);
		if (cursor2.moveToFirst())
			numberString = cursor2.getString(0);
		Log.v("---GA2---", "--- From DB : " + numberString);

	}*/
	
	// ---insert a message log into the message table---
	public void insertMessageLog(String message, String incident_name, String sender, List<Directory> reciever, String timestamp) 
	{
		try {
			db.execSQL("UPDATE tblMessageLog SET CurrentAlert ='0' WHERE IncidentName='" + incident_name + "'");
			
			HashMap<String, String> message_hash = new HashMap<String, String>();
			message_hash = IASUtilities.getMessageText(message);
			
			ContentValues initialValues = new ContentValues();
			initialValues.put(KEY_MESSAGE, message_hash.get("MSG_TEXT"));
			initialValues.put(KEY_MESSAGE_TS, timestamp);
			initialValues.put(KEY_DELIVERED, "0");
			initialValues.put(KEY_ACKNOWLEDGED, "0");
			initialValues.put(KEY_ACKNOWLEDGED_TS, "");
			initialValues.put(KEY_SENDER, sender);
			initialValues.put(KEY_INCIDENT_NAME, incident_name);
			initialValues.put(KEY_ISCURRENT_ALERT, "1");
			initialValues.put(KEY_MSG_TOKEN, message_hash.get("MSG_TOKEN"));
			
			for(Directory responder: reciever) {
				initialValues.put(KEY_RECIEVER, responder.getName());
				initialValues.put(KEY_RECIEVER_NO, responder.getContact());
				long a = db.insert(MESSAGE_LOG_TABLE, null, initialValues);
				Log.d("DB ID", String.valueOf(a));
			}
					
		}
		catch (Exception e)
		{
			Log.d("SQLite Error", e.getClass().getName());
		}
	}
	
	
	public HashMap<String, List<String>> getLatestAlertStatus(String timestamp, String incident_name)
	{
		HashMap<String, List<String>> user_alert_status = new HashMap<String, List<String>>();
		List<String> alert_details;
		try {
			Cursor cursor = db.rawQuery("SELECT Reciever, Message, Delivered, Acknowledged FROM tblMessageLog WHERE CurrentAlert='1'", null);
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				alert_details = new ArrayList<String>();
				alert_details.add(cursor.getString(1));
				alert_details.add(cursor.getString(2));
				alert_details.add(cursor.getString(3));
				user_alert_status.put(cursor.getString(0), alert_details);
				cursor.moveToNext();
			}	
		}
		catch(Exception e)
		{
			Log.d("SQLite Error", e.getClass().getName());
		}
		return user_alert_status;
	}
	
	public boolean updateDeliveryStatus(String contant, String token)
	{
		try {		
			db.execSQL("UPDATE tblMessageLog SET Delivered = '1' WHERE CurrentAlert ='1' AND RecieverNo = '" + contant + "' AND Token = '" + token + "'");
			Log.d("Reciever", contant);
			Log.d("Delivered", token);
			return true;
		}
		catch(Exception e) { Log.d("SQLite Error", e.getClass().getName()); }
		return false;
	}
	
	public boolean updateAcknowledgementStatus(String contant, String token)
	{
		try {
			db.execSQL("UPDATE tblMessageLog SET Acknowledged = '1' WHERE CurrentAlert ='1' AND RecieverNo = '" + contant + "' AND Token = '" + token + "'");
			Log.d("Reciever", contant);
			Log.d("Delivered", token);
			return true;
		}
		catch(Exception e) { Log.d("SQLite Error", e.getClass().getName()); }
		return false;
	}
	
	public boolean updateMayday(String contant)
	{
		try {
			db.execSQL("UPDATE tblMessageLog SET Message = 'Mayday', Acknowledged = '0', Delivered = '0' WHERE CurrentAlert ='1' AND RecieverNo = '" + contant + "'");
			return true;
		}
		catch(Exception e) { Log.d("SQLite Error", e.getClass().getName()); }
		return false;
	}
	
	public List<String> getAlertsByUser(String username, String incident_name)
	{
		List<String> alert_details = new ArrayList<String>();
		try {
			Cursor cursor = db.rawQuery("SELECT Message, MessageTS, Acknowledged FROM tblMessageLog WHERE Reciever='" + username + "' AND IncidentName='" + incident_name + "' ORDER BY _id DESC" , null);
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				String temp_message = cursor.getString(0) + " -- ";
				if(cursor.getString(2).equals("1"))
					temp_message = temp_message + "OK -- ";
				temp_message = temp_message + cursor.getString(1);
				alert_details.add(temp_message);
				cursor.moveToNext();
			}	
		}
		catch(Exception e)
		{
			Log.d("SQLite Error", e.getClass().getName());
		}
		return alert_details;
	}
	
}
