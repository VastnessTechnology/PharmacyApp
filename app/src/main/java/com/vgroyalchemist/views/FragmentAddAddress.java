package com.vgroyalchemist.views;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;
import com.vgroyalchemist.CommonClass.CommonClass;
import com.vgroyalchemist.R;
import com.vgroyalchemist.controller.MainActivity;
import com.vgroyalchemist.db.DBService;
import com.vgroyalchemist.loaders.LoaderTask;
import com.vgroyalchemist.loaders.LoaderTaskWithoutProgressDialog;
import com.vgroyalchemist.loaders.TaskExecutor;
import com.vgroyalchemist.utils.CommonHelper;
import com.vgroyalchemist.utils.Snackbar;
import com.vgroyalchemist.utils.Tags;
import com.vgroyalchemist.utils.TransparentProgressDialog;
import com.vgroyalchemist.utils.Utility;
import com.vgroyalchemist.vos.GetCityData;
import com.vgroyalchemist.vos.GetLoginData;
import com.vgroyalchemist.vos.GetStateData;
import com.vgroyalchemist.vos.ServerResponseVO;
import com.vgroyalchemist.vos.UserDetailsVO;
import com.vgroyalchemist.webservice.AddAddressInfoService;
import com.vgroyalchemist.webservice.EditAddressInfoService;
import com.vgroyalchemist.webservice.FetchCityService;
import com.vgroyalchemist.webservice.FetchStateService;
import com.vgroyalchemist.webservice.GetSingalAddressInfoService;
import com.vgroyalchemist.webservice.PostParseGet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;

/* developed by krishna 11-03-2021
 *  this class is user add  and edit address */
public class FragmentAddAddress extends NonCartFragment implements TextWatcher {

    public static final String FRAGMENT_ID = "8";

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
    EditText mEditTextState;
    EditText mEditTextCity;

    TextInputLayout mTextInputLayoutPincode;
    TextInputLayout mTextInputLayoutHouseNumber;
    TextInputLayout mTextInputLayoutName;
    TextInputLayout mTextInputLayoutContactNumber;
    TextInputLayout mTextInputLayoutstate;
    TextInputLayout mTextInputLayoutCity;

    RadioGroup mRadioGroup;
    RadioButton mRadioButtonHome;
    RadioButton mRadioButtonOffice;
    RadioButton mRadioButtonOther;

    String mStringPincode;
    String mStringHouseNumber;
    String mStringName;
    String mStringContactNumber;
    String mStringSelectLocation;
    String mStringSwitchValue;
    String mStringCity;
    String mStringState;
    String mStringCityId;
    String mStringAddressId;
    String mStringStateID = "";
    int selectedId;
    ImageView mImageViewSearch;
    ImageView mImageViewCart;

    Dialog popupView;
    Dialog popupViewCity;
    EditText mAutoCompleteTextView;
    ListView mListView;
    TextView mTextViewListTitle;
    StateAdapter mStateAdapter;
    CityAdapter mCityAdapter;
    String cityName = "";
    String cityID = "";
    String AddressId;
    GetLoginData mGetLoginData;
    ServerResponseVO mServerResponseVO;
    // userdata
    public SharedPreferences mSharedPreferencesUserId;
    SharedPreferences.Editor mEditor;

    public SharedPreferences mSharedPreferencesAddress;
    SharedPreferences.Editor mEditorAddress;

    PostParseGet mPostParseGet;

    String GetUserId;
    DBService mDbService;
    boolean isFromMenu = false;
    boolean isFromEdit = false;
    boolean isFromOrder = false;
    boolean IsFromOrderpres = false;
    boolean IsCart = false;
    boolean IsPresciption = false;
    boolean SelectAddress = false;
    Bundle mBundle;
    UserDetailsVO mUserDetailsVO;
    ArrayList<UserDetailsVO> mUserDetailsVOArrayList;
    ArrayList<GetStateData.StateDetail> mStateDetailsList;
    ArrayList<GetCityData.CityDeatils> mCityDetailsList;
    Switch mSwitchDefault;
    GetStateData mGetStateData;
    LinearLayout mLinearLayoutSwitch;
    ArrayList<String> mStringArrayListState;
    ServerResponseVO mServerResponseVOState;
    ServerResponseVO mServerResponseVOCity;

