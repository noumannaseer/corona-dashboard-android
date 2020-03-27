package com.fantech.covidplus.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import lombok.val;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fantech.covidplus.R;
import com.fantech.covidplus.activities.CountryStatsActivity;
import com.fantech.covidplus.databinding.FragmentMapViewBinding;
import com.fantech.covidplus.models.CoronaMap;
import com.fantech.covidplus.utils.AndroidUtil;
import com.fantech.covidplus.utils.Constants;
import com.fantech.covidplus.utils.ThemeUtils;
import com.fantech.covidplus.view_models.CoronaStatsViewModel;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;

import java.util.List;

//*****************************************************
public class MapViewFragment
        extends BaseFragment
        implements OnMapReadyCallback, GoogleMap.OnCircleClickListener
//*****************************************************
{
    private static final String MAP_VIEW_BUNDLE_KEY = "MAP_VIEW_BUNDLE_KEY";
    private View rootView;
    private FragmentMapViewBinding mBinding;
    private CoronaStatsViewModel mCoronaStatsViewModel;
    private GoogleMap mGoogleMap;
    private List<CoronaMap> mMapData;

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
        mCoronaStatsViewModel = ViewModelProviders.of(this)
                                                  .get(CoronaStatsViewModel.class);
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
        mCoronaStatsViewModel.getMapViewList(Constants.REPORT_DEATH)
                             .observe(this, new Observer<List<CoronaMap>>()
                             {
                                 @Override
                                 public void onChanged(List<CoronaMap> coronas)
                                 {
                                     mMapData = coronas;
                                     showDataOnMap();

                                 }
                             });
    }

    //*********************************************
    private void showDataOnMap()
    //*********************************************

    {
        mGoogleMap.setOnCircleClickListener(this);
        if (mMapData == null || mMapData.size() == 0)
        {
            return;
        }

        int index = 0;
        if (mGoogleMap != null)
            mGoogleMap.clear();
        LatLngBounds.Builder mBuilder = new LatLngBounds.Builder();
        for (val corona : mMapData)
        {

            mBuilder.include(new LatLng(Double.parseDouble(corona.getLatitude())
                    , Double.parseDouble(corona.getLongitude())));
            addMarkerToMap(corona.getCountry(),
                           "",
                           Double.parseDouble(corona.getLatitude()),
                           Double.parseDouble(corona.getLongitude()),
                           index, corona.getQuantity() * 10);

            index++;
        }
        if (ThemeUtils.getCurrentThemeIsDark())
            mGoogleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(getContext(), R.raw.mapview_night_mode));
        LatLngBounds bounds = mBuilder.build();
        int padding = 10;
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        mGoogleMap.animateCamera(cu);

    }


    //******************************************************************
    private void addMarkerToMap(String title, String snippet, double lat, double lng, int index, int radius)
    //******************************************************************
    {

        val circle = mGoogleMap.addCircle(new CircleOptions().center(new LatLng(lat, lng))
                                                             .radius(radius)
                                                             .strokeWidth(0f)
                                                             .fillColor(AndroidUtil.getColor(
                                                                     R.color.transparent_red)));
        circle.setTag(index);
        circle.setClickable(true);
    }

    //****************************************************
    public BitmapDescriptor getMarkerIcon(String color)
    //****************************************************
    {
        float[] hsv = new float[3];
        Color.colorToHSV(Color.parseColor(color), hsv);
        return BitmapDescriptorFactory.defaultMarker(hsv[0]);
    }

    //****************************************************
    @Override
    public void onCircleClick(Circle circle)
    //****************************************************
    {
        val index = Integer.parseInt(circle.getTag()
                                           .toString());
        Intent countryStatsIntent = new Intent(getActivity(), CountryStatsActivity.class);
        countryStatsIntent.putExtra(CountryStatsActivity.COUNTRY_NAME, mMapData.get(index)
                                                                               .getCountry());
        startActivity(countryStatsIntent);
    }

}
