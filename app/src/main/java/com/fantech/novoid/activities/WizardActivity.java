package com.fantech.novoid.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import android.os.Bundle;

import com.fantech.novoid.R;
import com.fantech.novoid.adapters.WizardPagerAdapter;
import com.fantech.novoid.databinding.ActivityWizardBinding;

public class WizardActivity
        extends AppCompatActivity
{

    private ActivityWizardBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_wizard);
        initControls();
    }

    //****************************************************************
    private void initControls()
    //****************************************************************
    {
        initViewPager();
        initCircleIndicator();
    }

    //****************************************************************
    private void initCircleIndicator()
    //****************************************************************
    {
        mBinding.circleIndicator.setViewPager(mBinding.viewPager);
    }

    //****************************************************************
    private void initViewPager()
    //****************************************************************
    {

        FragmentManager fm = getSupportFragmentManager();
        WizardPagerAdapter pagerAdapter = new WizardPagerAdapter(fm,
                                                                 FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mBinding.viewPager.setAdapter(pagerAdapter);
        mBinding.viewPager.setCurrentItem(0);

    }

}
