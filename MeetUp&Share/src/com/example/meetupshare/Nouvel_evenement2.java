package com.example.meetupshare;

import com.example.models.Event;
import com.example.models.User;
import com.example.webservice.Webservice;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.Header;
import org.json.JSONObject;

public class Nouvel_evenement2 extends Activity {

	private Event evenement;
	private User currentUser;
	private EditText adresse;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nouvel_evenement2);
		//Recuperation des informations stockees dans l'intent
		evenement = (Event) getIntent().getExtras().get("newEvent");
		currentUser = (User) getIntent().getExtras().get("currentUser");
		//Recuperation des informations saisies
		adresse = (EditText) findViewById(R.id.adresse);
	}

	/**
	 * Methode permettant la creation d'un event
	 * @param view
	 */
	public void creerEvenement(View view){	
		//TODO -> ajouter amis saisis + description + date + heure 
		RequestParams params = new RequestParams();
		params.add("title", evenement.getTitre());
		params.add("organizer", Integer.toString(1));
		//params.add("date", evenement.getDate());
		//params.add("heure", evenement.getHeure());
		params.add("place", adresse.getText().toString());
		//params.add("description", adresse.getText().toString());
		Log.d("params evenement", params.toString());

		//Controle des donnees du formulaire
		//TODO Controle des donnees entrees
		String url = "events.php?method=createevent&title="+evenement.getTitre()+"&organizer="+currentUser.getId()+"&place="+adresse.getText().toString();
		Webservice.post(url, null, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] response) {
				// called when response HTTP status is "200 OK"
				Log.d("Create event", "success");
				Toast toast = Toast.makeText(getApplicationContext(), "Evénement créé" , Toast.LENGTH_SHORT);
				toast.show();

				//Redirection vers la page principale
				Intent intent = new Intent(Nouvel_evenement2.this, Home.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("currentUser", currentUser);
				intent.putExtras(bundle);
				startActivity(intent);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
				// called when response HTTP status is "4XX" (eg. 401, 403, 404)
				Log.d("Create event", "failure");
				Toast toast = Toast.makeText(getApplicationContext(), "Echec de la création", Toast.LENGTH_SHORT);
				toast.show();
			}
		});
	}
}

