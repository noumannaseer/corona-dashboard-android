package com.fantech.novoid.view_models;

import android.app.Application;
import android.os.AsyncTask;

import com.fantech.novoid.dao.CoronaDAO;
import com.fantech.novoid.database.CoronaDatabase;
import com.fantech.novoid.models.Corona;
import com.fantech.novoid.models.CoronaGraph;
import com.fantech.novoid.models.CoronaMap;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

//****************************************************************
public class CoronaStatsViewModel
        extends AndroidViewModel
//****************************************************************
{
    private CoronaDAO mCoronaDAO;

    //****************************************************************
    public CoronaStatsViewModel(@NonNull Application application)
    //****************************************************************
    {
        super(application);
        mCoronaDAO = CoronaDatabase.getDatabaseInstance(application)
                                   .taskDao();
    }

    //****************************************************************
    public LiveData<List<Corona>> getRecords()
    //****************************************************************
    {
        return mCoronaDAO.getAllRecords();
    }

    //*********************************************************************
    public LiveData<List<Corona>> getCountriesListDeath()
    //*********************************************************************
    {

        return mCoronaDAO.getCountriesDeath();
    }

    //*********************************************************************
    public LiveData<List<Corona>> getCountriesListRecovered()
    //*********************************************************************
    {

        return mCoronaDAO.getCountriesRecovered();
    }

    //*********************************************************************
    public LiveData<List<Corona>> getCountriesListConfirmed()
    //*********************************************************************
    {

        return mCoronaDAO.getCountriesConfirmed();
    }


    //****************************************************************
    public void insert(final Corona corona)
    //****************************************************************
    {
        AsyncTask.execute(new Runnable()
        {
            @Override
            public void run()
            {
                mCoronaDAO.insert(corona);
            }
        });
    }


    //****************************************************************
    public void insert(final List<Corona> statsList)
    //****************************************************************
    {
        AsyncTask.execute(new Runnable()
        {
            @Override
            public void run()
            {
                mCoronaDAO.insert(statsList);
            }
        });
    }

    //****************************************************************
    public void clearDB()
    //****************************************************************
    {
        AsyncTask.execute(new Runnable()
        {
            @Override
            public void run()
            {
                mCoronaDAO.nukeTable();
            }
        });
    }

    //*********************************************************************
    public LiveData<Integer> countSum(int type)
    //*********************************************************************
    {
        return mCoronaDAO.getSum(type);
    }

    //*********************************************************************
    public LiveData<Integer> countSum(int type, String countryName)
    //*********************************************************************
    {
        return mCoronaDAO.getSum(type, countryName);
    }

    //*********************************************************************
    public LiveData<List<CoronaMap>> getMapViewList(int reportType)
    //*********************************************************************
    {
        return mCoronaDAO.getMapStats(reportType);
    }

    //*********************************************************************
    public LiveData<List<String>> getProvinceList(String countryName)
    //*********************************************************************
    {
        return mCoronaDAO.getProvinceList(countryName);
    }

    //*********************************************************************
    public LiveData<Integer> getProvinceStat(int type, String countryName, String state)
    //*********************************************************************
    {
        return mCoronaDAO.getSum(type, countryName, state);
    }

    //*********************************************************************
    public LiveData<List<CoronaGraph>> getLast30DaysRecord(Date currentDate,int reportType)
    //*********************************************************************
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(currentDate.getTime());
        calendar.add(Calendar.DAY_OF_MONTH, -30);
        return mCoronaDAO.getLast30DaysRecord(calendar.getTime(), currentDate,reportType);
    }
}
