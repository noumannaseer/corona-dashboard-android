package com.fantech.covidplus.dao;

import com.fantech.covidplus.models.Corona;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

//***********************************************
@Dao
public interface CoronaDAO
//***********************************************
{
    // Select all from Task table and order by "complete by" date
    @Query("SELECT * FROM corona ORDER By quantity")
    LiveData<List<Corona>> getAllTasks();

    // Select one task from Task table by id
    @Query("SELECT * FROM corona WHERE id=:id")
    LiveData<Corona> getTaskById(String id);

    @Insert()
    void insertRow(Corona corona);
}
