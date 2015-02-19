package com.example.meetupshare;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;

import com.example.meetupshare.adapters.EventAdapter;
import com.example.models.Event;
import com.example.models.User;
import com.example.webservice.Webservice;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

/**
 * Liste des evenements dont on peut voir les photos
 *
 */
public class SeePics extends MainActivity implements ListOfItems{

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
	 * Initialisation de la liste des evenements
	 */
	public void init(){
		RequestParams params = new RequestParams();
		params.put("idu", mCurrentUser.getId());

		String file = Webservice.eventsMethod();

		//Recuperation de la liste d'event passés
		Webservice.get(file+"?method=readeventsgone", params, new JsonHttpResponseHandler(){			
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
				URL url;
				try {
					url = new URL(mListEvent.get(position).getUrl());
					HttpsURLConnection connexion = (HttpsURLConnection)url.openConnection();
					Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(mListEvent.get(position).getUrl()));
					startActivity(intent); 
				} catch (MalformedURLException e) {
					e.printStackTrace();
					Toast toast = Toast.makeText(getApplicationContext(), "L'adresse renseignée pour le partage des photos n'est pas valide", Toast.LENGTH_SHORT);
					toast.show();
				} catch (IOException e) {
					e.printStackTrace();
				}
						
			}			
		});
	}

	/**
	 * Permet de remplir la liste des evenements
	 * @param array
	 */
	public void populateList(JSONArray array){
		for(int i = 0; i < array.length(); i++){
			Event e = new Event();
			try {
				e.setId(array.getJSONArray(i).optLong(0));
				e.setLocation(ajouterEspace(array.getJSONArray(i).optString(1)));
				e.setDate(deleteHour(array.getJSONArray(i).optString(2)));
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
	public void show(){
		//mise a jour de la liste d'evenements
		mAdapter.setEventList(mListEvent);
		//notify l'adapteur
		mAdapter.notifyDataSetChanged();
	}

	public void removeItemOfList(int i) {}

	public String ajouterEspace(String s){
		String res;
		res = s.replace("_", " ");
		return res;
	}

	public String deleteHour(String s){
		String res;

		int i = s.indexOf(" ");
		res = s.substring(0, i);
		return res;
	}

}
