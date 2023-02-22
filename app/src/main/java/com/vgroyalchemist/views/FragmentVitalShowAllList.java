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
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.vgroyalchemist.vos.VitalListVO;
import com.vgroyalchemist.webservice.DeleteNotiService;
import com.vgroyalchemist.webservice.DeleteVitalService;
import com.vgroyalchemist.webservice.PostParseGet;
import com.vgroyalchemist.webservice.ShowAllVitalListService;
import com.vgroyalchemist.webservice.VitalListService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/* developed by krishna 24-03-2021
*  show all vital added by user and delete vital */
public class FragmentVitalShowAllList extends NonCartFragment {

    //Variable Declaration
    public static final String FRAGMENT_ID = "47";

    public static MainActivity mainActivity;
    public View fragmentView = null;
    CommonHelper mCommonHelper;

    ImageView mImageViewBack;
    TextView mTextViewTitle;
    ImageView mImageViewSearch;
    ImageView mImageViewCart;

    TextView mTextViewVitalName;

    LinearLayout mLinearLayoutEmpty;

    RecyclerView mRecyclerViewVitalList;

    ArrayList<ShowAllVitalListVO> mArrayListVitalAddData;

    PostParseGet mPostParseGet;
    ServerResponseVO mServerResponseVO;

    // userdata
    public SharedPreferences mSharedPreferencesUserId;
    public SharedPreferences.Editor mEditor;
    String mStringUserid;

    Bundle mBundle;
    String mStringVitalId;
    String mStringVitalUnit;
    String mStringVital;
    VitalListAddDataAdpter vitalListAddDataAdpter;
    ServerResponseVO mServerResponseVODelete;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = (MainActivity) getActivity();
        mCommonHelper = new CommonHelper();

        mPostParseGet = new PostParseGet(mainActivity);

        mArrayListVitalAddData = new ArrayList<ShowAllVitalListVO>();


        mBundle = getArguments();

