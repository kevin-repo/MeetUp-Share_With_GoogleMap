package com.example.meetupshare;


import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Inscription d'un nouvel utilisateur
 * @author Romain
 *
 */
public class SignIn extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.signin);
		
		final EditText nom = (EditText) findViewById(R.id.nomInscription);
		final EditText prenom = (EditText) findViewById(R.id.prenomInscription);
		final EditText email = (EditText) findViewById(R.id.emailInscription);
		//final EditText password = (EditText) findViewById(R.id.passwordInscription);
		final Button validerBtn = (Button) findViewById(R.id.submitInscription);

		validerBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {					
				RequestParams params = new RequestParams();
				/*params.put("lname", nom.getText().toString());
				params.put("fname", prenom.getText().toString());
				params.put("email", email.getText().toString());*/
				params.add("lname", nom.getText().toString());
				params.add("fname", prenom.getText().toString());
				params.add("email", email.getText().toString());
				//params.put("pwd", password.getText().toString());
				
				Log.d("params", params.toString());
				
				//PROBLEME WEB SERVICE
				/*Webservice.post("?method=createuser", params, new JsonHttpResponseHandler(){
					public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
						Log.d("json", response.toString());		
					}
					
					public void onFailure(int statusCode, Header[] headers, String s, Throwable e) {
						Toast toast = Toast.makeText(getApplicationContext(), "Identifiants incorrects", Toast.LENGTH_SHORT);
						toast.show();
					}

				});*/
			}

		});

	}


}
