package com.vgroyalchemist.controller;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.ResultReceiver;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.security.ProviderInstaller;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.vgroyalchemist.db.DBService;
import com.vgroyalchemist.utils.Snackbar;
import com.vgroyalchemist.utils.SweetAlertDialog;
import com.vgroyalchemist.utils.Tags;
import com.vgroyalchemist.utils.Utility;
import com.vgroyalchemist.views.FragmentAboutUs;
import com.vgroyalchemist.views.FragmentAddAddress;
import com.vgroyalchemist.views.FragmentAddMedicine;
import com.vgroyalchemist.views.FragmentAddVital;
import com.vgroyalchemist.views.FragmentArticles;
import com.vgroyalchemist.views.FragmentArticlesDetails;
import com.vgroyalchemist.views.FragmentBookArticlesList;
import com.vgroyalchemist.views.FragmentChnagePassword;
import com.vgroyalchemist.views.FragmentComplainHistory;
import com.vgroyalchemist.views.FragmentComplainList;
import com.vgroyalchemist.views.FragmentConfirmOrder;
import com.vgroyalchemist.views.FragmentContentFullDescription;
import com.vgroyalchemist.views.FragmentEditProfile;
import com.vgroyalchemist.views.FragmentFeedbackDelivery;
import com.vgroyalchemist.views.FragmentFinalPaymentWebview;
import com.vgroyalchemist.views.FragmentFindSubstitutes;
import com.vgroyalchemist.views.FragmentForgotPassword;
import com.vgroyalchemist.views.FragmentInvoice;
import com.vgroyalchemist.views.FragmentLegal;
import com.vgroyalchemist.views.FragmentLoginScreen;
import com.vgroyalchemist.views.FragmentCart;
import com.vgroyalchemist.views.FragmentMyOrderList;
import com.vgroyalchemist.views.FragmentMyPrescription;
import com.vgroyalchemist.views.FragmentMyPrescriptionFullImage;
import com.vgroyalchemist.views.FragmentMyUploadPrescriptionOrderList;
import com.vgroyalchemist.views.FragmentNotification;
import com.vgroyalchemist.views.FragmentOrderFilter;
import com.vgroyalchemist.views.FragmentOrderReturn;
import com.vgroyalchemist.views.FragmentOrderSummary;
import com.vgroyalchemist.views.FragmentOrderWithPresciption;
import com.vgroyalchemist.views.FragmentPastSchedule;
import com.vgroyalchemist.views.FragmentPaymentDetails;
import com.vgroyalchemist.views.FragmentPreference;

import com.vgroyalchemist.views.FragmentProfile;
import com.vgroyalchemist.views.FragmentRatingReviews;
import com.vgroyalchemist.views.FragmentRatingReviewsDetails;
import com.vgroyalchemist.views.FragmentReferEarn;
import com.vgroyalchemist.views.FragmentResetPassword;
import com.vgroyalchemist.views.FragmentSchedule;
import com.vgroyalchemist.views.FragmentScheduleDetails;
import com.vgroyalchemist.views.FragmentSearchOrderSummary;
import com.vgroyalchemist.views.FragmentSelectAddress;
import com.vgroyalchemist.views.FragmentTabActivity;
import com.vgroyalchemist.views.FragmentTrackOrderLink;
import com.vgroyalchemist.views.FragmentUploadPrescription;
import com.vgroyalchemist.views.FragmentVerificationCode;
import com.vgroyalchemist.views.FragmentVerificationMoblieNo;
import com.vgroyalchemist.views.FragmentVital;
import com.vgroyalchemist.views.FragmentVitalDetails;
import com.vgroyalchemist.views.FragmentVitalShowAllList;
import com.vgroyalchemist.views.FragmentVitalShowTime;
import com.vgroyalchemist.views.FragmentWriteComplain;
import com.vgroyalchemist.views.ParentFragment;
import com.vgroyalchemist.R;
import com.vgroyalchemist.utils.CommonHelper;
import com.vgroyalchemist.vos.GetLoginData;
import com.vgroyalchemist.vos.ServerResponseVO;
import com.vgroyalchemist.webservice.GetAddressIntentService;
import com.vgroyalchemist.webservice.PostParseGet;

import java.util.ArrayList;
import java.util.List;


import static com.vgroyalchemist.views.FragmentLoginScreen.mainActivity;


