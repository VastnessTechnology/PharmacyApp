package com.vgroyalchemist.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import com.google.android.material.textfield.TextInputLayout;
import com.vgroyalchemist.R;
import com.vgroyalchemist.controller.MainActivity;
import com.vgroyalchemist.loaders.LoaderTask;
import com.vgroyalchemist.loaders.TaskExecutor;
import com.vgroyalchemist.utils.CommonHelper;
import com.vgroyalchemist.utils.Snackbar;
import com.vgroyalchemist.utils.Tags;
import com.vgroyalchemist.vos.GetLoginData;
import com.vgroyalchemist.vos.ServerResponseVO;
import com.vgroyalchemist.vos.UserDetailsVO;
import com.vgroyalchemist.webservice.AddAddressInfoService;
import com.vgroyalchemist.webservice.EditAddressInfoService;
import com.vgroyalchemist.webservice.PostParseGet;


public class FragmentEditAddress extends NonCartFragment {

    public static final String FRAGMENT_ID = "27";

    public static MainActivity mainActivity;
    CommonHelper mCommonHelper;
    View fragmnetview = null;

    ImageView mImageViewBack;
    TextView mTextViewTitle;
    TextView mTextViewsubmit;
    EditText mEditTextPinCode;
    EditText mEditTextHouseNumber;
    EditText mEditTextName;
    EditText mEditTextContactNumber;

    TextInputLayout mTextInputLayoutPincode;
    TextInputLayout mTextInputLayoutHouseNumber;
    TextInputLayout mTextInputLayoutName;
    TextInputLayout mTextInputLayoutContactNumber;

    RadioGroup mRadioGroup;
    RadioButton mRadioButtonHome;
    RadioButton mRadioButtonOffice;
    RadioButton mRadioButtonOther;

    String mStringPincode;
    String mStringHouseNumber;
    String mStringName;
    String mStringContactNumber;
    String mStringSelectLocation;
    String mStringAddressId;
    int selectedId;
    ImageView mImageViewSearch;
    ImageView mImageViewCart;

    GetLoginData mGetLoginData;
    ServerResponseVO mServerResponseVO;
    // userdata
    public SharedPreferences mSharedPreferencesUserId;
    SharedPreferences.Editor mEditor;
    PostParseGet mPostParseGet;

    String GetUserId;

    boolean isFromMenu = false;
    boolean isFromEdit =false;
    Bundle mBundle;
    UserDetailsVO mUserDetailsVO;
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
        fragmnetview = inflater.inflate(R.layout.fragment_add_address, container, false);

        mImageViewBack = fragmnetview.findViewById(R.id.fragment_imageview_back);
        mTextViewTitle =fragmnetview.findViewById(R.id.fragment_title_textview);
        mTextViewTitle.setText("Add Address");
        mImageViewSearch =fragmnetview.findViewById(R.id.fragment_Serach);
        mImageViewSearch.setVisibility(View.GONE);
        mImageViewCart =fragmnetview.findViewById(R.id.fragment_cart);
        mImageViewCart.setVisibility(View.GONE);

        mEditTextPinCode= fragmnetview.findViewById(R.id.fragment_Add_Address_edtext_Pincode);
        mEditTextHouseNumber =fragmnetview.findViewById(R.id.fragment_Add_Address_edtext_House_Name);
        mEditTextName =fragmnetview.findViewById(R.id.fragment_Add_Address_edtext_Name);
        mEditTextContactNumber =fragmnetview.findViewById(R.id.fragment_Add_Address_edtext_contact_number);

        mTextInputLayoutPincode =fragmnetview.findViewById(R.id.fragment_Add_Address_input_Pincode);
        mTextInputLayoutHouseNumber =fragmnetview.findViewById(R.id.fragment_Add_Address_input_House_Name);
        mTextInputLayoutName =fragmnetview.findViewById(R.id.fragment_Add_Address_input_Name);
        mTextInputLayoutContactNumber =fragmnetview.findViewById(R.id.fragment_Add_Address_input_contact_number);

        mRadioGroup =fragmnetview.findViewById(R.id.fragment_Add_Address_RadioGroup);
        mRadioButtonHome =fragmnetview.findViewById(R.id.fragment_Add_Address_RadioButton_Home);
        mRadioButtonOffice =fragmnetview.findViewById(R.id.fragment_Add_Address_RadioButton_Office);
        mRadioButtonOther =fragmnetview.findViewById(R.id.fragment_Add_Address_RadioButton_Other);
        mTextViewsubmit =fragmnetview.findViewById(R.id.fragment_Add_Address_textview_submit);

        mSharedPreferencesUserId =mainActivity.getSharedPreferences(Tags.TAG_SHARED_PREFERENCE_USER_ID ,0);
        GetUserId = mSharedPreferencesUserId.getString("UserId","");
        mEditor =mSharedPreferencesUserId.edit();

