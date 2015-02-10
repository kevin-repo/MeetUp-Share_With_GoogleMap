package com.example.meetupshare;

import java.util.ArrayList;

import com.example.meetupshare.adapters.FriendAdapter;
import com.example.models.Event;
import com.example.models.User;
import com.example.webservice.Webservice;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Nouvel_evenement2 extends Activity {

	private Event mEvenement;
	private User mCurrentUser;
	private EditText mAdresse;
	private ArrayList<User> mListFriend;
	private FriendAdapter mAdapter;
	private ListView mList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nouvel_evenement2);
		mListFriend = new ArrayList<User>();
		mAdapter = new FriendAdapter(this, R.layout.friend_list, mListFriend);
		mList = (ListView)findViewById(R.id.liste_contacts_event);
		//Recuperation des informations stockees dans l'intent
		mEvenement = (Event) getIntent().getExtras().get("newEvent");
		mCurrentUser = (User) getIntent().getExtras().get("currentUser");
		//Recuperation des informations saisies
		mAdresse = (EditText) findViewById(R.id.adresse);

		
		mList.setAdapter(mAdapter);
		
		//TODO Ameliorer encodage chaine json de retour + modifier onSuccess
		String url = "users.php?method=readfriends&idcurrent="+mCurrentUser.getId();
		Webservice.get(url, null, new JsonHttpResponseHandler(){			
			//Version 1
			public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
				Log.d("contact_list_event", "sucess");
				populateListFriends(response);
				showContacts();
			}

			public void onFailure(int statusCode, Header[] headers, String s, Throwable e) {
				Log.d("contact_list_event", "failure");
			}
		});

	}

	/**
	 * Methode permettant la creation d'un event
	 * @param view
	 */
	public void creerEvenement(View view){	
		//TODO -> ajouter amis saisis + description + date + heure 
		RequestParams params = new RequestParams();
		params.add("title", mEvenement.getTitre());
		params.add("organizer", Integer.toString(1));
		//params.add("date", evenement.getDate());
		//params.add("heure", evenement.getHeure());
		params.add("place", mAdresse.getText().toString());
		//params.add("description", adresse.getText().toString());
		Log.d("params evenement", params.toString());

		//Controle des donnees du formulaire
		//TODO Controle des donnees entrees
		String url = "events.php?method=createevent&title="+mEvenement.getTitre()+"&organizer="+mCurrentUser.getId()+"&place="+mAdresse.getText().toString();
		Webservice.post(url, null, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] response) {
				// called when response HTTP status is "200 OK"
				Log.d("Create event", "success");
				Toast toast = Toast.makeText(getApplicationContext(), "Evénement créé" , Toast.LENGTH_SHORT);
				toast.show();

				//Redirection vers la page principale
				Intent intent = new Intent(Nouvel_evenement2.this, Home.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("currentUser", mCurrentUser);
				intent.putExtras(bundle);
				startActivity(intent);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
				// called when response HTTP status is "4XX" (eg. 401, 403, 404)
				Log.d("Create event", "failure");
				Toast toast = Toast.makeText(getApplicationContext(), "Echec de la création", Toast.LENGTH_SHORT);
				toast.show();
			}
		});
	}

	//TODO Faire classe et y mettre ces fonctions pour eviter repetition de code
	/**
	 * Permet de remplir la liste des contacts
	 * @param array
	 */
	public void populateListFriends(JSONArray array){
		for(int i = 0; i < array.length(); i++){
			User contact = new User();
			try {
				contact.setId(array.getJSONArray(i).optLong(0));
				contact.setFirstname(array.getJSONArray(i).optString(1));
				contact.setLastname(array.getJSONArray(i).optString(2));
				mListFriend.add(contact);
			} catch (JSONException e1) {
				e1.printStackTrace();
			}		
		}
	}

	/**
	 * Permet d'afficher les contacts
	 */
	public void showContacts(){
		//mise a jour de la liste d'amis
		mAdapter.setFriendList(mListFriend);
		//notify l'adapteur
		mAdapter.notifyDataSetChanged();
	}
	
	

}

