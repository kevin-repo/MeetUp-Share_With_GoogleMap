package com.example.models;

import java.io.Serializable;
import java.util.ArrayList;

public class Event implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long id;
	private String titre;
	private String location;
	private String date;
	private String heure;
	private String description;
	
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

	public String getTitre() {
		return titre;
	}

	public void setTitre(String titre) {
		this.titre = titre;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getHeure() {
		return heure;
	}

	public void setHeure(String heure) {
		this.heure = heure;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	//Permet de tester la liste d'événements
	public Event(String titre, String date, String heure) {
		super();
		this.titre = titre;
		this.date = date;
		this.heure = heure;
	}
	
	public static ArrayList<Event> generateListOfEvent() {
		ArrayList<Event> listEvent = new ArrayList<Event>();
		
		listEvent.add(new Event("Event1", "01/02/15", "13:15"));
		listEvent.add(new Event("Event2", "02/02/15", "13:15"));
		listEvent.add(new Event("Event3", "03/02/15", "13:15"));
		listEvent.add(new Event("Event4", "04/02/15", "13:15"));
		listEvent.add(new Event("Event5", "05/02/15", "13:15"));
		listEvent.add(new Event("Event6", "06/02/15", "13:15"));
		listEvent.add(new Event("Event7", "07/02/15", "13:15"));
		listEvent.add(new Event("Event8", "08/02/15", "13:15"));
		listEvent.add(new Event("Event9", "09/02/15", "13:15"));
		listEvent.add(new Event("Event10", "10/02/15", "13:15"));
		
		return listEvent;
	}
}
