package com.rxjy.iwc2.api;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.client.params.ClientPNames;

import java.util.Locale;

public class ApiHttpClient
{

	public static AsyncHttpClient client;

	public static AsyncHttpClient getHttpClient()
	{
		return client;
	}

	public static void cancelAll(Context context)
	{
		client.cancelRequests(context, true);
	}

	public static void get(String partUrl, AsyncHttpResponseHandler handler)
	{
		client.get(partUrl, handler);
		log(new StringBuilder("GET ").append(partUrl).toString());
	}

	public static void get(String partUrl, RequestParams params, AsyncHttpResponseHandler handler)
	{
		client.get(partUrl, params, handler);
		log(new StringBuilder("GET ").append(partUrl).append("&").append(params).toString());
	}

	public static void log(String log) {
        Log.d("BaseApi", log);
//        TLog.log("Test", log);
    }

	public static void post(String partUrl, AsyncHttpResponseHandler handler)
	{
		client.post(partUrl, handler);
		log(new StringBuilder("POST ").append(partUrl).toString());
	}

	public static void post(String partUrl, RequestParams params, AsyncHttpResponseHandler handler)
	{
		client.post(partUrl, params, handler);
		log(new StringBuilder("POST ").append(partUrl).append("&").append(params).toString());
	}

	public static void setHttpClient(AsyncHttpClient c)
	{
		client = c;
		client.addHeader("Accept-Language", Locale.getDefault().toString());
//		client.addHeader("Host", HOST);
		client.addHeader("Connection", "Keep-Alive");
		client.getHttpClient().getParams().setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);

//		setUserAgent(ApiClientHelper.getUserAgent(AppContext.getInstance()));
	}


}
