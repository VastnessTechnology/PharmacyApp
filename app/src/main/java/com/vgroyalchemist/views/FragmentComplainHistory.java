package com.vgroyalchemist.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
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
import com.vgroyalchemist.vos.ComplainChatListVO;
import com.vgroyalchemist.vos.ServerResponseVO;
import com.vgroyalchemist.webservice.AddComplainService;
import com.vgroyalchemist.webservice.GetAllComplainListService;
import com.vgroyalchemist.webservice.PostParseGet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


/* developed by krishna 30-03-2021
 * complain history Details and Send Msg Replay */
public class FragmentComplainHistory extends NonCartFragment {

    //Variable Declaration
    public static final String FRAGMENT_ID = "53";

    public  MainActivity mainActivity;
    public View fragmentView = null;
    CommonHelper mCommonHelper;

    ImageView mImageViewBack;
    TextView mTextViewTitle;
    ImageView mImageViewSearch;
    ImageView mImageViewCart;

    RecyclerView mRecyclerViewMsgList;
    EditText mEditTextWriteMsg;
    ImageView mImageViewSend;
    LinearLayout mLinearLayoutMsg;

    ArrayList<ComplainChatListVO> mArrayListChat;

    Bundle mBundle;

    String mStringMedicineId;
    String mStringOrderId;
    String status;
    String mStringComplainId;

    // userdata
    public SharedPreferences mSharedPreferencesUserId;
    SharedPreferences.Editor mEditor;
    String mStringUserId;

    public SharedPreferences mPreferencesNotification;

    PostParseGet mPostParseGet;
    ServerResponseVO mServerResponseVO;
    ComplainHistoryAdapter complainHistoryAdapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainActivity = (MainActivity) getActivity();
        mCommonHelper = new CommonHelper();
        mPostParseGet = new PostParseGet(mainActivity);
        mBundle = getArguments();
        if (mBundle != null) {
            mStringMedicineId = mBundle.getString("Medicine_id");
            mStringOrderId = mBundle.getString("Order_id");
            status = mBundle.getString("status");
            mStringComplainId = mBundle.getString("complain_id");

        }
        mArrayListChat = new ArrayList<>();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentView = inflater.inflate(R.layout.fragment_complain_history, container, false);

        mImageViewBack = fragmentView.findViewById(R.id.fragment_imageview_back);
        mTextViewTitle = fragmentView.findViewById(R.id.fragment_title_textview);
        mTextViewTitle.setText("Complaint Details");
        mImageViewSearch = fragmentView.findViewById(R.id.fragment_Serach);
        mImageViewSearch.setVisibility(View.GONE);
        mImageViewCart = fragmentView.findViewById(R.id.fragment_cart);
        mImageViewCart.setVisibility(View.GONE);

        mRecyclerViewMsgList = fragmentView.findViewById(R.id.fragment_complain_history_listview_data);
        mEditTextWriteMsg = fragmentView.findViewById(R.id.fragment_complain_history_edit_message);
        mImageViewSend = fragmentView.findViewById(R.id.fragment_complain_history_tvsend);
        mLinearLayoutMsg = fragmentView.findViewById(R.id.fragment_complain_history_lnedtmessage);

        mSharedPreferencesUserId = mainActivity.getSharedPreferences(Tags.TAG_SHARED_PREFERENCE_USER_ID, 0);
        mStringUserId = mSharedPreferencesUserId.getString("UserId", "");
        mEditor = mSharedPreferencesUserId.edit();


