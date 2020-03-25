package com.fantech.covidplus.dao;

import com.fantech.covidplus.models.Corona;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import retrofit2.http.DELETE;

//***********************************************
@Dao
public interface CoronaDAO
//***********************************************
{
    // Select all from Task table and order by "complete by" date
    @Query("SELECT * FROM corona ORDER By quantity")
    LiveData<List<Corona>> getAllRecords();

    // Select one task from Task table by id
    @Query("SELECT * FROM corona WHERE id=:id")
    LiveData<Corona> getRecordById(String id);

    @Query("SELECT distinct country from corona")
    LiveData<List<String>> getCountries();

    @Query("SELECT sum(quantity) from corona where report_type=:type")
    LiveData<Integer> getSum(int type);

    @Query("SELECT sum(quantity) from corona where report_type=:type and country=:country")
    LiveData<Integer> getSum(int type, String country);


    @Insert()
    void insert(Corona corona);

    @Insert()
    void insert(List<Corona> corona);

    @Query("DELETE FROM corona")
    void nukeTable();
}
