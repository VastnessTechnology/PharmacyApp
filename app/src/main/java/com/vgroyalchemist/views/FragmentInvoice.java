package com.vgroyalchemist.views;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.vgroyalchemist.R;
import com.vgroyalchemist.controller.MainActivity;
import com.vgroyalchemist.utils.CommonHelper;
import com.vgroyalchemist.utils.Utility;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/* developed by krishna 24-03-2021
 *  Display invoice pdf */
public class FragmentInvoice extends NonCartFragment {

    //Variable Declaration
    public static final String FRAGMENT_ID = "50";

    public static MainActivity mainActivity;
    public View fragmentView = null;
    CommonHelper mCommonHelper;

    ImageView mImageViewBack;
    TextView mTextViewTitle;
    ImageView mImageViewSearch;
    ImageView mImageViewCart;

    WebView mWebViewInvoice;
    ProgressBar mProgressBar;

    Bundle mBundle;
    String mStringInvoice;
    ProgressDialog pDialog;
    boolean checkOnPageStartedCalled = false;

    PDFView pdfView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = (MainActivity) getActivity();
        mCommonHelper = new CommonHelper();

        mBundle = getArguments();
        if (mBundle != null) {
            mStringInvoice = mBundle.getString("invoice");

        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentView = inflater.inflate(R.layout.fragment_invoice, container, false);


        mImageViewBack = fragmentView.findViewById(R.id.fragment_imageview_back);
        mTextViewTitle = fragmentView.findViewById(R.id.fragment_title_textview);
        mTextViewTitle.setText("Invoice");
        mImageViewSearch = fragmentView.findViewById(R.id.fragment_Serach);
        mImageViewSearch.setVisibility(View.GONE);
        mImageViewCart = fragmentView.findViewById(R.id.fragment_cart);
        mImageViewCart.setVisibility(View.GONE);

        pdfView = fragmentView.findViewById(R.id.idPDFView);
        pdfView.setVisibility(View.VISIBLE);
        new RetrivePDFfromUrl().execute(mStringInvoice + ".pdf");


//        mWebViewInvoice = fragmentView.findViewById(R.id.fragment_invoice_webview);
        mProgressBar = fragmentView.findViewById(R.id.fragment_invoice_progressBar);


        mImageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mainActivity.onBackPressed();

            }
        });

//        pDialog = new ProgressDialog(mainActivity);
//        pDialog.setMessage("Please wait...");
//        pDialog.setCancelable(false);
//
//        WebSettings settings = mWebViewInvoice.getSettings();
//        settings.setDomStorageEnabled(true);
//
//        mWebViewInvoice.getSettings().setJavaScriptEnabled(true);
//        mWebViewInvoice.getSettings().setAppCacheEnabled(true);
//        mWebViewInvoice.getSettings().setSupportZoom(true);
//        mWebViewInvoice.setWebViewClient(webViewClient);
//        mWebViewInvoice.getSettings().setDomStorageEnabled(true);
//        mWebViewInvoice.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
//
//
//        mWebViewInvoice.loadUrl("https://docs.google.com/viewer?embedded=true&url=" + mStringInvoice + ".pdf");


        return fragmentView;
    }


    // create an async task class for loading pdf file from URL.


    public class RetrivePDFfromUrl extends AsyncTask<String, Void, InputStream> {




        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected InputStream doInBackground(String... args) {
            InputStream inputStream = null;
            try {
                URL url = new URL(args[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoInput(true);
                urlConnection.connect();
                inputStream = urlConnection.getInputStream();

                if (urlConnection.getResponseCode() == 200) {
                    // response is success.
                    // we are getting input stream from url
                    // and storing it in our variable.
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                }


            } catch (Exception e) {
                e.printStackTrace();

            }
            return inputStream;
        }

        @Override
        protected void onPostExecute(InputStream inputStream) {
            // after the execution of our async
            // task we are loading our pdf in our pdf view.
            pdfView.fromStream(inputStream).load();
        }


    }

//    public WebViewClient webViewClient = new WebViewClient() {
//
//        @Override
//        public void onPageStarted(WebView view, String url, Bitmap favicon) {
//
//            super.onPageStarted(view, url, favicon);
//
//
//            pDialog.show();
//
//        }
//
//        @Override
//        public boolean shouldOverrideUrlLoading(WebView view, String url) {
//
//            view.loadUrl(url);
//            return false;
//        }
//
//        @Override
//        public void onPageFinished(WebView view, String url) {
//
//            Utility.debugger("Vt url invoice..." + url);
//            hidepDialog();
//            mWebViewInvoice.setVisibility(View.VISIBLE);
//
//        }
//
//        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
//            Toast.makeText(getActivity(), "Oh no! " + description, Toast.LENGTH_SHORT).show();
//        }
//
//    };
//
//    private void hidepDialog() {
//        if (pDialog.isShowing())
//            pDialog.dismiss();
//    }

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