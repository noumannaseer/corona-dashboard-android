package com.fantech.novoid;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.fantech.novoid.utils.AndroidUtil;

import io.fabric.sdk.android.Fabric;
import lombok.NonNull;

//*************************************************
public class Covid
        extends Application
//*************************************************
{

    //*************************************************
    @Override
    public void onCreate()
    //*************************************************
    {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        AndroidUtil.setContext(getApplicationContext());
    }


    //*********************************************************************
    public static @NonNull
    Covid instance()
    //*********************************************************************
    {
        return (Covid)AndroidUtil.getApplicationContext();

    }

}