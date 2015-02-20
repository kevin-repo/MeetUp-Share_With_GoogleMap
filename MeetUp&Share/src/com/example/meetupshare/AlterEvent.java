package com.example.meetupshare;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


/**
 * Modification d'un event
 *
 */
public class AlterEvent extends MainActivity implements ListOfItems{

	private EditText mTitle, mLocation, mLink, mDescription;
	private TextView mInviteTextView;
	private Event mCurrentEvent;
	private DatePicker mDate;
	private RelativeLayout mNext, mPrevious;
	private ArrayList<User> mListFriend;
	private FriendAdapter mAdapter;
	private ListView mList;
	private User mCurrentUser;
	private List<String> mIdFriendSelectedList;
	private ArrayList<User> mListParticipant;
	private Button mContactListBtn;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alter_event);

		mTitle = (EditText)findViewById(R.id.alter_title_event);
		mLocation = (EditText)findViewById(R.id.alter_location_event);
		mLink = (EditText)findViewById(R.id.alter_link_event);
		mDescription = (EditText)findViewById(R.id.alter_description_event);
		mInviteTextView = (TextView)findViewById(R.id.alter_invite_text_view);
		mDate = (DatePicker)findViewById(R.id.alter_date_event);
		mNext = (RelativeLayout)findViewById(R.id.first_modification_event);
		mPrevious = (RelativeLayout)findViewById(R.id.second_modification_event);
		mList = (ListView)findViewById(R.id.alter_liste_contacts_event);
		mContactListBtn = (Button)findViewById(R.id.alter_liste_contacts_event_btn); 
		mListFriend = new ArrayList<User>();
		mAdapter = new FriendAdapter(this, R.layout.friend_list, mListFriend, false);
		mCurrentEvent = (Event) getIntent().getExtras().get("currentEvent");
		mCurrentUser = (User) getIntent().getExtras().get("currentUser");
		mListParticipant = (ArrayList<User>) getIntent().getExtras().get("participantList");
		
		Log.d("mListParticipant", ""+mListParticipant.size());
		mList.setAdapter(mAdapter);

		init();
		initFriendList();
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
	 * Initialisation de la liste des contacts
	 */
	private void initFriendList(){
		RequestParams params = new RequestParams();
		params.put("idcurrent", mCurrentUser.getId());

		String file = Webservice.usersMethod();

		Webservice.get(file+"?method=readfriends", params, new JsonHttpResponseHandler(){			
			public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
				Log.d("contact_list_event", "sucess");
				populateList(response);	
				show();
				
				if(mListFriend.size() == 0){
					mContactListBtn.setVisibility(View.GONE);
					mInviteTextView.setVisibility(View.GONE);
					mList.setVisibility(View.GONE);
				}
			}

			public void onFailure(int statusCode, Header[] headers, String s, Throwable e) {
				Log.d("contact_list_event", "failure");
				mContactListBtn.setVisibility(View.GONE);
				mInviteTextView.setVisibility(View.GONE);
				mList.setVisibility(View.GONE);
			}
		});
	}



	/**
	 * Modifier le nom de l'événement
	 * @param view
	 */
	public void alterTitle(View view){
		if(mTitle.getText().toString().equals("")){
			Toast toast = Toast.makeText(getApplicationContext(), "Veuillez saisir un titre", Toast.LENGTH_SHORT);
			toast.show();
		}else{
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
	}

	/**
	 * Modifier l'adresse de l'événement
	 * @param view
	 */
	public void alterLocation(View view){
		if(mLocation.getText().toString().equals("")){
			Toast toast = Toast.makeText(getApplicationContext(), "Veuillez saisir une adresse", Toast.LENGTH_SHORT);
			toast.show();
		}else{
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
	}

	/**
	 * Modifier le lien de partage de photos de l'événement
	 * @param view
	 */
	public void alterLink(View view){
		if(mLink.getText().toString().equals("")){
			Toast toast = Toast.makeText(getApplicationContext(), "Veuillez saisir un lien", Toast.LENGTH_SHORT);
			toast.show();
		}else{	
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
	}

	/**
	 * Modifier la description de l'événement
	 * @param view
	 */
	public void alterDescription(View view){
		if(mDescription.getText().toString().equals("")){
			Toast toast = Toast.makeText(getApplicationContext(), "Veuillez saisir une description", Toast.LENGTH_SHORT);
			toast.show();
		}else{	
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
	}

	/**
	 * Modifier la date de l'evenement
	 * @param view
	 */
	public void alterDate(View view){
		int jour = mDate.getDayOfMonth();
		int mois = mDate.getMonth()+1;
		int annee = mDate.getYear();
		String dateFormatee = annee + "-" + mois + "-" + jour;

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date dateTime = null;
		try {
			dateTime = simpleDateFormat.parse(dateFormatee);
		}catch (ParseException ex) {
			System.out.println("Exception "+ex);
		}
		dateTime.setHours(1);
		dateTime.setMinutes(0);
		dateTime.setSeconds(0);

		//Recuperation de la date du jour
		Date dateTimeToday = new Date();
		dateTimeToday.setHours(0);
		dateTimeToday.setMinutes(0);
		dateTimeToday.setSeconds(0);

		if(dateTimeToday.compareTo(dateTime) > 0){
			Toast toast = Toast.makeText(getApplicationContext(), "Veuillez saisir une date postérieur à la date du jour", Toast.LENGTH_SHORT);
			toast.show();
		}else{
			String file = Webservice.eventsMethod();
			String url = file+"?method=updatedate&event="+mCurrentEvent.getId()+"&date="+simpleDateFormat.format(dateTime);
			Webservice.post(url, null, new AsyncHttpResponseHandler() {
				@Override
				public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
					Log.d("alter_date", "success");
					Toast toast = Toast.makeText(getApplicationContext(), "Modification effectuée", Toast.LENGTH_SHORT);
					toast.show();
				}	
				@Override
				public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
					Log.d("alter_date", "failure");	
					Toast toast = Toast.makeText(getApplicationContext(), "Modification imposible", Toast.LENGTH_SHORT);
					toast.show();
				}
			});
		}
	}

	/**
	 * Modifier la liste d'invites a l'evenement
	 */
	public void alterContactList(){
		associateParticipants();
	}


	/**
	 * Acceder a la deuxieme partie des parametres de l'evenement a modifier
	 * @param view
	 */
	public void alterNext(View view){
		mNext.setVisibility(View.GONE);
		mPrevious.setVisibility(View.VISIBLE);
	}

	/**
	 * Retour a la premiere partie des parametres de l'evenement a modifier
	 * @param view
	 */
	public void alterPrevious(View view){
		mNext.setVisibility(View.VISIBLE);
		mPrevious.setVisibility(View.GONE);
	}

	/**
	 * Creation participant
	 */
	public void associateParticipants(){
		mIdFriendSelectedList = mAdapter.getIdCheckedItems();

		if(mAdapter.getCountIdCheckedItemsList() != 0){
			for(int i = 0; i < mIdFriendSelectedList.size(); i++){
				final int position = i;
				//Association des participants a l'evenement
				String url = "events.php?method=addparticipant&idu="+mIdFriendSelectedList.get(i)+"&event="+mCurrentEvent.getId();
				Webservice.post(url, null, new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						Log.d("associate_participant_event", "success");
						removeItemOfList(position);
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
		removeParticipants();
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

	/**
	 * Permet de supprimer les contacts participants deja a l'evenement de la liste des contacts a inviter
	 */
	public void removeParticipants(){
		for(int i = 0; i < mListParticipant.size(); i++){
			for(int j = 0; j < mListFriend.size(); j++){
				if(mListParticipant.get(i).getId() == mListFriend.get(j).getId()){
					mListFriend.remove(j);
				}
			}
		}
	}
	
	/**
	 * Permet de supprimer l'element se trouvant a la position i de la liste des contacts
	 * @param i Position de l'item dans la liste des evenements
	 */
	public void removeItemOfList(int i){
		for(int j = 0; j< mListFriend.size(); j++){
			if(Long.toString(mListFriend.get(j).getId()) == mIdFriendSelectedList.get(i)){
				mListFriend.remove(j);
			}
		}
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
