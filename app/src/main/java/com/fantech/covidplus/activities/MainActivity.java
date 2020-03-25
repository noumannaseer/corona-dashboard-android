package com.fantech.covidplus.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;

import com.fantech.covidplus.R;
import com.fantech.covidplus.databinding.ActivityMainBinding;
import com.fantech.covidplus.models.Corona;
import com.fantech.covidplus.network.ServiceGenerator;
import com.fantech.covidplus.services.CoronaServices;
import com.fantech.covidplus.utils.AndroidUtil;
import com.fantech.covidplus.utils.Constants;
import com.fantech.covidplus.utils.ThemeUtils;
import com.fantech.covidplus.utils.UIUtils;
import com.fantech.covidplus.view_models.CoronaStatsViewModel;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import lombok.SneakyThrows;
import lombok.val;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//*********************************************************************
public class MainActivity
        extends BaseActivity
//*********************************************************************
{
    private CoronaStatsViewModel mStatsViewModel;
    private ActivityMainBinding mBinding;
    private int mApiCount = 0;
    private List<Corona> mCoronaList;
    private boolean mIsObserved = false;

    //*********************************************************************
    @Override
    protected void onCreation(@Nullable Bundle savedInstanceState)
    //*********************************************************************
    {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        initControls();
    }

    //**********************************************************
    private void detectCurrentTheme()
    //**********************************************************
    {
        switch (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK)
        {
        case Configuration.UI_MODE_NIGHT_YES:
            findViewById(R.id.background).setBackgroundColor(AndroidUtil.getColor(R.color.app_background2));
            ThemeUtils.setDarkTheme(true, this, false);
            AndroidUtil.handler.postDelayed(() ->
            {
                mStatsViewModel.getRecords().observe(this, coronas ->
                {
                    if (mIsObserved)
                        return;
                    mIsObserved = true;
                    if (coronas.size() == 0)
                        loadStats();
                    else
                        gotoHomeActivity();
                });
            }, 1000);

            break;
        case Configuration.UI_MODE_NIGHT_NO:
            ThemeUtils.setDarkTheme(false, this, false);
            findViewById(R.id.background).setBackgroundColor(AndroidUtil.getColor(R.color.app_background1));
            AndroidUtil.handler.postDelayed(() ->
            {
                mStatsViewModel.getRecords().observe(this, coronas ->
                {
                    if (mIsObserved)
                        return;
                    mIsObserved = true;
                    if (coronas == null || coronas.size() == 0)
                        loadStats();
                    else
                        gotoHomeActivity();
                });
            }, 1000);
            break;
        }
    }

    //*********************************************************************
    private void gotoHomeActivity()
    //*********************************************************************
    {
        Intent homeIntent = new Intent(this, HomeActivity.class);
        startActivity(homeIntent);
        finish();
    }

    //*********************************************************************
    private void initControls()
    //*********************************************************************
    {
        initViewModel();
        detectCurrentTheme();
    }


    //*********************************************************************
    private void loadStats()
    //*********************************************************************
    {
        mApiCount = 0;
        mCoronaList = new ArrayList<>();
        loadDeathStats();
        loadConfirmedStats();
    }

    /*
    TO-DO
    Remove this service call from here, introduce a singleton class/Interface and call the services using that.
     */
    //*********************************************************************
    private void loadConfirmedStats()
    //*********************************************************************
    {
        val service = ServiceGenerator.createService(CoronaServices.class, Constants.BASE_URL);
        val deathStats = service.getConfirmedCases();
        showLoadingDialog();
        deathStats.enqueue(new Callback<ResponseBody>()
        {
            //*********************************************************************
            @SneakyThrows
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response)
            //*********************************************************************
            {
                val data = response.body().string();
                processData(data, Constants.REPORT_CONFIRMED);
                Log.d("stats", data);
            }

            //*********************************************************************
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t)
            //*********************************************************************
            {
                hideLoadingDialog();
            }
        });

    }

    /*
    TO-DO, remove this method and shift it to a singleton. It should jsut return a callback interface with success/failure response.
    All the data should be parsed.
     */
    //*********************************************************************
    private void loadDeathStats()
    //*********************************************************************
    {
        val service = ServiceGenerator.createService(CoronaServices.class, Constants.BASE_URL);
        val deathCasesService = service.getDeathCases();
        showLoadingDialog();
        deathCasesService.enqueue(new Callback<ResponseBody>()
        {
            //*********************************************************************
            @SneakyThrows
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response)
            //*********************************************************************
            {
                val responseData = response.body().string();
                processData(responseData, Constants.REPORT_DEATH);
                Log.d("stats", responseData);
            }

            //*********************************************************************
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t)
            //*********************************************************************
            {
                hideLoadingDialog();
                //TO-DO, Add the error message
            }
        });

    }


    //*********************************************************************
    private void processData(String data, int type)
    //*********************************************************************
    {
        if (mCoronaList == null)
            mCoronaList = new ArrayList<>();
        val rows = data.split("\\n");
        val columns = rows[0].split(",");
        for (int i = 1; i < rows.length; i++)
        {
            String row = rows[i];
            val rowValues = row.split(",");

            for (int j = 4; j < columns.length; j++)
            {
                //instead of processing it here, pass the rowValues[] array into the model and get a proper object.
                // Corona row = new Corona(rowValues,type); The constructor should process these values.
                Corona corona = new Corona();
                corona.setState(rowValues[0]);
                corona.setCountry(rowValues[1]);
                corona.setLatitude(rowValues[2]);
                corona.setLongitude(rowValues[3]);
                // move the formatter to the constants.
                corona.setDate(UIUtils.getDateFromString(columns[j], "mm/dd/yyyy"));
                corona.setReport_type(type);
                //this can crash in some cases. Please add necessary condition.
                int quantity = Integer.parseInt(rowValues[j].trim());
                corona.setQuantity(quantity);
                mCoronaList.add(corona);
            }

        }
        mApiCount++;
        if (mApiCount == 2)
        {
            hideLoadingDialog();
            mStatsViewModel.insert(mCoronaList);
            gotoHomeActivity();
        }
    }

    //*********************************************************************
    private void initViewModel()
    //*********************************************************************
    {
        mStatsViewModel = ViewModelProviders.of(this)
                                            .get(CoronaStatsViewModel.class);

    }
}
