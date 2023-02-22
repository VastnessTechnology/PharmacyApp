package com.vgroyalchemist.utils;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;

import com.vgroyalchemist.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import it.sephiroth.android.library.imagezoom.ImageViewTouch;


/**
 * To show zoomable image in a dialog.
 */

public class ZoomableImageDialog extends Dialog {

    // Image view which will take pan, zoom actions.
    private ImageViewTouch mIvZoom;

    // Main root View of Zoomable Image View.
    private View mRootView;

    public ZoomableImageDialog(@NonNull Context context) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_zoomable_image);
        mIvZoom = findViewById(R.id.iv_zoomable);
        mRootView = findViewById(android.R.id.content);
    }

    /**
     * To set image which is going to display on Zoomable View.
     * @param imageUrl Url of an image which is going to display.
     */
    public void setImage(@NonNull final String imageUrl) {
        ImageLoader.getInstance().displayImage(imageUrl, mIvZoom);
    }

    /**
     * To set background color of a Zoomable View.
     * @param bgColor Background color for Zoomable View.
     */
    public void setBackgroundColor(@ColorInt int bgColor) {
        mRootView.setBackgroundColor(bgColor);
    }

    @SuppressLint("ResourceType")
    public void setBackgroundImage(@ColorInt int bgColor) {
        mIvZoom.setBackgroundResource(bgColor);
    }
}
