package com.vgroyalchemist.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vgroyalchemist.R;
import com.vgroyalchemist.controller.MainActivity;
import com.vgroyalchemist.loaders.LoaderTask;
import com.vgroyalchemist.loaders.TaskExecutor;
import com.vgroyalchemist.utils.CommonHelper;
import com.vgroyalchemist.utils.Snackbar;
import com.vgroyalchemist.utils.Tags;
import com.vgroyalchemist.vos.MYPrescriptionVO;
import com.vgroyalchemist.vos.OrderDataVo;
import com.vgroyalchemist.vos.PresOrderList;
import com.vgroyalchemist.vos.ServerResponseVO;
import com.vgroyalchemist.webservice.MyPrescirptionListService;
import com.vgroyalchemist.webservice.PostParseGet;
import com.vgroyalchemist.webservice.PresOrderListService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.facebook.FacebookSdk.getApplicationContext;

/*  developed by krishna 01-06-2021
 *  List of orer with presciption  */
public class FragmentOrderWithPresciption extends NonCartFragment {


    public static final String FRAGMENT_ID = "58";

    public static MainActivity mainActivity;
    CommonHelper mCommonHelper;
    View fragmnetview = null;
    ImageView mImageViewBack;
    TextView mTextViewTitle;
    ImageView mImageViewSearch;
    ImageView mImageViewCart;

    RecyclerView mRecyclerViewPresctiptionOrder;
    TextView mTextViewNodata;

    ArrayList<PresOrderList> mArrayListOrderList;

    PostParseGet mPostParseGet;
    ServerResponseVO mServerResponseVO;

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

        mArrayListOrderList= new ArrayList<PresOrderList>();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmnetview = inflater.inflate(R.layout.fragment_order_with_presciption, container, false);

        mImageViewBack = fragmnetview.findViewById(R.id.fragment_imageview_back);
        mTextViewTitle = fragmnetview.findViewById(R.id.fragment_title_textview);
        mTextViewTitle.setText("Order With Prescription");
        mImageViewSearch = fragmnetview.findViewById(R.id.fragment_Serach);
        mImageViewSearch.setVisibility(View.GONE);
        mImageViewCart = fragmnetview.findViewById(R.id.fragment_cart);
        mImageViewCart.setVisibility(View.GONE);

        mSharedPreferencesUserId = mainActivity.getSharedPreferences(Tags.TAG_SHARED_PREFERENCE_USER_ID, 0);
        mStringUserId = mSharedPreferencesUserId.getString("UserId", "");
        mEditor = mSharedPreferencesUserId.edit();

        mRecyclerViewPresctiptionOrder = fragmnetview.findViewById(R.id.fragment_prescription_orderList_recycleview);
        mTextViewNodata = fragmnetview.findViewById(R.id.fragment_prescription_orderList_textview_nodata);

        mImageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.onBackPressed();
            }
        });


        GetOrderWithPresList();
        return fragmnetview;
    }


    public static class PresciptionOrderListData extends RecyclerView.Adapter<PresciptionOrderListData.MyViewHolderOrderPresc> {


        Context mContext;
        ArrayList<PresOrderList> mArrayListorder;

        public PresciptionOrderListData(MainActivity mainActivity, ArrayList<PresOrderList> mArrayListOrderList) {

            this.mContext = mainActivity;
            this.mArrayListorder = mArrayListOrderList;
        }

        @NonNull
        @Override
        public MyViewHolderOrderPresc onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_presciption_orderlist, parent, false);
            MyViewHolderOrderPresc holderOrder = new MyViewHolderOrderPresc(v);
            return holderOrder;
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolderOrderPresc holder, @SuppressLint("RecyclerView") int position) {

//
            if (!mArrayListorder.get(position).getDate().equalsIgnoreCase("null")) {

                String mDate = mArrayListorder.get(position).getDate();
                SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat fmt2 = new SimpleDateFormat("EEE, MMM, dd, yyyy");
                try {
                    Date date = fmt.parse(mDate);
                    String mString = fmt2.format(date);
                    holder.mTextViewDate.setText(mString);
                } catch (ParseException pe) {
                    pe.printStackTrace();
                }
            }

            if (!mArrayListorder.get(position).getOrderStatus().equalsIgnoreCase("null"))
            {

                holder.mTextViewStatus.setText(mArrayListorder.get(position).getOrderStatus());
            }

//            if (!mArrayListorder.get(position).getUserName().equalsIgnoreCase("null"))
//            {
//
//                holder.mTextViewUsername.setText(mArrayListorder.get(position).getUserName());
//            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    FragmentMyUploadPrescriptionOrderList  uploadPrescriptionOrderList = new FragmentMyUploadPrescriptionOrderList();
                    Bundle mBundle = new Bundle();
                    mBundle.putString("PrescirptionId" , mArrayListorder.get(position).getSrNo());
                    uploadPrescriptionOrderList.setArguments(mBundle);
                    mainActivity.addFragment(uploadPrescriptionOrderList, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, uploadPrescriptionOrderList.getClass().getName());
                }
            });

        }


        @Override
        public int getItemCount() {
            return mArrayListorder.size();
        }

        public static class MyViewHolderOrderPresc extends RecyclerView.ViewHolder {


                TextView mTextViewDate;
                TextView mTextViewStatus;
                TextView mTextViewViewImage;
                TextView mTextViewUsername;

            public MyViewHolderOrderPresc(View itemView) {
                super(itemView);

                mTextViewDate = itemView.findViewById(R.id.row_pres_order_textview_order_date_value);
                mTextViewStatus = itemView.findViewById(R.id.row_pres_order_textview_status_value);
                mTextViewViewImage = itemView.findViewById(R.id.row_pres_order_textview_view_image);
                mTextViewUsername = itemView.findViewById(R.id.row_pres_order_textview_name);

            }
        }
    }

    public void GetOrderWithPresList() {

        getLoaderManager().restartLoader(0, null, new LoaderManager.LoaderCallbacks<TaskExecutor>() {
            @Override
            public Loader<TaskExecutor> onCreateLoader(int arg0, Bundle arg1) {

                GetOrderWithPresListExecutor getPresciptionListExecutor = new GetOrderWithPresListExecutor(mainActivity, null);
                return new LoaderTask<TaskExecutor>(mainActivity, getPresciptionListExecutor);
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

                                    mArrayListOrderList = (ArrayList<PresOrderList>) mServerResponseVO.getData();

                                    mRecyclerViewPresctiptionOrder.setVisibility(View.VISIBLE);
                                    mTextViewNodata.setVisibility(View.GONE);
//
                                    if (mArrayListOrderList != null) {
                                        PresciptionOrderListData   orderListData = new PresciptionOrderListData(mainActivity, mArrayListOrderList);
                                        mRecyclerViewPresctiptionOrder.setHasFixedSize(true);
                                        mRecyclerViewPresctiptionOrder.setLayoutManager(new LinearLayoutManager(mainActivity));
                                        mRecyclerViewPresctiptionOrder.setAdapter(orderListData);

                                    }
                                }
                            } else {
                                mTextViewNodata.setVisibility(View.VISIBLE);
                                mRecyclerViewPresctiptionOrder.setVisibility(View.GONE);

//                                Snackbar.with(mainActivity).text(mServerResponseVO.getMsg()).show(mainActivity);
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
    public class GetOrderWithPresListExecutor extends TaskExecutor {
        protected GetOrderWithPresListExecutor(Context context, Bundle bundle) {
            super(context, bundle);
        }

        @Override
        protected Object executeTask() {

            PresOrderListService presOrderListService = new PresOrderListService();
            mServerResponseVO = presOrderListService.GetPresciptionOrderList(mainActivity, Tags.GetAllOrderPrecription, mStringUserId);

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