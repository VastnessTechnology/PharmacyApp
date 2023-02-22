package com.vgroyalchemist.views;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.vgroyalchemist.CommonClass.CommonClass;
import com.vgroyalchemist.R;
import com.vgroyalchemist.controller.MainActivity;
import com.vgroyalchemist.loaders.LoaderTask;
import com.vgroyalchemist.loaders.TaskExecutor;
import com.vgroyalchemist.utils.CommonHelper;
import com.vgroyalchemist.utils.Snackbar;
import com.vgroyalchemist.utils.SweetAlertDialogSingleButton;
import com.vgroyalchemist.utils.Tags;
import com.vgroyalchemist.utils.Utility;
import com.vgroyalchemist.vos.AllCartData;
import com.vgroyalchemist.vos.ServerResponseVO;
import com.vgroyalchemist.vos.TaxChargeVO;
import com.vgroyalchemist.vos.TotalArrayVo;
import com.vgroyalchemist.vos.UserDetailsVO;
import com.vgroyalchemist.webservice.AddOrderReturnService;
import com.vgroyalchemist.webservice.CheckPrescReqService;
import com.vgroyalchemist.webservice.DeleteAddressInfoService;
import com.vgroyalchemist.webservice.DeleteCartService;
import com.vgroyalchemist.webservice.GetAllCartService;
import com.vgroyalchemist.webservice.PostParseGet;
import com.vgroyalchemist.webservice.UpdateCartService;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.facebook.FacebookSdk.getApplicationContext;

/* developed by krishna 24-03-2021
*  display cart item and upload Prescirption required medicine */
public class FragmentCart extends ParentFragment {

    public static final String FRAGMENT_ID = "11";


    public static MainActivity mainActivity;
    CommonHelper mCommonHelper;
    View fragmnetview = null;
    ImageView mImageViewCart;
    ImageView mImageViewBack;
    TextView mTextViewTitle;
    ImageView mImageViewSearch;
    LinearLayout mLinearLayoutEmpty;
    TextView mTextViewContinueShopping;

    RecyclerView mRecyclerViewCartDta;

//    Total
    TextView mTextViewSubtotal;
    TextView mTextViewDiscount;
    TextView mTextViewShippingCharge;
    RelativeLayout mRelativeLayoutShippingCharge;
    TextView mTextViewGrossTotal;


    ArrayList<AllCartData> mArrayListCartData;
    ArrayList<TotalArrayVo> mTotalArrayVos;
    TotalArrayVo totalArrayVo = new TotalArrayVo();
    TextView mTextViewSubmit;

    // userdata
    public SharedPreferences mSharedPreferencesUserId;
    public SharedPreferences.Editor mEditor;

    //    address sharepreferance
    public SharedPreferences mSharedPreferencesAddress;
    SharedPreferences.Editor mEditorAddress;