public class MainActivity extends CustomFragmentActivity implements ProviderInstaller.ProviderInstallListener {

    public SearchView searchView;
    FragmentLoginScreen mFragmentLoginScreen;
    String mCurrentFragment = "";
    public Toolbar toolbar;


    View mActionbarView;
    CommonHelper mCommonHelper;
    ServerResponseVO mServerResponseVO;
    String mStringEmailid;

    private static final int ERROR_DIALOG_REQUEST_CODE = 1;
    private boolean mRetryProviderInstall;

    GetLoginData mGetLoginData;
    PostParseGet mPostParseGet;
    boolean hasLoggedIn;
    String GuestUser;



    //login
    public SharedPreferences mSharedPreferencesLoginFirst;
    public SharedPreferences.Editor editor;

    SharedPreferences mPreferencesPushNotification;
    SharedPreferences.Editor mEditorNoification;
    String mStringPushNotification = "";

    // userdata
    public SharedPreferences mSharedPreferencesUserId;
    public SharedPreferences.Editor mEditor;
    String mStringUserId;

    SharedPreferences mSharedPreferenceRegId;

    public SharedPreferences mSharedPreferencesLocation;
    public SharedPreferences.Editor mEditorLocation;

    SharedPreferences mPreferences;
    SharedPreferences.Editor mEditorNoti;

    public DBService mDBService;

    //    location permission
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    public static String[] PERMISSIONS_READ_SMS = {Manifest.permission.READ_SMS, Manifest.permission.ACCESS_FINE_LOCATION};
    private static final int READ_SMS_PERMISSIONS_REQUEST = 1;


    private boolean locationPermissionGranted;
    LocationManager locationManager;
    String str_PostalCode, str_City;


    public static final int PERMISSION_CALLBACK_CONSTANT = 100;
    public static final int REQUEST_PERMISSION_SETTING = 101;

    private FusedLocationProviderClient fusedLocationClient;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 2;

    private LocationAddressResultReceiver addressResultReceiver;

    private TextView currentAddTv;

    private Location currentLocation;

    private LocationCallback locationCallback;


    String mStringComplainId = "";
    String mStringOrderId = "";
    String mStringMedicineId = "";

    Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mFragmentLoginScreen = new FragmentLoginScreen();
        mCommonHelper = new CommonHelper();
        mServerResponseVO = new ServerResponseVO();
        mGetLoginData = new GetLoginData();
        mPostParseGet = new PostParseGet(MainActivity.this);
        mDBService = new DBService();
        getSupportFragmentManager().addOnBackStackChangedListener(backStackListener);

        mSharedPreferencesLoginFirst = getSharedPreferences(Tags.PREFS_NAME, 0);
        hasLoggedIn = mSharedPreferencesLoginFirst.getBoolean("hasLoggedIn", false);
        GuestUser = mSharedPreferencesLoginFirst.getString("GuestUser", "0");
        mSharedPreferencesUserId = getSharedPreferences(Tags.TAG_SHARED_PREFERENCE_USER_ID, 0);
        mGetLoginData = mPostParseGet.getUserDataObj(MainActivity.this);
        mEditor = mSharedPreferencesUserId.edit();
        mStringUserId = mSharedPreferencesUserId.getString("UserId", "");
        mStringEmailid = mSharedPreferencesUserId.getString("email", "");

