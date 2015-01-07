package com.example.meetupshare;

import java.io.Serializable;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.models.User;
import com.example.webservice.Webservice;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

public class Connexion extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.connexion);

		final Button connexionBtn = (Button) findViewById(R.id.connexion);
		connexionBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Toast toast = Toast.makeText(getApplicationContext(), "Click connexion", Toast.LENGTH_SHORT);
				toast.show();
				//récupération des informations entrées par l'user
				final EditText email = (EditText) findViewById(R.id.editText1);
				final EditText password = (EditText) findViewById(R.id.editText2);

				RequestParams params = new RequestParams();
				params.put("email", email.getText().toString());
				params.put("password", password.getText().toString());
				
				Log.d("params", params.toString());
				
				Webservice.get("?method=connexionuser", params, new JsonHttpResponseHandler(){
					public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
						Log.d("json", response.toString());
						Toast toast = Toast.makeText(getApplicationContext(), "connexion réussi", Toast.LENGTH_SHORT);
						toast.show();

						User user = new User();
						//user.setId(Long.parseLong(response.optString("id")));
						user.setEmail(response.optString("email"));
						user.setLastname(response.optString("lname"));
						user.setFirstname(response.optString("fname"));
						
						Intent intent = new Intent(Connexion.this, Home.class);
						Bundle bundle = new Bundle();
						bundle.putSerializable("user", user);
						intent.putExtras(bundle);
						startActivity(intent);
					}
					
					public void onFailure(int statusCode, Header[] headers, String s, Throwable e) {
						Toast toast = Toast.makeText(getApplicationContext(), "Identifiants incorrects", Toast.LENGTH_SHORT);
						toast.show();
					}

				});
			}
		});
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
