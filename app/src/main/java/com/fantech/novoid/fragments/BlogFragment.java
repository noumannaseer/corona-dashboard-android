package com.fantech.novoid.fragments;


import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;


import com.fantech.novoid.R;
import com.fantech.novoid.databinding.FragmentBlogBinding;
import com.fantech.novoid.interfaces.ConnectionChangeCallback;
import com.fantech.novoid.utils.AndroidUtil;
import com.fantech.novoid.utils.NetworkChangeReceiver;
import com.fantech.novoid.utils.ThemeUtils;

import androidx.fragment.app.Fragment;


//*********************************************************
public class BlogFragment
        extends Fragment
        implements ConnectionChangeCallback
//*********************************************************
{

    private WebView mWebView;
    private View mProgressView;
    public static final String INFO_TITLE = "INFO_TITLE";
    public static final String SCREEN_URL = "SCREEN_URL";
    private String mTitle;
    private NetworkChangeReceiver receiver;
    private ImageView mNoInternetImage;
    private IntentFilter intentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
    private View rootView;
    private FragmentBlogBinding mBinding;


    //*************************************************************************
    public BlogFragment()
    //*************************************************************************
    {

    }

    //*************************************************************************
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    //*************************************************************************
    {
        if (rootView == null)
        {
            mBinding = FragmentBlogBinding.inflate(inflater, container, false);
            rootView = mBinding.getRoot();
            initControls();
        }
        return rootView;
    }

    //*************************************************************************
    private void initControls()
    //*************************************************************************
    {
        mWebView = rootView.findViewById(R.id.web_view);
        mProgressView = rootView.findViewById(R.id.progress_view);
        mNoInternetImage = rootView.findViewById(R.id.no_internet);
        mWebView.setWebViewClient(new WebViewClient()
        {

            //**********************************************************************
            @Override
            public void onPageFinished(WebView view, String url)
            //**********************************************************************
            {
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
        if (ThemeUtils.getCurrentThemeIsDark())
            mWebView.loadUrl("file:///android_asset/guide_lines_dark.html");
        else
            mWebView.loadUrl("file:///android_asset/guidelines.html");
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


    //**********************************************************************
    @Override
    public void onConnectionChanged(boolean isConnected)
    //**********************************************************************
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
}
