package com.vgroyalchemist.adapter;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.PagerAdapter;


import com.bumptech.glide.Glide;
import com.vgroyalchemist.CommonClass.CommonClass;
import com.vgroyalchemist.R;
import com.vgroyalchemist.controller.MainActivity;
import com.vgroyalchemist.utils.ImageLoader;
import com.vgroyalchemist.utils.Tags;
import com.vgroyalchemist.utils.ZoomableImageView;
import com.vgroyalchemist.views.FragmentProductDetailFullImage;
import com.vgroyalchemist.vos.MedicineImagevo;
import com.vgroyalchemist.vos.ProductDetailVO;

import java.util.ArrayList;

public class ProductDetailBannerAdapter extends PagerAdapter {

    MainActivity mActivity;
    ArrayList<MedicineImagevo> imageList;
    ImageLoader imageLoader;
    ArrayList<ProductDetailVO> mProductDetailVOS;

    public ProductDetailBannerAdapter(MainActivity mContext, ArrayList<MedicineImagevo> imageList, ArrayList<ProductDetailVO> productDetailVOArrayList) {
        this.mActivity = mContext;
        this.imageList = imageList;
        this.mProductDetailVOS = productDetailVOArrayList;
        imageLoader = new ImageLoader(mActivity);
    }

    @Override
    public int getCount() {
        return imageList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        View itemView = LayoutInflater.from(mActivity).inflate(R.layout.item_pager_image_detail, container, false);


        ZoomableImageView mImageView = itemView.findViewById(R.id.item_pager_image_detail_imageview);

        mImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);

        CommonClass cmn = new CommonClass();



        if (imageList.get(position).getImagePath() != null  &&  !imageList.get(position).getImagePath().equalsIgnoreCase("null")) {
            mImageView.setPath(imageList.get(position).getImagePath());

        }

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentProductDetailFullImage mFragmentListData = new FragmentProductDetailFullImage();
                Bundle mBundle = new Bundle();
                mBundle.putSerializable(Tags.IMAGES_INTENT, imageList);
                mBundle.putInt(Tags.IMAGES_POSITION, position);
                mFragmentListData.setArguments(mBundle);
                mActivity.addFragment(mFragmentListData, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, mFragmentListData.getClass().getName());
            }
        });

        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }
}