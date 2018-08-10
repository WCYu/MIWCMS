package com.rxjy.iwc2;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.rxjy.iwc2.api.ApiHttpClient;
import com.rxjy.iwc2.database.ImageDB;
import com.rxjy.iwc2.utils.CrashHandler;
import com.yolanda.nohttp.NoHttp;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by qindd on 2016/12/13.
 */
public class App extends Application
{

    private static App instance;
    public ImageDB imgDB;

    @Override
    public void onCreate()
    {
        super.onCreate();
        instance = this;

        CrashHandler crashHanler = CrashHandler.getInstance();
        crashHanler.init(this);
        // 初始化NoHttp
        NoHttp.initialize(this);
        // 初始化网络请求
        AsyncHttpClient client = new AsyncHttpClient();
        ApiHttpClient.setHttpClient(client);
        // 数据库
        imgDB = new ImageDB(this);
    }

    public static App getInstance()
    {
        return instance;
    }

    public static Toast mToast;

    public static void displayToast(Context context, String contentStr) {
        if (mToast == null)
            mToast = Toast.makeText(context, contentStr, Toast.LENGTH_SHORT);
        else
            mToast.setText(contentStr);
        mToast.show();
    }

    public static String getCurProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager mActivityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager
                .getRunningAppProcesses()) {
            if (appProcess.pid == pid) {

                return appProcess.processName;
            }
        }
        return null;
    }

    public static Properties getProps(Context context){
        Properties props = new Properties();
        try{
            InputStream is = context.getAssets().open("appConfig.properties");
            props.load(is);
        } catch (IOException e){
            e.printStackTrace();
        }
        return props;
    }
    //获取版本号
    public static String getVersionCode() {
        try {
            PackageManager manager = getContext().getPackageManager();
            PackageInfo info = manager.getPackageInfo(getContext().getPackageName(), 0);
            return String.valueOf(info.versionCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    //获取版本名称
    public static String getVersionName() {
        try {
            PackageManager manager = getContext().getPackageManager();
            PackageInfo info = manager.getPackageInfo(getContext().getPackageName(), 0);
            return info.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    public static Context getContext() {
        return instance.getApplicationContext();
    }
}
