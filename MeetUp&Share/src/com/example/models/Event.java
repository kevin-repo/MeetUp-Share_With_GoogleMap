package com.example.models;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Modele d'un evenement
 *
 */
public class Event implements Serializable {

	private static final long serialVersionUID = 1L;
	private long id;
	private String titre, location, date, heure, description, url;
	
	/**
	 * Constructeur
	 */
	public Event() {
		super();
		this.id = 0;
		this.location = "";
		this.date = "";
		this.heure = "";
		this.description = "";
		this.url = "";
	}
	
	/**
	 * Constructeur prenant en parametre un objet JSON
	 * @param jsonEvent
	 * @throws JSONException
	 */
	public Event(JSONObject jsonEvent) throws JSONException {
		super();
		this.id = jsonEvent.getInt("id");
		this.titre = jsonEvent.getString("title");
		this.location = jsonEvent.getString("place");
		this.date = jsonEvent.getString("date");
	}
	
	/**
	 * Retourne Id
	 * @return long Id de l'evenement
	 */
	public long getId() {
		return id;
	}

	/**
	 * Remplace Id de l'evenement
	 * @param id
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * Retourne le lieu de l'evenement
	 * @return String
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * Remplace le lieu de l'evenement
	 * @param location
	 */
	public void setLocation(String location) {
		this.location = location;
	}

	/**
	 * Retourne le titre de l'evenement
	 * @return String
	 */
	public String getTitre() {
		return titre;
	}

	/**
	 * Remplace le titre de l'evenement
	 * @param titre
	 */
	public void setTitre(String titre) {
		this.titre = titre;
	}

	/**
	 * Retourne la date de l'evenement
	 * @return String
	 */
	public String getDate() {
		return date;
	}

	/**
	 * Remplace la date de l'evenement
	 * @param date
	 */
	public void setDate(String date) {
		this.date = date;
	}

	/**
	 * Retourne l'heure de l'evenement
	 * @return String
	 */
	public String getHeure() {
		return heure;
	}

	/**
	 * Remplace l'heure de l'evenement
	 * @param heure
	 */
	public void setHeure(String heure) {
		this.heure = heure;
	}

	/**
	 * Retourne la description de l'evenement
	 * @return String
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Remplace la description de l'evenement
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Retourne l'url pour le partage photo
	 * @return String
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * Remplace l'url pour le partage photo
	 * @param url
	 */
	public void setUrl(String url) {
		this.url = url;
	}

}