        mSharedPreferencesLocation = getSharedPreferences("location", 0);
        mEditorLocation = mSharedPreferencesLocation.edit();

//        mPreferences = getSharedPreferences(Tags.TAG_SHARED_PREFERENCE_PUSH_NOTIFICATION_DATA, 0);
//        mEditor = mPreferences.edit();
//        mStringComplainId = mPreferences.getString("notification_compalinId", "");
//        mStringMedicineId = mPreferences.getString("notification_MedicineId", "");
//        mStringOrderId = mPreferences.getString("notification_OrderId", "");
        mSharedPreferenceRegId = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);

        if (!mSharedPreferenceRegId.getString(Tags.REG_ID, "").equalsIgnoreCase("")) {
            Tags.mStringDeviceToken = mSharedPreferenceRegId.getString(Tags.REG_ID, "");
        }


        // Set a Toolbar to replace the ActionBar.
        toolbar = findViewById(R.id.activity_main_toolbar);
        setSupportActionBar(toolbar);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        //            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        toolbar.setPadding((int) getResources().getDimension(R.dimen._7sdp), toolbar.getPaddingTop(), (int) getResources().getDimension(R.dimen._7sdp), toolbar.getPaddingBottom());


        addressResultReceiver = new LocationAddressResultReceiver(new Handler());

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                currentLocation = locationResult.getLocations().get(0);
                getAddress();
            }
        };

        updateLocationUI();

        enterApplication();

    }

    public void enterApplication() {

        mPreferencesPushNotification = getSharedPreferences(Tags.TAG_SHARED_PREFERENCE_PUSH_NOTIFICATION, 0);
        mSharedPreferenceRegId = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);


        if (mStringPushNotification.equalsIgnoreCase("")) {
            mStringPushNotification = mPreferencesPushNotification.getString(Tags.PUSH_NOTIFICATION_KEY, "Y");
            mEditorNoification = mPreferencesPushNotification.edit();
            mEditorNoification.putString(Tags.PUSH_NOTIFICATION_KEY, mStringPushNotification);
            System.out.println("VT mStringPushNotification --->" + mStringPushNotification);
            mEditorNoification.commit();
        }

        mSharedPreferencesLoginFirst = getSharedPreferences(Tags.PREFS_NAME, 0);
        hasLoggedIn = mSharedPreferencesLoginFirst.getBoolean("hasLoggedIn", false);

