package com.oubeichen.resourcemonitor;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DBNAME = "resourcemonitor.db"; 
    public static final int VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DBNAME, null, VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL("CREATE TABLE IF NOT EXISTS " +
        		UsageLog.Camera.TNAME + "(" +
        		UsageLog.Camera._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
        		UsageLog.Camera.PACKAGENAME + " VARCHAR, " +
        		UsageLog.Camera.METHODNAME + " VARCHAR, " +
        		UsageLog.Camera.TYPE + " INTEGER, " +
        		UsageLog.Camera.TIME + " INTEGER" +
        		")");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS " + UsageLog.Camera.TNAME);
        onCreate(db);
    }

}
