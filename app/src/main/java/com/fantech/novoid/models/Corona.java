package com.fantech.novoid.models;

import android.text.TextUtils;

import java.util.Calendar;
import java.util.Date;

import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import lombok.val;


//********************************************
@Entity(tableName = "Corona")
public class Corona
//********************************************
{
    @PrimaryKey(autoGenerate = true)
    @Nullable
    private int id;
    @Nullable
    private int report_type;
    private String latitude;
    private String longitude;
    private int quantity;
    private String state;
    private String country;
    private Date date;

    //*******************************************************************************************
    public Corona()
    //*******************************************************************************************
    {
    }

    //*******************************************************************************************
    public Corona(String[] rowValues, String[] columns, Calendar calendar, int type, int dateIndex)
    //*******************************************************************************************
    {
        state = rowValues[0].replace("\"", "");
        country = rowValues[1].replace("\"", "");
        latitude = rowValues[2].replace("\"", "");
        longitude = rowValues[3].replace("\"", "");
        val date = columns[dateIndex].split("/");
        calendar.set(Calendar.MONTH, (Integer.parseInt(date[0]) - 1));
        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(date[1]));
        calendar.set(Calendar.YEAR, (2000 + Integer.parseInt(date[2])));
        setDate(calendar.getTime());
        report_type = type;
        quantity = Integer.parseInt(
                TextUtils.isEmpty(rowValues[dateIndex].trim()) ? "0" : rowValues[dateIndex].trim());
    }

    //************************************
    public int getId()
    //************************************
    {
        return id;
    }

    //************************************
    public void setId(int id)
    //************************************
    {
        this.id = id;
    }


    //***********************************
    public int getReport_type()
    //***********************************
    {
        return report_type;
    }

    //***********************************
    public void setReport_type(int report_type)
    //***********************************
    {
        this.report_type = report_type;
    }

    //***********************************
    public String getLatitude()
    //***********************************
    {
        return latitude;
    }

    //***********************************
    public void setLatitude(String latitude)
    //***********************************
    {
        this.latitude = latitude;
    }

    //***********************************
    public String getLongitude()
    //***********************************
    {
        return longitude;
    }

    //***********************************
    public void setLongitude(String longitude)
    //***********************************
    {
        this.longitude = longitude;
    }

    //***********************************
    public int getQuantity()
    //***********************************
    {
        return quantity;
    }

    //***********************************
    public void setQuantity(int quantity)
    //***********************************
    {
        this.quantity = quantity;
    }

    //***********************************
    public String getState()
    //***********************************
    {
        return state;
    }

    //***********************************
    public void setState(String state)
    //***********************************
    {
        this.state = state;
    }

    //***********************************
    public String getCountry()
    //***********************************
    {
        return country;
    }

    //***********************************
    public void setCountry(String country)
    //***********************************
    {
        this.country = country;
    }

    //***********************************
    public Date getDate()
    //***********************************
    {
        return date;
    }

    //***********************************
    public void setDate(Date date)
    //***********************************
    {
        this.date = date;
    }

}