    public static final int REQUEST_EXTERNAL_STORAGE = 1;
    public static String[] PERMISSIONS_STORAGE = {Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
    private static final int SELECT_PICTURE = 732;

    String mStringUserid;
    ServerResponseVO mServerResponseVO;
    ServerResponseVO mServerResponseVOUpdate;
    ServerResponseVO mServerResponseVODelete;
    ServerResponseVO mServerResponseVOUploadPre;
    PostParseGet mPostParseGet;
    CartAdapter cartAdapter;

    int mIntQty, minimumQty, mIntincrQty ,mIntTotalQtyIncrmnt;
    TaxChargeVO mTaxChargeVO ;

    TextView mTextViewUpload;
    RecyclerView mRecyclerViewImage;
    RelativeLayout mRelativeLayoutMain;

    static Bitmap mBitmap;
    ArrayList<Uri> mArrayUri;
    List<String> imagesEncodedList;
    Uri mImageUri;
    String imageEncoded;
    static ArrayList<Bitmap> mArrayListImage;

    ArrayList mImageVOArrayList;

    static String mStringImageBase64;

    //login
    public SharedPreferences mSharedPreferencesLoginFirst;
    SharedPreferences.Editor EditorLogin;
    String GuestUser;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainActivity = (MainActivity) getActivity();
        mCommonHelper = new CommonHelper();
        mPostParseGet = new PostParseGet(mainActivity);
        mArrayListCartData = new ArrayList<AllCartData>();
        mTaxChargeVO= new TaxChargeVO();
        mTotalArrayVos = new ArrayList<TotalArrayVo>();
        mArrayListImage = new ArrayList<Bitmap>();
        mArrayUri = new ArrayList<Uri>();

        mImageVOArrayList= new ArrayList();
        verifyStoragePermissions(mainActivity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmnetview = inflater.inflate(R.layout.fragment_cart, container, false);

        mImageViewBack = fragmnetview.findViewById(R.id.fragment_imageview_back);
        mTextViewTitle = fragmnetview.findViewById(R.id.fragment_title_textview);
        mTextViewTitle.setText("Cart");
        mImageViewSearch = fragmnetview.findViewById(R.id.fragment_Serach);
        mImageViewSearch.setVisibility(View.VISIBLE);
        mImageViewCart = fragmnetview.findViewById(R.id.fragment_cart);
        mImageViewCart.setVisibility(View.GONE);

        mTextViewSubmit = fragmnetview.findViewById(R.id.fragment_cart_textview_submit);
        mLinearLayoutEmpty = fragmnetview.findViewById(R.id.fragment_Cart_relative_empty);
        mTextViewContinueShopping = fragmnetview.findViewById(R.id.fragment_Cart_textview_cart_shopping);

        mRecyclerViewCartDta = fragmnetview.findViewById(R.id.fragment_Cart_Recyclerview);
        mTextViewUpload =fragmnetview.findViewById(R.id.fragment_Cart_textview_upload);
        mRecyclerViewImage = fragmnetview.findViewById(R.id.fragment_Cart_recycleview_upload_pres);
        mRelativeLayoutMain =fragmnetview.findViewById(R.id.fragment_Cart_relative_main);

        mTextViewSubtotal =fragmnetview.findViewById(R.id.row_review_textview_item_subtotal_total);
        mTextViewDiscount = fragmnetview.findViewById(R.id.row_review_textview_item_subtotal_Discout);
        mTextViewShippingCharge =fragmnetview.findViewById(R.id.row_review_textview_item_shipping_charge);
        mRelativeLayoutShippingCharge =fragmnetview.findViewById(R.id.row_review_relative_shipping_charge);
        mRelativeLayoutShippingCharge.setVisibility(View.GONE);
        mTextViewGrossTotal =fragmnetview.findViewById(R.id.row_review_textview_item_gross_total);

        mSharedPreferencesUserId = mainActivity.getSharedPreferences(Tags.TAG_SHARED_PREFERENCE_USER_ID, 0);
        mEditor = mSharedPreferencesUserId.edit();
        mStringUserid = mSharedPreferencesUserId.getString("UserId", "");

        mSharedPreferencesLoginFirst = mainActivity.getSharedPreferences(Tags.PREFS_NAME, 0);
        EditorLogin = mSharedPreferencesLoginFirst.edit();
        GuestUser = mSharedPreferencesLoginFirst.getString("GuestUser", "0");

        CommonClass cmn = new CommonClass();
        cmn.FirebaseToken(getActivity());

        if(GuestUser.equalsIgnoreCase("1"))
        {
            mStringUserid = "00000000-0000-0000-0000-000000000000";
        }

        mSharedPreferencesAddress = mainActivity.getSharedPreferences(Tags.TAG_SHARED_PREFRENCE_Address, 0);
        mEditorAddress = mSharedPreferencesAddress.edit();
//        AddAddress = mSharedPreferencesAddress.getBoolean("AddAddress",false);

        mImageViewSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.RemoveAllFragment();
                FragmentTabActivity fragmentTabActivity = new FragmentTabActivity();
                mainActivity.addFragment(fragmentTabActivity, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, fragmentTabActivity.getClass().getName());

//                mainActivity.removeAllBackFragments();
//                FragmentHomeNewTheme fragmentHomeNewTheme = new FragmentHomeNewTheme();
//                Bundle bundle = new Bundle();
//                bundle.putBoolean(getString(R.string.app_name), false);
//                fragmentHomeNewTheme.setArguments(bundle);
//                mainActivity.addFragment(fragmentHomeNewTheme, false, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, fragmentHomeNewTheme.getClass().getName());
            }
        });
       mImageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mainActivity.onBackPressed();
//                mainActivity.RemoveAllFragment();
//                FragmentTabActivity fragmentTabActivity = new FragmentTabActivity();
//                mainActivity.addFragment(fragmentTabActivity, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, fragmentTabActivity.getClass().getName());
            }
        });

        mTextViewSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                if(AddAddress == true)
