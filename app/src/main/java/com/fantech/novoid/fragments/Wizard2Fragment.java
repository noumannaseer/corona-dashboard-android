package com.fantech.novoid.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blongho.country_data.World;
import com.fantech.novoid.databinding.FragmentWizard2Binding;

//***********************************************************************
public class Wizard2Fragment
        extends BaseFragment
//***********************************************************************
{

    private View rootView;
    private FragmentWizard2Binding mBinding;


    //***********************************************************************
    @Override
    public View onCreateViewBaseFragment(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState)
    //***********************************************************************
    {
        if (rootView == null)
        {
            mBinding = FragmentWizard2Binding.inflate(inflater, parent, false);
            rootView = mBinding.getRoot();
            initControls();

        }
        return rootView;
    }

    private void initControls()
    {
        World.init(getActivity());
        final int flag = World.getFlagOf(mCountryName.toLowerCase());
        mBinding.countryFlag.setImageResource(flag);
    }
}
