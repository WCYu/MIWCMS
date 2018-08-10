package com.rxjy.iwc2.nohttp;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.yolanda.nohttp.Headers;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.download.DownloadListener;
import com.yolanda.nohttp.download.DownloadRequest;

/**
 * Created by Administrator on 2016/7/12.
 */
public class DownloadService extends Service implements DownloadListener{
    private String url;
    private DownloadRequest downloadRequest;
    private final static String PROGRESS_KEY = "download_progress";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // url 下载地址
        // fileFloader 保存的文件夹
        // fileName 文件名
        // isRange 是否断点续传下载
        // isDeleteOld 如果发现文件已经存在是否删除后重新下载
        downloadRequest = NoHttp.createDownloadRequest(url, "APP_PATH_ROOT", "nohttp.apk", true, false);
        // what 区分下载
        // downloadRequest 下载请求对象
        // downloadListener 下载监听
        CallServer.getDownloadInstance().add(0, downloadRequest, this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 检查之前的下载状态
        int beforeStatus = downloadRequest.checkBeforeStatus();
        switch (beforeStatus) {
            case DownloadRequest.STATUS_RESTART:
                break;
            case DownloadRequest.STATUS_RESUME:
//                int progress = PrefUtils.getIntValue(AppContext._context,PROGRESS_KEY);
                break;
            case DownloadRequest.STATUS_FINISH:
                break;
            default:
                break;
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDownloadError(int what, Exception exception) {

    }

    @Override
    public void onStart(int what, boolean isResume, long rangeSize, Headers responseHeaders, long allCount) {
        int progress = (int) (rangeSize * 100 / allCount);
    }

    @Override
    public void onProgress(int what, int progress, long fileCount) {
//        PrefUtils.putIntValue(AppContext._context,PROGRESS_KEY,progress);

    }

    @Override
    public void onFinish(int what, String filePath) {

    }

    @Override
    public void onCancel(int what) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(downloadRequest != null){
            downloadRequest.cancel();
        }
    }
}
