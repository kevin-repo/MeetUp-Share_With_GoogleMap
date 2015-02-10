package com.example.meetupshare;


import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.models.User;
import com.example.webservice.Webservice;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class Home extends Activity {

	User currentUser;
	private LinearLayout mLayoutInvitationFriends;
	private TextView mCptFriendRequests;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);

		mLayoutInvitationFriends = (LinearLayout) findViewById(R.id.invitation_friend_layout);
		mCptFriendRequests = (TextView) findViewById(R.id.friend_counter);
		//Recuperation des informations relatives a l'user
		if(getIntent() != null) {
			currentUser = (User)getIntent().getExtras().get("currentUser");
		}
		//recuperation du nombre de demandes d'amis
		getCountFriendRequests(); 

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
	 * Permet de récupérer les demandes d'amis
	 */
	public void getCountFriendRequests(){
		String url = "users.php?method=friendrequestcount&idcurrent="+currentUser.getId();
		Webservice.get(url, null, new JsonHttpResponseHandler(){
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				Log.d("friend_request", "success");

				try {
					if(response.getInt("count") > 0){
						mLayoutInvitationFriends.setVisibility(View.VISIBLE); //affichage du layout
						mCptFriendRequests.setText(Integer.toString(response.getInt("count"))); //affichage nombre de demandes d'amis
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			public void onFailure(int statusCode, Header[] headers, String s, Throwable e) {
				Log.d("friend_request", "failure");
			}
		});
	}

	/**
	 * Permet de récupérer les informations sur le prochain evenement
	 */
	public void getNextEvent(){
		
	}
	
	/**
	 * Suppression du compte de l'utilisateur 
	 * @param view
	 */
	public void supprimerCompte(View view){
		String url = "users.php?method=deleteuser&email="+currentUser.getEmail();
		Webservice.delete(url, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				Log.d("Delete account", "success");
				Toast toast = Toast.makeText(getApplicationContext(), "Compte supprimé", Toast.LENGTH_SHORT);
				toast.show();
				//passage a activity "Connexion"
				Intent intent = new Intent(getApplicationContext(),Connexion.class);	 
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 	 
				startActivity(intent);
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				Log.d("Delete account", "failure");
				Toast toast = Toast.makeText(getApplicationContext(), "Echec de la suppression", Toast.LENGTH_SHORT);
				toast.show();

			}			
		});
	}

}
