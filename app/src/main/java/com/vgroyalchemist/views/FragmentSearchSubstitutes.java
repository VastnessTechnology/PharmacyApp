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

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

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
import com.vgroyalchemist.vos.MedicineSearchList;
import com.vgroyalchemist.vos.ServerResponseVO;
import com.vgroyalchemist.webservice.PostParseGet;
import com.vgroyalchemist.webservice.SearchMedicineInfoService;

import java.util.ArrayList;

/* developed by krishna 24-03-2021
*  list of alternate  medicine */
public class FragmentSearchSubstitutes extends NonCartFragment {


    public static final String FRAGMENT_ID = "13";


    public static MainActivity mainActivity;
    CommonHelper mCommonHelper;
    View fragmnetview = null;

    AutoCompleteTextView mEditTextSearch;


    TextView mTextViewNoData;
    CircularProgressBar pgbProductList;
    RecyclerView mListViewIntelligent;
    RecyclerView mListViewSuggestion;
    ImageView mImageViewClear;

    private String mStringSearch = "";

    ArrayList<MedicineSearchList> mArrayListSearch;

    ServerResponseVO mServerResponseVOSearch;
    PostParseGet mPostParseGet;

    ImageView mImageViewBack;
    TextView mTextViewTitle;
    ImageView mImageViewSearch;
    ImageView mImageViewCart;
    TextView mTextViewBageCart;
    // userdata
    public SharedPreferences mSharedPreferencesUserId;
    public SharedPreferences.Editor mEditor;
    String mStringUserid;
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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        fragmnetview= inflater.inflate(R.layout.fragment_search_substitutes, container, false);

        mEditTextSearch =fragmnetview.findViewById(R.id.fragment_serach_subsitutes_edittext_search);
        mTextViewNoData =fragmnetview.findViewById(R.id.fragment_search_screen_textview_no_data);
        pgbProductList =fragmnetview.findViewById(R.id.fragment_search_screen_pgbProductList);
        mListViewIntelligent =fragmnetview.findViewById(R.id.fragment_search_screen_listview_data_intelligent);
        mImageViewClear = fragmnetview.findViewById(R.id.fragment_search_screen_imageview_cancel);
        mListViewSuggestion =fragmnetview.findViewById(R.id.fragment_search_screen_listview_data);

        mImageViewBack = fragmnetview.findViewById(R.id.fragment_imageview_back);
        mTextViewTitle = fragmnetview.findViewById(R.id.fragment_title_textview);
        mImageViewSearch =fragmnetview.findViewById(R.id.fragment_Serach);
        mImageViewSearch.setVisibility(View.GONE);
        mImageViewCart =fragmnetview.findViewById(R.id.fragment_cart);
        mTextViewBageCart = fragmnetview.findViewById(R.id.actionbar_title_textview_badge_cart);
        mImageViewCart.setVisibility(View.GONE);

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

                mListViewSuggestion.setVisibility(View.VISIBLE);
                mListViewIntelligent.setVisibility(View.GONE);
                mTextViewNoData.setVisibility(View.GONE);

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
                if (charSequence.length() == 0) {
                    mListViewSuggestion.setVisibility(View.VISIBLE);
                    mImageViewClear.setVisibility(View.GONE);
                    mTextViewNoData.setVisibility(View.GONE);
                } else {
                    mListViewSuggestion.setVisibility(View.GONE);
                    mImageViewClear.setVisibility(View.VISIBLE);
                }

                if (charSequence.length() >= 3) {
                    pgbProductList.setVisibility(View.VISIBLE);
                    loadIntelligentData();

                } else {
                    mTextViewNoData.setVisibility(View.GONE);
                    pgbProductList.setVisibility(View.GONE);
                    mListViewIntelligent.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        return  fragmnetview;
    }


