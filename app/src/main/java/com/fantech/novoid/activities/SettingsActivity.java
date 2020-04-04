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
        mBinding.help.setOnClickListener(view -> gotoHelpActivity());
        mBinding.faqs.setOnClickListener(view -> gotoFAQActivity());

    }

    //***********************************************************************
    private void gotoFAQActivity()
    //***********************************************************************
    {

        Intent faqIntent = new Intent(this, FAQActivity.class);
        startActivity(faqIntent);
    }

    //***********************************************************************
    private void gotoHelpActivity()
    //***********************************************************************
    {
        Intent helpIntent = new Intent(this, HelpActivity.class);
        startActivity(helpIntent);
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
