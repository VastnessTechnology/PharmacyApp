package com.vgroyalchemist.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
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
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.vgroyalchemist.CommonClass.CircularProgressBar;
import com.vgroyalchemist.CommonClass.CommonClass;
import com.vgroyalchemist.R;
import com.vgroyalchemist.adapter.spinner_adapter;
import com.vgroyalchemist.controller.MainActivity;
import com.vgroyalchemist.loaders.LoaderTask;
import com.vgroyalchemist.loaders.TaskExecutor;
import com.vgroyalchemist.utils.CommonHelper;
import com.vgroyalchemist.utils.Snackbar;
import com.vgroyalchemist.utils.Tags;
import com.vgroyalchemist.utils.Utility;
import com.vgroyalchemist.vos.MedicineCategoryListVO;
import com.vgroyalchemist.vos.MedicineSearchList;
import com.vgroyalchemist.vos.ServerResponseVO;
import com.vgroyalchemist.webservice.AddCartInfoService;
import com.vgroyalchemist.webservice.CategoryMedicineListInfoService;
import com.vgroyalchemist.webservice.GetCountDataService;
import com.vgroyalchemist.webservice.PostParseGet;
import com.vgroyalchemist.webservice.SearchMedicineInfoService;

import java.util.ArrayList;
import java.util.Locale;

/* developed by krishna 24-03-2021
*  display category for medicine */
public class FragmentMedicineCategoryList extends NonCartFragment {


    public static final String FRAGMENT_ID = "28";
    public static MainActivity mainActivity;
    CommonHelper mCommonHelper;
    View fragmnetview = null;
    RecyclerView mRecyclerViewCategoryLis;
    LinearLayout mLinearLayoutEmpty;
    ArrayList<MedicineCategoryListVO> medicineCategoryListVOS;
    ServerResponseVO mServerResponseVO;
    ServerResponseVO  mServerResponseVOCount;
    PostParseGet mPostParseGet;
    String mStringategoryid;
    Bundle bundle;
    ServerResponseVO mServerResponseVOCard;
    String mStringUserid;
    String mStringPrice;
    String mStringqty;
    String mStringMedicineId;
    String Category_Name;

    // userdata
    public SharedPreferences mSharedPreferencesUserId;
    public SharedPreferences.Editor mEditor;

    ImageView mImageViewBack;
    TextView mTextViewTitle;
    ImageView mImageViewSearch;
    ImageView mImageViewCart;
    TextView mTextViewBageCart;

    //login
    public SharedPreferences mSharedPreferencesLoginFirst;
    SharedPreferences.Editor mEditorLogin;
    String GuestUser;

