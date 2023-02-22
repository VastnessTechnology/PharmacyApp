package com.vgroyalchemist.views;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
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
import com.vgroyalchemist.webservice.PostParseGet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/* developed by krishna 24-03-2021
*  list of active Schedule */
public class FragmentSchedule extends NonCartFragment {

    //Variable Declaration
    public static final String FRAGMENT_ID = "24";

    public static MainActivity mainActivity;
    public View fragmentView = null;
    CommonHelper mCommonHelper;

    ImageView mImageViewBack;
    TextView mTextViewTitle;
    ImageView mImageViewSearch;
    ImageView mImageViewCart;
    ImageView mImageViewFilter;
    ImageView mImageViewSort;
    ImageView mImageViewAdd;
    TextView mTextViewPast;

    RecyclerView mRecyclerViewSchedule;
    Dialog mDialogFilter;
    Dialog dialog;
    Dialog mDialogSort;

    ListView mListViewType;
    ArrayList<String> mStringsType;
    TextView mEditTextType;

    ListView mListViewSort;
    ArrayList<String> mArrayListSort;
    ArrayList<ScheduleActiveVO> mArrayListSchedule;

    ServerResponseVO mServerResponseVO;
    PostParseGet mPostParseGet;

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
        mStringsType = new ArrayList<String>();
        mStringsType.add("All");
        mStringsType.add("Regular");
        mStringsType.add("As Needed");

        mArrayListSort = new ArrayList<String>();
        mArrayListSort.add("Medication");
        mArrayListSort.add("End Date");

        mArrayListSchedule = new ArrayList<ScheduleActiveVO>();
        mPostParseGet = new PostParseGet(mainActivity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentView = inflater.inflate(R.layout.fragment_schedule, container, false);

        mImageViewBack = fragmentView.findViewById(R.id.fragment_imageview_back);
        mTextViewTitle = fragmentView.findViewById(R.id.fragment_title_textview);
        mTextViewTitle.setText("Schedule");
        mImageViewSearch = fragmentView.findViewById(R.id.fragment_Serach);
        mImageViewSearch.setVisibility(View.GONE);
        mImageViewCart = fragmentView.findViewById(R.id.fragment_cart);
        mImageViewCart.setVisibility(View.GONE);

        mImageViewFilter = fragmentView.findViewById(R.id.fragment_schedule_imageview_filter);
        mImageViewSort = fragmentView.findViewById(R.id.fragment_schedule_imageview_sort);
        mImageViewAdd = fragmentView.findViewById(R.id.fragment_schedule_imageview_Add);

        mTextViewPast = fragmentView.findViewById(R.id.fragment_schedule_textview_past);
        mRecyclerViewSchedule = fragmentView.findViewById(R.id.fragment_Schedule_Recyclerview);


        mSharedPreferencesUserId = mainActivity.getSharedPreferences(Tags.TAG_SHARED_PREFERENCE_USER_ID, 0);
        mStringUSerid = mSharedPreferencesUserId.getString("UserId", "");
        mEditor = mSharedPreferencesUserId.edit();

        mCalendar = Calendar.getInstance();
        dateFormatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.US);
        currentDate = dateFormatter.format(mCalendar.getTime());
        Utility.debugger("Vt currentDate......" + currentDate);

        mImageViewFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mDialogFilter = new Dialog(mainActivity);
                mDialogFilter.requestWindowFeature(Window.FEATURE_NO_TITLE);
                mDialogFilter.setContentView(R.layout.row_schedule_filter);

                mEditTextType = mDialogFilter.findViewById(R.id.popup_filter_list_edit_type);

