package com.vgroyalchemist.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.vgroyalchemist.CommonClass.CommonClass;
import com.vgroyalchemist.R;
import com.vgroyalchemist.controller.MainActivity;
import com.vgroyalchemist.loaders.LoaderTask;
import com.vgroyalchemist.loaders.TaskExecutor;
import com.vgroyalchemist.utils.CommonHelper;
import com.vgroyalchemist.utils.Snackbar;
import com.vgroyalchemist.utils.Tags;
import com.vgroyalchemist.utils.Utility;
import com.vgroyalchemist.vos.AltMedicineVO;
import com.vgroyalchemist.vos.ServerResponseVO;
import com.vgroyalchemist.webservice.AddCartInfoService;
import com.vgroyalchemist.webservice.GetAltMedicineService;
import com.vgroyalchemist.webservice.PostParseGet;

import java.util.ArrayList;

/* developed by krishna 24-03-2021
*  find alternate  medicine list */
public class FragmentFindSubstitutes extends NonCartFragment {


    //Variable Declaration
    public static final String FRAGMENT_ID = "32";

    public static MainActivity mainActivity;
    public View fragmentView = null;
    CommonHelper mCommonHelper;

    ImageView mImageViewBack;
    TextView mTextViewTitle;
    ImageView mImageViewSearch;
    ImageView mImageViewCart;
    TextView mTextViewBageCart;

    PostParseGet mPostParseGet;

    TextView MedicineName;
    TextView mTextViewManufacture;
    TextView mTextViewPirce;
    ImageView mImageView;
    TextView mTextViewNodata;
    TextView mTextViewTitleSubstitue;
    RecyclerView mRecyclerViewSubstitue;
    RelativeLayout mRelativeLayoutSubstitue;
    LinearLayout mRelativeLayoutMain;

    Bundle mBundle;

    // userdata
    public SharedPreferences mSharedPreferencesUserId;
    public SharedPreferences.Editor mEditor;

    String mStringUserid;
    String mStringMedicineName;
    String mStringManufauture;
    String mStringPrice;
    String mStringMedicineID;
    String mStringImagePath;
    String mStringMedicineId;
    String mStringqty;

    ServerResponseVO mServerResponseVO;
    ServerResponseVO mServerResponseVOCard;

    ArrayList<AltMedicineVO> mArrayListAltMedicine;

    //login
    public SharedPreferences mSharedPreferencesLoginFirst;
    SharedPreferences.Editor EditorLogin;
    String GuestUser;


    //    count shared preferance
    public SharedPreferences mSharedPreferencesCount;
    public SharedPreferences.Editor mEditorCount;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = (MainActivity) getActivity();
        mCommonHelper = new CommonHelper();
        mPostParseGet = new PostParseGet(mainActivity);
        mArrayListAltMedicine = new ArrayList<AltMedicineVO>();
        mBundle = getArguments();

