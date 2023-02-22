package com.vgroyalchemist.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.text.HtmlCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.emilsjolander.components.StickyScrollViewItems.StickyScrollView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.vgroyalchemist.CommonClass.CommonClass;
import com.vgroyalchemist.CommonClass.LinkBuilder;
import com.vgroyalchemist.R;
import com.vgroyalchemist.adapter.ProductDetailBannerAdapter;
import com.vgroyalchemist.controller.MainActivity;
import com.vgroyalchemist.db.DBService;
import com.vgroyalchemist.loaders.LoaderTask;
import com.vgroyalchemist.loaders.LoaderTaskWithoutProgressDialog;
import com.vgroyalchemist.loaders.TaskExecutor;
import com.vgroyalchemist.utils.CommonHelper;
import com.vgroyalchemist.utils.CustomViewPager;
import com.vgroyalchemist.utils.Snackbar;
import com.vgroyalchemist.utils.Tags;
import com.vgroyalchemist.utils.Utility;
import com.vgroyalchemist.vos.AltMedicineVO;
import com.vgroyalchemist.vos.MedicineImagevo;
import com.vgroyalchemist.vos.ProductDetailVO;
import com.vgroyalchemist.vos.ServerResponseVO;
import com.vgroyalchemist.webservice.AddCartInfoService;
import com.vgroyalchemist.webservice.PostParseGet;
import com.vgroyalchemist.webservice.UpdateCartService;
import com.vgroyalchemist.webservice.productDeatilsInfoService;

import java.text.DecimalFormat;
import java.util.ArrayList;

import im.delight.android.webview.AdvancedWebView;


/*  developed by krishna 24-03-2021
 *  product Details page */
public class FragmentProductDeatils extends NonCartFragment implements ViewPager.OnPageChangeListener {

    public static final String FRAGMENT_ID = "29";
    public static MainActivity mainActivity;
    CommonHelper mCommonHelper;
    View fragmnetview = null;

    ProgressBar mProgressBar;
    ProgressBar mProgressBarRelatedItem;
    public StickyScrollView mStickyScrollView;
    RelativeLayout mRelativeLayoutTop;
    ImageView mImageViewShare;
    ImageView mImageViewPlaceholder;
    ImageView mImageViewRating;
    public ViewPager mViewPagerTop;
    Spinner mSpinner;
  //  RecyclerView mRecyclerViewBrand;
    TextView mTextViewMedicineName;
    TextView mTextViewSpecialPrice;
    TextView mTextViewMainPrice;
    TextView mTextViewDiscount;
    TextView mTextViewAvgRating;
    TextView mTextViewTotalRating;
    TextView mTextViewTotalReviews;
    TextView mTextViewAddTocart;
    TextView mTextManufacture;
    TextView mTextViewStoteg;
    TextView mTextViewMedicineContent;
    TextView mTextViewGotoCart;
    TextView mTextViewQtyAddToCart;
    TextView mTextViewPreScriptingReq;
    TextView mTextViewOutOfStock;

    public int dotsCount;
    public ImageView[] dots;

    public ProductDetailBannerAdapter mAdapter;
    ArrayList<ProductDetailVO> mProductDetailVOS;

    public LinearLayout pager_indicator;
    public LinearLayout mLinearLayoutRating;
   // public LinearLayout mLinearLayoutRecyclerview;
    public LinearLayout mLinearLayoutProductDescription;
    public LinearLayout mLinearLayoutUse;
    public LinearLayout mLinearLayoutSideEffect;
    public LinearLayout mLinearLayoutAlcohol;
    public LinearLayout mLinearLayoutPregnancy;
    public LinearLayout mLinearLayoutBreastFeeding;
    public LinearLayout mLinearLayoutDriving;
    public LinearLayout mLinearLayoutKidney;
    public LinearLayout mLinearLayoutLiver;
    public LinearLayout mLinearLayoutRelatedItem;
    public LinearLayout mLinearLayoutBottom;
    public LinearLayout mLinearLayoutstorge;
    public LinearLayout mLinearLayoutMedicineContent;
    public LinearLayout mLinearLayoutUseMedicine;
    public LinearLayout mLinearLayoutMedicineWorks;
    public LinearLayout mLinearLayoutQuickTips;
    public LinearLayout mLinearLayoutMissedDosage;

    RelativeLayout mRelativeLayoutRating;

    AdvancedWebView mWebViewDescription;
    AdvancedWebView mWebViewUse;
    AdvancedWebView mWebViewSideEffect;
    AdvancedWebView mWebViewUseMedicine;
    AdvancedWebView mWebViewMedicineWorks;
    AdvancedWebView mWebViewQuickTips;
    AdvancedWebView mWebViewMissedDosage;

    TextView mTextviewAlcohol;
    TextView mTextviewPregnancy;
    TextView mTextviewBreastFeeding;
    TextView mTextviewDriving;
    TextView mTextviewKidney;
    TextView mTextviewLiver;


    Bundle mBundle;
    String mStringmedicineId;

    public PostParseGet mPostParseGet;
    public ServerResponseVO serverResponseVO;
    public ServerResponseVO mServerResponseVOCard;
    public ServerResponseVO mServerResponseVOUpdate;


    ArrayAdapter<String> mAttributeAdapter;
    ArrayList<String> mArrayListSpinner;

    public ProductDetailVO mProductDetailVO;
   // ArrayList<AltMedicineVO> mStringsAlternetbarnd;

    static ArrayList<MedicineImagevo> mArrayListImageArray;
    // userdata
    public SharedPreferences mSharedPreferencesUserId;
    public SharedPreferences.Editor mEditor;

    public SharedPreferences mSharedPreferencesCount;
    public SharedPreferences.Editor mEditorCount;

    //login
    public SharedPreferences mSharedPreferencesLoginFirst;
    SharedPreferences.Editor EditorLogin;

    String mStringUserid;
    String GuestUser;
    String mStringqty;
    String mStringMedicineId;
    String mStringPrice;
    DecimalFormat decimalFormat;

    public DBService mDbService;
    ImageView mImageViewBack;
    TextView mTextViewTitle;
    ImageView mImageViewSearch;
    ImageView mImageViewCart;
    TextView mTextViewBageCart;

    LinearLayout mLinearLayoutQtyAdd;
    TextView mTextViewMinus;
    TextView mTextViewPlus;
    EditText mEditTextQtyCount;
    TextView mTextViewSeeAll;
    TextView mTextViewUnit;
    TextView mTextViewMedicineDescription;


    int mIntQty, minimumQty, mIntincrQty, mIntTotalQtyIncrmnt;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = (MainActivity) getActivity();
        mCommonHelper = new CommonHelper();
        mPostParseGet = new PostParseGet(mainActivity);
        mBundle = getArguments();
        mProductDetailVOS = new ArrayList<ProductDetailVO>();
        mArrayListImageArray = new ArrayList<MedicineImagevo>();
        mDbService = new DBService();
     //   mStringsAlternetbarnd = new ArrayList<AltMedicineVO>();