//        mPreferences = getSharedPreferences(Tags.TAG_SHARED_PREFERENCE_PUSH_NOTIFICATION_DATA, 0);
//        mEditor = mPreferences.edit();
//        mStringComplainId = mPreferences.getString("notification_compalinId", "");
//        mStringMedicineId = mPreferences.getString("notification_MedicineId", "");
//        mStringOrderId = mPreferences.getString("notification_OrderId", "");


        if (hasLoggedIn) {

            if (!mStringComplainId.equalsIgnoreCase("") && !mStringMedicineId.equalsIgnoreCase("") && !mStringOrderId.equalsIgnoreCase("")) {
                FragmentComplainHistory fragmentComplainHistory = new FragmentComplainHistory();
                Bundle mBundle = new Bundle();
                mBundle.putString("Medicine_id", mStringMedicineId);
                mBundle.putString("Order_id", mStringOrderId);
                mBundle.putString("status", "Open");
                mBundle.putString("complain_id", mStringComplainId);
                fragmentComplainHistory.setArguments(mBundle);
                addFragment(fragmentComplainHistory, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, fragmentComplainHistory.getClass().getName());
                getSharedPreferences(Tags.TAG_SHARED_PREFERENCE_PUSH_NOTIFICATION_DATA, 0).edit().clear().apply();
                return;
            } else {
                clearBackStackZero();
                clearBackStack();
                removeAllBackFragments();
                FragmentTabActivity fragmentTabActivity = new FragmentTabActivity();
                Bundle bundle = new Bundle();
                bundle.putString("fromhome", "Yes");
                fragmentTabActivity.setArguments(bundle);
                addFragment(fragmentTabActivity, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, fragmentTabActivity.getClass().getName());
            }

        } else {

            clearBackStackZero();
            clearBackStack();
            FragmentLoginScreen mFragmentDiscover = new FragmentLoginScreen();
            Bundle args = new Bundle();
            args.putString(getString(R.string.app_name), "");
            mFragmentDiscover.setArguments(args);
            addFragment(mFragmentDiscover, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, mFragmentDiscover.getClass().getName());

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Fragment currentFrag = getSupportFragmentManager().findFragmentById(R.id.activity_main_container);
        if (currentFrag != null) {
            if (currentFrag.getClass().getName() != null) {
                mCurrentFragment = currentFrag.getClass().getName();
            }
        }

        if (requestCode == ERROR_DIALOG_REQUEST_CODE) {
            mRetryProviderInstall = true;
        }


    }

    private FragmentManager.OnBackStackChangedListener backStackListener = new FragmentManager.OnBackStackChangedListener() {

        @Override
        public void onBackStackChanged() {

            String name = "";
            int count = getSupportFragmentManager().getBackStackEntryCount();
            FragmentManager.BackStackEntry backEntry = null;

            Fragment currentFrag = getSupportFragmentManager().findFragmentById(R.id.activity_main_container);
            if (currentFrag != null) {
                if (currentFrag.getClass().getName() != null) {
                    mCurrentFragment = currentFrag.getClass().getName();
                }
            }
            mActionbarView = getLayoutInflater().inflate(R.layout.activity_toolbar, null);
            Utility.debugger("VT on back press name......" + mCurrentFragment + " page count..." + count);
            if (count != 0) {
                backEntry = getSupportFragmentManager().getBackStackEntryAt(count - 1);
                name = backEntry.getName();

                mCommonHelper.hideKeyboard(MainActivity.this);
                Utility.debugger("VT name..." + name);
                if (getSupportFragmentManager().findFragmentByTag(name) != null) {
                    Utility.debugger("VT Status..." + getSupportFragmentManager().findFragmentByTag(name));

                    ((ParentFragment) getSupportFragmentManager().findFragmentByTag(name)).doWork();
                }
                if (getSupportFragmentManager().findFragmentByTag(name) instanceof FragmentLoginScreen ||
                        getSupportFragmentManager().findFragmentByTag(name) instanceof FragmentForgotPassword ||
                        getSupportFragmentManager().findFragmentByTag(name) instanceof FragmentVerificationCode ||
                        getSupportFragmentManager().findFragmentByTag(name) instanceof FragmentResetPassword ||
                        getSupportFragmentManager().findFragmentByTag(name) instanceof FragmentProfile ||
                        getSupportFragmentManager().findFragmentByTag(name) instanceof FragmentEditProfile ||
                        getSupportFragmentManager().findFragmentByTag(name) instanceof FragmentAddAddress ||
                        getSupportFragmentManager().findFragmentByTag(name) instanceof FragmentChnagePassword ||
                        getSupportFragmentManager().findFragmentByTag(name) instanceof FragmentUploadPrescription ||
                        getSupportFragmentManager().findFragmentByTag(name) instanceof FragmentCart ||
                        getSupportFragmentManager().findFragmentByTag(name) instanceof FragmentArticles ||
                        getSupportFragmentManager().findFragmentByTag(name) instanceof FragmentOrderSummary ||
                        getSupportFragmentManager().findFragmentByTag(name) instanceof FragmentMyOrderList ||
                        getSupportFragmentManager().findFragmentByTag(name) instanceof FragmentSchedule ||
                        getSupportFragmentManager().findFragmentByTag(name) instanceof FragmentAddMedicine ||
                        getSupportFragmentManager().findFragmentByTag(name) instanceof FragmentMyPrescription ||
                        getSupportFragmentManager().findFragmentByTag(name) instanceof FragmentFindSubstitutes ||
                        getSupportFragmentManager().findFragmentByTag(name) instanceof FragmentBookArticlesList ||
                        getSupportFragmentManager().findFragmentByTag(name) instanceof FragmentSearchOrderSummary ||
                        getSupportFragmentManager().findFragmentByTag(name) instanceof FragmentRatingReviews ||
                        getSupportFragmentManager().findFragmentByTag(name) instanceof FragmentOrderReturn ||
                        getSupportFragmentManager().findFragmentByTag(name) instanceof FragmentFeedbackDelivery ||
                        getSupportFragmentManager().findFragmentByTag(name) instanceof FragmentTrackOrderLink ||
                        getSupportFragmentManager().findFragmentByTag(name) instanceof FragmentSelectAddress ||
                        getSupportFragmentManager().findFragmentByTag(name) instanceof FragmentConfirmOrder ||
                        getSupportFragmentManager().findFragmentByTag(name) instanceof FragmentRatingReviewsDetails ||
                        getSupportFragmentManager().findFragmentByTag(name) instanceof FragmentAboutUs ||
                        getSupportFragmentManager().findFragmentByTag(name) instanceof FragmentPreference ||
                        getSupportFragmentManager().findFragmentByTag(name) instanceof FragmentArticlesDetails ||
                        getSupportFragmentManager().findFragmentByTag(name) instanceof FragmentNotification ||
                        getSupportFragmentManager().findFragmentByTag(name) instanceof FragmentMyPrescriptionFullImage ||
                        getSupportFragmentManager().findFragmentByTag(name) instanceof FragmentPastSchedule ||
                        getSupportFragmentManager().findFragmentByTag(name) instanceof FragmentScheduleDetails ||
                        getSupportFragmentManager().findFragmentByTag(name) instanceof FragmentOrderFilter ||
                        getSupportFragmentManager().findFragmentByTag(name) instanceof FragmentVerificationMoblieNo ||
                        getSupportFragmentManager().findFragmentByTag(name) instanceof FragmentVital ||
                        getSupportFragmentManager().findFragmentByTag(name) instanceof FragmentVitalShowAllList ||
                        getSupportFragmentManager().findFragmentByTag(name) instanceof FragmentAddVital ||
                        getSupportFragmentManager().findFragmentByTag(name) instanceof FragmentVitalDetails ||
                        getSupportFragmentManager().findFragmentByTag(name) instanceof FragmentVitalShowTime ||
                        getSupportFragmentManager().findFragmentByTag(name) instanceof FragmentLegal ||
                        getSupportFragmentManager().findFragmentByTag(name) instanceof FragmentContentFullDescription ||
                        getSupportFragmentManager().findFragmentByTag(name) instanceof FragmentInvoice ||
                        getSupportFragmentManager().findFragmentByTag(name) instanceof FragmentWriteComplain ||
                        getSupportFragmentManager().findFragmentByTag(name) instanceof FragmentComplainList ||
                        getSupportFragmentManager().findFragmentByTag(name) instanceof FragmentComplainHistory ||
                        getSupportFragmentManager().findFragmentByTag(name) instanceof FragmentPaymentDetails ||
                        getSupportFragmentManager().findFragmentByTag(name) instanceof FragmentFinalPaymentWebview ||
                        getSupportFragmentManager().findFragmentByTag(name) instanceof FragmentReferEarn ||
                        getSupportFragmentManager().findFragmentByTag(name) instanceof FragmentOrderWithPresciption ||
                        getSupportFragmentManager().findFragmentByTag(name) instanceof FragmentMyUploadPrescriptionOrderList

                ) {
                    toolbar.setVisibility(View.GONE);

                } else {
                    toolbar.setVisibility(View.VISIBLE);
                }
            } else if (count == 1) {
                backEntry = getSupportFragmentManager().getBackStackEntryAt(count - 1);
                name = backEntry.getName();
                Utility.debugger("VT Counter Only One..." + name);
                mCommonHelper.hideKeyboard(MainActivity.this);

                ((ParentFragment) getSupportFragmentManager().findFragmentByTag(name)).doWork();

                if (getSupportFragmentManager().findFragmentByTag(name) instanceof FragmentLoginScreen) {
                    toolbar.setVisibility(View.GONE);
                }
            } else {
                if (currentFrag != null) {
                    try {
                        ((ParentFragment) currentFrag).doWork();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if (mCurrentFragment.equalsIgnoreCase(Tags.TAG_FRAGMENT_HOME_NEW_THEME)) {
                    Utility.debugger("VT only Home Page." + mCurrentFragment);
                    setHomePageData();
                }
            }
        }
    };

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {

        super.onPostCreate(savedInstanceState);

    }


    @Override
    public void replaceFragment(Fragment fragment, boolean addToBackStack, int transition, String stringTag) {

        mCommonHelper.checkMemoryHeap();

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.anim_slide_in_left_fragment, R.anim.anim_slide_in_left_fragment);
        ft.replace(R.id.activity_main_container, fragment, stringTag);
        mCurrentFragment = fragment.getClass().getName();
        ft.setTransition(transition);
        if (addToBackStack) {
            ft.addToBackStack(fragment.getClass().getName());
        }
        ft.commit();
    }

    public void replaceCurentFragment(Fragment fragment) {
        String backStateName = fragment.getClass().getName();

        FragmentManager manager = getSupportFragmentManager();
        boolean fragmentPopped = manager.popBackStackImmediate(backStateName, 0);

        if (!fragmentPopped) { //fragment not in back stack, create it.
            FragmentTransaction ft = manager.beginTransaction();
            ft.replace(R.id.activity_main_container, fragment);
            ft.addToBackStack(fragment.getClass().getName());
            ft.commit();
        }
    }

    public void addFragment(Fragment fragment, boolean addToBackStack, int transition, String
            stringTag) {
        mCommonHelper.checkMemoryHeap();

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.anim_slide_in_left_fragment, R.anim.anim_slide_in_left_fragment);
        ft.add(R.id.activity_main_container, fragment, stringTag);
        mCurrentFragment = fragment.getClass().getName();
        ft.setTransition(transition);
        if (addToBackStack) {
            ft.addToBackStack(fragment.getClass().getName());
        }
        ft.commit();
    }

    @Override
    public void addFragmentWithoutAnimation(Fragment fragment, boolean addToBackStack, int transition, String stringTag) {

        mCommonHelper.checkMemoryHeap();

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.activity_main_container, fragment, stringTag);
        mCurrentFragment = fragment.getClass().getName();
        ft.setTransition(transition);
        if (addToBackStack) {
            ft.addToBackStack(fragment.getClass().getName());
        }
        ft.commit();
    }

    @Override
    public void removeFragment(Fragment fragment, String stringTag) {
        mCommonHelper.checkMemoryHeap();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.anim_slide_out_right_fragment, R.anim.anim_slide_out_right_fragment);
        ft.remove(fragment);
        mCurrentFragment = fragment.getClass().getName();
        ft.commit();
    }

    public void removeAllBackFragments() {
        FragmentManager manager = getSupportFragmentManager();
        if (manager.getBackStackEntryCount() > 0) {
            FragmentManager.BackStackEntry first = manager.getBackStackEntryAt(0);
            manager.popBackStack(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
            removeCurrentFragment();
        }
    }


    public void ReloadFragment(Fragment fragment, boolean addToBackStack, int transition, String
            stringTag) {

        mCommonHelper.checkMemoryHeap();

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.anim_slide_in_left_fragment, R.anim.anim_slide_in_left_fragment);
        ft.detach(fragment);
        ft.attach(fragment);
//        ft.add(R.id.activity_main_container, fragment, stringTag);
        mCurrentFragment = fragment.getClass().getName();
        ft.setTransition(transition);
        if (addToBackStack) {
            ft.addToBackStack(fragment.getClass().getName());
        }
        ft.commit();
    }


    public void removeCurrentFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Fragment currentFrag = getSupportFragmentManager().findFragmentById(R.id.activity_main_container);
        String fragName = "NONE";
        if (currentFrag != null)
            fragName = currentFrag.getClass().getSimpleName();
        if (currentFrag != null)
            transaction.remove(currentFrag);
        transaction.commit();
    }

    public void setHomePageData() {

        toolbar.setVisibility(View.VISIBLE);

        clearBackStackZero();
        removeAllBackFragments();
//        checkCartCount();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mCommonHelper.hideKeyboard(MainActivity.this);

    }


    @Override
    public void onBackPressed() {
        FragmentManager manager = getSupportFragmentManager();
        Fragment currentFrag = getSupportFragmentManager().findFragmentById(R.id.activity_main_container);

        if (currentFrag != null) {
            if (currentFrag.getClass().getName() != null) {
                mCurrentFragment = currentFrag.getClass().getName();
            }
        }
        Utility.debugger("VT on back mCurrentFragment..." + mCurrentFragment);


        if (mCurrentFragment.equalsIgnoreCase(Tags.TAG_FRAGMENT_LOGIN)) {

            if (((FragmentLoginScreen) getSupportFragmentManager().findFragmentByTag(Tags.TAG_FRAGMENT_LOGIN)).getInnerFlag()) {
                super.onBackPressed();
                return;
            } else {
                callExitAppSweetAlertDialog();
                return;
            }

        } else if (mCurrentFragment.equalsIgnoreCase(Tags.TAG_FRAGMENT_TAB)) {


//            int count = getSupportFragmentManager().getBackStackEntryCount();
//
//            if (count == 0) {
//                super.onBackPressed();
//                //additional code
//                callExitAppSweetAlertDialog();
//            } else {
//                getSupportFragmentManager().popBackStack();
//            }

//            if (Tags.bln_HomePage) {
//
//                if (Tags.bln_Page_Not_Leftmenu) {
//                    --Tags.CounterHome;
//                    if (Tags.CounterHome < 1) {
//                        Tags.CounterHome = 0;
//                        Tags.bln_Page_Not_Leftmenu = false;
//                        getSupportFragmentManager().popBackStack();
////                        MultipleCategoryPass_OnBackevent();
//                    } else {
//
//                        Tags.bln_HomePage = true;
//                        getSupportFragmentManager().popBackStack();
//                    }
//                }
//                else {
//                    --Tags.CounterHome;
//                    MultipleCategoryPass_OnBackevent();
//                }
//            }
////            else {
//                callExitAppSweetAlertDialog();
//            }


            if (getSupportFragmentManager().getBackStackEntryCount() >= 0) {
                callExitAppSweetAlertDialog();
            } else {
                super.onBackPressed();
            }
            return;
        } else if (mCurrentFragment.equalsIgnoreCase(Tags.TAG_FRAGMENT_CONFIRMORDER)) {

            removeAllBackFragments();
            clearBackStackZero();

            FragmentTabActivity fragmentTabActivity = new FragmentTabActivity();
            Bundle bundle = new Bundle();
            bundle.putBoolean(getString(R.string.app_name), false);
            fragmentTabActivity.setArguments(bundle);
            addFragment(fragmentTabActivity, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, fragmentTabActivity.getClass().getName());

            return;
        } else if (manager.getBackStackEntryCount() == 0) {

            callExitAppSweetAlertDialog();
            return;
        } else {
            removeFragment(currentFrag, currentFrag.getClass().getName());
            super.onBackPressed();
        }

    }


    public void clearBackStackZero() {
        FragmentManager fm = getSupportFragmentManager();
        for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }
    }

    public void clearBackStack() {
        FragmentManager manager = getSupportFragmentManager();
        if (manager.getBackStackEntryCount() > 0) {
            FragmentManager.BackStackEntry first = manager.getBackStackEntryAt(0);
//            manager.popBackStack(Tags.TAG_FRAGMENT_HOME_NEW_THEME, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            manager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        }
    }

    public void RemoveAllFragment() {
        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (mRetryProviderInstall) {
            // We can now safely retry installation.
            ProviderInstaller.installIfNeededAsync(MainActivity.this, MainActivity.this);
        }
        mRetryProviderInstall = false;
    }

    @Override
    public void onProviderInstalled() {

    }

    @Override
    public void onProviderInstallFailed(int i, Intent intent) {

        if (GooglePlayServicesUtil.isUserRecoverableError(i)) {
            GooglePlayServicesUtil.showErrorDialogFragment(
                    i,
                    this,
                    ERROR_DIALOG_REQUEST_CODE,
                    new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            // The user chose not to take the recovery action
                            onProviderInstallerNotAvailable();
                        }
                    });
        } else {
            // Google Play services is not available.
            onProviderInstallerNotAvailable();
        }
    }

    private void onProviderInstallerNotAvailable() {
    }


    public void callExitAppSweetAlertDialog() {
        SweetAlertDialog mSweetAlertDialog = new SweetAlertDialog(MainActivity.this)
                .setContentText("Are you sure you want to exit?")
                .setConfirmText(getResources().getString(R.string.yes))
                .setCancelText(getResources().getString(R.string.no))
                .showCancelButton(true)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                        try {
//                            stopService(new Intent(MainActivity.this, ActivitySplashScreen.class));
                            finish();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                })
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                    }
                });
        mSweetAlertDialog.show();
    }

    public class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            setResultCode(Activity.RESULT_OK);
        }
    }

    public class MyBroadcastReceiverCart extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
