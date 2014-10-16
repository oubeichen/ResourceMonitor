package com.oubeichen.resourcemonitor;

import static de.robv.android.xposed.XposedHelpers.findClass;
import java.lang.reflect.Method;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class CameraMonitor implements IXposedHookLoadPackage {

    // frameworks/base/core/java/android/hardware/Camera.java
    // http://developer.android.com/reference/android/hardware/Camera.html

    public void handleLoadPackage(final LoadPackageParam lpparam)
            throws Throwable {

        Class<?> hookClass = null;
        Method open = null, release = null;
        try {
            hookClass = findClass("android.hardware.Camera",
                    lpparam.classLoader);
            open = hookClass.getDeclaredMethod("startPreview");
            release = hookClass.getDeclaredMethod("stopPreview");
        } catch (Throwable ex) {
            XposedBridge.log("Class or method not found");
        }

        XposedBridge.hookMethod(open, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param)
                    throws Throwable {
                long time = System.currentTimeMillis();
                XposedBridge.log("Started camera: " + lpparam.packageName
                        + " at time: " + time);

                // open and update database
                DatabaseHelper dbHelper = new DatabaseHelper();
                dbHelper.insert("camerausage", lpparam.packageName, time);

            }

            @Override
            protected void afterHookedMethod(MethodHookParam param)
                    throws Throwable {
                long time = System.currentTimeMillis();
                XposedBridge.log("Started camera2: " + lpparam.packageName
                        + " at time: " + time);
            }
        });
        XposedBridge.hookMethod(release, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param)
                    throws Throwable {
                long time = System.currentTimeMillis();
                XposedBridge.log("Stopped camera: " + lpparam.packageName
                        + " at time: " + time);
            }

            @Override
            protected void afterHookedMethod(MethodHookParam param)
                    throws Throwable {
                long time = System.currentTimeMillis();
                XposedBridge.log("Stopped camera2: " + lpparam.packageName
                        + " at time: " + time);

                // open and update database
                DatabaseHelper dbHelper = new DatabaseHelper();
                dbHelper.update("camerausage", lpparam.packageName, time);

            }
        });
    }
}
