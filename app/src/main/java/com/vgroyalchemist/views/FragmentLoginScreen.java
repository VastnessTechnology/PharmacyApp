package com.vgroyalchemist.views;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.vgroyalchemist.CommonClass.CommonClass;
import com.vgroyalchemist.loaders.LoaderTaskWithoutProgressDialog;
import com.vgroyalchemist.loaders.TaskExecutor;
import com.vgroyalchemist.R;
import com.vgroyalchemist.controller.MainActivity;
import com.vgroyalchemist.loaders.LoaderTask;
import com.vgroyalchemist.utils.CommonHelper;
import com.vgroyalchemist.utils.Snackbar;
import com.vgroyalchemist.utils.Tags;
import com.vgroyalchemist.utils.Utility;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.material.textfield.TextInputLayout;
import com.vgroyalchemist.vos.GetLoginData;
import com.vgroyalchemist.vos.LegalDetailsVO;
import com.vgroyalchemist.vos.ServerResponseVO;
import com.vgroyalchemist.webservice.CheckpatientInfoService;
import com.vgroyalchemist.webservice.FetchLoginUserInfoService;
import com.vgroyalchemist.webservice.FetchOtpVerifyInfoService;
import com.vgroyalchemist.webservice.FetchSignupUserInfoService;
import com.vgroyalchemist.webservice.FetchSocialUserInfoService;
import com.vgroyalchemist.webservice.LegalDetailsService;
import com.vgroyalchemist.webservice.PostParseGet;

import org.json.JSONException;
import org.json.JSONObject;


/*  developed by krishna 24-03-2021
 *   login and sign up page  */
public class FragmentLoginScreen extends CartFragments {

    //Variable Declaration
    public static final String FRAGMENT_ID = "1";

    public static MainActivity mainActivity;
    public View fragmentView = null;


    TextInputEditText mTextInputEditTextMobileNo;
    TextInputEditText mTextInputEditTextVGNumber;
    TextInputLayout mInputLayoutVGNumber;
    TextInputLayout mInputLayoutMobileNo;

    TextView mTextViewSkip;
    TextView mTextViewGetCode;
    TextView mTextViewEmail;


    ImageView mImageViewBack;

    public String mStringSocialFirstName = "";
    public String mStringSocialLastName = "";
    public static String mStringSocialEmail = "";
    public String mStringPasswordDialog = "";
    public String mStringSocialGoogleId = "";
    Dialog mDialog;

    static ProgressDialog progress_dialog;

    CommonHelper mCommonHelper;
    PostParseGet mPostParseGet;
    GetLoginData mGetLoginData;

    public ServerResponseVO mServerResponseVO;
    String mStringLoginId;
    String mStringMobileNo;
    String mStringEmailAddress;
    String mStringPatientId;
    Bundle mBundle;

    // Device Token Check
    public SharedPreferences mSharedPreferencesDeviceToekn;
    public SharedPreferences.Editor mEditorDeviceToken;

    boolean hasLoggedIn;
    String GuestUser;
    //login
    public SharedPreferences mSharedPreferencesLoginFirst;
    SharedPreferences.Editor editor;

    //    Refeal Code
    public SharedPreferences mSharedPreferencesReferalLink;
    public SharedPreferences.Editor mEditorReferallink;

    // userdata
    public SharedPreferences mSharedPreferencesUserId;
    SharedPreferences.Editor mEditor;

    boolean isverify = false;
    boolean IsLoginEmail;
    TabLayout mTabLayout;
    public boolean mBooleanVerification = false;

    TextView mTextViewTermsCondition;
    TextView mTextViewPrivacyPolicy;

    LegalDetailsVO mLegalDetailsVO;
    boolean ISCorporate = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainActivity = (MainActivity) getActivity();
        mPostParseGet = new PostParseGet(mainActivity);
        mCommonHelper = new CommonHelper();
        mServerResponseVO = new ServerResponseVO();
        mGetLoginData = new GetLoginData();
        mBundle = getArguments();

