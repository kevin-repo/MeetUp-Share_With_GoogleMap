package com.example.webservice;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class Webservice{

	private static final String URL_CONNECT = "http://romainreyomond.netii.net/";
	private static final String URL_EVENTS = "events.php";
	private static final String URL_USERS = "users.php";
	
	private static AsyncHttpClient client = new AsyncHttpClient();

	public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
		client.get(getAbsoluteUrl(url), params, responseHandler);
	}

	public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
		client.post(getAbsoluteUrl(url), params, responseHandler);
	}

	public static void delete(String url, AsyncHttpResponseHandler responseHandler) {
		client.delete(getAbsoluteUrl(url), responseHandler);
	}
	
	public static String eventsMethod(){
		return URL_EVENTS;
	}
	
	public static String usersMethod(){
		return URL_USERS;
	}
	
	private static String getAbsoluteUrl(String relativeUrl) {
		return URL_CONNECT + relativeUrl;
	}
	
}