package com.rxjy.iwc2.service;

import android.app.Service;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Binder;
import android.os.IBinder;
import android.view.SurfaceHolder;

import com.rxjy.iwc2.interfaces.OnDetectedListener;

/**
 * Created by qindd on 2017/1/11.
 */
public class PictureService extends Service implements SurfaceHolder.Callback, Camera.PreviewCallback
{
    private OnDetectedListener onDetectedListener;

    //set interface
    public void setOnDetectedListener(OnDetectedListener listener){
        this.onDetectedListener = listener;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new MyBuild();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
    {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder)
    {

    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera)
    {
        onDetectedListener.onDetected("test");
    }

    class MyBuild extends Binder
    {
        public PictureService getMyService()
        {
            return PictureService.this;
        }
    }


}
