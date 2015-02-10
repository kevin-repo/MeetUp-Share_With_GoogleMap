package com.example.meetupshare;


import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;

import com.example.meetupshare.adapters.FriendAdapter;
import com.example.models.User;
import com.example.webservice.Webservice;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;


public class FriendRequest extends Activity{

	private ListView mList;
	private ArrayList<User> mListFriendRequest;
	private FriendAdapter mAdapter;
	private User mCurrentUser;
	private List<String> mIdFriendSelectedList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.friend_request_list);

		mListFriendRequest = new ArrayList<User>();
		mAdapter = new FriendAdapter(this, android.R.layout.simple_list_item_multiple_choice, mListFriendRequest);
		mList = (ListView)findViewById(R.id.liste_friend_request);

		mList.setAdapter(mAdapter);

		//Recuperation de l'user courrant
		mCurrentUser = (User)getIntent().getExtras().get("currentUser");

		//TODO Ameliorer encodage chaine json de retour + modifier onSuccess
		String url = "users.php?method=friendrequests&idcurrent="+mCurrentUser.getId();
		Webservice.get(url, null, new JsonHttpResponseHandler(){			
			//Version 1
			public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
				Log.d("friend_request_list", "sucess");
				populateListFriends(response);
				showContacts();
			}

			public void onFailure(int statusCode, Header[] headers, String s, Throwable e) {
				Log.d("friend_request_list", "failure");
			}
		});
	}

	/**
	 * Permet de valider la demande d'ami
	 * @param view
	 */
	public void validateFriendRequest(View view){
		mIdFriendSelectedList = mAdapter.getIdCheckedItems();

		if(mAdapter.getCountIdCheckedItemsList() != 0){
			for(int i = 0; i < mIdFriendSelectedList.size(); i++){
				final int position = i;
				String url = "users.php?method=validatefriend&idcurrent=" + mCurrentUser.getId() + "&idfriend=" + mIdFriendSelectedList.get(i);
				Webservice.post(url, null, new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						Log.d("validate_request_friend", "success");
						Toast toast = Toast.makeText(getApplicationContext(), "Ami ajouté à Contacts" , Toast.LENGTH_SHORT);
						toast.show();
						//suppression de l'ami selectionne de la liste friend					
						removeElementOfList(position);																		
						//mise a jour de la liste
						mAdapter.notifyDataSetChanged();
						//TODO passage a activity "Home" si liste de demande d'amis vide
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
						Log.d("validate_request_friend", "failure");
						Toast toast = Toast.makeText(getApplicationContext(), "Echec de l'ajout", Toast.LENGTH_SHORT);
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



	//TODO Mettre dans interface
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
				mListFriendRequest.add(contact);
			} catch (JSONException e1) {
				e1.printStackTrace();
			}		
		}
	}

	//TODO Mettre dans interface
	/**
	 * Permet d'afficher les contacts
	 */
	public void showContacts(){
		//mise a jour de la liste d'amis
		mAdapter.setFriendList(mListFriendRequest);
		//notify l'adapteur
		mAdapter.notifyDataSetChanged();
	}
	
	//TODO Mettre dans interface
	private void removeElementOfList(int position){
		for(int i = 0; i < mListFriendRequest.size(); i++){
			if(Long.toString(mListFriendRequest.get(i).getId()) == mIdFriendSelectedList.get(position)){
				mListFriendRequest.remove(i);
			}
		}
	}

}
