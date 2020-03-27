package com.fantech.covidplus.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fantech.covidplus.activities.CountryStatsActivity;
import com.fantech.covidplus.adapters.CountriesListAdapter;
import com.fantech.covidplus.databinding.FragmentCountriesBinding;
import com.fantech.covidplus.utils.AndroidUtil;
import com.fantech.covidplus.view_models.CoronaStatsViewModel;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import lombok.val;

//********************************************************
public class CountriesFragment
        extends BaseFragment
        implements CountriesListAdapter.CountryClickListener

//********************************************************
{

    private View rootView;
    private FragmentCountriesBinding mBinding;
    private CoronaStatsViewModel mCoronaStatsViewModel;
    private List<String> mCountriesList;


    //***********************************************************************
    @Override
    public View onCreateViewBaseFragment(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState)
    //***********************************************************************
    {
        if (rootView == null)
        {
            mBinding = FragmentCountriesBinding.inflate(inflater, parent, false);
            rootView = mBinding.getRoot();
            super.setFragment(CountriesFragment.this);
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
        showLoadingDialog();
        mCoronaStatsViewModel.getCountriesList()
                             .observe(this, strings ->
                             {
                                 mCountriesList = strings;
                                 showCountriesOnRecyclerView(mCountriesList);
                                 AndroidUtil.handler.postDelayed(() -> {
                                     hideLoadingDialog();
                                 }, 1000);
                             });
        attachTextChangeListener();

    }

    //***************************************************************
    private void attachTextChangeListener()
    //***************************************************************
    {
        mBinding.country.addTextChangedListener(new TextWatcher()
        {
            //***************************************************************
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            //***************************************************************
            {

            }

            //***************************************************************
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            //***************************************************************
            {
                if (s.length() == 0)
                    showCountriesOnRecyclerView(mCountriesList);
                else
                    filterCountry(s.toString());
            }

            //***************************************************************
            @Override
            public void afterTextChanged(Editable s)
            //***************************************************************
            {

            }
        });
    }

    //***********************************************************************
    private void filterCountry(String searchCountry)
    //***********************************************************************
    {
        List<String> filteredCountryList = new ArrayList<>();
        for (val country : mCountriesList)
        {
            if (country.toLowerCase()
                       .contains(searchCountry.toLowerCase()))
                filteredCountryList.add(country);
        }
        showCountriesOnRecyclerView(filteredCountryList);
    }

    //***********************************************************************
    private void showCountriesOnRecyclerView(List<String> countriesList)
    //***********************************************************************
    {
        CountriesListAdapter countriesListAdapter = new CountriesListAdapter(countriesList, this);
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mBinding.recyclerView.setAdapter(countriesListAdapter);
    }

    //***********************************************************************
    @Override
    public void onCountryClick(String country)
    //***********************************************************************
    {
        Intent countryStatsIntent = new Intent(getActivity(), CountryStatsActivity.class);
        countryStatsIntent.putExtra(CountryStatsActivity.COUNTRY_NAME, country);
        startActivity(countryStatsIntent);

    }
}
