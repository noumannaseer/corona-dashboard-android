package com.fantech.covidplus.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fantech.covidplus.R;
import com.fantech.covidplus.databinding.FragmentCountryStatsBinding;
import com.fantech.covidplus.utils.AndroidUtil;
import com.fantech.covidplus.utils.Constants;
import com.fantech.covidplus.utils.ThemeUtils;
import com.fantech.covidplus.view_models.CoronaStatsViewModel;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.interfaces.datasets.IPieDataSet;

import java.util.ArrayList;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
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

    private void initControls()
    {
        mCoronaStatsViewModel = ViewModelProviders.of(this)
                                                  .get(CoronaStatsViewModel.class);
        mCoronaStatsViewModel.countSum(Constants.REPORT_DEATH, mCountryName)
                             .observe(this,
                                      new Observer<Integer>()
                                      {
                                          @Override
                                          public void onChanged(Integer integer)
                                          {
                                              if (integer == null)
                                                  integer = 0;
                                              mTotalDeaths = integer;
                                              setPieChartValues(mTotalDeaths,
                                                                mTotalRecovered,
                                                                mTotalConfirmed);
                                              mBinding.totalDeaths.setText(
                                                      String.valueOf(mTotalDeaths));

                                          }
                                      });
        mCoronaStatsViewModel.countSum(Constants.REPORT_RECOVERED, mCountryName)
                             .observe(this,
                                      new Observer<Integer>()
                                      {
                                          @Override
                                          public void onChanged(Integer integer)
                                          {
                                              if (integer == null)
                                                  integer = 0;
                                              mTotalRecovered = integer;
                                              setPieChartValues(mTotalDeaths,
                                                                mTotalRecovered,
                                                                mTotalConfirmed);
                                              mBinding.totalRecovered.setText(
                                                      String.valueOf(mTotalRecovered));

                                          }
                                      });
        mCoronaStatsViewModel.countSum(Constants.REPORT_CONFIRMED, mCountryName)
                             .observe(this,
                                      new Observer<Integer>()
                                      {
                                          @Override
                                          public void onChanged(Integer integer)
                                          {
                                              if (integer == null)
                                                  integer = 0;

                                              mTotalConfirmed = integer;
                                              setPieChartValues(mTotalDeaths,
                                                                mTotalRecovered,
                                                                mTotalConfirmed);
                                              mBinding.totalConfirmed.setText(String.valueOf(
                                                      mTotalConfirmed));

                                          }
                                      });

    }

    //************************************************************
    private void setPieChartValues(Integer totalDeath, Integer totalRecovered, Integer totalConfirmed)
    //************************************************************
    {
        ArrayList NoOfEmp = new ArrayList();

        if (totalDeath == 0 && totalRecovered == 0 && totalConfirmed == 0)
        {
            return;
        }

        NoOfEmp.add(new PieEntry(totalDeath, AndroidUtil.getString(R.string.total_death)));
        NoOfEmp.add(new PieEntry(totalRecovered, AndroidUtil.getString(R.string.total_recorved)));
        NoOfEmp.add(new PieEntry(totalConfirmed, AndroidUtil.getString(R.string.total_confirmed)));
        PieDataSet dataSet = new PieDataSet(NoOfEmp, "");
        dataSet.setDrawValues(false);
        PieData data = new PieData();
        data.setDataSet(new PieDataSet(NoOfEmp, ""));
        dataSet.setValueLinePart1OffsetPercentage(90.f);
        dataSet.setValueLinePart1Length(.10f);
        dataSet.setValueLinePart2Length(.50f);

        IPieDataSet a;
        mBinding.chart1.setData(data);
        val colors = new ArrayList<Integer>();
        colors.add(AndroidUtil.getColor(R.color.red_color_background1));
        colors.add(AndroidUtil.getColor(R.color.green_color_background1));
        colors.add(AndroidUtil.getColor(R.color.blue_color_background1));

        val ds1 = new PieDataSet(NoOfEmp, "");
        ds1.setColors(colors);
        ds1.setSliceSpace(2f);
        ds1.setValueTextColor(Color.WHITE);
        ds1.setValueTextSize(12f);
        mBinding.chart1.setData(new PieData(ds1));
        ds1.setSliceSpace(0);
        dataSet.setColors(colors);
        mBinding.chart1.animateXY(1500, 1500);
        mBinding.chart1.setHoleRadius(20);
        mBinding.chart1.setTransparentCircleRadius(20);
        mBinding.chart1.setCenterTextSize(10);
        mBinding.chart1.setCenterTextColor(R.attr.green_color_background);
        mBinding.chart1.getDescription()
                       .setEnabled(false);
        mBinding.chart1.setHoleColor(android.R.color.transparent);
        Legend l = mBinding.chart1.getLegend();
        l.setTextSize(14f);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.CENTER);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        if (ThemeUtils.getCurrentThemeIsDark())
            l.setTextColor(Color.WHITE);
        else
            l.setTextColor(Color.BLACK);
        l.setEnabled(true);
        mBinding.chart1.setDrawEntryLabels(false);
        mBinding.chart1.setTransparentCircleRadius(35f);
    }

}
