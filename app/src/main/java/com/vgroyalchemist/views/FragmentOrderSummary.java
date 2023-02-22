package com.vgroyalchemist.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Message;
import android.os.TokenWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.vgroyalchemist.R;
import com.vgroyalchemist.controller.MainActivity;
import com.vgroyalchemist.db.DBService;
import com.vgroyalchemist.loaders.LoaderTask;
import com.vgroyalchemist.loaders.LoaderTaskWithoutProgressDialog;
import com.vgroyalchemist.loaders.TaskExecutor;
import com.vgroyalchemist.utils.CommonHelper;
import com.vgroyalchemist.utils.Snackbar;
import com.vgroyalchemist.utils.Tags;
import com.vgroyalchemist.utils.Utility;
import com.vgroyalchemist.vos.ServerResponseVO;
import com.vgroyalchemist.vos.UserDetailsVO;
import com.vgroyalchemist.webservice.AddOrderReturnService;
import com.vgroyalchemist.webservice.CheckUserAddressService;
import com.vgroyalchemist.webservice.GetDefualtAddressInfoService;
import com.vgroyalchemist.webservice.GetSingalAddressInfoService;
import com.vgroyalchemist.webservice.OrderWithPrescriptionService;
import com.vgroyalchemist.webservice.PostParseGet;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.facebook.FacebookSdk.getApplicationContext;

/* developed by krishna 1-03-2021
*  oreder place with presciption */
public class FragmentOrderSummary extends NonCartFragment {

    //Variable Declaration
    public static final String FRAGMENT_ID = "14";

    public static MainActivity mainActivity;
    public View fragmentView = null;
    CommonHelper mCommonHelper;
    ImageView mImageViewSearch;
    ImageView mImageViewCart;
    ImageView mImageViewBack;
    TextView mTextViewTitle;
    TextView mTextViewHouseNo;
    TextView mTextViewstreetName;
    TextView mTextViewPincode;
    TextView mTextViewChangeAddress;
    ImageView mImageViewEditAddress;
    RecyclerView mRecyclerViewPrescrption;
    ImageView mImageViewarrow;
    TextView mTextViewPrescrptionView;
    static ArrayList<Bitmap> mArrayListGetImage;
    ImageView mImageViewUPArrow;
    TextView mTextViewSubmit;
    RelativeLayout mRelativeLayout;
    TextView mTextViewAddAddress;

    static Bitmap mBitmap;
    Bundle mBundle;
    int mIntRawId = 0;
    ArrayList<UserDetailsVO> mArrayListUserDetailVO;
    DBService mDbService;
    private SharedPreferences mSharedPreferencesSelectedAddress;
    UserDetailsVO mUserDetailsVO;
    static String mStringImageBase64;
    String AddressID;
    String mStringAddressId;
    boolean SelectAddress = false;

    ServerResponseVO mServerResponseVO;
    ServerResponseVO mServerResponseVOSetAddress;
    PostParseGet mPostParseGet;

    // userdata
    public SharedPreferences mSharedPreferencesUserId;
    SharedPreferences.Editor mEditor;
    String mStringUserId;

    //    address
    ArrayList<UserDetailsVO> mUserDetailsVOArrayList;
    ArrayList mImageVOArrayList;

