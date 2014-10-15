package com.oubeichen.resourcemonitor;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import static de.robv.android.xposed.XposedHelpers.findClass;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class Cameramonitor implements IXposedHookLoadPackage {
	
	//http://developer.android.com/reference/android/hardware/Camera.html

    public void handleLoadPackage(final LoadPackageParam lpparam) throws Throwable {

    	Class<?> hookClass = null;
    	try{
    		hookClass = findClass("android.hardware.Camera", lpparam.classLoader);
    	}catch (Throwable ex) {
    		XposedBridge.log("Class not found");

		}
    	for (Method method : hookClass.getDeclaredMethods()){
    		XposedBridge.log(method.getName());
    	}
    	Method open = hookClass.getDeclaredMethod("open");
    	XposedBridge.hookMethod(open, new XC_MethodHook() {
        	@Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
        		long time=System.currentTimeMillis();
        		XposedBridge.log("Started camera: " + lpparam.packageName  + " at time: " + time);
				Bundle bundle = new Bundle();
				bundle.putString("type", "insert");
				bundle.putString("table", "camerausage");
				bundle.putString("packagename", lpparam.packageName);
				bundle.putLong("time", time);
				
				Intent intent = new Intent(ContextUtil.getInstance(), UpdateDBService.class);
				intent.putExtras(bundle);
				ContextUtil.getInstance().startService(intent);
            }
        	@Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
        		long time=System.currentTimeMillis();
        		XposedBridge.log("Started camera2: " + lpparam.packageName  + " at time: " + time);
            }
        });
    	Method release = hookClass.getDeclaredMethod("release");
    	XposedBridge.hookMethod(release, new XC_MethodHook() {
        	@Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
        		long time=System.currentTimeMillis();
        		XposedBridge.log("Stopped camera: " + lpparam.packageName  + " at time: " + time);
            }
        	@Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
        		long time=System.currentTimeMillis();
        		XposedBridge.log("Stopped camera2: " + lpparam.packageName  + " at time: " + time);
				Bundle bundle = new Bundle();
				bundle.putString("type", "update");
				bundle.putString("table", "camerausage");
				bundle.putString("packagename", lpparam.packageName);
				bundle.putLong("time", time);
				
				Intent intent = new Intent(ContextUtil.getInstance(), UpdateDBService.class);
				intent.putExtras(bundle);
				ContextUtil.getInstance().startService(intent);
            }
        });
    }
}	
