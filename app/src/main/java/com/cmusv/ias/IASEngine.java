package com.cmusv.ias;

public class IASEngine {
	private String engine_name = null;
	private String hotspot = null;
	
	public IASEngine() {
	}
	
	public IASEngine(String engine_name) {
		this.engine_name = engine_name;
	}

	public String getEngine_name() {
		return engine_name;
	}

	public void setEngine_name(String engine_name) {
		this.engine_name = engine_name;
	}

	public String getHotspot() {
		return hotspot;
	}

	public void setHotspot(String hotspot) {
		this.hotspot = hotspot;
	}

	@Override
	public String toString() {
		return "IASEngine [engine_name=" + engine_name + ", hotspot=" + hotspot
				+ "]";
	}
	
}
