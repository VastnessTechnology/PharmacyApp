package com.vgroyalchemist.utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Debug;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;


import com.vgroyalchemist.controller.MainActivity;

import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonHelper {
    // Variable Declaration
    MainActivity mainActivity;


    boolean matchFound = false;

    public CommonHelper() {
    }

    public CommonHelper(Activity mActivity) {
        mainActivity = (MainActivity) mActivity;
    }



    public static String getScreenResolution(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        return "{" + width + "," + height + "}";
    }

    public boolean checkPhoneNumber(String email) {

        /**
         * Regex finalised by Mihin 21st Aug. 2017
         */
        Pattern pattern = Pattern.compile("^(\\+\\d{1,3}[- ]?)?\\d{9,15}$");

        Matcher matcher = pattern.matcher(email);
        matchFound = matcher.matches();

        return matchFound;
    }

    /**
     * Function which checks email-id validation
     *
     * @param email
     * @return The return will be true if the valid string is available and false if not.
     */
    public boolean checkEmail(String email) {

        Pattern pattern = Pattern.compile("[a-z0-9A-Z\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-z0-9][a-z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-z0-9][a-z0-9\\-]{0,25}" +
                ")+");

        Matcher matcher = pattern.matcher(email);

        matchFound = matcher.matches();

        return matchFound;
    }



    public boolean check_Internet(Context mContext) {
        ConnectivityManager mConnectivityManager;
        NetworkInfo mNetworkInfo;
        mConnectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();

        return mNetworkInfo != null && mNetworkInfo.isConnectedOrConnecting();
    }


    public void showKeyboard(Activity mActivity, View view) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.toggleSoftInputFromWindow(view.getApplicationWindowToken(), InputMethodManager.SHOW_FORCED, 0);
    }

    public void hideKeyboard(Activity mActivity) {
        // Check if no view has focus:
        View view = mActivity.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public void showError(TextView mTextView, int mVisibility, String mString) {
        mTextView.setVisibility(mVisibility);
        mTextView.setText(mString);
    }


    public void checkMemoryHeap() {
        Double allocated = new Double(Debug.getNativeHeapAllocatedSize()) / new Double((1048576));
        Double available = new Double(Debug.getNativeHeapSize()) / 1048576.0;
        Double free = new Double(Debug.getNativeHeapFreeSize()) / 1048576.0;
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        df.setMinimumFractionDigits(2);

        System.out.println("VT debug===================");
        System.out.println("VT debug heap native: allocated " + df.format(allocated) + "MB of " + df.format(available) + "MB (" + df.format(free) + "MB free)");
        System.out.println("VT debug memory: allocated: " + df.format(new Double(Runtime.getRuntime().totalMemory()/1048576)) + "MB of " + df.format(new Double(Runtime.getRuntime().maxMemory()/1048576))+ "MB (" + df.format(new Double(Runtime.getRuntime().freeMemory()/1048576)) +"MB free)");

        Runtime.getRuntime().gc();
    }

    public void freeMemory(){
//        Fresco.getImagePipeline().clearCaches();
        System.runFinalization();
        Runtime.getRuntime().gc();
//        System.gc();
    }


}