package com.rxjy.iwc2;

import android.app.Activity;
import android.app.KeyguardManager;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PowerManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.rxjy.iwc2.api.bean.IsCaptureInfo;
import com.rxjy.iwc2.api.bean.ProjectInfo;
import com.rxjy.iwc2.api.bean.ServiceTimeInfo;
import com.rxjy.iwc2.api.bean.UpLoadPictureInfo;
import com.rxjy.iwc2.api.bean.VersionInfo;
import com.rxjy.iwc2.database.ImageBean;
import com.rxjy.iwc2.nohttp.bean.DeviceStateInfo;
import com.rxjy.iwc2.service.BatteryService;
import com.rxjy.iwc2.service.CoreService;
import com.rxjy.iwc2.service.StorageManagerService;
import com.rxjy.iwc2.utils.BitmapUtils;
import com.rxjy.iwc2.utils.JSONUtils;
import com.rxjy.iwc2.utils.NetUtil;

import com.rxjy.iwc2.utils.OkhttpUtils;

import com.rxjy.iwc2.utils.SHAAfter;
import com.rxjy.iwc2.utils.SPUtil;
import com.rxjy.iwc2.utils.StringUtils;
import com.rxjy.iwc2.utils.TToast;


import org.json.JSONException;
import org.json.JSONObject;
import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


import static com.rxjy.iwc2.Constants.imei;
import static com.rxjy.iwc2.service.CoreService.getImageStr;

/**
 * Created by 解亚鑫 on 2018/05/05.
 */
public class MainActivity extends Activity implements SurfaceHolder.Callback,
        Camera.PreviewCallback, View.OnClickListener {
    private Intent mCoreServiceIntent;
    private boolean mIsBind = false;
    private CoreService mCoreService;
    String photoPathNew;
    private TimerTask task;
    private Timer timer;
    private TimerTask task2;
    private Timer timer2;
    private Timer timerJie;
    private Timer timerJie2;
    private Timer timerJie3;
    private Timer timerJie4;

    private Camera mCamera;
    private Camera.Parameters parameters = null;
    private SurfaceView surfaceView;
    private boolean previewCallback = false;

    private Bitmap bitmap;// 原图
    private Bitmap savePicture; // 保存bitmap
    private Bitmap tempBitmap;// temp bitmap
    private Bitmap bitmap_;// 处理后的bitmap
    private Bitmap bgBitmap;// 背景图
    private Bitmap bgBitmap_;// 处理后的背景图片
    private long times = 0l;// 连续多久不动时间
    private long indexFrame = 0l;// 帧回调次数
    private long times_ = 0l;// temp time
    private int police_time = 10;// 确认障碍物开始(手机抖动)  //此处为了省电改成 3，默认10
    private int police_time_ = 3;// 确认障碍物              //此处为了省电改成 1，默认3
    private long times_2 = 0l;// 报警确认次数
    //    private int Rgb = 200; // 阀值 动作识别灵敏度(初始1000)
    int i = 1;// 持续监测累加次数

    private Camera.Size cameraCalSize;

    private Camera.PictureCallback jpegCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            Log.e("储存图片", "成功");
//            Log.i("MainActivity", "jpegCallback");
            if (camera.getParameters().getPictureFormat() == 256) {
                save(data, camera); // 保存图片
            }
        }
    };
    private Camera.PictureCallback jpegCallback2 = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
//            Log.i("MainActivity", "jpegCallback2");
            if (camera.getParameters().getPictureFormat() == 256) {
                getPhotoPath(data);
                camera.startPreview();
            }
        }
    };
    // OpenCV库加载并初始化成功后的回调函数
    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case BaseLoaderCallback.SUCCESS:
                    Log.i(TAG, "OpenCV 成功加载");
                    previewCallback = true;
                    break;
                default:
                    super.onManagerConnected(status);
                    Log.i(TAG, "OpenCV 加载失败");
                    break;
            }
        }
    };
    private Map<String, Object> container;
    private String time;
    private Map<String, Object> maps;
    private String time1;
    private String orderno1;
    private int areaid1;
    private int mCurrentTimer;
    private String st1;
    private Map<String, Object> isDeviceOnlinemap;
    private long timeC;
    private long timeInMillis;
    private Button btn_check;
    private DeviceStateInfo.ResBean res;
    private int news;
    private long l;
    private SharedPreferences.Editor edit;
    private SharedPreferences data1;
    private SurfaceHolder surfaceHolder = null;
    private int anInt;
    private int anInt1;


    private Camera.Size getPictureSize() {
        Camera.Size localSize = this.mCamera.getParameters().getPreviewSize();
        List localList = this.mCamera.getParameters().getSupportedPictureSizes();
        localSize.width = 0;
        localSize.height = 0;
        for (int j = 0; ; j++) {
            if (j >= localList.size())
                return localSize;
            int k = ((Camera.Size) localList.get(j)).width * ((Camera.Size) localList.get(j)).height;
            if (localSize.width * localSize.height < k)
                localSize = (Camera.Size) localList.get(j);
        }
    }

    private Camera.Size getPreviewSize() {
        Camera.Size localSize = this.mCamera.getParameters().getPreviewSize();
        List localList = this.mCamera.getParameters().getSupportedPreviewSizes();
        localSize.width = 0;
        localSize.height = 0;
        for (int j = 0; ; j++) {
            if (j >= localList.size())
                return localSize;
            int k = ((Camera.Size) localList.get(j)).width * ((Camera.Size) localList.get(j)).height;
            if (localSize.width * localSize.height < k)
                localSize = (Camera.Size) localList.get(j);
        }
    }
    public static TextView tv_flag;
    public static TextView tv_index;
    public static TextView tv_pic_count;
    public static TextView tv_rgb;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e("摄像头", "启动成功");
