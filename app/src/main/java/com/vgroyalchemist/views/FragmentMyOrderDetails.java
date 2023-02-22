package com.vgroyalchemist.views;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentTransaction;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.vgroyalchemist.R;
import com.vgroyalchemist.controller.MainActivity;
import com.vgroyalchemist.loaders.LoaderTask;
import com.vgroyalchemist.loaders.TaskExecutor;
import com.vgroyalchemist.utils.CommonHelper;
import com.vgroyalchemist.utils.Snackbar;
import com.vgroyalchemist.utils.Tags;
import com.vgroyalchemist.utils.Utility;
import com.vgroyalchemist.vos.OrderDetailsVO;
import com.vgroyalchemist.vos.OrderItemListVO;
import com.vgroyalchemist.vos.ServerResponseVO;
import com.vgroyalchemist.webservice.CancleOrderService;
import com.vgroyalchemist.webservice.OrderDeatilsService;
import com.vgroyalchemist.webservice.PostParseGet;
import com.vgroyalchemist.webservice.ReOrderService;
import com.vgroyalchemist.webservice.SendMessageService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/*  developed by krishna 24-03-2021
 *   order details page */
public class FragmentMyOrderDetails extends NonCartFragment {


    //Variable Declaration
    public static final String FRAGMENT_ID = "18";

    public MainActivity mainActivity;
    public View fragmentView = null;
    CommonHelper mCommonHelper;

    ImageView mImageViewBack;
    TextView mTextViewTitle;
    ImageView mImageViewSearch;
    ImageView mImageViewCart;
    TextView mTextViewBageCart;

    ProgressBar mProgressBar;
    LinearLayout mLinearLayoutReturn;
    RelativeLayout mRelativeLayoutDiscount;
    RelativeLayout mRelativeLayoutDeliverTitle;
    RelativeLayout mRelativeLayoutFeedbackDelivery;
    RelativeLayout mRelativeLayoutEstimatedate;
    RelativeLayout mRelativeLayoutTrackOrder;
    LinearLayout mLinearLayoutMainOrder;
    LinearLayout mLinearLayoutReOrder;
    LinearLayout mLinearLayoutCancle;
    TextView mTextViewPlacedNo;
    TextView mTextViewPlaceNoValue;
    TextView mTextViewEstimatedDelivery;
    TextView mTextViewEstimatedDeliveryValue;
    TextView mTextViewOderNumber;
    TextView mTextViewOrderNumberValue;
    TextView mTextViewOrderStatus;
    TextView mTextViewOderStatusValue;
    RecyclerView mRecyclerViewOrderIetm;
    TextView mTextViewOrderItemCount;
    TextView mTextViewTotalPrice;
    TextView mTextViewDiscout;
    TextView mTextViewShppingCharge;
    TextView mTextViewGrossTotal;
    TextView mTextViewCoupenDiscont;
    TextView mTextViewRe_orde_withoutcancle;
    RelativeLayout mRelativeLayoutCounpoenDiscount;

    LinearLayout mLinearLayoutReTry;

    RelativeLayout mRelativeLayoutMain;
    TextView mTextViewAddressHouseNo;
    TextView mTextViewAddressOne;
    TextView mTextViewAddressPincode;
    ImageView mImageViewTrackOrder;
    ImageView mImageViewDeliveryFeedback;
    RelativeLayout mRelativeLayoutDeliveryDate;
    TextView mTextViewDeliveryDateValue;
    TextView mTextViewTracklink;
    RelativeLayout mRelativeLayoutInvoice;
    View mView;


    Bundle mBundle;
    ServerResponseVO mServerResponseVOReOrder;
    ServerResponseVO mServerResponseVOCancleOrder;
    ServerResponseVO mServerResponseVOSendMessage;
    PostParseGet mPostParseGet;
    ServerResponseVO mServerResponseVO;
    TextView fabSend;

    String mStringOrderID;
    ArrayList<OrderItemListVO> mArrayListOrderItem;
    public OrderDetailsVO mOrderDetailsVO;
    ProgressDialog pDialog;

    // userdata
    public SharedPreferences mSharedPreferencesUserId;
    SharedPreferences.Editor mEditor;
    String mStringUserId;
    String mStringComment;
    Dialog mDialog;
    EditText mEditTextComment;
    TextInputLayout mTextInputLayoutComment;
    TextView mTextViewCommentAlert;

    TextView mTextViewTrackOrder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainActivity = (MainActivity) getActivity();
        mCommonHelper = new CommonHelper();
        mPostParseGet = new PostParseGet(mainActivity);
        mOrderDetailsVO = new OrderDetailsVO();

        mArrayListOrderItem = new ArrayList<OrderItemListVO>();

