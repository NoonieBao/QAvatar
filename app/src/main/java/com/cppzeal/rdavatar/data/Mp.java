package com.cppzeal.rdavatar.data;

import android.content.Context;
import android.content.SharedPreferences;

public class Mp {

    private static final String SHARED_PREF_NAME = "rdAvadar";
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

    private static final String DEF_FILE_NAME = "https://avatar.insomnia.icu/"; // 文件保存的本地文件名x

    public static final long MIN_PRE = 1 * 60; // 静态变量作为键

    private static SharedPreferences getSp(Context context) {

        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        }
        return sharedPreferences;
    }

    

    public static boolean shouldNotify(Context context) {
        return loadBool(context, NOTIFY_SWITCH);
    }

    public static boolean shouldToast(Context context) {
        return loadBool(context, TOAST_SWITCH);
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

    public static boolean shouldUpdate(Context context) {

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

    public static boolean shouldDownload(Context context) {
        return loadBool(context, DOWNLOAD_SWITCH);
    }

}