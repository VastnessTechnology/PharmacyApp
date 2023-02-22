package com.vgroyalchemist.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

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
import com.vgroyalchemist.vos.ServerResponseVO;
import com.vgroyalchemist.webservice.FetchGetProfilenfoService;
import com.vgroyalchemist.webservice.PostParseGet;

/* developed by krishna 24-03-2021
*  display user details , edit profile , manage address , chnage password*/
public class FragmentProfile extends NonCartFragment {


    public static final String FRAGMENT_ID = "6";

    public static MainActivity mainActivity;
    CommonHelper mCommonHelper;
    View fragmnetview = null;

    ImageView mImageViewBack;
    TextView mTextViewTitle;
    ImageView mImageViewSearch;
    ImageView mImageViewCart;
    ImageView mImageViewEditProfile;
    ImageView mImageViewAddress;
    ImageView mImageViewChnagePassowrd;

    EditText mEditTextName;
    EditText mEditTextMobileNo;
    EditText mEditTextEmailId;
    PostParseGet mPostParseGet;
    ServerResponseVO mServerResponseVO;
    // userdata
    public SharedPreferences mSharedPreferencesUserId;
    SharedPreferences.Editor mEditor;

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
        // Inflate the layout for this fragment

        fragmnetview = inflater.inflate(R.layout.fragment_profile, container, false);

        mImageViewBack = fragmnetview.findViewById(R.id.fragment_imageview_back);
        mTextViewTitle =fragmnetview.findViewById(R.id.fragment_title_textview);
        mTextViewTitle.setText("User Profile");
        mImageViewSearch =fragmnetview.findViewById(R.id.fragment_Serach);
        mImageViewSearch.setVisibility(View.GONE);
        mImageViewCart =fragmnetview.findViewById(R.id.fragment_cart);
        mImageViewCart.setVisibility(View.GONE);

        mImageViewEditProfile =fragmnetview.findViewById(R.id.fragment_profile_imageview_edit_profile);
        mImageViewAddress =fragmnetview.findViewById(R.id.fragment_profile_imageview_manage_address);
        mImageViewChnagePassowrd =fragmnetview.findViewById(R.id.fragment_profile_imageview_chnage_password);

        mEditTextName = fragmnetview.findViewById(R.id.fragment_Profile_edtext_Name);
        mEditTextMobileNo =fragmnetview.findViewById(R.id.fragment_profile_edtext_MobileNo);
        mEditTextEmailId =fragmnetview.findViewById(R.id.fragment_profile_edtext_EmailId);

        mEditTextName.setFocusable(false);
        mEditTextMobileNo.setFocusable(false);
        mEditTextEmailId.setFocusable(false);

        mSharedPreferencesUserId =mainActivity.getSharedPreferences(Tags.TAG_SHARED_PREFERENCE_USER_ID ,0);
        GetUserId = mSharedPreferencesUserId.getString("UserId","");
        mEditor =mSharedPreferencesUserId.edit();


        mImageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.onBackPressed();
            }
        });
        mImageViewEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentEditProfile mFragmentEditProfile = new FragmentEditProfile();
               mainActivity.addFragment(mFragmentEditProfile, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, mFragmentEditProfile.getClass().getName());
            }
        });

        mImageViewAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment currentFrag =mainActivity.getSupportFragmentManager().findFragmentById(R.id.activity_main_container);
                mCurrentFragment = currentFrag.getClass().getName();

                if (!mCurrentFragment.equalsIgnoreCase(Tags.TAG_FRAGEMENT_ADD_ADDRESS)) {

//                    FragmentAddAddress mFragmentAddAddress = new FragmentAddAddress();
//                    Bundle mBundle = new Bundle();
//                    mBundle.putBoolean("fromMenu",true);
//                    mBundle.putBoolean(Tags.isFromEdit, false);
//                    mFragmentAddAddress.setArguments(mBundle);
//                    mainActivity.addFragment(mFragmentAddAddress, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, mFragmentAddAddress.getClass().getName());

                    FragmentSelectAddress mFragmentSelectAddress = new FragmentSelectAddress();
                    Bundle mBundle = new Bundle();
                    mBundle.putBoolean("fromMenu",true);
                    mFragmentSelectAddress.setArguments(mBundle);
                    mainActivity.addFragment(mFragmentSelectAddress, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN,mFragmentSelectAddress.getClass().getName());
                }

            }
        });

        mImageViewChnagePassowrd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentChnagePassword mFragmentChnagePassword = new FragmentChnagePassword();
                mainActivity.addFragment(mFragmentChnagePassword, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, mFragmentChnagePassword.getClass().getName());
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
                        try {

                        if (mServerResponseVO.getStatus().equalsIgnoreCase("true")) {
//                            Snackbar.with(mainActivity).text(mServerResponseVO.getMsg()).show(mainActivity);
                            mCommonHelper.hideKeyboard(mainActivity);


                            mServerResponseVO.getData();
                            mEditTextName.setText(mServerResponseVO.getFirstName() + " " + mServerResponseVO.getLastName());
                            mEditTextMobileNo.setText(mServerResponseVO.getMobileNo());
                            mEditTextEmailId.setText(mServerResponseVO.getEmailId());
                        } else {
                            Snackbar.with(mainActivity).text(mServerResponseVO.getMsg()).show(mainActivity);
                            mCommonHelper.hideKeyboard(mainActivity);
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


    public void onResumeData() {
        mainActivity.toolbar.setVisibility(View.GONE);

        GetProfileData();
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