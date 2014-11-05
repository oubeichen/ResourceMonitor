package com.oubeichen.resourcemonitor;

import static de.robv.android.xposed.XposedHelpers.findClass;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.AndroidAppHelper;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;
import android.text.TextUtils;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class CameraMonitor implements IXposedHookLoadPackage {

    // frameworks/base/core/java/android/hardware/Camera.java
    // http://developer.android.com/reference/android/hardware/Camera.html

    public static final Uri CONTENT_URI = Uri.parse("content://com.oubeichen.resourcemonitor/camerausage");
    public static final String CLASS_NAME = "android.hardware.Camera";
    
    public void handleLoadPackage(final LoadPackageParam lpparam)
            throws Throwable {

        Class<?> hookClass = null;
        try {
            hookClass = findClass(CLASS_NAME, lpparam.classLoader);
        } catch (Throwable ex) {
            XposedBridge.log("Class or method not found");
        }
        for (Method hookMethod : hookClass.getDeclaredMethods()) {
            
            XposedBridge.hookMethod(hookMethod, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param)
                        throws Throwable {
                    long time = System.currentTimeMillis();
                    insertlog(lpparam.packageName, param.method.getName(), 1, time);

                }

                @Override
                protected void afterHookedMethod(MethodHookParam param)
                        throws Throwable {
                    long time = System.currentTimeMillis();
                    insertlog(lpparam.packageName, param.method.getName(), 2, time);
                }
            });
        }
    }

    /**
     * insert a new log
     * @param packagename
     * @param methodname
     * @param type
     * @param time
     * @return
     */
    public long insertlog(String packagename, String methodname, int type, long time) {
        ContentResolver res = AndroidAppHelper.currentApplication().getContentResolver();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
        Date date = new Date(time);
        ContentValues cv = new ContentValues();
        cv.put(UsageLog.Camera.PACKAGENAME, packagename);
        cv.put(UsageLog.Camera.METHODNAME, methodname);
        cv.put(UsageLog.Camera.TYPE, type);
        cv.put(UsageLog.Camera.TIME, sdf.format(date));
        Uri uri = res.insert(CONTENT_URI, cv);
        String lastPath = uri.getLastPathSegment();
        if (TextUtils.isEmpty(lastPath)) {
            XposedBridge.log("insert failure!");
            return -1;
        }
        return Integer.parseInt(lastPath);  
    }
}
