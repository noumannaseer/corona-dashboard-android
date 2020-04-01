package com.fantech.novoid.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;


import com.fantech.novoid.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import lombok.Setter;
import lombok.val;


//*******************************************************
public class GuideLineCustomView
        extends FrameLayout
//*******************************************************
{
    private Context mContext;
    private TextView mTextView;
    private ImageView mImageView;


    //*******************************************************
    public GuideLineCustomView(@NonNull Context context)
    //*******************************************************
    {
        super(context);
        init(context, null);
    }

    //*******************************************************
    public GuideLineCustomView(@NonNull Context context, @Nullable AttributeSet attrs)
    //*******************************************************
    {
        super(context, attrs);
        init(context, attrs);

    }

    //*******************************************************
    public GuideLineCustomView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr)
    //*******************************************************

    {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    //*******************************************************
    public GuideLineCustomView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes)
    //*******************************************************
    {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }


    //*******************************************************
    public void init(Context context, AttributeSet attributeSet)
    //*******************************************************
    {
        inflate(context, R.layout.guideline_custom_view, this);
        mContext = context;
        mImageView = findViewById(R.id.guide_image);
        mTextView = findViewById(R.id.guide_text);

        TypedArray attributes = mContext.obtainStyledAttributes(attributeSet,
                                                                R.styleable.GuideLineCustomView);
        val text = attributes.getString(R.styleable.GuideLineCustomView_text);
        val image = attributes.getDrawable(R.styleable.GuideLineCustomView_image);
        mTextView.setText(text);
        mImageView.setImageDrawable(image);

    }

    //*******************************************************
    private void initControl()
    //*******************************************************
    {

    }

}
