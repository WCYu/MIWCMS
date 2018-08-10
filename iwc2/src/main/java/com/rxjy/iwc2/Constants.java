package com.rxjy.iwc2;

import android.os.Environment;

import java.io.File;

/**
 * Created by qindd on 2016/12/13.
 */
public class Constants {

//	public static final String JRZN_URL = "http://api.jingrenzn.com:8083/";
//	public static final String JRZN_URL = "http://test4.rxjy.com:8004/";

    //    public static final String JRZN_LOCAL_URL = "http://10.10.13.43:8095/";
//    public static final String JRZN_LOCAL_URL = "http://192.168.1.230:801/";
    private static final String LOCAL_URL = "http://sxtapi.gc.cs/app";//测试
//        private static final String LOCAL_URL = "http://api.sxt.cs.rxjygc.com:9999/app";//
//    private static final String LOCAL_URL = "http://api.sxt.rxjygc.com:9999/app";//线上

    private final static String APP_CONFIG = "config";

//    public static String IMEI;
//    public static String ConfVerCode;
//    public static int Nact_Time;
//    public static int Act_Time;
//    public static int Interval_Time; // 拍摄间隔
//    public static int Low_Power;
//    public static int Low_Power_Failure_Count; // 电量过低首次上传失败后再次上传的次数 3
//    public static int Low_Power_Point = 5; // 百分点间隔 5
//    public static int UploadPic_Failure_Count; // 拍照失败后再次上传次数
//    //	public static String UploadPic_Failure_Time; // 拍照失败后再次上传时间间隔
//    public static int Nnet_Time;
//    public static int Img_Level;
//    public static int ImgSave_Time;
    public static int Check_OL_Time = 60;
//    public static String SSID;
//    public static String PassWord;
//    public static String SubItemId;
//    public static int VersionCode;
//    public static int IsTakePicture;
    public static String ElectricityNumber; // 电量值，2017/4/26新增字段
//    public static String ApkPath;
    public static int Rgb = 200; // 动作识别灵敏度
//    public static int Type = 3;//WPA或WPA2 PSK密码验证

    public static String apiKey = "rxjy@123";
    public static String apiSecret = "IWCS@456";

//    public static final String IsDeviceOnline = JRZN_LOCAL_URL + "api/ParameterConfiguration/IsDeviceOnline";
//    public static final String UploadPicture = JRZN_LOCAL_URL + "api/ParameterConfiguration/UploadPicture";
//    public static final String IsLowPower = JRZN_LOCAL_URL + "api/ParameterConfiguration/IsLowPower";
//    public static final String SendErrorMsg = JRZN_LOCAL_URL + "api/ParameterConfiguration/SendErrorMsg";
//    public static String downloadApk = JRZN_LOCAL_URL + "api/ParameterConfiguration/DownloadAPK";

    //获取服务器时间
    public static final String GETSERVICETIME = LOCAL_URL + "/deviceState/getTimeStamp";
    //设备绑定的项目
    public static final String ProjectIsDevice = LOCAL_URL + "/workorderDeviceInfo/getWorkorderDevice";
    //检测设备是否在线
    public static final String IsDeviceOnlineNew = LOCAL_URL + "/deviceState/updateDeviceState";
//    public static final String IsDeviceOnlineNew = "http://10.10.4.168:8080/cameraApi/deviceState/updateDeviceState";
    //获取设备拍摄参数
    public static final String GetDeviceParamet = LOCAL_URL + "/workorderParamSetting/photoSet";
    //上传图片
    public static final String UpLoadPictureNew = LOCAL_URL + "/photo/UploadPicture";
//    public static final String UpLoadPictureNew = "http://10.10.4.168:8080/cameraApi" + "/photo/UploadPicture";
    //抓拍上传图片
    public static final String UpLoadPictureZ = LOCAL_URL + "/photo/newUploadPicture";
    //是否抓拍
    public static final String IsCapture = LOCAL_URL + "/capturePhoto/getCapturePhoto";
    //最后拍照时间
    public static final String finallyUploadTime = LOCAL_URL + "/photo/finallyUploadTime ";

    public static String appKey = "e27f721005d3713ca8851599aedc91b7";
    public static String appSecret = "d0465e1c4cbad5023becc26ef3f302d9";

    public static int leave;
    public  static  String addCreater;
    public static long addTime;
    public static int batteryPercentage;
    public static int did;
    public static int dingwei;
    public static int endTime = 18;
    public static int equipmentInteractionError;
    public static int id;
    public static String imei;
    public static int imgSave_Time;
    public static int lowPowerFailureCount;
    public static int lowPowerPoint;
    public static int mobileInteraction;
    public static int motionSensitivity;
    public static String orderno;
    public static int paishe = 1;
    public static int saveImageSize;
    public static int saveResolvingPower;
    public static int sendImageSize;
    public static int sendInterval;
    public static int sendIntervalToImage;
    public static int shotInterval = 1800;
    public static int startTime = 8;
    public static String updateCreater;
    public static long updateTime;
    public static int uploadPicFailureCount;
    public static String versionNnumber;
    public static int wendu;
    public static String wifiAccount;
    public static String wifiPassword;
    public static int yuying;

    public static final int baseRequest = 100;

    public static final int ACTION_IS_DEVICE_ONLINE = baseRequest + 1;
    public static final int ACTION_UPLOAD_PICTURE = baseRequest + 2;
    public static final int ACTION_IS_LOW_POWER = baseRequest + 3;

    public static final int NOT_UPLOAD = 0;
    public static final int UPLOAD_SUCCEED = 1;
//    public static final int UPLOAD_FAILED = -1;

    public final static String DEFAULT_SAVE_IMAGE_PATH_PARENT = Environment
            .getExternalStorageDirectory()
            + File.separator
            + "IWC"
            + File.separator;

    public final static String DEFAULT_SAVE_IMAGE_PATH = Environment
            .getExternalStorageDirectory()
            + File.separator
            + "IWC"
            + File.separator + "iwc_img";

    public final static String DEFAULT_SAVE_IMAGE_PATH2 = Environment
            .getExternalStorageDirectory()
            + File.separator
            + "IWC"
            + File.separator + "iwc_img";

    public final static String DEFAULT_SAVE_FILE_PATH = Environment
            .getExternalStorageDirectory()
            + File.separator
            + "IWC"
            + File.separator + "download" + File.separator;
}
