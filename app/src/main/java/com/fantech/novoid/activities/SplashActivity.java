package com.fantech.novoid.activities;

import android.content.Intent;
import android.os.Bundle;

import com.fantech.novoid.R;
import com.fantech.novoid.databinding.ActivitySplashBinding;
import com.fantech.novoid.models.Corona;
import com.fantech.novoid.network.ServiceCalls;
import com.fantech.novoid.utils.AndroidUtil;
import com.fantech.novoid.utils.Constants;
import com.fantech.novoid.utils.SharedPreferencesUtils;
import com.fantech.novoid.view_models.CoronaStatsViewModel;

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
    private Calendar mCalender;
    private static final int THREE_API_CALLED = 3;

    //*********************************************************************
    @Override
    protected void onCreation(@Nullable Bundle savedInstanceState)
    //*********************************************************************
    {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_splash);
        initControls();
    }

    //*********************************************************************
    private void checkPreviousData()
    //*********************************************************************
    {
        mStatsViewModel.getRecords()
                       .observe(this, coronas ->
       {
           if (mIsObserved)
               return;
           mIsObserved = true;
           val lastUpdated = SharedPreferencesUtils.getLong(
                   SharedPreferencesUtils.LAST_UPDATED_TIME);
           long diff = System.currentTimeMillis() - lastUpdated;
           int numOfDays = (int)(diff / (1000 * 60 * 60 * 24));
           if (coronas.size() == 0 || lastUpdated == 0 || numOfDays >= 1)
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
        checkPreviousData();
    }


    //*********************************************************************
    private void loadStats()
    //*********************************************************************
    {
        mApiCount = 0;
        mCoronaList = new ArrayList<>();
        mCalender = Calendar.getInstance();
        mStatsViewModel.clearDB();
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
                Corona corona = new Corona(rowValues, columns, mCalender, type, j);
                mCoronaList.add(corona);
            }
        }
        mApiCount++;
        if (mApiCount == THREE_API_CALLED)
        {
            hideLoadingDialog();
            mStatsViewModel.insert(mCoronaList);
            SharedPreferencesUtils.setValue(SharedPreferencesUtils.LAST_UPDATED_TIME,
                                            System.currentTimeMillis());
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
