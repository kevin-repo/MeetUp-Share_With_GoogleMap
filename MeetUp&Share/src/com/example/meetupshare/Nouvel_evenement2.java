package com.example.meetupshare;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Nouvel_evenement2 extends Activity {
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nouvel_evenement2);
    }
	
	public void home(View view){
		 
		 Intent intent = new Intent(Nouvel_evenement2.this, Home.class);
		 startActivity(intent);
	 }

}
