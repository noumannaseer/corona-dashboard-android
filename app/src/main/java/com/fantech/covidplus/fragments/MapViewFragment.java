package com.fantech.covidplus.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fantech.covidplus.R;
import com.fantech.covidplus.databinding.FragmentDashboardBinding;
import com.fantech.covidplus.databinding.FragmentMapViewBinding;
import com.fantech.covidplus.view_models.CoronaStatsViewModel;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;

//*****************************************************
public class MapViewFragment
        extends BaseFragment
        implements OnMapReadyCallback
//*****************************************************
{


    private static final String MAP_VIEW_BUNDLE_KEY = "MAP_VIEW_BUNDLE_KEY";


    private View rootView;
    private FragmentMapViewBinding mBinding;
    private CoronaStatsViewModel mCoronaStatsViewModel;
    private GoogleMap mGoogleMap;


    //***********************************************************************
    @Override
    public View onCreateViewBaseFragment(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState)
    //***********************************************************************
    {
        if (rootView == null)
        {
            mBinding = FragmentMapViewBinding.inflate(inflater, parent, false);
            rootView = mBinding.getRoot();
            super.setFragment(MapViewFragment.this);
            initGoogleMap(savedInstanceState);
            initControls();

        }
        return rootView;
    }

    //***********************************************************************
    private void initControls()
    //***********************************************************************
    {

    }

    //******************************************************************
    private void initGoogleMap(Bundle savedInstanceState)
    //******************************************************************
    {

        Bundle mapViewBundle = null;
        if (savedInstanceState != null)
        {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
        }
        mBinding.mapview.onCreate(mapViewBundle);
        mBinding.mapview.getMapAsync(this);
    }


    //******************************************************************
    @Override
    public void onResume()
    //******************************************************************
    {
        super.onResume();
        mBinding.mapview.onResume();
    }

    //******************************************************************
    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap map)
    //******************************************************************
    {
        mGoogleMap = map;
        mGoogleMap.getUiSettings()
                  .setAllGesturesEnabled(true);
        mGoogleMap.getUiSettings()
                  .setZoomControlsEnabled(true);
        mGoogleMap.getUiSettings()
                  .setAllGesturesEnabled(true);

    }

}
