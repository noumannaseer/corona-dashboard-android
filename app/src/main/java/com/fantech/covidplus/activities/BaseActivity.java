package com.fantech.covidplus.activities;

import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.fantech.covidplus.R;
import com.fantech.covidplus.utils.ThemeUtils;

import androidx.annotation.LayoutRes;
import androidx.annotation.MainThread;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import lombok.NonNull;


//****************************************************
public abstract class BaseActivity
        extends AppCompatActivity
//****************************************************
{

    private Dialog mDialog;

    //********************************************************************
    @Override
    protected final void onCreate(@Nullable Bundle savedInstanceState)
    //********************************************************************
    {
        ThemeUtils.applyTheme(this);
        super.onCreate(savedInstanceState);
        onCreation(savedInstanceState);

    }

    //********************************************************************
    protected void showLoadingDialog()
    //********************************************************************
    {
        if (mDialog == null)
        {
            mDialog = new Dialog(this, R.style.CustomTransparentDialog);
            mDialog.setContentView(R.layout.progress_dialog);
            mDialog.setCancelable(false);
            mDialog.setCanceledOnTouchOutside(false);
        }
        mDialog.show();
    }

    //********************************************************************
    protected void showLoadingDialog(@NonNull String loadingMessage)
    //********************************************************************
    {
        if (mDialog == null)
        {
            mDialog = new Dialog(this, R.style.CustomTransparentDialog);
            mDialog.setContentView(R.layout.progress_dialog);
            mDialog.setCancelable(false);
            mDialog.setCanceledOnTouchOutside(false);
        }
        ImageView image = mDialog.findViewById(R.id.splash_icon);
        ObjectAnimator animation = ObjectAnimator.ofFloat(image, "rotationY", 0.0f, 360f);
        animation.setDuration(800);
        animation.setRepeatCount(ObjectAnimator.INFINITE);
        animation.setInterpolator(new AccelerateDecelerateInterpolator());
        animation.start();
        TextView textView = mDialog.findViewById(R.id.loading_text);
        textView.setText(loadingMessage);

        mDialog.show();
    }

    //********************************************************************
    protected void hideLoadingDialog()
    //********************************************************************
    {
        mDialog.dismiss();
    }

    /**
     * All the activity should override this.
     *
     * @param savedInstanceState
     */
    //********************************************************************
    protected abstract void onCreation(@Nullable Bundle savedInstanceState);
    //********************************************************************

    /**
     * have your base layout here, inflate/add on top of your base layout
     *
     * @param layoutResID
     */
    //********************************************************************
    @Override
    public void setContentView(@LayoutRes int layoutResID)
    {
        super.setContentView(layoutResID);
    }
    //********************************************************************

    /**
     * have your base layout here, inflate/add on top of your base layout
     *
     * @param view
     */
    //*************************************************************
    @Override
    public void setContentView(View view)
    //*************************************************************
    {
        super.setContentView(view);
    }

    /**
     * Method 1:
     * This has to be called when theme is changed in the activity.This internally recreated the activity
     */
    //*************************************************************
    public void reCreate()
    //*************************************************************
    {
        Bundle savedInstanceState = new Bundle();
        //this is important to save all your open states/fragment states
        onSaveInstanceState(savedInstanceState);
        //this is needed to release the resources
        super.onDestroy();

        //call on create where new theme is applied
        onCreate(
                savedInstanceState);//you can pass bundle arguments to skip your code/flows on this scenario
    }

    /**
     * Method 2:
     * This has to be called when theme is changed in the activity.This internally restarts the activity
     * This gives you the flicker
     */
    //*************************************************************
    public void restartActivity()
    //*************************************************************
    {
        Intent i = getIntent();
        this.overridePendingTransition(0, 0);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        this.finish();
        //restart the activity without animation
        this.overridePendingTransition(0, 0);
        this.startActivity(i);
    }


    // ******************************************************************
    @MainThread
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    // ******************************************************************
    {
        switch (item.getItemId())
        {
        case android.R.id.home:
            // go to previous screen when app icon in action bar is clicked
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}