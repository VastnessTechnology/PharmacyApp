package com.vgroyalchemist.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.TokenWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vgroyalchemist.R;
import com.vgroyalchemist.controller.MainActivity;
import com.vgroyalchemist.loaders.LoaderTask;
import com.vgroyalchemist.loaders.TaskExecutor;
import com.vgroyalchemist.utils.CommonHelper;
import com.vgroyalchemist.utils.Snackbar;
import com.vgroyalchemist.utils.Tags;
import com.vgroyalchemist.utils.Utility;
import com.vgroyalchemist.vos.OrderDataVo;
import com.vgroyalchemist.vos.ServerResponseVO;
import com.vgroyalchemist.vos.UserDetailsVO;
import com.vgroyalchemist.webservice.DeleteAddressInfoService;
import com.vgroyalchemist.webservice.GetAllAddressInfoService;
import com.vgroyalchemist.webservice.GetAllOrderService;
import com.vgroyalchemist.webservice.PostParseGet;
import com.vgroyalchemist.webservice.ReOrderService;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/* developed by krishna 24-03-2021
*  Order List */
public class FragmentMyOrderList extends NonCartFragment {

    //Variable Declaration
    public static final String FRAGMENT_ID = "17";

    public static MainActivity mainActivity;
    public View fragmentView = null;
    CommonHelper mCommonHelper;

    ImageView mImageViewBack;
    TextView mTextViewTitle;
    ImageView mImageViewSearch;
    ImageView mImageViewCart;

    TextView mTextViewFilter;
    RecyclerView mRecyclerViewOrderList;

    OrderListData orderListData;

    TextView mTextViewNoData;
    ArrayList<OrderDataVo> mArrayListOrderList;
    ServerResponseVO mServerResponseVO;
    PostParseGet mPostParseGet;
    // userdata
    public SharedPreferences mSharedPreferencesUserId;
    SharedPreferences.Editor mEditor;

    String mStringUserId;
    ServerResponseVO mServerResponseVOReOrder;

