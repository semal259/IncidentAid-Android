package com.cmusv.ias;

import java.util.ArrayList;
import java.util.List;

import android.app.Application;
import android.util.Log;

public class SharedData extends Application
{
	//private AcknowledgementTab ackTabObj = null;; // it will have a ref to the current running object or null
	private CustomDialog flashScreen = null;     // it will have a ref to the current running flashscreen. 
	private IASUser currentUser = null;
	
	private String localContact; //for now - Gerard
	private String localRole; //for now - Gerard
	
	private List<String> userContactList = new ArrayList<String>();
	
	public CustomDialog getFlashScreen() {
		return flashScreen;
	}

	public void setFlashScreen(CustomDialog flashScreen) {
		this.flashScreen = flashScreen;
	}
	
	public void setUserContactList(List<String> userContactList) {
		this.userContactList = userContactList;
	}
	
	public List<String> getUserContactList() {
		return userContactList;
	}

	//public AcknowledgementTab getAckTabObj() {
	//	return ackTabObj;
	//}

	//public void setAckTabObj(AcknowledgementTab ackTabObj) {
	//	this.ackTabObj = ackTabObj;
	//}

	public String getLocalContact() {
		return localContact;
	}

	public void setLocalContact(String localContact) {
		this.localContact = localContact;
	}

	public String getLocalRole() {
		return localRole;
	}

	public void setLocalRole(String localRole) {
		this.localRole = localRole;
	}
	
  public IASUser getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(IASUser currentUser) {
		this.currentUser = currentUser;
	}
	
	public static int parseEngineNumber(Object selectedItem) {
		String[] engine = selectedItem.toString().split(" ");
		int engine_number = -1;
		
		if (engine.length == 2 && "Engine".equals(engine[0])) {
			try {
				engine_number = Integer.parseInt(engine[1]);
			} catch (NumberFormatException e) {
			}
		}
		
		Log.d("parseEngineNumber", "engine_number = " + engine_number);
		
		return engine_number;
	}
	

}
