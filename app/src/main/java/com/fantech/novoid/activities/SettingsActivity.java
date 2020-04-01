package com.fantech.novoid.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;

import com.fantech.novoid.R;
import com.fantech.novoid.databinding.ActivitySettingsBinding;
import com.fantech.novoid.utils.ThemeUtils;

//******************************************************
public class SettingsActivity
        extends BaseActivity
//******************************************************
{
    private ActivitySettingsBinding mBinding;

    //******************************************************
    @Override
    protected void onCreation(@Nullable Bundle savedInstanceState)
    //******************************************************
    {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_settings);
        initControls();

    }

    //***********************************************************************
    private void initControls()
    //***********************************************************************
    {
        setSupportActionBar(mBinding.toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mBinding.darkModeSwitch.setChecked(ThemeUtils.getCurrentThemeIsDark());
        mBinding.darkModeSwitch.setOnClickListener(view -> {
            switchTheme();
        });
        mBinding.about.setOnClickListener(view -> gotoAboutActivity());

    }

    //***********************************************************************
    private void gotoAboutActivity()
    //***********************************************************************
    {
        Intent aboutIntent = new Intent(this, AboutActivity.class);
        startActivity(aboutIntent);
    }

    //***********************************************************************
    private void switchTheme()
    //***********************************************************************
    {
        ThemeUtils.setDarkTheme(mBinding.darkModeSwitch.isChecked(), this, true);
    }
}
