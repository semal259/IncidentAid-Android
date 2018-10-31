package com.cmusv.ias;

public class IASUser {

	private String user_n = null;
	private String password = null;
	private String job_role = null;
	private String incident_role = null;
	private String contact = null;
	private String engine_name = null;

	public IASUser() {

	}

	public IASUser(String user_name) {
		this.user_n = user_name;
	}

	public IASUser(String user_name, String password, String contact,
			String engine_name) {
		this.user_n = user_name;
		this.password = password;
		this.contact = contact;
		this.engine_name = engine_name;
	}

	public String getUser_name() {
		return user_n;
	}

	public void setUser_name(String user_name) {
		this.user_n = user_name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getJob_role() {
		return job_role;
	}

	public void setJob_role(String job_role) {
		this.job_role = job_role;
	}

	public String getIncident_role() {
		return incident_role;
	}

	public void setIncident_role(String incident_role) {
		this.incident_role = incident_role;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getEngine_name() {
		return engine_name;
	}

	public void setEngine_name(String engine_name) {
		this.engine_name = engine_name;
	}

	@Override
	public String toString() {
		return "IASUser [user_n=" + user_n + ", password=" + password
				+ ", job_role=" + job_role + ", incident_role=" + incident_role
				+ ", contact=" + contact + ", engine_name=" + engine_name + "]";
	}
}