        if(mBundle != null)
        {
            mStringVitalId = mBundle.getString("VitalID");
            mStringVitalUnit = mBundle.getString("VitalUnit");
            mStringVital = mBundle.getString("mStringVital");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentView = inflater.inflate(R.layout.fragment_vital_list, container, false);

        mImageViewBack = fragmentView.findViewById(R.id.fragment_imageview_back);
        mTextViewTitle = fragmentView.findViewById(R.id.fragment_title_textview);
        mTextViewTitle.setText("Vitals");
        mImageViewSearch = fragmentView.findViewById(R.id.fragment_Serach);
        mImageViewSearch.setVisibility(View.GONE);
        mImageViewCart = fragmentView.findViewById(R.id.fragment_cart);
        mImageViewCart.setImageResource(R.drawable.top_btn_add);

        mLinearLayoutEmpty = fragmentView.findViewById(R.id.fragment_PastSchedule_relative_empty);
        mRecyclerViewVitalList = fragmentView.findViewById(R.id.fragment_vital_list_Recyclerview);

        mTextViewVitalName = fragmentView.findViewById(R.id.fragment_vital_list_vital_name);

        if(mStringVital != null)
        {
            mTextViewVitalName.setText(mStringVital);
        }
        mSharedPreferencesUserId = mainActivity.getSharedPreferences(Tags.TAG_SHARED_PREFERENCE_USER_ID, 0);
        mEditor = mSharedPreferencesUserId.edit();
        mStringUserid = mSharedPreferencesUserId.getString("UserId", "");


        mImageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.onBackPressed();
            }

        });
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



        return fragmentView;
    }


    //   vital list as date display
    public class VitalListAddDataAdpter extends RecyclerView.Adapter<VitalListAddDataAdpter.MyViewHolderVitalAddList> {

        Context mContext;
        ArrayList<ShowAllVitalListVO> mArrayListVitalAddList;

        public VitalListAddDataAdpter(MainActivity mainActivity, ArrayList<ShowAllVitalListVO> mArrayListVitalAddData) {
            this.mContext = mainActivity;
            this.mArrayListVitalAddList = mArrayListVitalAddData;
        }

        @NonNull
        @Override
        public MyViewHolderVitalAddList onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_addvitals_list, parent, false);
            MyViewHolderVitalAddList myViewHolderPastSchedule = new MyViewHolderVitalAddList(v);
            return myViewHolderPastSchedule;
        }

        @SuppressLint("ResourceType")
        @Override
        public void onBindViewHolder(@NonNull MyViewHolderVitalAddList holder, int position) {



            if(!mArrayListVitalAddList.get(position).getUnit().equalsIgnoreCase("null"))
            {
                holder.mTextViewUnit.setText(mArrayListVitalAddList.get(position).getValue());
            }
            if(!mArrayListVitalAddList.get(position).getValue().equalsIgnoreCase("null"))
            {
                holder.mTextViewUnitValue.setText(mArrayListVitalAddList.get(position).getUnit());
            }

            if(!mArrayListVitalAddList.get(position).getVitalTime().equalsIgnoreCase("null") && mArrayListVitalAddList.get(position).getVitalTime() != null)
            {
                String mDate = mArrayListVitalAddList.get(position).getVitalTime();
                SimpleDateFormat fmt = new SimpleDateFormat("HH:mm:ss");
                SimpleDateFormat fmt2 = new SimpleDateFormat("hh:mm a");
                try {
                    Date date = fmt.parse(mDate);
                    String mString = fmt2.format(date);
                    holder.mTextViewTime.setText(mString);
                } catch (ParseException pe) {
                    pe.printStackTrace();
                }

            }
            if(!mArrayListVitalAddList.get(position).getVitalDate().equalsIgnoreCase("null") && mArrayListVitalAddList.get(position).getVitalDate() != null)
            {
                String mDate = mArrayListVitalAddList.get(position).getVitalDate();
                SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat fmt2 = new SimpleDateFormat("EEE, MMM d, yyyy");
                try {
                    Date date = fmt.parse(mDate);
                    String mString = fmt2.format(date);
                    holder.mTextViewDate.setText(mString);
                } catch (ParseException pe) {
                    pe.printStackTrace();
                }
            }

            holder.mImageViewDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (!mCommonHelper.check_Internet(getActivity())) {
                        Snackbar.with(mainActivity).text(getResources().getString(R.string.interneterror)).show(mainActivity);
                        return;
                    }


                    final Bundle mBundle = new Bundle();
                    mBundle.putString(getString(R.string.app_name), mArrayListVitalAddList.get(position).getId());


                    getLoaderManager().restartLoader(0, mBundle, new LoaderManager.LoaderCallbacks<TaskExecutor>() {
                        @Override
                        public Loader<TaskExecutor> onCreateLoader(int arg0, Bundle arg1) {

                            DeleteVitalExecutor deleteVitalExecutor = new DeleteVitalExecutor(mainActivity, mBundle);
                            return new LoaderTask<TaskExecutor>(mainActivity, deleteVitalExecutor);
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

                                            mArrayListVitalAddList.remove(position);
                                            vitalListAddDataAdpter.notifyDataSetChanged();


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
            });


        }

        @Override
        public int getItemCount() {
            return mArrayListVitalAddList.size();
        }

        public class MyViewHolderVitalAddList extends RecyclerView.ViewHolder {


            TextView mTextViewUnit;
            TextView mTextViewUnitValue;
            TextView mTextViewDate;
            TextView mTextViewTime;
            ImageView mImageViewDelete;


            public MyViewHolderVitalAddList(View itemView) {
                super(itemView);

                mTextViewTime = itemView.findViewById(R.id.row_vital_list_textview_vitaltime);
                mTextViewUnit = itemView.findViewById(R.id.row_vital_list_textview_vitalvalue);
                mTextViewUnitValue = itemView.findViewById(R.id.row_vital_list_textview_vitalunit);
                mTextViewDate = itemView.findViewById(R.id.row_vital_list_textview_vitaldatetime);
                mImageViewDelete = itemView.findViewById(R.id.row_vital_list_imageview_delete);

            }
        }
    }


    public class DeleteVitalExecutor extends TaskExecutor {

        String mstringvidalId;

        protected DeleteVitalExecutor(Context context, Bundle bundle) {
            super(context, bundle);
            this.mstringvidalId = bundle.getString(getString(R.string.app_name));
        }

        @Override
        protected Object executeTask() {

            try {

                DeleteVitalService fetchVersionInfoService = new DeleteVitalService();
                mServerResponseVODelete = fetchVersionInfoService.deleteNoti(mainActivity, Tags.DeleteVital,mstringvidalId);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    //    Show All Vital List Data
    public void ShowAllvitallist() {

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

                                    mArrayListVitalAddData = (ArrayList<ShowAllVitalListVO>) mServerResponseVO.getData();
                                    if (mArrayListVitalAddData != null && mArrayListVitalAddData.size() > 0) {

                                        mLinearLayoutEmpty.setVisibility(View.GONE);
                                        mRecyclerViewVitalList.setVisibility(View.VISIBLE);
                                        vitalListAddDataAdpter = new VitalListAddDataAdpter(mainActivity, mArrayListVitalAddData);
                                        mRecyclerViewVitalList.setHasFixedSize(true);
                                        mRecyclerViewVitalList.setLayoutManager(new LinearLayoutManager(mainActivity));
                                        mRecyclerViewVitalList.setAdapter(vitalListAddDataAdpter);

                                    }
                                    else {
                                        mLinearLayoutEmpty.setVisibility(View.VISIBLE);
                                        mRecyclerViewVitalList.setVisibility(View.GONE);
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
            ShowAllVitalListService vitalListService = new ShowAllVitalListService();
            mServerResponseVO = vitalListService.vitalList(mainActivity, Tags.GetUserVitals , mStringUserid,mStringVitalId);

            return null;
        }
    }

    public void onResumeData() {
        mainActivity.toolbar.setVisibility(View.GONE);

        ShowAllvitallist();
    }

    @Override
    public void doWork() {
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

    @Override
    public void onPause() {
        super.onPause();
    }

}