//        autoDownloadAPK();
        com.blankj.utilcode.util.Utils.init(this);
        OpenCVLoader.initDebug();
        surfaceView = (SurfaceView) findViewById(R.id.surface_view);
        surfaceView.getHolder().addCallback(this);
        surfaceView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        surfaceView.getHolder().setFixedSize(176, 144); // 设置Surface分辨率
        surfaceView.getHolder().setKeepScreenOn(true);// 屏幕常亮
        // 唤醒屏幕并保持常亮
        wakeUpAndUnlock(this);
        tv_flag = (TextView) findViewById(R.id.tv_flag);
        btn_check = (Button) findViewById(R.id.btn_check);
        btn_check.setOnClickListener(this);
        tv_pic_count = (TextView) findViewById(R.id.tv_pic_count);
        tv_rgb = (TextView) findViewById(R.id.tv_rgb);
        String time = System.currentTimeMillis() + "";
        maps = new HashMap<String, Object>();
        maps.put("appsecret", Constants.appSecret);//
        maps.put("ConfVerCode", "0.0");
        maps.put("timeStamp", time);
        maps.put("imei", getIMEI());
        String signNoParmas = SHAAfter.createSign(maps, Constants.appKey);
        container = new HashMap<String, Object>();
        container.put("imei", getIMEI());
        container.put("ConfVerCode", "0.0");
        container.put("timeStamp", time);
        container.put("apiSign", signNoParmas);
        //捕获异常handler
