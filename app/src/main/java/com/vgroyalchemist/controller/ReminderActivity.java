package com.vgroyalchemist.controller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.vgroyalchemist.R;
import com.vgroyalchemist.loaders.LoaderTask;
import com.vgroyalchemist.loaders.LoaderTaskAndroid;
import com.vgroyalchemist.loaders.TaskExecutor;
import com.vgroyalchemist.utils.CommonHelper;
import com.vgroyalchemist.utils.Snackbar;
import com.vgroyalchemist.utils.Tags;
import com.vgroyalchemist.utils.Utility;
import com.vgroyalchemist.views.FragmentSchedule;
import com.vgroyalchemist.vos.ScheduleActiveVO;
import com.vgroyalchemist.vos.ServerResponseVO;
import com.vgroyalchemist.webservice.GetActiveScheduleService;
import com.vgroyalchemist.webservice.PostParseGet;
import com.vgroyalchemist.webservice.SecudleReminderService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ReminderActivity extends AppCompatActivity {

    ImageView mImageViewBack;
    TextView mTextViewTitle;
    ImageView mImageViewSearch;
    ImageView mImageViewCart;

    TextView mTextViewTaken;
    TextView mTextViewSoonze;
    TextView mTextViewNonTaken;

    TextView mTextViewTime;
    TextView mTextViewMedicinename;
    TextView mTextViewDosage;
    TextView mTextViewDate;


    Dialog dialog;

    ListView mListViewType;
    ArrayList<String> mStringsType;
    ServerResponseVO mServerResponseVO;
    PostParseGet mPostParseGet;

    ReminderActivity mReminderActivity;

    String mStringAction;
    String mStringSoonzeTime;
    String mStringReminderId ="";

    String mStringMedicineName;
    String mStringDosage;
    String  mStringTime;


    Intent mIntent;
    Bundle extras;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

        mPostParseGet = new PostParseGet(ReminderActivity.this);
        mReminderActivity = this;

        mImageViewBack = findViewById(R.id.fragment_imageview_back);
        mTextViewTitle =findViewById(R.id.fragment_title_textview);
        mTextViewTitle.setText("Reminder");
        mImageViewSearch =findViewById(R.id.fragment_Serach);
        mImageViewSearch.setVisibility(View.GONE);
        mImageViewCart =findViewById(R.id.fragment_cart);
        mImageViewCart.setVisibility(View.GONE);

        mTextViewTaken = findViewById(R.id.activity_reminder_textview_taken);
        mTextViewNonTaken = findViewById(R.id.activity_reminder_textview_skip);
        mTextViewSoonze = findViewById(R.id.activity_reminder_textview_Soonze);

        mTextViewTime = findViewById(R.id.activity_reminder_textview_time);
        mTextViewDate = findViewById(R.id.activity_reminder_textview_date);
        mTextViewMedicinename = findViewById(R.id.activity_reminder_medicine_name);

        mStringsType = new ArrayList<String>();
        mStringsType.add("5 Minutes");
        mStringsType.add("10 Minutes");
        mStringsType.add("15 Minutes");
        mStringsType.add("30 Minutes");
        mStringsType.add("60 Minutes");

        extras =getIntent().getExtras();
        if (extras != null) {
            mStringReminderId = extras.getString("ReminderId");
            mStringMedicineName = extras.getString("medicinename");
            mStringDosage = extras.getString("dosage");
            mStringTime =extras.getString("time");
        }

        if(mStringMedicineName != null)
        {
            mTextViewMedicinename.setText(mStringMedicineName + " - " + mStringDosage);
        }
        if(mStringTime != null)
        {
            mTextViewTime.setText(mStringTime);
//            String mDate = mStringTime;
//            SimpleDateFormat fmt = new SimpleDateFormat("HH:mm:ss");
//            SimpleDateFormat fmt2 = new SimpleDateFormat("hh:mm a");
//            try {
//                Date date = fmt.parse(mDate);
//                String mString = fmt2.format(date);
//                mTextViewTime.setText(mString);
//            } catch (ParseException pe) {
//                pe.printStackTrace();
//            }

        }
        mImageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(ReminderActivity.this ,MainActivity.class);
                startActivity(mIntent);
            }
        });

        mTextViewTaken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStringAction = "1";
                addreminderaction();
            }
        });

        mTextViewNonTaken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStringAction = "3";
                addreminderaction();

            }
        });

        mTextViewSoonze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStringAction = "2";
                DilogSoonzeList();

            }
        });
    }


    public void DilogSoonzeList() {
        dialog = new Dialog(ReminderActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_list_item);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        mListViewType = dialog.findViewById(R.id.dialog_listview);
        MyBaseAdapter myBaseAdapter = new MyBaseAdapter(ReminderActivity.this, R.layout.row_title, mStringsType);
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

                convertView = ReminderActivity.this.getLayoutInflater().inflate(R.layout.row_title, parent, false);
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
                    mStringSoonzeTime = TypeList.get(position);
                    addreminderaction();

                }
            });

            return convertView;
        }
    }

    static class ViewHolderBank {
        TextView mTextViewTitle;
    }


//  display add schedule list

    public void addreminderaction() {
        getLoaderManager().restartLoader(0, null, new android.app.LoaderManager.LoaderCallbacks<TaskExecutor>() {
            @Override
            public android.content.Loader<TaskExecutor> onCreateLoader(int id, Bundle args) {
                GetScheduleListData mGuestUserExecutor = new GetScheduleListData(mReminderActivity, null);
                return new LoaderTaskAndroid<TaskExecutor>(mReminderActivity, mGuestUserExecutor);
            }

            @Override
            public void onLoadFinished(android.content.Loader<TaskExecutor> loader, TaskExecutor data) {

                getLoaderManager().destroyLoader(0);


                if (mServerResponseVO != null && mServerResponseVO.getStatus() != null) {
                    if (mServerResponseVO.getStatus().equalsIgnoreCase("true")) {
//                        Snackbar.with(mReminderActivity).text(mServerResponseVO.getMsg()).show(mReminderActivity);

                            finish();

                    } else {
                        Snackbar.with(mReminderActivity).text(mServerResponseVO.getMsg()).show(mReminderActivity);
                    }
                }
            }

            @Override
            public void onLoaderReset(android.content.Loader<TaskExecutor> loader) {

            }
        });
    }

    public class GetScheduleListData extends TaskExecutor {

        protected GetScheduleListData(Context context, Bundle bundle) {
            super(context, bundle);
        }

        @Override
        protected Object executeTask() {

            SecudleReminderService mGetAllCartService = new SecudleReminderService();

            Utility.debugger("Vt Reminder id..." + mStringReminderId);
            Utility.debugger("Vt mStringSoonzeTime..." + mStringSoonzeTime);
            Utility.debugger("Vt mStringAction..." + mStringAction);
            try {
                mServerResponseVO = mGetAllCartService.getAllSchedule(ReminderActivity.this, Tags.UpdateScheduleReminder, mStringReminderId, mStringSoonzeTime,mStringAction);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    @Override
    protected void onResume() {


        super.onResume();
    }
}