package com.fantech.covidplus.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fantech.covidplus.R;
import com.fantech.covidplus.utils.AndroidUtil;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;
import lombok.NonNull;
import lombok.val;

//************************************************************
public abstract class BaseFragment extends Fragment
//************************************************************
{
    private Activity mBaseActivity;
    private Fragment mFragment;
    private Dialog mLoadingBar;
    protected View mRootView;

    //************************************************************
    public void onCreate(Bundle savedInstanceState)
    //************************************************************
    {
        super.onCreate(savedInstanceState);
    }

    //************************************************************************************************
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanseState)
    //************************************************************************************************
    {
        View view = onCreateViewBaseFragment(inflater, parent, savedInstanseState);
        return view;
    }

    //*********************************************************************
    @NonNull
    public abstract View onCreateViewBaseFragment(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState);
    //*********************************************************************

    //*********************************************************************
    public void setFragment(Fragment fragment)
    //*********************************************************************
    {
        if (mFragment != null)
            return;
        mFragment = fragment;
        mBaseActivity = mFragment.getActivity();
    }

    //*********************************************************************
    protected void setText(TextView textView, String value)
    //*********************************************************************
    {
        val textValue = TextUtils.isEmpty(value) ? AndroidUtil.getString(R.string.n_a) : value;
        textView.setText(textValue);
    }

    //***********************************************************************
    protected void setTextWithoutNA(TextView textView, String value)
    //***********************************************************************
    {
        if (!TextUtils.isEmpty(value))
            textView.setText(value);
    }

    //***********************************************************************
    public void showLoadingDialog()
    //***********************************************************************
    {
        if (mLoadingBar == null)
        {
            mLoadingBar = new Dialog(mBaseActivity, R.style.CustomTransparentDialog);
            mLoadingBar.setCancelable(false);
            mLoadingBar.setCanceledOnTouchOutside(false);
        }
        mLoadingBar.show();
    }
    //***********************************************************************

    public void hideLoadingDialog()
    //***********************************************************************
    {
        if (mLoadingBar != null)
        {
            mLoadingBar.dismiss();
        }
    }

    //*******************************************
    public void navigateUp()
    //*******************************************
    {
        NavHostFragment.findNavController(mFragment)
                       .navigateUp();
    }

    //*************************************************
    public void navigateTo(int destinationId)
    //*************************************************
    {
        NavHostFragment.findNavController(mFragment)
                       .navigate(destinationId);
    }

    //*************************************************
    public void navigateTo(int destinationId, Bundle bundle)
    //*************************************************
    {
        NavHostFragment.findNavController(mFragment)
                       .navigate(destinationId, bundle);
    }

    //*************************************************
    public void navigateTo(int destinationId, NavOptions navOptions)
    //*************************************************
    {
        NavHostFragment.findNavController(mFragment)
                       .navigate(destinationId, null, navOptions);
    }

    //*************************************************
    public void navigateTo(int destinationId, Bundle bundle, NavOptions navOptions)
    //*************************************************
    {
        NavHostFragment.findNavController(mFragment)
                       .navigate(destinationId, bundle, navOptions);
    }

}