//                {
//                    FragmentSearchOrderSummary searchOrderSummary = new FragmentSearchOrderSummary();
//                    mainActivity.addFragment(searchOrderSummary, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, searchOrderSummary.getClass().getName());
//                }
//                else {
//                    FragmentAddAddress mFragmentListData = new FragmentAddAddress();
//                    Bundle args = new Bundle();
//                    args.putBoolean(Tags.isFromEdit, false);
//                    args.putBoolean("fromorder",false);
//                    args.putBoolean("IsCart",true);
//                    mFragmentListData.setArguments(args);
//                    mainActivity.addFragment(mFragmentListData, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, mFragmentListData.getClass().getName());
//                }

                if(GuestUser.equalsIgnoreCase("1"))
                {
                    FragmentLoginScreen fragmentLoginScreen = new FragmentLoginScreen();
                    Bundle mBundle   = new Bundle();
                    mBundle.putBoolean(Tags.IS_FROM_LOGIN,true);
                    fragmentLoginScreen.setArguments(mBundle);
                    mainActivity.addFragment(fragmentLoginScreen, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, fragmentLoginScreen.getClass().getName());
                }
                else {
                    UploadAllPresciption();
                }

            }
        });

        mTextViewContinueShopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    mainActivity.RemoveAllFragment();
                    FragmentTabActivity fragmentTabActivity = new FragmentTabActivity();
                    mainActivity.addFragment(fragmentTabActivity, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, fragmentTabActivity.getClass().getName());




