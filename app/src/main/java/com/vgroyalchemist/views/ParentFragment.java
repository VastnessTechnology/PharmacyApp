package com.vgroyalchemist.views;

import android.view.View;

import androidx.fragment.app.Fragment;

import com.vgroyalchemist.R;
import com.vgroyalchemist.controller.MainActivity;


public abstract class ParentFragment extends Fragment {

    MainActivity mainActivity;
    String mCurrentFragment = "";

    public abstract void doWork();

    @Override
    public void onResume() {
        super.onResume();

        mainActivity = (MainActivity) getActivity();




        Fragment currentFrag = mainActivity.getSupportFragmentManager().findFragmentById(R.id.activity_main_container);
        mCurrentFragment = currentFrag.getClass().getName();


    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
    }



    public void checkCart() {
        try {
            mainActivity = (MainActivity) getActivity();
//            mainActivity.showCart();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
