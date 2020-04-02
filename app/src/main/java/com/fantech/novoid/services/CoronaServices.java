package com.fantech.novoid.services;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;

//*******************************************************************
public interface CoronaServices
//*******************************************************************
{
    //*******************************************************************
    @GET("time_series_covid19_deaths_global.csv")
    Call<ResponseBody>
    //*******************************************************************
    getDeathCases();
    //*******************************************************************


    //*******************************************************************
    @GET("time_series_covid19_confirmed_global.csv")
    Call<ResponseBody>
    //*******************************************************************
    getConfirmedCases();
    //*******************************************************************

    //*******************************************************************
    @GET("time_series_covid19_recovered_global.csv")
    Call<ResponseBody>
    //*******************************************************************
    getRecoveredCases();
    //*******************************************************************


}
