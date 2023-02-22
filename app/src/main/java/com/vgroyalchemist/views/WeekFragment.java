package com.vgroyalchemist.views;

import android.graphics.Color;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.vgroyalchemist.R;


import java.util.ArrayList;

/* developed by krishna 24-03-2021
*  Draw week graph */
public class WeekFragment extends Fragment {

    private View fragmentView = null;

    ArrayList<String> xweekvalueweek = new ArrayList<String>();
    ArrayList<BarEntry> yweekvalueweek = new ArrayList<BarEntry>();

    LineChart lineChart2;
    BarChart barChart2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        fragmentView = inflater.inflate(R.layout.fragment_day, container, false);
        barChart2 = (BarChart)fragmentView.findViewById(R.id.chart);

        xweekvalueweek = FragmentVitalDetails.xweekvalueweek;
        yweekvalueweek= FragmentVitalDetails.yweekvalueweek;

        TextView nodata = (TextView)fragmentView.findViewById(R.id.nodata);
        if (xweekvalueweek.size() > 0){

            barChart2.setVisibility(View.VISIBLE);
            nodata.setVisibility(View.GONE);
            openChart();
        }else {

            barChart2.setVisibility(View.GONE);
            nodata.setVisibility(View.VISIBLE);
        }
        return fragmentView;

    }

    private void openChart() {

        BarDataSet bardataset = new BarDataSet(yweekvalueweek, "Vital");
        barChart2.animateY(5000);
        BarData data = new BarData(xweekvalueweek, bardataset);
        bardataset.setColor(getResources().getColor(R.color.parrot_green));
        barChart2.setDescription("");
        barChart2.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        CustomMarkerView mv = new CustomMarkerView (getActivity(), R.layout.markerview);
        barChart2.setMarkerView(mv);
        barChart2.setData(data);

//        LineDataSet dataset = new LineDataSet(yweekvalueweek, "");
//        LineData lineData = new LineData(xweekvalueweek, dataset);
//
//        dataset.setColors(ColorTemplate.COLORFUL_COLORS);
//        dataset.setDrawCubic(false);
//        dataset.setDrawCircles(true);
//        dataset.setCircleSize(1.5f);
//        dataset.setCircleColor(Color.BLACK);
//        dataset.setDrawValues(true);
//        dataset.setDrawFilled(true);
//
//        lineChart2.setData(lineData);
//        lineChart2.setExtraTopOffset(10);
//        lineChart2.animateY(500);
//
//        lineChart2.getLegend().setEnabled(false);
//        lineChart2.setDescription("");

    }
}