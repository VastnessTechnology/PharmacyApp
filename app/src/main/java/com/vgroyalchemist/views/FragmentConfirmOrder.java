package com.vgroyalchemist.views;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.vgroyalchemist.R;
import com.vgroyalchemist.controller.MainActivity;
import com.vgroyalchemist.utils.CommonHelper;

//developed by Krishna 11-03-2021
// Order Confrim
public class FragmentConfirmOrder extends NonCartFragment {


    //Variable Declaration
    public static final String FRAGMENT_ID = "20";

    public static MainActivity mainActivity;
    public View fragmentView = null;
    CommonHelper mCommonHelper;

    ImageView mImageViewBack;
    TextView mTextViewTitle;
    ImageView mImageViewSearch;
    ImageView mImageViewCart;

    TextView mTextViewOrderId;
    Button mButtonCountinue;

    Bundle mBundle;
    String mStringOrderNo;
    boolean ISFromCart;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = (MainActivity) getActivity();
        mCommonHelper = new CommonHelper();

        mBundle = getArguments();
        if(mBundle != null)
        {
            mStringOrderNo =mBundle.getString("OrderNO");
            ISFromCart = mBundle.getBoolean("ISFromCart");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentView= inflater.inflate(R.layout.fragment_confirm_order, container, false);

        mImageViewBack = fragmentView.findViewById(R.id.fragment_imageview_back);
        mImageViewBack.setVisibility(View.GONE);
        mTextViewTitle =fragmentView.findViewById(R.id.fragment_title_textview);
        mTextViewTitle.setText("Order Confirm");
        mImageViewSearch =fragmentView.findViewById(R.id.fragment_Serach);
        mImageViewSearch.setVisibility(View.GONE);
        mImageViewCart =fragmentView.findViewById(R.id.fragment_cart);
        mImageViewCart.setVisibility(View.GONE);
        mTextViewOrderId=fragmentView.findViewById(R.id.fragment_confirmation_order_id);
        mButtonCountinue= fragmentView.findViewById(R.id.fragment_confirmation_Countinue);


        if(ISFromCart == true)
        {
            mTextViewOrderId.setVisibility(View.VISIBLE);
        }
        else {
            mTextViewOrderId.setVisibility(View.GONE);
        }
        if(mStringOrderNo != null)
        {
            mTextViewOrderId.setText("Order Id :" + mStringOrderNo);
        }
        mButtonCountinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.RemoveAllFragment();
                FragmentTabActivity fragmentTabActivity = new FragmentTabActivity();
                mainActivity.addFragment(fragmentTabActivity, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, fragmentTabActivity.getClass().getName());
            }
        });
        return  fragmentView;

    }

    public void onResumeData() {
        mainActivity.toolbar.setVisibility(View.GONE);
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