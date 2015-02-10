package com.example.meetupshare;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.webservice.Webservice;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;


/**
 * Inscription d'un nouvel utilisateur
 * @author Romain
 *
 */
public class SignIn extends Activity {

	private EditText nom, prenom, email, password;
	private Button validerBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.signin);

		nom = (EditText) findViewById(R.id.nomInscription);
		prenom = (EditText) findViewById(R.id.prenomInscription);
		email = (EditText) findViewById(R.id.emailInscription);
		password = (EditText) findViewById(R.id.passwordInscription);
		validerBtn = (Button) findViewById(R.id.submitInscription);
	}

	/**
	 * Validation de l'inscription d'un nouvel utilisateur
	 * @param view
	 */
	public void validateInscription(View view) {
		validerBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				RequestParams params = new RequestParams();
				params.put("lname", nom.getText().toString());
				params.put("fname", prenom.getText().toString());
				params.put("email", email.getText().toString());
				params.put("pwd", password.getText().toString());

				Log.d("params", params.toString());

				String url = "users.php?method=createuser&lname="+nom.getText().toString()+"&fname="+prenom.getText().toString()+"&email="+email.getText().toString()+"&pwd="+password.getText().toString();
				Webservice.post(url, null, new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, Header[] headers, byte[] response) {
						// called when response HTTP status is "200 OK"
						Log.d("Create account", "success");
						Toast toast = Toast.makeText(getApplicationContext(), "Création réussie", Toast.LENGTH_SHORT);
						toast.show();
						//passage a activity "Connexion"
						Intent intent = new Intent(SignIn.this, Connexion.class);
						startActivity(intent);
						finish();
					}

					@Override
					public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
						// called when response HTTP status is "4XX" (eg. 401, 403, 404)
						Log.d("Create account", "failure");
						Toast toast = Toast.makeText(getApplicationContext(), "Echec de la création", Toast.LENGTH_SHORT);
						toast.show();
					}
				});
			}

		});

	}


}