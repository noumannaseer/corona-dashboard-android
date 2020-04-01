package com.fantech.novoid.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.fantech.novoid.repository.CoronaStatsRepository;
import com.fantech.novoid.R;
import com.fantech.novoid.databinding.ActivitySplashBinding;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

//*********************************************************************
public class SplashActivity
        extends BaseActivity
//*********************************************************************
{
    private ActivitySplashBinding mBinding;


    //*********************************************************************
    @Override
    protected void onCreation(@Nullable Bundle savedInstanceState)
    //*********************************************************************
    {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_splash);
        initControls();
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
        CoronaStatsRepository.getInstance(this,this, () -> gotoHomeActivity())
                             .checkPreviousData();
    }

}
