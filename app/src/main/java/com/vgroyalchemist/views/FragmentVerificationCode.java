package com.vgroyalchemist.views;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentTransaction;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.vgroyalchemist.R;

import com.vgroyalchemist.controller.MainActivity;
import com.vgroyalchemist.loaders.LoaderTask;
import com.vgroyalchemist.loaders.TaskExecutor;
import com.vgroyalchemist.utils.CommonHelper;
import com.vgroyalchemist.utils.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.vgroyalchemist.utils.Tags;
import com.vgroyalchemist.utils.Utility;
import com.vgroyalchemist.vos.ServerResponseVO;

import com.vgroyalchemist.webservice.CheckEmailInfoService;
import com.vgroyalchemist.webservice.FetchOtpVerifyInfoService;
import com.vgroyalchemist.webservice.FetchSignupUserInfoService;
import com.vgroyalchemist.webservice.ForgotPasswordVerifyOtpInfoService;
import com.vgroyalchemist.webservice.PostParseGet;
import com.vgroyalchemist.webservice.VerifyResendOtpInfoService;

/* developed by krishna 24-03-2021
 *  enter otp forgot password */
public class FragmentVerificationCode extends NonCartFragment {

    public static final String FRAGMENT_ID = "3";
    public static MainActivity mainActivity;
    CommonHelper mCommonHelper;
    View fragmnetview = null;

    EditText mEditTextOtpNumber;
    TextInputLayout mTextInputLayoutOtpNumber;
    ImageView mImageViewBack;
    TextView mTextViewTitle;
    TextView mTextViewResnedotp;
    TextView mTextViewResendTimer;
    TextView mTextViewSubmit;
    TextView mTextViewMobileNo;
    TextView mTextViewSigninTitle;

    String mStringGetotpValue;
    ServerResponseVO mServerResponseVO;
    ServerResponseVO mServerResponseVOResnedOtp;
    PostParseGet mPostParseGet;

    Bundle mBundle;
    boolean mStringIsVerify;
    String mStringGetEmailId;
    boolean ISEmailId;
    String MobileNo;
    boolean ISCorporate;

    //login
    public SharedPreferences mSharedPreferencesLoginFirst;
    SharedPreferences.Editor editor;
    boolean hasLoggedIn;
    boolean IsLoginEmail;
    String mStringEmailId;
    String mStringUserid;

    // userdata
    public SharedPreferences mSharedPreferencesUserId;
    SharedPreferences.Editor mEditor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainActivity = (MainActivity) getActivity();
        mCommonHelper = new CommonHelper();
        mPostParseGet = new PostParseGet(mainActivity);

        mBundle = getArguments();

        if (mBundle != null) {
            mStringGetEmailId = mBundle.getString("EmailId");
            mStringIsVerify = mBundle.getBoolean("ISFromVerify");
            ISEmailId = mBundle.getBoolean("ISEmail");
            MobileNo = mBundle.getString("MobileNo");
            IsLoginEmail = mBundle.getBoolean("IsLoginEmail");
            ISCorporate = mBundle.getBoolean("ISCorporate");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmnetview = inflater.inflate(R.layout.fragment_verification_forgot_password, container, false);


        mImageViewBack = fragmnetview.findViewById(R.id.fragment_passwordverify_image_back);

        mEditTextOtpNumber = fragmnetview.findViewById(R.id.fragment_passwordverify_edttext_EmailId);
        mTextInputLayoutOtpNumber = fragmnetview.findViewById(R.id.fragment_forgotpassword_input_EmailId);
        mTextViewResnedotp = fragmnetview.findViewById(R.id.fragment_passwordverify_textview_resend_msg);
        mTextViewSubmit = fragmnetview.findViewById(R.id.fragment_passwordverify_textview_submit);
        mTextViewResendTimer = fragmnetview.findViewById(R.id.fragment_passwordverify_textview_resend_timer);

        mTextViewMobileNo = fragmnetview.findViewById(R.id.fragment_verifyOtp_textview_mobile_no);
        mTextViewSigninTitle = fragmnetview.findViewById(R.id.fragment_verifyotp_title);

        mSharedPreferencesLoginFirst = mainActivity.getSharedPreferences(Tags.PREFS_NAME, 0);
        hasLoggedIn = mSharedPreferencesLoginFirst.getBoolean("hasLoggedIn", false);


        mSharedPreferencesUserId = mainActivity.getSharedPreferences(Tags.TAG_SHARED_PREFERENCE_USER_ID, 0);
        mEditor = mSharedPreferencesUserId.edit();
        mStringEmailId = mSharedPreferencesUserId.getString("EmailId", "");
        mStringUserid = mSharedPreferencesUserId.getString("UserId", "");


        if (ISEmailId) {
            mTextViewSigninTitle.setText(getString(R.string.sing_with_email));
            mTextViewMobileNo.setText(mStringGetEmailId);
        } else {
            mTextViewSigninTitle.setText(getString(R.string.verifynumber));
            mTextViewMobileNo.setText(MobileNo);
        }
        new CountDownTimer(30000, 1000) {

            @SuppressLint("SetTextI18n")
            public void onTick(long millisUntilFinished) {
                mTextViewResendTimer.setText(" Resend in 00." + millisUntilFinished / 1000);
                //here you can have your logic to set text to edittext
            }

            public void onFinish() {
                mTextViewResendTimer.setVisibility(View.VISIBLE);
                mTextViewResnedotp.setVisibility(View.VISIBLE);
                mTextViewResendTimer.setText("Resend On SMS");
                mEditTextOtpNumber.setText("");
            }

        }.start();
        mTextViewResendTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!mCommonHelper.check_Internet(getActivity())) {
                    Snackbar.with(mainActivity).text(getResources().getString(R.string.interneterror)).show(mainActivity);
                    return;
                }

