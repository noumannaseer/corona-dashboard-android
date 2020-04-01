package com.fantech.novoid.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.lifecycle.ViewModelProviders;
import lombok.val;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.fantech.novoid.R;
import com.fantech.novoid.activities.CountryStatsActivity;
import com.fantech.novoid.databinding.FragmentMapViewBinding;
import com.fantech.novoid.interfaces.ConnectionChangeCallback;
import com.fantech.novoid.models.Corona;
import com.fantech.novoid.models.CoronaCountry;
import com.fantech.novoid.models.CoronaMap;
import com.fantech.novoid.utils.AndroidUtil;
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
        implements ConnectionChangeCallback

//*****************************************************
{
    private View rootView;
    private FragmentMapViewBinding mBinding;
    private CoronaStatsViewModel mCoronaStatsViewModel;
    private GoogleMap mGoogleMap;
    private List<CoronaMap> mMapData;
    private List<CoronaCountry> mCountriesList;
    private List<Corona> mDeathStats;
    private List<Corona> mRecoveredStats;
    private List<Corona> mConfirmedStats;
    private WebView mWebView;
    private View mProgressView;
    private ImageView mNoInternetImage;


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
        mWebView = rootView.findViewById(R.id.web_view);
        mProgressView = rootView.findViewById(R.id.progress_view);
        mNoInternetImage = rootView.findViewById(R.id.no_internet);
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

        mWebView.setWebViewClient(new WebViewClient()
        {

            //**********************************************************************
            @Override
            public void onPageFinished(WebView view, String url)
            //**********************************************************************
            {
                mBinding.mapLine.setVisibility(View.VISIBLE);
                super.onPageFinished(view, url);
                switchVisibility(true);
            }

            //**********************************************************************
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon)
            //**********************************************************************
            {
                super.onPageStarted(view, url, favicon);
                switchVisibility(false);
            }
        });
        mWebView.getSettings()
                .setJavaScriptEnabled(true);
        mWebView.getSettings()
                .setDomStorageEnabled(true);
        mWebView.getSettings()
                .setDatabaseEnabled(true);
        mWebView.getSettings()
                .setMinimumFontSize(1);
        mWebView.getSettings()
                .setMinimumLogicalFontSize(1);
        mWebView.getSettings()
                .setJavaScriptEnabled(true);
        mWebView.getSettings()
                .setJavaScriptEnabled(true);
        mWebView.getSettings()
                .setUseWideViewPort(true);

    }

    //*************************************************
    private void processList()
    //*************************************************
    {
        if (mRecoveredStats == null || mConfirmedStats == null || mDeathStats == null ||
                mRecoveredStats.size()==0 || mConfirmedStats.size()==0 || mDeathStats.size()==0
        )
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
        mRecoveredStats=null;
        mDeathStats=null;
        mConfirmedStats=null;

        mWebView.getSettings()
                .setJavaScriptEnabled(true);
        mWebView.addJavascriptInterface(new JavaScriptInterface(getContext(), mCountriesList),
                                        "AndroidNativeCode");

        mWebView.loadUrl("file:///android_asset/index.html");
    }

    @Override
    public void onConnectionChanged(boolean isConnected)
    {

        if (!isConnected)
        {
            mWebView.setVisibility(View.GONE);
            mProgressView.setVisibility(View.GONE);
            mNoInternetImage.setVisibility(View.VISIBLE);
        }
        else
        {
            mWebView.setVisibility(View.GONE);
            mWebView.reload();
            mProgressView.setVisibility(View.VISIBLE);
            mNoInternetImage.setVisibility(View.GONE);
        }
    }

    //**********************************************************************
    private void switchVisibility(boolean pageLoaded)
    //**********************************************************************
    {

        if (AndroidUtil.isNetworkStatusAvailable())
        {
            if (pageLoaded)
            {
                mWebView.setVisibility(View.VISIBLE);
                mProgressView.setVisibility(View.GONE);
                mNoInternetImage.setVisibility(View.GONE);
            }
            else
            {
                mWebView.setVisibility(View.GONE);
                mProgressView.setVisibility(View.VISIBLE);
                mNoInternetImage.setVisibility(View.GONE);
            }

        }
        else
        {
            mWebView.setVisibility(View.GONE);
            mProgressView.setVisibility(View.GONE);
            mNoInternetImage.setVisibility(View.VISIBLE);
        }
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
                    int isDark = ThemeUtils.getCurrentThemeIsDark()?1:0;
                    mWebView.loadUrl("javascript:setJson(" + jArray + "," + isDark + ")");
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
