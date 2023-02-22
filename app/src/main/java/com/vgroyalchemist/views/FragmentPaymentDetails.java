package com.vgroyalchemist.views;

import android.annotation.SuppressLint;
import android.content.Context;
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
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

/* developed by krishna 2-04-2021
 *   payment method  */
public class FragmentPaymentDetails extends NonCartFragment {

    //Variable Declaration
    public static final String FRAGMENT_ID = "54";

    public static MainActivity mainActivity;
    public View fragmentView = null;
    CommonHelper mCommonHelper;
    ImageView mImageViewSearch;
    ImageView mImageViewCart;
    ImageView mImageViewBack;
    TextView mTextViewTitle;

    RadioGroup mRadioGroup;
    RadioButton mRadioButtonCod;
    RadioButton mRadioButtonRozarPay;

    TextView mTextViewConfrimeOrder;

    // userdata
    public SharedPreferences mSharedPreferencesUserId;
    SharedPreferences.Editor mEditor;

    String mStringUserId;
    String AddressID;
    String mStringSubtotal;
    String ShippingCharge;
    String GorssTotal;
    String CoupneDisc;
    String mStringDiscount;
    String JsonString;
    String mStringOrderId;
    boolean isOrder = false;

    Bundle mBundle;
    ArrayList mImageVOArrayList;
    static ArrayList<Bitmap> mArrayListGetImage;
    static Bitmap mBitmap;
    static String mStringImageBase64;

    String mStringSelectop;
    String mStringOrderStatus;

    ServerResponseVO mServerResponseVOChnageOrderStatus;
    ServerResponseVO mServerResponseVOPlaceOrder;
    PostParseGet mPostParseGet;
    String PaymentReceived;

    public SharedPreferences mSharedPreferencesLatLng;
    SharedPreferences.Editor mEditorLatLng;

    String mStringLatitude;
    String mStringLongitude;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = (MainActivity) getActivity();
        mCommonHelper = new CommonHelper();
        mPostParseGet = new PostParseGet(mainActivity);
        mImageVOArrayList = new ArrayList();
        mBundle = getArguments();

