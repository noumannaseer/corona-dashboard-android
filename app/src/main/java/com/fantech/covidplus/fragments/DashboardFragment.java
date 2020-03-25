package com.fantech.covidplus.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fantech.covidplus.R;
import com.fantech.covidplus.databinding.FragmentDashboardBinding;
import com.fantech.covidplus.utils.Constants;
import com.fantech.covidplus.view_models.CoronaStatsViewModel;
import com.google.android.material.tabs.TabLayout;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

//********************************************
public class DashboardFragment extends BaseFragment
//********************************************
{
    private View rootView;
    private FragmentDashboardBinding mBinding;
    private CoronaStatsViewModel mCoronaStatsViewModel;

    //***********************************************************************
    @Override
    public View onCreateViewBaseFragment(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState)
    //***********************************************************************
    {
        if (rootView == null)
        {
            mBinding = FragmentDashboardBinding.inflate(inflater, parent, false);
            rootView = mBinding.getRoot();
            super.setFragment(DashboardFragment.this);
            initControls();
        }
        return rootView;
    }

    //**********************************************
    private void initControls()
    //**********************************************
    {
        mCoronaStatsViewModel = ViewModelProviders.of(this).get(CoronaStatsViewModel.class);
        addTabs();

        mCoronaStatsViewModel.countSum(Constants.REPORT_DEATH).observe(this, new Observer<Integer>()
        {
            //**********************************************
            @Override
            public void onChanged(Integer integer)
            //**********************************************
            {
                mBinding.totalDeaths.setText(String.valueOf(integer));
            }
        });

        mCoronaStatsViewModel.countSum(Constants.REPORT_CONFIRMED).observe(this, new Observer<Integer>()
        {
            //**********************************************
            @Override
            public void onChanged(Integer integer)
            //**********************************************
            {
                mBinding.totalConfirmed.setText(String.valueOf(integer));
            }
        });

    }

    //**********************************************
    private void addTabs()
    //**********************************************
    {

        mBinding.tabLayout.addTab(mBinding.tabLayout.newTab()
                                                    .setText(
                                                            getString(R.string.actual)));
        mBinding.tabLayout.addTab(mBinding.tabLayout.newTab()
                                                    .setText(getString(R.string.logrithmic)));
        mBinding.tabLayout.addTab(mBinding.tabLayout.newTab()
                                                    .setText(getString(R.string.daily_cases)));

        mBinding.tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
    }

}
