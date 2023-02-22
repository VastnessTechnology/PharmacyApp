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

import com.google.android.material.textfield.TextInputLayout;
import com.vgroyalchemist.R;
import com.vgroyalchemist.controller.MainActivity;
import com.vgroyalchemist.loaders.LoaderTask;
import com.vgroyalchemist.loaders.TaskExecutor;
import com.vgroyalchemist.utils.CommonHelper;
import com.vgroyalchemist.utils.Snackbar;
import com.vgroyalchemist.utils.Tags;
import com.vgroyalchemist.utils.Utility;
import com.vgroyalchemist.vos.ServerResponseVO;
import com.vgroyalchemist.webservice.CheckMobileOtpService;
import com.vgroyalchemist.webservice.CheckMobileResendOtpService;
import com.vgroyalchemist.webservice.FetchOtpVerifyInfoService;
import com.vgroyalchemist.webservice.ForgotPasswordVerifyOtpInfoService;
import com.vgroyalchemist.webservice.PostParseGet;
import com.vgroyalchemist.webservice.VerifyResendOtpInfoService;

/* developed by krishna 24-03-2021
*  Enter otp edit mobile no conformation */
public class FragmentVerificationMoblieNo extends NonCartFragment {

    public static final String FRAGMENT_ID = "42";
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

    String mStringGetotpValue;
    ServerResponseVO mServerResponseVO;
    ServerResponseVO mServerResponseVOResnedOtp;
    PostParseGet mPostParseGet;

    Bundle mBundle;
    boolean mStringIsVerify;
    String mStringMobileNO;
    String mStringEmailid;
    // userdata
    public SharedPreferences mSharedPreferencesUserId;
    SharedPreferences.Editor mEditor;

    String mstringUserID;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainActivity = (MainActivity) getActivity();
        mCommonHelper = new CommonHelper();
        mPostParseGet = new PostParseGet(mainActivity);

        mBundle = getArguments();

        if (mBundle != null) {
            mStringMobileNO =mBundle.getString("mobileno");
            mStringEmailid =mBundle.getString("Emailid");
            Utility.debugger("VT verify otp email...."+ mStringMobileNO);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmnetview =inflater.inflate(R.layout.fragment_verification_forgot_password, container, false);


        mImageViewBack =fragmnetview.findViewById(R.id.fragment_passwordverify_image_back);

        mEditTextOtpNumber= fragmnetview.findViewById(R.id.fragment_passwordverify_edttext_EmailId);
        mTextInputLayoutOtpNumber =fragmnetview.findViewById(R.id.fragment_forgotpassword_input_EmailId);
        mTextViewResnedotp =fragmnetview.findViewById(R.id.fragment_passwordverify_textview_resend);
        mTextViewSubmit =fragmnetview.findViewById(R.id.fragment_passwordverify_textview_submit);
        mTextViewResendTimer =fragmnetview.findViewById(R.id.fragment_passwordverify_textview_resend_timer);


        mSharedPreferencesUserId =mainActivity.getSharedPreferences(Tags.TAG_SHARED_PREFERENCE_USER_ID ,0);
        mstringUserID = mSharedPreferencesUserId.getString("UserId","");
        mEditor =mSharedPreferencesUserId.edit();

        new CountDownTimer(60000, 1000) {

            public void onTick(long millisUntilFinished) {
                mTextViewResendTimer.setText("Resend in 00." + millisUntilFinished / 1000 );
                //here you can have your logic to set text to edittext
            }

            public void onFinish() {
                mTextViewResendTimer.setVisibility(View.GONE);
                mTextViewResnedotp.setVisibility(View.VISIBLE);
                mTextViewResnedotp.setText("Resend OTP");
                mEditTextOtpNumber.setText("");
            }

        }.start();


        mTextViewResnedotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!mCommonHelper.check_Internet(getActivity())) {
                    Snackbar.with(mainActivity).text(getResources().getString(R.string.interneterror)).show(mainActivity);
                    return;
                }

