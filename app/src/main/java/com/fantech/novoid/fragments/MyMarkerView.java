
package com.fantech.novoid.fragments;

import android.content.Context;
import android.widget.TextView;

import com.fantech.novoid.R;
import com.fantech.novoid.models.CoronaGraph;
import com.fantech.novoid.utils.UIUtils;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

import java.util.List;

import lombok.Setter;


//*********************************************
public class MyMarkerView
        extends MarkerView
//*********************************************
{

    private TextView mSearchCount;
    private TextView mSearchDate;
    @Setter
    private List<CoronaGraph> mStats;


    //******************************************************
    public MyMarkerView(Context context, int layoutResource)
    //******************************************************
    {

        super(context, layoutResource);
        mSearchCount = findViewById(R.id.search_count);
        mSearchDate = findViewById(R.id.search_date);
    }


    //******************************************************
    @Override
    public void refreshContent(Entry e, Highlight highlight)
    //******************************************************
    {

        super.refreshContent(e, highlight);


        if (e instanceof CandleEntry)
        {
            CandleEntry ce = (CandleEntry)e;
        }
        else
        {
            mSearchCount.setText("" + e.getY());
            mSearchDate.setText(UIUtils.getDate(mStats.get((int)e.getX())
                                                      .getDate()
                                                      .getTime(), "dd-mm-yyyy"));
        }

    }

    //******************************************************
    @Override
    public MPPointF getOffset()
    //******************************************************
    {
        return new MPPointF(-(getWidth() / 2), -(float)(getHeight() / 2.65));
    }
}
