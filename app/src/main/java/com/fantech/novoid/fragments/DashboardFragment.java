package com.fantech.novoid.fragments;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.fantech.novoid.R;
import com.fantech.novoid.databinding.ChartDetailDialogBinding;
import com.fantech.novoid.databinding.FragmentDashboardBinding;
import com.fantech.novoid.repository.CoronaStatsRepository;
import com.fantech.novoid.utils.AndroidUtil;
import com.fantech.novoid.utils.Constants;
import com.fantech.novoid.utils.SharedPreferencesUtils;
import com.fantech.novoid.utils.ThemeUtils;
import com.fantech.novoid.utils.UIUtils;
import com.fantech.novoid.view_models.CoronaStatsViewModel;

import java.util.Date;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
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


    //*************************************************************************
    public DashboardFragment()
    //*************************************************************************
    {

    }

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

    //**********************************************
    private void loadStats()
    //**********************************************
    {

        if(ThemeUtils.getCurrentThemeIsDark())
            mBinding.pullToRefresh.setBackgroundColor(AndroidUtil.getColor(R.color.app_background1));
        showLoadingDialog();
        new CoronaStatsRepository(this, getActivity(), isSuccessfully -> initControls(isSuccessfully))
                             .checkPreviousData();
    }


    //**********************************************
    private void initControls(boolean isSuccessfully)
    //**********************************************
    {
        hideLoadingDialog();

        if(mBinding.pullToRefresh.isRefreshing())
            mBinding.pullToRefresh.setRefreshing(false);
        mBinding.pullToRefresh.setOnRefreshListener(this);

        if (!isSuccessfully)
        {
            return;
        }
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

        val updatedTime = SharedPreferencesUtils.getLong(SharedPreferencesUtils.LAST_UPDATED_TIME);
        mBinding.lastUpdated.setText(UIUtils.getDate(updatedTime,
                                                     "hh:mm, MMM dd, yyyy"));


    }



    //*****************************************************************************
    @Override
    public void onRefresh()
    {
        SharedPreferencesUtils.setValue(SharedPreferencesUtils.LAST_UPDATED_TIME,(long)0);
        loadStats();
    }
}
