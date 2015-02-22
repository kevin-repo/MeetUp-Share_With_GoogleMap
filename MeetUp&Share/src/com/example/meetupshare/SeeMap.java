package com.example.meetupshare;


import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.models.Event;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import android.os.Bundle;
import android.util.Log;


public class SeeMap extends MainActivity{

	private Event mCurrentEvent;
	//	private LatLng mLocation;
	//	private Marker mMarker;
	//	private GoogleMap mMap;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map);
		//		mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
		//		mMarker = mMap.addMarker(null);
		mCurrentEvent = (Event) getIntent().getExtras().get("currentEvent");

		init();
	}

	/**
	 * Permet d'initialiser la map avec les coordonnees de l'evenement
	 */
	private void init(){
		String location = mCurrentEvent.getLocation();
		getLonLatFromAddress(location);
		//ajout du marqueur sur le lieu de l'evenement
		//		mMarker.setPosition(mLocation);
		//		mMarker.setTitle(mCurrentEvent.getTitre());
		//		//focus sur le marqueur avec un zoom de 15
		//		mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mLocation, 15));
	}

	/**
	 * Permet de recuperer les coordonnees correspondants a l'adresse
	 * @param address
	 */
	private static void getLonLatFromAddress(String address) {
		String url = "http://maps.google.com/maps/api/geocode/json?address=" + address + "&sensor=false";
		Log.d("url", url);
		AsyncHttpClient client = new AsyncHttpClient();
		client.get(url, null, new JsonHttpResponseHandler(){
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				Log.d("get_lon_lat", "success");		
			    try {    
			        double lng = ((JSONArray)response.get("results")).getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getDouble("lng");
			        double lat = ((JSONArray)response.get("results")).getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getDouble("lat");

		        	//mLocation.longitude = lng;
		        	//mLocation.latitude = lat;

			        Log.d("latitude", "" + lat);
			        Log.d("longitude", "" + lng);
			    } catch (JSONException e) {
			        e.printStackTrace();
			        //mLocation.latitude = 0;
		        	//mLocation.longitude = 0;
			    }
			}

			public void onFailure(int statusCode, Header[] headers, String s, Throwable e) {
				Log.d("get_lon_lat", "failure");
				//mLocation.latitude = 0;
				//mLocation.longitude = 0;
			}

		});	
		
	}

}
