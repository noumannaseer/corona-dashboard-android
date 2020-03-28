package com.fantech.covidplus.dao;

import com.fantech.covidplus.models.Corona;
import com.fantech.covidplus.models.CoronaGraph;
import com.fantech.covidplus.models.CoronaMap;

import java.util.Date;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

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

    @Query("SELECT  country,sum(quantity) as quantity,latitude,longitude,report_type,id from corona where report_type=0  group by country order by country")
    LiveData<List<Corona>> getCountriesDeath();

    @Query("SELECT  country,sum(quantity) as quantity,latitude,longitude,report_type,id from corona where report_type=1  group by country order by country")
    LiveData<List<Corona>> getCountriesConfirmed();


    @Query("SELECT  country,sum(quantity) as quantity,latitude,longitude,report_type,id from corona where report_type=2  group by country order by country")
    LiveData<List<Corona>> getCountriesRecovered();


    @Query("SELECT sum(quantity) from corona where report_type=:type")
    LiveData<Integer> getSum(int type);

    @Query("SELECT sum(quantity) from corona where report_type=:type and country=:country")
    LiveData<Integer> getSum(int type, String country);

    @Query("SELECT sum(quantity) from corona where report_type=:type and country=:country and state=:state")
    LiveData<Integer> getSum(int type, String country, String state);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Corona corona);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<Corona> corona);

    @Query("DELETE FROM corona")
    void nukeTable();

    @Query("select distinct country ,(select count(quantity) from corona where country=country) as quantity,latitude,longitude from corona where report_type=:reportType order by  quantity desc")
    LiveData<List<CoronaMap>> getMapStats(int reportType);

    @Query("select  distinct state from corona where country=:countryName ")
    LiveData<List<String>> getProvinceList(String countryName);

    // Select one task from Task table by id
    @Query("SELECT date,sum(quantity) as quantity FROM corona WHERE date between :startDate AND :endDate GROUP BY date")
    LiveData<List<CoronaGraph>> getLast30DaysRecord(Date startDate, Date endDate);

}
