package com.example.meetupshare;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Home extends Activity {
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
    }
	
	public void contacts(View view){
		 
		 Intent intent = new Intent(Home.this, Contacts.class);
		 startActivity(intent);
	 }
	
	public void calandar(View view){
		 
		 Intent intent = new Intent(Home.this, Calandar.class);
		 startActivity(intent);
	 }
	
	public void nouvel_evenement(View view){
		 
		 Intent intent = new Intent(Home.this, Nouvel_evenement.class);
		 startActivity(intent);
	 }
	
}
