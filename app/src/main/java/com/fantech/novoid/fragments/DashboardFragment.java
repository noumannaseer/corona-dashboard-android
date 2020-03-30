package com.fantech.novoid.fragments;

import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fantech.novoid.R;
import com.fantech.novoid.databinding.FragmentDashboardBinding;
import com.fantech.novoid.models.CoronaGraph;
import com.fantech.novoid.utils.Constants;
import com.fantech.novoid.utils.ThemeUtils;
import com.fantech.novoid.utils.UIUtils;
import com.fantech.novoid.view_models.CoronaStatsViewModel;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.Utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.core.content.ContextCompat;
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
        mCoronaStatsViewModel.countSum(Constants.REPORT_DEATH)
                             .observe(this,
          integer -> {
              if (integer == null)
                  integer = 0;
              mBinding.totalDeaths.setText(
                      UIUtils.getFormattedAmount(integer));
          });

        mCoronaStatsViewModel.countSum(Constants.REPORT_CONFIRMED)
                             .observe(this, integer -> {
         if (integer == null)
             integer = 0;
         mBinding.totalConfirmed.setText(
                 UIUtils.getFormattedAmount(integer));
     });

        mCoronaStatsViewModel.countSum(Constants.REPORT_RECOVERED)
                             .observe(this,
          integer -> {
              if (integer == null)
                  integer = 0;
              mBinding.totalRecovered.setText(
                      UIUtils.getFormattedAmount(integer));
          });
        mCoronaStatsViewModel.getLast30DaysRecord(Calendar.getInstance()
                                                          .getTime())
                             .observe(this,
          coronas -> {
              if (coronas == null || coronas.size() == 0)
                  return;
              showLineChart(coronas);
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
    private void showLineChart(List<CoronaGraph> list)
    //************************************************************
    {

        ArrayList<Entry> values = new ArrayList<>();
        int i = 0;
        for (val graph : list)
        {
            values.add(new Entry(i++, graph.getQuantity()));
        }
        setLineCharProperties(list);
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
            set1 = new LineDataSet(values, "Last 30 days");
            set1.setDrawIcons(false);
            set1.enableDashedLine(10f, 5f, 0f);
            set1.enableDashedHighlightLine(10f, 5f, 0f);
            if (!ThemeUtils.getCurrentThemeIsDark())
            {
                set1.setColor(Color.DKGRAY);
                set1.setCircleColor(Color.DKGRAY);
            }
            else
            {
                set1.setColor(Color.WHITE);
                set1.setCircleColor(Color.WHITE);
            }
            set1.setLineWidth(1f);
            set1.setCircleRadius(3f);
            set1.setDrawCircleHole(false);
            set1.setValueTextSize(9f);
            set1.setDrawFilled(true);
            set1.setFormLineWidth(1f);
            set1.setFormLineDashEffect(new DashPathEffect(new float[] { 10f, 5f }, 0f));
            set1.setFormSize(15.f);
            if (Utils.getSDKInt() >= 18)
            {
                Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.fade_blue);
                set1.setFillDrawable(drawable);
            }
            else
            {
                set1.setFillColor(Color.DKGRAY);
            }
            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);
            LineData data = new LineData(dataSets);
            mBinding.lineChart.setData(data);
        }
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
            set1.setDrawIcons(false);
            set1.enableDashedLine(10f, 5f, 0f);
            set1.enableDashedHighlightLine(10f, 5f, 0f);
            if (!ThemeUtils.getCurrentThemeIsDark())
            {
                set1.setColor(Color.DKGRAY);
                set1.setCircleColor(Color.DKGRAY);
            }
            else
            {
                set1.setColor(Color.WHITE);
                set1.setCircleColor(Color.WHITE);

            }
            set1.setLineWidth(1f);
            set1.setCircleRadius(3f);
            set1.setDrawCircleHole(false);
            set1.setValueTextSize(9f);
            set1.setDrawFilled(true);
            set1.setFormLineWidth(1f);
            set1.setFormLineDashEffect(new DashPathEffect(new float[] { 10f, 5f }, 0f));
            set1.setFormSize(15.f);
            if (Utils.getSDKInt() >= 18)
            {
                Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.fade_blue);
                set1.setFillDrawable(drawable);
            }
            else
            {
                set1.setFillColor(Color.DKGRAY);
            }
            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);
            LineData data = new LineData(dataSets);
            mBinding.lineChart.setData(data);
        }
    }

}
