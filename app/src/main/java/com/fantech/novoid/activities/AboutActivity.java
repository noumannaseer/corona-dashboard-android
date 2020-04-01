package com.fantech.novoid.activities;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.fantech.novoid.R;
import com.fantech.novoid.databinding.ActivityAboutBinding;

//*********************************************
public class AboutActivity
        extends BaseActivity
//*********************************************
{

    private ActivityAboutBinding mBinding;

    //*********************************************
    @Override
    protected void onCreation(@Nullable Bundle savedInstanceState)
    //*********************************************
    {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_about);
        initControls();
    }

    //******************************************
    private void initControls()
    //******************************************
    {
        setSupportActionBar(mBinding.toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

    }
}
