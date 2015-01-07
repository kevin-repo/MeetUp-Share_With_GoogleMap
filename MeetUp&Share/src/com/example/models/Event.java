package com.example.models;

public class Event {

	private long id;
	private String location;
	
	public Event() {
		super();
		this.id = 0;
		this.location = "";
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
	
}