        mBundle = getArguments();
        if (mBundle != null) {
            mStringOrderID = mBundle.getString("OrderId");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentView = inflater.inflate(R.layout.fragment_my_order_details, container, false);

        mImageViewBack = fragmentView.findViewById(R.id.fragment_imageview_back);
        mTextViewTitle = fragmentView.findViewById(R.id.fragment_title_textview);
        mTextViewTitle.setText("Order Details");
        mImageViewSearch = fragmentView.findViewById(R.id.fragment_Serach);
        mImageViewSearch.setVisibility(View.GONE);
        mImageViewCart = fragmentView.findViewById(R.id.fragment_cart);
        mTextViewBageCart = fragmentView.findViewById(R.id.actionbar_title_textview_badge_cart);
        mImageViewCart.setVisibility(View.VISIBLE);

        mLinearLayoutReOrder = fragmentView.findViewById(R.id.fragment_my_orderDetails_linear_order_re_order);
        mLinearLayoutCancle = fragmentView.findViewById(R.id.fragment_my_orderDetails_linear_order_cancle);
        mTextViewPlacedNo = fragmentView.findViewById(R.id.fragment_my_orderDetails_textview_place_no);
        mTextViewPlaceNoValue = fragmentView.findViewById(R.id.fragment_my_orderDetails_textview_place_no_value);
        mTextViewEstimatedDelivery = fragmentView.findViewById(R.id.fragment_my_orderDetails_textview_estimate_date);
        mTextViewEstimatedDeliveryValue = fragmentView.findViewById(R.id.fragment_my_orderDetails_textview_estimate_date_value);
        mTextViewOderNumber = fragmentView.findViewById(R.id.fragment_my_orderDetails_textview_order_number);
        mTextViewOrderNumberValue = fragmentView.findViewById(R.id.fragment_my_orderDetails_textview_order_number_value);
        mTextViewOrderStatus = fragmentView.findViewById(R.id.fragment_my_orderDetails_textview_order_status);
        mTextViewOderStatusValue = fragmentView.findViewById(R.id.fragment_my_orderDetails_textview_order_status_value);
        mRecyclerViewOrderIetm = fragmentView.findViewById(R.id.fragment_my_orderDetails_recycleview);
        mTextViewOrderItemCount = fragmentView.findViewById(R.id.fragment_my_orderDetails_textview_item_title);
        mTextViewTotalPrice = fragmentView.findViewById(R.id.row_review_textview_item_subtotal_total);
        fabSend = fragmentView.findViewById(R.id.btnSendMessage);
        mTextViewDiscout = fragmentView.findViewById(R.id.row_review_textview_item_subtotal_Discout);
        mTextViewShppingCharge = fragmentView.findViewById(R.id.row_review_textview_item_shipping_charge);
        mTextViewGrossTotal = fragmentView.findViewById(R.id.row_review_textview_item_gross_total);
        mTextViewCoupenDiscont = fragmentView.findViewById(R.id.row_review_textview_item_Discout_coupen_value);
        mRelativeLayoutCounpoenDiscount = fragmentView.findViewById(R.id.row_review_relative_coupen_discount);

        mTextViewRe_orde_withoutcancle = fragmentView.findViewById(R.id.fragment_my_orderDetails_textview_re_order_Without_cancle);
        mLinearLayoutMainOrder = fragmentView.findViewById(R.id.fragment_my_orderDetails_Linear_Main);
        mRelativeLayoutEstimatedate = fragmentView.findViewById(R.id.fragment_my_orderDetails_relative_estimate_date);
        mRelativeLayoutTrackOrder = fragmentView.findViewById(R.id.fragment_my_orderDetails_relative_track_order);
        mRelativeLayoutFeedbackDelivery = fragmentView.findViewById(R.id.fragment_my_orderDetails_relative_delivery_feedback);
        mRelativeLayoutDeliverTitle = fragmentView.findViewById(R.id.fragment_my_orderDetails_relative_Deliery_tilte);
        mRelativeLayoutDiscount = fragmentView.findViewById(R.id.row_subtotal_discount);
        mImageViewSearch = fragmentView.findViewById(R.id.fragment_Serach);
        mImageViewSearch.setVisibility(View.VISIBLE);
        mImageViewCart = fragmentView.findViewById(R.id.fragment_cart);
        mImageViewCart.setVisibility(View.VISIBLE);
        mTextViewAddressHouseNo = fragmentView.findViewById(R.id.fragment_my_orderDetails_textview_house_no);
        mTextViewAddressOne = fragmentView.findViewById(R.id.fragment_my_orderDetails_textview_address_one);
        mTextViewAddressPincode = fragmentView.findViewById(R.id.fragment_my_orderDetails_textview_pincodxe);
        mLinearLayoutReturn = fragmentView.findViewById(R.id.fragment_my_orderDetails_linear_order_Return);
        mProgressBar = fragmentView.findViewById(R.id.fragment_my_orderDetails_progressbar);
        mRelativeLayoutMain = fragmentView.findViewById(R.id.fragment_my_orderDetails_relative_main);
        mImageViewTrackOrder = fragmentView.findViewById(R.id.fragment_my_orderDetails_track_order);
        mImageViewDeliveryFeedback = fragmentView.findViewById(R.id.fragment_my_orderDetails_delivery_feedback);
        mRelativeLayoutDeliveryDate = fragmentView.findViewById(R.id.fragment_my_orderDetails_relative_Delivery_date);
        mTextViewDeliveryDateValue = fragmentView.findViewById(R.id.fragment_my_orderDetails_textview_Delivery_date_value);
        mRelativeLayoutInvoice = fragmentView.findViewById(R.id.fragment_my_orderDetails_relative_dwonload_invoice);
        mTextViewTrackOrder = fragmentView.findViewById(R.id.fragment_my_orderDetails_textview_item_trackOrder);
        mView = fragmentView.findViewById(R.id.fragment_my_orderDetails_daonload_invoice_view);

        mLinearLayoutReTry = fragmentView.findViewById(R.id.fragment_my_orderDetails_linear_order_re_try);


        mSharedPreferencesUserId = mainActivity.getSharedPreferences(Tags.TAG_SHARED_PREFERENCE_USER_ID, 0);
        mStringUserId = mSharedPreferencesUserId.getString("UserId", "");
        mEditor = mSharedPreferencesUserId.edit();



        mImageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.onBackPressed();
            }
        });
        mRelativeLayoutTrackOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTrackOrderLink fragmentTrackOrder = new FragmentTrackOrderLink();
                Bundle mBundle = new Bundle();
                mBundle.putString("TrackLink", mOrderDetailsVO.getTrackLink());
                fragmentTrackOrder.setArguments(mBundle);
                mainActivity.addFragment(fragmentTrackOrder, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, fragmentTrackOrder.getClass().getName());
            }
        });


        mTextViewTrackOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentTrackOrder mFragmentTrackOrder = new FragmentTrackOrder();
                Bundle mBundle = new Bundle();
                mBundle.putString("OrderNo", mOrderDetailsVO.getOrderNo());
                mBundle.putString("orderdate", mOrderDetailsVO.getDate());
                mBundle.putString("Orderstatus", mOrderDetailsVO.getOrderStatus());
                mBundle.putString("OrderId", mOrderDetailsVO.getOrderId());
                mBundle.putString("DelvType", mOrderDetailsVO.getDelvType());
                mFragmentTrackOrder.setArguments(mBundle);

                mainActivity.addFragment(mFragmentTrackOrder, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, mFragmentTrackOrder.getClass().getName());

            }
        });

        mRelativeLayoutFeedbackDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentFeedbackDelivery fragmentFeedbackDelivery = new FragmentFeedbackDelivery();
                Bundle mBundle = new Bundle();
                mBundle.putString("Orderstatus", mOrderDetailsVO.getOrderStatus());
                mBundle.putString("orderId", mOrderDetailsVO.getOrderId());
                mBundle.putString("deliveryDate", mOrderDetailsVO.getDeliveryDate());
                fragmentFeedbackDelivery.setArguments(mBundle);
                mainActivity.addFragment(fragmentFeedbackDelivery, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, fragmentFeedbackDelivery.getClass().getName());
            }
        });
        mImageViewSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentMedicines fragmentMedicines = new FragmentMedicines();
                mainActivity.addFragment(fragmentMedicines, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, fragmentMedicines.getClass().getName());
            }
        });
