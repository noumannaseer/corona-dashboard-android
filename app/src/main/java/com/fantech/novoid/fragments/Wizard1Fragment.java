package com.fantech.novoid.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fantech.novoid.R;
import com.fantech.novoid.activities.WizardActivity;
import com.fantech.novoid.databinding.FragmentWizard1Binding;
import com.fantech.novoid.databinding.FragmentWizard2Binding;

//***********************************************************************
public class Wizard1Fragment
        extends BaseFragment
//***********************************************************************
{

    private View rootView;
    private FragmentWizard1Binding mBinding;

    //*************************************************************************
    public Wizard1Fragment()
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
            mBinding = FragmentWizard1Binding.inflate(inflater, parent, false);
            rootView = mBinding.getRoot();
            initControls();

        }
        return rootView;
    }

    //***********************************************************************
    private void initControls()
    //***********************************************************************
    {
        mBinding.next.setOnClickListener(view -> proceedNext());
    }

    //***********************************************************************
    private void proceedNext()
    //***********************************************************************

    {
        ((WizardActivity)getActivity()).Next();
    }

}
