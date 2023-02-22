package com.vgroyalchemist.views;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.vgroyalchemist.R;
import com.vgroyalchemist.controller.MainActivity;
import com.vgroyalchemist.utils.CommonHelper;
import com.vgroyalchemist.utils.Tags;

/*  developed by krishna 24-03-2021
*  set notification setting */
public class FragmentPreference extends NonCartFragment {

    //Variable Declaration
    public static final String FRAGMENT_ID = "36";

    public static MainActivity mainActivity;
    public View fragmentView = null;
    CommonHelper mCommonHelper;

    TextView mTextViewSubmit;
    ImageView mImageViewBack;
    TextView mTextViewTitle;
    ImageView mImageViewSearch;
    ImageView mImageViewCart;

    Switch mASwitchToggale;

    String mStringPushNotification;


    private SharedPreferences mSharedPreferencesNotification;
    private SharedPreferences.Editor mEditorNotification;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = (MainActivity) getActivity();
        mCommonHelper = new CommonHelper();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentView = inflater.inflate(R.layout.fragment_preference, container, false);

        mImageViewBack = fragmentView.findViewById(R.id.fragment_imageview_back);
        mTextViewTitle = fragmentView.findViewById(R.id.fragment_title_textview);
        mTextViewTitle.setText("Preference");
        mImageViewSearch = fragmentView.findViewById(R.id.fragment_Serach);
        mImageViewSearch.setVisibility(View.GONE);
        mImageViewCart = fragmentView.findViewById(R.id.fragment_cart);
        mImageViewCart.setVisibility(View.GONE);

        mASwitchToggale = fragmentView.findViewById(R.id.fragment_preference_togglebutton_promotional);


        mSharedPreferencesNotification = mainActivity.getSharedPreferences(Tags.TAG_SHARED_PREFERENCE_PUSH_NOTIFICATION, 0);
        mStringPushNotification = mSharedPreferencesNotification.getString(Tags.PUSH_NOTIFICATION_KEY, "Y");
        mEditorNotification = mSharedPreferencesNotification.edit();
        mEditorNotification.putString(Tags.PUSH_NOTIFICATION_KEY, mStringPushNotification);
        mEditorNotification.commit();

        mASwitchToggale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mASwitchToggale.isChecked() == true) {
                    mStringPushNotification = "Y";
                    mEditorNotification.putString(Tags.PUSH_NOTIFICATION_KEY, mStringPushNotification);
                    mEditorNotification.commit();
                } else {
                    mStringPushNotification = "N";
                    mEditorNotification.putString(Tags.PUSH_NOTIFICATION_KEY, mStringPushNotification);
                    mEditorNotification.commit();
                }
            }
        });

        mImageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.onBackPressed();
            }
        });

        if (!mStringPushNotification.equalsIgnoreCase("")) {
            if (mStringPushNotification.equalsIgnoreCase("Y")) {
                mASwitchToggale.setChecked(true);
            } else {
                mASwitchToggale.setChecked(false);
            }
        }


        return fragmentView;

    }

    public void onResumeData() {
        if(mainActivity.toolbar != null) {
            mainActivity.toolbar.setVisibility(View.GONE);
        }

    }

    @Override
    public void doWork() {
        super.doWork();
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
}