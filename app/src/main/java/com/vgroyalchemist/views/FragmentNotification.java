package com.vgroyalchemist.views;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
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
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.vgroyalchemist.vos.NotificationVO;
import com.vgroyalchemist.vos.OrderDataVo;
import com.vgroyalchemist.vos.ServerResponseVO;
import com.vgroyalchemist.vos.UserDetailsVO;
import com.vgroyalchemist.webservice.DeleteAddressInfoService;
import com.vgroyalchemist.webservice.DeleteNotiService;
import com.vgroyalchemist.webservice.GetAllAddressInfoService;
import com.vgroyalchemist.webservice.GetNotificationService;
import com.vgroyalchemist.webservice.PostParseGet;
import com.vgroyalchemist.webservice.ReadNotiService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/* developed by krishna 24-03-2021
*  Get Notification list and delete notification*/
public class FragmentNotification extends NonCartFragment {

    //Variable Declaration
    public static final String FRAGMENT_ID = "37";

    public static MainActivity mainActivity;
    public View fragmentView = null;
    CommonHelper mCommonHelper;

    ImageView mImageViewSearch;
    ImageView mImageViewCart;
    ImageView mImageViewBack;
    TextView mTextViewTitle;

    Dialog mDialog;

    LinearLayout mLinearLayoutNodata;
    RelativeLayout mRelativeLayoutRecycleview;
    RecyclerView mRecyclerViewNotification;

    ArrayList<NotificationVO> mStringsNotificationData;

    PostParseGet mPostParseGet;
    ServerResponseVO mServerResponseVO;
    ServerResponseVO mServerResponseVODelete;

    NotificationListData notificationListData;

    // userdata
    public SharedPreferences mSharedPreferencesUserId;
    SharedPreferences.Editor mEditor;
    String mStringUserId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = (MainActivity) getActivity();
        mCommonHelper = new CommonHelper();
        mPostParseGet = new PostParseGet(mainActivity);

        mStringsNotificationData = new ArrayList<NotificationVO>();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentView = inflater.inflate(R.layout.fragment_notification, container, false);

        mImageViewBack = fragmentView.findViewById(R.id.fragment_imageview_back);
        mTextViewTitle = fragmentView.findViewById(R.id.fragment_title_textview);
        mTextViewTitle.setText("Notification");
        mImageViewSearch = fragmentView.findViewById(R.id.fragment_Serach);
        mImageViewSearch.setVisibility(View.GONE);
        mImageViewCart = fragmentView.findViewById(R.id.fragment_cart);
        mImageViewCart.setVisibility(View.GONE);
        mLinearLayoutNodata = fragmentView.findViewById(R.id.fragment_notification_relative_empty);
        mRecyclerViewNotification =fragmentView.findViewById(R.id.fragment_notification_Recyclerview);


        mSharedPreferencesUserId = mainActivity.getSharedPreferences(Tags.TAG_SHARED_PREFERENCE_USER_ID, 0);
        mStringUserId = mSharedPreferencesUserId.getString("UserId", "");
        mEditor = mSharedPreferencesUserId.edit();

        mImageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.onBackPressed();
            }
        });


        GetAllNotification();

        return fragmentView;
    }


    public class NotificationListData extends RecyclerView.Adapter<NotificationListData.MyViewHolderNotification>{


        Context mContext;
        ArrayList<NotificationVO> stringArrayList;

        public NotificationListData(MainActivity mainActivity, ArrayList<NotificationVO> mStringsNotificationData) {

            this.mContext =mainActivity;
            this.stringArrayList =mStringsNotificationData;
        }

        @NonNull
        @Override
        public MyViewHolderNotification onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_notification, parent, false);
            MyViewHolderNotification myViewHolderNotification = new MyViewHolderNotification(v);
            return myViewHolderNotification;
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolderNotification holder, int position) {


            if (stringArrayList.get(position).getIsRead().equalsIgnoreCase("false")) {

                holder.mTextViewNotiTitle.setTypeface(null, Typeface.BOLD);
                holder.mTextViewNotiDetails.setTypeface(null, Typeface.BOLD);
                holder.mTextViewDateTime.setTypeface(null, Typeface.BOLD);
                holder.mTextViewNotiTitle.setTextColor(getResources().getColor(R.color.black));
                holder.mTextViewNotiDetails.setTextColor(getResources().getColor(R.color.black));
                holder.mTextViewDateTime.setTextColor(getResources().getColor(R.color.black));

            }
            else {

                holder.mTextViewNotiTitle.setTypeface(null, Typeface.NORMAL);
                holder.mTextViewNotiDetails.setTypeface(null, Typeface.NORMAL);
                holder.mTextViewDateTime.setTypeface(null, Typeface.NORMAL);
                holder.mTextViewNotiTitle.setTextColor(getResources().getColor(R.color.dark_grey));
                holder.mTextViewNotiDetails.setTextColor(getResources().getColor(R.color.dark_grey));
                holder.mTextViewDateTime.setTextColor(getResources().getColor(R.color.dark_grey));
            }

            holder.mTextViewNotiTitle.setText(stringArrayList.get(position).getTitle());
            holder.mTextViewNotiDetails.setText(stringArrayList.get(position).getMessage());

            if(!stringArrayList.get(position).getAddedDate().equalsIgnoreCase("null") && stringArrayList.get(position).getAddedDate()!= null)
            {
                String mDate = stringArrayList.get(position).getAddedDate();
                SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat fmt2 = new SimpleDateFormat("EEE, MMM d, yyyy");
                try {
                    Date date = fmt.parse(mDate);
                    String mString = fmt2.format(date);
                    holder.mTextViewDateTime.setText(mString);
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
                    mBundle.putString(getString(R.string.app_name), stringArrayList.get(position).getNotiId());


                    getLoaderManager().restartLoader(0, mBundle, new LoaderManager.LoaderCallbacks<TaskExecutor>() {
                        @Override
                        public Loader<TaskExecutor> onCreateLoader(int arg0, Bundle arg1) {

                            DeleteNotiExecutor mSearchOrderProductProcess = new DeleteNotiExecutor(mainActivity, mBundle);
                            return new LoaderTask<TaskExecutor>(mainActivity, mSearchOrderProductProcess);
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

                                            stringArrayList.remove(position);
                                            notificationListData.notifyDataSetChanged();

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

            holder.mImageViewInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if (!mCommonHelper.check_Internet(getActivity())) {
                        Snackbar.with(mainActivity).text(getResources().getString(R.string.interneterror)).show(mainActivity);
                        return;
                    }


                    final Bundle mBundle = new Bundle();
                    mBundle.putString(getString(R.string.app_name), stringArrayList.get(position).getNotiId());


                    getLoaderManager().restartLoader(0, mBundle, new LoaderManager.LoaderCallbacks<TaskExecutor>() {
                        @Override
                        public Loader<TaskExecutor> onCreateLoader(int arg0, Bundle arg1) {

                            ReadNotiExecutor readNotiExecutor = new ReadNotiExecutor(mainActivity, mBundle);
                            return new LoaderTaskWithoutProgressDialog<TaskExecutor>(mainActivity, readNotiExecutor);
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

                                            mDialog = new Dialog(mainActivity);
                                            mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                                            mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                            mDialog.setContentView(R.layout.popup_notification);
                                            mDialog.setCanceledOnTouchOutside(false);

                                            final TextView mTextViewSubmit = mDialog.findViewById(R.id.popup_notification_textview_submit);
                                            final TextView mTextViewTitle = mDialog.findViewById(R.id.popup_notification_textview_title);
                                            final TextView mTextViewMessage = mDialog.findViewById(R.id.popup_notification_textview_message);


                                            mTextViewTitle.setText(stringArrayList.get(position).getTitle());
                                            mTextViewMessage.setText(stringArrayList.get(position).getMessage());
                                            mTextViewSubmit.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    mDialog.dismiss();
                                                    GetAllNotification();
                                                }
                                            });

                                            mDialog.show();
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
            return stringArrayList.size();
        }

        public class MyViewHolderNotification extends RecyclerView.ViewHolder {


            ImageView mImageViewLogo;
            TextView mTextViewNotiTitle;
            TextView mTextViewNotiDetails;
            TextView mTextViewDateTime;
            ImageView mImageViewDelete;
            ImageView mImageViewInfo;

            public MyViewHolderNotification(View itemView) {
                super(itemView);

                    mTextViewNotiTitle  =itemView.findViewById(R.id.row_notification_textview_name);
                    mImageViewLogo = itemView.findViewById(R.id.row_notification_imageview_main);
                    mTextViewNotiDetails = itemView.findViewById(R.id.row_notification_textview_details);
                    mTextViewDateTime = itemView.findViewById(R.id.row_notification_textview_date_time);
                    mImageViewDelete = itemView.findViewById(R.id.row_notification_imageview_delete);
                    mImageViewInfo = itemView.findViewById(R.id.row_notification_imageview_info);
            }
        }
    }

    public void onResumeData() {
        if(mainActivity.toolbar != null) {
            mainActivity.toolbar.setVisibility(View.GONE);
        }
    }

    public class DeleteNotiExecutor extends TaskExecutor {

        String stringNotiId;

        protected DeleteNotiExecutor(Context context, Bundle bundle) {
            super(context, bundle);
            this.stringNotiId = bundle.getString(getString(R.string.app_name));
        }

        @Override
        protected Object executeTask() {

            try {

                DeleteNotiService fetchVersionInfoService = new DeleteNotiService();
                mServerResponseVODelete = fetchVersionInfoService.deleteNoti(mainActivity, Tags.DeleteNotification,stringNotiId);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }


    public class ReadNotiExecutor extends TaskExecutor {

        String stringNotiId;

        protected ReadNotiExecutor(Context context, Bundle bundle) {
            super(context, bundle);
            this.stringNotiId = bundle.getString(getString(R.string.app_name));
        }

        @Override
        protected Object executeTask() {

            try {

                ReadNotiService fetchVersionInfoService = new ReadNotiService();
                mServerResponseVODelete = fetchVersionInfoService.ReadNoti(mainActivity, Tags.ReadNotification,stringNotiId,mStringUserId);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }


    public void GetAllNotification() {

        getLoaderManager().restartLoader(0, null, new LoaderManager.LoaderCallbacks<TaskExecutor>() {
            @Override
            public Loader<TaskExecutor> onCreateLoader(int arg0, Bundle arg1) {

                GetAllNotificationEx getAllNotificationEx = new GetAllNotificationEx(mainActivity, null);
                return new LoaderTask<TaskExecutor>(mainActivity, getAllNotificationEx);
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

                                mStringsNotificationData = (ArrayList<NotificationVO>) mServerResponseVO.getData();

                                if (mStringsNotificationData != null && mStringsNotificationData.size() > 0) {
                                    mLinearLayoutNodata.setVisibility(View.GONE);
                                    mRecyclerViewNotification.setVisibility(View.VISIBLE);

                                    notificationListData = new NotificationListData(mainActivity, mStringsNotificationData);
                                    mRecyclerViewNotification.setHasFixedSize(true);
                                    mRecyclerViewNotification.setLayoutManager(new LinearLayoutManager(mainActivity));
                                    mRecyclerViewNotification.setAdapter(notificationListData);
                                } else {
                                    mLinearLayoutNodata.setVisibility(View.VISIBLE);
                                    mRecyclerViewNotification.setVisibility(View.GONE);
                                }
                            }
                        } else {
                            Snackbar.with(mainActivity).text(mServerResponseVO.getMsg()).show(mainActivity);
                            mCommonHelper.hideKeyboard(mainActivity);
                        }
                        }
                        catch (Exception e)
                        {

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
    public class GetAllNotificationEx extends TaskExecutor {
        protected GetAllNotificationEx(Context context, Bundle bundle) {
            super(context, bundle);
        }

        @Override
        protected Object executeTask() {

            GetNotificationService getNotificationService = new GetNotificationService();
            mServerResponseVO = getNotificationService.GetNotification(mainActivity, Tags.GetAllNotification ,mStringUserId);

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

    @Override
    public void onDestroy() {
        super.onDestroy();

        mCommonHelper.hideKeyboard(mainActivity);
    }
}