    TransparentProgressDialog mProgressDialog;
    static ProgressDialog progress_dialog;
    ArrayList<Bitmap> mArrayListGetImage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainActivity = (MainActivity) getActivity();
        mCommonHelper = new CommonHelper();
        mPostParseGet = new PostParseGet(mainActivity);
        mUserDetailsVOArrayList = new ArrayList<UserDetailsVO>();
        mDbService = new DBService();
        mGetStateData = new GetStateData();
        mStateDetailsList = new ArrayList<GetStateData.StateDetail>();
        mCityDetailsList = new ArrayList<GetCityData.CityDeatils>();
        mStringArrayListState = new ArrayList<String>();
        mArrayListGetImage = new ArrayList<Bitmap>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmnetview = inflater.inflate(R.layout.fragment_add_address, container, false);

        mImageViewBack = fragmnetview.findViewById(R.id.fragment_imageview_back);
        mTextViewTitle = fragmnetview.findViewById(R.id.fragment_title_textview);

        mImageViewSearch = fragmnetview.findViewById(R.id.fragment_Serach);
        mImageViewSearch.setVisibility(View.GONE);
        mImageViewCart = fragmnetview.findViewById(R.id.fragment_cart);
        mImageViewCart.setVisibility(View.GONE);

        mEditTextPinCode = fragmnetview.findViewById(R.id.fragment_Add_Address_edtext_Pincode);
        mEditTextHouseNumber = fragmnetview.findViewById(R.id.fragment_Add_Address_edtext_House_Name);
        mEditTextName = fragmnetview.findViewById(R.id.fragment_Add_Address_edtext_Name);
        mEditTextContactNumber = fragmnetview.findViewById(R.id.fragment_Add_Address_edtext_contact_number);
        mEditTextState = fragmnetview.findViewById(R.id.fragment_Add_Address_edtext_state);
        mEditTextCity = fragmnetview.findViewById(R.id.fragment_Add_Address_edtext_City);

        mTextInputLayoutPincode = fragmnetview.findViewById(R.id.fragment_Add_Address_input_Pincode);
        mTextInputLayoutHouseNumber = fragmnetview.findViewById(R.id.fragment_Add_Address_input_House_Name);
        mTextInputLayoutName = fragmnetview.findViewById(R.id.fragment_Add_Address_input_Name);
        mTextInputLayoutContactNumber = fragmnetview.findViewById(R.id.fragment_Add_Address_input_contact_number);
        mTextInputLayoutCity = fragmnetview.findViewById(R.id.fragment_Add_Address_input_city);
        mTextInputLayoutstate = fragmnetview.findViewById(R.id.fragment_Add_Address_input_state);

        mRadioGroup = fragmnetview.findViewById(R.id.fragment_Add_Address_RadioGroup);
        mRadioButtonHome = fragmnetview.findViewById(R.id.fragment_Add_Address_RadioButton_Home);
        mRadioButtonOffice = fragmnetview.findViewById(R.id.fragment_Add_Address_RadioButton_Office);
        mRadioButtonOther = fragmnetview.findViewById(R.id.fragment_Add_Address_RadioButton_Other);
        mTextViewsubmit = fragmnetview.findViewById(R.id.fragment_Add_Address_textview_submit);
        mLinearLayoutSwitch = fragmnetview.findViewById(R.id.fragment_add_address_temp_linear_switch);
        mSwitchDefault = fragmnetview.findViewById(R.id.fragment_add_address_temp_switch);

        mSharedPreferencesUserId = mainActivity.getSharedPreferences(Tags.TAG_SHARED_PREFERENCE_USER_ID, 0);
        GetUserId = mSharedPreferencesUserId.getString("UserId", "");
        mEditor = mSharedPreferencesUserId.edit();

