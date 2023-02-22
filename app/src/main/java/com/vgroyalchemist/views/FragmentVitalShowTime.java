package com.vgroyalchemist.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
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
import android.widget.TextView;

import com.vgroyalchemist.R;
import com.vgroyalchemist.controller.MainActivity;
import com.vgroyalchemist.loaders.LoaderTask;
import com.vgroyalchemist.loaders.TaskExecutor;
import com.vgroyalchemist.utils.CommonHelper;
import com.vgroyalchemist.utils.Snackbar;
import com.vgroyalchemist.utils.Tags;
import com.vgroyalchemist.vos.ServerResponseVO;
import com.vgroyalchemist.vos.ShowAllVitalListVO;
import com.vgroyalchemist.webservice.PostParseGet;
import com.vgroyalchemist.webservice.ShowAllVitalListService;
import com.vgroyalchemist.webservice.ShowAllVitalListTimeService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class FragmentVitalShowTime extends NonCartFragment {


    //Variable Declaration
    public static final String FRAGMENT_ID = "44";

    public static MainActivity mainActivity;
    public View fragmentView = null;
    CommonHelper mCommonHelper;

    ImageView mImageViewBack;
    TextView mTextViewTitle;
    ImageView mImageViewSearch;
    ImageView mImageViewCart;

    RecyclerView mRecyclerViewVitalListTime;
    ArrayList<ShowAllVitalListVO> mArrayListVitalDataTime;

    Bundle mBundle;
    String mStringVitalId;
    String mStringVitalDate;

    PostParseGet mPostParseGet;
    ServerResponseVO mServerResponseVO;

    // userdata
    public SharedPreferences mSharedPreferencesUserId;
    public SharedPreferences.Editor mEditor;
    String mStringUserid;
    String mStringVitalUnit;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = (MainActivity) getActivity();
        mCommonHelper = new CommonHelper();

        mArrayListVitalDataTime = new ArrayList();

        mPostParseGet = new PostParseGet(mainActivity);


        mBundle = getArguments();

        if(mBundle != null)
        {
            mStringVitalId = mBundle.getString("ShowVitalID");
            mStringVitalDate = mBundle.getString("VitalDate");
            mStringVitalUnit = mBundle.getString("VitalUnit");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentView = inflater.inflate(R.layout.fragment_vital_show_time, container, false);

        mImageViewBack = fragmentView.findViewById(R.id.fragment_imageview_back);
        mTextViewTitle = fragmentView.findViewById(R.id.fragment_title_textview);
        mTextViewTitle.setText("Vitals");
        mImageViewSearch = fragmentView.findViewById(R.id.fragment_Serach);
        mImageViewSearch.setVisibility(View.GONE);
        mImageViewCart = fragmentView.findViewById(R.id.fragment_cart);
        mImageViewCart.setImageResource(R.drawable.top_btn_add);

        mImageViewCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentAddVital fragmentAddVital = new FragmentAddVital();
                Bundle mBundle = new Bundle();
                mBundle.putString("VitalID",mStringVitalId);
                mBundle.putString("VitalUnit",mStringVitalUnit);
                fragmentAddVital.setArguments(mBundle);
                mainActivity.addFragment(fragmentAddVital, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, fragmentAddVital.getClass().getName());
            }
        });

        mRecyclerViewVitalListTime = fragmentView.findViewById(R.id.fragment_vital_list_Time_Recyclerview);

        mSharedPreferencesUserId = mainActivity.getSharedPreferences(Tags.TAG_SHARED_PREFERENCE_USER_ID, 0);
        mEditor = mSharedPreferencesUserId.edit();
        mStringUserid = mSharedPreferencesUserId.getString("UserId", "");


        mImageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.onBackPressed();
            }
        });




        ShowAllvitalTimelist();
        return  fragmentView;
    }

    //   display vital list as time apadter
    public class VitalListAddDataTimeAdpter extends RecyclerView.Adapter<VitalListAddDataTimeAdpter.MyViewHolderVitalAddListTime> {

        Context mContext;
        ArrayList<ShowAllVitalListVO> mArrayListVitalAddListTime;

        public VitalListAddDataTimeAdpter(MainActivity mainActivity, ArrayList<ShowAllVitalListVO> mArrayListVitalDataTime) {
            this.mContext = mainActivity;
            this.mArrayListVitalAddListTime = mArrayListVitalDataTime;
        }
        @NonNull
        @Override
        public MyViewHolderVitalAddListTime onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_addvitals_list, parent, false);
            MyViewHolderVitalAddListTime myViewHolderVitalAddListTime = new MyViewHolderVitalAddListTime(v);
            return myViewHolderVitalAddListTime;
        }

        @SuppressLint("ResourceType")
        @Override
        public void onBindViewHolder(@NonNull MyViewHolderVitalAddListTime holder, int position) {

            holder.mImageViewNext.setVisibility(View.GONE);

            if(!mArrayListVitalAddListTime.get(position).getVital().equalsIgnoreCase("null"))
            {
                holder.mTextViewVitalName.setText(mArrayListVitalAddListTime.get(position).getVital());
            }

            if(!mArrayListVitalAddListTime.get(position).getUnit().equalsIgnoreCase("null"))
            {
                holder.mTextViewUnit.setText(mArrayListVitalAddListTime.get(position).getValue());
            }
            if(!mArrayListVitalAddListTime.get(position).getValue().equalsIgnoreCase("null"))
            {
                holder.mTextViewUnitValue.setText(mArrayListVitalAddListTime.get(position).getUnit());
            }

            if(!mArrayListVitalAddListTime.get(position).getTime().equalsIgnoreCase("null") && mArrayListVitalAddListTime.get(position).getTime() != null)
            {
                String mDate = mArrayListVitalAddListTime.get(position).getVitalTime();
                SimpleDateFormat fmt = new SimpleDateFormat("HH:mm:ss");
                SimpleDateFormat fmt2 = new SimpleDateFormat("hh:mm a");
                try {
                    Date date = fmt.parse(mDate);
                    String mString = fmt2.format(date);
                    holder.mTextViewDate.setText(mString);
                } catch (ParseException pe) {
                    pe.printStackTrace();
                }
            }
        }

        @Override
        public int getItemCount() {
            return mArrayListVitalAddListTime.size();
        }

        public class MyViewHolderVitalAddListTime extends RecyclerView.ViewHolder {

            TextView mTextViewVitalName;
            TextView mTextViewUnit;
            TextView mTextViewUnitValue;
            TextView mTextViewDate;
            ImageView mImageViewNext;


            public MyViewHolderVitalAddListTime(View itemView) {
                super(itemView);

                mTextViewVitalName = itemView.findViewById(R.id.row_vital_list_textview_vitalname);
                mTextViewUnit = itemView.findViewById(R.id.row_vital_list_textview_vitalvalue);
                mTextViewUnitValue = itemView.findViewById(R.id.row_vital_list_textview_vitalunit);
                mTextViewDate = itemView.findViewById(R.id.row_vital_list_textview_vitaldatetime);



            }
        }
    }

    //    Show All Vital List Data
    public void ShowAllvitalTimelist() {

        getLoaderManager().restartLoader(0, null, new LoaderManager.LoaderCallbacks<TaskExecutor>() {
            @Override
            public Loader<TaskExecutor> onCreateLoader(int arg0, Bundle arg1) {

                ShowAllVitalListExe getVitalListExe = new ShowAllVitalListExe(mainActivity, null);
                return new LoaderTask<TaskExecutor>(mainActivity, getVitalListExe);
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

                                    mArrayListVitalDataTime = (ArrayList<ShowAllVitalListVO>) mServerResponseVO.getData();
                                    if (mArrayListVitalDataTime != null && mArrayListVitalDataTime.size() > 0) {

                                        VitalListAddDataTimeAdpter vitalListAddDataTimeAdpter = new VitalListAddDataTimeAdpter(mainActivity, mArrayListVitalDataTime);
                                        mRecyclerViewVitalListTime.setHasFixedSize(true);
                                        mRecyclerViewVitalListTime.setLayoutManager(new LinearLayoutManager(mainActivity));
                                        mRecyclerViewVitalListTime.setAdapter(vitalListAddDataTimeAdpter);

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
    public class ShowAllVitalListExe extends TaskExecutor {
        protected ShowAllVitalListExe(Context context, Bundle bundle) {
            super(context, bundle);
        }

        @Override
        protected Object executeTask() {
             ShowAllVitalListTimeService vitalListService = new ShowAllVitalListTimeService();
            mServerResponseVO = vitalListService.vitalListTime(mainActivity, Tags.GetUserVitalsDateWise , mStringUserid,mStringVitalId,mStringVitalDate);

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

    @Override
    public void onPause() {
        super.onPause();
    }
}