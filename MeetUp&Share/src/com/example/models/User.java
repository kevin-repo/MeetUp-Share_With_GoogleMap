package com.example.models;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long id;
	private String email;
	private String lastname;
	private String firstname;

	public User() {
		super();
		this.id = 0;
		this.email = "";
		this.lastname = "";
		this.firstname = "";
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	//Permet de tester la liste d'événements
	public User(String fname, String lname) {
		super();
		this.firstname = fname;
		this.lastname = lname;
	}

	public static ArrayList<User> generateListOfFriend() {
		ArrayList<User> listFriend = new ArrayList<User>();

		listFriend.add(new User("Johnny", "Halliday"));
		listFriend.add(new User("Sebastien", "Patoche"));
		listFriend.add(new User("Zinedine", "Zidane"));

		return listFriend;
	}
}