        mPreferencesNotification = mainActivity.getSharedPreferences(Tags.TAG_SHARED_PREFERENCE_PUSH_NOTIFICATION_DATA, 0);
        mPreferencesNotification.edit().clear().apply();

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        mImageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.onBackPressed();
            }
        });

        if (status != null) {
            if (status.equalsIgnoreCase("Resolve")) {
                mLinearLayoutMsg.setVisibility(View.GONE);
            }
        }

        mImageViewSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (mEditTextWriteMsg.getText().toString().equalsIgnoreCase("")) {
                    Snackbar.with(mainActivity).text(getResources().getString(R.string.valid_complain)).show(mainActivity);
                    return;
                }

                AddComplain();
            }
        });
        GetAllComplainList();

        return fragmentView;
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
        mPreferencesNotification = mainActivity.getSharedPreferences(Tags.TAG_SHARED_PREFERENCE_PUSH_NOTIFICATION_DATA, 0);
        mPreferencesNotification.edit().clear().apply();
    }


    // complain medicine list
    public void GetAllComplainList() {
        getLoaderManager().restartLoader(0, null, new LoaderManager.LoaderCallbacks<TaskExecutor>() {
            @Override
            public Loader<TaskExecutor>onCreateLoader(int arg0, Bundle arg1) {

                GetAllComplainList complainMedicineList = new GetAllComplainList(mainActivity, null);
                return new LoaderTask<TaskExecutor>(mainActivity, complainMedicineList);
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

                        if (mServerResponseVO.getStatus().equalsIgnoreCase("true")) {
//                            Snackbar.with(mainActivity).text(mServerResponseVO.getMsg()).show(mainActivity);
                            mCommonHelper.hideKeyboard(mainActivity);

                            if (mServerResponseVO.getData() != null) {

                                mArrayListChat = (ArrayList<ComplainChatListVO>) mServerResponseVO.getData();

                                if (mArrayListChat != null && mArrayListChat.size() > 0) {

                                    LinearLayoutManager layoutManager = new LinearLayoutManager(mainActivity);
                                    layoutManager.setStackFromEnd(true);
                                     complainHistoryAdapter = new ComplainHistoryAdapter(mainActivity, mArrayListChat);
                                    mRecyclerViewMsgList.setHasFixedSize(true);
                                    mRecyclerViewMsgList.setLayoutManager(layoutManager);
                                    mRecyclerViewMsgList.setAdapter(complainHistoryAdapter);
                                    mRecyclerViewMsgList.scrollToPosition(complainHistoryAdapter.getItemCount() -1);

                                    mRecyclerViewMsgList.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                                        @Override
                                        public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                                            mRecyclerViewMsgList.smoothScrollToPosition(complainHistoryAdapter.getItemCount() -1);
                                        }
                                    });
                                }
                            }
                        } else {
                            Snackbar.with(mainActivity).text(mServerResponseVO.getMsg()).show(mainActivity);
                            mCommonHelper.hideKeyboard(mainActivity);
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
    public class GetAllComplainList extends TaskExecutor {
        protected GetAllComplainList(Context context, Bundle bundle) {
            super(context, bundle);
        }

        @Override
        protected Object executeTask() {

            GetAllComplainListService getAllOrderService = new GetAllComplainListService();
            mServerResponseVO = getAllOrderService.getallcomplainList(mainActivity, Tags.GetAllComplain, mStringUserId, mStringMedicineId, mStringComplainId);

            return null;
        }
    }


    public class ComplainHistoryAdapter extends RecyclerView.Adapter<ComplainHistoryAdapter.ViewHolderHistory> {

        Context mContext;
        ArrayList<ComplainChatListVO> mArrayList;



        public ComplainHistoryAdapter(MainActivity mainActivity, ArrayList<ComplainChatListVO> mArrayListChat) {
            this.mContext = mainActivity;
            this.mArrayList = mArrayListChat;
        }

        @NonNull
        @Override
        public ViewHolderHistory onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


            View rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_complain_chat_history,parent,false);
            ViewHolderHistory viewHolder = new ViewHolderHistory(rowView);


            return viewHolder;
        }


        @Override
        public void onBindViewHolder(@NonNull ViewHolderHistory holder, int position) {



            holder.mTextViewDate.setText(mArrayList.get(position).getDatestr());
            if (position > 0) {
                if (mArrayList.get(position).getDatestr().equalsIgnoreCase(mArrayList.get(position - 1).getDatestr())) {
                    holder.mTextViewDate.setVisibility(View.GONE);
                }
                else {
                    holder.mTextViewDate.setVisibility(View.VISIBLE);
                }
            }
            else {
                holder.mTextViewDate.setVisibility(View.VISIBLE);
            }

            if (mArrayList.get(position).getAddFrom().equalsIgnoreCase("User")) {
                holder.mTextViewUserDate.setVisibility(View.VISIBLE);
                holder.mTextViewUserMsg.setVisibility(View.VISIBLE);
                holder.mRelativeLayoutUser.setVisibility(View.VISIBLE);
                holder.mTextViewAdminmsg.setVisibility(View.GONE);
                holder.mTextViewAdminDate.setVisibility(View.GONE);
                holder.mRelativeLayoutAdmin.setVisibility(View.GONE);

                holder.mTextViewUserMsg.setText(mArrayList.get(position).getComplain());

                String mDate = mArrayList.get(position).getAddedDate();
                @SuppressLint("SimpleDateFormat") SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
                @SuppressLint("SimpleDateFormat") SimpleDateFormat fmt2 = new SimpleDateFormat("hh:mm a");


                try {
                    Date date = fmt.parse(mDate);
                    String mString = fmt2.format(date);


                    holder.mTextViewUserDate.setText(mString);
                } catch (ParseException pe) {
                    pe.printStackTrace();
                }

            } else {
                holder.mTextViewAdminmsg.setVisibility(View.VISIBLE);
                holder.mTextViewAdminDate.setVisibility(View.VISIBLE);
                holder.mRelativeLayoutAdmin.setVisibility(View.VISIBLE);
                holder.mTextViewUserDate.setVisibility(View.GONE);
                holder.mTextViewUserMsg.setVisibility(View.GONE);
                holder.mRelativeLayoutUser.setVisibility(View.GONE);


                holder.mTextViewAdminmsg.setText(mArrayList.get(position).getComplain());


                String mDate = mArrayList.get(position).getAddedDate();
                @SuppressLint("SimpleDateFormat") SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
                @SuppressLint("SimpleDateFormat") SimpleDateFormat fmt2 = new SimpleDateFormat("hh:mm a");
                try {
                    Date date = fmt.parse(mDate);
                    String mString = fmt2.format(date);
                    holder.mTextViewAdminDate.setText(mString);
                } catch (ParseException pe) {
                    pe.printStackTrace();
                }
            }
        }

        @Override
        public int getItemCount() {
            return mArrayList.size();
        }

        public class ViewHolderHistory extends RecyclerView.ViewHolder {
            TextView mTextViewUserMsg;
            TextView mTextViewUserDate;
            TextView mTextViewAdminmsg;
            TextView mTextViewAdminDate;
            RelativeLayout mRelativeLayoutUser;
            RelativeLayout mRelativeLayoutAdmin;
            TextView mTextViewDate;

            public ViewHolderHistory(View itemView) {
                super(itemView);

                mTextViewUserMsg = (TextView) itemView.findViewById(R.id.row_complain_chat_history_user_Msg);
                mTextViewUserDate = (TextView) itemView.findViewById(R.id.row_complain_chat_history_user_textview_date);
                mTextViewAdminmsg = (TextView) itemView.findViewById(R.id.row_complain_chat_history_textview_admin);
                mTextViewAdminDate = (TextView) itemView.findViewById(R.id.row_complain_chat_history_textview_date);
                mRelativeLayoutUser = (RelativeLayout) itemView.findViewById(R.id.row_complain_chat_history_user);
                mRelativeLayoutAdmin = (RelativeLayout) itemView.findViewById(R.id.row_complain_chat_history_admin);
                mTextViewDate = (TextView) itemView.findViewById(R.id.row_complain_chat_textview_date);

            }
        }
//        public class ViewHolderDate extends RecyclerView.ViewHolder {
//
//            TextView mTextViewDate;
//            public ViewHolderDate(@NonNull View itemView) {
//                super(itemView);
//                mTextViewDate = (TextView) itemView.findViewById(R.id.row_complain_chat_textview_date);
//            }
//        }
    }

