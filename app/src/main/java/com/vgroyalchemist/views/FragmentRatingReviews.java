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

import com.bumptech.glide.Glide;
import com.vgroyalchemist.R;
import com.vgroyalchemist.controller.MainActivity;
import com.vgroyalchemist.loaders.LoaderTask;
import com.vgroyalchemist.loaders.TaskExecutor;
import com.vgroyalchemist.utils.CommonHelper;
import com.vgroyalchemist.utils.Snackbar;
import com.vgroyalchemist.utils.Tags;
import com.vgroyalchemist.utils.Utility;
import com.vgroyalchemist.vos.ServerResponseVO;
import com.vgroyalchemist.webservice.AddReviewService;
import com.vgroyalchemist.webservice.PostParseGet;
import com.vgroyalchemist.webservice.ReOrderService;

/* developed by krishna 24-03-2021
* Add rating and reviews */
public class FragmentRatingReviews extends NonCartFragment {

    //Variable Declaration
    public static final String FRAGMENT_ID = "31";

    public static MainActivity mainActivity;
    public View fragmentView = null;
    CommonHelper mCommonHelper;

    ImageView mImageViewBack;
    TextView mTextViewTitle;
    ImageView mImageViewSearch;
    ImageView mImageViewCart;
    ImageView mImageViewMedicineImage;
    TextView mTextViewMedicineNAme;
    RatingBar mRatingBar;
    EditText mEditTextWriteMsg;
    TextView mTextViewSubmit;

    // userdata
    public SharedPreferences mSharedPreferencesUserId;
    SharedPreferences.Editor mEditor;
    String mStringUserId;
    String mStringMedicineId;
    String mStringReviewText;
    String mStringRating;
    String mStringMedicineName;
    String mStringImagePath;

    Bundle mBundle;
    PostParseGet mPostParseGet;
    ServerResponseVO mServerResponseVO;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = (MainActivity) getActivity();
        mCommonHelper = new CommonHelper();
        mPostParseGet = new PostParseGet(mainActivity);
        mBundle = getArguments();
        if(mBundle != null)
        {
            mStringMedicineId = mBundle.getString("Medicine_id");
            mStringImagePath = mBundle.getString("Medicine_image");
            mStringMedicineName =mBundle.getString("Medicine_name");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentView = inflater.inflate(R.layout.fragment_rating_reviews, container, false);

        mImageViewBack = fragmentView.findViewById(R.id.fragment_imageview_back);
        mTextViewTitle =fragmentView.findViewById(R.id.fragment_title_textview);
        mTextViewTitle.setText("Add Review");
        mImageViewSearch =fragmentView.findViewById(R.id.fragment_Serach);
        mImageViewSearch.setVisibility(View.GONE);
        mImageViewCart =fragmentView.findViewById(R.id.fragment_cart);
        mImageViewCart.setVisibility(View.GONE);
        mImageViewMedicineImage =fragmentView.findViewById(R.id.fragment_rating_review_imageview);
        mTextViewMedicineNAme =fragmentView.findViewById(R.id.fragment_rating_reviews_textview_name);
        mRatingBar =fragmentView.findViewById(R.id.fragment_rating_reviews_ratingbar);
        mEditTextWriteMsg =fragmentView.findViewById(R.id.fragment_rating_reviews_edittext_review);
        mTextViewSubmit =fragmentView.findViewById(R.id.fragment_rating_reviews_textview_submit);


        mSharedPreferencesUserId = mainActivity.getSharedPreferences(Tags.TAG_SHARED_PREFERENCE_USER_ID, 0);
        mStringUserId = mSharedPreferencesUserId.getString("UserId", "");
        mEditor = mSharedPreferencesUserId.edit();

        if(mStringImagePath == null)
        {
            Glide.with(mainActivity).load(mStringImagePath)
                    .placeholder(R.drawable.logo)
                    .into(mImageViewMedicineImage);
        }
        else {
            Glide.with(mainActivity)
                    .load(R.drawable.ic_med)
                    .into(mImageViewMedicineImage);
        }

        if(mStringMedicineName != null)
        {
            mTextViewMedicineNAme.setText(mStringMedicineName);
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

//                mStringRating = String.valueOf(mRatingBar.getRating());
                mStringRating = String.valueOf(Math.round(mRatingBar.getRating()));
                Utility.debugger("Vt rating......" + mStringRating);
                mStringReviewText = mEditTextWriteMsg.getText().toString().trim();

                if(mStringRating.equals("0")) {
                    Snackbar.with(mainActivity).text("Please Select Rating").show(mainActivity);
                    return;
                }

                if(Math.round(mRatingBar.getRating())<=3.0f)
                {
                    Snackbar.with(mainActivity).text("Please write your message").show(mainActivity);
                    return;
                }


                AddReview();

            }
        });
        return fragmentView;
    }

//    Add Review

    public void AddReview() {
        getLoaderManager().restartLoader(0, mBundle, new LoaderManager.LoaderCallbacks<TaskExecutor>() {
            @Override
            public Loader<TaskExecutor> onCreateLoader(int arg0, Bundle arg1) {

                AddReviewexecutor mAddReviewexecutor = new AddReviewexecutor(mainActivity, null);
                return new LoaderTask<TaskExecutor>(mainActivity, mAddReviewexecutor);
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
                            else
                            {
                                Snackbar.with(mainActivity).text(mServerResponseVO.getMsg()).show(mainActivity);
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



    public class AddReviewexecutor extends TaskExecutor {

        protected AddReviewexecutor(Context context, Bundle bundle) {
            super(context, bundle);
        }

        @Override
        protected Object executeTask() {
            try {
                AddReviewService addReviewService = new AddReviewService();
                mServerResponseVO = addReviewService.AddReview(mainActivity, Tags.ADDREVIEW, mStringMedicineId, mStringUserId ,mStringReviewText ,mStringRating);

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