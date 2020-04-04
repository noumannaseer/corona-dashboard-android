package com.fantech.novoid.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.text.TextUtils;

import com.fantech.novoid.R;
import com.fantech.novoid.activities.CountryStatsActivity;
import com.fantech.novoid.database.CoronaDatabase;
import com.fantech.novoid.models.CountryReader;

import androidx.core.app.NotificationCompat;
import lombok.val;

//******************************************************
public class AlertReceiver
        extends BroadcastReceiver
//******************************************************
{

    //******************************************************
    @Override
    public void onReceive(Context context, Intent intent)
    //******************************************************
    {
        val countryName = CountryReader.getUserCountry(context);
        sendNotification(countryName, context);
    }

    //******************************************************
    private void sendNotification(String countryName, Context context)
    //******************************************************
    {

        if (TextUtils.isEmpty(countryName))
            return;

        AsyncTask.execute(() -> {
            val mCoronaDAO = CoronaDatabase.getDatabaseInstance(context)
                                           .taskDao();
            val deaths = mCoronaDAO.getSumSynchronous(Constants.REPORT_DEATH, countryName);
            val confirmed = mCoronaDAO.getSumSynchronous(Constants.REPORT_CONFIRMED,
                                                         countryName);
            val recovered = mCoronaDAO.getSumSynchronous(Constants.REPORT_RECOVERED,
                                                         countryName);
            if (deaths == null || confirmed == null || recovered == null)
                return;
            Intent countryDetailIntent = new Intent(
                    context,
                    CountryStatsActivity.class);
            countryDetailIntent.putExtra(
                    CountryStatsActivity.COUNTRY_NAME,
                    countryName);
            String stats = AndroidUtil.getString(
                    R.string.stats_notification,
                    deaths, confirmed, recovered);
            createNotification(context,
                               AndroidUtil.getString(
                                       R.string.today_stats),
                               stats,
                               countryDetailIntent);
        });

    }

    //*****************************************************************************************
    private void createNotification(Context context, String title, String body, Intent intent)
    //*****************************************************************************************
    {

        NotificationManager notificationManager = (NotificationManager)context.getSystemService(
                Context.NOTIFICATION_SERVICE);
        int notificationId = 1;
        String channelId = "channel-01sas";
        String channelName = "Channel Namesasasasasasa";
        int importance = NotificationManager.IMPORTANCE_HIGH;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
        {
            NotificationChannel mChannel = new NotificationChannel(
                    channelId, channelName, importance);
            notificationManager.createNotificationChannel(mChannel);
        }
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, channelId)
                .setContentTitle(title)
                .setContentText(body);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            mBuilder.setSmallIcon(R.drawable.single_color_noviod);
            mBuilder.setColor(context.getResources()
                                     .getColor(android.R.color.black));
        }
        else
        {
            mBuilder.setSmallIcon(R.drawable.novoid);
        }
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntentWithParentStack(intent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
                0,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
        mBuilder.setContentIntent(resultPendingIntent);

        mBuilder.setAutoCancel(true);
        notificationManager.notify(notificationId, mBuilder.build());
    }

}