        if(mBundle != null)
        {
            AddressID= mBundle.getString("AddressID");
            mStringSubtotal = mBundle.getString("subtotal");
            ShippingCharge = mBundle.getString("shipping_charge");
            GorssTotal = mBundle.getString("GrossTotal");
            CoupneDisc = mBundle.getString("CoupneDisc");
            mStringDiscount = mBundle.getString("Discoiunt");
            mArrayListGetImage = (ArrayList<Bitmap>) getArguments().getSerializable("imagearray");
            mStringOrderId = mBundle.getString("OrderId");
            isOrder = mBundle.getBoolean("isOrder");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentView = inflater.inflate(R.layout.fragment_payment_details, container, false);

        mImageViewBack = fragmentView.findViewById(R.id.fragment_imageview_back);
        mTextViewTitle =fragmentView.findViewById(R.id.fragment_title_textview);
        mTextViewTitle.setText("Payment Method");
        mImageViewSearch =fragmentView.findViewById(R.id.fragment_Serach);
        mImageViewSearch.setVisibility(View.GONE);
        mImageViewCart =fragmentView.findViewById(R.id.fragment_cart);
        mImageViewCart.setVisibility(View.GONE);

        mRadioGroup = fragmentView.findViewById(R.id.fragment_payment_relative_title_payemt_mode_radioGroup);
        mRadioButtonCod = fragmentView.findViewById(R.id.fragment_payment_relative_title_payemt_mode_radio_cod);
        mRadioButtonRozarPay = fragmentView.findViewById(R.id.fragment_payment_relative_title_payemt_mode_radio_rozerPay);
        mTextViewConfrimeOrder = fragmentView.findViewById(R.id.fragment_order_Search_summary_textview_submit);

        mSharedPreferencesUserId =mainActivity.getSharedPreferences(Tags.TAG_SHARED_PREFERENCE_USER_ID ,0);
        mStringUserId = mSharedPreferencesUserId.getString("UserId","");
        mEditor =mSharedPreferencesUserId.edit();

        mSharedPreferencesLatLng = mainActivity.getSharedPreferences("LatLng",0);
        mStringLatitude = mSharedPreferencesLatLng.getString("Latitude","");
        mStringLongitude = mSharedPreferencesLatLng.getString("Longitude","");
        mEditorLatLng = mSharedPreferencesLatLng.edit();


        mImageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.onBackPressed();
            }
        });

        mRadioButtonRozarPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mStringSelectop ="RozarPay";
                mStringOrderStatus = "";
                PaymentReceived ="";
            }
        });

        mRadioButtonCod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStringSelectop = "COD";
                mStringOrderStatus ="1";
                PaymentReceived = "false";
            }
        });


        mTextViewConfrimeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mRadioButtonCod.isChecked() != true && mRadioButtonRozarPay.isChecked() != true)
                {
                    Snackbar.with(mainActivity).text("Please Select Payment Method").show(mainActivity);
                    return;
                }

                if(mStringSelectop.equalsIgnoreCase("COD"))
                {
                    if(isOrder == true)
                    {
                        ChangeOrderStatus();
                    }
                    else {
                        PlaceOrder();
                    }
                }
                else {
                    RozarPayMethod();
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

    public  void RozarPayMethod()
    {

        try {
            JSONObject jObj = new JSONObject();
            jObj.put("Usrid",mStringUserId);
//            jObj.put("AddressId",AddressID);
//            jObj.put("GrossRs",GorssTotal);
//            jObj.put("TotalRs",mStringSubtotal);
//            jObj.put("DeliveryCharge",ShippingCharge);
//            jObj.put("CoupenDisc",CoupneDisc);
//            jObj.put("Img",FragmentPaymentDetails.CreatePlaceOrderJson());
//            jObj.put("EmaiId","devanshipujara@gmail.com");
//            jObj.put("MobNo","9208572365");
//            jObj.put("Name","Devanshi pujara");
            jObj.put("Amount",GorssTotal);

            JsonString = jObj.toString();
            System.out.println("Vt json String--------->" +JsonString);



            String Url = Tags.BASEURL+"Payment/MakePaymentEntry?"+"UserId="+mStringUserId+"&"+"Amount="+GorssTotal;
            System.out.println("VT url -------->" + Url);
            FragmentFinalPaymentWebview fragmentFinalPaymentWebview = new FragmentFinalPaymentWebview();
            Bundle mBundle = new Bundle();
            mBundle.putString("url",Url);
            mBundle.putString("AddressID",AddressID);
            mBundle.putString("subtotal",mStringSubtotal);
            mBundle.putString("shipping_charge",ShippingCharge);
            mBundle.putString("GrossTotal",GorssTotal);
            mBundle.putString("CoupneDisc",CoupneDisc);
            mBundle.putString("Discoiunt",mStringDiscount);
            mBundle.putSerializable("imagearray",mArrayListGetImage);
            mBundle.putString("OrderId",mStringOrderId);
            fragmentFinalPaymentWebview.setArguments(mBundle);
            mainActivity.addFragment(fragmentFinalPaymentWebview, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, fragmentFinalPaymentWebview.getClass().getName());

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public static JSONObject CreatePlaceOrderJson() {

        JSONObject jsonObject = new JSONObject();

        try {
            try {
                int i = 0;
                while (i < mArrayListGetImage.size()) {
                    mBitmap = mArrayListGetImage.get(i);
                    ByteArrayOutputStream bao = new ByteArrayOutputStream();
                    mBitmap.compress(Bitmap.CompressFormat.JPEG, 70, bao);
                    byte[] ba = bao.toByteArray();
                    mStringImageBase64 = Base64.encodeToString(ba, Base64.DEFAULT);
                    i++;

                    jsonObject.put("VastnessTechnology"+ i, mStringImageBase64);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

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

                                    @SuppressLint("HandlerLeak") Handler handler = new Handler() {
                                        @Override
                                        public void handleMessage(Message msg) {
                                            if (msg.what == Tags.WHAT) {
                                                FragmentConfirmOrder mConfirmOrder = new FragmentConfirmOrder();
                                                Bundle mBundle = new Bundle();
                                                mBundle.putString("OrderNO",mServerResponseVOPlaceOrder.getOrderNo());
                                                mBundle.putBoolean("ISFromCart",true);
                                                mConfirmOrder.setArguments(mBundle);
                                                mainActivity.addFragment(mConfirmOrder, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, mConfirmOrder.getClass().getName());

                                                mainActivity.getSharedPreferences("LatLng", 0).edit().clear().apply();
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


    public class PlaceOrderDataExecutor extends TaskExecutor {
        protected PlaceOrderDataExecutor(Context context, Bundle bundle) {
            super(context, bundle);
        }
        @Override
        protected Object executeTask() {
            PlaceOrderService mGetAllCartService = new PlaceOrderService();
            try {
                mServerResponseVOPlaceOrder = mGetAllCartService.getAllCart(mainActivity, Tags.PLACEORDER, mStringUserId ,AddressID ,mStringSubtotal,ShippingCharge,GorssTotal,CoupneDisc,mStringDiscount,"Cash On Delivery",
                        "1",PaymentReceived,mImageVOArrayList ,mStringLatitude ,mStringLongitude);
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
                mServerResponseVOChnageOrderStatus = mGetAllCartService.getchnageOrderStatus(mainActivity, Tags.ChangeOrderStatus, mStringUserId ,mStringOrderId ,"Cash On Delivery",mStringOrderStatus);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}