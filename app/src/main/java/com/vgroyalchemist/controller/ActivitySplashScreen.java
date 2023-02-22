package com.vgroyalchemist.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Base64;
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.vgroyalchemist.CommonClass.CommonClass;
import com.vgroyalchemist.R;
import com.vgroyalchemist.utils.CommonHelper;
import com.vgroyalchemist.utils.SweetAlertDialogSingleButton;
import com.vgroyalchemist.utils.Tags;
import com.vgroyalchemist.utils.Utility;
import com.vgroyalchemist.views.FragmentLoginScreen;

import org.apache.commons.lang.StringEscapeUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ActivitySplashScreen extends AppCompatActivity {


    // Device Token Check
    public SharedPreferences mSharedPreferencesDeviceToekn;
    public SharedPreferences.Editor mEditorDeviceToken;

    public SharedPreferences mSharedPreferencesLoginFirst;
    SharedPreferences.Editor editor;

    private Activity mcontext;
    CommonHelper mCommonHelper;
    String mStringReferalCode = "";
    public SharedPreferences.Editor mEditorReferallink;
    public SharedPreferences mSharedPreferencesReferalLink;

    public String mStringCurrencySymbol = "";
    boolean hasLoggedIn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        mcontext = ActivitySplashScreen.this;
        mCommonHelper =new CommonHelper();

        mSharedPreferencesDeviceToekn = getSharedPreferences(Tags.TAG_SHARED_PREFERENCE_APP_DEVICETOKEN, 0);
        Tags.CounterHome = 0;
        Tags.bln_Page_Not_Leftmenu = false;
        Tags.bln_HomePage = false;



        mSharedPreferencesReferalLink = getSharedPreferences(Tags.TAG_SHARED_PREFERENCE_REFERAL_CODE, 0);
        mEditorReferallink = mSharedPreferencesReferalLink.edit();

        // Device Token
        CommonClass cmn = new CommonClass();
        cmn.FirebaseToken(ActivitySplashScreen.this);

        mSharedPreferencesLoginFirst = getSharedPreferences(Tags.PREFS_NAME, 0);
        hasLoggedIn = mSharedPreferencesLoginFirst.getBoolean("hasLoggedIn", false);

        Utility.debugger("VT token shared pref..." + mSharedPreferencesDeviceToekn.getString(Tags.REG_ID, ""));
        if (!mSharedPreferencesDeviceToekn.getString(Tags.REG_ID, "").equalsIgnoreCase("")) {
            Tags.mStringDeviceToken = mSharedPreferencesDeviceToekn.getString(Tags.REG_ID, "");
        }


        if (Tags.mStringDeviceToken.equalsIgnoreCase("")) {
            Tags.mStringDeviceToken = "12345";
        }

        Utility.debugger("VT token in splash..." + Tags.mStringDeviceToken);

        mStringCurrencySymbol  ="\u20B9";

        if (!mStringCurrencySymbol.equalsIgnoreCase("")) {
            String unescapedText1 = StringEscapeUtils.unescapeHtml(mStringCurrencySymbol);
            Utility.debugger("vt Currency Symbol----" + unescapedText1);
            Tags.LABEL_CURRENCY_SIGN = unescapedText1;

        }

        Utility.debugger("VT token in splash..." + Tags.mStringDeviceToken);

//        mSharedPreferencesLoginFirst = mcontext.getSharedPreferences(Tags.PREFS_NAME, 0);
//        editor = mSharedPreferencesLoginFirst.edit();
//        editor.putBoolean("hasLoggedIn", true);
//        editor.commit();

        PackageInfo info;
        try {
            info = getPackageManager().getPackageInfo(Tags.PACKAGENAME, PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                //String something = new String(Base64.encodeBytes(md.digest()));
                Utility.debugger("VT hash key.." + something);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("no such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("exception", e.toString());
        }

          if (!mCommonHelper.check_Internet(this)) {
                SweetAlertDialogSingleButton mSweetAlertDialog = new SweetAlertDialogSingleButton(ActivitySplashScreen.this)
                        .setContentText(getResources().getString(R.string.interneterror))
                        .setConfirmText(getResources().getString(R.string.dialog_ok))
                        .setConfirmClickListener(new SweetAlertDialogSingleButton.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialogSingleButton sDialog) {
                                sDialog.dismissWithAnimation();
                                finish();
                            }
                        });
                mSweetAlertDialog.setCancelable(false);
                mSweetAlertDialog.show();
                return;
          }

        new CountDownTimer(2000, 1000) {

            public void onTick(long millisUntilFinished) {
//                mTextViewResnedTime.setText("seconds remaining: " + millisUntilFinished / 1000);
                //here you can have your logic to set text to edittext
            }

            public void onFinish() {

                if (hasLoggedIn) {
                    Intent mIntent = new Intent(ActivitySplashScreen.this, MainActivity.class);
                    startActivity(mIntent);
                    finish();
                }
                else {
                    Intent mIntent = new Intent(ActivitySplashScreen.this, IntroductionScreen.class);
                    startActivity(mIntent);
                    finish();
                }

            }

        }.start();


        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(ActivitySplashScreen.this.getIntent())
                .addOnSuccessListener(ActivitySplashScreen.this, new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                        // Get deep link from result (may be null if no link is found)
                        Uri uri = null;

                        if (pendingDynamicLinkData != null) {

                            uri = pendingDynamicLinkData.getLink();

                            System.out.println("VT deepLink  -------->" + uri.toString());
                            Utility.debugger("VT mStringDeeplinkingID link..." + pendingDynamicLinkData.getLink());
//
                            mStringReferalCode = uri.getQueryParameter("invitedby");

                            Utility.debugger("VT get Referal Code ...." + mStringReferalCode);
                            mEditorReferallink.putString("invitedby", mStringReferalCode);
                            mEditorReferallink.commit();

                        } else {
                            Utility.debugger("VT uri null....");

                            mStringReferalCode = "";
                            mEditorReferallink.putString("invitedby", mStringReferalCode);
                            mEditorReferallink.commit();
                        }
                    }
                })
                .addOnFailureListener(ActivitySplashScreen.this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Utility.debugger("VT getDynamicLink:onFailure" + e.toString());
                    }
                });

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
}