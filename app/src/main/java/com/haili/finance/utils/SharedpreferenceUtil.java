package com.haili.finance.utils;

/**
 * Created by pc on 2016/9/7.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.haili.finance.base.BaseApplication;


public class SharedpreferenceUtil {
    public static final String SharedpreferenceName = "sharedData";
    public static SharedPreferences sp = BaseApplication.getApplication().getSharedPreferences(SharedpreferenceName,
            Context.MODE_PRIVATE);

    public static void put(String key, Object value) {
        if (BaseApplication.getApplication() != null) {
            Editor editor = sp.edit();
            if (value instanceof Boolean) {
                editor.putBoolean(key, (Boolean) value);
            } else if (value instanceof Float) {
                editor.putFloat(key, (Float) value);
            } else if (value instanceof Integer) {
                editor.putInt(key, (Integer) value);
            } else if (value instanceof Long) {
                editor.putLong(key, (Long) value);
            } else if (value instanceof String) {
                editor.putString(key, (String) value);
            }
            editor.commit();
        }
    }

    public static boolean getBoolean(String key) {
        return sp.getBoolean(key, false);
    }

    public static boolean getBoolean(String key, boolean def) {
        return sp.getBoolean(key, def);
    }

    public static float getFloat(String key) {
        return sp.getFloat(key, 0);
    }

    public static float getFloat(String key, float def) {
        return sp.getFloat(key, def);
    }

    public static int getInt(String key) {
        return sp.getInt(key, 0);
    }

    public static int getInt(String key, int def) {
        return sp.getInt(key, def);
    }

    public static String getString(String key) {
        String result = "";
        try {
            result = sp.getString(key, "");
        } catch (Exception e) {

        }
        return result;
    }

    public static String getString(String key, String def) {
        String result = "";
        try {
            result = sp.getString(key, def);
        } catch (Exception e) {
        }
        return result;
    }

    public static long getLong(String key) {
        return sp.getLong(key, 0);
    }

    public static long getLong(String key, long def) {
        return sp.getLong(key, def);
    }

    public static void exit() {
        sp.edit().clear().commit();
    }
}