    Bundle mBundle;
    boolean isfromfilter;
    DecimalFormat decimalFormat;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = (MainActivity) getActivity();
        mCommonHelper = new CommonHelper();
        mPostParseGet = new PostParseGet(mainActivity);
        mArrayListOrderList = new ArrayList<OrderDataVo>();
        decimalFormat = new DecimalFormat(Tags.DECIMAL_FORMAT);
        mBundle = getArguments();
        if (mBundle != null) {
            mArrayListOrderList = mBundle.getParcelableArrayList("OrderFilterList");
            isfromfilter = mBundle.getBoolean("IsFromFilter");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentView = inflater.inflate(R.layout.fragment_my_order_list, container, false);

        mImageViewBack = fragmentView.findViewById(R.id.fragment_imageview_back);
        mTextViewTitle = fragmentView.findViewById(R.id.fragment_title_textview);
        mTextViewTitle.setText("My Order");
        mImageViewSearch = fragmentView.findViewById(R.id.fragment_Serach);
        mImageViewSearch.setVisibility(View.GONE);
        mImageViewCart = fragmentView.findViewById(R.id.fragment_cart);
        mImageViewCart.setVisibility(View.GONE);
        mRecyclerViewOrderList = fragmentView.findViewById(R.id.fragment_my_orderlist_recycleview);
        mTextViewNoData = fragmentView.findViewById(R.id.fragment_my_orderlist_textview_nodata);
        mTextViewFilter = fragmentView.findViewById(R.id.fragment_my_orderlist_textview_Filter);

        mSharedPreferencesUserId = mainActivity.getSharedPreferences(Tags.TAG_SHARED_PREFERENCE_USER_ID, 0);
        mStringUserId = mSharedPreferencesUserId.getString("UserId", "");
        mEditor = mSharedPreferencesUserId.edit();


        mImageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.onBackPressed();
            }
        });

        mTextViewFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mainActivity.removeCurrentFragment();
                FragmentOrderFilter orderFilter = new FragmentOrderFilter();
                mainActivity.addFragment(orderFilter, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, orderFilter.getClass().getName());
            }
        });

        return fragmentView;
    }


    public static FragmentMyOrderList newInstance() {
        FragmentMyOrderList fragmentMyOrderList = new FragmentMyOrderList();
        return fragmentMyOrderList;
    }

    public void OrderListData() {
        getLoaderManager().restartLoader(0, null, new LoaderManager.LoaderCallbacks<TaskExecutor>() {
            @Override
            public Loader<TaskExecutor> onCreateLoader(int arg0, Bundle arg1) {

                GetAllOrderList mGetAllOrderList = new GetAllOrderList(mainActivity, null);
                return new LoaderTask<TaskExecutor>(mainActivity, mGetAllOrderList);
            }

            @Override
            public void onLoadFinished(Loader<TaskExecutor> arg0, TaskExecutor arg1) {

                if (isAdded()) {
                    getLoaderManager().destroyLoader(0);
                    if (!mCommonHelper.check_Internet(mainActivity)) {
                        Snackbar.with(mainActivity).text(getResources().getString(R.string.interneterror)).show(mainActivity);
                        return;
                    }
                    if (mPostParseGet.isNetError)
                        Snackbar.with(mainActivity).text(getResources().getString(R.string.interneterror)).show(mainActivity);
                    else if (mPostParseGet.isOtherError)
                        Snackbar.with(mainActivity).text(getResources().getString(R.string.notabletocommunicatewithserver)).show(mainActivity);
                    else {

                        try {
                            if (mServerResponseVO.getStatus().equalsIgnoreCase("true")) {
//                            Snackbar.with(mainActivity).text(mServerResponseVO.getMsg()).show(mainActivity);
                                mCommonHelper.hideKeyboard(mainActivity);

                                if (mServerResponseVO.getData() != null) {

                                    mArrayListOrderList = (ArrayList<OrderDataVo>) mServerResponseVO.getData();

                                    if (mArrayListOrderList != null && mArrayListOrderList.size() > 0) {
                                        mTextViewNoData.setVisibility(View.GONE);
                                        mRecyclerViewOrderList.setVisibility(View.VISIBLE);
                                        mTextViewFilter.setVisibility(View.VISIBLE);

                                        orderListData = new OrderListData(mainActivity, mArrayListOrderList);
                                        mRecyclerViewOrderList.setHasFixedSize(true);
                                        mRecyclerViewOrderList.setLayoutManager(new LinearLayoutManager(mainActivity));
                                        mRecyclerViewOrderList.setAdapter(orderListData);
                                    } else {
                                        mTextViewNoData.setVisibility(View.VISIBLE);
                                        mRecyclerViewOrderList.setVisibility(View.GONE);
                                        mTextViewFilter.setVisibility(View.GONE);

                                    }
                                }
                            } else {
                                Snackbar.with(mainActivity).text(mServerResponseVO.getMsg()).show(mainActivity);
                                mCommonHelper.hideKeyboard(mainActivity);
                            }
                        }
                        catch (Exception e)
                        {

                        }
                    }
                }
            }

            @Override
            public void onLoaderReset(Loader<TaskExecutor> arg0) {

            }
        });
    }

    @SuppressLint("RestrictedApi")
    public class GetAllOrderList extends TaskExecutor {
        protected GetAllOrderList(Context context, Bundle bundle) {
            super(context, bundle);
        }

        @Override
        protected Object executeTask() {

            GetAllOrderService getAllOrderService = new GetAllOrderService();
            mServerResponseVO = getAllOrderService.getAllOrder(mainActivity, Tags.GETALLORDER, mStringUserId);

            return null;
        }
    }

    public class OrderListData extends RecyclerView.Adapter<OrderListData.MyViewHolderOrder> {


        Context mContext;
        ArrayList<OrderDataVo> mArrayListorder;

        public OrderListData(MainActivity mainActivity, ArrayList<OrderDataVo> mArrayListOrderList) {

            this.mContext = mainActivity;
            this.mArrayListorder = mArrayListOrderList;
        }

        @NonNull
        @Override
        public MyViewHolderOrder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_my_orderlist, parent, false);
            MyViewHolderOrder holderOrder = new MyViewHolderOrder(v);
            return holderOrder;
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull MyViewHolderOrder holder, int position) {

            if (!mArrayListorder.get(position).getOrderStatus().equalsIgnoreCase("null")) {
                holder.mTextViewStatus.setText(mArrayListorder.get(position).getOrderStatus());
            }
            if (!mArrayListorder.get(position).getPaymentstatus().equalsIgnoreCase("null")) {
                holder.mTextViewPaymentStatus.setText(mArrayListorder.get(position).getPaymentstatus());
            }
//            if (!mArrayListOrderList.get(position).getUserName().equalsIgnoreCase("null")) {
//                holder.mTextViewName.setText(mArrayListOrderList.get(position).getUserName());
//            }
            if (!mArrayListOrderList.get(position).getOrderNo().equalsIgnoreCase("null")) {
                holder.mTextViewOrderNo.setText(mArrayListOrderList.get(position).getOrderNo());
            }
            if (!mArrayListOrderList.get(position).getDate().equalsIgnoreCase("null")) {

                String mDate = mArrayListOrderList.get(position).getDate();
                SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat fmt2 = new SimpleDateFormat("EEE, MMM, dd, yyyy");
                try {
                    Date date = fmt.parse(mDate);
                    String mString = fmt2.format(date);
                    holder.mTextViewOrderDate.setText(mString);
                } catch (ParseException pe) {
                    pe.printStackTrace();
                }
            }
            if (!mArrayListOrderList.get(position).getTotalRs().equalsIgnoreCase("null")) {
                holder.mTextViewamount.setText( Tags.LABEL_CURRENCY_SIGN + Utility.getDecimalFormate(mainActivity,Double.valueOf(mArrayListOrderList.get(position).getGrossRs()),0.0,Tags.DECIMAL_FORMAT));
//                holder.mTextViewamount.setText(Tags.LABEL_CURRENCY_SIGN + new DecimalFormat("##,###,##0.##").format(mArrayListOrderList.get(position).getGrossRs()));
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentMyOrderDetails fragmentMyOrderDetails = new  FragmentMyOrderDetails();
                    Bundle mBundle = new Bundle();
                    mBundle.putString("OrderId", mArrayListOrderList.get(position).getOrderId());
                    fragmentMyOrderDetails.setArguments(mBundle);
                    mainActivity.addFragment(fragmentMyOrderDetails, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, fragmentMyOrderDetails.getClass().getName());
                }
            });
            holder.mTextViewReoder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (!mCommonHelper.check_Internet(getActivity())) {
                        Snackbar.with(mainActivity).text(getResources().getString(R.string.interneterror)).show(mainActivity);
                        return;
                    }
                    final Bundle mBundle = new Bundle();
                    mBundle.putString(getString(R.string.app_name), mArrayListOrderList.get(position).getOrderId());
                }
            });
        }


        @Override
        public int getItemCount() {
            return mArrayListorder.size();
        }

        public class MyViewHolderOrder extends RecyclerView.ViewHolder {


            TextView mTextViewName;
            TextView mTextViewDetails;
            TextView mTextViewOrderNo;
            TextView mTextViewOrderDate;
            TextView mTextViewOrderExpectedDate;
            TextView mTextViewReoder;
            TextView mTextViewamount;
            TextView mTextViewStatus;
            TextView mTextViewPaymentStatus;
            LinearLayout mLinearLayoutExpectedDate;

            public MyViewHolderOrder(View itemView) {
                super(itemView);

//                mTextViewName = itemView.findViewById(R.id.row_my_order_textview_name);
                mTextViewDetails = itemView.findViewById(R.id.row_my_order_textview_Details);
                mTextViewOrderNo = itemView.findViewById(R.id.row_my_order_textview_order_value);
                mTextViewOrderDate = itemView.findViewById(R.id.row_my_order_textview_order_date_value);
//                mTextViewOrderExpectedDate = itemView.findViewById(R.id.row_my_order_textview_ExpectedDate_value);
                mTextViewReoder = itemView.findViewById(R.id.row_my_order_textview_reorder);
                mTextViewamount = itemView.findViewById(R.id.row_my_order_textview_order_amounrt);
                mTextViewStatus = itemView.findViewById(R.id.row_my_order_textview_status_value);
//                mLinearLayoutExpectedDate = itemView.findViewById(R.id.row_my_order_linear_order_ExpectedDate);
                mTextViewPaymentStatus =itemView.findViewById(R.id.row_my_order_textview_payment_status_value);
            }
        }
    }

    public void onResumeData() {
        mainActivity.toolbar.setVisibility(View.GONE);

        if (isfromfilter) {
            if (mArrayListOrderList != null && mArrayListOrderList.size() > 0) {

                mTextViewNoData.setVisibility(View.GONE);
                mRecyclerViewOrderList.setVisibility(View.VISIBLE);
                mTextViewFilter.setVisibility(View.VISIBLE);

                orderListData = new OrderListData(mainActivity, mArrayListOrderList);
                mRecyclerViewOrderList.setHasFixedSize(true);
                mRecyclerViewOrderList.setLayoutManager(new LinearLayoutManager(mainActivity));
                mRecyclerViewOrderList.setAdapter(orderListData);

            } else {
                mTextViewNoData.setVisibility(View.VISIBLE);
                mRecyclerViewOrderList.setVisibility(View.GONE);
                mTextViewFilter.setVisibility(View.VISIBLE);
            }
        } else {


            OrderListData();
        }


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