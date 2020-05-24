package com.example.xxc.mvpdemo.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import java.io.UnsupportedEncodingException;

/**
 * @ClassName: StringsUtil
 * @Model : (所属模块名称)
 * @Description: 字符串操作工具类
 */
public class StringUtil {

    public static final String UNKNOWN = "undef";

    /**
     * getRealStr (检查输入字符是否符合最大长度，如果超大最大长度，截取前面到最大长度的字串)
     *
     * @param str
     * @param maxLength
     * @return String
     */
    public static String getRealStr(String str, int maxLength) {
        String defStr = StringUtil.UNKNOWN;
        if (null != str && maxLength > 0) {
            if (str.length() < maxLength) {
                defStr = str;
            } else {
                defStr = str.substring(0, maxLength);
            }
        }
        return defStr;
    }

    /**
     * @param str 字符串
     * @return boolean true:表示字串为null或者字串为空串,false表示字串不为空
     * @Title: isStringEmpty
     * @Description: 判断字符串是否为空
     */
    public static boolean isStringEmpty(String str) {
        return (null == str || str.trim().length() == 0);
    }

    /**
     * @param str 字符串
     * @return boolean true:表示字串有效,false表示字串为空或者未知
     * @Title: isValidate
     * @Description: 判断字符串是否有效
     */
    public static boolean isValidate(String str) {
        return (!StringUtil.isStringEmpty(str) && !UNKNOWN.equalsIgnoreCase(str.trim()));
    }

    public static boolean isValidates(String... strings) {
        if (null != strings && strings.length > 0) {
            for (int i = 0; i < strings.length; i++) {
                if (StringUtil.isStringEmpty(strings[i])) {
                    return false;
                }
            }
        }
        return true;
    }

    public static String setStringIfEmpty(String strObj) {
        return StringUtil.isStringEmpty(strObj) ? StringUtil.UNKNOWN : strObj;
    }

    public static String setStringIfEmpty(String strObj, String defaultValue) {
        return StringUtil.isStringEmpty(strObj) ? defaultValue : strObj;
    }

    /**
     * @param str 字串
     * @return byte[] 字节流
     * @Title: getUTF8Bytes
     * @Description: 将字串转为utf_8编码的字节流
     */
    public static byte[] getUTF8Bytes(String str) {
        return getBytes(str, "UTF-8");
    }

    public static String getString(byte[] bytes) {
        String rlt = StringUtil.UNKNOWN;
        if (null == bytes || bytes.length == 0) {
            return StringUtil.UNKNOWN;
        }

        try {
            rlt = new String(bytes, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return rlt;
    }

    /**
     * @param str     字串
     * @param charset 编码格式
     * @return byte[] 字节流
     * @Title: getBytes
     * @Description: 将字串转为指定编码格式的字节流
     */
    public static byte[] getBytes(String str, String charset) {
        byte[] bytes = new byte[0];

        if (StringUtil.isStringEmpty(str) || StringUtil.isStringEmpty(charset)) {
            return bytes;
        }

        try {
            bytes = str.getBytes(charset);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return bytes;
    }

    public static String[] getUrl(String[] urlDee, String urlSec) {
        String[] url = new String[urlDee.length];
        if (null != urlDee && urlDee.length > 0 && isValidate(urlSec)) {
            for (int i = 0; i < urlDee.length; i++) {
                url[i] = urlDee[i] + urlSec;
            }
            return url;
        }
        return null;
    }

    public static String getMetaData(Context context, String name) {
        String rlt = null;
        PackageManager pm = context.getPackageManager();
        try {
            ApplicationInfo appInfo = pm.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            if (appInfo != null) {
                Bundle metaData = appInfo.metaData;
                if (metaData != null) {
                    Object obj = metaData.get(name);
                    if (null != obj) rlt = obj.toString();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (isStringEmpty(rlt) ? UNKNOWN : rlt);
    }

    public static String cutStr(String targetStr, String positionStr) {
        return targetStr == null || !targetStr.contains(positionStr) ? null : targetStr.substring(targetStr.indexOf(positionStr) + positionStr.length()).trim();
    }



}
