package com.vgroyalchemist.views;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
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
import android.view.ViewTreeObserver;
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
import com.vgroyalchemist.vos.GetAllReviewsVO;
import com.vgroyalchemist.vos.OrderDataVo;
import com.vgroyalchemist.vos.ServerResponseVO;
import com.vgroyalchemist.webservice.GetAllOrderService;
import com.vgroyalchemist.webservice.GetAllReviewService;
import com.vgroyalchemist.webservice.PostParseGet;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/* developed by krishna 24-03-2021
*  list of all Rating & Reviews */
public class FragmentRatingReviewsDetails extends NonCartFragment {

    //Variable Declaration
    public static final String FRAGMENT_ID = "33";

    public static MainActivity mainActivity;
    public View fragmentView = null;
    CommonHelper mCommonHelper;

    ImageView mImageViewBack;
    TextView mTextViewTitle;
    ImageView mImageViewSearch;
    ImageView mImageViewCart;
    TextView mTextViewMedicineName;
    TextView mTextViewRatingAvg;
    TextView mTextViewTotalRatingAvg;
    TextView mTextViewTotalCustomerRating;
    TextView mTextViewTitleRating;
    RecyclerView mRecyclerViewRating;

    ArrayList<GetAllReviewsVO> mArrayListReview;

    Bundle mBundle;
    String mStringMedicineId;
    String mStringAvgRating;
    String mStringTotalReview;
    String mStringTotalRating;
    String mStringMedicineName;
    PostParseGet mPostParseGet;
    ServerResponseVO mServerResponseVO;
    boolean expandable;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = (MainActivity) getActivity();
        mCommonHelper = new CommonHelper();
        mPostParseGet = new PostParseGet(mainActivity);
        mArrayListReview = new ArrayList<GetAllReviewsVO>();


