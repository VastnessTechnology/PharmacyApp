package com.vgroyalchemist.views;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.vgroyalchemist.R;
import com.vgroyalchemist.controller.MainActivity;
import com.vgroyalchemist.loaders.LoaderTask;
import com.vgroyalchemist.loaders.TaskExecutor;
import com.vgroyalchemist.utils.CommonHelper;
import com.vgroyalchemist.utils.Snackbar;
import com.vgroyalchemist.utils.Tags;
import com.vgroyalchemist.utils.Utility;
import com.vgroyalchemist.vos.ServerResponseVO;
import com.vgroyalchemist.webservice.AddDeliveryFeedbackService;
import com.vgroyalchemist.webservice.AddReviewService;
import com.vgroyalchemist.webservice.PostParseGet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/* developed by krishna 24-03-2021
*   feedback delivery */
public class FragmentFeedbackDelivery extends NonCartFragment {

    //Variable Declaration
    public static final String FRAGMENT_ID = "34";

    public static MainActivity mainActivity;
    public View fragmentView = null;
    CommonHelper mCommonHelper;

    ImageView mImageViewBack;
    TextView mTextViewTitle;
    ImageView mImageViewSearch;
    ImageView mImageViewCart;

    TextView mTextViewDate;
    TextView mTextViewStatus;
    RatingBar mRatingBar;
    EditText mEditTextWriteMsg;
    TextView mTextViewSubmit;

    // userdata
    public SharedPreferences mSharedPreferencesUserId;
    SharedPreferences.Editor mEditor;
    String mStringUserId;
    String OrderStaus;
    Bundle mBundle;
    ServerResponseVO mServerResponseVO;
    PostParseGet mPostParseGet;
    String mStringReviewText;
    String mStringRating;
    String mStringOrderId;
    String mStringDeliveryDate;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = (MainActivity) getActivity();
        mCommonHelper = new CommonHelper();
        mPostParseGet =new PostParseGet(mainActivity);
        mBundle = getArguments();
        if(mBundle != null)
        {
            OrderStaus = mBundle.getString("Orderstatus");
            mStringOrderId = mBundle.getString("orderId");
            mStringDeliveryDate = mBundle.getString("deliveryDate");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentView = inflater.inflate(R.layout.fragment_feedback_delivery, container, false);

        mImageViewBack = fragmentView.findViewById(R.id.fragment_imageview_back);
        mTextViewTitle =fragmentView.findViewById(R.id.fragment_title_textview);
        mTextViewTitle.setText("Delivery Feedback");
        mImageViewSearch =fragmentView.findViewById(R.id.fragment_Serach);
        mImageViewSearch.setVisibility(View.GONE);
        mImageViewCart =fragmentView.findViewById(R.id.fragment_cart);
        mImageViewCart.setVisibility(View.GONE);

        mTextViewDate= fragmentView.findViewById(R.id.fragment_feedback_delivery_textview_Date);
        mTextViewStatus =fragmentView.findViewById(R.id.fragment_feedback_delivery_textview_status);
        mRatingBar =fragmentView.findViewById(R.id.fragment_feedback_delivery_ratingbar);
        mEditTextWriteMsg =fragmentView.findViewById(R.id.fragment_feedback_delivery_edittext_review);
        mTextViewSubmit =fragmentView.findViewById(R.id.fragment_feedback_delivery_textview_submit);

        mSharedPreferencesUserId = mainActivity.getSharedPreferences(Tags.TAG_SHARED_PREFERENCE_USER_ID, 0);
        mStringUserId = mSharedPreferencesUserId.getString("UserId", "");
        mEditor = mSharedPreferencesUserId.edit();

        if(OrderStaus != null)
        {
            mTextViewStatus.setText(OrderStaus);
        }

        if(mStringDeliveryDate != null)
        {

            String mDate = mStringDeliveryDate;
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat fmt2 = new SimpleDateFormat("EEE, MMM, d, yyyy");
            try {
                Date date = fmt.parse(mDate);
                String mString = fmt2.format(date);
                mTextViewDate.setText(getResources().getString(R.string.delivery_text) + " " + mString);
            } catch (ParseException pe) {
                pe.printStackTrace();
            }

        }


        mImageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.onBackPressed();
            }
        });

        mTextViewSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mStringRating = String.valueOf(Math.round(mRatingBar.getRating()));
                Utility.debugger("Vt rating......" + mStringRating);
                mStringReviewText = mEditTextWriteMsg.getText().toString().trim();

                if(mStringRating.equals("0")) {
                    Snackbar.with(mainActivity).text("Please Select Rating").show(mainActivity);
                    return;
                }

                AddDeliveryReview();
            }
        });

        return fragmentView;

    }


//     Rating Review

    public void AddDeliveryReview() {
        getLoaderManager().restartLoader(0, mBundle, new LoaderManager.LoaderCallbacks<TaskExecutor>() {
            @Override
            public Loader<TaskExecutor> onCreateLoader(int arg0, Bundle arg1) {

                AddDeliveryexecutor mAddDeliveryexecutor = new AddDeliveryexecutor(mainActivity, null);
                return new LoaderTask<TaskExecutor>(mainActivity, mAddDeliveryexecutor);
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
                                Snackbar.with(mainActivity).text(mServerResponseVO.getMsg()).show(mainActivity);
                                mainActivity.onBackPressed();

                            }
                        } else {
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



    public class AddDeliveryexecutor extends TaskExecutor {

        protected AddDeliveryexecutor(Context context, Bundle bundle) {
            super(context, bundle);
        }

        @Override
        protected Object executeTask() {
            try {
                AddDeliveryFeedbackService addReviewService = new AddDeliveryFeedbackService();
                mServerResponseVO = addReviewService.AddDeliveryfeedback(mainActivity, Tags.DeLIVERYFEEDBACK, mStringOrderId ,mStringReviewText ,mStringRating);

            } catch (Exception e) {
                e.printStackTrace();
            }
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

}