package com.fantech.novoid.activities;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.fantech.novoid.R;
import com.fantech.novoid.databinding.ActivityHomeBinding;
import com.fantech.novoid.fragments.BlogFragment;
import com.fantech.novoid.fragments.CountriesFragment;
import com.fantech.novoid.fragments.DashboardFragment;
import com.fantech.novoid.fragments.GuideLinesFragment;
import com.fantech.novoid.fragments.MapViewFragment;
import com.fantech.novoid.fragments.SettingsFragment;
import com.fantech.novoid.models.CountryReader;
import com.fantech.novoid.utils.AndroidUtil;
import com.fantech.novoid.utils.Constants;
import com.fantech.novoid.utils.ThemeUtils;
import com.fantech.novoid.view_models.CoronaStatsViewModel;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

//**************************************************
public class HomeActivity
        extends BaseActivity
//**************************************************
{
    private ActivityHomeBinding mBinding;
    private DashboardFragment mDashboardFragment;
    private CountriesFragment mCountriesFragment;
    private MapViewFragment mMapViewFragment;
    private GuideLinesFragment mGuideLinesFragment;
    private SettingsFragment mSettingsFragment;
    private boolean mIsDarkTheme;
    private CoronaStatsViewModel mCoronaStatsViewModel;

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
                          mGuideLinesFragment);
                  break;
              default:
                  break;
              }
              return true;
          });
        loadFragment(mDashboardFragment);
       sendNotification(CountryReader.getUserCountry(this));

    }

    //******************************************************
    private void sendNotification(String countryName)
    //******************************************************
    {

        if(TextUtils.isEmpty(countryName))
            return;
        mCoronaStatsViewModel = ViewModelProviders.of(this)
                                                  .get(CoronaStatsViewModel.class);
        mCoronaStatsViewModel.countSum(Constants.REPORT_DEATH,countryName)
         .observe(this,
                  deaths -> {
                     mCoronaStatsViewModel.countSum(Constants.REPORT_CONFIRMED,countryName)
                          .observe(this, confirmed -> {
                              mCoronaStatsViewModel.countSum(Constants.REPORT_RECOVERED,countryName)
                                                   .observe(this, recovered -> {
                                                       Intent countryDetailIntent=new Intent(this,CountryStatsActivity.class);
                                                       countryDetailIntent.putExtra(CountryStatsActivity.COUNTRY_NAME,countryName);
                                                       String stats=AndroidUtil.getString(R.string.stats_notification,deaths,confirmed,recovered);
                                                       createNotification(this, AndroidUtil.getString(R.string.today_stats), stats, countryDetailIntent);
                                                   });
                          });
                  });

    }

    //*****************************************************************************************
    private void createNotification(Context context, String title, String body, Intent intent)
    //*****************************************************************************************
    {

        NotificationManager notificationManager = (NotificationManager)context.getSystemService(
                Context.NOTIFICATION_SERVICE);

        int notificationId = 1;
        String channelId = "channel-01sas";
        String channelName = "Channel Namesasasasasasa";
        int importance = NotificationManager.IMPORTANCE_HIGH;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
        {
            NotificationChannel mChannel = new NotificationChannel(
                    channelId, channelName, importance);
            notificationManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.mipmap.novoi_icon)
                .setContentTitle(title)
                .setContentText(body);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntentWithParentStack(intent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
                0,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
        mBuilder.setContentIntent(resultPendingIntent);

        mBuilder.setAutoCancel(true);
        notificationManager.notify(notificationId, mBuilder.build());
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