//     Send some Msg
    public void AddComplain() {
        getLoaderManager().restartLoader(0, mBundle, new LoaderManager.LoaderCallbacks<TaskExecutor>() {
            @Override
            public Loader<TaskExecutor> onCreateLoader(int arg0, Bundle arg1) {

                AddMedicineComplain mAddReviewexecutor = new AddMedicineComplain(mainActivity, null);
                return new LoaderTaskWithoutProgressDialog<TaskExecutor>(mainActivity, mAddReviewexecutor);
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

                        if (mServerResponseVO != null && mServerResponseVO.getStatus() != null) {

                            if (mServerResponseVO.getStatus().equalsIgnoreCase("true")) {
//                                Snackbar.with(mainActivity).text(mServerResponseVO.getMsg()).show(mainActivity);

                                mEditTextWriteMsg.setText("");
                                GetAllComplainList();
                            }
                            else {
                                Snackbar.with(mainActivity).text(mServerResponseVO.getMsg()).show(mainActivity);

                            }
                        }
                        else {
                            Snackbar.with(mainActivity).text(mServerResponseVO.getMsg()).show(mainActivity);
                        }
                    }
                }
            }

            @Override
            public void onLoaderReset(Loader<TaskExecutor> arg0) {

            }
        });


    }


    public class AddMedicineComplain extends TaskExecutor {

        protected AddMedicineComplain(Context context, Bundle bundle) {
            super(context, bundle);
        }

        @Override
        protected Object executeTask() {
            try {
                AddComplainService addReviewService = new AddComplainService();
                mServerResponseVO = addReviewService.AddReview(mainActivity, Tags.AddComplain, mStringMedicineId, mStringUserId, mStringOrderId, mEditTextWriteMsg.getText().toString().trim(), "Open", mStringComplainId, "1");

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

}