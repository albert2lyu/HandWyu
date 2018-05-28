package com.imstuding.www.handwyu.ToolUtil;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table course(" +
                "jxcdmc text," +
                "teaxms text," +
                "xq text," +
                "jcdm text," +
                "kcmc text," +
                "zc text," +
                "year text)");

        db.execSQL("create table week(" +
                "xq text," +
                "rq text," +
                "zc text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
