package com.fantech.novoid.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.blongho.country_data.World;
import com.fantech.novoid.R;
import com.fantech.novoid.adapters.CustomFragmentPageAdapter;
import com.fantech.novoid.databinding.ActivityCountryStatsBinding;
import com.fantech.novoid.utils.AndroidUtil;
import com.fantech.novoid.utils.Constants;
import com.fantech.novoid.utils.ThemeUtils;
import com.fantech.novoid.view_models.CoronaStatsViewModel;
import com.google.android.material.tabs.TabLayout;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

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

    //******************************************************************
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    //******************************************************************
    {
        switch (item.getItemId())
        {
        case R.id.action_share:
            captureAndShareScreenShot();
            break;
        }
        return super.onOptionsItemSelected(item);
    }

    //******************************************************************
    private void captureAndShareScreenShot()
    //******************************************************************
    {
        if(getPermission())
        takeScreenshot();

    }

    //************************************************************************
    private boolean getPermission()
    //************************************************************************
    {
        if (ActivityCompat.checkSelfPermission(this,
                                                      Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)

        {
            ActivityCompat.requestPermissions(this,
                                              new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE },
                                              1);
            return false;
        }
        else
        {
            return true;
        }

    }

    //**********************************************************
    private void takeScreenshot()
    //**********************************************************
    {
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);
        try {
            String mPath = Environment.getExternalStorageDirectory().toString() + "/" + now + ".jpeg";
            View v1 = getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);
            File imageFile = new File(mPath);
            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();
            Bitmap screenShotBitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
            share(screenShotBitmap);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
    //**********************************************************
    private void share(Bitmap bitmap)
    //**********************************************************
    {
        String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, Constants.IMAGE_DESCRIPTION, null);
        Uri uri = Uri.parse(path);
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/jpeg");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(Intent.createChooser(intent, AndroidUtil.getString(R.string.share_stats_with_friend)));

    }


    //******************************************************************
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    //******************************************************************
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_share, menu);
        Drawable drawable = menu.findItem(R.id.action_share)
                                .getIcon();
        drawable = DrawableCompat.wrap(drawable);
        if (ThemeUtils.getCurrentThemeIsDark())
            DrawableCompat.setTint(drawable, AndroidUtil.getColor(android.R.color.white));
        else
            DrawableCompat.setTint(drawable, AndroidUtil.getColor(android.R.color.background_dark));

        menu.findItem(R.id.action_share)
            .setIcon(drawable);
        return true;
    }
}
