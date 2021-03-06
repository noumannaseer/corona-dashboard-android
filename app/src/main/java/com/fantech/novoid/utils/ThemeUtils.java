package com.fantech.novoid.utils;

import android.app.Activity;
import android.content.SharedPreferences;


import com.fantech.novoid.R;
import com.fantech.novoid.activities.BaseActivity;

import static android.content.Context.MODE_PRIVATE;
import static com.fantech.novoid.utils.AndroidUtil.getApplicationContext;


//****************************************************
public class ThemeUtils
//****************************************************
{
    private static final String CURRENT_THEME = "CURRENT_THEME";
    public static final String PREF_KEY_FILE_NAME = "COVIDPLUS";

    //****************************************************************
    public static void applyTheme(Activity activity)
    //****************************************************************
    {
        if (!getCurrentThemeIsDark())
        {
            activity.getApplicationContext()
                    .setTheme(R.style.AppTheme);
            activity.setTheme(R.style.AppTheme);
        }
        else
        {
            activity.getApplicationContext()
                    .setTheme(R.style.darktheme);
            activity.setTheme(R.style.darktheme);
        }
    }

    //****************************************************************
    public static void setDarkTheme(boolean lightTheme, Activity activity, boolean restartActivity)
    //****************************************************************
    {
        SharedPreferences sharedPreferences = getApplicationContext()
                .getSharedPreferences(PREF_KEY_FILE_NAME,
                                      MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(CURRENT_THEME, lightTheme);
        editor.commit();
        applyTheme(activity);
        if (restartActivity)
            ((BaseActivity)activity).restartActivity();

    }

    //****************************************************************
    public static boolean getCurrentThemeIsDark()
    //****************************************************************
    {
        SharedPreferences sharedPreferences = getApplicationContext()
                .getSharedPreferences(PREF_KEY_FILE_NAME,
                                      MODE_PRIVATE);
        boolean value = sharedPreferences.getBoolean(CURRENT_THEME, false);
        return value;

    }
}