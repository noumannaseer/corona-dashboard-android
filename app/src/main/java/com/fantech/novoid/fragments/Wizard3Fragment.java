package com.fantech.novoid.fragments;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.fantech.novoid.R;
import com.fantech.novoid.activities.WizardActivity;
import com.fantech.novoid.databinding.FragmentWizard3Binding;
import com.fantech.novoid.databinding.SelectTimeDialogBinding;
import com.fantech.novoid.utils.AndroidUtil;
import com.fantech.novoid.utils.SharedPreferencesUtils;
import com.fantech.novoid.utils.UIUtils;

import java.util.Calendar;

import androidx.databinding.DataBindingUtil;
import lombok.val;

//***********************************************************************
public class Wizard3Fragment
        extends BaseFragment
//***********************************************************************
{

    private View rootView;
    private FragmentWizard3Binding mBinding;
    private long mTime = 0;


    //*************************************************************************
    public Wizard3Fragment()
    //*************************************************************************
    {

    }

    //***********************************************************************
    @Override
    public View onCreateViewBaseFragment(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState)
    //***********************************************************************
    {
        if (rootView == null)
        {
            mBinding = FragmentWizard3Binding.inflate(inflater, parent, false);
            rootView = mBinding.getRoot();
            initControls();
        }
        return rootView;
    }

    //***********************************************************************
    private void initControls()
    //***********************************************************************
    {
        SharedPreferencesUtils.setValue(SharedPreferencesUtils.IS_ALARM_SET,false);
        mBinding.selectTime.setOnClickListener(view -> showTimeDialog());
        mBinding.next.setOnClickListener(view -> proceedNext());
        val todayCalender = Calendar.getInstance();
        todayCalender.set(Calendar.HOUR_OF_DAY,9);
        todayCalender.set(Calendar.MINUTE,0);
        mTime=todayCalender.getTimeInMillis();
        mBinding.time.setText(UIUtils.getDate(todayCalender.getTimeInMillis(), "hh:mm a"));
    }

    //***********************************************************************
    private void proceedNext()
    //***********************************************************************
    {
        if (mTime == 0)
        {
            UIUtils.displayAlertDialog(
                    AndroidUtil.getString(R.string.please_select_notification_time),
                    AndroidUtil.getString(R.string.notification_time), getActivity(),
                    AndroidUtil.getDrawable(R.drawable.set_notification));
            return;
        }
        SharedPreferencesUtils.setValue(SharedPreferencesUtils.NOTIFICATION_TIME, mTime);
        ((WizardActivity)getActivity()).Next();
    }

    //***********************************************************************
    private void showTimeDialog()
    //***********************************************************************
    {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow()
              .setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        SelectTimeDialogBinding selectTimeDialog = DataBindingUtil.inflate(
                LayoutInflater.from(getActivity()), R.layout.select_time_dialog, null, false);
        dialog.setContentView(selectTimeDialog.getRoot());
        selectTimeDialog.timePicker.setIs24HourView(true);
        selectTimeDialog.selectTime.setOnClickListener(view -> {
            Calendar calendar = Calendar.getInstance();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {

                calendar.set(Calendar.HOUR_OF_DAY, selectTimeDialog.timePicker.getHour());
                calendar.set(Calendar.MINUTE, selectTimeDialog.timePicker.getMinute());
            }
            else
            {
                calendar.set(Calendar.HOUR_OF_DAY, selectTimeDialog.timePicker.getCurrentHour());
                calendar.set(Calendar.MINUTE, selectTimeDialog.timePicker.getCurrentMinute());
            }
            mTime = calendar.getTimeInMillis();
            mBinding.time.setText(UIUtils.getDate(calendar.getTimeInMillis(), "hh:mm a"));
            dialog.dismiss();
        });
        dialog.show();
    }

}