        if(mBundle != null)
        {
            mStringMedicineID = mBundle.getString("MedicineID");
            mStringMedicineName = mBundle.getString("medicineName");
            mStringManufauture = mBundle.getString("Manufacture");
            mStringPrice = mBundle.getString("Price");
            mStringImagePath = mBundle.getString("imagePath");
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentView = inflater.inflate(R.layout.fragment_find_substitutes, container, false);

        mImageViewBack = fragmentView.findViewById(R.id.fragment_imageview_back);
        mTextViewTitle = fragmentView.findViewById(R.id.fragment_title_textview);
        mTextViewTitle.setText("Find Substitutes");
        mImageViewSearch = fragmentView.findViewById(R.id.fragment_Serach);
        mImageViewSearch.setVisibility(View.GONE);
        mImageViewCart = fragmentView.findViewById(R.id.fragment_cart);
        mImageViewCart.setVisibility(View.VISIBLE);
        mTextViewBageCart = fragmentView.findViewById(R.id.actionbar_title_textview_badge_cart);


        MedicineName = fragmentView.findViewById(R.id.fragment_find_substitutes_textview__medicine_name);
        mTextViewManufacture = fragmentView.findViewById(R.id.fragment_find_substitutes_textview_menufacture);
        mTextViewPirce = fragmentView.findViewById(R.id.fragment_find_substitutes_textview_price);
        mImageView = fragmentView.findViewById(R.id.fragment_find_substitutes_imageview);
        mTextViewNodata =fragmentView.findViewById(R.id.fragment_find_substitutes_textview_no_data);
        mTextViewTitleSubstitue =fragmentView.findViewById(R.id.fragment_find_substitutes_title_textview);
        mRecyclerViewSubstitue =fragmentView.findViewById(R.id.fragment_find_substitutes_recycleview);
        mRelativeLayoutSubstitue =fragmentView.findViewById(R.id.fragment_find_substitutes_relative);
        mRelativeLayoutMain = fragmentView.findViewById(R.id.fragment_find_substitutes_relative_main);

        mSharedPreferencesUserId = mainActivity.getSharedPreferences(Tags.TAG_SHARED_PREFERENCE_USER_ID, 0);
        mEditor = mSharedPreferencesUserId.edit();
        mStringUserid = mSharedPreferencesUserId.getString("UserId", "");

        mSharedPreferencesLoginFirst = mainActivity.getSharedPreferences(Tags.PREFS_NAME, 0);
        EditorLogin = mSharedPreferencesLoginFirst.edit();
        GuestUser = mSharedPreferencesLoginFirst.getString("GuestUser", "0");

        CommonClass cmn = new CommonClass();
        cmn.FirebaseToken(getActivity());

        if(GuestUser.equalsIgnoreCase("1"))
        {
            mStringUserid = "00000000-0000-0000-0000-000000000000";
        }
        else {
//            Tags.mStringDeviceToken = "";
        }

        mSharedPreferencesCount = mainActivity.getSharedPreferences(Tags.TAG_SHARED_PREFERENCE_Cart_Count, 0);
        mEditorCount = mSharedPreferencesCount.edit();


        if(mStringMedicineName != null)
        {
            MedicineName.setText(mStringMedicineName);
        }
        if(mStringPrice != null)
        {
            mTextViewPirce.setText(Tags.LABEL_CURRENCY_SIGN + Utility.getDecimalFormate(mainActivity,Double.parseDouble(mStringPrice),0.0,Tags.DECIMAL_FORMAT));
        }

        if(mStringManufauture != null)
        {
            mTextViewManufacture.setText(mStringManufauture);
        }
        mImageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mainActivity.onBackPressed();
            }
        });

        mImageViewCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentCart fragmentCart = new FragmentCart();
                mainActivity.addFragment(fragmentCart, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, fragmentCart.getClass().getName());
            }
        });

        if (mStringImagePath != null) {

            Glide.with(mainActivity).load(mStringImagePath)
                    .placeholder(R.drawable.ic_med)
                    .into(mImageView);
        } else {
            Glide.with(mainActivity)
                    .load(R.drawable.ic_med)
                    .into(mImageView);
        }

        GetAltMedicineData();

        return fragmentView;
    }



    public void GetAltMedicineData() {

        getLoaderManager().restartLoader(0, null, new LoaderManager.LoaderCallbacks<TaskExecutor>() {
            @Override
            public Loader<TaskExecutor> onCreateLoader(int arg0, Bundle arg1) {

                GetAltMedicineExecutor getAltMedicineExecutor = new GetAltMedicineExecutor(mainActivity, null);
                return new LoaderTask<TaskExecutor>(mainActivity, getAltMedicineExecutor);
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

                                    mRelativeLayoutSubstitue.setVisibility(View.VISIBLE);
                                    mTextViewNodata.setVisibility(View.GONE);

                                    mArrayListAltMedicine = (ArrayList<AltMedicineVO>) mServerResponseVO.getData();
                                    if (mArrayListAltMedicine != null && mArrayListAltMedicine.size() > 0) {
                                        mTextViewTitleSubstitue.setText("Substitutes " + "(" + mArrayListAltMedicine.size() + ")");
                                        AltMedicineListAdpter altMedicineListAdpter = new AltMedicineListAdpter(mainActivity, mArrayListAltMedicine);
                                        mRecyclerViewSubstitue.setHasFixedSize(true);
                                        mRecyclerViewSubstitue.setLayoutManager(new LinearLayoutManager(mainActivity));
                                        mRecyclerViewSubstitue.setAdapter(altMedicineListAdpter);
                                    }
                                }
                            } else {
                                mRelativeLayoutSubstitue.setVisibility(View.GONE);
                                mTextViewNodata.setVisibility(View.VISIBLE);
//                            Snackbar.with(mainActivity).text(mServerResponseVO.getMsg()).show(mainActivity);
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
    public class GetAltMedicineExecutor extends TaskExecutor {
        protected GetAltMedicineExecutor(Context context, Bundle bundle) {
            super(context, bundle);
        }

        @Override
        protected Object executeTask() {

            GetAltMedicineService getAllOrderService = new GetAltMedicineService();
            mServerResponseVO = getAllOrderService.getAltMedicine(mainActivity, Tags.FINDALMEDICINE, mStringMedicineID ,mStringUserid,Tags.mStringDeviceToken);

            return null;
        }
    }


    public class AltMedicineListAdpter extends RecyclerView.Adapter<AltMedicineListAdpter.MyViewHolderAltMedicineList> {


        Context mContext;
        ArrayList<AltMedicineVO> mArrayListitem;

        public AltMedicineListAdpter(MainActivity mainActivity, ArrayList<AltMedicineVO> mArrayListReview) {
            this.mContext = mainActivity;
            this.mArrayListitem = mArrayListReview;
        }

        @NonNull
        @Override
        public MyViewHolderAltMedicineList onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_categorylist, parent, false);
            MyViewHolderAltMedicineList viewHolderOrderItem = new MyViewHolderAltMedicineList(v);
            return viewHolderOrderItem;
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull MyViewHolderAltMedicineList holder, @SuppressLint("RecyclerView") int position) {

            if(mArrayListitem.get(position).getMedicineName() != null && !mArrayListitem.get(position).getMedicineName().equalsIgnoreCase("null"))
            {
                holder.mTextViewMedicineName.setText(mArrayListitem.get(position).getMedicineName());
            }

            if(mArrayListitem.get(position).getManufacturer() != null && !mArrayListitem.get(position).getManufacturer().equalsIgnoreCase("null"))
            {
                holder.mTextViewMedicineDescription.setText(mArrayListitem.get(position).getManufacturer());
            }
            else {
                holder.mTextViewMedicineDescription.setVisibility(View.GONE);
            }
            if(mArrayListitem.get(position).getPrice() != null && !mArrayListitem.get(position).getPrice().equalsIgnoreCase("null") )
            {
                holder.mTextViewPrice.setText(Tags.LABEL_CURRENCY_SIGN + Utility.getDecimalFormate(mainActivity,Double.parseDouble(mArrayListitem.get(position).getPrice()),0.0,Tags.DECIMAL_FORMAT));
            }
            else {
                holder.mTextViewPrice.setVisibility(View.GONE);
            }

            if(mArrayListitem.get(position).getTotalReview().equalsIgnoreCase("0.0") ||
                    mArrayListitem.get(position).getAvgReview() == 0.0 ||
                    mArrayListitem.get(position).getTotalRating().equalsIgnoreCase("0.0"))
            {
                holder.mLinearLayoutReview.setVisibility(View.GONE);
            }
            else {
                holder.mLinearLayoutReview.setVisibility(View.VISIBLE);
                if (!mArrayListitem.get(position).getTotalReview().equalsIgnoreCase("null")) {
                    holder.mTextViewUserAvg.setText(mArrayListitem.get(position).getTotalReview());

                } else {
                    holder.mTextViewUserAvg.setVisibility(View.GONE);
                }

                if(mArrayListitem.get(position).getAvgReview() != 0.0)
                {
                    holder.mRatingBar.setRating(mArrayListitem.get(position).getAvgReview());
                }
                else {

                    holder.mRatingBar.setVisibility(View.GONE);
                }
                if (!mArrayListitem.get(position).getTotalRating().equalsIgnoreCase("null")) {
                    holder.mTextViewRating.setText( " ( "+ mArrayListitem.get(position).getTotalRating() + " rating ) ");

                } else {
                    holder.mTextViewRating.setVisibility(View.GONE);
                }
            }
            if(mArrayListitem.get(position).getStockAvailablity().equalsIgnoreCase("true"))
            {
                holder.mTextViewOutOfStock.setVisibility(View.GONE);
                holder.mTextViewAdd.setVisibility(View.VISIBLE);
            }
            else {
                holder.mTextViewOutOfStock.setVisibility(View.VISIBLE);
                holder.mTextViewAdd.setVisibility(View.GONE);
            }

            if (mArrayListitem.get(position).getMedicineImgPath() != null && !mArrayListitem.get(position).getMedicineImgPath().equalsIgnoreCase("null")) {

                holder.mImageView.setVisibility(View.VISIBLE);
                Glide.with(mContext).load(mArrayListitem.get(position).getMedicineImgPath())
                        .placeholder(R.drawable.ic_med)
                        .into(holder.mImageView);
            } else {
                holder.mImageView.setVisibility(View.VISIBLE);
                Glide.with(mContext)
                        .load(R.drawable.ic_med)
                        .into(holder.mImageView);
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentProductDeatils fragmentProductDeatils = new FragmentProductDeatils();
                    Bundle mBundle  = new Bundle();
                    mBundle.putString("medicineid",mArrayListitem.get(position).getAltMedicineId());
                    fragmentProductDeatils.setArguments(mBundle);
                    mainActivity.addFragment(fragmentProductDeatils, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, fragmentProductDeatils.getClass().getName());
                }
            });

            holder.mTextViewAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mStringMedicineId = mArrayListitem.get(position).getMedicineId();
                    mStringqty = "1";
                    mStringPrice = mArrayListitem.get(position).getPrice();

                    AddCartData();
                }
            });
        }
        @Override
        public int getItemCount() {
            return mArrayListitem.size();
        }

        public class MyViewHolderAltMedicineList extends RecyclerView.ViewHolder {

            ImageView mImageView;
            TextView mTextViewMedicineName;
            TextView mTextViewPrice;
            TextView mTextViewDicount;
            TextView mTextViewMrp;
            TextView mTextViewAdd;
            TextView mTextViewOutOfStock;
            TextView mTextViewMedicineDescription;
            TextView mTextViewUserAvg;
            TextView mTextViewRating;
            RatingBar mRatingBar;
            LinearLayout mLinearLayoutReview;

            public MyViewHolderAltMedicineList(View itemView) {
                super(itemView);
                mImageView= itemView.findViewById(R.id.row_catlog_imageview_main);
                mTextViewMedicineName = itemView.findViewById(R.id.row_catlog_textview_name);
                mTextViewPrice =itemView.findViewById(R.id.row_catlog_textview_price);
                mTextViewMrp =itemView.findViewById(R.id.row_catlog_textview_mrp_price);
                mTextViewDicount =itemView.findViewById(R.id.row_catlog_textview_dicount);
                mTextViewAdd =itemView.findViewById(R.id.row_catlog_imageview_cart);
                mTextViewOutOfStock= itemView.findViewById(R.id.row_catlog_imageview_out_of_stock);

                mTextViewMedicineDescription = itemView.findViewById(R.id.row_catlog_textview_Medicine_Description);
                mTextViewUserAvg = itemView.findViewById(R.id.row_catlog_textview_UserAge);
                mTextViewRating = itemView.findViewById(R.id.row_catlog_textview_TotalRating);
                mRatingBar = itemView.findViewById(R.id.row_catlog_ratingBar);
                mLinearLayoutReview = itemView.findViewById(R.id.row_review_linear);
            }
        }
    }


    public void AddCartData() {

        getLoaderManager().restartLoader(0, null, new LoaderManager.LoaderCallbacks<TaskExecutor>() {
            @Override
            public Loader<TaskExecutor> onCreateLoader(int arg0, Bundle arg1) {
                AddCartExecutor updateCartQty = new AddCartExecutor(mainActivity, null);
                return new LoaderTask<TaskExecutor>(mainActivity, updateCartQty);
            }

            @Override
            public void onLoadFinished(Loader<TaskExecutor> arg0, TaskExecutor arg1) {

                if (isAdded()) {

                    getLoaderManager().destroyLoader(0);
                    if (mPostParseGet.isNetError) {
                        Snackbar.with(mainActivity).text(getResources().getString(R.string.interneterror)).show(mainActivity);
                    } else if (mPostParseGet.isOtherError)
                        Snackbar.with(mainActivity).text(getResources().getString(R.string.notabletocommunicatewithserver)).show(mainActivity);
                    else {
                        if (mServerResponseVOCard != null && mServerResponseVOCard.getStatus() != null) {

                            try {
                                if (mServerResponseVOCard.getStatus().equalsIgnoreCase("true")) {

                                    if (mServerResponseVOCard.getData() != null) {
                                        Snackbar.with(mainActivity).text(mServerResponseVOCard.getMsg()).show(mainActivity);
                                        if(mServerResponseVOCard.getCartCount().equalsIgnoreCase("0"))
                                        {
                                            mTextViewBageCart.setVisibility(View.GONE);
                                        }
                                        else {
                                            mTextViewBageCart.setVisibility(View.VISIBLE);
                                            mTextViewBageCart.setText(mServerResponseVOCard.getCartCount());

                                            mEditorCount.putString(Tags.COUNT, mServerResponseVOCard.getCartCount());
                                            mEditorCount.apply();
                                        }
                                    }
                                } else {
                                    Snackbar.with(mainActivity).text(mServerResponseVOCard.getMsg()).show(mainActivity);
                                    mCommonHelper.hideKeyboard(mainActivity);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
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


    public class AddCartExecutor extends TaskExecutor {

        protected AddCartExecutor(Context context, Bundle bundle) {
            super(context, bundle);
        }

        @Override
        protected Object executeTask() {

            AddCartInfoService addCartInfoService = new AddCartInfoService();
            try {
                mServerResponseVOCard = addCartInfoService.AddCartinfo(mainActivity, Tags.AddCard, "", mStringUserid, mStringPrice, mStringqty, mStringMedicineId, Tags.mStringDeviceToken);
            } catch (Exception e) {
                e.printStackTrace();
            }

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