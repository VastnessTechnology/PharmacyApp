package com.vgroyalchemist.views;

import android.annotation.SuppressLint;
import android.content.Context;
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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vgroyalchemist.R;
import com.vgroyalchemist.controller.MainActivity;
import com.vgroyalchemist.loaders.LoaderTask;
import com.vgroyalchemist.loaders.LoaderTaskWithoutProgressDialog;
import com.vgroyalchemist.loaders.TaskExecutor;
import com.vgroyalchemist.utils.CommonHelper;
import com.vgroyalchemist.utils.Snackbar;
import com.vgroyalchemist.utils.Tags;
import com.vgroyalchemist.vos.OrderDetailsVO;
import com.vgroyalchemist.vos.ReminderScheduleDetailsVO;
import com.vgroyalchemist.vos.ScheduleDetailsVO;
import com.vgroyalchemist.vos.ServerResponseVO;
import com.vgroyalchemist.webservice.OrderDeatilsService;
import com.vgroyalchemist.webservice.PostParseGet;
import com.vgroyalchemist.webservice.ScheduleDeatilsService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/* developed by krishna 24-03-2021
*  Display Details of Active Schedule */
public class FragmentScheduleDetails extends NonCartFragment {

    //Variable Declaration
    public static final String FRAGMENT_ID = "40";

    public static MainActivity mainActivity;
    public View fragmentView = null;
    CommonHelper mCommonHelper;

    ImageView mImageViewBack;
    TextView mTextViewTitle;
    ImageView mImageViewSearch;
    ImageView mImageViewCart;

    TextView mTextViewStartDate;
    TextView mTextViewEndDate;
    TextView mTextViewSchedulePattern;
    RecyclerView mRecyclerViewReminderList;
    RelativeLayout mRelativeLayout;

    ArrayList<ReminderScheduleDetailsVO> mArrayListRemiderList;
    String mStringScheduleId;

    Bundle mBundle;

    ServerResponseVO mServerResponseVO;
    PostParseGet mPostParseGet;
    ProgressBar mProgressBar;

    ScheduleDetailsVO mScheduleDetailsVO;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainActivity = (MainActivity) getActivity();
        mCommonHelper = new CommonHelper();
        mPostParseGet = new PostParseGet(mainActivity);

        mBundle =getArguments();

        if(mBundle != null)
        {
            mStringScheduleId = mBundle.getString("ScheduleID");
        }

        mScheduleDetailsVO = new ScheduleDetailsVO();
        mArrayListRemiderList = new ArrayList<ReminderScheduleDetailsVO>();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentView = inflater.inflate(R.layout.fragment_schedule_details, container, false);


        mImageViewBack = fragmentView.findViewById(R.id.fragment_imageview_back);
        mTextViewTitle =fragmentView.findViewById(R.id.fragment_title_textview);
        mTextViewTitle.setText("Schedule Deatils");
        mImageViewSearch =fragmentView.findViewById(R.id.fragment_Serach);
        mImageViewSearch.setVisibility(View.GONE);
        mImageViewCart =fragmentView.findViewById(R.id.fragment_cart);
        mImageViewCart.setVisibility(View.GONE);

