package com.example.meetupshare;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.example.meetupshare.adapters.FriendAdapter;
import com.example.models.Event;
import com.example.models.User;
import com.example.webservice.Webservice;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Nouvel_evenement2 extends MainActivity {

	private Event mEvenement;
	private User mCurrentUser;
	private EditText mAdresse, mLink, mDescription;
	private ArrayList<User> mListFriend;
	private FriendAdapter mAdapter;
	private ListView mList;
	private List<String> mIdFriendSelectedList;
	private Event mCurrentEvent;

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
		mCurrentEvent = new Event();
		//Recuperation des informations saisies
		mAdresse = (EditText) findViewById(R.id.adresse);
		mLink = (EditText) findViewById(R.id.url);
		mDescription = (EditText) findViewById(R.id.description);

		mList.setAdapter(mAdapter);

		init();
	}


	/**
	 * Initialisation de l'activity
	 */
	public void init(){
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
		String titreSansEspace = supprimerEspace(mEvenement.getTitre());
		String placeSansEspace = supprimerEspace(mAdresse.getText().toString());
		String descriptionSansEspace = supprimerEspace(mDescription.getText().toString());
		
		String date2 = mEvenement.getDate();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date dateFormate2 = null;
        try
        {
            dateFormate2 = simpleDateFormat.parse(date2);
        }
        catch (ParseException ex)
        {
            System.out.println("Exception "+ex);
        }
		
		String url = "events.php?method=createevent&title="+titreSansEspace+"&organizer="+mCurrentUser.getId()+"&place="+placeSansEspace+"&description="+descriptionSansEspace+"&link="+mLink.getText().toString()+"&date="+simpleDateFormat.format(dateFormate2);
		Log.d("url", url);
		Webservice.post(url, null, new JsonHttpResponseHandler() {
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				Log.d("create_event", "success");
				Log.d("response", ""+response.optString("id_event"));
				
				mCurrentEvent.setId(Long.parseLong(response.optString("id_event")));	
				associateOrganizer();
				associateParticipants();
				
				Toast toast = Toast.makeText(getApplicationContext(), "Evénement créé" , Toast.LENGTH_SHORT);
				toast.show();
				//Redirection vers la page principale
				Intent intent = new Intent(Nouvel_evenement2.this, Home.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("currentUser", mCurrentUser);
				intent.putExtras(bundle);
				startActivity(intent);
				finish();
			}

			public void onFailure(int statusCode, Header[] headers, String s, Throwable e) {
				Log.d("create_event", "failure");
				Toast toast = Toast.makeText(getApplicationContext(), "Echec de la création", Toast.LENGTH_SHORT);
				toast.show();
			}
		});	
	}

	/**
	 * Ajouter l'organisateur de l'event a l'event
	 */
	public void associateOrganizer(){
		String url = "events.php?method=addorganizeratevent&idu="+mCurrentUser.getId()+"&event="+mCurrentEvent.getId();
		Webservice.post(url, null, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				Log.d("associate_organizer_event", "success");
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				Log.d("associate_organizer_event", "failure");		
			}			
		});	
	}
	
	
	/**
	 * Creation participant
	 */
	public void associateParticipants(){
		mIdFriendSelectedList = mAdapter.getIdCheckedItems();
		
		if(mAdapter.getCountIdCheckedItemsList() != 0){
			for(int i = 0; i < mIdFriendSelectedList.size(); i++){
				//Association des participants a l'evenement
				String url = "events.php?method=addparticipant&idu="+mIdFriendSelectedList.get(i)+"&event="+mCurrentEvent.getId();
				Webservice.post(url, null, new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						Log.d("associate_participant_event", "success");
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
						Log.d("associate_participant_event", "failure");	
					}			
				});		
			}
			//vide le contenu de la liste contenant les id et positions des amis a supprimer
			mAdapter.initializeIdCheckedItems();
		}
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

	public String supprimerEspace(String s){
		String res;
		res = s.replace(" ", "_");
		return res;
	}
	
}

