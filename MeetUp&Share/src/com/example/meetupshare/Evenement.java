package com.example.meetupshare;

import com.example.models.Event;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class Evenement extends Activity {
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.evenement);
        
        TextView date = (TextView) findViewById(R.id.date_event_evenement_layout);
        TextView heure = (TextView) findViewById(R.id.heure_event_evenement_layout);
        TextView titre = (TextView) findViewById(R.id.titre_event_evenement_layout);
        
        Event currentEvent = (Event) getIntent().getExtras().get("currentEvent");
        
        date.setText(currentEvent.getDate());
        heure.setText(currentEvent.getHeure());
        titre.setText(currentEvent.getTitre());
    }
		
}
