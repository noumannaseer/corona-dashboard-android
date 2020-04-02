package com.fantech.novoid.fragments;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Trace;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.fantech.novoid.R;
import com.fantech.novoid.databinding.ChartDetailDialogBinding;
import com.fantech.novoid.databinding.FragmentDashboardBinding;
import com.fantech.novoid.models.CoronaGraph;
import com.fantech.novoid.repository.CoronaStatsRepository;
import com.fantech.novoid.utils.AndroidUtil;
import com.fantech.novoid.utils.Constants;
import com.fantech.novoid.utils.SharedPreferencesUtils;
import com.fantech.novoid.utils.UIUtils;
import com.fantech.novoid.view_models.CoronaStatsViewModel;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import lecho.lib.hellocharts.formatter.SimpleAxisValueFormatter;
import lecho.lib.hellocharts.listener.LineChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.Viewport;
import lombok.val;

//********************************************
public class DashboardFragment
        extends BaseFragment
    implements SwipeRefreshLayout.OnRefreshListener
//********************************************
{
    private View rootView;
    private FragmentDashboardBinding mBinding;
    private CoronaStatsViewModel mCoronaStatsViewModel;

    //***********************************************************************
    public void onCreate(Bundle savedInstanceState)
    //***********************************************************************
    {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

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
           loadStats();

        }
        return rootView;
    }

    private void loadStats()
    {

        showLoadingDialog();
        new CoronaStatsRepository(this, getActivity(), () -> initControls())
                             .checkPreviousData();
    }

    //**********************************************
    private void initControls()
    //**********************************************
    {
        hideLoadingDialog();
        if(mBinding.pullToRefresh.isRefreshing())
            mBinding.pullToRefresh.setRefreshing(false);
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

        showGraph(Constants.REPORT_DEATH);
        addMapTabs();
        val updatedTime = SharedPreferencesUtils.getLong(SharedPreferencesUtils.LAST_UPDATED_TIME);
        mBinding.lastUpdated.setText(AndroidUtil.getString(R.string.updated_at_template,
                                                           UIUtils.getDate(updatedTime,
                                                                           "MMM dd, yyyy hh:mm a z")));
        mBinding.pullToRefresh.setOnRefreshListener(this);


    }

    //**********************************************
    private void showGraph(int reportType)
    //**********************************************
    {
        mCoronaStatsViewModel.getLast30DaysRecord(Calendar.getInstance()
                                                          .getTime(), reportType)
         .observe(this,
                  coronas -> {
                      if (coronas == null || coronas.size() == 0)
                          return;
                      initLineGraph(coronas, reportType);
                  });

    }

    //**********************************************
    private void addMapTabs()
    //**********************************************
    {
        mBinding.tabLayout.removeAllTabs();
        mBinding.tabLayout.addTab(mBinding.tabLayout.newTab()
                                                    .setText(
                                                            getString(R.string.total_death)));
        mBinding.tabLayout.addTab(mBinding.tabLayout.newTab()
                                                    .setText(getString(R.string.total_confirmed)));
        mBinding.tabLayout.addTab(mBinding.tabLayout.newTab()
                                                    .setText(getString(R.string.total_recorved)));

        mBinding.tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        mBinding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener()
        {
            @Override
            public void onTabSelected(TabLayout.Tab tab)
            {
                showGraph(tab.getPosition());
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

    //**********************************************
    private void addMainTabs()
    //**********************************************
    {
        mBinding.countryWorldWide.addTab(mBinding.countryWorldWide.newTab()
                                                                  .setText(
                                                                          getString(
                                                                                  R.string.total_death)));
        mBinding.countryWorldWide.addTab(mBinding.countryWorldWide.newTab()
                                                                  .setText(getString(
                                                                          R.string.total_confirmed)));
        mBinding.countryWorldWide.addTab(mBinding.countryWorldWide.newTab()
                                                                  .setText(getString(
                                                                          R.string.total_recorved)));

        mBinding.tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        mBinding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener()
        {
            @Override
            public void onTabSelected(TabLayout.Tab tab)
            {
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


    //*****************************************************************************
    private void initLineGraph(List<CoronaGraph> coronaGraphs, int reportType)
    //*****************************************************************************
    {

        List yAxisValues = new ArrayList();
        List axisValues = new ArrayList();


        Line line = new Line(yAxisValues).setColor(AndroidUtil.getColor(R.color.graph_color));


        int i = 0;
        int max = 0;
        for (val stats : coronaGraphs)
        {
            val xAxisLabel = UIUtils.getDate(stats.getDate()
                                                  .getTime(),
                                             "MMM dd");
            axisValues.add(i, new AxisValue(i).setLabel(xAxisLabel));
            val q = stats.getQuantity() / 1000;
            if (q > max)
                max = q;
            yAxisValues.add(new PointValue(i, q));
            i++;
        }


        List lines = new ArrayList();
        lines.add(line);
        LineChartData data = new LineChartData();
        data.setLines(lines);
        Axis axis = new Axis();
        axis.setValues(axisValues);
        axis.setTextSize(10);
        axis.setTextColor(AndroidUtil.getColor(R.color.graph_color));
        data.setAxisXBottom(axis);
        axis.setHasTiltedLabels(true);
        axis.setMaxLabelChars(8);


        Axis yAxis = new Axis();
        if (reportType == Constants.REPORT_DEATH)
            yAxis.setName(AndroidUtil.getString(R.string.total_death_in_k));
        else if (reportType == Constants.REPORT_CONFIRMED)
            yAxis.setName(AndroidUtil.getString(R.string.total_confirmed_in_k));
        else if (reportType == Constants.REPORT_RECOVERED)
            yAxis.setName(AndroidUtil.getString(R.string.total_recovered_in_k));



        SimpleAxisValueFormatter formatter = new SimpleAxisValueFormatter();
        formatter.setAppendedText(new char[] { 'k' });//text added after value, for example $1.99k

        yAxis.setFormatter(formatter);
        yAxis.setTextColor(AndroidUtil.getColor(R.color.graph_color));
        yAxis.setTextSize(10);
        yAxis.setHasTiltedLabels(true);
        data.setAxisYLeft(yAxis);



        mBinding.chart.setHorizontalScrollBarEnabled(true);
        mBinding.chart.setVerticalScrollBarEnabled(true);


        mBinding.chart.setLineChartData(data);
        mBinding.chart.animate();
        Viewport viewport = new Viewport(mBinding.chart.getMaximumViewport());
        viewport.top = max;
        viewport.bottom=0;
        mBinding.chart.setOnValueTouchListener(new LineChartOnValueSelectListener()
        {
            @Override
            public void onValueSelected(int lineIndex, int pointIndex, PointValue value)
            {
                int a=10;
                showDetail(coronaGraphs.get(pointIndex).getDate(),coronaGraphs.get(pointIndex).getQuantity());

            }

            @Override
            public void onValueDeselected()
            {

            }
        });

        mBinding.chart.setZoomEnabled(false);
        mBinding.chart.setMaximumViewport(viewport);


    }

    //*****************************************************************************
    private void showDetail(Date date, int quantity)
    //*****************************************************************************
    {

        final Dialog dialog = new Dialog(getActivity());

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


        ChartDetailDialogBinding chartDetail = DataBindingUtil.inflate(
                LayoutInflater.from(getActivity()), R.layout.chart_detail_dialog, null, false);
        dialog.setContentView(chartDetail.getRoot());

        chartDetail.stat.setText(UIUtils.getFormattedAmount(quantity));
        chartDetail.date.setText(UIUtils.getDate(date.getTime(),"MMM dd, yyyy"));

        dialog.show();

    }

    @Override
    public void onRefresh()
    {
        SharedPreferencesUtils.setValue(SharedPreferencesUtils.LAST_UPDATED_TIME,(long)0);
        loadStats();
    }
}
