package com.fantech.covidplus.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.TextView;
import android.widget.Toast;

import com.fantech.covidplus.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import androidx.annotation.Nullable;
import androidx.annotation.UiThread;
import androidx.fragment.app.FragmentActivity;
import lombok.NonNull;
import lombok.val;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static android.content.Context.MODE_PRIVATE;
import static com.fantech.covidplus.utils.AndroidUtil.getApplicationContext;


//*****************************************************************
public class UIUtils
//*****************************************************************
{


    public static final String USER_REMEMBER = "USER_REMEMBER";
    public static final String USER_NAME = "USER_NAME";
    public static final String PREF_KEY_FILE_NAME = "SwimPro";
    public static final String INTRO_LOADED = "INTRO_LOADED";
    public static final String CURRENT_LANGUAGE = "CURRENT_LANGUAGE";
    public static final String PACKAGE_STATUS = "PACKAGE_STATUS";


    //*****************************************************************
    public static void setPackageStatus(boolean status)
    //*****************************************************************
    {
        SharedPreferences sharedPreferences = getApplicationContext()
                .getSharedPreferences(PREF_KEY_FILE_NAME,
                                      MODE_PRIVATE);
        // Writing data to SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(PACKAGE_STATUS, status);
        editor.commit();
    }

    //*****************************************************************
    public static boolean getPackageStatus()
    //*****************************************************************
    {
        SharedPreferences sharedPreferences = getApplicationContext()
                .getSharedPreferences(PREF_KEY_FILE_NAME,
                                      MODE_PRIVATE);
        boolean value = sharedPreferences.getBoolean(PACKAGE_STATUS, false);
        return value;

    }


    //*****************************************************************
    public static RequestBody getMultiPartForm(@NonNull String value)
    //*****************************************************************
    {
        RequestBody body = RequestBody.create(MediaType.parse("text/plain"), value);
        return body;
    }




  /*  //*****************************************************************
    public static String getMimeType(String url)
    //*****************************************************************
    {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null)
        {
            type = MimeTypeMap.getSingleton()
                              .getMimeTypeFromExtension(extension);
        }
        return type;
    }*/

    public static String getMimeType(Uri uri, Context context)
    {
        String mimeType = null;
        if (uri.getScheme()
               .equals(ContentResolver.SCHEME_CONTENT))
        {
            ContentResolver cr = context.getContentResolver();
            mimeType = cr.getType(uri);
        }
        else
        {
            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri
                                                                               .toString());
            mimeType = MimeTypeMap.getSingleton()
                                  .getMimeTypeFromExtension(
                                          fileExtension.toLowerCase());
        }
        return mimeType;
    }

    //*****************************************************************
    private static MultipartBody.Part prepareFilePart(String partName, Uri fileUri, Activity activity)
    //*****************************************************************
    {
        val file = new File(fileUri.getPath());
        RequestBody requestFile =
                RequestBody.create(
                        MediaType.parse(getMimeType(fileUri, activity)),
                        file
                );
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }


    //****************************************************************
    public static void setCurrentLanguage(boolean status)
    //****************************************************************
    {
        SharedPreferences sharedPreferences = getApplicationContext()
                .getSharedPreferences(PREF_KEY_FILE_NAME,
                                      MODE_PRIVATE);
        // Writing data to SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(CURRENT_LANGUAGE, status);
        editor.commit();
    }

    //****************************************************************
    public static @Nullable
    boolean getCurrentLanguage()
    //****************************************************************
    {
        SharedPreferences sharedPreferences = getApplicationContext()
                .getSharedPreferences(PREF_KEY_FILE_NAME,
                                      MODE_PRIVATE);
        boolean value = sharedPreferences.getBoolean(CURRENT_LANGUAGE, false);
        return value;
    }

    //*****************************************************************
    public static String getText(TextView textView)
    //*****************************************************************

    {
        val data = textView.getText()
                           .toString();
        if (TextUtils.isEmpty(data))
        {
            textView.setError(AndroidUtil.getString(R.string.required));
            return null;
        }
        return textView.getText()
                       .toString();
    }

    //*****************************************************************
    public static Date getDateFromString(@NonNull String dateString, @NonNull String formatter)
    //*****************************************************************
    {
        String dtStart = dateString;
        SimpleDateFormat format = new SimpleDateFormat(formatter);
        try
        {
            Date date = format.parse(dtStart);
            return date;
        }
        catch (ParseException e)
        {
            e.printStackTrace();
            return null;
        }
    }


    //**************************************************************************************************
    public static boolean isVideoFile(String path)
    //**************************************************************************************************
    {
        String mimeType = URLConnection.guessContentTypeFromName(path);
        return mimeType != null && mimeType.startsWith("video");
    }


    //*****************************************************************
    public static String getCurrentDate()
    //*****************************************************************
    {
        String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        return date;
    }

    //*****************************************************************
    public static String getCurrentDateWithDash()
    //*****************************************************************
    {
        String date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        return date;
    }


    //*****************************************************************
    public static String addOneDayCalendar(String inputDate, int i)
    //*****************************************************************

    {

        String date = getCurrentDateWithDash();
        if (!TextUtils.isEmpty(inputDate))
        {
            date = inputDate;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Calendar c = Calendar.getInstance();
        try
        {
            c.setTime(sdf.parse(date));
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        c.add(Calendar.DATE, i);
        return sdf.format(c.getTime());
    }

    //*****************************************************************
    public static String addOneMonthCalendar(String inputDate, int i)
    //*****************************************************************
    {

        String date = getCurrentDate();
        if (!TextUtils.isEmpty(inputDate))
        {
            date = inputDate;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Calendar c = Calendar.getInstance();
        try
        {
            c.setTime(sdf.parse(date));
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        c.add(Calendar.MONTH, i);
        return sdf.format(c.getTime());
    }


    //*****************************************************************
    public static String addOneWeekCalendar(int i)
    //*****************************************************************
    {

        String date = getCurrentDate();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Calendar c = Calendar.getInstance();
        try
        {
            c.setTime(sdf.parse(date));
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        c.add(Calendar.WEEK_OF_MONTH, i);
        return sdf.format(c.getTime());
    }

    //****************************************************************
    public static void setUserRemember(boolean status)
    //****************************************************************
    {
        SharedPreferences sharedPreferences = getApplicationContext()
                .getSharedPreferences(PREF_KEY_FILE_NAME,
                                      MODE_PRIVATE);
        // Writing data to SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(USER_REMEMBER, status);
        editor.commit();
    }

    //****************************************************************
    public static @Nullable
    boolean getUserRemember()
    //****************************************************************
    {
        SharedPreferences sharedPreferences = getApplicationContext()
                .getSharedPreferences(PREF_KEY_FILE_NAME,
                                      MODE_PRIVATE);
        boolean value = sharedPreferences.getBoolean(USER_REMEMBER, false);
        return value;
    }

    //****************************************************************
    public static void setUserName(String userName)
    //****************************************************************
    {
        SharedPreferences sharedPreferences = getApplicationContext()
                .getSharedPreferences(PREF_KEY_FILE_NAME,
                                      MODE_PRIVATE);
        // Writing data to SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_NAME, userName);
        editor.commit();
    }


    //*****************************************************************
    public static int getTotalWorked(long start, long end)
    //*****************************************************************
    {
        final int MILLI_TO_HOUR = 1000 * 60;
        return (int)(end - start) / MILLI_TO_HOUR;
    }


    //*****************************************************************
    public static long getDaysDifference(long start, long end)
    //*****************************************************************
    {


        long timeDifference = start - end;
        long days = timeDifference / (1000 * 60 * 60 * 24);
        long hours = timeDifference / (1000 * 60 * 60) - (days * 24);

        return days;
    }


    //*****************************************************************
    public static int getTimeDifferenceInHours(long start, long end)
    //*****************************************************************
    {
        long timeDifference = start - end;
        long days = timeDifference / (1000 * 60 * 60 * 24);
        long hours = timeDifference / (1000 * 60 * 60) - (days * 24);
        long minutes = timeDifference / (1000 * 60) - (days * 24 * 60) - (hours * 60);

        Log.d("time", days + " " + hours + " " + minutes);

        if (hours != 0)
        {
            return Integer.parseInt("" + (minutes + (hours * 60)));
        }
        else
        {
            return Integer.parseInt("" + minutes);
        }

    }

    //****************************************************************
    public static @Nullable
    String getUserName()
    //****************************************************************
    {
        SharedPreferences sharedPreferences = getApplicationContext()
                .getSharedPreferences(PREF_KEY_FILE_NAME,
                                      MODE_PRIVATE);
        String value = sharedPreferences.getString(USER_NAME, null);
        return value;
    }


    //****************************************************************
    public static @Nullable
    boolean getIntroLoaded()
    //****************************************************************
    {
        SharedPreferences sharedPreferences = getApplicationContext()
                .getSharedPreferences(PREF_KEY_FILE_NAME,
                                      MODE_PRIVATE);
        boolean value = sharedPreferences.getBoolean(INTRO_LOADED, false);
        return value;
    }


    //****************************************************************
    public static void setIntroLoaded(boolean value)
    //****************************************************************
    {

        SharedPreferences sharedPreferences = getApplicationContext()
                .getSharedPreferences(PREF_KEY_FILE_NAME,
                                      MODE_PRIVATE);
        // Writing data to SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(INTRO_LOADED, value);
        editor.commit();

    }

    //******************************************************************
    public static boolean isValidEmailId(String email)
    //******************************************************************
    {

        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                                       + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                                       + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                                       + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                                       + "[0-9]{1,2}|-25[0-5]|2[0-4][0-9])){1}|"
                                       + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$")
                      .matcher(email)
                      .matches();
    }


    public static int getRandomNumber(int max)
    {
        return new Random().nextInt(max);
    }


    //*****************************************************************
    public static void sendEmail(FragmentActivity activity, String email)
    //*****************************************************************
    {
        final Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("plain/text");
        emailIntent.putExtra(Intent.EXTRA_EMAIL,
                             new String[] { email });
        emailIntent.putExtra(Intent.EXTRA_SUBJECT,
                             "Email Subject");
        emailIntent.putExtra(Intent.EXTRA_TEXT,
                             "Email Body");
        activity.startActivity(Intent.createChooser(
                emailIntent, "Send mail..."));

    }


    //*****************************************************************
    public static String filterCategory(String selectedCategory)
    //*****************************************************************
    {
        return TextUtils.isEmpty(selectedCategory) ? "" : "[" + selectedCategory + "]";
    }


    //*****************************************************************
    public interface BitmapLoadInterface
            //*****************************************************************
    {
        void onBitmapLoaded(Bitmap bitmap);
    }


    //*********************************************************************
    public static void handleFailure(Throwable t, Context context)
    //*********************************************************************
    {
        UIUtils.displayAlertDialog(t.getLocalizedMessage(), AndroidUtil.getString(R.string.error),
                                   context);
    }

    public static boolean getGPSStatus(Context context)
    {
        val mLocationManager = (LocationManager)context.getSystemService(
                Context.LOCATION_SERVICE);
        return mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    //******************************************************************
    public static void displayAlertDialog(String message, String title, Context context)
    //******************************************************************
    {
        new AlertDialog.Builder(context)
                .setMessage(message)
                .setTitle(title)
                .setCancelable(false)
                .setPositiveButton(R.string.ok, null)
                .create()
                .show();
    }

    //*********************************************************************
    public static void openLinkInBrowser(@Nullable String link)
    //*********************************************************************
    {
        if (TextUtils.isEmpty(link))
            return;
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.setData(Uri.parse(link));
        getApplicationContext()
                .startActivity(i);

    }


    //*************************************************
    private static String readJsonFile(InputStream inputStream)
    //*************************************************
    {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        byte[] bufferByte = new byte[1024];
        int length;
        try
        {
            while ((length = inputStream.read(bufferByte)) != -1)
            {
                outputStream.write(bufferByte, 0, length);
            }
            outputStream.close();
            inputStream.close();
        }
        catch (IOException e)
        {

        }
        return outputStream.toString();
    }


    //******************************************************************
    public static void displayAlertDialog(String message, String title, Context context, String positive, DialogInterface.OnClickListener listerner, Drawable alertIcon)
    //******************************************************************
    {

        new AlertDialog.Builder(context)
                .setMessage(message)
                .setTitle(title)
                .setIcon(alertIcon)
                .setPositiveButton(positive, listerner)
                .create()
                .show();
    }

    //******************************************************************
    public static void displayAlertDialog(String message, String title, Context context, String positive, DialogInterface.OnClickListener listerner)
    //******************************************************************
    {
        new AlertDialog.Builder(context)
                .setMessage(message)
                .setTitle(title)
                //.setNegativeButton("No", listerner)
                .setPositiveButton(positive, listerner)
                .create()
                .show();
    }


    //******************************************************************
    public static void displayAlertDialog(String message, String title, Context context, String positive, String negative, DialogInterface.OnClickListener listerner)
    //******************************************************************
    {

        new AlertDialog.Builder(context)
                .setMessage(message)
                .setTitle(title)
                .setNegativeButton(negative, listerner)
                .setCancelable(false)
                .setPositiveButton(positive, listerner)
                .create()
                .show();
    }

    //******************************************************************
    public static void displayAlertDialog(String message, String title, Context context, String positive, String negative, DialogInterface.OnClickListener listerner, Drawable icon)
    //******************************************************************
    {

        new AlertDialog.Builder(context)
                .setMessage(message)
                .setTitle(title)
                .setIcon(icon)
                .setNegativeButton(negative, listerner)
                .setCancelable(false)
                .setPositiveButton(positive, listerner)
                .create()
                .show();
    }


    //*****************************************************************
    public interface CheckBoxSingleItemListener
            //*****************************************************************
    {
        void onItemSelected(int itemIndex);
    }

    //*****************************************************************
    public static String getAppVersion()
    //*****************************************************************
    {
        try
        {
            PackageInfo pInfo = getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(getApplicationContext()
                                            .getPackageName(), 0);
            String version = pInfo.versionName;

            return version;
        }
        catch (PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();
            return "0";
        }

    }


    /**
     * Toast a message.
     *
     * @param longToast Should the toast be a long one?
     * @param message   Message to toast.
     */
    //******************************************************************
    @UiThread
    public static void testToast(boolean longToast,
                                 @NonNull String message)
    //******************************************************************
    {

        Toast
                .makeText(getApplicationContext(), message,
                          longToast ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT)
                .show();
    }


    //*********************************************************************
    private void printKeyHash(Activity activity)
    //*********************************************************************
    {
        // Add code to print out the key hash
        try
        {
            PackageInfo info = activity.getPackageManager()
                                       .getPackageInfo("com.example.editme",
                                                       PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures)
            {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        }
        catch (PackageManager.NameNotFoundException e)
        {
            Log.e("KeyHash:", e.toString());
        }
        catch (NoSuchAlgorithmException e)
        {
            Log.e("KeyHash:", e.toString());
        }
    }

    //*************************************************************
    public static String getTimeFromDate(Date timestamp)
    //*************************************************************
    {
        SimpleDateFormat localDateFormat = new SimpleDateFormat("HH:mm");
        String time = localDateFormat.format(timestamp);
        return time;
    }

    //*************************************************************
    public static String getDate(long timestamp)
    //*************************************************************
    {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(timestamp);
        String date = DateFormat.format("dd MMM yyyy", cal)
                                .toString();
        return date;
    }

    //*************************************************************
    public static String getDate(long timestamp, String format)
    //*************************************************************
    {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(timestamp);
        String date = DateFormat.format(format, cal)
                                .toString();
        return date;
    }


    //*********************************************************************
    public static void startSmsIntent(@NonNull String body, @NonNull String number, Context context)
    //*********************************************************************
    {

        Intent intentSms = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + number));
        intentSms.putExtra("sms_body", body);
        context.startActivity(intentSms);
    }

    //******************************************************************
    public static boolean isValidMessage(String email)
    //******************************************************************
    {

        return !Pattern.compile("\\s*")
                       .matcher(email)
                       .matches();
    }

    public static void composeEmail(String addresses, String subject, String body, Context context)
    {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:" + addresses)); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, body);

        if (intent.resolveActivity(context.getPackageManager()) != null)
        {
            context.startActivity(intent);
        }
    }


    //**********************************************************************************
    private static long getUnitBetweenDates(Date startDate, Date endDate, TimeUnit unit)
//**********************************************************************************
    {

        long timeDiff = endDate.getTime() - startDate.getTime();
        return unit.convert(timeDiff, TimeUnit.MILLISECONDS);
    }

    public static int getDaysBetweenDates(String start, String end)
//**********************************************************************************
    {

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        Date startDate, endDate;
        long numberOfDays = 0;
        try
        {
            startDate = dateFormat.parse(start);
            endDate = dateFormat.parse(end);
            numberOfDays = getUnitBetweenDates(startDate, endDate, TimeUnit.DAYS);

        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }


        return (int)numberOfDays;

    }


    //*****************************************************
    public interface CheckBoxMultipleItemListener
            //*****************************************************
    {
        void onItemsSelected(List<Integer> itemsIndex);

    }

    //*****************************************************
    public static String fileType(Uri uri)
    //*****************************************************
    {
        ContentResolver cR = getApplicationContext()
                .getContentResolver();
        return cR.getType(uri);
    }

    public static String getColoredSpanned(String text, String color)
    {
        String input = "<font color=" + color + ">" + text + "</font>";
        return input;
    }

    //**************************************************************************************************
    public static void underlineTextView(TextView textView)
    //**************************************************************************************************
    {
        textView.setPaintFlags(
                textView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
    }


    //*****************************************************
    public static String getFileName(Uri uri)
    //*****************************************************
    {
        String result = null;
        if (uri.getScheme()
               .equals("content"))
        {
            Cursor cursor = getApplicationContext()
                    .getContentResolver()
                    .query(uri, null, null, null, null);
            try
            {
                if (cursor != null && cursor.moveToFirst())
                {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            }
            finally
            {
                cursor.close();
            }
        }
        if (result == null)
        {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1)
            {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    public static Bitmap retriveVideoFrameFromVideo(String videoPath)
    {
        Bitmap bitmap = null;
        MediaMetadataRetriever mediaMetadataRetriever = null;
        try
        {
            mediaMetadataRetriever = new MediaMetadataRetriever();
            if (Build.VERSION.SDK_INT >= 14)
                mediaMetadataRetriever.setDataSource(videoPath, new HashMap<String, String>());
            else
                mediaMetadataRetriever.setDataSource(videoPath);
            //   mediaMetadataRetriever.setDataSource(videoPath);
            bitmap = mediaMetadataRetriever.getFrameAtTime(1,
                                                           MediaMetadataRetriever.OPTION_CLOSEST);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (mediaMetadataRetriever != null)
            {
                mediaMetadataRetriever.release();
            }
        }
        return bitmap;
    }


    //***************************************************
    public static void hideKeyboard(Activity activity)
    //***************************************************
    {
        InputMethodManager imm = (InputMethodManager)activity.getSystemService(
                Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null)
        {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}

