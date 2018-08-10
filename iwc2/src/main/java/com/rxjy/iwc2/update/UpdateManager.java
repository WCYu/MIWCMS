package com.rxjy.iwc2.update;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

import com.rxjy.iwc2.Constants;
import com.rxjy.iwc2.utils.FileUtils;
import com.yolanda.nohttp.RequestMethod;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by qdd on 2016/8/9.
 */
public class UpdateManager
{
    private static int mVersionCode;

    public static boolean isUpdate(Context context)
    {
        mVersionCode = getVersionCode(context);
        boolean isUpdate;
        if(0.0 > mVersionCode)
            isUpdate = true;
        else
            isUpdate = false;
        return isUpdate;
    }

    public static int getVersionCode(Context context)
    {
        PackageInfo info = null;
        try {
            info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info.versionCode;
    }

    public static void downloadApk(final String apkPath, final Handler handler)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(apkPath != null && !"".equals(apkPath)){
                    getApkFromServer(apkPath, handler);
                }
            }
        }).start();
    }

    private static void getApkFromServer(String apkPath, Handler handler){
        if(FileUtils.checkSaveLocationExists()){
            try {
                URL url = new URL(apkPath);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(30000);
                conn.setRequestMethod("GET");
                if(conn.getResponseCode() == 200)
                {
                    InputStream is = conn.getInputStream();
                    int fileSize = conn.getContentLength();//根据响应获取文件大小
                    String dir = Environment.getExternalStorageDirectory().getPath();
                    File file = new File(dir + File.separator, "iwc.apk");
                    FileOutputStream fos = new FileOutputStream(file);
                    BufferedInputStream bis = new BufferedInputStream(is);
                    byte[] buffer = new byte[1024];
                    int len;
                    int number = 0;

                    while((len = bis.read(buffer)) != -1)
                    {
                        fos.write(buffer, 0, len);
                    }
                    Message msg = handler.obtainMessage();
                    msg.arg1 = UPDATE_COMPLETE;
                    handler.sendMessage(msg);

                    fos.close();
                    bis.close();
                    is.close();
                }
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    public static final int UPDATE_COMPLETE = 2;

    /**
     * 软件静默安装
     * @return 安装结果:获取到的result值<br>
     *
     * 如果安装成功的话是“
     * pkg: /data/local/tmp/Calculator.apk  /nSuccess”，<br>
     * 如果是失败的话，则没有结尾的“Success”。
     */
    public static String silentInstall()
    {
        String filepath = Environment.getExternalStorageDirectory().getPath();
        String apkAbsolutePath = filepath + "/iwc.apk";
        String[] args = {"pm", "install", "-r", apkAbsolutePath};
        String result = "";
        ProcessBuilder processBuilder = new ProcessBuilder(args);
        Process process = null;
        InputStream errIs = null;
        InputStream inIs = null;
        try
        {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int read = -1;
            process = processBuilder.start();
            errIs = process.getErrorStream();
            while ((read = errIs.read()) != -1)
            {
                baos.write(read);
            }
            baos.write("/n".getBytes());
            inIs = process.getInputStream();
            while ((read = inIs.read()) != -1)
            {
                baos.write(read);
            }
            byte[] data = baos.toByteArray();
            result = new String(data);
            // 尝试重启
//            reboot();
        } catch (Exception e)
        {
            e.printStackTrace();
        } finally
        {
            try
            {
                if (errIs != null)
                {
                    errIs.close();
                }
                if (inIs != null)
                {
                    inIs.close();
                }
            } catch (IOException e)
            {
                e.printStackTrace();
            }
            if (process != null)
            {
                process.destroy();
            }
        }
        return result;
    }
}