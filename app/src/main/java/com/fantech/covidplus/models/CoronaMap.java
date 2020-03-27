package com.fantech.covidplus.models;

import androidx.room.Entity;

//********************************************
@Entity(tableName = "Corona")
public class CoronaMap
//********************************************
{
    private int quantity;
    private String country;
    private String latitude;
    private String longitude;

    public String getLatitude()
    {
        return latitude;
    }

    public void setLatitude(String latitude)
    {
        this.latitude = latitude;
    }

    public String getLongitude()
    {
        return longitude;
    }

    public void setLongitude(String longitude)
    {
        this.longitude = longitude;
    }

    //********************************************
    public int getQuantity()
    //********************************************
    {
        return quantity;
    }

    //********************************************
    public void setQuantity(int quantity)
    //********************************************
    {
        this.quantity = quantity;
    }

    //********************************************
    public String getCountry()
    //********************************************
    {
        return country;
    }

    //********************************************
    public void setCountry(String country)
    //********************************************
    {
        this.country = country;
    }
}