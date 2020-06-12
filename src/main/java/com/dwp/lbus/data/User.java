package com.dwp.lbus.data;

import io.vertx.core.json.JsonObject;

public class User extends BaseUser {

	private String city;

	public User(int id, String first_name, String last_name, String email, String ip_address, Object latitude,
			Object longitude, String city) {
		super(id, first_name, last_name, email, ip_address, latitude, longitude);
		this.city = city;
	}

	public User(JsonObject json) {
		super(json);
		this.city = json.getString("city");
	}

	public User() {

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
		if (this.getCity() != null) {
			json.put("city", this.getCity());
		}
		return json;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

}
