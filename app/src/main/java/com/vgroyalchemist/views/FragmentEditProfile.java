package com.vgroyalchemist.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
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
import com.vgroyalchemist.utils.Tags;
import com.vgroyalchemist.vos.GetLoginData;
import com.vgroyalchemist.vos.ServerResponseVO;
import com.vgroyalchemist.webservice.CheckMobileNoService;
import com.vgroyalchemist.webservice.FetchGetProfilenfoService;
import com.vgroyalchemist.webservice.PostParseGet;
import com.vgroyalchemist.webservice.UpdateProfileInfoService;

/* developed by krishna 24-03-2021
*  edit profile data */

public class FragmentEditProfile extends NonCartFragment {


    public static final String FRAGMENT_ID = "7";

    public static MainActivity mainActivity;
    CommonHelper mCommonHelper;
    View fragmnetview = null;

    ImageView mImageViewBack;

    TextView mTextViewTitle;

    EditText mEditTextFirstName;
    EditText mEditTextLastName;
    EditText mEditTextMobileNo;
    EditText mEditTextEmailId;

    TextView mTextViewSubmit;
    ImageView mImageViewSearch;
    ImageView mImageViewCart;
    String mStringFirstName;
    String mStringLastName;
    String mStringMobileNo;
    String mStringEmailId;

    GetLoginData mGetLoginData;
    PostParseGet mPostParseGet;
    ServerResponseVO mServerResponseVOupdate;
    ServerResponseVO mServerResponseVO;
    ServerResponseVO mServerResponseVOCheckMobileNo;
    // userdata
    public SharedPreferences mSharedPreferencesUserId;
    SharedPreferences.Editor mEditor;

    String GetUserId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mainActivity = (MainActivity) getActivity();
        mCommonHelper = new CommonHelper();
        mGetLoginData= new GetLoginData();
        mPostParseGet = new PostParseGet(mainActivity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmnetview = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        mImageViewBack = fragmnetview.findViewById(R.id.fragment_imageview_back);
        mTextViewTitle =fragmnetview.findViewById(R.id.fragment_title_textview);
        mTextViewTitle.setText("Edit Profile");
        mImageViewSearch =fragmnetview.findViewById(R.id.fragment_Serach);
        mImageViewSearch.setVisibility(View.GONE);
        mImageViewCart =fragmnetview.findViewById(R.id.fragment_cart);
        mImageViewCart.setVisibility(View.GONE);

        mEditTextFirstName =fragmnetview.findViewById(R.id.fragment_Edit_Profile_edtext_FirstName);
        mEditTextLastName =fragmnetview.findViewById(R.id.fragment_Edit_Profile_edtext_LastName);
        mEditTextMobileNo =fragmnetview.findViewById(R.id.fragment_Edit_profile_edtext_MobileNo);
        mEditTextEmailId =fragmnetview.findViewById(R.id.fragment_Edit_profile_edtext_EmailId);
        mTextViewSubmit =fragmnetview.findViewById(R.id.fragment_Edit_profile_textview_submit);

        mEditTextEmailId.setFocusable(false);

        mSharedPreferencesUserId =mainActivity.getSharedPreferences(Tags.TAG_SHARED_PREFERENCE_USER_ID ,0);
        GetUserId = mSharedPreferencesUserId.getString("UserId","");
        mEditor =mSharedPreferencesUserId.edit();


        mGetLoginData = mPostParseGet.getUserDataObj(mainActivity);
        mImageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.onBackPressed();
            }
        });

        GetProfileData();



        mTextViewSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!mCommonHelper.check_Internet(getActivity())) {
                    Snackbar.with(mainActivity).text(getResources().getString(R.string.interneterror)).show(mainActivity);
                    return;
                }

                mStringFirstName =mEditTextFirstName.getText().toString().trim();
                mStringLastName =mEditTextLastName.getText().toString().trim();
                mStringMobileNo =mEditTextMobileNo.getText().toString().trim();
                mStringEmailId =mEditTextEmailId.getText().toString().trim();

                CheckMobileNo();