//                mainActivity.removeAllBackFragments();
//                FragmentHomeNewTheme fragmentHomeNewTheme = new FragmentHomeNewTheme();
//                Bundle bundle = new Bundle();
//                bundle.putBoolean(getString(R.string.app_name), false);
//                fragmentHomeNewTheme.setArguments(bundle);
//                mainActivity.addFragment(fragmentHomeNewTheme, false, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, fragmentHomeNewTheme.getClass().getName());
            }
        });

        mTextViewUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ActivityCompat.checkSelfPermission(mainActivity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    verifyStoragePermissions(mainActivity);
                    return;
                }

                if (ActivityCompat.checkSelfPermission(mainActivity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    Snackbar.with(mainActivity).text(getResources().getString(R.string.enable_permission)).show(mainActivity);
                    return;
                }

                selectImage();
            }
        });



        return fragmnetview;
    }


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

                                    mArrayListCartData = (ArrayList<AllCartData>) mTaxChargeVO.getmAllCartData();

                                    if (mArrayListCartData != null && mArrayListCartData.size() > 0) {
                                        mLinearLayoutEmpty.setVisibility(View.GONE);
                                        mTextViewSubmit.setVisibility(View.VISIBLE);
                                        mRelativeLayoutMain.setVisibility(View.VISIBLE);

                                        cartAdapter = new CartAdapter(mainActivity, mArrayListCartData);
                                        mRecyclerViewCartDta.setHasFixedSize(true);
                                        mRecyclerViewCartDta.setLayoutManager(new LinearLayoutManager(mainActivity));
                                        mRecyclerViewCartDta.setAdapter(cartAdapter);
                                    } else {
                                        mLinearLayoutEmpty.setVisibility(View.VISIBLE);
                                        mTextViewSubmit.setVisibility(View.GONE);
                                        mRelativeLayoutMain.setVisibility(View.GONE);

                                    }

                                    totalArrayVo = mTaxChargeVO.getmTotalArrayVo();

                                    if(totalArrayVo != null)
                                    {
                                        if(!totalArrayVo.getSubTotalRs().equalsIgnoreCase("null"))
                                        {
                                            mTextViewSubtotal.setText(Tags.LABEL_CURRENCY_SIGN + Utility.getDecimalFormate(mainActivity,Double.parseDouble(totalArrayVo.getSubTotalRs()),0.0,Tags.DECIMAL_FORMAT));
                                        }
                                        if(!totalArrayVo.getDiscRs().equalsIgnoreCase("null"))
                                        {
                                            mTextViewDiscount.setText("- " + Tags.LABEL_CURRENCY_SIGN  + Utility.getDecimalFormate(mainActivity,Double.parseDouble(totalArrayVo.getDiscRs()),0.0,Tags.DECIMAL_FORMAT));
                                        }
                                        if(!totalArrayVo.getTotallRs().equalsIgnoreCase("null"))
                                        {
                                            mTextViewGrossTotal.setText(Tags.LABEL_CURRENCY_SIGN + Utility.getDecimalFormate(mainActivity,Double.parseDouble(totalArrayVo.getTotallRs()),0.0,Tags.DECIMAL_FORMAT));
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
                mServerResponseVO = mGetAllCartService.getAllCart(mainActivity, Tags.GETALLCARTDATA, mStringUserid ,"" ,"",Tags.mStringDeviceToken);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyViewHolderCart> {
        Context mContext;
        ArrayList<AllCartData> mArrayListData;

        public CartAdapter(MainActivity mainActivity, ArrayList<AllCartData> mArrayListCartData) {
            this.mContext = mainActivity;
            this.mArrayListData = mArrayListCartData;
        }

        @NonNull
        @Override
        public MyViewHolderCart onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_cart_product, parent, false);
            MyViewHolderCart holderCart = new MyViewHolderCart(v);
            return holderCart;
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull MyViewHolderCart holder, @SuppressLint("RecyclerView") int position) {

            if(!mArrayListData.get(position).getPrice().equalsIgnoreCase("null") && mArrayListData.get(position).getPrice() != null)
            {
                holder.mTextViewPrice.setText(Tags.LABEL_CURRENCY_SIGN + Utility.getDecimalFormate(mainActivity,Double.parseDouble( mArrayListData.get(position).getPrice()),0.0,Tags.DECIMAL_FORMAT));
            }

            if(!mArrayListData.get(position).getAmount().equalsIgnoreCase("null") && mArrayListData.get(position).getAmount() != null)
            {
                holder.mTextViewTotal.setText(Tags.LABEL_CURRENCY_SIGN + Utility.getDecimalFormate(mainActivity,Double.parseDouble( mArrayListData.get(position).getAmount()),0.0,Tags.DECIMAL_FORMAT));
            }

            if(!mArrayListData.get(position).getQty().equalsIgnoreCase("null") && mArrayListData.get(position).getQty() != null)
            {
                holder.mTextViewQtyValue.setText(mArrayListData.get(position).getQty());
            }

            if(!mArrayListData.get(position).getMedicineName().equalsIgnoreCase("null"))
            {
                holder.mTextViewMedicineName.setText(mArrayListData.get(position).getMedicineName());
            }

            if(!mArrayListData.get(position).getContent().equalsIgnoreCase("null"))
            {
                holder.mTextViewContext.setText(mArrayListData.get(position).getContent());
            }

            if (mArrayListData.get(position).getMedicineImgPath() != null) {

                Glide.with(mContext).load(mArrayListData.get(position).getMedicineImgPath())
                        .placeholder(R.drawable.ic_med)
                        .into(holder.mImageViewMedicineimage);
            } else {
                Glide.with(mContext)
                        .load(R.drawable.ic_med)
                        .into(holder.mImageViewMedicineimage);
            }


            try {
                minimumQty = Integer.parseInt(mArrayListData.get(position).getQty());
            } catch (Exception e) {
                minimumQty = 1;
                e.printStackTrace();
            }


            holder.mTextViewQtyPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mIntTotalQtyIncrmnt = 30;
                    mIntQty =Integer.parseInt(holder.mTextViewQtyValue.getText().toString());

                    if (mIntQty < mIntTotalQtyIncrmnt) {

                        if (!mCommonHelper.check_Internet(getActivity())) {
                            Snackbar.with(mainActivity).text(getResources().getString(R.string.interneterror)).show(mainActivity);
                            return;
                        }

                        mIntQty = Integer.parseInt(holder.mTextViewQtyValue.getText().toString()) + 1;

                        holder.mTextViewQtyValue.setText(String.valueOf(mIntQty));

                        final Bundle mBundle = new Bundle();
                        mBundle.putString(getString(R.string.app_name), mArrayListData.get(position).getCartId());
                        mBundle.putString("medicineid", mArrayListData.get(position).getMedicineId());
                        mBundle.putString("qtyplus", String.valueOf(mIntQty));
                        mBundle.putString("priceplus", mArrayListData.get(position).getPrice());

                        getLoaderManager().restartLoader(0, null, new LoaderManager.LoaderCallbacks<TaskExecutor>() {
                            @Override
                            public Loader<TaskExecutor> onCreateLoader(int arg0, Bundle arg1) {
                                UpdateCartQty updateCartQty = new UpdateCartQty(mainActivity, mBundle);
                                return new LoaderTask<TaskExecutor>(mainActivity, updateCartQty);
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
                                        if (mServerResponseVOUpdate != null && mServerResponseVOUpdate.getStatus() != null) {

                                            try {
                                                if (mServerResponseVOUpdate.getStatus().equalsIgnoreCase("true")) {

                                                    if (mServerResponseVOUpdate.getData() != null) {

                                                        getCartData();

                                                    }
                                                } else {
                                                    Snackbar.with(mainActivity).text(mServerResponseVOUpdate.getMsg()).show(mainActivity);
                                                    mCommonHelper.hideKeyboard(mainActivity);
                                                }

                                            } catch (Exception e) {
                                                e.printStackTrace();
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
                    else {
                        QtyAlert();
                    }

                }
            });

            holder.mTextViewQtyminus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mIntincrQty = 1;
                    mIntQty =Integer.parseInt(holder.mTextViewQtyValue.getText().toString());

                    if (mIntQty <= mIntincrQty) {

                        holder.mTextViewQtyminus.setClickable(false);
                    }
                    else {
                        mIntQty = Integer.parseInt(holder.mTextViewQtyValue.getText().toString()) - 1;

                        holder.mTextViewQtyValue.setText(String.valueOf(mIntQty));

                        final Bundle mBundle = new Bundle();
                        mBundle.putString(getString(R.string.app_name), mArrayListData.get(position).getCartId());
                        mBundle.putString("medicineid", mArrayListData.get(position).getMedicineId());
                        mBundle.putString("qtyplus", String.valueOf(mIntQty));
                        mBundle.putString("priceplus", mArrayListData.get(position).getPrice());

                        getLoaderManager().restartLoader(0, null, new LoaderManager.LoaderCallbacks<TaskExecutor>() {
                            @Override
                            public Loader<TaskExecutor> onCreateLoader(int arg0, Bundle arg1) {
                                UpdateCartQty updateCartQty = new UpdateCartQty(mainActivity, mBundle);
                                return new LoaderTask<TaskExecutor>(mainActivity, updateCartQty);
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
                                        if (mServerResponseVOUpdate != null && mServerResponseVOUpdate.getStatus() != null) {

                                            try {
                                                if (mServerResponseVOUpdate.getStatus().equalsIgnoreCase("true")) {

                                                    if (mServerResponseVOUpdate.getData() != null) {

                                                        getCartData();
                                                    }
                                                } else {
                                                    Snackbar.with(mainActivity).text(mServerResponseVOUpdate.getMsg()).show(mainActivity);
                                                    mCommonHelper.hideKeyboard(mainActivity);
                                                }

                                            } catch (Exception e) {
                                                e.printStackTrace();
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


                }

            });
            holder.mLinearLayoutRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!mCommonHelper.check_Internet(getActivity())) {
                        Snackbar.with(mainActivity).text(getResources().getString(R.string.interneterror)).show(mainActivity);
                        return;
                    }

                    final Bundle mBundle = new Bundle();
                    mBundle.putString(getString(R.string.app_name), mArrayListData.get(position).getCartId());


                    getLoaderManager().restartLoader(0, mBundle, new LoaderManager.LoaderCallbacks<TaskExecutor>() {
                        @Override
                        public Loader<TaskExecutor> onCreateLoader(int arg0, Bundle arg1) {

                            DeleteCartExecutor deleteCartExecutor = new DeleteCartExecutor(mainActivity, mBundle);
                            return new LoaderTask<TaskExecutor>(mainActivity, deleteCartExecutor);
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
                                    if (mServerResponseVODelete != null && mServerResponseVODelete.getStatus() != null) {

                                        if (mServerResponseVODelete.getStatus().equalsIgnoreCase("true")) {

                                            mArrayListData.remove(position);
                                            cartAdapter.notifyDataSetChanged();
                                            getCartData();

                                            Snackbar.with(mainActivity).text(mServerResponseVODelete.getMsg()).show(mainActivity);
                                        } else {
                                            Snackbar.with(mainActivity).text(mServerResponseVODelete.getMsg()).show(mainActivity);
                                        }
                                    } else {
                                        Snackbar.with(mainActivity).text(mServerResponseVODelete.getMsg()).show(mainActivity);
                                    }
                                }
                            }
                        }

                        @Override
                        public void onLoaderReset(Loader<TaskExecutor> arg0) {

                        }
                    });

                }
            });
        }


        @Override
        public int getItemCount() {
            return mArrayListData.size();
        }



        public class MyViewHolderCart extends RecyclerView.ViewHolder {

            ImageView mImageViewMedicineimage;
            TextView mTextViewMedicineName;
            TextView mTextViewTotal;
            TextView mTextViewQtyminus;
            TextView mTextViewQtyPlus;
            EditText mTextViewQtyValue;
            LinearLayout mLinearLayoutRemove;
            TextView mTextViewContext;
            TextView mTextViewPrice;


            public MyViewHolderCart(View itemView) {
                super(itemView);

                mImageViewMedicineimage = itemView.findViewById(R.id.row_cart_imageview_main);
                mTextViewMedicineName = itemView.findViewById(R.id.row_cart_textview__medicine_name);
                mTextViewTotal = itemView.findViewById(R.id.row_cart_textview_item_total);
                mTextViewQtyminus = itemView.findViewById(R.id.row_cart_textview_qty_minus);
                mTextViewQtyPlus = itemView.findViewById(R.id.row_cart_textview_qty_plus);
                mTextViewQtyValue = itemView.findViewById(R.id.row_cart_textview_total_qty);
                mLinearLayoutRemove = itemView.findViewById(R.id.nav_header_medicines_Liner_signout);
                mTextViewContext =itemView.findViewById(R.id.row_cart_textview_item_context);
                mTextViewPrice =itemView.findViewById(R.id.row_cart_textview_item_price);

            }

        }
    }

    public void QtyAlert(){
        AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
        builder.setMessage(getResources().getString(R.string.qtyerror));
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
        return;
    }

