package com.oubeichen.resourcemonitor;

import android.app.Service;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.widget.ListView;

public class Monitorservice extends Service{

	public static final String DB_NAME="usagedata.db";
	SQLiteDatabase db;

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
    public void OpenCreateDB(){  
        db = openOrCreateDatabase(DB_NAME, this.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS camerausage (_id INTEGER PRIMARY KEY AUTOINCREMENT, packagename VARCHAR, lastexecuted BIGINT, usage BIGINT)");
        db.close();
    }
}
