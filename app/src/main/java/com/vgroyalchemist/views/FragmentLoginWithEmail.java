package com.vgroyalchemist.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.vgroyalchemist.R;
import com.vgroyalchemist.controller.MainActivity;
import com.vgroyalchemist.loaders.LoaderTask;
import com.vgroyalchemist.loaders.TaskExecutor;
import com.vgroyalchemist.utils.CommonHelper;
import com.vgroyalchemist.utils.Snackbar;
import com.vgroyalchemist.utils.Tags;
import com.vgroyalchemist.vos.ServerResponseVO;
import com.vgroyalchemist.webservice.CheckEmailInfoService;
import com.vgroyalchemist.webservice.PostParseGet;

/**
 * developed by krishna 08-01-22
 * *  Login With Email
 */
public class
FragmentLoginWithEmail extends NonCartFragment {

    public static final String FRAGMENT_ID = "68";
    public static MainActivity mainActivity;
    CommonHelper mCommonHelper;
    View fragmnetView = null;
    PostParseGet mPostParseGet;
    ImageView mImageViewBack;

    TextInputEditText mEditTextEmailId;
    TextInputLayout mTextInputLayoutEmailId;
    TextView mTextViewSubmit;

    String mStringEmailId;
    String mStringMobileNo;

    public ServerResponseVO mServerResponseVO;
    public SharedPreferences mSharedPreferencesUserId;
    public SharedPreferences.Editor mEditor;
    String mStringUserid;

    Bundle mBundle;
    boolean IsLoginEmail;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = (MainActivity) getActivity();
        mCommonHelper = new CommonHelper();
        mPostParseGet = new PostParseGet(mainActivity);
        mServerResponseVO = new ServerResponseVO();

        mBundle = getArguments();
        if (mBundle != null) {
            IsLoginEmail = mBundle.getBoolean("IsLoginEmail");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmnetView = inflater.inflate(R.layout.fragment_login_with_email, container, false);

        mImageViewBack = fragmnetView.findViewById(R.id.fragment_LoginWithEmail_image_back);
        mEditTextEmailId = fragmnetView.findViewById(R.id.fragment_LoginWithEmail_edttext_EmailId);
        mTextViewSubmit = fragmnetView.findViewById(R.id.fragment_LoginWithEmail_textview_submit);
        mTextInputLayoutEmailId = fragmnetView.findViewById(R.id.fragment_LoginWithEmail_input_EmailId);


        mSharedPreferencesUserId = mainActivity.getSharedPreferences(Tags.TAG_SHARED_PREFERENCE_USER_ID, 0);
        mEditor = mSharedPreferencesUserId.edit();
        mStringUserid = mSharedPreferencesUserId.getString("UserId", "");

        mImageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.onBackPressed();
            }
        });

        mTextViewSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mStringEmailId = mEditTextEmailId.getText().toString();
                mStringMobileNo = "";
                if (!ValidEmail()) {
                    return;
                }

                if (IsLoginEmail) {

                    mEditor.putString("EmailId", mEditTextEmailId.getText().toString());
                    mEditor.apply();
                    mainActivity.RemoveAllFragment();
                    FragmentLoginScreen fragmentLoginScreen = new FragmentLoginScreen();
                    Bundle mBundle = new Bundle();
                    mBundle.putBoolean("IsLoginEmail",true);
                    fragmentLoginScreen.setArguments(mBundle);
                    mainActivity.addFragment(fragmentLoginScreen, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, fragmentLoginScreen.getClass().getName());

                } else {

                    CheckEmailId();
                }

            }
        });
        return fragmnetView;
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


                                    mEditor.putString("EmailId", mEditTextEmailId.getText().toString());
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


    private boolean ValidEmail() {
        String email = mEditTextEmailId.getText().toString().trim();

        if (email.isEmpty()) {
            mTextInputLayoutEmailId.setError(getResources().getString(R.string.validEmail));
            requestFocus(mEditTextEmailId);
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
}