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
import com.vgroyalchemist.webservice.AddBookMarkInfoService;
import com.vgroyalchemist.webservice.GetAllArticlesListInfoService;
import com.vgroyalchemist.webservice.PostParseGet;
import java.util.ArrayList;

/* developed by krishna 24-03-2021
 *  Articles  list display*/
public class FragmentArticles extends NonCartFragment {


    //Variable Declaration
    public static final String FRAGMENT_ID = "5";

    public static MainActivity mainActivity;
    public View fragmentView = null;
    CommonHelper mCommonHelper;

    ImageView mImageViewBack;
    TextView mTextViewTitle;
    RecyclerView mRecyclerViewArticlesData;
    LinearLayout mLinearLayoutEmpty;
    ImageView mImageViewSearch;
    ImageView mImageViewCart;
    ServerResponseVO mServerResponseVO;
    ServerResponseVO mServerResponseVOBookMark;
    ServerResponseVO mServerResponseVORemoveBookMark;
    PostParseGet mPostParseGet;
    ArrayList<AllArticlesListVo> mAllArticlesListVos;
    String mStringArticlesId;
    ImageView mImageViewBookMark;
    ImageView mImageViewRemoveBookMark;

    // userdata
    public SharedPreferences mSharedPreferencesUserId;
    public SharedPreferences.Editor mEditor;

    String mStringUserid;

    boolean IsRemove = false;