//    Delete cart value

    public class DeleteCartExecutor extends TaskExecutor {

        String mStringCartId;

        protected DeleteCartExecutor(Context context, Bundle bundle) {
            super(context, bundle);
            this.mStringCartId = bundle.getString(getString(R.string.app_name));
        }

        @Override
        protected Object executeTask() {

            try {

                DeleteCartService fetchVersionInfoService = new DeleteCartService();
                mServerResponseVODelete = fetchVersionInfoService.deleteCart(mainActivity, Tags.DELETECART,mStringCartId);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

//    update cart qty

    public class UpdateCartQty extends TaskExecutor {

        String mStringCartId;
        String medicineid;
        String qty;
        String price;

        protected UpdateCartQty(Context context, Bundle bundle) {
            super(context, bundle);

            this.mStringCartId = bundle.getString(getString(R.string.app_name));
            this.medicineid =bundle.getString("medicineid");
            this.price =bundle.getString("priceplus");
            this.qty =bundle.getString("qtyplus");
        }

        @Override
        protected Object executeTask() {

            UpdateCartService updateCartService = new UpdateCartService();
            try {
                mServerResponseVOUpdate = updateCartService.UpdateCartData(mainActivity, Tags.UPDATECARTDATA, mStringUserid ,mStringCartId ,medicineid,qty,price ,Tags.mStringDeviceToken);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    public void onResumeData() {
        mainActivity.toolbar.setVisibility(View.GONE);


        mCommonHelper.hideKeyboard(mainActivity);

        getCartData();
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

//     this funtion use ask permission
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have read or write permission
//        int callPermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA);
//
//        if (callPermission != PackageManager.PERMISSION_GRANTED) {
//            // We don't have permission so prompt the user
//            ActivityCompat.requestPermissions(
//                    activity,
//                    PERMISSIONS_STORAGE,
//                    REQUEST_EXTERNAL_STORAGE
//            );
//        }
        int callPermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA);

        if (callPermission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    private void selectImage() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(mainActivity);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, 1);
                } else if (options[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.putExtra("android.intent.extra.ALLOW_MULTIPLE", true);
                    intent.setAction("android.intent.action.GET_CONTENT");
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), 2);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            if (requestCode == 1) {
                onCaptureImageResult(data);
            } else {
                onSelectFromGalleryResult(data);
            }
        }

    }

    private void onCaptureImageResult(Intent data) {

        mBitmap = (Bitmap) data.getExtras().get("data");
        mArrayListImage.add(mBitmap);
        Uri imageUri = getImageUri(getApplicationContext(), mBitmap);
        File file = new File(getRealPathFromURI(imageUri));
        PrintStream printStream = System.out;
        printStream.println("Vt URI ----->" + imageUri);
        PrintStream printStream2 = System.out;
        printStream2.println("Vt file ----->" + file);
        mArrayUri.add(imageUri);
        callRecyclview();

    }


    @SuppressLint("Range")
    private void onSelectFromGalleryResult(Intent data) {
        try {
            String[] strArr = {"_data"};
            imagesEncodedList = new ArrayList();
            if (data.getData() != null) {
                mImageUri = data.getData();
                Cursor query = getActivity().getContentResolver().query(mImageUri, strArr, null, null, null);
                query.moveToFirst();
                imageEncoded = query.getString(query.getColumnIndex(strArr[0]));
                query.close();
//                if(mImageUri.getPath().endsWith(".gif"))
//                {
//                    SweetAlertDialogSingleButton mSweetAlertDialog = new SweetAlertDialogSingleButton(mainActivity)
//                            .setContentText(getResources().getString(R.string.gifalert))
//                            .setConfirmText(getResources().getString(R.string.dialog_ok))
//                            .setConfirmClickListener(new SweetAlertDialogSingleButton.OnSweetClickListener() {
//                                @Override
//                                public void onClick(SweetAlertDialogSingleButton sDialog) {
//                                    sDialog.dismissWithAnimation();
//
//                                }
//                            });
//                    mSweetAlertDialog.setCancelable(false);
//                    mSweetAlertDialog.show();
//                    return;
//                }
//                else {
                    mArrayUri.add(mImageUri);
                    mBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), mImageUri);
                    mArrayListImage.add(mBitmap);
                    callRecyclview();
//                }

            } else if (data.getClipData() != null) {
                ClipData clipData = data.getClipData();
                for (int i3 = 0; i3 < clipData.getItemCount(); i3++) {
                    Uri uri = clipData.getItemAt(i3).getUri();
                    mArrayUri.add(uri);
                    Cursor query2 = getActivity().getContentResolver().query(uri, strArr, null, null, null);
                    query2.moveToFirst();
                    imageEncoded = query2.getString(query2.getColumnIndex(strArr[0]));
                    imagesEncodedList.add(imageEncoded);
                    query2.close();

//                    if(uri.getPath().endsWith(".gif"))
//                    {
//                        SweetAlertDialogSingleButton mSweetAlertDialog = new SweetAlertDialogSingleButton(mainActivity)
//                                .setContentText(getResources().getString(R.string.gifalert))
//                                .setConfirmText(getResources().getString(R.string.dialog_ok))
//                                .setConfirmClickListener(new SweetAlertDialogSingleButton.OnSweetClickListener() {
//                                    @Override
//                                    public void onClick(SweetAlertDialogSingleButton sDialog) {
//                                        sDialog.dismissWithAnimation();
//
//                                    }
//                                });
//                        mSweetAlertDialog.setCancelable(false);
//                        mSweetAlertDialog.show();
//                        return;
//                    }
//                    else {
                        mBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                        mArrayListImage.add(mBitmap);
                        callRecyclview();
//                    }
                }
                Log.v("LOG_TAG", "Selected Images" + mArrayUri.size());
            }


        } catch (Exception unused) {
            Toast.makeText(mainActivity, "Something went wrong", Toast.LENGTH_LONG).show();
        }
    }

    public String getRealPathFromURI(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) return null;
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String s = cursor.getString(column_index);
        cursor.close();
        return s;
    }

    public Uri getImageUri(Context context, Bitmap bitmap2) {
        bitmap2.compress(Bitmap.CompressFormat.JPEG, 70, new ByteArrayOutputStream());
        return Uri.parse(MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap2, "ThisIsImageTitleString",  null));
    }

    private void callRecyclview() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 3);
        mRecyclerViewImage.setLayoutManager(gridLayoutManager);
        UploadPrescAdapter customAdapter = new UploadPrescAdapter(mainActivity, mArrayUri);
        mRecyclerViewImage.setAdapter(customAdapter);
    }

    public class UploadPrescAdapter extends RecyclerView.Adapter<UploadPrescAdapter.MyViewHolderUploadPre> {

        Context mContext;
        ArrayList<Uri> mArrayList;

        public UploadPrescAdapter(MainActivity mainActivity, ArrayList<Uri> mArrayUri) {
            this.mContext = mainActivity;
            this.mArrayList = mArrayUri;
        }

        @NonNull
        @Override
        public MyViewHolderUploadPre onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_upload_prescription, parent, false);
            MyViewHolderUploadPre vh = new MyViewHolderUploadPre(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolderUploadPre holder, @SuppressLint("RecyclerView") int position) {

            holder.mImageViewSelectIamge.setImageURI(mArrayList.get(position));
            holder.mImageViewDelete.setTag(position);
            holder.mImageViewDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mArrayList.remove(position);
                    mArrayListImage.remove(position);
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 4);
                    mRecyclerViewImage.setLayoutManager(gridLayoutManager);
                    UploadPrescAdapter customAdapter = new UploadPrescAdapter(mainActivity, mArrayList);
                    mRecyclerViewImage.setAdapter(customAdapter);

                }
            });
        }

        @Override
        public int getItemCount() {
            return mArrayList.size();
        }

        public class MyViewHolderUploadPre extends RecyclerView.ViewHolder {
            ImageView mImageViewSelectIamge;
            ImageView mImageViewDelete;

            public MyViewHolderUploadPre(View itemView) {
                super(itemView);
                mImageViewSelectIamge = (ImageView) itemView.findViewById(R.id.row_upload_prescription_imageview);
                mImageViewDelete = (ImageView) itemView.findViewById(R.id.row_upload_prescription_imageview_delete);
            }
        }
    }

    //    upload prescription
    public void UploadAllPresciption()
    {

        getLoaderManager().restartLoader(0, null, new LoaderManager.LoaderCallbacks<TaskExecutor>() {
            @Override
            public Loader<TaskExecutor> onCreateLoader(int arg0, Bundle arg1) {
                UploadPresciption addOrderReturn = new UploadPresciption(mainActivity, null);
                return new LoaderTask<TaskExecutor>(mainActivity, addOrderReturn);
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
                        if (mServerResponseVOUploadPre != null && mServerResponseVOUploadPre.getStatus() != null) {

                            if (mServerResponseVOUploadPre.getStatus().equalsIgnoreCase("true")) {

                                if (mServerResponseVOUploadPre.getData() != null) {
//                                    Snackbar.with(mainActivity).text(mServerResponseVOUploadPre.getMsg()).show(mainActivity);

                                    @SuppressLint("HandlerLeak") Handler handler = new Handler() {
                                        @Override
                                        public void handleMessage(Message msg) {
                                            if (msg.what == Tags.WHAT) {
                                                FragmentSearchOrderSummary searchOrderSummary = new FragmentSearchOrderSummary();
                                                Bundle mBundle  = new Bundle();
                                                mBundle.putSerializable("imagearray",mArrayListImage);
                                                searchOrderSummary.setArguments(mBundle);
                                                mainActivity.addFragment(searchOrderSummary, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, searchOrderSummary.getClass().getName());
                                            }
                                        }
                                    };
                                    handler.sendEmptyMessage(Tags.WHAT);
                                }
                            }
                            else {
                                Snackbar.with(mainActivity).text(mServerResponseVOUploadPre.getMsg()).show(mainActivity);
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

    public class UploadPresciption extends TaskExecutor
    {

        protected UploadPresciption(Context context, Bundle bundle) {
            super(context, bundle);
        }

        @Override
        protected Object executeTask() {

            CheckPrescReqService mGetAllCartService = new CheckPrescReqService();

            try {
                mServerResponseVOUploadPre = mGetAllCartService.addorderreturn(mainActivity, Tags.CheckPrescReqInOrder, mStringUserid,mImageVOArrayList);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    public static JSONObject UploadBase64InOreder() {

        JSONObject optionJSON = new JSONObject();

        try {
            try {
                int i = 0;
                while (i < mArrayListImage.size()) {
                    mBitmap = mArrayListImage.get(i);
                    ByteArrayOutputStream bao = new ByteArrayOutputStream();
                    mBitmap.compress(Bitmap.CompressFormat.JPEG, 70, bao);
                    byte[] ba = bao.toByteArray();
                    mStringImageBase64 = Base64.encodeToString(ba, Base64.DEFAULT);
                    i++;

                    optionJSON.put("VastnessTechnology" + i, mStringImageBase64);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return optionJSON;
    }




}