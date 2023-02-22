package com.vgroyalchemist.CommonClass;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.DisplayMetrics;
import android.util.Log;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.vgroyalchemist.utils.Tags;
import com.vgroyalchemist.utils.Utility;
import java.io.File;
import static android.content.Context.MODE_PRIVATE;
import static com.vgroyalchemist.controller.AppController.TAG;

public class CommonClass {

    public void FirebaseToken(final Context mcontext) {
        // Device Token
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {

                        String token = task.getResult();
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            token = "12345";
                            return;
                        }
                        Tags.mStringDeviceToken = token;
                        Utility.debugger("VT token Common Class pref..." + Tags.mStringDeviceToken);

                        SharedPreferences sharedPreferences = mcontext.getSharedPreferences(Tags.TAG_SHARED_PREFERENCE_APP_DEVICETOKEN,
                                MODE_PRIVATE);
                        SharedPreferences.Editor myEdit = sharedPreferences.edit();
                        myEdit.putString(Tags.REG_ID, token);
                        myEdit.commit();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(mcontext, "Issue in getting Device token. "
//                                + e.toString(), Toast.LENGTH_SHORT).show();
                        Tags.mStringDeviceToken = "12345";
                    }
                });
    }

    public static File Filedata(Context mContext) {
        File EXternalDirectory = mContext.getExternalFilesDir(null);
        return EXternalDirectory;
    }


    public static int DeviceWidth_DP(Activity context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        float density = context.getResources().getDisplayMetrics().density;
        float dpWidth = displayMetrics.widthPixels / density;
        int width = Math.round(dpWidth) + 150;
//        Log.v("log_tag","Final All Width " + width);
        return width;
    }

    public int HeightMedBanner(int DeviceWidth) {
        int mHeightValue = ((DeviceWidth * 670) / 1600);
        return mHeightValue;
    }

}