//        mainActivity.mImageViewCart.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                FragmentCart fragmentCart = new FragmentCart();
//                mainActivity.addFragment(fragmentCart, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, fragmentCart.getClass().getName());
//
//
//            }
//        });
//
//        mainActivity.mImageViewNotification.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                FragmentNotification fragmentNotification = new FragmentNotification();
//                mainActivity.addFragment(fragmentNotification, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, fragmentNotification.getClass().getName());
//            }
//        });
        mLinearLayoutReOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReOrder();
            }
        });
        mTextViewRe_orde_withoutcancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReOrder();
            }
        });
        mLinearLayoutCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!mCommonHelper.check_Internet(getActivity())) {
                    Snackbar.with(mainActivity).text(getResources().getString(R.string.interneterror)).show(mainActivity);
                    return;
                }
                dialogCancelOrder();

            }
        });

        mLinearLayoutReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentOrderReturn fragmentOrderReturn = new FragmentOrderReturn();
                Bundle mBundle = new Bundle();
                mBundle.putString("OrderID", mStringOrderID);
                fragmentOrderReturn.setArguments(mBundle);
                mainActivity.addFragment(fragmentOrderReturn, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, fragmentOrderReturn.getClass().getName());
            }
        });

        mRelativeLayoutInvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                FragmentInvoice fragmentInvoice = new FragmentInvoice();
                Bundle mBundle = new Bundle();
                mBundle.putString("invoice", mOrderDetailsVO.getInvoicePdfLink());
                fragmentInvoice.setArguments(mBundle);
                mainActivity.addFragment(fragmentInvoice, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, fragmentInvoice.getClass().getName());

            }
        });

        mImageViewCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                FragmentCart fragmentCart = new FragmentCart();
                mainActivity.addFragment(fragmentCart, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, fragmentCart.getClass().getName());

            }
        });

        mLinearLayoutReTry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                FragmentPaymentDetails fragmentPaymentDetails = new FragmentPaymentDetails();
                Bundle mBundle = new Bundle();
                mBundle.putString("OrderId", mOrderDetailsVO.getOrderId());
                mBundle.putBoolean("isOrder", true);
                mBundle.putString("GrossTotal", mOrderDetailsVO.getGrossRs());
                fragmentPaymentDetails.setArguments(mBundle);
                mainActivity.addFragment(fragmentPaymentDetails, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, fragmentPaymentDetails.getClass().getName());


            }
        });
        return fragmentView;
    }


    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    public static FragmentMyOrderDetails newInstance() {
        FragmentMyOrderDetails fragmentMyOrderDetails = new FragmentMyOrderDetails();
        return fragmentMyOrderDetails;
    }


    public void GetorderDetails() {
        getLoaderManager().restartLoader(0, mBundle, new LoaderManager.LoaderCallbacks<TaskExecutor>() {
            @Override
            public Loader<TaskExecutor> onCreateLoader(int arg0, Bundle arg1) {

                OrderDetailsExecutor mReOrderExecutor = new OrderDetailsExecutor(mainActivity, null);
                return new LoaderTask<TaskExecutor>(mainActivity, mReOrderExecutor);
            }

            @Override
            public void onLoadFinished(Loader<TaskExecutor> arg0, TaskExecutor arg1) {
                mProgressBar.setVisibility(View.VISIBLE);
                if (isAdded()) {
                    getLoaderManager().destroyLoader(0);
                    if (mPostParseGet.isNetError)
                        Snackbar.with(mainActivity).text(getResources().getString(R.string.interneterror)).show(mainActivity);
                    else if (mPostParseGet.isOtherError)
                        Snackbar.with(mainActivity).text(getResources().getString(R.string.notabletocommunicatewithserver)).show(mainActivity);
                    else {

                        if (mServerResponseVO != null && mServerResponseVO.getStatus() != null) {

                            if (mServerResponseVO.getStatus().equalsIgnoreCase("true")) {
//                                Snackbar.with(mainActivity).text(mServerResponseVO.getMsg()).show(mainActivity);

                                if (mServerResponseVO.getData() != null) {
                                    mOrderDetailsVO = (OrderDetailsVO) mServerResponseVO.getData();

                                    if (mOrderDetailsVO != null) {
//                                         Place On
                                        if (!mOrderDetailsVO.getDate().equalsIgnoreCase("null") && mOrderDetailsVO.getDate() != null) {
                                            String mDate = mOrderDetailsVO.getDate();
                                            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
                                            SimpleDateFormat fmt2 = new SimpleDateFormat("EEE, MMM d, yyyy");
                                            try {
                                                Date date = fmt.parse(mDate);
                                                String mString = fmt2.format(date);
                                                mTextViewPlaceNoValue.setText(mString);
                                            } catch (ParseException pe) {
                                                pe.printStackTrace();
                                            }
                                        }
//                                        Estimate Date
                                        if (!mOrderDetailsVO.getExpectedDelvDate().equalsIgnoreCase("null") && mOrderDetailsVO.getExpectedDelvDate() != null) {
                                            mRelativeLayoutEstimatedate.setVisibility(View.VISIBLE);
                                            String mDate = mOrderDetailsVO.getExpectedDelvDate();
                                            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
                                            SimpleDateFormat fmt2 = new SimpleDateFormat("EEE, MMM d, yyyy");
                                            try {
                                                Date date = fmt.parse(mDate);
                                                String mString = fmt2.format(date);
                                                mTextViewEstimatedDeliveryValue.setText(mString);
                                            } catch (ParseException pe) {
                                                pe.printStackTrace();
                                            }
                                        } else {
                                            mRelativeLayoutEstimatedate.setVisibility(View.GONE);
                                        }

//                                        Delivery Date
                                        if (!mOrderDetailsVO.getDeliveryDate().equalsIgnoreCase("null") && mOrderDetailsVO.getDeliveryDate() != null) {
                                            mRelativeLayoutDeliveryDate.setVisibility(View.VISIBLE);
                                            String mDate = mOrderDetailsVO.getDeliveryDate();
                                            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
                                            SimpleDateFormat fmt2 = new SimpleDateFormat("EEE, MMM d, yyyy");
                                            try {
                                                Date date = fmt.parse(mDate);
                                                String mString = fmt2.format(date);
                                                mTextViewDeliveryDateValue.setText(mString);
                                            } catch (ParseException pe) {
                                                pe.printStackTrace();
                                            }
                                        } else {
                                            mRelativeLayoutDeliveryDate.setVisibility(View.GONE);
                                        }


//                                        Order No
                                        if (!mOrderDetailsVO.getOrderNo().equalsIgnoreCase("null") && mOrderDetailsVO.getOrderNo() != null) {
                                            mTextViewOrderNumberValue.setText(mOrderDetailsVO.getOrderNo());
                                        }

//                                        Order Status
                                        if (!mOrderDetailsVO.getOrderStatus().equalsIgnoreCase("null") && mOrderDetailsVO.getOrderStatus() != null) {
                                            mTextViewOderStatusValue.setText(mOrderDetailsVO.getOrderStatus());
                                        }

//                                      Order Deatils Item List

                                        mArrayListOrderItem = mOrderDetailsVO.getmOrderItemListVOS();
                                        if (mArrayListOrderItem != null && mArrayListOrderItem.size() > 0) {

                                            mTextViewOrderItemCount.setText(getResources().getString(R.string.orderitem) + " " + "(" + mArrayListOrderItem.size() + ")");
                                            OrderItemAdpater orderItemAdpater = new OrderItemAdpater(mainActivity, mArrayListOrderItem);
                                            mRecyclerViewOrderIetm.setHasFixedSize(true);
                                            mRecyclerViewOrderIetm.setLayoutManager(new LinearLayoutManager(mainActivity));
                                            mRecyclerViewOrderIetm.setAdapter(orderItemAdpater);
                                        }

//                                      order cancle & return & reoder
                                        if (mOrderDetailsVO.getOrderStatus().equalsIgnoreCase("Delivered")) {
                                            mLinearLayoutReturn.setVisibility(View.VISIBLE);
                                            mLinearLayoutCancle.setVisibility(View.GONE);
                                            mRelativeLayoutTrackOrder.setVisibility(View.GONE);
                                        }
                                        if (mOrderDetailsVO.getOrderStatus().equalsIgnoreCase("Cancel") || mOrderDetailsVO.getOrderStatus().equalsIgnoreCase("Return")) {
                                            mLinearLayoutReturn.setVisibility(View.GONE);
                                            mLinearLayoutCancle.setVisibility(View.GONE);
                                            mLinearLayoutMainOrder.setVisibility(View.GONE);
                                            mTextViewRe_orde_withoutcancle.setVisibility(View.VISIBLE);
                                            mRelativeLayoutDeliveryDate.setVisibility(View.GONE);
                                            mRelativeLayoutEstimatedate.setVisibility(View.GONE);
                                        }

//                                        track order visiblity
                                        if (!mOrderDetailsVO.getDelvType().equalsIgnoreCase(null)) {
                                            if (mOrderDetailsVO.getDelvType().equalsIgnoreCase("Delivery")) {
                                                mRelativeLayoutTrackOrder.setVisibility(View.GONE);
                                                if (mOrderDetailsVO.getOrderStatus().equalsIgnoreCase("Dispatch") || mOrderDetailsVO.getOrderStatus().equalsIgnoreCase("Out for Delivery"))
                                                {
                                                    fabSend.setVisibility(View.VISIBLE);
                                                    fabSend.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                                                            final EditText edittext = new EditText(getActivity());
                                                            alert.setMessage("Enter Your Message");
                                                            alert.setTitle("Send");
                                                            alert.setView(edittext);
                                                            alert.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                                                                public void onClick(DialogInterface dialog, int whichButton) {
                                                                    String message = edittext.getText().toString();
                                                                    sendMessage(message);
                                                                }
                                                            });
                                                            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                                public void onClick(DialogInterface dialog, int whichButton) {
                                                                    // what ever you want to do with No option.
                                                                }
                                                            });
                                                            alert.show();
                                                        }
                                                    });
                                                }
                                                else
                                                {
                                                    fabSend.setVisibility(View.GONE);
                                                }

                                            } else {
                                                if (mOrderDetailsVO.getOrderStatus().equalsIgnoreCase("Dispatch") || mOrderDetailsVO.getOrderStatus().equalsIgnoreCase("Out for Delivery")) {
                                                    mRelativeLayoutDeliverTitle.setVisibility(View.VISIBLE);
                                                    mRelativeLayoutTrackOrder.setVisibility(View.VISIBLE);
                                                    mRelativeLayoutDeliveryDate.setVisibility(View.GONE);

                                                } else {
                                                    mRelativeLayoutDeliverTitle.setVisibility(View.GONE);
                                                    mRelativeLayoutTrackOrder.setVisibility(View.GONE);

                                                }
                                                if(!mOrderDetailsVO.getDelvType().equalsIgnoreCase("Delivery"))
                                                fabSend.setVisibility(View.GONE);
                                            }
                                        }

//                                       Delivery Feedback
                                        if (mOrderDetailsVO.getOrderStatus().equalsIgnoreCase("Delivered")) {
                                            mRelativeLayoutFeedbackDelivery.setVisibility(View.VISIBLE);
                                            mRelativeLayoutTrackOrder.setVisibility(View.GONE);
                                            mRelativeLayoutEstimatedate.setVisibility(View.GONE);
                                            mRelativeLayoutDeliveryDate.setVisibility(View.VISIBLE);

                                        } else {
                                            mRelativeLayoutFeedbackDelivery.setVisibility(View.GONE);

                                        }

//                                          hide show delivery date
                                        if (mOrderDetailsVO.getOrderStatus().equalsIgnoreCase("Pending")) {
                                            mRelativeLayoutDeliveryDate.setVisibility(View.GONE);
                                            mRelativeLayoutEstimatedate.setVisibility(View.GONE);
                                        }

                                        if (mOrderDetailsVO.getOrderStatus().equalsIgnoreCase("Confirm")) {
                                            mRelativeLayoutDeliveryDate.setVisibility(View.GONE);
                                            mRelativeLayoutEstimatedate.setVisibility(View.VISIBLE);
                                        }

                                        if (mOrderDetailsVO.getOrderStatus().equalsIgnoreCase("Payment Failed")) {
                                            mLinearLayoutReTry.setVisibility(View.VISIBLE);
                                            mLinearLayoutReOrder.setVisibility(View.GONE);
                                        }

                                        if (mOrderDetailsVO.getIsInvoiceCreated().equalsIgnoreCase("True")) {

                                            mRelativeLayoutInvoice.setVisibility(View.VISIBLE);
                                            mView.setVisibility(View.VISIBLE);
                                        }

                                        if (mOrderDetailsVO.getOrderStatus().equalsIgnoreCase("Cancel")) {
                                            mTextViewTrackOrder.setVisibility(View.GONE);
                                        } else {
                                            mTextViewTrackOrder.setVisibility(View.VISIBLE);
                                        }


//                                        Address Data Set
                                        if (!mOrderDetailsVO.getUserName().equalsIgnoreCase("null") && mOrderDetailsVO.getUserName() != null) {
                                            mTextViewAddressHouseNo.setText(mOrderDetailsVO.getUserName());
                                        }
                                        if (!mOrderDetailsVO.getAddress().equalsIgnoreCase("null") && mOrderDetailsVO.getAddress() != null) {
                                            mTextViewAddressOne.setText(mOrderDetailsVO.getAddress());
                                        }
                                        if (mOrderDetailsVO.getPincode() != null) {
                                            mTextViewAddressPincode.setText(mOrderDetailsVO.getCity() + "," + mOrderDetailsVO.getPincode() + "  " + mOrderDetailsVO.getState());
                                        }

//                                        total value set
                                        if (!mOrderDetailsVO.getTotalMRPRs().equalsIgnoreCase("0.0") && mOrderDetailsVO.getTotalMRPRs() != null) {
                                            mTextViewTotalPrice.setText(Tags.LABEL_CURRENCY_SIGN + Utility.getDecimalFormate(mainActivity, Double.valueOf(mOrderDetailsVO.getTotalMRPRs()), 0.0, Tags.DECIMAL_FORMAT));

                                        }

                                        if (mOrderDetailsVO.getItemTotalDisc() != null && !mOrderDetailsVO.getTotalRs().equalsIgnoreCase("0.0")) {
                                            mTextViewDiscout.setText("- " + (Tags.LABEL_CURRENCY_SIGN + Utility.getDecimalFormate(mainActivity, Double.valueOf(mOrderDetailsVO.getItemTotalDisc()), 0.0, Tags.DECIMAL_FORMAT)));
                                        } else {
                                            mTextViewDiscout.setText("- " + Tags.LABEL_CURRENCY_SIGN + "0.0");
                                        }


                                        if (!mOrderDetailsVO.getCoupenDisc().equalsIgnoreCase("0.0") && mOrderDetailsVO.getCoupenDisc() != null) {
                                            mRelativeLayoutCounpoenDiscount.setVisibility(View.VISIBLE);
                                            mTextViewCoupenDiscont.setText("- " + (Tags.LABEL_CURRENCY_SIGN + Utility.getDecimalFormate(mainActivity, Double.valueOf(mOrderDetailsVO.getCoupenDisc()), 0.0, Tags.DECIMAL_FORMAT)));
                                        }


                                        if (!mOrderDetailsVO.getDeliveryCharge().equalsIgnoreCase("0.0") && mOrderDetailsVO.getDeliveryCharge() != null) {
                                            mTextViewShppingCharge.setText(Tags.LABEL_CURRENCY_SIGN + Utility.getDecimalFormate(mainActivity, Double.valueOf(mOrderDetailsVO.getDeliveryCharge()), 0.0, Tags.DECIMAL_FORMAT));
                                        } else {
                                            mTextViewShppingCharge.setText(Tags.LABEL_CURRENCY_SIGN + "0");
                                        }
                                        if (!mOrderDetailsVO.getTotalRs().equalsIgnoreCase("0.0") && mOrderDetailsVO.getTotalRs() != null) {
                                            mTextViewGrossTotal.setText(Tags.LABEL_CURRENCY_SIGN + Utility.getDecimalFormate(mainActivity, Double.valueOf(mOrderDetailsVO.getTotalRs()), 0.0, Tags.DECIMAL_FORMAT));
                                        }

                                    }
                                    mProgressBar.setVisibility(View.GONE);
                                    mRelativeLayoutMain.setVisibility(View.VISIBLE);
                                }
                            }
                        } else {
                            mProgressBar.setVisibility(View.GONE);
                            Snackbar.with(mainActivity).text(mServerResponseVO.getMsg()).show(mainActivity);
                        }
                    }
                }
            }

            @Override
            public void onLoaderReset(Loader<TaskExecutor> arg0) {
            }
        });
    }

    //order Deatils
    public class OrderDetailsExecutor extends TaskExecutor {

        protected OrderDetailsExecutor(Context context, Bundle bundle) {
            super(context, bundle);
        }

        @Override
        protected Object executeTask() {
            try {
                OrderDeatilsService fetchVersionInfoService = new OrderDeatilsService();
                mServerResponseVO = fetchVersionInfoService.Orderdetails(mainActivity, Tags.GETORDERDetails, mStringOrderID);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }


    //     cancle order
    public void dialogCancelOrder() {

        mDialog = new Dialog(mainActivity);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.popup_order_password);
        mDialog.setCanceledOnTouchOutside(false);

        final TextView mTextViewSubmit = mDialog.findViewById(R.id.popup_order_password_textview_cancel_order);
        mTextViewCommentAlert = mDialog.findViewById(R.id.popup_order_comment);
        mEditTextComment = mDialog.findViewById(R.id.popup_order_password_edittext_comment);
        mTextInputLayoutComment = mDialog.findViewById(R.id.popup_order_password_input_layout_comment);

        mTextViewSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mStringComment = mEditTextComment.getText().toString();
                if (!validComment()) {
                    return;
                }
                CancleOrder();
            }
        });

        mDialog.show();
    }


    public class OrderItemAdpater extends RecyclerView.Adapter<OrderItemAdpater.MyViewHolderOrderItem> {


        Context mContext;
        ArrayList<OrderItemListVO> mArrayListitem;


        public OrderItemAdpater(MainActivity mainActivity, ArrayList<OrderItemListVO> mArrayListOrderItem) {
            this.mContext = mainActivity;
            this.mArrayListitem = mArrayListOrderItem;
        }

        @NonNull
        @Override
        public MyViewHolderOrderItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_order_item, parent, false);
            MyViewHolderOrderItem viewHolderOrderItem = new MyViewHolderOrderItem(v);
            return viewHolderOrderItem;
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolderOrderItem holder, @SuppressLint("RecyclerView") int position) {

            if (!mArrayListitem.get(position).getMedicineName().equalsIgnoreCase("null") && mArrayListitem.get(position).getMedicineName() != null) {
                holder.mTextViewName.setText(mArrayListitem.get(position).getMedicineName());
            }

            if (!mArrayListitem.get(position).getPrice().equalsIgnoreCase("null") && mArrayListitem.get(position).getPrice() != null) {
                holder.mTextViewPrice.setText(Tags.LABEL_CURRENCY_SIGN + Utility.getDecimalFormate(mainActivity, Double.valueOf(mArrayListitem.get(position).getPrice()), 0.0, Tags.DECIMAL_FORMAT));
            }

            if (!mArrayListitem.get(position).getQty().equalsIgnoreCase("null") && mArrayListitem.get(position).getQty() != null) {
                holder.mTextViewQty.setText(mArrayListitem.get(position).getQty());
            }

            if (mArrayListitem.get(position).getMedicineImgPath() != null && !mArrayListitem.get(position).getMedicineImgPath().equalsIgnoreCase("null")) {

                Glide.with(mContext).load(mArrayListitem.get(position).getMedicineImgPath())
                        .placeholder(R.drawable.ic_med)
                        .into(holder.mImageView);
            } else {
                Glide.with(mContext)
                        .load(R.drawable.ic_med)
                        .into(holder.mImageView);
            }

            holder.mRelativeLayoutReview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentRatingReviews fragmentRatingReviews = new FragmentRatingReviews();
                    Bundle mBundle = new Bundle();
                    mBundle.putString("Medicine_id", mArrayListitem.get(position).getMedicineId());
                    mBundle.putString("Medicine_name", mArrayListitem.get(position).getMedicineName());
                    mBundle.putString("Medicine_image", mArrayListitem.get(position).getMedicineImgPath());
                    fragmentRatingReviews.setArguments(mBundle);
                    mainActivity.addFragment(fragmentRatingReviews, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, fragmentRatingReviews.getClass().getName());
                }
            });

            holder.mRelativeLayoutComplain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    FragmentWriteComplain fragmentWriteComplain = new FragmentWriteComplain();
                    Bundle mBundle = new Bundle();
                    mBundle.putString("Medicine_id", mArrayListitem.get(position).getMedicineId());
                    mBundle.putString("Order_id", mOrderDetailsVO.getOrderId());
                    mBundle.putString("order_no", mOrderDetailsVO.getOrderNo());
                    mBundle.putString("medicine_name", mArrayListitem.get(position).getMedicineName());
                    fragmentWriteComplain.setArguments(mBundle);
                    mainActivity.addFragment(fragmentWriteComplain, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, fragmentWriteComplain.getClass().getName());
                }
            });

        }

        @Override
        public int getItemCount() {
            return mArrayListitem.size();
        }

        public class MyViewHolderOrderItem extends RecyclerView.ViewHolder {

            TextView mTextViewName;
            TextView mTextViewQty;
            TextView mTextViewPrice;
            ImageView mImageViewReview;
            ImageView mImageView;
            RelativeLayout mRelativeLayoutReview;
            RelativeLayout mRelativeLayoutComplain;

            LinearLayout mLinearLayout;

            public MyViewHolderOrderItem(View itemView) {
                super(itemView);

                mImageView = itemView.findViewById(R.id.row_order_details_imageview);
                mTextViewName = itemView.findViewById(R.id.row_order_details_textview__medicine_name);
                mTextViewQty = itemView.findViewById(R.id.row_order_details_textview_price_value);
                mTextViewPrice = itemView.findViewById(R.id.row_order_details_textview_amount);
                mImageViewReview = itemView.findViewById(R.id.fragment_my_orderDetails_write_review);
                mRelativeLayoutReview = itemView.findViewById(R.id.fragment_my_orderDetails_relative_write_review);
                mRelativeLayoutComplain = itemView.findViewById(R.id.fragment_my_orderDetails_relative_write_complain);
            }
        }
    }

    //    cancle api call
    public void CancleOrder() {
        getLoaderManager().restartLoader(0, mBundle, new LoaderManager.LoaderCallbacks<TaskExecutor>() {
            @Override
            public Loader<TaskExecutor> onCreateLoader(int arg0, Bundle arg1) {

                CancleExecutor mCancleExecutor = new CancleExecutor(mainActivity, null);
                return new LoaderTask<TaskExecutor>(mainActivity, mCancleExecutor);
            }

            @Override
            public void onLoadFinished(Loader<TaskExecutor> arg0, TaskExecutor arg1) {

                if (isAdded()) {
                    getLoaderManager().destroyLoader(0);
                    if (mPostParseGet.isNetError)
                        Snackbar.with(mainActivity).text(getResources().getString(R.string.interneterror)).show(mainActivity);
                    else if (mPostParseGet.isOtherError)
                        Snackbar.with(mainActivity).text(getResources().getString(R.string.notabletocommunicatewithserver)).show(mainActivity);
                    else {
                        if (mServerResponseVOCancleOrder != null && mServerResponseVOCancleOrder.getStatus() != null) {
                            if (mServerResponseVOCancleOrder.getStatus().equalsIgnoreCase("true")) {
                                mDialog.dismiss();
                                mLinearLayoutMainOrder.setVisibility(View.GONE);
                                mTextViewRe_orde_withoutcancle.setVisibility(View.VISIBLE);
                                mTextViewOderStatusValue.setText("Canceled");
                                Snackbar.with(mainActivity).text(mServerResponseVOCancleOrder.getMsg()).show(mainActivity);
                            }
                        } else {
                            Snackbar.with(mainActivity).text(mServerResponseVOCancleOrder.getMsg()).show(mainActivity);
                        }
                    }
                }
            }

            @Override
            public void onLoaderReset(Loader<TaskExecutor> arg0) {
            }
        });
    }

    public class CancleExecutor extends TaskExecutor {

        protected CancleExecutor(Context context, Bundle bundle) {
            super(context, bundle);
        }

        @Override
        protected Object executeTask() {
            try {
                CancleOrderService fetchVersionInfoService = new CancleOrderService();
                mServerResponseVOCancleOrder = fetchVersionInfoService.CancleOrder(mainActivity, Tags.CANCLEORDER, mStringOrderID, mStringUserId, mStringComment);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }


    //    send message api call
    public void sendMessage(String message) {
        getLoaderManager().restartLoader(0, mBundle, new LoaderManager.LoaderCallbacks<TaskExecutor>() {
            @Override
            public Loader<TaskExecutor> onCreateLoader(int arg0, Bundle arg1) {

                Bundle bundle = new Bundle();
                bundle.putString("msg",message);
                SendMessageExecutor sendMessageExecutor = new SendMessageExecutor(mainActivity, bundle);
                return new LoaderTask<TaskExecutor>(mainActivity, sendMessageExecutor);
            }

            @Override
            public void onLoadFinished(Loader<TaskExecutor> arg0, TaskExecutor arg1) {

                if (isAdded()) {
                    getLoaderManager().destroyLoader(0);
                    if (mPostParseGet.isNetError)
                        Snackbar.with(mainActivity).text(getResources().getString(R.string.interneterror)).show(mainActivity);
                    else if (mPostParseGet.isOtherError)
                        Snackbar.with(mainActivity).text(getResources().getString(R.string.notabletocommunicatewithserver)).show(mainActivity);
                    else {
                        if (mServerResponseVOSendMessage != null && mServerResponseVOSendMessage.getStatus() != null) {
                            if (mServerResponseVOSendMessage.getStatus().equalsIgnoreCase("true")) {
                                Snackbar.with(mainActivity).text(mServerResponseVOSendMessage.getMsg()).show(mainActivity);
                            }
                        } else {
                            Snackbar.with(mainActivity).text(mServerResponseVOSendMessage.getMsg()).show(mainActivity);
                        }
                    }
                }
            }

            @Override
            public void onLoaderReset(Loader<TaskExecutor> arg0) {
            }
        });
    }

    public class SendMessageExecutor extends TaskExecutor {
        Bundle bundle;
        protected SendMessageExecutor(Context context, Bundle bundle) {
            super(context, bundle);
            this.bundle = bundle;
        }

        @Override
        protected Object executeTask() {
            try {
                SendMessageService fetchVersionInfoService = new SendMessageService();
                mServerResponseVOSendMessage = fetchVersionInfoService.sendMessage(mainActivity, Tags.SENDMESSAGE,mStringUserId,
                        mOrderDetailsVO.getOrderId(), bundle.getString("msg"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }




    private boolean validComment() {
        String comment = mEditTextComment.getText().toString().trim();

        if (comment.isEmpty()) {
            mTextViewCommentAlert.setVisibility(View.VISIBLE);
            mCommonHelper.hideKeyboard(mainActivity);
            return false;
        }

        return true;
    }


    //    Reorer Api call
    public void ReOrder() {
        getLoaderManager().restartLoader(0, mBundle, new LoaderManager.LoaderCallbacks<TaskExecutor>() {
            @Override
            public Loader<TaskExecutor> onCreateLoader(int arg0, Bundle arg1) {

                ReOrderExecutor mReOrderExecutor = new ReOrderExecutor(mainActivity, null);
                return new LoaderTask<TaskExecutor>(mainActivity, mReOrderExecutor);
            }

            @Override
            public void onLoadFinished(Loader<TaskExecutor> arg0, TaskExecutor arg1) {

                if (isAdded()) {
                    getLoaderManager().destroyLoader(0);
                    if (mPostParseGet.isNetError)
                        Snackbar.with(mainActivity).text(getResources().getString(R.string.interneterror)).show(mainActivity);
                    else if (mPostParseGet.isOtherError)
                        Snackbar.with(mainActivity).text(getResources().getString(R.string.notabletocommunicatewithserver)).show(mainActivity);
                    else {
                        if (mServerResponseVOReOrder != null && mServerResponseVOReOrder.getStatus() != null) {

                            if (mServerResponseVOReOrder.getStatus().equalsIgnoreCase("true")) {
                                Snackbar.with(mainActivity).text(mServerResponseVOReOrder.getMsg()).show(mainActivity);

                                mTextViewBageCart.setVisibility(View.VISIBLE);
                                mTextViewBageCart.setText(mServerResponseVOReOrder.getCartCount());
                            } else {
                                Snackbar.with(mainActivity).text(mServerResponseVOReOrder.getMsg()).show(mainActivity);
                            }
                        } else {
                            Snackbar.with(mainActivity).text(mServerResponseVOReOrder.getMsg()).show(mainActivity);
                        }
                    }
                }
            }

            @Override
            public void onLoaderReset(Loader<TaskExecutor> arg0) {

            }
        });


    }

    public class ReOrderExecutor extends TaskExecutor {

        protected ReOrderExecutor(Context context, Bundle bundle) {
            super(context, bundle);
        }

        @Override
        protected Object executeTask() {
            try {
                ReOrderService fetchVersionInfoService = new ReOrderService();
                mServerResponseVOReOrder = fetchVersionInfoService.ReOrders(mainActivity, Tags.REORDER, mStringOrderID, mStringUserId);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public void onResumeData() {

        GetorderDetails();
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