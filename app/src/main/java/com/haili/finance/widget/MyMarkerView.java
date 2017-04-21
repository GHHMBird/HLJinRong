package com.haili.finance.widget;


import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import com.haili.finance.R;
import com.jn.chart.components.MarkerView;
import com.jn.chart.data.CandleEntry;
import com.jn.chart.data.Entry;
import com.jn.chart.highlight.Highlight;
import com.jn.chart.utils.Utils;

/**
 * Created by Monkey on 2016/12/30.
 */

public class MyMarkerView extends MarkerView {

    private TextView tvContent;

    public MyMarkerView(Context context, int layoutResource) {
        super(context, layoutResource);

        tvContent = (TextView) findViewById(R.id.tvContent);
    }

    // callbacks everytime the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    @Override
    public void refreshContent(Entry e, Highlight highlight) {

        if (e instanceof CandleEntry) {

            CandleEntry ce = (CandleEntry) e;

//            tvContent.setText("" + Utils.formatNumber(ce.getHigh(), 2, true)+"元");
//            Log.i("linechart",ce.getHigh()+","+ Utils.formatNumber(ce.getHigh(),2, true));
        } else {

//            tvContent.setText("" + Utils.formatNumber(e.getY(), 2, true)+"元");
//            Log.i("linechart",e.getY()+","+ Utils.formatNumber(e.getY(), 2, true));

        }
    }

    @Override
    public int getXOffset(float xpos) {
        return 0;
    }

    @Override
    public int getYOffset(float ypos) {
        return 0;
    }

}
