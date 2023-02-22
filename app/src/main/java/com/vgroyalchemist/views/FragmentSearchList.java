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
import com.vgroyalchemist.vos.MedicineSearchList;
import com.vgroyalchemist.vos.ServerResponseVO;
import com.vgroyalchemist.webservice.AddCartInfoService;
import com.vgroyalchemist.webservice.PostParseGet;
import com.vgroyalchemist.webservice.SearchMedicineInfoService;

import java.util.ArrayList;

/**
 * Create by krishna vaddoriya 20-12-2021
 */
public class FragmentSearchList extends NonCartFragment {

    public static final String FRAGMENT_ID = "65";
    public static MainActivity mainActivity;
    CommonHelper mCommonHelper;
    View fragmnetview = null;
    RecyclerView mRecyclerViewSearchList;
    LinearLayout mLinearLayoutEmpty;
    ArrayList<MedicineSearchList> MedicineSearchListVo;
    ServerResponseVO mServerResponseVOSearch;
    ServerResponseVO mServerResponseVOCount;
    PostParseGet mPostParseGet;
    String mStringSearch;
    Bundle bundle;
    ServerResponseVO mServerResponseVOCard;
    String mStringUserid;
    String mStringPrice;
    String mStringqty;
    String mStringMedicineId;

    // userdata
    public SharedPreferences mSharedPreferencesUserId;
    public SharedPreferences.Editor mEditor;

    ImageView mImageViewBack;
    TextView mTextViewTitle;
    ImageView mImageViewSearch;
    ImageView mImageViewCart;
    TextView mTextViewBageCart;


    public SharedPreferences mSharedPreferencesCount;
    public SharedPreferences.Editor mEditorCount;

