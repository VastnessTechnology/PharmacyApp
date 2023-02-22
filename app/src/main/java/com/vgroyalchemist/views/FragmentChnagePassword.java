package com.vgroyalchemist.views;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
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

import com.google.android.material.textfield.TextInputLayout;
import com.vgroyalchemist.R;
import com.vgroyalchemist.controller.MainActivity;
import com.vgroyalchemist.loaders.LoaderTask;
import com.vgroyalchemist.loaders.TaskExecutor;
import com.vgroyalchemist.utils.CommonHelper;
import com.vgroyalchemist.utils.Snackbar;
import com.vgroyalchemist.utils.Tags;
import com.vgroyalchemist.vos.ServerResponseVO;
import com.vgroyalchemist.webservice.ChnagePassowrdInfoService;
import com.vgroyalchemist.webservice.PostParseGet;

import java.util.regex.Pattern;

import cz.msebera.android.httpclient.util.TextUtils;

/*  developed by krishna 24-03-2021
*  chnage user password*/
public class FragmentChnagePassword extends NonCartFragment {


    public static final String FRAGMENT_ID = "9";

    public static MainActivity mainActivity;
    CommonHelper mCommonHelper;
    View fragmnetview = null;

    ImageView mImageViewBack;
    TextView mTextViewTitle;
    ImageView mImageViewSearch;
    ImageView mImageViewCart;

    EditText mEditTextCurrentPasword;
    EditText mEditTextNewPassword;
    EditText mEditTextReTypePassword;

    TextInputLayout mTextInputLayoutCurrentPassword;
    TextInputLayout mTextInputLayoutNewPassword;
    TextInputLayout mTextInputLayoutReTypePassword;

    TextView mTextViewSubmit;
    LinearLayout mLinearLayoutErrorMsg;
    TextView mTextViewChnagePasswordChar;
    TextView mTextViewChnagePaswordDigit;
    TextView mTextViewChnagePasswordUppercase;
    TextView mTextViewChnagePasswordLowerCase;

    CheckBox mCheckBoxCurrentPassword;
    CheckBox mCheckBoxNewPassword;
    CheckBox mCheckBoxReTypePassword;

    String mStringCurrentPassword;
    String mStringNewPassword;
    String mStringReTypePassword;

    // userdata
    public SharedPreferences mSharedPreferencesUserId;
    SharedPreferences.Editor mEditor;
    PostParseGet mPostParseGet;
    ServerResponseVO mServerResponseVO;
    String GetUserId;

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
        fragmnetview = inflater.inflate(R.layout.fragment_chnage_password, container, false);

        mImageViewBack = fragmnetview.findViewById(R.id.fragment_imageview_back);
        mTextViewTitle =fragmnetview.findViewById(R.id.fragment_title_textview);
        mTextViewTitle.setText("Change Password");
        mImageViewSearch =fragmnetview.findViewById(R.id.fragment_Serach);
        mImageViewSearch.setVisibility(View.GONE);
        mImageViewCart =fragmnetview.findViewById(R.id.fragment_cart);
        mImageViewCart.setVisibility(View.GONE);

        mEditTextCurrentPasword = fragmnetview.findViewById(R.id.fragment_Chnage_Password_edittext_Currentpassword);
        mEditTextNewPassword =fragmnetview.findViewById(R.id.fragment_Chnage_Password_edittext_Newpassword);
        mEditTextReTypePassword =fragmnetview.findViewById(R.id.fragment_Chnage_Password_edittext_Retype__Newpassword);

        mTextInputLayoutCurrentPassword =fragmnetview.findViewById(R.id.fragment_Chnage_Password_input_layout_Currentpassword);
        mTextInputLayoutNewPassword =fragmnetview.findViewById(R.id.fragment_Chnage_Password_input_layout_Newpassword);
        mTextInputLayoutReTypePassword =fragmnetview.findViewById(R.id.fragment_Chnage_Password_input_layout_Retype__Newpassword);

        mTextViewSubmit=fragmnetview.findViewById(R.id.fragment_Change_Password_textview_submit);

