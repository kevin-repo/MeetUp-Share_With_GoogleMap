package com.example.meetupshare;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.meetupshare.adapters.FriendAdapter;
import com.example.models.Event;
import com.example.models.User;
import com.example.webservice.Webservice;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

public class Contacts extends Activity  {

	//private String mIdUser;
	private int mPositionItemSelected;
	private ArrayList<User> mListFriend;
	private FriendAdapter mAdapter;
	private Button mAddBtn;
	private Button mRemoveBtn;
	private Button mValiderBtn;
	private String mIdFriendSelected;
	private User mCurrentUser;
	private EditText mSearchFriend;
	private EditText mMailFriend;
	private ListView mList;
	private List<String> mIdFriendSelectedList;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contacts);

		mListFriend = new ArrayList<User>();
		mAdapter = new FriendAdapter(this, android.R.layout.simple_list_item_multiple_choice, mListFriend);
		mList = (ListView)findViewById(R.id.liste_contacts);
		mRemoveBtn = (Button) findViewById(R.id.remove_friend_btn);
		mValiderBtn = (Button) findViewById(R.id.validate_add_friend_btn);
		mAddBtn = (Button) findViewById(R.id.add_friend_btn);
		mSearchFriend = (EditText) findViewById(R.id.recherche_ami);
		mMailFriend = (EditText) findViewById(R.id.mail_friend);

		mList.setAdapter(mAdapter);
		//mList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

		//Recuperation de l'user courrant
		mCurrentUser = (User)getIntent().getExtras().get("currentUser");

		//TODO Ameliorer encodage chaine json de retour + modifier onSuccess
		String url = "users.php?method=readfriends&idcurrent="+mCurrentUser.getId();
		Webservice.get(url, null, new JsonHttpResponseHandler(){			
			//Version 1
			public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
				Log.d("contact_list", "sucess");
				populateListFriends(response);
				showContacts();
			}

			public void onFailure(int statusCode, Header[] headers, String s, Throwable e) {
				Log.d("contact_list", "failure");
			}
		});
	}



	/**
	 * Affichage du formulaire permettant d'ajouter un ami
	 * @param view
	 */
	public void addFriend(View view) {
		//formulaire pour ajout d'un ami devient visible
		mSearchFriend.setVisibility(View.GONE);
		mList.setVisibility(View.GONE);	
		mRemoveBtn.setVisibility(View.GONE);
		mAddBtn.setVisibility(View.GONE);
		mValiderBtn.setVisibility(View.VISIBLE);
		mMailFriend.setVisibility(View.VISIBLE);
	}

	//TODO
	public void validateAdd(View view) {
		//TO DO -> Implementer web service ajout d'un ami
		/*
		RequestParams params = new RequestParams();
		params.put("currentuser", mCurrentUser.getEmail());

		Webservice.post("?method=addfriend", params, new JsonHttpResponseHandler(){
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				//mise a jour de la liste des amis
				mAdapter.notifyDataSetChanged();
			}			
			public void onFailure(int statusCode, Header[] headers, String s, Throwable e) {
				Toast toast = Toast.makeText(getApplicationContext(), "Ajout du contact impossible", Toast.LENGTH_SHORT);
				toast.show();
			}
		});
		 */
		//formulaire pour ajout d'un ami devient invisible
		mSearchFriend.setVisibility(View.VISIBLE);
		mList.setVisibility(View.VISIBLE);	
		mRemoveBtn.setVisibility(View.VISIBLE);
		mAddBtn.setVisibility(View.VISIBLE);
		mValiderBtn.setVisibility(View.GONE);
		mMailFriend.setVisibility(View.GONE);
	}

	/**
	 * Suppression d'un ami de la liste des contacts
	 * @param view
	 */
	public void removeFriend(View view) {
		mIdFriendSelectedList = mAdapter.getIdCheckedItems();

		//TODO ajouter les positions a la liste pour faciliter suppression
		if(mAdapter.getCountIdCheckedItemsList() != 0){
			for(int i = 0; i < mIdFriendSelectedList.size(); i++){
				final int position = i;
				String url = "users.php?method=deletefriend&idcurrent=" + mCurrentUser.getId() + "&idfriend=" + mIdFriendSelectedList.get(i);
				Webservice.delete(url, new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						Log.d("delete_contact", "success");
						Toast toast = Toast.makeText(getApplicationContext(), "Contact supprimé", Toast.LENGTH_SHORT);
						toast.show();
						//suppression de l'ami selectionne de la liste friend					
						removeFriendofListFriend(position);																		
						//mise a jour de la liste
						mAdapter.notifyDataSetChanged();			
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
						Log.d("delete_contact", "failure");
						Toast toast = Toast.makeText(getApplicationContext(), "Echec de la suppression", Toast.LENGTH_SHORT);
						toast.show();		
					}			
				});
			}
			//vide le contenu de la liste contenant les id et positions des amis a supprimer
			mAdapter.initializeIdCheckedItems();
			mAdapter.initializemPositionItemsChecked();
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
				mListFriend.add(contact);
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
		mAdapter.setFriendList(mListFriend);
		//notify l'adapteur
		mAdapter.notifyDataSetChanged();
	}
	
	//TODO Mettre dans interface
	private void removeFriendofListFriend(int position){
		for(int j = 0; j< mListFriend.size(); j++){
			if(Long.toString(mListFriend.get(j).getId()) == mIdFriendSelectedList.get(position)){
				mListFriend.remove(j);
			}
		}
	}
}
