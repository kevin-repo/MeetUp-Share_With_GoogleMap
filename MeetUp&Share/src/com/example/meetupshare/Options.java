package com.example.meetupshare;

import org.apache.http.Header;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.models.User;
import com.example.webservice.Webservice;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * Parametres du compte de l'user
 *
 */
public class Options extends MainActivity {

	private User mCurrentUser;
	private EditText mNom, mPrenom, mPassword;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.options);

		mNom = (EditText) findViewById(R.id.modifnom);
		mPrenom = (EditText) findViewById(R.id.modifprenom);
		mPassword = (EditText) findViewById(R.id.modifmdp);

		//Recuperation des informations relatives a l'user
		if(getIntent() != null) {
			mCurrentUser = (User)getIntent().getExtras().get("currentUser");
		}
	}




	public void modifierNom(View view){
		String url = "users.php?method=updatelname&idcurrent=1&lname="+mNom.getText().toString()+"";

		Webservice.post(url, null, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				Log.d("Modification nom", "Modification effectuée");
				Toast toast = Toast.makeText(getApplicationContext(), "Modification effectuée", Toast.LENGTH_SHORT);
				toast.show();
			}	
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				Log.d("ko", "La modification a échouée");	
				Toast toast = Toast.makeText(getApplicationContext(), "Modification imposible", Toast.LENGTH_SHORT);
				toast.show();
			}
		});
	}

	public void modifierPrenom(View view){
		String url = "users.php?method=updatefname&idcurrent=1&lname="+mPrenom.getText().toString()+"";

		Webservice.post(url, null, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				Log.d("Modification prénom", "Modification effectuée");
				Toast toast = Toast.makeText(getApplicationContext(), "Modification effectuée", Toast.LENGTH_SHORT);
				toast.show();
			}	
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				Log.d("ko", "La modification a échouée");	
				Toast toast = Toast.makeText(getApplicationContext(), "Modification imposible", Toast.LENGTH_SHORT);
				toast.show();
			}
		});

	}

	public void modifierMdp(View view){
		String url = "users.php?method=?method=updatepwd&idcurrent=1&lname="+mPassword.getText().toString()+"";

		Webservice.post(url, null, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				Log.d("Modification m", "Modification effectuée");
				Toast toast = Toast.makeText(getApplicationContext(), "Modification effectuée", Toast.LENGTH_SHORT);
				toast.show();
			}	
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				Log.d("ko", "La modification a échouée");	
				Toast toast = Toast.makeText(getApplicationContext(), "Modification imposible", Toast.LENGTH_SHORT);
				toast.show();
			}
		});

	}

	/**
	 * Suppression du compte de l'utilisateur 
	 * @param view
	 */
	public void supprimerCompte(View view){
		String url = "users.php?method=deleteuser&email="+mCurrentUser.getEmail();
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
