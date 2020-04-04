package com.fantech.novoid.models;

import android.content.Context;
import android.telephony.TelephonyManager;

import com.fantech.novoid.R;
import com.fantech.novoid.utils.AndroidUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import lombok.NonNull;
import lombok.val;

//********************************************************************
public class CountryReader
//********************************************************************
{

    //********************************************************************
    private static String getCountryName(@NonNull String countryCode)
    //********************************************************************
    {
        val countriesList = AndroidUtil.getStringArray(R.array.countries);
        for (val country : countriesList)
        {
            val countryDetail = new Country(country);
            if (countryDetail.getCountryCode()
                             .toLowerCase()
                             .equals(countryCode.toLowerCase()))
            {
                return countryDetail.getCountryName();
            }
        }
        return null;

    }

    //********************************************************************
    public static String getUserCountry(Context context)
    //********************************************************************
    {
        try
        {
            final TelephonyManager tm = (TelephonyManager)context.getSystemService(
                    Context.TELEPHONY_SERVICE);
            final String simCountry = tm.getSimCountryIso();
            if (simCountry != null && simCountry.length() == 2)
            { // SIM country code is available
                return getCountryName(simCountry.toLowerCase(Locale.US));
            }
            else if (tm.getPhoneType() != TelephonyManager.PHONE_TYPE_CDMA)
            { // device is not 3G (would be unreliable)
                String networkCountry = tm.getNetworkCountryIso();
                if (networkCountry != null && networkCountry.length() == 2)
                { // network country code is available
                    return getCountryName(networkCountry.toLowerCase(Locale.US));
                }
            }
        }
        catch (Exception e)
        {
        }
        return null;
    }

    //********************************************************************
    public static List<String> getCountriesList()
    //********************************************************************
    {
        val countriesList = AndroidUtil.getStringArray(R.array.countries);
        List<String> countriesStringList = new ArrayList<>();
        for (val country : countriesList)
        {
            val countryDetail = new Country(country);
            countriesStringList.add(countryDetail.getCountryName());

        }
        return countriesStringList;

    }

}
