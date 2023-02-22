package com.vgroyalchemist.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vgroyalchemist.R;
import com.vgroyalchemist.controller.MainActivity;
import com.vgroyalchemist.loaders.LoaderTask;
import com.vgroyalchemist.loaders.TaskExecutor;
import com.vgroyalchemist.utils.CommonHelper;
import com.vgroyalchemist.utils.Snackbar;
import com.vgroyalchemist.utils.SweetAlertDialogSingleButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.vgroyalchemist.utils.Tags;
import com.vgroyalchemist.utils.Utility;
import com.vgroyalchemist.vos.ServerResponseVO;
import com.vgroyalchemist.webservice.ForgotPasswordVerifyOtpInfoService;
import com.vgroyalchemist.webservice.PostParseGet;
import com.vgroyalchemist.webservice.ResetPasswordInfoService;

import java.util.regex.Pattern;

/*  developed by krishna 24-03-2021
*   Enter new password for reset password */
public class FragmentResetPassword extends NonCartFragment {

    public static final String FRAGMENT_ID = "4";
    public static MainActivity mainActivity;
    CommonHelper mCommonHelper;
    View fragmnetview = null;

    ImageView mImageViewBack;
    TextView mTextViewTitle;
    EditText mEditTextNewPassword;
    TextInputLayout mTextInputLayoutNewPassword;
    TextView mTextViewDone;
    CheckBox mImageViewEyes;
    TextView mTextViewErrorPasswordForgotpassword;
    TextView mTextViewErrorPasswordForgotpasswordUpperCase;
    TextView mTextViewErrorPasswordForgotpasswordLowerCase;
    TextView mTextViewErrorPasswordForgotpasswordDigit;
    LinearLayout mLinearLayoutPasswordRequirment;

    String mStringGetNewPassword;
    Bundle mBundle;
    String mStringGetEmailId;

    ServerResponseVO mServerResponseVO;
    PostParseGet mPostParseGet;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = (MainActivity) getActivity();
        mCommonHelper = new CommonHelper();
        mPostParseGet = new PostParseGet(mainActivity);


        mBundle = getArguments();

