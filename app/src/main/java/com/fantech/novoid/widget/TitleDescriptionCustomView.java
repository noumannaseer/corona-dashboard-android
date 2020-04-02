package com.fantech.novoid.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.fantech.novoid.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import lombok.val;


//*******************************************************
public class TitleDescriptionCustomView
        extends FrameLayout
//*******************************************************
{
    private Context mContext;
    private TextView mTitle;
    private TextView mDescription;


    //*******************************************************
    public TitleDescriptionCustomView(@NonNull Context context)
    //*******************************************************
    {
        super(context);
        init(context, null);
    }

    //*******************************************************
    public TitleDescriptionCustomView(@NonNull Context context, @Nullable AttributeSet attrs)
    //*******************************************************
    {
        super(context, attrs);
        init(context, attrs);

    }

    //*******************************************************
    public TitleDescriptionCustomView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr)
    //*******************************************************

    {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    //*******************************************************
    public TitleDescriptionCustomView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes)
    //*******************************************************
    {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }


    //*******************************************************
    public void init(Context context, AttributeSet attributeSet)
    //*******************************************************
    {
        inflate(context, R.layout.custom_title_description, this);
        mContext = context;
        mTitle = findViewById(R.id.title);
        mDescription = findViewById(R.id.description);

        TypedArray attributes = mContext.obtainStyledAttributes(attributeSet,
                                                                R.styleable.TitleDescriptionCustomView);
        val title = attributes.getString(R.styleable.TitleDescriptionCustomView_title);
        val desc = attributes.getString(R.styleable.TitleDescriptionCustomView_description);
        mTitle.setText(title);
        mDescription.setText(desc);

    }

    //*******************************************************
    private void initControl()
    //*******************************************************
    {

    }

}
