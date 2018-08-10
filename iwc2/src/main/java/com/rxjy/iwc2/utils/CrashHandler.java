package com.rxjy.iwc2.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Process;
import android.util.Log;

import com.rxjy.iwc2.MainActivity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by qindd on 2017/2/4.
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {

    private static final String TAG = "CrashHandler";
    private static final boolean DEBUG = true;

    private static final String PATH = Environment.getExternalStorageDirectory().getPath() + "/CrashTest/log/";
    private static final String FILE_NAME = "crash";
    private static final String FILE_NAME_SUFFIX = ".txt";

    private static CrashHandler sInstance = new CrashHandler();
    private Thread.UncaughtExceptionHandler mDefaultCrashHandler;
    private Context mContext;

    private File exceptionContentFile;

    public CrashHandler() {
    }

    public static CrashHandler getInstance(){
        return sInstance;
    }

    public void init(Context context){
//        mDefaultCrashHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
        mContext = context.getApplicationContext();
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        try{
            dumpexceptionToSDCard(ex);
//            uploadExceptionToServer();
        }catch(IOException e){
            e.printStackTrace();
        }
        ex.printStackTrace();
        restartApp();
        if(mDefaultCrashHandler != null){
            mDefaultCrashHandler.uncaughtException(thread, ex);
        }else{
            Process.killProcess(Process.myPid());
        }
    }

    private void restartApp()
    {
        Intent intent = new Intent(mContext, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }

    private void dumpexceptionToSDCard(Throwable ex) throws IOException{
        if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
        {
            if(DEBUG){
                Log.w(TAG, "sdcard unmounted,skip dump exception");
                return;
            }
        }
        File dir = new File(Environment.getExternalStorageDirectory(), "IWC/CrashLog/");
        if(!dir.exists()){
            dir.mkdirs();
        }

        long current = System.currentTimeMillis();
        // 创建文件取名字不能用“：”
        String time = new SimpleDateFormat("yyyy-MM-dd_HHmmss").format(new Date(current));
        exceptionContentFile = new File(dir, FILE_NAME + time + FILE_NAME_SUFFIX);

        try {
            FileWriter fw = new FileWriter(exceptionContentFile);
            BufferedWriter bufferedWriter = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(bufferedWriter);
            pw.println(time);
            dumpPhoneInfo(pw);
            pw.println();
            ex.printStackTrace(pw);
            pw.close();
            SPUtil.put(mContext, "exceptionContentFile", exceptionContentFile.getPath());
        }catch (Exception e){
            e.printStackTrace();
            String ee = e.toString();
            Log.e(TAG, "dump crash info failed");
            Log.e(TAG, ee);
        }
    }

    private void dumpPhoneInfo(PrintWriter pw) throws PackageManager.NameNotFoundException
    {
        // 应用的版本名称和版本号
        PackageManager pm = mContext.getPackageManager();
        PackageInfo pi = pm.getPackageInfo(mContext.getPackageName(), PackageManager.GET_ACTIVITIES);
        pw.print("App Version: ");
        pw.print(pi.versionName);
        pw.print('_');
        pw.println(pi.versionCode);

        // cpu架构
        pw.print("CPU ABI: ");
        pw.println(Build.CPU_ABI);
    }

//    public static void uploadExceptionToServer(){
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
//                String value = "IMEI"+ AppConfig.IMEI+"SubItemId"+AppConfig.SubItemId+"timeStamp"+timeStamp;
//                StringBuilder sb = new StringBuilder();
//                sb.append(AppConfig.apiKey).append(value).append(AppConfig.apiSecret);
//                value = sb.toString();
//
//                String ExceptionContent = txt2String(exceptionContentFile);
//                // 需判断网络
//                if(NetUtils.isWiFiConnected(AppContext.getInstance())){
//                    IWCApi.SendErrorMsg(AppConfig.SendErrorMsg, AppConfig.apiKey, ExceptionContent,
//                            SHA.SHA1(value), timeStamp, AppConfig.IMEI, AppConfig.SubItemId, mHandler);
//                }else{
//                }
//
//            }
//        }).start();
//    }

    public static String txt2String(File file){
        StringBuilder result = new StringBuilder();
        try{
            BufferedReader br = new BufferedReader(new FileReader(file));//构造一个BufferedReader类来读取文件
            String s = null;
            while((s = br.readLine())!=null){//使用readLine方法，一次读一行
//                result.append(System.lineSeparator()+s);
                result.append(s);
            }
            br.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return result.toString();
    }

}

