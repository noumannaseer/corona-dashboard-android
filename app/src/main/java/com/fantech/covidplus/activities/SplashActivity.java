package com.fantech.covidplus.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;

import com.fantech.covidplus.R;
import com.fantech.covidplus.databinding.ActivitySplashBinding;
import com.fantech.covidplus.models.Corona;
import com.fantech.covidplus.network.ServiceCalls;
import com.fantech.covidplus.utils.AndroidUtil;
import com.fantech.covidplus.utils.Constants;
import com.fantech.covidplus.utils.ThemeUtils;
import com.fantech.covidplus.view_models.CoronaStatsViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import lombok.val;

//*********************************************************************
public class SplashActivity
        extends BaseActivity
//*********************************************************************
{
    private CoronaStatsViewModel mStatsViewModel;
    private ActivitySplashBinding mBinding;
    private int mApiCount = 0;
    private List<Corona> mCoronaList;
    private boolean mIsObserved = false;
    Calendar mCalender;

    //*********************************************************************
    @Override
    protected void onCreation(@Nullable Bundle savedInstanceState)
    //*********************************************************************
    {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_splash);
        initControls();
    }

    //**********************************************************
    private void detectCurrentTheme()
    //**********************************************************
    {
        switch (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK)
        {
        case Configuration.UI_MODE_NIGHT_YES:
            findViewById(R.id.background).setBackgroundColor(
                    AndroidUtil.getColor(R.color.app_background2));
            ThemeUtils.setDarkTheme(true, this, false);
            onThemeSet();

            break;
        case Configuration.UI_MODE_NIGHT_NO:
            ThemeUtils.setDarkTheme(false, this, false);
            findViewById(R.id.background).setBackgroundColor(
                    AndroidUtil.getColor(R.color.app_background1));
            onThemeSet();
            break;
        }
    }

    //*********************************************************************
    private void onThemeSet()
    //*********************************************************************
    {
        mStatsViewModel.getRecords()
                       .observe(this, coronas ->
                       {
                           if (mIsObserved)
                               return;
                           mIsObserved = true;
                           if (coronas.size() == 0)
                               loadStats();
                           else
                           {
                               AndroidUtil.handler.postDelayed(() -> {
                                   gotoHomeActivity();
                               }, 100);
                           }
                       });

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
        mCalender = Calendar.getInstance();
        ServiceCalls.instance()
                    .loadConfirmedStats((isSuccessFull, errorMessage, data) ->
                                        {
                                            if (isSuccessFull)
                                            {
                                                processData(data, Constants.REPORT_CONFIRMED);
                                                return;
                                            }
                                            AndroidUtil.toast(false, errorMessage);
                                            hideLoadingDialog();
                                        });

        ServiceCalls.instance()
                    .loadDeathStats((isSuccessFull, errorMessage, data) ->
                                    {
                                        if (isSuccessFull)
                                        {
                                            processData(data, Constants.REPORT_DEATH);
                                            return;
                                        }
                                        AndroidUtil.toast(false, errorMessage);
                                        hideLoadingDialog();
                                    });
        ServiceCalls.instance()
                    .loadRecoveredStats((isSuccessFull, errorMessage, data) ->
                                        {
                                            if (isSuccessFull)
                                            {
                                                processData(data, Constants.REPORT_RECOVERED);
                                                return;
                                            }
                                            AndroidUtil.toast(false, errorMessage);
                                            hideLoadingDialog();
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
            row = row.replace(", ", " ");
            val rowValues = row.split(",");
            for (int j = 4; j < columns.length; j++)
            {
                Corona corona = new Corona();
                corona.setState(rowValues[0]);
                corona.setCountry(rowValues[1]);
                corona.setLatitude(rowValues[2]);
                corona.setLongitude(rowValues[3]);
                val date = columns[j].split("/");
                mCalender.set(Calendar.MONTH, (Integer.parseInt(date[0]) - 1));
                mCalender.set(Calendar.DAY_OF_MONTH, Integer.parseInt(date[1]));
                mCalender.set(Calendar.YEAR, (2000 + Integer.parseInt(date[2])));
                corona.setDate(mCalender.getTime());
                corona.setReport_type(type);
                int quantity = Integer.parseInt(
                        TextUtils.isEmpty(rowValues[j].trim()) ? "0" : rowValues[j].trim());
                corona.setQuantity(quantity);
                mCoronaList.add(corona);
            }

        }
        mApiCount++;
        if (mApiCount == 3)
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
