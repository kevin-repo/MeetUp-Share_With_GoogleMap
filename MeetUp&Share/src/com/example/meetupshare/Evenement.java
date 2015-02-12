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
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import android.R.bool;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Evenement extends MainActivity {

	private Event mCurrentEvent;
	private TextView mDate, mHeure, mTitre, mDescription;
	private ArrayList<User> mListParticipant;
	private ParticipantAdapter mAdapter;
	private ListView mList;
	private User mCurrentUser;
	private Button mParticipateEventBtn, mRefuseEventBtn;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.evenement);

		mDate = (TextView) findViewById(R.id.date_event_evenement_layout);
		mHeure = (TextView) findViewById(R.id.heure_event_evenement_layout);
		mTitre = (TextView) findViewById(R.id.titre_event_evenement_layout);
		mParticipateEventBtn = (Button) findViewById(R.id.participate_event_btn);
		mRefuseEventBtn = (Button) findViewById(R.id.refuse_event_btn);
		mDescription = (TextView) findViewById(R.id.description);
		
		mCurrentEvent = (Event) getIntent().getExtras().get("currentEvent");
		mCurrentUser = (User) getIntent().getExtras().get("currentUser");
		
		mListParticipant = new ArrayList<User>();
		mAdapter = new ParticipantAdapter(this, android.R.layout.simple_list_item_multiple_choice, mListParticipant);
		mList = (ListView)findViewById(R.id.liste_participants);

		mList.setAdapter(mAdapter);
		
		//Recuperation des informations de l'evenement
		String url = "events.php?method=readcurrent&id="+mCurrentEvent.getId();
		Webservice.get(url, null, new JsonHttpResponseHandler(){
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				Log.d("read_event", "success");

				mDate.setText(deleteHour(response.optString("date")));
				mHeure.setText(response.optString("hour"));
				mTitre.setText(ajouterEspace(response.optString("title")));
				mDescription.setText(ajouterEspace(response.optString("description")));
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
				Log.d("participant_list", "success");
				populateList(response);
				show();
			}

			public void onFailure(int statusCode, Header[] headers, String s, Throwable e) {
				Log.d("participant_list", "failure");
			}
		});
		
		String url3 = "events.php?method=readparticipation&idu="+mCurrentUser.getId()+"&event="+mCurrentEvent.getId();
		Webservice.get(url3, null, new JsonHttpResponseHandler(){			
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				Log.d("is_participate", "sucess");
				try {
					if(response.getInt("participate") == 1){ //user participe a event
						mParticipateEventBtn.setVisibility(View.GONE);
					}		
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			public void onFailure(int statusCode, Header[] headers, String s, Throwable e) {
				Log.d("is_participate", "failure");
			}
		});

	}


	public void isParticipate(){
		String url2 = "events.php?method=readparticipation&idu="+mCurrentUser.getId()+"&event="+mCurrentEvent.getId();
		Webservice.get(url2, null, new JsonHttpResponseHandler(){			
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				Log.d("is_participate", "sucess");
				try {
					if(response.getInt("participate") == 1){ //user participe a event
						mParticipateEventBtn.setVisibility(View.GONE);
					}		
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			public void onFailure(int statusCode, Header[] headers, String s, Throwable e) {
				Log.d("is_participate", "failure");
			}
		});
	}
	
	/**
	 * Current user participe a l'evenement
	 */
	public void participateEvent(View view){
		String url = "events.php?method=participateevent&idu="+mCurrentUser.getId()+"&event="+mCurrentEvent.getId();
		Webservice.post(url, null, new AsyncHttpResponseHandler() {	
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				Log.d("participate_event", "sucess");
				Toast toast = Toast.makeText(getApplicationContext(), "Vous participez à l'événement" , Toast.LENGTH_SHORT);
				toast.show();
				mParticipateEventBtn.setVisibility(View.GONE);
				User user = new User();
				user.setId(mCurrentUser.getId());
				user.setFirstname(mCurrentUser.getFirstname());
				user.setLastname(mCurrentUser.getLastname());
				mAdapter.add(user);
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				Log.d("participate_event", "failure");		
			}
		});
	}
	
	/**
	 * Current user ne participe pas a l'evenement
	 */
	public void refuseEvent(View view){
		String url = "events.php?method=refuseparticipateevent&idu="+mCurrentUser.getId()+"&event="+mCurrentEvent.getId();
		Webservice.post(url, null, new AsyncHttpResponseHandler() {	
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				Log.d("refuse_event", "sucess");
				Toast toast = Toast.makeText(getApplicationContext(), "Evénement supprimé de la liste" , Toast.LENGTH_SHORT);
				toast.show();
				//Passage à l'activity Calendar
				Intent intent = new Intent(Evenement.this, Calandar.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("currentEvent", mCurrentEvent);
				bundle.putSerializable("currentUser", mCurrentUser);
				intent.putExtras(bundle);
				startActivity(intent);
				finish();
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				Log.d("refuse_event", "failure");		
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

	
	public String ajouterEspace(String s){
		String res;
		res = s.replace("%", " ");
		return res;
	}
	
	public String deleteHour(String s){
		String res;

		int i = s.indexOf(" ");
		res = s.substring(0, i);
		return res;
	}
	
}
