package com.rxjy.iwcserver;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by qdd on 2016/9/24.
 */
public class CoreService extends Service
{

    private static final String IWC_PACKAGENAME = "com.rxjy.iwc2";

    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_PACKAGE_REPLACED);

        BootReceiver mReceiver = new BootReceiver();
        registerReceiver(mReceiver, intentFilter);
        Toast.makeText(this, "IWCServer-->> CoreService-->> onCreate", Toast.LENGTH_SHORT).show();

//        PackageManager packageManager = this.getPackageManager();
//        Intent intent = packageManager.getLaunchIntentForPackage("com.rxjy.iwc");
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED | Intent.FLAG_ACTIVITY_CLEAR_TOP) ;
//        this.startActivity(intent);
    }

    //  提示流程：卸载-安装-替换

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        return Service.START_STICKY;
    }

//    public String silentInstall()
//    {
//        String filepath = Environment.getExternalStorageDirectory().getPath();
//        String filepath = getAssets().
//        String apkAbsolutePath = filepath + "/iwc.apk";
//        String[] args = {"pm", "install", "-r", apkAbsolutePath};
//        String result = "";
//        ProcessBuilder processBuilder = new ProcessBuilder(args);
//        Process process = null;
//        InputStream errIs = null;
//        InputStream inIs = null;
//        try
//        {
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            int read = -1;
//            process = processBuilder.start();
//            errIs = process.getErrorStream();
//            while ((read = errIs.read()) != -1)
//            {
//                baos.write(read);
//            }
//            baos.write("/n".getBytes());
//            inIs = process.getInputStream();
//            while ((read = inIs.read()) != -1)
//            {
//                baos.write(read);
//            }
//            byte[] data = baos.toByteArray();
//            result = new String(data);
//            // 尝试重启
//            reboot();
//        } catch (Exception e)
//        {
//            e.printStackTrace();
//        } finally
//        {
//            try
//            {
//                if (errIs != null)
//                {
//                    errIs.close();
//                }
//                if (inIs != null)
//                {
//                    inIs.close();
//                }
//            } catch (IOException e)
//            {
//                e.printStackTrace();
//            }
//            if (process != null)
//            {
//                process.destroy();
//            }
//        }
//        return result;
//    }

}
