package com.fantech.novoid.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.fantech.novoid.R;
import com.fantech.novoid.databinding.ActivityHelpBinding;

//***********************************************
public class HelpActivity
        extends BaseActivity
//***********************************************
{

    private ActivityHelpBinding mBinding;

    //***********************************************
    @Override
    protected void onCreation(@Nullable Bundle savedInstanceState)
    //***********************************************
    {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_help);
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
