package com.example.meetupshare;

import org.apache.http.Header;
import org.json.JSONObject;

import com.example.models.Event;
import com.example.models.User;
import com.example.webservice.Webservice;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Nouvel_evenement2 extends Activity {

	Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nouvel_evenement2);
		intent = getIntent();      
	}

	public void creerEvenement(View view){
		//Recuperation des informations relative a l'evenement
		Event evenement = (Event) getIntent().getExtras().get("newEvent");
		//Recuperation des informations saisies
		final EditText adresse = (EditText) findViewById(R.id.adresse);
		final EditText description = (EditText) findViewById(R.id.description);
		//TO DO -> ajouter amis saisis 
		
		RequestParams params = new RequestParams();
		params.add("title", evenement.getTitre());
		params.add("organizer", Integer.toString(1));// ???? A MODIFIER
		//params.add("date", evenement.getDate());
		//params.add("heure", evenement.getHeure());
		params.add("place", adresse.getText().toString());
		//params.add("description", adresse.getText().toString());
		
		Log.d("params evenement", params.toString());
		
		//PROBLEME WEB SERVICE
		/*Webservice.post("?method=createevent", params, new JsonHttpResponseHandler(){
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				Log.d("json evenement", response.toString());
				Toast toast = Toast.makeText(getApplicationContext(), "Evénement créé" , Toast.LENGTH_SHORT);
				toast.show();
				
				//Redirection vers la page principale
				Intent intent = new Intent(Nouvel_evenement2.this, Home.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("currentUser", (User) intent.getExtras().get("currentUser"));
				intent.putExtras(bundle);
				startActivity(intent);
			}
			
			public void onFailure(int statusCode, Header[] headers, String s, Throwable e) {
				Toast toast = Toast.makeText(getApplicationContext(), "Création de l'événement impossible ", Toast.LENGTH_SHORT);
				toast.show();
			}

		});*/ 
	}

}
