package com.fantech.covidplus;

import android.os.Bundle;
import android.util.Log;

import com.fantech.covidplus.models.Corona;
import com.fantech.covidplus.network.ServiceGenerator;
import com.fantech.covidplus.others.Constants;
import com.fantech.covidplus.others.UIUtils;
import com.fantech.covidplus.services.CoronaServices;
import com.fantech.covidplus.view_models.CoronaStatsViewModel;

import java.io.Console;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import lombok.SneakyThrows;
import lombok.val;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity
        extends AppCompatActivity
{

    CoronaStatsViewModel mStatsViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initControls();
    }

    private void initControls()
    {
        initViewModel();

        val service = ServiceGenerator.createService(CoronaServices.class, Constants.DETEH_URL);
        val getDeathStats = service.getConfirmStats();
        getDeathStats.enqueue(new Callback<ResponseBody>()
        {
            @SneakyThrows
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response)
            {
                String a = response.body()
                                   .string();
                processData(a, 0);
                Log.d("stats", a);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t)
            {
            }
        });

    }

    private void processData(String data, int type)
    {
        val rows = data.split("\\n");
        val columns = rows[0].split(",");
        for (int dateIndex = 4; dateIndex < columns.length; dateIndex++)
        {
            Corona corona = new Corona();
            corona.setDate(UIUtils.getDateFromString(columns[dateIndex], "mm/dd/yyyy"));
            for (int rowIndex = 1; rowIndex < rows.length; rowIndex++)
            {
                String row = rows[rowIndex];
                val rowValues = row.split(",");
                corona.setState(rowValues[0]);
                corona.setCountry(rowValues[1]);
                corona.setLatitude(rowValues[2]);
                corona.setLongitude(rowValues[3]);
                corona.setQuantity(Integer.parseInt(rowValues[dateIndex]));
                corona.setReport_type(type);
            }
        }

    }

    private void initViewModel()
    {
        mStatsViewModel = ViewModelProviders.of(this)
                                            .get(CoronaStatsViewModel.class);

    }
}
