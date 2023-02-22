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
*  draw month graph */
public class MonthFragment extends Fragment {

    private View fragmentView = null;

    ArrayList<String> xweekvaluemonth = new ArrayList<String>();
    ArrayList<BarEntry> yweekvaluemonth = new ArrayList<BarEntry>();

    BarChart lineChart3;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        fragmentView = inflater.inflate(R.layout.fragment_day, container, false);
        lineChart3 = (BarChart) fragmentView.findViewById(R.id.chart);

        xweekvaluemonth = FragmentVitalDetails.xweekvaluemonth;
        yweekvaluemonth = FragmentVitalDetails.yweekvaluemonth;

        TextView nodata = (TextView) fragmentView.findViewById(R.id.nodata);
        if (xweekvaluemonth.size() > 0) {

            lineChart3.setVisibility(View.VISIBLE);
            nodata.setVisibility(View.GONE);
            openChart();
        } else {
            lineChart3.setVisibility(View.GONE);
            nodata.setVisibility(View.VISIBLE);
        }
        return fragmentView;
    }

    private void openChart() {

        BarDataSet bardataset = new BarDataSet(yweekvaluemonth, "Vital");
        lineChart3.animateY(5000);
        BarData data = new BarData(xweekvaluemonth, bardataset);
        bardataset.setColor(getResources().getColor(R.color.parrot_green));
        lineChart3.setDescription("");
        lineChart3.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        lineChart3.setData(data);

        CustomMarkerView mv = new CustomMarkerView (getActivity(), R.layout.markerview);
        lineChart3.setMarkerView(mv);

//        LineDataSet dataset = new LineDataSet(yweekvaluemonth, "");
//        LineData lineData = new LineData(xweekvaluemonth, dataset);
//
//        dataset.setColors(ColorTemplate.COLORFUL_COLORS);
//        dataset.setDrawCubic(false);
//        dataset.setDrawCircles(true);
//        dataset.setCircleSize(1.5f);
//        dataset.setCircleColor(Color.BLACK);
//        dataset.setDrawValues(true);
//        dataset.setDrawFilled(true);
//
//        lineChart3.setData(lineData);
//        lineChart3.setExtraTopOffset(10);
//        lineChart3.animateY(0);
//
//        lineChart3.getLegend().setEnabled(false);
//        lineChart3.setDescription("");
    }
}