    String mStringLatitude;
    String mStringLongtitude;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = (MainActivity) getActivity();
        mCommonHelper = new CommonHelper();
        mArrayListGetImage = new ArrayList<Bitmap>();
        mArrayListUserDetailVO = new ArrayList<UserDetailsVO>();
        mPostParseGet = new PostParseGet(mainActivity);
        mDbService = new DBService();
        mBundle = getArguments();
        mUserDetailsVO = new UserDetailsVO();
        mUserDetailsVOArrayList = new ArrayList<UserDetailsVO>();
        mImageVOArrayList = new ArrayList();
        if (mBundle != null) {
            SelectAddress = mBundle.getBoolean("SelAddress");
            mStringAddressId = mBundle.getString("SelcteAddresssId");
            mArrayListGetImage = (ArrayList<Bitmap>) getArguments().getSerializable("imagearray");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentView = inflater.inflate(R.layout.fragment_order_summary, container, false);


        mImageViewBack = fragmentView.findViewById(R.id.fragment_imageview_back);
        mTextViewTitle = fragmentView.findViewById(R.id.fragment_title_textview);
        mTextViewTitle.setText("Order Summary");
        mImageViewSearch = fragmentView.findViewById(R.id.fragment_Serach);
        mImageViewSearch.setVisibility(View.GONE);
        mImageViewCart = fragmentView.findViewById(R.id.fragment_cart);
        mImageViewCart.setVisibility(View.GONE);

        mTextViewHouseNo = fragmentView.findViewById(R.id.fragment_order_summary_textview_address_houseNo);
        mTextViewstreetName = fragmentView.findViewById(R.id.fragment_order_summary_textview_address_one);
        mTextViewPincode = fragmentView.findViewById(R.id.fragment_order_summary_textview_address_pincode);
        mTextViewChangeAddress = fragmentView.findViewById(R.id.fragment_order_summary_textview_add_address_chnage);
        mImageViewEditAddress = fragmentView.findViewById(R.id.fragment_order_summary_address_imageview_edit);
        mRecyclerViewPrescrption = fragmentView.findViewById(R.id.fragment_order_summary_recyclerview);
        mImageViewarrow = fragmentView.findViewById(R.id.fragment_order_summary_imageview_prescription_attech_dwon_arrow);
        mTextViewPrescrptionView = fragmentView.findViewById(R.id.fragment_order_summary_textview_prescription_attech);
        mImageViewUPArrow = fragmentView.findViewById(R.id.fragment_order_summary_imageview_prescription_attech_up_arrow);
        mTextViewSubmit = fragmentView.findViewById(R.id.fragment_order_summary_textview_submit);

        mRelativeLayout = fragmentView.findViewById(R.id.fragment_order_summary_relative);
        mTextViewAddAddress = fragmentView.findViewById(R.id.fragment_order_summary_textview_add_address);

        mSharedPreferencesSelectedAddress = mainActivity.getSharedPreferences(Tags.TAG_SHARED_PREFERENCE_SELECTED_ADDRESS, 0);
        mIntRawId = mSharedPreferencesSelectedAddress.getInt(Tags.USER_FIRST_NAME, 0);
        mSharedPreferencesUserId = mainActivity.getSharedPreferences(Tags.TAG_SHARED_PREFERENCE_USER_ID, 0);
        mStringUserId = mSharedPreferencesUserId.getString("UserId", "");
        mEditor = mSharedPreferencesUserId.edit();



        mTextViewSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    OrderConfrimWithPrescrption();
            }
        });

        mImageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.onBackPressed();
            }
        });

        mTextViewChangeAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment currentFrag = mainActivity.getSupportFragmentManager().findFragmentById(R.id.activity_main_container);
                mCurrentFragment = currentFrag.getClass().getName();

                if (!mCurrentFragment.equalsIgnoreCase(Tags.TAG_FRAGEMENT_SELECT_ADDRESS)) {
//                    mainActivity.removeAllBackFragments();
                    FragmentSelectAddress mFragmentSelectAddress = new FragmentSelectAddress();
                    Bundle mBundle = new Bundle();
                    mBundle.putSerializable("imagearray", mArrayListGetImage);
                    mBundle.putBoolean("IsFromOrderPre", true);
                    mFragmentSelectAddress.setArguments(mBundle);
                    mainActivity.addFragment(mFragmentSelectAddress, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, mFragmentSelectAddress.getClass().getName());
                }
            }
        });

        mImageViewEditAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment currentFrag = mainActivity.getSupportFragmentManager().findFragmentById(R.id.activity_main_container);
                mCurrentFragment = currentFrag.getClass().getName();

                if (!mCurrentFragment.equalsIgnoreCase(Tags.TAG_FRAGEMENT_ADD_ADDRESS)) {

                    Utility.debugger("Vt Defualt Adress id...." + AddressID);
//                    mainActivity.removeAllBackFragments();
                    FragmentAddAddress mFragmentListData = new FragmentAddAddress();
                    Bundle args = new Bundle();
                    args.putBoolean(Tags.isFromEdit, false);
                    args.putBoolean("fromorder", false);
                    args.putSerializable("imagearray", mArrayListGetImage);
                    args.putString("AddressId", AddressID);
                    args.putBoolean("IsFromOrderpres", true);
                    mFragmentListData.setArguments(args);
                    mainActivity.addFragment(mFragmentListData, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, mFragmentListData.getClass().getName());
                }
            }
        });

        mImageViewarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mRecyclerViewPrescrption.setVisibility(View.VISIBLE);
                mImageViewUPArrow.setVisibility(View.VISIBLE);
                mImageViewarrow.setVisibility(View.GONE);
            }
        });

        mImageViewUPArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRecyclerViewPrescrption.setVisibility(View.GONE);
                mImageViewarrow.setVisibility(View.VISIBLE);
                mImageViewUPArrow.setVisibility(View.GONE);

            }
        });
        if (mArrayListGetImage.size() == 0) {
            mRecyclerViewPrescrption.setVisibility(View.GONE);
        } else {
            mTextViewPrescrptionView.setText(getResources().getString(R.string.view_prescription) + " " + "(" + mArrayListGetImage.size() + ")");
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 3);
            mRecyclerViewPrescrption.setLayoutManager(gridLayoutManager);
            PrescriptionGetImageAdapter prescriptionGetImageAdapter = new PrescriptionGetImageAdapter(mainActivity, mArrayListGetImage);
            mRecyclerViewPrescrption.setAdapter(prescriptionGetImageAdapter);
        }

        mTextViewAddAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentAddAddress mFragmentListData = new FragmentAddAddress();
                Bundle args = new Bundle();
                args.putBoolean(Tags.isFromEdit, false);
                args.putBoolean("IsPresciption", true);
                args.putSerializable("imagearray",mArrayListGetImage);
                mFragmentListData.setArguments(args);
                mainActivity.addFragment(mFragmentListData, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, mFragmentListData.getClass().getName());

            }
        });

        return fragmentView;
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
                        try {
                            if (mServerResponseVOSetAddress.getStatus().equalsIgnoreCase("true")) {
//                            Snackbar.with(mainActivity).text(mServerResponseVO.getMsg()).show(mainActivity);
                                mCommonHelper.hideKeyboard(mainActivity);
                                mRelativeLayout.setVisibility(View.VISIBLE);
                                mTextViewSubmit.setVisibility(View.VISIBLE);
                                if (mServerResponseVOSetAddress.getData() != null) {
                                    mUserDetailsVOArrayList = (ArrayList<UserDetailsVO>) mServerResponseVOSetAddress.getData();

                                    if (mUserDetailsVOArrayList != null) {
                                        for (int i = 0; i < mUserDetailsVOArrayList.size(); i++) {
                                            mTextViewHouseNo.setText(mUserDetailsVOArrayList.get(i).getName());
                                            mTextViewstreetName.setText(mUserDetailsVOArrayList.get(i).getAddress());
                                            mTextViewPincode.setText(mUserDetailsVOArrayList.get(i).getCityName() + "," + mUserDetailsVOArrayList.get(i).getPincode() + "   " + mUserDetailsVOArrayList.get(i).getStateName());

                                            AddressID = mUserDetailsVOArrayList.get(i).getAddressId();


                                        }
                                        String address = mTextViewHouseNo.getText().toString() + mTextViewstreetName.getText().toString() +mTextViewPincode.getText().toString() ;
                                        GeocodingLocation locationAddress = new GeocodingLocation();
                                        locationAddress.getAddressFromLocation(address,
                                                getApplicationContext(), new GeocoderHandler());

                                    }
                                }
                            } else {
                                Snackbar.with(mainActivity).text(mServerResponseVOSetAddress.getMsg()).show(mainActivity);
                                mCommonHelper.hideKeyboard(mainActivity);
                            }
                        } catch (Exception e) {

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
            mServerResponseVOSetAddress = mEditAddressInfoService.fetcheditaddress(mainActivity, Tags.GetAddressData, mStringAddressId);
            return null;
        }
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

                            if (mServerResponseVO.getData() != null) {
                                mUserDetailsVOArrayList = (ArrayList<UserDetailsVO>) mServerResponseVO.getData();
                                mRelativeLayout.setVisibility(View.VISIBLE);
                                mTextViewSubmit.setVisibility(View.VISIBLE);
                                if (mUserDetailsVOArrayList != null) {
                                    for (int i = 0; i < mUserDetailsVOArrayList.size(); i++) {
                                        mTextViewHouseNo.setText(mUserDetailsVOArrayList.get(i).getName());
                                        mTextViewstreetName.setText(mUserDetailsVOArrayList.get(i).getAddress());
                                        mTextViewPincode.setText(mUserDetailsVOArrayList.get(i).getCityName() + "," + mUserDetailsVOArrayList.get(i).getPincode() + "   " + mUserDetailsVOArrayList.get(i).getStateName());

                                        AddressID = mUserDetailsVOArrayList.get(i).getAddressId();

                                    }

                                    String address = mTextViewHouseNo.getText().toString()+mTextViewstreetName.getText().toString() +mTextViewPincode.getText().toString() ;
                                    GeocodingLocation locationAddress = new GeocodingLocation();
                                    locationAddress.getAddressFromLocation(address,
                                            getApplicationContext(), new GeocoderHandler());
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


    public class PrescriptionGetImageAdapter extends RecyclerView.Adapter<PrescriptionGetImageAdapter.MyViewHolder> {

        Context mContext;
        ArrayList<Bitmap> mArrayList;

        public PrescriptionGetImageAdapter(MainActivity mainActivity, ArrayList<Bitmap> mArrayListGetImage) {

            this.mContext = mainActivity;
            this.mArrayList = mArrayListGetImage;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_upload_prescription, parent, false);
            MyViewHolder vh = new MyViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

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

        public class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView mImageViewSelectIamge;
            ImageView mImageViewDelete;

            public MyViewHolder(View itemView) {
                super(itemView);
                mImageViewSelectIamge = (ImageView) itemView.findViewById(R.id.row_upload_prescription_imageview);
                mImageViewDelete = (ImageView) itemView.findViewById(R.id.row_upload_prescription_imageview_delete);
            }
        }
    }

    public void onResumeData() {

        try {
            DBService dbService = new DBService();
            mArrayListUserDetailVO = dbService.getSavedSingleAddressData(mainActivity, mIntRawId);

            if (mArrayListUserDetailVO != null && mArrayListUserDetailVO.size() > 0) {

                mUserDetailsVO.setUserId(mArrayListUserDetailVO.get(0).getUserId());
                mUserDetailsVO.setAddressId(mArrayListUserDetailVO.get(0).getAddressId());
                mUserDetailsVO.setAddress(mArrayListUserDetailVO.get(0).getAddress());
                mUserDetailsVO.setName(mArrayListUserDetailVO.get(0).getName());
                mUserDetailsVO.setContactNo(mArrayListUserDetailVO.get(0).getContactNo());
                mUserDetailsVO.setPincode(mArrayListUserDetailVO.get(0).getPincode());
                mUserDetailsVO.setContactNo(mArrayListUserDetailVO.get(0).getContactNo());
                mUserDetailsVO.setAddressType(mArrayListUserDetailVO.get(0).getAddressType());

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        mainActivity.toolbar.setVisibility(View.GONE);
        CheckUserAddress();
    }

    //  Confrime Order With Prescription

    public void OrderConfrimWithPrescrption() {

        getLoaderManager().restartLoader(0, null, new LoaderManager.LoaderCallbacks<TaskExecutor>() {
            @Override
            public Loader<TaskExecutor> onCreateLoader(int arg0, Bundle arg1) {
                OrderWithPresciprion orderWithPresciprion = new OrderWithPresciprion(mainActivity, null);
                return new LoaderTask<TaskExecutor>(mainActivity, orderWithPresciprion);
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
                        if (mServerResponseVO != null && mServerResponseVO.getStatus() != null) {

                            if (mServerResponseVO.getStatus().equalsIgnoreCase("true")) {

                                if (mServerResponseVO.getData() != null) {
//                                    Snackbar.with(mainActivity).text(mServerResponseVO.getMsg()).show(mainActivity);

                                    @SuppressLint("HandlerLeak") Handler handler = new Handler() {
                                        @Override
                                        public void handleMessage(Message msg) {
                                            if (msg.what == Tags.WHAT) {
                                                FragmentConfirmOrder mConfirmOrder = new FragmentConfirmOrder();
                                                Bundle mBundle = new Bundle();
                                                mBundle.putBoolean("ISFromCart", false);
                                                mConfirmOrder.setArguments(mBundle);
                                                mainActivity.addFragment(mConfirmOrder, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, mConfirmOrder.getClass().getName());
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
            }

            @Override
            public void onLoaderReset(Loader<TaskExecutor> arg0) {
            }
        });
    }


    public class OrderWithPresciprion extends TaskExecutor {

        protected OrderWithPresciprion(Context context, Bundle bundle) {
            super(context, bundle);
        }

        @Override
        protected Object executeTask() {

            OrderWithPrescriptionService orderWithPrescriptionService = new OrderWithPrescriptionService();

            try {
                mServerResponseVO = orderWithPrescriptionService.OrderWithPres(mainActivity, Tags.ORDERWITHPRIESCIRPTION, mStringUserId, AddressID, mImageVOArrayList ,mStringLatitude,mStringLongtitude);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
    }


    public static JSONObject createCustomOptionJson() {

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

                    jsonObject.put("VastnessTechnology" + i, mStringImageBase64);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
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
                            mTextViewChangeAddress.setVisibility(View.VISIBLE);
                            mTextViewAddAddress.setVisibility(View.GONE);
                            mTextViewSubmit.setVisibility(View.VISIBLE);
                            if(SelectAddress == true)
                            {
                                SelctedAddress();
                            }
                            else {
                                GetDefaultAddress();
                            }

                        } else {
//                            Snackbar.with(mainActivity).text(mServerResponseVO.getMsg()).show(mainActivity);
                            mCommonHelper.hideKeyboard(mainActivity);
                            mRelativeLayout.setVisibility(View.GONE);
                            mTextViewChangeAddress.setVisibility(View.GONE);
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
                            mStringLatitude = String.valueOf(address.getLatitude());
                            mStringLongtitude = String.valueOf(address.getLongitude());

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