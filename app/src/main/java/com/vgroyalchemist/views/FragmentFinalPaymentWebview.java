package com.vgroyalchemist.views;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.vgroyalchemist.R;
import com.vgroyalchemist.controller.MainActivity;
import com.vgroyalchemist.loaders.LoaderTask;
import com.vgroyalchemist.loaders.TaskExecutor;
import com.vgroyalchemist.utils.CommonHelper;
import com.vgroyalchemist.utils.Snackbar;
import com.vgroyalchemist.utils.Tags;
import com.vgroyalchemist.utils.Utility;
import com.vgroyalchemist.vos.ServerResponseVO;
import com.vgroyalchemist.webservice.ChangeOrderStatusService;
import com.vgroyalchemist.webservice.PlaceOrderService;
import com.vgroyalchemist.webservice.PostParseGet;

import java.util.ArrayList;

/* developed by krishna 2-04-2021
 *   payment webview */
public class FragmentFinalPaymentWebview extends NonCartFragment {


    //Variable Declaration
    public static final String FRAGMENT_ID = "55";

    public static MainActivity mainActivity;
    CommonHelper mCommonHelper;
    public View fragmentView = null;


    ProgressBar mProgressBar;
    WebView mWebView;

    Bundle mBundle;
    String mStringUrl;
    String AddressID;
    String mStringSubtotal;
    String ShippingCharge;
    String GorssTotal;
    String CoupneDisc;
    String mStringDiscount;
    String OrderStatus;
    String mStringOrderId;
    String mStringOrderStatus;
    String PaymentReceived;

    // userdata
    public SharedPreferences mSharedPreferencesUserId;
    SharedPreferences.Editor mEditor;

    String mStringUserId;

    ArrayList mImageVOArrayList;
    static ArrayList<Bitmap> mArrayListGetImage;
    static Bitmap mBitmap;
    static String mStringImageBase64;

    ServerResponseVO mServerResponseVOPlaceOrder;
    ServerResponseVO mServerResponseVOChnageOrderStatus;
    PostParseGet mPostParseGet;


    public SharedPreferences mSharedPreferencesLatLng;
    SharedPreferences.Editor mEditorLatLng;
    String mStringLatitude;
    String mStringLongitude;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = (MainActivity) getActivity();
        mCommonHelper = new CommonHelper();
        mImageVOArrayList = new ArrayList();
        mPostParseGet = new PostParseGet(mainActivity);
        mBundle = getArguments();

        if(mBundle != null)
        {
            mStringUrl = mBundle.getString("url");
            AddressID= mBundle.getString("AddressID");
            mStringSubtotal = mBundle.getString("subtotal");
            ShippingCharge = mBundle.getString("shipping_charge");
            GorssTotal = mBundle.getString("GrossTotal");
            CoupneDisc = mBundle.getString("CoupneDisc");
            mStringDiscount = mBundle.getString("Discoiunt");
            mArrayListGetImage = (ArrayList<Bitmap>) getArguments().getSerializable("imagearray");
            mStringOrderId = mBundle.getString("OrderId");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentView = inflater.inflate(R.layout.fragment_final_payment_webview, container, false);

        mProgressBar = fragmentView.findViewById(R.id.fragment_payment_webview_webview_progressbar);
        mWebView = fragmentView.findViewById(R.id.fragment_payment_webview);

        mSharedPreferencesUserId =mainActivity.getSharedPreferences(Tags.TAG_SHARED_PREFERENCE_USER_ID ,0);
        mStringUserId = mSharedPreferencesUserId.getString("UserId","");
        mEditor =mSharedPreferencesUserId.edit();

        mSharedPreferencesLatLng = mainActivity.getSharedPreferences("LatLng",0);
        mStringLatitude = mSharedPreferencesLatLng.getString("Latitude","");
        mStringLongitude = mSharedPreferencesLatLng.getString("Longitude","");
        mEditorLatLng = mSharedPreferencesLatLng.edit();

        mWebView.setVisibility(View.VISIBLE);

        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setAppCacheEnabled(true);
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mWebView.setWebViewClient(webViewClient);
        mWebView.loadUrl(mStringUrl);

        return fragmentView;
    }