        mSharedPreferencesAddress = mainActivity.getSharedPreferences(Tags.TAG_SHARED_PREFRENCE_Address, 0);
        mEditorAddress = mSharedPreferencesAddress.edit();

        mUserDetailsVO = new UserDetailsVO();
        mGetLoginData = new GetLoginData();
        mBundle = getArguments();
        if (mBundle != null) {

            isFromMenu = getArguments().getBoolean("fromMenu");
            isFromEdit = mBundle.getBoolean(Tags.isFromEdit);
            isFromOrder = mBundle.getBoolean("fromorder");
            AddressId = mBundle.getString("AddressId");
            IsFromOrderpres = mBundle.getBoolean("IsFromOrderpres");
            IsCart = mBundle.getBoolean("IsCart");
            IsPresciption = mBundle.getBoolean("IsPresciption");
            mArrayListGetImage = (ArrayList<Bitmap>) getArguments().getSerializable("imagearray");
            SelectAddress = mBundle.getBoolean("SelectAddress");
        }

        if (isFromEdit == true) {
            mTextViewTitle.setText("Edit Address");
        } else {
            mTextViewTitle.setText("Add Address");
        }

        if (isFromEdit == true || isFromOrder == true || IsFromOrderpres == true) {
            EditAddressData();
        }

        mEditTextState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogState();
            }
        });

        mSwitchDefault.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mStringSwitchValue = "true";
                } else {
                    mStringSwitchValue = "false";
                }
            }
        });
        mImageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Fragment currentFrag = mainActivity.getSupportFragmentManager().findFragmentById(R.id.activity_main_container);
