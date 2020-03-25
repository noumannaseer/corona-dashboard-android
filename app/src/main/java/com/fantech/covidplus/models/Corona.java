package com.fantech.covidplus.models;

import java.util.Date;

import androidx.room.Entity;
import androidx.room.PrimaryKey;


//********************************************
@Entity(tableName = "Corona")
public class Corona
//********************************************
{
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int report_type;
    private String latitude;
    private String longitude;
    private int quantity;
    private String state;
    private String country;
    private Date date;


    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public int getReport_type()
    {
        return report_type;
    }

    public void setReport_type(int report_type)
    {
        this.report_type = report_type;
    }

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

    public int getQuantity()
    {
        return quantity;
    }

    public void setQuantity(int quantity)
    {
        this.quantity = quantity;
    }

    public String getState()
    {
        return state;
    }

    public void setState(String state)
    {
        this.state = state;
    }

    public String getCountry()
    {
        return country;
    }

    public void setCountry(String country)
    {
        this.country = country;
    }

    public Date getDate()
    {
        return date;
    }

    public void setDate(Date date)
    {
        this.date = date;
    }

}
