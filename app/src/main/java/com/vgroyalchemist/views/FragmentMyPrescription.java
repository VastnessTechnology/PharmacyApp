package com.vgroyalchemist.views;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.vgroyalchemist.R;
import com.vgroyalchemist.controller.MainActivity;
import com.vgroyalchemist.loaders.LoaderTask;
import com.vgroyalchemist.loaders.TaskExecutor;
import com.vgroyalchemist.utils.CommonHelper;
import com.vgroyalchemist.utils.Snackbar;
import com.vgroyalchemist.utils.Tags;
import com.vgroyalchemist.utils.Utility;
import com.vgroyalchemist.vos.MYPrescriptionVO;
import com.vgroyalchemist.vos.PrescriptionImage;
import com.vgroyalchemist.vos.ServerResponseVO;
import com.vgroyalchemist.vos.UserDetailsVO;
import com.vgroyalchemist.webservice.GetDefualtAddressInfoService;
import com.vgroyalchemist.webservice.MyPrescirptionListService;
import com.vgroyalchemist.webservice.PostParseGet;

import java.util.ArrayList;

import static com.facebook.FacebookSdk.getApplicationContext;

/*  developed by krishna 24-03-2021
*  uploaded prescription list */
public class FragmentMyPrescription extends NonCartFragment {


    public static final String FRAGMENT_ID = "8";

    public static MainActivity mainActivity;
    CommonHelper mCommonHelper;
    View fragmnetview = null;

    ImageView mImageViewBack;
    TextView mTextViewTitle;
    ImageView mImageViewSearch;
    ImageView mImageViewCart;

    LinearLayout mLinearLayoutEmpty;
    RelativeLayout mRelativeLayoutRecycle;
    RecyclerView mRecyclerViewMyPrescription;
    TextView mTextViewDone;
    ArrayList<MYPrescriptionVO> mArrayListImageList;
    ImageListAdapter imageListAdapter;
    ArrayList<Uri> mTempArry;
    FragmentUploadPrescription fragmentUploadPrescription;
    ArrayList<Uri> selectedItems = new ArrayList<Uri>();

    // userdata
    public SharedPreferences mSharedPreferencesUserId;
    SharedPreferences.Editor mEditor;
    String mStringUserId;

    ServerResponseVO mServerResponseVO;
    PostParseGet mPostParseGet;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainActivity = (MainActivity) getActivity();
        mCommonHelper = new CommonHelper();
        mArrayListImageList = new ArrayList<MYPrescriptionVO>();
        mPostParseGet = new PostParseGet(mainActivity);
        mTempArry = new ArrayList<Uri>();
        fragmentUploadPrescription = new FragmentUploadPrescription();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmnetview = inflater.inflate(R.layout.fragment_my_prescription, container, false);


        mImageViewBack = fragmnetview.findViewById(R.id.fragment_imageview_back);
        mTextViewTitle = fragmnetview.findViewById(R.id.fragment_title_textview);
        mTextViewTitle.setText("Existing Prescriptions");
        mImageViewSearch = fragmnetview.findViewById(R.id.fragment_Serach);
        mImageViewSearch.setVisibility(View.GONE);
        mImageViewCart = fragmnetview.findViewById(R.id.fragment_cart);
        mImageViewCart.setVisibility(View.GONE);
        mLinearLayoutEmpty = fragmnetview.findViewById(R.id.fragment_myprescription_relative_empty);
        mRecyclerViewMyPrescription = fragmnetview.findViewById(R.id.fragment_myprescription_recyclerview);
        mTextViewDone = fragmnetview.findViewById(R.id.fragment_myprescription_textview_submit);
        mRelativeLayoutRecycle = fragmnetview.findViewById(R.id.fragment_myprescription_relative_recycleview);

        mSharedPreferencesUserId = mainActivity.getSharedPreferences(Tags.TAG_SHARED_PREFERENCE_USER_ID, 0);
        mStringUserId = mSharedPreferencesUserId.getString("UserId", "");
        mEditor = mSharedPreferencesUserId.edit();

        mImageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mainActivity.onBackPressed();
            }
        });


        GetPresciptionList();


        mTextViewDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedItems = imageListAdapter.getCheckedItems();
                Utility.debugger("VT Selected Item...." + selectedItems.size());
                mainActivity.removeAllBackFragments();
                FragmentUploadPrescription mFragmentUploadPrescription = new FragmentUploadPrescription();
                Bundle bundle = new Bundle();
                bundle.putSerializable("SelectImage", selectedItems);
                mFragmentUploadPrescription.setArguments(bundle);
                mainActivity.addFragment(mFragmentUploadPrescription, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, mFragmentUploadPrescription.getClass().getName());
            }
        });
        return fragmnetview;
    }

    public class ImageListAdapter extends RecyclerView.Adapter<ImageListAdapter.MyViewHolderImageList> {

        Context mContext;
        ArrayList<MYPrescriptionVO> mArrayList;
        SparseBooleanArray mSparseBooleanArray;

        public ImageListAdapter(MainActivity mainActivity, ArrayList<MYPrescriptionVO> mArrayListImageList) {
            this.mContext = mainActivity;
            this.mArrayList = mArrayListImageList;
            mSparseBooleanArray = new SparseBooleanArray();
        }

        @NonNull
        @Override
        public MyViewHolderImageList onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_my_prescription, parent, false);
            MyViewHolderImageList vh = new MyViewHolderImageList(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolderImageList holder, int position) {


//            holder.mImageViewSelectIamge.setImageURI(Uri.parse(mArrayList.get(position).getmImageView()));
            if(mArrayList.get(position).getImageName() != null)
            {
                Glide.with(mainActivity).load(mArrayList.get(position).getImageName())
                        .placeholder(R.drawable.logo)
                        .into(holder.mImageViewSelectIamge);
            }


            holder.mCheckBoxSelect.setTag(position);
            holder.mCheckBoxSelect.setChecked(mSparseBooleanArray.get(position));
            holder.mCheckBoxSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    mSparseBooleanArray.put((Integer) buttonView.getTag(), isChecked);
                }
            });

            holder.mImageViewSelectIamge.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentMyPrescriptionFullImage fragmentMyPrescriptionFullImage = new FragmentMyPrescriptionFullImage();
                    Bundle bundle = new Bundle();
                    bundle.putString("seletedImageName", mArrayList.get(position).getImageName());
                    fragmentMyPrescriptionFullImage.setArguments(bundle);
                    mainActivity.addFragment(fragmentMyPrescriptionFullImage, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, fragmentMyPrescriptionFullImage.getClass().getName());

                }
            });
        }

        public ArrayList<Uri> getCheckedItems() {

            for (int i = 0; i < mArrayList.size(); i++) {
                if (mSparseBooleanArray.get(i)) {
                    mTempArry.add(Uri.parse(mArrayList.get(i).getImageName()));
                }
            }
            return mTempArry;
        }

        @Override
        public int getItemCount() {
            return mArrayList.size();
        }

        public class MyViewHolderImageList extends RecyclerView.ViewHolder {
            ImageView mImageViewSelectIamge;
            CheckBox mCheckBoxSelect;
            int id;

            public MyViewHolderImageList(View itemView) {
                super(itemView);
                mImageViewSelectIamge = (ImageView) itemView.findViewById(R.id.row_my_prescription_imageview);
                mCheckBoxSelect = (CheckBox) itemView.findViewById(R.id.row_my_prescription_checkbox);
            }
        }
    }


    //    getPrescription List
    public void GetPresciptionList() {

        getLoaderManager().restartLoader(0, null, new LoaderManager.LoaderCallbacks<TaskExecutor>() {
            @Override
            public Loader<TaskExecutor> onCreateLoader(int arg0, Bundle arg1) {

                GetPresciptionListExecutor getPresciptionListExecutor = new GetPresciptionListExecutor(mainActivity, null);
                return new LoaderTask<TaskExecutor>(mainActivity, getPresciptionListExecutor);
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
                        try {
                            if (mServerResponseVO.getStatus().equalsIgnoreCase("true")) {
//                            Snackbar.with(mainActivity).text(mServerResponseVO.getMsg()).show(mainActivity);
                                mCommonHelper.hideKeyboard(mainActivity);


                                if (mServerResponseVO.getData() != null) {

                                    mArrayListImageList = (ArrayList<MYPrescriptionVO>) mServerResponseVO.getData();
                                    mLinearLayoutEmpty.setVisibility(View.GONE);
                                    mRelativeLayoutRecycle.setVisibility(View.VISIBLE);
                                    mTextViewDone.setVisibility(View.VISIBLE);
//
                                    if (mArrayListImageList != null) {
                                        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 3);
                                        mRecyclerViewMyPrescription.setLayoutManager(gridLayoutManager);
                                        imageListAdapter = new ImageListAdapter(mainActivity, mArrayListImageList);
                                        mRecyclerViewMyPrescription.setAdapter(imageListAdapter);
                                    }
                                }
                            } else {
                                mLinearLayoutEmpty.setVisibility(View.VISIBLE);
                                mRelativeLayoutRecycle.setVisibility(View.GONE);
                                mTextViewDone.setVisibility(View.GONE);
//                                Snackbar.with(mainActivity).text(mServerResponseVO.getMsg()).show(mainActivity);
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
    public class GetPresciptionListExecutor extends TaskExecutor {
        protected GetPresciptionListExecutor(Context context, Bundle bundle) {
            super(context, bundle);
        }

        @Override
        protected Object executeTask() {

            MyPrescirptionListService getAllAddressInfoService = new MyPrescirptionListService();
            mServerResponseVO = getAllAddressInfoService.GetPresciptionList(mainActivity, Tags.GetAllUploadedPrecription, mStringUserId);

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