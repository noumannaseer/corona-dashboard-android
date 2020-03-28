package com.fantech.covidplus.models;

import java.util.Date;

import androidx.room.Entity;

//**********************************************************
//@Entity(tableName = "Corona")
public class CoronaGraph
//**********************************************************
{
    private int quantity;
    private Date date;

    //**********************************************************
    public CoronaGraph(int quantity, Date date)
    //**********************************************************
    {
        this.quantity = quantity;
        this.date = date;
    }

    //**********************************************************
    public int getQuantity()
    //**********************************************************
    {
        return quantity;
    }

    //**********************************************************
    public void setQuantity(int quantity)
    //**********************************************************
    {
        this.quantity = quantity;
    }

    //**********************************************************
    public Date getDate()
    //**********************************************************
    {
        return date;
    }

    //**********************************************************
    public void setDate(Date date)
    //**********************************************************
    {
        this.date = date;
    }
}