//        Thread.setDefaultUncaughtExceptionHandler(this);
        SharedPreferences data = this.getSharedPreferences("data", this.MODE_PRIVATE);
        edit = data.edit();
        startTimerJie();//获取设备参数、检测设备是否在线  15分钟
        startTimerJie2();//获取抓拍  50秒
        startTimerJie3();//获取服务器时间 1个小时
        data1 = getSharedPreferences("data", MODE_PRIVATE);

        if (NetUtil.isConnected(MainActivity.this)) {
            Log.e("摄像头", "网络正常");
            getParaments();
        } else {
            //没网络 从SP中获取拍摄间隔
            Log.e("摄像头", "无网络");
            int shotInterval = data1.getInt("shotInterval", 0);
            //第一次启动 参数为0
            if (shotInterval != 0) {
                Log.e("摄像头", "本地储存拍照时间");
                startTimer(shotInterval);
            } else {
                Log.e("摄像头", "默认参数 半小时拍摄一次");
                //默认参数 半小时拍摄一次
                startTimer(1800);
            }
            init();
            timeC = data1.getLong("timeC", 0);
        }
    }

    private void getSystemTime() {
        OkhttpUtils.doPost(Constants.GETSERVICETIME, container, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String string = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Gson gson1 = new Gson();
                        ServiceTimeInfo serviceTimeInfo = gson1.fromJson(string, ServiceTimeInfo.class);
                        long date = serviceTimeInfo.getRes().getDate();
                        String st = serviceTimeInfo.getRes().getSt();
                        Calendar calendar = Calendar.getInstance();
                        try {
                            calendar.setTime(new SimpleDateFormat("yyyyMMdd_HHmmss").parse(st));
                            timeInMillis = calendar.getTimeInMillis();
                            Date date6 = new Date(timeInMillis);
                            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd_HHmmss");
                            String format1 = sdf1.format(date6);
                            l = System.currentTimeMillis();
                            timeC = date - l;

                            edit.putLong("timeC", timeC);
                            edit.commit();
                            Date date5 = new Date(l);
                            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd_HHmmss");
                            String format2 = sdf2.format(date5);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

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
                    time = System.currentTimeMillis() + "";
                    Map<String, Object> maps = new HashMap<String, Object>();
                    maps.put("appsecret", Constants.appSecret);
                    maps.put("ConfVerCode", 1);
                    maps.put("timeStamp", time);
                    maps.put("imei", getIMEI());
                    String signNoParmas = SHAAfter.createSign(maps, Constants.appKey);
                    container = new HashMap<String, Object>();
                    container.put("imei", getIMEI());
                    container.put("ConfVerCode", 1);
                    container.put("timeStamp", time);
                    container.put("apiSign", signNoParmas);
                    OkhttpUtils.doPost(Constants.GetDeviceParamet, container, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {

                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            final String string = response.body().string();
                            Message msg = new Message();
                            msg.obj = string;
                            msg.what = 1;
                            handlerJie.sendMessage(msg);
                        }
                    });

                    Map<String, Object> maps1 = new HashMap<String, Object>();
                    maps1.put("appsecret", Constants.appSecret);
                    maps1.put("ConfVerCode", 1);
                    maps1.put("timeStamp", time);
                    maps1.put("imei", getIMEI());
                    maps1.put("power", Constants.leave);
                    maps1.put("lon", 42.5);
                    maps1.put("lat", 42.3);
                    String sign = SHAAfter.createSign(maps1, Constants.appKey);
                    isDeviceOnlinemap = new HashMap<>();
                    isDeviceOnlinemap.put("imei", getIMEI());
                    isDeviceOnlinemap.put("ConfVerCode", 1);
                    isDeviceOnlinemap.put("timeStamp", time);
                    isDeviceOnlinemap.put("apiSign", sign);
                    isDeviceOnlinemap.put("orderno", Constants.orderno);
                    isDeviceOnlinemap.put("power", Constants.leave);
//                    Log.i(TAG, "设备电量>>>>>>>>>>>>>>>>>>>>" + Constants.leave);
                    isDeviceOnlinemap.put("lon", 42.5);
                    isDeviceOnlinemap.put("lat", 42.3);

                    OkhttpUtils.doPost(Constants.IsDeviceOnlineNew, isDeviceOnlinemap, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {

                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String string = response.body().string();
                            Message msg = new Message();
                            msg.obj = string;
                            msg.what = 3;
                            handlerJie.sendMessage(msg);
                        }
                    });
                }
            };
        }
        timerJie.schedule(timerTask, 1000, 90 * 1000);
    }

    private Handler handler3 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 4:
                    String obj = (String) msg.obj;
                    ServiceTimeInfo serviceTimeInfo = JSONUtils.toObject(obj, ServiceTimeInfo.class);
                    long date = serviceTimeInfo.getRes().getDate();
                    long l = System.currentTimeMillis();
                    timeC = date - l;
                    edit.putLong("timeC", timeC);
                    edit.commit();
                    break;
                case 5:
                    String string = (String) msg.obj;
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(string);
                        Gson gson = new Gson();
                        ProjectInfo projectInfo = gson.fromJson(string, ProjectInfo.class);
                        String code = projectInfo.getCode();
                        if (code.equals("10000")) {
                            areaid1 = projectInfo.getRes().getAreaid();
                            orderno1 = projectInfo.getRes().getOrderno();

                            edit.putString("orderno", orderno1);
                            edit.putInt("areaid", areaid1);
                            edit.commit();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };
    private Handler handler2 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 2:
                    String obj1 = (String) msg.obj;
                    Gson gson = new Gson();
                    IsCaptureInfo isCaptureInfo1 = gson.fromJson(obj1, IsCaptureInfo.class);
                    String code1 = isCaptureInfo1.getCode();
                    if (code1.equals("10000")) {
                        int capture = isCaptureInfo1.getRes().getCapture();
                        if (capture == 1) {
                            mCamera.takePicture(null, null, jpegCallback2);
                        } else if (capture == 0) {
                        }
                    }
                    break;
            }
        }
    };
    private TimerTask timerTask2;

    public void startTimerJie2() {

        if (timerJie2 == null) {
            timerJie2 = new Timer();
        }
        if (timerTask2 == null) {
            //WorkerThread不能操作UI，交给Handler处理
            timerTask2 = new TimerTask() {
                @Override
                public void run() {
                    OkhttpUtils.doPost(Constants.IsCapture, container, new okhttp3.Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {

                        }

                        @Override
                        public void onResponse(Call call, final okhttp3.Response response) throws IOException {

                            final String string = response.body().string();
                            Message msg = new Message();
                            msg.obj = string;
                            msg.what = 2;
                            handler2.sendMessage(msg);
                        }
                    });

                }
            };
        }
        timerJie2.schedule(timerTask2, 1000, 50 * 1000);
    }

    private TimerTask timerTask3;

    public void stopTimerJie3() {
        if (timerTask3 != null) {
            timerTask3.cancel();
            timerTask3 = null;
        }
        if (timerJie3 != null) {
            timerJie3.cancel();
            timerJie3.purge();
            timerJie3 = null;
        }
    }

    private TimerTask timerTask4;

    public class TimerTask3 extends TimerTask {
        @Override
        public void run() {
            OkhttpUtils.doPost(Constants.GETSERVICETIME, container, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String string = response.body().string();
                    Message msg = new Message();
                    msg.obj = string;
                    msg.what = 4;
                    handler3.sendMessage(msg);
                }
            });
            OkhttpUtils.doPost(Constants.ProjectIsDevice, container, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                }

                @Override
                public void onResponse(Call call, final okhttp3.Response response) throws IOException {
                    final String string = response.body().string();
                    Message msg = new Message();
                    msg.obj = string;
                    msg.what = 5;
                    handler3.sendMessage(msg);
                }

            });
        }
    }

    public void startTimerJie3() {
        stopTimerJie3();
        if (timerJie3 == null) {
            timerJie3 = new Timer(true);
        }
        if (timerTask3 == null) {
            timerTask3 = new TimerTask3();
        }
        timerJie3.schedule(timerTask3, 2 * 1000, 3600 * 1000);
    }

    private Handler handlerJie = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    String obj = (String) msg.obj;
                    //到时间后，想要执行的代码
                    Log.i(TAG, "设备参数：================" + obj);
                    Gson gson1 = new Gson();
                    DeviceStateInfo deviceStateInfo = gson1.fromJson(obj, DeviceStateInfo.class);
                    String code = deviceStateInfo.getCode();
                    if (code.equals("10000")) {
                        res = deviceStateInfo.getRes();
                        int shotInterval = res.getShotInterval();
                        if(Constants.shotInterval == shotInterval){
                            Log.e("摄像头","拍照间隔未改变");
                        }else {
                            Log.e("摄像头","拍照间隔发生改变");
                            parseFromService(res);
                            startTimer(Constants.shotInterval);
                        }
                        news = res.getShotInterval();
                    }
                    Log.e(TAG, "摄像头 拍照间隔." + obj);
                    break;
                case 3:
                    String obj2 = (String) msg.obj;
                    Gson gson = new Gson();
                    DeviceStateInfo deviceStateInf = gson.fromJson(obj2, DeviceStateInfo.class);
                    String cod = deviceStateInf.getCode();
                    Log.e(TAG, "设备的实时状态>>>>>>>>>>>>>>>>>." + obj2.toString());
                    break;
                default:
                    break;
            }
        }

    };

    private String getIMEI() {
        TelephonyManager teleManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        String imei = teleManager.getDeviceId();
        return imei;
    }

    private void init() {
        // 照相机预览的控件

        // 启动服务
        mCoreServiceIntent = new Intent(this, CoreService.class);
        startCoreService();
        // 写一个服务用来删除多余照片
        startService(new Intent(this, StorageManagerService.class));
        // 启动电量服务
        startService(new Intent(this, BatteryService.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
//            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_1_0, this, mLoaderCallback);
        } else {
//            Log.d(TAG, "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopCoreService();

    }

//    //捕获异常
//    @Override
//    public void uncaughtException(Thread thread, Throwable ex) {
//        Log.i(TAG, "EXCEPTION>>>>>>>>>>>>>" + ex.getMessage());
//    }

    private String timeNew;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_check:
                tv_flag.setText(timeNew);
                if (NetUtil.isConnected(MainActivity.this)) {
                    tv_rgb.setText("网络正常");
                } else {
                    tv_rgb.setText("无网络连接");
                }
                break;
        }
    }

    public void startCoreService() {
        doBindService();
    }

    public void stopCoreService() {
        doUnbindService();
    }

    private void doBindService() {
        if (App.getCurProcessName(this).equals(getPackageName())) {
            bindService(mCoreServiceIntent, serviceConnection, Context.BIND_AUTO_CREATE);
            mIsBind = true;
        }
    }

    private void doUnbindService() {
        if (mIsBind) {
            unbindService(serviceConnection);
            mIsBind = false;
        }
    }

    ServiceConnection serviceConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName name, IBinder service) {
            mCoreService = ((CoreService.CoreBinder) service).getService();
        }

        public void onServiceDisconnected(ComponentName name) {
            mCoreService = null;
        }
    };

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        surfaceHolder = holder;
        Log.e(TAG, "surfaceCreated();");
        try {
            mCamera = Camera.open(0); // 开启摄像头
            mCamera.setPreviewDisplay(holder);// 设置用于显示拍照影像的SurfaceHolder对象
            mCamera.setDisplayOrientation(getPreviewDegree(MainActivity.this));
            mCamera.startPreview(); // 开始预览
            cameraCalSize = getPictureSize();//获取保存图片最大size
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.e(TAG, "surfaceChanged();");
        try {
            parameters = mCamera.getParameters();
            mCamera.setParameters(parameters);
            parameters.setPictureFormat(ImageFormat.JPEG); // 设置图片格式
            parameters.setPreviewSize(width, height); // 设置预览大小
            parameters.setPictureSize(width, height); // 设置保存的图片尺寸
            anInt = width;
            anInt1 = height;
            parameters.setJpegQuality(100); // 设置照片质量
            // 此处为自己添加
//            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
//            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
            mCamera.setPreviewCallback(this);
            // 聚焦
//            startFocus();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.e(TAG, "surfaceDestroyed();");
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.setPreviewCallback(null);
            mCamera.release(); // 释放照相机
            mCamera = null;
        }
    }

    private boolean isPreviewFrame = true;
    private Handler handler = new Handler();

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            isPreviewFrame = true;
        }
    };

    /**
     * Called as preview frames are displayed.  This callback is invoked
     * on the event thread was called from.
     */
    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        if (isPreviewFrame) {
            // 动作识别(取模跳帧)
            if (indexFrame++ % 10 != 0) // 刷新次数逢6的倍数才往下执行，否则都return
                return;
            Camera.Size size = camera.getParameters().getPreviewSize();
//            Camera.Size size = camera.getParameters().getSupportedPictureSizes();
            try {
                YuvImage image = new YuvImage(data, ImageFormat.NV21, size.width, size.height, null);
                if (image != null) {
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    image.compressToJpeg(new Rect(0, 0, size.width, size.height), 100, stream);
                    // 得到bitmap
                    Bitmap bmp = BitmapFactory.decodeByteArray(stream.toByteArray(), 0, stream.size());
                    // **********************
                    // 因为图片会放生旋转，因此要对图片进行旋转到和手机在一个方向上
                    bmp = rotatingImageView(-270, bmp);
                    savePicture = bmp; // 为了保存原图
                    // 图片太大处理较慢，就把图片缩放裁剪
                    Matrix matrix = new Matrix();
                    matrix.postScale(0.125f, 0.125f); // 长和宽缩小的比例  (160 × 90), 实际上的像素点好像更少
//                  matrix.postScale(0.25f, 0.25f); // 长和宽缩小的比例  (160 × 90), 实际上的像素点好像更少
                    if (previewCallback == true) {
                        bitmap = bmp.createBitmap(bmp, 0, 0, size.height, size.width, matrix, true);//创建图片
                        bitmap_ = procSrc2Gray(bitmap);// 灰度
                        bitmap_ = changeBitmap(bitmap);// 二值
                        // 识别
                        getPolice();

//                        Toast.makeText(mCoreService, "执行拍照任务-----------"+Constants.shotInterval, Toast.LENGTH_SHORT).show();
                    }
                    // **********************************
                    stream.close();
                }
            } catch (Exception ex) {
//                Log.e("Sys", "Error:" + ex.getMessage());
            }
        } else {

        }
    }

