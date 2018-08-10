package com.rxjy.iwc2.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetUtil {

    // 判断wifi是否连接
    public static boolean isWiFiConnected(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] infos = connectivity.getAllNetworkInfo();
            if (infos != null) {
                for (NetworkInfo ni : infos) {
                    if (ni.isAvailable()&& ni.isConnected()) {
//                        Toast.makeText(context, "网络可用："+ni.getTypeName(), Toast.LENGTH_SHORT).show();
                        return true;
                    }
                }
            }
        }
//        Toast.makeText(context, "网络可不可用", Toast.LENGTH_SHORT).show();

        return false;
    }

//	// 判断wifi是否连接
//	public static boolean isWiFiConnected(Context context) {
//		ConnectivityManager connectivity = (ConnectivityManager) context
//				.getSystemService(Context.CONNECTIVITY_SERVICE);
//		if (connectivity != null) {
//			NetworkInfo[] infos = connectivity.getAllNetworkInfo();
//			if (infos != null) {
//				for (NetworkInfo ni : infos) {
//					if (ni.getTypeName().equals("WIFI") && ni.isConnected()) {
//						return true;
//					}
//				}
//			}
//		}
//		return false;
//	}

    // 判断wifi是否可用
    public static boolean isWiFiAvailable(Context context) {
        final ConnectivityManager connMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if (wifi.isAvailable() || mobile.isAvailable())
            return true;
        else
            return false;
    }

    /**
     * 判断网络是否连接
     *
     * @param context Context
     * @return 网络是否连接
     */
    public static boolean isConnected(Context context) {

        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (null != connectivity) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (null != info && info.isConnected()) {
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }
        return false;
    }

}