        mBundle = getArguments();
        if (mBundle != null) {
            mStringMedicineId = mBundle.getString("MedicineID");
            mStringAvgRating = mBundle.getString("AvgRting");
            mStringTotalReview = mBundle.getString("totalReview");
            mStringTotalRating = mBundle.getString("totalRating");
            mStringMedicineName = mBundle.getString("medicineName");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentView = inflater.inflate(R.layout.fragment_rating_reviews_details, container, false);

        mImageViewBack = fragmentView.findViewById(R.id.fragment_imageview_back);
        mTextViewTitle = fragmentView.findViewById(R.id.fragment_title_textview);
        mTextViewTitle.setText("Review Details");
        mImageViewSearch = fragmentView.findViewById(R.id.fragment_Serach);
        mImageViewSearch.setVisibility(View.GONE);
        mImageViewCart = fragmentView.findViewById(R.id.fragment_cart);
        mImageViewCart.setVisibility(View.GONE);
        mTextViewMedicineName = fragmentView.findViewById(R.id.fragment_rating_reviews_details_textview_name);
        mTextViewRatingAvg = fragmentView.findViewById(R.id.fragment_rating_reviews_details_textview_star);
//        mTextViewTotalRatingAvg = fragmentView.findViewById(R.id.fragment_rating_reviews_details_textview_star);
        mTextViewTotalCustomerRating = fragmentView.findViewById(R.id.fragment_rating_reviews_details_textview_total_customers);
        mRecyclerViewRating = fragmentView.findViewById(R.id.fragment_rating_reviews_details_recycleview);
        mTextViewTitleRating =fragmentView.findViewById(R.id.fragment_rating_reviews_title_textview);

        if(mStringAvgRating != null)
        {
            mTextViewRatingAvg.setText((mStringAvgRating));
        }
        if(mStringTotalReview != null)
        {
            mTextViewTotalCustomerRating.setText( getResources().getString(R.string.fromcustomers).replace("%1$s", getString(R.string.dynamic_val, mStringTotalReview)));
        }
//        if(mStringTotalRating != null)
//        {
//            mTextViewTotalRatingAvg.setText(mStringTotalRating);
//        }
        if(mStringMedicineName != null)
        {
            mTextViewMedicineName.setText(mStringMedicineName);
        }
        mImageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mainActivity.onBackPressed();
            }
        });
        GetAllOrderData();

        return fragmentView;
    }


    public void GetAllOrderData() {

        getLoaderManager().restartLoader(0, null, new LoaderManager.LoaderCallbacks<TaskExecutor>() {
            @Override
            public Loader<TaskExecutor> onCreateLoader(int arg0, Bundle arg1) {

                GetAllReviewList mGetAllReviewList = new GetAllReviewList(mainActivity, null);
                return new LoaderTask<TaskExecutor>(mainActivity, mGetAllReviewList);
            }

            @Override
            public void onLoadFinished(Loader<TaskExecutor> arg0, TaskExecutor arg1) {

                if (isAdded()) {
                    getLoaderManager().destroyLoader(0);
                    try {
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

                                    mArrayListReview = (ArrayList<GetAllReviewsVO>) mServerResponseVO.getData();

                                    if (mArrayListReview != null && mArrayListReview.size() > 0) {

                                        mTextViewTitleRating.setText("All Reviews " + "(" + mArrayListReview.size() + ")");
                                        ReviewListAdapter orderItemAdpater = new ReviewListAdapter(mainActivity, mArrayListReview);
                                        mRecyclerViewRating.setHasFixedSize(true);
                                        mRecyclerViewRating.setLayoutManager(new LinearLayoutManager(mainActivity));
                                        mRecyclerViewRating.setAdapter(orderItemAdpater);

                                    }
                                }
                            } else {
                                Snackbar.with(mainActivity).text(mServerResponseVO.getMsg()).show(mainActivity);
                                mCommonHelper.hideKeyboard(mainActivity);
                            }
                        }
                    }
                    catch (Exception e)
                    {

                    }
                }
            }

            @Override
            public void onLoaderReset(Loader<TaskExecutor> arg0) {

            }
        });
    }


    @SuppressLint("RestrictedApi")
    public class GetAllReviewList extends TaskExecutor {
        protected GetAllReviewList(Context context, Bundle bundle) {
            super(context, bundle);
        }

        @Override
        protected Object executeTask() {

            GetAllReviewService getAllOrderService = new GetAllReviewService();
            mServerResponseVO = getAllOrderService.getAllreviews(mainActivity, Tags.GETALLREVIEW, mStringMedicineId);

            return null;
        }
    }


    public class ReviewListAdapter extends RecyclerView.Adapter<ReviewListAdapter.MyViewHolderReviewList> {


        Context mContext;
        ArrayList<GetAllReviewsVO> mArrayListitem;

        public ReviewListAdapter(MainActivity mainActivity, ArrayList<GetAllReviewsVO> mArrayListReview) {
            this.mContext = mainActivity;
            this.mArrayListitem = mArrayListReview;
        }

        @NonNull
        @Override
        public MyViewHolderReviewList onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_rating_review, parent, false);
            MyViewHolderReviewList viewHolderOrderItem = new MyViewHolderReviewList(v);
            return viewHolderOrderItem;
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolderReviewList holder, int position) {


            holder.mTextViewName.setText(mArrayListitem.get(position).getUserName());
            holder.mTextViewRatingAvg.setText(mArrayListitem.get(position).getReview());

            if (mArrayListitem.get(position).getReview().equalsIgnoreCase("1")) {
                holder.mRelativeLayoutRating.setBackgroundColor(mainActivity.getResources().getColor(R.color.red_color));
            } else if (mArrayListitem.get(position).getReview().equalsIgnoreCase("2")) {
                holder.mRelativeLayoutRating.setBackgroundColor(mainActivity.getResources().getColor(R.color.color_cart));
            } else if (Integer.valueOf(mArrayListitem.get(position).getReview()) >= 3) {
                holder.mRelativeLayoutRating.setBackgroundColor(mainActivity.getResources().getColor(R.color.dark_green));
            }

            if(mArrayListitem.get(position).getReviewText() != null) {
                holder.mTextViewRatingMsg.setText(mArrayListitem.get(position).getReviewText());
            }

            holder.mTextViewLess.setText("Less");
            holder.mTextViewMore.setText("More");

            holder.mTextViewRatingMsg.setTag(position);
            holder.mTextViewMore.setTag(position);
            holder.mTextViewLess.setTag(position);

            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
            String mDate = mArrayListitem.get(position).getReviewDate();

            Date newDate = null;
            try {
                newDate = fmt.parse(mDate);
                fmt = new SimpleDateFormat("MMM dd, yyyy");
                String date = fmt.format(newDate);
                holder.mTextViewDate.setText(date);
                Utility.debugger("VT date...." + date);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            holder.mTextViewRatingMsg.post(new Runnable() {
                @Override
                public void run() {
                    int lineCnt = holder.mTextViewRatingMsg.getLineCount();
                    if (lineCnt >= 3) {
                        holder.mTextViewMore.setVisibility(View.VISIBLE);
                    } else {
                        holder.mTextViewMore.setVisibility(View.GONE);
                    }

                    if (holder.mTextViewLess.getVisibility() == View.VISIBLE) {
                        holder.mTextViewMore.setVisibility(View.GONE);
                    }
                }
            });
            holder.mTextViewRatingMsg.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    if (expandable) {
                        expandable = false;
                        if (holder.mTextViewRatingMsg.getLineCount() >= 3) {
                            holder.mTextViewMore.setVisibility(View.VISIBLE);
                            ObjectAnimator animation = ObjectAnimator.ofInt(holder.mTextViewRatingMsg, "maxLines", 3);
                            animation.setDuration(0).start();
                        }
                    }
                }
            });

            holder.mTextViewMore.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    holder.mTextViewMore.setVisibility(View.GONE);
                    holder.mTextViewLess.setVisibility(View.VISIBLE);
                    holder.mTextViewRatingMsg.setMaxLines(Integer.MAX_VALUE);
                }
            });

            holder.mTextViewLess.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    holder.mTextViewLess.setVisibility(View.GONE);
                    holder.mTextViewMore.setVisibility(View.VISIBLE);
                    holder.mTextViewRatingMsg.setMaxLines(3);
                }
            });

        }


        @Override
        public int getItemCount() {
            return mArrayListitem.size();
        }

        public class MyViewHolderReviewList extends RecyclerView.ViewHolder {

            TextView mTextViewName;
            TextView mTextViewRatingAvg;
            TextView mTextViewRatingMsg;
            TextView mTextViewMore;
            TextView mTextViewLess;
            TextView mTextViewDate;
            RelativeLayout mRelativeLayoutRating;


            public MyViewHolderReviewList(View itemView) {
                super(itemView);

                mTextViewName = itemView.findViewById(R.id.row_rating_review_textview_title_uesrname);
                mTextViewRatingAvg = itemView.findViewById(R.id.row_rating_review_textview_start);
                mTextViewRatingMsg = itemView.findViewById(R.id.row_rating_review_textview_msg);
                mTextViewMore = itemView.findViewById(R.id.row_rating_review_textview_more);
                mTextViewLess = itemView.findViewById(R.id.row_rating_review_textview_less);
                mTextViewDate = itemView.findViewById(R.id.row_rating_review_textview_date);
                mRelativeLayoutRating =itemView.findViewById(R.id.row_rating_review_star);
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
}