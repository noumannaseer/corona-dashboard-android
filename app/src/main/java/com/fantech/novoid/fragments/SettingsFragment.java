package com.fantech.novoid.fragments;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fantech.novoid.databinding.FragmentDashboardBinding;
import com.fantech.novoid.databinding.FragmentSettingsBinding;
import com.fantech.novoid.utils.ThemeUtils;
import com.fantech.novoid.view_models.CoronaStatsViewModel;

//**********************************************
public class SettingsFragment
        extends BaseFragment
//**********************************************
{
    private View rootView;
    private FragmentSettingsBinding mBinding;
    private CoronaStatsViewModel mCoronaStatsViewModel;

    //***********************************************************************
    @Override
    public View onCreateViewBaseFragment(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState)
    //***********************************************************************
    {
        if (rootView == null)
        {
            mBinding = FragmentSettingsBinding.inflate(inflater, parent, false);
            rootView = mBinding.getRoot();
            super.setFragment(SettingsFragment.this);
            initControls();
        }
        return rootView;
    }

    //***********************************************************************
    private void initControls()
    //***********************************************************************
    {
        mBinding.darkModeSwitch.setChecked(ThemeUtils.getCurrentThemeIsDark());
        mBinding.darkModeSwitch.setOnClickListener(view->{
            switchTheme();
        });

    }

    private void switchTheme()
    {
        ThemeUtils.setDarkTheme(mBinding.darkModeSwitch.isChecked(),getActivity(),true);
    }
}
