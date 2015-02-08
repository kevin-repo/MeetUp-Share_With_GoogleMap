package com.example.meetupshare;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.models.User;
import com.example.webservice.Webservice;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

/**
 * Connexion d'un utilisateur
 * @author Romain
 *
 */
public class Connexion extends Activity{

	private EditText email, password;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.connexion);
		email = (EditText) findViewById(R.id.editText1);
		password = (EditText) findViewById(R.id.editText2);
	}

	/**
	 * Methode permettant la connexion d'un utilisateur a son compte
	 * @param view
	 */
	public void connectToAccount(View view) {
		RequestParams params = new RequestParams();
		params.put("email", email.getText().toString());
		params.put("pwd", password.getText().toString());

		Log.d("params", params.toString());

		Webservice.get("users.php?method=connexionuser", params, new JsonHttpResponseHandler(){
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				Log.d("connexion_json", response.toString());

				User user = new User();
				user.setId(Long.parseLong(response.optString("id")));
				user.setEmail(response.optString("email"));
				user.setLastname(response.optString("lname"));
				user.setFirstname(response.optString("fname"));

				Intent intent = new Intent(Connexion.this, Home.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("currentUser", user);
				intent.putExtras(bundle);
				startActivity(intent);
			}

			public void onFailure(int statusCode, Header[] headers, String s, Throwable e) {
				Toast toast = Toast.makeText(getApplicationContext(), "Identifiants incorrects", Toast.LENGTH_SHORT);
				toast.show();
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

}
