package com.vgroyalchemist.views;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vgroyalchemist.R;
import com.vgroyalchemist.controller.MainActivity;
import com.vgroyalchemist.loaders.LoaderTask;
import com.vgroyalchemist.loaders.TaskExecutor;
import com.vgroyalchemist.utils.CommonHelper;
import com.vgroyalchemist.utils.Snackbar;
import com.vgroyalchemist.utils.Tags;
import com.vgroyalchemist.vos.LegalDetailsVO;
import com.vgroyalchemist.vos.OrderDetailsVO;
import com.vgroyalchemist.vos.ServerResponseVO;
import com.vgroyalchemist.webservice.LegalDetailsService;
import com.vgroyalchemist.webservice.OrderDeatilsService;
import com.vgroyalchemist.webservice.PostParseGet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/* developed by krishna 24-03-2021
*  privacy policy , team and condition and Return & refund */
public class FragmentLegal extends NonCartFragment {

    public static final String FRAGMENT_ID = "48";

    public static MainActivity mainActivity;
    CommonHelper mCommonHelper;
    View fragmnetview = null;

    ImageView mImageViewBack;
    TextView mTextViewTitle;
    ImageView mImageViewSearch;
    ImageView mImageViewCart;

    RelativeLayout mRelativeLayoutPrivacyPolicy;
    RelativeLayout mRelativeLayoutTermCondition;
    RelativeLayout mRelativeLayoutReturnRefund;

    ServerResponseVO mServerResponseVO;
    PostParseGet mPostParseGet;

    LegalDetailsVO mLegalDetailsVO;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = (MainActivity) getActivity();
        mCommonHelper = new CommonHelper();
        mPostParseGet = new PostParseGet(mainActivity);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmnetview = inflater.inflate(R.layout.fragment_legal, container, false);

        mImageViewBack = fragmnetview.findViewById(R.id.fragment_imageview_back);
        mTextViewTitle = fragmnetview.findViewById(R.id.fragment_title_textview);
        mTextViewTitle.setText("Legal");
        mImageViewSearch = fragmnetview.findViewById(R.id.fragment_Serach);
        mImageViewSearch.setVisibility(View.GONE);
        mImageViewCart = fragmnetview.findViewById(R.id.fragment_cart);
        mImageViewCart.setVisibility(View.GONE);

        mRelativeLayoutPrivacyPolicy = fragmnetview.findViewById(R.id.fragment_legal_linear_privacy_policy);
        mRelativeLayoutTermCondition = fragmnetview.findViewById(R.id.fragment_legal_linear_condition);
        mRelativeLayoutReturnRefund = fragmnetview.findViewById(R.id.fragment_legal_linear_return_refund);


        getLoaderManager().restartLoader(0, null, new LoaderManager.LoaderCallbacks<TaskExecutor>() {
            @Override
            public Loader<TaskExecutor> onCreateLoader(int arg0, Bundle arg1) {

                LegalDataExecutor mLegalDataExecutor = new LegalDataExecutor(mainActivity, null);
                return new LoaderTask<TaskExecutor>(mainActivity, mLegalDataExecutor);
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

                                if (mServerResponseVO.getData() != null) {
                                    mLegalDetailsVO = (LegalDetailsVO) mServerResponseVO.getData();

                                }
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


        mRelativeLayoutPrivacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentContentFullDescription fragmentContentFullDescription = new FragmentContentFullDescription();
                Bundle mBundle  = new Bundle();
                mBundle.putString("LegalDetails",mLegalDetailsVO.getPrivacyPolicy());
                mBundle.putString("privcy",getResources().getString(R.string.app_name));
                fragmentContentFullDescription.setArguments(mBundle);
                mainActivity.addFragment(fragmentContentFullDescription, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, fragmentContentFullDescription.getClass().getName());

            }
        });
        mRelativeLayoutTermCondition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentContentFullDescription fragmentContentFullDescription = new FragmentContentFullDescription();
                Bundle mBundle  = new Bundle();
                mBundle.putString("LegalDetails",mLegalDetailsVO.getTermsCondition());
                mBundle.putString("TermCondition",getResources().getString(R.string.app_name));
                fragmentContentFullDescription.setArguments(mBundle);
                mainActivity.addFragment(fragmentContentFullDescription, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, fragmentContentFullDescription.getClass().getName());
            }
        });
        mRelativeLayoutReturnRefund.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentContentFullDescription fragmentContentFullDescription = new FragmentContentFullDescription();
                Bundle mBundle  = new Bundle();
                mBundle.putString("LegalDetails",mLegalDetailsVO.getReturnRefundPolicy());
                mBundle.putString("returnfund",getResources().getString(R.string.app_name));
                fragmentContentFullDescription.setArguments(mBundle);
                mainActivity.addFragment(fragmentContentFullDescription, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, fragmentContentFullDescription.getClass().getName());
            }
        });

        mImageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.onBackPressed();
            }
        });

        return fragmnetview;
    }


    //order Deatils
    public class LegalDataExecutor extends TaskExecutor {

        protected LegalDataExecutor(Context context, Bundle bundle) {
            super(context, bundle);
        }

        @Override
        protected Object executeTask() {
            try {
                LegalDetailsService legalDetailsService = new LegalDetailsService();
                mServerResponseVO = legalDetailsService.LegalDetails(mainActivity, Tags.GetPrivacyDetails);

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