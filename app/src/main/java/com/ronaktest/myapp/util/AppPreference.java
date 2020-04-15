package com.ronaktest.myapp.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.widget.Toast;

public class AppPreference {
    private static AppPreference appPreference;
    private final SharedPreferences sharedPreferences;

    private AppPreference(Context context) {
        sharedPreferences = context.getSharedPreferences(Constants.APP_FOLDER, Context.MODE_PRIVATE);
    }

    public static AppPreference getInstance(Context context) {
        if (appPreference == null)
            appPreference = new AppPreference(context);

        return appPreference;
    }

    public void removeKey(String key) {
        sharedPreferences.edit().remove(key).commit();
    }

    public void putLong(String key, long val) {
        sharedPreferences.edit().putLong(key, val).apply();
    }

    public long getLong(String key) {
        return sharedPreferences.getLong(key, -1);
    }

    public void putString(String key, String val) {
        sharedPreferences.edit().putString(key, val).apply();
    }

    public String getString(String key) {
        return sharedPreferences.getString(key, "");
    }

    public void putBoolean(String key, boolean val) {
        sharedPreferences.edit().putBoolean(key, val).apply();
    }

    public boolean getBoolean(String key) {
        return sharedPreferences.getBoolean(key, false);
    }

    public boolean isKeyExist(String key) {
        return sharedPreferences.contains(key);
    }

    public void showToast(Context mContext, String msg) {
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
    }

    public void putInt(String key, int val) {
        sharedPreferences.edit().putInt(key, val).apply();
    }

    public int getInt(String key) {
        return sharedPreferences.getInt(key, 0);
    }

    public static boolean isAppInstalled(Context context, String packageName) {
        try {
            context.getPackageManager().getApplicationInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
}