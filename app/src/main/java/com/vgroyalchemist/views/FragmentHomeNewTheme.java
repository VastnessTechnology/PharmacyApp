package com.vgroyalchemist.views;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.vgroyalchemist.CommonClass.CircularProgressBar;
import com.vgroyalchemist.CommonClass.CommonClass;
import com.vgroyalchemist.R;
import com.vgroyalchemist.controller.MainActivity;
import com.vgroyalchemist.loaders.LoaderTask;
import com.vgroyalchemist.loaders.LoaderTaskWithoutProgressDialog;
import com.vgroyalchemist.loaders.TaskExecutor;
import com.vgroyalchemist.utils.CirclePageIndicator;
import com.vgroyalchemist.utils.CommonHelper;
import com.vgroyalchemist.utils.CustomAdapterInfinite;
import com.vgroyalchemist.utils.InfiniteViewPager;
import com.vgroyalchemist.utils.Snackbar;
import com.vgroyalchemist.utils.Tags;
import com.vgroyalchemist.utils.Utility;
import com.vgroyalchemist.vos.CategoryList;
import com.vgroyalchemist.vos.DashboardCategoryVO;
import com.vgroyalchemist.vos.DashboardMedicineCategory;
import com.vgroyalchemist.vos.GetAllBannerMenuVO;
import com.vgroyalchemist.vos.MedicineSearchList;
import com.vgroyalchemist.vos.ServerResponseVO;
import com.vgroyalchemist.webservice.AddCartInfoService;
import com.vgroyalchemist.webservice.GetCategoryListInfoService;
import com.vgroyalchemist.webservice.GetCountDataService;
import com.vgroyalchemist.webservice.PostParseGet;
import com.vgroyalchemist.webservice.SearchMedicineInfoService;
import com.vgroyalchemist.webservice.UpdateCartService;

import java.util.ArrayList;

import static com.facebook.FacebookSdk.getApplicationContext;

/* developed by krishna 24-03-2021 */
public class FragmentHomeNewTheme extends NonCartFragment {


    //Variable Declaration
    public static final String FRAGMENT_ID = "5";

    public static MainActivity mainActivity;
    public View fragmentView = null;
    CommonHelper mCommonHelper;


    LinearLayout mLinearLayoutMedicines;
    LinearLayout mLinearLayoutLabTest;
    LinearLayout mLinearLayoutMyPrescription;

    TextView mTextViewUpload;

    Bundle mBundle;
    public static String fromhome = "";

    RecyclerView mRecyclerViewIntelignt;
    RecyclerView mRecyclerViewData;
    TextView mTextViewNoData;
    CircularProgressBar pgbProductList;
    ImageView mImageViewClear;
    AutoCompleteTextView mEditTextSearch;
    RelativeLayout mRelativeLayoutMain;
    private String mStringSearch = "";
    ArrayList<MedicineSearchList> mArrayListSearch;
    ArrayAdapter<String> mAttributeAdapter;
    ArrayList<String> mArrayListSpinner;
    ServerResponseVO mServerResponseVOSearch;
    ServerResponseVO mServerResponseVOCount;
    PostParseGet mPostParseGet;

    public LinearLayout mLinearLayoutLocation;
    public ImageView mImageViewLocation;
    public TextView mTextViewLocation;
    public ImageView mImageViewCart;
    public ImageView mImageViewNotification;
    public RelativeLayout mRelativeLayoutCart;
    public TextView mTextViewBageCart;
    public TextView mTextViewBageNotification;
    public RelativeLayout mRelativeLayoutOptions;

    //    count shared preferance
    public SharedPreferences mSharedPreferencesCount;
    public SharedPreferences.Editor mEditorCount;

    // userdata
    public SharedPreferences mSharedPreferencesUserId;
    public SharedPreferences.Editor mEditor;

    String mStringUserid;
    String mStringPrice;
    String mStringqty;
    String mStringMedicineId;
    boolean isFromConfirm;
    public ServerResponseVO mServerResponseVOCard;
    ServerResponseVO mServerResponseVO;
    String mStringMenuId;
    ArrayList<GetAllBannerMenuVO> getAllBannerMenuVOS;
    ArrayList<GetAllBannerMenuVO> getAllBannerHomeVOS;


    public static InfiniteViewPager viewpager_infinite,viewpager_homebanner;
    public static CirclePageIndicator indicator,indicator_home;

    public SharedPreferences mSharedPreferencesLocation;
    public SharedPreferences.Editor mEditorLocation;

    TextView mTextViewCategoryNo1;
    TextView mTextViewSeeAllCategory1;
    RecyclerView mRecyclerViewCategory1;
    LinearLayout mLinearLayoutCategory1;


    TextView mTextViewCategoryNo2;
    TextView mTextViewSeeAllCategory2;
    RecyclerView mRecyclerViewCategory2;
    LinearLayout mLinearLayoutCategory2;

    TextView mTextViewCategoryNo3;
    TextView mTextViewSeeAllCategory3;
    RecyclerView mRecyclerViewCategory3;
    LinearLayout mLinearLayoutCategory3;

    TextView mTextViewCategoryNo4;
    TextView mTextViewSeeAllCategory4;
    RecyclerView mRecyclerViewCategory4;
    LinearLayout mLinearLayoutCategory4;

    RecyclerView mRecyclerViewPopularCategory;
    LinearLayout mLinearLayoutPopularCategory;

    ArrayList<MedicineSearchList> mArrayListCategory1;
    ArrayList<MedicineSearchList> mArrayListCategory2;
    ArrayList<MedicineSearchList> mArrayListCategory3;
    ArrayList<MedicineSearchList> mArrayListCategory4;
    ArrayList<DashboardMedicineCategory> mDashboardCategoryVos;
    ArrayList<CategoryList> mArrayListPopularCategory;
    DashboardCategoryVO mCategoryVo;
    //login
    public SharedPreferences mSharedPreferencesLoginFirst;
    SharedPreferences.Editor mEditorLogin;
    String GuestUser;

    String mStringCategoryId1;
    String mStringCategoryId2;
    String mStringCategoryId3;
    String mStringCategoryId4;

    String mStringCategoryName1;
    String mStringCategoryName2;
    String mStringCategoryName3;
    String mStringCategoryName4;

    private Location currentLocation;

    private LocationCallback locationCallback;
    int mIntQty, minimumQty, mIntincrQty, mIntTotalQtyIncrmnt;
    public ServerResponseVO mServerResponseVOUpdate;

    TextView mTextViewMinus;
    TextView mTextViewPlus;
    EditText mEditTextValue;
    LinearLayout mLinearLayoutQty;


    // Device Token Check
    public SharedPreferences mSharedPreferencesDeviceToekn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = (MainActivity) getActivity();
        mCommonHelper = new CommonHelper();
        mPostParseGet = new PostParseGet(mainActivity);
//        mFragmentHomeNewTheme = new FragmentHomeNewTheme();
        mArrayListSearch = new ArrayList<MedicineSearchList>();
        getAllBannerMenuVOS = new ArrayList<GetAllBannerMenuVO>();
        getAllBannerHomeVOS = new ArrayList<GetAllBannerMenuVO>();
        mBundle = getArguments();
        mArrayListSpinner = new ArrayList<String>();
        mArrayListCategory1 = new ArrayList<MedicineSearchList>();
        mDashboardCategoryVos = new ArrayList<DashboardMedicineCategory>();
        mCategoryVo = new DashboardCategoryVO();
        mArrayListCategory2 = new ArrayList<MedicineSearchList>();
        mArrayListCategory3 = new ArrayList<MedicineSearchList>();
        mArrayListCategory4 = new ArrayList<MedicineSearchList>();
        mArrayListPopularCategory = new ArrayList<CategoryList>();

