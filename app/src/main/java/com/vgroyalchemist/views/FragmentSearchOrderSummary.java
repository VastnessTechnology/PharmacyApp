package com.vgroyalchemist.views;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.vgroyalchemist.R;
import com.vgroyalchemist.controller.MainActivity;
import com.vgroyalchemist.loaders.LoaderTask;
import com.vgroyalchemist.loaders.LoaderTaskWithoutProgressDialog;
import com.vgroyalchemist.loaders.TaskExecutor;
import com.vgroyalchemist.requestobjects.GetCoupenInfo;
import com.vgroyalchemist.utils.CommonHelper;
import com.vgroyalchemist.utils.Snackbar;
import com.vgroyalchemist.utils.Tags;
import com.vgroyalchemist.utils.Utility;
import com.vgroyalchemist.vos.AllCartData;
import com.vgroyalchemist.vos.CounponlistTypeVO;
import com.vgroyalchemist.vos.ServerResponseVO;
import com.vgroyalchemist.vos.TaxChargeVO;
import com.vgroyalchemist.vos.TotalArrayVo;
import com.vgroyalchemist.vos.UserDetailsVO;
import com.vgroyalchemist.webservice.CheckUserAddressService;
import com.vgroyalchemist.webservice.GetAllAddressInfoService;
import com.vgroyalchemist.webservice.GetAllCartService;
import com.vgroyalchemist.webservice.GetCoupenService;
import com.vgroyalchemist.webservice.GetDefualtAddressInfoService;
import com.vgroyalchemist.webservice.GetSingalAddressInfoService;
import com.vgroyalchemist.webservice.PlaceOrderService;
import com.vgroyalchemist.webservice.PostParseGet;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.facebook.FacebookSdk.getApplicationContext;

/* developed by krishna 4-03-2021
 *   apply coupon code , user address edit select, display oder item list ,order place  */
public class FragmentSearchOrderSummary extends NonCartFragment {

    //Variable Declaration
    public static final String FRAGMENT_ID = "22";

    public static MainActivity mainActivity;
    public View fragmentView = null;
    CommonHelper mCommonHelper;
    ImageView mImageViewSearch;
    ImageView mImageViewCart;
    ImageView mImageViewBack;
    TextView mTextViewTitle;
    TextView mTextViewTotalPrice;
    TextView mTextViewDiscout;
    TextView mTextViewShppingCharge;
    TextView mTextViewGrossTotal;
    TextView mTextViewChnageAddress;
    TextView mTextViewName;
    TextView mTextViewAddresOne;
    TextView mTextViewPincode;
    ImageView mImageViewEditAddress;
    RecyclerView mRecyclerView;
    TextView mTextViewSubmit;
    TextView mTextViewOrderiteamCount;
    TextView mTextViewApply;
    EditText mEditTextCoupenCode;
    ImageView mImageViewDelete;
    TextView mTextViewCoupenValue;
    RelativeLayout mRelativeLayoutCoupenDisc;
    RelativeLayout mRelativeLayoutMain;
    RelativeLayout mRelativeLayoutAddress;
    TextView mTextViewAddAddress;
    LinearLayout mLinearLayoutAddress;
    RelativeLayout mRelativeLayout;

    Dialog mDialog;
    TextInputLayout mTextInputLayoutPromocode;
    private MyBaseAdaptercode mBaseAdaptercode;
    TaxChargeVO mTaxChargeVO;
    //    address
    ArrayList<UserDetailsVO> mUserDetailsVOArrayList;
    ArrayList<AllCartData> mArrayListItem;
    ArrayList<CounponlistTypeVO> mCounponlistTypeVOS;
    // userdata
    public SharedPreferences mSharedPreferencesUserId;
    SharedPreferences.Editor mEditor;

    public SharedPreferences mSharedPreferencesLatLng;
    SharedPreferences.Editor mEditorLatLng;

    String mStringUserId;
    String AddressID;
    String GorssTotal;
    String mStringSubtotal;
    String ShippingCharge;
    String CoupneDisc;
    String CoupenCodeValue;
    String mStringDiscount;

    ServerResponseVO mServerResponseVO;
    ServerResponseVO mServerResponseVOSetAddress;
    ServerResponseVO mServerResponseVOPlaceOrder;
    ServerResponseVO mServerResponseVOCoupen;
    PostParseGet mPostParseGet;
    TotalArrayVo totalArrayVo = new TotalArrayVo();

    Bundle mBundle;
    String AddressId;
    boolean SelectAddress = false;
    boolean isapply = false;
    boolean isDelete = false;

    RelativeLayout mRelativeLayoutUploadPretext;
    RelativeLayout mRelativeLayoutUploadPreImage;
    RecyclerView mRecyclerViewImage;
    ImageView mImageViewDwonArrow;
    ImageView mImageViewUpArrow;

