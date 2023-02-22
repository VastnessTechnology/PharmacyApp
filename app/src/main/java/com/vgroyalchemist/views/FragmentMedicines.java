package com.vgroyalchemist.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.nfc.Tag;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.vgroyalchemist.CommonClass.CircularProgressBar;
import com.vgroyalchemist.CommonClass.CommonClass;
import com.vgroyalchemist.R;
import com.vgroyalchemist.adapter.spinner_adapter;
import com.vgroyalchemist.controller.MainActivity;
import com.vgroyalchemist.loaders.LoaderTask;
import com.vgroyalchemist.loaders.LoaderTaskWithoutProgressDialog;
import com.vgroyalchemist.loaders.TaskExecutor;
import com.vgroyalchemist.utils.CirclePageIndicator;
import com.vgroyalchemist.utils.CommonHelper;
import com.vgroyalchemist.utils.CustomAdapterInfinite;
import com.vgroyalchemist.utils.InfiniteViewPager;
import com.vgroyalchemist.utils.Snackbar;
import com.vgroyalchemist.utils.Tags;
import com.vgroyalchemist.utils.Utility;
import com.vgroyalchemist.vos.CategoryList;
import com.vgroyalchemist.vos.GetAllBannerMenuVO;
import com.vgroyalchemist.vos.MedicineSearchList;
import com.vgroyalchemist.vos.ServerResponseVO;
import com.vgroyalchemist.webservice.AddCartInfoService;
import com.vgroyalchemist.webservice.BannerMenuInfoService;
import com.vgroyalchemist.webservice.GetAllBannerMenuInfoService;
import com.vgroyalchemist.webservice.GetCountDataService;
import com.vgroyalchemist.webservice.MedicinListInfoService;
import com.vgroyalchemist.webservice.PostParseGet;
import com.vgroyalchemist.webservice.SearchMedicineInfoService;

import java.util.ArrayList;

import static com.facebook.FacebookSdk.getApplicationContext;

/* developed by krishna 24-03-2021
*   search medicine name ,display medicine category */
public class FragmentMedicines extends NonCartFragment {

    public static final String FRAGMENT_ID = "12";


    public static MainActivity mainActivity;
    CommonHelper mCommonHelper;
    View fragmnetview = null;

    TextView mTextViewPresciptionUpload;

    RecyclerView mRecyclerViewCategory;
    RecyclerView mRecyclerViewIntelignt;
    RecyclerView mRecyclerViewData;
    LinearLayout mLinearLayoutCategoryTitle;
    TextView mTextViewNoData;
    CircularProgressBar pgbProductList;
    ImageView mImageViewClear;
    AutoCompleteTextView mEditTextSearch;
    RelativeLayout mRelativeLayoutMain;
    private String mStringSearch = "";
    ArrayList<CategoryList> mArrayListCategory;
    ArrayList<MedicineSearchList> mArrayListSearch;
    ArrayList<GetAllBannerMenuVO> getAllBannerMenuVOS;

    ArrayAdapter<String> mAttributeAdapter;
    ArrayList<String> mArrayListSpinner;
    PostParseGet mPostParseGet;
    ServerResponseVO mServerResponseVOCategoryList;
    ServerResponseVO mServerResponseVOSearch;
    ServerResponseVO mServerResponseVO;
    ServerResponseVO mServerResponseVOCount;

    // userdata
    public SharedPreferences mSharedPreferencesUserId;
    public SharedPreferences.Editor mEditor;


    //    count shared preferance
    public SharedPreferences mSharedPreferencesCount;
    public SharedPreferences.Editor mEditorCount;


    String mStringUserid;
    String mStringPrice;
    String mStringqty;
    String mStringMedicineId;
    String CartCount;
    public ServerResponseVO mServerResponseVOCard;

    String mStringMenuName;
    String mStringMenuId;

    public static InfiniteViewPager viewpager_infinite;
    public static CirclePageIndicator indicator;

    ImageView mImageViewBack;
    TextView mTextViewTitle;
    ImageView mImageViewSearch;
    ImageView mImageViewCart;
    TextView mTextViewBageCart;

