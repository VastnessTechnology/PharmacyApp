package com.vgroyalchemist.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.data.BarEntry;
import com.google.android.material.tabs.TabLayout;
import com.vgroyalchemist.R;
import com.vgroyalchemist.controller.MainActivity;
import com.vgroyalchemist.loaders.LoaderTask;
import com.vgroyalchemist.loaders.LoaderTaskAndroid;
import com.vgroyalchemist.loaders.LoaderTaskAndroidWithoutProgressDialog;
import com.vgroyalchemist.loaders.TaskExecutor;
import com.vgroyalchemist.utils.CommonHelper;
import com.vgroyalchemist.utils.Snackbar;
import com.vgroyalchemist.utils.Tags;
import com.vgroyalchemist.utils.Utility;
import com.vgroyalchemist.vos.ServerResponseVO;

import com.github.mikephil.charting.data.Entry;
import com.vgroyalchemist.vos.VitalListVO;
import com.vgroyalchemist.webservice.PostParseGet;
import com.vgroyalchemist.webservice.VitalGraphDataService;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/* developed by krishna  24-03-2021
*  vital graph class */
public class FragmentVitalDetails extends NonCartFragment implements TabLayout.OnTabSelectedListener {

    //Variable Declaration
    public static final String FRAGMENT_ID = "46";

    public static MainActivity mainActivity;
    public View fragmentView = null;
    CommonHelper mCommonHelper;

    ImageView mImageViewBack;
    TextView mTextViewTitle;
    ImageView mImageViewSearch;
    ImageView mImageViewCart;

    TextView mTextViewVitalName;
    TextView mTextViewAVG;


    private TabLayout tabLayout;
    private ViewPager viewPager;

    TextView mTextViewDisplay;
    TextView mTextViewShowAll;
    ServerResponseVO mServerResponseVO;

    public static ArrayList<String> xweekvalueday = new ArrayList<String>();
    public static ArrayList<BarEntry> yweekvalueday = new ArrayList<BarEntry>();


    public static ArrayList<String> xweekvalueweek = new ArrayList<String>();
    public static ArrayList<BarEntry> yweekvalueweek = new ArrayList<BarEntry>();

    public static ArrayList<String> xweekvaluemonth = new ArrayList<String>();
    public static ArrayList<BarEntry> yweekvaluemonth = new ArrayList<BarEntry>();

    public static ArrayList<String> xweekvalueyear = new ArrayList<String>();
    public static ArrayList<BarEntry> yweekvalueyear = new ArrayList<BarEntry>();

    Bundle mBundle;

    String mStringVitalId;
    String mStringVitalUnit;
    String mStringVital;

    PostParseGet mPostParseGet;
    ServerResponseVO mServerResponseVOToday;

    SharedPreferences mSharedPreferences;
    SharedPreferences.Editor mEditorChart;

    JSONObject oldjsonResponseData;

    // userdata
    public SharedPreferences mSharedPreferencesUserId;
    public SharedPreferences.Editor mEditor;
    String mStringUserid;
    String currentDate;
    Calendar mCalendar;
    private SimpleDateFormat dateFormatter;

    String mStringType;
    String mStringFromDate;
    String mVitaldata = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = (MainActivity) getActivity();
        mCommonHelper = new CommonHelper();
        mPostParseGet = new PostParseGet(mainActivity);
        mBundle = getArguments();

