package com.example.meetupshare;


import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.models.User;
import com.example.webservice.Webservice;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * Fenetre d'accueil de l'application
 *
 */
public class Home extends MainActivity {

	private User mCurrentUser;
	private LinearLayout mLayoutInvitation, mLayoutInvitationFriends, mLayoutInvitationEvents, mDateLayout, mPlaceLayout;
	private TextView mCptFriendRequests, mCptEventRequests, mIdNextEvent, mTitleNextEvent, mDateNextEvent, mPlaceNextEvent, mNullNextEvent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);

		mLayoutInvitation = (LinearLayout) findViewById(R.id.invitation_layout);
		mLayoutInvitationFriends = (LinearLayout) findViewById(R.id.invitation_friend_layout);
		mLayoutInvitationEvents = (LinearLayout) findViewById(R.id.invitation_event_layout);
		mDateLayout = (LinearLayout) findViewById(R.id.date_nex_event_layout);
		mPlaceLayout = (LinearLayout) findViewById(R.id.lieu_next_event_layout);
		mCptFriendRequests = (TextView) findViewById(R.id.friend_counter);
		mCptEventRequests = (TextView) findViewById(R.id.event_counter);
		mIdNextEvent = (TextView) findViewById(R.id.id_next_event);
		mTitleNextEvent = (TextView) findViewById(R.id.titre_next_event);
		mDateNextEvent = (TextView) findViewById(R.id.date_next_event);
		mPlaceNextEvent = (TextView) findViewById(R.id.lieu_next_event);
		mNullNextEvent = (TextView) findViewById(R.id.null_next_event);

		//Recuperation des informations relatives a l'user
		if(getIntent() != null) {
			mCurrentUser = (User)getIntent().getExtras().get("currentUser");
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

	/**
	 * Redirection vers activity "Contacts"
	 * @param view
	 */
	public void contacts(View view){	 
		Intent intent = new Intent(Home.this, Contacts.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable("currentUser", mCurrentUser);
		intent.putExtras(bundle);
		startActivity(intent);
	}

	/**
	 * Redirection vers activity "Calandar"
	 * @param view
	 */
	public void calandar(View view){	 
		Intent intent = new Intent(Home.this, Calandar.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable("currentUser", mCurrentUser);
		intent.putExtras(bundle);
		startActivity(intent);
	}

	/**
	 * Redirection vers activity "Nouvel_evenement"
	 * @param view
	 */
	public void nouvel_evenement(View view){	 
		Intent intent = new Intent(Home.this, Nouvel_evenement.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable("currentUser", mCurrentUser);
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
		bundle.putSerializable("currentUser", mCurrentUser);
		intent.putExtras(bundle);
		startActivity(intent);
	}

	/**
	 * Redirection vers activity "EventRequest"
	 * @param view
	 */
	public void eventRequests (View view){
		Intent intent = new Intent(Home.this, EventRequest.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable("currentUser", mCurrentUser);
		intent.putExtras(bundle);
		startActivity(intent);
	}

	/**
	 * Redirection vers activity "SeePics"
	 * @param view
	 */
	public void seePictures (View view){
		Intent intent = new Intent(Home.this, SeePics.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable("currentUser", mCurrentUser);
		intent.putExtras(bundle);
		startActivity(intent);
	}

	/**
	 * Permet de récupérer les demandes d'amis
	 */
	public void getCountFriendRequests(){
		RequestParams params = new RequestParams();
		params.put("idcurrent", mCurrentUser.getId());
		
		String file = Webservice.usersMethod();
	
		Webservice.get(file+"?method=friendrequestcount", params, new JsonHttpResponseHandler(){
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
		RequestParams params = new RequestParams();
		params.put("idu", mCurrentUser.getId());
		
		String file = Webservice.eventsMethod();
		
		Webservice.get(file+"?method=eventrequestcount", params, new JsonHttpResponseHandler(){
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
		RequestParams params = new RequestParams();
		params.put("idu", mCurrentUser.getId());
		
		String file = Webservice.eventsMethod();
		
		Webservice.get(file+"?method=nextevent", params, new JsonHttpResponseHandler(){
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				Log.d("next_event", "success");
				mIdNextEvent.setText(response.optString("id"));
				if(!mIdNextEvent.getText().toString().equals("null")){
					mTitleNextEvent.setText(ajouterEspace(response.optString("title")));
					mDateNextEvent.setText(deleteHour(response.optString("date")));
					mPlaceNextEvent.setText(ajouterEspace(response.optString("place")));
				}else{
					//aucun prochain evenement
					mTitleNextEvent.setVisibility(View.GONE);
					mDateLayout.setVisibility(View.GONE);
					mPlaceLayout.setVisibility(View.GONE);
					mNullNextEvent.setVisibility(View.VISIBLE);
				}
			}

			public void onFailure(int statusCode, Header[] headers, String s, Throwable e) {
				Log.d("next_event", "failure");
				mTitleNextEvent.setVisibility(View.GONE);
				mDateLayout.setVisibility(View.GONE);
				mPlaceLayout.setVisibility(View.GONE);
				mNullNextEvent.setVisibility(View.VISIBLE);
			}
		});
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
