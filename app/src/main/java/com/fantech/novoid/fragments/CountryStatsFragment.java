package com.fantech.novoid.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blongho.country_data.World;
import com.fantech.novoid.databinding.FragmentCountryStatsBinding;
import com.fantech.novoid.utils.Constants;
import com.fantech.novoid.utils.UIUtils;
import com.fantech.novoid.view_models.CoronaStatsViewModel;

import androidx.lifecycle.ViewModelProviders;

//*********************************************************
public class CountryStatsFragment
        extends BaseFragment
//*********************************************************
{


    private View rootView;
    private FragmentCountryStatsBinding mBinding;
    private String mCountryName;
    private CoronaStatsViewModel mCoronaStatsViewModel;
    private int mTotalDeaths = 0;
    private int mTotalRecovered = 0;
    private int mTotalConfirmed = 0;

    //***********************************************************************
    public CountryStatsFragment(String mCountryName)
    //***********************************************************************
    {
        this.mCountryName = mCountryName;
    }

    //*************************************************************************
    public CountryStatsFragment()
    //*************************************************************************
    {

    }


    //***********************************************************************
    @Override
    public View onCreateViewBaseFragment(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState)
    //***********************************************************************
    {
        if (rootView == null)
        {
            mBinding = FragmentCountryStatsBinding.inflate(inflater, parent, false);
            rootView = mBinding.getRoot();
            super.setFragment(CountryStatsFragment.this);
            initControls();
        }
        return rootView;
    }

    //****************************************************
    private void initControls()
    //****************************************************
    {
        mCoronaStatsViewModel = ViewModelProviders.of(this)
                                                  .get(CoronaStatsViewModel.class);
        mCoronaStatsViewModel.countSum(Constants.REPORT_DEATH, mCountryName)
                             .observe(this,
          integer -> {
              if (integer == null)
                  integer = 0;
              mTotalDeaths = integer;
              mBinding.totalDeaths.setText(
                      UIUtils.getFormattedAmount(mTotalDeaths));

          });
        mCoronaStatsViewModel.countSum(Constants.REPORT_RECOVERED, mCountryName)
                             .observe(this,
          integer -> {
              if (integer == null)
                  integer = 0;
              mTotalRecovered = integer;
              mBinding.totalRecovered.setText(UIUtils.getFormattedAmount(mTotalRecovered));
          });
        mCoronaStatsViewModel.countSum(Constants.REPORT_CONFIRMED, mCountryName)
                             .observe(this,
          integer -> {
              if (integer == null)
                  integer = 0;

              mTotalConfirmed = integer;

              mBinding.totalConfirmed.setText(UIUtils.getFormattedAmount(
                      mTotalConfirmed));
          });

        World.init(getContext());
        final int flag = World.getFlagOf(mCountryName.toLowerCase());
        mBinding.flag.setImageResource(flag);


    }



}
