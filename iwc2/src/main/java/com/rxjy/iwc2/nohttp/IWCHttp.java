package com.rxjy.iwc2.nohttp;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.rxjy.iwc2.Constants;
import com.rxjy.iwc2.nohttp.bean.DeviceOnlineBean;
import com.rxjy.iwc2.nohttp.bean.IsLowPowerBean;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.Response;

/**
 * Created by qindd on 2016/12/15.
 */
public class IWCHttp
{

//    public static void IsDeviceOnline(Context context, String apiKey, String apiSign,
//               String timeStamp, String IMEI, String ConfVerCode, String ElectricityNumber, HttpListener httpListener)
//    {
//        Request<String> request = NoHttp.createStringRequest(Constants.IsDeviceOnline, RequestMethod.POST);
//        DeviceOnlineBean bean = new DeviceOnlineBean();
//        bean.setApiKey(apiKey);
//        bean.setApiSign(apiSign);
//        bean.setTimeStamp(timeStamp);
//        bean.setIMEI(IMEI);
//        bean.setConfVerCode(ConfVerCode);
//        bean.setElectricityNumber(ElectricityNumber);
//
//        Gson gson = new Gson();
//        String json = gson.toJson(bean);
//        request.add("jsonData", json);
//        CallServer.getRequestInstance().add(context, Constants.ACTION_IS_DEVICE_ONLINE, request,
//                httpListener, true, false);
//        Log.e("CoreService", "isDeviceOnline...request...");
//    }
//
//    public static void IsDeviceOnlineNew(Context context, String apiKey, String apiSign,
//                                      String timeStamp, String IMEI, String ConfVerCode, String ElectricityNumber, HttpListener httpListener)
//    {
//        Request<String> request = NoHttp.createStringRequest(Constants.IsDeviceOnline, RequestMethod.POST);
//        DeviceOnlineBean bean = new DeviceOnlineBean();
//        bean.setApiKey(apiKey);
//        bean.setApiSign(apiSign);
//        bean.setTimeStamp(timeStamp);
//        bean.setIMEI(IMEI);
//        bean.setConfVerCode(ConfVerCode);
//        bean.setElectricityNumber(ElectricityNumber);
//
//        Gson gson = new Gson();
//        String json = gson.toJson(bean);
//        request.add("jsonData", json);
//        CallServer.getRequestInstance().add(context, Constants.ACTION_IS_DEVICE_ONLINE, request,
//                httpListener, true, false);
//        Log.e("CoreService", "isDeviceOnline...request...");
//    }
//    public static void UploadPicture(Context context, String apiSign, String timeStamp,
//                                     String IMEI, String confVerCode , HttpListener httpListener)
//    {
////        // 创建请求。
////        Request<String> request = NoHttp.createStringRequest(Constants.UploadPicture, RequestMethod.POST);
////        request.add("apiKey", apiKey);
////        request.add("apiSign", apiSign);
////        request.add("timeStamp", timeStamp);
////        request.add("IMEI", IMEI);
////        request.add("PictureFile", pic_base64);
////        request.add("SubItemId", SubItemId);
////        // 调用同步请求，直接拿到请求结果。
////        Response<String> response = NoHttp.startRequestSync(request);
////
////        return response;
//
//        Request<String> request = NoHttp.createStringRequest("http://sxtapi.gc.cs/app/workorderDeviceInfo/getWorkorderDevice", RequestMethod.POST);
//
//        request.add("apiSign", apiSign);
//        request.add("timeStamp", timeStamp);
//        request.add("imei", IMEI);
//        request.add("ConfVerCode", confVerCode);
//
//        CallServer.getRequestInstance().add(context, Constants.ACTION_UPLOAD_PICTURE, request,
//                httpListener, true, false);
//    }
//
//    public static void IsLowPower(Context context, String apiKey, String apiSign, String timeStamp,
//                  String IMEI, String SubItemId, int ElectValue, HttpListener httpListener){
//        Request<String> request = NoHttp.createStringRequest(Constants.IsLowPower, RequestMethod.POST);
//        IsLowPowerBean bean = new IsLowPowerBean();
//        bean.setApiKey(apiKey);
//        bean.setApiSign(apiSign);
//        bean.setTimeStamp(timeStamp);
//        bean.setIMEI(IMEI);
//        bean.setSubItemId(SubItemId);
//        bean.setElectValue(ElectValue);
//
//        Gson gson = new Gson();
//        String json = gson.toJson(bean);
//        request.add("jsonData", json);
//        CallServer.getRequestInstance().add(context, Constants.ACTION_IS_LOW_POWER, request,
//                httpListener, true, false);
//    }


}