    public WebViewClient webViewClient = new WebViewClient() {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {

            mProgressBar.setVisibility(View.GONE);
            mWebView.setVisibility(View.VISIBLE);
            Utility.debugger("VT url..." + url);

            if(url.equalsIgnoreCase(Tags.BASEURL + "Payment/PaymentSuccess"))
            {
                mWebView.setVisibility(View.GONE);
                OrderStatus = "1";
                PaymentReceived = "true";
                if(mStringOrderId != null)
                {
                    ChangeOrderStatus();
                }
                else {

                    PlaceOrder();
                }

            }
            else if(url.equalsIgnoreCase(Tags.BASEURL + "Payment/PaymentFailed")){


                mWebView.setVisibility(View.GONE);
                OrderStatus = "8";
                PaymentReceived ="false";
                if(mStringOrderId != null)
                {
                    ChangeOrderStatus();
                }
                else {

                    PlaceOrder();
                }


//                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mainActivity);
//                alertDialogBuilder.setMessage("Your Payment Failed Please Try Again...!!");
//                alertDialogBuilder.setPositiveButton("Ok",
//                        new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface arg0, int arg1) {
//                                mainActivity.removeAllBackFragments();
//                                FragmentHomeNewTheme mFragmentHomeNewTheme = new FragmentHomeNewTheme();
//                                mainActivity.addFragment(mFragmentHomeNewTheme, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, mFragmentHomeNewTheme.getClass().getName());
//                            }
//                        });
//                AlertDialog alertDialog = alertDialogBuilder.create();
//                alertDialog.show();
            }

            else if(url.equalsIgnoreCase(Tags.BASEURL + "Payment/PaymentCancel"))
            {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mainActivity);
                alertDialogBuilder.setMessage("Your Payment Failed Please Try Again...!!");
                alertDialogBuilder.setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                mainActivity.RemoveAllFragment();
                                FragmentTabActivity fragmentTabActivity = new FragmentTabActivity();
                                mainActivity.addFragment(fragmentTabActivity, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, fragmentTabActivity.getClass().getName());

//                                mainActivity.removeAllBackFragments();
//                                FragmentHomeNewTheme mFragmentHomeNewTheme = new FragmentHomeNewTheme();
//                                mainActivity.addFragment(mFragmentHomeNewTheme, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, mFragmentHomeNewTheme.getClass().getName());
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
//            if (url.contains("paymentId=")) {
//                Utility.debugger("Vt url in iff...");
//                mWebView.setVisibility(View.GONE);
//            } else {
//                Utility.debugger("Vt url in else...");
//                mWebView.setVisibility(View.VISIBLE);
//            }
//            mWebView.loadUrl("javascript:window.HTMLOUT.processHTML(document.getElementsByTagName('body')[0].innerHTML);");
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

    @SuppressLint("HandlerLeak")
    //  place order api code
    public void PlaceOrder() {
        getLoaderManager().restartLoader(0, null, new LoaderManager.LoaderCallbacks<TaskExecutor>() {
            @Override
            public Loader<TaskExecutor> onCreateLoader(int arg0, Bundle arg1) {
                PlaceOrderDataExecutor mPlaceOrderDataExecutor = new PlaceOrderDataExecutor(mainActivity, null);
                return new LoaderTask<TaskExecutor>(mainActivity, mPlaceOrderDataExecutor);
            }
            @Override
            public void onLoadFinished(Loader<TaskExecutor> arg0, TaskExecutor arg1) {
                if (isAdded()) {
                    getLoaderManager().destroyLoader(0);
                    if (mPostParseGet.isNetError) {
                        Snackbar.with(mainActivity).text(getResources().getString(R.string.interneterror)).show(mainActivity);
                    } else if (mPostParseGet.isOtherError)
                        Snackbar.with(mainActivity).text(getResources().getString(R.string.notabletocommunicatewithserver)).show(mainActivity);
                    else {
                        if (mServerResponseVOPlaceOrder != null && mServerResponseVOPlaceOrder.getStatus() != null) {

                            if (mServerResponseVOPlaceOrder.getStatus().equalsIgnoreCase("true")) {

                                if (mServerResponseVOPlaceOrder.getData() != null) {

                                    if(mServerResponseVOPlaceOrder.getOrderStatus().equalsIgnoreCase("1")) {

                                        Handler handler = new Handler() {
                                            @Override
                                            public void handleMessage(Message msg) {
                                                if (msg.what == Tags.WHAT) {
                                                    FragmentConfirmOrder mConfirmOrder = new FragmentConfirmOrder();
                                                    Bundle mBundle = new Bundle();
                                                    mBundle.putString("OrderNO", mServerResponseVOPlaceOrder.getOrderNo());
                                                    mBundle.putBoolean("ISFromCart", true);
                                                    mConfirmOrder.setArguments(mBundle);
                                                    mainActivity.addFragment(mConfirmOrder, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, mConfirmOrder.getClass().getName());
                                                    mainActivity.getSharedPreferences("LatLng", 0).edit().clear().apply();
                                                }
                                            }
                                        };
                                        handler.sendEmptyMessage(Tags.WHAT);
                                    }
                                    else if(mServerResponseVOPlaceOrder.getOrderStatus().equalsIgnoreCase("8"))
                                    {

                                        Handler handler = new Handler() {
                                            @Override
                                            public void handleMessage(Message msg) {
                                                if (msg.what == Tags.WHAT) {
                                                    mainActivity.removeAllBackFragments();
                                                    FragmentMyOrderList myOrderList = new FragmentMyOrderList();
                                                    mainActivity.addFragment(myOrderList, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, myOrderList.getClass().getName());
                                                }
                                            }
                                        };
                                        handler.sendEmptyMessage(Tags.WHAT);
                                    }
                                }
                            } else {
                                Snackbar.with(mainActivity).text(mServerResponseVOPlaceOrder.getMsg()).show(mainActivity);
                                mCommonHelper.hideKeyboard(mainActivity);
                            }
                        }
                    }
                }
            }
            @Override
            public void onLoaderReset(Loader<TaskExecutor> arg0) {
            }
        });
    }


    public class PlaceOrderDataExecutor extends TaskExecutor {
        protected PlaceOrderDataExecutor(Context context, Bundle bundle) {
            super(context, bundle);
        }
        @Override
        protected Object executeTask() {
            PlaceOrderService mGetAllCartService = new PlaceOrderService();
            try {
                mServerResponseVOPlaceOrder = mGetAllCartService.getAllCart(mainActivity, Tags.PLACEORDER, mStringUserId ,AddressID ,mStringSubtotal,ShippingCharge,GorssTotal,CoupneDisc,mStringDiscount,
                        "RazorPay",OrderStatus,
                        PaymentReceived,mImageVOArrayList,mStringLatitude ,mStringLongitude);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }


    //    chageorder status
    public void ChangeOrderStatus() {
        getLoaderManager().restartLoader(0, null, new LoaderManager.LoaderCallbacks<TaskExecutor>() {
            @Override
            public Loader<TaskExecutor> onCreateLoader(int arg0, Bundle arg1) {
                ChangeOrderStatusExecutor mChangeOrderStatusExecutor = new ChangeOrderStatusExecutor(mainActivity, null);
                return new LoaderTask<TaskExecutor>(mainActivity, mChangeOrderStatusExecutor);
            }
            @Override
            public void onLoadFinished(Loader<TaskExecutor> arg0, TaskExecutor arg1) {
                if (isAdded()) {
                    getLoaderManager().destroyLoader(0);
                    if (mPostParseGet.isNetError) {
                        Snackbar.with(mainActivity).text(getResources().getString(R.string.interneterror)).show(mainActivity);
                    } else if (mPostParseGet.isOtherError)
                        Snackbar.with(mainActivity).text(getResources().getString(R.string.notabletocommunicatewithserver)).show(mainActivity);
                    else {
                        if (mServerResponseVOChnageOrderStatus != null && mServerResponseVOChnageOrderStatus.getStatus() != null) {

                            if (mServerResponseVOChnageOrderStatus.getStatus().equalsIgnoreCase("true")) {

                                if (mServerResponseVOChnageOrderStatus.getData() != null) {

                                    @SuppressLint("HandlerLeak") Handler handler = new Handler() {
                                        @Override
                                        public void handleMessage(Message msg) {
                                            if (msg.what == Tags.WHAT) {
                                                mainActivity.removeAllBackFragments();
                                                FragmentMyOrderList myOrderList = new FragmentMyOrderList();
                                                mainActivity.addFragment(myOrderList, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, myOrderList.getClass().getName());
                                            }
                                        }
                                    };
                                    handler.sendEmptyMessage(Tags.WHAT);
                                }
                            } else {
                                Snackbar.with(mainActivity).text(mServerResponseVOPlaceOrder.getMsg()).show(mainActivity);
                                mCommonHelper.hideKeyboard(mainActivity);
                            }
                        }
                    }
                }
            }
            @Override
            public void onLoaderReset(Loader<TaskExecutor> arg0) {
            }
        });
    }


    public class ChangeOrderStatusExecutor extends TaskExecutor {
        protected ChangeOrderStatusExecutor(Context context, Bundle bundle) {
            super(context, bundle);
        }
        @Override
        protected Object executeTask() {
            ChangeOrderStatusService mGetAllCartService = new ChangeOrderStatusService();
            try {
                mServerResponseVOChnageOrderStatus = mGetAllCartService.getchnageOrderStatus(mainActivity, Tags.ChangeOrderStatus, mStringUserId ,mStringOrderId ,"RazorPay",OrderStatus);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}