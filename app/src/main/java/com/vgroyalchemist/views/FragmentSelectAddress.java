package com.vgroyalchemist.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.opengl.Visibility;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vgroyalchemist.R;
import com.vgroyalchemist.controller.MainActivity;
import com.vgroyalchemist.loaders.LoaderTask;
import com.vgroyalchemist.loaders.LoaderTaskWithoutProgressDialog;
import com.vgroyalchemist.loaders.TaskExecutor;
import com.vgroyalchemist.utils.CommonHelper;
import com.vgroyalchemist.utils.Snackbar;
import com.vgroyalchemist.utils.SweetAlertDialog;
import com.vgroyalchemist.utils.Tags;
import com.vgroyalchemist.vos.GetLoginData;
import com.vgroyalchemist.vos.ServerResponseVO;
import com.vgroyalchemist.vos.UserDetailsVO;
import com.vgroyalchemist.webservice.DeleteAddressInfoService;
import com.vgroyalchemist.webservice.GetAllAddressInfoService;
import com.vgroyalchemist.webservice.GetSingalAddressInfoService;
import com.vgroyalchemist.webservice.PostParseGet;

import java.util.ArrayList;

// developed by krishna 5-03-2021
/* select user multiple address */
public class FragmentSelectAddress extends NonCartFragment {


    //Variable Declaration
    public static final String FRAGMENT_ID = "15";

    public static MainActivity mainActivity;
    public View fragmentView = null;
    CommonHelper mCommonHelper;

    RecyclerView mRecyclerViewAddresList;
    TextView mTextViewAddNew;
    TextView mTextViewSubmit;
    ImageView mImageViewBack;
    TextView mTextViewTitle;
    ImageView mImageViewSearch;
    ImageView mImageViewCart;
    LinearLayout mLinearLayoutEmpty;
    RelativeLayout mRelativeLayoutReycle;
    ArrayList mArrayListAddressData;

    GetLoginData mGetLoginData;
    ServerResponseVO mServerResponseVO;
    // userdata
    public SharedPreferences mSharedPreferencesUserId;
    SharedPreferences.Editor mEditor;
    PostParseGet mPostParseGet;

    String GetUserId;
    String AddressId;
    int row_index =0;

    ArrayList<UserDetailsVO> mArrayListUserDetailVO;
    ServerResponseVO mServerResponseVODelete;
    SelectAddressAdapter selectAddressAdapter;

    boolean IsFromOrderPre;
    boolean ISSelected;
    boolean IsFromMenu;
    Bundle mBundle;
    ArrayList<Bitmap> mArrayListGetImage;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainActivity = (MainActivity) getActivity();
        mCommonHelper = new CommonHelper();
        mPostParseGet = new PostParseGet(mainActivity);
        mArrayListUserDetailVO = new ArrayList<UserDetailsVO>();
        mArrayListGetImage = new ArrayList<Bitmap>();
        mBundle = getArguments();

