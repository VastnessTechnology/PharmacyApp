package com.vgroyalchemist.views;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.vgroyalchemist.R;
import com.vgroyalchemist.controller.MainActivity;
import com.vgroyalchemist.utils.CommonHelper;
import com.potyvideo.library.AndExoPlayerView;
import im.delight.android.webview.AdvancedWebView;

/* developed by krishna 24-03-2021
 *  Articles Details */
public class FragmentArticlesDetails extends CartFragments implements AdvancedWebView.Listener {

    //Variable Declaration
    public static final String FRAGMENT_ID = "26";

    public static MainActivity mainActivity;
    public View fragmentView = null;
    CommonHelper mCommonHelper;
    ImageView mImageViewSearch;
    ImageView mImageViewCart;
    ImageView mImageViewBack;
    TextView mTextViewTitle;
    TextView mTextViewShortDescription;
    ImageView mImageViewPath;
    AdvancedWebView mWebView;

    RelativeLayout mRelativeLayoutWebView;
    String mStringDescription;
    String mStringShortDescription;
    String mImagePath;

    AndExoPlayerView andExoPlayerView;

    Bundle mBundle;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = (MainActivity) getActivity();
        mCommonHelper = new CommonHelper();

        mBundle =getArguments();

        if(mBundle != null)
        {
            mStringDescription = mBundle.getString("Description");
            mStringShortDescription = mBundle.getString("ShortDescription");
            mImagePath = mBundle.getString("ImagePath");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentView = inflater.inflate(R.layout.fragment_articles_details, container, false);

        mImageViewBack = fragmentView.findViewById(R.id.fragment_imageview_back);
        mTextViewTitle =fragmentView.findViewById(R.id.fragment_title_textview);
        mImageViewSearch =fragmentView.findViewById(R.id.fragment_Serach);
        mImageViewSearch.setVisibility(View.GONE);
        mImageViewCart =fragmentView.findViewById(R.id.fragment_cart);
        mImageViewCart.setVisibility(View.GONE);

        mTextViewTitle.setText("Articles Details");
        mWebView = fragmentView.findViewById(R.id.fragment_full_description_webview);

        mTextViewShortDescription = fragmentView.findViewById(R.id.fragment_article_shortDescription);
        mImageViewPath = fragmentView.findViewById(R.id.fragment_Artical_ImagePath);
        mRelativeLayoutWebView = fragmentView.findViewById(R.id.RelativeMain_Image);
        andExoPlayerView = fragmentView.findViewById(R.id.andExoPlayerView);
        if(mImagePath.endsWith(".mp4"))
        {
            andExoPlayerView.setVisibility(View.VISIBLE);
            mRelativeLayoutWebView.setVisibility(View.GONE);
        }

        else {
            andExoPlayerView.setVisibility(View.GONE);
           mRelativeLayoutWebView.setVisibility(View.VISIBLE);
        }

        andExoPlayerView.setShowFullScreen(true);
        andExoPlayerView.setPlayWhenReady(true);
        andExoPlayerView.setSource(mImagePath);



        mTextViewShortDescription.setText(mStringShortDescription);

        Glide.with(mainActivity).load(mImagePath)
                .placeholder(R.drawable.logo)
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .skipMemoryCache(true)
                .into(mImageViewPath);
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
    public void onPause() {
        super.onPause();
        if (andExoPlayerView != null) {
            Log.v("log_tag", "Pause ");
            andExoPlayerView.setPlayWhenReady(false);
            andExoPlayerView.stopPlayer();
        }
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

        if (andExoPlayerView != null) {
            Log.v("log_tag", "Destroy ");
            andExoPlayerView.setPlayWhenReady(false);
            andExoPlayerView.stopPlayer();
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
}