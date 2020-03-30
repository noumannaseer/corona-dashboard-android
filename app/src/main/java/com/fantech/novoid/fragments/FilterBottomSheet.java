package com.fantech.novoid.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.fantech.novoid.R;
import com.fantech.novoid.databinding.FilterBottomSheetBinding;
import com.fantech.novoid.models.SortingType;
import com.fantech.novoid.utils.AndroidUtil;
import com.fantech.novoid.utils.UIUtils;
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
    private SortingType mSortType;


    //*******************************************************************************
    public FilterBottomSheet(FilterClickListener listener, SortingType sortType)
    //*******************************************************************************
    {
        mListener = listener;
        mSortType = sortType;
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

        switch (mSortType)
        {
        case ASCENDING:
            mBinding.accending.setChecked(true);
            break;
        case DESCENDING:
            mBinding.descending.setChecked(true);
            break;
        case DEATH_CASES:
            mBinding.deathCases.setChecked(true);
            break;
        case CONFIRMED_CASES:
            mBinding.confirmedCases.setChecked(true);
            break;
        case RECOVERED_CASES:
            mBinding.recoverCases.setChecked(true);
            break;
        }
        UIUtils.setRadioExclusiveClick(mBinding.sortType);

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
                mListener.onApplyFilterClick(mDeathRange,
                                             (int)mBinding.sortType.getTag());
                this.dismiss();
            }
        });

    }


    //***********************************************************
    public interface FilterClickListener
            //***********************************************************
    {
        void onApplyFilterClick(int deathRange, int sortingType);
    }

}
