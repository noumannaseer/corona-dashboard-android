package com.fantech.covidplus.adapters;


import com.fantech.covidplus.fragments.CountryStatsFragment;
import com.fantech.covidplus.fragments.ProvisionalStatsFragment;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

//*****************************************************
public class CustomFragmentPageAdapter
        extends FragmentStatePagerAdapter
//*****************************************************
{


    private CountryStatsFragment mCountryStatsFragment;
    private ProvisionalStatsFragment mProvisionalStatsFragment;
    private final int COUNTRY_STATS_FRAGMENT = 0;
    private final int PROVISIONAL_STATS_FRAGMENT = COUNTRY_STATS_FRAGMENT + 1;

    //*****************************************************
    public CustomFragmentPageAdapter(FragmentManager fm, String countryName)
    //*****************************************************
    {
        super(fm);
        mCountryStatsFragment = new CountryStatsFragment(countryName);
        mProvisionalStatsFragment = new ProvisionalStatsFragment(countryName);
    }

    //*****************************************************
    @Override
    public Fragment getItem(int fragmentId)
    //*****************************************************
    {
        switch (fragmentId)
        {
        case COUNTRY_STATS_FRAGMENT:
            return mCountryStatsFragment;
        case PROVISIONAL_STATS_FRAGMENT:
            return mProvisionalStatsFragment;
        default:
            return null;
        }
    }


    //*****************************************************
    @Override
    public int getCount()
    //*****************************************************
    {
        return 2;
    }
}
