package com.rxjy.iwc2.service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.rxjy.iwc2.Constants;
import com.rxjy.iwc2.nohttp.HttpListener;
import com.rxjy.iwc2.nohttp.IWCHttp;
import com.rxjy.iwc2.utils.SHA;
import com.rxjy.iwc2.utils.StringUtils;
import com.rxjy.iwc2.utils.TToast;
import com.yolanda.nohttp.Headers;
import com.yolanda.nohttp.rest.Response;
import com.yolanda.nohttp.tools.HeaderParser;
import com.yolanda.nohttp.tools.IOUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.rxjy.iwc2.Constants.leave;

/**
 * Created by qdd on 2016/9/20.
 */
public class BatteryService extends Service implements HttpListener {
    private static final String TAG = "BatteryService";
    private Context mContext;
    private int index = 0;
    private boolean isSend = true;

    private int mPowerValue;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "BatteryService__onCreate");
        IntentFilter batteryFilter = new IntentFilter();
        batteryFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(batteryReceiver, batteryFilter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        Log.e(TAG, "onStartCommand-------");
        return Service.START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
//        Log.e(TAG, "onDestroy-------");
        super.onDestroy();
        this.unregisterReceiver(batteryReceiver);
    }

    private BroadcastReceiver batteryReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mContext = context;
            if (intent.getAction().equals(Intent.ACTION_BATTERY_CHANGED)) {
                int curValue = intent.getIntExtra("level", 0);
                StringBuffer sb = new StringBuffer();
                Constants.leave = curValue;
                Log.i(TAG,"设备电量>>>>>>>>>"+Constants.leave);
                if (curValue <= StringUtils.toInt(20) && curValue > 0) {
                    if (curValue == StringUtils.toInt(20) ||
                            (StringUtils.toInt(20) - curValue) % StringUtils.toInt(10) == 0) {
//                        TToast.showS(context, "当前电量值："+curValue);
                        if (mPowerValue != curValue) {
                            sendToServer(curValue);
                        }
                        mPowerValue = curValue;
                        Constants.ElectricityNumber = mPowerValue + "";
                    }
                }
            }
        }
    };

    @SuppressLint("SimpleDateFormat")
    private void sendToServer(int ElectValue) {
//        String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
//        String value = "ElectValue" + ElectValue + "IMEI" + Constants.IMEI + "SubItemId"
//                + Constants.SubItemId + "timeStamp" + timeStamp;
//        StringBuilder sb = new StringBuilder();
//        sb.append(Constants.apiKey).append(value).append(Constants.apiSecret);
//        value = sb.toString();
//
//        IWCHttp.IsLowPower(mContext, Constants.apiKey, SHA.SHA1(value), timeStamp, Constants.IMEI,
//                Constants.SubItemId, ElectValue, this);
    }

    @Override
    public void onSucceed(int what, Response response) {
        String responseStr = IOUtils.toString(response.getByteArray(),
                HeaderParser.parseHeadValue(response.getHeaders().getContentType(),
                        Headers.HEAD_KEY_CONTENT_TYPE, ""));
        if (what == Constants.ACTION_IS_LOW_POWER) {
            JSONObject jsonObject;
            try {
                jsonObject = new JSONObject(responseStr);
                int resCode = jsonObject.getInt("StatusCode");
                if (resCode == 1) {
                    Toast.makeText(mContext, "低电量请求发送成功", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onFailed(int what, String url, Object tag, CharSequence message, int responseCode, long networkMillis) {

    }

}
