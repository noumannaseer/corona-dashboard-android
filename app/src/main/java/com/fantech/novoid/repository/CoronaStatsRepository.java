package com.fantech.novoid.repository;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.fantech.novoid.R;
import com.fantech.novoid.dao.CoronaDAO;
import com.fantech.novoid.database.CoronaDatabase;
import com.fantech.novoid.models.Corona;
import com.fantech.novoid.network.ServiceCalls;
import com.fantech.novoid.utils.AndroidUtil;
import com.fantech.novoid.utils.Constants;
import com.fantech.novoid.utils.SharedPreferencesUtils;
import com.fantech.novoid.view_models.CoronaStatsViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProviders;
import lombok.val;

//**************************************************
public class CoronaStatsRepository
//**************************************************

{

    private LifecycleOwner mLifeCycleOwner;
    private static CoronaStatsRepository instance;
    private DBDataListener mDbDataListener;
    private int mApiCount = 0;
    private List<Corona> mCoronaList;
    private boolean mIsObserved = false;
    private Calendar mCalender;
    private static final int THREE_API_CALLED = 3;
    private CoronaDAO mCoronaDAO;
    private static final String LOG_REPO="LOG_REPO";

    //**************************************************
    public CoronaStatsRepository(LifecycleOwner lifeCycleOwner, Context application, DBDataListener dbDataListener)
    //**************************************************
    {
        this.mLifeCycleOwner = lifeCycleOwner;
        mDbDataListener=dbDataListener;
        mCoronaDAO = CoronaDatabase.getDatabaseInstance(application)
                                   .taskDao();
    }

    //************************************************************
    public void checkPreviousData()
    //************************************************************
    {

        Log.d(LOG_REPO, "calling observer");


        mCoronaDAO.getAllRecords()
           .observe(mLifeCycleOwner, coronas ->
           {

               Log.d(LOG_REPO, "enter in observer");
               if (mIsObserved)
                   return;
               Log.d(LOG_REPO,"calculating day");
               mIsObserved = true;
               val lastUpdated = SharedPreferencesUtils.getLong(
                       SharedPreferencesUtils.LAST_UPDATED_TIME);
               long diff = System.currentTimeMillis() - lastUpdated;
               int numOfDays = (int)(diff / (1000 * 60 * 60 * 24));
               if (coronas.size() == 0 || lastUpdated == 0 || numOfDays >= 1)
                   loadStats();
               else
               {
                   Log.d(LOG_REPO,"Opening home activity");
                   mCoronaDAO.getAllRecords().removeObservers(mLifeCycleOwner);


                   if (mDbDataListener != null)
                       mDbDataListener.onDataInsertedInDB(true);
               }
           });
    }
    //*********************************************************************
    private void loadStats()
    //*********************************************************************
    {
        Log.d(LOG_REPO,"Stats loading");
        if(!AndroidUtil.isNetworkStatusAvailable())
        {
            AndroidUtil.toast(false,AndroidUtil.getString(R.string.no_internet));
            if (mDbDataListener != null)
                mDbDataListener.onDataInsertedInDB(false);
            return;

        }

        mApiCount = 0;
        mCoronaList = new ArrayList<>();
        mCalender = Calendar.getInstance();
        AsyncTask.execute(() -> mCoronaDAO.nukeTable());
        ServiceCalls.instance()
                    .loadConfirmedStats((isSuccessFull, errorMessage, data) ->
        {
            if (isSuccessFull)
            {
                processData(data, Constants.REPORT_CONFIRMED);
                return;
            }
            AndroidUtil.toast(false, errorMessage);
        });

        ServiceCalls.instance()
                    .loadDeathStats((isSuccessFull, errorMessage, data) ->
        {
            if (isSuccessFull)
            {
                processData(data, Constants.REPORT_DEATH);
                return;
            }
            AndroidUtil.toast(false, errorMessage);
        });
        ServiceCalls.instance()
                    .loadRecoveredStats((isSuccessFull, errorMessage, data) ->
        {
            if (isSuccessFull)
            {
                processData(data, Constants.REPORT_RECOVERED);
                return;
            }
            AndroidUtil.toast(false, errorMessage);
        });

    }


    //*********************************************************************
    private void processData(String data, int type)
    //*********************************************************************
    {
        if (mCoronaList == null)
            mCoronaList = new ArrayList<>();
        val rows = data.split("\\n");
        val columns = rows[0].split(",");
        for (int i = 1; i < rows.length; i++)
        {
            String row = rows[i];
            row = row.replace(", ", " ");
            val rowValues = row.split(",");
            for (int j = 4; j < columns.length; j++)
            {
                Corona corona = new Corona(rowValues, columns, mCalender, type, j);
                mCoronaList.add(corona);
            }
        }
        mApiCount++;
        if (mApiCount == THREE_API_CALLED)
        {
            AsyncTask.execute(() -> mCoronaDAO.insert(mCoronaList));
            SharedPreferencesUtils.setValue(SharedPreferencesUtils.LAST_UPDATED_TIME,
                                            System.currentTimeMillis());
            Log.d(LOG_REPO,"data loaded opening home activity");
            mCoronaDAO.getAllRecords().removeObservers(mLifeCycleOwner);
            if(mDbDataListener!=null)
                mDbDataListener.onDataInsertedInDB(true);
        }
    }
    //************************************************************
    public interface DBDataListener
    //************************************************************
    {
        //************************************************************
        void onDataInsertedInDB(boolean isSuccessfully);
        //************************************************************
    }

}
