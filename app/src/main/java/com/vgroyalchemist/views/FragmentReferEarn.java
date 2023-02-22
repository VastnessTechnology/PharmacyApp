package com.vgroyalchemist.views;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.vgroyalchemist.CommonClass.LinkBuilder;
import com.vgroyalchemist.R;
import com.vgroyalchemist.controller.MainActivity;
import com.vgroyalchemist.utils.CommonHelper;
import com.vgroyalchemist.utils.Tags;


public class FragmentReferEarn extends NonCartFragment {

    //Variable Declaration
    public static final String FRAGMENT_ID = "57";

    public static MainActivity mainActivity;
    public View fragmentView = null;
    CommonHelper mCommonHelper;

    ImageView mImageViewBack;
    TextView mTextViewTitle;
    ImageView mImageViewSearch;
    ImageView mImageViewCart;

    TextView mTextViewCode;
    TextView mTextViewShareCode;


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
        fragmentView = inflater.inflate(R.layout.fragment_refer_earn, container, false);

        mImageViewBack = fragmentView.findViewById(R.id.fragment_imageview_back);
        mTextViewTitle = fragmentView.findViewById(R.id.fragment_title_textview);
        mTextViewTitle.setText("Refer & Earn");
        mImageViewSearch = fragmentView.findViewById(R.id.fragment_Serach);
        mImageViewSearch.setVisibility(View.GONE);
        mImageViewCart = fragmentView.findViewById(R.id.fragment_cart);
        mImageViewCart.setVisibility(View.GONE);


        mTextViewCode = fragmentView.findViewById(R.id.fragment_ReferEarn_textview_code);
        mTextViewShareCode = fragmentView.findViewById(R.id.fragment_ReferEarn_textview_share_code);


        mTextViewShareCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
//

                    String link = "https://pharmacyapp.com/?invitedby=" + "Refrl";
                    System.out.println("VT share link ------->" + link);


                    String builtLink = new LinkBuilder()
                            .setLink(link)
                            .setDomain(getString(R.string.deep_link_host))
                            .setSt(getString(R.string.app_name))
                            .setSd(getString(R.string.app_name))
                            .setApn(Tags.PACKAGENAME)
                            .setIbi("com.vgroyalchemist")
//                            .setIsi("1479028718")
                            .build();

                    DynamicLink.Builder builder = FirebaseDynamicLinks.getInstance().createDynamicLink()
                            .setLongLink(Uri.parse(builtLink));

                    builder.buildShortDynamicLink()
                            .addOnSuccessListener(mainActivity, new OnSuccessListener<ShortDynamicLink>() {
                                @Override
                                public void onSuccess(ShortDynamicLink shortDynamicLink) {

                                    Uri uri = shortDynamicLink.getShortLink();
                                    String Sharelink = uri.toString();

                                    System.out.println("VT short link ---->" + uri);

                                    Intent share = new Intent(Intent.ACTION_SEND);
                                    share.setType("text/plain");
                                    share.putExtra(Intent.EXTRA_TEXT, Sharelink);
                                    startActivity(Intent.createChooser(share, "Share Link"));
                                }
                            });
                } catch (Exception e) {
                    e.toString();
                }
            }
        });

        return fragmentView;
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