                mEditTextType.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DilogTypeList();
                    }
                });

                TextView mTextViewCancle = mDialogFilter.findViewById(R.id.popup_filter_list_Textview_Cancle);
                TextView mTextViewOK = mDialogFilter.findViewById(R.id.popup_filter_list_Textview_OK);

                mTextViewCancle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDialogFilter.dismiss();
                    }
                });

                mDialogFilter.show();
            }
        });

        mImageViewAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentAddMedicine fragmentAddMedicine = new FragmentAddMedicine();
                mainActivity.addFragment(fragmentAddMedicine, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, fragmentAddMedicine.getClass().getName());

            }
        });
        mImageViewSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DilogSort();
            }
        });
        mImageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mainActivity.onBackPressed();
            }
        });

        mTextViewPast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentPastSchedule fragmentPastSchedule = new FragmentPastSchedule();
                mainActivity.addFragment(fragmentPastSchedule, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, fragmentPastSchedule.getClass().getName());
            }
        });

        return fragmentView;
    }


    //   Schedule adpter class
    public class ScheduleListAdpter extends RecyclerView.Adapter<ScheduleListAdpter.MyViewHolderSchedule> {

        Context mContext;
        ArrayList<ScheduleActiveVO> mArrayListScheduleItem;

        public ScheduleListAdpter(MainActivity mainActivity, ArrayList<ScheduleActiveVO> mArrayListSchedule) {
            this.mContext = mainActivity;
            this.mArrayListScheduleItem = mArrayListSchedule;
        }

        @NonNull
        @Override
        public MyViewHolderSchedule onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_schedule_past_future, parent, false);
            MyViewHolderSchedule myViewHolderPastSchedule = new MyViewHolderSchedule(v);
            return myViewHolderPastSchedule;
        }

        @SuppressLint("ResourceType")
        @Override
        public void onBindViewHolder(@NonNull MyViewHolderSchedule holder, int position) {

            holder.mTextViewDate.setVisibility(View.VISIBLE);
            if (!mArrayListScheduleItem.get(position).getMedicineName().equalsIgnoreCase("") && mArrayListScheduleItem.get(position).getMedicineName() != null) {

                holder.mTextViewMedicineName.setText(mArrayListScheduleItem.get(position).getMedicineName());
            }
            if (!mArrayListScheduleItem.get(position).getDosage().equalsIgnoreCase("") && mArrayListScheduleItem.get(position).getDosage() != null) {

                holder.mTextViewDosage.setText(mArrayListScheduleItem.get(position).getDosage());
            }
            if (!mArrayListScheduleItem.get(position).getTime().equalsIgnoreCase("") && mArrayListScheduleItem.get(position).getTime() != null) {

                holder.mTextViewTime.setText(mArrayListScheduleItem.get(position).getTime());
            }
            if (!mArrayListScheduleItem.get(position).getDateday().equalsIgnoreCase("") && mArrayListScheduleItem.get(position).getDateday() != null) {

                holder.mTextViewDate.setText(mArrayListScheduleItem.get(position).getDateday());
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentScheduleDetails fragmentScheduleDetails = new FragmentScheduleDetails();
                    Bundle mBundle = new Bundle();
                    mBundle.putString("ScheduleID",mArrayListScheduleItem.get(position).getScheduleId());
                    fragmentScheduleDetails.setArguments(mBundle);
                    mainActivity.addFragment(fragmentScheduleDetails, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, fragmentScheduleDetails.getClass().getName());
                }
            });
        }

        @Override
        public int getItemCount() {
            return mArrayListScheduleItem.size();
        }

        public class MyViewHolderSchedule extends RecyclerView.ViewHolder {

            TextView mTextViewTime;
            TextView mTextViewDate;
            TextView mTextViewDosage;
            TextView mTextViewMedicineName;

            public MyViewHolderSchedule(View itemView) {
                super(itemView);

                mTextViewDate = itemView.findViewById(R.id.row_schedule_textview_Schedule_Date);
                mTextViewTime = itemView.findViewById(R.id.row_schedule_textview_Schedule_time);
                mTextViewDosage = itemView.findViewById(R.id.row_schedule_textview_Schedule_Dosage);
                mTextViewMedicineName = itemView.findViewById(R.id.row_schedule_textview_Schedule_MedicineName);
            }
        }
    }


    // Dialog Sort
    public void DilogSort() {
        mDialogSort = new Dialog(mainActivity);
        mDialogSort.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialogSort.setContentView(R.layout.dialog_list_item);
        mDialogSort.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        mListViewSort = mDialogSort.findViewById(R.id.dialog_listview);

        MyBaseAdapterSort myBaseAdapterSort = new MyBaseAdapterSort(mainActivity, R.layout.row_title, mArrayListSort);
        mListViewSort.setAdapter(myBaseAdapterSort);
        mDialogSort.show();
    }

    //    Sort Dialog adapter
    public class MyBaseAdapterSort extends ArrayAdapter<String> {

        ArrayList<String> SortList;

        public MyBaseAdapterSort(Context context, int resource, ArrayList<String> objects) {
            super(context, resource, objects);
            SortList = objects;
        }

        @Override
        public int getCount() {
            return SortList.size();
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            ViewHolderSort holder;

            if (convertView == null) {

                convertView = mainActivity.getLayoutInflater().inflate(R.layout.row_title, parent, false);
                holder = new ViewHolderSort();

                holder.mTextViewTitle = convertView.findViewById(R.id.row_name_textview_name);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolderSort) convertView.getTag();
            }

            holder.mTextViewTitle.setText(SortList.get(position));


            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mDialogSort.dismiss();
//                    mEditTextType.setText(SortList.get(position));
                }
            });

            return convertView;
        }
    }

    static class ViewHolderSort {
        TextView mTextViewTitle;
    }


    // Dialog Type list
    public void DilogTypeList() {
        dialog = new Dialog(mainActivity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_list_item);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        mListViewType = dialog.findViewById(R.id.dialog_listview);
        MyBaseAdapter myBaseAdapter = new MyBaseAdapter(mainActivity, R.layout.row_title, mStringsType);
        mListViewType.setAdapter(myBaseAdapter);
        dialog.show();

    }


    public class MyBaseAdapter extends ArrayAdapter<String> {

        ArrayList<String> TypeList;

        public MyBaseAdapter(Context context, int resource, ArrayList<String> objects) {
            super(context, resource, objects);
            TypeList = objects;
        }

        @Override
        public int getCount() {
            return TypeList.size();
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            ViewHolderBank holder;

            if (convertView == null) {

                convertView = mainActivity.getLayoutInflater().inflate(R.layout.row_title, parent, false);
                holder = new ViewHolderBank();

                holder.mTextViewTitle = convertView.findViewById(R.id.row_name_textview_name);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolderBank) convertView.getTag();
            }

            holder.mTextViewTitle.setText(TypeList.get(position));


            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    dialog.dismiss();
                    mEditTextType.setText(TypeList.get(position));
                }
            });

            return convertView;
        }
    }

    static class ViewHolderBank {
        TextView mTextViewTitle;
    }


    public void onResumeData() {
        mainActivity.toolbar.setVisibility(View.GONE);

        GetActiveScheduleList();
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


//    display add schedule list

    public void GetActiveScheduleList() {

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

                                    mArrayListSchedule = (ArrayList<ScheduleActiveVO>) mServerResponseVO.getData();

                                    if (mArrayListSchedule != null && mArrayListSchedule.size() > 0) {
                                        mRecyclerViewSchedule.setVisibility(View.VISIBLE);

                                        ScheduleListAdpter pastScheduleListAdpter = new ScheduleListAdpter(mainActivity, mArrayListSchedule);
                                        mRecyclerViewSchedule.setHasFixedSize(true);
                                        mRecyclerViewSchedule.setLayoutManager(new LinearLayoutManager(mainActivity));
                                        mRecyclerViewSchedule.setAdapter(pastScheduleListAdpter);

                                    } else {

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

            GetActiveScheduleService mGetAllCartService = new GetActiveScheduleService();

            try {
                mServerResponseVO = mGetAllCartService.getAllSchedule(mainActivity, Tags.GetActiveSchedule, mStringUSerid, currentDate);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}