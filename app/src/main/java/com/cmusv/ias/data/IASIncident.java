package com.cmusv.ias.data;

import java.util.ArrayList;
import java.util.List;

public class IASIncident {

	private String incident_name = null;
	private String commander = null;
	private List<String> firefighters = new ArrayList<String>();
	private double gps_lat = 0.0;
	private double gps_long = 0.0;
	private String street = null;
	private String city = null;
	private String state = null;
	private String zip = null;
	private String start_time = null;
	private String end_time = null;
	private String time_stamp = null;
	
	public IASIncident() {

	}

	public String getIncident_name() {
		return incident_name;
	}

	public void setIncident_name(String incident_name) {
		this.incident_name = incident_name;
	}

	public String getCommander() {
		return commander;
	}

	public void setCommander(String commander) {
		this.commander = commander;
	}

	public List<String> getFirefighters() {
		return firefighters;
	}

	public void setFirefighters(List<String> firefighters) {
		this.firefighters = firefighters;
	}

	public double getGps_lat() {
		return gps_lat;
	}

	public void setGps_lat(double gps_lat) {
		this.gps_lat = gps_lat;
	}

	public double getGps_long() {
		return gps_long;
	}

	public void setGps_long(double gps_long) {
		this.gps_long = gps_long;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getStart_time() {
		return start_time;
	}

	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}

	public String getEnd_time() {
		return end_time;
	}

	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}

	public String getTime_stamp() {
		return time_stamp;
	}

	public void setTime_stamp(String time_stamp) {
		this.time_stamp = time_stamp;
	}

	@Override
	public String toString() {
		return "IASIncident [incident_name=" + incident_name + ", commander="
				+ commander + ", firefighters=" + firefighters + ", gps_lat="
				+ gps_lat + ", gps_long=" + gps_long + ", street=" + street
				+ ", city=" + city + ", state=" + state + ", zip=" + zip
				+ ", start_time=" + start_time + ", end_time=" + end_time
				+ ", time_stamp=" + time_stamp + "]";
	}

}