        for (int i = 1; i <= 30; i++) {
            mArrayListSpinner.add(String.valueOf(i));
        }

        if (mBundle != null) {
            fromhome = mBundle.getString("fromhome");
            isFromConfirm = mBundle.getBoolean(getString(R.string.app_name));
        }

        if (fromhome == null) {
            fromhome = "";
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        fragmentView = inflater.inflate(R.layout.fragment_home_new_theme, container, false);


        mLinearLayoutMedicines = fragmentView.findViewById(R.id.fragment_home_screen_linear_medicines);
        mLinearLayoutLabTest = fragmentView.findViewById(R.id.fragment_home_screen_linear_LabTest);
        mLinearLayoutMyPrescription = fragmentView.findViewById(R.id.fragment_home_screen_linear_UploadPresc);

        mTextViewUpload = fragmentView.findViewById(R.id.fragment_home_new_theme_textview_upload);
        mRecyclerViewIntelignt = fragmentView.findViewById(R.id.fragment_home_screen_listview_data_intelligent);
        mRecyclerViewData = fragmentView.findViewById(R.id.fragment_home_screen_listview_data);
        mTextViewNoData = fragmentView.findViewById(R.id.fragment_home_screen_textview_no_data);
        mEditTextSearch = fragmentView.findViewById(R.id.fragment_home_screen_edittext_search);
        pgbProductList = fragmentView.findViewById(R.id.fragment_home_screen_pgbProductList);
        mImageViewClear = fragmentView.findViewById(R.id.fragment_home_screen_imageview_cancel);
        mRelativeLayoutMain = fragmentView.findViewById(R.id.fragment_home_screen_relative);

        mLinearLayoutLocation = fragmentView.findViewById(R.id.actionbar_title_Linear_location);
        mTextViewLocation = fragmentView.findViewById(R.id.actionbar_title_textview_locationhome);
        mImageViewCart = fragmentView.findViewById(R.id.actionbar_title_image_cart);
        mImageViewNotification = fragmentView.findViewById(R.id.actionbar_title_image_notification);
        mTextViewBageCart = fragmentView.findViewById(R.id.actionbar_title_textview_badge_cart);
        mTextViewBageNotification = fragmentView.findViewById(R.id.actionbar_title_textview_badge_notification);
        mRelativeLayoutOptions = fragmentView.findViewById(R.id.actionbar_title_relative_options);
        mImageViewLocation = fragmentView.findViewById(R.id.actionbar_title_imageview_location);

        mTextViewCategoryNo1 = fragmentView.findViewById(R.id.fragment_home_new_theme_category_No1);
        mTextViewSeeAllCategory1 = fragmentView.findViewById(R.id.fragment_home_new_theme_category_No1_seeAll);
        mRecyclerViewCategory1 = fragmentView.findViewById(R.id.fragment_home_new_theme_RecyclerView_category_No1);
        mLinearLayoutCategory1 = fragmentView.findViewById(R.id.fragment_home_new_theme_Linear_category_No1);


        mTextViewCategoryNo2 = fragmentView.findViewById(R.id.fragment_home_new_theme_category_No2);
        mTextViewSeeAllCategory2 = fragmentView.findViewById(R.id.fragment_home_new_theme_category_No2_seeAll);
        mRecyclerViewCategory2 = fragmentView.findViewById(R.id.fragment_home_new_theme_RecyclerView_category_No2);
        mLinearLayoutCategory2 = fragmentView.findViewById(R.id.fragment_home_new_theme_Linear_category_No2);

        mTextViewCategoryNo3 = fragmentView.findViewById(R.id.fragment_home_new_theme_category_No3);
        mTextViewSeeAllCategory3 = fragmentView.findViewById(R.id.fragment_home_new_theme_category_No3_seeAll);
        mRecyclerViewCategory3 = fragmentView.findViewById(R.id.fragment_home_new_theme_RecyclerView_category_No3);
        mLinearLayoutCategory3 = fragmentView.findViewById(R.id.fragment_home_new_theme_Linear_category_No3);

        mTextViewCategoryNo4 = fragmentView.findViewById(R.id.fragment_home_new_theme_category_No4);
        mTextViewSeeAllCategory4 = fragmentView.findViewById(R.id.fragment_home_new_theme_category_No4_seeAll);
        mRecyclerViewCategory4 = fragmentView.findViewById(R.id.fragment_home_new_theme_RecyclerView_category_No4);
        mLinearLayoutCategory4 = fragmentView.findViewById(R.id.fragment_home_new_theme_Linear_category_No4);

        mRecyclerViewPopularCategory = fragmentView.findViewById(R.id.fragment_home_new_theme_RecyclerView_popular_category);
        mLinearLayoutPopularCategory = fragmentView.findViewById(R.id.fragment_home_new_theme_Linear_popular_category);

        viewpager_infinite = fragmentView.findViewById(R.id.viewpager_infinite_home);
        viewpager_homebanner = fragmentView.findViewById(R.id.viewpager_infinite_home_banner);
        indicator = fragmentView.findViewById(R.id.indicator);
        indicator_home = fragmentView.findViewById(R.id.indicator_home);

        mSharedPreferencesCount = mainActivity.getSharedPreferences(Tags.TAG_SHARED_PREFERENCE_Cart_Count, 0);
        mEditorCount = mSharedPreferencesCount.edit();

        mSharedPreferencesUserId = mainActivity.getSharedPreferences(Tags.TAG_SHARED_PREFERENCE_USER_ID, 0);
        mEditor = mSharedPreferencesUserId.edit();
        mStringUserid = mSharedPreferencesUserId.getString("UserId", "");

        mSharedPreferencesLoginFirst = mainActivity.getSharedPreferences(Tags.PREFS_NAME, 0);
        mEditorLogin = mSharedPreferencesLoginFirst.edit();
        GuestUser = mSharedPreferencesLoginFirst.getString("GuestUser", "0");

        CommonClass cmn = new CommonClass();
        cmn.FirebaseToken(getActivity());

        if (GuestUser.equalsIgnoreCase("1")) {
            mImageViewNotification.setVisibility(View.GONE);
            mTextViewBageNotification.setVisibility(View.GONE);
            mStringUserid = "00000000-0000-0000-0000-000000000000";

        } else {
            mImageViewNotification.setVisibility(View.VISIBLE);
            mTextViewBageNotification.setVisibility(View.VISIBLE);
//            Tags.mStringDeviceToken = "";
        }

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                currentLocation = locationResult.getLocations().get(0);
                mainActivity.getAddress();
            }
        };

        mSharedPreferencesLocation = mainActivity.getSharedPreferences("location", 0);
        mEditorLocation = mSharedPreferencesLocation.edit();
        String City = mSharedPreferencesLocation.getString("City", "");
        String PostCode = mSharedPreferencesLocation.getString("PostCode", "");

        if (City != null) {
            mImageViewLocation.setVisibility(View.VISIBLE);
            mTextViewLocation.setText(City + "\n" + PostCode);
        } else {
            mLinearLayoutLocation.setVisibility(View.GONE);
        }


        mTextViewUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment currentFrag = mainActivity.getSupportFragmentManager().findFragmentById(R.id.activity_main_container);
                mCurrentFragment = currentFrag.getClass().getName();

                if (!mCurrentFragment.equalsIgnoreCase(Tags.TAG_FRAGMENT_UPLOAD_PRESCRIPTION)) {
                    FragmentUploadPrescription mFragmentUploadPrescription = new FragmentUploadPrescription();
                    mainActivity.addFragment(mFragmentUploadPrescription, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, mFragmentUploadPrescription.getClass().getName());
                }

            }
        });
        mLinearLayoutMedicines.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment currentFrag = mainActivity.getSupportFragmentManager().findFragmentById(R.id.activity_main_container);
                mCurrentFragment = currentFrag.getClass().getName();

                if (!mCurrentFragment.equalsIgnoreCase(Tags.TAG_FRAGMENT_MEDICINES)) {
                    FragmentMedicines fragmentCart = new FragmentMedicines();
                    mainActivity.addFragment(fragmentCart, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, fragmentCart.getClass().getName());
                }


