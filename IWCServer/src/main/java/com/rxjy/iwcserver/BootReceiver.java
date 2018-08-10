package com.rxjy.iwcserver;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Handler;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Administrator on 2016/9/22.
 */
public class BootReceiver extends BroadcastReceiver
{

    public static String PACKAGE_NAME = "com.rxjy.iwc2";

    @Override
    public void onReceive(Context context, Intent intent)
    {
        Intent intentStart;
        PackageManager packageManager = context.getPackageManager();

        if (intent.getAction().equals(intent.ACTION_PACKAGE_REMOVED))
        {
            String packageName = intent.getDataString();
            if (PACKAGE_NAME.equals(packageName))
            {
                Toast.makeText(context, "卸载了:" + packageName + "包名的程序", Toast.LENGTH_SHORT).show();
//                intentStart = packageManager.getLaunchIntentForPackage(packageName);
//                intentStart.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
//                        Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED | Intent.FLAG_ACTIVITY_CLEAR_TOP) ;
//                context.startActivity(intentStart);
            }
        }

        if (intent.getAction().equals(intent.ACTION_PACKAGE_ADDED))
        {
            String packageName = intent.getDataString();
            if (PACKAGE_NAME.equals(packageName))
            {
                Toast.makeText(context, "安装了:" + packageName + "包名的程序", Toast.LENGTH_SHORT).show();

                intentStart = packageManager.getLaunchIntentForPackage(PACKAGE_NAME);
                intentStart.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intentStart);
            }
        }

        if (intent.getAction().equals(intent.ACTION_PACKAGE_REPLACED))
        {
            String packageName = intent.getDataString();
            if ("package:com.rxjy.iwc2".equals(packageName))
            {
                Toast.makeText(context, "替换了:" + packageName + "包名的程序", Toast.LENGTH_SHORT).show();

                intentStart = packageManager.getLaunchIntentForPackage(PACKAGE_NAME);
                intentStart.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intentStart);
            }
        }
    }

    private Intent getIntentWithPackageName(Context context, String packageName)
    {
        PackageInfo packageinfo = null;
        try {
            packageinfo = context.getPackageManager().getPackageInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();
        }
        if (packageinfo == null)
        {
            return null;
        }

        // 创建一个类别为CATEGORY_LAUNCHER的该包名的Intent
        Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
        resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        resolveIntent.setPackage(packageinfo.packageName);

        // 通过getPackageManager()的queryIntentActivities方法遍历
        List<ResolveInfo> resolveinfoList = context.getPackageManager().queryIntentActivities(resolveIntent, 0);

        ResolveInfo resolveinfo = resolveinfoList.iterator().next();

        if (resolveinfo != null)
        {
            // packagename = 参数packname
            String packagename = resolveinfo.activityInfo.packageName;
            // 这个就是我们要找的该APP的LAUNCHER的Activity[组织形式：packagename.mainActivityname]
            String className = resolveinfo.activityInfo.name;
            // LAUNCHER Intent
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);

            // 设置ComponentName参数1:packagename参数2:MainActivity路径
            ComponentName cn = new ComponentName(packagename, className);

            intent.setComponent(cn);
            return intent;
        }
        return null;
    }
}
