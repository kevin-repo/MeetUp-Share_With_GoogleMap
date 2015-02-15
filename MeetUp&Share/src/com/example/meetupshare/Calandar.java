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
import com.loopj.android.http.RequestParams;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Liste des evenements
 *
 */
public class Calandar extends MainActivity implements ListOfItems{

	private User mCurrentUser;
	private Event mCurrentEvent;
	private List<Event> mListEvent;
	private ListView mList;
	private EventAdapter mAdapter;
	private List<String> mIdEventSelectedList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.calandar);

		mListEvent = new ArrayList<Event>();
		mList = (ListView)findViewById(R.id.listEvent);
		mCurrentUser = (User)getIntent().getExtras().get("currentUser");
		mCurrentEvent = new Event();

		mAdapter = new EventAdapter(this, android.R.layout.simple_list_item_multiple_choice, mListEvent, false);
		mList.setAdapter(mAdapter);

		init();
	}

	/**
	 * Initialisation de la liste des evenements
	 */
	private void init(){
		RequestParams params = new RequestParams();
		params.put("idu", mCurrentUser.getId());
		
		String file = Webservice.eventsMethod();
		
		Webservice.get(file+"?method=readevents", params, new JsonHttpResponseHandler(){			
			public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
				Log.d("event_list", "success");
				populateList(response);
				show();
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

	/**
	 * Permet de remplir la liste des evenements
	 * @param array
	 */
	public void populateList(JSONArray array){
		for(int i = 0; i < array.length(); i++){
			Event e = new Event();
			try {
				e.setId(array.getJSONArray(i).optLong(0));
				e.setLocation(ajouterEspace(array.getJSONArray(i).optString(1)));
				e.setDate(deleteHour(array.getJSONArray(i).optString(2)));
				e.setTitre(ajouterEspace(array.getJSONArray(i).optString(3)));
				e.setHeure(array.getJSONArray(i).optString(4));
				mListEvent.add(e);
			} catch (JSONException e1) {
				e1.printStackTrace();
			}		
		}
	}

	/**
	 * Permet d'afficher les evenements
	 */
	public void show(){
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
				final String url1 = "events.php?method=readcurrent&id=" + mIdEventSelectedList.get(i);
				final String url = "events.php?method=deleteevent&event=" + mIdEventSelectedList.get(i);
				Webservice.get(url1, null, new JsonHttpResponseHandler(){
					public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
						Log.d("readevent", "success");
						User user = new User();
						user.setId(Long.parseLong(response.optString("id_u")));
						if (mCurrentUser.getId()==user.getId()){
							Webservice.delete(url, new AsyncHttpResponseHandler() {
								@Override
								public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
									Log.d("delete_event", "success");
									Toast toast = Toast.makeText(getApplicationContext(), "Evenement supprimé", Toast.LENGTH_SHORT);
									toast.show();
									//suppression de l'evenement selectionne de la liste event
									removeItemOfList(position);
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
					}

					public void onFailure(int statusCode, Header[] headers, String s, Throwable e) {
						Log.d("connexion_user", "failure");
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

	/**
	 * Permet de supprimer l'element se trouvant a la position i de la liste des evenements
	 * @param i Position de l'item dans la liste des evenements
	 */
	public void removeItemOfList(int i){
		for(int j = 0; j< mListEvent.size(); j++){
			if(Long.toString(mListEvent.get(j).getId()) == mIdEventSelectedList.get(i)){
				mListEvent.remove(j);
			}
		}
	}

	/**
	 * Permet de remplacer "_" par un espace afin d'ameliorer l'affichage
	 * @param s
	 * @return
	 */
	public String ajouterEspace(String s){
		String res;
		res = s.replace("_", " ");
		return res;
	}

	/**
	 * 
	 * @param s
	 * @return
	 */
	public String deleteHour(String s){
		String res;

		int i = s.indexOf(" ");
		res = s.substring(0, i);
		return res;
	}

}