    //login
    public SharedPreferences mSharedPreferencesLoginFirst;
    SharedPreferences.Editor EditorLogin;
    String GuestUser;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainActivity = (MainActivity) getActivity();
        mCommonHelper = new CommonHelper();
        mPostParseGet = new PostParseGet(mainActivity);
        mArrayListSearch = new ArrayList<MedicineSearchList>();

        mArrayListCategory = new ArrayList<CategoryList>();
        getAllBannerMenuVOS = new ArrayList<GetAllBannerMenuVO>();
        mArrayListSpinner = new ArrayList<String>();
        for (int i = 1; i <= 30; i++) {
            mArrayListSpinner.add(String.valueOf(i));
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmnetview = inflater.inflate(R.layout.fragment_medicines, container, false);

        mImageViewBack = fragmnetview.findViewById(R.id.fragment_imageview_back);
        mTextViewTitle = fragmnetview.findViewById(R.id.fragment_title_textview);
        mImageViewSearch =fragmnetview.findViewById(R.id.fragment_Serach);
        mImageViewSearch.setVisibility(View.GONE);
        mImageViewCart =fragmnetview.findViewById(R.id.fragment_cart);
        mTextViewBageCart = fragmnetview.findViewById(R.id.actionbar_title_textview_badge_cart);
        mImageViewCart.setVisibility(View.VISIBLE);

        mTextViewPresciptionUpload = fragmnetview.findViewById(R.id.fragment_Medicine_order_prescription_textview_upload);

        mRecyclerViewCategory = fragmnetview.findViewById(R.id.fragment_medicine_category_recyclerview);
        mRecyclerViewIntelignt = fragmnetview.findViewById(R.id.fragment_Medicine_listview_data_intelligent);
        mRecyclerViewData = fragmnetview.findViewById(R.id.fragment_Medicine_listview_data);
        mTextViewNoData = fragmnetview.findViewById(R.id.fragment_Medicine_textview_no_data);
        mEditTextSearch = fragmnetview.findViewById(R.id.fragment_Medicine_edittext_search);
        pgbProductList = fragmnetview.findViewById(R.id.fragment_Medicine_pgbProductList);
        mImageViewClear = fragmnetview.findViewById(R.id.fragment_Medicines_imageview_cancel);
        mRelativeLayoutMain = fragmnetview.findViewById(R.id.fragment_Medicine_relative_all);
        mLinearLayoutCategoryTitle = fragmnetview.findViewById(R.id.fragment_Medicine_theme_relative_loader);

        viewpager_infinite = fragmnetview.findViewById(R.id.viewpager_infinite);
        indicator = fragmnetview.findViewById(R.id.indicator);

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

        String Count = mSharedPreferencesCount.getString(Tags.COUNT, "");
        Utility.debugger("vt Count...." + Count);
        if (Count.equalsIgnoreCase("") || Count.equalsIgnoreCase("0")) {
            mTextViewBageCart.setVisibility(View.GONE);
        } else {
            mTextViewBageCart.setVisibility(View.VISIBLE);
            mTextViewBageCart.setText(Count);
        }

        mImageViewCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentCart fragmentCart = new FragmentCart();
                mainActivity.addFragment(fragmentCart, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, fragmentCart.getClass().getName());
            }
        });

        mImageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mainActivity.onBackPressed();
                mainActivity.RemoveAllFragment();
                FragmentTabActivity fragmentTabActivity = new FragmentTabActivity();
                mainActivity.addFragment(fragmentTabActivity, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, fragmentTabActivity.getClass().getName());
            }

        });

        mImageViewClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditTextSearch.setText("");
                mStringSearch = "";

                mRecyclerViewIntelignt.setVisibility(View.GONE);
                mRecyclerViewData.setVisibility(View.GONE);
                mTextViewNoData.setVisibility(View.GONE);
                mRelativeLayoutMain.setVisibility(View.VISIBLE);
                mImageViewClear.setVisibility(View.GONE);
            }
        });
        mEditTextSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    FragmentSearchList fragmentSearchList = new FragmentSearchList();
                    Bundle mBundle = new Bundle();
                    mBundle.putString("SearchText", mEditTextSearch.getText().toString());
                    fragmentSearchList.setArguments(mBundle);
                    mainActivity.addFragment(fragmentSearchList, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, fragmentSearchList.getClass().getName());
                    return true;
                }
                return false;
            }
        });


        mEditTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

                if (charSequence.length() == 0) {
                    mImageViewClear.setVisibility(View.GONE);
                } else {
                    mImageViewClear.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {

                if (charSequence.length() >= 3) {
                    pgbProductList.setVisibility(View.VISIBLE);
                    GetSearchData();
                    mRecyclerViewIntelignt.setVisibility(View.VISIBLE);
                    mRelativeLayoutMain.setVisibility(View.GONE);
                } else {
                    mTextViewNoData.setVisibility(View.GONE);
                    pgbProductList.setVisibility(View.GONE);
                    mRecyclerViewIntelignt.setVisibility(View.GONE);
                    mRelativeLayoutMain.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        mTextViewPresciptionUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentUploadPrescription mFragmentUploadPrescription = new FragmentUploadPrescription();
               mainActivity.addFragment(mFragmentUploadPrescription, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, mFragmentUploadPrescription.getClass().getName());
            }
        });

        MedicinesCategoryListData();
        return fragmnetview;

    }


    public class SearchMedicineAdapter extends RecyclerView.Adapter<SearchMedicineAdapter.MyViewHolderMedicinesSearch> {
        Context mContext;
        ArrayList<MedicineSearchList> mArrayList;

        public SearchMedicineAdapter(MainActivity mainActivity, ArrayList<MedicineSearchList> mArrayListSearch) {
            this.mContext = mainActivity;
            this.mArrayList = mArrayListSearch;
        }

        @NonNull
        @Override
        public MyViewHolderMedicinesSearch onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_suggestion, parent, false);
            MyViewHolderMedicinesSearch myViewHolderSearch = new MyViewHolderMedicinesSearch(v);
            return myViewHolderSearch;
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull MyViewHolderMedicinesSearch holder, int position) {

            int pos = position;
            holder.mTextViewMdicineName.setText(mArrayList.get(pos).getMedicineName());

            if (mArrayList.get(pos).getUnitsPerPack() != null  && !mArrayList.get(pos).getUnitsPerPack().equalsIgnoreCase("null")) {

                holder.mTextViewPackTitle.setText("Pack :" );
                holder.mTextViewPack.setText( mArrayList.get(pos).getUnitsPerPack());
            }

            if (mArrayList.get(pos).getPrice() != null && !mArrayList.get(pos).getPrice().equalsIgnoreCase("null")) {
                holder.mTextViewPriceVlaue.setText( getResources().getString(R.string.price) +Tags.LABEL_CURRENCY_SIGN + Utility.getDecimalFormate(mainActivity,Double.parseDouble(mArrayList.get(pos).getPrice()),0.0,Tags.DECIMAL_FORMAT));
            }
            else {
                holder.mTextViewPriceVlaue.setText(getResources().getString(R.string.price) +" "+ Tags.LABEL_CURRENCY_SIGN +  Utility.getDecimalFormate(mainActivity,Double.parseDouble(mArrayList.get(pos).getMrp()),0.0,Tags.DECIMAL_FORMAT));
                holder.mTextViewDiscountPrice.setVisibility(View.GONE);
            }

            if (mArrayList.get(pos).getMrp().equals("null") || mArrayList.get(pos).getMrp().equals("0")) {
                holder.mTextViewDiscountPrice.setVisibility(View.GONE);
            } else {
                holder.mTextViewDiscountPrice.setVisibility(View.VISIBLE);
                holder.mTextViewDiscountPrice.setText(Tags.LABEL_CURRENCY_SIGN +  Utility.getDecimalFormate(mainActivity,Double.parseDouble(mArrayList.get(pos).getMrp()),0.0,Tags.DECIMAL_FORMAT));
                holder.mTextViewDiscountPrice.setPaintFlags(holder.mTextViewDiscountPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }

            if(mArrayList.get(pos).getPrice().equalsIgnoreCase(mArrayList.get(pos).getMrp()) || mArrayList.get(pos).getPrice().equalsIgnoreCase("null") )
            {
                holder.mTextViewDiscountPrice.setVisibility(View.GONE);
            }

            if(mArrayList.get(pos).getStockAvailablity().equalsIgnoreCase("true"))
            {
                holder.mTextViewBuy.setVisibility(View.VISIBLE);
                holder.mTextViewOutOfStok.setVisibility(View.GONE);
            }
            else {
                holder.mTextViewBuy.setVisibility(View.GONE);
                holder.mTextViewOutOfStok.setVisibility(View.VISIBLE);
            }

            if (mArrayList.get(pos).getImagePath() != null) {

                Glide.with(mContext).load(mArrayList.get(pos).getImagePath())
                        .placeholder(R.drawable.ic_med)
                        .into(holder.mImageViewMedicine);
            } else {
                Glide.with(mContext)
                        .load(R.drawable.ic_med)
                        .into(holder.mImageViewMedicine);
            }
            holder.mTextViewBuy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.mTextViewBuy.setVisibility(View.GONE);
                    holder.mSpinner.setVisibility(View.VISIBLE);
//                    mAttributeAdapter = new spinner_adapter(mainActivity, mArrayListSpinner);
//                    mAttributeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                    holder.mSpinner.setAdapter(mAttributeAdapter);

                    mAttributeAdapter = new ArrayAdapter<String>(mainActivity, android.R.layout.simple_spinner_item, mArrayListSpinner);
                    mAttributeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    holder.mSpinner.setAdapter(mAttributeAdapter);
                    holder.mSpinner.setAdapter(mAttributeAdapter);

                    mStringMedicineId = mArrayList.get(pos).getMedicineId();
                    mStringqty = String.valueOf(holder.mSpinner.getSelectedItem());
                    mStringPrice = mArrayList.get(pos).getPrice();

                    AddCartData();
                }
            });

            holder.mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                    if (parent.getItemAtPosition(position).equals("1")) {

                    } else {
                        holder.mSpinner.getSelectedItem();

                        mStringMedicineId = mArrayList.get(pos).getMedicineId();
                        mStringqty = String.valueOf(holder.mSpinner.getSelectedItem());
                        mStringPrice = mArrayList.get(pos).getPrice();

                        AddCartData();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentProductDeatils fragmentProductDeatils = new FragmentProductDeatils();
                    Bundle mBundle = new Bundle();
                    mBundle.putString("medicineid", mArrayList.get(pos).getMedicineId());
                    fragmentProductDeatils.setArguments(mBundle);
                    mainActivity.addFragment(fragmentProductDeatils, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, fragmentProductDeatils.getClass().getName());
                }
            });
        }
        @Override
        public int getItemCount() {
            return mArrayList.size();
        }

        public class MyViewHolderMedicinesSearch extends RecyclerView.ViewHolder {

            ImageView mImageViewMedicine;
            TextView mTextViewMdicineName;
            TextView mTextViewPriceVlaue;
            TextView mTextViewDiscountPrice;
            TextView mTextViewBuy;
            Spinner mSpinner;
            TextView mTextViewPack;
            TextView mTextViewOutOfStok;
            TextView mTextViewPackTitle;

            public MyViewHolderMedicinesSearch(View itemView) {
                super(itemView);

                mImageViewMedicine = itemView.findViewById(R.id.row_suggestion_imageview);
                mTextViewMdicineName = itemView.findViewById(R.id.row_suggestion_textview__medicine_name);
                mTextViewPriceVlaue = itemView.findViewById(R.id.row_suggestion_textview_price_value);
                mTextViewDiscountPrice = itemView.findViewById(R.id.row_suggestion_textview_price_discount);
                mTextViewBuy = itemView.findViewById(R.id.row_suggestion_textview_buy);
                mSpinner = itemView.findViewById(R.id.row_suggestion_spinner);
                mTextViewPack = itemView.findViewById(R.id.row_suggestion_textview_pack);
                mTextViewOutOfStok = itemView.findViewById(R.id.row_suggestion_textview_out_of_stock);
                mTextViewPackTitle = itemView.findViewById(R.id.row_suggestion_textview_pack_text);
            }
        }
    }


    public void GetSearchData() {

        getLoaderManager().restartLoader(0, null, new LoaderManager.LoaderCallbacks<TaskExecutor>() {
            @Override
            public Loader<TaskExecutor> onCreateLoader(int arg0, Bundle arg1) {

                SearchMedicine searchMedicine = new SearchMedicine(mainActivity, null);
                return new LoaderTaskWithoutProgressDialog<TaskExecutor>(mainActivity, searchMedicine);
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
//                                mCommonHelper.hideKeyboard(mainActivity);
                                mainActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                                if (mServerResponseVOSearch.getData() != null) {

                                    mArrayListSearch = (ArrayList<MedicineSearchList>) mServerResponseVOSearch.getData();
                                    if (mArrayListSearch != null && mArrayListSearch.size() > 0) {

                                        mRecyclerViewIntelignt.setVisibility(View.VISIBLE);
                                        mRelativeLayoutMain.setVisibility(View.GONE);
                                        pgbProductList.setVisibility(View.GONE);
                                        mTextViewNoData.setVisibility(View.GONE);

                                        SearchMedicineAdapter searchAdapter = new SearchMedicineAdapter(mainActivity, mArrayListSearch);
                                        mRecyclerViewIntelignt.setHasFixedSize(true);
                                        mRecyclerViewIntelignt.setLayoutManager(new LinearLayoutManager(mainActivity));
                                        mRecyclerViewIntelignt.setAdapter(searchAdapter);

                                    }
                                }
                            } else {

                                mTextViewNoData.setVisibility(View.VISIBLE);
                                mRecyclerViewIntelignt.setVisibility(View.GONE);
                                mCommonHelper.hideKeyboard(mainActivity);
                            }
                            pgbProductList.setVisibility(View.GONE);
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
            mStringSearch = mEditTextSearch.getText().toString().trim();
            SearchMedicineInfoService getAllAddressInfoService = new SearchMedicineInfoService();
            mServerResponseVOSearch = getAllAddressInfoService.fetchLoginUserInformation(mainActivity, Tags.SearchMedicine, mStringSearch,mStringUserid,Tags.mStringDeviceToken);

            return null;
        }
    }


    public void MedicinesCategoryListData() {

        getLoaderManager().restartLoader(0, null, new LoaderManager.LoaderCallbacks<TaskExecutor>() {
            @Override
            public Loader<TaskExecutor> onCreateLoader(int arg0, Bundle arg1) {

                MedicinesList medicinesList = new MedicinesList(mainActivity, null);
                return new LoaderTask<TaskExecutor>(mainActivity, medicinesList);
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
                            if (mServerResponseVOCategoryList.getStatus().equalsIgnoreCase("true")) {
//                            Snackbar.with(mainActivity).text(mServerResponseVO.getMsg()).show(mainActivity);
                                mCommonHelper.hideKeyboard(mainActivity);


                                if (mServerResponseVOCategoryList.getData() != null) {

                                    mArrayListCategory = (ArrayList<CategoryList>) mServerResponseVOCategoryList.getData();
                                    if (mArrayListCategory != null && mArrayListCategory.size() > 0) {

                                        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 3);
                                        mRecyclerViewCategory.setLayoutManager(gridLayoutManager);
                                        MedicinesCategoryAdapter customAdapter = new MedicinesCategoryAdapter(mainActivity, mArrayListCategory);
                                        mRecyclerViewCategory.setAdapter(customAdapter);

                                    }
                                }

                                GetHomeCount();

                            } else {
                                mLinearLayoutCategoryTitle.setVisibility(View.GONE);
                                mRecyclerViewCategory.setVisibility(View.GONE);
                                Snackbar.with(mainActivity).text(mServerResponseVOCategoryList.getMsg()).show(mainActivity);
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
    public class MedicinesList extends TaskExecutor {
        protected MedicinesList(Context context, Bundle bundle) {
            super(context, bundle);
        }

        @Override
        protected Object executeTask() {
            MedicinListInfoService getAllAddressInfoService = new MedicinListInfoService();
            mServerResponseVOCategoryList = getAllAddressInfoService.fetchLoginUserInformation(mainActivity, Tags.MedicineList);

            return null;
        }
    }



    public class MedicinesCategoryAdapter extends RecyclerView.Adapter<MedicinesCategoryAdapter.MyViewHolder> {

        ArrayList<CategoryList> mArrayList;
        Context mContext;


        public MedicinesCategoryAdapter(MainActivity mainActivity, ArrayList<CategoryList> mArrayListCategory) {
            this.mContext = mainActivity;
            this.mArrayList = mArrayListCategory;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_category, parent, false);
            MyViewHolder vh = new MyViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

            holder.mTextViewCategoryName.setText(mArrayList.get(position).getMedicineCategoryName());
            if (mArrayList.get(position).getImagePath() != null) {

                Glide.with(mContext).load(mArrayList.get(position).getImagePath())
                        .placeholder(R.drawable.ic_med)
                        .into(holder.mImageViewCategoryIamge);
            } else {
                Glide.with(mContext)
                        .load(R.drawable.ic_med)
                        .into(holder.mImageViewCategoryIamge);
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentMedicineCategoryList fragmentMedicineCategoryList = new FragmentMedicineCategoryList();
                    Bundle mBundle = new Bundle();
                    mBundle.putString("Category_id", mArrayList.get(position).getMedicineCategoryId());
                    mBundle.putString("Category_Name", mArrayList.get(position).getMedicineCategoryName());
                    fragmentMedicineCategoryList.setArguments(mBundle);
                    mainActivity.addFragment(fragmentMedicineCategoryList, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, fragmentMedicineCategoryList.getClass().getName());
                }
            });

        }


        @Override
        public int getItemCount() {
            return mArrayList.size();
        }


        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView mTextViewCategoryName;
            ImageView mImageViewCategoryIamge;

            public MyViewHolder(View itemView) {
                super(itemView);
                mImageViewCategoryIamge = (ImageView) itemView.findViewById(R.id.fragment_row_category_imageview_categoryimage);
                mTextViewCategoryName = (TextView) itemView.findViewById(R.id.fragment_row_category_textview_categoryname);
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
                                        mTextViewBageCart.setVisibility(View.VISIBLE);
                                        mTextViewBageCart.setText(mServerResponseVOCard.getCartCount());
                                        Snackbar.with(mainActivity).text(mServerResponseVOCard.getMsg()).show(mainActivity);

                                        mEditorCount.putString(Tags.COUNT, mServerResponseVOCard.getCartCount());
                                        mEditorCount.apply();
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
    public void onResume() {
        super.onResume();
        mainActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        if (viewpager_infinite != null) {
            viewpager_infinite.startAutoScroll();
            viewpager_infinite.setAutoScrollTime(5000);
        }

        onResumeData();

    }

    public void onResumeData() {

//        GetHomeCount();
    }
    @Override
    public void doWork() {
        super.doWork();
        onResumeData();
    }


    @Override
    public void onPause() {
        super.onPause();
        if (viewpager_infinite != null) {
            viewpager_infinite.stopAutoScroll();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        mainActivity.getSharedPreferences(Tags.COUNT, 0).edit().clear().apply();
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
        mainActivity.getSharedPreferences(Tags.COUNT, 0).edit().clear().apply();
    }


    public void GetAllBannerMenu() {

        getLoaderManager().restartLoader(0, null, new LoaderManager.LoaderCallbacks<TaskExecutor>() {
            @Override
            public Loader<TaskExecutor> onCreateLoader(int arg0, Bundle arg1) {

                GetAllBannerMenuList getAllBannerMenuList = new GetAllBannerMenuList(mainActivity, null);
                return new LoaderTask<TaskExecutor>(mainActivity, getAllBannerMenuList);
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

                                    getAllBannerMenuVOS = (ArrayList<GetAllBannerMenuVO>) mServerResponseVO.getData();
                                    if (getAllBannerMenuVOS != null && getAllBannerMenuVOS.size() > 0) {

                                        for(int i = 0; i < getAllBannerMenuVOS.size(); i++) {

                                            if(getAllBannerMenuVOS.get(i).getMenuName().equalsIgnoreCase("Medicine Category"))
                                            {
                                                mStringMenuId = getAllBannerMenuVOS.get(i).getMenuId();
                                                BannerMenu();
                                            }
                                        }
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
    public class GetAllBannerMenuList extends TaskExecutor {
        protected GetAllBannerMenuList(Context context, Bundle bundle) {
            super(context, bundle);
        }

        @Override
        protected Object executeTask() {
            GetAllBannerMenuInfoService getAllAddressInfoService = new GetAllBannerMenuInfoService();
            mServerResponseVO = getAllAddressInfoService.fetchLoginUserInformation(mainActivity, Tags.GetBannerMenu);

            return null;
        }
    }

    public void BannerMenu() {

        getLoaderManager().restartLoader(0, null, new LoaderManager.LoaderCallbacks<TaskExecutor>() {
            @Override
            public Loader<TaskExecutor> onCreateLoader(int arg0, Bundle arg1) {

                GetAllBannerMenu allBannerMenu = new GetAllBannerMenu(mainActivity, null);
                return new LoaderTask<TaskExecutor>(mainActivity, allBannerMenu);
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

                                    getAllBannerMenuVOS = (ArrayList<GetAllBannerMenuVO>) mServerResponseVO.getData();
                                    if (getAllBannerMenuVOS != null && getAllBannerMenuVOS.size() > 0) {

                                        for(int i=0 ; i <getAllBannerMenuVOS.size(); i++) {
//                                            if (getAllBannerMenuVOS.get(i).getBanner() != null) {
//                                                Glide.with(mainActivity).load(getAllBannerMenuVOS.get(i).getBanner())
//                                                        .placeholder(R.drawable.logo)
//                                                        .into(mImageViewOfferBanner);
//                                            }
                                            if (getAllBannerMenuVOS.get(i).getBanner() != null)
                                            {
                                                if (getAllBannerMenuVOS.size() == 1) {

                                                        CustomAdapterInfinite   mCustomAdapterInfinite = new CustomAdapterInfinite(mainActivity, getAllBannerMenuVOS);
                                                        viewpager_infinite.setAdapter(mCustomAdapterInfinite);
                                                        viewpager_infinite.stopAutoScroll();

                                                }
                                                else
                                                {
                                                    CustomAdapterInfinite   mCustomAdapterInfinite = new CustomAdapterInfinite(mainActivity, getAllBannerMenuVOS);
                                                    viewpager_infinite.setAdapter(mCustomAdapterInfinite);
                                                    indicator.setViewPager(viewpager_infinite);

                                                    if (viewpager_infinite != null) {
                                                        viewpager_infinite.startAutoScroll();
                                                        viewpager_infinite.setAutoScrollTime(5000);
                                                    }
                                                }
                                            }
                                        }
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
    public class GetAllBannerMenu extends TaskExecutor {
        protected GetAllBannerMenu(Context context, Bundle bundle) {
            super(context, bundle);
        }

        @Override
        protected Object executeTask() {
            BannerMenuInfoService infoService = new BannerMenuInfoService();
            mServerResponseVO = infoService.bannermenu(mainActivity, Tags.GetAllBanner, "Medicine");

            return null;
        }
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

                                        mEditorCount.putString(Tags.COUNT, mServerResponseVOCount.getCartCount());
                                        mEditorCount.commit();
                                    }


                                    GetAllBannerMenu();

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