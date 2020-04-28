package com.fantech.novoid.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.fantech.novoid.R;
import com.fantech.novoid.databinding.ActivityHomeBinding;
import com.fantech.novoid.fragments.CountriesFragment;
import com.fantech.novoid.fragments.DashboardFragment;
import com.fantech.novoid.fragments.GuideLinesFragment;
import com.fantech.novoid.fragments.SettingsFragment;
import com.fantech.novoid.utils.AndroidUtil;
import com.fantech.novoid.utils.ThemeUtils;
import com.fantech.novoid.utils.UIUtils;

import androidx.annotation.Nullable;
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
    private GuideLinesFragment mGuideLinesFragment;
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
        mGuideLinesFragment = new GuideLinesFragment();
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
                                                                          case R.id.stay_safe:
                                                                              mBinding.fragmentName.setText(
                                                                                      AndroidUtil.getString(
                                                                                              R.string.guide_lines));
                                                                              loadFragment(
                                                                                      mGuideLinesFragment);
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
            killApp();
            break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void killApp()
    {
        UIUtils.displayAlertDialog(AndroidUtil.getString(R.string.do_you_really_clode),
                                   AndroidUtil.getString(R.string.close_app), this,
                                   AndroidUtil.getString(R.string.yes),
                                   AndroidUtil.getString(R.string.no)
                , new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        if (which == -1)
                        {
                            finish();
                            System.exit(0);
                        }
                    }
                });
    }

}
