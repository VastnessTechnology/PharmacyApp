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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.vgroyalchemist.R;
import com.vgroyalchemist.controller.MainActivity;
import com.vgroyalchemist.loaders.LoaderTask;
import com.vgroyalchemist.loaders.TaskExecutor;
import com.vgroyalchemist.utils.CommonHelper;
import com.vgroyalchemist.utils.Snackbar;
import com.vgroyalchemist.utils.Tags;
import com.vgroyalchemist.vos.AllArticlesListVo;
import com.vgroyalchemist.vos.ComplainMedicineListVO;
import com.vgroyalchemist.vos.OrderDataVo;
import com.vgroyalchemist.vos.ServerResponseVO;
import com.vgroyalchemist.webservice.GetAllComplainMedicineService;
import com.vgroyalchemist.webservice.GetAllOrderService;
import com.vgroyalchemist.webservice.PostParseGet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/* developed by krishna 30-03-2021
 * complain medicine item list  */
public class FragmentComplainList extends NonCartFragment {

    //Variable Declaration
    public static final String FRAGMENT_ID = "52";

    public static MainActivity mainActivity;
    public View fragmentView = null;
    CommonHelper mCommonHelper;

    ImageView mImageViewBack;
    TextView mTextViewTitle;
    ImageView mImageViewSearch;
    ImageView mImageViewCart;

    RecyclerView mRecyclerView;
    LinearLayout mLinearLayoutEmpty;

    ArrayList<ComplainMedicineListVO> mArrayListItemName;

    // userdata
    public SharedPreferences mSharedPreferencesUserId;
    SharedPreferences.Editor mEditor;
    String mStringUserId;

    PostParseGet mPostParseGet;
    ServerResponseVO mServerResponseVO;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = (MainActivity) getActivity();
        mCommonHelper = new CommonHelper();
        mPostParseGet = new PostParseGet(mainActivity);
        mArrayListItemName = new ArrayList<ComplainMedicineListVO>();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentView = inflater.inflate(R.layout.fragment_complain_list, container, false);

        mImageViewBack = fragmentView.findViewById(R.id.fragment_imageview_back);
        mTextViewTitle =fragmentView.findViewById(R.id.fragment_title_textview);
        mTextViewTitle.setText("Complaint Item List");
        mImageViewSearch =fragmentView.findViewById(R.id.fragment_Serach);
        mImageViewSearch.setVisibility(View.GONE);
        mImageViewCart =fragmentView.findViewById(R.id.fragment_cart);
        mImageViewCart.setVisibility(View.GONE);

        mRecyclerView = fragmentView.findViewById(R.id.fragment_complain_List_listview_data);
        mLinearLayoutEmpty = fragmentView.findViewById(R.id.fragment_complain_List_relative_empty);


        mSharedPreferencesUserId = mainActivity.getSharedPreferences(Tags.TAG_SHARED_PREFERENCE_USER_ID, 0);
        mStringUserId = mSharedPreferencesUserId.getString("UserId", "");
        mEditor = mSharedPreferencesUserId.edit();


        mImageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.onBackPressed();
            }
        });

        ComplainMedicieList();
        return fragmentView;
    }

    public class ComplainItemListAdapter extends RecyclerView.Adapter<ComplainItemListAdapter.ViewHolderList> {

        Context mContext;
        ArrayList<ComplainMedicineListVO> mArrayList;
        public ComplainItemListAdapter(MainActivity mainActivity, ArrayList<ComplainMedicineListVO> mArrayListItemName) {
            this.mContext = mainActivity;
            this.mArrayList =mArrayListItemName;
        }

        @NonNull
        @Override
        public ViewHolderList onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_complain_list, parent, false);
            ViewHolderList vh = new ViewHolderList(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolderList holder, int position) {

            if(!mArrayList.get(position).getMedicineName().equalsIgnoreCase("") && mArrayList.get(position).getMedicineName() != null)
            {
                holder.mTextViewMedicineName.setText("MedicineName : " + mArrayList.get(position).getMedicineName());
            }


            if(!mArrayList.get(position).getOrderNo().equalsIgnoreCase("") && mArrayList.get(position).getOrderNo() != null)
            {
                holder.mTextViewOrderNo.setText("Order No : " + mArrayList.get(position).getOrderNo());
            }

            if(mArrayList.get(position).getStatus() != null && !mArrayList.get(position).getStatus().equalsIgnoreCase(""))
            {
                holder.mTextViewStatus.setText(mArrayList.get(position).getStatus());
            }

            if(mArrayList.get(position).getAddedDate() != null)
            {
                String mDate = mArrayList.get(position).getAddedDate();
                SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat fmt2 = new SimpleDateFormat("MMM, d, yyyy");
                try {
                    Date date = fmt.parse(mDate);
                    String mString = fmt2.format(date);
                  holder.mTextViewDate.setText(mString);
                } catch (ParseException pe) {
                    pe.printStackTrace();
                }
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    FragmentComplainHistory fragmentComplainHistory = new FragmentComplainHistory();
                    Bundle mBundle = new Bundle();
                    mBundle.putString("Medicine_id",mArrayList.get(position).getMedicineId());
                    mBundle.putString("Order_id",mArrayList.get(position).getOrderId());
                    mBundle.putString("status",mArrayList.get(position).getStatus());
                    mBundle.putString("complain_id",mArrayList.get(position).getComplainId());

                    fragmentComplainHistory.setArguments(mBundle);
                    mainActivity.addFragment(fragmentComplainHistory, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, fragmentComplainHistory.getClass().getName());
                }
            });
        }
        @Override
        public int getItemCount() {
            return mArrayList.size();
        }

        public class ViewHolderList extends RecyclerView.ViewHolder {
            TextView mTextViewMedicineName;
            TextView mTextViewStatus;
            TextView mTextViewDate;
            TextView mTextViewOrderNo;

            public ViewHolderList(View itemView) {
                super(itemView);

                mTextViewMedicineName = (TextView) itemView.findViewById(R.id.row_complain_list_textview_name);
                mTextViewStatus = (TextView) itemView.findViewById(R.id.row_complain_list_textview_status_name);
                mTextViewDate = (TextView)itemView.findViewById(R.id.row_complain_list_textview_date);
                mTextViewOrderNo =(TextView)itemView.findViewById(R.id.row_complain_list_textview_order_id);
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


    public void ComplainMedicieList() {
        getLoaderManager().restartLoader(0, null, new LoaderManager.LoaderCallbacks<TaskExecutor>() {
            @Override
            public Loader<TaskExecutor> onCreateLoader(int arg0, Bundle arg1) {

                ComplainMedicineList complainMedicineList = new ComplainMedicineList(mainActivity, null);
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
                        try {


                        if (mServerResponseVO.getStatus().equalsIgnoreCase("true")) {
//                            Snackbar.with(mainActivity).text(mServerResponseVO.getMsg()).show(mainActivity);
                            mCommonHelper.hideKeyboard(mainActivity);

                            if (mServerResponseVO.getData() != null) {

                                mArrayListItemName = (ArrayList<ComplainMedicineListVO>) mServerResponseVO.getData();

                                if (mArrayListItemName != null && mArrayListItemName.size() > 0) {
                                    mLinearLayoutEmpty.setVisibility(View.GONE);
                                    mRecyclerView.setVisibility(View.VISIBLE);



                                    ComplainItemListAdapter complainItemListAdapter = new ComplainItemListAdapter(mainActivity, mArrayListItemName);
                                    mRecyclerView.setHasFixedSize(true);
                                    mRecyclerView.setLayoutManager(new LinearLayoutManager(mainActivity));
                                    mRecyclerView.setAdapter(complainItemListAdapter);

                                } else {
                                    mLinearLayoutEmpty.setVisibility(View.VISIBLE);
                                    mRecyclerView.setVisibility(View.GONE);


                                }
                            }
                        } else {
                            Snackbar.with(mainActivity).text(mServerResponseVO.getMsg()).show(mainActivity);
                            mCommonHelper.hideKeyboard(mainActivity);
                        }
                        }
                        catch (Exception e) {
                            e.printStackTrace();
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
    public class ComplainMedicineList extends TaskExecutor {
        protected ComplainMedicineList(Context context, Bundle bundle) {
            super(context, bundle);
        }

        @Override
        protected Object executeTask() {

            GetAllComplainMedicineService getAllOrderService = new GetAllComplainMedicineService();
            mServerResponseVO = getAllOrderService.getallMedicineList(mainActivity, Tags.GetAllComplainMedicine, mStringUserId);

            return null;
        }
    }

}