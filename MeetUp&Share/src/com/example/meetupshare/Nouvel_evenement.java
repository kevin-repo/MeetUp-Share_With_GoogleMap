package com.example.meetupshare;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.example.models.Event;
import com.example.models.User;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

/**
 * Creation d'un nouvel evenement
 *
 */
public class Nouvel_evenement extends MainActivity {

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
		int mois = date.getMonth()+1;
		int annee = date.getYear();
		String dateFormatee = annee + "-" + mois + "-" + jour;
		
		//Traitement pour la recuperation de l'heure au format string
		//TODO Regler le probleme d'implementation de l'heure avec la base de donnees
//		int minute = heure.getCurrentMinute();
//		int hour = heure.getCurrentHour();
//		String heureFormatee = hour + ":" + minute;
			
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date dateTime = null;
        try {
            dateTime = simpleDateFormat.parse(dateFormatee);
        }catch (ParseException ex) {
            System.out.println("Exception "+ex);
        }
        dateTime.setHours(1);
        dateTime.setMinutes(0);
        dateTime.setSeconds(0);
        
		//Recuperation de la date du jour
        Date dateTimeToday = new Date();
		dateTimeToday.setHours(0);
		dateTimeToday.setMinutes(0);
		dateTimeToday.setSeconds(0);
		
		//Test sur la validite de la date (date de l'evenement inferieur a la date du jour)
		if(dateTimeToday.compareTo(dateTime) > 0){
			Toast toast = Toast.makeText(getApplicationContext(), "Veuillez saisir une date postérieur à la date du jour", Toast.LENGTH_SHORT);
			toast.show();
		} else if(titre.getText().toString().equals("")){
			Toast toast = Toast.makeText(getApplicationContext(), "Veuillez saisir un titre", Toast.LENGTH_SHORT);
			toast.show();
		} else {
			Event evenement = new Event();
			evenement.setTitre(titre.getText().toString());
			evenement.setDate(dateFormatee);
			//evenement.setHeure(heureFormatee);

			Intent intent = new Intent(Nouvel_evenement.this, Nouvel_evenement2.class);	
			Bundle bundle = new Bundle();
			bundle.putSerializable("newEvent", evenement);
			bundle.putSerializable("currentUser", (User)getIntent().getExtras().get("currentUser"));
			intent.putExtras(bundle);
			startActivity(intent);
			finish();
		}
	}

}

