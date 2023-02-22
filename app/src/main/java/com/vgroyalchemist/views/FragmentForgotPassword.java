package com.vgroyalchemist.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.FragmentTransaction;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.vgroyalchemist.R;
import com.vgroyalchemist.controller.MainActivity;
import com.vgroyalchemist.loaders.LoaderTask;
import com.vgroyalchemist.loaders.TaskExecutor;
import com.vgroyalchemist.utils.CommonHelper;
import com.vgroyalchemist.utils.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.vgroyalchemist.utils.Tags;
import com.vgroyalchemist.vos.ServerResponseVO;
import com.vgroyalchemist.webservice.FetchForgotPassowrdEmailInfoService;
import com.vgroyalchemist.webservice.PostParseGet;

/* developed by krishna 24-03-2021
*  user email id using forgot password */
public class FragmentForgotPassword extends CartFragments {

    public static final String FRAGMENT_ID = "2";

    public static MainActivity mainActivity;
    CommonHelper mCommonHelper;
    EditText mEditTextEmail;

    ImageView mImageViewBack;
    TextView mTextViewTitle;

    TextInputLayout mTextInputLayoutEmailId;

    TextView mTextViewSubmit;

    String mStringGetEmailId;

    View fragmnetview = null;

    ServerResponseVO mServerResponseVO;
    PostParseGet mPostParseGet;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = (MainActivity) getActivity();
        mCommonHelper = new CommonHelper();
        mPostParseGet = new PostParseGet(mainActivity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmnetview = inflater.inflate(R.layout.fragment_forgot_password, container, false);

        mEditTextEmail = (EditText) fragmnetview.findViewById(R.id.fragment_forgotpassword_edttext_EmailId);
        mTextInputLayoutEmailId = fragmnetview.findViewById(R.id.fragment_forgotpassword_input_EmailId);
        mTextViewSubmit = fragmnetview.findViewById(R.id.fragment_forgotpassword_textview_submit);
        mImageViewBack = fragmnetview.findViewById(R.id.fragment_forgotpassword_image_back);
        mTextViewTitle = fragmnetview.findViewById(R.id.fragment_forgotpassword_title_textview);
        mTextViewTitle.setText("Forgot Password");

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

                mStringGetEmailId = mEditTextEmail.getText().toString().trim();

                if (!validateEmail()) {
                    return;
                }
                ForgotPasswsordEmailExecute();
            }
        });
        return fragmnetview;
    }

    private boolean validateEmail() {
        String email = mEditTextEmail.getText().toString().trim();

        if (email.isEmpty() || !mCommonHelper.isValidEmail(email)) {
            mTextInputLayoutEmailId.setError(getResources().getString(R.string.validEmail));
            requestFocus(mEditTextEmail);
            mCommonHelper.hideKeyboard(mainActivity);
            return false;
        } else {
            mTextInputLayoutEmailId.setErrorEnabled(false);
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

    public class ForgotpassowrdEmail extends TaskExecutor {
        protected ForgotpassowrdEmail(Context context, Bundle bundle) {
            super(context, bundle);
        }

        @Override
        protected Object executeTask() {
            try {
                FetchForgotPassowrdEmailInfoService fetchVersionInfoService = new FetchForgotPassowrdEmailInfoService();
                mServerResponseVO = fetchVersionInfoService.fetchSignupUserInformation(mainActivity, Tags.ForgotPasswordSendEmail, mStringGetEmailId);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public void ForgotPasswsordEmailExecute() {

        getLoaderManager().restartLoader(0, null, new LoaderManager.LoaderCallbacks<TaskExecutor>() {
            @Override
            public Loader<TaskExecutor> onCreateLoader(int arg0, Bundle arg1) {

                ForgotpassowrdEmail mForgotpassowrdEmail = new ForgotpassowrdEmail(mainActivity, null);
                return new LoaderTask<TaskExecutor>(mainActivity, mForgotpassowrdEmail);
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
                                mCommonHelper.hideKeyboard(mainActivity);

                                mServerResponseVO.getData();

                                @SuppressLint("HandlerLeak") Handler handler = new Handler() {
                                    @Override
                                    public void handleMessage(Message msg) {
                                        if (msg.what == Tags.WHAT) {

                                            FragmentVerificationCode mFragmentVerificationForgotPassword = new FragmentVerificationCode();
                                            Bundle args = new Bundle();
                                            args.putString("EmailId", mServerResponseVO.getEmailId());
                                            mFragmentVerificationForgotPassword.setArguments(args);
                                            mainActivity.addFragment(mFragmentVerificationForgotPassword, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, mFragmentVerificationForgotPassword.getClass().getName());


                                        }
                                    }
                                };
                                handler.sendEmptyMessage(Tags.WHAT);
                            } else {
                                mCommonHelper.hideKeyboard(mainActivity);
                                Snackbar.with(mainActivity).text(mServerResponseVO.getMsg()).show(mainActivity);
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