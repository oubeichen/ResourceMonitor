package com.oubeichen.resourcemonitor;

import android.net.Uri;
import android.provider.BaseColumns;

public class UsageLog {
    public static final String DBNAME = "resourcemonitor"; 
    public static final int VERSION = 1;
    
    public static final String AUTHORITY = "com.oubeichen.resourcemonitor";

    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/com.oubeichen.resourcemonitor";
    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/com.oubeichen.resourcemonitor";
    // 表数据列
    public static final String DEFAULT_SORT_ORDER = "_id asc"; 
    
    /** 
     * 保存camerausage表中用到的常量 
     * 
     */  
    public static final class Camera implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.parse("content://"
                + AUTHORITY + "/camerausage");
        public static final String TNAME = "camerausage";
        public static final String PACKAGENAME = "packagename";
        // public static final String CLASSNAME = "classname";
        public static final String METHODNAME = "methodname";
        public static final String TYPE = "type";
        public static final String TIME = "usagetime";
    }
}
