package com.oubeichen.resourcemonitor;

import java.io.File;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

public class DatabaseHelper {

    public static final String DATABASE_FILE_PATH = Environment
            .getExternalStorageDirectory().getAbsolutePath();
    private SQLiteDatabase database;

    public DatabaseHelper() {
        onCreate(getWritableDatabase());
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
        db.close();
    }

    public void onUpgrade(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS " + UsageLog.Camera.TNAME);
        onCreate(db);
    }

    /**
     * clear all usage data
     */
    public void clear() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + UsageLog.Camera.TNAME);
        onCreate(db);
        db.close();
    }

    public SQLiteDatabase getReadableDatabase() {
        database = SQLiteDatabase.openOrCreateDatabase(DATABASE_FILE_PATH
                + File.separator + UsageLog.DBNAME, null);
        return database;
    }

    public SQLiteDatabase getWritableDatabase() {
        database = SQLiteDatabase.openOrCreateDatabase(DATABASE_FILE_PATH
                + File.separator + UsageLog.DBNAME, null);
        return database;
    }

}