        if (mBundle != null) {
            mBooleanVerification = getArguments().getBoolean(Tags.IS_FROM_LOGIN);
            IsLoginEmail = mBundle.getBoolean("IsLoginEmail");
        }

    }

    public boolean getInnerFlag() {
        return mBooleanVerification;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentView = inflater.inflate(R.layout.fragment_login_screen, container, false);

        mTextInputEditTextMobileNo = fragmentView.findViewById(R.id.fragment_login_temp_edittext_mobileno);
        mInputLayoutMobileNo = fragmentView.findViewById(R.id.input_layout_name);

        mTextInputEditTextVGNumber = fragmentView.findViewById(R.id.fragment_login_temp_edittext_VGNumber);
        mInputLayoutVGNumber = fragmentView.findViewById(R.id.input_layout_VGNumber);

        mTextViewEmail = fragmentView.findViewById(R.id.fragment_login_textview_emailid);

        mTextViewGetCode = fragmentView.findViewById(R.id.fragment_login_temp_textview_submit);
        mTextViewSkip = fragmentView.findViewById(R.id.fragment_login_temp_textview_Skip);

        mTextViewTermsCondition = fragmentView.findViewById(R.id.fragment_login_textview_Terms_conditions);
        mTextViewPrivacyPolicy = fragmentView.findViewById(R.id.fragment_login_textview_Privacypolicy);

        mTabLayout = fragmentView.findViewById(R.id.fragment_login_temp_tablayout);


        mTabLayout.addTab(mTabLayout.newTab().setText(getResources().getString(R.string.personal)));
        mTabLayout.addTab(mTabLayout.newTab().setText(getResources().getString(R.string.corporate)));
        mTabLayout.getTabAt(0).select();

        mInputLayoutMobileNo.setVisibility(View.VISIBLE);
        mTextInputEditTextMobileNo.setHint(getResources().getString(R.string.mobile_no));

        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    mInputLayoutMobileNo.setVisibility(View.VISIBLE);
                    mInputLayoutVGNumber.setVisibility(View.GONE);
                    mTextInputEditTextMobileNo.setHint(getResources().getString(R.string.mobile_no));
                    mTextInputEditTextVGNumber.setHint("");
                }
                if (tab.getPosition() == 1) {
                    ISCorporate = true;
                    mInputLayoutMobileNo.setVisibility(View.GONE);
                    mInputLayoutVGNumber.setVisibility(View.VISIBLE);
                    mTextInputEditTextMobileNo.setHint("");
                    mTextViewEmail.setVisibility(View.GONE);
                    mTextInputEditTextVGNumber.setHint(getResources().getString(R.string.enter_reisteredid));

                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        mSharedPreferencesLoginFirst = mainActivity.getSharedPreferences(Tags.PREFS_NAME, 0);
        hasLoggedIn = mSharedPreferencesLoginFirst.getBoolean("hasLoggedIn", false);
        GuestUser = mSharedPreferencesLoginFirst.getString("GuestUser", "0");

        if (mBooleanVerification) {
            if (GuestUser.equalsIgnoreCase("1")) {
                mTextViewSkip.setVisibility(View.GONE);
            } else {
                mTextViewSkip.setVisibility(View.VISIBLE);
            }
        }
        mSharedPreferencesReferalLink = mainActivity.getSharedPreferences(Tags.TAG_SHARED_PREFERENCE_REFERAL_CODE, 0);
        mEditorReferallink = mSharedPreferencesReferalLink.edit();

        mSharedPreferencesUserId = mainActivity.getSharedPreferences(Tags.TAG_SHARED_PREFERENCE_USER_ID, 0);
        mEditor = mSharedPreferencesUserId.edit();

        mTextViewGetCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ISCorporate) {

                    mStringPatientId = mTextInputEditTextVGNumber.getText().toString();
                    if (!validateVGNumber()) {
                        return;
                    }

                    CheckPatientExecute();
                } else {
                    mStringMobileNo = mTextInputEditTextMobileNo.getText().toString().trim();
                    mStringEmailAddress = "";
                    if (!validateMobile()) {
                        return;
                    }

                    if (mStringMobileNo.length() != 10) {
                        mInputLayoutMobileNo.setError(getResources().getString(R.string.validmobilenodigit));
                        return;
                    }

                    signupExecute();
                }


            }
        });

        mTextViewEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentLoginWithEmail fragmentLoginWithEmail = new FragmentLoginWithEmail();
                Bundle mBundle = new Bundle();
                mBundle.putBoolean("IsLoginEmail", true);
                fragmentLoginWithEmail.setArguments(mBundle);
                mainActivity.addFragment(fragmentLoginWithEmail, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, fragmentLoginWithEmail.getClass().getName());
            }
        });

        mTextViewSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                editor = mSharedPreferencesLoginFirst.edit();
                editor.putBoolean("hasLoggedIn", true);
                editor.putString("GuestUser", "1");
                editor.apply();
                mEditor.putString("UserId", "00000000-0000-0000-0000-000000000000");
                mEditor.apply();

                mainActivity.clearBackStackZero();
                mainActivity.clearBackStack();
                mainActivity.removeAllBackFragments();
                FragmentTabActivity fragmentTabActivity = new FragmentTabActivity();
                mainActivity.addFragment(fragmentTabActivity, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, fragmentTabActivity.getClass().getName());

            }
        });


