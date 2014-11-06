package com.oubeichen.resourcemonitor;

import java.util.HashMap;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

public class UsageProvider extends ContentProvider {

    DatabaseHelper helper;
    private static HashMap<String, String> cameraUsageMap;

    public static final int CAMERA = 1;
    public static final int CAMERA_ID = 2;

    private static final UriMatcher sMatcher;

    static{
            sMatcher = new UriMatcher(UriMatcher.NO_MATCH);
            sMatcher.addURI(UsageLog.AUTHORITY, "camerausage", CAMERA);
            sMatcher.addURI(UsageLog.AUTHORITY, "camerausage" + "/#", CAMERA_ID); 
  
            cameraUsageMap = new HashMap<String, String>();
            cameraUsageMap.put(UsageLog.Camera._ID, UsageLog.Camera._ID);
            cameraUsageMap.put(UsageLog.Camera.PACKAGENAME, UsageLog.Camera.PACKAGENAME);
            cameraUsageMap.put(UsageLog.Camera.METHODNAME, UsageLog.Camera.METHODNAME); 
            cameraUsageMap.put(UsageLog.Camera.TYPE, UsageLog.Camera.TYPE);
            cameraUsageMap.put(UsageLog.Camera.TIME, UsageLog.Camera.TIME);
    }

    @Override
    public int delete(Uri uri, String where, String[] whereArgs) {
        SQLiteDatabase db = helper.getWritableDatabase();
        int count = 0;
        switch (sMatcher.match(uri)) {
        case CAMERA:
            count = db.delete(UsageLog.Camera.TNAME, where, whereArgs);
            break;
        case CAMERA_ID:
            String id = uri.getPathSegments().get(1);
            count = db.delete(UsageLog.Camera.TNAME, UsageLog.Camera._ID
                            + "=" + id
                            + (!TextUtils.isEmpty(where) ?
                            " AND (" + where + ')'
                            : ""), whereArgs);
            break;
        default:
            throw new IllegalArgumentException("Unknown URI "+uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        
        return count;
    }

    @Override
    public String getType(Uri uri) {
        switch (sMatcher.match(uri)) {
        case CAMERA:
            return UsageLog.CONTENT_TYPE;
        case CAMERA_ID:
            return UsageLog.CONTENT_ITEM_TYPE;
        default:
            throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues initialValues) {
        // Validate the requested uri
        if (sMatcher.match(uri) != CAMERA) {
            throw new IllegalArgumentException("Unknown URI " + uri);
        }

        ContentValues values;
        if (initialValues != null) {
            values = new ContentValues(initialValues);
        } else {
            values = new ContentValues();
        }

        // Make sure that the fields are all set
        if (values.containsKey(UsageLog.Camera.PACKAGENAME) == false) {
            values.put(UsageLog.Camera.PACKAGENAME, "");
        }
        if (values.containsKey(UsageLog.Camera.METHODNAME) == false) {
            values.put(UsageLog.Camera.METHODNAME, "");
        }
        if (values.containsKey(UsageLog.Camera.TYPE) == false) {
            values.put(UsageLog.Camera.TYPE, -1);
        }
        if (values.containsKey(UsageLog.Camera.TIME) == false) {
            values.put(UsageLog.Camera.TIME, "");
        }

        SQLiteDatabase db = helper.getWritableDatabase();
        long rowId = db.insert(UsageLog.Camera.TNAME, UsageLog.Camera.PACKAGENAME, values);
        if (rowId > 0) {
            Uri noteUri = ContentUris.withAppendedId(UsageLog.Camera.CONTENT_URI, rowId);
            getContext().getContentResolver().notifyChange(noteUri, null);
            return noteUri;
        }
        throw new SQLException("Failed to insert row into " + uri); 
    }

    @Override
    public boolean onCreate() {
        helper = new DatabaseHelper(getContext());
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
            String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(UsageLog.Camera.TNAME);

        switch (sMatcher.match(uri)) {
        case CAMERA:
            qb.setProjectionMap(cameraUsageMap);
            break;

        case CAMERA_ID:
            qb.setProjectionMap(cameraUsageMap);
            qb.appendWhere(UsageLog.Camera._ID + "=" + uri.getPathSegments().get(1));
            break;

        default:
            throw new IllegalArgumentException("Unknown URI " + uri);
        }
        // If no sort order is specified use the default
        String orderBy;
        if (TextUtils.isEmpty(sortOrder)) {
            orderBy = UsageLog.DEFAULT_SORT_ORDER;
        } else {
            orderBy = sortOrder;
        }

        // Get the database and run the query
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, orderBy);

        // Tell the cursor what uri to watch, so it knows when its source data changes
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Override
    public int update(Uri arg0, ContentValues arg1, String arg2, String[] arg3) {
        // TODO Auto-generated method stub
        return 0;
    }

}