        mCheckBoxCurrentPassword =fragmnetview.findViewById(R.id.fragment_Change_Password_imageview_eye_CurrentPassword);
        mCheckBoxNewPassword =fragmnetview.findViewById(R.id.fragment_Change_Password_imageview_eye_NewPassword);
        mCheckBoxReTypePassword =fragmnetview.findViewById(R.id.fragment_Change_Password_imageview_eye_Retype__NewPassword);
        mLinearLayoutErrorMsg = fragmnetview.findViewById(R.id.fragment_Chnage_password_linear_password_requerment);
        mTextViewChnagePasswordChar =fragmnetview.findViewById(R.id.fragment_Chnage_password_textview_error_charter);
        mTextViewChnagePasswordLowerCase =fragmnetview.findViewById(R.id.fragment_Chnage_password_textview_error_lowercase);
        mTextViewChnagePasswordUppercase =fragmnetview.findViewById(R.id.fragment_Chaage_password_textview_error_uppercase);
        mTextViewChnagePaswordDigit =fragmnetview.findViewById(R.id.fragment_Chnage_password_textview_error_digit);

        mSharedPreferencesUserId =mainActivity.getSharedPreferences(Tags.TAG_SHARED_PREFERENCE_USER_ID ,0);
        GetUserId = mSharedPreferencesUserId.getString("UserId","");
        mEditor =mSharedPreferencesUserId.edit();


        mEditTextNewPassword.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                mLinearLayoutErrorMsg.setVisibility(View.VISIBLE);