//    class MainTask extends TimerTask {
//        @Override
//        public void run() {
//            if (mCamera == null) {
////                Log.e("tag", "MainTask---run()_1");
//                mCamera = Camera.open(0); // 开启摄像头
//                mCamera.setDisplayOrientation(getPreviewDegree(MainActivity.this));
//                mCamera.startPreview(); // 开始预览
////                Log.e("tag", "MainTask---run()_2");
//            }
//        }
//
//    }

    // 自动对焦
    Timer focusTimer = new Timer(true);

    TimerTask focusTask = new TimerTask() {

        @Override
        public void run() {
            if (mCamera != null)
                mCamera.autoFocus(null);
        }
    };

    public void startFocus() {
        focusTimer.schedule(focusTask, 0, 300 * 1000);
    }

    // 提供一个静态方法，用于根据手机方向获得相机预览画面旋转的角度
    public static int getPreviewDegree(Activity activity) {
        // 获得手机的方向
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        int degree = 0;
        // 根据手机的方向计算相机预览画面应该选择的角度
        switch (rotation) {
            case Surface.ROTATION_0:
                degree = 90;
                break;
            case Surface.ROTATION_90:
                degree = 0;
                break;
            case Surface.ROTATION_180:
                degree = 270;
                break;
            case Surface.ROTATION_270:
                degree = 180;
                break;
        }
        return degree;
    }

    // 旋转
    public Bitmap rotatingImageView(int angle, Bitmap bitmap) {
        // 旋转图片 动作
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        // 创建新的图片
        Bitmap bm = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return bm;
    }

    private boolean mIsSurfaceCreated = false;
    private boolean mIsTimerRunning = false;

    // 识别系统
    public void getPolice() {
//        Log.i(TAG, "测试TIMER2");
        if (tempBitmap != null && times <= police_time) { // police_time = 10
            if (isEquals(tempBitmap, bitmap_) == true) {
                times++;
                times_2 = 0; // 确认报警次数
                if (times < police_time) {
//                    Log.e(TAG, "number:持续不动次数" + times + ";");
                    App.displayToast(this, "持续不动" + times + "次");
                }
            } else if (isEquals(tempBitmap, bitmap_) == false && times > 0) {
//                Log.e(TAG, "手机在动！");
                App.displayToast(this, "手机在动！");
                times_ = 0;
            }
        }

        if (tempBitmap != null && times < police_time) { // 重置times
            if (isEquals(tempBitmap, bitmap_) == false) {
                times = times_;
            }
        }

        tempBitmap = bitmap_;                   // 更新背景图
        if (times == police_time) {
            bgBitmap = bitmap;                  // 背景图
            bgBitmap_ = procSrc2Gray(bgBitmap); // 灰度
            bgBitmap_ = changeBitmap(bgBitmap); // 二值

            App.displayToast(this, "开始执行程序");
        }

        if (times > police_time) {
            Toast.makeText(mCoreService, "Constants.shotInterval" + Constants.shotInterval, Toast.LENGTH_SHORT).show();
            //  有结果，则终止线程
            if (isEquals(bgBitmap_, bitmap_) == false) {
                times_2++; // 障碍物确认次数
                if (times_2 > 0) {
//                    Log.e(TAG, "确认持续次数：" + times_2);
                    App.displayToast(this, "确认障碍物" + times_2 + "次");
                }
                if (times_2 > police_time_) {

                    // police_time_ = 5
                    // 有结果，则终止线程
                    this.focusTimer.cancel();
                    times_2 = times_;// 确认之后清零，重新确认障碍物
                    // 保存图片
//                    saveBitmapToFile(savePicture);
                    times = 0;
                    police_time = 5;
                    isPreviewFrame = false;
                    i++;
                    App.displayToast(this, "开始第" + i + "次检测");
                }
            } else {
//                Log.e(TAG, "无障碍物！");
                times_2 = 0;
                App.displayToast(this, "无障碍物！");
            }
        }
    }

    // 比较图片
    public boolean isEquals(Bitmap b1, Bitmap b2) {
        int xCount = b1.getWidth();//获取x轴个数
        int yCount = b1.getHeight();//获取y轴个数
        int number = 0;
        for (int x = 0; x < xCount; x++) {
            for (int y = 0; y < yCount; y++) {
                // 比较每个像素点颜色
                if (b1.getPixel(x, y) != b2.getPixel(x, y)) {
                    number++;
                }
            }
        }
//        Log.e(TAG, "差异值：" + number);
        // displayToast("差异值："+ number);
        if (number < Constants.Rgb)
            return true;
        return false;
    }

    public boolean isEquals2(Bitmap b1, Bitmap b2) {
        int x = b1.getWidth();
        int y = b1.getHeight();
        int m = 0;
        int n = 0;
        if (n >= x) {
//            Log.e("MainActivity", "差异值：" + m + ";总数：" + x * y);
            if (m < Constants.Rgb)
                return true;
        } else {
            for (int i1 = 0; ; i1++) {
                if (i1 >= y) {
                    n++;
                    break;
                }
                if (b1.getPixel(n, i1) != b2.getPixel(n, i1))
                    m++;
            }
        }
        return false;
    }

    // 灰度化
    public Bitmap procSrc2Gray(Bitmap bm) {
        Mat rgbMat = new Mat(); // 声明Mat 灰度化处理是不能用bitmap 得用mat
        Mat grayMat = new Mat();
        Mat mat = new Mat();
        Bitmap grayBmp = Bitmap.createBitmap(bm.getWidth(), bm.getHeight(), Bitmap.Config.ARGB_8888);
        Utils.bitmapToMat(bm, rgbMat);// convert original bitmap to Mat, R G B.bitmap转mat
        Imgproc.cvtColor(rgbMat, mat, Imgproc.COLOR_RGB2HLS);// 亮度
        Imgproc.cvtColor(rgbMat, grayMat, Imgproc.COLOR_RGB2GRAY);// 灰度化方法
        Utils.matToBitmap(mat, grayBmp);
        return grayBmp;
    }

    // 二值化
    public Bitmap changeBitmap(Bitmap bm) {
        Mat rgbMat = new Mat();
        Mat grayMat = new Mat();
        Bitmap grayBmp = Bitmap.createBitmap(bm.getWidth(), bm.getHeight(), Bitmap.Config.ARGB_8888);
        Utils.bitmapToMat(bm, rgbMat);
        Imgproc.threshold(rgbMat, grayMat, 100, 255, Imgproc.THRESH_BINARY);
        Utils.matToBitmap(grayMat, grayBmp);
        return grayBmp;
    }

    Map<String, Object> map = new HashMap<String, Object>();

    // 储存图片
    public void save(final byte[] data, Camera camera) {
        if (data != null) {
            long l = System.currentTimeMillis();
            long newTime = l + timeC;
            Date date5 = new Date(newTime);
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd_HHmmss");
            String format2 = sdf2.format(date5);

            String subString1 = StringUtils.getSubString(0, 8, format2);
            String subString21 = StringUtils.getSubString(9, 15, format2);
            String dateString1 = subString1 + subString21;
            String subString12 = StringUtils.getSubString(0, 14, dateString1);
            String adobePromotionDate1 = getAdobePromotionDate(subString12);
            map.put("imei", getIMEI());
            map.put("finallyTime", adobePromotionDate1);
            if (Constants.paishe == 1) {
                File picFile = new File(Constants.DEFAULT_SAVE_IMAGE_PATH_PARENT);
                picFile = new File(picFile, "iwc_img");
                if (!picFile.exists() && !picFile.mkdirs()) {
                    TToast.showL(MainActivity.this, "无法创建日期文件夹");
                    return;
                }

                String photoName = format2 + ".jpg";
                String photoPath = "";
                if (picFile != null) {
                    photoPath = picFile.getPath() + File.separator + photoName;
                }
                try {
                    FileOutputStream fos = new FileOutputStream(photoPath);
                    fos.write(data);
                    fos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                OkhttpUtils.doPost(Constants.finallyUploadTime, map, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String string1 = response.body().string();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                            }
                        });
                    }
                });

                ImageBean imgBean = new ImageBean();
                imgBean.setName(photoName);
                imgBean.setPath(photoPath);
                imgBean.setTime(format2);
                imgBean.setRescode(Constants.NOT_UPLOAD); // 0
                SharedPreferences sp = getSharedPreferences("data", MODE_PRIVATE);
                String orderno2 = sp.getString("orderno", "");
                int areaid = sp.getInt("areaid", 0);
                imgBean.setOrderno(orderno2);
                imgBean.setAreaid(areaid);
                App.getInstance().imgDB.insert(imgBean);
                if (camera != null) {
                    try {
                        Log.e("摄像头", "----请求预览----成功");
                        camera.startPreview();
                    } catch (Exception e) {
                        Log.e("摄像头", "----请求失败----"+e.getMessage());
                    }
                } else {

                }

                if (mCamera != null) {
                    try {
                        Log.e("摄像头", "---关闭资源---成功");
                        mCamera.stopPreview();
                        mCamera.setPreviewCallback(null);
                        mCamera.release(); // 释放照相机
                        mCamera = null;
                    } catch (Exception e) {
                        Log.e("摄像头", "---关闭资源---失败" + e.toString());
                    }
                }
            } else {

            }
        }
    }

    public void getPhotoPath(byte[] data) {
        File picFile = new File(Constants.DEFAULT_SAVE_IMAGE_PATH_PARENT);
        picFile = new File(picFile, "iwc_img2");
        if (!picFile.exists() && !picFile.mkdirs()) {
            TToast.showL(MainActivity.this, "无法创建日期文件夹");
        }
        String photoName = "iwc" + System.currentTimeMillis() + ".jpg";
        if (picFile != null) {
            photoPathNew = picFile.getPath() + File.separator + photoName;
        }
        try {
            FileOutputStream fos = new FileOutputStream(photoPathNew);
            fos.write(data);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
//        Log.i(TAG, "压缩完后的路径>>>>>" + photoPathNew);
        if (photoPathNew != null) {
            String s = BitmapUtils.compressImageUpload(photoPathNew, 300, 300);
            Log.i(TAG, "开始上传抓拍>>>>>" + photoPathNew);
            String imageStr = getImageStr(s);
            Map<String, Object> maps = new HashMap<String, Object>();
            maps.put("appsecret", Constants.appSecret);
            maps.put("ConfVerCode", 1);
            maps.put("timeStamp", time);
            maps.put("imei", getIMEI());

            String sign = SHAAfter.createSign(maps, Constants.appKey);//签名

            final Map<String, Object> map = new HashMap<>();
            map.put("ConfVerCode", 1);
            map.put("timeStamp", time);
            map.put("imei", getIMEI());
            map.put("imgBase", imageStr);
            maps.put("orderno", "");
            maps.put("areaid", 0);
            map.put("apiSign", sign);
            map.put("saveResolvingPower", Constants.saveResolvingPower + "*" + Constants.saveResolvingPower);
            map.put("saveImageSize", Constants.sendImageSize + "*" + Constants.sendImageSize);
            map.put("size", 150);
            OkhttpUtils.doPost(Constants.UpLoadPictureZ, map, new okhttp3.Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
//                    Log.i("tag", "ERROR>>>>>>>>>>>>>>." + e.getMessage());
                }

                @Override
                public void onResponse(Call call, okhttp3.Response response) throws IOException {
                    final String string = response.body().string();
                    Log.i("tag", "抓拍上传照片.>>>>>>>>>>===============================" + string);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Gson gson = new Gson();
                            UpLoadPictureInfo upLoadPictureInfo = gson.fromJson(string, UpLoadPictureInfo.class);
                            String code = upLoadPictureInfo.getCode();
                            //上传成功之后删除
                            if (code.equals("10000")) {
                                Log.i("tag", "抓拍上传成功");
                                mCamera.stopPreview();
                                mCamera.setPreviewCallback(null);
                                mCamera.release(); // 释放照相机
//                                mCamera = null;
                            } else {

                            }
                        }
                    });
                }
            });
        }
    }


    //解锁
    private void wakeUpAndUnlock(Context context) {
        KeyguardManager km = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        KeyguardManager.KeyguardLock kl = km.newKeyguardLock("unLock");
        //解锁
        kl.disableKeyguard();
        //获取电源管理器对象
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        //获取PowerManager.WakeLock对象,后面的参数|表示同时传入两个值,最后的是LogCat里用的Tag
        PowerManager.WakeLock mWakeLock = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_DIM_WAKE_LOCK, "bright");
        //点亮屏幕
        mWakeLock.acquire();
    }

    private void parseFromService(DeviceStateInfo.ResBean res) {
        Constants.addCreater = res.getAddCreater() != null ? res.getAddCreater() : "keyBoarder";
        Constants.addTime = res.getAddTime() != 0 ? res.getAddTime() : 0;
        Constants.batteryPercentage = res.getBatteryPercentage() != 0 ? res.getBatteryPercentage() : 0;
        Constants.did = res.getDid() != 0 ? res.getDid() : 0;
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
//        Log.i(TAG, "mainTask2>>>>>>>>>>>>>>>>.paishe=====4444" + Constants.paishe);
        Constants.saveImageSize = res.getSaveImageSize() != 0 ? res.getSaveImageSize() : 0;
        Constants.saveResolvingPower = res.getSaveResolvingPower() != 0 ? res.getSaveResolvingPower() : 0;
        Constants.sendImageSize = res.getSendImageSize() != 0 ? res.getSendImageSize() : 0;
        Constants.sendInterval = res.getSendInterval() != 0 ? res.getSendInterval() : 1800;
        Constants.sendIntervalToImage = res.getSendIntervalToImage() != 0 ? res.getSendIntervalToImage() : 0;
        Constants.shotInterval = res.getShotInterval() != 0 ? res.getShotInterval() : 1800;
        Log.e("摄像头", "更新本地储存拍照时间"+Constants.shotInterval);
        news = res.getShotInterval() != 0 ? res.getShotInterval() : 1800;
        Constants.startTime = res.getStartTime() != 0 ? res.getStartTime() : 6;
        Constants.updateCreater = res.getUpdateCreater() != null ? res.getUpdateCreater() : "";
        Constants.updateTime = res.getUpdateTime() != 0 ? res.getUpdateTime() : 0;
        Constants.uploadPicFailureCount = res.getUploadPicFailureCount() != 0 ? res.getUploadPicFailureCount() : 0;
        Constants.versionNnumber = res.getVersionNnumber() != null ? res.getVersionNnumber() : "";
        Constants.wendu = res.getWendu() != 0 ? res.getWendu() : 0;
        Constants.wifiAccount = res.getWifiAccount() != null ? res.getWifiAccount() : "";
        Constants.wifiPassword = res.getWifiPassword() != null ? res.getWifiPassword() : "";
        Constants.yuying = res.getYuying() != 0 ? res.getYuying() : 0;

        edit.putInt("shotInterval", res.getShotInterval());

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
        init();
    }


    private void isTakePicture() {
        long l = System.currentTimeMillis();
        long newTime = l + timeC;
        Date date5 = new Date(newTime);
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String format2 = sdf2.format(date5);
        timeNew = format2;
        String subString21 = StringUtils.getSubString(9, 10, format2);
        String subString = StringUtils.getSubString(0, 2, subString21);
        int integer = Integer.valueOf(subString);
        Log.e("摄像头", "拍照区间"+integer+"起"+ Constants.startTime+"终"+ Constants.endTime);
        if (isInTimePart(integer, Constants.startTime, Constants.endTime)) {
            Log.e("摄像头", "拍照---开始---");
            if (mCamera != null) {
                Log.e("摄像头", "拍照---成功---");
                mCamera.takePicture(null, null, jpegCallback);
            } else {
                Log.e("摄像头", "拍照---重新启动---");
                try {
                    if (surfaceHolder != null) {
                        Log.e("摄像头", "拍照---重新成功---");
                        mCamera = Camera.open(0); // 开启摄像头
                        mCamera.setPreviewDisplay(surfaceHolder);// 设置用于显示拍照影像的SurfaceHolder对象
                        mCamera.setDisplayOrientation(getPreviewDegree(MainActivity.this));
                        mCamera.startPreview(); // 开始预览
                        cameraCalSize = getPictureSize();//获取保存图片最大size
                        parameters = mCamera.getParameters();
                        mCamera.setParameters(parameters);
                        parameters.setPictureFormat(ImageFormat.JPEG); // 设置图片格式
                        parameters.setPreviewSize(anInt, anInt1); // 设置预览大小
                        parameters.setPictureSize(anInt, anInt1); // 设置保存的图片尺寸
                        parameters.setJpegQuality(100); // 设置照片质量
                        // 此处为自己添加
//            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
//            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
                        mCamera.setPreviewCallback(this);
                        mCamera.takePicture(null, null, jpegCallback);
                    } else {
                        mCamera.takePicture(null, null, jpegCallback);
                        Log.e("对象为空", "失败");
                    }
                } catch (Exception e) {
                    Log.e("异常报错", "失败" + e.toString());
                }
            }
        } else {
            Log.e("摄像头", "---拍照---未在拍照区间");
        }
    }

    public boolean isInTimePart(int now, int startTime, int endTime) {
        Boolean isIn = false;
        //开始时间大于结束时间
        if (startTime > endTime) {
            //22~6 21 23 11             22 20 8
            if (now <= startTime && now < endTime) {
                isIn = true;

            } else if (now >= startTime && now > endTime) {
                isIn = true;
            } else {
                isIn = false;
            }

        } else if (startTime < endTime) {
            //6~22 开始时间小于结束时间
            if (now >= startTime && now < endTime) {
                isIn = true;
            } else {
                isIn = false;
            }

        }
        return isIn;
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

    private ProgressDialog mProgressDialog;

    private void autoDownloadAPK() {
//        Log.i("tag------------", "START--------------------" + Environment.getExternalStorageDirectory().getAbsolutePath());

//        Log.i("tag------------", "START" + App.getVersionCode());
        time = System.currentTimeMillis() + "";
        Map<String, Object> maps = new HashMap<String, Object>();
        maps.put("appsecret", Constants.appSecret);//
        maps.put("ConfVerCode", App.getVersionCode());
        maps.put("timeStamp", time);
        maps.put("imei", getIMEI());
        String sign = SHAAfter.createSign(maps, Constants.appKey);//签名
        final Map<String, Object> map = new HashMap<String, Object>();
        map.put("apiSign", sign);
        map.put("timeStamp", time);
        map.put("ConfVerCode", App.getVersionCode());
        map.put("imei", getIMEI());
        OkhttpUtils.doPost("http://sxtapi.gc.cs/app/version/getVersion", map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("tag------------", e.getMessage());

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String string = response.body().string();
                Log.i("tag------------", string);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        final VersionInfo versionInfo = JSONUtils.toObject(string, VersionInfo.class);
                        if (versionInfo.getRes().getVersionUrl() != null) {
                            if (versionInfo.getRes().getVersionNo() > Integer.parseInt(App.getVersionCode())) {
                                String versionUrl = versionInfo.getRes().getVersionUrl();
                                AutoInstaller installer = AutoInstaller.getDefault(MainActivity.this);
                                installer.installFromUrl(versionUrl);
                                mProgressDialog = new ProgressDialog(MainActivity.this);
                                mProgressDialog.setMessage("正在下载");
                                installer.setOnStateChangedListener(new AutoInstaller.OnStateChangedListener() {
                                    @Override
                                    public void onStart() {
                                        mProgressDialog.show();
                                    }

                                    @Override
                                    public void onComplete() {
                                        mProgressDialog.dismiss();
                                    }

                                    @Override
                                    public void onNeed2OpenService() {
                                        Toast.makeText(MainActivity.this, "请打开辅助功能服务", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                tv_pic_count.setText("已是最新版本");
                            }
                        }
                    }
                });
            }
        });
        Log.i("tag------------", "END");
    }

    public boolean exeSh() {
        String sh = "su -c pm install -r " + Environment.getExternalStorageDirectory().getAbsolutePath() + "/iwc2.apk";
        ProcessBuilder pb = new ProcessBuilder(sh);

        // 设置shell的当前目录
        pb.directory(new File("/"));

        Process process = null;
        BufferedReader in = null;
        PrintWriter out = null;
        try {
            process = pb.start();

            // 获取输入流，可以通过它获取SHELL的输出。
            in = new BufferedReader(new InputStreamReader(
                    process.getInputStream()));

            // 获取输出流，可以通过它向SHELL发送命令。
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
                    process.getOutputStream())), true);

            // 弹出superuser等授权管理
            out.println(sh);
            out.println("exit");


            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
                process.destroy();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
    }


    private Timer httpRequestTimer;
    private HttpRequestTask httpRequestTask;

    private void startTimer(int periodTime) {
        Log.e("摄像头","开始正常定时拍照"+periodTime);
        stopTimer();
        if (httpRequestTimer == null)
            httpRequestTimer = new Timer();
        if (httpRequestTask == null)
            httpRequestTask = new HttpRequestTask(periodTime);
        httpRequestTimer.schedule(httpRequestTask, 2000, periodTime * 1000);
    }

    private void stopTimer() {
        if (httpRequestTask != null) {
            httpRequestTask.cancel();
            httpRequestTask = null;
        }
        if (httpRequestTimer != null) {
            httpRequestTimer.cancel();
            httpRequestTimer.purge();
            httpRequestTimer = null;
        }

    }

    class HttpRequestTask extends TimerTask {
        int requestRateTime;
        int requestRateTimeNew;

        public HttpRequestTask(int requestRateTime) {
            this.requestRateTime = requestRateTime;
        }

        @Override
        public void run() {
            if (Constants.paishe == 1) {
                Log.e("tag_定时", "---成功---");
                isTakePicture();
            } else {
            }
        }
    }

    public void getParaments() {
        OkhttpUtils.doPost(Constants.GetDeviceParamet, container, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String string = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("摄像头","返回拍照时间间隔"+string);
                        Gson gson1 = new Gson();
                        DeviceStateInfo deviceStateInfo = gson1.fromJson(string, DeviceStateInfo.class);
                        String code = deviceStateInfo.getCode();
                        if (code.equals("10000")) {
                            DeviceStateInfo.ResBean res = deviceStateInfo.getRes();
                            parseFromService(res);
                            news = Constants.shotInterval;
                            startTimer(Constants.shotInterval);
                        }
                    }
                });
            }
        });
    }

    private static final String TAG = "MainActivity";
    private String tempPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "DownLoad";
    private Context mContext = MainActivity.this;

    /**
     * 静默安装 并启动
     *
     * @return
     */
    public boolean slientInstall() {
        // 进行资源的转移 将assets下的文件转移到可读写文件目录下
        createFile();

        File file = new File(tempPath);
        boolean result = false;
        Process process = null;
        OutputStream out = null;
        Log.i(TAG, "file.getPath()：" + file.getPath());
        if (file.exists()) {
            System.out.println(file.getPath() + "==");
            try {
                process = Runtime.getRuntime().exec("su");
                out = process.getOutputStream();
                DataOutputStream dataOutputStream = new DataOutputStream(out);
                // 获取文件所有权限
                dataOutputStream.writeBytes("chmod 777 " + file.getPath()
                        + "\n");
                // 进行静默安装命令
                dataOutputStream
                        .writeBytes("LD_LIBRARY_PATH=/vendor/lib:/system/lib pm install -r "
                                + file.getPath());
                dataOutputStream.flush();
                // 关闭流操作
                dataOutputStream.close();
                out.close();
                int value = process.waitFor();

                // 代表成功
                if (value == 0) {
                    Log.i(TAG, "安装成功！");
                    result = true;
                    // 失败
                } else if (value == 1) {
                    Log.i(TAG, "安装失败！");
                    result = false;
                    // 未知情况
                } else {
                    Log.i(TAG, "未知情况！");
                    result = false;
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (!result) {
                Log.i(TAG, "root权限获取失败，将进行普通安装");
                normalInstall(mContext);
                result = true;
            }
        }
        return result;
    }

    /**
     * 进行资源的转移 将assets下的文件转移到可读写文件目录下
     */
    public void createFile() {
        InputStream is = null;
        FileOutputStream fos = null;
        try {
            // 从assets文件夹中获取testapk.apk文件
            is = getAssets().open("iwc2-release.apk");
            File file = new File(tempPath);
            file.createNewFile();
            fos = new FileOutputStream(file);
            byte[] temp = new byte[1024];
            int i = 0;
            while ((i = is.read(temp)) > 0) {
                fos.write(temp, 0, i);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 传统安装
     *
     * @param context
     */
    public void normalInstall(Context context) {
        // 进行资源的转移 将assets下的文件转移到可读写文件目录下
//        createFile();
        File file = new File(tempPath + "iwc2-release.apk");
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        context.startActivity(intent);
        startApp(mContext);
    }

    /**
     * 安装后自启动apk
     *
     * @param context
     */
    private static void startApp(Context context) {
        execRootShellCmd("am start -S  " + context.getPackageName() + "/"
                + MainActivity.class.getCanonicalName() + " \n");
    }

    /**
     * 执行shell命令
     *
     * @param cmds
     * @return
     */
    private static boolean execRootShellCmd(String... cmds) {
        if (cmds == null || cmds.length == 0) {
            return false;
        }
        DataOutputStream dos = null;
        InputStream dis = null;
        Process p = null;
        try {
            p = Runtime.getRuntime().exec("su");// 经过Root处理的android系统即有su命令
            dos = new DataOutputStream(p.getOutputStream());

            for (int i = 0; i < cmds.length; i++) {
                dos.writeBytes(cmds[i] + " \n");
            }
            dos.writeBytes("exit \n");

            int code = p.waitFor();

            return code == 0;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (dos != null) {
                    dos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (dis != null) {
                    dis.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
            try {
                if (p != null) {
                    p.destroy();
                    p = null;
                }
            } catch (Exception e3) {
                e3.printStackTrace();
            }
        }
        return false;
    }

}