    //login
    public SharedPreferences mSharedPreferencesLoginFirst;
    SharedPreferences.Editor EditorLogin;
    String GuestUser;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = (MainActivity) getActivity();
        mCommonHelper = new CommonHelper();
        MedicineSearchListVo = new ArrayList<MedicineSearchList>();
        mPostParseGet = new PostParseGet(mainActivity);
        bundle = getArguments();
        if (bundle != null) {
            mStringSearch = bundle.getString("SearchText");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        fragmnetview = inflater.inflate(R.layout.fragment_search_list, container, false);

        mRecyclerViewSearchList = fragmnetview.findViewById(R.id.fragment_Medicine_Search_recycleView);
        mLinearLayoutEmpty = fragmnetview.findViewById(R.id.fragment__Medicine_Search_relative_empty);

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


        mImageViewBack = fragmnetview.findViewById(R.id.fragment_imageview_back);
        mTextViewTitle = fragmnetview.findViewById(R.id.fragment_title_textview);
        mImageViewSearch = fragmnetview.findViewById(R.id.fragment_Serach);
        mImageViewSearch.setVisibility(View.GONE);
        mImageViewCart = fragmnetview.findViewById(R.id.fragment_cart);
        mTextViewBageCart = fragmnetview.findViewById(R.id.actionbar_title_textview_badge_cart);
        mImageViewCart.setVisibility(View.VISIBLE);


        mSharedPreferencesCount = mainActivity.getSharedPreferences(Tags.TAG_SHARED_PREFERENCE_Cart_Count, 0);
        mEditorCount = mSharedPreferencesCount.edit();
        String Count = mSharedPreferencesCount.getString(Tags.COUNT, "0");
        if(Count.equalsIgnoreCase("0")) {
            mTextViewBageCart.setVisibility(View.GONE);
        }
        else {
            mTextViewBageCart.setVisibility(View.VISIBLE);
            mTextViewBageCart.setText(Count);
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
                FragmentCart  fragmentCart = new FragmentCart();
                mainActivity.addFragment(fragmentCart,true,FragmentTransaction.TRANSIT_FRAGMENT_OPEN,fragmentCart.getClass().getName());
            }
        });
        GetSearchData();

        return fragmnetview;
    }


    public void GetSearchData() {

        getLoaderManager().restartLoader(0, null, new LoaderManager.LoaderCallbacks<TaskExecutor>() {
            @Override
            public Loader<TaskExecutor> onCreateLoader(int arg0, Bundle arg1) {

                SearchMedicine searchMedicine = new SearchMedicine(mainActivity, null);
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
                            if (mServerResponseVOSearch.getStatus().equalsIgnoreCase("true")) {
//                            Snackbar.with(mainActivity).text(mServerResponseVO.getMsg()).show(mainActivity);
                                mCommonHelper.hideKeyboard(mainActivity);
                                if (mServerResponseVOSearch.getData() != null) {

                                    MedicineSearchListVo = (ArrayList<MedicineSearchList>) mServerResponseVOSearch.getData();
                                    if (MedicineSearchListVo != null && MedicineSearchListVo.size() > 0) {

                                        mRecyclerViewSearchList.setVisibility(View.VISIBLE);
                                        mLinearLayoutEmpty.setVisibility(View.GONE);


                                        MedicineCategoryList searchAdapter = new MedicineCategoryList(mainActivity, MedicineSearchListVo);
                                        mRecyclerViewSearchList.setHasFixedSize(true);
                                        mRecyclerViewSearchList.setLayoutManager(new LinearLayoutManager(mainActivity));
                                        mRecyclerViewSearchList.setAdapter(searchAdapter);

                                    }
                                }
                            } else {

                                mLinearLayoutEmpty.setVisibility(View.VISIBLE);
                                mRecyclerViewSearchList.setVisibility(View.GONE);
//                                mCommonHelper.hideKeyboard(mainActivity);
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
    public class SearchMedicine extends TaskExecutor {
        protected SearchMedicine(Context context, Bundle bundle) {
            super(context, bundle);
        }

        @Override
        protected Object executeTask() {

            SearchMedicineInfoService getAllAddressInfoService = new SearchMedicineInfoService();
            mServerResponseVOSearch = getAllAddressInfoService.fetchLoginUserInformation(mainActivity, Tags.SearchMedicine, mStringSearch,mStringUserid,Tags.mStringDeviceToken);

            return null;
        }
    }


    public class MedicineCategoryList extends RecyclerView.Adapter<MedicineCategoryList.MyViewHolderMedicinesCatList> {
        Context mContext;
        ArrayList<MedicineSearchList> medicineSearchListArrayList;

        public MedicineCategoryList(MainActivity mainActivity, ArrayList<MedicineSearchList> medicineCategoryListVOS) {
            this.mContext = mainActivity;
            this.medicineSearchListArrayList = medicineCategoryListVOS;
        }

        @NonNull
        @Override
        public MedicineCategoryList.MyViewHolderMedicinesCatList onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_categorylist, parent, false);
            return new MyViewHolderMedicinesCatList(v);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull MedicineCategoryList.MyViewHolderMedicinesCatList holder, @SuppressLint("RecyclerView") int position) {

            if (!medicineSearchListArrayList.get(position).getDiscount().equalsIgnoreCase("null") && !medicineSearchListArrayList.get(position).getDiscount().equalsIgnoreCase("0.0")) {
                holder.mTextViewDicount.setText(Tags.LABEL_CURRENCY_SIGN + Utility.getDecimalFormate(mainActivity, Double.parseDouble(medicineSearchListArrayList.get(position).getDiscount()), 0.0, Tags.DECIMAL_FORMAT));
            } else {
                holder.mTextViewDicount.setVisibility(View.GONE);
            }

            if (!medicineSearchListArrayList.get(position).getManufacturer().equalsIgnoreCase("null")) {
                holder.mTextViewMedicineDescription.setText(medicineSearchListArrayList.get(position).getManufacturer());

            } else {
                holder.mTextViewMedicineDescription.setVisibility(View.GONE);
            }

            if(medicineSearchListArrayList.get(position).getTotalReview().equalsIgnoreCase("0.0") ||
                    medicineSearchListArrayList.get(position).getAvgReview() == 0.0 ||
                    medicineSearchListArrayList.get(position).getTotalRating().equalsIgnoreCase("0.0"))
            {
                holder.mLinearLayoutReview.setVisibility(View.GONE);
            }
            else {
                holder.mLinearLayoutReview.setVisibility(View.VISIBLE);
                if (!medicineSearchListArrayList.get(position).getTotalReview().equalsIgnoreCase("null")) {
                    holder.mTextViewUserAvg.setText(medicineSearchListArrayList.get(position).getTotalReview());

                } else {
                    holder.mTextViewUserAvg.setVisibility(View.GONE);
                }

                if(medicineSearchListArrayList.get(position).getAvgReview() != 0.0)
                {
                    holder.mRatingBar.setRating(medicineSearchListArrayList.get(position).getAvgReview());
                }
                else {

                    holder.mRatingBar.setVisibility(View.GONE);
                }
                if (!medicineSearchListArrayList.get(position).getTotalRating().equalsIgnoreCase("null")) {
                    holder.mTextViewRating.setText( " ( "+ medicineSearchListArrayList.get(position).getTotalRating() + " rating ) ");

                } else {
                    holder.mTextViewRating.setVisibility(View.GONE);
                }
            }

            if (!medicineSearchListArrayList.get(position).getMrp().equalsIgnoreCase("null")) {
                holder.mTextViewMrp.setText(Tags.LABEL_CURRENCY_SIGN + Utility.getDecimalFormate(mainActivity, Double.parseDouble(medicineSearchListArrayList.get(position).getMrp()), 0.0, Tags.DECIMAL_FORMAT));
                holder.mTextViewMrp.setPaintFlags(holder.mTextViewMrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                holder.mTextViewMrp.setVisibility(View.GONE);
            }

            if (!medicineSearchListArrayList.get(position).getPrice().equalsIgnoreCase("null")) {
                holder.mTextViewPrice.setText(Tags.LABEL_CURRENCY_SIGN + Utility.getDecimalFormate(mainActivity, Double.parseDouble(medicineSearchListArrayList.get(position).getPrice()), 0.0, Tags.DECIMAL_FORMAT));

            } else {
                holder.mTextViewPrice.setVisibility(View.GONE);
            }

            if (medicineSearchListArrayList.get(position).getPrice().equalsIgnoreCase(medicineSearchListArrayList.get(position).getMrp()) || medicineSearchListArrayList.get(position).getPrice().equalsIgnoreCase("null")) {
                holder.mTextViewMrp.setVisibility(View.GONE);
            }

            if (medicineSearchListArrayList.get(position).getStockAvailablity().equalsIgnoreCase("true")) {
                holder.mTextViewAdd.setVisibility(View.VISIBLE);
                holder.mTextViewOutOfStock.setVisibility(View.GONE);
            } else {
                holder.mTextViewAdd.setVisibility(View.GONE);
                holder.mTextViewOutOfStock.setVisibility(View.VISIBLE);
            }

            holder.mTextViewCategoryName.setText(medicineSearchListArrayList.get(position).getMedicineName());

            if (medicineSearchListArrayList.get(position).getImagePath() != null) {

                Glide.with(mContext).load(medicineSearchListArrayList.get(position).getImagePath())
                        .placeholder(R.drawable.ic_med)
                        .into(holder.mImageViewCategory);
            } else {
                Glide.with(mContext)
                        .load(R.drawable.ic_med)
                        .into(holder.mImageViewCategory);
            }

            holder.mTextViewAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mStringMedicineId = medicineSearchListArrayList.get(position).getMedicineId();
                    mStringqty = "1";
                    mStringPrice = medicineSearchListArrayList.get(position).getPrice();

                    AddCartData();
                }
            });

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentProductDeatils fragmentProductDeatils = new FragmentProductDeatils();
                    Bundle mBundle = new Bundle();
                    mBundle.putString("medicineid", medicineSearchListArrayList.get(position).getMedicineId());
                    fragmentProductDeatils.setArguments(mBundle);
                    mainActivity.addFragment(fragmentProductDeatils, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, fragmentProductDeatils.getClass().getName());
                }
            });


        }


        @Override
        public int getItemCount() {
            return medicineSearchListArrayList.size();
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

                mImageViewCategory = itemView.findViewById(R.id.row_catlog_imageview_main);
                mTextViewCategoryName = itemView.findViewById(R.id.row_catlog_textview_name);
                mTextViewPrice = itemView.findViewById(R.id.row_catlog_textview_price);
                mTextViewMrp = itemView.findViewById(R.id.row_catlog_textview_mrp_price);
                mTextViewDicount = itemView.findViewById(R.id.row_catlog_textview_dicount);
                mTextViewAdd = itemView.findViewById(R.id.row_catlog_imageview_cart);
                mTextViewOutOfStock = itemView.findViewById(R.id.row_catlog_imageview_out_of_stock);

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

                                            mEditorCount.putString(Tags.COUNT, mServerResponseVOCount.getNotiCount());
                                            mEditorCount.commit();
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
                mServerResponseVOCard = addCartInfoService.AddCartinfo(mainActivity, Tags.AddCard, "", mStringUserid, mStringPrice, mStringqty, mStringMedicineId ,Tags.mStringDeviceToken);
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
        mainActivity.getSharedPreferences(Tags.TAG_SHARED_PREFERENCE_Cart_Count,0).edit().clear().apply();
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

}