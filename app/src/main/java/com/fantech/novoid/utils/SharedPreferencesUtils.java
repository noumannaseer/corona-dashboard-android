package com.fantech.novoid.utils;

import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.val;

import static android.content.Context.MODE_PRIVATE;
import static com.fantech.novoid.utils.AndroidUtil.getApplicationContext;

public class SharedPreferencesUtils
{


    public static final String PREF_KEY_FILE_NAME = getApplicationContext()
            .getPackageName();
    public static final String LAST_UPDATED_TIME = "LAST_UPDATED_TIME";


    public static void setValue(String key, boolean status)
    {
        SharedPreferences sharedPreferences = getApplicationContext()
                .getSharedPreferences(PREF_KEY_FILE_NAME,
                                      MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, status);
        editor.commit();
    }

    public static void setValue(String key, long value)
    {
        SharedPreferences sharedPreferences = getApplicationContext()
                .getSharedPreferences(PREF_KEY_FILE_NAME,
                                      MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(key, value);
        editor.commit();
    }


    public static void setValue(String key, String value)
    {
        SharedPreferences sharedPreferences = getApplicationContext()
                .getSharedPreferences(PREF_KEY_FILE_NAME,
                                      MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static void setValue(String key, int value)
    {
        SharedPreferences sharedPreferences = getApplicationContext()
                .getSharedPreferences(PREF_KEY_FILE_NAME,
                                      MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.apply();
        editor.commit();
    }


    public static void setListValue(String key, String item)
    {
        SharedPreferences sharedPreferences = getApplicationContext()
                .getSharedPreferences(PREF_KEY_FILE_NAME,
                                      MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Set<String> stringSet = sharedPreferences.getStringSet(key, null);
        if (stringSet == null)
            stringSet = new HashSet<>();
        stringSet.add(item);
        editor.putStringSet(key, stringSet);
        editor.apply();
        editor.commit();
    }

    public static List<String> getListValue(String key)
    {
        SharedPreferences sharedPreferences = getApplicationContext()
                .getSharedPreferences(PREF_KEY_FILE_NAME,
                                      MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Set<String> stringSet = sharedPreferences.getStringSet(key, null);
        if (stringSet == null)
            stringSet = new HashSet<>();
        List<String> stringList = new ArrayList<>();
        for (val setItem : stringSet)
        {
            stringList.add(setItem);
        }

        return stringList;
    }


    public static int getInt(String key)
    {
        SharedPreferences sharedPreferences = getApplicationContext()
                .getSharedPreferences(PREF_KEY_FILE_NAME,
                                      MODE_PRIVATE);
        int value = sharedPreferences.getInt(key, 0);
        return value;

    }


    public static long getLong(String key)
    {
        SharedPreferences sharedPreferences = getApplicationContext()
                .getSharedPreferences(PREF_KEY_FILE_NAME,
                                      MODE_PRIVATE);
        long value = sharedPreferences.getLong(key, 0);
        return value;

    }

    public static boolean getBoolean(String key)
    {
        SharedPreferences sharedPreferences = getApplicationContext()
                .getSharedPreferences(PREF_KEY_FILE_NAME,
                                      MODE_PRIVATE);
        boolean value = sharedPreferences.getBoolean(key, false);
        return value;

    }

    public static String getString(String key)
    {
        SharedPreferences sharedPreferences = getApplicationContext()
                .getSharedPreferences(PREF_KEY_FILE_NAME,
                                      MODE_PRIVATE);
        String value = sharedPreferences.getString(key, null);
        return value;

    }
}