//            checkCartCount();
        }

    }

    @Override
    public void onPause() {

        super.onPause();
        fusedLocationClient.removeLocationUpdates(locationCallback);
        Fragment currentFrag = getSupportFragmentManager().findFragmentById(R.id.activity_main_container);

        if (currentFrag != null) {
            if (currentFrag.getClass().getName() != null) {
                mCurrentFragment = currentFrag.getClass().getName();
            }
        }
    }

    @Override
    protected void onDestroy() {
        System.runFinalization();
        Runtime.getRuntime().gc();
        getSharedPreferences(Tags.TAG_SHARED_PREFERENCE_PUSH_NOTIFICATION_DATA, 0).edit().clear().apply();
        super.onDestroy();

    }

    @Override
    protected void onStop() {
        System.runFinalization();
        Runtime.getRuntime().gc();
        super.onStop();
    }

//    public void showCart() {
//        int count = 0;
//        try {
//            count = mDBService.getTotalCartCount(MainActivity.this,
//                    mStringUserId);
//
//            if (count <= 0) {
//                mTextViewBageCart.setVisibility(View.GONE);
//            } else {
//                mTextViewBageCart.setVisibility(View.VISIBLE);
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    public void updateCartCount(int count) {
//        if (count <= 0) {
//            mTextViewBageCart.setText("");
//            mTextViewBageCart.setVisibility(View.GONE);
//        } else {
//            mTextViewBageCart.setText(String.valueOf(count));
//            mTextViewBageCart.setVisibility(View.VISIBLE);
//        }
//        showCart();
//    }
//
//    public void checkCartCount() {
//
//        int count = 0;
//
//
//        count = mDBService.getTotalCartCount(MainActivity.this, mStringUserId);
//        updateCartCount(count);
//
//    }

//    location permission


    private void updateLocationUI() {

        try {
//            if (locationPermissionGranted) {
//                map.setMyLocationEnabled(true);
//                map.getUiSettings().setMyLocationButtonEnabled(true);
//                Log.v("log_tag", "Check Perm");

//                ActivityCompat.requestPermissions(mainActivity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

            int permissionSendMessage = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
            int locationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
//                int locationPermission1 = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION);
            List<String> listPermissionsNeeded = new ArrayList<String>();
            if (locationPermission != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }
//                if (locationPermission != PackageManager.PERMISSION_GRANTED) {
//                    listPermissionsNeeded.add(Manifest.permission.ACCESS_BACKGROUND_LOCATION);
//                }
            if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            }

            if (!listPermissionsNeeded.isEmpty()) {
                ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), LOCATION_PERMISSION_REQUEST_CODE);
            }

            locationManager = (LocationManager) MainActivity.this.getSystemService(Context.LOCATION_SERVICE);

            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                OnGPS();
            } else {
                startLocationUpdates();
            }

