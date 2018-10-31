/**
 * 
 */
package com.cmusv.ias;

import java.util.Date;

/**
 * 
 *
 */
public class IASEvent {
	
	// Removed incident name from event structure.
	// Event will be correlated to current incident by incident ID
	// on the server side only. Event may include incident number,
	// if multiple incidents are to be handled during the given day,
	// in the future implementation, the event structure sizing
	// needs to be revisited then, e.g. with more efficient
	// information packing/encoding
	private String user_n = null;	// user name
	private int r = 0;				// incident role: 1-commander, 2-firefighter
	private int tp = 0;				// message type: 1-REQ, 2-ACK, 3-COP
	private int m = 0;				// message: 1 - Vacate
									// message: 2 - Utilities On
									// message: 3 - Utilities Off
									// message: 4 - Rescue In Progress
									// message: 5 - PAR
									// message: 6 - Life Hazard
									// message: 7 - All Clear
									// message: 8 - Vertical Vent
									// message: 9 - Cross Vent
	private double lt = 0.0;		// GPS latitude, 6 decimal places
	private double lg = 0.0;		// GPS longitude, 6 decimal places
	private String tk = null;		// token string for keeping track of messages
	private String ts = null;		// time stamp string

	public IASEvent() {

	}

	public IASEvent(String user_n, int r, int tp, int m, double lt, double lg,
			String tk) {
		this.user_n = user_n;
		this.r = r;
		this.tp = tp;
		this.m = m;
		this.lt = lt;
		this.lg = lg;
		this.tk = tk;
		Date date = new Date();
		this.ts = date.toString();
	}

	public String getUser_n() {
		return user_n;
	}

	public void setUser_n(String user_n) {
		this.user_n = user_n;
	}

	public int getR() {
		return r;
	}

	public void setR(int r) {
		this.r = r;
	}

	public int getTp() {
		return tp;
	}

	public void setTp(int tp) {
		this.tp = tp;
	}

	public int getM() {
		return m;
	}

	public void setM(int m) {
		this.m = m;
	}

	public double getLt() {
		return lt;
	}

	public void setLt(double lt) {
		this.lt = lt;
	}

	public double getLg() {
		return lg;
	}

	public void setLg(double lg) {
		this.lg = lg;
	}

	public String getTk() {
		return tk;
	}

	public void setTk(String tk) {
		this.tk = tk;
	}

	public String getTs() {
		return ts;
	}

	public void setTs(String ts) {
		this.ts = ts;
	}

	@Override
	public String toString() {
		return "IASEvent [user_n=" + user_n + ", r=" + r + ", tp=" + tp
				+ ", m=" + m + ", lt=" + lt + ", lg=" + lg + ", tk=" + tk
				+ ", ts=" + ts + "]";
	}
}
