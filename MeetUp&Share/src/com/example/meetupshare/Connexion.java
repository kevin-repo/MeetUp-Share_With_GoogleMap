package com.example.meetupshare;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Connexion extends Activity{
	 @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.connexion);
	    }
	 /**
	 * Passage sur une autre activity
	 */
	 public void signIn(View view){
	 
		 Intent intent = new Intent(Connexion.this, SignIn.class);
		 startActivity(intent);
	 }
	 
	 public void home(View view){
		 
		 Intent intent = new Intent(Connexion.this, Home.class);
		 startActivity(intent);
	 }
}
