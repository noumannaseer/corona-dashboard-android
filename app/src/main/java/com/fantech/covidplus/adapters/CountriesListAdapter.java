package com.fantech.covidplus.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.fantech.covidplus.R;
import com.fantech.covidplus.databinding.ListViewCountriesBinding;

import java.util.List;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import lombok.NonNull;
import lombok.val;
//******************************************************************
public class CountriesListAdapter extends RecyclerView.Adapter<CountriesListAdapter.ViewHolder>
//******************************************************************
{
    private List<String> mCountriesList;
    private CountryClickListener mCountryClickListener;

    //******************************************************************
    public CountriesListAdapter(List<String> mCountriesList, CountryClickListener mCountryClickListener)
    //******************************************************************
    {
        this.mCountriesList = mCountriesList;
        this.mCountryClickListener = mCountryClickListener;
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
    {
        return super.getItemId(position);
    }
    //**********************************************

    //**********************************************
    @androidx.annotation.NonNull
    @Override
    public ViewHolder onCreateViewHolder(@androidx.annotation.NonNull ViewGroup parent, int viewType)
    //**********************************************
    {
        ListViewCountriesBinding mBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()), R.layout.list_view_countries, parent,
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
        holder.mBinding.country.setText(item);
        holder.mBinding.mainView.setOnClickListener(view ->
        {
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
    public class ViewHolder extends RecyclerView.ViewHolder
    //**********************************************
    {
        ListViewCountriesBinding mBinding;

        //**********************************************
        public ViewHolder(@NonNull ListViewCountriesBinding itemView)
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
