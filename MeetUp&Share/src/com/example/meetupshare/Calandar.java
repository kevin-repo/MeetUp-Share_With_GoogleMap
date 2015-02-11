package com.example.meetupshare;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.meetupshare.adapters.EventAdapter;
import com.example.models.Event;
import com.example.models.User;
import com.example.webservice.Webservice;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class Calandar extends Activity {

	private User mCurrentUser;
	private Event mCurrentEvent;
	private List<Event> mListEvent;
	private ListView mList;
	private EventAdapter mAdapter;
	private Button mAddBtn;
	private Button mRemoveBtn;
	private String mIdEventSelected;
	private int mPositionItemSelected;
	private List<String> mIdEventSelectedList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.calandar);

		mListEvent = new ArrayList<Event>();
		mAddBtn = (Button)findViewById(R.id.add_event_btn);
		mRemoveBtn = (Button)findViewById(R.id.remove_event_btn);
		mList = (ListView)findViewById(R.id.listEvent);
		mCurrentUser = (User)getIntent().getExtras().get("currentUser");
		mCurrentEvent = new Event();

		mAdapter = new EventAdapter(this, android.R.layout.simple_list_item_multiple_choice, mListEvent);
		mList.setAdapter(mAdapter);

		//TODO Ameliorer encodage chaine json de retour + modifier onSuccess
		String url = "events.php?method=readevents&idu="+mCurrentUser.getId();
		Webservice.get(url, null, new JsonHttpResponseHandler(){			
			//Version 1
			public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
				Log.d("event_list", "success");
				populateListEvents(response);
				showEvents();
			}

			public void onFailure(int statusCode, Header[] headers, String s, Throwable e) {
				Log.d("event_list", "failure");
			}
		});

		//Ecouteur d'événement sur la liste des event
		mList.setOnItemClickListener(new OnItemClickListener () {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				mCurrentEvent.setId(mListEvent.get(position).getId());
				//Passage à l'activity Evenement
				Intent intent = new Intent(Calandar.this, Evenement.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("currentEvent", mCurrentEvent);
				bundle.putSerializable("currentUser", mCurrentUser);
				intent.putExtras(bundle);
				startActivity(intent);	
			}			
		});
	}
	

//	protected void onResume() {
//		super.onResume();
//		this.onCreate(null);
//	}
	
	/**
	 * Permet de remplir la liste des evenements
	 * @param array
	 */
	protected void populateListEvents(JSONArray array){
		for(int i = 0; i < array.length(); i++){
			Event e = new Event();
			try {
				e.setId(array.getJSONArray(i).optLong(0));
				e.setLocation(array.getJSONArray(i).optString(1));
				//TODO Separer date et heure
				e.setDate(array.getJSONArray(i).optString(2));
				e.setTitre(array.getJSONArray(i).optString(3));
				mListEvent.add(e);
			} catch (JSONException e1) {
				e1.printStackTrace();
			}		
		}
	}

	/**
	 * Permet d'afficher les evenements
	 */
	protected void showEvents(){
		//mise a jour de la liste d'evenements
		mAdapter.setEventList(mListEvent);
		//notify l'adapteur
		mAdapter.notifyDataSetChanged();
	}

	/**
	 * Redirection vers activity "Nouvel_evenement"
	 * @param view
	 */
	public void addEvent(View view){
		//Passage à l'activity Evenement
		Intent intent = new Intent(Calandar.this, Nouvel_evenement.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable("currentUser", mCurrentUser);
		intent.putExtras(bundle);
		startActivity(intent);
	}

	/**
	 * Suppression d'un event de la liste des evenements
	 * @param view
	 */
	public void removeEvent(View view){
		mIdEventSelectedList = mAdapter.getIdCheckedItems();

		//TODO ajouter les positions a la liste pour faciliter suppression
		if(mAdapter.getCountIdCheckedItemsList() != 0){
			for(int i = 0; i < mIdEventSelectedList.size(); i++){
				final int position = i;
				String url = "events.php?method=deleteevent&event=" + mIdEventSelectedList.get(i);
				Webservice.delete(url, new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						Log.d("delete_event", "success");
						Toast toast = Toast.makeText(getApplicationContext(), "Evenement supprimé", Toast.LENGTH_SHORT);
						toast.show();
						//suppression de l'evenement selectionne de la liste event
						removeEventofListEvent(position);
						//mise a jour de la liste
						mAdapter.notifyDataSetChanged();
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
						Log.d("delete_event", "failure");
						Toast toast = Toast.makeText(getApplicationContext(), "Echec de la suppression", Toast.LENGTH_SHORT);
						toast.show();		
					}			
				});
			}
			//vide le contenu de la liste contenant les id et positions des amis a supprimer
			mAdapter.initializeIdCheckedItems();
			mAdapter.initializemPositionItemsChecked();
		}else{
			Toast toast = Toast.makeText(getApplicationContext(), "Veuillez sélectionner un événement à supprimer", Toast.LENGTH_SHORT);
			toast.show();
		}
	}
	
	private void removeEventofListEvent(int position){
		for(int j = 0; j< mListEvent.size(); j++){
			if(Long.toString(mListEvent.get(j).getId()) == mIdEventSelectedList.get(position)){
				mListEvent.remove(j);
			}
		}
	}

}

