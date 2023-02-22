package com.vgroyalchemist.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vgroyalchemist.R;
import com.vgroyalchemist.controller.MainActivity;
import com.vgroyalchemist.loaders.LoaderTask;
import com.vgroyalchemist.loaders.TaskExecutor;
import com.vgroyalchemist.utils.CommonHelper;
import com.vgroyalchemist.utils.Snackbar;
import com.vgroyalchemist.utils.Tags;
import com.vgroyalchemist.vos.OrderDataVo;
import com.vgroyalchemist.vos.OrderFiltersVo;
import com.vgroyalchemist.vos.OrderFlterMenu;
import com.vgroyalchemist.vos.ServerResponseVO;
import com.vgroyalchemist.vos.TimeFilterVO;
import com.vgroyalchemist.webservice.GetAllOrderService;
import com.vgroyalchemist.webservice.GetOrderFilterApplyService;
import com.vgroyalchemist.webservice.GetOrderFilterMenuService;
import com.vgroyalchemist.webservice.PostParseGet;

import java.util.ArrayList;

// developed by krishna 9-03-2021
// this class is use get order filter list data

public class FragmentOrderFilter extends NonCartFragment {

    //Variable Declaration
    public static final String FRAGMENT_ID = "41";

    public static MainActivity mainActivity;
    public View fragmentView = null;
    CommonHelper mCommonHelper;

    ImageView mImageViewBack;
    TextView mTextViewTitle;
    ImageView mImageViewSearch;
    ImageView mImageViewCart;

    TextView mTextViewOrderApply;
    RelativeLayout mRelativeLayoutMain;
    RelativeLayout mLinearLayoutOrderFilter;
    RadioGroup mRadioGroupOrderFilter;
    RadioGroup.LayoutParams mRadioGroupLayoutParamsOrder;

    RelativeLayout mLinearLayoutTimeFilter;
    RadioGroup mRadioGroupTimeFilter;
    RadioGroup.LayoutParams mRadioGroupLayoutParamsTime;

    OrderFiltersVo mOrderFiltersVo;
    ArrayList<OrderFlterMenu> mStringArrayList;
    ArrayList<TimeFilterVO> mTimeFilterVOS;
    // userdata
    public SharedPreferences mSharedPreferencesUserId;
    SharedPreferences.Editor mEditor;
    String mStringUserId;
    String mStringOrderId;
    String mStringTimeId;

    ServerResponseVO mServerResponseVO;
    PostParseGet mPostParseGet;
    ArrayList<OrderDataVo> mArrayListOrderList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = (MainActivity) getActivity();
        mCommonHelper = new CommonHelper();
        mPostParseGet = new PostParseGet(mainActivity);

        mOrderFiltersVo = new OrderFiltersVo();
        mStringArrayList = new ArrayList<OrderFlterMenu>();
        mTimeFilterVOS = new ArrayList<TimeFilterVO>();
        mArrayListOrderList = new ArrayList<OrderDataVo>();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentView = inflater.inflate(R.layout.fragment_order_filter, container, false);

        mImageViewBack = fragmentView.findViewById(R.id.fragment_imageview_back);
        mTextViewTitle = fragmentView.findViewById(R.id.fragment_title_textview);
        mTextViewTitle.setText("Order Filter");
        mImageViewSearch = fragmentView.findViewById(R.id.fragment_Serach);
        mImageViewSearch.setVisibility(View.GONE);
        mImageViewCart = fragmentView.findViewById(R.id.fragment_cart);
        mImageViewCart.setVisibility(View.GONE);

        mLinearLayoutOrderFilter = fragmentView.findViewById(R.id.fragment_my_orderlist_filter_linear_radiobutton);
        mRadioGroupOrderFilter = fragmentView.findViewById(R.id.fragment_my_orderlist_filter_radiogroup);
        mTextViewOrderApply = fragmentView.findViewById(R.id.fragment_my_orderlist_filter_textview_apply);

        mLinearLayoutTimeFilter = fragmentView.findViewById(R.id.fragment_my_orderlist_filter_linear_radiobutton_time_filter);
        mRadioGroupTimeFilter = fragmentView.findViewById(R.id.fragment_my_orderlist_filter_radiogroup_Time_filter);
        mRelativeLayoutMain =fragmentView.findViewById(R.id.fragment_my_orderlist_filter_relative_main);

