package com.fantech.novoid.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blongho.country_data.World;
import com.fantech.novoid.activities.HomeActivity;
import com.fantech.novoid.activities.WizardActivity;
import com.fantech.novoid.databinding.FragmentWizard2Binding;
import com.fantech.novoid.models.CountryReader;

import lombok.val;

//***********************************************************************
public class Wizard2Fragment
        extends BaseFragment
//***********************************************************************
{

    private View rootView;
    private FragmentWizard2Binding mBinding;

    //*************************************************************************
    public Wizard2Fragment()
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
            mBinding = FragmentWizard2Binding.inflate(inflater, parent, false);
            rootView = mBinding.getRoot();
            initControls();

        }
        return rootView;
    }

    //***********************************************************************
    private void initControls()
    //***********************************************************************
    {
        World.init(getActivity());
        val countryName = CountryReader.getUserCountry(getActivity());
        setCountryFlag(countryName);
        mBinding.countryList.setText(countryName);
        mBinding.next.setOnClickListener(view -> proceedNext());
        mBinding.cancel.setOnClickListener(view -> gotoHomeActivity());

    }

    //***********************************************************************
    private void gotoHomeActivity()
    //***********************************************************************
    {
        Intent homeIntent = new Intent(getActivity(), HomeActivity.class);
        getActivity().startActivity(homeIntent);
        getActivity().finish();

    }

    //***********************************************************************
    private void proceedNext()
    //***********************************************************************
    {
        ((WizardActivity)getActivity()).Next();
    }

    //***********************************************************************
    private void setCountryFlag(String countryName)
    //***********************************************************************
    {

        final int flag = World.getFlagOf(countryName);
        mBinding.countryFlag.setImageResource(flag);
    }
}
