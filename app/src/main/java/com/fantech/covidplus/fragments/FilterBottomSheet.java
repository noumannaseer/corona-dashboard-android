package com.fantech.covidplus.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.fantech.covidplus.R;
import com.fantech.covidplus.databinding.FilterBottomSheetBinding;
import com.fantech.covidplus.utils.AndroidUtil;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import lombok.val;

//*******************************************************************************
public class FilterBottomSheet
        extends BottomSheetDialogFragment
//*******************************************************************************
{


    private FilterClickListener mListener;
    private FilterBottomSheetBinding mBinding;
    private View rootView;
    private int mDeathRange = 100;
    private boolean mIsAscending = true;


    //*******************************************************************************
    public FilterBottomSheet(FilterClickListener listener, boolean isAscending)
    //*******************************************************************************
    {
        mListener = listener;
        mIsAscending = isAscending;
    }


    //********************************************************************************************************************
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    //********************************************************************************************************************
    {
        if (rootView == null)
        {
            mBinding = FilterBottomSheetBinding.inflate(inflater, container, false);
            rootView = mBinding.getRoot();
        }
        initControls();

        return rootView;
    }


    //********************************************************************************************************************
    private void initControls()
    //********************************************************************************************************************
    {
        val status = AndroidUtil.getString(R.string.low);
        if (mIsAscending)
            mBinding.accending.setChecked(true);
        else
            mBinding.descending.setChecked(true);
        mBinding.status.setText(status);

        mBinding.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                String status = "";
                if (progress >= 0 && progress <= 33)
                {
                    progress = 0;
                    status = AndroidUtil.getString(R.string.low);
                    mDeathRange = 100;

                }
                else if (progress > 33 && progress <= 66)
                {
                    progress = 33;
                    status = AndroidUtil.getString(R.string.meduim);
                    mDeathRange = 500;

                }
                else if (progress > 66 && progress <= 80)
                {
                    progress = 66;
                    status = AndroidUtil.getString(R.string.high);
                    mDeathRange = 5000;
                }
                else
                {
                    progress = 100;
                    status = AndroidUtil.getString(R.string.all_record);
                    mDeathRange = 0;
                }
                mBinding.status.setText(status);
                seekBar.setProgress(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {

            }
        });
        mBinding.applyFilter.setOnClickListener(view -> {
            if (mListener != null)
            {
                mListener.onApplyFilterClick(mDeathRange, mBinding.accending.isChecked());
                this.dismiss();
            }
        });

    }


    //***********************************************************
    public interface FilterClickListener
            //***********************************************************
    {
        void onApplyFilterClick(int deathRange, boolean isAscending);
    }

}