//                Intent mIntent = new Intent(mainActivity, MapsActivity.class);
//                startActivity(mIntent);
            }
        });

        mLinearLayoutLabTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment currentFrag = mainActivity.getSupportFragmentManager().findFragmentById(R.id.activity_main_container);
                mCurrentFragment = currentFrag.getClass().getName();

                if (!mCurrentFragment.equalsIgnoreCase(Tags.TAG_FRAGEMNT_ARTICLES)) {
                  //  FragmentLabTest fragmentLabTest = new FragmentLabTest();
                   // mainActivity.addFragment(fragmentLabTest, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, fragmentLabTest.getClass().getName());
                }
            }
        });

        mLinearLayoutMyPrescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment currentFrag = mainActivity.getSupportFragmentManager().findFragmentById(R.id.activity_main_container);
                mCurrentFragment = currentFrag.getClass().getName();

                if (!mCurrentFragment.equalsIgnoreCase(Tags.TAG_FRAGMENT_UPLOAD_PRESCRIPTION)) {
                    FragmentUploadPrescription mFragmentUploadPrescription = new FragmentUploadPrescription();
                    mainActivity.addFragment(mFragmentUploadPrescription, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, mFragmentUploadPrescription.getClass().getName());
                }
            }
        });

        mImageViewClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditTextSearch.setText("");
                mStringSearch = "";

                mRecyclerViewIntelignt.setVisibility(View.GONE);
                mRecyclerViewData.setVisibility(View.GONE);
                mTextViewNoData.setVisibility(View.GONE);
                mRelativeLayoutMain.setVisibility(View.VISIBLE);
                mImageViewClear.setVisibility(View.GONE);
