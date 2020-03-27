package com.fantech.covidplus;

import android.app.Application;
import android.util.Log;

import com.fantech.covidplus.network.ServiceGenerator;
import com.fantech.covidplus.services.CoronaServices;
import com.fantech.covidplus.utils.AndroidUtil;
import com.fantech.covidplus.utils.Constants;

import androidx.annotation.Nullable;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.val;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
