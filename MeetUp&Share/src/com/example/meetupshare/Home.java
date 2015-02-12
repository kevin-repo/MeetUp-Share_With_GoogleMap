package com.example.meetupshare;


import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.models.User;
import com.example.webservice.Webservice;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

public class Home extends MainActivity {

	User currentUser;
	private LinearLayout mLayoutInvitation, mLayoutInvitationFriends, mLayoutInvitationEvents;
	private TextView mCptFriendRequests, mCptEventRequests, mIdNextEvent, mTitleNextEvent, mDateNextEvent, mPlaceNextEvent, mNullNextEvent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);

		mLayoutInvitation = (LinearLayout) findViewById(R.id.invitation_layout);
		mLayoutInvitationFriends = (LinearLayout) findViewById(R.id.invitation_friend_layout);
		mLayoutInvitationEvents = (LinearLayout) findViewById(R.id.invitation_event_layout);
		mCptFriendRequests = (TextView) findViewById(R.id.friend_counter);
		mCptEventRequests = (TextView) findViewById(R.id.event_counter);
		mIdNextEvent = (TextView) findViewById(R.id.id_next_event);
		mTitleNextEvent = (TextView) findViewById(R.id.titre_next_event);
		mDateNextEvent = (TextView) findViewById(R.id.date_next_event);
		mPlaceNextEvent = (TextView) findViewById(R.id.lieu_next_event);
		mNullNextEvent = (TextView) findViewById(R.id.null_next_event);

		//Recuperation des informations relatives a l'user
		if(getIntent() != null) {
			currentUser = (User)getIntent().getExtras().get("currentUser");
		}
		//recuperation du prochain evenement
		getNextEvent();		
		//recuperation du nombre de demandes d'amis
		getCountFriendRequests();
		//recuperation du nombre d'invitations a des events
		getCountEventRequests();

	}
	
	@Override
	protected void onResume() {
		super.onResume();
		this.onCreate(null);
	}

	public void contacts(View view){	 
		Intent intent = new Intent(Home.this, Contacts.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable("currentUser", currentUser);
		intent.putExtras(bundle);
		startActivity(intent);
	}

	public void calandar(View view){	 
		Intent intent = new Intent(Home.this, Calandar.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable("currentUser", currentUser);
		intent.putExtras(bundle);
		startActivity(intent);
	}

	public void nouvel_evenement(View view){	 
		Intent intent = new Intent(Home.this, Nouvel_evenement.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable("currentUser", currentUser);
		intent.putExtras(bundle);
		startActivity(intent);
	}
	
	public void optionCompte(View view){	 
		Intent intent = new Intent(Home.this, Options.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable("currentUser", currentUser);
		intent.putExtras(bundle);
		startActivity(intent);
	}


	/**
	 * Redirection vers activity "FriendRequest"
	 * @param view
	 */
	public void friendRequests (View view){
		Intent intent = new Intent(Home.this, FriendRequest.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable("currentUser", currentUser);
		intent.putExtras(bundle);
		startActivity(intent);
	}

	/**
	 * Redirection vers activity "Calendar"
	 * @param view
	 */
	public void eventRequests (View view){
		Intent intent = new Intent(Home.this, Calandar.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable("currentUser", currentUser);
		intent.putExtras(bundle);
		startActivity(intent);
	}
	
	public void seePictures (View view){
		Intent intent = new Intent(Home.this, SeePics.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable("currentUser", currentUser);
		intent.putExtras(bundle);
		startActivity(intent);
	}
	
	
	/**
	 * Permet de récupérer les demandes d'amis
	 */
	public void getCountFriendRequests(){
		String url = "users.php?method=friendrequestcount&idcurrent="+currentUser.getId();
		Webservice.get(url, null, new JsonHttpResponseHandler(){
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				Log.d("friend_request", "success");

				try {
					if(response.getInt("count") > 0){
						mLayoutInvitation.setVisibility(View.VISIBLE); //affichage du layout
						mLayoutInvitationFriends.setVisibility(View.VISIBLE);
						mCptFriendRequests.setText(Integer.toString(response.getInt("count"))); //affichage nombre de demandes d'amis
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			public void onFailure(int statusCode, Header[] headers, String s, Throwable e) {
				Log.d("friend_request", "failure");
			}
		});
	}

	/**
	 * Permet de récupérer les invitations non confirmées a des evenements
	 */
	public void getCountEventRequests(){
		String url = "events.php?method=eventrequestcount&idu="+currentUser.getId();
		Webservice.get(url, null, new JsonHttpResponseHandler(){
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				Log.d("event_request", "success");
				
				try {
					if(response.getInt("count") > 0){
						mLayoutInvitation.setVisibility(View.VISIBLE); //affichage du layout
						mLayoutInvitationEvents.setVisibility(View.VISIBLE);
						mCptEventRequests.setText(Integer.toString(response.getInt("count"))); //affichage nombre de demandes d'amis
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			public void onFailure(int statusCode, Header[] headers, String s, Throwable e) {
				Log.d("event_request", "failure");
			}
		});
	}
	
	/**
	 * Permet de récupérer les informations sur le prochain evenement
	 */
	public void getNextEvent(){
		//TODO recuperer prochain evenement non ulterieur a la date du jour (modifier requete)
		String url = "events.php?method=nextevent&idu="+currentUser.getId();
		Webservice.get(url, null, new JsonHttpResponseHandler(){
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				Log.d("next_event", "success");
				mIdNextEvent.setText(response.optString("id"));
				mTitleNextEvent.setText(ajouterEspace(response.optString("title")));
				mDateNextEvent.setText(response.optString("date"));
				mPlaceNextEvent.setText(ajouterEspace(response.optString("place")));
				if(mIdNextEvent.getText().toString().equals("null")){
					//aucun prochain evenement
					mTitleNextEvent.setVisibility(View.GONE);
					mDateNextEvent.setVisibility(View.GONE);
					mPlaceNextEvent.setVisibility(View.GONE);
					mNullNextEvent.setVisibility(View.VISIBLE);
				}
			}

			public void onFailure(int statusCode, Header[] headers, String s, Throwable e) {
				Log.d("next_event", "failure");
			}
		});
	}

	public String ajouterEspace(String s){
		String res;
		res = s.replace("%", " ");
		return res;
	}

}
