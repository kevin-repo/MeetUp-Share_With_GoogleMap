package com.example.meetupshare;

import com.example.models.Event;
import com.example.models.User;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

public class Nouvel_evenement extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nouvel_evenement);
	}

	public void nouvel_evenement2(View view){
		//Recuperation des informations de l'evenement
		final EditText titre = (EditText) findViewById(R.id.titre);
		final DatePicker date = (DatePicker) findViewById(R.id.dateEvenement);
		final TimePicker heure = (TimePicker) findViewById(R.id.HeureEvenement);
		
		//Traitement pour la recuperation d'une date au format string
		int jour = date.getDayOfMonth();
		int mois = date.getMonth(); //TO DO -> pb récupération numéro du mois
		int annee = date.getYear();
		String dateFormatee = jour + "/" + mois + "/" + annee;
		
		//Traitement pour la recuperation de l'heure au format string
		int minute = heure.getCurrentMinute();
		int hour = heure.getCurrentHour();
		String heureFormatee = hour + ":" + minute;
		
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
