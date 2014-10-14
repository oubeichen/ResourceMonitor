package com.oubeichen.resourcemonitor;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class Cameramonitor implements IXposedHookLoadPackage {
	
	//http://developer.android.com/reference/android/hardware/Camera.html

    public void handleLoadPackage(final LoadPackageParam lpparam) throws Throwable {

        findAndHookMethod("android.hardware.Camera", lpparam.classLoader, "open", new XC_MethodHook() {
        	@Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
        		long time=System.currentTimeMillis();
        		XposedBridge.log("Started camera: " + lpparam.packageName  + " at time: " + time);
            }
        	@Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
        		long time=System.currentTimeMillis();
        		XposedBridge.log("Started camera2: " + lpparam.packageName  + " at time: " + time);
            }
        });
        
        findAndHookMethod("android.hardware.Camera", lpparam.classLoader, "release", new XC_MethodHook() {
        	@Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
        		long time=System.currentTimeMillis();
        		XposedBridge.log("Stopped camera: " + lpparam.packageName  + " at time: " + time);
            }
        	@Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
        		long time=System.currentTimeMillis();
        		XposedBridge.log("Stopped camera2: " + lpparam.packageName  + " at time: " + time);
            }
        });
    }
}	