        if (mBundle != null) {
            mStringGetEmailId =getArguments().getString("EmailId");
            Utility.debugger("VT reset password email...."+ mStringGetEmailId);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmnetview =inflater.inflate(R.layout.fragment_reset_password, container, false);

        mEditTextNewPassword=fragmnetview.findViewById(R.id.fragment_reset_password_edttext_NewPassword);
        mTextInputLayoutNewPassword =fragmnetview.findViewById(R.id.fragment_resetpassword_input_Newpassowrd);
        mTextViewDone =fragmnetview.findViewById(R.id.fragment_reset_password_textview_done);
        mImageViewEyes =fragmnetview.findViewById(R.id.fragment_reset_password_imageview_eye_login);
        mTextViewErrorPasswordForgotpassword = fragmnetview.findViewById(R.id.fragment_reset_password_textview_error_passowrd_charter);
        mTextViewErrorPasswordForgotpasswordUpperCase = fragmnetview.findViewById(R.id.fragment_reset_password_textview_error_passowrd_uppercase);
        mTextViewErrorPasswordForgotpasswordLowerCase = fragmnetview.findViewById(R.id.fragment_reset_password_textview_error_passowrd_lowercase);
        mTextViewErrorPasswordForgotpasswordDigit = fragmnetview.findViewById(R.id.fragment_reset_password_textview_error_passowrd_digit);
        mLinearLayoutPasswordRequirment=fragmnetview.findViewById(R.id.fragment_reset_password_linear_password_requerment);

        mImageViewBack =fragmnetview.findViewById(R.id.fragment_reset_password_image_back);
        mImageViewBack.setVisibility(View.VISIBLE);
        mTextViewTitle =fragmnetview.findViewById(R.id.fragment_reset_password_title_textview);
        mTextViewTitle.setText("Reset Password");

        mImageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.onBackPressed();
            }
        });

        mImageViewEyes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mImageViewEyes.setBackground(getResources().getDrawable(R.drawable.ic_password_eye));
                    mEditTextNewPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    mEditTextNewPassword.setSelection(mEditTextNewPassword.getText().length());
                } else {
                    mImageViewEyes.setBackground(getResources().getDrawable(R.drawable.ic_password_eye_hide));
                    mEditTextNewPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    mEditTextNewPassword.setSelection(mEditTextNewPassword.getText().length());
                }
            }
        });

        mEditTextNewPassword.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                mLinearLayoutPasswordRequirment.setVisibility(View.VISIBLE);

                if (s.length() > 0) {

                    if (!(s.length() >= 8)) {
                        mTextViewErrorPasswordForgotpassword.setText(getResources().getString(R.string.password_min_characters));
                        mTextViewErrorPasswordForgotpassword.setTextColor(Color.RED);
                    } else {
                        mTextViewErrorPasswordForgotpassword.setText(getResources().getString(R.string.password_min_characters));
                        mTextViewErrorPasswordForgotpassword.setTextColor(getResources().getColor(R.color.gray));
                    }

                    Pattern digitCasePatten = Pattern.compile("[0-9]");
                    if (!digitCasePatten.matcher(s).find())
                    {
                        mTextViewErrorPasswordForgotpasswordDigit.setText(getResources().getString(R.string.digit_text));
                        mTextViewErrorPasswordForgotpasswordDigit.setTextColor(Color.RED);
                    }
                    else {
                        mTextViewErrorPasswordForgotpasswordDigit.setText(getResources().getString(R.string.digit_text));
                        mTextViewErrorPasswordForgotpasswordDigit.setTextColor(getResources().getColor(R.color.gray));
                    }

                    Pattern UpperCasePatten = Pattern.compile("[A-Z]");
                    if (!UpperCasePatten.matcher(s).find())
                    {
                        mTextViewErrorPasswordForgotpasswordUpperCase.setText(getResources().getString(R.string.uppercase_text));
                        mTextViewErrorPasswordForgotpasswordUpperCase.setTextColor(Color.RED);
                    }
                    else {
                        mTextViewErrorPasswordForgotpasswordUpperCase.setText(getResources().getString(R.string.uppercase_text));
                        mTextViewErrorPasswordForgotpasswordUpperCase.setTextColor(getResources().getColor(R.color.gray));
                    }

                    Pattern lowerCasePatten = Pattern.compile("[a-z]");
                    if (!lowerCasePatten.matcher(s).find())
                    {
                        mTextViewErrorPasswordForgotpasswordLowerCase.setText(getResources().getString(R.string.lowerscase_text));
                        mTextViewErrorPasswordForgotpasswordLowerCase.setTextColor(Color.RED);
                    }
                    else {
                        mTextViewErrorPasswordForgotpasswordLowerCase.setText(getResources().getString(R.string.lowerscase_text));
                        mTextViewErrorPasswordForgotpasswordLowerCase.setTextColor(getResources().getColor(R.color.gray));
                    }
                } else {
                    mTextViewErrorPasswordForgotpassword.setTextColor(getResources().getColor(R.color.gray));
                    mTextViewErrorPasswordForgotpasswordDigit.setTextColor(getResources().getColor(R.color.gray));
                    mTextViewErrorPasswordForgotpasswordUpperCase.setTextColor(getResources().getColor(R.color.gray));
                    mTextViewErrorPasswordForgotpasswordLowerCase.setTextColor(getResources().getColor(R.color.gray));
                }
            }
        });

        mTextViewDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!mCommonHelper.check_Internet(getActivity())) {
                    Snackbar.with(mainActivity).text(getResources().getString(R.string.interneterror)).show(mainActivity);
                    return;
                }

                mStringGetNewPassword =mEditTextNewPassword.getText().toString().trim();

                ResetPasswordexecute();
            }
        });

        return fragmnetview;
    }


    public void onResumeData() {
        mainActivity.toolbar.setVisibility(View.GONE);
    }


    @Override
    public void onResume() {
        super.onResume();
        mainActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        onResumeData();
    }

    @Override
    public void doWork() {
        onResumeData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mCommonHelper.hideKeyboard(mainActivity);
    }


    public class ResetPassword extends TaskExecutor {
        protected ResetPassword(Context context, Bundle bundle) {
            super(context, bundle);
        }

        @Override
        protected Object executeTask() {
            try {
                ResetPasswordInfoService mResetPasswordInfoService = new ResetPasswordInfoService();
                mServerResponseVO = mResetPasswordInfoService.fetchSignupUserInformation(mainActivity, Tags.SetNewPassword, mStringGetEmailId,mStringGetNewPassword);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public void ResetPasswordexecute() {

        getLoaderManager().restartLoader(0, null, new LoaderManager.LoaderCallbacks<TaskExecutor>() {
            @Override
            public Loader<TaskExecutor> onCreateLoader(int arg0, Bundle arg1) {

                ResetPassword mVerifyOtp = new ResetPassword(mainActivity, null);
                return new LoaderTask<TaskExecutor>(mainActivity, mVerifyOtp);
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
                                Handler handler = new Handler() {
                                    @Override
                                    public void handleMessage(Message msg) {
                                        if (msg.what == Tags.WHAT) {

                                            SweetAlertDialogSingleButton mSweetAlertDialog = new SweetAlertDialogSingleButton(mainActivity)
                                                    .setTitleText(getResources().getString(R.string.password_msg))
                                                    .setConfirmText("Ok")
                                                    .setConfirmClickListener(new SweetAlertDialogSingleButton.OnSweetClickListener() {
                                                        @Override
                                                        public void onClick(SweetAlertDialogSingleButton sDialog) {
                                                            sDialog.dismissWithAnimation();
                                                            if (mServerResponseVO.getIsActive().equalsIgnoreCase("true")){
                                                                FragmentLoginScreen mResetPassword = new FragmentLoginScreen();
                                                                mainActivity.addFragment(mResetPassword, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, mResetPassword.getClass().getName());
                                                            }
                                                        }
                                                    });
                                            mSweetAlertDialog.setCancelable(false);
                                            mSweetAlertDialog.show();
                                            return;

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