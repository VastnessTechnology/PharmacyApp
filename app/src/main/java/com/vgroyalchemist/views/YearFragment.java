package com.vgroyalchemist.views;

import android.graphics.Color;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
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
*  Draw year graph*/
public class YearFragment extends Fragment {

    private View fragmentView = null;
    ArrayList<String> xweekvalueyear = new ArrayList<String>();
    ArrayList<BarEntry> yweekvalueyear = new ArrayList<BarEntry>();

    BarChart lineChart4;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        fragmentView = inflater.inflate(R.layout.fragment_day, container, false);
        lineChart4 = (BarChart)fragmentView.findViewById(R.id.chart);

        xweekvalueyear = FragmentVitalDetails.xweekvalueyear;
        yweekvalueyear= FragmentVitalDetails.yweekvalueyear;

        TextView nodata = (TextView)fragmentView.findViewById(R.id.nodata);

        if (xweekvalueyear.size() > 0){
            lineChart4.setVisibility(View.VISIBLE);
            nodata.setVisibility(View.GONE);
            openChart();
        }else {
            lineChart4.setVisibility(View.GONE);
            nodata.setVisibility(View.VISIBLE);
        }
        return fragmentView;
    }

    private void openChart() {

        BarDataSet bardataset = new BarDataSet(yweekvalueyear, "Vital");
        lineChart4.animateY(5000);
        BarData data = new BarData(xweekvalueyear, bardataset);
        lineChart4.setDescription("");
        bardataset.setColor(getResources().getColor(R.color.parrot_green));
        lineChart4.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        CustomMarkerView mv = new CustomMarkerView (getActivity(), R.layout.markerview);
        lineChart4.setMarkerView(mv);
        lineChart4.setData(data);



//        LineDataSet dataset = new LineDataSet(yweekvalueyear, "");
//        LineData lineData = new LineData(xweekvalueyear, dataset);
//
//        dataset.setColors(ColorTemplate.COLORFUL_COLORS);
//        dataset.setDrawCubic(false);
//        dataset.setDrawCircles(true);
//        dataset.setCircleSize(1.5f);
//        dataset.setCircleColor(Color.BLACK);
//        dataset.setDrawValues(true);
//        dataset.setDrawFilled(true);
//
//        lineChart4.setData(lineData);
//        lineChart4.setExtraTopOffset(10);
//        lineChart4.animateY(0);
//
//        lineChart4.getLegend().setEnabled(false);
//        lineChart4.setDescription("");
    }
}