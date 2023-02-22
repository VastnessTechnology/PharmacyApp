package com.vgroyalchemist.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
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
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.vgroyalchemist.CommonClass.CommonClass;
import com.vgroyalchemist.R;
import com.vgroyalchemist.controller.MainActivity;
import com.vgroyalchemist.loaders.LoaderTask;
import com.vgroyalchemist.loaders.TaskExecutor;
import com.vgroyalchemist.utils.CommonHelper;
import com.vgroyalchemist.utils.Snackbar;
import com.vgroyalchemist.utils.Tags;
import com.vgroyalchemist.vos.AllArticlesListVo;
import com.vgroyalchemist.vos.ServerResponseVO;
import com.vgroyalchemist.webservice.BookMarkArticlesListInfoService;
import com.vgroyalchemist.webservice.PostParseGet;
import java.util.ArrayList;

/* developed by krishna 24-03-2021
 *  Articles book mark list */
public class FragmentBookArticlesList extends NonCartFragment {


    //Variable Declaration
    public static final String FRAGMENT_ID = "30";

    public static MainActivity mainActivity;
    public View fragmentView = null;
    CommonHelper mCommonHelper;

    ImageView mImageViewBack;
    TextView mTextViewTitle;
    RecyclerView mRecyclerViewArticlesData;
    LinearLayout mLinearLayoutEmpty;
    ImageView mImageViewSearch;
    ImageView mImageViewCart;

    // userdata
    public SharedPreferences mSharedPreferencesUserId;
    public SharedPreferences.Editor mEditor;

    String mStringUserid;

    ServerResponseVO mServerResponseVO;
    PostParseGet mPostParseGet;

    ArrayList<AllArticlesListVo> mAllArticlesListVos;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = (MainActivity) getActivity();
        mCommonHelper = new CommonHelper();
        mPostParseGet = new PostParseGet(mainActivity);
        mAllArticlesListVos = new ArrayList<AllArticlesListVo>();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentView = inflater.inflate(R.layout.fragment_book_articles_list, container, false);
        mImageViewBack = fragmentView.findViewById(R.id.fragment_imageview_back);
        mTextViewTitle = fragmentView.findViewById(R.id.fragment_title_textview);
        mTextViewTitle.setText("Articles BookMark");
        mImageViewSearch = fragmentView.findViewById(R.id.fragment_Serach);
        mImageViewSearch.setVisibility(View.GONE);
        mImageViewCart = fragmentView.findViewById(R.id.fragment_cart);
        mImageViewCart.setVisibility(View.GONE);

        CommonClass cmn = new CommonClass();
        cmn.FirebaseToken(getActivity());

        mRecyclerViewArticlesData = fragmentView.findViewById(R.id.fragment_Articles_bookmark_listview_data);
        mLinearLayoutEmpty = fragmentView.findViewById(R.id.fragment_Articles_bookmark_relative_empty);

        mSharedPreferencesUserId = mainActivity.getSharedPreferences(Tags.TAG_SHARED_PREFERENCE_USER_ID, 0);
        mEditor = mSharedPreferencesUserId.edit();
        mStringUserid = mSharedPreferencesUserId.getString("UserId", "");

        mImageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//
//                Fragment currentFrag = mainActivity.getSupportFragmentManager().findFragmentById(R.id.activity_main_container);
//                mCurrentFragment = currentFrag.getClass().getName();
//                mainActivity.removeFragment(currentFrag, currentFrag.getClass().getName());
                mainActivity.onBackPressed();
            }
        });
        GetBookMarkArticlesData();
        return fragmentView;
    }


    public void GetBookMarkArticlesData() {

        getLoaderManager().restartLoader(0, null, new LoaderManager.LoaderCallbacks<TaskExecutor>() {
            @Override
            public Loader<TaskExecutor> onCreateLoader(int arg0, Bundle arg1) {

                ArticlesBookmarkListExecutor articlesBookmarkListExecutor = new ArticlesBookmarkListExecutor(mainActivity, null);
                return new LoaderTask<TaskExecutor>(mainActivity, articlesBookmarkListExecutor);
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

                                    mAllArticlesListVos = (ArrayList<AllArticlesListVo>) mServerResponseVO.getData();
                                    if (mAllArticlesListVos != null && mAllArticlesListVos.size() > 0) {

                                        mRecyclerViewArticlesData.setVisibility(View.VISIBLE);
                                        mLinearLayoutEmpty.setVisibility(View.GONE);


                                        BookMArkArticlesAdapter customAdapter = new BookMArkArticlesAdapter(mainActivity, mAllArticlesListVos);
                                        mRecyclerViewArticlesData.setHasFixedSize(true);
                                        mRecyclerViewArticlesData.setLayoutManager(new LinearLayoutManager(mainActivity));
                                        mRecyclerViewArticlesData.setAdapter(customAdapter);

                                    }
                                }
                            } else {

                                mLinearLayoutEmpty.setVisibility(View.VISIBLE);
                                mRecyclerViewArticlesData.setVisibility(View.GONE);
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
    public class ArticlesBookmarkListExecutor extends TaskExecutor {
        protected ArticlesBookmarkListExecutor(Context context, Bundle bundle) {
            super(context, bundle);
        }

        @Override
        protected Object executeTask() {

            BookMarkArticlesListInfoService getAllAddressInfoService = new BookMarkArticlesListInfoService();
            mServerResponseVO = getAllAddressInfoService.BookmarkAll(mainActivity, Tags.GetBookmarkDetail, mStringUserid, Tags.mStringDeviceToken);

            return null;
        }
    }


    public static class BookMArkArticlesAdapter extends RecyclerView.Adapter<BookMArkArticlesAdapter.ViewHolderBookMark> {

        Context mContext;
        ArrayList<AllArticlesListVo> mArrayList;

        public BookMArkArticlesAdapter(MainActivity mainActivity, ArrayList<AllArticlesListVo> mArrayListCategory) {
            this.mContext = mainActivity;
            this.mArrayList = mArrayListCategory;
        }

        @NonNull
        @Override
        public ViewHolderBookMark onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_articles, parent, false);
            ViewHolderBookMark vh = new ViewHolderBookMark(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolderBookMark holder, @SuppressLint("RecyclerView") final int position) {


            holder.mTextViewArticlesName.setText(mArrayList.get(position).getShortDesc());
            if (mArrayList.get(position).getImageName() != null) {
                holder.mImageViewArticlesIamge.setVisibility(View.VISIBLE);
                Glide.with(mContext).load(mArrayList.get(position).getImageName())
                        .placeholder(R.drawable.logo)
                        .diskCacheStrategy(DiskCacheStrategy.DATA)
                        .skipMemoryCache(true)
                        .into(holder.mImageViewArticlesIamge);
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    FragmentArticlesDetails fragmentArticlesDetails = new FragmentArticlesDetails();
                    Bundle mBundle = new Bundle();
                    mBundle.putString("Description", mArrayList.get(position).getLongDesc());
                    fragmentArticlesDetails.setArguments(mBundle);
                    mainActivity.addFragment(fragmentArticlesDetails, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, fragmentArticlesDetails.getClass().getName());
                }
            });
        }

        @Override
        public int getItemCount() {
            return mArrayList.size();
        }

        public static class ViewHolderBookMark extends RecyclerView.ViewHolder {
            TextView mTextViewArticlesName;
            ImageView mImageViewArticlesIamge;
            ImageView mImageViewBookMark;

            public ViewHolderBookMark(View itemView) {
                super(itemView);
                mImageViewArticlesIamge = (ImageView) itemView.findViewById(R.id.row_articles_imageview_banner_image);
                mTextViewArticlesName = (TextView) itemView.findViewById(R.id.row_articles_textview_title);
                mImageViewBookMark = (ImageView) itemView.findViewById(R.id.row_articles_imageview_bookmark);
                mImageViewBookMark.setVisibility(View.GONE);
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