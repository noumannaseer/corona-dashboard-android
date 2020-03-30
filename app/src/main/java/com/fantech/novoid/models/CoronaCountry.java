package com.fantech.novoid.models;

//********************************************
//@Entity(tableName = "Corona")
public class CoronaCountry
//********************************************
{
    private String latitude;
    private String longitude;
    private String country;
    private int totalDeath;
    private int totalRecovered;
    private int totalConfirmed;

    //********************************************
    public String getLatitude()
    //********************************************
    {
        return latitude;
    }

    public CoronaCountry(String latitude, String longitude, String country, int totalDeath, int totalRecovered, int totalConfirmed)
    {
        this.latitude = latitude;
        this.longitude = longitude;
        this.country = country;
        this.totalDeath = totalDeath;
        this.totalRecovered = totalRecovered;
        this.totalConfirmed = totalConfirmed;
    }

    //********************************************
    public void setLatitude(String latitude)
    //********************************************
    {
        this.latitude = latitude;
    }

    //********************************************
    public String getLongitude()
    //********************************************
    {
        return longitude;
    }

    //********************************************
    public void setLongitude(String longitude)
    //********************************************
    {
        this.longitude = longitude;
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

    //********************************************
    public int getTotalDeath()
    //********************************************
    {
        return totalDeath;
    }

    //********************************************
    public void setTotalDeath(int totalDeath)
    //********************************************
    {
        this.totalDeath = totalDeath;
    }

    //********************************************
    public int getTotalRecovered()
    //********************************************
    {
        return totalRecovered;
    }

    //********************************************
    public void setTotalRecovered(int totalRecovered)
    //********************************************
    {
        this.totalRecovered = totalRecovered;
    }

    //********************************************
    public int getTotalConfirmed()
    //********************************************
    {
        return totalConfirmed;
    }

    //********************************************
    public void setTotalConfirmed(int totalConfirmed)
    //********************************************
    {
        this.totalConfirmed = totalConfirmed;
    }
}