    static ArrayList<Bitmap> mArrayListGetImage;
    static Bitmap mBitmap;
    static String mStringImageBase64;
    ArrayList mImageVOArrayList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainActivity = (MainActivity) getActivity();
        mCommonHelper = new CommonHelper();
        mPostParseGet = new PostParseGet(mainActivity);
        mUserDetailsVOArrayList = new ArrayList<UserDetailsVO>();
        mArrayListItem = new ArrayList<AllCartData>();
        mTaxChargeVO = new TaxChargeVO();
        mCounponlistTypeVOS = new ArrayList<CounponlistTypeVO>();
        mBundle = getArguments();
        mArrayListGetImage = new ArrayList<Bitmap>();
        mImageVOArrayList = new ArrayList();
        if (mBundle != null) {
            AddressId = mBundle.getString("SelcteAddresssId");
            SelectAddress = mBundle.getBoolean("SelAddress");
            mArrayListGetImage = (ArrayList<Bitmap>) getArguments().getSerializable("imagearray");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentView = inflater.inflate(R.layout.fragment_search_order_summary, container, false);

        mImageViewBack = fragmentView.findViewById(R.id.fragment_imageview_back);
        mTextViewTitle = fragmentView.findViewById(R.id.fragment_title_textview);
        mTextViewTitle.setText("Order Summary");
        mImageViewSearch = fragmentView.findViewById(R.id.fragment_Serach);
        mImageViewSearch.setVisibility(View.GONE);
        mImageViewCart = fragmentView.findViewById(R.id.fragment_cart);
        mImageViewCart.setVisibility(View.GONE);

        mTextViewChnageAddress = fragmentView.findViewById(R.id.fragment_order_Search_summary_textview_add_address_chnage);
        mTextViewName = fragmentView.findViewById(R.id.fragment_order_Search_summary_textview_address_houseNo);
        mTextViewAddresOne = fragmentView.findViewById(R.id.fragment_order_Search_summary_textview_address_one);
        mTextViewPincode = fragmentView.findViewById(R.id.fragment_order_Search_summary_textview_address_pincode);
        mTextViewTotalPrice = fragmentView.findViewById(R.id.row_review_textview_item_subtotal_total);
        mTextViewDiscout = fragmentView.findViewById(R.id.row_review_textview_item_subtotal_Discout);
        mTextViewShppingCharge = fragmentView.findViewById(R.id.row_review_textview_item_shipping_charge);
        mTextViewGrossTotal = fragmentView.findViewById(R.id.row_review_textview_item_gross_total);
        mImageViewEditAddress = fragmentView.findViewById(R.id.fragment_order_Search_summary_address_imageview_edit);
        mRecyclerView = fragmentView.findViewById(R.id.fragment_order_Search_summary_recycleview);
        mTextViewSubmit = fragmentView.findViewById(R.id.fragment_order_Search_summary_textview_submit);
        mTextViewOrderiteamCount = fragmentView.findViewById(R.id.fragment_order_Search_summary_textview_order_item_title);
        mTextViewApply = fragmentView.findViewById(R.id.fragment_order_Search_summary_textview_apply);
        mEditTextCoupenCode = fragmentView.findViewById(R.id.fragment_order_Search_summary_edittext_promocode);
        mImageViewDelete = fragmentView.findViewById(R.id.fragment_order_Search_summary_imageview_delete_code);
        mTextInputLayoutPromocode = fragmentView.findViewById(R.id.fragment_order_Search_summary_textinput);
        mTextViewCoupenValue = fragmentView.findViewById(R.id.row_review_textview_item_Discout_coupen_value);
        mRelativeLayoutCoupenDisc = fragmentView.findViewById(R.id.row_review_relative_coupen_discount);
        mRelativeLayoutMain = fragmentView.findViewById(R.id.fragment_order_Search_summary_relative_main_address);
        mRelativeLayoutAddress = fragmentView.findViewById(R.id.fragment_order_Search_summary_relative_address_title);

        mRelativeLayoutUploadPretext = fragmentView.findViewById(R.id.fragment_order_Search_summary_relative_presctiprion);
        mRelativeLayoutUploadPreImage = fragmentView.findViewById(R.id.fragment_order_Search_summary_linear_add_prescrption);
        mRecyclerViewImage = fragmentView.findViewById(R.id.fragment_order_Search_summary_recyclerview);
        mImageViewDwonArrow = fragmentView.findViewById(R.id.fragment_order_Search_summary_imageview_prescription_attech_dwon_arrow);
        mImageViewUpArrow = fragmentView.findViewById(R.id.fragment_order_Search_summary_imageview_prescription_attech_up_arrow);

        mTextViewAddAddress = fragmentView.findViewById(R.id.fragment_order_Search_summary_textview_add_address);
        mLinearLayoutAddress = fragmentView.findViewById(R.id.fragment_order_Search_summary_linear_Address);
        mRelativeLayout = fragmentView.findViewById(R.id.fragment_order_Search_summary_relative);

        mSharedPreferencesUserId = mainActivity.getSharedPreferences(Tags.TAG_SHARED_PREFERENCE_USER_ID, 0);
        mStringUserId = mSharedPreferencesUserId.getString("UserId", "");
        mEditor = mSharedPreferencesUserId.edit();

        mSharedPreferencesLatLng = mainActivity.getSharedPreferences("LatLng",0);
        mEditorLatLng = mSharedPreferencesLatLng.edit();

        mTextViewSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                PlaceOrder();

                FragmentPaymentDetails fragmentPaymentDetails = new FragmentPaymentDetails();
                Bundle mBundle = new Bundle();
                mBundle.putString("AddressID", AddressID);
                mBundle.putString("subtotal", mStringSubtotal);
                mBundle.putString("shipping_charge", ShippingCharge);
                mBundle.putString("GrossTotal", GorssTotal);
                mBundle.putString("CoupneDisc", CoupneDisc);
                mBundle.putString("Discoiunt", mStringDiscount);
                mBundle.putSerializable("imagearray", mArrayListGetImage);
                fragmentPaymentDetails.setArguments(mBundle);
                mainActivity.addFragment(fragmentPaymentDetails, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, fragmentPaymentDetails.getClass().getName());
            }
        });
        mTextViewChnageAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment currentFrag = mainActivity.getSupportFragmentManager().findFragmentById(R.id.activity_main_container);
                mCurrentFragment = currentFrag.getClass().getName();

                if (!mCurrentFragment.equalsIgnoreCase(Tags.TAG_FRAGEMENT_SELECT_ADDRESS)) {
                    FragmentSelectAddress mFragmentSelectAddress = new FragmentSelectAddress();
                    Bundle mBundle = new Bundle();
                    mBundle.putSerializable("imagearray", mArrayListGetImage);
                    mFragmentSelectAddress.setArguments(mBundle);
                    mainActivity.addFragment(mFragmentSelectAddress, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, mFragmentSelectAddress.getClass().getName());
                }

            }
        });

        mEditTextCoupenCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetCoupenCode();
            }
        });

        mTextViewApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mEditTextCoupenCode.getText().toString().equalsIgnoreCase("")) {
//                    Snackbar.with(mainActivity).text(getString(R.string.enter_promocode)).show(mainActivity);
                    Snackbar.with(mainActivity).text("Please Enter Promocode").show(mainActivity);
                    return;
                }

                isapply = true;
                getCartData();
            }
        });

        mImageViewEditAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment currentFrag = mainActivity.getSupportFragmentManager().findFragmentById(R.id.activity_main_container);
                mCurrentFragment = currentFrag.getClass().getName();

                if (!mCurrentFragment.equalsIgnoreCase(Tags.TAG_FRAGEMENT_ADD_ADDRESS)) {
                    Utility.debugger("Vt Defualt Adress id...." + AddressID);
                    FragmentAddAddress mFragmentListData = new FragmentAddAddress();
                    Bundle args = new Bundle();
                    args.putBoolean(Tags.isFromEdit, false);
                    args.putBoolean("fromorder", true);
                    args.putBoolean("fromMenu", false);
                    args.putSerializable("imagearray", mArrayListGetImage);
                    args.putString("AddressId", AddressID);
                    mFragmentListData.setArguments(args);
                    mainActivity.addFragment(mFragmentListData, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, mFragmentListData.getClass().getName());

                }

            }
        });

        mImageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Fragment currentFrag =mainActivity.getSupportFragmentManager().findFragmentById(R.id.activity_main_container);
