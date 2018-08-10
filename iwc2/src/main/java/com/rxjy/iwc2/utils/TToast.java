package com.rxjy.iwc2.utils;

import android.content.Context;
import android.widget.Toast;

public class TToast
{
	public static final void showL(Context context, String text)
	{
		Toast.makeText(context, text, Toast.LENGTH_LONG).show();
	}

	public static final void showS(Context context, String text)
	{
		Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
	}
}
