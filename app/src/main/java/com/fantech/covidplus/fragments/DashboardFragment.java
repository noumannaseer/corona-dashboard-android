package com.fantech.covidplus.fragments;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fantech.covidplus.R;
import com.fantech.covidplus.databinding.FragmentDashboardBinding;
import com.fantech.covidplus.models.CoronaGraph;
import com.fantech.covidplus.utils.AndroidUtil;
import com.fantech.covidplus.utils.Constants;
import com.fantech.covidplus.view_models.CoronaStatsViewModel;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.Utils;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import lombok.NonNull;
import lombok.val;

//********************************************
public class DashboardFragment
        extends BaseFragment
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
        mCoronaStatsViewModel = ViewModelProviders.of(this)
                                                  .get(CoronaStatsViewModel.class);
        addTabs();
        mCoronaStatsViewModel.countSum(Constants.REPORT_DEATH)
                             .observe(this,
                                      integer -> {
                                          if (integer == null)
                                              integer = 0;
                                          mBinding.totalDeaths.setText(
                                                  String.valueOf(integer));
                                      });

        mCoronaStatsViewModel.countSum(Constants.REPORT_CONFIRMED)
                             .observe(this, integer -> {
                                 if (integer == null)
                                     integer = 0;
                                 mBinding.totalConfirmed.setText(
                                         String.valueOf(integer));
                             });

        mCoronaStatsViewModel.countSum(Constants.REPORT_RECOVERED)
                             .observe(this,
                                      integer -> {
                                          if (integer == null)
                                              integer = 0;
                                          mBinding.totalRecovered.setText(
                                                  String.valueOf(integer));
                                      });
        mCoronaStatsViewModel.getLast30DaysRecord(Calendar.getInstance()
                                                          .getTime())
                             .observe(this,
                                      new Observer<List<CoronaGraph>>()
                                      {
                                          @Override
                                          public void onChanged(List<CoronaGraph> coronas)
                                          {
                                              if (coronas == null || coronas.size() == 0)
                                                  return;
                                              showLineChart(0, coronas);
                                          }
                                      });

    }

    //************************************************************
    public void setLineCharProperties(@NonNull List<CoronaGraph> statsList)
    //************************************************************
    {

        if (statsList.size() == 0)
            return;

        mBinding.lineChart.setTouchEnabled(true);
        mBinding.lineChart.setPinchZoom(true);
        MyMarkerView mv = new MyMarkerView(getActivity(),
                                           R.layout.custom_marker_view);

        mv.setMStats(statsList);
        mv.setChartView(mBinding.lineChart);

        mBinding.lineChart.setMarker(mv);

        LimitLine llXAxis = new LimitLine(10f, "Index 10");

        XAxis xAxis = mBinding.lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.disableGridDashedLine();
        YAxis leftAxis = mBinding.lineChart.getAxisLeft();
        leftAxis.setDrawZeroLine(false);
        leftAxis.setDrawLimitLinesBehindData(false);

        mBinding.lineChart.getAxisRight()
                          .setEnabled(false);
        mBinding.lineChart.getAxisLeft()
                          .setDrawGridLines(false);
        mBinding.lineChart.getXAxis()
                          .setDrawGridLines(false);
        mBinding.lineChart.fitScreen();
        mBinding.lineChart.getAxisLeft()
                          .setDrawGridLines(false);
        mBinding.lineChart.getXAxis()
                          .setDrawGridLines(false);

        mBinding.lineChart.getAxis(YAxis.AxisDependency.LEFT)
                          .setEnabled(false);
        mBinding.lineChart.animateXY(1200, 1000);

    }

    //************************************************************
    private void showLineChart(int type, List<CoronaGraph> list)
    //************************************************************
    {

        ArrayList<Entry> values = new ArrayList<>();
        int i = 0;
        for (val graph : list)
        {
            values.add(new Entry(i++, graph.getQuantity()));

        }

        if (type == 0)
        {
            setLineCharProperties(list);

        }
        else if (type == 1)
        {

            setLineCharProperties(list);

        }
        else
        {
            setLineCharProperties(list);
        }


        LineDataSet set1;
        if (mBinding.lineChart.getData() != null &&
                mBinding.lineChart.getData()
                                  .getDataSetCount() > 0)
        {
            set1 = (LineDataSet)mBinding.lineChart.getData()
                                                  .getDataSetByIndex(0);
            set1.setValues(values);
            mBinding.lineChart.getData()
                              .notifyDataChanged();
            mBinding.lineChart.notifyDataSetChanged();
        }
        else
        {
            set1 = new LineDataSet(values, "Sample Data");
            set1.setColor(AndroidUtil.getColor(R.color.green_color_background1));
            set1.setCircleColor(AndroidUtil.getColor(R.color.green_color_background1));
            set1.setLineWidth(5f);
            set1.setCircleRadius(6f);
            set1.setDrawCircleHole(false);
            set1.setValueTextSize(9f);
            set1.setFillAlpha(30);
            set1.setFillColor(AndroidUtil.getColor(R.color.green_color_background1));
            set1.setDrawFilled(true);
            if (Utils.getSDKInt() >= 18)
            {
                Drawable drawable = AndroidUtil.getDrawable(R.drawable.fade_blue);
                set1.setFillDrawable(drawable);
            }
            else
            {
                set1.setFillColor(Color.DKGRAY);
            }

            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);
            set1.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
            set1.setDrawValues(false);
            LineData data = new LineData(dataSets);
            mBinding.lineChart.getXAxis()
                              .setEnabled(false);
            mBinding.lineChart.getLegend()
                              .setEnabled(false);
            mBinding.lineChart.setData(data);
            mBinding.lineChart.getDescription()
                              .setEnabled(false);
            mBinding.lineChart.getAxisLeft()
                              .setDrawLabels(false);
            mBinding.lineChart.setTranslationZ(10);
            mBinding.lineChart.getAxisRight()
                              .setDrawLabels(false);

        }
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