        decimalFormat = new DecimalFormat(Tags.DECIMAL_FORMAT);
        mArrayListSpinner = new ArrayList<String>();
        for (int i = 1; i <= 30; i++) {
            mArrayListSpinner.add(String.valueOf(i));
        }
        if (mBundle != null) {
            mStringmedicineId = mBundle.getString("medicineid");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmnetview = inflater.inflate(R.layout.fragment_product_deatils, container, false);

        mImageViewBack = fragmnetview.findViewById(R.id.fragment_imageview_back);
        mTextViewTitle = fragmnetview.findViewById(R.id.fragment_title_textview);
        mImageViewSearch = fragmnetview.findViewById(R.id.fragment_Serach);
        mImageViewSearch.setVisibility(View.GONE);
        mImageViewCart = fragmnetview.findViewById(R.id.fragment_cart);
        mTextViewBageCart = fragmnetview.findViewById(R.id.actionbar_title_textview_badge_cart);
        mImageViewCart.setVisibility(View.VISIBLE);
        mTextViewBageCart.setVisibility(View.GONE);

        mProgressBar = fragmnetview.findViewById(R.id.fragment_product_details_progressbar);
        mStickyScrollView = fragmnetview.findViewById(R.id.fragment_product_details_new_design_new_scrollview);
        mRelativeLayoutTop = fragmnetview.findViewById(R.id.fragment_product_details_new_design_relative_data);
        mImageViewPlaceholder = fragmnetview.findViewById(R.id.fragment_product_details_new_design_imageview_placeholder);
        mViewPagerTop = (CustomViewPager) fragmnetview.findViewById(R.id.fragment_product_details_new_design_viewpager_top);
        pager_indicator = fragmnetview.findViewById(R.id.fragment_product_details_new_design_indicator);
        mImageViewShare = fragmnetview.findViewById(R.id.fragment_product_details_new_design_imageview_share);
        mTextViewMedicineName = fragmnetview.findViewById(R.id.fragment_product_details_new_design_textview_product_name);
        mTextViewSpecialPrice = fragmnetview.findViewById(R.id.fragment_product_details_new_design_textview_main_price);
        mTextViewMainPrice = fragmnetview.findViewById(R.id.fragment_product_details_new_design_textview_main_mrp);
        mTextViewDiscount = fragmnetview.findViewById(R.id.fragment_product_details_new_design_textview_discount);
        mSpinner = fragmnetview.findViewById(R.id.fragment_product_details_qty_spinner);
        mLinearLayoutRating = fragmnetview.findViewById(R.id.fragment_product_details_new_design_relative_rating_review);
        mTextViewAvgRating = fragmnetview.findViewById(R.id.fragment_product_details_new_design_textview_star);
        mTextViewTotalRating = fragmnetview.findViewById(R.id.fragment_product_details_new_design_textview_total_ratings);
        mTextViewTotalReviews = fragmnetview.findViewById(R.id.fragment_product_details_new_design_textview_total_reviews);
     //   mLinearLayoutRecyclerview = fragmnetview.findViewById(R.id.fragment_product_details_linear_alternate_brand);
        //mRecyclerViewBrand = fragmnetview.findViewById(R.id.fragment_product_details_textview_alternate_brand_recycleview);
        mLinearLayoutProductDescription = fragmnetview.findViewById(R.id.fragment_product_details_new_design_relative_package_detail);
        mWebViewDescription = fragmnetview.findViewById(R.id.fragment_full_description_webview);
        mLinearLayoutUse = fragmnetview.findViewById(R.id.fragment_product_details_new_design_linear_use_desciption);
        mWebViewUse = fragmnetview.findViewById(R.id.fragment_use_description_webview);
        mLinearLayoutSideEffect = fragmnetview.findViewById(R.id.fragment_product_details_new_design_linear_SideEffects_desciption);
        mWebViewSideEffect = fragmnetview.findViewById(R.id.fragment_SideEffects_description_webview);
        mLinearLayoutAlcohol = fragmnetview.findViewById(R.id.fragment_product_details_new_design_linear_Alcohol_desciption);
        mTextviewAlcohol = fragmnetview.findViewById(R.id.fragment_Alcohol_description_textview);
        mLinearLayoutPregnancy = fragmnetview.findViewById(R.id.fragment_product_details_new_design_linear_Pregnancy_desciption);
        mTextviewPregnancy = fragmnetview.findViewById(R.id.fragment_Pregnancy_description_textview);
        mLinearLayoutBreastFeeding = fragmnetview.findViewById(R.id.fragment_product_details_new_design_linear_Breastfeeding_desciption);
        mTextviewBreastFeeding = fragmnetview.findViewById(R.id.fragment_Breastfeeding_description_textview);
        mLinearLayoutDriving = fragmnetview.findViewById(R.id.fragment_product_details_new_design_linear_Driving_desciption);
        mTextviewDriving = fragmnetview.findViewById(R.id.fragment_Driving_description_textview);
        mLinearLayoutKidney = fragmnetview.findViewById(R.id.fragment_product_details_new_design_linear_Kidney_desciption);
        mTextviewKidney = fragmnetview.findViewById(R.id.fragment_Kidney_description_textview);
        mLinearLayoutLiver = fragmnetview.findViewById(R.id.fragment_product_details_new_design_linear_Liver_desciption);
        mTextviewLiver = fragmnetview.findViewById(R.id.fragment_Liver_description_textview);
        mProgressBarRelatedItem = fragmnetview.findViewById(R.id.fragment_product_details_new_design_pgbProductDetailsRelated);
        mLinearLayoutRelatedItem = fragmnetview.findViewById(R.id.fragment_product_details_new_design_related_container);
        mLinearLayoutBottom = fragmnetview.findViewById(R.id.fragment_product_details_new_design_linear_bottom_menu);
        mTextViewAddTocart = fragmnetview.findViewById(R.id.fragment_product_details_AddToCart);
        mTextManufacture = fragmnetview.findViewById(R.id.fragment_product_details_new_design_textview_product_Manufacturer);
        mImageViewRating = fragmnetview.findViewById(R.id.imageview_arrow_rating);
        mLinearLayoutstorge = fragmnetview.findViewById(R.id.fragment_product_details_new_design_linear_storage);
        mTextViewStoteg = fragmnetview.findViewById(R.id.fragment_product_details_new_design_textview_Storage_value);
        mTextViewMedicineContent = fragmnetview.findViewById(R.id.fragment_product_details_new_design_textview_medicine_content_value);
        mLinearLayoutMedicineContent = fragmnetview.findViewById(R.id.fragment_product_details_new_design_linear_medicine_content);
        mLinearLayoutUseMedicine = fragmnetview.findViewById(R.id.fragment_product_details_new_design_linear_UseMedicine_desciption);
        mWebViewUseMedicine = fragmnetview.findViewById(R.id.fragment_UseMedicine_description_webview);
        mLinearLayoutMedicineWorks = fragmnetview.findViewById(R.id.fragment_product_details_new_design_linear_MedicineWorks_desciption);
        mWebViewMedicineWorks = fragmnetview.findViewById(R.id.fragment_MedicineWorks_description_webview);
        mLinearLayoutQuickTips = fragmnetview.findViewById(R.id.fragment_product_details_new_design_linear_QuickTips_desciption);
        mWebViewQuickTips = fragmnetview.findViewById(R.id.fragment_QuickTips_description_webview);
        mLinearLayoutMissedDosage = fragmnetview.findViewById(R.id.fragment_product_details_new_design_linear_MissedDosage_desciption);
        mWebViewMissedDosage = fragmnetview.findViewById(R.id.fragment_MissedDosage_description_webview);
        mTextViewQtyAddToCart = fragmnetview.findViewById(R.id.fragment_product_details_new_design_textview_ADD_CART);
        mTextViewGotoCart = fragmnetview.findViewById(R.id.fragment_product_details_GoToCart);
        mRelativeLayoutRating = fragmnetview.findViewById(R.id.fragment_product_details_new_design_star);
        mTextViewPreScriptingReq = fragmnetview.findViewById(R.id.fragment_product_details_new_design_textview_Requried_pre);
        mTextViewOutOfStock = fragmnetview.findViewById(R.id.fragment_product_details_new_design_textview_Out_of_stcok);


        mLinearLayoutQtyAdd = fragmnetview.findViewById(R.id.fragment_product_details_linearlayout_qty);
        mTextViewMinus = fragmnetview.findViewById(R.id.fragment_product_details_textview_qty_minus);
        mTextViewPlus = fragmnetview.findViewById(R.id.fragment_product_details_textview_qty_plus);
        mEditTextQtyCount = fragmnetview.findViewById(R.id.fragment_product_details_textview_total_qty);
        mTextViewSeeAll = fragmnetview.findViewById(R.id.fragment_product_details_textview_textview_seeall);
        mTextViewUnit = fragmnetview.findViewById(R.id.fragment_product_details_new_design_textview_qty_Description);
        mTextViewMedicineDescription = fragmnetview.findViewById(R.id.fragment_product_details_new_design_textview_MedicineDescription);
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
        else {
//            Tags.mStringDeviceToken = "";
        }

        mSharedPreferencesCount = mainActivity.getSharedPreferences(Tags.TAG_SHARED_PREFERENCE_Cart_Count, 0);
        mEditorCount = mSharedPreferencesCount.edit();
        String Count = mSharedPreferencesCount.getString(Tags.COUNT, "");
        if (Count.equalsIgnoreCase("") || Count.equalsIgnoreCase("0") ) {
            mTextViewBageCart.setVisibility(View.GONE);
        } else {
            mTextViewBageCart.setVisibility(View.VISIBLE);
            mTextViewBageCart.setText(Count);
        }


        mAdapter = new ProductDetailBannerAdapter(mainActivity, mArrayListImageArray, mProductDetailVOS);
        mViewPagerTop.setAdapter(mAdapter);
        mViewPagerTop.setOnPageChangeListener(FragmentProductDeatils.this);


        mImageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.onBackPressed();
            }
        });

        mTextViewSeeAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentFindSubstitutes fragmentFindSubstitutes = new FragmentFindSubstitutes();
                Bundle mBundle = new Bundle();
                mBundle.putString("MedicineID", mProductDetailVO.getMedicineId());
                mBundle.putString("medicineName", mProductDetailVO.getMedicineName());
                mBundle.putString("Manufacture", mProductDetailVO.getManufacturer());
                mBundle.putString("Price", mProductDetailVO.getPrice());
                mBundle.putString("imagePath","mProductDetailVO.getMedicineImagevos().get(0).getImagePath()");
                fragmentFindSubstitutes.setArguments(mBundle);
                mainActivity.addFragment(fragmentFindSubstitutes, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, fragmentFindSubstitutes.getClass().getName());
            }
        });
        mTextViewPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIntTotalQtyIncrmnt = 30;
                mIntQty = Integer.parseInt(mEditTextQtyCount.getText().toString());

                if (mIntQty < mIntTotalQtyIncrmnt) {

                    if (!mCommonHelper.check_Internet(getActivity())) {
                        Snackbar.with(mainActivity).text(getResources().getString(R.string.interneterror)).show(mainActivity);
                        return;
                    }

                    mIntQty = Integer.parseInt(mEditTextQtyCount.getText().toString()) + 1;

                    mEditTextQtyCount.setText(String.valueOf(mIntQty));

                    UpdateCart();

                } else {


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
            }
        });

        mTextViewMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mIntincrQty = 1;
                mIntQty = Integer.parseInt(mEditTextQtyCount.getText().toString());

                if (mIntQty == mIntincrQty) {

                    mTextViewMinus.setClickable(false);

                } else {
                    mIntQty = Integer.parseInt(mEditTextQtyCount.getText().toString()) - 1;

                    mEditTextQtyCount.setText(String.valueOf(mIntQty));

                    UpdateCart();
                }

            }
        });

        mLinearLayoutRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentRatingReviewsDetails fragmentRatingReviewsDetails = new FragmentRatingReviewsDetails();
                Bundle mBundle = new Bundle();
                mBundle.putString("MedicineID", mProductDetailVO.getMedicineId());
                mBundle.putString("AvgRting", mProductDetailVO.getAvgReview());
                mBundle.putString("totalRating", mProductDetailVO.getTotalRating());
                mBundle.putString("totalReview", mProductDetailVO.getTotalReview());
                mBundle.putString("medicineName", mProductDetailVO.getMedicineName());
                fragmentRatingReviewsDetails.setArguments(mBundle);
                mainActivity.addFragment(fragmentRatingReviewsDetails, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, fragmentRatingReviewsDetails.getClass().getName());
            }
        });

        mTextViewQtyAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddCart();
            }
        });


        mTextViewGotoCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentCart fragmentCart = new FragmentCart();
                mainActivity.addFragment(fragmentCart, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, fragmentCart.getClass().getName());
            }
        });

        mImageViewShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareAllText(true, true, true);
            }
        });

        mTextViewAddTocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddCart();
            }
        });

        mImageViewCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentCart fragmentCart = new FragmentCart();
                mainActivity.addFragment(fragmentCart, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, fragmentCart.getClass().getName());
            }
        });

        return fragmnetview;
    }


    public void DetailsPage() {
        //        drtails page set up
        getLoaderManager().restartLoader(0, null, new LoaderManager.LoaderCallbacks<TaskExecutor>() {
                    @Override
                    public Loader<TaskExecutor> onCreateLoader(int arg0, Bundle arg1) {

                        GetProductDetails getProductDetails = new GetProductDetails(mainActivity, null);
                        return new LoaderTaskWithoutProgressDialog<TaskExecutor>(mainActivity, getProductDetails);
                    }

                    @SuppressLint({"SetJavaScriptEnabled", "SetTextI18n"})
                    @Override
                    public void onLoadFinished(Loader<TaskExecutor> arg0, TaskExecutor arg1) {
                        getLoaderManager().destroyLoader(0);
                        mProgressBar.setVisibility(View.VISIBLE);

                        //if (isAdded()) {
                        if (mPostParseGet.isNetError)
                            Snackbar.with(mainActivity).text(getResources().getString(R.string.interneterror)).show(mainActivity);
                        else if (mPostParseGet.isOtherError)
                            Snackbar.with(mainActivity).text(getResources().getString(R.string.notabletocommunicatewithserver)).show(mainActivity);
                        else {
                            if (serverResponseVO != null) {

                                if (serverResponseVO.getStatus() != null &&
                                        serverResponseVO.getStatus().equalsIgnoreCase("true")) {

                                    mProductDetailVO = (ProductDetailVO) serverResponseVO.getData();

                                    if (mProductDetailVO != null) {

                                        mProductDetailVO.setUid(mStringUserid);


//                                Medicine name
                                        if (mProductDetailVO.getMedicineName() != null) {
                                            mTextViewMedicineName.setText(mProductDetailVO.getMedicineName());
                                        }
//                                  Medicine Description
                                        if (mProductDetailVO.getMedicineDescription() == null || mProductDetailVO.getMedicineDescription().equalsIgnoreCase("null") ) {
                                            mTextViewMedicineDescription.setVisibility(View.GONE);
                                        }
                                        else {

                                            mTextViewMedicineDescription.setText(mProductDetailVO.getMedicineDescription());
                                        }


                                        if (mProductDetailVO.getManufacturer() != null) {
                                            mTextManufacture.setText(mProductDetailVO.getManufacturer());
                                        }
//                                Mrp price

                                        if (mProductDetailVO.getPrice().equalsIgnoreCase("null") && mProductDetailVO.getPrice() != null || mProductDetailVO.getPrice().equalsIgnoreCase("0")) {

                                            mTextViewSpecialPrice.setVisibility(View.GONE);
                                        } else {
                                            mTextViewSpecialPrice.setText(Tags.LABEL_CURRENCY_SIGN + Utility.getDecimalFormate(mainActivity, Double.parseDouble(mProductDetailVO.getPrice()), 0.0, Tags.DECIMAL_FORMAT));
                                        }

                                        /**
                                         * main price
                                         */
                                        if (mProductDetailVO.getMrp().equalsIgnoreCase("null") && mProductDetailVO.getMrp() != null || mProductDetailVO.getMrp().equalsIgnoreCase("0")) {
                                            mTextViewMainPrice.setVisibility(View.GONE);
                                        } else {
                                            mTextViewMainPrice.setText("MRP " + Tags.LABEL_CURRENCY_SIGN + Utility.getDecimalFormate(mainActivity, Double.parseDouble(mProductDetailVO.getMrp()), 0.0, Tags.DECIMAL_FORMAT));
                                            mTextViewMainPrice.setPaintFlags(mTextViewMainPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                                        }

                                        if (mProductDetailVO.getMrp().equalsIgnoreCase(mProductDetailVO.getPrice()) || mProductDetailVO.getPrice().equalsIgnoreCase("null")) {
                                            mTextViewMainPrice.setVisibility(View.GONE);
                                        }

                                        /**
                                         * dscount price
                                         */
                                        if (mProductDetailVO.getDiscount().equalsIgnoreCase("null") && mProductDetailVO.getDiscount() != null || mProductDetailVO.getDiscount().equalsIgnoreCase("0.0")) {
                                            mTextViewDiscount.setVisibility(View.GONE);
                                        } else {

                                            mTextViewDiscount.setText((Tags.LABEL_CURRENCY_SIGN + Utility.getDecimalFormate(mainActivity, Double.parseDouble(mProductDetailVO.getDiscount()), 0.0, Tags.DECIMAL_FORMAT) + " off"));

                                        }

//                                 prescription Requeried
                                        if (mProductDetailVO.getPrescriptionReq().equalsIgnoreCase("true")) {
                                            mTextViewPreScriptingReq.setVisibility(View.VISIBLE);
                                        } else {
                                            mTextViewPreScriptingReq.setVisibility(View.GONE);
                                        }

                                        if (!mProductDetailVO.getUnitsPerPack().equalsIgnoreCase("null")) {
                                            mTextViewUnit.setText(mProductDetailVO.getUnitsPerPack());
                                        }

//                                 out of stcok product

                                        if (mProductDetailVO.getStockAvailablity().equalsIgnoreCase("true")) {
                                            mTextViewOutOfStock.setVisibility(View.GONE);
                                            mTextViewAddTocart.setVisibility(View.VISIBLE);
                                        } else {
                                            mTextViewOutOfStock.setVisibility(View.VISIBLE);
                                            mTextViewAddTocart.setVisibility(View.GONE);
                                            mLinearLayoutBottom.setVisibility(View.GONE);
                                        }

//                                Storage value
                                        if (mProductDetailVO.getStorage().equalsIgnoreCase("null")) {
                                            mLinearLayoutstorge.setVisibility(View.GONE);
                                        } else {
                                            mTextViewStoteg.setText(mProductDetailVO.getStorage());
                                        }

//                                check qty add to cart value

                                        if (!mProductDetailVO.getMediCartCount().equalsIgnoreCase("null")) {
//                                            if (mProductDetailVO.getMediCartCount().equalsIgnoreCase("true")) {
                                                if (mProductDetailVO.getStockAvailablity().equalsIgnoreCase("True"))
                                                {
                                                    if(!mProductDetailVO.getCartqty().equalsIgnoreCase("0"))
                                                    {
                                                        mLinearLayoutQtyAdd.setVisibility(View.VISIBLE);
                                                        mTextViewQtyAddToCart.setVisibility(View.GONE);
                                                        mTextViewAddTocart.setVisibility(View.GONE);
                                                        mTextViewGotoCart.setVisibility(View.VISIBLE);
                                                        mTextViewOutOfStock.setVisibility(View.GONE);
                                                        mEditTextQtyCount.setText(mProductDetailVO.getCartqty());

                                                    }
                                                    else {
                                                        mLinearLayoutQtyAdd.setVisibility(View.GONE);
                                                        mTextViewQtyAddToCart.setVisibility(View.VISIBLE);
                                                        mTextViewAddTocart.setVisibility(View.VISIBLE);
                                                        mTextViewGotoCart.setVisibility(View.GONE);

                                                    }

                                                }
                                                else {
                                                    mLinearLayoutQtyAdd.setVisibility(View.GONE);
                                                    mTextViewGotoCart.setVisibility(View.GONE);
                                                    mTextViewAddTocart.setVisibility(View.GONE);
                                                    mTextViewOutOfStock.setVisibility(View.VISIBLE);
                                                    mTextViewQtyAddToCart.setVisibility(View.GONE);
                                                }
//                                            }
//                                            else {
//                                                mTextViewGotoCart.setVisibility(View.GONE);
//                                                mTextViewAddTocart.setVisibility(View.VISIBLE);
//                                            }

                                        }
//                                Spinner QTY

//                                mAttributeAdapter = new spinneradapter(mainActivity, mArrayListSpinner);
//                                mAttributeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                                mSpinner.setAdapter(mAttributeAdapter);

                                        mAttributeAdapter = new ArrayAdapter<String>(mainActivity, android.R.layout.simple_spinner_item, mArrayListSpinner);
                                        mAttributeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                                mSpinner.setAdapter(mAttributeAdapter);
                                        mSpinner.setAdapter(mAttributeAdapter);

//                                medicine content
                                        if (mProductDetailVO.getMedicineContent().equalsIgnoreCase("null")) {
                                            mLinearLayoutMedicineContent.setVisibility(View.GONE);
                                        } else {
                                            mTextViewMedicineContent.setText(mProductDetailVO.getMedicineContent());
                                        }

//                                rating review

                                        if (GuestUser.equalsIgnoreCase("1")) {
                                            mLinearLayoutRating.setVisibility(View.GONE);
                                        } else {

                                            if (mProductDetailVO.getAvgReview() != null && mProductDetailVO.getAvgReview().equalsIgnoreCase("0.0")) {
                                                mLinearLayoutRating.setVisibility(View.GONE);
                                            } else {

                                                mLinearLayoutRating.setVisibility(View.VISIBLE);
                                                if (!mProductDetailVO.getAvgReview().equalsIgnoreCase("0.0")) {
                                                    Double mDoubleRating = Double.valueOf(mProductDetailVO.getAvgReview());
                                                    mTextViewAvgRating.setText(new DecimalFormat("##.##").format(mDoubleRating));

                                                    if (mDoubleRating == 1) {
                                                        mTextViewAvgRating.setBackgroundColor(mainActivity.getResources().getColor(R.color.red_color));
                                                        mRelativeLayoutRating.setBackgroundColor(mainActivity.getResources().getColor(R.color.red_color));
                                                    } else if (mDoubleRating > 1 && mDoubleRating < 3) {
                                                        mTextViewAvgRating.setBackgroundColor(mainActivity.getResources().getColor(R.color.color_cart));
                                                        mRelativeLayoutRating.setBackgroundColor(mainActivity.getResources().getColor(R.color.color_cart));
                                                    } else if (mDoubleRating >= 3) {
                                                        mTextViewAvgRating.setBackgroundColor(mainActivity.getResources().getColor(R.color.dark_green));
                                                        mRelativeLayoutRating.setBackgroundColor(mainActivity.getResources().getColor(R.color.dark_green));
                                                    }
                                                }
                                                if (!mProductDetailVO.getTotalRating().equalsIgnoreCase("0.0")) {
                                                    mTextViewTotalRating.setText(mProductDetailVO.getTotalRating() + " " + getResources().getString(R.string.ratingsonly));
                                                }
                                                if (!mProductDetailVO.getTotalReview().equalsIgnoreCase("0.0")) {
                                                    mTextViewTotalReviews.setText(mProductDetailVO.getTotalReview() + " " + getResources().getString(R.string.reviewsonly));
                                                }
                                            }
                                        }

//                                alternet barnd

                                   //     mStringsAlternetbarnd = mProductDetailVO.getAltMedicineVOArrayList();
                                    //    if (mStringsAlternetbarnd != null && mStringsAlternetbarnd.size() > 0) {
                                         //   AlternetBarndAdapter searchAdapter = new AlternetBarndAdapter(mainActivity, mStringsAlternetbarnd);
                                            //mRecyclerViewBrand.setHasFixedSize(true);
                                           // mRecyclerViewBrand.setLayoutManager(new LinearLayoutManager(mainActivity));
                                          //  mRecyclerViewBrand.setAdapter(searchAdapter);
                                      //  } else {
                                        //    mLinearLayoutRecyclerview.setVisibility(View.GONE);
                                       // }


//                              IMAGE URL
                                        if (mProductDetailVO.getMedicineImagevos() != null && mProductDetailVO.getMedicineImagevos().size() > 0) {
                                            mArrayListImageArray.clear();
                                            mArrayListImageArray.addAll(mProductDetailVO.getMedicineImagevos());
                                            mAdapter.notifyDataSetChanged();
                                            System.out.println("VT image array ----->" + mProductDetailVO.getMedicineImagevos());
                                            setUiPageViewController();
                                            mViewPagerTop.setCurrentItem(0);
                                            mViewPagerTop.setOnPageChangeListener(FragmentProductDeatils.this);
                                            mImageViewPlaceholder.setVisibility(View.GONE);


                                        } else {
                                            mViewPagerTop.setVisibility(View.GONE);
                                            mImageViewPlaceholder.setVisibility(View.VISIBLE);
                                        }


                                        /**
                                         * Webview Description
                                         */

                                        if (mProductDetailVO.getDescription() != null && !mProductDetailVO.getDescription().equalsIgnoreCase("null")) {

                                            mLinearLayoutProductDescription.setVisibility(View.VISIBLE);

                                            mWebViewDescription.getSettings().setJavaScriptEnabled(true);
                                            mWebViewDescription.getSettings().setAppCacheEnabled(false);
                                            mWebViewDescription.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
                                            mWebViewDescription.setWebViewClient(new AppWebViewClient());
                                            mWebViewDescription.setLongClickable(false);
                                            mWebViewDescription.setHapticFeedbackEnabled(false);
                                            mWebViewDescription.setOnLongClickListener(new View.OnLongClickListener() {
                                                @Override
                                                public boolean onLongClick(View v) {
                                                    return true;
                                                }
                                            });

                                            mWebViewDescription.loadHtml(mProductDetailVO.getDescription(), "text/html", "UTF-8");
                                        } else {
                                            mLinearLayoutProductDescription.setVisibility(View.GONE);
                                        }

                                        mWebViewDescription.setWebViewClient(new WebViewClient() {
                                            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                                                if (url != null && (url.startsWith("http://") || url.startsWith("https://"))) {
                                                    view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                                                    return true;
                                                } else {
                                                    return false;
                                                }
                                            }
                                        });
                                        /**
                                         * Webview uses
                                         */

                                        if (mProductDetailVO.getUses() != null && !mProductDetailVO.getUses().equalsIgnoreCase("null")) {

                                            mLinearLayoutUse.setVisibility(View.VISIBLE);

                                            mWebViewUse.getSettings().setJavaScriptEnabled(true);
                                            mWebViewUse.getSettings().setAppCacheEnabled(false);
                                            mWebViewUse.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
                                            mWebViewUse.setWebViewClient(new AppWebViewClient());
                                            mWebViewUse.setLongClickable(false);
                                            mWebViewUse.setHapticFeedbackEnabled(false);

                                            mWebViewUse.setOnLongClickListener(new View.OnLongClickListener() {
                                                @Override
                                                public boolean onLongClick(View v) {
                                                    return true;
                                                }
                                            });

                                            mWebViewUse.loadHtml(mProductDetailVO.getUses(), "text/html", "UTF-8");
                                        } else {
                                            mLinearLayoutUse.setVisibility(View.GONE);
                                        }

                                        mWebViewUse.setWebViewClient(new WebViewClient() {
                                            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                                                if (url != null && (url.startsWith("http://") || url.startsWith("https://"))) {
                                                    view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                                                    return true;
                                                } else {
                                                    return false;
                                                }
                                            }
                                        });

                                        /**
                                         * Webview side effect
                                         */
                                        if (mProductDetailVO.getSideEffects() != null && !mProductDetailVO.getSideEffects().equalsIgnoreCase("null")) {

                                            mLinearLayoutSideEffect.setVisibility(View.VISIBLE);

                                            mWebViewSideEffect.getSettings().setJavaScriptEnabled(true);
                                            mWebViewSideEffect.getSettings().setAppCacheEnabled(false);
                                            mWebViewSideEffect.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
                                            mWebViewSideEffect.setWebViewClient(new AppWebViewClient());
                                            mWebViewSideEffect.setHapticFeedbackEnabled(false);

                                            mWebViewSideEffect.setOnLongClickListener(new View.OnLongClickListener() {
                                                @Override
                                                public boolean onLongClick(View v) {
                                                    return true;
                                                }
                                            });

                                            mWebViewSideEffect.loadHtml(mProductDetailVO.getSideEffects(), "text/html", "UTF-8");
                                        } else {
                                            mLinearLayoutSideEffect.setVisibility(View.GONE);
                                        }

                                        mWebViewSideEffect.setWebViewClient(new WebViewClient() {
                                            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                                                if (url != null && (url.startsWith("http://") || url.startsWith("https://"))) {
                                                    view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                                                    return true;
                                                } else {
                                                    return false;
                                                }
                                            }
                                        });

                                        /**
                                         * Webview UseMedicine
                                         */
                                        if (mProductDetailVO.getUseMedicine() != null && !mProductDetailVO.getUseMedicine().equalsIgnoreCase("null")) {

                                            mLinearLayoutUseMedicine.setVisibility(View.VISIBLE);

                                            mWebViewUseMedicine.getSettings().setJavaScriptEnabled(true);
                                            mWebViewUseMedicine.getSettings().setAppCacheEnabled(false);
                                            mWebViewUseMedicine.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
                                            mWebViewUseMedicine.setWebViewClient(new AppWebViewClient());
                                            mWebViewUseMedicine.setLongClickable(false);
                                            mWebViewUseMedicine.setHapticFeedbackEnabled(false);

                                            mWebViewUseMedicine.setOnLongClickListener(new View.OnLongClickListener() {
                                                @Override
                                                public boolean onLongClick(View v) {
                                                    return true;
                                                }
                                            });

                                            mWebViewUseMedicine.loadHtml(mProductDetailVO.getUseMedicine(), "text/html", "UTF-8");
                                        } else {
                                            mLinearLayoutUseMedicine.setVisibility(View.GONE);
                                        }

                                        mWebViewUseMedicine.setWebViewClient(new WebViewClient() {
                                            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                                                if (url != null && (url.startsWith("http://") || url.startsWith("https://"))) {
                                                    view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                                                    return true;
                                                } else {
                                                    return false;
                                                }
                                            }
                                        });
                                        /**
                                         * Webview MedicineWorks
                                         */
                                        if (mProductDetailVO.getMedicineWorks() != null && !mProductDetailVO.getMedicineWorks().equalsIgnoreCase("null")) {

                                            mLinearLayoutMedicineWorks.setVisibility(View.VISIBLE);

                                            mWebViewMedicineWorks.getSettings().setJavaScriptEnabled(true);
                                            mWebViewMedicineWorks.getSettings().setAppCacheEnabled(false);
                                            mWebViewMedicineWorks.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
                                            mWebViewMedicineWorks.setWebViewClient(new AppWebViewClient());
                                            mWebViewMedicineWorks.setLongClickable(false);
                                            mWebViewMedicineWorks.setHapticFeedbackEnabled(false);

                                            mWebViewMedicineWorks.setOnLongClickListener(new View.OnLongClickListener() {
                                                @Override
                                                public boolean onLongClick(View v) {
                                                    return true;
                                                }
                                            });

                                            mWebViewMedicineWorks.loadHtml(mProductDetailVO.getMedicineWorks(), "text/html", "UTF-8");
                                        } else {
                                            mLinearLayoutMedicineWorks.setVisibility(View.GONE);
                                        }

                                        mWebViewMedicineWorks.setWebViewClient(new WebViewClient() {
                                            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                                                if (url != null && (url.startsWith("http://") || url.startsWith("https://"))) {
                                                    view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                                                    return true;
                                                } else {
                                                    return false;
                                                }
                                            }
                                        });

                                        /**
                                         * Webview QuickTips
                                         */
                                        if (mProductDetailVO.getQuickTips() != null && !mProductDetailVO.getQuickTips().equalsIgnoreCase("null")) {

                                            mLinearLayoutQuickTips.setVisibility(View.VISIBLE);

                                            mWebViewQuickTips.getSettings().setJavaScriptEnabled(true);
                                            mWebViewQuickTips.getSettings().setAppCacheEnabled(false);
                                            mWebViewQuickTips.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
                                            mWebViewQuickTips.setWebViewClient(new AppWebViewClient());
                                            mWebViewQuickTips.setLongClickable(false);
                                            mWebViewQuickTips.setHapticFeedbackEnabled(false);

                                            mWebViewQuickTips.setOnLongClickListener(new View.OnLongClickListener() {
                                                @Override
                                                public boolean onLongClick(View v) {
                                                    return true;
                                                }
                                            });

                                            mWebViewQuickTips.loadHtml(mProductDetailVO.getQuickTips(), "text/html", "UTF-8");
                                        } else {
                                            mLinearLayoutQuickTips.setVisibility(View.GONE);
                                        }

                                        mWebViewQuickTips.setWebViewClient(new WebViewClient() {
                                            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                                                if (url != null && (url.startsWith("http://") || url.startsWith("https://"))) {
                                                    view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                                                    return true;
                                                } else {
                                                    return false;
                                                }
                                            }
                                        });

                                        /**
                                         * Webview MissedDosage
                                         */
                                        if (mProductDetailVO.getMissedDosage() != null && !mProductDetailVO.getMissedDosage().equalsIgnoreCase("null")) {

                                            mLinearLayoutMissedDosage.setVisibility(View.VISIBLE);

                                            mWebViewMissedDosage.getSettings().setJavaScriptEnabled(true);
                                            mWebViewMissedDosage.getSettings().setAppCacheEnabled(false);
                                            mWebViewMissedDosage.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
                                            mWebViewMissedDosage.setWebViewClient(new AppWebViewClient());
                                            mWebViewMissedDosage.setHapticFeedbackEnabled(false);

                                            mWebViewMissedDosage.setOnLongClickListener(new View.OnLongClickListener() {
                                                @Override
                                                public boolean onLongClick(View v) {
                                                    return true;
                                                }
                                            });

                                            mWebViewMissedDosage.loadHtml(mProductDetailVO.getMissedDosage(), "text/html", "UTF-8");
                                        } else {
                                            mLinearLayoutMissedDosage.setVisibility(View.GONE);
                                        }

                                        mWebViewMissedDosage.setWebViewClient(new WebViewClient() {
                                            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                                                if (url != null && (url.startsWith("http://") || url.startsWith("https://"))) {
                                                    view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                                                    return true;
                                                } else {
                                                    return false;
                                                }
                                            }
                                        });
                                        /**
                                         * Webview Alcohol
                                         */

                                        if (mProductDetailVO.getAlcohol() != null && !mProductDetailVO.getAlcohol().equalsIgnoreCase("null")) {

                                            mLinearLayoutAlcohol.setVisibility(View.VISIBLE);

                                            mTextviewAlcohol.setText(HtmlCompat.fromHtml( mProductDetailVO.getAlcohol(),0));
                                        } else {
                                            mLinearLayoutAlcohol.setVisibility(View.VISIBLE);
                                            mTextviewAlcohol.setText("No interaction found/established");
                                        }

                                        /**
                                         * Webview pregnancy
                                         */
                                        if (mProductDetailVO.getPregnancy() != null && !mProductDetailVO.getPregnancy().equalsIgnoreCase("null")) {

                                            mLinearLayoutPregnancy.setVisibility(View.VISIBLE);

                                            mTextviewPregnancy.setText(HtmlCompat.fromHtml(mProductDetailVO.getPregnancy(),0));


                                        } else {
                                            mTextviewPregnancy.setText("No interaction found/established");
                                            mLinearLayoutPregnancy.setVisibility(View.VISIBLE);
                                        }

                                        /**
                                         * Webview brestFeeding
                                         */
                                        if (mProductDetailVO.getBreastfeeding() != null && !mProductDetailVO.getBreastfeeding().equalsIgnoreCase("null")) {

                                            mLinearLayoutBreastFeeding.setVisibility(View.VISIBLE);

                                            mTextviewBreastFeeding.setText(HtmlCompat.fromHtml( mProductDetailVO.getBreastfeeding(),0));
                                        } else {
                                            mLinearLayoutBreastFeeding.setVisibility(View.VISIBLE);
                                            mTextviewBreastFeeding.setText("No interaction found/established");
                                        }


                                        /**
                                         * Webview Drivng
                                         */

                                        if (mProductDetailVO.getDriving() != null && !mProductDetailVO.getDriving().equalsIgnoreCase("null")) {

                                            mLinearLayoutDriving.setVisibility(View.VISIBLE);


                                            mTextviewDriving.setText(HtmlCompat.fromHtml(mProductDetailVO.getDriving(),0));
                                        } else {
                                            mLinearLayoutDriving.setVisibility(View.VISIBLE);
                                            mTextviewDriving.setText("No interaction found/established");
                                        }


                                        /**
                                         * Webview Liver
                                         */
                                        if (mProductDetailVO.getLiver() != null && !mProductDetailVO.getLiver().equalsIgnoreCase("null")) {

                                            mLinearLayoutLiver.setVisibility(View.VISIBLE);


                                            mTextviewLiver.setText(HtmlCompat.fromHtml( mProductDetailVO.getLiver(),0));
                                        } else {
                                            mLinearLayoutLiver.setVisibility(View.VISIBLE);
                                            mTextviewLiver.setText("No interaction found/established");
                                        }


                                        /**
                                         * Webview Kindney
                                         */
                                        if (mProductDetailVO.getKidney() != null && !mProductDetailVO.getKidney().equalsIgnoreCase("null")) {

                                            mLinearLayoutKidney.setVisibility(View.VISIBLE);


                                            mTextviewKidney.setText(HtmlCompat.fromHtml(mProductDetailVO.getKidney(),0));
                                        } else {
                                            mLinearLayoutKidney.setVisibility(View.VISIBLE);
                                            mTextviewKidney.setText("No interaction found/established");
                                        }


                                        mRelativeLayoutTop.setVisibility(View.VISIBLE);
                                        mStickyScrollView.setVisibility(View.VISIBLE);

                                    }

                                    mProgressBar.setVisibility(View.GONE);
                                    mLinearLayoutBottom.setVisibility(View.VISIBLE);

//                            updateCartUI();

                                } else {
                                    Snackbar.with(mainActivity).text(serverResponseVO.getMsg()).show(mainActivity);
                                    mProgressBar.setVisibility(View.GONE);
                                    mStickyScrollView.setVisibility(View.GONE);
                                    @SuppressLint("HandlerLeak") Handler handler = new Handler() {
                                        @Override
                                        public void handleMessage(Message msg) {
                                            if (msg.what == Tags.WHAT) {
                                                final Handler handler = new Handler();
                                                handler.postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        mainActivity.onBackPressed();
                                                    }
                                                }, 800);
                                            }
                                        }
                                    };
                                    handler.sendEmptyMessage(Tags.WHAT);
                                }
                            }
                        }
                    }

                    @Override
                    public void onLoaderReset(Loader<TaskExecutor> arg0) {

                    }
                }
        );
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

        for (int i = 0; i < dotsCount; i++) {
            dots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselection));
        }
        dots[position].setImageDrawable(getResources().getDrawable(R.drawable.selection));
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    @SuppressLint("RestrictedApi")
    public class GetProductDetails extends TaskExecutor {
        protected GetProductDetails(Context context, Bundle bundle) {
            super(context, bundle);
        }

        @Override
        protected Object executeTask() {
            Utility.debugger("VT Medicine id ......" + mStringmedicineId);
            productDeatilsInfoService getAllAddressInfoService = new productDeatilsInfoService();
            serverResponseVO = getAllAddressInfoService.getproductdetails(mainActivity, Tags.ProductDeatils, mStringmedicineId, mStringUserid ,Tags.mStringDeviceToken);

            return null;
        }
    }

