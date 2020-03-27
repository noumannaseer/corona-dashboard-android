package com.fantech.covidplus.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fantech.covidplus.R;


//******************************************************************
public class StaySafeFragment extends Fragment
//******************************************************************
{

    //******************************************************************
    public StaySafeFragment()
    //******************************************************************
    {
        // Required empty public constructor
    }


    //******************************************************************
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    //******************************************************************
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_stay_safe, container, false);
    }
}