//                new GetSearchItemFromDB().execute();
            }
        });

        mEditTextSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    FragmentSearchList fragmentSearchList = new FragmentSearchList();
                    Bundle mBundle = new Bundle();
                    mBundle.putString("SearchText", mEditTextSearch.getText().toString());
                    fragmentSearchList.setArguments(mBundle);
                    mainActivity.addFragment(fragmentSearchList, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, fragmentSearchList.getClass().getName());
                    return true;
                }
                return false;
            }
        });

        mEditTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

                if (charSequence.length() == 0) {
                    mImageViewClear.setVisibility(View.GONE);
                } else {
                    mImageViewClear.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
//                if (charSequence.length() == 0) {
//                    mListViewSuggestion.setVisibility(View.VISIBLE);
//                    mImageViewClear.setVisibility(View.GONE);
//                    mTextViewNoData.setVisibility(View.GONE);
//                } else {
//                    mListViewSuggestion.setVisibility(View.GONE);
//                    mImageViewClear.setVisibility(View.VISIBLE);
//                }

                if (charSequence.length() >= 3) {
                    pgbProductList.setVisibility(View.VISIBLE);
                    GetSearchData();
                    mRecyclerViewIntelignt.setVisibility(View.VISIBLE);
                    mRelativeLayoutMain.setVisibility(View.GONE);

                } else {
                    mTextViewNoData.setVisibility(View.GONE);
                    pgbProductList.setVisibility(View.GONE);
                    mRecyclerViewIntelignt.setVisibility(View.GONE);
                    mRelativeLayoutMain.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

//        mEditTextSearch.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//
//                mListViewSuggestion.setVisibility(View.VISIBLE);
//                mListViewIntelligent.setVisibility(View.GONE);
//                mTextViewNoData.setVisibility(View.GONE);
//
////                new GetSearchItemFromDB().execute();
//
//                return false;
//            }
//        });

        mTextViewBageCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                FragmentCart fragmentCart = new FragmentCart();
                mainActivity.addFragment(fragmentCart, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, fragmentCart.getClass().getName());

            }
        });

        mTextViewBageNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                FragmentNotification fragmentNotification = new FragmentNotification();
                mainActivity.addFragment(fragmentNotification, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, fragmentNotification.getClass().getName());

            }
        });

        mImageViewCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                FragmentCart fragmentCart = new FragmentCart();
                mainActivity.addFragment(fragmentCart, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, fragmentCart.getClass().getName());

            }
        });

        mImageViewNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                FragmentNotification fragmentNotification = new FragmentNotification();
                mainActivity.addFragment(fragmentNotification, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, fragmentNotification.getClass().getName());

            }
        });


        mTextViewSeeAllCategory1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentMedicineCategoryList fragmentMedicineCategoryList = new FragmentMedicineCategoryList();
                Bundle mBundle = new Bundle();
                mBundle.putString("Category_id", mStringCategoryId1);
                mBundle.putString("Category_Name", mStringCategoryName1);
                fragmentMedicineCategoryList.setArguments(mBundle);
                mainActivity.addFragment(fragmentMedicineCategoryList, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, fragmentMedicineCategoryList.getClass().getName());
            }
        });


        mTextViewSeeAllCategory2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentMedicineCategoryList fragmentMedicineCategoryList = new FragmentMedicineCategoryList();
                Bundle mBundle = new Bundle();
                mBundle.putString("Category_id", mStringCategoryId2);
                mBundle.putString("Category_Name", mStringCategoryName2);
                fragmentMedicineCategoryList.setArguments(mBundle);
                mainActivity.addFragment(fragmentMedicineCategoryList, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, fragmentMedicineCategoryList.getClass().getName());
            }
        });

        mTextViewSeeAllCategory3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentMedicineCategoryList fragmentMedicineCategoryList = new FragmentMedicineCategoryList();
                Bundle mBundle = new Bundle();
                mBundle.putString("Category_id", mStringCategoryId3);
                mBundle.putString("Category_Name", mStringCategoryName3);
                fragmentMedicineCategoryList.setArguments(mBundle);
                mainActivity.addFragment(fragmentMedicineCategoryList, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, fragmentMedicineCategoryList.getClass().getName());
            }
        });

        mTextViewSeeAllCategory4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentMedicineCategoryList fragmentMedicineCategoryList = new FragmentMedicineCategoryList();
                Bundle mBundle = new Bundle();
                mBundle.putString("Category_id", mStringCategoryId4);
                mBundle.putString("Category_Name", mStringCategoryName4);
                fragmentMedicineCategoryList.setArguments(mBundle);
                mainActivity.addFragment(fragmentMedicineCategoryList, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, fragmentMedicineCategoryList.getClass().getName());
            }
        });

        return fragmentView;
    }

    @Override
    public void onStart() {
        super.onStart();
        GetHomeCount();
    }

    public void GetSearchData() {

        getLoaderManager().restartLoader(0, null, new LoaderManager.LoaderCallbacks<TaskExecutor>() {
            @Override
            public Loader<TaskExecutor> onCreateLoader(int arg0, Bundle arg1) {

                SearchMedicine searchMedicine = new SearchMedicine(mainActivity, null);
                return new LoaderTask<TaskExecutor>(mainActivity, searchMedicine);
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
                            if (mServerResponseVOSearch.getStatus().equalsIgnoreCase("true")) {
//                            Snackbar.with(mainActivity).text(mServerResponseVO.getMsg()).show(mainActivity);
//                                mCommonHelper.hideKeyboard(mainActivity);
                                mainActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                                if (mServerResponseVOSearch.getData() != null) {

                                    mArrayListSearch = (ArrayList<MedicineSearchList>) mServerResponseVOSearch.getData();
                                    if (mArrayListSearch != null && mArrayListSearch.size() > 0) {

                                        mRecyclerViewIntelignt.setVisibility(View.VISIBLE);
                                        mRelativeLayoutMain.setVisibility(View.GONE);
                                        pgbProductList.setVisibility(View.GONE);
                                        mTextViewNoData.setVisibility(View.GONE);

                                        SearchHomeDataAdapter searchAdapter = new SearchHomeDataAdapter(mainActivity, mArrayListSearch);
                                        mRecyclerViewIntelignt.setHasFixedSize(true);
                                        mRecyclerViewIntelignt.setLayoutManager(new LinearLayoutManager(mainActivity));
                                        mRecyclerViewIntelignt.setAdapter(searchAdapter);

                                    }
                                }
                            } else {

                                mTextViewNoData.setVisibility(View.VISIBLE);
                                mRecyclerViewIntelignt.setVisibility(View.GONE);
//                                mCommonHelper.hideKeyboard(mainActivity);
                            }
                            pgbProductList.setVisibility(View.GONE);
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
    public class SearchMedicine extends TaskExecutor {
        protected SearchMedicine(Context context, Bundle bundle) {
            super(context, bundle);
        }

        @Override
        protected Object executeTask() {
            mStringSearch = mEditTextSearch.getText().toString().trim();
            SearchMedicineInfoService getAllAddressInfoService = new SearchMedicineInfoService();
            mServerResponseVOSearch = getAllAddressInfoService.fetchLoginUserInformation(mainActivity, Tags.SearchMedicine, mStringSearch, mStringUserid, Tags.mStringDeviceToken);

            return null;
        }
    }

    public class SearchHomeDataAdapter extends RecyclerView.Adapter<SearchHomeDataAdapter.MyViewHolderHomesSearch> {
        Context mContext;
        ArrayList<MedicineSearchList> mArrayList;


        public SearchHomeDataAdapter(MainActivity mainActivity, ArrayList<MedicineSearchList> mArrayListSearch) {
            this.mContext = mainActivity;
            this.mArrayList = mArrayListSearch;
        }

        @NonNull
        @Override
        public MyViewHolderHomesSearch onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_suggestion, parent, false);
            MyViewHolderHomesSearch myViewHolderSearch = new MyViewHolderHomesSearch(v);
            return myViewHolderSearch;
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull final MyViewHolderHomesSearch holder, int position) {

            final int pos = position;
            holder.mTextViewMdicineName.setText(mArrayList.get(pos).getMedicineName());

            if (mArrayList.get(pos).getUnitsPerPack() != null && !mArrayList.get(pos).getUnitsPerPack().equalsIgnoreCase("null")) {
                holder.mTextViewPackTitle.setText("Pack :");
                holder.mTextViewPack.setText(mArrayList.get(pos).getUnitsPerPack());
            }


            if (mArrayList.get(pos).getPrice() != null && !mArrayList.get(pos).getPrice().equalsIgnoreCase("null")) {
                holder.mTextViewPriceVlaue.setText(getResources().getString(R.string.price) + " " + Tags.LABEL_CURRENCY_SIGN + Utility.getDecimalFormate(mainActivity, Double.parseDouble(mArrayList.get(pos).getPrice()), 0.0, Tags.DECIMAL_FORMAT));
            } else {
                holder.mTextViewPriceVlaue.setText(getResources().getString(R.string.price) + " " + Tags.LABEL_CURRENCY_SIGN + Utility.getDecimalFormate(mainActivity, Double.parseDouble(mArrayList.get(pos).getMrp()), 0.0, Tags.DECIMAL_FORMAT));

            }


            if (mArrayList.get(pos).getMrp().equals("null") || mArrayList.get(pos).getMrp().equals("0")) {
                holder.mTextViewDiscountPrice.setVisibility(View.GONE);
            } else {
                holder.mTextViewDiscountPrice.setVisibility(View.VISIBLE);
                holder.mTextViewDiscountPrice.setText(Tags.LABEL_CURRENCY_SIGN + Utility.getDecimalFormate(mainActivity, Double.parseDouble(mArrayList.get(pos).getMrp()), 0.0, Tags.DECIMAL_FORMAT));
                holder.mTextViewDiscountPrice.setPaintFlags(holder.mTextViewDiscountPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }

            if (mArrayList.get(pos).getPrice().equalsIgnoreCase(mArrayList.get(pos).getMrp()) || mArrayList.get(pos).getPrice().equalsIgnoreCase("null")) {
                holder.mTextViewDiscountPrice.setVisibility(View.GONE);
            }


            if (mArrayList.get(pos).getStockAvailablity().equalsIgnoreCase("true")) {
                holder.mTextViewBuy.setVisibility(View.VISIBLE);
                holder.mTextViewOutOfStok.setVisibility(View.GONE);
            } else {
                holder.mTextViewBuy.setVisibility(View.GONE);
                holder.mTextViewOutOfStok.setVisibility(View.VISIBLE);
            }
            holder.mTextViewBuy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.mTextViewBuy.setVisibility(View.GONE);
                    holder.mSpinner.setVisibility(View.VISIBLE);
                    mAttributeAdapter = new ArrayAdapter<String>(mainActivity, android.R.layout.simple_spinner_item, mArrayListSpinner);
                    mAttributeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    holder.mSpinner.setAdapter(mAttributeAdapter);


                    mStringMedicineId = mArrayList.get(pos).getMedicineId();
                    mStringqty = String.valueOf(holder.mSpinner.getSelectedItem());
                    mStringPrice = mArrayList.get(pos).getPrice();

                    AddCartData();

                }
            });

            holder.mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                    if (parent.getItemAtPosition(position).equals("1")) {

                    } else {
                        holder.mSpinner.getSelectedItem();

                        mStringMedicineId = mArrayList.get(pos).getMedicineId();
                        mStringqty = String.valueOf(holder.mSpinner.getSelectedItem());
                        mStringPrice = mArrayList.get(pos).getPrice();

                        AddCartData();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

            if (mArrayList.get(pos).getImagePath() != null && !mArrayList.get(pos).getImagePath().equalsIgnoreCase("null")) {

                Glide.with(mContext).load(mArrayList.get(pos).getImagePath())
                        .placeholder(R.drawable.ic_med)
                        .into(holder.mImageViewMedicine);
            } else {
                Glide.with(mContext)
                        .load(R.drawable.ic_med)
                        .into(holder.mImageViewMedicine);
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentProductDeatils fragmentProductDeatils = new FragmentProductDeatils();
                    Bundle mBundle = new Bundle();
                    mBundle.putString("medicineid", mArrayList.get(pos).getMedicineId());
                    fragmentProductDeatils.setArguments(mBundle);
                    mainActivity.addFragment(fragmentProductDeatils, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, fragmentProductDeatils.getClass().getName());
                }
            });
        }


        @Override
        public int getItemCount() {
            return mArrayList.size();
        }

        public class MyViewHolderHomesSearch extends RecyclerView.ViewHolder {

            ImageView mImageViewMedicine;
            TextView mTextViewMdicineName;
            TextView mTextViewPriceVlaue;
            TextView mTextViewDiscountPrice;
            TextView mTextViewBuy;
            Spinner mSpinner;
            TextView mTextViewPack;
            TextView mTextViewPackTitle;
            TextView mTextViewOutOfStok;

            public MyViewHolderHomesSearch(View itemView) {
                super(itemView);

                mImageViewMedicine = itemView.findViewById(R.id.row_suggestion_imageview);
                mTextViewMdicineName = itemView.findViewById(R.id.row_suggestion_textview__medicine_name);
                mTextViewPriceVlaue = itemView.findViewById(R.id.row_suggestion_textview_price_value);
                mTextViewDiscountPrice = itemView.findViewById(R.id.row_suggestion_textview_price_discount);
                mTextViewBuy = itemView.findViewById(R.id.row_suggestion_textview_buy);
                mSpinner = itemView.findViewById(R.id.row_suggestion_spinner);
                mTextViewPack = itemView.findViewById(R.id.row_suggestion_textview_pack);
                mTextViewOutOfStok = itemView.findViewById(R.id.row_suggestion_textview_out_of_stock);
                mTextViewPackTitle = itemView.findViewById(R.id.row_suggestion_textview_pack_text);
            }
        }
    }

    public void AddCartData() {

        getLoaderManager().restartLoader(0, null, new LoaderManager.LoaderCallbacks<TaskExecutor>() {
            @Override
            public Loader<TaskExecutor> onCreateLoader(int arg0, Bundle arg1) {
                AddCartExecutor updateCartQty = new AddCartExecutor(mainActivity, null);
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
                        if (mServerResponseVOCard != null && mServerResponseVOCard.getStatus() != null) {
                            try {
                                if (mServerResponseVOCard.getStatus().equalsIgnoreCase("true")) {

                                    if (mServerResponseVOCard.getCartCount() != null) {
                                        Snackbar.with(mainActivity).text(mServerResponseVOCard.getMsg()).show(mainActivity);
                                        if (mServerResponseVOCard.getCartCount().equalsIgnoreCase("0")) {
                                            mTextViewBageCart.setVisibility(View.GONE);

                                            mEditorCount.putString(Tags.COUNT, mServerResponseVOCard.getCartCount());
                                            mEditorCount.apply();
                                        } else {
                                            mTextViewBageCart.setVisibility(View.VISIBLE);
                                            mTextViewBageCart.setText(mServerResponseVOCard.getCartCount());

//                                            mLinearLayoutQty.setVisibility(View.VISIBLE);
//                                            mTextViewAddtocart.setVisibility(View.GONE);
                                            mEditorCount.putString(Tags.COUNT, mServerResponseVOCard.getCartCount());
                                            mEditorCount.apply();
                                        }


                                    }
                                } else {
                                    Snackbar.with(mainActivity).text(mServerResponseVOCard.getMsg()).show(mainActivity);
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


    public class AddCartExecutor extends TaskExecutor {


        protected AddCartExecutor(Context context, Bundle bundle) {
            super(context, bundle);
        }

        @Override
        protected Object executeTask() {

            AddCartInfoService addCartInfoService = new AddCartInfoService();
            try {
                mServerResponseVOCard = addCartInfoService.AddCartinfo(mainActivity, Tags.AddCard, "", mStringUserid, mStringPrice, mStringqty, mStringMedicineId, Tags.mStringDeviceToken);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    //  get home count
    public void GetHomeCount() {
        getLoaderManager().restartLoader(0, null, new LoaderManager.LoaderCallbacks<TaskExecutor>() {
            @Override
            public Loader<TaskExecutor> onCreateLoader(int arg0, Bundle arg1) {

                GetHomeCountData searchMedicine = new GetHomeCountData(mainActivity, null);
                return new LoaderTaskWithoutProgressDialog<TaskExecutor>(mainActivity, searchMedicine);
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
                            if (mServerResponseVOCount.getStatus().equalsIgnoreCase("true")) {
//                            Snackbar.with(mainActivity).text(mServerResponseVO.getMsg()).show(mainActivity);
                                mCommonHelper.hideKeyboard(mainActivity);
                                if (mServerResponseVOCount.getData() != null) {
                                    if (mServerResponseVOCount.getCartCount().equalsIgnoreCase("0")) {
                                        mTextViewBageCart.setVisibility(View.GONE);
                                        mEditorCount.putString(Tags.COUNT, mServerResponseVOCount.getCartCount());
                                        mEditorCount.apply();
                                    } else {
                                        mTextViewBageCart.setVisibility(View.VISIBLE);
                                        mTextViewBageCart.setText(mServerResponseVOCount.getCartCount());

                                        mEditorCount.putString(Tags.COUNT, mServerResponseVOCount.getCartCount());
                                        mEditorCount.apply();
                                    }

                                    if (mServerResponseVOCount.getNotiCount().equalsIgnoreCase("0")) {
                                        mTextViewBageNotification.setVisibility(View.GONE);
                                        mEditorCount.putString(Tags.NOTICOUNT, mServerResponseVOCount.getNotiCount());
                                        mEditorCount.apply();
                                    } else {
                                        if (GuestUser.equalsIgnoreCase("1")) {
                                            mTextViewBageNotification.setVisibility(View.GONE);
                                            mTextViewBageNotification.setVisibility(View.GONE);
                                        } else {
                                            mTextViewBageNotification.setVisibility(View.VISIBLE);
                                            mTextViewBageNotification.setText(mServerResponseVOCount.getNotiCount());

                                            mEditorCount.putString(Tags.NOTICOUNT, mServerResponseVOCount.getNotiCount());
                                            mEditorCount.apply();
                                        }
                                    }

//                                    GetAllBannerMenu();
                                    GetCategoryList();
                                }
                            } else {
                                Snackbar.with(mainActivity).text(mServerResponseVOCount.getMsg()).show(mainActivity);
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
    public class GetHomeCountData extends TaskExecutor {
        protected GetHomeCountData(Context context, Bundle bundle) {
            super(context, bundle);
        }

        @Override
        protected Object executeTask() {
            GetCountDataService getAllAddressInfoService = new GetCountDataService();
            mServerResponseVOCount = getAllAddressInfoService.getdatacount(mainActivity, Tags.GETCOUNT, mStringUserid, Tags.mStringDeviceToken);

            return null;
        }
    }

    public static class DownloadService extends IntentService {

        private static final String TAG = "DownloadService";

        @Override
        public void onCreate() {
            super.onCreate();
        }

        @Override
        protected void onHandleIntent(@Nullable Intent intent) {
        }

        public DownloadService(String name) {
            super(name);
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

//        if (viewpager_infinite != null) {
//            viewpager_infinite.startAutoScroll();
//            viewpager_infinite.setAutoScrollTime(3000);
//        }
//        GetHomeCount();
        onResumeData();
    }

    public void onResumeData() {


    }

    @Override
    public void onPause() {
        super.onPause();

        mainActivity.getSharedPreferences(Tags.TAG_SHARED_PREFERENCE_Cart_Count, 0).edit().clear().apply();
//        if (viewpager_infinite != null) {
//            viewpager_infinite.stopAutoScroll();
//        }
    }


    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mCommonHelper.freeMemory();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public class MedicinesCategoryAdapter extends RecyclerView.Adapter<MedicinesCategoryAdapter.MyViewHolder> {

        ArrayList<CategoryList> mArrayList;
        Context mContext;


        public MedicinesCategoryAdapter(MainActivity mainActivity, ArrayList<CategoryList> mArrayListCategory) {
            this.mContext = mainActivity;
            this.mArrayList = mArrayListCategory;
        }

        @Override
        public MedicinesCategoryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_category_dashboard, parent, false);
            MedicinesCategoryAdapter.MyViewHolder vh = new MedicinesCategoryAdapter.MyViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(@NonNull MedicinesCategoryAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {

            holder.mTextViewCategoryName.setText(mArrayList.get(position).getMedicineCategoryName());
            if (mArrayList.get(position).getImagePath() != null) {

                Glide.with(mContext).load(mArrayList.get(position).getImagePath())
                        .placeholder(R.drawable.ic_med)
                        .into(holder.mImageViewCategoryIamge);
            } else {
                Glide.with(mContext)
                        .load(R.drawable.ic_med)
                        .into(holder.mImageViewCategoryIamge);
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentMedicineCategoryList fragmentMedicineCategoryList = new FragmentMedicineCategoryList();
                    Bundle mBundle = new Bundle();
                    mBundle.putString("Category_id", mArrayList.get(position).getMedicineCategoryId());
                    mBundle.putString("Category_Name",  mArrayList.get(position).getMedicineCategoryName());
                    fragmentMedicineCategoryList.setArguments(mBundle);
                    mainActivity.addFragment(fragmentMedicineCategoryList, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, fragmentMedicineCategoryList.getClass().getName());
                }
            });

        }


        @Override
        public int getItemCount() {
            return mArrayList.size();
        }


        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView mTextViewCategoryName;
            ImageView mImageViewCategoryIamge;

            public MyViewHolder(View itemView) {
                super(itemView);
                mImageViewCategoryIamge = (ImageView) itemView.findViewById(R.id.fragment_row_Dashboardcategory_imageview_categoryimage);
                mTextViewCategoryName = (TextView) itemView.findViewById(R.id.fragment_row_Dashboardcategory_textview_categoryname);
            }
        }
    }


    public void GetCategoryList() {

        getLoaderManager().restartLoader(0, null, new LoaderManager.LoaderCallbacks<TaskExecutor>() {
            @Override
            public Loader<TaskExecutor> onCreateLoader(int arg0, Bundle arg1) {

                GetCategoryList getCategoryList = new GetCategoryList(mainActivity, null);
                return new LoaderTask<TaskExecutor>(mainActivity, getCategoryList);
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

                                    mCategoryVo = (DashboardCategoryVO) mServerResponseVO.getData();

                                    if (mCategoryVo != null) {
                                        mDashboardCategoryVos = mCategoryVo.getDashboardMedicineCategories();
                                        if (mDashboardCategoryVos != null && mDashboardCategoryVos.size() > 0) {


                                            if (mDashboardCategoryVos.get(0).getMedicineSearchLists() != null) {
                                                mArrayListCategory1 = mDashboardCategoryVos.get(0).getMedicineSearchLists();
                                                mStringCategoryId1 = mDashboardCategoryVos.get(0).getMedicineCategoryId();
                                                mStringCategoryName1 = mDashboardCategoryVos.get(0).getMedicineCategoryName();
                                                mLinearLayoutCategory1.setVisibility(View.VISIBLE);
                                                mTextViewCategoryNo1.setText(mDashboardCategoryVos.get(0).getMedicineCategoryName());
                                                CategoryAdapter categoryAdapter = new CategoryAdapter(mainActivity, mArrayListCategory1);
                                                mRecyclerViewCategory1.setHasFixedSize(true);
                                                mRecyclerViewCategory1.setLayoutManager(new LinearLayoutManager(mainActivity, LinearLayoutManager.HORIZONTAL, false));
                                                mRecyclerViewCategory1.setAdapter(categoryAdapter);
                                            }
                                            if (mDashboardCategoryVos.get(1).getMedicineSearchLists() != null) {
                                                mArrayListCategory2 = mDashboardCategoryVos.get(1).getMedicineSearchLists();
                                                mStringCategoryId2 = mDashboardCategoryVos.get(1).getMedicineCategoryId();
                                                mStringCategoryName2 = mDashboardCategoryVos.get(1).getMedicineCategoryName();
                                                mLinearLayoutCategory2.setVisibility(View.VISIBLE);
                                                mTextViewCategoryNo2.setText(mDashboardCategoryVos.get(1).getMedicineCategoryName());
                                                CategoryAdapter categoryAdapter2 = new CategoryAdapter(mainActivity, mArrayListCategory2);
                                                mRecyclerViewCategory2.setHasFixedSize(true);
                                                mRecyclerViewCategory2.setLayoutManager(new LinearLayoutManager(mainActivity, LinearLayoutManager.HORIZONTAL, false));
                                                mRecyclerViewCategory2.setAdapter(categoryAdapter2);
                                            }
                                            if (mDashboardCategoryVos.size() == 3) {
                                                if (mDashboardCategoryVos.get(2).getMedicineSearchLists() != null) {
                                                    mArrayListCategory3 = mDashboardCategoryVos.get(2).getMedicineSearchLists();
                                                    mStringCategoryId3 = mDashboardCategoryVos.get(2).getMedicineCategoryId();
                                                    mStringCategoryName3 = mDashboardCategoryVos.get(2).getMedicineCategoryName();
                                                    mLinearLayoutCategory3.setVisibility(View.VISIBLE);
                                                    mTextViewCategoryNo3.setText(mDashboardCategoryVos.get(2).getMedicineCategoryName());
                                                    CategoryAdapter categoryAdapter3 = new CategoryAdapter(mainActivity, mArrayListCategory3);
                                                    mRecyclerViewCategory3.setHasFixedSize(true);
                                                    mRecyclerViewCategory3.setLayoutManager(new LinearLayoutManager(mainActivity, LinearLayoutManager.HORIZONTAL, false));
                                                    mRecyclerViewCategory3.setAdapter(categoryAdapter3);
                                                }
                                            }
                                            if (mDashboardCategoryVos.size() == 4) {
                                                if (mDashboardCategoryVos.get(3).getMedicineSearchLists() != null) {
                                                    mArrayListCategory4 = mDashboardCategoryVos.get(3).getMedicineSearchLists();
                                                    mStringCategoryId4 = mDashboardCategoryVos.get(3).getMedicineCategoryId();
                                                    mStringCategoryName4 = mDashboardCategoryVos.get(3).getMedicineCategoryName();
                                                    mLinearLayoutCategory4.setVisibility(View.VISIBLE);
                                                    mTextViewCategoryNo4.setText(mDashboardCategoryVos.get(3).getMedicineCategoryName());
                                                    CategoryAdapter categoryAdapter4 = new CategoryAdapter(mainActivity, mArrayListCategory4);
                                                    mRecyclerViewCategory4.setHasFixedSize(true);
                                                    mRecyclerViewCategory4.setLayoutManager(new LinearLayoutManager(mainActivity, LinearLayoutManager.HORIZONTAL, false));
                                                    mRecyclerViewCategory4.setAdapter(categoryAdapter4);
                                                }
                                            }
                                        }

                                        mArrayListPopularCategory = mCategoryVo.getmCategoryLists();
                                        if (mArrayListPopularCategory != null && mArrayListPopularCategory.size() > 0) {
                                            mLinearLayoutPopularCategory.setVisibility(View.VISIBLE);
                                            GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 3);
                                            mRecyclerViewPopularCategory.setLayoutManager(gridLayoutManager);
                                            MedicinesCategoryAdapter customAdapter = new MedicinesCategoryAdapter(mainActivity, mArrayListPopularCategory);
                                            mRecyclerViewPopularCategory.setAdapter(customAdapter);

                                        }
                                        getAllBannerMenuVOS = mCategoryVo.getGetAllBannerMenuVOS();
                                        getAllBannerHomeVOS = mCategoryVo.getGetMainBannerMenuVOS();
                                        if (getAllBannerMenuVOS != null && getAllBannerMenuVOS.size() > 0) {

                                            for (int i = 0; i < getAllBannerMenuVOS.size(); i++) {
                                                if (getAllBannerMenuVOS.get(i).getBanner() != null) {
                                                    if (getAllBannerMenuVOS.size() == 1) {

                                                        CustomAdapterInfinite   mCustomAdapterInfinite = new CustomAdapterInfinite(mainActivity, getAllBannerMenuVOS);
                                                        viewpager_infinite.setAdapter(mCustomAdapterInfinite);
                                                        viewpager_infinite.stopAutoScroll();

                                                    }
                                                    else
                                                    {
                                                        CustomAdapterInfinite   mCustomAdapterInfinite = new CustomAdapterInfinite(mainActivity, getAllBannerMenuVOS);
                                                        viewpager_infinite.setAdapter(mCustomAdapterInfinite);
                                                        indicator.setViewPager(viewpager_infinite);

                                                        if (viewpager_infinite != null) {
                                                            viewpager_infinite.startAutoScroll();
                                                            viewpager_infinite.setAutoScrollTime(3000);
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        if (getAllBannerHomeVOS != null && getAllBannerHomeVOS.size() > 0) {

                                            for (int i = 0; i < getAllBannerHomeVOS.size(); i++) {
                                                if (getAllBannerHomeVOS.get(i).getBanner() != null) {
                                                    if (getAllBannerHomeVOS.size() == 1) {

                                                        CustomAdapterInfinite   mCustomAdapterInfinite = new CustomAdapterInfinite(mainActivity, getAllBannerHomeVOS);
                                                        viewpager_homebanner.setAdapter(mCustomAdapterInfinite);
                                                        viewpager_homebanner.stopAutoScroll();

                                                    }
                                                    else
                                                    {
                                                        CustomAdapterInfinite   mCustomAdapterInfinite = new CustomAdapterInfinite(mainActivity, getAllBannerHomeVOS);
                                                        viewpager_homebanner.setAdapter(mCustomAdapterInfinite);
                                                        indicator_home.setViewPager(viewpager_homebanner);

                                                        if (viewpager_homebanner != null) {
                                                            viewpager_homebanner.startAutoScroll();
                                                            viewpager_homebanner.setAutoScrollTime(3000);
                                                        }
                                                    }
                                                }
                                            }
                                        }

                                    }
                                }
                            } else {
                                Snackbar.with(mainActivity).text(mServerResponseVO.getMsg()).show(mainActivity);
                                mCommonHelper.hideKeyboard(mainActivity);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
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
    public class GetCategoryList extends TaskExecutor {
        protected GetCategoryList(Context context, Bundle bundle) {
            super(context, bundle);
        }

        @Override
        protected Object executeTask() {
            GetCategoryListInfoService getCategoryListInfoService = new GetCategoryListInfoService();
            mServerResponseVO = getCategoryListInfoService.GetCategoryList(mainActivity, Tags.GetCategoryList);

            return null;
        }
    }

    //    Category no1 adapter setup
    public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

        Context mContext;
        ArrayList<MedicineSearchList> mArrayList;

        public CategoryAdapter(MainActivity mainActivity, ArrayList<MedicineSearchList> mArrayListCategory1) {
            this.mContext = mainActivity;
            this.mArrayList = mArrayListCategory1;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_home_category_view, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") final int position) {


            if (!mArrayList.get(position).getDiscount().equalsIgnoreCase("null") && !mArrayList.get(position).getDiscount().equalsIgnoreCase("0.0")) {
                holder.mTextViewDiscount.setText(Tags.LABEL_CURRENCY_SIGN + Utility.getDecimalFormate(mainActivity, Double.parseDouble(mArrayList.get(position).getDiscount()), 0.0, Tags.DECIMAL_FORMAT));
            } else {
                holder.mTextViewDiscount.setVisibility(View.GONE);
            }

            if (!mArrayList.get(position).getManufacturer().equalsIgnoreCase("null")) {
                holder.mTextViewMedicineDescription.setText(mArrayList.get(position).getManufacturer());

            } else {
                holder.mTextViewMedicineDescription.setVisibility(View.GONE);
            }

            if (mArrayList.get(position).getTotalReview().equalsIgnoreCase("0.0") ||
                    mArrayList.get(position).getAvgReview() == 0.0 ||
                    mArrayList.get(position).getTotalRating().equalsIgnoreCase("0.0")) {
                holder.mLinearLayoutRaing.setVisibility(View.GONE);
            } else {
                if (!mArrayList.get(position).getTotalReview().equalsIgnoreCase("null")) {
                    holder.mTextViewAvgUser.setText(mArrayList.get(position).getTotalReview());

                } else {
                    holder.mTextViewAvgUser.setVisibility(View.GONE);
                }

                if (mArrayList.get(position).getAvgReview() != 0.0) {
                    holder.mRatingBar.setRating(mArrayList.get(position).getAvgReview());
                } else {

                    holder.mRatingBar.setVisibility(View.GONE);
                }
                if (!mArrayList.get(position).getTotalRating().equalsIgnoreCase("null")) {
                    holder.mTextViewUserRating.setText(" ( " + mArrayList.get(position).getTotalRating() + " rating ) ");

                } else {
                    holder.mTextViewUserRating.setVisibility(View.GONE);
                }
            }

            if (!mArrayList.get(position).getMrp().equalsIgnoreCase("null")) {
                holder.mTextViewMRP.setText(Tags.LABEL_CURRENCY_SIGN + Utility.getDecimalFormate(mainActivity, Double.parseDouble(mArrayList.get(position).getMrp()), 0.0, Tags.DECIMAL_FORMAT));
                holder.mTextViewMRP.setPaintFlags(holder.mTextViewMRP.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                holder.mTextViewMRP.setVisibility(View.GONE);
            }

            if (!mArrayList.get(position).getPrice().equalsIgnoreCase("null")) {
                holder.mTextViewPrice.setText(Tags.LABEL_CURRENCY_SIGN + Utility.getDecimalFormate(mainActivity, Double.parseDouble(mArrayList.get(position).getPrice()), 0.0, Tags.DECIMAL_FORMAT));

            } else {
                holder.mTextViewPrice.setVisibility(View.GONE);
            }

            if (mArrayList.get(position).getPrice().equalsIgnoreCase(mArrayList.get(position).getMrp()) || mArrayList.get(position).getPrice().equalsIgnoreCase("null")) {
                holder.mTextViewMRP.setVisibility(View.GONE);
            }

            if (mArrayList.get(position).getStockAvailablity().equalsIgnoreCase("true")) {

//                if(!mArrayList.get(position).getCartQty().equalsIgnoreCase("0"))
//                {
//                    mLinearLayoutQty.setVisibility(View.VISIBLE);
//                    mEditTextValue.setText(mArrayList.get(position).getCartQty());
//                    mTextViewAddtocart.setVisibility(View.GONE);
//                    holder.mTextViewOutOfStock.setVisibility(View.GONE);
//                }
//                else {
//                    mTextViewAddtocart.setVisibility(View.VISIBLE);
//                    holder.mTextViewOutOfStock.setVisibility(View.GONE);
//                    mLinearLayoutQty.setVisibility(View.GONE);
//                }

              holder. mTextViewAddtocart.setVisibility(View.VISIBLE);
                holder.mTextViewOutOfStock.setVisibility(View.GONE);
                mLinearLayoutQty.setVisibility(View.GONE);
            }
            else {
               holder.mTextViewAddtocart.setVisibility(View.GONE);
                holder.mTextViewOutOfStock.setVisibility(View.VISIBLE);
            }

            holder.mTextViewMedicineName.setText(mArrayList.get(position).getMedicineName());

            if (mArrayList.get(position).getImagePath() != null) {

                Glide.with(mContext).load(mArrayList.get(position).getImagePath())
                        .placeholder(R.drawable.ic_med)
                        .into(holder.mImageViewMedicineImage);
            } else {
                Glide.with(mContext)
                        .load(R.drawable.ic_med)
                        .into(holder.mImageViewMedicineImage);
            }


           holder.mTextViewAddtocart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mStringMedicineId = mArrayList.get(position).getMedicineId();
                    mStringqty = "1";
                    mStringPrice = mArrayList.get(position).getPrice();


                    AddCartData();
                }
            });


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentProductDeatils fragmentProductDeatils = new FragmentProductDeatils();
                    Bundle mBundle = new Bundle();
                    mBundle.putString("medicineid", mArrayList.get(position).getMedicineId());

                    fragmentProductDeatils.setArguments(mBundle);
                    mainActivity.addFragment(fragmentProductDeatils, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, fragmentProductDeatils.getClass().getName());
                }
            });



            mTextViewPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mIntTotalQtyIncrmnt = 30;

                    mStringMedicineId = mArrayList.get(position).getMedicineId();
                    mStringPrice = mArrayList.get(position).getPrice();
                    if (mIntQty < mIntTotalQtyIncrmnt) {

                        if (!mCommonHelper.check_Internet(getActivity())) {
                            Snackbar.with(mainActivity).text(getResources().getString(R.string.interneterror)).show(mainActivity);
                            return;
                        }

                        mIntQty = Integer.parseInt(mEditTextValue.getText().toString()) + 1;
                        UpdateCart();
                        mEditTextValue.setText(String.valueOf(mIntQty));

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
                    mStringMedicineId = mArrayList.get(position).getMedicineId();
                    mIntincrQty = 1;

                    mStringPrice = mArrayList.get(position).getPrice();
                    if (mIntQty == mIntincrQty) {

                       mTextViewMinus.setClickable(false);

                    } else {
                        mIntQty = Integer.parseInt(mEditTextValue.getText().toString()) - 1;

                        mEditTextValue.setText(String.valueOf(mIntQty));

                        UpdateCart();
                    }

                }
            });


        }

        @Override
        public int getItemCount() {
            return mArrayList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            ImageView mImageViewMedicineImage;
            TextView mTextViewMedicineName;
            TextView mTextViewMedicineDescription;
            TextView mTextViewPrice;
            TextView mTextViewDiscount;
            TextView mTextViewMRP;
            RatingBar mRatingBar;
            TextView mTextViewUserRating;
            TextView mTextViewAvgUser;
            TextView mTextViewOutOfStock;
            TextView mTextViewAddtocart;
            LinearLayout mLinearLayoutRaing;

            public ViewHolder(View itemView) {
                super(itemView);
                mImageViewMedicineImage = itemView.findViewById(R.id.row_product_grid_imageview_main);
                mTextViewMedicineName = itemView.findViewById(R.id.row_product_grid_textview_medicine_name);
                mTextViewMedicineDescription = itemView.findViewById(R.id.row_product_grid_textview_medicine_Description);
                mTextViewPrice = itemView.findViewById(R.id.row_product_grid_textview_price);
                mTextViewDiscount = itemView.findViewById(R.id.row_product_grid_textview_dicount);
                mTextViewMRP = itemView.findViewById(R.id.row_product_grid_textview_mrp_price);
                mRatingBar = itemView.findViewById(R.id.row_catlog_ratingBar);
                mTextViewUserRating = itemView.findViewById(R.id.row_product_grid_textview_TotalRating);
                mTextViewAvgUser = itemView.findViewById(R.id.row_product_grid_textview_UserAvg);
                mTextViewAddtocart = itemView.findViewById(R.id.row_product_grid_textview_AddToCart);
                mTextViewOutOfStock = itemView.findViewById(R.id.row_product_grid_textview_outofstock);
                mTextViewMinus = itemView.findViewById(R.id.fragment_product_details_textview_qty_minus);
                mTextViewPlus = itemView.findViewById(R.id.fragment_product_details_textview_qty_plus);
                mEditTextValue = itemView.findViewById(R.id.fragment_product_details_textview_total_qty);
                mLinearLayoutRaing = itemView.findViewById(R.id.row_product_grid_Linear_userRating);
                mLinearLayoutQty = itemView.findViewById(R.id.fragment_product_details_linearlayout_qty);
            }
        }
    }


    public void UpdateCart() {



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
            mServerResponseVOCard = updateCartService.UpdateCartData(mainActivity, Tags.UPDATECARTDATA, mStringUserid, "00000000-0000-0000-0000-000000000000",
                    mStringMedicineId, String.valueOf(mIntQty), mStringPrice,Tags.mStringDeviceToken );

            return null;
        }
    }


}
