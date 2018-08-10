package com.rxjy.iwc2.service;

import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;
import android.os.StatFs;
import android.support.annotation.Nullable;
import android.util.Log;

import com.rxjy.iwc2.Constants;
import com.rxjy.iwc2.utils.FileUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by qdd on 2016/9/20.
 */
public class StorageManagerService extends Service
{

    private static final String TAG = "StorageManagerService";
    private TimerTask task;
    private Timer timer;

    // 用来存储图片地址
//    List<String> imgPathList;
    private File rootFile;
    private List<File> listFile;
    private static final int DELETE_IMG_COUNT = 1; // 之前是10，改为1了

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
        Log.i(TAG, TAG + "_onCreate");
//        TToast.showS(this, TAG + "_onCreate");
        long allSize = getSDAllSize();
        long freeSize = getSDFreeSize();
        Log.e(TAG, "SD卡总容量-----" + allSize);
        Log.e(TAG, "SD卡剩余空间----" + freeSize);

        timerStart();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        Log.i(TAG, TAG + "_onStartCommand");
        return Service.START_STICKY;
    }

    private void timerStart()
    {
        timerStop();

        if(timer == null)
            timer = new Timer();
        if(task == null)
            task = new MyTask();

        timer.schedule(task, 5*1000, 60*1000);
    }

    private void timerStop(){
        if(task != null){
            task.cancel();
            task = null;
        }
        if(timer != null)
        {
            timer.cancel();
            timer.purge();
            timer = null;
        }
    }

    class MyTask extends TimerTask
    {
        @Override
        public void run() {
            if(getSDAllSize() - getSDFreeSize() >= getSDAllSize() * 2 / 3)
            {
//                imgPathList = new ArrayList<String>();
                rootFile = new File(Constants.DEFAULT_SAVE_IMAGE_PATH2);
                listFile = FileUtils.listPathFiles(rootFile.getPath());

                if(listFile != null && listFile.size() > 0){

                    Collections.sort(listFile, new Comparator<File>()
                    {
                        @Override
                        public int compare(File lhs, File rhs)
                        {
                            if(lhs.lastModified() > rhs.lastModified())
                                return 1;
                            else if(lhs.lastModified() == rhs.lastModified())
                                return 0;
                            else
                                return -1;
                        }
                    });
                }
                // 删除最以前的照片
                deleteImgMostBefore(listFile);
            }
        }
    }

    class ComparatorDate implements Comparator
    {
        public int compare(Object obj1, Object obj2)
        {
            Date begin = (Date) obj1;
            Date end = (Date) obj2;
            if (begin.after(end))
            {
                return 1;
            } else
            {
                return -1;
            }
        }
    }

    private void deleteImgMostBefore(List<File> listFile)
    {
        if (listFile != null && listFile.size() > DELETE_IMG_COUNT)
        {
            for (int i = 0; i < DELETE_IMG_COUNT; i++)
            {
                FileUtils.deleteFile(listFile.get(i).getPath());
            }
        }
    }

    /**
     * 查看SD卡剩余空间
     * @return
     */
    public long getSDFreeSize(){
        //取得SD卡文件路径
        File path = Environment.getExternalStorageDirectory();
        StatFs sf = new StatFs(path.getPath());
        //获取单个数据块的大小(Byte)
        long blockSize = sf.getBlockSize();
        //空闲的数据块的数量
        long freeBlocks = sf.getAvailableBlocks();
        //返回SD卡空闲大小
        //return freeBlocks * blockSize;  //单位Byte
        //return (freeBlocks * blockSize)/1024;   //单位KB
        return (freeBlocks * blockSize)/1024 /1024; //单位MB
    }

    /**
     * 查看SD卡总容量
     * @return
     */
    public long getSDAllSize(){
        //取得SD卡文件路径
        File path = Environment.getExternalStorageDirectory();
        StatFs sf = new StatFs(path.getPath());
        //获取单个数据块的大小(Byte)
        long blockSize = sf.getBlockSize();
        //获取所有数据块数
        long allBlocks = sf.getBlockCount();
        //返回SD卡大小
        //return allBlocks * blockSize; //单位Byte
        //return (allBlocks * blockSize)/1024; //单位KB
        return (allBlocks * blockSize)/1024/1024; //单位MB
    }

    private void deleteExtraFile()
    {
        int saveDays = Constants.imgSave_Time;

        String[] deleteDates = checkDates(saveDays, 60);
        deleteDateFile(deleteDates);
    }

    private String checkDate(int saveDays)
    {
        String deleteDate = getDate(new Date(), saveDays);
        return deleteDate;
    }

    private String[] checkDates(int saveDays, int delay)
    {
        String[] deleteDates = getDates(new Date(), saveDays, delay);
        return deleteDates;
    }

    public String getDate(Date curDate, int saveDays)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(curDate);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        long curMils = calendar.getTime().getTime();
        long milliseconds = saveDays * (1000 * 60 * 60 * 24);
        long toMils = curMils - milliseconds;

        Date toDate = new Date(toMils);
        return new SimpleDateFormat("yyyyMMdd").format(toDate);
    }

    public String[] getDates(Date curDate, int saveDays, int delay)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(curDate);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        long curMils = calendar.getTime().getTime();

        long milliseconds;
        long toMils;
        String[] dateStrs = new String[delay];
        for (int i = 0; i < delay; i++)
        {
            milliseconds = (long)(saveDays + i) * (1000 * 60 * 60 * 24);
            toMils = curMils - milliseconds;
            Date toDate = new Date(toMils);
            String dateStr = new SimpleDateFormat("yyyyMMdd").format(toDate);
            dateStrs[i] = dateStr;
        }
        return dateStrs;
    }

    private void deleteDateFile(String[] deleteDates)
    {
        File parentFile = new File(Environment.
                getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "IWC");
        for (String deleteDate : deleteDates)
        {
            File dateFile = new File(parentFile, deleteDate);
            if (dateFile.exists())
            {
                FileUtils.clearFileWithPath(dateFile.getPath());
                if (dateFile.isDirectory())
                {
                    dateFile.delete();
                }
            }
        }
    }

}
