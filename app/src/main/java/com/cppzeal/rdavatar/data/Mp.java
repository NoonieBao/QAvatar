package com.cppzeal.rdavatar.data;

import android.content.Context;
import android.content.SharedPreferences;

public class Mp {

    private static final String SHARED_PREF_NAME = "qavatar";
    private static SharedPreferences sharedPreferences = null;

    public static final String LAST_UPLOAD = "LAST_UPLOAD"; // 静态变量作为键
    public static final String LAST_DOWNLOAD = "LAST_DOWNLOAD"; // 静态变量作为键

    public static final String UPLOAD_FRE = "UPLOAD_FRE"; // 静态变量作为键
    public static final String DOWNLOAD_FRE = "DOWN_FRE"; // 静态变量作为键

    public static final String TOAST_SWITCH = "TOAST_SWITCH"; // 静态变量作为键
    public static final String NOTIFY_SWITCH = "NOTIFY_SWITCH"; // 静态变量作为键

    public static final String DOWNLOAD_SWITCH = "DOWNLOAD_SWITCH"; // 静态变量作为键

    public static final String DOWNLOAD_URL = "DOWNLOAD_URL"; // 静态变量作为键

    public static final String HAS_SET_URL = "FIRST_SET_URL"; // 静态变量作为键

    public static final String TARGRT_ACC = "TARGRT_ACC"; // 静态变量作为键

    public static final String GLOBAL_SWITCH = "TARGRT_ACC"; // 静态变量作为键

    private static final String DEF_FILE_NAME = "https://avatar.corosy.com/"; // 文件保存的本地文件名x

//    private static final String DEF_FILE_NAME = "https://avatar.insomnia.icu/"; // 文件保存的本地文件名x

    public static final String INFO_FROM_DEV_URL = "https://json.api.corosy.com/qavatarinfo";

    private static final String INFO_FROM_DEV = "INFO_FROM_DEV";

    private static final String LAST_INFO_TIME = "LAST_INFO_TIME"; //秒为单位


    public  static long getLastInfoTime(Context context){
        return loadLong(context, LAST_INFO_TIME);
    }
    public static boolean needGetInfo(Context context){
        long l = loadLong(context, LAST_INFO_TIME);
        long currentTimeSeconds = System.currentTimeMillis() / 1000; // 当前时间戳（精确到秒）
        return l < currentTimeSeconds + 3600 * 24;
    }

    public static String retrieveInfoFromSp(Context context){
        return loadStr(context, INFO_FROM_DEV);
    }

    public static void onUpdateInfoFromDev(Context context){
        long currentTimeSeconds = System.currentTimeMillis() / 1000; // 当前时间戳（精确到秒）
        saveData(context,LAST_INFO_TIME,currentTimeSeconds);
    }
    public  static void  saveInfoFromDev(Context context, Object value){
        saveData(context,INFO_FROM_DEV,value);
    }
    public static final long MIN_PRE = 1 * 60; // 静态变量作为键

    private static SharedPreferences getSp(Context context) {

        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        }
        return sharedPreferences;
    }

    public static boolean globalSwitch(Context context) {
        return loadBool(context, GLOBAL_SWITCH);
    }

    public static boolean downloadSwitch(Context context) {
        //
        return loadBool(context, DOWNLOAD_SWITCH);
    }

    public static void saveUrl(Context context, String key, Object value) {
        saveData(context, HAS_SET_URL, true);
        saveData(context, key, value);
    }

    public static void saveData(Context context, String key, Object value) {
        
        SharedPreferences.Editor editor = getSp(context).edit();
        if (value instanceof String) {
            editor.putString(key, (String) value);
        } else if (value instanceof Boolean) {
            editor.putBoolean(key, (Boolean) value);
        } else if (value instanceof Long) {
            editor.putLong(key, (Long) value);
        }

        editor.apply();

    }

    public static void saveData(Context context) {
        long currentTimeSeconds = System.currentTimeMillis() / 1000; // 当前时间戳（精确到秒）
        saveData(context, LAST_UPLOAD, currentTimeSeconds);
    }

    public static void tryDownload(Context context) {
        long currentTimeSeconds = System.currentTimeMillis() / 1000; // 当前时间戳（精确到秒）
        saveData(context,LAST_DOWNLOAD,currentTimeSeconds);
    }

    // 读取保存的时间戳

    public static String getDownloadUrl(Context context) {
        if (!loadBool(context, HAS_SET_URL)) {
            return DEF_FILE_NAME;
        }
        return loadStr(context, DOWNLOAD_URL);
    }

    public static String getTargetAcc(Context context) {
        long l = loadLong(context, TARGRT_ACC);
        if(l==0){
            return "输入当前QQ号";
        }
        return String.valueOf(l);
    }
    public static boolean notifySwitch(Context context) {
        return loadBool(context, NOTIFY_SWITCH);
    }

    public static boolean toastSwitch(Context context) {
        return loadBool(context, TOAST_SWITCH);
    }

    public static long getUploadFre(Context context) {
        long l = loadLong(context, UPLOAD_FRE);
        if (l < MIN_PRE) {
            l = MIN_PRE;
        }
        return l;
    }

    public static long getDownloadFre(Context context) {
        long l = loadLong(context, DOWNLOAD_FRE);
        if (l < MIN_PRE) {
            l = MIN_PRE;
        }
        return l;
    }
    public static long getLastUploadTime(Context context) {
        return loadLong(context, LAST_UPLOAD);
    }

    public static long calTimeDif(Context context, String key) {
        long savedTimestamp = loadLong(context, key);
        long currentTimestamp = System.currentTimeMillis() / 1000;
        return currentTimestamp - savedTimestamp;
    }

    public static String loadStr(Context context, String key) {
        SharedPreferences sharedPreferences = getSp(context);
        return sharedPreferences.getString(key, "none.");
    }

    public static long loadLong(Context context, String key) {
        SharedPreferences sharedPreferences = getSp(context);
        return sharedPreferences.getLong(key, 0);
    }

    public static boolean loadBool(Context context, String key) {
        SharedPreferences sharedPreferences = getSp(context);
        return sharedPreferences.getBoolean(key, false);
    }
    // 计算时间差（秒）




    public static boolean shouldUpdateNow(Context context) {

        long l = calTimeDif(context, LAST_UPLOAD);
        if (l > getUploadFre(context)) {
            return true;
        }
        return false;
    }

    public static boolean shouldDownloadNow(Context context) {
        long l = calTimeDif(context, LAST_DOWNLOAD);
        if (l > getDownloadFre(context)) {
            return true;
        }
        return false;
    }



}