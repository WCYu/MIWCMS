package com.rxjy.iwc2.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.rxjy.iwc2.MainActivity;

/**
 * 开机自启动App
 * @author qdd
 * @create 2016/4/22
 */
public class StartAppReceiver extends BroadcastReceiver
{
	@Override
	public void onReceive(Context context, Intent intent)
	{
		if(intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED))
		{
			Intent i = new Intent(context, MainActivity.class);
			i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(i);
			Log.i("tag","开启广播");
		}
	}
}