//Alternet barnds adpter

    public class AlternetBarndAdapter extends RecyclerView.Adapter<AlternetBarndAdapter.MyViewHolderAlternetBarnd> {
        Context mContext;
        ArrayList<AltMedicineVO> mArrayList;

        public AlternetBarndAdapter(MainActivity mainActivity, ArrayList<AltMedicineVO> mStringsAlternetbarnd) {
            this.mContext = mainActivity;
            this.mArrayList = mStringsAlternetbarnd;
        }

        @NonNull
        @Override
        public MyViewHolderAlternetBarnd onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_details_medicine, parent, false);
            MyViewHolderAlternetBarnd myViewHolderSearch = new MyViewHolderAlternetBarnd(v);
            return myViewHolderSearch;
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull MyViewHolderAlternetBarnd holder, @SuppressLint("RecyclerView") int position) {

            holder.itemView.setClickable(false);

            if (!mArrayList.get(position).getMedicineName().equalsIgnoreCase("null") && mArrayList.get(position).getMedicineName() != null) {
                holder.mTextViewMdicineName.setText(mArrayList.get(position).getMedicineName());
            }
            if (!mArrayList.get(position).getPrice().equalsIgnoreCase("null") && mArrayList.get(position).getPrice() != null) {
                holder.mTextViewTablet.setText(Tags.LABEL_CURRENCY_SIGN + Utility.getDecimalFormate(mainActivity, Double.parseDouble(mArrayList.get(position).getPrice()), 0.0, Tags.DECIMAL_FORMAT));

            } else {
                holder.mTextViewTablet.setVisibility(View.GONE);
            }
            if (!mArrayList.get(position).getManufacturer().equalsIgnoreCase("null") && mArrayList.get(position).getManufacturer() != null) {
                holder.mTextViewcompnayName.setText(mArrayList.get(position).getManufacturer());
            } else {
                holder.mTextViewcompnayName.setVisibility(View.GONE);
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    FragmentProductDeatils fragmentProductDeatils = new FragmentProductDeatils();
                    Bundle mBundle = new Bundle();
                    mBundle.putString("medicineid", mArrayList.get(position).getAltMedicineId());
                    fragmentProductDeatils.setArguments(mBundle);
                    mainActivity.addFragment(fragmentProductDeatils, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, fragmentProductDeatils.getClass().getName());
                }
            });

        }


        @Override
        public int getItemCount() {
            if(mArrayList.size() >= 5 )
            {
                return 5;
            }
            else {
                return mArrayList.size();
            }
        }

        public class MyViewHolderAlternetBarnd extends RecyclerView.ViewHolder {

            TextView mTextViewMdicineName;
            TextView mTextViewTablet;
            TextView mTextViewcompnayName;
            TextView mTextViewsaveValue;


            public MyViewHolderAlternetBarnd(View itemView) {
                super(itemView);

                mTextViewMdicineName = itemView.findViewById(R.id.row_details_medicine_name);
                mTextViewTablet = itemView.findViewById(R.id.row_details_medicine_tablet);
                mTextViewcompnayName = itemView.findViewById(R.id.row_details_medicine_company_name);
                mTextViewsaveValue = itemView.findViewById(R.id.row_details_medicine_save_value);
            }
        }
    }

