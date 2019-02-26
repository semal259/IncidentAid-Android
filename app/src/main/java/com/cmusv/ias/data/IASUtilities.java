package com.cmusv.ias.data;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

public class IASUtilities {
	
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd/yy HH:mm:ss", java.util.Locale.getDefault());
	private static LatLng coords = new LatLng(37.429212, -122.138121);
	
	public static LatLng getGeoCoords(String address, Activity oActivity)
	{
		Geocoder coder = new Geocoder(oActivity, Locale.ENGLISH);
    	try 
    	{
    		List<Address> lstaddress = coder.getFromLocationName(address, 2);
    		if (address != null && !address.isEmpty()) 
    			coords = new LatLng(lstaddress.get(0).getLatitude(), lstaddress.get(0).getLongitude());
    	} 
    	catch (IOException e) 
    	{
    		e.printStackTrace();	 	
    	}
		return coords;
	}
	
	public static String getResolvedAddress(String address, Activity oActivity, IASIncident oIASIncident)
	{	
		Geocoder coder = new Geocoder(oActivity, Locale.ENGLISH);
    	try 
    	{
    		List<Address> lstaddress = new ArrayList<Address>();
    		lstaddress = coder.getFromLocationName(address, 2);
    		if (address != null && !address.isEmpty()) 
    		{
    			oIASIncident.setStreet(lstaddress.get(0).getAddressLine(0));
    			oIASIncident.setCity(lstaddress.get(0).getLocality());
    			oIASIncident.setState(lstaddress.get(0).getAdminArea());
    			oIASIncident.setZip(lstaddress.get(0).getPostalCode());
    			address = oIASIncident.getStreet() + ", " + oIASIncident.getCity() + ", " + oIASIncident.getState() + ", " + oIASIncident.getZip();
    		}
    	} 
    	catch (IOException e) 
    	{
    		e.printStackTrace();	 	
    	}
    	return address;
	}
	
	public static String getCurrentDateTime() {
	    return DATE_FORMAT.format(new Date());
	}
	
	public static boolean isValidContact(String contact) {
		Log.d("isValidContact", "contact.length = " + contact.length());
		if (contact.length() < 6) return false;
		Log.d("isValidContact", "contact = " + contact);
		Log.d("isValidContact", "contact.substring(0, 4) = " + contact.substring(0, 5));
		
		return "+1".equals(contact.substring(0, 5));
	}
	
	public static HashMap<String, List<String>> getUsersForIncident(List<String> listDataHeader, List<IASUser> inc_users)
	{
		HashMap<String, List<String>> user_engine_list = new HashMap<String, List<String>>();
		HashSet<String> engNames = new HashSet<String>();
		if(inc_users != null)
    	{
			for(IASUser each_user : inc_users)
				engNames.add(each_user.getEngine_name());
			listDataHeader.addAll(engNames);
			for(String engName : engNames) 
			{
				List<String> userName = new ArrayList<String>();
				for(IASUser user : inc_users) 
					if(user.getEngine_name().equals(engName)) 
						userName.add(user.getUser_name());
				user_engine_list.put(engName, userName);
			}
    	}
		return user_engine_list;
	}
	
	public static List<Directory> getUserContactsForIncident(List<IASUser> inc_users, String userName)
	{  	
		List<Directory> user_contact_list = new ArrayList<Directory>();
		for(IASUser each_user : inc_users)
		{
			if(!each_user.getUser_name().equals(userName))
			{
				Directory dir = new Directory(each_user.getUser_name(), each_user.getContact());
				user_contact_list.add(dir);
			}
		}
		return user_contact_list;
	}
	
	public static List<String> getUserNamesFromUser(List<IASUser> inc_users)
	{  	
		List<String> user_names_list = new ArrayList<String>();
		for(IASUser each_user : inc_users)
			user_names_list.add(each_user.getUser_name());
		return user_names_list;
	}
	
	public static HashMap<String, String> getMessageText(String pre_message) 
	{
		HashMap<String, String> message = new HashMap<String, String>();
		String[] messageArray = pre_message.split("/");
		message.put("APP_TOKEN", messageArray[0]);
		message.put("MSG_TYPE", messageArray[1]);
		message.put("MSG_TOKEN", messageArray[2]);
		message.put("MSG_TEXT", messageArray[3]);
		return message;
	}
	
	public static IASEvent parseToEvent(String message, String username, int request_type, int user_role)
	{
		IASEvent event = new IASEvent();
		HashMap<String, String> hashMessage = getMessageText(message);	
		if(message.contains("Vacate"))
			event.setM(1);
		else if(message.contains("Mayday")) 
			event.setM(2);
		else if(message.contains("PAR")) 
			event.setM(3);
		else if(message.contains("RIC")) 
			event.setM(4);
		else if(message.contains("Utilities Off")) 
			event.setM(5);
		else if(message.contains("Utilities On")) 
			event.setM(6);
		else if(message.contains("Vertical Vent")) 
			event.setM(7);
		else if(message.contains("Cross Vent")) 
			event.setM(8);
		else if(message.contains("All Clear")) 
			event.setM(9);
		else if(message.contains("Life Haz")) 
			event.setM(10);
		else if(message.contains("Mayday Ack")) 
			event.setM(11);
		else 
			event.setM(0);
		event.setR(user_role);
		event.setTk(hashMessage.get("MSG_TOKEN"));
		event.setTp(request_type);
		event.setTs(getCurrentDateTime());
		event.setUser_n(username);
		return event;
	}
}
