package com.vgroyalchemist.views;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.FragmentTransaction;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.RecyclerView;

import com.vgroyalchemist.R;
import com.vgroyalchemist.controller.MainActivity;
import com.vgroyalchemist.loaders.LoaderTask;
import com.vgroyalchemist.loaders.TaskExecutor;
import com.vgroyalchemist.utils.CommonHelper;
import com.vgroyalchemist.utils.Snackbar;
import com.vgroyalchemist.utils.Tags;
import com.vgroyalchemist.vos.AllArticlesListVo;
import com.vgroyalchemist.vos.CounponlistTypeVO;
import com.vgroyalchemist.vos.ServerResponseVO;
import com.vgroyalchemist.webservice.GetCoupenService;
import com.vgroyalchemist.webservice.PostParseGet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class FragmentCouponCode extends NonCartFragment {


    //Variable Declaration
    public static final String FRAGMENT_ID = "65";

    public static MainActivity mainActivity;
    public View fragmentView = null;
    CommonHelper mCommonHelper;

    ImageView mImageViewBack;
    TextView mTextViewTitle;
    ImageView mImageViewSearch;
    ImageView mImageViewCart;

    ListView mListView;
    TextView mTextView;

    private MyBaseAdaptercode mBaseAdaptercode;
    PostParseGet mPostParseGet;
    ServerResponseVO mServerResponseVOCoupen;
    ArrayList<CounponlistTypeVO> mCounponlistTypeVOS;

    // userdata
    public SharedPreferences mSharedPreferencesUserId;
    public SharedPreferences.Editor mEditor;
    String mStringUserid;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = (MainActivity) getActivity();
        mCommonHelper = new CommonHelper();
        mPostParseGet = new PostParseGet(mainActivity);
        mCounponlistTypeVOS = new ArrayList<CounponlistTypeVO>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentView = inflater.inflate(R.layout.fragment_coupon_code, container, false);

        mImageViewBack = fragmentView.findViewById(R.id.fragment_imageview_back);
        mTextViewTitle = fragmentView.findViewById(R.id.fragment_title_textview);
        mTextViewTitle.setText("Apply Coupon");
        mImageViewSearch = fragmentView.findViewById(R.id.fragment_Serach);
        mImageViewSearch.setVisibility(View.GONE);
        mImageViewCart = fragmentView.findViewById(R.id.fragment_cart);
        mImageViewCart.setVisibility(View.GONE);

        mListView = fragmentView.findViewById(R.id.fragment_coupon_listview);
        mTextView = fragmentView.findViewById(R.id.fragment_coupon_textview_nodata);

        mSharedPreferencesUserId = mainActivity.getSharedPreferences(Tags.TAG_SHARED_PREFERENCE_USER_ID, 0);
        mEditor = mSharedPreferencesUserId.edit();
        mStringUserid = mSharedPreferencesUserId.getString("UserId", "");

        mImageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mainActivity.RemoveAllFragment();
                FragmentTabActivity fragmentTabActivity = new FragmentTabActivity();
                mainActivity.addFragment(fragmentTabActivity, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, fragmentTabActivity.getClass().getName());
            }
        });

        return fragmentView;

    }

    @Override
    public void onStart() {
        super.onStart();
        GetCoupenCode();
    }

    //    get coupen code
    public void GetCoupenCode() {
        getLoaderManager().restartLoader(0, null, new LoaderManager.LoaderCallbacks<TaskExecutor>() {
            @Override
            public Loader<TaskExecutor> onCreateLoader(int arg0, Bundle arg1) {
                GetCoupenCodeExecutor mGetCoupenCodeExecutor = new GetCoupenCodeExecutor(mainActivity, null);
                return new LoaderTask<TaskExecutor>(mainActivity, mGetCoupenCodeExecutor);
            }

            @Override
            public void onLoadFinished(Loader<TaskExecutor> arg0, TaskExecutor arg1) {
                if (isAdded()) {
                    getLoaderManager().destroyLoader(0);
                    if (mPostParseGet.isNetError) {
                        Snackbar.with(mainActivity).text(getResources().getString(R.string.interneterror)).show(mainActivity);
                    } else if (mPostParseGet.isOtherError)
                        Snackbar.with(mainActivity).text(getResources().getString(R.string.notabletocommunicatewithserver)).show(mainActivity);
                    else {
                        if (mServerResponseVOCoupen != null && mServerResponseVOCoupen.getStatus() != null) {

                            if (mServerResponseVOCoupen.getStatus().equalsIgnoreCase("true")) {

                                if (mServerResponseVOCoupen.getData() != null) {

                                    if (mCounponlistTypeVOS != null) {
                                        mCounponlistTypeVOS = (ArrayList<CounponlistTypeVO>) mServerResponseVOCoupen.getData();

                                        if (mCounponlistTypeVOS.size() == 0) {
                                            mListView.setVisibility(View.GONE);
                                            mTextView.setVisibility(View.VISIBLE);
                                            mBaseAdaptercode = new MyBaseAdaptercode(mainActivity, R.layout.row_coupon, mCounponlistTypeVOS);
                                            mListView.setAdapter(mBaseAdaptercode);
                                        } else {
                                            mListView.setVisibility(View.VISIBLE);
                                            mTextView.setVisibility(View.GONE);
                                            mBaseAdaptercode = new MyBaseAdaptercode(mainActivity, R.layout.row_coupon, mCounponlistTypeVOS);
                                            mListView.setAdapter(mBaseAdaptercode);
                                        }

                                    } else {
//                                        mTextInputLayoutPromocode.setError(getResources().getString(R.string.no_coupe_code));
                                    }


                                }
                            } else {
                                Snackbar.with(mainActivity).text(mServerResponseVOCoupen.getMsg()).show(mainActivity);
                                mCommonHelper.hideKeyboard(mainActivity);
                            }
                        }
                    }
                }
            }

            @Override
            public void onLoaderReset(Loader<TaskExecutor> arg0) {
            }
        });
    }


    public class GetCoupenCodeExecutor extends TaskExecutor {
        protected GetCoupenCodeExecutor(Context context, Bundle bundle) {
            super(context, bundle);
        }

        @Override
        protected Object executeTask() {
            GetCoupenService mGetAllCartService = new GetCoupenService();
            try {
                mServerResponseVOCoupen = mGetAllCartService.getcoupen(mainActivity, Tags.GETCOUPEN ,mStringUserid);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }


    public class MyBaseAdaptercode extends ArrayAdapter<CounponlistTypeVO> {

        ArrayList<CounponlistTypeVO> mArrayList;

        public MyBaseAdaptercode(Context context, int resource, ArrayList<CounponlistTypeVO> objects) {
            super(context, resource, objects);
            mArrayList = objects;
        }

        @Override
        public int getCount() {
            return mArrayList.size();
        }

        @SuppressLint("SetTextI18n")
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {


            final ViewHoldercode holder;

            if (convertView == null) {

                convertView = mainActivity.getLayoutInflater().inflate(R.layout.row_coupon, parent, false);
                holder = new ViewHoldercode();


                holder.mTextViewProductName = convertView.findViewById(R.id.row_coupon_textview_couponname);
                holder.mTextViewproductDesctiption = convertView.findViewById(R.id.row_coupon_textview_description);
                holder.mTextViewApply = convertView.findViewById(R.id.row_coupon_textview_Apply);
                holder.mTextViewExpireDate = convertView.findViewById(R.id.row_coupon_textview_ExpiresDate);

                convertView.setTag(holder);

            } else {
                holder = (ViewHoldercode) convertView.getTag();
            }


            holder.mTextViewProductName.setText(mArrayList.get(position).getCoupenCode());
            if (mArrayList.get(position).getDescription() != null && !mArrayList.get(position).getDescription().equalsIgnoreCase("null")) {

                holder.mTextViewproductDesctiption.setText(mArrayList.get(position).getDescription());
            }

            if (mArrayList.get(position).getExpiryDate() != null || !mArrayList.get(position).getExpiryDate().equalsIgnoreCase("null")) {
                String mDate = mArrayList.get(position).getExpiryDate();
                SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                SimpleDateFormat fmt2 = new SimpleDateFormat("yyyy-MMM-dd");
                try {
                    Date date = fmt.parse(mDate);
                    String mString = fmt2.format(date);

                    holder.mTextViewExpireDate.setText("Expires :- " + mString);
                } catch (ParseException pe) {
                    pe.printStackTrace();
                }
            }


            holder.mTextViewApply.setVisibility(View.GONE);

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

//                    CoupenCodeValue =mArrayList.get(position).getCoupenCode();
//                    mEditTextCoupenCode.setText(mArrayList.get(position).getCoupenCode());
//                    mDialog.dismiss();

                }
            });

            holder.mTextViewproductDesctiption.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    CoupenCodeValue =mArrayList.get(position).getCoupenCode();
//                    mEditTextCoupenCode.setText(mArrayList.get(position).getCoupenCode());
//                    mDialog.dismiss();
                }
            });


            return convertView;
        }
    }

    static class ViewHoldercode {
        TextView mTextViewProductName;
        TextView mTextViewproductDesctiption;
        TextView mTextViewExpireDate;
        TextView mTextViewApply;


    }


    public void onResumeData() {

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
