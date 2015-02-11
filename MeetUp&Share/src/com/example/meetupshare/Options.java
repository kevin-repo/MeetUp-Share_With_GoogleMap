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


public class Options extends Activity {
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.options, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
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
