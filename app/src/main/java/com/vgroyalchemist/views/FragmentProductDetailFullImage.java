package com.vgroyalchemist.views;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.vgroyalchemist.R;
import com.vgroyalchemist.controller.MainActivity;
import com.vgroyalchemist.utils.Tags;
import com.vgroyalchemist.utils.ZoomableImageView;
import com.vgroyalchemist.vos.MedicineImagevo;
import com.vgroyalchemist.webservice.PostParseGet;

import java.util.ArrayList;

import it.sephiroth.android.library.imagezoom.ImageViewTouch;
import it.sephiroth.android.library.imagezoom.ImageViewTouchBase;

public class FragmentProductDetailFullImage extends NonCartFragment implements ViewPager.OnPageChangeListener {

    //Variable Declaration
    public static final String FRAGMENT_ID = "56";

    private View fragmentView = null;

    private MainActivity mainActivity;

    private PostParseGet mPostParseGet;

    SharedPreferences mSharedPreferencesSupplier;

    String mStringSupplierId = "";

    Bundle mBundle;

    ViewPager mViewPager;
//	LinearLayout mLinearLayout;

    ArrayList<MedicineImagevo> mStringArrayListImages;

    ProductFullImageAdapter mAdapter;
    private int dotsCount;
    private ImageView[] dots;

    int mIntPos = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainActivity = (MainActivity) getActivity();

        mBundle = getArguments();
        mPostParseGet = new PostParseGet(mainActivity);
        mStringArrayListImages = new ArrayList<MedicineImagevo>();
        if (mBundle != null) {
            mStringArrayListImages = (ArrayList<MedicineImagevo>) mBundle.getSerializable(Tags.IMAGES_INTENT);
            mIntPos = mBundle.getInt(Tags.IMAGES_POSITION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        fragmentView = inflater.inflate(R.layout.fragment_full_image, container, false);

        mViewPager = fragmentView.findViewById(R.id.fragment_full_image_view_pager);
//		mLinearLayout = (LinearLayout) fragmentView.findViewById(R.id.fragment_full_image_indicator);



        mAdapter = new ProductFullImageAdapter(mainActivity, mStringArrayListImages);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOnPageChangeListener(FragmentProductDetailFullImage.this);

        mViewPager.setCurrentItem(mIntPos);

        return fragmentView;
    }

    public class ProductFullImageAdapter extends PagerAdapter {
        Activity mActivity;
        ArrayList<MedicineImagevo> imageList;
        LayoutInflater mLayoutInflater;

        public ProductFullImageAdapter(Activity mContext, ArrayList<MedicineImagevo> imageList) {
            this.mActivity = mContext;
            this.imageList = imageList;
            mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount()    {
            return imageList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {

            View itemView = mLayoutInflater.inflate(R.layout.detail_full_image, container, false);
            ImageViewTouch mImageView = itemView.findViewById(R.id.detail_full_bigimageview);
            mImageView.setDisplayType(ImageViewTouchBase.DisplayType.FIT_TO_SCREEN);


            if (imageList.get(position) != null) {
                Glide.with(mActivity)
                        .load(imageList.get(position).getImagePath())
                        .override(768, 432)
                        .placeholder(R.drawable.logo)
                        .skipMemoryCache(true)
                        .into(mImageView);
//                mImageView.setPath(imageList.get(position).getImagePath());

//                ImageLoader.getInstance().displayImage(imageList.get(position), mImageView);
                ImageLoader.getInstance().clearMemoryCache();



            }


            container.addView(itemView);
            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((RelativeLayout) object);
        }
    }

    public static FragmentProductDetailFullImage newInstance() {
       FragmentProductDetailFullImage mFragmentLoginSearch = new FragmentProductDetailFullImage();
        return mFragmentLoginSearch;
    }

    @Override
    public void doWork() {
        super.doWork();
        onResumeData();
    }

    public void onResumeData() {

    }

    @Override
    public void onResume() {
        super.onResume();
        mainActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        onResumeData();
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {

    }

    @Override
    public void onPageSelected(int position) {

    }
}