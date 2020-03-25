package com.fantech.covidplus.view_models;

import android.app.Application;
import android.os.AsyncTask;

import com.fantech.covidplus.dao.CoronaDAO;
import com.fantech.covidplus.database.CoronaDatabase;
import com.fantech.covidplus.models.Corona;

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
    public LiveData<List<String>> getCountriesList()
    //*********************************************************************
    {

        return mCoronaDAO.getCountries();
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
        //use the Workermanager. avoid Asynctask for memory leaks.
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

}