        if(mBundle != null)
        {
            IsFromOrderPre = mBundle.getBoolean("IsFromOrderPre");
            IsFromMenu = mBundle.getBoolean("fromMenu");
            mArrayListGetImage = (ArrayList<Bitmap>) getArguments().getSerializable("imagearray");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentView = inflater.inflate(R.layout.fragment_select_address, container, false);

        mImageViewBack = fragmentView.findViewById(R.id.fragment_imageview_back);
        mTextViewTitle = fragmentView.findViewById(R.id.fragment_title_textview);

        mImageViewSearch = fragmentView.findViewById(R.id.fragment_Serach);
        mImageViewSearch.setVisibility(View.GONE);
        mImageViewCart = fragmentView.findViewById(R.id.fragment_cart);
        mImageViewCart.setVisibility(View.GONE);

        mTextViewSubmit = fragmentView.findViewById(R.id.fragment_select_address_textview_submit);
        mTextViewAddNew = fragmentView.findViewById(R.id.fragment_select_address_textview_add_address);
        mRecyclerViewAddresList = fragmentView.findViewById(R.id.fragment_select_address_recycleview);
        mLinearLayoutEmpty = fragmentView.findViewById(R.id.fragment_select_address_relative_empty);
        mRelativeLayoutReycle = fragmentView.findViewById(R.id.fragment_select_address_relative_main);

        mSharedPreferencesUserId = mainActivity.getSharedPreferences(Tags.TAG_SHARED_PREFERENCE_USER_ID, 0);
        GetUserId = mSharedPreferencesUserId.getString("UserId", "");
        mEditor = mSharedPreferencesUserId.edit();

        if(IsFromMenu == true)
        {
            mTextViewTitle.setText("Addresses");
        }
        else {
            mTextViewTitle.setText("Select Address");
        }

        mImageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             mainActivity.onBackPressed();
            }
        });

        mTextViewAddNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment currentFrag = mainActivity.getSupportFragmentManager().findFragmentById(R.id.activity_main_container);
                mCurrentFragment = currentFrag.getClass().getName();

                if (!mCurrentFragment.equalsIgnoreCase(Tags.TAG_FRAGEMENT_ADD_ADDRESS)) {
                    FragmentAddAddress mFragmentAddAddress = new FragmentAddAddress();
                    Bundle mBundle = new Bundle();
                    mBundle.putBoolean("fromMenu",false);
                    mBundle.putBoolean(Tags.isFromEdit, false);
                    mFragmentAddAddress.setArguments(mBundle);
                    mainActivity.addFragment(mFragmentAddAddress, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, mFragmentAddAddress.getClass().getName());
                }
            }
        });


        return fragmentView;
    }


    public class SelectAddressAdapter extends RecyclerView.Adapter<SelectAddressAdapter.MyViewHolder> {


        Context mContext;
        ArrayList<UserDetailsVO> mArrayListAddress;

        public SelectAddressAdapter(MainActivity mainActivity, ArrayList mArrayListUserDetailVO) {
            this.mContext = mainActivity;
            this.mArrayListAddress = mArrayListUserDetailVO;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_select_address, parent, false);
            MyViewHolder myViewHolderAddress = new MyViewHolder(v);
            return myViewHolderAddress;
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

            holder.mTextViewAddressOne.setText(mArrayListAddress.get(position).getAddress());
            holder.mTextViewPincode.setText(mArrayListAddress.get(position).getCityName() + "," + mArrayListAddress.get(position).getPincode() + "  " + mArrayListAddress.get(position).getStateName());
            holder.mTextViewHouseNo.setText(mArrayListAddress.get(position).getName());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AddressId = mArrayListAddress.get(position).getAddressId();
                    row_index = position;
                    selectAddressAdapter.notifyDataSetChanged();

                    if(!IsFromMenu == true) {

                        if (IsFromOrderPre == true) {
                            mainActivity.removeAllBackFragments();
                            FragmentOrderSummary mFragmentOrderSummary = new FragmentOrderSummary();
                            Bundle mBundle = new Bundle();
                            mBundle.putString("SelcteAddresssId", AddressId);
                            mBundle.putBoolean("SelAddress",true);
                            mBundle.putSerializable("imagearray", mArrayListGetImage);
                            mFragmentOrderSummary.setArguments(mBundle);
//                            mainActivity.replaceCurentFragment(mFragmentOrderSummary);
                            mainActivity.addFragment(mFragmentOrderSummary, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, mFragmentOrderSummary.getClass().getName());
                        } else {
                            mainActivity.removeAllBackFragments();
                            FragmentSearchOrderSummary mFragmentSelectAddress = new FragmentSearchOrderSummary();
                            Bundle mBundle = new Bundle();
                            mBundle.putString("SelcteAddresssId",AddressId);
                            mBundle.putBoolean("SelAddress",true);
                            mBundle.putSerializable("imagearray", mArrayListGetImage);
                            mFragmentSelectAddress.setArguments(mBundle);
//                            mainActivity.replaceCurentFragment(mFragmentSelectAddress);
                            mainActivity.addFragment(mFragmentSelectAddress, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, mFragmentSelectAddress.getClass().getName());
                        }
                    }
                }
            });


                if (row_index == position) {
                    holder.mLinearLayout.setBackground(getResources().getDrawable(R.drawable.row_shadow_color));
                } else {
                    holder.mLinearLayout.setBackground(getResources().getDrawable(R.drawable.rowshadow));
                }


            holder.mImageViewDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (!mCommonHelper.check_Internet(getActivity())) {
                        Snackbar.with(mainActivity).text(getResources().getString(R.string.interneterror)).show(mainActivity);
                        return;
                    }

                    if (mArrayListAddress.get(position).getDefaultAddress().equalsIgnoreCase("true")) {
                        Snackbar.with(mainActivity).text(getResources().getString(R.string.cannotdelete)).show(mainActivity);
                        return;
                    }

                    SweetAlertDialog mSweetAlertDialog = new SweetAlertDialog(mainActivity)
                            .setContentText(getResources().getString(R.string.removethisaddress))
                            .setConfirmText(getResources().getString(R.string.yes))
                            .setCancelText(getResources().getString(R.string.no))
                            .showCancelButton(true)
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismiss();

                                    final Bundle mBundle = new Bundle();
                                    mBundle.putString(getString(R.string.app_name), mArrayListAddress.get(position).getAddressId());

                                    getLoaderManager().restartLoader(0, mBundle, new LoaderManager.LoaderCallbacks<TaskExecutor>() {
                                        @Override
                                        public Loader<TaskExecutor> onCreateLoader(int arg0, Bundle arg1) {

                                            DeleteAddressExecutor mSearchOrderProductProcess = new DeleteAddressExecutor(mainActivity, mBundle);
                                            return new LoaderTask<TaskExecutor>(mainActivity, mSearchOrderProductProcess);
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

                                                    if (mServerResponseVODelete != null && mServerResponseVODelete.getStatus() != null) {

                                                        if (mServerResponseVODelete.getStatus().equalsIgnoreCase("true")) {

                                                            mArrayListAddress.remove(position);
                                                            selectAddressAdapter.notifyDataSetChanged();


                                                            Snackbar.with(mainActivity).text(mServerResponseVODelete.getMsg()).show(mainActivity);
                                                        } else {
                                                            Snackbar.with(mainActivity).text(mServerResponseVODelete.getMsg()).show(mainActivity);
                                                        }
                                                    } else {
                                                        Snackbar.with(mainActivity).text(mServerResponseVODelete.getMsg()).show(mainActivity);
                                                    }
                                                }
                                            }
                                        }

                                        @Override
                                        public void onLoaderReset(Loader<TaskExecutor> arg0) {

                                        }
                                    });

                                }
                            })
                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                }
                            });
                    mSweetAlertDialog.show();
                }
            });

            holder.mImageViewEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!mCommonHelper.check_Internet(getActivity())) {
                        Snackbar.with(mainActivity).text(getResources().getString(R.string.interneterror)).show(mainActivity);
                        return;
                    }

                    UserDetailsVO mDetailsVO = (UserDetailsVO) v.getTag();
                    if(IsFromMenu == true)
                    {
                        FragmentAddAddress mFragmentListData = new FragmentAddAddress();
                        Bundle args = new Bundle();
                        args.putParcelable(Tags.KEY_ADDRESS_DATA, mDetailsVO);
                        args.putBoolean(Tags.isFromEdit, true);
                        args.putString("AddressId",mArrayListAddress.get(position).getAddressId());
                        mFragmentListData.setArguments(args);
                        mainActivity.addFragment(mFragmentListData, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, mFragmentListData.getClass().getName());

                    }
                    else {
                        FragmentAddAddress mFragmentListData = new FragmentAddAddress();
                        Bundle args = new Bundle();
                        args.putParcelable(Tags.KEY_ADDRESS_DATA, mDetailsVO);
                        args.putBoolean(Tags.isFromEdit, true);
                        args.putBoolean("SelectAddress", true);
                        args.putString("AddressId",mArrayListAddress.get(position).getAddressId());
                        mFragmentListData.setArguments(args);
                        mainActivity.addFragment(mFragmentListData, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, mFragmentListData.getClass().getName());

                    }


                }
            });

        }


        @Override
        public int getItemCount() {
            return mArrayListAddress.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            TextView mTextViewHouseNo;
            TextView mTextViewAddressOne;
            TextView mTextViewPincode;
            ImageView mImageViewEdit;
            ImageView mImageViewDelete;

            LinearLayout mLinearLayout;

            public MyViewHolder(View itemView) {
                super(itemView);

                mTextViewHouseNo = itemView.findViewById(R.id.row_select_address_textview_houseno);
                mTextViewAddressOne = itemView.findViewById(R.id.row_select_address_textview_address_one);
                mTextViewPincode = itemView.findViewById(R.id.row_select_address_textview_pincode);
                mImageViewEdit = itemView.findViewById(R.id.row_change_address_imageview_edit);
                mImageViewDelete = itemView.findViewById(R.id.row_change_address_imageview_delete);
                mLinearLayout = itemView.findViewById(R.id.row_select_address_linear_Address);
            }
        }
    }

    public class DeleteAddressExecutor extends TaskExecutor {

        String stringAddressId;

        protected DeleteAddressExecutor(Context context, Bundle bundle) {
            super(context, bundle);
            this.stringAddressId = bundle.getString(getString(R.string.app_name));
        }

        @Override
        protected Object executeTask() {

            try {

                DeleteAddressInfoService fetchVersionInfoService = new DeleteAddressInfoService();
                mServerResponseVODelete = fetchVersionInfoService.deleteAddress(mainActivity, Tags.DeleteAddress,stringAddressId);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public void GetAddressData() {

        getLoaderManager().restartLoader(0, null, new LoaderManager.LoaderCallbacks<TaskExecutor>() {
            @Override
            public Loader<TaskExecutor> onCreateLoader(int arg0, Bundle arg1) {

                GetAllAddress mAddAddressExecutor = new GetAllAddress(mainActivity, null);
                return new LoaderTask<TaskExecutor>(mainActivity, mAddAddressExecutor);
            }

            @Override
            public void onLoadFinished(Loader<TaskExecutor> arg0, TaskExecutor arg1) {

                if (isAdded()) {
                    getLoaderManager().destroyLoader(0);
                    if (!mCommonHelper.check_Internet(mainActivity)) {
                        Snackbar.with(mainActivity).text(getResources().getString(R.string.interneterror)).show(mainActivity);
                        return;
                    }
                    if (mPostParseGet.isNetError)
                        Snackbar.with(mainActivity).text(getResources().getString(R.string.interneterror)).show(mainActivity);
                    else if (mPostParseGet.isOtherError)
                        Snackbar.with(mainActivity).text(getResources().getString(R.string.notabletocommunicatewithserver)).show(mainActivity);
                    else {

                        if (mServerResponseVO.getStatus().equalsIgnoreCase("true")) {
//                            Snackbar.with(mainActivity).text(mServerResponseVO.getMsg()).show(mainActivity);
                            mCommonHelper.hideKeyboard(mainActivity);


                            if (mServerResponseVO.getData() != null) {

                                mArrayListUserDetailVO = (ArrayList<UserDetailsVO>) mServerResponseVO.getData();

                                if (mArrayListUserDetailVO != null && mArrayListUserDetailVO.size() > 0) {
                                    mLinearLayoutEmpty.setVisibility(View.GONE);
                                    mRelativeLayoutReycle.setVisibility(View.VISIBLE);

                                    selectAddressAdapter = new SelectAddressAdapter(mainActivity, mArrayListUserDetailVO);
                                    mRecyclerViewAddresList.setHasFixedSize(true);
                                    mRecyclerViewAddresList.setLayoutManager(new LinearLayoutManager(mainActivity));
                                    mRecyclerViewAddresList.setAdapter(selectAddressAdapter);
                                } else {
                                    mLinearLayoutEmpty.setVisibility(View.VISIBLE);
                                    mRelativeLayoutReycle.setVisibility(View.GONE);
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
    public class GetAllAddress extends TaskExecutor {
        protected GetAllAddress(Context context, Bundle bundle) {
            super(context, bundle);
        }

        @Override
        protected Object executeTask() {

            GetAllAddressInfoService getAllAddressInfoService = new GetAllAddressInfoService();
            mServerResponseVO = getAllAddressInfoService.fetchLoginUserInformation(mainActivity, Tags.GetAllUserAddress, GetUserId);

            return null;
        }
    }



    public void onResumeData() {
        if(mainActivity.toolbar != null) {
            mainActivity.toolbar.setVisibility(View.GONE);
        }

        GetAddressData();

    }

    @Override
    public void doWork() {
        super.doWork();
        onResumeData();
    }

    @Override
    public void onResume() {
        super.onResume();
        onResumeData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mCommonHelper.hideKeyboard(mainActivity);
    }

}