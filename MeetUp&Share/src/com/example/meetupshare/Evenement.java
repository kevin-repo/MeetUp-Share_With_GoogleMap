package com.example.meetupshare;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.meetupshare.adapters.FriendAdapter;
import com.example.meetupshare.adapters.ParticipantAdapter;
import com.example.models.Event;
import com.example.models.User;
import com.example.webservice.Webservice;
import com.loopj.android.http.JsonHttpResponseHandler;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Evenement extends Activity {

	private Event mCurrentEvent;
	private TextView mDate, mHeure, mTitre;
	private ArrayList<User> mListParticipant;
	private ParticipantAdapter mAdapter;
	private ListView mList;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.evenement);

		mDate = (TextView) findViewById(R.id.date_event_evenement_layout);
		mHeure = (TextView) findViewById(R.id.heure_event_evenement_layout);
		mTitre = (TextView) findViewById(R.id.titre_event_evenement_layout);

		mCurrentEvent = (Event) getIntent().getExtras().get("currentEvent");

		mListParticipant = new ArrayList<User>();
		mAdapter = new ParticipantAdapter(this, android.R.layout.simple_list_item_multiple_choice, mListParticipant);
		mList = (ListView)findViewById(R.id.liste_participants);

		mList.setAdapter(mAdapter);
		
		//Recuperation des informations de l'evenement
		String url = "events.php?method=readcurrent&id="+mCurrentEvent.getId();
		Webservice.get(url, null, new JsonHttpResponseHandler(){
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				Log.d("read_event", "success");

				//TODO Séparer date et heure + afficher organisateur de la soirée
				mDate.setText(response.optString("date"));
				mHeure.setText(response.optString("hh:mm"));
				mTitre.setText(response.optString("title"));
			}

			public void onFailure(int statusCode, Header[] headers, String s, Throwable e) {
				Log.d("read_event", "failure");
				Toast toast = Toast.makeText(getApplicationContext(), "Lecture de l'événement impossible", Toast.LENGTH_SHORT);
				toast.show();
			}

		});	

		//TODO Ameliorer encodage chaine json de retour + modifier onSuccess
		String url2 = "events.php?method=readeventparticipants&event="+mCurrentEvent.getId();
		Webservice.get(url2, null, new JsonHttpResponseHandler(){			
			//Version 1
			public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
				Log.d("participant_list", "sucess");
				populateList(response);
				show();
			}

			public void onFailure(int statusCode, Header[] headers, String s, Throwable e) {
				Log.d("participant_list", "failure");
			}
		});

	}

	//TODO Mettre dans interface
	/**
	 * Permet de remplir la liste des participants
	 * @param array
	 */
	public void populateList(JSONArray array){
		for(int i = 0; i < array.length(); i++){
			User contact = new User();
			try {
				contact.setId(array.getJSONArray(i).optLong(0));
				contact.setFirstname(array.getJSONArray(i).optString(1));
				contact.setLastname(array.getJSONArray(i).optString(2));
				mListParticipant.add(contact);
			} catch (JSONException e1) {
				e1.printStackTrace();
			}		
		}
	}

	//TODO Mettre dans interface
	/**
	 * Permet d'afficher les participants
	 */
	public void show(){
		//mise a jour de la liste d'amis
		mAdapter.setParticipantList(mListParticipant);
		//notify l'adapteur
		mAdapter.notifyDataSetChanged();
	}

}
