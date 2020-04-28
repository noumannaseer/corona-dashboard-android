package com.fantech.novoid.activities;

import android.content.Intent;
import android.os.Bundle;

import com.fantech.novoid.R;
import com.fantech.novoid.databinding.ActivitySplashBinding;
import com.fantech.novoid.utils.AndroidUtil;
import com.fantech.novoid.utils.SharedPreferencesUtils;


import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import lombok.val;

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

        AndroidUtil.handler.postDelayed(() -> {

            val updatedTime = SharedPreferencesUtils.getLong(
                    SharedPreferencesUtils.LAST_UPDATED_TIME);
            gotoHomeActivity();
        }, 1000);
    }


    //*********************************************************************
    private void gotoHomeActivity()
    //*********************************************************************
    {

        Intent homeIntent = new Intent(this, HomeActivity.class);
        startActivity(homeIntent);
        finish();
    }

}
