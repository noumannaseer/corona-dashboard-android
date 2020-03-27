package com.fantech.covidplus.activities;

import android.os.Bundle;

import com.fantech.covidplus.R;
import com.fantech.covidplus.adapters.CustomFragmentPageAdapter;
import com.fantech.covidplus.databinding.ActivityCountryStatsBinding;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;

//*******************************************************
public class CountryStatsActivity
        extends BaseActivity
//*******************************************************
{

    private ActivityCountryStatsBinding mBinding;
    public static final String COUNTRY_NAME = "COUNTRY_NAME";
    private String mCountryName;

    //*******************************************************
    @Override
    protected void onCreation(@Nullable Bundle savedInstanceState)
    //*******************************************************
    {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_country_stats);
        initControls();
    }

    //*******************************************************
    private void initControls()
    //*******************************************************
    {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        addTabs();
        getParcelable();
        mBinding.countryName.setText(mCountryName);
        attachViewPageAdaptor();
    }

    //*************************************************************
    private void getParcelable()
    //*************************************************************
    {
        if (getIntent() == null || getIntent().getExtras() == null)
            return;
        if (getIntent().getExtras()
                       .containsKey(COUNTRY_NAME))
        {
            mCountryName = getIntent().getStringExtra(COUNTRY_NAME);
        }

    }

    //**********************************************
    private void addTabs()
    //**********************************************
    {
        mBinding.tabLayout.addTab(mBinding.tabLayout.newTab()
                                                    .setText(
                                                            getString(R.string.total)));
        mBinding.tabLayout.addTab(mBinding.tabLayout.newTab()
                                                    .setText(getString(R.string.provincial)));
        mBinding.tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
    }

    //********************************************************
    private void attachViewPageAdaptor()
    //********************************************************
    {
        CustomFragmentPageAdapter mCustomFragmentPageAdapter = new CustomFragmentPageAdapter(
                getSupportFragmentManager(), mCountryName);
        mBinding.viewPager.setOffscreenPageLimit(0);
        mBinding.viewPager.setAdapter(mCustomFragmentPageAdapter);
        mBinding.viewPager.setCurrentItem(0);
        mBinding.viewPager.addOnPageChangeListener(
                new TabLayout.TabLayoutOnPageChangeListener(mBinding.tabLayout));
        mBinding.tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener()
        {
            @Override
            public void onTabSelected(TabLayout.Tab tab)
            {
                mBinding.viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab)
            {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab)
            {
            }
        });

    }
}
