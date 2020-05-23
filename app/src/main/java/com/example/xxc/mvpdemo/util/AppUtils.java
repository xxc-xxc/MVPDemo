package com.example.xxc.mvpdemo.util;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.text.TextUtils;

import com.example.xxc.mvpdemo.bean.ApkInfo;

import java.util.ArrayList;
import java.util.List;


public class AppUtils {

    /**
     * 获取packInfo
     */
    public static PackageInfo getPackageInfo(Context context, String packageName, int flag) {
        PackageInfo packageInfo = null;
        if (null == context || StringUtil.isStringEmpty(packageName)) {
            return null;
        }
        try {
            PackageManager pm = context.getPackageManager();
            if (pm != null) {
                packageInfo = pm.getPackageInfo(packageName, flag);
            }
        } catch (PackageManager.NameNotFoundException e) {
            // Log.e("找不到该包名");
        }

        return packageInfo;
    }


    /**
     * 获取签名
     *
     * @param context
     * @param packageName
     * @return
     */
    public static Signature[] getSignature(Context context, String packageName) {
        Signature[] signatures = null;

        PackageInfo packInfo = getPackageInfo(context, packageName, PackageManager.GET_SIGNATURES);
        if (null != packInfo) {
            signatures = packInfo.signatures;
        }

        return signatures;
    }


    /**
     * 获取包名
     */
    public static String getPackage(Context context) {
        return StringUtil.setStringIfEmpty(context.getPackageName());
    }

    /**
     * 获取签名MD5
     */
    public static String getSignatureMd5(Context context, String packageName) {
        String md5 = null;

        Signature[] signs = getSignature(context, packageName);
        if (null != signs && signs.length > 0) {

            StringBuilder sb = new StringBuilder();
            for (Signature sig : signs) {
                sb.append(sig.toCharsString());
            }

            //  rlt = SecUtils.getInformationFingerprintByMD5(sb.toString(), StrCharset.UTF_8);
        }
        return StringUtil.isStringEmpty(md5) ? null : md5;
    }

    /**
     * 获取app名称
     */
    public static String getAppName(Context context) {
        if (null == context) {
            return null;
        }

        PackageManager packageManager = context.getPackageManager();
        PackageInfo pakeInfo = getPackageInfo(context, context.getPackageName(), PackageManager
                .GET_UNINSTALLED_PACKAGES);
        if (pakeInfo != null) {
            ApplicationInfo appInfo = pakeInfo.applicationInfo;
            return appInfo.loadLabel(packageManager)
                    .toString();
        }
        return null;
    }


    /**
     * 获取app版本名称
     */
    public static String getAppVersionName(Context context) {
        if (null == context) {
            return null;
        }

        PackageInfo packageInfo = getPackageInfo(context, context.getPackageName(), PackageManager
                .GET_UNINSTALLED_PACKAGES);
        return null == packageInfo ? null : packageInfo.versionName;
    }


    /**
     * 获取app版本号
     */
    public static int getAppVersionCode(Context context, String packageName) {
        if (null == context) {
            return 0;
        }

        PackageInfo packageInfo = getPackageInfo(context, packageName, PackageManager
                .GET_UNINSTALLED_PACKAGES);
        return null == packageInfo ? 0 : packageInfo.versionCode;
    }

    /**
     * 判断apk是否存在
     */
    public static boolean isApkExist(Context context, String packageName) {
        PackageInfo packageInfo = getPackageInfo(context, packageName, PackageManager.GET_UNINSTALLED_PACKAGES);
        return packageInfo != null;
    }


    /**
     * 获取meta的值
     */
    public static String getMetaData(Context context, String key) {

        PackageInfo packageInfo = getPackageInfo(context, context.getPackageName(), PackageManager.GET_META_DATA);

        ApplicationInfo appInfo = packageInfo.applicationInfo;
        Bundle metaData = (null != appInfo) ? appInfo.metaData : null;

        return (null != metaData && !StringUtil.isStringEmpty(key)) ? String.valueOf(metaData.get(key)) : null;

    }

