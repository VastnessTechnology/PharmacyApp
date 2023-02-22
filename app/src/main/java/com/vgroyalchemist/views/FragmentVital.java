package com.vgroyalchemist.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;
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
import com.vgroyalchemist.vos.CategoryList;
import com.vgroyalchemist.vos.ServerResponseVO;
import com.vgroyalchemist.vos.VitalListVO;
import com.vgroyalchemist.webservice.MedicinListInfoService;
import com.vgroyalchemist.webservice.PostParseGet;
import com.vgroyalchemist.webservice.VitalListService;

import java.util.ArrayList;

import static com.facebook.FacebookSdk.getApplicationContext;

// developed by krishna 12-03-2021
// display vital list

public class FragmentVital extends NonCartFragment {

    //Variable Declaration
    public static final String FRAGMENT_ID = "43";

    public static MainActivity mainActivity;
    public View fragmentView = null;
    CommonHelper mCommonHelper;

    ImageView mImageViewBack;
    TextView mTextViewTitle;
    ImageView mImageViewSearch;
    ImageView mImageViewCart;

    RecyclerView mRecyclerViewVital;
    ArrayList<VitalListVO> mArrayListVitalList;

    PostParseGet mPostParseGet;
    ServerResponseVO mServerResponseVO;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainActivity = (MainActivity) getActivity();
        mCommonHelper = new CommonHelper();
        mPostParseGet = new PostParseGet(mainActivity);


        mArrayListVitalList = new ArrayList<VitalListVO>();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentView = inflater.inflate(R.layout.fragment_vital, container, false);


        mImageViewBack = fragmentView.findViewById(R.id.fragment_imageview_back);
        mTextViewTitle = fragmentView.findViewById(R.id.fragment_title_textview);
        mTextViewTitle.setText("Vital");
        mImageViewSearch = fragmentView.findViewById(R.id.fragment_Serach);
        mImageViewSearch.setVisibility(View.GONE);
        mImageViewCart = fragmentView.findViewById(R.id.fragment_cart);
        mImageViewCart.setVisibility(View.GONE);

        mRecyclerViewVital = fragmentView.findViewById(R.id.fragment_vital_Recyclerview);


        mImageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.onBackPressed();
            }
        });


        GetVitalListData();

        return fragmentView;
    }


    //   vital list  adpter class
    public class VitalListAdpter extends RecyclerView.Adapter<VitalListAdpter.MyViewHolderVital> {

        Context mContext;
        ArrayList<VitalListVO> mArrayListVital;

        public VitalListAdpter(MainActivity mainActivity, ArrayList<VitalListVO> mArrayListVitalList) {
            this.mContext = mainActivity;
            this.mArrayListVital = mArrayListVitalList;
        }
        @NonNull
        @Override
        public MyViewHolderVital onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_vital, parent, false);
            MyViewHolderVital myViewHolderPastSchedule = new MyViewHolderVital(v);
            return myViewHolderPastSchedule;
        }

        @SuppressLint("ResourceType")
        @Override
        public void onBindViewHolder(@NonNull MyViewHolderVital holder, int position) {

            if(!mArrayListVital.get(position).getVital().equalsIgnoreCase("null"))
            {
                holder.mTextViewVitalName.setText(mArrayListVital.get(position).getVital());
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentVitalDetails fragmentVitalDetails = new FragmentVitalDetails();
                    Bundle mBundle = new Bundle();
                    mBundle.putString("VitalID",mArrayListVital.get(position).getVitalID());
                    mBundle.putString("VitalUnit",mArrayListVital.get(position).getUnit());
                    mBundle.putString("VitalName",mArrayListVital.get(position).getVital());
                    fragmentVitalDetails.setArguments(mBundle);
                    mainActivity.addFragment(fragmentVitalDetails, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, fragmentVitalDetails.getClass().getName());
                }
            });
        }

        @Override
        public int getItemCount() {
            return mArrayListVital.size();
        }

        public class MyViewHolderVital extends RecyclerView.ViewHolder {

            TextView mTextViewVitalName;

            public MyViewHolderVital(View itemView) {
                super(itemView);

                mTextViewVitalName = itemView.findViewById(R.id.row_vital_list_textview_vitalname);

            }
        }
    }


// get vital list data api function
    public void GetVitalListData() {

        getLoaderManager().restartLoader(0, null, new LoaderManager.LoaderCallbacks<TaskExecutor>() {
            @Override
            public Loader<TaskExecutor> onCreateLoader(int arg0, Bundle arg1) {

                GetVitalListExe getVitalListExe = new GetVitalListExe(mainActivity, null);
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

                                    mArrayListVitalList = (ArrayList<VitalListVO>) mServerResponseVO.getData();
                                    if (mArrayListVitalList != null && mArrayListVitalList.size() > 0) {

                                        VitalListAdpter pastScheduleListAdpter = new VitalListAdpter(mainActivity, mArrayListVitalList);
                                        mRecyclerViewVital.setHasFixedSize(true);
                                        mRecyclerViewVital.setLayoutManager(new LinearLayoutManager(mainActivity));
                                        mRecyclerViewVital.setAdapter(pastScheduleListAdpter);

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
    public class GetVitalListExe extends TaskExecutor {
        protected GetVitalListExe(Context context, Bundle bundle) {
            super(context, bundle);
        }

        @Override
        protected Object executeTask() {
            VitalListService vitalListService = new VitalListService();
            mServerResponseVO = vitalListService.vitalList(mainActivity, Tags.GetAllVitals);

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