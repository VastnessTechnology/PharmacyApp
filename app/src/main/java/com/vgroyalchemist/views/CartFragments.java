package com.vgroyalchemist.views;

import android.view.View;

import com.vgroyalchemist.controller.MainActivity;


public abstract class CartFragments extends ParentFragment {

    MainActivity mainActivity;

    @Override
    public void onResume() {

        super.onResume();

    }

    @Override
    public void doWork() {
        if (mainActivity.toolbar != null) {
            mainActivity.toolbar.setVisibility(View.GONE);
        }
    }
}