                if (s.length() > 0) {

                    if (!(s.length() >= 8)) {
                        mTextViewChnagePasswordChar.setText(getResources().getString(R.string.password_min_characters));
                        mTextViewChnagePasswordChar.setTextColor(Color.RED);
                    } else {
                        mTextViewChnagePasswordChar.setText(getResources().getString(R.string.password_min_characters));
                        mTextViewChnagePasswordChar.setTextColor(getResources().getColor(R.color.gray));
                    }

                    Pattern digitCasePatten = Pattern.compile("[0-9]");
                    if (!digitCasePatten.matcher(s).find())
                    {
                        mTextViewChnagePaswordDigit.setText(getResources().getString(R.string.digit_text));
                        mTextViewChnagePaswordDigit.setTextColor(Color.RED);
                    }
                    else {
                        mTextViewChnagePaswordDigit.setText(getResources().getString(R.string.digit_text));
                        mTextViewChnagePaswordDigit.setTextColor(getResources().getColor(R.color.gray));
                    }

                    Pattern UpperCasePatten = Pattern.compile("[A-Z]");
                    if (!UpperCasePatten.matcher(s).find())
                    {
                        mTextViewChnagePasswordUppercase.setText(getResources().getString(R.string.uppercase_text));
                        mTextViewChnagePasswordUppercase.setTextColor(Color.RED);
                    }
                    else {
                        mTextViewChnagePasswordUppercase.setText(getResources().getString(R.string.uppercase_text));
                        mTextViewChnagePasswordUppercase.setTextColor(getResources().getColor(R.color.gray));
                    }

                    Pattern lowerCasePatten = Pattern.compile("[a-z]");
                    if (!lowerCasePatten.matcher(s).find())
                    {
                        mTextViewChnagePasswordLowerCase.setText(getResources().getString(R.string.lowerscase_text));
                        mTextViewChnagePasswordLowerCase.setTextColor(Color.RED);
                    }
                    else {
                        mTextViewChnagePasswordLowerCase.setText(getResources().getString(R.string.lowerscase_text));
                        mTextViewChnagePasswordLowerCase.setTextColor(getResources().getColor(R.color.gray));
                    }
                } else {
                    mTextViewChnagePasswordChar.setTextColor(getResources().getColor(R.color.gray));
                    mTextViewChnagePaswordDigit.setTextColor(getResources().getColor(R.color.gray));
                    mTextViewChnagePasswordUppercase.setTextColor(getResources().getColor(R.color.gray));
                    mTextViewChnagePasswordLowerCase.setTextColor(getResources().getColor(R.color.gray));
                }
            }
        });

        mCheckBoxCurrentPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mCheckBoxCurrentPassword.setBackground(getResources().getDrawable(R.drawable.ic_password_eye));
                    mEditTextCurrentPasword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    mEditTextCurrentPasword.setSelection(mEditTextCurrentPasword.getText().length());
                } else {
                    mCheckBoxCurrentPassword.setBackground(getResources().getDrawable(R.drawable.ic_password_eye_hide));
                    mEditTextCurrentPasword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    mEditTextCurrentPasword.setSelection(mEditTextCurrentPasword.getText().length());
                }
            }
        });



        mCheckBoxNewPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mCheckBoxNewPassword.setBackground(getResources().getDrawable(R.drawable.ic_password_eye));
                    mEditTextNewPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    mEditTextNewPassword.setSelection(mEditTextNewPassword.getText().length());
                } else {
                    mCheckBoxNewPassword.setBackground(getResources().getDrawable(R.drawable.ic_password_eye_hide));
                    mEditTextNewPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    mEditTextNewPassword.setSelection(mEditTextNewPassword.getText().length());
                }
            }
        });

        mCheckBoxReTypePassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mCheckBoxReTypePassword.setBackground(getResources().getDrawable(R.drawable.ic_password_eye));
                    mEditTextReTypePassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    mEditTextReTypePassword.setSelection(mEditTextReTypePassword.getText().length());
                } else {
                    mCheckBoxReTypePassword.setBackground(getResources().getDrawable(R.drawable.ic_password_eye_hide));
                    mEditTextReTypePassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    mEditTextReTypePassword.setSelection(mEditTextReTypePassword.getText().length());
                }
            }
        });

        mTextViewSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mCommonHelper.check_Internet(getActivity())) {
                    Snackbar.with(mainActivity).text(getResources().getString(R.string.interneterror)).show(mainActivity);
                    return;
                }

                mStringCurrentPassword = mEditTextCurrentPasword.getText().toString().trim();
                mStringNewPassword =mEditTextNewPassword.getText().toString().trim();
                mStringReTypePassword =mEditTextReTypePassword.getText().toString().trim();

                if(!validCurrentPassword())
                {
                    return;
                }
                if(!validNewPassword())
                {
                    return;
                }
                if(!validReTypePassword())
                {
                    return;
                }

                if (!TextUtils.isEmpty(mStringNewPassword) && !TextUtils.isEmpty(mStringReTypePassword)) {
                    if (!mStringNewPassword.equals(mStringReTypePassword)) {
                        mTextInputLayoutReTypePassword.setError(getResources().getString(R.string.validconfrimPassowrd));
                        requestFocus(mEditTextReTypePassword);
                        mCommonHelper.hideKeyboard(mainActivity);
                        return;
                    }
                }

                ChnagePassword();
            }
        });
        mImageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.onBackPressed();
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

    private boolean validCurrentPassword() {

        if (mEditTextCurrentPasword.getText().toString().trim().isEmpty()) {
            mTextInputLayoutCurrentPassword.setError(getResources().getString(R.string.validCurrentPassowrd));
            requestFocus(mEditTextCurrentPasword);
            mCommonHelper.hideKeyboard(mainActivity);
            return false;
        } else {
            mTextInputLayoutCurrentPassword.setErrorEnabled(false);
        }

        return true;
    }


    private boolean validNewPassword() {

        if (mEditTextNewPassword.getText().toString().trim().isEmpty()) {
            mTextInputLayoutNewPassword.setError(getResources().getString(R.string.validPassword));
            requestFocus(mEditTextNewPassword);
            mCommonHelper.hideKeyboard(mainActivity);
            return false;
        } else {
            mTextInputLayoutNewPassword.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validReTypePassword() {

        if (mEditTextReTypePassword.getText().toString().trim().isEmpty()) {
            mTextInputLayoutReTypePassword.setError(getResources().getString(R.string.validPassword));
            requestFocus(mEditTextReTypePassword);
            mCommonHelper.hideKeyboard(mainActivity);
            return false;
        } else {
            mTextInputLayoutReTypePassword.setErrorEnabled(false);
        }

        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            mainActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }


    public void ChnagePassword() {

        getLoaderManager().restartLoader(0, null, new LoaderManager.LoaderCallbacks<TaskExecutor>() {
            @Override
            public Loader<TaskExecutor> onCreateLoader(int arg0, Bundle arg1) {

                ChnagePasswordExecutor mChnagePasswordExecutor = new ChnagePasswordExecutor(mainActivity, null);
                return new LoaderTask<TaskExecutor>(mainActivity, mChnagePasswordExecutor);
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
//                            Snackbar.with(mainActivity).text(mServerResponseVO.getMsg()).show(mainActivity);
                            mCommonHelper.hideKeyboard(mainActivity);


                            mServerResponseVO.getData();
                            Handler handler = new Handler() {
                                @Override
                                public void handleMessage(Message msg) {
                                    if (msg.what == Tags.WHAT) {
                                        mainActivity.onBackPressed();
                                    }
                                }
                            };
                            handler.sendEmptyMessage(Tags.WHAT);

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
    public class ChnagePasswordExecutor extends TaskExecutor {
        protected ChnagePasswordExecutor(Context context, Bundle bundle) {
            super(context, bundle);
        }

        @Override
        protected Object executeTask() {

            ChnagePassowrdInfoService addAddressInfoService = new ChnagePassowrdInfoService();

            mServerResponseVO = addAddressInfoService.fetchLoginUserInformation(mainActivity, Tags.ChnagePassword, GetUserId ,mStringCurrentPassword,mStringNewPassword);

            return null;
        }
    }

}