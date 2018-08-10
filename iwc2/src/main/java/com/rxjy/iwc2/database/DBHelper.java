package com.rxjy.iwc2.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public static final String IWC_DATABASE_NAME = "iwc22";

    public static final String IMG_TABLE_NAME = "iwc_img";

    public static final String CREATE_IMG_TABLE = "create table "
            + IMG_TABLE_NAME
            + " (_id integer primary key autoincrement, name vachar(50),"
            + " time vachar(20), path vachar(50), rescode integer,orderno vachar(20),areaid integer)";

    public DBHelper(Context context) {
        super(context, IWC_DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_IMG_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

}