                mStringGetotpValue = mEditTextOtpNumber.getText().toString().trim();

//                if(!validateOtp())
//                {
//                    return;
//                }

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

                if(!validateOtp())
                {
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

    public void onResumeData() {
        mainActivity.toolbar.setVisibility(View.GONE);
    }

    @Override
    public void doWork() {
        onResumeData();
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


    public class VerifyResendOtp extends TaskExecutor {
        protected VerifyResendOtp(Context context, Bundle bundle) {
            super(context, bundle);
        }

        @Override
        protected Object executeTask() {
            try {

                CheckMobileResendOtpService mVerifyOtpInfoService = new CheckMobileResendOtpService();
                mServerResponseVOResnedOtp = mVerifyOtpInfoService.CheckResndOTp(mainActivity, Tags.MobileReSentOtp, mStringMobileNO,mStringGetotpValue,mstringUserID);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }


    public void VerifyResendOtpexecute() {
        new CountDownTimer(60000, 1000) {

            public void onTick(long millisUntilFinished) {
                mTextViewResnedotp.setVisibility(View.GONE);
                mTextViewResendTimer.setVisibility(View.VISIBLE);
                mTextViewResendTimer.setText("Resend in 00." + millisUntilFinished / 1000 );
                //here you can have your logic to set text to edittext
            }

            public void onFinish() {
                mTextViewResendTimer.setVisibility(View.GONE);
                mTextViewResnedotp.setVisibility(View.VISIBLE);
                mTextViewResnedotp.setText("Resend OTP");
            }

        }.start();

        getLoaderManager().restartLoader(0, null, new LoaderManager.LoaderCallbacks<TaskExecutor>() {
            @Override
            public Loader<TaskExecutor> onCreateLoader(int arg0, Bundle arg1) {

                VerifyResendOtp mVerifyResendOtp = new VerifyResendOtp(mainActivity, null);
                return new LoaderTask<TaskExecutor>(mainActivity, mVerifyResendOtp);
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
                        if (mServerResponseVOResnedOtp != null && mServerResponseVOResnedOtp.getStatus() != null) {
                            if (mServerResponseVOResnedOtp.getStatus().equalsIgnoreCase("true")) {
                                mCommonHelper.hideKeyboard(mainActivity);
//                                    Snackbar.with(mainActivity).text(mServerResponseVOResnedOtp.getMsg()).show(mainActivity);
                            }
                            else {
                                mCommonHelper.hideKeyboard(mainActivity);
                                Snackbar.with(mainActivity).text(mServerResponseVOResnedOtp.getMsg()).show(mainActivity);
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
                        if (mServerResponseVO.getStatus().equalsIgnoreCase("true")) {
                            mainActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
//                            Snackbar.with(mainActivity).text(mServerResponseVO.getMsg()).show(mainActivity);

                            mServerResponseVO.getData();


                            @SuppressLint("HandlerLeak") Handler handler = new Handler() {
                                @Override
                                public void handleMessage(Message msg) {
                                    if (msg.what == Tags.WHAT) {
                                       mainActivity.onBackPressed();
                                    }
                                }
                            };
                            handler.sendEmptyMessage(Tags.WHAT);
                        } else {
                            mainActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
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

    @SuppressLint("RestrictedApi")
    public class OtpVerify extends TaskExecutor {
        protected OtpVerify(Context context, Bundle bundle) {
            super(context, bundle);
        }

        @Override
        protected Object executeTask() {

            CheckMobileOtpService fetchOtpVerifyInfoService = new CheckMobileOtpService();

            mServerResponseVO = fetchOtpVerifyInfoService.CheckmobileNo(mainActivity, Tags.MobileOtpVerification, mStringMobileNO, mStringGetotpValue ,mstringUserID);

            return null;
        }
    }

}