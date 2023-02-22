package com.vgroyalchemist.views;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.vgroyalchemist.R;
import com.vgroyalchemist.controller.MainActivity;
import com.vgroyalchemist.utils.CommonHelper;
import com.vgroyalchemist.utils.Utility;

/* developed by krishna 24-03-2021
*   load track order url */
public class FragmentTrackOrderLink extends NonCartFragment {

    //Variable Declaration
    public static final String FRAGMENT_ID = "21";

    public static MainActivity mainActivity;
    public View fragmentView = null;
    CommonHelper mCommonHelper;

    ImageView mImageViewBack;
    TextView mTextViewTitle;
    ImageView mImageViewSearch;
    ImageView mImageViewCart;

    ProgressBar mProgressBar;
    WebView mWebViewTrack;
    Bundle mBundle;
    String mStringTrackOrder;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = (MainActivity) getActivity();
        mCommonHelper = new CommonHelper();

        mBundle = getArguments();
        if(mBundle != null)
        {
            mStringTrackOrder = mBundle.getString("TrackLink");
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentView= inflater.inflate(R.layout.fragment_track_order, container, false);

        mImageViewBack = fragmentView.findViewById(R.id.fragment_imageview_back);
        mTextViewTitle =fragmentView.findViewById(R.id.fragment_title_textview);
        mTextViewTitle.setText("Track Order");
        mImageViewSearch =fragmentView.findViewById(R.id.fragment_Serach);
        mImageViewSearch.setVisibility(View.GONE);
        mImageViewCart =fragmentView.findViewById(R.id.fragment_cart);
        mImageViewCart.setVisibility(View.GONE);
        mWebViewTrack = fragmentView.findViewById(R.id.fragment_track_order_webview);
        mProgressBar =fragmentView.findViewById(R.id.fragment_track_order_progressBar);


        mImageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mainActivity.onBackPressed();
            }
        });

        WebSettings settings = mWebViewTrack.getSettings();
        settings.setDomStorageEnabled(true);

        mWebViewTrack.getSettings().setJavaScriptEnabled(true);
        mWebViewTrack.getSettings().setAppCacheEnabled(true);
        mWebViewTrack.getSettings().setDomStorageEnabled(true);
        mWebViewTrack.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mWebViewTrack.setWebViewClient(webViewClient);
        mWebViewTrack.clearCache(true);
        mWebViewTrack.loadUrl(mStringTrackOrder);

//        mWebViewTrack.loadData(mStringTrackOrder, "html", "UTF-8");


        return  fragmentView;

    }

    public WebViewClient webViewClient = new WebViewClient() {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {

            Utility.debugger("Vt url..." + url);
            mProgressBar.setVisibility(View.GONE);
            mWebViewTrack.setVisibility(View.VISIBLE);
        }
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            Toast.makeText(getActivity(), "Oh no! " + description, Toast.LENGTH_SHORT).show();
        }

    };

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