//    public class spinneradapter extends ArrayAdapter<Integer> {
//        public spinneradapter(@NonNull Context context, Integer[] resource) {
//            super(context, 0,resource);
//        }
//
//        @NonNull
//        @Override
//        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//            return initView(position, convertView, parent);
//        }
//
//        @Override
//        public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//            return initView(position, convertView, parent);
//        }
//
//        private View initView(int position, View convertView, ViewGroup parent) {
//            if (convertView == null) {
//                convertView = LayoutInflater.from(getContext()).inflate(
//                        R.layout.spn_product_attribute, parent, false
//                );
//            }
//
//            TextView textViewName = convertView.findViewById(R.id.txt_name);
//
//            textViewName.setText(String.valueOf(position));
//
//
//
//            return convertView;
//        }
//    }

    private void setUiPageViewController() {
        pager_indicator.removeAllViews();
        dotsCount = mAdapter.getCount();
        Utility.debugger("VT dotsCount...." + dotsCount);

        if (dotsCount > 0) {
            dots = new ImageView[dotsCount];

            for (int i = 0; i < dotsCount; i++) {
                dots[i] = new ImageView(mainActivity);
                dots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselection));

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

                params.setMargins(3, 0, 3, 0);
                pager_indicator.addView(dots[i], params);

            }
            dots[0].setImageDrawable(getResources().getDrawable(R.drawable.selection));
        }

    }

    public class AppWebViewClient extends WebViewClient {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }
    }


    private void ShareAllText(boolean Price, boolean Desc, boolean Link) {

        String mStringPrice = "";
        String mStringDesc = "";

        if (isAdded()) {

            String builtLink = new LinkBuilder()
                    .setDomain(getString(R.string.deep_link_host))
                    .setLink(getString(R.string.deep_linkmainurl, mProductDetailVO.getMedicineId(), mProductDetailVO.getMedicineName()))
                    .setSt(getString(R.string.app_name))
                    .setApn(Tags.PACKAGENAME)
                    .setIbi(Tags.PACKAGENAME)
//                   .setIsi("1479028718")
                    .build();

            if (Price) {
                mStringPrice = "MRP" + ": " + Tags.LABEL_CURRENCY_SIGN + mProductDetailVO.getMrp() +
                        "\n" + getString(R.string.discount) + ": " + (mProductDetailVO.getDiscount()) + "";

            }

            if (Desc) {
                if (mProductDetailVO.getDescription().equalsIgnoreCase("")) {
                    mStringDesc = "";
                } else {
                    mStringDesc = "Product Desctiption " + " : " + mProductDetailVO.getDescription();
                }
            }

            String finalMStringPrice = mStringPrice;
            String finalMStringDesc = mStringDesc;
            DynamicLink.Builder builder = FirebaseDynamicLinks.getInstance().createDynamicLink()
                    .setLongLink(Uri.parse(builtLink));

            builder.buildShortDynamicLink()
                    .addOnSuccessListener(mainActivity, new OnSuccessListener<ShortDynamicLink>() {
                        @Override
                        public void onSuccess(ShortDynamicLink shortDynamicLink) {
                            Utility.debugger("VT linksss...." + shortDynamicLink.getShortLink());

                            Intent share = new Intent(Intent.ACTION_SEND);
                            share.setType("text/plain");
                            String mStringData = "";
                            Uri DynamicLink;

                            if (Link) {
                                DynamicLink = shortDynamicLink.getShortLink();
                            } else {
                                DynamicLink = Uri.parse("");
                            }


                            mStringData = getString(R.string.lookFound) + " " + getString(R.string.app_name) + "\n\n" +
                                    "Medicine" + ": " + mProductDetailVO.getMedicineName() +
                                    "\n" + finalMStringPrice +
                                    "\n\n" + DynamicLink;
                            Log.v("log_tag", "All Details " + mStringData);

                            ShareNormalText(share, mStringData);


                        }
                    })
                    .addOnFailureListener(mainActivity, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            e.printStackTrace();
                            Utility.debugger("VT linksss...." + e.getMessage());
                        }
                    });
        }

    }


    private void ShareNormalText(Intent share, String mStringData) {
        share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        share.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        share.putExtra(Intent.EXTRA_TEXT, mStringData);//shortLink

        startActivity(Intent.createChooser(share, "Sharevia"));
    }

    public void AddCart() {


        if (!mCommonHelper.check_Internet(getActivity())) {
            Snackbar.with(mainActivity).text(getResources().getString(R.string.interneterror)).show(mainActivity);
            return;
        }

        if (mProductDetailVO != null) {

            mStringPrice = mProductDetailVO.getPrice();
//            mStringqty = String.valueOf(mSpinner.getSelectedItem());
            mStringqty = "1";
            mStringMedicineId = mProductDetailVO.getMedicineId();

//            mProductDetailVO.setmStringSelectQtyId(mStringqty);


            Utility.debugger("VT cart price...." + mStringPrice);
            Utility.debugger("VT cart Qty...." + mStringqty);

        }
        getLoaderManager().restartLoader(0, null, new LoaderManager.LoaderCallbacks<TaskExecutor>() {
            @Override
            public Loader<TaskExecutor> onCreateLoader(int arg0, Bundle arg1) {

                AddCartExecutor addCartExecutor = new AddCartExecutor(mainActivity, null);
                return new LoaderTask<TaskExecutor>(mainActivity, addCartExecutor);
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
                            if (mServerResponseVOCard.getStatus().equalsIgnoreCase("true")) {
                                mCommonHelper.hideKeyboard(mainActivity);

                                if (mServerResponseVOCard.getData() != null) {
//                                    mDbService.isProductAvailableInCart(mainActivity, mStringUserid,mProductDetailVO.getMedicineId());
//
//                                    mDbService.addOrUpdateSingleItemToCartDetail(mainActivity, mProductDetailVO);
//
//                                    cartCount = mDbService.getTotalCartCount(mainActivity, mStringUserid);
//                                    mainActivity.updateCartCount(cartCount);
//                                    Utility.debugger("vt cart count after...." + cartCount);
//                                    updateCartUI();
                                    if (mServerResponseVOCard.getCartCount() != null) {
                                        if (mServerResponseVOCard.getCartCount().equalsIgnoreCase("0")) {
                                            mTextViewBageCart.setVisibility(View.GONE);
                                            mEditorCount.putString(Tags.COUNT, mServerResponseVOCard.getCartCount());
                                            mEditorCount.apply();
                                        } else {
                                            mTextViewBageCart.setVisibility(View.VISIBLE);
                                            mTextViewBageCart.setText(mServerResponseVOCard.getCartCount());

                                            mEditorCount.putString(Tags.COUNT, mServerResponseVOCard.getCartCount());
                                            mEditorCount.apply();
                                        }
                                    }
//                                    Snackbar.with(mainActivity).text(mServerResponseVOCard.getMsg()).show(mainActivity);
                                    mLinearLayoutQtyAdd.setVisibility(View.VISIBLE);
                                    mTextViewQtyAddToCart.setVisibility(View.GONE);
                                    mTextViewAddTocart.setVisibility(View.GONE);
                                    mTextViewGotoCart.setVisibility(View.VISIBLE);
                                }
                            } else {

                                Snackbar.with(mainActivity).text(mServerResponseVOCard.getMsg()).show(mainActivity);
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
    public class AddCartExecutor extends TaskExecutor {
        protected AddCartExecutor(Context context, Bundle bundle) {
            super(context, bundle);
        }

        @Override
        protected Object executeTask() {
            AddCartInfoService addCartInfoService = new AddCartInfoService();
            mServerResponseVOCard = addCartInfoService.AddCartinfo(mainActivity, Tags.AddCard, "", mStringUserid, mStringPrice, mStringqty, mStringMedicineId,Tags.mStringDeviceToken);

            return null;
        }
    }


    public void UpdateCart() {

        mStringMedicineId = mProductDetailVO.getMedicineId();

        if (!mCommonHelper.check_Internet(getActivity())) {
            Snackbar.with(mainActivity).text(getResources().getString(R.string.interneterror)).show(mainActivity);
            return;
        }

        getLoaderManager().restartLoader(0, null, new LoaderManager.LoaderCallbacks<TaskExecutor>() {
            @Override
            public Loader<TaskExecutor> onCreateLoader(int arg0, Bundle arg1) {
                UpdateCartQty updateCartQty = new UpdateCartQty(mainActivity, arg1);
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


    @SuppressLint("RestrictedApi")
    public class UpdateCartQty extends TaskExecutor {
        protected UpdateCartQty(Context context, Bundle bundle) {
            super(context, bundle);
        }

        @Override
        protected Object executeTask() {
            UpdateCartService updateCartService = new UpdateCartService();
            mServerResponseVOCard = updateCartService.UpdateCartData(mainActivity, Tags.UPDATECARTDATA, mStringUserid, "00000000-0000-0000-0000-000000000000", mStringMedicineId, String.valueOf(mIntQty), mProductDetailVO.getPrice() ,Tags.mStringDeviceToken );

            return null;
        }
    }


    public void updateCartUI() {

        boolean flag;
        flag = mDbService.isProductAvailableInCart(mainActivity, mStringUserid, mProductDetailVO.getMedicineId());

        Utility.debugger("VT update cart flag..." + flag);
        if (flag) {
            mTextViewAddTocart.setText(getResources().getString(R.string.gotocart));
        } else {
            mTextViewAddTocart.setText(getResources().getString(R.string.addtocart));
        }

    }


    @Override
    public void doWork() {
        super.doWork();
        onResumeData();
    }

    @Override
    public void onResume() {
        super.onResume();
        mainActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        onResumeData();
    }

    public void onResumeData() {

        DetailsPage();
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mCommonHelper.freeMemory();
        mainActivity.getSharedPreferences(Tags.COUNT, 0).edit().clear().apply();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mainActivity.getSharedPreferences(Tags.COUNT, 0).edit().clear().apply();
    }

}