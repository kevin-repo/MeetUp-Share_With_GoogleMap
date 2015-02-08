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
import com.loopj.android.http.JsonHttpResponseHandler;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class Calandar extends Activity {

	private User mCurrentUser;
	private List<Event> mListEvent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.calandar);

		mCurrentUser = (User)getIntent().getExtras().get("currentUser");
		
		

		//V1
		/*final ArrayList<Event> listEvent = Event.generateListOfEvent();
		EventAdapter adapter = new EventAdapter(listEvent, this);
		final ListView list = (ListView)findViewById(R.id.listEvent);
		*/

		//V2
		mListEvent = populateEventList();
		EventAdapter adapter = new EventAdapter(mListEvent, this);
		final ListView list = (ListView)findViewById(R.id.listEvent);
		
		list.setAdapter(adapter);

		//Ecouteur d'événement sur la liste des event
		list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {		
				Toast toast = Toast.makeText(getApplicationContext(), mListEvent.get(position).getTitre(), Toast.LENGTH_SHORT);
				toast.show();

				Event currentEvent = new Event();
				currentEvent.setTitre(mListEvent.get(position).getTitre());
				currentEvent.setDate(mListEvent.get(position).getDate());
				currentEvent.setHeure(mListEvent.get(position).getHeure());

				//Passage à l'activity Evenement
				Intent intent = new Intent(Calandar.this, Evenement.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("currentEvent", currentEvent);
				bundle.putSerializable("currentUser", mCurrentUser);
				intent.putExtras(bundle);
				startActivity(intent);			
			} 
		});
	}

	
	protected List<Event> populateEventList() {
		List<Event> result = new ArrayList<Event>();
		String url = "events.php?method=readevents&idu="+mCurrentUser.getId();
		Webservice.get(url, null, new JsonHttpResponseHandler(){	
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				Log.d("event_list", "sucess");
				/*Log.d("response", response.toString());
				try {
					JSONArray array = response.getJSONArray("");
//					for(int i = 0; i<array.length(); i++){
//						mListEvent.add(new Event(array.getJSONObject(i)));
//					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
				try {
					JSONObject o = response.getJSONObject("11");
					
					Log.d("title",o.getString("title"));
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				JSONArray array = response.optJSONArray("");
				Log.d("lenght", Integer.toString(array.length()));
				for(int i = 0; i<array.length(); i++){
					try {
						mListEvent.add(new Event(array.getJSONObject(i)));
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}

			public void onFailure(int statusCode, Header[] headers, String s, Throwable e) {
				Log.d("event_list", "failure");
			}
		});
		return result;
	}
}