//                mCurrentFragment = currentFrag.getClass().getName();
//                mainActivity. removeFragment(currentFrag, currentFrag.getClass().getName());
                mainActivity.onBackPressed();
            }
        });

        if (mArrayListGetImage.size() == 0) {
            mRecyclerViewImage.setVisibility(View.GONE);

        } else {

            mRelativeLayoutUploadPretext.setVisibility(View.VISIBLE);
            mRelativeLayoutUploadPreImage.setVisibility(View.VISIBLE);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 3);
            mRecyclerViewImage.setLayoutManager(gridLayoutManager);
            OrderPrescriptionGetImageAdapter prescriptionGetImageAdapter = new OrderPrescriptionGetImageAdapter(mainActivity, mArrayListGetImage);
            mRecyclerViewImage.setAdapter(prescriptionGetImageAdapter);
        }

        mImageViewDwonArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mRecyclerViewImage.setVisibility(View.VISIBLE);
                mImageViewUpArrow.setVisibility(View.VISIBLE);
                mImageViewDwonArrow.setVisibility(View.GONE);
            }
        });

        mImageViewUpArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRecyclerViewImage.setVisibility(View.GONE);
                mImageViewDwonArrow.setVisibility(View.VISIBLE);
                mImageViewUpArrow.setVisibility(View.GONE);

            }
        });

        mImageViewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!mEditTextCoupenCode.getText().toString().trim().equalsIgnoreCase("")) {
                    mEditTextCoupenCode.setText("");
                    mTextInputLayoutPromocode.setError("");
                    mImageViewDelete.setVisibility(View.INVISIBLE);

                    isDelete = true;
                    CoupenCodeValue = "";
                    getCartData();
                }
            }
        });


        mEditTextCoupenCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    mImageViewDelete.setVisibility(View.VISIBLE);
                } else {
                    mImageViewDelete.setVisibility(View.INVISIBLE);
                }
            }
        });

        mTextViewAddAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentAddAddress mFragmentListData = new FragmentAddAddress();
                Bundle args = new Bundle();
                args.putBoolean(Tags.isFromEdit, false);
                args.putBoolean("IsCart", true);
                args.putSerializable("imagearray", mArrayListGetImage);
                mFragmentListData.setArguments(args);
                mainActivity.addFragment(mFragmentListData, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, mFragmentListData.getClass().getName());

            }
        });


        return fragmentView;
    }


