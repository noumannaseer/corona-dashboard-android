package com.fantech.covidplus.activities;

import android.os.Bundle;

import com.fantech.covidplus.R;
import com.fantech.covidplus.databinding.ActivityHomeBinding;
import com.fantech.covidplus.fragments.CountriesFragment;
import com.fantech.covidplus.fragments.DashboardFragment;
import com.fantech.covidplus.fragments.MapViewFragment;
import com.fantech.covidplus.fragments.StaySafeFragment;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

//**************************************************
public class HomeActivity extends BaseActivity
//**************************************************
{
    private ActivityHomeBinding mBinding;
    private DashboardFragment mDashboardFragment;
    private CountriesFragment mCountriesFragment;
    private MapViewFragment mMapViewFragment;
    private StaySafeFragment mStaySafeFragment;

    //**************************************************
    @Override
    protected void onCreation(@Nullable Bundle savedInstanceState)
    //**************************************************
    {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        initControls();
    }

    //**************************************************
    private void initControls()
    //**************************************************
    {
        mDashboardFragment = new DashboardFragment();
        mCountriesFragment = new CountriesFragment();
        mMapViewFragment = new MapViewFragment();
        mStaySafeFragment = new StaySafeFragment();
        mBinding.bottomNavigation.setOnNavigationItemSelectedListener(item ->
        {
            switch (item.getItemId())
            {
            case R.id.dashboard:
                loadFragment(mDashboardFragment);
                break;
            case R.id.countries:
                loadFragment(mCountriesFragment);
                break;
            case R.id.map_view:
                loadFragment(mMapViewFragment);
                break;
            case R.id.stay_safe:
                loadFragment(mStaySafeFragment);
                break;
            default:
                break;
            }
            return true;
        });
        loadFragment(mDashboardFragment);
    }

    //**********************************************
    private void loadFragment(Fragment fragment)
    //**********************************************
    {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.commit();
    }

}
