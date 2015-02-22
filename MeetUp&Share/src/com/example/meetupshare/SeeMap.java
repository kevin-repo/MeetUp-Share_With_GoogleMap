package com.example.meetupshare;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.models.Event;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

/**
 * Affichage de la map
 *
 */
public class SeeMap extends MainActivity{

	private Event mCurrentEvent;
	private static LatLng mLocation;
	private Marker mMarker;
	private GoogleMap mMap;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map);
		mCurrentEvent = (Event) getIntent().getExtras().get("currentEvent");
		
		try {
			initializeMap();
		} catch (Exception  e) {
			e.printStackTrace();
			Toast toast = Toast.makeText(getApplicationContext(), "Impossible d'initialiser la carte", Toast.LENGTH_SHORT);
			toast.show();
		}
		init();
	}
	
	/**
	 * Permet d'initialiser la carte
	 */
	private void initializeMap() {
		if(mMap == null){
			mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
			mMap.getUiSettings().setZoomControlsEnabled(true);
			//controle si la map est bien cree
			if(mMap == null){
				Toast toast = Toast.makeText(getApplicationContext(), "Impossible de créer la carte", Toast.LENGTH_SHORT);
				toast.show();
			}	
		}	
	}

	/**
	 * Permet d'ajouter a la map un marqueur correspondant aux coordonnees de l'evenement
	 */
	private void init(){
		String location = mCurrentEvent.getLocation();
		getLonLatFromAddress(location);
	}

	/**
	 * Permet de recuperer les coordonnees correspondants a l'adresse
	 * @param address
	 */
	private void getLonLatFromAddress(String address) {
		String url = "http://maps.google.com/maps/api/geocode/json?address=" + address + "&sensor=false";
		Log.d("url", url);
		AsyncHttpClient client = new AsyncHttpClient();
		client.get(url, null, new JsonHttpResponseHandler(){
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				Log.d("get_lon_lat", "success");		
			    try {    
			        double lng = ((JSONArray)response.get("results")).getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getDouble("lng");
			        double lat = ((JSONArray)response.get("results")).getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getDouble("lat");
		        	
			        mLocation = new LatLng (lat, lng);
			        mMarker = mMap.addMarker( new MarkerOptions().position(mLocation).title(mCurrentEvent.getTitre()));
			        //focus sur le marqueur avec un zoom de 15
			        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mLocation, 15));
			        
			    } catch (JSONException e) {
			        e.printStackTrace();
			        mLocation = new LatLng (0, 0);
			    }
			}

			public void onFailure(int statusCode, Header[] headers, String s, Throwable e) {
				Log.d("get_lon_lat", "failure");
				mLocation = new LatLng (0, 0);
			}

		});	
		
	}

}