// default address api call

    public void GetDefaultAddress() {

        getLoaderManager().restartLoader(0, null, new LoaderManager.LoaderCallbacks<TaskExecutor>() {
            @Override
            public Loader<TaskExecutor> onCreateLoader(int arg0, Bundle arg1) {

                GetDefualtAddress mAddAddressExecutor = new GetDefualtAddress(mainActivity, null);
                return new LoaderTask<TaskExecutor>(mainActivity, mAddAddressExecutor);
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
                            mRelativeLayout.setVisibility(View.VISIBLE);
                            mTextViewSubmit.setVisibility(View.VISIBLE);

                            if (mServerResponseVO.getData() != null) {
                                mUserDetailsVOArrayList = (ArrayList<UserDetailsVO>) mServerResponseVO.getData();

                                if (mUserDetailsVOArrayList != null) {
                                    for (int i = 0; i < mUserDetailsVOArrayList.size(); i++) {
                                        mTextViewName.setText(mUserDetailsVOArrayList.get(i).getName());
                                        mTextViewAddresOne.setText(mUserDetailsVOArrayList.get(i).getAddress());
                                        mTextViewPincode.setText(mUserDetailsVOArrayList.get(i).getCityName() + "," + mUserDetailsVOArrayList.get(i).getPincode() + "   " + mUserDetailsVOArrayList.get(i).getStateName());


                                        AddressID = mUserDetailsVOArrayList.get(i).getAddressId();
                                    }
                                    String address = mTextViewAddresOne.getText().toString() + mTextViewPincode.getText().toString() ;
                                    Utility.debugger("vt Address ----- " +address);
                                    GeocodingLocation locationAddress = new GeocodingLocation();
                                    locationAddress.getAddressFromLocation(address,
                                            getApplicationContext(), new GeocoderHandler());

                                    getCartData();
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
    public class GetDefualtAddress extends TaskExecutor {
        protected GetDefualtAddress(Context context, Bundle bundle) {
            super(context, bundle);
        }

        @Override
        protected Object executeTask() {

            GetDefualtAddressInfoService getAllAddressInfoService = new GetDefualtAddressInfoService();
            mServerResponseVO = getAllAddressInfoService.GetDefualtAddress(mainActivity, Tags.GETDEFUALTADDRESS, mStringUserId);

            return null;
        }
    }

    //     get cart api call
    public void getCartData() {
        getLoaderManager().restartLoader(0, null, new LoaderManager.LoaderCallbacks<TaskExecutor>() {
            @Override
            public Loader<TaskExecutor> onCreateLoader(int arg0, Bundle arg1) {
                GetCartData mSearchOrderProductProcess = new GetCartData(mainActivity, null);
                return new LoaderTask<TaskExecutor>(mainActivity, mSearchOrderProductProcess);
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onLoadFinished(Loader<TaskExecutor> arg0, TaskExecutor arg1) {
                if (isAdded()) {
                    getLoaderManager().destroyLoader(0);
                    if (mPostParseGet.isNetError) {
                        Snackbar.with(mainActivity).text(getResources().getString(R.string.interneterror)).show(mainActivity);
                    } else if (mPostParseGet.isOtherError)
                        Snackbar.with(mainActivity).text(getResources().getString(R.string.notabletocommunicatewithserver)).show(mainActivity);
                    else {
                        if (mServerResponseVO != null && mServerResponseVO.getStatus() != null) {

                            if (mServerResponseVO.getStatus().equalsIgnoreCase("true")) {

                                if (mServerResponseVO.getData() != null) {

                                    mTaxChargeVO = ((TaxChargeVO) mServerResponseVO.getData());

                                    mArrayListItem = (ArrayList<AllCartData>) mTaxChargeVO.getmAllCartData();

                                    if (mArrayListItem != null && mArrayListItem.size() > 0) {


                                        mTextViewOrderiteamCount.setText(getResources().getString(R.string.item_list_as_prescrption) + "(" + mArrayListItem.size() + ")");
                                        SearchOrderIteam searchOrderIteam = new SearchOrderIteam(mainActivity, mArrayListItem);
                                        mRecyclerView.setHasFixedSize(true);
                                        mRecyclerView.setLayoutManager(new LinearLayoutManager(mainActivity));
                                        mRecyclerView.setAdapter(searchOrderIteam);
                                    }
                                    totalArrayVo = mTaxChargeVO.getmTotalArrayVo();

                                    if (totalArrayVo != null) {
                                        if (isapply) {
                                            if (totalArrayVo.getCoupenRs() != null && !totalArrayVo.getCoupenRs().equalsIgnoreCase("null")) {


                                                if (isDelete) {
                                                    mTextInputLayoutPromocode.setError("");
                                                    mRelativeLayoutCoupenDisc.setVisibility(View.GONE);
                                                    isDelete = false;

                                                } else {
                                                    if (totalArrayVo.getCoupenRs().equalsIgnoreCase("0.0")) {
                                                        mTextInputLayoutPromocode.setError("Invalid Coupon Code.");
                                                        mRelativeLayoutCoupenDisc.setVisibility(View.GONE);
                                                    } else {
                                                        mRelativeLayoutCoupenDisc.setVisibility(View.VISIBLE);
                                                        mTextInputLayoutPromocode.setError("");
                                                        mTextViewCoupenValue.setText("- " + Tags.LABEL_CURRENCY_SIGN + Utility.getDecimalFormate(mainActivity, Double.parseDouble(totalArrayVo.getCoupenRs()), 0.0, Tags.DECIMAL_FORMAT));
                                                    }
                                                }

                                            } else {
                                                mRelativeLayoutCoupenDisc.setVisibility(View.GONE);
                                            }
                                        }

                                        if (!totalArrayVo.getSubTotalRs().equalsIgnoreCase("null")) {
                                            mTextViewTotalPrice.setText(Tags.LABEL_CURRENCY_SIGN + Utility.getDecimalFormate(mainActivity, Double.parseDouble(totalArrayVo.getSubTotalRs()), 0.0, Tags.DECIMAL_FORMAT));

                                            mStringSubtotal = totalArrayVo.getSubTotalRs();
                                        }

                                        if (!totalArrayVo.getDiscRs().equalsIgnoreCase("null")) {
                                            mTextViewDiscout.setText("- " + Tags.LABEL_CURRENCY_SIGN + Utility.getDecimalFormate(mainActivity, Double.parseDouble(totalArrayVo.getDiscRs()), 0.0, Tags.DECIMAL_FORMAT));
                                            mStringDiscount = totalArrayVo.getDiscRs();
                                        }
                                        if (!totalArrayVo.getShippingCharge().equalsIgnoreCase("null")) {
                                            mTextViewShppingCharge.setText(Tags.LABEL_CURRENCY_SIGN + Utility.getDecimalFormate(mainActivity, Double.parseDouble(totalArrayVo.getShippingCharge()), 0.0, Tags.DECIMAL_FORMAT));
                                            ShippingCharge = totalArrayVo.getShippingCharge();
                                        } else {
                                            mTextViewShppingCharge.setText(Tags.LABEL_CURRENCY_SIGN + "0.0");
                                            ShippingCharge = "0";
                                        }
                                        if (!totalArrayVo.getTotallRs().equalsIgnoreCase("null")) {
                                            mTextViewGrossTotal.setText(Tags.LABEL_CURRENCY_SIGN + Utility.getDecimalFormate(mainActivity, Double.parseDouble(totalArrayVo.getTotallRs()), 0.0, Tags.DECIMAL_FORMAT));
                                            GorssTotal = totalArrayVo.getTotallRs();
                                        }

                                        if (totalArrayVo.getCoupenRs() != null) {
                                            CoupneDisc = totalArrayVo.getCoupenRs();
                                        } else {
                                            CoupneDisc = "0";
                                        }


                                    }
                                }
                            } else {
                                Snackbar.with(mainActivity).text(mServerResponseVO.getMsg()).show(mainActivity);
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


    public class GetCartData extends TaskExecutor {
        protected GetCartData(Context context, Bundle bundle) {
            super(context, bundle);
        }

        @Override
        protected Object executeTask() {
            GetAllCartService mGetAllCartService = new GetAllCartService();
            try {
                mServerResponseVO = mGetAllCartService.getAllCart(mainActivity, Tags.GETALLCARTDATA, mStringUserId, AddressID, CoupenCodeValue,Tags.mStringDeviceToken );
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public class SearchOrderIteam extends RecyclerView.Adapter<SearchOrderIteam.MyViewHolderItem> {
        Context mContext;
        ArrayList<AllCartData> mArrayListitem;

        public SearchOrderIteam(MainActivity mainActivity, ArrayList<AllCartData> mArrayListItem) {

            this.mContext = mainActivity;
            this.mArrayListitem = mArrayListItem;
        }


        @NonNull
        @Override
        public MyViewHolderItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_order_summary, parent, false);
            MyViewHolderItem myViewHolderItem = new MyViewHolderItem(v);
            return myViewHolderItem;
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolderItem holder, int position) {

            if (!mArrayListitem.get(position).getMedicineName().equalsIgnoreCase("null")) {
                holder.mTextViewName.setText(mArrayListitem.get(position).getMedicineName());
            }

            if (!mArrayListitem.get(position).getContent().equalsIgnoreCase("null")) {
                holder.mTextViewDesciprion.setText(mArrayListitem.get(position).getContent());
            }

            if (!mArrayListitem.get(position).getPrice().equalsIgnoreCase("null")) {
                holder.mTextViewPrice.setText(Tags.LABEL_CURRENCY_SIGN + Utility.getDecimalFormate(mainActivity, Double.parseDouble(mArrayListitem.get(position).getAmount()), 0.0, Tags.DECIMAL_FORMAT));
            }

            if (!mArrayListitem.get(position).getQty().equalsIgnoreCase("null")) {
                holder.mTextViewqty.setText("Qty - " + mArrayListitem.get(position).getQty());
            }
        }

        @Override
        public int getItemCount() {
            return mArrayListitem.size();
        }


        public class MyViewHolderItem extends RecyclerView.ViewHolder {

            TextView mTextViewName;
            TextView mTextViewDesciprion;
            TextView mTextViewPrice;
            TextView mTextViewqty;


            LinearLayout mLinearLayout;

            public MyViewHolderItem(View itemView) {
                super(itemView);

                mTextViewName = itemView.findViewById(R.id.row__order_item_textview_medicine_name);
                mTextViewDesciprion = itemView.findViewById(R.id.row__order_item_textview_medicine_description);
                mTextViewPrice = itemView.findViewById(R.id.row__order_item_textview_price);
                mTextViewqty = itemView.findViewById(R.id.row__order_item_textview_Qty);


            }
        }
    }

    //  select address api call
    public void SelctedAddress() {

        getLoaderManager().restartLoader(0, null, new LoaderManager.LoaderCallbacks<TaskExecutor>() {
            @Override
            public Loader<TaskExecutor> onCreateLoader(int arg0, Bundle arg1) {

                EditAddressExecutor editAddressExecutor = new EditAddressExecutor(mainActivity, null);
                return new LoaderTaskWithoutProgressDialog<TaskExecutor>(mainActivity, editAddressExecutor);
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
                        if (mServerResponseVOSetAddress.getStatus().equalsIgnoreCase("true")) {
//                            Snackbar.with(mainActivity).text(mServerResponseVO.getMsg()).show(mainActivity);
                            mCommonHelper.hideKeyboard(mainActivity);
                            mRelativeLayout.setVisibility(View.VISIBLE);
                            mTextViewSubmit.setVisibility(View.VISIBLE);

                            if (mServerResponseVOSetAddress.getData() != null) {
                                mUserDetailsVOArrayList = (ArrayList<UserDetailsVO>) mServerResponseVOSetAddress.getData();

                                if (mUserDetailsVOArrayList != null) {
                                    for (int i = 0; i < mUserDetailsVOArrayList.size(); i++) {
                                        mTextViewName.setText(mUserDetailsVOArrayList.get(i).getName());
                                        mTextViewAddresOne.setText(mUserDetailsVOArrayList.get(i).getAddress());
                                        mTextViewPincode.setText(mUserDetailsVOArrayList.get(i).getCityName() + "," + mUserDetailsVOArrayList.get(i).getPincode() + "   " + mUserDetailsVOArrayList.get(i).getStateName());

                                        AddressID = mUserDetailsVOArrayList.get(i).getAddressId();

                                    }
                                    String address = mTextViewAddresOne.getText().toString() +mTextViewPincode.getText().toString() ;
                                    Utility.debugger("Vt Selected Address ----->" + address);
                                    GeocodingLocation locationAddress = new GeocodingLocation();
                                    locationAddress.getAddressFromLocation(address,
                                            getApplicationContext(), new GeocoderHandler());

                                    getCartData();
                                }
                            }
                        } else {
                            Snackbar.with(mainActivity).text(mServerResponseVOSetAddress.getMsg()).show(mainActivity);
                            mRelativeLayout.setVisibility(View.GONE);
                            mTextViewSubmit.setVisibility(View.GONE);
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
    public class EditAddressExecutor extends TaskExecutor {
        protected EditAddressExecutor(Context context, Bundle bundle) {
            super(context, bundle);
        }

        @Override
        protected Object executeTask() {
            GetSingalAddressInfoService mEditAddressInfoService = new GetSingalAddressInfoService();
            mServerResponseVOSetAddress = mEditAddressInfoService.fetcheditaddress(mainActivity, Tags.GetAddressData, AddressId);
            return null;
        }
    }


    private class GeocoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            String locationAddress;
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    locationAddress = bundle.getString("address");
                    break;
                default:
                    locationAddress = null;
            }
//            Toast.makeText(mainActivity, locationAddress, Toast.LENGTH_SHORT).show();

        }
    }

    //    get coupen code
    public void GetCoupenCode() {
        getLoaderManager().restartLoader(0, null, new LoaderManager.LoaderCallbacks<TaskExecutor>() {
            @Override
            public Loader<TaskExecutor> onCreateLoader(int arg0, Bundle arg1) {
                GetCoupenCodeExecutor mGetCoupenCodeExecutor = new GetCoupenCodeExecutor(mainActivity, null);
                return new LoaderTask<TaskExecutor>(mainActivity, mGetCoupenCodeExecutor);
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
                        if (mServerResponseVOCoupen != null && mServerResponseVOCoupen.getStatus() != null) {

                            if (mServerResponseVOCoupen.getStatus().equalsIgnoreCase("true")) {

                                if (mServerResponseVOCoupen.getData() != null) {

                                    mDialog = new Dialog(mainActivity, android.R.style.Theme_Black_NoTitleBar);
                                    mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                    mDialog.setContentView(R.layout.fragment_coupon_code);
                                    mDialog.setCanceledOnTouchOutside(false);

                                    mDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);

                                    ListView mListView = mDialog.findViewById(R.id.fragment_coupon_listview);
                                    TextView mTextView = mDialog.findViewById(R.id.fragment_coupon_textview_nodata);
                                    ImageView mImageViewBack = mDialog.findViewById(R.id.fragment_imageview_back);
                                    TextView mTextViewTitle = mDialog.findViewById(R.id.fragment_title_textview);
                                    mTextViewTitle.setText("Apply Coupon");
                                    ImageView mImageViewSearch = mDialog.findViewById(R.id.fragment_Serach);
                                    mImageViewSearch.setVisibility(View.GONE);
                                    ImageView mImageViewCart = mDialog.findViewById(R.id.fragment_cart);
                                    mImageViewCart.setVisibility(View.GONE);


                                    mImageViewBack.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            mDialog.dismiss();
                                        }
                                    });

                                    if (mCounponlistTypeVOS != null) {
                                        mCounponlistTypeVOS = (ArrayList<CounponlistTypeVO>) mServerResponseVOCoupen.getData();

                                        if (mCounponlistTypeVOS.size() == 0) {
                                            mListView.setVisibility(View.GONE);
                                            mTextView.setVisibility(View.VISIBLE);
                                            mBaseAdaptercode = new MyBaseAdaptercode(mainActivity, R.layout.row_coupon, mCounponlistTypeVOS);
                                            mListView.setAdapter(mBaseAdaptercode);
                                        } else {
                                            mListView.setVisibility(View.VISIBLE);
                                            mTextView.setVisibility(View.GONE);
                                            mBaseAdaptercode = new MyBaseAdaptercode(mainActivity, R.layout.row_coupon, mCounponlistTypeVOS);
                                            mListView.setAdapter(mBaseAdaptercode);
                                        }

                                        mDialog.show();
                                    } else {
                                        mTextInputLayoutPromocode.setError(getResources().getString(R.string.no_coupe_code));
                                    }


                                }
                            } else {
                                Snackbar.with(mainActivity).text(mServerResponseVOCoupen.getMsg()).show(mainActivity);
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


    public class GetCoupenCodeExecutor extends TaskExecutor {
        protected GetCoupenCodeExecutor(Context context, Bundle bundle) {
            super(context, bundle);
        }

        @Override
        protected Object executeTask() {
            GetCoupenService mGetAllCartService = new GetCoupenService();
            try {
                mServerResponseVOCoupen = mGetAllCartService.getcoupen(mainActivity, Tags.GETCOUPEN,mStringUserId);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }


    public class MyBaseAdaptercode extends ArrayAdapter<CounponlistTypeVO> {

        ArrayList<CounponlistTypeVO> mArrayList;

        public MyBaseAdaptercode(Context context, int resource, ArrayList<CounponlistTypeVO> objects) {
            super(context, resource, objects);
            mArrayList = objects;
        }

        @Override
        public int getCount() {
            return mArrayList.size();
        }

        @SuppressLint("SetTextI18n")
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {


            final ViewHoldercode holder;

            if (convertView == null) {

                convertView = mainActivity.getLayoutInflater().inflate(R.layout.row_coupon, parent, false);
                holder = new ViewHoldercode();


                holder.mTextViewProductName = convertView.findViewById(R.id.row_coupon_textview_couponname);
                holder.mTextViewproductDesctiption = convertView.findViewById(R.id.row_coupon_textview_description);
                holder.mTextViewExpireDate = convertView.findViewById(R.id.row_coupon_textview_ExpiresDate);

                convertView.setTag(holder);

            } else {
                holder = (ViewHoldercode) convertView.getTag();
            }


            holder.mTextViewProductName.setText(mArrayList.get(position).getCoupenCode());
            if (mArrayList.get(position).getDescription() != null && !mArrayList.get(position).getDescription().equalsIgnoreCase("null")) {

                holder.mTextViewproductDesctiption.setText(mArrayList.get(position).getDescription());
            }

            if (mArrayList.get(position).getExpiryDate() != null || !mArrayList.get(position).getExpiryDate().equalsIgnoreCase("null")) {
                String mDate = mArrayList.get(position).getExpiryDate();
                SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                SimpleDateFormat fmt2 = new SimpleDateFormat("yyyy-MMM-dd");
                try {
                    Date date = fmt.parse(mDate);
                    String mString = fmt2.format(date);

                    holder.mTextViewExpireDate.setText("Expires :- " + mString);
                } catch (ParseException pe) {
                    pe.printStackTrace();
                }
            }

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    CoupenCodeValue = mArrayList.get(position).getCoupenCode();
                    mEditTextCoupenCode.setText(mArrayList.get(position).getCoupenCode());
                    mDialog.dismiss();

                }
            });

            holder.mTextViewproductDesctiption.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CoupenCodeValue = mArrayList.get(position).getCoupenCode();
                    mEditTextCoupenCode.setText(mArrayList.get(position).getCoupenCode());
                    mDialog.dismiss();
                }
            });


            return convertView;
        }
    }

    static class ViewHoldercode {
        TextView mTextViewProductName;
        TextView mTextViewproductDesctiption;
        TextView mTextViewExpireDate;

    }

    public void onResumeData() {
        mainActivity.toolbar.setVisibility(View.GONE);

        CheckUserAddress();
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


    public class OrderPrescriptionGetImageAdapter extends RecyclerView.Adapter<OrderPrescriptionGetImageAdapter.MyViewHolderOderPre> {

        Context mContext;
        ArrayList<Bitmap> mArrayList;

        public OrderPrescriptionGetImageAdapter(MainActivity mainActivity, ArrayList<Bitmap> mArrayListGetImage) {

            this.mContext = mainActivity;
            this.mArrayList = mArrayListGetImage;
        }

        @NonNull
        @Override
        public MyViewHolderOderPre onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_upload_prescription, parent, false);
            MyViewHolderOderPre vh = new MyViewHolderOderPre(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolderOderPre holder, int position) {

//            if(mArrayList.get(position) != null ) {
//                Glide.with(mContext)
//                        .load(mArrayList.get(position))
//                        .placeholder(R.drawable.logo)
//                        .centerCrop()
//                        .into(holder.mImageViewSelectIamge);
//            }
            holder.mImageViewSelectIamge.setImageBitmap(mArrayList.get(position));
            holder.mImageViewDelete.setTag(position);
            holder.mImageViewDelete.setVisibility(View.GONE);

        }

        @Override
        public int getItemCount() {
            return mArrayList.size();
        }

        public class MyViewHolderOderPre extends RecyclerView.ViewHolder {
            ImageView mImageViewSelectIamge;
            ImageView mImageViewDelete;

            public MyViewHolderOderPre(View itemView) {
                super(itemView);
                mImageViewSelectIamge = (ImageView) itemView.findViewById(R.id.row_upload_prescription_imageview);
                mImageViewDelete = (ImageView) itemView.findViewById(R.id.row_upload_prescription_imageview_delete);
            }
        }
    }

    // check user address avaliber or not
    public void CheckUserAddress() {

        getLoaderManager().restartLoader(0, null, new LoaderManager.LoaderCallbacks<TaskExecutor>() {
            @Override
            public Loader<TaskExecutor> onCreateLoader(int arg0, Bundle arg1) {

                CheckUserAddress checkUserAddress = new CheckUserAddress(mainActivity, null);
                return new LoaderTask<TaskExecutor>(mainActivity, checkUserAddress);
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

                            mRelativeLayout.setVisibility(View.VISIBLE);
                            mTextViewChnageAddress.setVisibility(View.VISIBLE);
                            mTextViewAddAddress.setVisibility(View.GONE);
                            mTextViewSubmit.setVisibility(View.VISIBLE);
                            if (SelectAddress == true) {
                                SelctedAddress();
                            } else {
                                GetDefaultAddress();
                            }

                        } else {
//                            Snackbar.with(mainActivity).text(mServerResponseVO.getMsg()).show(mainActivity);
                            mCommonHelper.hideKeyboard(mainActivity);
                            mRelativeLayout.setVisibility(View.GONE);
                            mTextViewChnageAddress.setVisibility(View.GONE);
                            mTextViewAddAddress.setVisibility(View.VISIBLE);
                            mTextViewSubmit.setVisibility(View.GONE);
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
    public class CheckUserAddress extends TaskExecutor {
        protected CheckUserAddress(Context context, Bundle bundle) {
            super(context, bundle);
        }

        @Override
        protected Object executeTask() {

            CheckUserAddressService getAllAddressInfoService = new CheckUserAddressService();
            mServerResponseVO = getAllAddressInfoService.checkuseraddress(mainActivity, Tags.CheckUserAddress, mStringUserId);

            return null;
        }
    }


    public class GeocodingLocation {

        private static final String TAG = "GeocodingLocation";

        public  void getAddressFromLocation(final String locationAddress,
                                                  final Context context, final Handler handler) {
            Thread thread = new Thread() {
                @Override
                public void run() {
                    Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                    String result = null;
                    try {
                        List addressList = geocoder.getFromLocationName(locationAddress, 1);
                        if (addressList != null && addressList.size() > 0) {
                            Address address = (Address) addressList.get(0);
                            StringBuilder sb = new StringBuilder();
                            sb.append(address.getLatitude()).append("\n");
                            sb.append(address.getLongitude()).append("\n");
                            mEditorLatLng.putString("Latitude", String.valueOf(address.getLatitude()));
                            mEditorLatLng.putString("Longitude", String.valueOf(address.getLongitude()));
                            mEditorLatLng.commit();
                            result = sb.toString();
                        }
                    } catch (IOException e) {
                        Log.e(TAG, "Unable to connect to Geocoder", e);
                    } finally {
                        Message message = Message.obtain();
                        message.setTarget(handler);
                        if (result != null) {
                            message.what = 1;
                            Bundle bundle = new Bundle();
                            result = " Latitude and Longitude :\n" + result;
                            bundle.putString("address", result);
                            message.setData(bundle);
                        }
                        message.sendToTarget();
                    }
                }
            };
            thread.start();
        }
    }


}