package com.vgroyalchemist.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.android.material.tabs.TabLayout;
import com.vgroyalchemist.R;
import com.vgroyalchemist.controller.MainActivity;
import com.vgroyalchemist.utils.CommonHelper;
import com.vgroyalchemist.webservice.PostParseGet;

import java.util.Objects;

/**
 * Created by krishna 17-12-2021
 */
public class FragmentTabActivity extends NonCartFragment implements TabLayout.OnTabSelectedListener {

    //Variable Declaration
    public static final String FRAGMENT_ID = "61";

    public static MainActivity mainActivity;
    public View fragmentView = null;
    CommonHelper mCommonHelper;
    PostParseGet mPostParseGet;


    TabLayout tabLayout;
    ViewPager viewPager;

    TabLayoutAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = (MainActivity) getActivity();
        mCommonHelper = new CommonHelper();
        mPostParseGet = new PostParseGet(mainActivity);
    }

    @SuppressLint({"UseCompatLoadingForDrawables", "ClickableViewAccessibility"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentView = inflater.inflate(R.layout.fragment_tab_activity, container, false);

        tabLayout = fragmentView.findViewById(R.id.fragment_Dashboard_tablayout);
        viewPager = fragmentView.findViewById(R.id.fragment_Dashboard_pager);


        tabLayout.addTab(tabLayout.newTab().setIcon(getResources().getDrawable(R.drawable.homeselector)));
        tabLayout.addTab(tabLayout.newTab().setIcon(getResources().getDrawable(R.drawable.searchselecter)));
        tabLayout.addTab(tabLayout.newTab().setIcon(getResources().getDrawable(R.drawable.arcicalselector)));
        tabLayout.addTab(tabLayout.newTab().setIcon(getResources().getDrawable(R.drawable.offerselecter)));
        tabLayout.addTab(tabLayout.newTab().setIcon(getResources().getDrawable(R.drawable.accountselecter)));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);


        adapter = new TabLayoutAdapter(mainActivity, getFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(3);

        tabLayout.addOnTabSelectedListener(this);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        viewPager.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                return true;
            }
        });
        return fragmentView;
    }

    public class TabLayoutAdapter extends FragmentStatePagerAdapter {
        int tabCount;
        Context mContext;
        private boolean enableSwipe;

        @SuppressLint("WrongConstant")
        public TabLayoutAdapter(MainActivity mainActivity, FragmentManager fm, int tabCount) {
            super(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
            this.mContext = mainActivity;
            this.tabCount = tabCount;
        }


        @NonNull
        @Override
        public Fragment getItem(int position) {


            switch (position) {
                case 0:
                    return new FragmentHomeNewTheme();
                case 1:
                    return new FragmentSearchSubstitutes();
                case 2:
                    return new FragmentArticles();
                case 3:
                    return new FragmentCouponCode();
                case 4:
                    return new FragmentMenu();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return tabCount;
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            Fragment fragment = (Fragment) object;
            if (fragment != null) {
                fragment.setMenuVisibility(false);
            }
        }
        public void setEnableSwipe(boolean enableSwipe) {
            this.enableSwipe = enableSwipe;
        }

    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());

        if (tab.getPosition() == 1) {
            tabLayout.setVisibility(View.GONE);
        } else if (tab.getPosition() == 2) {
            tabLayout.setVisibility(View.GONE);
        } else if (tab.getPosition() == 3) {
            tabLayout.setVisibility(View.GONE);
        }

    }



    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public void onResumeData() {

    }

    @Override
    public void doWork() {
        onResumeData();
    }

    @Override
    public void onResume() {
        super.onResume();

        onResumeData();

    }


}