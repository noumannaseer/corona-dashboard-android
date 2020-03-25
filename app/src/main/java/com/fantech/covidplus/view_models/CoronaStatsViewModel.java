package com.fantech.covidplus.view_models;

import android.app.Application;

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
    public LiveData<List<Corona>> getTasks()
    //****************************************************************
    {
        return mCoronaDAO.getAllTasks();
    }

    //****************************************************************
    public void insertRow(Corona corona)
    //****************************************************************
    {
        mCoronaDAO.insertRow(corona);
    }
}
