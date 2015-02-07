package com.example.meetupshare;


import com.example.models.User;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class Home extends Activity {

	User currentUser;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
		//Recuperation des informations relatives a l'user
		if(getIntent() != null) {
			currentUser = (User)getIntent().getExtras().get("currentUser");
		}
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
	 * Suppression du compte de l'utilisateur 
	 * @param view
	 */
	public void supprimerCompte(View view){
		RequestParams params = new RequestParams();
		params.put("email", currentUser.getEmail());

		Log.d("params", params.toString());

		/*Webservice.post("?method=deleteuser", params, new JsonHttpResponseHandler(){
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				Log.d("json", response.toString());
				Toast toast = Toast.makeText(getApplicationContext(), "Compte supprimé", Toast.LENGTH_SHORT);
				toast.show();
				//Redirection vers la page d'accueil
				Intent intent = new Intent(Home.this, Connexion.class);
				startActivity(intent);
			}			
			public void onFailure(int statusCode, Header[] headers, String s, Throwable e) {
				Toast toast = Toast.makeText(getApplicationContext(), "Suppression impossible", Toast.LENGTH_SHORT);
				toast.show();
			}
		});*/
	}

}
