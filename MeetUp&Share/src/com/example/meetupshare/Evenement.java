package com.example.meetupshare;

import org.apache.http.Header;
import org.json.JSONObject;

import com.example.models.Event;
import com.example.models.User;
import com.example.webservice.Webservice;
import com.loopj.android.http.JsonHttpResponseHandler;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class Evenement extends Activity {
	
	private Event mCurrentEvent;
	private TextView mDate, mHeure, mTitre;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.evenement);
        
        mDate = (TextView) findViewById(R.id.date_event_evenement_layout);
        mHeure = (TextView) findViewById(R.id.heure_event_evenement_layout);
        mTitre = (TextView) findViewById(R.id.titre_event_evenement_layout);
        
        mCurrentEvent = (Event) getIntent().getExtras().get("currentEvent");
        
        //Recuperation des informations de l'evenement
        String url = "events.php?method=readcurrent&id="+mCurrentEvent.getId();
        Webservice.get(url, null, new JsonHttpResponseHandler(){
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				Log.d("read_event", "success");
				
				//TODO Séparer date et heure + afficher organisateur de la soirée
				mDate.setText(response.optString("date"));
				mHeure.setText(response.optString("hh:mm"));
		        mTitre.setText(response.optString("title"));
			}

			public void onFailure(int statusCode, Header[] headers, String s, Throwable e) {
				Log.d("read_event", "failure");
				Toast toast = Toast.makeText(getApplicationContext(), "Lecture de l'événement impossible", Toast.LENGTH_SHORT);
				toast.show();
			}

		});	
        
        
    }
		
}
