package com.vgroyalchemist.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.bumptech.glide.Glide;
import com.vgroyalchemist.CommonClass.CommonClass;
import com.vgroyalchemist.R;
import com.vgroyalchemist.controller.MainActivity;
import com.vgroyalchemist.vos.GetAllBannerMenuVO;
import java.util.ArrayList;

public class CustomAdapterInfinite extends InfinitePagerAdapter {

    Context mContext;
    LayoutInflater mLayoutInflater;

    String mStringAddButton = "";
    String mStringScaleType = "";

    SharedPreferences mSharedPreferencesAddButton;
    SharedPreferences mSharedPreferencesScale;

    DisplayMetrics metrics;
    int densityDpi;
    MainActivity mainActivity;

    int height, width;
    HolderBanner mHolderBanner;

    private ArrayList<GetAllBannerMenuVO> storeProductList = new ArrayList<GetAllBannerMenuVO>();


    public CustomAdapterInfinite(Activity context, ArrayList<GetAllBannerMenuVO> list) {

        this.mContext = context;
        this.mainActivity = (MainActivity) context;
        this.storeProductList = list;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);



        metrics = mContext.getResources().getDisplayMetrics();
        densityDpi = (int) (metrics.density * 160f);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        mainActivity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;
    }



    @Override
    public int getItemCount() {
        return storeProductList.size();
    }

    @Override
    public View getView(final int position, View view, ViewGroup container) {
//        view = mLayoutInflater.inflate(R.layout.row_medium_banner, container, false);

        if (view == null) {
            view = mainActivity.getLayoutInflater().inflate(R.layout.row_medium_banner, container, false);
            mHolderBanner = new HolderBanner();
            mHolderBanner.mImageView = view.findViewById(R.id.row_banner_imageview_main);
            mHolderBanner.mRelativeMed = view.findViewById(R.id.row_banner_main);
            view.setTag(mHolderBanner);
        } else {
            mHolderBanner = (HolderBanner) view.getTag();
        }

        int widthRelative = 0;

        // Device Height Width Set Image -----------------------------
        CommonClass cmn = new CommonClass();
        widthRelative = width ;
        int Height = cmn.HeightMedBanner(widthRelative);

        mHolderBanner.mImageView.getLayoutParams().height = Height;
        mHolderBanner.mImageView.getLayoutParams().width = widthRelative;
        mHolderBanner.mImageView.requestLayout();
        mHolderBanner.mImageView.setScaleType(ImageView.ScaleType.FIT_XY);


        int NewWidth = cmn.DeviceWidth_DP(mainActivity);

        Glide.with(mContext).load(storeProductList.get(position).getBanner() + "?width=" + NewWidth)
                .placeholder(R.drawable.logo).into(mHolderBanner.mImageView);



//        ClickGuard.guard(view);

        return view;
    }

    @Override
    public void destroyItem(View collection, int position, Object o) {
        View view = (View) o;
        ((InfiniteViewPager) collection).removeView(view);
        view = null;
    }

    private class HolderBanner {
        ImageView mImageView;
        RelativeLayout mRelativeMed;
    }

//    private class ViewHolder {
//        ImageView image;
//
//        public ViewHolder(View view) {
//            image = (ImageView) view.findViewById(R.id.row_banner_imageview_main);
//        }
//    }


}
