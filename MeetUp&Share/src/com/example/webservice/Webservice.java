package com.example.webservice;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class Webservice{

	private static final String Url_Connect = "http://romainreyomond.netii.net/users.php";
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
	
	private static String getAbsoluteUrl(String relativeUrl) {
		return Url_Connect + relativeUrl;
	}
	
}