package com.example.meetupshare;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Nouvel_evenement extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nouvel_evenement);
    }
    
    public void nouvel_evenement2(View view){
		 
		 Intent intent = new Intent(Nouvel_evenement.this, Nouvel_evenement2.class);
		 startActivity(intent);
	 }
}
