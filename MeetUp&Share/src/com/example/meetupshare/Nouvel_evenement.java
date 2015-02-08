package com.example.meetupshare;

import com.example.models.Event;
import com.example.models.User;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

public class Nouvel_evenement extends Activity {

	private EditText titre;
	private DatePicker date;
	private TimePicker heure;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nouvel_evenement);
		//Recuperation des informations de l'evenement
		titre = (EditText) findViewById(R.id.titre);
		date = (DatePicker) findViewById(R.id.dateEvenement);
		heure = (TimePicker) findViewById(R.id.HeureEvenement);
	}

	/**
	 * Transmission des donnees du formulaire a activity "Nouvel_evenement2"
	 * @param view
	 */
	public void nouvel_evenement2(View view){
		//Traitement pour la recuperation d'une date au format string
		int jour = date.getDayOfMonth();
		int mois = date.getMonth(); //TO DO -> pb récupération numéro du mois
		int annee = date.getYear();
		String dateFormatee = jour + "/" + mois + "/" + annee;

		//Traitement pour la recuperation de l'heure au format string
		int minute = heure.getCurrentMinute();
		int hour = heure.getCurrentHour();
		String heureFormatee = hour + ":" + minute;

		//Controle des donnees du formulaire
		//TODO Controle des donnees entrees

		Event evenement = new Event();
		evenement.setTitre(titre.getText().toString());
		evenement.setDate(dateFormatee);
		evenement.setHeure(heureFormatee);

		Intent intent = new Intent(Nouvel_evenement.this, Nouvel_evenement2.class);	
		Bundle bundle = new Bundle();
		bundle.putSerializable("newEvent", evenement);
		bundle.putSerializable("currentUser", (User)getIntent().getExtras().get("currentUser"));
		intent.putExtras(bundle);
		startActivity(intent);
	}
}