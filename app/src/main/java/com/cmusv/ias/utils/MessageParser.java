package com.cmusv.ias.utils;

import android.util.Log;

public class MessageParser {
	
	private  boolean isAppMessage;
	private  boolean isAcknowledgement;
	private  String  messageStamp;
	private  String  messageContent;
	private  String  msg;
	
	public static final String APPLICATION_STAMP= "*!="; // a delimiter to be used to filter  SMS/Messages that 
														 // are generated by the MMAER APP (the first 3 char of any msg)
	public static final String ACK_STAMP="ACK";// a delimiter to be used to filter acknowledgments msgs from other msgs
											   // msg.substring(3,6)

	public MessageParser(String msg)
	{
		this.msg =msg;
		processMSG(msg);
	}
	
	
	public void processMSG(String msg)
	{
		Log.v("--Gerard--","MessageParser->processMSG()");
		
		if(this.msg!=null && this.msg.length()>=9)
		{
			if(this.msg.substring(0,3).equals(APPLICATION_STAMP)) {
				Log.d("-- Nelson --", "has APPLICATION STAMP");
				
				isAppMessage=true;

			} else {
				Log.d("-- Nelson --", "does not have APPLICATION STAMP");
				
				isAppMessage=false;
				
			}
		
			if(this.msg.contains(ACK_STAMP))
				isAcknowledgement=true;
			else
				isAcknowledgement=false;
		
			this.messageStamp= this.msg.substring(6,9);
			this.messageContent= this.msg.substring(9);
		}
		else
		{
			this.isAppMessage=false;
			this.isAcknowledgement=false;
		}
	}
	
	public boolean isAppMessage()
	{
		return isAppMessage;
	}
	
	public boolean isAcknowledgement()
	{
		return isAcknowledgement;
	}
	
	public String getMessage()
	{
		return messageContent;
	}
	
	public String getMessageStamp()
	{
		return messageStamp;
	}
	
}