//            }
//            else {
////                map.setMyLocationEnabled(false);
////                map.getUiSettings().setMyLocationButtonEnabled(false);
////                lastKnownLocation = null;
//                startLocationUpdates();
//            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void OnGPS() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));

                locationCallback = new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        currentLocation = locationResult.getLocations().get(0);
                        getAddress();
                    }
                };

            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    public void startLocationUpdates() {
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) ||
                (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
//                || (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED)
        ) {

            int permissionSendMessage = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
            int locationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
//            int locationPermission1 = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION);
            List<String> listPermissionsNeeded = new ArrayList<String>();
            if (locationPermission != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }
//            if (locationPermission1 != PackageManager.PERMISSION_GRANTED) {
//                listPermissionsNeeded.add(Manifest.permission.ACCESS_BACKGROUND_LOCATION);
//            }
            if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            }
            if (!listPermissionsNeeded.isEmpty()) {
                ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), LOCATION_PERMISSION_REQUEST_CODE);
            }


        } else {
            LocationRequest locationRequest = new LocationRequest();
            locationRequest.setInterval(10000);
            locationRequest.setFastestInterval(10000);
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
            }
        }
    }

    @SuppressWarnings("MissingPermission")
    public void getAddress() {

        if (!Geocoder.isPresent()) {
//            Toast.makeText(MainActivity.this,
//                    "Can't find current address, ",
//                    Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(this, GetAddressIntentService.class);
        intent.putExtra("add_receiver", addressResultReceiver);
        intent.putExtra("add_location", currentLocation);
        startService(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startLocationUpdates();
                } else {
//                    Toast.makeText(this, "Location permission not granted, " +
//                                    "restart the app if you want the feature",
//                            Toast.LENGTH_SHORT).show();
                }
                return;
            }

        }
    }

    private class LocationAddressResultReceiver extends ResultReceiver {
        LocationAddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {

            if (resultCode == 0) {
                Log.d("Address", "Location null retrying");
                getAddress();
            }

            if (resultCode == 1) {
//                Toast.makeText(MainActivity.this,
//                        "Address not found, ",
//                        Toast.LENGTH_SHORT).show();
            }

            str_PostalCode = resultData.getString("Postal Code");
            str_City = resultData.getString("Locality");

            mEditorLocation.putString("City", str_City);
            mEditorLocation.putString("PostCode", str_PostalCode);
            mEditorLocation.apply();
//            showResults(str_PostalCode, str_City);
        }
    }

//    public void showResults(String postal_code, String City) {
//        mTextViewLocation.setText(City + "\n" + postal_code);
//        mImageViewLocation.setVisibility(View.VISIBLE);
//
//    }


    @Override
    protected void onResume() {
        super.onResume();
        startLocationUpdates();
    }
}