//                mCurrentFragment = currentFrag.getClass().getName();
//                mainActivity.removeFragment(currentFrag, currentFrag.getClass().getName());
                mainActivity.onBackPressed();
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
                mStringSelectLocation = mRadioButtonOffice.getText().toString().trim();
            }
        });
        mRadioButtonOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStringSelectLocation = mRadioButtonOther.getText().toString().trim();
            }
        });
        mTextViewsubmit.setOnClickListener(v -> {

            if (!mCommonHelper.check_Internet(getActivity())) {
                Snackbar.with(mainActivity).text(getResources().getString(R.string.interneterror)).show(mainActivity);
                return;
            }

            mStringPincode = mEditTextPinCode.getText().toString().trim();
            mStringHouseNumber = mEditTextHouseNumber.getText().toString().trim();
            mStringName = mEditTextName.getText().toString().trim();
            mStringContactNumber = mEditTextContactNumber.getText().toString().trim();

            if (!validPincode()) {
                return;
            }
            if (!validHouseName()) {
                return;
            }
            if (!validName()) {
                return;
            }
            if (!validContactNo()) {
                return;
            }
            if (!validstate()) {
                return;
            }
            if (!validCity()) {
                return;
            }
            if (!validRadio()) {
                return;
            }
            System.out.println("VT Radio text..." + mStringSelectLocation);

            AddAddress();
        });

        new GetStateDataProcess().execute();
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

    private boolean validstate() {
        String state = mEditTextState.getText().toString().trim();

        if (state.isEmpty()) {
            mTextInputLayoutstate.setError(getResources().getString(R.string.validstate));
            requestFocus(mEditTextState);
            mCommonHelper.hideKeyboard(mainActivity);
            return false;
        } else {
            mTextInputLayoutstate.setErrorEnabled(false);
        }

        return true;
    }


    private boolean validCity() {
        String City = mEditTextCity.getText().toString().trim();

        if (City.isEmpty()) {
            mTextInputLayoutCity.setError(getResources().getString(R.string.validcity));
            requestFocus(mEditTextCity);
            mCommonHelper.hideKeyboard(mainActivity);
            return false;
        } else {
            mTextInputLayoutCity.setErrorEnabled(false);
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


    public void AddAddress() {

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
                            @SuppressLint("HandlerLeak") Handler handler = new Handler() {
                                @Override
                                public void handleMessage(Message msg) {
                                    if (msg.what == Tags.WHAT) {
                                        mUserDetailsVO.setUserId(GetUserId);
                                        mUserDetailsVO.setAddress(mStringHouseNumber);
                                        mUserDetailsVO.setContactNo(mStringContactNumber);
                                        mUserDetailsVO.setAddressId(mServerResponseVO.getAddressId());
                                        mUserDetailsVO.setAddressType(mStringSelectLocation);
                                        mUserDetailsVO.setPincode(mStringPincode);
                                        mUserDetailsVO.setName(mStringName);

                                        if (SelectAddress == true && isFromEdit == true) {
                                            mainActivity.removeAllBackFragments();
                                            FragmentSelectAddress mFragmentSelectAddress = new FragmentSelectAddress();
                                            Bundle mBundle = new Bundle();
                                            mBundle.putBoolean("fromMenu", false);
                                            mBundle.putSerializable("imagearray", mArrayListGetImage);
                                            mFragmentSelectAddress.setArguments(mBundle);
                                            mainActivity.addFragment(mFragmentSelectAddress, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, mFragmentSelectAddress.getClass().getName());
                                        } else if (isFromEdit == true) {
                                            mainActivity.removeAllBackFragments();
                                            FragmentSelectAddress mFragmentSelectAddress = new FragmentSelectAddress();
                                            Bundle mBundle = new Bundle();
                                            mBundle.putBoolean("fromMenu", true);
                                            mFragmentSelectAddress.setArguments(mBundle);
                                            mainActivity.addFragment(mFragmentSelectAddress, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, mFragmentSelectAddress.getClass().getName());
                                        } else if (isFromOrder == true) {
//                                            mainActivity.removeAllBackFragments();
                                            FragmentSearchOrderSummary mFragmentSelectAddress = new FragmentSearchOrderSummary();
                                            Bundle mBundle = new Bundle();
                                            mBundle.putSerializable("imagearray", mArrayListGetImage);
                                            mFragmentSelectAddress.setArguments(mBundle);
                                            mainActivity.replaceCurentFragment(mFragmentSelectAddress);
                                        } else if (IsFromOrderpres == true) {
//                                            mainActivity.removeAllBackFragments();
                                            FragmentOrderSummary fragmentOrderSummary = new FragmentOrderSummary();
                                            Bundle mBundle = new Bundle();
                                            mBundle.putSerializable("imagearray", mArrayListGetImage);
                                            fragmentOrderSummary.setArguments(mBundle);
                                            mainActivity.replaceCurentFragment(fragmentOrderSummary);
                                        } else if (IsCart == true) {
//                                            mainActivity.removeAllBackFragments();
                                            FragmentSearchOrderSummary mFragmentSelectAddress = new FragmentSearchOrderSummary();
                                            Bundle mBundle = new Bundle();
                                            mBundle.putSerializable("imagearray", mArrayListGetImage);
                                            mFragmentSelectAddress.setArguments(mBundle);
                                            mainActivity.replaceCurentFragment(mFragmentSelectAddress);
//                                            mainActivity.addFragment(mFragmentSelectAddress, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, mFragmentSelectAddress.getClass().getName());

                                        } else if (IsPresciption == true) {
//                                            mainActivity.removeAllBackFragments();
                                            FragmentOrderSummary fragmentOrderSummary = new FragmentOrderSummary();
                                            Bundle mBundle = new Bundle();
                                            mBundle.putSerializable("imagearray", mArrayListGetImage);
                                            fragmentOrderSummary.setArguments(mBundle);
                                            mainActivity.replaceCurentFragment(fragmentOrderSummary);
//                                            mainActivity.addFragment(fragmentOrderSummary, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, fragmentOrderSummary.getClass().getName());
                                        } else {
                                            mainActivity.onBackPressed();
                                        }
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

            AddAddressInfoService addAddressInfoService = new AddAddressInfoService();
            EditAddressInfoService mEditAddressInfoService = new EditAddressInfoService();

            if (isFromEdit == true || isFromOrder == true || IsFromOrderpres == true) {
                mServerResponseVO = mEditAddressInfoService.fetcheditaddress(mainActivity, Tags.UpdateAddress, GetUserId, AddressId, mStringPincode, mStringHouseNumber,
                        mStringName, mStringContactNumber, mStringSelectLocation, mStringSwitchValue, mStringStateID, mStringCityId);
            } else {
                if (!mSwitchDefault.isChecked()) {
                    mStringSwitchValue = "false";
                }

                mServerResponseVO = addAddressInfoService.fethaddress(mainActivity, Tags.AddAddress, GetUserId, " ", mStringPincode, mStringHouseNumber,
                        mStringName, mStringContactNumber, mStringSelectLocation, mStringSwitchValue, mStringStateID, mStringCityId);
            }

            return null;
        }
    }


    public void EditAddressData() {

        getLoaderManager().restartLoader(0, null, new LoaderManager.LoaderCallbacks<TaskExecutor>() {
            @Override
            public Loader<TaskExecutor> onCreateLoader(int arg0, Bundle arg1) {

                EditAddressExecutor editAddressExecutor = new EditAddressExecutor(mainActivity, null);
                return new LoaderTaskWithoutProgressDialog<TaskExecutor>(mainActivity, editAddressExecutor);
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


                                mUserDetailsVOArrayList = (ArrayList<UserDetailsVO>) mServerResponseVO.getData();

                                if (mUserDetailsVOArrayList != null) {
                                    for (int i = 0; i < mUserDetailsVOArrayList.size(); i++) {
                                        mEditTextName.setText(mUserDetailsVOArrayList.get(i).getName());
                                        mEditTextContactNumber.setText(mUserDetailsVOArrayList.get(i).getContactNo());
                                        mEditTextCity.setText(mUserDetailsVOArrayList.get(i).getCityName());
                                        mEditTextState.setText(mUserDetailsVOArrayList.get(i).getStateName());
                                        mEditTextPinCode.setText(mUserDetailsVOArrayList.get(i).getPincode());
                                        mEditTextHouseNumber.setText(mUserDetailsVOArrayList.get(i).getAddress());

                                        if (mUserDetailsVOArrayList.get(i).getAddressType().equalsIgnoreCase("Home")) {
                                            mRadioButtonHome.setChecked(true);
                                            mStringSelectLocation = mRadioButtonHome.getText().toString().trim();
                                        } else if (mUserDetailsVOArrayList.get(i).getAddressType().equalsIgnoreCase("Office")) {
                                            mRadioButtonOffice.setChecked(true);
                                            mStringSelectLocation = mRadioButtonOffice.getText().toString().trim();
                                        } else {
                                            mRadioButtonOther.setChecked(true);
                                            mStringSelectLocation = mRadioButtonOther.getText().toString().trim();
                                        }


                                        if (mUserDetailsVOArrayList.get(i).getDefaultAddress().equalsIgnoreCase("true")) {
                                            mSwitchDefault.setChecked(true);
                                            mSwitchDefault.setVisibility(View.GONE);
                                        } else {
                                            mSwitchDefault.setChecked(false);
                                            mSwitchDefault.setVisibility(View.VISIBLE);
                                        }

                                        mStringSwitchValue = mUserDetailsVOArrayList.get(i).getDefaultAddress();
                                        mStringCityId = mUserDetailsVOArrayList.get(i).getCityId();
                                        mStringStateID = mUserDetailsVOArrayList.get(i).getStateId();
                                        new GetCityDataExecute().execute();
                                    }
                                }
                            } else {
                                Snackbar.with(mainActivity).text(mServerResponseVO.getMsg()).show(mainActivity);
                                mCommonHelper.hideKeyboard(mainActivity);
                            }
                        } catch (Exception e) {
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
    public class EditAddressExecutor extends TaskExecutor {
        protected EditAddressExecutor(Context context, Bundle bundle) {
            super(context, bundle);
        }

        @Override
        protected Object executeTask() {


            GetSingalAddressInfoService mEditAddressInfoService = new GetSingalAddressInfoService();
            if (isFromEdit || isFromOrder || IsFromOrderpres) {
                mServerResponseVO = mEditAddressInfoService.fetcheditaddress(mainActivity, Tags.GetAddressData, AddressId);
            }
            return null;
        }
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {


        if (mStateAdapter != null)
            mStateAdapter.getFilter().filter(s.toString());

        if (mCityAdapter != null)
            mCityAdapter.getFilter().filter(s.toString());
    }

    @Override
    public void afterTextChanged(Editable s) {

    }


    public class GetStateDataProcess extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            FetchStateService fetchMyOrderService = new FetchStateService();
            mServerResponseVOState = fetchMyOrderService.fetchStateData(mainActivity, Tags.GetStatesWebservice);
            return null;
        }

        @SuppressLint("NewApi")
        @Override
        protected void onPostExecute(Void result) {

            if (isAdded()) {

                if (!mCommonHelper.check_Internet(mainActivity)) {
                    Snackbar.with(mainActivity).text(getResources().getString(R.string.interneterror)).show(mainActivity);
                    return;
                }
                if (mPostParseGet.isNetError)
                    Snackbar.with(mainActivity).text(getResources().getString(R.string.interneterror)).show(mainActivity);
                else if (mPostParseGet.isOtherError)
                    Snackbar.with(mainActivity).text(getResources().getString(R.string.notabletocommunicatewithserver)).show(mainActivity);
                else {
                    try {
                        if (mServerResponseVOState.getStatus().equalsIgnoreCase("true")) {

                            if (mServerResponseVOState.getData() != null) {
                                mStateDetailsList = (ArrayList<GetStateData.StateDetail>) mServerResponseVOState.getData();

                                if (mStateDetailsList != null) {
                                    mStringArrayListState.clear();

//                                    for (int i = 0; i < mStateDetailsList.size(); i++) {
//
//                                        mStringState = mStateDetailsList.get(i).getState();
//                                        mStringStateID = mStateDetailsList.get(i).getStateId();
//                                    }
                                }
                            }
                        }
                    } catch (Exception e) {

                    }
                }
            }
            super.onPostExecute(result);
        }
    }

    public void DialogState() {

        popupView = new Dialog(mainActivity);
        popupView.requestWindowFeature(Window.FEATURE_NO_TITLE);
        popupView.setContentView(R.layout.dialog_country);
        popupView.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        mAutoCompleteTextView = popupView.findViewById(R.id.dialog_country_autocomplettext);
        mAutoCompleteTextView.addTextChangedListener(this);

        mListView = popupView.findViewById(R.id.dialog_country_listview);
        mTextViewListTitle = popupView.findViewById(R.id.dialog_country_title);
        mTextViewListTitle.setText("Select State");

        mStateAdapter = new StateAdapter(mainActivity, R.layout.row_title, mStateDetailsList);
        mListView.setAdapter(mStateAdapter);
        popupView.show();
    }


    public class StateAdapter extends ArrayAdapter<GetStateData.StateDetail> {

        ArrayList<GetStateData.StateDetail> mArrayList;
        ArrayList<GetStateData.StateDetail> searchList;
        private Filter filter;

        public StateAdapter(Context context, int resource, ArrayList<GetStateData.StateDetail> objects) {
            super(context, resource, objects);
            mArrayList = objects;
            searchList = objects;
        }

        public ArrayList<GetStateData.StateDetail> getOriginalDataSet() {
            return mArrayList;
        }

        public ArrayList<GetStateData.StateDetail> getSearchDataSet() {
            return searchList;
        }

        public void setSearchDataSet(ArrayList<GetStateData.StateDetail> searchDataSet) {
            searchList = new ArrayList<GetStateData.StateDetail>(searchDataSet);
            notifyDataSetChanged();
        }

        @Override
        public Filter getFilter() {
            if (filter == null)
                filter = new CustomFilterState(this);
            return filter;
        }

        @Override
        public int getCount() {

            return searchList.size();
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            ViewHolderState holder;

            if (convertView == null) {

                convertView = mainActivity.getLayoutInflater().inflate(R.layout.row_title, parent, false);
                holder = new ViewHolderState();

                holder.mTextViewTitle = convertView.findViewById(R.id.row_name_textview_name);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolderState) convertView.getTag();
            }


            holder.mTextViewTitle.setText(searchList.get(position).getState());

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mStringState = searchList.get(position).getState();
                    mStringStateID = searchList.get(position).getStateId();

                    mEditTextState.setText(mStringState);
                    mEditTextCity.setText("");
                    popupView.dismiss();
                    mCommonHelper.hideKeyboard(mainActivity);
                    new GetCityDataExecute().execute();
                }
            });
            return convertView;
        }
    }

    static class ViewHolderState {
        TextView mTextViewTitle;
    }

    private class CustomFilterState extends Filter {

        StateAdapter adapter;

        public CustomFilterState(StateAdapter adapter) {
            this.adapter = adapter;
        }


        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            Utility.debugger(" filter called ");
            FilterResults results = new FilterResults();
            String prefix = constraint.toString().toLowerCase(Locale.getDefault()).trim();
            if (prefix == null || prefix.length() == 0) {
                ArrayList<GetStateData.StateDetail> list = new ArrayList<GetStateData.StateDetail>(adapter.getOriginalDataSet());
                results.values = list;
                results.count = list.size();
            } else {
                final ArrayList<GetStateData.StateDetail> list = new ArrayList<GetStateData.StateDetail>(adapter.getOriginalDataSet());
                final ArrayList<GetStateData.StateDetail> nlist = new ArrayList<GetStateData.StateDetail>();
                int count = list.size();
                for (int i = 0; i < count; i++) {
                    final GetStateData.StateDetail menuItemVO = list.get(i);
                    final String value = menuItemVO.getState().toLowerCase(Locale.getDefault());

                    Utility.debugger("value: " + value + " prefix: " + prefix + " compare: " + value.contains(prefix));
                    if (value.contains(prefix)) {
                        nlist.add(menuItemVO);
                    }
                }
                results.values = nlist;
                results.count = nlist.size();
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            adapter.setSearchDataSet((ArrayList<GetStateData.StateDetail>) filterResults.values);
        }
    }


//    city Data

    public class GetCityDataExecute extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            mProgressDialog = new TransparentProgressDialog(mainActivity, 0, false);
            mProgressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            FetchCityService fetchMyOrderService = new FetchCityService();
            mServerResponseVOCity = fetchMyOrderService.fetchcityData(mainActivity, Tags.getCityData, mStringStateID);
            return null;
        }

        @SuppressLint("NewApi")
        @Override
        protected void onPostExecute(Void result) {

            if (isAdded()) {

                if (!mCommonHelper.check_Internet(mainActivity)) {
                    Snackbar.with(mainActivity).text(getResources().getString(R.string.interneterror)).show(mainActivity);
                    return;
                }
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }
                if (mPostParseGet.isNetError)
                    Snackbar.with(mainActivity).text(getResources().getString(R.string.interneterror)).show(mainActivity);
                else if (mPostParseGet.isOtherError)
                    Snackbar.with(mainActivity).text(getResources().getString(R.string.notabletocommunicatewithserver)).show(mainActivity);
                else {
                    if (mServerResponseVOCity.getStatus().equalsIgnoreCase("true")) {

                        if (mServerResponseVOCity.getData() != null) {

                            mEditTextCity.setClickable(true);
                            mEditTextCity.setEnabled(true);
                            mEditTextCity.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    DialogCityData();
                                }
                            });

                            mCityDetailsList = (ArrayList<GetCityData.CityDeatils>) mServerResponseVOCity.getData();

//                            if (mCityDetailsList != null) {
//                                mStringArrayListState.clear();
//
//                                for (int i = 0; i < mCityDetailsList.size(); i++) {
//
//                                    mStringStateID = mCityDetailsList.get(i).getStateId();
//                                    mStringCity =mCityDetailsList.get(i).getCityName();
//                                    mStringCityId =mCityDetailsList.get(i).getCityId();
//                                }
//
//                            }
                        }
                    }
                }

            }
            super.onPostExecute(result);
        }

    }


    public void DialogCityData() {

        popupViewCity = new Dialog(mainActivity);
        popupViewCity.requestWindowFeature(Window.FEATURE_NO_TITLE);
        popupViewCity.setContentView(R.layout.dialog_country);
        popupViewCity.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        mAutoCompleteTextView = popupViewCity.findViewById(R.id.dialog_country_autocomplettext);
        mAutoCompleteTextView.addTextChangedListener(this);

        mListView = popupViewCity.findViewById(R.id.dialog_country_listview);
        mTextViewListTitle = popupViewCity.findViewById(R.id.dialog_country_title);
        mTextViewListTitle.setText("Select City");

        mCityAdapter = new CityAdapter(mainActivity, R.layout.row_title, mCityDetailsList);
        mListView.setAdapter(mCityAdapter);
        popupViewCity.show();
    }

    public class CityAdapter extends ArrayAdapter<GetCityData.CityDeatils> {

        ArrayList<GetCityData.CityDeatils> mArrayList;
        ArrayList<GetCityData.CityDeatils> searchList;
        private Filter filter;

        public CityAdapter(Context context, int resource, ArrayList<GetCityData.CityDeatils> objects) {
            super(context, resource, objects);
            mArrayList = objects;
            searchList = objects;
        }

        public ArrayList<GetCityData.CityDeatils> getOriginalDataSet() {
            return mArrayList;
        }

        public ArrayList<GetCityData.CityDeatils> getSearchDataSet() {
            return searchList;
        }

        public void setSearchDataSet(ArrayList<GetCityData.CityDeatils> searchDataSet) {
            searchList = new ArrayList<GetCityData.CityDeatils>(searchDataSet);
            notifyDataSetChanged();
        }

        @Override
        public Filter getFilter() {
            if (filter == null)
                filter = new CustomFilterCity(this);
            return filter;
        }

        @Override
        public int getCount() {

            return searchList.size();
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            ViewHolderState holder;

            if (convertView == null) {

                convertView = mainActivity.getLayoutInflater().inflate(R.layout.row_title, parent, false);
                holder = new ViewHolderState();

                holder.mTextViewTitle = convertView.findViewById(R.id.row_name_textview_name);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolderState) convertView.getTag();
            }


            holder.mTextViewTitle.setText(searchList.get(position).getCityName());

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mStringCity = searchList.get(position).getCityName();
                    mStringCityId = searchList.get(position).getCityId();

                    mEditTextCity.setText(mStringCity);
                    popupViewCity.dismiss();
                    mCommonHelper.hideKeyboard(mainActivity);
                }
            });
            return convertView;
        }
    }


    private class CustomFilterCity extends Filter {

        CityAdapter adapter;

        public CustomFilterCity(CityAdapter adapter) {
            this.adapter = adapter;
        }


        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            Utility.debugger(" filter called ");
            FilterResults results = new FilterResults();
            String prefix = constraint.toString().toLowerCase(Locale.getDefault()).trim();
            if (prefix == null || prefix.length() == 0) {
                ArrayList<GetCityData.CityDeatils> list = new ArrayList<GetCityData.CityDeatils>(adapter.getOriginalDataSet());
                results.values = list;
                results.count = list.size();
            } else {
                final ArrayList<GetCityData.CityDeatils> list = new ArrayList<GetCityData.CityDeatils>(adapter.getOriginalDataSet());
                final ArrayList<GetCityData.CityDeatils> nlist = new ArrayList<GetCityData.CityDeatils>();
                int count = list.size();
                for (int i = 0; i < count; i++) {
                    final GetCityData.CityDeatils menuItemVO = list.get(i);
                    final String value = menuItemVO.getCityName().toLowerCase(Locale.getDefault());

                    Utility.debugger("value: " + value + " prefix: " + prefix + " compare: " + value.contains(prefix));
                    if (value.contains(prefix)) {
                        nlist.add(menuItemVO);
                    }
                }
                results.values = nlist;
                results.count = nlist.size();
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            adapter.setSearchDataSet((ArrayList<GetCityData.CityDeatils>) filterResults.values);
        }
    }


}