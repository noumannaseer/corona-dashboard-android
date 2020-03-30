package com.fantech.novoid.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.lifecycle.ViewModelProviders;
import lombok.val;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.fantech.novoid.R;
import com.fantech.novoid.activities.CountryStatsActivity;
import com.fantech.novoid.databinding.FragmentMapViewBinding;
import com.fantech.novoid.models.Corona;
import com.fantech.novoid.models.CoronaCountry;
import com.fantech.novoid.models.CoronaMap;
import com.fantech.novoid.utils.Constants;
import com.fantech.novoid.utils.ThemeUtils;
import com.fantech.novoid.view_models.CoronaStatsViewModel;
import com.google.android.gms.maps.GoogleMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

//*****************************************************
public class MapViewFragment
        extends BaseFragment
//*****************************************************
{
    private View rootView;
    private FragmentMapViewBinding mBinding;
    private CoronaStatsViewModel mCoronaStatsViewModel;
    private GoogleMap mGoogleMap;
    private List<CoronaMap> mMapData;
    WebView view;
    private List<CoronaCountry> mCountriesList;
    private List<Corona> mDeathStats;
    private List<Corona> mRecoveredStats;
    private List<Corona> mConfirmedStats;


    @SuppressLint("SetJavaScriptEnabled")
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

        view = rootView.findViewById(R.id.webview);
        mCoronaStatsViewModel.getCountriesListDeath()
         .observe(this, strings ->
         {
             mDeathStats = strings;
             processList();
         });
        mCoronaStatsViewModel.getCountriesListRecovered()
         .observe(this, strings ->
         {
             mRecoveredStats = strings;
             processList();
         });
        mCoronaStatsViewModel.getCountriesListConfirmed()
         .observe(this, strings ->
         {
             mConfirmedStats = strings;
             processList();
         });

    }

    //*************************************************
    private void processList()
    //*************************************************
    {
        if (mRecoveredStats == null || mConfirmedStats == null || mDeathStats == null)
            return;
        mCountriesList = new ArrayList<>();
        for (int i = 0; i < mRecoveredStats.size(); i++)
        {
            val recovered = mRecoveredStats.get(i);
            val death = mDeathStats.get(i);
            val confirmed = mConfirmedStats.get(i);

            mCountriesList.add(new CoronaCountry(recovered.getLatitude(),
                                                 recovered.getLongitude(),
                                                 recovered.getCountry(),
                                                 death.getQuantity(),
                                                 recovered.getQuantity(),
                                                 confirmed.getQuantity()));
        }

        view.getSettings()
            .setJavaScriptEnabled(true);
        view.addJavascriptInterface(new JavaScriptInterface(getContext(), mCountriesList),
                                    "AndroidNativeCode");

        view.loadUrl("file:///android_asset/index.html");
    }


    //*****************************************************
    public class JavaScriptInterface
            //*****************************************************
    {
        private Context mContext;
        private List<CoronaCountry> mCountryList;

        //*****************************************************
        public JavaScriptInterface(Context mContext, List<CoronaCountry> mCountryList)
        //*****************************************************
        {
            this.mContext = mContext;
            this.mCountryList = mCountryList;
        }


        //*****************************************************
        @JavascriptInterface
        public void getValueJson()
                throws JSONException
        //*****************************************************
        {
            final JSONArray jArray = new JSONArray();


            for (val country : mCountryList)
            {
                JSONObject jObject = new JSONObject();
                jObject.put(Constants.COUNTRY, country.getCountry());
                jObject.put(Constants.DEATHS, country.getTotalDeath());
                jObject.put(Constants.RECOVERED, country.getTotalRecovered());
                jObject.put(Constants.CONFIRMED, country.getTotalConfirmed());
                jArray.put(jObject);
            }

            //*****************************************************
            getActivity().runOnUiThread(new Runnable()
                    //*****************************************************
            {
                //*****************************************************
                @Override
                public void run()
                //*****************************************************
                {
                    int isDark = 0;
                    if (ThemeUtils.getCurrentThemeIsDark())
                        isDark = 1;
                    view.loadUrl("javascript:setJson(" + jArray + "," + isDark + ")");
                }
            });

        }

        //*****************************************************
        @JavascriptInterface
        public void onMarkerCLick(String country)
                throws JSONException
        //*****************************************************
        {

            getActivity().runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    Intent countryStatIntent = new Intent(getActivity(),
                                                          CountryStatsActivity.class);
                    countryStatIntent.putExtra(CountryStatsActivity.COUNTRY_NAME, country);
                    startActivity(countryStatIntent);
                }
            });

        }
    }

}
