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
import com.fantech.covidplus.models.Corona;
import com.fantech.covidplus.models.CoronaCountry;
import com.fantech.covidplus.view_models.CoronaStatsViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
    private List<CoronaCountry> mCountriesList;
    private List<Corona> mDeathStats;
    private List<Corona> mRecoveredStats;
    private List<Corona> mConfirmedStats;
    private boolean mIsAscending = true;


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

        attachTextChangeListener();
        mBinding.filter.setOnClickListener(view -> showFilter());

    }

    //*************************************************************
    private void showFilter()
    //*************************************************************
    {
        FilterBottomSheet filterBottomSheet =
                new FilterBottomSheet(
                        (deathRange, isAscending) -> {
                            mIsAscending = isAscending;
                            showCountriesOnRecyclerView(mCountriesList);

                        }, mIsAscending);
        filterBottomSheet.show(getActivity().getSupportFragmentManager(),
                               filterBottomSheet.getTag());

    }

    //*************************************************************
    private void processList()
    //*************************************************************
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
        showCountriesOnRecyclerView(mCountriesList);
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
        List<CoronaCountry> filteredCountryList = new ArrayList<>();
        for (val country : mCountriesList)
        {
            if (country.getCountry()
                       .toLowerCase()
                       .contains(searchCountry.toLowerCase()))
                filteredCountryList.add(country);
        }
        showCountriesOnRecyclerView(filteredCountryList);
    }

    //***********************************************************************
    private void showCountriesOnRecyclerView(List<CoronaCountry> countriesList)
    //***********************************************************************
    {
        if (mIsAscending)
        {
            Collections.sort(countriesList, new Comparator<CoronaCountry>()
            {
                @Override
                public int compare(CoronaCountry o1, CoronaCountry o2)
                {
                    return o1.getCountry()
                             .compareTo(o2.getCountry());
                }

                @Override
                public boolean equals(Object obj)
                {
                    return false;
                }
            });

        }
        else
        {
            Collections.sort(countriesList, new Comparator<CoronaCountry>()
            {
                @Override
                public int compare(CoronaCountry o1, CoronaCountry o2)
                {
                    return o2.getCountry()
                             .compareTo(o1.getCountry());
                }

                @Override
                public boolean equals(Object obj)
                {
                    return false;
                }
            });

        }
        CountriesListAdapter countriesListAdapter = new CountriesListAdapter(countriesList, this);
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mBinding.recyclerView.setAdapter(countriesListAdapter);
        hideLoadingDialog();
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
