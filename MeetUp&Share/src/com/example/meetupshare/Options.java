package com.example.meetupshare;

import org.apache.http.Header;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.models.User;
import com.example.webservice.Webservice;
import com.loopj.android.http.AsyncHttpResponseHandler;


public class Options extends MainActivity {
	User currentUser;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.options);
		
		//Recuperation des informations relatives a l'user
		if(getIntent() != null) {
			currentUser = (User)getIntent().getExtras().get("currentUser");
		}
	}


	/**
	 * Suppression du compte de l'utilisateur 
	 * @param view
	 */
	public void supprimerCompte(View view){
		String url = "users.php?method=deleteuser&email="+currentUser.getEmail();
		Webservice.delete(url, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				Log.d("Delete account", "success");
				Toast toast = Toast.makeText(getApplicationContext(), "Compte supprimé", Toast.LENGTH_SHORT);
				toast.show();
				//passage a activity "Connexion"
				Intent intent = new Intent(getApplicationContext(),Connexion.class);	 
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 	 
				startActivity(intent);
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				Log.d("Delete account", "failure");
				Toast toast = Toast.makeText(getApplicationContext(), "Echec de la suppression", Toast.LENGTH_SHORT);
				toast.show();

			}			
		});
	}
}
