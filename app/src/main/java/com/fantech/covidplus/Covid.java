package com.fantech.covidplus;

import android.app.Application;

import com.fantech.covidplus.others.AndroidUtil;

import androidx.lifecycle.AndroidViewModel;

public class Covid extends Application
{
    @Override
    public void onCreate()
    {
        super.onCreate();
        AndroidUtil.setContext(getApplicationContext());
    }
}
