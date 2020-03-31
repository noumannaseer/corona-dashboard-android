package com.fantech.novoid.work_manager;

import java.util.concurrent.TimeUnit;

import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;

//***********************************************************
public class StatsLoaderWorker extends Scheduler.Worker
//***********************************************************
{

    //***********************************************************
    @Override
    public Disposable schedule(Runnable run, long delay, TimeUnit unit)
    //***********************************************************
    {
        return null;
    }

    //***********************************************************
    @Override
    public void dispose()
    //***********************************************************
    {

    }

    //***********************************************************
    @Override
    public boolean isDisposed()
    //***********************************************************
    {
        return false;
    }
}
