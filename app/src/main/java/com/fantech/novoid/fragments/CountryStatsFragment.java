package com.fantech.novoid.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fantech.novoid.R;
import com.fantech.novoid.databinding.FragmentCountryStatsBinding;
import com.fantech.novoid.utils.AndroidUtil;
import com.fantech.novoid.utils.Constants;
import com.fantech.novoid.utils.ThemeUtils;
import com.fantech.novoid.utils.UIUtils;
import com.fantech.novoid.view_models.CoronaStatsViewModel;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.ViewModelProviders;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lombok.val;

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

    public CountryStatsFragment(String mCountryName)
    {
        this.mCountryName = mCountryName;
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
              setPieChartValues(mTotalDeaths,
                                mTotalRecovered,
                                mTotalConfirmed);
              mBinding.totalDeaths.setText(
                      UIUtils.getFormattedAmount(mTotalDeaths));

          });
        mCoronaStatsViewModel.countSum(Constants.REPORT_RECOVERED, mCountryName)
                             .observe(this,
          integer -> {
              if (integer == null)
                  integer = 0;
              mTotalRecovered = integer;
              setPieChartValues(mTotalDeaths,
                                mTotalRecovered,
                                mTotalConfirmed);
              mBinding.totalRecovered.setText(UIUtils.getFormattedAmount(mTotalRecovered));
          });
        mCoronaStatsViewModel.countSum(Constants.REPORT_CONFIRMED, mCountryName)
                             .observe(this,
          integer -> {
              if (integer == null)
                  integer = 0;

              mTotalConfirmed = integer;
              setPieChartValues(mTotalDeaths,
                                mTotalRecovered,
                                mTotalConfirmed);
              mBinding.totalConfirmed.setText(UIUtils.getFormattedAmount(
                      mTotalConfirmed));
          });

    }

    //************************************************************
    private void setPieChartValues(Integer totalDeath, Integer totalRecovered, Integer totalConfirmed)
    //************************************************************
    {
        if (totalDeath == 0 && totalRecovered == 0 && totalConfirmed == 0)
        {
            return;
        }
        List< SliceValue > pieData = new ArrayList<>();
        pieData.add(new SliceValue(totalDeath,AndroidUtil.getColor(R.color.red_color_background1 )));
        pieData.add(new SliceValue(totalRecovered, AndroidUtil.getColor(R.color.yellow_color_background1)));
        pieData.add(new SliceValue(totalConfirmed, AndroidUtil.getColor(R.color.graph_color)));

        PieChartData pieChartData = new PieChartData(pieData);
        mBinding.chart.setPieChartData(pieChartData);
    }

}
