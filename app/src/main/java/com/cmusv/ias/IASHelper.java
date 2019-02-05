/**
 * 
 */
package com.cmusv.ias;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class IASHelper {

	// -----> REMOVE block after testing and clean-up - Gerard
	//
	// private static String postRoute = "http://httpbin.org/post";
	// private static String postRoute =
	// "http://httpbin-org-p9daa3ih3res.runscope.net/event_http";
	// private static String postRoute =
	// "http://shrouded-spire-4422.herokuapp.com/event_http";
	// private static String postRoute =
	// "http://shrouded--spire--4422-herokuapp-com-p9daa3ih3res.runscope.net/event_http";
	// private static String postRoute =
	// "http://107.170.238.227:3000/event_http";
	// private static String getRouteUsers =
	// "http://107.170.238.227:3000/all_users";
	//
	//
	// <-----REMOVE block after testing and clean-up - Gerard

	private static final String LOG_TAG = "IASHelper";
	//
	// ROUTES for end points - BEGIN
	//
	//private static String routeIP = "http://107.170.238.227:3000";
    // private static String routeIP = "http://104.131.135.109:3000";
	private static String routeIP = "http://54.202.157.179:3000";

	private static String routeCreateUser = routeIP + "/create_user";
	private static String routeCreateEvent = routeIP + "/create_event";
	private static String routeCreateIncident = routeIP + "/create_incident";
	private static String routeCreateEngine = routeIP + "/create_engine";

	private static String routeGetAllUsers = routeIP + "/get_all_users";
	private static String routeGetAllEvents = routeIP + "/get_all_events";
	private static String routeGetAllIncidents = routeIP + "/get_all_incidents";
	private static String routeGetAllEngines = routeIP + "/get_all_engines";

	private static String routeGetUserByUserName = routeIP
			+ "/get_user_by_user_name";
	private static String routeGetUsersByJobRole = routeIP
			+ "/get_users_by_job_role";
	private static String routeGetUsersByEngineName = routeIP
			+ "/get_users_by_engine_name";
	private static String routeGetUsersByIncidentName = routeIP
			+ "/get_users_by_incident_name";
	private static String routeGetEventsByIncidentName = routeIP
			+ "/get_events_by_incident_name";
	private static String routeGetEventsByUserName = routeIP
			+ "/get_events_by_user_name";
	private static String routeGetIncidentByIncidentName = routeIP
			+ "/get_incident_by_incident_name";
	private static String routeGetCurrentIncidentByUserName = routeIP
			+ "/get_current_incident_by_user_name";
	private static String routeGetIncidentsByUserName = routeIP
			+ "/get_incidents_by_user_name";
	private static String routeGetEngineByEngineName = routeIP
			+ "/get_engine_by_engine_name";

	private static String routeDeleteUserByUserName = routeIP
			+ "/delete_user_by_user_name";
	private static String routeDeleteEventsByIncidentName = routeIP
			+ "/delete_events_by_incident_name";
	private static String routeDeleteIncidentByIncidentName = routeIP
			+ "/delete_incident_by_incident_name";
	private static String routeDeleteEngineByEngineName = routeIP
			+ "/delete_engine_by_engine_name";

	private static String routeUpdateUserByUserName = routeIP
			+ "/update_user_by_user_name";
	private static String routeUpdateIncidentByIncidentName = routeIP
			+ "/update_incident_by_incident_name";
	private static String routeUpdateIncidentCommander = routeIP
			+ "/update_incident_commander";
	private static String routeUpdateEngineByEngineName = routeIP
			+ "/update_engine_by_engine_name";

	private static String routeAuthenticateUser = routeIP
			+ "/authenticate_user";
	//
	// ROUTES for end points - END
	//

	private List<IASEvent> iasEvents = new ArrayList<IASEvent>(); // temporary -
																	// to be
																	// removed
																	// once tied
																	// to SQLite
	//private String localContact;
	//private String localRole;

	public IASHelper() {
		// TODO Auto-generated constructor stub
	}

	public IASHelper(String routeIP) {
		setRouteIP(routeIP);
	}

	public IASHelper(String localContact, String localRole) {
		// TODO Auto-generated constructor stub
		//this.localContact = localContact;
		//this.localRole = localRole;
	}

	public void setLocalContact(String localContact) {
		//this.localContact = localContact;
	}

	public void setLocalRole(String localRole) {
		//this.localRole = localRole;
	}

	public void addBufferEvents(String incident, String contact,
			String message, String type) { // temporary
		// -
		// Gerard
		// to be
		// removed
		// once tied
		// to SQLite

		//IASEvent event = new IASEvent(incident, localContact, localRole, type,
		//		message, 0.0, 0.0, "token");

		//this.iasEvents.add(event);
	}

	public List<IASEvent> getBufferEvents() { // temporary - to be removed once
												// tied to SQLite - Gerard
		return iasEvents;
	}

	public void clearBufferEvents() { // temporary - to be removed once tied to
										// SQLite - Gerard
		this.iasEvents.clear();
	}

	//
	// New routes here
	//

	public static String getRouteIP() {
		return routeIP;
	}

	public static void setRouteIP(String routeIP) {
		IASHelper.routeIP = routeIP;
	}

	public static String getRouteCreateUser() {
		return routeCreateUser;
	}

	public static void setRouteCreateUser(String routeCreateUser) {
		IASHelper.routeCreateUser = routeCreateUser;
	}

	public static String getRouteCreateEvent() {
		return routeCreateEvent;
	}

	public static void setRouteCreateEvent(String routeCreateEvent) {
		IASHelper.routeCreateEvent = routeCreateEvent;
	}

	public static String getRouteCreateIncident() {
		return routeCreateIncident;
	}

	public static void setRouteCreateIncident(String routeCreateIncident) {
		IASHelper.routeCreateIncident = routeCreateIncident;
	}

	public static String getRouteCreateEngine() {
		return routeCreateEngine;
	}

	public static void setRouteCreateEngine(String routeCreateEngine) {
		IASHelper.routeCreateEngine = routeCreateEngine;
	}

	public static String getRouteGetAllUsers() {
		return routeGetAllUsers;
	}

	public static void setRouteGetAllUsers(String routeGetAllUsers) {
		IASHelper.routeGetAllUsers = routeGetAllUsers;
	}

	public static String getRouteGetAllEvents() {
		return routeGetAllEvents;
	}

	public static void setRouteGetAllEvents(String routeGetAllEvents) {
		IASHelper.routeGetAllEvents = routeGetAllEvents;
	}

	public static String getRouteGetAllIncidents() {
		return routeGetAllIncidents;
	}

	public static void setRouteGetAllIncidents(String routeGetAllIncidents) {
		IASHelper.routeGetAllIncidents = routeGetAllIncidents;
	}

	public static String getRouteGetAllEngines() {
		return routeGetAllEngines;
	}

	public static void setRouteGetAllEngines(String routeGetAllEngines) {
		IASHelper.routeGetAllEngines = routeGetAllEngines;
	}

	public static String getRouteGetUserByUserName() {
		return routeGetUserByUserName;
	}

	public static void setRouteGetUserByUserName(String routeGetUserByUserName) {
		IASHelper.routeGetUserByUserName = routeGetUserByUserName;
	}

	public static String getRouteGetUsersByJobRole() {
		return routeGetUsersByJobRole;
	}

	public static void setRouteGetUsersByJobRole(String routeGetUsersByJobRole) {
		IASHelper.routeGetUsersByJobRole = routeGetUsersByJobRole;
	}

	public static String getRouteGetUsersByEngineName() {
		return routeGetUsersByEngineName;
	}

	public static void setRouteGetUsersByEngineName(
			String routeGetUsersByEngineName) {
		IASHelper.routeGetUsersByEngineName = routeGetUsersByEngineName;
	}

	public static String getRouteGetUsersByIncidentName() {
		return routeGetUsersByIncidentName;
	}

	public static void setRouteGetUsersByIncidentName(
			String routeGetUsersByIncidentName) {
		IASHelper.routeGetUsersByIncidentName = routeGetUsersByIncidentName;
	}
	
	public static String getRouteGetEventsByIncidentName() {
		return routeGetEventsByIncidentName;
	}

	public static void setRouteGetEventsByIncidentName(
			String routeGetEventsByIncidentName) {
		IASHelper.routeGetEventsByIncidentName = routeGetEventsByIncidentName;
	}

	public static String getRouteGetEventsByUserName() {
		return routeGetEventsByUserName;
	}

	public static void setRouteGetEventsByUserName(
			String routeGetEventsByUserName) {
		IASHelper.routeGetEventsByUserName = routeGetEventsByUserName;
	}

	public static String getRouteGetIncidentByIncidentName() {
		return routeGetIncidentByIncidentName;
	}

	public static void setRouteGetIncidentByIncidentName(
			String routeGetIncidentByIncidentName) {
		IASHelper.routeGetIncidentByIncidentName = routeGetIncidentByIncidentName;
	}

	public static String getRouteGetCurrentIncidentByUserName() {
		return routeGetCurrentIncidentByUserName;
	}

	public static void setRouteGetCurrentIncidentByUserName(
			String routeGetCurrentIncidentByUserName) {
		IASHelper.routeGetCurrentIncidentByUserName = routeGetCurrentIncidentByUserName;
	}

	public static String getRouteGetIncidentsByUserName() {
		return routeGetIncidentsByUserName;
	}

	public static void setRouteGetIncidentsByUserName(
			String routeGetIncidentsByUserName) {
		IASHelper.routeGetIncidentsByUserName = routeGetIncidentsByUserName;
	}

	public static String getRouteGetEngineByEngineName() {
		return routeGetEngineByEngineName;
	}

	public static void setRouteGetEngineByEngineName(
			String routeGetEngineByEngineName) {
		IASHelper.routeGetEngineByEngineName = routeGetEngineByEngineName;
	}

	public static String getRouteDeleteUserByUserName() {
		return routeDeleteUserByUserName;
	}

	public static void setRouteDeleteUserByUserName(
			String routeDeleteUserByUserName) {
		IASHelper.routeDeleteUserByUserName = routeDeleteUserByUserName;
	}

	public static String getRouteDeleteEventsByIncidentName() {
		return routeDeleteEventsByIncidentName;
	}

	public static void setRouteDeleteEventsByIncidentName(
			String routeDeleteEventsByIncidentName) {
		IASHelper.routeDeleteEventsByIncidentName = routeDeleteEventsByIncidentName;
	}

	public static String getRouteDeleteIncidentByIncidentName() {
		return routeDeleteIncidentByIncidentName;
	}

	public static void setRouteDeleteIncidentByIncidentName(
			String routeDeleteIncidentByIncidentName) {
		IASHelper.routeDeleteIncidentByIncidentName = routeDeleteIncidentByIncidentName;
	}

	public static String getRouteDeleteEngineByEngineName() {
		return routeDeleteEngineByEngineName;
	}

	public static void setRouteDeleteEngineByEngineName(
			String routeDeleteEngineByEngineName) {
		IASHelper.routeDeleteEngineByEngineName = routeDeleteEngineByEngineName;
	}

	public static String getRouteUpdateUserByUserName() {
		return routeUpdateUserByUserName;
	}

	public static void setRouteUpdateUserByUserName(
			String routeUpdateUserByUserName) {
		IASHelper.routeUpdateUserByUserName = routeUpdateUserByUserName;
	}

	public static String getRouteUpdateIncidentByIncidentName() {
		return routeUpdateIncidentByIncidentName;
	}

	public static void setRouteUpdateIncidentByIncidentName(
			String routeUpdateIncidentByIncidentName) {
		IASHelper.routeUpdateIncidentByIncidentName = routeUpdateIncidentByIncidentName;
	}

	public static String getRouteUpdateIncidentCommander() {
		return routeUpdateIncidentCommander;
	}

	public static void setRouteUpdateIncidentCommander(
			String routeUpdateIncidentCommander) {
		IASHelper.routeUpdateIncidentCommander = routeUpdateIncidentCommander;
	}

	public static String getRouteUpdateEngineByEngineName() {
		return routeUpdateEngineByEngineName;
	}

	public static void setRouteUpdateEngineByEngineName(
			String routeUpdateEngineByEngineName) {
		IASHelper.routeUpdateEngineByEngineName = routeUpdateEngineByEngineName;
	}

	public static String getRouteAuthenticateUser() {
		return routeAuthenticateUser;
	}

	public static void setRouteAuthenticateUser(String routeAuthenticateUser) {
		IASHelper.routeAuthenticateUser = routeAuthenticateUser;
	}

	public static String listToJSON(List<?> list) {

		Gson gson = new Gson();
		return (gson.toJson(list));
	}

	public static boolean sendPOST(Context context, String route, String json) {

		Log.d(LOG_TAG, "sendPOST()");

		try {

			ConnectivityManager connMgr = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
			AsyncHTTPRequest req = new AsyncHTTPRequest();

			if (networkInfo != null && networkInfo.isConnected()) {

				String str = req.postToURL(route, json);
				
				if (str.equals("200")) {
					Log.v(LOG_TAG, "sendPOST() :"+ str);
					return true;
				} else {
					Log.v(LOG_TAG, "sendPOST() :"+ str);
					return false;
				}

			} else {

				Log.v(LOG_TAG, "sendPOST() :Lost Network Connectivity");
			}

		} catch (Exception e) {
			Log.d(LOG_TAG, e.getClass().getName());
			if (e.getLocalizedMessage() != null)
				Log.d(LOG_TAG, e.getLocalizedMessage());
		}
		return false;
	}
	
	public static String sendPOSTGET(Context context, String route, String json) {

		Log.d(LOG_TAG, "sendPOSTGET()");
		String str = new String();

		try {

			ConnectivityManager connMgr = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
			AsyncHTTPRequest req = new AsyncHTTPRequest();

			if (networkInfo != null && networkInfo.isConnected()) {

				str = req.postGetToURL(route, json);

				Log.v(LOG_TAG, "sendPOSTGET() :OK");

				return str;

			} else {

				Log.v(LOG_TAG, "sendPOSTGET() :Lost Network Connectivity");
			}

		} catch (Exception e) {
			Log.d(LOG_TAG, e.getClass().getName());
			if (e.getLocalizedMessage() != null)
				Log.d(LOG_TAG, e.getLocalizedMessage());
		}
		return str;
	}

	public static String receiveGET(Context context, String route) {

		Log.d(LOG_TAG, "receiveGET()");
		String json = new String();

		try {
			ConnectivityManager connMgr = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
			AsyncHTTPRequest req = new AsyncHTTPRequest();

			if (networkInfo != null && networkInfo.isConnected()) {

				json = req.getFromURL(route);
				Log.v(LOG_TAG, "receiveGET() :OK");

			} else {

				Log.v(LOG_TAG, "receiveGET() :Lost Network Connectivity");
			}
		} catch (Exception e) {
			Log.d(LOG_TAG, e.getClass().getName());
			if (e.getLocalizedMessage() != null)
				Log.d(LOG_TAG, e.getLocalizedMessage());
		}
		return json;
	}

	// ---------------------------------------
	//
	// CREATE end points
	//
	// ---------------------------------------

	public static boolean createUser(Context context, IASUser user) {

		Log.d(LOG_TAG, "createUser()");

		// POST

		// Prepare json object
		Gson gson = new Gson();
		String jsonTo = gson.toJson(user);

		// Send json
		if (sendPOST(context, getRouteCreateUser(), jsonTo)) {
			Log.v(LOG_TAG, "createUser() :POST OK");
			return true;
		} else {
			Log.v(LOG_TAG, "createUser() :POST Failed");
		}

		return false;
	}

	public static boolean createEvent(Context context, IASEvent event) {

		Log.d(LOG_TAG, "createEvent()");

		// Prepare json object
		Gson gson = new Gson();
		String jsonTo = gson.toJson(event);

		Log.v(LOG_TAG, "createEvent() :"+jsonTo.length());
		
		// Send json
		if (sendPOST(context, getRouteCreateEvent(), jsonTo)) {
			Log.v(LOG_TAG, "createEvent() :POST OK");
			return true;
		} else {
			Log.v(LOG_TAG, "createEvent() :POST Failed");
		}

		return false;
	}

	public static boolean createIncident(Context context, IASIncident incident) {

		Log.d(LOG_TAG, "createIncident()");

		// Prepare json object
		Gson gson = new Gson();
		String jsonTo = gson.toJson(incident);

		// Send json
		if (sendPOST(context, getRouteCreateIncident(), jsonTo)) {
			Log.v(LOG_TAG, "createIncident() :POST OK");
			return true;
		} else {
			Log.v(LOG_TAG, "createIncident() :POST Failed");
		}

		return false;
	}

	public static boolean createEngine(Context context, IASEngine engine) {

		Log.d(LOG_TAG, "createEngine()");

		// Prepare json object
		Gson gson = new Gson();
		String jsonTo = gson.toJson(engine);

		// Send json
		if (sendPOST(context, getRouteCreateEngine(), jsonTo)) {
			Log.v(LOG_TAG, "createEngine() :POST OK");
			return true;
		} else {
			Log.v(LOG_TAG, "createEngine() :POST Failed");
		}

		return false;
	}

	// ---------------------------------------
	//
	// READ end points
	//
	// ---------------------------------------

	public static List<IASUser> getAllUsers(Context context) {

		Log.d(LOG_TAG, "getAllUsers()");

		// GET

		// Receive user details from back-end
		String jsonFrom = receiveGET(context, getRouteGetAllUsers());

		// Deserialize result
		if (jsonFrom.isEmpty()) {
			Log.d(LOG_TAG, "getAllUsers() :GET Failed, Empty JSON");
			return null;
		}

		try {
			Gson gson = new Gson();
			Type listType = new TypeToken<ArrayList<IASUser>>() {
			}.getType();
			List<IASUser> list = gson.fromJson(jsonFrom, listType);
			Log.d(LOG_TAG, "getAllUsers() :GET OK, JSON");
			return list;

		} catch (Exception e) {
			Log.d(LOG_TAG, e.getClass().getName());
			if (e.getLocalizedMessage() != null)
				Log.d(LOG_TAG, e.getLocalizedMessage());
		}
		Log.d(LOG_TAG, "getAllUsers() :GET Failed, JSON");
		return null;
	}

	public static List<IASEvent> getAllEvents(Context context) {

		Log.d(LOG_TAG, "getAllEvents()");

		// GET

		// Receive user details from back-end
		String jsonFrom = receiveGET(context, getRouteGetAllEvents());

		// Deserialize result
		if (jsonFrom.isEmpty()) {
			Log.d(LOG_TAG, "getAllEvents() :GET Failed, Empty JSON");
			return null;
		}

		try {
			Gson gson = new Gson();
			Type listType = new TypeToken<ArrayList<IASEvent>>() {
			}.getType();
			List<IASEvent> list = gson.fromJson(jsonFrom, listType);
			Log.d(LOG_TAG, "getAllEvents() :GET OK, JSON");
			return list;

		} catch (Exception e) {
			Log.d(LOG_TAG, e.getClass().getName());
			if (e.getLocalizedMessage() != null)
				Log.d(LOG_TAG, e.getLocalizedMessage());
		}
		Log.d(LOG_TAG, "getAllEvents() :GET Failed, JSON");
		return null;
	}

	public static List<IASIncident> getAllIncidents(Context context) {

		Log.d(LOG_TAG, "getAllIncidents()");

		// GET

		// Receive user details from back-end
		String jsonFrom = receiveGET(context, getRouteGetAllIncidents());

		// Deserialize result
		if (jsonFrom.isEmpty()) {
			Log.d(LOG_TAG, "getAllIncidents() :GET Failed, Empty JSON");
			return null;
		}

		try {
			Gson gson = new Gson();
			Type listType = new TypeToken<ArrayList<IASIncident>>() {
			}.getType();
			List<IASIncident> list = gson.fromJson(jsonFrom, listType);
			Log.d(LOG_TAG, "getAllIncidents() :GET OK, JSON");
			return list;

		} catch (Exception e) {
			Log.d(LOG_TAG, e.getClass().getName());
			if (e.getLocalizedMessage() != null)
				Log.d(LOG_TAG, e.getLocalizedMessage());
		}
		Log.d(LOG_TAG, "getAllIncidents() :GET Failed, JSON");
		return null;
	}
	
	public static Integer[] getAllCOPThumbnailIdsByIncident(Context context) {
		Integer[] mThumbIds = {
    		R.drawable.cop_1_t,
    		R.drawable.cop_2_t,
    		R.drawable.cop_3_t,
    		R.drawable.cop_4_t,
    		R.drawable.cop_5_t,
    		R.drawable.cop_6_t,
    		R.drawable.cop_7_t,
    		R.drawable.cop_1_t,
    		R.drawable.cop_2_t,
    		R.drawable.cop_3_t,
    		R.drawable.cop_4_t,
    		R.drawable.cop_5_t,
    		R.drawable.cop_6_t,
    		R.drawable.cop_7_t,
    		R.drawable.cop_1_t,
    		R.drawable.cop_2_t,
    		R.drawable.cop_3_t,
    		R.drawable.cop_4_t,
    		R.drawable.cop_5_t,
    		R.drawable.cop_6_t,
    		R.drawable.cop_7_t
	    };
		
		return mThumbIds;
	}
	
	public static Integer getFullCOP(Context context, int pos) {
		Integer[] mFullIds = {
    		R.drawable.cop_1,
    		R.drawable.cop_2,
    		R.drawable.cop_3,
    		R.drawable.cop_4,
    		R.drawable.cop_5,
    		R.drawable.cop_6,
    		R.drawable.cop_7,
    		R.drawable.cop_1,
    		R.drawable.cop_2,
    		R.drawable.cop_3,
    		R.drawable.cop_4,
    		R.drawable.cop_5,
    		R.drawable.cop_6,
    		R.drawable.cop_7,
    		R.drawable.cop_1,
    		R.drawable.cop_2,
    		R.drawable.cop_3,
    		R.drawable.cop_4,
    		R.drawable.cop_5,
    		R.drawable.cop_6,
    		R.drawable.cop_7
	    };
		
		return mFullIds[pos];
	}

	public static List<IASEngine> getAllEngines(Context context) {
		// TODO - REMOVE comment when not needed once backend ready
		/*
		 * List<IASEngine> engine_list = new ArrayList<IASEngine>();
		 * engine_list.add(new IASEngine("AMB 1")); engine_list.add(new
		 * IASEngine("ENG 1")); engine_list.add(new IASEngine("ENG 2"));
		 * engine_list.add(new IASEngine("ENG 3")); engine_list.add(new
		 * IASEngine("ENG 4")); return engine_list;
		 */

		Log.d(LOG_TAG, "getAllEngines()");

		// GET

		// Receive user details from back-end
		String jsonFrom = receiveGET(context, getRouteGetAllEngines());
		List<IASEngine> list = new ArrayList<IASEngine>();
		// Deserialize result
		if (jsonFrom.isEmpty()) {
			Log.d(LOG_TAG, "getAllEngines() :GET Failed, Empty JSON");
			return null;
		}

		try {
			Gson gson = new Gson();
			Type listType = new TypeToken<ArrayList<IASEngine>>() {
			}.getType();
			list = gson.fromJson(jsonFrom, listType);
			Log.d(LOG_TAG, "getAllEngines() :GET OK, JSON");
			return list;

		} catch (Exception e) {
			Log.d(LOG_TAG, e.getClass().getName());
			if (e.getLocalizedMessage() != null)
				Log.d(LOG_TAG, e.getLocalizedMessage());
		}
		Log.d(LOG_TAG, "getAllEngines() :GET Failed, JSON");
		return null;
	}

	public static List<String> getAllEnginesNames(Context context) {

		Log.d(LOG_TAG, "getAllEnginesNames()");

		List<IASEngine> all_engines = getAllEngines(context);
		List<String> engine_names = new ArrayList<String>();
		for (IASEngine engine : all_engines) {
				engine_names.add(engine.getEngine_name());
		}

		return engine_names;
	}
	

	public static IASUser getUserByUserName(Context context, String user_name) {

		Log.d(LOG_TAG, "getUserByUserName()");

		// POST

		// Prepare json object
		Gson gson = new Gson();
		IASUser user = new IASUser(user_name);
		String jsonTo = gson.toJson(user);

		// Send post and read result
		String jsonFrom = sendPOSTGET(context, getRouteGetUserByUserName(), jsonTo);
		if (!jsonFrom.isEmpty()) {
			try {
				user = gson.fromJson(jsonFrom, IASUser.class);
				Log.d(LOG_TAG, "getUserByUserName() :GET OK, JSON");
				return user;

			} catch (Exception e) {
				Log.d(LOG_TAG, e.getClass().getName());
				if (e.getLocalizedMessage() != null)
					Log.d(LOG_TAG, e.getLocalizedMessage());
			}

			Log.v(LOG_TAG, "getUserByUserName() :POST OK");

		} else {
			Log.v(LOG_TAG, "getUserByUserName() :POST Failed");
			return null;
		}

		Log.d(LOG_TAG, "getUserByUserName() :GET Failed, JSON");
		return null;
	}

	public static List<IASUser> getUsersByJobRole(Context context, String role) {

		Log.d(LOG_TAG, "getUsersByJobRole()");

		// GET

		Gson gson = new Gson();
		IASUser user = new IASUser();
		user.setJob_role(role);
		String jsonTo = gson.toJson(user);

		// Send post and read result
		String jsonFrom = sendPOSTGET(context, getRouteGetUsersByJobRole(), jsonTo);

		// Deserialize result
		if (jsonFrom.isEmpty()) {
			Log.d(LOG_TAG, "getUsersByJobRole() :GET Failed, Empty JSON");
			return null;
		}

		try {
			Type listType = new TypeToken<ArrayList<IASUser>>() {
			}.getType();
			List<IASUser> list = gson.fromJson(jsonFrom, listType);
			Log.d(LOG_TAG, "getUsersByJobRole() :GET OK, JSON");
			return list;

		} catch (Exception e) {
			Log.d(LOG_TAG, e.getClass().getName());
			if (e.getLocalizedMessage() != null)
				Log.d(LOG_TAG, e.getLocalizedMessage());
		}
		Log.d(LOG_TAG, "getUsersByJobRole() :GET Failed, JSON");
		return null;
	}

	public static List<IASUser> getUsersByEngineName(Context context,
			String engine_name) {

		Log.d(LOG_TAG, "getUsersByEngineName()");

		// POST

		// Prepare json object
		Gson gson = new Gson();
		IASEngine engine = new IASEngine();
		engine.setEngine_name(engine_name);
		String jsonTo = gson.toJson(engine);

		// Send post and read result
		String jsonFrom = sendPOSTGET(context, getRouteGetUsersByEngineName(),
				jsonTo);
		
		if (!jsonFrom.isEmpty()) {
			try {

				Type listType = new TypeToken<ArrayList<IASUser>>() {
				}.getType();
				List<IASUser> list = gson.fromJson(jsonFrom, listType);
				Log.d(LOG_TAG, "getUsersByEngineName() :GET OK, JSON");
				return list;

			} catch (Exception e) {
				Log.d(LOG_TAG, e.getClass().getName());
				if (e.getLocalizedMessage() != null)
					Log.d(LOG_TAG, e.getLocalizedMessage());
			}

			Log.v(LOG_TAG, "getUsersByEngineName() :POST OK");

		} else {
			Log.v(LOG_TAG, "getUsersByEngineName() :POST Failed");
			return null;
		}

		Log.d(LOG_TAG, "getUsersByEngineName() :GET Failed, JSON");
		return null;
	}

	public static List<String> getUserNamesByEngineName(Context context, String engine_name) {
		List<String> user_names_by_engine = new ArrayList<String>();
		List<IASUser> userList = new ArrayList<IASUser>();
		userList = getUsersByEngineName(context, engine_name);
		
		for (IASUser all_users : userList) {
			user_names_by_engine.add(all_users.getUser_name());
		}

		return user_names_by_engine;
	}

	public static List<IASUser> getUsersByIncidentName(Context context,
			String incident_name) {

		Log.d(LOG_TAG, "getUsersByIncidentName()");

		// POST

		// Prepare json object
		Gson gson = new Gson();
		IASIncident incident = new IASIncident();
		incident.setIncident_name(incident_name);
		String jsonTo = gson.toJson(incident);

		// Send post and read result
		String jsonFrom = sendPOSTGET(context, getRouteGetUsersByIncidentName(),
				jsonTo);
		if (!jsonFrom.isEmpty()) {
			try {

				Type listType = new TypeToken<ArrayList<IASUser>>() {
				}.getType();
				List<IASUser> list = gson.fromJson(jsonFrom, listType);
				Log.d(LOG_TAG, "getUsersByIncidentName() :GET OK, JSON");
				return list;

			} catch (Exception e) {
				Log.d(LOG_TAG, e.getClass().getName());
				if (e.getLocalizedMessage() != null)
					Log.d(LOG_TAG, e.getLocalizedMessage());
			}

			Log.v(LOG_TAG, "getUsersByIncidentName() :POST OK");

		} else {
			Log.v(LOG_TAG, "getUsersByIncidentName() :POST Failed");
			return null;
		}

		Log.d(LOG_TAG, "getUsersByIncidentName() :GET Failed, JSON");
		return null;
	}
	
	public static List<IASEvent> getEventsByIncidentName(Context context,
			String incident_name) {

		Log.d(LOG_TAG, "getEventsByIncidentName()");

		// POST

		// Prepare json object
		Gson gson = new Gson();
		IASIncident incident = new IASIncident();
		incident.setIncident_name(incident_name);
		String jsonTo = gson.toJson(incident);

		// Send post and read result
		String jsonFrom = sendPOSTGET(context, getRouteGetEventsByIncidentName(),
				jsonTo);
		if (!jsonFrom.isEmpty()) {
			try {

				Type listType = new TypeToken<ArrayList<IASEvent>>() {
				}.getType();
				List<IASEvent> list = gson.fromJson(jsonFrom, listType);
				Log.d(LOG_TAG, "getEventsByIncidentName() :GET OK, JSON");
				return list;

			} catch (Exception e) {
				Log.d(LOG_TAG, e.getClass().getName());
				if (e.getLocalizedMessage() != null)
					Log.d(LOG_TAG, e.getLocalizedMessage());
			}

			Log.v(LOG_TAG, "getEventsByIncidentName() :POST OK");

		} else {
			Log.v(LOG_TAG, "getEventsByIncidentName() :POST Failed");
			return null;
		}

		Log.d(LOG_TAG, "getEventsByIncidentName() :GET Failed, JSON");
		return null;
	}
	
	public static String getLastAlertByIncidentName(Context context, String incident_name) {
		List<IASEvent> all_incidents = getEventsByIncidentName(context, incident_name);
		if (all_incidents.size() == 0) return "None";

		
		return getAlertDescription(all_incidents.get(0).getM());
	}

	private static String getAlertDescription(int m) {
		String description = "Invalid";
		switch (m) {
			case 1: description = "VACATE";
					break;
			case 2: description = "UTILITIES ON";
					break;
			case 3: description = "UTILITIES OFF";
					break;
			case 4: description = "RESCUE IN PROGRESS";
					break;
			case 5: description = "PAR";
					break;
			case 6: description = "LIFE HAZ";
					break;
			case 7: description = "ALL CLEAR";
					break;
			case 8: description = "VERT VENT";
					break;
			case 9: description = "CROSS VENT";
					break;
			default: break;
		}
		return description;
	}

	public static List<IASEvent> getEventsByUserName(Context context,
			String user_name) {

		Log.d(LOG_TAG, "getEventsByUserName()");

		// POST

		// Prepare json object
		Gson gson = new Gson();
		IASUser user = new IASUser();
		user.setUser_name(user_name);
		String jsonTo = gson.toJson(user);

		// Send post and read result
		String jsonFrom = sendPOSTGET(context, getRouteGetEventsByUserName(),
				jsonTo);
		if (!jsonFrom.isEmpty()) {
			try {

				Type listType = new TypeToken<ArrayList<IASEvent>>() {
				}.getType();
				List<IASEvent> list = gson.fromJson(jsonFrom, listType);
				Log.d(LOG_TAG, "getEventsByUserName() :GET OK, JSON");
				return list;

			} catch (Exception e) {
				Log.d(LOG_TAG, e.getClass().getName());
				if (e.getLocalizedMessage() != null)
					Log.d(LOG_TAG, e.getLocalizedMessage());
			}

			Log.v(LOG_TAG, "getEventsByUserName() :POST OK");

		} else {
			Log.v(LOG_TAG, "getEventsByUserName() :POST Failed");
			return null;
		}

		Log.d(LOG_TAG, "getEventsByUserName() :GET Failed, JSON");
		return null;
	}

	public static IASIncident getIncidentByIncidentName(Context context,
			String incident_name) {

		Log.d(LOG_TAG, "getIncidentByIncidentName()");

		// POST

		// Prepare json object
		Gson gson = new Gson();
		IASIncident incident = new IASIncident();
		incident.setIncident_name(incident_name);
		String jsonTo = gson.toJson(incident);

		// Send post and read result
		String jsonFrom = sendPOSTGET(context,
				getRouteGetIncidentByIncidentName(), jsonTo);
		if (!jsonFrom.isEmpty()) {
			try {

				incident = gson.fromJson(jsonFrom, IASIncident.class);
				Log.d(LOG_TAG, "getIncidentByIncidentName() :GET OK, JSON");
				return incident;

			} catch (Exception e) {
				Log.d(LOG_TAG, e.getClass().getName());
				if (e.getLocalizedMessage() != null)
					Log.d(LOG_TAG, e.getLocalizedMessage());
			}

			Log.v(LOG_TAG, "getIncidentByIncidentName() :POST OK");

		} else {
			Log.v(LOG_TAG, "getIncidentByIncidentName() :POST Failed");
			return null;
		}

		Log.d(LOG_TAG, "getIncidentByIncidentName() :GET Failed, JSON");
		return null;
	}

	public static IASIncident getCurrentIncidentByUserName(Context context,
			String user_name) {

		Log.d(LOG_TAG, "getCurrentIncidentByUserName()");

		// POST

		// Prepare json object
		Gson gson = new Gson();
		IASUser user = new IASUser();
		user.setUser_name(user_name);
		String jsonTo = gson.toJson(user);

		// Send post and read result
		String jsonFrom = sendPOSTGET(context,
				getRouteGetCurrentIncidentByUserName(), jsonTo);
		if (!jsonFrom.isEmpty()) {
			try {

				IASIncident incident = gson.fromJson(jsonFrom,
						IASIncident.class);
				Log.d(LOG_TAG, "getCurrentIncidentByUserName() :GET OK, JSON");
				return incident;

			} catch (Exception e) {
				Log.d(LOG_TAG, e.getClass().getName());
				if (e.getLocalizedMessage() != null)
					Log.d(LOG_TAG, e.getLocalizedMessage());
			}

			Log.v(LOG_TAG, "getCurrentIncidentByUserName() :POST OK");

		} else {
			Log.v(LOG_TAG, "getCurrentIncidentByUserName() :POST Failed");
			return null;
		}

		Log.d(LOG_TAG, "getCurrentIncidentByUserName() :GET Failed, JSON");
		return null;
	}

	public static List<IASIncident> getIncidentsByUserName(Context context,
			String user_name) {

		Log.d(LOG_TAG, "getIncidentsByUserName()");

		// POST

		// Prepare json object
		Gson gson = new Gson();
		IASUser user = new IASUser();
		user.setUser_name(user_name);
		String jsonTo = gson.toJson(user);

		// Send post and read result
		String jsonFrom = sendPOSTGET(context, getRouteGetIncidentsByUserName(),
				jsonTo);
		if (!jsonFrom.isEmpty()) {
			try {

				Type listType = new TypeToken<ArrayList<IASIncident>>() {
				}.getType();
				List<IASIncident> list = gson.fromJson(jsonFrom, listType);
				Log.d(LOG_TAG, "getIncidentsByUserName() :GET OK, JSON");
				return list;

			} catch (Exception e) {
				Log.d(LOG_TAG, e.getClass().getName());
				if (e.getLocalizedMessage() != null)
					Log.d(LOG_TAG, e.getLocalizedMessage());
			}

			Log.v(LOG_TAG, "getIncidentsByUserName() :POST OK");

		} else {
			Log.v(LOG_TAG, "getIncidentsByUserName() :POST Failed");
			return null;
		}

		Log.d(LOG_TAG, "getIncidentsByUserName() :GET Failed, JSON");
		return null;
	}

	public static IASEngine getEngineByEngineName(Context context,
			String engine_name) {

		Log.d(LOG_TAG, "getEngineByEngineName()");

		// POST

		// Prepare json object
		Gson gson = new Gson();
		IASEngine engine = new IASEngine();
		engine.setEngine_name(engine_name);
		String jsonTo = gson.toJson(engine);

		// Send post and read result
		String jsonFrom = sendPOSTGET(context, getRouteGetEngineByEngineName(),
				jsonTo);
		if (!jsonFrom.isEmpty()) {
			try {

				engine = gson.fromJson(jsonFrom, IASEngine.class);
				Log.d(LOG_TAG, "getEngineByEngineName() :GET OK, JSON");
				return engine;

			} catch (Exception e) {
				Log.d(LOG_TAG, e.getClass().getName());
				if (e.getLocalizedMessage() != null)
					Log.d(LOG_TAG, e.getLocalizedMessage());
			}

			Log.v(LOG_TAG, "getEngineByEngineName() :POST OK");

		} else {
			Log.v(LOG_TAG, "getEngineByEngineName() :POST Failed");
			return null;
		}

		Log.d(LOG_TAG, "getEngineByEngineName() :GET Failed, JSON");
		return null;
	}

	// ---------------------------------------
	//
	// DELETE end points
	//
	// ---------------------------------------

	public static boolean deleteUserByUserName(Context context, String user_name) {

		Log.d(LOG_TAG, "deleteUserByUserName()");

		// POST

		// Prepare json object
		Gson gson = new Gson();
		IASUser user = new IASUser();
		user.setUser_name(user_name);
		String jsonTo = gson.toJson(user);

		// Send json
		if (sendPOST(context, getRouteDeleteUserByUserName(), jsonTo)) {
			Log.v(LOG_TAG, "deleteUserByUserName() :POST OK");
			return true;
		} else {
			Log.v(LOG_TAG, "deleteUserByUserName() :POST Failed");
		}

		return false;
	}

	public static boolean deleteEventsByIncidentName(Context context,
			String incident_name) {
		
		Log.d(LOG_TAG, "deleteEventsByIncidentName()");

		// POST

		// Prepare json object
		Gson gson = new Gson();
		IASIncident incident = new IASIncident();
		incident.setIncident_name(incident_name);
		String jsonTo = gson.toJson(incident);

		// Send json
		if (sendPOST(context, getRouteDeleteEventsByIncidentName(), jsonTo)) {
			Log.v(LOG_TAG, "deleteEventsByIncidentName() :POST OK");
			return true;
		} else {
			Log.v(LOG_TAG, "deleteEventsByIncidentName() :POST Failed");
		}

		return false;
	}

	public static boolean deleteIncidentByIncidentName(Context context,
			String incident_name) {
		
		Log.d(LOG_TAG, "deleteIncidentByIncidentName()");

		// POST

		// Prepare json object
		Gson gson = new Gson();
		IASIncident incident = new IASIncident();
		incident.setIncident_name(incident_name);
		String jsonTo = gson.toJson(incident);

		// Send json
		if (sendPOST(context, getRouteDeleteIncidentByIncidentName(), jsonTo)) {
			Log.v(LOG_TAG, "deleteIncidentByIncidentName() :POST OK");
			return true;
		} else {
			Log.v(LOG_TAG, "deleteIncidentByIncidentName() :POST Failed");
		}

		return false;
	}

	public static boolean deleteEngineByEngineName(Context context,
			String engine_name) {
		
		Log.d(LOG_TAG, "deleteEngineByEngineName()");

		// POST

		// Prepare json object
		Gson gson = new Gson();
		IASEngine engine = new IASEngine();
		engine.setEngine_name(engine_name);
		String jsonTo = gson.toJson(engine);

		// Send json
		if (sendPOST(context, getRouteDeleteEngineByEngineName(), jsonTo)) {
			Log.v(LOG_TAG, "deleteEngineByEngineName() :POST OK");
			return true;
		} else {
			Log.v(LOG_TAG, "deleteEngineByEngineName() :POST Failed");
		}

		return false;
	}

	// ---------------------------------------
	//
	// UPDATE end points
	//
	// ---------------------------------------

	public static boolean updateUserByUserName(Context context, IASUser user) {

		Log.d(LOG_TAG, "updateUserByUserName()");

		// POST

		// Prepare json object
		Gson gson = new Gson();
		String jsonTo = gson.toJson(user);

		// Send json
		if (sendPOST(context, getRouteUpdateUserByUserName(), jsonTo)) {
			Log.v(LOG_TAG, "updateUserByUserName() :POST OK");
			return true;
		} else {
			Log.v(LOG_TAG, "updateUserByUserName() :POST Failed");
		}

		return false;
	}

	public static boolean updateIncidentByIncidentName(Context context,
			IASIncident incident) {

		Log.d(LOG_TAG, "updateIncidentByIncidentName()");

		// POST

		// Prepare json object
		Gson gson = new Gson();
		String jsonTo = gson.toJson(incident);

		// Send json
		if (sendPOST(context, getRouteUpdateIncidentByIncidentName(), jsonTo)) {
			Log.v(LOG_TAG, "updateIncidentByIncidentName() :POST OK");
			return true;
		} else {
			Log.v(LOG_TAG, "updateIncidentByIncidentName() :POST Failed");
		}

		return false;
	}

	public static boolean updateIncidentCommander(Context context,
			String incident_name, String commander) {

		Log.d(LOG_TAG, "updateIncidentCommander()");

		// POST

		// Prepare json object
		Gson gson = new Gson();
		IASIncident incident = new IASIncident();
		incident.setIncident_name(incident_name);
		incident.setCommander(commander);
		String jsonTo = gson.toJson(incident);

		// Send json
		if (sendPOST(context, getRouteUpdateIncidentCommander(), jsonTo)) {
			Log.v(LOG_TAG, "udpateIncidentFirefighters() :POST OK");
			return true;
		} else {
			Log.v(LOG_TAG, "updateIncidentCommander() :POST Failed");
		}

		return false;
	}

	public static boolean updateEngineByEngineName(Context context,
			IASEngine engine) {

		Log.d(LOG_TAG, "updateEngineByEngineName()");

		// POST

		// Prepare json object
		Gson gson = new Gson();
		String jsonTo = gson.toJson(engine);

		// Send json
		if (sendPOST(context, getRouteUpdateEngineByEngineName(), jsonTo)) {
			Log.v(LOG_TAG, "updateEngineByEngineName() :POST OK");
			return true;
		} else {
			Log.v(LOG_TAG, "updateEngineByEngineName() :POST Failed");
		}

		return false;
	}

	// ---------------------------------------
	//
	// AUTHENTICATE end points
	//
	// ---------------------------------------

	public static boolean authenticateUser(Context context, String user_name,
			String password, String contact, String engine_name, String role) {

		return true;
		/*
		// Prepare json object
		Gson gson = new Gson();
		IASUser user = new IASUser(user_name);
		user.setPassword(password);
		user.setContact(contact);
		user.setEngine_name(engine_name);
		user.setJob_role(role);
		String jsonTo = gson.toJson(user);

		// Send post
		if (sendPOST(context, getRouteAuthenticateUser(), jsonTo)) {
			Log.v(LOG_TAG, "authenticateUser() :POST OK");
			return true;
		} else {
			Log.v(LOG_TAG, "authenticateUser() :POST Failed");
		}
		return false;*/
	}

	// /////////////////////////
	//
	// - Other remaining methods here, for potential removal when ready - GG
	//
	//

	public static String receiveGETUsers(Context context, String route) { // Gerard

		Log.d(LOG_TAG, "receiveGETUsers()");
		String json = new String();

		try {
			ConnectivityManager connMgr = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
			AsyncHTTPRequest req = new AsyncHTTPRequest();

			if (networkInfo != null && networkInfo.isConnected()) {

				json = req.getFromURL(getRouteGetAllUsers());

				Log.v(LOG_TAG, "receiveGETUsers() :OK");

			} else {

				Log.v(LOG_TAG, "receiveGETUsers() :Lost Network Connectivity");
			}
		} catch (Exception e) {
			Log.d(LOG_TAG, e.getClass().getName());
			if (e.getLocalizedMessage() != null)
				Log.d(LOG_TAG, e.getLocalizedMessage());
		}

		return json;
	}

	public static void sqlPutListPersonnel(DBAdapter db, String json) { // Gerard
		// public static <T> List<T> jsonToListUsers(Class<T> theClass, String
		// route) {

		Log.d(LOG_TAG, "sqlPutListPersonnel()");

		if (json.isEmpty()) {
			Log.d(LOG_TAG, "sqlPutListPersonnel(): Received Empty JSON");
			return;
		}

		Log.v("--Gerard--", "sqlPutListPersonnel(): Received JSON-->" + json);

		try {
			// De-serialize JSON
			Gson gson = new Gson();
			Type listType = new TypeToken<ArrayList<IASUser>>() {
			}.getType();
			List<IASUser> list = gson.fromJson(json, listType);

			// Insert records to local SQLite DB
			db.open();
			for (IASUser user : list) {
				db.insertPersonnel(user.getUser_name(), user.getContact());
			}
			db.close();
		} catch (Exception e) {
			Log.d(LOG_TAG, e.getClass().getName());
			if (e.getLocalizedMessage() != null)
				Log.d(LOG_TAG, e.getLocalizedMessage());
		}
	}

	public static List<IASEvent> sqlGetListPersonnel(DBAdapter db) { // -Gerard

		Log.d(LOG_TAG, "sqlToListPersonnel()");

		List<IASEvent> iasEvents = new ArrayList<IASEvent>();

		// Get list from SQlite
		// IASEvent event = new IASEvent();

		// event.phonenumber = "phone number1";
		// event.message = "Vacate";
		// event.timeStamp = "time-date1";
		// iasEvents.add(event);

		// event = new IASEvent();
		// event.phonenumber = "phone number2";
		// event.message = "All Clear";
		// event.timeStamp = "time-date2";
		// iasEvents.add(event);

		return iasEvents;
	}

	/*
	 * public static void sendLocalPOST(Context context, DBAdapter db) { -
	 * Gerard
	 * 
	 * Log.d(LOG_TAG, "sendLocalPOST()");
	 * 
	 * List<IASEvent> events = sqlGetListPersonnel(db); if (sendPOST(context,
	 * getPostRoute(), listToJSON(events))) { // // TODO - clear sent msgs from
	 * SQLite buffer
	 * 
	 * Log.v(LOG_TAG, "sendLocalPOST() :Cleared Sent Buffer");
	 * 
	 * } else { Log.v(LOG_TAG, "sendLocalPOST() :Failed"); } }
	 */

}