                if (ISEmailId) {
                    MobileNo = "";
                } else {
                    mStringGetEmailId = "";
                }
                VerifyResendOtpexecute();
            }
        });

        mImageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.onBackPressed();
            }
        });
        mTextViewSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (!mCommonHelper.check_Internet(getActivity())) {
                    Snackbar.with(mainActivity).text(getResources().getString(R.string.interneterror)).show(mainActivity);
                    return;
                }

                mStringGetotpValue = mEditTextOtpNumber.getText().toString().trim();
                if (ISEmailId) {
                    MobileNo = "";
                } else {
                    mStringGetEmailId = "";
                }

                if (!validateOtp()) {
                    return;
                }

                OtpVerifyexecute();


            }
        });


        return fragmnetview;
    }

    private boolean validateOtp() {
        String email = mEditTextOtpNumber.getText().toString().trim();

        if (email.isEmpty()) {
            mTextInputLayoutOtpNumber.setError(getResources().getString(R.string.valid_otp));
            requestFocus(mEditTextOtpNumber);
            mCommonHelper.hideKeyboard(mainActivity);
            return false;
        } else {
            mTextInputLayoutOtpNumber.setErrorEnabled(false);
        }

        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            mainActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase("otp")) {
                final String message = intent.getStringExtra("message");
                // message is the fetching OTP
                mEditTextOtpNumber.setText(message);
            }
        }
    };

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
        LocalBroadcastManager.getInstance(mainActivity).registerReceiver(receiver, new IntentFilter("otp"));

        onResumeData();
    }

    @Override
    public void onPause() {
        LocalBroadcastManager.getInstance(mainActivity).unregisterReceiver(receiver);
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mCommonHelper.hideKeyboard(mainActivity);
    }


    public class SignupUserExecutor extends TaskExecutor {
        protected SignupUserExecutor(Context context, Bundle bundle) {
            super(context, bundle);
        }

        @Override
        protected Object executeTask() {
            try {
                FetchSignupUserInfoService fetchVersionInfoService = new FetchSignupUserInfoService();
                mServerResponseVO = fetchVersionInfoService.fetchSignupUserInformation(mainActivity, Tags.Signup, MobileNo, mStringGetEmailId, Tags.mStringDeviceToken, getString(R.string.device_type));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public void VerifyResendOtpexecute() {


        new CountDownTimer(60000, 1000) {

            @SuppressLint("SetTextI18n")
            public void onTick(long millisUntilFinished) {
                mTextViewResnedotp.setVisibility(View.VISIBLE);
                mTextViewResendTimer.setVisibility(View.VISIBLE);
                mTextViewResendTimer.setText("Resend in 00." + millisUntilFinished / 1000);
                //here you can have your logic to set text to edittext
            }

            @SuppressLint("SetTextI18n")
            public void onFinish() {
                mTextViewResendTimer.setVisibility(View.VISIBLE);
                mTextViewResnedotp.setVisibility(View.VISIBLE);
                mTextViewResendTimer.setText("Resend On SMS");
            }

        }.start();


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

    //    Otpverification

    public void OtpVerifyexecute() {

        getLoaderManager().restartLoader(0, null, new LoaderManager.LoaderCallbacks<TaskExecutor>() {
            @Override
            public Loader<TaskExecutor> onCreateLoader(int arg0, Bundle arg1) {

                OtpVerify otpVerify = new OtpVerify(mainActivity, null);
                return new LoaderTask<TaskExecutor>(mainActivity, otpVerify);
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


                            if (mServerResponseVO.getStatus().equalsIgnoreCase("true")) {
                                mainActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
//                            Snackbar.with(mainActivity).text(mServerResponseVO.getMsg()).show(mainActivity);

                                mServerResponseVO.getData();

                                editor = mSharedPreferencesLoginFirst.edit();
                                editor.putBoolean("hasLoggedIn", true);
                                editor.putString("GuestUser", "0");
                                editor.apply();
                                mEditor.putString("UserId", mServerResponseVO.getUsrid());
                                mEditor.apply();

                                mStringUserid = mServerResponseVO.getUsrid();

                                if (IsLoginEmail) {
                                    CheckEmailId();
                                }
                                if (ISCorporate) {
                                    mEditor.putString("EmailId", mServerResponseVO.getEmailId());
                                    mEditor.apply();
                                    mainActivity.RemoveAllFragment();
                                    FragmentTabActivity fragmentTabActivity = new FragmentTabActivity();
                                    mainActivity.addFragment(fragmentTabActivity, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, fragmentTabActivity.getClass().getName());
                                } else {
                                    if (mServerResponseVO.getEmailId().equalsIgnoreCase("null") || mServerResponseVO.getEmailId() == null) {
                                        FragmentLoginWithEmail fragmentLoginWithEmail = new FragmentLoginWithEmail();
                                        mainActivity.addFragment(fragmentLoginWithEmail, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, fragmentLoginWithEmail.getClass().getName());
                                    } else {

                                        mEditor.putString("EmailId", mServerResponseVO.getEmailId());
                                        mEditor.apply();
                                        mainActivity.RemoveAllFragment();
                                        FragmentTabActivity fragmentTabActivity = new FragmentTabActivity();
                                        mainActivity.addFragment(fragmentTabActivity, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, fragmentTabActivity.getClass().getName());

                                    }
                                }

                            } else {
                                mainActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                                Snackbar.with(mainActivity).text(mServerResponseVO.getMsg()).show(mainActivity);
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
    public class OtpVerify extends TaskExecutor {
        protected OtpVerify(Context context, Bundle bundle) {
            super(context, bundle);
        }

        @Override
        protected Object executeTask() {

            FetchOtpVerifyInfoService fetchOtpVerifyInfoService = new FetchOtpVerifyInfoService();

            mServerResponseVO = fetchOtpVerifyInfoService.fetchLoginUserInformation(mainActivity, Tags.OTPVerify, mStringGetEmailId, MobileNo, mStringGetotpValue);

            return null;
        }
    }


    public void CheckEmailId() {

        getLoaderManager().restartLoader(0, null, new LoaderManager.LoaderCallbacks<TaskExecutor>() {
            @Override
            public Loader<TaskExecutor> onCreateLoader(int arg0, Bundle arg1) {

                CheckEmailID mGuestUserExecutor = new CheckEmailID(mainActivity, null);
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
                                    mEditor.putString("EmailId", mStringEmailId);
                                    mEditor.apply();

                                    @SuppressLint("HandlerLeak")
                                    Handler handler = new Handler() {
                                        @Override
                                        public void handleMessage(Message msg) {
                                            if (msg.what == Tags.WHAT) {
                                                mainActivity.RemoveAllFragment();
                                                FragmentTabActivity fragmentTabActivity = new FragmentTabActivity();
                                                mainActivity.addFragment(fragmentTabActivity, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, fragmentTabActivity.getClass().getName());
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


    public class CheckEmailID extends TaskExecutor {
        protected CheckEmailID(Context context, Bundle bundle) {
            super(context, bundle);
        }

        @Override
        protected Object executeTask() {
            try {
                CheckEmailInfoService fetchVersionInfoService = new CheckEmailInfoService();
                mServerResponseVO = fetchVersionInfoService.fetchSignupUserInformation(mainActivity, Tags.CheckEmail, mStringUserid, mStringEmailId);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

}