    /**
     * 通过包名启动apk
     */
    public static boolean startApk(Context context, String packageName) {
        boolean rlt = false;

        if (isApkExist(context, packageName)) {
            PackageManager pkgMgr = context.getPackageManager();
            if (null != pkgMgr) {
                Intent intent = pkgMgr.getLaunchIntentForPackage(packageName);
                if (null != intent) {
                    context.startActivity(intent);
                    rlt = true;
                }
            }
        }

        return rlt;
    }


    /**
     * 通过包名停止apk
     */
    public static boolean stopApk(Context context, String packageName) {
        boolean rlt = false;
        // 应用存在,且关闭的不是自己
        if (isApkExist(context, packageName) && !context.getPackageName().equalsIgnoreCase(packageName)) {
            ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            if (null != manager) {
                manager.killBackgroundProcesses(packageName);
                rlt = true;
            }
        }

        return rlt;
    }


    /**
     * 获取已安装的所有apk的信息
     */
    public static List<ApkInfo> getInstallApkInfo(Context context) {
        ArrayList<ApkInfo> appList = new ArrayList<>(); //用来存储获取的应用信息数据
        List<PackageInfo> packages = context.getPackageManager().getInstalledPackages(0);

        for (int i = 0; i < packages.size(); i++) {
            PackageInfo packageInfo = packages.get(i);
            ApkInfo tmpInfo = new ApkInfo();
            tmpInfo.apkName = packageInfo.applicationInfo.loadLabel(context.getPackageManager()).toString();
            tmpInfo.pkgName = packageInfo.packageName;
            tmpInfo.apkVerName = packageInfo.versionName;
            tmpInfo.verCode = packageInfo.versionCode;
            if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                appList.add(tmpInfo);//如果非系统应用，则添加至appList
            }
        }
        return appList;
    }


    /**
     * 获取栈中的所有activity
     */
    public static List<ActivityManager.RunningTaskInfo> getRunningTaskInfo(Context context) {
        List<ActivityManager.RunningTaskInfo> taskInfoList = null;

        if (null == context) {
            return null;
        }

        ActivityManager aManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (null != aManager) {
            taskInfoList = aManager.getRunningTasks(100);
        }

        return taskInfoList;
    }

    /**
     * 判断该activity是否在栈顶
     */
    public static boolean isAppRunningTopActivity(Context context, String activityName) {
        boolean isTop = false;

        if (null == context || TextUtils.isEmpty(activityName)) {
            return isTop;
        }

        List<ActivityManager.RunningTaskInfo> runningTasks = getRunningTaskInfo(context);
        if (null != runningTasks && !runningTasks.isEmpty()) {
            String topClsName = runningTasks.get(0).topActivity.getClassName();
            // LoggerUtil.e("topClsName:" + topClsName);
            isTop = !TextUtils.isEmpty(topClsName) && topClsName.equals(activityName);
        }

        return isTop;
    }


    /**
     * 判断是否为系统apk
     */
    public static boolean isSystemApp(Context context, String packageName) {
        PackageInfo pi = getPackageInfo(context, packageName, 0);
        if ((pi.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) > 0) {
            return true;
        }
        return false;
    }

    /**
     * 获取应用的启动页className
     */
    public static String getAppStartActivity(Context context, String packageName) {
        if (packageName.isEmpty()) {
            return "";
        }

        PackageManager packageManager = context.getPackageManager();
        Intent intent = packageManager.getLaunchIntentForPackage(packageName);
        if (intent != null) {
            ComponentName componentName = intent.getComponent();
            String name = componentName.getClassName();
            return name;
        }
        return "";

    }

    /**
     * 判断某个任务是否在运行
     *
     * @param context
     * @param processName
     * @return
     */
    public static boolean isRunningTaskExist(Context context, String processName) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> processList = am.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo info : processList) {
            if (info.processName.equals(processName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断某个服务是否存在
     *
     * @param context
     * @param serName
     * @return
     */
    public static boolean isServicesExisted(Context context, String serName) {

        ActivityManager ac = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList = ac.getRunningServices(Integer.MAX_VALUE);
        for (int i = 0; i < serviceList.size(); i++) {
            ActivityManager.RunningServiceInfo serviceInfo = serviceList.get(i);
            ComponentName serviceName = serviceInfo.service;
            if (serviceName.getClassName().equals(serName)) {
                return true;
            }
        }
        return false;
    }

}