        mSharedPreferencesUserId = mainActivity.getSharedPreferences(Tags.TAG_SHARED_PREFERENCE_USER_ID, 0);
        mStringUserId = mSharedPreferencesUserId.getString("UserId", "");
        mEditor = mSharedPreferencesUserId.edit();

        mImageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.onBackPressed();
            }
        });


        OrderFilterMenuData();

        mTextViewOrderApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                OrderFilterapplyData();
            }
        });
        return fragmentView;
    }

// get order filter list data
    public void OrderFilterMenuData() {
        getLoaderManager().restartLoader(0, null, new LoaderManager.LoaderCallbacks<TaskExecutor>() {
            @Override
            public Loader<TaskExecutor> onCreateLoader(int arg0, Bundle arg1) {

                GetAllOrderFilterMenu getAllOrderFilterMenu = new GetAllOrderFilterMenu(mainActivity, null);
                return new LoaderTask<TaskExecutor>(mainActivity, getAllOrderFilterMenu);
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

                        if (mServerResponseVO.getStatus().equalsIgnoreCase("true")) {
//                            Snackbar.with(mainActivity).text(mServerResponseVO.getMsg()).show(mainActivity);
                            mCommonHelper.hideKeyboard(mainActivity);

                            if (mServerResponseVO.getData() != null) {

                                mRelativeLayoutMain.setVisibility(View.VISIBLE);
                                mTextViewOrderApply.setVisibility(View.VISIBLE);

                                mOrderFiltersVo = (OrderFiltersVo) mServerResponseVO.getData();

                                mStringArrayList = mOrderFiltersVo.getOrderFlterMenus();
                                if (mStringArrayList != null && mStringArrayList.size() > 0) {


                                    for (int i = 0; i < mStringArrayList.size(); i++) {

                                        RadioButton radioButton = new RadioButton(mainActivity);
                                        radioButton.setText(mStringArrayList.get(i).getName());
                                        radioButton.setTag(mStringArrayList.get(i).getId());

                                        if (i == 0) {
                                            radioButton.setChecked(true);
                                            mStringOrderId =mStringArrayList.get(0).getId();
                                        }

                                        radioButton.setId(i);

                                        radioButton.setTextSize(16);
                                        radioButton.setTextColor(getResources().getColor(R.color.gray));
                                        radioButton.setGravity(Gravity.CENTER_VERTICAL);
                                        radioButton.setPadding(20, 20, 10, 20);

                                        final int sdk = Build.VERSION.SDK_INT;
                                        if (sdk < Build.VERSION_CODES.JELLY_BEAN) {
                                            radioButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.custom_divider));
                                        } else {
                                            radioButton.setBackground(getResources().getDrawable(R.drawable.custom_divider));
                                        }

                                        mRadioGroupLayoutParamsOrder = new RadioGroup.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                                        mRadioGroupLayoutParamsOrder.setMargins(10, 15, 10, 15);
                                        mRadioGroupOrderFilter.addView(radioButton, mRadioGroupLayoutParamsOrder);


                                    }
                                    mRadioGroupOrderFilter.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                        @Override
                                        public void onCheckedChanged(RadioGroup group, int checkedId) {

                                            ((RadioButton) mRadioGroupOrderFilter.getChildAt(checkedId)).setChecked(true);
                                            mRadioGroupOrderFilter.getCheckedRadioButtonId();
                                            int selectedId = mRadioGroupOrderFilter.getCheckedRadioButtonId();

                                            mStringOrderId =mStringArrayList.get(selectedId).getId();
                                        }
                                    });
                                    ((RadioButton) mRadioGroupOrderFilter.getChildAt(0)).setChecked(true);
                                }

                                mTimeFilterVOS = mOrderFiltersVo.getmTimeFilterVOS();

                                if (mTimeFilterVOS != null && mTimeFilterVOS.size() > 0) {

                                    for (int i = 0; i < mTimeFilterVOS.size(); i++) {
                                        RadioButton radioButton = new RadioButton(mainActivity);
                                        radioButton.setText(mTimeFilterVOS.get(i).getName());
                                        radioButton.setTag(mTimeFilterVOS.get(i).getId());

                                        if (i == 0) {
                                            radioButton.setChecked(true);
                                            mStringTimeId =mTimeFilterVOS.get(0).getId();
                                        }
                                        radioButton.setId(i);
                                        radioButton.setTextSize(16);
                                        radioButton.setTextColor(getResources().getColor(R.color.gray));
                                        radioButton.setGravity(Gravity.CENTER_VERTICAL);
                                        radioButton.setPadding(20, 20, 10, 20);

                                        final int sdk = Build.VERSION.SDK_INT;
                                        if (sdk < Build.VERSION_CODES.JELLY_BEAN) {
                                            radioButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.custom_divider));
                                        } else {
                                            radioButton.setBackground(getResources().getDrawable(R.drawable.custom_divider));
                                        }

                                        mRadioGroupLayoutParamsTime = new RadioGroup.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                                        mRadioGroupLayoutParamsTime.setMargins(10, 15, 10, 15);
                                        mRadioGroupTimeFilter.addView(radioButton, mRadioGroupLayoutParamsTime);

                                    }

                                    mRadioGroupTimeFilter.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                        @Override
                                        public void onCheckedChanged(RadioGroup group, int checkedId) {

                                            ((RadioButton) mRadioGroupTimeFilter.getChildAt(checkedId)).setChecked(true);
                                             mRadioGroupTimeFilter.getCheckedRadioButtonId();
                                            int selectedId = mRadioGroupTimeFilter.getCheckedRadioButtonId();

                                            mStringTimeId =mTimeFilterVOS.get(selectedId).getId();
                                        }
                                    });
                                    ((RadioButton) mRadioGroupTimeFilter.getChildAt(0)).setChecked(true);

                                }

                            }
                        } else {
                            Snackbar.with(mainActivity).text(mServerResponseVO.getMsg()).show(mainActivity);
                            mCommonHelper.hideKeyboard(mainActivity);
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
    public class GetAllOrderFilterMenu extends TaskExecutor {
        protected GetAllOrderFilterMenu(Context context, Bundle bundle) {
            super(context, bundle);
        }

        @Override
        protected Object executeTask() {

            GetOrderFilterMenuService getOrderFilterMenuService = new GetOrderFilterMenuService();
            mServerResponseVO = getOrderFilterMenuService.getAllOrder(mainActivity, Tags.GetOrderFilterMenu, mStringUserId);

            return null;
        }
    }


// Apply Order Filter
    public void OrderFilterapplyData() {
        getLoaderManager().restartLoader(0, null, new LoaderManager.LoaderCallbacks<TaskExecutor>() {
            @Override
            public Loader<TaskExecutor> onCreateLoader(int arg0, Bundle arg1) {

                GetAllOrderFilterapply getAllOrderFilterapply = new GetAllOrderFilterapply(mainActivity, null);
                return new LoaderTask<TaskExecutor>(mainActivity, getAllOrderFilterapply);
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

                        if (mServerResponseVO.getStatus().equalsIgnoreCase("true")) {
//                            Snackbar.with(mainActivity).text(mServerResponseVO.getMsg()).show(mainActivity);
                            mCommonHelper.hideKeyboard(mainActivity);

                             mServerResponseVO.getData();

                            if (mServerResponseVO.getData() != null) {

                                mArrayListOrderList = (ArrayList<OrderDataVo>) mServerResponseVO.getData();

                                @SuppressLint("HandlerLeak") Handler handler = new Handler() {
                                    @Override
                                    public void handleMessage(Message msg) {
                                        if (msg.what == Tags.WHAT) {
//                                            mainActivity.onBackPressed();

                                            mainActivity.removeCurrentFragment();
                                            FragmentMyOrderList fragmentMyOrderList = new FragmentMyOrderList();
                                            Bundle mBundle  = new Bundle();
                                            mBundle.putParcelableArrayList("OrderFilterList",mArrayListOrderList);
                                            mBundle.putBoolean("IsFromFilter",true);
                                            fragmentMyOrderList.setArguments(mBundle);
                                           mainActivity.addFragment(fragmentMyOrderList, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, fragmentMyOrderList.getClass().getName());
                                        }
                                    }
                                };
                                handler.sendEmptyMessage(Tags.WHAT);
                            }
                        } else {
                            Snackbar.with(mainActivity).text(mServerResponseVO.getMsg()).show(mainActivity);
                            mCommonHelper.hideKeyboard(mainActivity);
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
    public class GetAllOrderFilterapply extends TaskExecutor {
        protected GetAllOrderFilterapply(Context context, Bundle bundle) {
            super(context, bundle);
        }

        @Override
        protected Object executeTask() {

            GetOrderFilterApplyService getOrderFilterMenuService = new GetOrderFilterApplyService();
            mServerResponseVO = getOrderFilterMenuService.OrderFilterApply(mainActivity, Tags.GetOrderFilterList, mStringUserId,mStringOrderId,mStringTimeId);

            return null;
        }
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