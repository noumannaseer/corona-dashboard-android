package com.fantech.novoid;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.fantech.novoid.utils.AndroidUtil;
import com.google.firebase.analytics.FirebaseAnalytics;

import io.fabric.sdk.android.Fabric;
import lombok.NonNull;

//*************************************************
public class Covid
        extends Application
//*************************************************
{
    private FirebaseAnalytics mFirebaseAnalytics;

    //*************************************************
    @Override
    public void onCreate()
    //*************************************************
    {
        super.onCreate();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        AndroidUtil.setContext(getApplicationContext());
        Fabric.with(this, new Crashlytics());
    }


    //*********************************************************************
    public static @NonNull
    Covid instance()
    //*********************************************************************
    {
        return (Covid)AndroidUtil.getApplicationContext();
    }

}
