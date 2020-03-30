package com.fantech.novoid.models;

import com.fantech.novoid.R;

//***********************************************************
public enum SortingType
//***********************************************************
{
    ASCENDING("Ascending"),
    DESCENDING("Descending"),
    DEATH_CASES("Death cases"),
    RECOVERED_CASES("Recovered cases"),
    CONFIRMED_CASES("Confirmed cases");
    private String mSortType;

    //***********************************************************
    SortingType(String sortType)
    //***********************************************************
    {
        mSortType = sortType;
    }

    //***********************************************************
    public String getSortingType()
    //***********************************************************
    {
        return mSortType;
    }

    //***********************************************************
    public static SortingType getSortType(int id)
    //***********************************************************
    {
        if (R.id.accending == id)
            return ASCENDING;
        else if (R.id.descending == id)
            return DESCENDING;
        else if (R.id.death_cases == id)
            return DEATH_CASES;
        else if (R.id.recover_cases == id)
            return RECOVERED_CASES;
        else if (R.id.confirmed_cases == id)
            return CONFIRMED_CASES;
        else
            return ASCENDING;
    }
}
