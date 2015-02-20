package com.example.meetupshare;

import org.apache.http.Header;
import org.json.JSONObject;

import com.example.models.Event;
import com.example.webservice.Webservice;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;



public class AlterEvent extends MainActivity{

	private EditText mTitle, mLocation, mLink, mDescription;
	private Event mCurrentEvent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alter_event);

		mTitle = (EditText)findViewById(R.id.alter_title_event);
		mLocation = (EditText)findViewById(R.id.alter_location_event);
		mLink = (EditText)findViewById(R.id.alter_link_event);
		mDescription = (EditText)findViewById(R.id.alter_description_event);

		mCurrentEvent = (Event) getIntent().getExtras().get("currentEvent");

		init();
	}

	/**
	 * Initialisation des EditText
	 */
	private void init() {
		String file = Webservice.eventsMethod();
		String url = file+"?method=readcurrent&id="+mCurrentEvent.getId();
		Webservice.get(url, null, new JsonHttpResponseHandler(){
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				Log.d("read_event", "success");
				mTitle.setText(ajouterEspace(response.optString("title")));
				mLocation.setText(ajouterEspace(response.optString("place")));
				mLink.setText(response.optString("photo_link"));
				mDescription.setText(ajouterEspace(response.optString("description")));
			}

			public void onFailure(int statusCode, Header[] headers, String s, Throwable e) {
				Log.d("read_event", "failure");
				Toast toast = Toast.makeText(getApplicationContext(), "Lecture de l'événement impossible", Toast.LENGTH_SHORT);
				toast.show();
			}
		});	

	}

	/**
	 * Modifier le nom de l'événement
	 * @param view
	 */
	public void alterTitle(View view){
		String file = Webservice.eventsMethod();
		String url = file+"?method=updatetitle&event="+mCurrentEvent.getId()+"&title="+supprimerEspace(mTitle.getText().toString());

		Webservice.post(url, null, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				Log.d("alter_title", "success");
				Toast toast = Toast.makeText(getApplicationContext(), "Modification effectuée", Toast.LENGTH_SHORT);
				toast.show();
			}	
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				Log.d("alter_title", "failure");	
				Toast toast = Toast.makeText(getApplicationContext(), "Modification imposible", Toast.LENGTH_SHORT);
				toast.show();
			}
		});
	}

	/**
	 * Modifier l'adresse de l'événement
	 * @param view
	 */
	public void alterLocation(View view){
		String file = Webservice.eventsMethod();
		String url = file+"?method=updateplace&event="+mCurrentEvent.getId()+"&place="+supprimerEspace(mLocation.getText().toString());

		Webservice.post(url, null, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				Log.d("alter_location", "success");
				Toast toast = Toast.makeText(getApplicationContext(), "Modification effectuée", Toast.LENGTH_SHORT);
				toast.show();
			}	
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				Log.d("alter_location", "failure");	
				Toast toast = Toast.makeText(getApplicationContext(), "Modification imposible", Toast.LENGTH_SHORT);
				toast.show();
			}
		});	
	}

	/**
	 * Modifier le lien de partage de photos de l'événement
	 * @param view
	 */
	public void alterLink(View view){
		String file = Webservice.eventsMethod();
		String url = file+"?method=updatelink&event="+mCurrentEvent.getId()+"&link="+supprimerEspace(mLink.getText().toString());

		Webservice.post(url, null, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				Log.d("alter_link", "success");
				Toast toast = Toast.makeText(getApplicationContext(), "Modification effectuée", Toast.LENGTH_SHORT);
				toast.show();
			}	
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				Log.d("alter_link", "failure");	
				Toast toast = Toast.makeText(getApplicationContext(), "Modification imposible", Toast.LENGTH_SHORT);
				toast.show();
			}
		});
	}

	/**
	 * Modifier la description de l'événement
	 * @param view
	 */
	public void alterDescription(View view){
		String file = Webservice.eventsMethod();
		String url = file+"?method=updatedescription&event="+mCurrentEvent.getId()+"&description="+supprimerEspace(mDescription.getText().toString());

		Webservice.post(url, null, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				Log.d("alter_description", "success");
				Toast toast = Toast.makeText(getApplicationContext(), "Modification effectuée", Toast.LENGTH_SHORT);
				toast.show();
			}	
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				Log.d("alter_description", "failure");	
				Toast toast = Toast.makeText(getApplicationContext(), "Modification imposible", Toast.LENGTH_SHORT);
				toast.show();
			}
		});
	}


	private String ajouterEspace(String s){
		String res;
		res = s.replace("_", " ");
		return res;
	}
	
	public String supprimerEspace(String s){
		String res;
		res = s.replace(" ", "_");
		return res;
	}

}
