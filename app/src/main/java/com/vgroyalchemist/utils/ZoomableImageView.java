package com.vgroyalchemist.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.vgroyalchemist.R;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ZoomableImageView extends RelativeLayout {

    private ProgressBar mSpinner;

    private ImageView mImageView;

    private String mImagePath;

    private ZoomableImageDialog mDialog;

    private int mWarningImgIcon;

    private int mZoomableImageBackground;

    public ZoomableImageView(Context context) {
        this(context, null);
    }

    public ZoomableImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ZoomableImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        final TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ZoomableImageView, defStyleAttr, 0);

        final String imageUrl;

        try {
            mWarningImgIcon = ta.getResourceId(R.styleable.ZoomableImageView_error_icon, R.drawable.logo);
            mZoomableImageBackground = ta.getColor(R.styleable.ZoomableImageView_zoomable_image_background,
                    ContextCompat.getColor(context, android.R.color.white));
            imageUrl = ta.getString(R.styleable.ZoomableImageView_image_url);
        } finally {
            ta.recycle();
        }

        initImageLoader(context);
        intializeViews(context);

        if (imageUrl != null) {
            setPath(imageUrl);
        }
    }

    private void intializeViews(final Context context) {
        initializeImageHolder(context);
        initializeProgressBar(context);
    }

    /**
     * To set image url path and to display image on Ui.
     *
     * @param imgPath Url of ImageView.
     */
    public void setPath(String imgPath) {
        this.mImagePath = imgPath;
        loadImage();
    }

    private void loadImage() {
        ImageLoader.getInstance().displayImage(mImagePath, mImageView, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                mSpinner.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

                mSpinner.setVisibility(View.GONE);
//                mImageView.setImageResource(mWarningImgIcon);
                mImageView.setImageResource(R.drawable.logo);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                mSpinner.setVisibility(View.GONE);
                mImageView.setBackgroundResource(0);
                mImageView.setVisibility(VISIBLE);
            }
        });
    }

    private void initializeImageHolder(final Context context) {
        mImageView = new ImageView(context);
        final LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        mImageView.setLayoutParams(layoutParams);
//        mImageView.setBackgroundResource(R.drawable.bg_placeholder);

        /**
         * Set scale type from center inside to fit center on 12-12-2017 where image resize issue 250*250 & 1000*1000
         */
        mImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        addView(mImageView);
    }

    public void setScaleType(@NonNull ImageView.ScaleType scaleType) {
        mImageView.setScaleType(scaleType);
    }

    private void initializeProgressBar(final Context context) {
        mSpinner = new ProgressBar(context);
        final LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        mSpinner.setLayoutParams(layoutParams);
        addView(mSpinner);
    }

    public void setImageBackground(final int imgResID) {
        mImageView.setBackgroundResource(imgResID);
    }

    public void setImageResource(final int imgResID) {
        mSpinner.setVisibility(View.GONE);
        mImageView.setImageResource(imgResID);
    }


    private void initializeZoomableDialog(final Context context) {
        mDialog = new ZoomableImageDialog(context);
    }

    /**
     * To initialize Image loader with default configurations. This method will called once.
     *
     * @param context Context of View.
     */
    private void initImageLoader(Context context) {

        // If configuration is not set then set it right away
        if (!ImageLoader.getInstance().isInited()) {

            DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .bitmapConfig(Bitmap.Config.ARGB_8888)
                    .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                    .resetViewBeforeLoading(true)
                    .build();

            final ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
            config.threadPriority(Thread.NORM_PRIORITY - 2);
            config.denyCacheImageMultipleSizesInMemory();
            config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
            config.diskCacheSize(50 * 1024 * 1024);
            config.writeDebugLogs();
            config.memoryCache(new WeakMemoryCache());
            config.defaultDisplayImageOptions(defaultOptions);
            config.tasksProcessingOrder(QueueProcessingType.LIFO);

            // Initialize ImageLoader with configuration.
            ImageLoader.getInstance().init(config.build());
        }
    }

}
