package com.cmusv.ias;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

public class IASData {

	public static List<String> getUserNamesByEngineName(Context context, String engine_name) {
		List<String> user_names_by_engine = new ArrayList<String>();
		user_names_by_engine.add("Jack");
		user_names_by_engine.add("Jill");
		user_names_by_engine.add("James");
		user_names_by_engine.add("Jones");
		user_names_by_engine.add("Jane");
		user_names_by_engine.add("Jose");
		return user_names_by_engine;
	}
	
	public static List<String> getAllEnginesNames(Context context) {
		List<String> engine_names = new ArrayList<String>();
		engine_names.add("F1");
		engine_names.add("F2");
		engine_names.add("F3");
		engine_names.add("F4");
		engine_names.add("F5");
		engine_names.add("F6");
		return engine_names;
	}
	
	public static IASIncident getCurrentIncidentByUserName(Context context, String user_name) {
		IASIncident oIASIncident = new IASIncident();
		oIASIncident.setIncident_name("Fire");
		oIASIncident.setCommander("Jack");
		oIASIncident.setCity("San Jose");
		oIASIncident.setState("CA");
		oIASIncident.setStreet("190 Ryland St");
		oIASIncident.setZip("95110");
		oIASIncident.setStart_time(IASUtilities.getCurrentDateTime());
		oIASIncident.setFirefighters(getUserNamesByEngineName(context, ""));
		return oIASIncident;
	}
	
	public static boolean createIncident(Context context, IASIncident incident) {
		return true;
	}
	
	public static boolean authenticateUser(Context context, String user_name,
			String password, String contact, String engine_name) {

		return true;
	}
	
}
