package com.fantech.covidplus.database;

import android.content.Context;

import com.fantech.covidplus.dao.CoronaDAO;
import com.fantech.covidplus.models.Corona;
import com.fantech.covidplus.models.DateConverter;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;


@Database(entities = { Corona.class }, version = 1)
@TypeConverters({ DateConverter.class })
//******************************************************************
public abstract class CoronaDatabase extends RoomDatabase
//******************************************************************
{
    public static CoronaDatabase sInstance;

    //******************************************************************
    public abstract CoronaDAO taskDao();
    //******************************************************************

    //******************************************************************
    // Get a database instance
    public static synchronized CoronaDatabase getDatabaseInstance(Context context)
    //******************************************************************
    {
        if (sInstance == null)
        {
            sInstance = create(context);
        }
        return sInstance;
    }

    // Create the database
    //******************************************************************
    static CoronaDatabase create(Context context)
    //******************************************************************
    {
        //Please move the database name to DB constants.
        RoomDatabase.Builder<CoronaDatabase> builder = Room.databaseBuilder(
                context.getApplicationContext(),
                CoronaDatabase.class, "corona");
        return builder.build();
    }
}