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
*  Draw Day Graph*/
public class DayFragment extends Fragment {

    private View fragmentView = null;

    TextView nodata;

    ArrayList<String> xweekvalueday = new ArrayList<String>();
    ArrayList<BarEntry> yweekvalueday = new ArrayList<BarEntry>();

    LineChart lineChart1;
    BarDataSet set1;
    BarChart barChart;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        fragmentView = inflater.inflate(R.layout.fragment_day, container, false);
        barChart = (BarChart) fragmentView.findViewById(R.id.chart);
        nodata = (TextView)fragmentView.findViewById(R.id.nodata);

        xweekvalueday = FragmentVitalDetails.xweekvalueday;
        yweekvalueday = FragmentVitalDetails.yweekvalueday;

        if (xweekvalueday.size() > 0){
            barChart.setVisibility(View.VISIBLE);
            nodata.setVisibility(View.GONE);
            openChart();
        }else {
            barChart.setVisibility(View.GONE);
            nodata.setVisibility(View.VISIBLE);
        }
        return fragmentView;
    }

    private void openChart() {

        BarDataSet bardataset = new BarDataSet(yweekvalueday, "Vital");
        barChart.animateY(5000);
        BarData data = new BarData(xweekvalueday, bardataset);
        bardataset.setColor(getResources().getColor(R.color.parrot_green));
        barChart.setDescription("");
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        barChart.setData(data);

        CustomMarkerView mv = new CustomMarkerView (getActivity(), R.layout.markerview);
        barChart.setMarkerView(mv);

//        LineDataSet dataset = new LineDataSet(yweekvalueday, "");
//        LineData lineData = new LineData(xweekvalueday, dataset);
//
//        dataset.setColors(ColorTemplate.COLORFUL_COLORS);
//        dataset.setDrawCubic(false);
//        dataset.setDrawCircles(true);
//        dataset.setCircleSize(1.5f);
//        dataset.setCircleColor(Color.BLACK);
//        dataset.setDrawValues(true);
//        dataset.setDrawFilled(true);
//
////        lineChart1.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
//
//        lineChart1.setData(lineData);
//        lineChart1.setExtraTopOffset(10);
//        lineChart1.animateY(0);
//
//        lineChart1.getLegend().setEnabled(false);
//        lineChart1.setDescription("");
    }
}