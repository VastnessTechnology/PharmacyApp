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
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;
import com.vgroyalchemist.R;
import com.vgroyalchemist.controller.MainActivity;
import com.vgroyalchemist.loaders.LoaderTask;
import com.vgroyalchemist.loaders.TaskExecutor;
import com.vgroyalchemist.utils.CommonHelper;
import com.vgroyalchemist.utils.Snackbar;
import com.vgroyalchemist.utils.Tags;
import com.vgroyalchemist.vos.ServerResponseVO;
import com.vgroyalchemist.webservice.AddComplainService;
import com.vgroyalchemist.webservice.AddReviewService;
import com.vgroyalchemist.webservice.PostParseGet;


/* developed by krishna 30-03-2021
 * Write a Product Complain msg  */
public class FragmentWriteComplain extends NonCartFragment {

    //Variable Declaration
    public static final String FRAGMENT_ID = "51";

    public static MainActivity mainActivity;
    public View fragmentView = null;
    CommonHelper mCommonHelper;

    ImageView mImageViewBack;
    TextView mTextViewTitle;
    ImageView mImageViewSearch;
    ImageView mImageViewCart;

    TextInputLayout mTextInputLayoutWritecomplain;
    EditText mEditTextWriteComplain;
    TextView mTextViewSubmit;
    TextView mTextViewOrderNo;
    TextView mTextViewMedicineName;
    Bundle mBundle;

    String mStringOrderId;
    String mStringMedicineId;
    String mStringComplainMsg;
    String mStringOrderNo;
    String mStringMedicineName;

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
        mBundle = getArguments();
        if(mBundle != null)
        {
            mStringOrderId = mBundle.getString("Order_id");
            mStringMedicineId = mBundle.getString("Medicine_id");
            mStringOrderNo = mBundle.getString("order_no");
            mStringMedicineName = mBundle.getString("medicine_name");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentView = inflater.inflate(R.layout.fragment_write_complain, container, false);

        mImageViewBack = fragmentView.findViewById(R.id.fragment_imageview_back);
        mTextViewTitle =fragmentView.findViewById(R.id.fragment_title_textview);
        mTextViewTitle.setText("Write Complaint");
        mImageViewSearch =fragmentView.findViewById(R.id.fragment_Serach);
        mImageViewSearch.setVisibility(View.GONE);
        mImageViewCart =fragmentView.findViewById(R.id.fragment_cart);
        mImageViewCart.setVisibility(View.GONE);

        mTextInputLayoutWritecomplain = fragmentView.findViewById(R.id.fragment_Write_complain_input_layout_message);
        mEditTextWriteComplain = fragmentView.findViewById(R.id.fragment_Write_complain_edittext_review);
        mTextViewSubmit = fragmentView.findViewById(R.id.fragment_Write_complain_textview_submit);
        mTextViewOrderNo = fragmentView.findViewById(R.id.fragment_Write_complain_textview_orderNo);
        mTextViewMedicineName = fragmentView.findViewById(R.id.fragment_Write_complain_textview_medicine_name);



        mSharedPreferencesUserId = mainActivity.getSharedPreferences(Tags.TAG_SHARED_PREFERENCE_USER_ID, 0);
        mStringUserId = mSharedPreferencesUserId.getString("UserId", "");
        mEditor = mSharedPreferencesUserId.edit();

        if(mStringOrderNo != null)
        {
            mTextViewOrderNo.setText("Order No :- "+ mStringOrderNo);
        }

        if(mStringMedicineName != null)
        {
            mTextViewMedicineName.setText("Medicine Name :- " + mStringMedicineName);
        }
        mTextViewSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mStringComplainMsg = mEditTextWriteComplain.getText().toString().trim();
                if(!validatemsg())
                {
                    return;
                }

                AddComplain();

            }
        });

        mImageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.onBackPressed();
            }
        });

        return fragmentView;
    }

    private boolean validatemsg() {
        String msg = mEditTextWriteComplain.getText().toString().trim();

        if (msg.isEmpty()) {
            mTextInputLayoutWritecomplain.setError(getResources().getString(R.string.valid_complain_msg));
            requestFocus(mEditTextWriteComplain);
            mCommonHelper.hideKeyboard(mainActivity);
            return false;
        } else {
            mTextInputLayoutWritecomplain.setError(null);
            mTextInputLayoutWritecomplain.setErrorEnabled(false);
        }

        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            mainActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
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

//     Add complain order
    public void AddComplain() {
        getLoaderManager().restartLoader(0, mBundle, new LoaderManager.LoaderCallbacks<TaskExecutor>() {
            @Override
            public Loader<TaskExecutor> onCreateLoader(int arg0, Bundle arg1) {

                AddMedicineComplain mAddReviewexecutor = new AddMedicineComplain(mainActivity, null);
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
                            else {
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



    public class AddMedicineComplain extends TaskExecutor {

        protected AddMedicineComplain(Context context, Bundle bundle) {
            super(context, bundle);
        }

        @Override
        protected Object executeTask() {
            try {
                AddComplainService addReviewService = new AddComplainService();
                mServerResponseVO = addReviewService.AddReview(mainActivity, Tags.AddComplain, mStringMedicineId, mStringUserId ,mStringOrderId ,mStringComplainMsg ,"Open","0","0");

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }


}