//        mStringGetReferralCode = mSharedPreferencesReferalLink.getString("invitedby", "");
//        System.out.println("VT set invite code.........." + mStringGetReferralCode);
//        if (mStringGetReferralCode != null) {
//            mEditTextReferralCode.setText(mStringGetReferralCode);
//        } else {
//            mEditTextReferralCode.setText("");
//        }
        // Device Token
        CommonClass cmn = new CommonClass();
        cmn.FirebaseToken(getActivity());

        mSharedPreferencesDeviceToekn = mainActivity.getSharedPreferences(Tags.TAG_SHARED_PREFERENCE_APP_DEVICETOKEN, 0);

        Tags.mStringDeviceToken = mSharedPreferencesDeviceToekn.getString(Tags.REG_ID, "");
        Utility.debugger("VT Device Toekn...." + Tags.mStringDeviceToken);


        mTextViewPrivacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentContentFullDescription fragmentContentFullDescription = new FragmentContentFullDescription();
                Bundle mBundle = new Bundle();
                mBundle.putString("LegalDetails", mLegalDetailsVO.getPrivacyPolicy());
                mBundle.putString("privcy", getResources().getString(R.string.app_name));
                fragmentContentFullDescription.setArguments(mBundle);
                mainActivity.addFragment(fragmentContentFullDescription, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, fragmentContentFullDescription.getClass().getName());
            }
        });


        mTextViewTermsCondition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentContentFullDescription fragmentContentFullDescription = new FragmentContentFullDescription();
                Bundle mBundle = new Bundle();
                mBundle.putString("LegalDetails", mLegalDetailsVO.getTermsCondition());
                mBundle.putString("TermCondition", getResources().getString(R.string.app_name));
                fragmentContentFullDescription.setArguments(mBundle);
                mainActivity.addFragment(fragmentContentFullDescription, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, fragmentContentFullDescription.getClass().getName());
            }
        });


        return fragmentView;
    }


    public void PrivacyData() {
        getLoaderManager().restartLoader(0, null, new LoaderManager.LoaderCallbacks<TaskExecutor>() {
            @Override
            public Loader<TaskExecutor> onCreateLoader(int arg0, Bundle arg1) {

                LegalDataExecutor mLegalDataExecutor = new LegalDataExecutor(mainActivity, null);
                return new LoaderTaskWithoutProgressDialog<TaskExecutor>(mainActivity, mLegalDataExecutor);
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

                        if (mServerResponseVO != null && mServerResponseVO.getStatus() != null) {

                            if (mServerResponseVO.getStatus().equalsIgnoreCase("true")) {
//                                Snackbar.with(mainActivity).text(mServerResponseVO.getMsg()).show(mainActivity);

                                if (mServerResponseVO.getData() != null) {
                                    mLegalDetailsVO = (LegalDetailsVO) mServerResponseVO.getData();

                                }
                            }
                        } else {

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

    public class LegalDataExecutor extends TaskExecutor {

        protected LegalDataExecutor(Context context, Bundle bundle) {
            super(context, bundle);
        }

        @Override
        protected Object executeTask() {
            try {
                LegalDetailsService legalDetailsService = new LegalDetailsService();
                mServerResponseVO = legalDetailsService.LegalDetails(mainActivity, Tags.GetPrivacyDetails);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private boolean validateMobile() {
        String email = mTextInputEditTextMobileNo.getText().toString().trim();

        if (email.isEmpty()) {
            mInputLayoutMobileNo.setError(getResources().getString(R.string.validmobileno));
            requestFocus(mTextInputEditTextMobileNo);
            mCommonHelper.hideKeyboard(mainActivity);
            return false;
        } else {
            mInputLayoutMobileNo.setErrorEnabled(false);
        }

        return true;
    }


    private boolean validateVGNumber() {
        String email = mTextInputEditTextVGNumber.getText().toString().trim();

        if (email.isEmpty()) {
            mInputLayoutVGNumber.setError(getResources().getString(R.string.validCorporate));
            requestFocus(mTextInputEditTextVGNumber);
            mCommonHelper.hideKeyboard(mainActivity);
            return false;
        } else {
            mInputLayoutVGNumber.setErrorEnabled(false);
        }

        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            mainActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    public class SignupUserExecutor extends TaskExecutor {
        protected SignupUserExecutor(Context context, Bundle bundle) {
            super(context, bundle);
        }

        @Override
        protected Object executeTask() {
            try {
                FetchSignupUserInfoService fetchVersionInfoService = new FetchSignupUserInfoService();
                mServerResponseVO = fetchVersionInfoService.fetchSignupUserInformation(mainActivity, Tags.Signup, mStringMobileNo, mStringEmailAddress, Tags.mStringDeviceToken, getString(R.string.device_type));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public void signupExecute() {

        getLoaderManager().restartLoader(0, null, new LoaderManager.LoaderCallbacks<TaskExecutor>() {
            @Override
            public Loader<TaskExecutor> onCreateLoader(int arg0, Bundle arg1) {

                SignupUserExecutor mGuestUserExecutor = new SignupUserExecutor(mainActivity, null);
                return new LoaderTask<TaskExecutor>(mainActivity, mGuestUserExecutor);
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

                            if (mServerResponseVO != null && mServerResponseVO.getStatus() != null) {
//                                Snackbar.with(mainActivity).text(mServerResponseVO.getMsg()).show(mainActivity);
                                if (mServerResponseVO.getStatus().equalsIgnoreCase("true")) {
                                    mCommonHelper.hideKeyboard(mainActivity);


                                    mServerResponseVO.getData();
//                                    Tags.User_Id = mServerResponseVO.getUsrid();
//                                    Tags.Email_Id = mServerResponseVO.getEmailId();
//
//                                    mEditor.putString("UserId", mServerResponseVO.getUsrid());
//                                    mEditor.commit();

                                    @SuppressLint("HandlerLeak")
                                    Handler handler = new Handler() {
                                        @Override
                                        public void handleMessage(Message msg) {
                                            if (msg.what == Tags.WHAT) {

                                                if (IsLoginEmail) {
                                                    FragmentVerificationCode fragmentVerificationCode = new FragmentVerificationCode();
                                                    Bundle mBundle = new Bundle();
                                                    mBundle.putString("MobileNo", mTextInputEditTextMobileNo.getText().toString());
                                                    mBundle.putBoolean("ISEmail", false);
                                                    mBundle.putBoolean("IsLoginEmail", true);
                                                    fragmentVerificationCode.setArguments(mBundle);
                                                    mainActivity.addFragment(fragmentVerificationCode, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, fragmentVerificationCode.getClass().getName());
                                                } else {
                                                    FragmentVerificationCode fragmentVerificationCode = new FragmentVerificationCode();
                                                    Bundle mBundle = new Bundle();
                                                    mBundle.putString("MobileNo", mTextInputEditTextMobileNo.getText().toString());
                                                    mBundle.putBoolean("ISEmail", false);
                                                    mBundle.putBoolean("IsLoginEmail", false);
                                                    fragmentVerificationCode.setArguments(mBundle);
                                                    mainActivity.addFragment(fragmentVerificationCode, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, fragmentVerificationCode.getClass().getName());
                                                }
                                            }
                                        }
                                    };
                                    handler.sendEmptyMessage(Tags.WHAT);
                                } else {
                                    mCommonHelper.hideKeyboard(mainActivity);
                                    Snackbar.with(mainActivity).text(mServerResponseVO.getMsg()).show(mainActivity);
                                }
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


    public class CheckPatinetExecutor extends TaskExecutor {
        protected CheckPatinetExecutor(Context context, Bundle bundle) {
            super(context, bundle);
        }

        @Override
        protected Object executeTask() {
            try {
                CheckpatientInfoService fetchVersionInfoService = new CheckpatientInfoService();
                mServerResponseVO = fetchVersionInfoService.fetchSignupUserInformation(mainActivity, Tags.CheckPatient, mStringPatientId, Tags.mStringDeviceToken, getString(R.string.device_type));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public void CheckPatientExecute() {

        getLoaderManager().restartLoader(0, null, new LoaderManager.LoaderCallbacks<TaskExecutor>() {
            @Override
            public Loader<TaskExecutor> onCreateLoader(int arg0, Bundle arg1) {

                CheckPatinetExecutor mGuestUserExecutor = new CheckPatinetExecutor(mainActivity, null);
                return new LoaderTask<TaskExecutor>(mainActivity, mGuestUserExecutor);
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
                            if (mServerResponseVO != null && mServerResponseVO.getStatus() != null) {
//                                Snackbar.with(mainActivity).text(mServerResponseVO.getMsg()).show(mainActivity);
                                if (mServerResponseVO.getStatus().equalsIgnoreCase("true")) {
                                    mCommonHelper.hideKeyboard(mainActivity);
                                    mServerResponseVO.getData();
//                                    Tags.User_Id = mServerResponseVO.getUsrid();
//                                    Tags.Email_Id = mServerResponseVO.getEmailId();
//
//                                    mEditor.putString("UserId", mServerResponseVO.getUsrid());
//                                    mEditor.commit();

                                    @SuppressLint("HandlerLeak")
                                    Handler handler = new Handler() {
                                        @Override
                                        public void handleMessage(Message msg) {
                                            if (msg.what == Tags.WHAT) {
                                                FragmentVerificationCode fragmentVerificationCode = new FragmentVerificationCode();
                                                Bundle mBundle = new Bundle();
                                                mBundle.putString("MobileNo",mServerResponseVO.getMobileNo());
                                                mBundle.putBoolean("ISEmail", false);
                                                mBundle.putBoolean("IsLoginEmail", false);
                                                mBundle.putBoolean("ISCorporate", true);
                                                fragmentVerificationCode.setArguments(mBundle);
                                                mainActivity.addFragment(fragmentVerificationCode, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, fragmentVerificationCode.getClass().getName());


                                            }
                                        }
                                    };
                                    handler.sendEmptyMessage(Tags.WHAT);
                                } else {
                                    mCommonHelper.hideKeyboard(mainActivity);
                                    Snackbar.with(mainActivity).text(mServerResponseVO.getMsg()).show(mainActivity);
                                }
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


    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase("otp")) {
                final String message = intent.getStringExtra("message");
                // message is the fetching OTP

//                mEditTextPasswordLogin.setText(message);
            }
        }
    };

    @Override
    public void onPause() {
        super.onPause();

        LocalBroadcastManager.getInstance(mainActivity).unregisterReceiver(receiver);
    }

    public void onResumeData() {
        PrivacyData();
        mainActivity.toolbar.setVisibility(View.GONE);
    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(mainActivity).registerReceiver(receiver, new IntentFilter("otp"));

        onResumeData();
    }

    @Override
    public void doWork() {
        onResumeData();
    }

}