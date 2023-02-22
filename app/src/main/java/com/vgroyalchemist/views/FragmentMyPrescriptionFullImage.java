package com.vgroyalchemist.views;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.vgroyalchemist.R;
import com.vgroyalchemist.controller.MainActivity;
import com.vgroyalchemist.utils.CommonHelper;

import it.sephiroth.android.library.imagezoom.ImageViewTouch;
import it.sephiroth.android.library.imagezoom.ImageViewTouchBase;

/*  developed by krishna 24-03-2021
*  uploaded prescription click display full image  */
public class FragmentMyPrescriptionFullImage extends NonCartFragment {

    public static final String FRAGMENT_ID = "38";

    public static MainActivity mainActivity;
    CommonHelper mCommonHelper;
    View fragmnetview = null;

    ImageView mImageViewBack;
    TextView mTextViewTitle;
    ImageView mImageViewSearch;
    ImageView mImageViewCart;
    ImageViewTouch mImageView;

    Bundle mBundle ;

    String mStringImagePath;
    String mStringImage;
    boolean Order;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainActivity = (MainActivity) getActivity();
        mCommonHelper = new CommonHelper();

        mBundle = getArguments();

        if(mBundle != null)
        {
            mStringImagePath = mBundle.getString("seletedImageName");
            mStringImage = mBundle.getString("ImageUrl");
            Order = mBundle.getBoolean("orderpre");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmnetview = inflater.inflate(R.layout.fragment_my_prescription_full_image, container, false);

        mImageViewBack = fragmnetview.findViewById(R.id.fragment_imageview_back);
        mTextViewTitle = fragmnetview.findViewById(R.id.fragment_title_textview);
        mImageViewSearch = fragmnetview.findViewById(R.id.fragment_Serach);
        mImageViewSearch.setVisibility(View.GONE);
        mImageViewCart = fragmnetview.findViewById(R.id.fragment_cart);
        mImageViewCart.setVisibility(View.GONE);
        mTextViewTitle.setText("Image");
        mImageView = fragmnetview.findViewById(R.id.fragment_full_bigimageview);
//        mImageView.setDisplayType(ImageViewTouchBase.DisplayType.FIT_TO_SCREEN);



        if(Order == true)
        {
            Glide.with(mainActivity)
                    .load(mStringImage)
                    .into(mImageView);
        }
        else {
            if (mStringImagePath != null) {
                Glide.with(mainActivity)
                        .load(mStringImagePath)
                        .into(mImageView);
            }

        }

        mImageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.onBackPressed();
            }
        });
        return  fragmnetview;
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
}