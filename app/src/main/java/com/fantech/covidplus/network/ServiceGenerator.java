package com.fantech.covidplus.network;


import com.fantech.covidplus.R;
import com.fantech.covidplus.utils.AndroidUtil;

import lombok.Getter;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;


//************************************************
public class ServiceGenerator
//************************************************
{
    private @Getter
    static Retrofit mRetroFit;

    //**************************************
    public static Retrofit getRetrofitInstance(String baseUrl)
    //**************************************
    {
        if (mRetroFit == null)
        {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(interceptor)
                    .build();

            mRetroFit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(client)
                    .build();
        }
        return mRetroFit;
    }


    //**************************************
    public static <S> S createService(Class<S> serviceClass, String baseUrl)
    //**************************************
    {
        if (!AndroidUtil.isNetworkStatusAvialable())
        {
            AndroidUtil.toast(false, AndroidUtil.getString(R.string.no_internet));
            return null;
        }
        return getRetrofitInstance(baseUrl).create(serviceClass);
    }

}
