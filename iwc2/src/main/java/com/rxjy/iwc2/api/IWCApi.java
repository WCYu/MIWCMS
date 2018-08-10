package com.rxjy.iwc2.api;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.rxjy.iwc2.api.bean.DeviceOnlineBean;
import com.rxjy.iwc2.api.bean.IsLowPowerBean;
import com.rxjy.iwc2.api.bean.SendErrorMsgBean;

public class IWCApi
{

	public static void IsDeviceOnline(String url, String apiKey,
			String apiSign, String timeStamp, String IMEI, String ConfVerCode,
			AsyncHttpResponseHandler handler)
	{
		RequestParams params = new RequestParams();
		DeviceOnlineBean bean = new DeviceOnlineBean();
		bean.setApiKey(apiKey);
		bean.setApiSign(apiSign);
		bean.setTimeStamp(timeStamp);
		bean.setIMEI(IMEI);
		bean.setConfVerCode(ConfVerCode);

		Gson gson = new Gson();
		String json = gson.toJson(bean);
		params.put("jsonData", json);
		ApiHttpClient.post(url, params, handler);
	}

	public static void get(String url, AsyncHttpResponseHandler handler)
	{
		ApiHttpClient.get(url, handler);
	}

	public static void UploadPicture(String url, String apiKey, String PictureFile, String apiSign,
			String timeStamp, String IMEI, String SubItemId, AsyncHttpResponseHandler handler)
	{
//		RequestParams params = new RequestParams();
//		UploadPictureBean bean = new UploadPictureBean();
//		bean.setApiKey(apiKey);
//		bean.setApiSign(apiSign);
//		bean.setTimeStamp(timeStamp);
//		bean.setIMEI(IMEI);
//		bean.setPictureFile(PictureFile);
//		bean.setSubItemId(SubItemId);
//
//		Gson gson = new Gson();
//		String json = gson.toJson(bean);
//		params.put("jsonData", json);
//		ApiHttpClient.post(url, params, handler);

		RequestParams params = new RequestParams();
		params.put("apiKey", apiKey);
		params.put("apiSign", apiSign);
		params.put("timeStamp", timeStamp);
		params.put("IMEI", IMEI);
		params.put("PictureFile", PictureFile);
		params.put("SubItemId", SubItemId);

		ApiHttpClient.post(url, params, handler);
	}

	public static void SendErrorMsg(String url, String apiKey, String ExceptionContent, String apiSign,
 			String timeStamp, String IMEI, String SubItemId, AsyncHttpResponseHandler handler)
	{
		RequestParams params = new RequestParams();
		SendErrorMsgBean bean = new SendErrorMsgBean();
		bean.setApiKey(apiKey);
		bean.setApiSign(apiSign);
		bean.setTimeStamp(timeStamp);
		bean.setIMEI(IMEI);
		bean.setExceptionContent(ExceptionContent);
		bean.setSubItemId(SubItemId);
		Gson gson = new Gson();
		String json = gson.toJson(bean);
		params.put("jsonData", json);
		ApiHttpClient.post(url, params, handler);
	}

	public static void IsLowPower(String url, String apiKey, String apiSign, String timeStamp,
			String IMEI, String SubItemId, int ElectValue, AsyncHttpResponseHandler handler)
	{
		RequestParams params = new RequestParams();
		IsLowPowerBean bean = new IsLowPowerBean();
		bean.setApiKey(apiKey);
		bean.setApiSign(apiSign);
		bean.setTimeStamp(timeStamp);
		bean.setIMEI(IMEI);
		bean.setSubItemId(SubItemId);
		bean.setElectValue(ElectValue);

		Gson gson = new Gson();
		String json = gson.toJson(bean);
		params.put("jsonData", json);
		ApiHttpClient.post(url, params, handler);
	}


}
