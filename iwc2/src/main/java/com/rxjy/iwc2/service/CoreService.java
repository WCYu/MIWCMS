package com.rxjy.iwc2.service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.blankj.utilcode.util.FileUtils;
import com.google.gson.Gson;
import com.rxjy.iwc2.App;
import com.rxjy.iwc2.Constants;
import com.rxjy.iwc2.MainActivity;
import com.rxjy.iwc2.api.bean.IsCaptureInfo;
import com.rxjy.iwc2.api.bean.ProjectInfo;
import com.rxjy.iwc2.api.bean.ServiceTimeInfo;
import com.rxjy.iwc2.api.bean.UpLoadPictureInfo;
import com.rxjy.iwc2.database.ImageBean;
import com.rxjy.iwc2.nohttp.bean.DeviceStateInfo;
import com.rxjy.iwc2.update.UpdateManager;
import com.rxjy.iwc2.utils.BitmapUtils;
import com.rxjy.iwc2.utils.JSONUtils;
import com.rxjy.iwc2.utils.NetUtils;
import com.rxjy.iwc2.utils.OkhttpUtils;
import com.rxjy.iwc2.utils.SHA;
import com.rxjy.iwc2.utils.SHAAfter;
import com.rxjy.iwc2.utils.SPUtil;
import com.rxjy.iwc2.utils.StringUtils;
import com.rxjy.iwc2.utils.TToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import Decoder.BASE64Encoder;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.rxjy.iwc2.Constants.imei;

/**
 * Created by qindd on 2016/12/13.
 */