        mUserDetailsVO = new UserDetailsVO();
        mGetLoginData= new GetLoginData();
        mBundle = getArguments();
        if (mBundle != null) {

            mUserDetailsVO = mBundle.getParcelable(Tags.KEY_ADDRESS_DATA);

            if (mUserDetailsVO == null) {
                mUserDetailsVO = new UserDetailsVO();
            }

//            isFromMenu = getArguments().getBoolean("fromMenu");
//            isFromEdit = mBundle.getBoolean(Tags.isFromEdit);
        }

        mImageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Fragment currentFrag =mainActivity.getSupportFragmentManager().findFragmentById(R.id.activity_main_container);
//                mCurrentFragment = currentFrag.getClass().getName();
//                mainActivity. removeFragment(currentFrag, currentFrag.getClass().getName());
            }
        });

        mRadioButtonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStringSelectLocation = mRadioButtonHome.getText().toString().trim();

            }
        });
        mRadioButtonOffice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStringSelectLocation =mRadioButtonOffice.getText().toString().trim();
            }
        });
        mRadioButtonOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStringSelectLocation =mRadioButtonOther.getText().toString().trim();
            }
        });
        mTextViewsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!mCommonHelper.check_Internet(getActivity())) {
                    Snackbar.with(mainActivity).text(getResources().getString(R.string.interneterror)).show(mainActivity);
                    return;
                }

                mStringPincode = mEditTextPinCode.getText().toString().trim();
                mStringHouseNumber = mEditTextHouseNumber.getText().toString().trim();
                mStringName =mEditTextName.getText().toString().trim();
                mStringContactNumber =mEditTextContactNumber.getText().toString().trim();

                if(!validPincode())
                {
                    return;
                }
                if(!validHouseName())
                {
                    return;
                }
                if(!validName())
                {
                    return;
                }
                if(!validContactNo())
                {
                    return;
                }
                if(!validRadio())
                {
                    return;
                }
                System.out.println("VT Radio text..."+ mStringSelectLocation);

                UpdateAddress();
            }
        });


        return fragmnetview;
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

    private boolean validHouseName() {
        String housename = mEditTextHouseNumber.getText().toString().trim();

        if (housename.isEmpty()) {
            mTextInputLayoutHouseNumber.setError(getResources().getString(R.string.validaddress));
            requestFocus(mEditTextHouseNumber);
            mCommonHelper.hideKeyboard(mainActivity);
            return false;
        } else {
            mTextInputLayoutHouseNumber.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validPincode() {
        String pincode = mEditTextPinCode.getText().toString().trim();

        if (pincode.isEmpty()) {
            mTextInputLayoutPincode.setError(getResources().getString(R.string.validpincode));
            requestFocus(mEditTextPinCode);
            mCommonHelper.hideKeyboard(mainActivity);
            return false;
        } else {
            mTextInputLayoutPincode.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validName() {
        String Name = mEditTextName.getText().toString().trim();

        if (Name.isEmpty()) {
            mTextInputLayoutName.setError(getResources().getString(R.string.validName));
            requestFocus(mEditTextName);
            mCommonHelper.hideKeyboard(mainActivity);
            return false;
        } else {
            mTextInputLayoutName.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validContactNo() {
        String ContactNo = mEditTextContactNumber.getText().toString().trim();

        if (ContactNo.isEmpty()) {
            mTextInputLayoutContactNumber.setError(getResources().getString(R.string.validmobileno));
            requestFocus(mEditTextContactNumber);
            mCommonHelper.hideKeyboard(mainActivity);
            return false;
        } else {
            mTextInputLayoutContactNumber.setErrorEnabled(false);
        }

        return true;
    }


    private boolean validRadio() {


        if (mRadioGroup.getCheckedRadioButtonId() == -1) {
            Snackbar.with(mainActivity).text(getResources().getString(R.string.validlocation)).show(mainActivity);
            mCommonHelper.hideKeyboard(mainActivity);
            return false;
        } else {
            mTextInputLayoutContactNumber.setErrorEnabled(false);
        }

        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            mainActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }


    public void UpdateAddress() {

        getLoaderManager().restartLoader(0, null, new LoaderManager.LoaderCallbacks<TaskExecutor>() {
            @Override
            public Loader<TaskExecutor> onCreateLoader(int arg0, Bundle arg1) {

                AddAddressExecutor mAddAddressExecutor = new AddAddressExecutor(mainActivity, null);
                return new LoaderTask<TaskExecutor>(mainActivity, mAddAddressExecutor);
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
    public class AddAddressExecutor extends TaskExecutor {
        protected AddAddressExecutor(Context context, Bundle bundle) {
            super(context, bundle);
        }

        @Override
        protected Object executeTask() {

            EditAddressInfoService  mEditAddressInfoService = new EditAddressInfoService();

//                mServerResponseVO = mEditAddressInfoService.fetcheditaddress(mainActivity, Tags.UpdateAddress, GetUserId ,mUserDetailsVO.getAddressId(),mStringPincode, mStringHouseNumber,
//                        mStringName, mStringContactNumber ,mStringSelectLocation, mStringSelectLocation,mStringSwitchValue,mStringStateID,mStringCityId);
            return null;
        }
    }


}