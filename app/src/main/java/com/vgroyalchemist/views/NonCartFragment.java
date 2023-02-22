package com.vgroyalchemist.views;

import android.os.Bundle;
import android.view.View;

import com.vgroyalchemist.controller.MainActivity;

public abstract class NonCartFragment extends ParentFragment {

    MainActivity mainActivity;

    @Override
    public void onResume() {
        super.onResume();

        mainActivity = (MainActivity) getActivity();
    }

    @Override
    public void doWork() {


    }


}