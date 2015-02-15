package com.example.meetupshare;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.webservice.Webservice;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;


/**
 * Inscription d'un nouvel utilisateur
 *
 */
public class SignIn extends Activity {

	private EditText mNom, mPrenom, mEmail, mPassword;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.signin);

		mNom = (EditText) findViewById(R.id.nomInscription);
		mPrenom = (EditText) findViewById(R.id.prenomInscription);
		mEmail = (EditText) findViewById(R.id.emailInscription);
		mPassword = (EditText) findViewById(R.id.passwordInscription);
	}

	/**
	 * Validation de l'inscription d'un nouvel utilisateur
	 * @param view
	 */
	public void validateInscription(View view) {
		RequestParams params = new RequestParams();
		params.put("lname", mNom.getText().toString());
		params.put("fname", mPrenom.getText().toString());
		params.put("email", mEmail.getText().toString());
		params.put("pwd", mPassword.getText().toString());

		Log.d("params", params.toString());

		String url = "users.php?method=createuser&lname="+mNom.getText().toString()+"&fname="+mPrenom.getText().toString()+"&email="+mEmail.getText().toString()+"&pwd="+mPassword.getText().toString();
		Webservice.post(url, null, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] response) {
				Log.d("create_account", "success");
				Toast toast = Toast.makeText(getApplicationContext(), "Création réussie", Toast.LENGTH_SHORT);
				toast.show();
				//passage a activity "Connexion"
				Intent intent = new Intent(SignIn.this, Connexion.class);
				startActivity(intent);
				finish();
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
				Log.d("create_account", "failure");
				Toast toast = Toast.makeText(getApplicationContext(), "Echec de la création", Toast.LENGTH_SHORT);
				toast.show();
			}
		});
	}

}