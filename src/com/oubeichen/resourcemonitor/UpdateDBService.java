package com.oubeichen.resourcemonitor;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;

public class UpdateDBService extends Service{

	DatabaseHelper dbHelper;
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public void onCreate() {
		dbHelper = new DatabaseHelper(ContextUtil.getInstance());
	}

	public void onStart(Intent intent, int startId) {
		Bundle bundle = intent.getExtras();
		String type = bundle.getString("type");
		String packagename = bundle.getString("packagename");
		String table = bundle.getString("table");
		Long time = bundle.getLong("time");
		
		if(type.equals("insert")){
    		DatabaseHelper dbHelper = new DatabaseHelper(ContextUtil.getInstance());
    		dbHelper.getWritableDatabase();
    		dbHelper.insert(table, packagename, time);
		}else if(type.equals("update")){
    		DatabaseHelper dbHelper = new DatabaseHelper(ContextUtil.getInstance());
    		dbHelper.getWritableDatabase();
    		dbHelper.insert(table, packagename, time);
		}
	}

	public void onDestroy() {
		
	}
}
