package com.example.models;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Modele d'un utilisateur
 *
 */
public class User implements Serializable{

	private static final long serialVersionUID = 1L;
	private long id;
	private String email, lastname, firstname;

	/**
	 * Constructeur
	 */
	public User() {
		super();
		this.id = 0;
		this.email = "";
		this.lastname = "";
		this.firstname = "";
	}
	
	/**
	 * Constructeur prenant en parametre un objet JSON
	 * @param jsonUser
	 * @throws JSONException
	 */
	public User(JSONObject jsonUser) throws JSONException {
		super();
		this.id = jsonUser.getInt("id");
		this.email = jsonUser.getString("email");
		this.lastname = jsonUser.getString("lname");
		this.firstname = jsonUser.getString("fname");
	}

	/**
	 * Retourne l'id de l'user
	 * @return long
	 */
	public long getId() {
		return id;
	}

	/**
	 * Remplace l'id de l'user
	 * @param id
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * Retourne le mail de l'user
	 * @return String
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Remplace le mail de l'user
	 * @param email
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Retourne le nom de famille de l'user
	 * @return String
	 */
	public String getLastname() {
		return lastname;
	}

	/**
	 * Remplace le nom de famille de l'user
	 * @param lastname
	 */
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	/**
	 * Retourne le prenom de l'user
	 * @return String
	 */
	public String getFirstname() {
		return firstname;
	}

	/**
	 * Remplace le prenom de l'user
	 * @param firstname
	 */
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	
}
