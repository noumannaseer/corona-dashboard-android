package com.fantech.covidplus.services;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface CoronaServices
{
    @GET("csse_covid_19_time_series/time_series_covid19_deaths_global.csv")
    Call<ResponseBody>
    getConfirmStats();
}
