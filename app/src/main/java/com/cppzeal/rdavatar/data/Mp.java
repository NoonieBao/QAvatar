package com.cppzeal.rdavatar.data;

import android.content.Context;
import android.content.SharedPreferences;

public class Mp {

    private static final String SHARED_PREF_NAME = "qavatar";
    private static SharedPreferences sharedPreferences = null;

    public static final String LAST_UPLOAD = "LAST_UPLOAD"; // 上一次上传时间
    public static final String LAST_DOWNLOAD = "LAST_DOWNLOAD"; // 上一次下载时间

    private static final String LAST_INFO_TIME = "LAST_INFO_TIME"; // 上次拉取公告时间

    public static final String UPLOAD_FRE = "UPLOAD_FRE"; // 上传间隔
    public static final String DOWNLOAD_FRE = "DOWN_FRE"; // 下载间隔

    public static final long MIN_PRE = 1 * 60; // 静态变量作为键

    public static final String TOAST_SWITCH = "TOAST_SWITCH"; // Toast开关
    public static final String NOTIFY_SWITCH = "NOTIFY_SWITCH"; // Notification开关

    public static final String DOWNLOAD_SWITCH = "DOWNLOAD_SWITCH"; // 下载开关

    public static final String DOWNLOAD_URL = "DOWNLOAD_URL"; // 静态变量作为键

    public static final String HAS_SET_URL = "FIRST_SET_URL"; // 用户是否自定义了源

    private static final String DEF_FILE_NAME = "https://avatar.corosy.com/"; // 默认的头像源


//    public static final String TARGRT_ACC = "TARGRT_ACC"; // 静态变量作为键

    public static final String GLOBAL_SWITCH = "GLOBAL_SWITCH"; // 静态变量作为键


    public static final String INFO_FROM_DEV_URL = "https://json.api.corosy.com/qavatarinfo";   //公告地址

    private static final String INFO_FROM_DEV = "INFO_FROM_DEV";    //公告


    private static SharedPreferences getSp(Context context) {

        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        }
        return sharedPreferences;
    }


    public static void onTryDownloading(Context context) {
        long currentTimeSeconds = System.currentTimeMillis() / 1000; // 当前时间戳（精确到秒）
        saveData(context, LAST_DOWNLOAD, currentTimeSeconds);
    }

    public static void onRetrievedInfoFromDev(Context context, Object value) {
        long currentTimeSeconds = System.currentTimeMillis() / 1000; // 当前时间戳（精确到秒）
        saveData(context, LAST_INFO_TIME, currentTimeSeconds);
        saveData(context, INFO_FROM_DEV, value);
    }


    // 读取保存的时间戳

    //    public static String getTargetAcc(Context context) {
//        long l = loadLong(context, TARGRT_ACC);
//        if (l == 0) {
//            return "输入当前QQ号";
//        }
//        return String.valueOf(l);
//    }
    public static boolean globalSwitch(Context context) {
        return loadBool(context, GLOBAL_SWITCH);
    }

    public static boolean downloadSwitch(Context context) {
        //
        return loadBool(context, DOWNLOAD_SWITCH);
    }

    public static boolean notifySwitch(Context context) {
        return loadBool(context, NOTIFY_SWITCH);
    }

    public static boolean toastSwitch(Context context) {
        return loadBool(context, TOAST_SWITCH);
    }

    public static long getLastInfoTime(Context context) {
        return loadLong(context, LAST_INFO_TIME);
    }

    public static String getInfoFromDevFromSp(Context context) {
        return loadStr(context, INFO_FROM_DEV);
    }

    public static long getUploadFre(Context context) {
        long l = loadLong(context, UPLOAD_FRE);
        if (l < MIN_PRE) {
            l = MIN_PRE;
        }
        return l;
    }

    public static String getDownloadUrl(Context context) {
        if (!loadBool(context, HAS_SET_URL)) {
            return DEF_FILE_NAME;
        }
        return loadStr(context, DOWNLOAD_URL);
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

    public static boolean shouldRefreshInfo(Context context) {
        long l = loadLong(context, LAST_INFO_TIME);
        long currentTimeSeconds = System.currentTimeMillis() / 1000; // 当前时间戳（精确到秒）
        return l + 3600 * 24 < currentTimeSeconds;
    }

}