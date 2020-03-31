package com.fantech.novoid.activities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.crashlytics.android.Crashlytics;
import com.fantech.novoid.R;
import com.fantech.novoid.databinding.ActivityHomeBinding;
import com.fantech.novoid.fragments.BlogFragment;
import com.fantech.novoid.fragments.CountriesFragment;
import com.fantech.novoid.fragments.DashboardFragment;
import com.fantech.novoid.fragments.MapViewFragment;
import com.fantech.novoid.fragments.SettingsFragment;
import com.fantech.novoid.utils.AndroidUtil;
import com.fantech.novoid.utils.ThemeUtils;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
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
    private boolean mIsDarkTheme;

    //**************************************************
    @Override
    protected void onCreation(@Nullable Bundle savedInstanceState)
    //**************************************************
    {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        initControls();
        mIsDarkTheme = ThemeUtils.getCurrentThemeIsDark();
    }

    //***********************************************
    @Override
    protected void onResume()
    //***********************************************
    {
        super.onResume();
        if (mIsDarkTheme != ThemeUtils.getCurrentThemeIsDark())
        {
            mIsDarkTheme = ThemeUtils.getCurrentThemeIsDark();
            restartActivity();
        }
    }

    //**************************************************
    private void initControls()
    //**************************************************
    {
        setSupportActionBar(mBinding.toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
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
                  mBinding.fragmentName.setText(
                          AndroidUtil.getString(
                                  R.string.dashboard));
                  loadFragment(
                          mDashboardFragment);
                  break;
              case R.id.countries:
                  mBinding.fragmentName.setText(
                          AndroidUtil.getString(
                                  R.string.countries));
                  loadFragment(
                          mCountriesFragment);
                  break;
              case R.id.map_view:
                  mBinding.fragmentName.setText(
                          AndroidUtil.getString(
                                  R.string.covid_global_view));
                  loadFragment(
                          new MapViewFragment());
                  break;
              case R.id.stay_safe:
                  mBinding.fragmentName.setText(
                          AndroidUtil.getString(
                                  R.string.guide_lines));
                  loadFragment(
                          mBlogFragment);
                  break;
              default:
                  break;
              }
              return true;
          });
        loadFragment(mDashboardFragment);
    }

    //******************************************************************
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    //******************************************************************
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings_menu, menu);
        Drawable drawable = menu.findItem(R.id.action_settings)
                                .getIcon();
        drawable = DrawableCompat.wrap(drawable);
        if (ThemeUtils.getCurrentThemeIsDark())
            DrawableCompat.setTint(drawable, AndroidUtil.getColor(android.R.color.white));
        else
            DrawableCompat.setTint(drawable, AndroidUtil.getColor(android.R.color.background_dark));

        menu.findItem(R.id.action_settings)
            .setIcon(drawable);

        return true;
    }

    //**********************************************
    private void loadFragment(Fragment fragment)
    //**********************************************
    {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.commit();
    }

    //******************************************************************
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    //******************************************************************
    {
        switch (item.getItemId())
        {
        case R.id.action_settings:
            gotoSettingActivity();
            break;
        }
        return super.onOptionsItemSelected(item);
    }

    //******************************************************************
    private void gotoSettingActivity()
    //******************************************************************
    {
        Intent settingsIntent = new Intent(this, SettingsActivity.class);
        startActivity(settingsIntent);
    }
}