//                UpdateUserProfile();
            }
        });

        return fragmnetview;
    }

    public void GetProfileData() {
        getLoaderManager().restartLoader(0, null, new LoaderManager.LoaderCallbacks<TaskExecutor>() {
            @Override
            public Loader<TaskExecutor> onCreateLoader(int arg0, Bundle arg1) {

                GetProfileExecuter mGetProfileExecuter = new GetProfileExecuter(mainActivity, null);
                return new LoaderTask<TaskExecutor>(mainActivity, mGetProfileExecuter);
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

                            mEditTextFirstName.setText(mServerResponseVO.getFirstName() );
                            mEditTextLastName.setText(mServerResponseVO.getLastName());
                            mEditTextMobileNo.setText(mServerResponseVO.getMobileNo());
                            mEditTextEmailId.setText(mServerResponseVO.getEmailId());
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
    public class GetProfileExecuter extends TaskExecutor {
        protected GetProfileExecuter(Context context, Bundle bundle) {
            super(context, bundle);
        }

        @Override
        protected Object executeTask() {

            FetchGetProfilenfoService fetchGetProfilenfoService = new FetchGetProfilenfoService();
            mServerResponseVO = fetchGetProfilenfoService.fetchLoginUserInformation(mainActivity, Tags.GetProfile, GetUserId);

            return null;
        }
    }

//update user profile data
    public void UpdateUserProfile() {

        getLoaderManager().restartLoader(0, null, new LoaderManager.LoaderCallbacks<TaskExecutor>() {
            @Override
            public Loader<TaskExecutor> onCreateLoader(int arg0, Bundle arg1) {

                UpdateProfile mUpdateProfile = new UpdateProfile(mainActivity, null);
                return new LoaderTask<TaskExecutor>(mainActivity, mUpdateProfile);
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

                        if (mServerResponseVOupdate.getStatus().equalsIgnoreCase("true")) {
//                            Snackbar.with(mainActivity).text(mServerResponseVO.getMsg()).show(mainActivity);
                            mCommonHelper.hideKeyboard(mainActivity);


                            mServerResponseVOupdate.getData();
                            Handler handler = new Handler() {
                                @Override
                                public void handleMessage(Message msg) {
                                    if (msg.what == Tags.WHAT) {

//                                        mainActivity.onBackPressed();
                                        mainActivity.removeAllBackFragments();
                                        FragmentProfile mFragmentProfile = new FragmentProfile();
                                        mainActivity.addFragment(mFragmentProfile, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, mFragmentProfile.getClass().getName());
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
    public class UpdateProfile extends TaskExecutor {
        protected UpdateProfile(Context context, Bundle bundle) {
            super(context, bundle);
        }

        @Override
        protected Object executeTask() {

            mStringFirstName =mEditTextFirstName.getText().toString().trim();
            mStringLastName =mEditTextLastName.getText().toString().trim();
            mStringMobileNo =mEditTextMobileNo.getText().toString().trim();
            mStringEmailId =mEditTextEmailId.getText().toString().trim();

            UpdateProfileInfoService fetchGetProfilenfoService = new UpdateProfileInfoService();
            mServerResponseVOupdate = fetchGetProfilenfoService.fetchLoginUserInformation(mainActivity, Tags.UpdateProfile, GetUserId ,mStringFirstName,mStringLastName,mStringMobileNo,mStringEmailId);

            return null;
        }
    }

//    check mobile number change

    public void CheckMobileNo() {

        getLoaderManager().restartLoader(0, null, new LoaderManager.LoaderCallbacks<TaskExecutor>() {
            @Override
            public Loader<TaskExecutor> onCreateLoader(int arg0, Bundle arg1) {

                CheckMobileNo checkMobileNo = new CheckMobileNo(mainActivity, null);
                return new LoaderTask<TaskExecutor>(mainActivity, checkMobileNo);
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

                        if (mServerResponseVOCheckMobileNo.getStatus().equalsIgnoreCase("true")) {
//                            Snackbar.with(mainActivity).text(mServerResponseVO.getMsg()).show(mainActivity);
                            mCommonHelper.hideKeyboard(mainActivity);


                            mServerResponseVOCheckMobileNo.getData();
                            if(mServerResponseVOCheckMobileNo != null) {

                                if(mServerResponseVOCheckMobileNo.getCheckOTP().equalsIgnoreCase("true"))
                                {
                                    UpdateUserProfile();
                                }
                                else {
                                    Handler handler = new Handler() {
                                        @Override
                                        public void handleMessage(Message msg) {
                                            if (msg.what == Tags.WHAT) {

                                                FragmentVerificationMoblieNo fragmentVerificationMoblieNo = new FragmentVerificationMoblieNo();
                                                Bundle args = new Bundle();
                                                args.putString("mobileno", mStringMobileNo);
                                                args.putString("Emailid", mStringEmailId);
                                                fragmentVerificationMoblieNo.setArguments(args);
                                                mainActivity.addFragment(fragmentVerificationMoblieNo, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, fragmentVerificationMoblieNo.getClass().getName());


                                            }
                                        }
                                    };
                                    handler.sendEmptyMessage(Tags.WHAT);
                                }
                            }
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
    public class CheckMobileNo extends TaskExecutor {
        protected CheckMobileNo(Context context, Bundle bundle) {
            super(context, bundle);
        }

        @Override
        protected Object executeTask() {

            CheckMobileNoService checkMobileNoService = new CheckMobileNoService();
            mServerResponseVOCheckMobileNo = checkMobileNoService.CheckMobileNo(mainActivity, Tags.CheckProfileMobileNo,GetUserId,mStringMobileNo);

            return null;
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
}