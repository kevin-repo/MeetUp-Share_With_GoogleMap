package com.example.meetupshare;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.example.meetupshare.adapters.EventAdapter;
import com.example.models.Event;
import com.example.models.User;
import com.example.webservice.Webservice;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

public class EventRequest extends MainActivity{

	private ArrayList<Event> mListEvent;
	private ListView mList;
	private User mCurrentUser;
	private Event mCurrentEvent;
	private EventAdapter mAdapter;
	private JSONArray mIdEventSelectedList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.event_request);

		mListEvent = new ArrayList<Event>();
		mList = (ListView)findViewById(R.id.event_list_request);
		mCurrentUser = (User)getIntent().getExtras().get("currentUser");
		mCurrentEvent = new Event();

		mAdapter = new EventAdapter(this, android.R.layout.simple_list_item_multiple_choice, mListEvent, true);
		mList.setAdapter(mAdapter);

		//TODO Ameliorer encodage chaine json de retour + modifier onSuccess
		String url = "events.php?method=readeventrequests&idu="+mCurrentUser.getId();
		Webservice.get(url, null, new JsonHttpResponseHandler(){			
			//Version 1
			public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
				Log.d("event_list", "success");
				populateList(response);
				show();
			}

			public void onFailure(int statusCode, Header[] headers, String s, Throwable e) {
				Log.d("event_list", "failure");
				finish();
			}
		});

		//Ecouteur d'événement sur la liste des event
		mList.setOnItemClickListener(new OnItemClickListener () {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				mCurrentEvent.setId(mListEvent.get(position).getId());
				//Passage à l'activity Evenement
				Intent intent = new Intent(EventRequest.this, Evenement.class);
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
	protected void populateList(JSONArray array){
		for(int i = 0; i < array.length(); i++){
			Event e = new Event();
			try {
				e.setId(array.getJSONArray(i).optLong(0));
				e.setLocation(array.getJSONArray(i).optString(1));
				e.setDate(deleteHour(array.getJSONArray(i).optString(2)));
				e.setTitre(array.getJSONArray(i).optString(3));
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
	protected void show(){
		//mise a jour de la liste d'evenements
		mAdapter.setEventList(mListEvent);
		//notify l'adapteur
		mAdapter.notifyDataSetChanged();
	}

	/*private void removeEventofListEvent(int position){
		for(int j = 0; j< mListEvent.size(); j++){
			if(Long.toString(mListEvent.get(j).getId()) == mIdEventSelectedList.get(position)){
				mListEvent.remove(j);
			}
		}
	}*/

	public String deleteHour(String s){
		String res;

		int i = s.indexOf(" ");
		res = s.substring(0, i);
		return res;
	}
}
