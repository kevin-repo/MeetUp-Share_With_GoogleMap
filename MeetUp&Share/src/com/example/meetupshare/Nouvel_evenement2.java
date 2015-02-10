package com.example.meetupshare;

import java.util.ArrayList;
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
	private List<String> mIdFriendSelectedList;

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
		mIdFriendSelectedList = mAdapter.getIdCheckedItems();

		//TODO ajouter les positions a la liste pour faciliter suppression
		//+ changer url + voir pour ajouts événements + ajouter id_participant
		if(mAdapter.getCountIdCheckedItemsList() != 0){
			for(int i = 0; i < mIdFriendSelectedList.size(); i++){
				String url = "events.php?method=createevent&title="+mEvenement.getTitre()+"&organizer="+mCurrentUser.getId()+"&place="+mAdresse.getText().toString();
				Webservice.delete(url, new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						Log.d("Create event", "success");
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

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
						Log.d("Create event", "failure");
						Toast toast = Toast.makeText(getApplicationContext(), "Echec de la création", Toast.LENGTH_SHORT);
						toast.show();		
					}			
				});
			}
			//vide le contenu de la liste contenant les id et positions des amis a supprimer
			mAdapter.initializeIdCheckedItems();
		}else{
			Toast toast = Toast.makeText(getApplicationContext(), "Veuillez sélectionner un contact à supprimer", Toast.LENGTH_SHORT);
			toast.show();
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



}