  //  TextView mTextViewCategoryTitle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = (MainActivity) getActivity();
        mCommonHelper = new CommonHelper();
        medicineCategoryListVOS= new ArrayList<MedicineCategoryListVO>();
        mPostParseGet = new PostParseGet(mainActivity);
        bundle = getArguments();
        if(bundle != null)
        {
            mStringategoryid = bundle.getString("Category_id");
            Category_Name = bundle.getString("Category_Name");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmnetview= inflater.inflate(R.layout.fragment_medicine_category_list, container, false);
        mRecyclerViewCategoryLis =fragmnetview.findViewById(R.id.fragment_Medicine_Category_recycleView);
        mLinearLayoutEmpty =fragmnetview.findViewById(R.id.fragment__Medicine_Category_relative_empty);

        mSharedPreferencesUserId = mainActivity.getSharedPreferences(Tags.TAG_SHARED_PREFERENCE_USER_ID, 0);
        mEditor = mSharedPreferencesUserId.edit();
        mStringUserid = mSharedPreferencesUserId.getString("UserId", "");

        mSharedPreferencesLoginFirst = mainActivity.getSharedPreferences(Tags.PREFS_NAME, 0);
        mEditorLogin = mSharedPreferencesLoginFirst.edit();
        GuestUser = mSharedPreferencesLoginFirst.getString("GuestUser", "0");

      //  mTextViewCategoryTitle = fragmnetview.findViewById(R.id.fragment__Medicine_Category_textview_title);

        CommonClass cmn = new CommonClass();
        cmn.FirebaseToken(getActivity());

        if(GuestUser.equalsIgnoreCase("1"))
        {
            mStringUserid = "00000000-0000-0000-0000-000000000000";
        }
        else {
//            Tags.mStringDeviceToken = "";
        }

        mImageViewBack = fragmnetview.findViewById(R.id.fragment_imageview_back);
        mTextViewTitle = fragmnetview.findViewById(R.id.fragment_title_textview);
        mImageViewSearch =fragmnetview.findViewById(R.id.fragment_Serach);
        mImageViewSearch.setVisibility(View.GONE);
        mImageViewCart =fragmnetview.findViewById(R.id.fragment_cart);
        mTextViewBageCart = fragmnetview.findViewById(R.id.actionbar_title_textview_badge_cart);
        mImageViewCart.setVisibility(View.VISIBLE);

        mTextViewTitle.setText(Category_Name + " Medicine List");

        ((SearchView) fragmnetview.findViewById(R.id.searchViewMedicine)).setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterData(newText);
                //    adapter.getFilter().filter(newText);
                return false;
            }
        });


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

        GetCategorychData();

        return fragmnetview;
    }

    private void filterData(String query) {
        ArrayList<MedicineCategoryListVO> searchLists = new ArrayList<>();
        for (MedicineCategoryListVO listData: medicineCategoryListVOS)
        {
            if (listData.getMedicineName().toLowerCase().contains(query))
            {
                searchLists.add(listData);
            }
        }

        MedicineCategoryList searchAdapter = new MedicineCategoryList(mainActivity, searchLists);
        mRecyclerViewCategoryLis.setHasFixedSize(true);
        mRecyclerViewCategoryLis.setLayoutManager(new LinearLayoutManager(mainActivity));
        mRecyclerViewCategoryLis.setAdapter(searchAdapter);
    }


    public void GetCategorychData() {

        getLoaderManager().restartLoader(0, null, new LoaderManager.LoaderCallbacks<TaskExecutor>() {
            @Override
            public Loader<TaskExecutor> onCreateLoader(int arg0, Bundle arg1) {

                MedicineCategoryListExecutor medicineCategoryListExecutor = new MedicineCategoryListExecutor(mainActivity, null);
                return new LoaderTask<TaskExecutor>(mainActivity, medicineCategoryListExecutor);
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

                                    medicineCategoryListVOS = (ArrayList<MedicineCategoryListVO>) mServerResponseVO.getData();
                                    if (medicineCategoryListVOS != null && medicineCategoryListVOS.size() > 0) {

                                        mRecyclerViewCategoryLis.setVisibility(View.VISIBLE);
                                        mLinearLayoutEmpty.setVisibility(View.GONE);

                                        MedicineCategoryList searchAdapter = new MedicineCategoryList(mainActivity, medicineCategoryListVOS);
                                        mRecyclerViewCategoryLis.setHasFixedSize(true);
                                        mRecyclerViewCategoryLis.setLayoutManager(new LinearLayoutManager(mainActivity));
                                        mRecyclerViewCategoryLis.setAdapter(searchAdapter);

                                    }
                                }
                            } else {
                                mLinearLayoutEmpty.setVisibility(View.VISIBLE);
                                mRecyclerViewCategoryLis.setVisibility(View.GONE);
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
    public class MedicineCategoryListExecutor extends TaskExecutor
    {
        protected MedicineCategoryListExecutor(Context context, Bundle bundle) {
            super(context, bundle);
        }

        @Override
        protected Object executeTask() {

            CategoryMedicineListInfoService categoryMedicineListInfoService = new CategoryMedicineListInfoService();
            mServerResponseVO = categoryMedicineListInfoService.ListInfomation(mainActivity, Tags.MedicineCategoryList,mStringategoryid,mStringUserid ,Tags.mStringDeviceToken);

            return null;
        }
    }


    public class MedicineCategoryList extends RecyclerView.Adapter<MedicineCategoryList.MyViewHolderMedicinesCatList>
    {
        Context mContext;
        ArrayList<MedicineCategoryListVO> medicineCategoryListVOS;
        public MedicineCategoryList(MainActivity mainActivity, ArrayList<MedicineCategoryListVO> medicineCategoryListVOS) {
            this.mContext=mainActivity;
            this.medicineCategoryListVOS=medicineCategoryListVOS;
        }

        @NonNull
        @Override
        public MyViewHolderMedicinesCatList onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_categorylist, parent, false);
            MyViewHolderMedicinesCatList myViewHolderMedicinesCatList = new MyViewHolderMedicinesCatList(v);
            return myViewHolderMedicinesCatList;
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull MyViewHolderMedicinesCatList holder, int position) {

            if(!medicineCategoryListVOS.get(position).getDiscount().equalsIgnoreCase("null") && !medicineCategoryListVOS.get(position).getDiscount().equalsIgnoreCase("0.0"))
            {
                holder.mTextViewDicount.setText(Tags.LABEL_CURRENCY_SIGN + Utility.getDecimalFormate(mainActivity,Double.parseDouble(medicineCategoryListVOS.get(position).getDiscount()),0.0,Tags.DECIMAL_FORMAT));
            }
            else {
                holder.mTextViewDicount.setVisibility(View.GONE);
            }

            if (!medicineCategoryListVOS.get(position).getManufacturer().equalsIgnoreCase("null")) {
                holder.mTextViewMedicineDescription.setText(medicineCategoryListVOS.get(position).getManufacturer());

            } else {
                holder.mTextViewMedicineDescription.setVisibility(View.GONE);
            }

            if(medicineCategoryListVOS.get(position).getTotalReview().equalsIgnoreCase("0.0") ||
                    medicineCategoryListVOS.get(position).getAvgReview() == 0.0 ||
                    medicineCategoryListVOS.get(position).getTotalRating().equalsIgnoreCase("0.0"))
            {
                holder.mLinearLayoutReview.setVisibility(View.GONE);
            }
            else {
                holder.mLinearLayoutReview.setVisibility(View.VISIBLE);
                if (!medicineCategoryListVOS.get(position).getTotalReview().equalsIgnoreCase("null")) {
                    holder.mTextViewUserAvg.setText(medicineCategoryListVOS.get(position).getTotalReview());

                } else {
                    holder.mTextViewUserAvg.setVisibility(View.GONE);
                }

                if(medicineCategoryListVOS.get(position).getAvgReview() != 0.0)
                {
                    holder.mRatingBar.setRating(medicineCategoryListVOS.get(position).getAvgReview());
                }
                else {

                    holder.mRatingBar.setVisibility(View.GONE);
                }
                if (!medicineCategoryListVOS.get(position).getTotalRating().equalsIgnoreCase("null")) {
                    holder.mTextViewRating.setText( " ( "+ medicineCategoryListVOS.get(position).getTotalRating() + " rating ) ");

                } else {
                    holder.mTextViewRating.setVisibility(View.GONE);
                }
            }


            if(!medicineCategoryListVOS.get(position).getMrp().equalsIgnoreCase("null"))
            {
                holder.mTextViewMrp.setText(Tags.LABEL_CURRENCY_SIGN + Utility.getDecimalFormate(mainActivity,Double.parseDouble(medicineCategoryListVOS.get(position).getMrp()),0.0,Tags.DECIMAL_FORMAT));
                holder.mTextViewMrp.setPaintFlags(holder.mTextViewMrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }
            else {
                holder.mTextViewMrp.setVisibility(View.GONE);
            }

            if(!medicineCategoryListVOS.get(position).getPrice().equalsIgnoreCase("null"))
            {
                holder.mTextViewPrice.setText(Tags.LABEL_CURRENCY_SIGN + Utility.getDecimalFormate(mainActivity,Double.parseDouble(medicineCategoryListVOS.get(position).getPrice()),0.0,Tags.DECIMAL_FORMAT));

            }
            else {
                holder.mTextViewPrice.setVisibility(View.GONE);
            }

            if(medicineCategoryListVOS.get(position).getPrice().equalsIgnoreCase(medicineCategoryListVOS.get(position).getMrp()) || medicineCategoryListVOS.get(position).getPrice().equalsIgnoreCase("null") )
            {
                holder.mTextViewMrp.setVisibility(View.GONE);
            }

            if(medicineCategoryListVOS.get(position).getStockAvailablity().equalsIgnoreCase("true"))
            {
                holder.mTextViewAdd.setVisibility(View.VISIBLE);
                holder.mTextViewOutOfStock.setVisibility(View.GONE);
            }
            else {
                holder.mTextViewAdd.setVisibility(View.GONE);
                holder.mTextViewOutOfStock.setVisibility(View.VISIBLE);
            }

            holder.mTextViewCategoryName.setText(medicineCategoryListVOS.get(position).getMedicineName());

            if(medicineCategoryListVOS.get(position).getImagePath() != null) {

                Glide.with(mContext).load(medicineCategoryListVOS.get(position).getImagePath())
                        .placeholder(R.drawable.ic_med)
                        .into(holder.mImageViewCategory);
            }
            else {
                Glide.with(mContext)
                        .load(R.drawable.ic_med)
                        .into(holder.mImageViewCategory);
            }

           holder.mTextViewAdd.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {

                   mStringMedicineId = medicineCategoryListVOS.get(position).getMedicineId();
                   mStringqty = "1";
                   mStringPrice = medicineCategoryListVOS.get(position).getPrice();

                   AddCartData();
               }
           });

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentProductDeatils fragmentProductDeatils = new FragmentProductDeatils();
                    Bundle mBundle  = new Bundle();
                    mBundle.putString("medicineid",medicineCategoryListVOS.get(position).getMedicineId());
                    fragmentProductDeatils.setArguments(mBundle);
                    mainActivity.addFragment(fragmentProductDeatils, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, fragmentProductDeatils.getClass().getName());
                }
            });
        }

        @Override
        public int getItemCount() {
            return medicineCategoryListVOS.size();
        }

        public class MyViewHolderMedicinesCatList extends RecyclerView.ViewHolder {

            ImageView mImageViewCategory;
            TextView mTextViewCategoryName;
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

            public MyViewHolderMedicinesCatList(View itemView) {
                super(itemView);

                mImageViewCategory= itemView.findViewById(R.id.row_catlog_imageview_main);
                mTextViewCategoryName= itemView.findViewById(R.id.row_catlog_textview_name);
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





    @Override
    public void doWork() {
        super.doWork();
        onResumeData();
    }

    @Override
    public void onResume() {
        super.onResume();
        mainActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        onResumeData();
    }

    public void onResumeData() {


    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mCommonHelper.freeMemory();
//        if (adViewBanner != null) {
//            adViewBanner.onDestroy();
//        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    //  get home count
    public void GetHomeCount() {
        getLoaderManager().restartLoader(0, null, new LoaderManager.LoaderCallbacks<TaskExecutor>() {
            @Override
            public Loader<TaskExecutor> onCreateLoader(int arg0, Bundle arg1) {

                GetHomeCountData searchMedicine = new GetHomeCountData(mainActivity, null);
                return new LoaderTask<TaskExecutor>(mainActivity, searchMedicine);
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
                            if (mServerResponseVOCount.getStatus().equalsIgnoreCase("true")) {
//                            Snackbar.with(mainActivity).text(mServerResponseVO.getMsg()).show(mainActivity);
                                mCommonHelper.hideKeyboard(mainActivity);
                                if (mServerResponseVOCount.getData() != null) {
                                    if (mServerResponseVOCount.getCartCount().equalsIgnoreCase("0")) {
                                        mTextViewBageCart.setVisibility(View.GONE);
                                    } else {
                                        mTextViewBageCart.setVisibility(View.VISIBLE);
                                        mTextViewBageCart.setText(mServerResponseVOCount.getCartCount());

                                    }
                                }
                            } else {
                                Snackbar.with(mainActivity).text(mServerResponseVOCount.getMsg()).show(mainActivity);
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
    public class GetHomeCountData extends TaskExecutor {
        protected GetHomeCountData(Context context, Bundle bundle) {
            super(context, bundle);
        }

        @Override
        protected Object executeTask() {
            GetCountDataService getAllAddressInfoService = new GetCountDataService();
            mServerResponseVOCount = getAllAddressInfoService.getdatacount(mainActivity, Tags.GETCOUNT, mStringUserid,Tags.mStringDeviceToken);

            return null;
        }
    }

}