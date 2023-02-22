package com.vgroyalchemist.views;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.vgroyalchemist.R;
import com.vgroyalchemist.controller.MainActivity;
import com.vgroyalchemist.utils.CommonHelper;
import com.vgroyalchemist.utils.Tags;
import com.vgroyalchemist.utils.Utility;

import im.delight.android.webview.AdvancedWebView;

/* developed by krishna 24-03-2021
*  legal data display full description  */
public class FragmentContentFullDescription extends NonCartFragment implements AdvancedWebView.Listener{


    public static final String FRAGMENT_ID = "49";

    public static MainActivity mainActivity;
    CommonHelper mCommonHelper;
    View fragmnetview = null;

    ImageView mImageViewBack;
    TextView mTextViewTitle;
    ImageView mImageViewSearch;
    ImageView mImageViewCart;

    AdvancedWebView mWebView;

    String mStringDescription;
    String mStringPrivcy_policy;
    String  mStringCondition;
    String mStringRefund;
    Bundle mBundle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = (MainActivity) getActivity();
        mCommonHelper = new CommonHelper();
        mBundle = getArguments();
        if(mBundle != null)
        {
            mStringDescription = mBundle.getString("LegalDetails");
            mStringPrivcy_policy = mBundle.getString("privcy");
            mStringCondition = mBundle.getString("TermCondition");
            mStringRefund = mBundle.getString("returnfund");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmnetview = inflater.inflate(R.layout.fragment_content_full_description, container, false);

        mImageViewBack = fragmnetview.findViewById(R.id.fragment_imageview_back);
        mTextViewTitle =fragmnetview.findViewById(R.id.fragment_title_textview);
        mImageViewSearch =fragmnetview.findViewById(R.id.fragment_Serach);
        mImageViewSearch.setVisibility(View.GONE);
        mImageViewCart =fragmnetview.findViewById(R.id.fragment_cart);
        mImageViewCart.setVisibility(View.GONE);

        mWebView = fragmnetview.findViewById(R.id.fragment_full_description_webview);

        if(mStringPrivcy_policy != null)
        {
            mTextViewTitle.setText(getResources().getString(R.string.privacy_policy));
        }
        if(mStringCondition != null)
        {
            mTextViewTitle.setText(getResources().getString(R.string.terms_condition));
        }
        if(mStringRefund != null)
        {
            mTextViewTitle.setText(getResources().getString(R.string.return_policy));
        }
        mImageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.onBackPressed();
            }
        });



        mWebView.setListener(getActivity(), this);
        mWebView.setGeolocationEnabled(false);
        mWebView.setMixedContentAllowed(false);
        mWebView.setCookiesEnabled(true);
        mWebView.setThirdPartyCookiesEnabled(true);
        mWebView.setFitsSystemWindows(true);
        mWebView.setHorizontalScrollbarOverlay(false);
        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
//                Toast.makeText(getActivity(), "Finished loading", Toast.LENGTH_SHORT).show();


            }

        });
        mWebView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
//                Toast.makeText(getActivity(), title, Toast.LENGTH_SHORT).show();
            }

        });
        mWebView.loadHtml(mStringDescription);


        return fragmnetview;
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
        if (mWebView != null) {
            try {
                Class.forName("android.webkit.WebView").getMethod("onPause", (Class[]) null).invoke(mWebView, (Object[]) null);
            } catch (Exception e) {
            }
            mWebView.clearView();
            mWebView.stopLoading();
        }
    }

    @Override
    public void onPageStarted(String url, Bitmap favicon) {

    }

    @Override
    public void onPageFinished(String url) {

    }

    @Override
    public void onPageError(int errorCode, String description, String failingUrl) {

    }

    @Override
    public void onDownloadRequested(String url, String suggestedFilename, String mimeType, long contentLength, String contentDisposition, String userAgent) {

    }

    @Override
    public void onExternalPageRequest(String url) {

    }
    public static FragmentContentFullDescription newInstance() {
        FragmentContentFullDescription fragmentContentFullDescription = new FragmentContentFullDescription();
        return fragmentContentFullDescription;
    }

}