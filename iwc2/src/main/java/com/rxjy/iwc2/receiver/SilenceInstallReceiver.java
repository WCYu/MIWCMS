package com.rxjy.iwc2.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.rxjy.iwc2.utils.TToast;

/**
 * Created by 解亚鑫 on 2018/6/15.
 */

public class SilenceInstallReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.PACKAGE_REPLACED")){
            String packageName = intent.getDataString();
            TToast.showL(context,"升级了:" + packageName + "包名的程序");
            Log.i("TAG","升级了:" + packageName + "包名的程序");
//            startApp(context);
        }

        // 接收安装广播
        if (intent.getAction().equals("android.intent.action.PACKAGE_ADDED")) {
            String packageName = intent.getDataString();
            TToast.showL(context,"安装了:" +packageName + "包名的程序");
            Log.i("TAG","安装了:" + packageName + "包名的程序");

            // 监测到升级后执行app的启动 只能启动自身 一般用于软件更新
            startApp(context);
        }

        //接收卸载广播
        if (intent.getAction().equals("android.intent.action.PACKAGE_REMOVED")) {
            String packageName = intent.getDataString();
            TToast.showL(context,"卸载了:"  + packageName + "包名的程序");
            Log.i("TAG","卸载了:" + packageName + "包名的程序");
        }
    }

    /**
     * 监测到升级后执行app的启动
     */
    public void startApp(Context context){
        // 根据包名打开安装的apk
        Intent resolveIntent = context.getPackageManager().getLaunchIntentForPackage("com.rxjy.iwc2");
        context.startActivity(resolveIntent);

        // 打开自身 一般用于软件升级
//        Intent intent = new Intent(context, MainActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        context.startActivity(intent);
    }
}
