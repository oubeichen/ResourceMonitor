package com.oubeichen.resourcemonitor;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

	public DatabaseHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}
	public DatabaseHelper(Context context){
		super(context, "usagedata", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
        db.execSQL("CREATE TABLE IF NOT EXISTS camerausage (id INTEGER PRIMARY KEY AUTOINCREMENT, packagename VARCHAR, lastexecuted BIGINT, usage BIGINT)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldver, int newver) {
		// TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS camerausage");
        onCreate(db);
	}

	/**
     * return all usage data
     * @param table
     * @return
     */
    public List<String> selectAll(String table){
        SQLiteDatabase db = this.getReadableDatabase();    	
    	String packagename;
    	long usage;

        Cursor c = db.query(table, new String[]{"packagename", "usage"}, null, null, null, null, null);
    	List<String> list = new ArrayList<String>();
        while (c.moveToNext()) { 
            packagename = c.getString(c.getColumnIndex("packagename"));
            usage = c.getLong(c.getColumnIndex("usage"));
            String string = "" + packagename + " \t" + usage;
            list.add(string);    
        }  
        c.close();
        return list;
    }
    
    /**
     * insert a usage data
     * @param table
     * @param packagename
     * @param lastexecuted
     * @return
     */
    public long insert(String table, String packagename, long lastexecuted){
        SQLiteDatabase db = this.getWritableDatabase();
        long ret = -1;
        ContentValues cv = new ContentValues();
        Cursor c = db.query(table, null, "packagename = ?", new String[]{packagename}, null, null, null);
        
        cv.put("lastexecuted", lastexecuted);
        if(c.moveToFirst() == false){//empty cursor
        	// insert a row
            cv.put("packagename", packagename);
            cv.put("usage", 0);//zero when initializing
            ret = db.insert(table, null, cv);
        }else{
        	// update exist row
        	ret = db.update(table, cv, "packagename = ?", new String[]{packagename});
        }

        db.close();
        return ret;
    }
    
    /**
     * clear all usage data
     */
    public void clear()
    {
    	SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS camerausage");
        onCreate(db);
        db.close();
    }
    
    /**
     * update usage data
     * @param table
     * @param packagename
     * @param usage
     * @return 
     */
    public int update(String table, String packagename, long time)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        int ret = -1;
        ContentValues cv=new ContentValues(); 
        Cursor c = db.query(table, null, "packagename = ?", new String[]{packagename}, null, null, "id asc");
        if(c.moveToFirst() == false){//empty cursor
        	//nothing because the usage data of the package doesn't exist
        	
        }else{
        	// update usage and reset lastexecuted
        	long usage = c.getLong(c.getColumnIndex("usage"));
        	long lastexecuted = c.getLong(c.getColumnIndex("lastexecuted"));

        	usage = usage + (time - lastexecuted);
        	cv.put("lastexecuted", 0);
        	cv.put("usage", usage);
        	ret = db.update(table, cv, "packagename = ?", new String[]{packagename});
        }
        db.close();
        return ret;
    }
}
