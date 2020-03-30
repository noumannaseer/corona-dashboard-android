package com.fantech.novoid.network;

import com.fantech.novoid.R;
import com.fantech.novoid.services.CoronaServices;
import com.fantech.novoid.utils.AndroidUtil;
import com.fantech.novoid.utils.Constants;

import androidx.annotation.Nullable;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.val;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServiceCalls
{
    //*********************************************************************
    public void loadDeathStats(@NonNull APICallListener apiCallListener)
    //*********************************************************************
    {
        val service = ServiceGenerator.createService(CoronaServices.class, Constants.BASE_URL);
        if (service == null)
        {
            apiCallListener.onAPICallCompleted(false,
                                               AndroidUtil.getString(R.string.no_internet),
                                               null);
            return;
        }
        val deathCasesService = service.getDeathCases();
        deathCasesService.enqueue(new Callback<ResponseBody>()
        {
            //*********************************************************************
            @SneakyThrows
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response)
            //*********************************************************************
            {
                val responseData = response.body();
                if (responseData != null)
                {
                    apiCallListener.onAPICallCompleted(true, null, responseData.string());
                }
                else
                    apiCallListener.onAPICallCompleted(false, response.errorBody()
                                                                      .string(), null);
            }

            //*********************************************************************
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t)
            //*********************************************************************
            {
                apiCallListener.onAPICallCompleted(false, t.getLocalizedMessage(), null);
            }
        });

    }

    //*********************************************************************
    public void loadConfirmedStats(@NonNull APICallListener apiCallListener)
    //*********************************************************************
    {
        val service = ServiceGenerator.createService(CoronaServices.class, Constants.BASE_URL);
        if (service == null)
        {
            apiCallListener.onAPICallCompleted(false,
                                               AndroidUtil.getString(R.string.no_internet),
                                               null);
            return;
        }
        val deathCasesService = service.getConfirmedCases();
        deathCasesService.enqueue(new Callback<ResponseBody>()
        {
            //*********************************************************************
            @SneakyThrows
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response)
            //*********************************************************************
            {
                val responseData = response.body();
                if (responseData != null)
                {
                    apiCallListener.onAPICallCompleted(true, null, responseData.string());
                }
                else
                    apiCallListener.onAPICallCompleted(false, response.errorBody()
                                                                      .string(), null);
            }

            //*********************************************************************
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t)
            //*********************************************************************
            {
                apiCallListener.onAPICallCompleted(false, t.getLocalizedMessage(), null);
            }
        });

    }

    //*********************************************************************
    public void loadRecoveredStats(@NonNull APICallListener apiCallListener)
    //*********************************************************************
    {
        val service = ServiceGenerator.createService(CoronaServices.class, Constants.BASE_URL);
        if (service == null)
        {
            apiCallListener.onAPICallCompleted(false,
                                               AndroidUtil.getString(R.string.no_internet),
                                               null);
            return;
        }
        val deathCasesService = service.getRecoveredCases();
        deathCasesService.enqueue(new Callback<ResponseBody>()
        {
            //*********************************************************************
            @SneakyThrows
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response)
            //*********************************************************************
            {
                val responseData = response.body();
                if (responseData != null)
                {
                    apiCallListener.onAPICallCompleted(true, null, responseData.string());
                }
                else
                    apiCallListener.onAPICallCompleted(false, response.errorBody()
                                                                      .string(), null);
            }

            //*********************************************************************
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t)
            //*********************************************************************
            {
                apiCallListener.onAPICallCompleted(false, t.getLocalizedMessage(), null);
            }
        });

    }


    //**********************************************************
    public interface APICallListener
            //**********************************************************
    {
        void onAPICallCompleted(boolean isSuccessFull, @Nullable String errorMessage, @NonNull String data);
    }

    //*********************************************************************
    public static @NonNull
    ServiceCalls instance()
    //*********************************************************************
    {
        return new ServiceCalls();
    }

}
