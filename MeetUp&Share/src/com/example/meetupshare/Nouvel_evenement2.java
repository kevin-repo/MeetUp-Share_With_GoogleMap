package com.example.meetupshare;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import com.example.meetupshare.adapters.FriendAdapter;
import com.example.models.Event;
import com.example.models.User;
import com.example.webservice.Webservice;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Creation d'un nouvel evenement 2
 *
 */
public class Nouvel_evenement2 extends MainActivity implements ListOfItems{

	private Event mEvenement, mCurrentEvent;
	private User mCurrentUser;
	private EditText mAdresse, mLink, mDescription;
	private ArrayList<User> mListFriend;
	private FriendAdapter mAdapter;
	private ListView mList;
	private List<String> mIdFriendSelectedList;
	private LinearLayout mListLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nouvel_evenement2);
		mListFriend = new ArrayList<User>();
		mAdapter = new FriendAdapter(this, R.layout.friend_list, mListFriend, false);
		mList = (ListView)findViewById(R.id.liste_contacts_event);
		mListLayout = (LinearLayout) findViewById(R.id.invites_new_event_linearlayout);
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
		RequestParams params = new RequestParams();
		params.put("idcurrent", mCurrentUser.getId());

		String file = Webservice.usersMethod();

		Webservice.get(file+"?method=readfriends", params, new JsonHttpResponseHandler(){			
			//Version 1
			public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
				Log.d("contact_list_event", "sucess");
				populateList(response);
				show();
			}

			public void onFailure(int statusCode, Header[] headers, String s, Throwable e) {
				Log.d("contact_list_event", "failure");
				mListLayout.setVisibility(View.GONE);
			}
		});
	}

	/**
	 * Permet d'acceder au navigateur internet pour aller recuperer le lien de stockage des photos
	 * @param view
	 */
	public void searchLink(View view){
		URL url;
		try {
			url = new URL("https://www.google.com/intl/fr_fr/drive/");
			HttpsURLConnection connexion = (HttpsURLConnection)url.openConnection();
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/intl/fr_fr/drive/"));
			startActivity(intent); 
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			Toast toast = Toast.makeText(getApplicationContext(), "Impossible d'accéder à internet", Toast.LENGTH_SHORT);
			toast.show();
		}
	}
	
	
	/**
	 * Methode permettant la creation d'un event
	 * @param view
	 */
	public void creerEvenement(View view){
		String titreSansEspace = supprimerEspace(mEvenement.getTitre());
		String placeSansEspace = supprimerEspace(mAdresse.getText().toString());
		String descriptionSansEspace = supprimerEspace(mDescription.getText().toString());

		String dateEvent = mEvenement.getDate();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date dateTime = null;
		try {
			dateTime = simpleDateFormat.parse(dateEvent);
		}catch (ParseException ex) {
			System.out.println("Exception "+ex);
		}

		//Test sur les parametres entres
		if(mAdresse.getText().toString().equals("")){
			Toast toast = Toast.makeText(getApplicationContext(), "Veuillez saisir une adresse", Toast.LENGTH_SHORT);
			toast.show();
		} else {
			String url = "events.php?method=createevent&title="+titreSansEspace+"&organizer="+mCurrentUser.getId()+"&place="+placeSansEspace+"&description="+descriptionSansEspace+"&link="+mLink.getText().toString()+"&date="+simpleDateFormat.format(dateTime);
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
	}

	/**
	 * Ajouter l'organisateur de l'event a l'event
	 */
	public void associateOrganizer(){
		String file = Webservice.eventsMethod();
		String url = file+"?method=addorganizeratevent&idu="+mCurrentUser.getId()+"&event="+mCurrentEvent.getId();
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

	/**
	 * Permet de remplir la liste des contacts
	 * @param array
	 */
	public void populateList(JSONArray array){
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
	public void show(){
		//mise a jour de la liste d'amis
		mAdapter.setFriendList(mListFriend);
		//notify l'adapteur
		mAdapter.notifyDataSetChanged();
	}

	public void removeItemOfList(int i) {}

	public String supprimerEspace(String s){
		String res;
		res = s.replace(" ", "_");
		return res;
	}

}