    public void loadIntelligentData() {

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

                        if (mServerResponseVOSearch.getStatus().equalsIgnoreCase("true")) {
//                            Snackbar.with(mainActivity).text(mServerResponseVO.getMsg()).show(mainActivity);
                            mCommonHelper.hideKeyboard(mainActivity);
                            if (mServerResponseVOSearch.getData() != null) {

                                mListViewIntelligent.setVisibility(View.VISIBLE);
                                pgbProductList.setVisibility(View.GONE);
                                mTextViewNoData.setVisibility(View.GONE);

                                mArrayListSearch.clear();
                                mArrayListSearch = (ArrayList<MedicineSearchList>) mServerResponseVOSearch.getData();
                                if (mArrayListSearch != null && mArrayListSearch.size() > 0) {

                                    IntelligentSearchBaseAdapter searchAdapter = new IntelligentSearchBaseAdapter(mainActivity, mArrayListSearch);
                                    mListViewIntelligent.setHasFixedSize(true);
                                    mListViewIntelligent.setLayoutManager(new LinearLayoutManager(mainActivity));
                                    mListViewIntelligent.setAdapter(searchAdapter);
                                    mListViewSuggestion.setVisibility(View.GONE);

                                }
                            }
                        } else {
                            mTextViewNoData.setVisibility(View.VISIBLE);
                            mListViewIntelligent.setVisibility(View.GONE);
                            mListViewSuggestion.setVisibility(View.GONE);
                            mCommonHelper.hideKeyboard(mainActivity);
                        }

                        pgbProductList.setVisibility(View.GONE);
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
            mServerResponseVOSearch = getAllAddressInfoService.fetchLoginUserInformation(mainActivity, Tags.SearchMedicine, mStringSearch, mStringUserid,Tags.mStringDeviceToken);

            return null;
        }
    }


    public static class IntelligentSearchBaseAdapter extends RecyclerView.Adapter<IntelligentSearchBaseAdapter.MyViewHolderSearch> {

        Context mContext;
        ArrayList<MedicineSearchList> mArrayList;

        public IntelligentSearchBaseAdapter(MainActivity mainActivity, ArrayList<MedicineSearchList> mArrayListSearch) {
            this.mContext =mainActivity;
            this.mArrayList =mArrayListSearch;
        }

        @NonNull
        @Override
        public MyViewHolderSearch onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_search_suggestion, parent, false);
            MyViewHolderSearch myViewHolderSearch = new MyViewHolderSearch(v);
            return myViewHolderSearch;
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolderSearch holder, int position) {

            holder.mImageViewClose.setVisibility(View.GONE);

            if(mArrayList.get(position).getMedicineName() != null)
            {
                holder.mTextViewMdicineName.setText(mArrayList.get(position).getMedicineName());
            }

            holder.mRelativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentFindSubstitutes  fragmentFindSubstitutes = new FragmentFindSubstitutes();
                    Bundle mBundle = new Bundle();
                    mBundle.putString("MedicineID",mArrayList.get(position).getMedicineId());
                    mBundle.putString("medicineName",mArrayList.get(position).getMedicineName());
                    mBundle.putString("Manufacture",mArrayList.get(position).getManufacturer());
                    mBundle.putString("Price",mArrayList.get(position).getPrice());
                    mBundle.putString("imagePath",mArrayList.get(position).getImagePath());
                    fragmentFindSubstitutes.setArguments(mBundle);
                    mainActivity.addFragment(fragmentFindSubstitutes, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, fragmentFindSubstitutes.getClass().getName());
                }
            });

        }

        @Override
        public int getItemCount() {
            return mArrayList.size();
        }

        public static class MyViewHolderSearch extends RecyclerView.ViewHolder {

           ImageView mImageViewClose;
           TextView mTextViewMdicineName;
           RelativeLayout mRelativeLayout;


            public MyViewHolderSearch(View itemView) {
                super(itemView);
                mImageViewClose=itemView.findViewById(R.id.row_suggestion_imageview_main);
                mTextViewMdicineName=itemView.findViewById(R.id.row_Search_suggestion_textview_name);
                mRelativeLayout = itemView.findViewById(R.id.row_suggestion_rel);
            }
        }

    }


    @Override
    public void onResume() {
        super.onResume();
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
        mCommonHelper.hideKeyboard(mainActivity);
        mStringSearch = "";
        mEditTextSearch.setText("");
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        mCommonHelper.freeMemory();
        mCommonHelper.hideKeyboard(mainActivity);
        mStringSearch = "";
        mEditTextSearch.setText("");
        getLoaderManager().destroyLoader(0);
        super.onDestroy();
    }

}