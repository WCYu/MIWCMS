package com.rxjy.iwc2.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class ImageDB {

    public static final String TAG = "ImgDatabase";
    private DBHelper dbHelper;
    private SQLiteDatabase dbDatabase;

    public ImageDB(Context context) {
        dbHelper = new DBHelper(context);
        dbDatabase = dbHelper.getWritableDatabase();
    }

    public synchronized void insert(ImageBean bean) {
        String sql = "insert into " + DBHelper.IMG_TABLE_NAME;

        sql += "(_id, name, time, path, rescode,orderno,areaid) values(null, ?, ?, ?, ?, ?, ?)";

        dbDatabase.execSQL(sql, new String[]{
//				bean.getId()+"",
                bean.getName() + "",
                bean.getTime() + "",
                bean.getPath() + "",
                bean.getRescode() + "",
                bean.getOrderno() + "",
                bean.getAreaid() + ""});
    }

//	public void delete(int id)
//	{
//		String sql = ("delete from " + DBHelper.IMG_TABLE_NAME + " where _id=?");
//		dbDatabase.execSQL(sql, new Integer[] { id });
//	}

    public void delete(int rescode) {
        String sql = ("delete from " + DBHelper.IMG_TABLE_NAME + " where rescode=?");
        dbDatabase.execSQL(sql, new Integer[]{rescode});
    }

    public void delete(String uploadSuccessPath) {
        String sql = ("delete from " + DBHelper.IMG_TABLE_NAME + " where path=?");
        dbDatabase.execSQL(sql, new String[]{uploadSuccessPath});
    }

    public void deleteName(String uploadSuccessName) {
        String sql = ("delete from " + DBHelper.IMG_TABLE_NAME + " where name=?");
        dbDatabase.execSQL(sql, new String[]{uploadSuccessName});
    }

    public void update() {
        String sql = ("update " + DBHelper.IMG_TABLE_NAME + " set name=?, time=?, path=?, rescode=? where _id=?");
        dbDatabase.execSQL(sql, new String[]{});
    }

    public synchronized List<ImageBean> query() {
        ArrayList<ImageBean> data = new ArrayList<ImageBean>();
        if (!dbDatabase.isOpen()) {
            Log.e(TAG, "Error occurred when obtaining projectinfo. Database is not open.");
            return null;
        }
        Cursor cursor = dbDatabase.rawQuery("select * from " + DBHelper.IMG_TABLE_NAME, null);
        if (cursor == null) {
            return null;
        }
        if (cursor.getCount() == 0) {
            return data;
        }
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            ImageBean imageBean = new ImageBean();
            imageBean.setId(cursor.getInt(0));
            imageBean.setName(cursor.getString(1));
            imageBean.setTime(cursor.getString(2));
            imageBean.setPath(cursor.getString(3));
            imageBean.setRescode(cursor.getInt(4));
            imageBean.setOrderno(cursor.getString(5));
            imageBean.setAreaid(cursor.getInt(6));
            data.add(imageBean);
        }
        if (!cursor.isClosed()) {
            cursor.close();
        }
        return data;
    }

    public synchronized List<ImageBean> query(int rescode) {
        ArrayList<ImageBean> data = new ArrayList<ImageBean>();
        if (!dbDatabase.isOpen()) {
            Log.e(TAG, "Error occurred when obtaining projectinfo. Database is not open.");
            return null;
        }
        Cursor cursor = dbDatabase.rawQuery("select * from " + DBHelper.IMG_TABLE_NAME + " where rescode='" + rescode + "'", null);
        if (cursor == null) {
            return null;
        }
        if (cursor.getCount() == 0) {
            return data;
        }
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            ImageBean imageBean = new ImageBean();
            imageBean.setId(cursor.getInt(0));
            imageBean.setName(cursor.getString(1));
            imageBean.setTime(cursor.getString(2));
            imageBean.setPath(cursor.getString(3));
            imageBean.setRescode(cursor.getInt(4));
            imageBean.setOrderno(cursor.getString(5));
            imageBean.setAreaid(cursor.getInt(6));
            data.add(imageBean);
        }
        if (!cursor.isClosed()) {
            cursor.close();
        }
        return data;
    }


}