        mTextViewStartDate  = fragmentView.findViewById(R.id.fragment_textview_Schedule_details_StartDate_value);
        mTextViewEndDate = fragmentView.findViewById(R.id.fragment_textview_Schedule_details_EndDate_value);
        mTextViewSchedulePattern =fragmentView.findViewById(R.id.fragment_textview_Schedule_details_pattern_value);
        mRecyclerViewReminderList = fragmentView.findViewById(R.id.fragment_Schedule_details_Reminde_Recycleview);
        mProgressBar =fragmentView.findViewById(R.id.fragment_Schedule_details_progressbar);
        mRelativeLayout = fragmentView.findViewById(R.id.fragment_Schedule_details_relative_main);
        mImageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.onBackPressed();
            }
        });

        getLoaderManager().restartLoader(0, mBundle, new LoaderManager.LoaderCallbacks<TaskExecutor>() {
            @Override
            public Loader<TaskExecutor> onCreateLoader(int arg0, Bundle arg1) {

                ScheduleDetailsExecutor scheduleDetailsExecutor = new ScheduleDetailsExecutor(mainActivity, null);
                return new LoaderTaskWithoutProgressDialog<TaskExecutor>(mainActivity, scheduleDetailsExecutor);
            }
            @Override
            public void onLoadFinished(Loader<TaskExecutor> arg0, TaskExecutor arg1) {
                mProgressBar.setVisibility(View.VISIBLE);
                if (isAdded()) {
                    getLoaderManager().destroyLoader(0);
                    if (mPostParseGet.isNetError)
                        Snackbar.with(mainActivity).text(getResources().getString(R.string.interneterror)).show(mainActivity);
                    else if (mPostParseGet.isOtherError)
                        Snackbar.with(mainActivity).text(getResources().getString(R.string.notabletocommunicatewithserver)).show(mainActivity);
                    else {

                        if (mServerResponseVO != null && mServerResponseVO.getStatus() != null) {

                            if (mServerResponseVO.getStatus().equalsIgnoreCase("true")) {
//                                Snackbar.with(mainActivity).text(mServerResponseVO.getMsg()).show(mainActivity);

                                if(mServerResponseVO.getData() != null)
                                {
                                    mScheduleDetailsVO = (ScheduleDetailsVO) mServerResponseVO.getData();

                                    if(mScheduleDetailsVO != null)
                                    {
//                                        Start Date
                                        if(!mScheduleDetailsVO.getStartDate().equalsIgnoreCase("null") && mScheduleDetailsVO.getStartDate() != null)
                                        {
                                            String mDate = mScheduleDetailsVO.getStartDate();
                                            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
                                            SimpleDateFormat fmt2 = new SimpleDateFormat("EEE, MMM d, yyyy");
                                            try {
                                                Date date = fmt.parse(mDate);
                                                String mString = fmt2.format(date);
                                                mTextViewStartDate.setText(mString);
                                            } catch (ParseException pe) {
                                                pe.printStackTrace();
                                            }
                                        }

//                                        End Date
                                        if(!mScheduleDetailsVO.getEndDate().equalsIgnoreCase("null") && mScheduleDetailsVO.getEndDate() != null)
                                        {
                                            String mDate = mScheduleDetailsVO.getEndDate();
                                            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
                                            SimpleDateFormat fmt2 = new SimpleDateFormat("EEE, MMM d, yyyy");
                                            try {
                                                Date date = fmt.parse(mDate);
                                                String mString = fmt2.format(date);
                                                mTextViewEndDate.setText(mString);
                                            } catch (ParseException pe) {
                                                pe.printStackTrace();
                                            }
                                        }

//                                        Schedule Pattern
                                        if(!mScheduleDetailsVO.getSchedulePattener().equalsIgnoreCase("null") && mScheduleDetailsVO.getSchedulePattener() != null)
                                        {
                                            mTextViewSchedulePattern.setText(mScheduleDetailsVO.getSchedulePattener());
                                        }

//                                        Reminder List Data
                                        mArrayListRemiderList = mScheduleDetailsVO.getmReminderScheduleDetailsVOS();
                                        if(mArrayListRemiderList != null && mArrayListRemiderList.size() > 0)
                                        {

                                            ScheduleDeatailsAdpter mArrayListSchedule = new ScheduleDeatailsAdpter(mainActivity, mArrayListRemiderList);
                                            mRecyclerViewReminderList.setHasFixedSize(true);
                                            mRecyclerViewReminderList.setLayoutManager(new LinearLayoutManager(mainActivity));
                                            mRecyclerViewReminderList.setAdapter(mArrayListSchedule);
                                        }


                                    }
                                    mProgressBar.setVisibility(View.GONE);
                                    mRelativeLayout.setVisibility(View.VISIBLE);
                                }
                            }
                        } else {
                            mProgressBar.setVisibility(View.GONE);
                            Snackbar.with(mainActivity).text(mServerResponseVO.getMsg()).show(mainActivity);
                        }
                    }
                }
            }
            @Override
            public void onLoaderReset(Loader<TaskExecutor> arg0) {
            }
        });




        return fragmentView;
    }


    //Schedule Deatils
    public class ScheduleDetailsExecutor extends TaskExecutor {

        protected ScheduleDetailsExecutor(Context context, Bundle bundle) {
            super(context, bundle);
        }

        @Override
        protected Object executeTask() {
            try {
                ScheduleDeatilsService fetchVersionInfoService = new ScheduleDeatilsService();
                mServerResponseVO = fetchVersionInfoService.Scheduleetails(mainActivity, Tags.GetScheduleDetails, mStringScheduleId);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    //   Schedule Deatils Reminder adpter class
    public class ScheduleDeatailsAdpter extends RecyclerView.Adapter<ScheduleDeatailsAdpter.MyViewHolderScheduleReminder> {

        Context mContext;
        ArrayList<ReminderScheduleDetailsVO> mArrayListScheduleItem;

        public ScheduleDeatailsAdpter(MainActivity mainActivity, ArrayList<ReminderScheduleDetailsVO> mArrayListRemiderList) {
            this.mContext = mainActivity;
            this.mArrayListScheduleItem = mArrayListRemiderList;
        }
        @NonNull
        @Override
        public MyViewHolderScheduleReminder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_schedule_reminder_time_list, parent, false);
            MyViewHolderScheduleReminder scheduleReminder = new MyViewHolderScheduleReminder(v);
            return scheduleReminder;
        }

        @SuppressLint("ResourceType")
        @Override
        public void onBindViewHolder(@NonNull MyViewHolderScheduleReminder holder, int position) {

            if(!mArrayListScheduleItem.get(position).getReminderDateTime().equalsIgnoreCase("null"))
            {
                String mDate = mArrayListScheduleItem.get(position).getReminderDateTime();
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
            if(!mArrayListScheduleItem.get(position).getTime().equalsIgnoreCase("null"))
            {
                holder.mTextViewTime.setText(mArrayListScheduleItem.get(position).getTime());
            }
            if(!mArrayListScheduleItem.get(position).getDosage().equalsIgnoreCase("null"))
            {
                if(!mScheduleDetailsVO.getMedicineName().equalsIgnoreCase("null"))
                {
                    holder.mTextViewtablet.setText( mScheduleDetailsVO.getMedicineName() + "  -  " + mArrayListScheduleItem.get(position).getDosage());
                }
            }
        }

        @Override
        public int getItemCount() {
            return mArrayListScheduleItem.size();
        }

        public class MyViewHolderScheduleReminder extends RecyclerView.ViewHolder {

            TextView mTextViewTime;
            TextView mTextViewtablet;
            TextView mTextViewDate;

            public MyViewHolderScheduleReminder(View itemView) {
                super(itemView);

                mTextViewtablet = itemView.findViewById(R.id.row_Schedule_reminder_textview_item);
                mTextViewTime = itemView.findViewById(R.id.row_Schedule_reminder_textview_time);
                mTextViewDate = itemView.findViewById(R.id.row_schedule_Reminder_textview_Date);
            }
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