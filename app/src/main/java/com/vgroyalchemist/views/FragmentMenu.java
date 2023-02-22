package com.vgroyalchemist.views;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vgroyalchemist.R;
import com.vgroyalchemist.controller.MainActivity;
import com.vgroyalchemist.utils.CommonHelper;
import com.vgroyalchemist.utils.SweetAlertDialog;
import com.vgroyalchemist.utils.Tags;
import com.vgroyalchemist.vos.GetLoginData;
import com.vgroyalchemist.webservice.PostParseGet;

/**
 * create by krishna 17-12-2021
 */
public class FragmentMenu extends NonCartFragment {

    //Variable Declaration
    public static final String FRAGMENT_ID = "62";

    public static MainActivity mainActivity;
    public View fragmentView = null;
    CommonHelper mCommonHelper;
    PostParseGet mPostParseGet;

    TextView mTextViewUserName;
    TextView mTextViewSignOut;
    TextView mTextViewSingIn;
    TextView mTextViewOrder_with_prescrtiption;
    TextView mTextViewSchedule;
    TextView mTextViewVital;
    TextView mTextViewMyOrder;
    TextView mTextViewPreference;
    TextView mTextViewAbout_us;
    TextView mTextViewLegal;
    TextView mTextViewComplainList;
    TextView mTextViewMyConsultations;

 //   LinearLayout mLinearLayoutOrderWith;
    LinearLayout mLinearLayoutMyOrder;
    LinearLayout mLinearLayoutSchedule;
    LinearLayout mLinearLayoutSingOut;
    LinearLayout mLinearLayoutComplain;

    //login
    public SharedPreferences mSharedPreferencesLoginFirst;
    public SharedPreferences.Editor editor;
    boolean hasLoggedIn;
    String GuestUser;
    String mStringEmailId;

    // userdata
    public SharedPreferences mSharedPreferencesUserId;
    SharedPreferences.Editor mEditor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = (MainActivity) getActivity();
        mPostParseGet = new PostParseGet(mainActivity);
        mCommonHelper = new CommonHelper();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentView = inflater.inflate(R.layout.fragment_menu, container, false);

        mTextViewUserName = fragmentView.findViewById(R.id.fragment_Menu_textview_username);
        mTextViewOrder_with_prescrtiption = fragmentView.findViewById(R.id.fragment_Menu_textview_order_with_prescrtiption);
        mTextViewSchedule = fragmentView.findViewById(R.id.fragment_Menu_textview_schedule);
        mTextViewVital = fragmentView.findViewById(R.id.fragment_Menu_textview_vital);
        mTextViewMyOrder = fragmentView.findViewById(R.id.fragment_Menu_textview_myOrder);
        mTextViewPreference = fragmentView.findViewById(R.id.fragment_Menu_textview_preference);
        mTextViewAbout_us = fragmentView.findViewById(R.id.fragment_Menu_textview_about_us);
        mTextViewMyConsultations = fragmentView.findViewById(R.id.fragment_Menu_textview_myconsultations);
        mTextViewSignOut = fragmentView.findViewById(R.id.fragment_Menu_textview_signout);
        mTextViewLegal = fragmentView.findViewById(R.id.fragment_Menu_textview_legal);
        mTextViewComplainList = fragmentView.findViewById(R.id.fragment_Menu_textview_refer_complain_list);
        mTextViewSingIn = fragmentView.findViewById(R.id.fragment_Menu_textview_signup);

        //mLinearLayoutOrderWith = fragmentView.findViewById(R.id.fragment_Menu_linaer_order_with_prescription);
        mLinearLayoutMyOrder = fragmentView.findViewById(R.id.fragment_Menu_Liner_myOrder);
        mLinearLayoutSchedule = fragmentView.findViewById(R.id.fragment_Menu_Liner_schedule);
        mLinearLayoutSingOut = fragmentView.findViewById(R.id.fragment_Menu_Liner_signout);
        mLinearLayoutComplain = fragmentView.findViewById(R.id.fragment_Menu_Liner_complain_list);

        mSharedPreferencesLoginFirst = mainActivity.getSharedPreferences(Tags.PREFS_NAME, 0);
        hasLoggedIn = mSharedPreferencesLoginFirst.getBoolean("hasLoggedIn", false);
        GuestUser = mSharedPreferencesLoginFirst.getString("GuestUser", "0");

        mSharedPreferencesUserId = mainActivity.getSharedPreferences(Tags.TAG_SHARED_PREFERENCE_USER_ID, 0);
        mEditor = mSharedPreferencesUserId.edit();
        mStringEmailId = mSharedPreferencesUserId.getString("EmailId", "");



