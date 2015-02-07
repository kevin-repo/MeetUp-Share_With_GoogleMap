package com.example.meetupshare;

import java.util.ArrayList;

import com.example.meetupshare.adapters.EventAdapter;
import com.example.models.Event;
import com.example.models.User;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class Calandar extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.calandar);

		final ArrayList<Event> listEvent = Event.generateListOfEvent();
		EventAdapter adapter = new EventAdapter(listEvent, this);
		final ListView list = (ListView)findViewById(R.id.listEvent);

		list.setAdapter(adapter);

		//Ecouteur d'événement sur la liste des event
		list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {		
				Toast toast = Toast.makeText(getApplicationContext(), listEvent.get(position).getTitre(), Toast.LENGTH_SHORT);
				toast.show();
				
				Event currentEvent = new Event();
				currentEvent.setTitre(listEvent.get(position).getTitre());
				currentEvent.setDate(listEvent.get(position).getDate());
				currentEvent.setHeure(listEvent.get(position).getHeure());
				
				//Passage à l'activity Evenement
				Intent intent = new Intent(Calandar.this, Evenement.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("currentEvent", currentEvent);
				bundle.putSerializable("currentUser", (User)getIntent().getExtras().get("currentUser"));
				intent.putExtras(bundle);
				startActivity(intent);			
			} 
		});
	}
}

