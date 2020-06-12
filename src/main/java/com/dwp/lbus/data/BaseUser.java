package com.dwp.lbus.data;

import io.vertx.core.json.JsonObject;

public class BaseUser {
	private int id;

	private String first_name;

	private String last_name;

	private String email;

	private String ip_address;

	private Object latitude;

	private Object longitude;

	public BaseUser(int id, String first_name, String last_name, String email, String ip_address, Object latitude,
			Object longitude) {
		this.id = id;
		this.first_name = first_name;
		this.last_name = last_name;
		this.email = email;
		this.ip_address = ip_address;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public BaseUser(JsonObject json) {
		this.id = json.getInteger("id");
		this.first_name = json.getString("first_name");
		this.last_name = json.getString("last_name");
		this.email = json.getString("email");
		this.ip_address = json.getString("ip_address");
		this.latitude = json.getValue("latitude");
		this.longitude = json.getValue("longitude");
	}
	
	public BaseUser() {
		
	}

	public JsonObject toJson() {
		JsonObject json = new JsonObject();
		json.put("id", this.getId());
		if (this.getFirst_name() != null) {
			json.put("first_name", this.getFirst_name());
		}
		if (this.getLast_name() != null) {
			json.put("last_name", this.getLast_name());
		}
		if (this.getEmail() != null) {
			json.put("email", this.getEmail());
		}
		if (this.getIp_address() != null) {
			json.put("ip_address", this.getIp_address());
		}
		json.put("latitude", this.getLatitude());
		json.put("longitude", this.getLongitude());
		return json;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFirst_name() {
		return first_name;
	}

	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	public String getLast_name() {
		return last_name;
	}

	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getIp_address() {
		return ip_address;
	}

	public void setIp_address(String ip_address) {
		this.ip_address = ip_address;
	}

	public Object getLatitude() {
		return latitude;
	}

	public void setLatitude(Object latitude) {
		this.latitude = latitude;
	}

	public Object getLongitude() {
		return longitude;
	}

	public void setLongitude(Object longitude) {
		this.longitude = longitude;
	}

}