        if (GuestUser.equalsIgnoreCase("1")) {
         //   mLinearLayoutOrderWith.setVisibility(View.GONE);
            mLinearLayoutMyOrder.setVisibility(View.GONE);
            mTextViewSingIn.setVisibility(View.VISIBLE);
            mTextViewUserName.setVisibility(View.VISIBLE);
            mLinearLayoutSchedule.setVisibility(View.GONE);
            mLinearLayoutComplain.setVisibility(View.GONE);
            mLinearLayoutSingOut.setVisibility(View.GONE);

        } else {
       //     mLinearLayoutOrderWith.setVisibility(View.VISIBLE);
            mLinearLayoutMyOrder.setVisibility(View.VISIBLE);
            mTextViewSingIn.setVisibility(View.GONE);
            mTextViewUserName.setVisibility(View.VISIBLE);
            mTextViewUserName.setText(mStringEmailId);
            mLinearLayoutSchedule.setVisibility(View.VISIBLE);
            mLinearLayoutComplain.setVisibility(View.VISIBLE);
            mLinearLayoutSingOut.setVisibility(View.VISIBLE);
        }

        mTextViewSingIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentLoginScreen fragmentLoginScreen = new FragmentLoginScreen();
                Bundle mBundle   = new Bundle();
                mBundle.putBoolean(Tags.IS_FROM_LOGIN,true);
                fragmentLoginScreen.setArguments(mBundle);
                mainActivity.addFragment(fragmentLoginScreen, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, fragmentLoginScreen.getClass().getName());
            }
        });

        mLinearLayoutComplain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentComplainList fragmentComplainList = new FragmentComplainList();
                mainActivity.addFragment(fragmentComplainList, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, fragmentComplainList.getClass().getName());

            }
        });

        mTextViewPreference.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                FragmentPreference fragmentPreference = new FragmentPreference();
                mainActivity.addFragment(fragmentPreference, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, fragmentPreference.getClass().getName());

            }
        });

        mTextViewVital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentVital fragmentVital = new FragmentVital();
                mainActivity.addFragment(fragmentVital, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, fragmentVital.getClass().getName());

            }
        });

        mTextViewLegal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentLegal fragmentLegal = new FragmentLegal();
                mainActivity.addFragment(fragmentLegal, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, fragmentLegal.getClass().getName());

            }
        });
        mTextViewSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentSchedule fragmentSchedule = new FragmentSchedule();
                mainActivity.addFragment(fragmentSchedule, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, fragmentSchedule.getClass().getName());

            }
        });
        mTextViewAbout_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentAboutUs fragmentAboutUs = new FragmentAboutUs();
                mainActivity.addFragment(fragmentAboutUs, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, fragmentAboutUs.getClass().getName());

            }
        });

        mTextViewMyOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentMyOrderList fragmentMyOrderList = new FragmentMyOrderList();
                mainActivity.addFragment(fragmentMyOrderList, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, fragmentMyOrderList.getClass().getName());

            }
        });
        mTextViewOrder_with_prescrtiption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentOrderWithPresciption fragmentOrderWithPresciption = new FragmentOrderWithPresciption();
                mainActivity.addFragment(fragmentOrderWithPresciption,true,FragmentTransaction.TRANSIT_FRAGMENT_OPEN,fragmentOrderWithPresciption.getClass().getName());
            }
        });

        mTextViewSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SweetAlertDialog mSweetAlertDialog = new SweetAlertDialog(mainActivity)
                        .setContentText("Are you sure you want to logout?")
                        .setConfirmText("Yes")
                        .setCancelText("No")
                        .showCancelButton(true)
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismiss();
                                editor = mSharedPreferencesLoginFirst.edit();
                                editor.putBoolean("hasLoggedIn", false);
                                editor.apply();
                                mainActivity.getSharedPreferences(Tags.PREFS_NAME, 0).edit().clear().apply();
                                mainActivity.getSharedPreferences(Tags.TAG_SHARED_PREFERENCE_REFERAL_CODE, 0).edit().clear().apply();
                                mainActivity.getSharedPreferences(Tags.TAG_SHARED_PREFERENCE_USER_ID, 0).edit().clear().apply();
                                mainActivity.getSharedPreferences(Tags.TAG_SHARED_PREFERENCE_Cart_Count, 0).edit().clear().apply();
//                                mainActivity.getSharedPreferences("location", 0).edit().clear().apply();
                                mainActivity.getSharedPreferences("LatLng", 0).edit().clear().apply();
//                                getSharedPreferences(Tags.TAG_SHARED_PREFRENCE_Address, 0).edit().clear().commit();


                                FragmentLoginScreen mFragmentDiscover = new FragmentLoginScreen();
                                Bundle args = new Bundle();
                                args.putString(getString(R.string.app_name), "");
                                mFragmentDiscover.setArguments(args);
                                mainActivity.addFragment(mFragmentDiscover, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, mFragmentDiscover.getClass().getName());
                            }
                        })
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                            }
                        });
                mSweetAlertDialog.show();

            }
        });
        return fragmentView;
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
        mainActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        onResumeData();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        mCommonHelper.hideKeyboard(mainActivity);
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}