public class CoreService extends Service {
    public static String TAG = "CoreService";
    private Timer httpRequestTimer;
    private HttpRequestTask httpRequestTask;
    private Timer httpRequestTimer2;
    private HttpRequestTask2 httpRequestTask2;

    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.arg1) {
                case UpdateManager.UPDATE_COMPLETE:
                    UpdateManager.silentInstall();
                    break;
                default:
                    break;
            }
        }
    };

    private final IBinder mBinder = new CoreBinder();
    private String time;
    private String orderno;
    private int areaid;
    private Map<String, Object> container;
    private String signNoParmas;
    private Map<String, Object> maps;
    private Map<String, Object> isDeviceOnlinemap;
    private String st;
    private DeviceStateInfo.ResBean res;

    public class CoreBinder extends Binder {
        public CoreService getService() {
            return CoreService.this;
        }
    }

    @Override
    public void onCreate() {
        try {
            Thread.sleep(5 * 1000);
            Log.i(TAG, "发送间隔>>>>>>>" + Constants.sendInterval);
            if (Constants.sendInterval != 0) {
                startTimer2(Constants.sendInterval * 1000);
            } else {
                startTimer2(300 * 1000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        time = System.currentTimeMillis() + "";
//        init();
        super.onCreate();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.e(TAG, "onBind");
        return mBinder;
    }

    @Override
    public void onRebind(Intent intent) {
        Log.e(TAG, "onRebind");
        super.onRebind(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.e(TAG, "onUnbind");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy");
        super.onDestroy();
//        stopTimer();
    }

//    private void init() {
//        if (!NetUtils.isWiFiConnected(App.getInstance())) {
//        }
//        time = System.currentTimeMillis() + "";
//    }

    private Timer timerJie;
    private TimerTask timerTask;

    public void startTimerJie() {

        if (timerJie == null) {
            timerJie = new Timer();
        }
        if (timerTask == null) {
            //WorkerThread不能操作UI，交给Handler处理
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    //WorkerThread不能操作UI，交给Handler处理

                }
            };
        }
        timerJie.schedule(timerTask, 1000, 60 * 1000);
    }

    private Handler handlerJie = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                String obj = (String) msg.obj;
                //到时间后，想要执行的代码
                Gson gson1 = new Gson();
                DeviceStateInfo deviceStateInfo = gson1.fromJson(obj, DeviceStateInfo.class);
                String code = deviceStateInfo.getCode();
                Log.i("tag", "code>>>>>>>>>>>>>>>>" + code);
                if (code.equals("10000")) {
                    res = deviceStateInfo.getRes();
                    parseFromService(res);
                }

            }
        }
    };

    private void parseFromService(DeviceStateInfo.ResBean res) {
        Constants.addCreater = res.getAddCreater() != null ? res.getAddCreater() : "keyBoarder";
        Constants.addTime = res.getAddTime() != 0 ? res.getAddTime() : 0;
        Constants.batteryPercentage = res.getBatteryPercentage() != 0 ? res.getBatteryPercentage() : 10;
        Constants.did = res.getDid() != 0 ? res.getDid() : 5;
        Constants.dingwei = res.getDingwei() != 0 ? res.getDingwei() : 0;
        Constants.endTime = res.getEndTime() != 0 ? res.getEndTime() : 6;
        Constants.equipmentInteractionError = res.getEquipmentInteractionError() != 0 ? res.getEquipmentInteractionError() : 0;
        Constants.id = res.getId() != 0 ? res.getId() : 2;
        imei = res.getImei() != null ? res.getImei() : "";
        Constants.imgSave_Time = res.getImgSave_Time() != 0 ? res.getImgSave_Time() : 0;
        Constants.lowPowerFailureCount = res.getLowPowerFailureCount() != 0 ? res.getLowPowerFailureCount() : 0;
        Constants.lowPowerPoint = res.getLowPowerPoint() != 0 ? res.getLowPowerPoint() : 0;
        Constants.mobileInteraction = res.getMobileInteraction() != 0 ? res.getMobileInteraction() : 0;
        Constants.motionSensitivity = res.getMotionSensitivity() != 0 ? res.getMotionSensitivity() : 0;
        Constants.orderno = res.getOrderno() != null ? res.getOrderno() : "";
        Constants.paishe = res.getPaishe() != -1 ? res.getPaishe() : 1;
        Constants.saveImageSize = res.getSaveImageSize() != 0 ? res.getSaveImageSize() : 0;
        Constants.saveResolvingPower = res.getSaveResolvingPower() != 0 ? res.getSaveResolvingPower() : 0;
        Constants.sendImageSize = res.getSendImageSize() != 0 ? res.getSendImageSize() : 0;
        Constants.sendInterval = res.getSendInterval() != 0 ? res.getSendInterval() : 1800;
        Constants.sendIntervalToImage = res.getSendIntervalToImage() != 0 ? res.getSendIntervalToImage() : 0;
        Constants.shotInterval = res.getShotInterval() != 0 ? res.getShotInterval() : 1800;
        Constants.updateCreater = res.getUpdateCreater() != null ? res.getUpdateCreater() : "";
        Constants.updateTime = res.getUpdateTime() != 0 ? res.getUpdateTime() : 0;
        Constants.versionNnumber = res.getVersionNnumber() != null ? res.getVersionNnumber() : "";
        Constants.wifiAccount = res.getWifiAccount() != null ? res.getWifiAccount() : "";
        Constants.wifiPassword = res.getWifiPassword() != null ? res.getWifiPassword() : "";

        SPUtil.put(App.getInstance(), "addCreater", Constants.addCreater);
        SPUtil.put(App.getInstance(), "addTime", Constants.addTime);
        SPUtil.put(App.getInstance(), "batteryPercentage", Constants.batteryPercentage);
        SPUtil.put(App.getInstance(), "did", Constants.did);
        SPUtil.put(App.getInstance(), "dingwei", Constants.dingwei);
        SPUtil.put(App.getInstance(), "endTime", Constants.endTime);
        SPUtil.put(App.getInstance(), "equipmentInteractionError", Constants.equipmentInteractionError);
        SPUtil.put(App.getInstance(), "id", Constants.id);
        SPUtil.put(App.getInstance(), "imei", imei);
        SPUtil.put(App.getInstance(), "imgSave_Time", Constants.imgSave_Time);
        SPUtil.put(App.getInstance(), "lowPowerFailureCount", Constants.lowPowerFailureCount);
        SPUtil.put(App.getInstance(), "lowPowerPoint", Constants.lowPowerPoint);
        SPUtil.put(App.getInstance(), "mobileInteraction", Constants.mobileInteraction);
        SPUtil.put(App.getInstance(), "motionSensitivity", Constants.motionSensitivity);
        SPUtil.put(App.getInstance(), "orderno", Constants.orderno);
        SPUtil.put(App.getInstance(), "paishe", Constants.paishe);
        Log.i(TAG, "mainTask2>>>>>>>>>>>>>>>>.paishe=====5555" + Constants.paishe);
        SPUtil.put(App.getInstance(), "saveImageSize", Constants.saveImageSize);
        SPUtil.put(App.getInstance(), "saveResolvingPower", Constants.saveResolvingPower);
        SPUtil.put(App.getInstance(), "sendImageSize", Constants.sendImageSize);
        SPUtil.put(App.getInstance(), "sendInterval", Constants.sendInterval);
        SPUtil.put(App.getInstance(), "sendIntervalToImage", Constants.sendIntervalToImage);
        SPUtil.put(App.getInstance(), "shotInterval", Constants.shotInterval);
        SPUtil.put(App.getInstance(), "startTime", Constants.startTime);
        SPUtil.put(App.getInstance(), "updateCreater", Constants.updateCreater);
        SPUtil.put(App.getInstance(), "updateTime", Constants.updateTime);
        SPUtil.put(App.getInstance(), "uploadPicFailureCount", Constants.uploadPicFailureCount);
        SPUtil.put(App.getInstance(), "versionNnumber", Constants.versionNnumber);
        SPUtil.put(App.getInstance(), "wendu", Constants.wendu);
        SPUtil.put(App.getInstance(), "wifiAccount", Constants.wifiAccount);
        SPUtil.put(App.getInstance(), "wifiPassword", Constants.wifiPassword);
        SPUtil.put(App.getInstance(), "yuying", Constants.yuying);
        Log.i("tag", "sp存储完毕");
    }


    class HttpRequestTask extends TimerTask {
        int requestRateTime;
        int requestRateTimeNew;

        public HttpRequestTask(int requestRateTime) {
            this.requestRateTime = requestRateTime;
        }

        @Override
        public void run() {
//            getDevice();

            requestRateTimeNew = StringUtils.toInt(60);
            if (requestRateTime / 1000 != requestRateTimeNew) {
//                startTimer(requestRateTimeNew * 1000);
            }
        }
    }

    private void startTimer2(int periodTime) {
        stopTimer2();
        if (httpRequestTimer2 == null)
            httpRequestTimer2 = new Timer();
        if (httpRequestTask2 == null)
            httpRequestTask2 = new HttpRequestTask2(periodTime);
        httpRequestTimer2.schedule(httpRequestTask2, 2000, periodTime);
    }

    private void stopTimer2() {
        if (httpRequestTimer2 != null) {
            httpRequestTimer2.cancel();
            httpRequestTimer2.purge();
            httpRequestTimer2 = null;
        }
        if (httpRequestTask2 != null) {
            httpRequestTask2.cancel();
            httpRequestTask2 = null;
        }
    }

    class HttpRequestTask2 extends TimerTask {
        int requestRateTime;
        int requestRateTimeNew;

        public HttpRequestTask2(int requestRateTime) {
            this.requestRateTime = requestRateTime;
        }

        @Override
        public void run() {
            Log.i(TAG, Constants.sendInterval + "Constans.sendInterval---------service");
            uploadPicRequest();
        }
    }

    private String getIMEI() {
        TelephonyManager teleManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        String imei = teleManager.getDeviceId();
        return imei;
    }

    // 时间戳
    private String getTimeStamp() {
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        return timeStamp;
    }


    /**
     * 上传照片的定时任务
     */

    private String uploadSuccessPath; // 这条路径是用来删除数据库中的数据的
    List<ImageBean> imageBeanList = new ArrayList<>();

    private void uploadPicRequest() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 查询数据库，得到需要上传的图片路径，封装成一个list
                imageBeanList = App.getInstance().imgDB.query(Constants.NOT_UPLOAD);

                Log.i(TAG, "剩余" + imageBeanList.size() + "张");
                for (int i = 0; i < imageBeanList.size(); i++) {
                    Log.i(TAG, "上传之前的列表>>>>>>>>第" + i + imageBeanList.get(i).getPath());
                }
                if (imageBeanList != null && imageBeanList.size() != 0) {
                    String photoPath;
                    int areaid = imageBeanList.get(0).getAreaid();
                    String orderno = imageBeanList.get(0).getOrderno();
                    uploadSuccessPath = photoPath = imageBeanList.get(0).getPath();
                    //获取文件 判断图片是否是完整图片
                    Log.i(TAG, "压缩完后的路径>>>>>" + photoPath);
                    File fileByPath = FileUtils.getFileByPath(photoPath);
                    if (photoPath == null) {
                        //图片有问题从数据库和文件夹删除
                        App.getInstance().imgDB.delete(imageBeanList.get(0).getPath());
                        FileUtils.deleteFile(fileByPath);
                        return;
                    } else {
                        String name = imageBeanList.get(0).getName();
                        String subString = StringUtils.getSubString(0, 8, name);
                        String subString2 = StringUtils.getSubString(9, 15, name);
                        String dateString = subString + subString2;
                        String subString11 = StringUtils.getSubString(0, 14, dateString);
                        String adobePromotionDate = getAdobePromotionDate(subString11);

                        String s = BitmapUtils.compressImageUpload(photoPath, 500, 700);

                        String imageStr = getImageStr(s);
                        Map<String, Object> maps = new HashMap<String, Object>();
                        maps.put("appsecret", Constants.appSecret);
                        maps.put("ConfVerCode", 1);
                        maps.put("timeStamp", time);
                        maps.put("imei", getIMEI());
                        maps.put("orderno", orderno);
                        maps.put("areaid", areaid);
                        String sign = SHAAfter.createSign(maps, Constants.appKey);//签名
//            Log.i("tag", "CoreService orderno>>>>>>>>>>>>>>>" + orderno);
//            Log.i("tag", "CoreService areaid>>>>>>>>>>>>>>>" + areaid);

                        final Map<String, Object> map = new HashMap<>();
                        map.put("ConfVerCode", 1);
                        map.put("photoTime", adobePromotionDate);
                        map.put("timeStamp", time);
                        map.put("imei", getIMEI());
                        map.put("orderno", orderno);
                        map.put("areaid", areaid);
                        map.put("imgBase", imageStr);
                        map.put("apiSign", sign);
                        map.put("saveResolvingPower", Constants.saveResolvingPower + "*" + Constants.saveResolvingPower);
                        map.put("saveImageSize", Constants.saveImageSize + "*" + Constants.sendImageSize);
                        map.put("size", 150);
                        OkhttpUtils.doPost(Constants.UpLoadPictureNew, map, new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                Log.i("tag", "上传照片ERROR>>>>>>>>>>>>>>." + e.getMessage());
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                String string = response.body().string();
                                Log.i(TAG, "上传照片>>>>>>>>" + string);
                                UpLoadPictureInfo upLoadPictureInfo = JSONUtils.toObject(string, UpLoadPictureInfo.class);
                                String code = upLoadPictureInfo.getCode();
                                //上传成功之后删除
                                if (code.equals("10000")) {
                                    App.getInstance().imgDB.delete(uploadSuccessPath);
                                    imageBeanList.clear();
                                    imageBeanList = App.getInstance().imgDB.query();
                                    for (int i = 0; i < imageBeanList.size(); i++) {
                                        Log.i(TAG, "上传之后的列表>>>>>" + imageBeanList.get(i).getPath());
                                    }
                                } else {
                                }
                            }
                        });
                    }

                } else {

                }
            }
        }).start();

    }


    /**
     * 判断是否是4G网络
     *
     * @param context
     * @return
     */
    public static boolean is3rd(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkINfo = cm.getActiveNetworkInfo();
        if (networkINfo != null
                && networkINfo.getType() == ConnectivityManager.TYPE_MOBILE) {
            return true;
        }
        return false;
    }

    public static String getImageStr(String imgFile) {
        InputStream inputStream = null;
        byte[] b = null;
        try {
            if (null != imgFile) {
                inputStream = new FileInputStream(imgFile);
                int count = 0;
                while (count == 0) {
                    count = inputStream.available();
                }
                b = new byte[count];
//                Log.i("tag", "imgfile============" + imgFile);
//                Log.i("tag", "count============" + count);
                inputStream.read(b);
                inputStream.close();
            } else {

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 加密
        BASE64Encoder encoder = new BASE64Encoder();
        if (null != b) {
            return encoder.encode(b);
        } else {
            return "error";
        }
    }

    public static String getAdobePromotionDate(String startTime) {
        Date d1 = null;
        SimpleDateFormat sdf1 = null;
        try {
            d1 = new SimpleDateFormat("yyyyMMddhhmmss").parse(startTime);
            sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return sdf1.format(d1);
    }
//    File file = new File(DEFAULT_SAVE_IMAGE_PATH_PARENT);
//    String[] list = file.list();
//    File[] files = file.listFiles();
//    for (int i = 0; i < files.length; i++) {
//        if(files[i].exists() && files[i].isFile()){
//            if(files[i].length()==0){
//                files[i].delete();
//            }
//        }
//    }
}