        if (mBundle != null) {
            mStringVitalId = mBundle.getString("VitalID");
            mStringVitalUnit = mBundle.getString("VitalUnit");
            mStringVital = mBundle.getString("VitalName");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentView = inflater.inflate(R.layout.fragment_vital_details, container, false);


        mImageViewBack = fragmentView.findViewById(R.id.fragment_imageview_back);
        mTextViewTitle = fragmentView.findViewById(R.id.fragment_title_textview);
        mTextViewTitle.setText("Vital Details");
        mImageViewSearch = fragmentView.findViewById(R.id.fragment_Serach);
        mImageViewSearch.setVisibility(View.GONE);
        mImageViewCart = fragmentView.findViewById(R.id.fragment_cart);
        mImageViewCart.setImageResource(R.drawable.top_btn_add);


        tabLayout = fragmentView.findViewById(R.id.fragment_vital_Details_tablayout);
        viewPager = fragmentView.findViewById(R.id.fragment_vital_Details_pager);

        mTextViewDisplay = fragmentView.findViewById(R.id.fragment_vital_Details_textdisplay);
        mTextViewShowAll = fragmentView.findViewById(R.id.fragment_Add_vital_textview_show_all);

        mTextViewVitalName = fragmentView.findViewById(R.id.fragment_vital_Details_vital_name);
        mTextViewAVG = fragmentView.findViewById(R.id.fragment_vital_Details_avg);


        mSharedPreferences = mainActivity.getSharedPreferences("vitallistchart", 0);
        mEditorChart = mSharedPreferences.edit();
        mVitaldata = mSharedPreferences.getString("chartdata", "");

        mSharedPreferencesUserId = mainActivity.getSharedPreferences(Tags.TAG_SHARED_PREFERENCE_USER_ID, 0);
        mEditor = mSharedPreferencesUserId.edit();
        mStringUserid = mSharedPreferencesUserId.getString("UserId", "");

        mCalendar = Calendar.getInstance();
        dateFormatter = new SimpleDateFormat("yyyy/MM/dd", Locale.US);
        currentDate = dateFormatter.format(mCalendar.getTime());
        mStringFromDate = currentDate;


        if(mStringVital != null)
        {
            mTextViewVitalName.setText(mStringVital);
        }
        mImageViewCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentAddVital fragmentAddVital = new FragmentAddVital();
                Bundle mBundle = new Bundle();
                mBundle.putString("VitalID", mStringVitalId);
                mBundle.putString("VitalUnit", mStringVitalUnit);
                fragmentAddVital.setArguments(mBundle);
                mainActivity.addFragment(fragmentAddVital, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, fragmentAddVital.getClass().getName());
            }
        });

        tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.day)));
        tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.week)));
        tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.month)));
        tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.year)));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        tabLayout.getTabAt(0).select();
        if (tabLayout.getTabAt(0).isSelected()) {
            mStringType = "day";
        }

        tabLayout.setOnTabSelectedListener(this);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        mTextViewShowAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentVitalShowAllList fragmentVitalShowAllList = new FragmentVitalShowAllList();
                Bundle mBundle = new Bundle();
                mBundle.putString("VitalID", mStringVitalId);
                mBundle.putString("VitalUnit", mStringVitalUnit);
                mBundle.putString("mStringVital", mStringVital);
                fragmentVitalShowAllList.setArguments(mBundle);
                mainActivity.addFragment(fragmentVitalShowAllList, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, fragmentVitalShowAllList.getClass().getName());
            }
        });

        mImageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.onBackPressed();
            }
        });


        return fragmentView;
    }

    public void onResumeData() {
        mainActivity.toolbar.setVisibility(View.GONE);

        callVitalDetails();
    }

    @Override
    public void doWork() {
        onResumeData();
    }

    @Override
    public void onResume() {
        super.onResume();
        mainActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        onResumeData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mCommonHelper.hideKeyboard(mainActivity);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {

        System.out.println("VT tab postion ------>" + tab.getPosition());
        if (tab.getPosition() == 0) {
            mStringType = "day";
            callVitalDetails();
        }
        if (tab.getPosition() == 1) {

            mStringType = "week";
            callVitalDetails();
        }

        if (tab.getPosition() == 2) {

            mStringType = "month";
            callVitalDetails();

            ArrayList<String> strings = new ArrayList<String>();
            //                JSONArray jsonArray = new JSONArray(str);

            for (int i = 0; i < 10; i++) {
                strings.add(String.valueOf(i));
            }

        }
        if (tab.getPosition() == 3) {

            mStringType = "year";
            callVitalDetails();

            ArrayList<String> strings = new ArrayList<String>();
            for (int i = 0; i < 10; i++) {
                strings.add(String.valueOf(i));
            }

//            String year = mServerResponseVO.getYearlist();
//            ArrayList<String> yeararraylist = new ArrayList<>();
//            try {
//                JSONArray jsonArray = new JSONArray(year);
//
//                for (int i = 0; i < jsonArray.length(); i++) {
//                    yeararraylist.add(jsonArray.get(i).toString());
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
////            String text = mServerResponseVO.getTextlist();
//            ArrayList<String> textarraylist = new ArrayList<>();
//            try {
//                JSONArray jsonArray = new JSONArray(text);
//
//                for (int i = 0; i < jsonArray.length(); i++) {
//                    textarraylist.add(jsonArray.get(i).toString());
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//            mTextViewDisplay.setText(textarraylist.get(0) + "-" + yeararraylist.get(0) + " " + "To" + " " + textarraylist.get(textarraylist.size() - 1) + "-" + yeararraylist.get(yeararraylist.size() - 1));
        }

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

        public void prepareChart(JSONObject jsonResponseData) {

            xweekvalueday = new ArrayList<String>();
            yweekvalueday = new ArrayList<BarEntry>();
            xweekvalueweek = new ArrayList<String>();
            yweekvalueweek = new ArrayList<BarEntry>();
            xweekvaluemonth = new ArrayList<String>();
            yweekvaluemonth = new ArrayList<BarEntry>();
            xweekvalueyear = new ArrayList<String>();
            yweekvalueyear = new ArrayList<BarEntry>();

            JSONObject day = null;

            if (mStringType.equalsIgnoreCase("day")) {
                selectTabIndex(0);
            } else if (mStringType.equalsIgnoreCase("week")) {
                selectTabIndex(1);
            } else if (mStringType.equalsIgnoreCase("month")) {
                selectTabIndex(2);
            } else {
                selectTabIndex(3);
            }
            try {
                if (mStringType.equalsIgnoreCase("day"))
                {
                    day = jsonResponseData.getJSONObject("details");
                    if (day.has("yvalue") && day.has("xvalue")) {

                        String yvalue = day.getString("yvalue");
                        String xvalue = day.getString("xvalue");
                        String Avg = day.getString("avg");

                        mTextViewAVG.setText("Avg : " + Avg);
                        JSONArray jsonResponseDatadayarray = new JSONArray(yvalue);
                        JSONArray jsonResponseDatadayarrayxvalue = new JSONArray(xvalue);
                        for (int i = 0; i < jsonResponseDatadayarray.length(); i++) {
                            yweekvalueday.add(new BarEntry(Float.parseFloat(jsonResponseDatadayarray.get(i).toString()), i));

                        }
                        for (int i = 0; i < jsonResponseDatadayarrayxvalue.length(); i++) {
                            xweekvalueday.add(jsonResponseDatadayarrayxvalue.get(i).toString());
                        }
                    }
                }
                if (mStringType.equalsIgnoreCase("week")) {
                    JSONObject week = jsonResponseData.getJSONObject("details");

                    if (week.has("yvalue") && week.has("xvalue")) {
                        String yvalue = week.getString("yvalue");
                        String xvalue = week.getString("xvalue");
                        String Avg = week.getString("avg");

                        mTextViewAVG.setText("Avg : " + Avg);
                        JSONArray jsonResponseDataweekarray = new JSONArray(yvalue);
                        JSONArray jsonResponseDataweekarrayxvalue = new JSONArray(xvalue);

                        for (int i = 0; i < jsonResponseDataweekarray.length(); i++) {
                            if (!jsonResponseDataweekarray.get(i).toString().equalsIgnoreCase("")) {
                                yweekvalueweek.add(new BarEntry(Float.parseFloat(jsonResponseDataweekarray.get(i).toString()), i));
                            }
                        }
                        for (int i = 0; i < jsonResponseDataweekarrayxvalue.length(); i++) {
                            xweekvalueweek.add(jsonResponseDataweekarrayxvalue.get(i).toString());
                        }
                    }
                }
                if (mStringType.equalsIgnoreCase("month")) {
                    JSONObject month = jsonResponseData.getJSONObject("details");
                    if (month.has("yvalue") && month.has("xvalue")) {

                        String yvalue = month.getString("yvalue");
                        String xvalue = month.getString("xvalue");
                        String Avg = month.getString("avg");

                        mTextViewAVG.setText("Avg : " + Avg);

                        JSONArray jsonResponseDatamonthyvalue = new JSONArray(yvalue);
                        JSONArray jsonResponseDatamonthxvalue = new JSONArray(xvalue);

                        for (int i = 0; i < jsonResponseDatamonthyvalue.length(); i++) {

                            if (!jsonResponseDatamonthyvalue.get(i).toString().equalsIgnoreCase("")) {

                                yweekvaluemonth.add(new BarEntry(Float.parseFloat(jsonResponseDatamonthyvalue.get(i).toString()), i));
                            }
                        }

                        for (int i = 0; i < jsonResponseDatamonthxvalue.length(); i++) {
                            xweekvaluemonth.add(jsonResponseDatamonthxvalue.get(i).toString());
                        }
                    }
                }

                if (mStringType.equalsIgnoreCase("year")) {
                    JSONObject year = jsonResponseData.getJSONObject("details");
                    if (year.has("yvalue") && year.has("xvalue")) {

                        String yvalue = year.getString("yvalue");
                        String xvalue = year.getString("xvalue");
                        String Avg = year.getString("avg");

                        mTextViewAVG.setText("Avg : " + Avg);
                        JSONArray jsonResponseDatayearyvalue = new JSONArray(yvalue);
                        JSONArray jsonResponseDatayearxvalue = new JSONArray(xvalue);

                        for (int i = 0; i < jsonResponseDatayearyvalue.length(); i++) {
                            if (!jsonResponseDatayearyvalue.get(i).toString().equalsIgnoreCase(""))
                                yweekvalueyear.add(new BarEntry(Float.parseFloat(jsonResponseDatayearyvalue.get(i).toString()), i));
                        }
                        for (int i = 0; i < jsonResponseDatayearxvalue.length(); i++) {
                            xweekvalueyear.add(jsonResponseDatayearxvalue.get(i).toString());

                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            RVVitalDetalListAdapter adapter = new RVVitalDetalListAdapter(getFragmentManager(), tabLayout.getTabCount());
            viewPager.setAdapter(adapter);
        }

    public static class RVVitalDetalListAdapter extends FragmentStatePagerAdapter {
        int tabCount;

        //        private GetLanguageData.GetLanguage mGetLanguage =new GetLanguageData.GetLanguage();
        public RVVitalDetalListAdapter(FragmentManager fm, int tabCount) {
            super(fm);

            this.tabCount = tabCount;
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    DayFragment dayFragment = new DayFragment();
                    return dayFragment;
                case 1:
                    WeekFragment weekFragment = new WeekFragment();
                    return weekFragment;
                case 2:
                    MonthFragment monthFragment = new MonthFragment();
                    return monthFragment;
                case 3:
                    YearFragment yearFragment = new YearFragment();
                    return yearFragment;
                default:
                    return null;
            }

        }

        @Override
        public int getCount() {
            return tabCount;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            switch (position) {
                case 0:
                    return "DAY";
                case 1:
                    return "WEEK";
                case 2:
                    return "MONTH";
                case 3:
                    return "YEAR";

            }
            return null;
        }
    }


    public void callVitalDetails() {

        xweekvalueday = new ArrayList<String>();
        yweekvalueday = new ArrayList<BarEntry>();
        xweekvalueweek = new ArrayList<String>();
        yweekvalueweek = new ArrayList<BarEntry>();
        xweekvaluemonth = new ArrayList<String>();
        yweekvaluemonth = new ArrayList<BarEntry>();
        xweekvalueyear = new ArrayList<String>();
        yweekvalueyear = new ArrayList<BarEntry>();


        getLoaderManager().restartLoader(0, null, new LoaderManager.LoaderCallbacks<TaskExecutor>() {
            @Override
            public Loader<TaskExecutor> onCreateLoader(int arg0, Bundle arg1) {

                VitalDetailExecutor getVitalListExe = new VitalDetailExecutor(mainActivity, null);
                return new LoaderTask<TaskExecutor>(mainActivity, getVitalListExe);
            }

            @Override
            public void onLoadFinished(@NonNull Loader<TaskExecutor> loader, TaskExecutor data) {
                try {
                    getLoaderManager().destroyLoader(0);

                    if (mServerResponseVO.getStatus().equalsIgnoreCase("true")) {

                        JSONObject jsonResponseData = (JSONObject) mServerResponseVO.getData();
                        prepareChart(jsonResponseData);


                        mEditorChart.putString("chartdata", String.valueOf(jsonResponseData));
                        mEditorChart.commit();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onLoaderReset(@NonNull Loader<TaskExecutor> loader) {

            }
        });
    }

    private void selectTabIndex(final int index) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                tabLayout.setScrollPosition(index, 0, true);
                viewPager.setCurrentItem(index);

            }
        }, 100);

    }

    public class VitalDetailExecutor extends TaskExecutor {


        protected VitalDetailExecutor(Context context, Bundle bundle) {
            super(context, bundle);
        }

        @Override
        protected Object executeTask() {
            try {


                VitalGraphDataService vitalGraphDataService = new VitalGraphDataService();
                mServerResponseVO = vitalGraphDataService.vitalListGraph(mainActivity, Tags.GetVitalGraph, mStringUserid, mStringVitalId, mStringType);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public void setUnitChartData() {

        if (mVitaldata != null && !mVitaldata.equalsIgnoreCase("")) {
            try {
                oldjsonResponseData = new JSONObject(mVitaldata);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            xweekvalueday = new ArrayList<String>();
            yweekvalueday = new ArrayList<BarEntry>();
            xweekvalueweek = new ArrayList<String>();
            yweekvalueweek = new ArrayList<BarEntry>();
            xweekvaluemonth = new ArrayList<String>();
            yweekvaluemonth = new ArrayList<BarEntry>();
            xweekvalueyear = new ArrayList<String>();
            yweekvalueyear = new ArrayList<BarEntry>();

            prepareChart(oldjsonResponseData);
            callVitalDetailsInbackground();
        } else {
            if (mCommonHelper.check_Internet(mainActivity)) {
                Utility.debugger("VT in obj else vitaldetail...");
                callVitalDetails();
            } else {
                Snackbar.with(mainActivity).text(getResources().getString(R.string.interneterror)).show(mainActivity);
            }
        }
    }

    public void callVitalDetailsInbackground() {


        getLoaderManager().restartLoader(0, null, new LoaderManager.LoaderCallbacks<TaskExecutor>() {
            @Override
            public Loader<TaskExecutor> onCreateLoader(int arg0, Bundle arg1) {

                VitalDetailExecutor getVitalListExe = new VitalDetailExecutor(mainActivity, null);
                return new LoaderTask<TaskExecutor>(mainActivity, getVitalListExe);
            }

            @Override
            public void onLoadFinished(@NonNull Loader<TaskExecutor> loader, TaskExecutor data) {
                try {
                    getLoaderManager().destroyLoader(0);
                    if (mServerResponseVO != null && mServerResponseVO.getStatus() != null) {

                        if (mServerResponseVO.getStatus().equalsIgnoreCase("1")) {

                            tabLayout.post(new Runnable() {
                                @Override
                                public void run() {
                                    tabLayout.setupWithViewPager(viewPager);
                                }
                            });


                            JSONObject jsonResponseData = (JSONObject) mServerResponseVO.getData();
                            prepareChart(jsonResponseData);

                            mEditor.putString("chartdata", String.valueOf(jsonResponseData));
                            mEditor.commit();
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onLoaderReset(@NonNull Loader<TaskExecutor> loader) {

            }
        });
    }


}

