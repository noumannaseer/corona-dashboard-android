package com.fantech.novoid.activities;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.fantech.novoid.R;
import com.fantech.novoid.databinding.ActivityFAQBinding;

//**********************************************
public class FAQActivity
        extends BaseActivity
//**********************************************
{


    private ActivityFAQBinding mBinding;


    //**********************************************
    @Override
    protected void onCreation(@Nullable Bundle savedInstanceState)
    //**********************************************
    {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_f_a_q);
        initControls();

    }

    //**********************************************
    private void initControls()
    //**********************************************
    {
        setSupportActionBar(mBinding.toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }
}
