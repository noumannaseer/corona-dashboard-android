package com.fantech.covidplus;

import android.app.Application;

import com.fantech.covidplus.utils.AndroidUtil;

//*************************************************
public class Covid extends Application
//*************************************************
{
    //*************************************************
    @Override
    public void onCreate()
    //*************************************************
    {
        super.onCreate();
        AndroidUtil.setContext(getApplicationContext());
    }
}
