package com.fantech.novoid.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fantech.novoid.adapters.ProvinceListAdapter;
import com.fantech.novoid.databinding.FragmentProvisionalStatsBinding;
import com.fantech.novoid.view_models.CoronaStatsViewModel;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;


//***********************************************************************
public class ProvisionalStatsFragment
        extends BaseFragment
//***********************************************************************
{

    private View rootView;
    private FragmentProvisionalStatsBinding mBinding;
    private String mCountryName;
    private CoronaStatsViewModel mCoronaStatsViewModel;

    //***********************************************************************
    public ProvisionalStatsFragment(String mCountryName)
    //***********************************************************************
    {
        this.mCountryName = mCountryName;
    }

    //*************************************************************************
    public ProvisionalStatsFragment()
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
            mBinding = FragmentProvisionalStatsBinding.inflate(inflater, parent, false);
            rootView = mBinding.getRoot();
            super.setFragment(ProvisionalStatsFragment.this);
            initControls();
        }
        return rootView;
    }

    //*******************************************************
    private void initControls()
    //*******************************************************
    {
        mCoronaStatsViewModel = ViewModelProviders.of(this)
                                                  .get(CoronaStatsViewModel.class);
        mCoronaStatsViewModel.getProvinceList(mCountryName)
                             .observe(this,
      coronas -> showProvinceOnRecyclerView(coronas));
    }

    //*******************************************************************
    private void showProvinceOnRecyclerView(List<String> coronas)
    //*******************************************************************
    {
        if (coronas == null)
            coronas = new ArrayList<>();

        if (coronas.size() == 0 || TextUtils.isEmpty(coronas.get(0)))
        {
            mBinding.recyclerView.setVisibility(View.GONE);
            mBinding.noResultFound.setVisibility(View.VISIBLE);
        }
        else
        {
            mBinding.recyclerView.setVisibility(View.VISIBLE);
            mBinding.noResultFound.setVisibility(View.GONE);
        }
        ProvinceListAdapter provinceListAdapter = new ProvinceListAdapter(coronas,
                                                                          null,
                                                                          this,
                                                                          mCountryName);
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mBinding.recyclerView.setAdapter(provinceListAdapter);
    }
}
