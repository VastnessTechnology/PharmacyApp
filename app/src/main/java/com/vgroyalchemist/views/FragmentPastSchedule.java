package com.vgroyalchemist.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vgroyalchemist.R;
import com.vgroyalchemist.controller.MainActivity;
import com.vgroyalchemist.loaders.LoaderTask;
import com.vgroyalchemist.loaders.TaskExecutor;
import com.vgroyalchemist.utils.CommonHelper;
import com.vgroyalchemist.utils.Snackbar;
import com.vgroyalchemist.utils.Tags;
import com.vgroyalchemist.utils.Utility;
import com.vgroyalchemist.vos.ScheduleActiveVO;
import com.vgroyalchemist.vos.ServerResponseVO;
import com.vgroyalchemist.webservice.GetActiveScheduleService;
import com.vgroyalchemist.webservice.GetPastScheduleService;
import com.vgroyalchemist.webservice.PostParseGet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/* developed by krishna 24-03-2021
*  Display list past schedule*/
public class FragmentPastSchedule extends NonCartFragment {

    //Variable Declaration
    public static final String FRAGMENT_ID = "39";

    public static MainActivity mainActivity;
    public View fragmentView = null;
    CommonHelper mCommonHelper;

    ImageView mImageViewBack;
    TextView mTextViewTitle;
    ImageView mImageViewSearch;
    ImageView mImageViewCart;

    LinearLayout mLinearLayoutEmpty;
    RelativeLayout mRelativeLayoutMain;
    RecyclerView mRecyclerViewSchedule;


    ArrayList mArrayListPastSchedule;
    PostParseGet mPostParseGet;
    ServerResponseVO mServerResponseVO;

    // userdata
    public SharedPreferences mSharedPreferencesUserId;
    SharedPreferences.Editor mEditor;
    String mStringUSerid;
    String currentDate;
    Calendar mCalendar;
    private SimpleDateFormat dateFormatter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainActivity = (MainActivity) getActivity();
        mCommonHelper = new CommonHelper();

        mArrayListPastSchedule = new ArrayList();
        mArrayListPastSchedule.add(1);
        mArrayListPastSchedule.add(2);

        mPostParseGet = new PostParseGet(mainActivity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentView = inflater.inflate(R.layout.fragment_past_schedule, container, false);


        mImageViewBack = fragmentView.findViewById(R.id.fragment_imageview_back);
        mTextViewTitle = fragmentView.findViewById(R.id.fragment_title_textview);
        mTextViewTitle.setText("Past Schedule");
        mImageViewSearch = fragmentView.findViewById(R.id.fragment_Serach);
        mImageViewSearch.setVisibility(View.GONE);
        mImageViewCart = fragmentView.findViewById(R.id.fragment_cart);
        mImageViewCart.setVisibility(View.GONE);


        mLinearLayoutEmpty = fragmentView.findViewById(R.id.fragment_PastSchedule_relative_empty);
        mRecyclerViewSchedule = fragmentView.findViewById(R.id.fragment_PastSchedule_Recyclerview);


        mSharedPreferencesUserId = mainActivity.getSharedPreferences(Tags.TAG_SHARED_PREFERENCE_USER_ID, 0);
        mStringUSerid = mSharedPreferencesUserId.getString("UserId", "");
        mEditor = mSharedPreferencesUserId.edit();

        mCalendar = Calendar.getInstance();
        dateFormatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.US);
        currentDate = dateFormatter.format(mCalendar.getTime());
        Utility.debugger("Vt currentDate......" + currentDate);

        mImageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.onBackPressed();
            }
        });


        return fragmentView;
    }



    public void onResumeData() {
        mainActivity.toolbar.setVisibility(View.GONE);

        GetAPastScheduleList();
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


    //    display Past schedule list

    public void GetAPastScheduleList() {

        getLoaderManager().restartLoader(0, null, new LoaderManager.LoaderCallbacks<TaskExecutor>() {
            @Override
            public Loader<TaskExecutor> onCreateLoader(int arg0, Bundle arg1) {
                GetScheduleListData scheduleListData = new GetScheduleListData(mainActivity, null);
                return new LoaderTask<TaskExecutor>(mainActivity, scheduleListData);
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
                        if (mServerResponseVO != null && mServerResponseVO.getStatus() != null) {

                            if (mServerResponseVO.getStatus().equalsIgnoreCase("true")) {

                                if (mServerResponseVO.getData() != null) {

                                    mArrayListPastSchedule = (ArrayList<ScheduleActiveVO>) mServerResponseVO.getData();

                                    if (mArrayListPastSchedule != null && mArrayListPastSchedule.size() > 0) {
                                        mRecyclerViewSchedule.setVisibility(View.VISIBLE);
                                        mLinearLayoutEmpty.setVisibility(View.GONE);

                                        PastScheduleListAdpter pastScheduleListAdpter = new PastScheduleListAdpter(mainActivity, mArrayListPastSchedule);
                                        mRecyclerViewSchedule.setHasFixedSize(true);
                                        mRecyclerViewSchedule.setLayoutManager(new LinearLayoutManager(mainActivity));
                                        mRecyclerViewSchedule.setAdapter(pastScheduleListAdpter);

                                    } else {
                                        mRecyclerViewSchedule.setVisibility(View.GONE);
                                        mLinearLayoutEmpty.setVisibility(View.VISIBLE);
                                    }
                                }
                            } else {
//                                Snackbar.with(mainActivity).text(mServerResponseVO.getMsg()).show(mainActivity);
                                mCommonHelper.hideKeyboard(mainActivity);
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

    public class GetScheduleListData extends TaskExecutor {

        protected GetScheduleListData(Context context, Bundle bundle) {
            super(context, bundle);
        }

        @Override
        protected Object executeTask() {

            GetPastScheduleService mGetAllCartService = new GetPastScheduleService();

            try {
                mServerResponseVO = mGetAllCartService.getAllSchedule(mainActivity, Tags.GetPastSchedule, mStringUSerid,currentDate);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    //    Past Schedule adpter class
    public class PastScheduleListAdpter extends RecyclerView.Adapter<PastScheduleListAdpter.MyViewHolderPastSchedule> {

        Context mContext;
        ArrayList<ScheduleActiveVO> mArrayListScheduleItem;

        public PastScheduleListAdpter(MainActivity mainActivity, ArrayList<ScheduleActiveVO> mArrayListPastSchedule) {
            this.mContext = mainActivity;
            this.mArrayListScheduleItem = mArrayListPastSchedule;
        }

        @NonNull
        @Override
        public MyViewHolderPastSchedule onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_schedule_past_future, parent, false);
            MyViewHolderPastSchedule myViewHolderPastSchedule = new MyViewHolderPastSchedule(v);
            return myViewHolderPastSchedule;
        }

        @SuppressLint("ResourceType")
        @Override
        public void onBindViewHolder(@NonNull MyViewHolderPastSchedule holder, int position) {

            holder.mTextViewDate.setVisibility(View.VISIBLE);

            String color = mArrayListScheduleItem.get(position).getColourcode();
            if(!mArrayListScheduleItem.get(position).getMedicineName().equalsIgnoreCase("") && mArrayListScheduleItem.get(position).getMedicineName() != null ){

                holder.mTextViewMedicineName.setText(mArrayListScheduleItem.get(position).getMedicineName());
                holder.mTextViewMedicineName.setTextColor(Color.parseColor(color));
            }

            if(!mArrayListScheduleItem.get(position).getDosage().equalsIgnoreCase("") && mArrayListScheduleItem.get(position).getDosage() != null ){

                holder.mTextViewDosage.setText(mArrayListScheduleItem.get(position).getDosage());
                holder.mTextViewDosage.setTextColor(Color.parseColor(color));
            }
            if(!mArrayListScheduleItem.get(position).getTime().equalsIgnoreCase("") && mArrayListScheduleItem.get(position).getTime() != null ){

                holder.mTextViewTime.setText(mArrayListScheduleItem.get(position).getTime());
                holder.mTextViewTime.setTextColor(Color.parseColor(color));
            }
            if(!mArrayListScheduleItem.get(position).getDateday().equalsIgnoreCase("") && mArrayListScheduleItem.get(position).getDateday() != null ){

                holder.mTextViewDate.setText(mArrayListScheduleItem.get(position).getDateday());
                holder.mTextViewDate.setTextColor(Color.parseColor(color));
            }

        }

        @Override
        public int getItemCount() {
            return mArrayListScheduleItem.size();
        }

        public class MyViewHolderPastSchedule extends RecyclerView.ViewHolder {

            TextView mTextViewTime;
            TextView mTextViewDate;
            TextView mTextViewDosage;
            TextView mTextViewMedicineName;
            public MyViewHolderPastSchedule(View itemView) {
                super(itemView);

                mTextViewDate = itemView.findViewById(R.id.row_schedule_textview_Schedule_Date);
                mTextViewTime = itemView.findViewById(R.id.row_schedule_textview_Schedule_time);
                mTextViewDosage = itemView.findViewById(R.id.row_schedule_textview_Schedule_Dosage);
                mTextViewMedicineName = itemView.findViewById(R.id.row_schedule_textview_Schedule_MedicineName);
            }
        }


    }
}