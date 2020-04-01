package com.fantech.novoid.models;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.val;

//************************************************************************
@Setter
@Getter
public class Country
//************************************************************************
{
    private String countryCode;
    private String countryName;
    private String olympicCode;
    private String dialCode;

    //************************************************************************
    //AF:AFG:Afghanistan:+93
    public Country(@NonNull String countryString)
    //************************************************************************
    {

        val countryDetail = countryString.split(":");
        if (countryDetail.length < 4)
            return;
        countryCode = countryDetail[0];
        olympicCode = countryDetail[1];
        countryName = countryDetail[2];
        dialCode = countryDetail[3];
    }

}
