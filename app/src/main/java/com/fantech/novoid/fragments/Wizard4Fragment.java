package com.fantech.novoid.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fantech.novoid.activities.HomeActivity;
import com.fantech.novoid.databinding.FragmentWizard4Binding;


//***********************************************************************
public class Wizard4Fragment
        extends BaseFragment
//***********************************************************************
{


    private View rootView;
    private FragmentWizard4Binding mBinding;
    private long mTime;


    //***********************************************************************
    @Override
    public View onCreateViewBaseFragment(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState)
    //***********************************************************************
    {
        if (rootView == null)
        {
            mBinding = FragmentWizard4Binding.inflate(inflater, parent, false);
            rootView = mBinding.getRoot();
            initControls();
        }
        return rootView;
    }

    //***********************************************************************
    private void initControls()
    //***********************************************************************
    {
        mBinding.getStarted.setOnClickListener(view -> gotoHomeActivity());
    }

    //***********************************************************************
    private void gotoHomeActivity()
    //***********************************************************************
    {
        Intent homeIntent = new Intent(getActivity(), HomeActivity.class);
        startActivity(homeIntent);
        getActivity().finish();
    }

}
