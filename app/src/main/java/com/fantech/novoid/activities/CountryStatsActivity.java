package com.fantech.novoid.activities;

import android.os.Bundle;
import android.text.TextUtils;

import com.blongho.country_data.World;
import com.fantech.novoid.R;
import com.fantech.novoid.adapters.CustomFragmentPageAdapter;
import com.fantech.novoid.databinding.ActivityCountryStatsBinding;
import com.fantech.novoid.utils.Constants;
import com.fantech.novoid.utils.UIUtils;
import com.fantech.novoid.view_models.CoronaStatsViewModel;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import lombok.val;

//*******************************************************
public class CountryStatsActivity
        extends BaseActivity
//*******************************************************
{

    private ActivityCountryStatsBinding mBinding;
    public static final String COUNTRY_NAME = "COUNTRY_NAME";
    private String mCountryName;
    private CoronaStatsViewModel mCoronaStatsViewModel;

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
        mCoronaStatsViewModel = ViewModelProviders.of(this)
                                                  .get(CoronaStatsViewModel.class);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getParcelable();
        addTabs();
        val identifierName = Constants.FLAG_PREFIX + mCountryName
                .toLowerCase()
                .replace(" ", "_");
        UIUtils.setDrawable(identifierName,mBinding.flag,this);
        World.init(this);
        final int flag = World.getFlagOf(mCountryName.toLowerCase());
        mBinding.flag.setImageResource(flag);
        mBinding.countryName.setText(mCountryName);
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
        mCoronaStatsViewModel.getProvinceList(mCountryName)
         .observe(this,
                  coronas -> {
                      mBinding.tabLayout.addTab(mBinding.tabLayout.newTab()
                                                                  .setText(
                                                                          getString(
                                                                                  R.string.total)));
                      int pageCount = 1;
                      if (coronas.size() > 0 && !TextUtils.isEmpty(
                              coronas.get(0)))
                      {
                          mBinding.tabLayout.addTab(mBinding.tabLayout.newTab()
                                                                      .setText(
                                                                              getString(
                                                                                      R.string.provincial)));
                          pageCount = 2;
                      }
                      attachViewPageAdaptor(pageCount);

                      mBinding.tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

                  });

    }

    //********************************************************
    private void attachViewPageAdaptor(int pageCount)
    //********************************************************
    {
        CustomFragmentPageAdapter mCustomFragmentPageAdapter = new CustomFragmentPageAdapter(
                getSupportFragmentManager(), mCountryName, pageCount);
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