    //login
    public SharedPreferences mSharedPreferencesLoginFirst;
    SharedPreferences.Editor mEditorLogin;
    String GuestUser;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = (MainActivity) getActivity();
        mCommonHelper = new CommonHelper();
        mAllArticlesListVos = new ArrayList<AllArticlesListVo>();
        mPostParseGet = new PostParseGet(mainActivity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentView = inflater.inflate(R.layout.fragment_articles, container, false);

        mImageViewBack = fragmentView.findViewById(R.id.fragment_imageview_back);
        mTextViewTitle = fragmentView.findViewById(R.id.fragment_title_textview);
        mTextViewTitle.setText("Articles");
        mImageViewSearch = fragmentView.findViewById(R.id.fragment_Serach);
        mImageViewSearch.setVisibility(View.GONE);
        mImageViewCart = fragmentView.findViewById(R.id.fragment_cart);

        mSharedPreferencesUserId = mainActivity.getSharedPreferences(Tags.TAG_SHARED_PREFERENCE_USER_ID, 0);
        mEditor = mSharedPreferencesUserId.edit();
        mStringUserid = mSharedPreferencesUserId.getString("UserId", "");

        mSharedPreferencesLoginFirst = mainActivity.getSharedPreferences(Tags.PREFS_NAME, 0);
        mEditorLogin = mSharedPreferencesLoginFirst.edit();
        GuestUser = mSharedPreferencesLoginFirst.getString("GuestUser", "0");

        CommonClass cmn = new CommonClass();
        cmn.FirebaseToken(getActivity());

        if (GuestUser.equalsIgnoreCase("1")) {
            mImageViewCart.setVisibility(View.GONE);
            mStringUserid = "00000000-0000-0000-0000-000000000000";

        } else {
            mImageViewCart.setVisibility(View.VISIBLE);
            mImageViewCart.setImageResource(R.drawable.ic_bookmark);
//            Tags.mStringDeviceToken = "";
        }

        mImageViewCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment currentFrag = mainActivity.getSupportFragmentManager().findFragmentById(R.id.activity_main_container);
                mCurrentFragment = currentFrag.getClass().getName();

                if (!mCurrentFragment.equalsIgnoreCase(Tags.TAG_FRAGEMENT_ADD_ADDRESS)) {

                    FragmentBookArticlesList mFragmentAddAddress = new FragmentBookArticlesList();
                    mainActivity.addFragment(mFragmentAddAddress, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, mFragmentAddAddress.getClass().getName());
                }
            }
        });


        mImageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mainActivity.RemoveAllFragment();
                FragmentTabActivity fragmentTabActivity = new FragmentTabActivity();
                mainActivity.addFragment(fragmentTabActivity, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, fragmentTabActivity.getClass().getName());
            }
        });

        mRecyclerViewArticlesData = fragmentView.findViewById(R.id.fragment_Articles_listview_data);
        mLinearLayoutEmpty = fragmentView.findViewById(R.id.fragment_Articles_relative_empty);
        return fragmentView;
    }


    @Override
    public void onStart() {
        super.onStart();
        GetArticlesData();
    }


    public void GetArticlesData() {

        getLoaderManager().restartLoader(0, null, new LoaderManager.LoaderCallbacks<TaskExecutor>() {
            @Override
            public Loader<TaskExecutor> onCreateLoader(int arg0, Bundle arg1) {

                ArticlesListExecutor articlesListExecutor = new ArticlesListExecutor(mainActivity, null);
                return new LoaderTask<TaskExecutor>(mainActivity, articlesListExecutor);
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
                                mAllArticlesListVos.clear();
                                if (mServerResponseVO.getData() != null) {

                                    mAllArticlesListVos = (ArrayList<AllArticlesListVo>) mServerResponseVO.getData();
                                    if (mAllArticlesListVos != null && mAllArticlesListVos.size() > 0) {

                                        mRecyclerViewArticlesData.setVisibility(View.VISIBLE);
                                        mLinearLayoutEmpty.setVisibility(View.GONE);


                                        ArticlesAdapter customAdapter = new ArticlesAdapter(mainActivity, mAllArticlesListVos);
                                        mRecyclerViewArticlesData.setHasFixedSize(true);
                                        mRecyclerViewArticlesData.setLayoutManager(new LinearLayoutManager(mainActivity));
                                        mRecyclerViewArticlesData.setAdapter(customAdapter);

                                    } else {
                                        mLinearLayoutEmpty.setVisibility(View.VISIBLE);
                                        mRecyclerViewArticlesData.setVisibility(View.GONE);
                                        mCommonHelper.hideKeyboard(mainActivity);
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
    public class ArticlesListExecutor extends TaskExecutor {
        protected ArticlesListExecutor(Context context, Bundle bundle) {
            super(context, bundle);
        }

        @Override
        protected Object executeTask() {

            GetAllArticlesListInfoService getAllAddressInfoService = new GetAllArticlesListInfoService();
            mServerResponseVO = getAllAddressInfoService.fetchLoginUserInformation(mainActivity, Tags.ListAllAtricles, mStringUserid , Tags.mStringDeviceToken);

            return null;
        }
    }


    public class ArticlesAdapter extends RecyclerView.Adapter<ArticlesAdapter.ViewHolder> {

        Context mContext;
        ArrayList<AllArticlesListVo> mArrayList;

        public ArticlesAdapter(MainActivity mainActivity, ArrayList<AllArticlesListVo> mArrayListCategory) {
            this.mContext = mainActivity;
            this.mArrayList = mArrayListCategory;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_articles, parent, false);
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {


            holder.mTextViewArticlesName.setText(mArrayList.get(position).getShortDesc());

//            Glide.with(mContext).load("https://videocdn.bodybuilding.com/video/mp4/62000/62792m.mp4")
//                    .placeholder(R.drawable.logo)
//                    .diskCacheStrategy(DiskCacheStrategy.DATA)
//                    .skipMemoryCache(true)
//                    .into(holder.mImageViewArticlesIamge);

            if(mArrayList.get(position).getImageName().endsWith(".mp4"))
            {
                holder.mImageViewArticlesIamge.setVisibility(View.GONE);
                holder.mImageViewArticlesVideo.setVisibility(View.VISIBLE);
                if (mArrayList.get(position).getImageName() != null) {
                    Glide.with(mContext).load("https://videocdn.bodybuilding.com/video/mp4/62000/62792m.mp4")
                            .placeholder(R.drawable.logo)
                            .diskCacheStrategy(DiskCacheStrategy.DATA)
                            .skipMemoryCache(true)
                            .into(holder.mImageViewArticlesIamge);
                }
            }
            else {
                holder.mImageViewArticlesIamge.setVisibility(View.VISIBLE);
                holder.mImageViewArticlesVideo.setVisibility(View.GONE);
                if (mArrayList.get(position).getImageName() != null) {
                    Glide.with(mContext).load(mArrayList.get(position).getImageName())
                            .placeholder(R.drawable.logo)
                            .diskCacheStrategy(DiskCacheStrategy.DATA)
                            .skipMemoryCache(true)
                            .into(holder.mImageViewArticlesIamge);
                }
            }

            if (GuestUser.equalsIgnoreCase("1")) {
                mImageViewBookMark.setVisibility(View.GONE);
            } else {
                mImageViewBookMark.setVisibility(View.VISIBLE);

                if (mArrayList.get(position).getBookMark().equalsIgnoreCase("1")) {
                    mImageViewRemoveBookMark.setVisibility(View.VISIBLE);
                    mImageViewBookMark.setVisibility(View.GONE);
                } else {
                    mImageViewRemoveBookMark.setVisibility(View.GONE);
                    mImageViewBookMark.setVisibility(View.VISIBLE);
                }
            }


            mImageViewBookMark.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    final Bundle mBundle = new Bundle();
                    mBundle.putString(getString(R.string.app_name), mArrayList.get(position).getId());

                    getLoaderManager().restartLoader(0, mBundle, new LoaderManager.LoaderCallbacks<TaskExecutor>() {
                        @Override
                        public Loader<TaskExecutor> onCreateLoader(int arg0, Bundle arg1) {

                            AddBookMarkExecutor addBookMarkExecutor = new AddBookMarkExecutor(mainActivity, mBundle);
                            return new LoaderTask<TaskExecutor>(mainActivity, addBookMarkExecutor);
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
                                    if (mServerResponseVOBookMark.getStatus().equalsIgnoreCase("true")) {

                                        mImageViewRemoveBookMark.setVisibility(View.VISIBLE);
                                        mImageViewBookMark.setVisibility(View.GONE);
                                        Snackbar.with(mainActivity).text(mServerResponseVOBookMark.getMsg()).show(mainActivity);
                                        mCommonHelper.hideKeyboard(mainActivity);
                                        GetArticlesData();
                                    } else {
                                        Snackbar.with(mainActivity).text(mServerResponseVOBookMark.getMsg()).show(mainActivity);
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
            });

            mImageViewRemoveBookMark.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    IsRemove = true;

                    final Bundle mBundle = new Bundle();
                    mBundle.putString(getString(R.string.app_name), mArrayList.get(position).getId());

                    getLoaderManager().restartLoader(0, mBundle, new LoaderManager.LoaderCallbacks<TaskExecutor>() {
                        @Override
                        public Loader<TaskExecutor> onCreateLoader(int arg0, Bundle arg1) {

                            AddBookMarkExecutor addBookMarkExecutor = new AddBookMarkExecutor(mainActivity, mBundle);
                            return new LoaderTask<TaskExecutor>(mainActivity, addBookMarkExecutor);
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
                                    if (mServerResponseVORemoveBookMark.getStatus().equalsIgnoreCase("true")) {

                                        mImageViewRemoveBookMark.setVisibility(View.GONE);
                                        mImageViewBookMark.setVisibility(View.VISIBLE);
                                        Snackbar.with(mainActivity).text(mServerResponseVORemoveBookMark.getMsg()).show(mainActivity);
                                        mCommonHelper.hideKeyboard(mainActivity);
                                        GetArticlesData();

                                        IsRemove = false;
                                    } else {
                                        Snackbar.with(mainActivity).text(mServerResponseVORemoveBookMark.getMsg()).show(mainActivity);
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
            });

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    FragmentArticlesDetails fragmentArticlesDetails = new FragmentArticlesDetails();
                    Bundle mBundle = new Bundle();
                    mBundle.putString("Description", mArrayList.get(position).getLongDesc());
                    mBundle.putString("ShortDescription", mArrayList.get(position).getShortDesc());
                    mBundle.putString("ImagePath", mArrayList.get(position).getImageName());
                    fragmentArticlesDetails.setArguments(mBundle);
                    mainActivity.addFragment(fragmentArticlesDetails, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, fragmentArticlesDetails.getClass().getName());
                }
            });
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }


        @Override
        public int getItemCount() {
            return mArrayList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView mTextViewArticlesName;
            ImageView mImageViewArticlesIamge;
            ImageView mImageViewArticlesVideo;

            public ViewHolder(View itemView) {
                super(itemView);
                mImageViewArticlesIamge = (ImageView) itemView.findViewById(R.id.row_articles_imageview_banner_image);
                mTextViewArticlesName = (TextView) itemView.findViewById(R.id.row_articles_textview_title);
                mImageViewBookMark = (ImageView) itemView.findViewById(R.id.row_articles_imageview_bookmark);
                mImageViewRemoveBookMark = itemView.findViewById(R.id.row_articles_imageview_bookmark_addbook);
                mImageViewArticlesVideo = itemView.findViewById(R.id.row_articles_imageview_banner_video);
            }
        }
    }


    @SuppressLint("RestrictedApi")
    public class AddBookMarkExecutor extends TaskExecutor {

        String mStringArticlesId;

        protected AddBookMarkExecutor(Context context, Bundle bundle) {
            super(context, bundle);
            this.mStringArticlesId = bundle.getString(getString(R.string.app_name));
        }

        @Override
        protected Object executeTask() {

            AddBookMarkInfoService getAllAddressInfoService = new AddBookMarkInfoService();

            if (IsRemove == true) {
                mServerResponseVORemoveBookMark = getAllAddressInfoService.addbookmark(mainActivity, Tags.REMOVEBOOKMARK, mStringArticlesId, mStringUserid);
            } else {

                mServerResponseVOBookMark = getAllAddressInfoService.addbookmark(mainActivity, Tags.AddBookMark, mStringArticlesId, mStringUserid);
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