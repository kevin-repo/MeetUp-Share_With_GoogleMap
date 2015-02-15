package com.example.meetupshare;

import org.json.JSONArray;

public interface ListOfItems {
	
	void populateList(JSONArray array);
	void show();
	void removeItemOfList(int i);
	
}
