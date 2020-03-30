package com.fantech.novoid.adapters;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fantech.novoid.R;
import com.fantech.novoid.databinding.ListViewProvinceStatsBinding;
import com.fantech.novoid.utils.Constants;
import com.fantech.novoid.utils.UIUtils;
import com.fantech.novoid.view_models.CoronaStatsViewModel;

import java.util.List;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import lombok.NonNull;
import lombok.val;

//**********************************************************
public class ProvinceListAdapter
        extends RecyclerView.Adapter<ProvinceListAdapter.ViewHolder>
//**********************************************************
{
    private List<String> mCountriesList;
    private CountriesListAdapter.CountryClickListener mCountryClickListener;
    private CoronaStatsViewModel mCoronaStatsViewModel;
    private String mCountryName;
    private Fragment mFragment;

    //******************************************************************
    public ProvinceListAdapter(List<String> mCountriesList, CountriesListAdapter.CountryClickListener mCountryClickListener, Fragment fragment, String countryName)
    //******************************************************************
    {
        this.mCountriesList = mCountriesList;
        this.mCountryClickListener = mCountryClickListener;
        mCoronaStatsViewModel = ViewModelProviders.of(fragment)
                                                  .get(CoronaStatsViewModel.class);
        mCountryName = countryName;
        mFragment = fragment;
    }

    //**********************************************
    @Override
    public int getItemViewType(int position)
    //**********************************************
    {
        return position;
    }

    //**********************************************
    @Override
    public long getItemId(int position)
    //**********************************************
    {
        return super.getItemId(position);
    }

    //**********************************************
    @androidx.annotation.NonNull
    @Override
    public ViewHolder onCreateViewHolder(@androidx.annotation.NonNull ViewGroup parent, int viewType)
    //**********************************************
    {
        ListViewProvinceStatsBinding mBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()), R.layout.list_view_province_stats, parent,
                false);
        ViewHolder holder = new ViewHolder(mBinding);

        return holder;
    }

    //**********************************************
    @Override
    public void onBindViewHolder(@androidx.annotation.NonNull ViewHolder holder, int position)
    //**********************************************
    {
        val item = mCountriesList.get(position);
        holder.mBinding.province.setText(item);
        if(TextUtils.isEmpty(item))
        {
            holder.mBinding.mainView.setVisibility(View.GONE);
        }
        holder.mBinding.mainView.setOnClickListener(view ->
        {
            if (holder.mBinding.up.getVisibility() == View.VISIBLE)
            {
                holder.mBinding.up.setVisibility(
                        View.GONE);
                holder.mBinding.down.setVisibility(
                        View.VISIBLE);
                holder.mBinding.statsLayout.setVisibility(
                        View.GONE);
            }
            else
            {
                holder.mBinding.statsLayout.setVisibility(
                        View.VISIBLE);
                holder.mBinding.up.setVisibility(
                        View.VISIBLE);
                holder.mBinding.down.setVisibility(
                        View.GONE);

                mCoronaStatsViewModel.getProvinceStat(
                        Constants.REPORT_DEATH,
                        mCountryName, item)
                                     .observe(mFragment,
                  integer -> {
                      if (integer == null)
                          integer = 0;
                      holder.mBinding.totalDeaths.setText(
                              UIUtils.getFormattedAmount(
                                      integer));

                  });

                mCoronaStatsViewModel.getProvinceStat(
                        Constants.REPORT_CONFIRMED,
                        mCountryName, item)
                                     .observe(mFragment,
                  integer -> {
                      if (integer == null)
                          integer = 0;
                      holder.mBinding.totalConfirmed.setText(UIUtils.getFormattedAmount(
                              integer));


                  });
                mCoronaStatsViewModel.getProvinceStat(
                        Constants.REPORT_RECOVERED,
                        mCountryName, item)
                                     .observe(mFragment,
                  integer -> {
                      if (integer == null)
                          integer = 0;
                      holder.mBinding.totalRecovered.setText(
                              UIUtils.getFormattedAmount(
                                      integer));

                  });

            }

            if (mCountryClickListener == null)
                return;
            mCountryClickListener.onCountryClick(item);
        });

    }

    //**********************************************
    @Override
    public int getItemCount()
    //**********************************************
    {
        return mCountriesList.size();
    }

    //**********************************************
    public class ViewHolder
            extends RecyclerView.ViewHolder
            //**********************************************
    {
        ListViewProvinceStatsBinding mBinding;

        //**********************************************
        public ViewHolder(@NonNull ListViewProvinceStatsBinding itemView)
        //**********************************************
        {
            super(itemView.getRoot());
            mBinding = itemView;
        }
    }


    //**********************************************
    public interface CountryClickListener
            //**********************************************
    {
        void onCountryClick(String country);
    }
}
