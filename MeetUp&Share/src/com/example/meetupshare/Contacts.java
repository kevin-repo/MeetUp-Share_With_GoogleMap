package com.example.meetupshare;

import java.util.ArrayList;

import com.example.meetupshare.adapters.FriendAdapter;
import com.example.models.User;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

public class Contacts extends Activity  {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contacts);
		
		final ArrayList<User> listFriend = User.generateListOfFriend();
		FriendAdapter adapter = new FriendAdapter(listFriend, this);
		final ListView list = (ListView)findViewById(R.id.liste_contacts);

		list.setAdapter(adapter);
	}
	
	public void addFriend() {	
		
	}
	
	public void removeFriend() {
		
	}
}
