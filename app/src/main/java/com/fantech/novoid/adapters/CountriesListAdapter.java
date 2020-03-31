package com.fantech.novoid.adapters;

import android.graphics.drawable.PictureDrawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.blongho.country_data.World;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.StreamEncoder;
import com.fantech.novoid.R;
import com.fantech.novoid.databinding.ListViewCountriesBinding;
import com.fantech.novoid.models.CoronaCountry;
import com.fantech.novoid.utils.AndroidUtil;
import com.fantech.novoid.utils.Constants;
import com.fantech.novoid.utils.ImageLoader;
import com.fantech.novoid.utils.UIUtils;

import java.io.InputStream;
import java.util.List;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import lombok.NonNull;
import lombok.val;

//******************************************************************
public class CountriesListAdapter
        extends RecyclerView.Adapter<CountriesListAdapter.ViewHolder>
//******************************************************************
{
    private List<CoronaCountry> mCountriesList;
    private CountryClickListener mCountryClickListener;
    private ImageLoader mImageLoader;

    //******************************************************************
    public CountriesListAdapter(List<CoronaCountry> mCountriesList, CountryClickListener mCountryClickListener)
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
        mImageLoader=new ImageLoader(parent.getContext());
        return holder;
    }

    //**********************************************
    @Override
    public void onBindViewHolder(@androidx.annotation.NonNull ViewHolder holder, int position)
    //**********************************************
    {
        val item = mCountriesList.get(position);
        holder.mBinding.country.setText(item.getCountry());
        holder.mBinding.totalDeaths.setText(UIUtils.getFormattedAmount(item.getTotalDeath()));
        holder.mBinding.totalConfirmed.setText(
                UIUtils.getFormattedAmount(item.getTotalConfirmed()));
        holder.mBinding.totalRecovered.setText(
                UIUtils.getFormattedAmount(item.getTotalRecovered()));
        holder.mBinding.mainView.setOnClickListener(view ->
        {
            if (mCountryClickListener == null)
                return;
            mCountryClickListener.onCountryClick(
                    item.getCountry());
        });

        World.init(holder.itemView.getContext());
        final int flag = World.getFlagOf(item.getCountry().toLowerCase());
       holder.mBinding.flag.setImageResource(flag);


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
