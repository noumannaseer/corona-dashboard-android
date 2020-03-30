package com.fantech.novoid.activities;

import android.os.Bundle;

import com.fantech.novoid.R;
import com.fantech.novoid.databinding.ActivityHomeBinding;
import com.fantech.novoid.fragments.BlogFragment;
import com.fantech.novoid.fragments.CountriesFragment;
import com.fantech.novoid.fragments.DashboardFragment;
import com.fantech.novoid.fragments.MapViewFragment;
import com.fantech.novoid.fragments.SettingsFragment;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

//**************************************************
public class HomeActivity
        extends BaseActivity
//**************************************************
{
    private ActivityHomeBinding mBinding;
    private DashboardFragment mDashboardFragment;
    private CountriesFragment mCountriesFragment;
    private MapViewFragment mMapViewFragment;
    private BlogFragment mBlogFragment;
    private SettingsFragment mSettingsFragment;

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
        mBlogFragment = new BlogFragment();
        mSettingsFragment = new SettingsFragment();
        mBinding.bottomNavigation.setOnNavigationItemSelectedListener(item ->
          {
              switch (item.getItemId())
              {
              case R.id.dashboard:
                  loadFragment(
                          mDashboardFragment);
                  break;
              case R.id.countries:
                  loadFragment(
                          mCountriesFragment);
                  break;
              case R.id.map_view:
                  loadFragment(
                          new MapViewFragment());
                  break;
              case R.id.stay_safe:
                  loadFragment(
                          mBlogFragment);
                  break;
              case R.id.settings:
                  loadFragment(
                          mSettingsFragment);
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
