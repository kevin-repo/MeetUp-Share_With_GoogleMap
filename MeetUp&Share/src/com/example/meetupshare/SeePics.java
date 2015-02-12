package com.example.meetupshare;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;

import com.example.meetupshare.adapters.EventAdapter;
import com.example.models.Event;
import com.example.models.User;
import com.example.webservice.Webservice;
import com.loopj.android.http.JsonHttpResponseHandler;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class SeePics extends MainActivity{

	private ArrayList<Event> mListEvent;
	private ListView mList;
	private User mCurrentUser;
	private EventAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.see_pictures);

		mListEvent = new ArrayList<Event>();
		mList = (ListView)findViewById(R.id.event_gone_list);
		mCurrentUser = (User)getIntent().getExtras().get("currentUser");

		mAdapter = new EventAdapter(this, android.R.layout.simple_list_item_multiple_choice, mListEvent, true);
		mList.setAdapter(mAdapter);

		init();
	}


	
	/**
	 * Initialisation de l'activity
	 */
	public void init(){
		//Recuperation de la liste d'event passés
		String url = "events.php?method=readeventsgone&idu="+mCurrentUser.getId();
		Webservice.get(url, null, new JsonHttpResponseHandler(){			
			//Version 1
			public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
				Log.d("event_gone_list", "success");
				populateList(response);
				show();
			}

			public void onFailure(int statusCode, Header[] headers, String s, Throwable e) {
				Log.d("event_gone_list", "failure");
			}
		});

		//Ecouteur d'événement sur la liste des event
		mList.setOnItemClickListener(new OnItemClickListener () {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(mListEvent.get(position).getUrl()));
				startActivity(intent); 
			}			
		});
	}

	/**
	 * Permet de remplir la liste des evenements
	 * @param array
	 */
	protected void populateList(JSONArray array){
		for(int i = 0; i < array.length(); i++){
			Event e = new Event();
			try {
				e.setId(array.getJSONArray(i).optLong(0));
				e.setLocation(ajouterEspace(array.getJSONArray(i).optString(1)));
				//TODO Separer date et heure
				e.setDate(array.getJSONArray(i).optString(2));
				e.setTitre(ajouterEspace(array.getJSONArray(i).optString(3)));
				e.setUrl(array.getJSONArray(i).optString(4));
				mListEvent.add(e);
			} catch (JSONException e1) {
				e1.printStackTrace();
			}		
		}
	}

	/**
	 * Permet d'afficher les evenements
	 */
	protected void show(){
		//mise a jour de la liste d'evenements
		mAdapter.setEventList(mListEvent);
		//notify l'adapteur
		mAdapter.notifyDataSetChanged();
	}

	public String ajouterEspace(String s){
		String res;
		res = s.replace("%", " ");
		return res;
	}
	
}
