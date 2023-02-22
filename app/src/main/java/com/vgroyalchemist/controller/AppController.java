package com.vgroyalchemist.controller;

import android.app.Application;
import android.content.Context;
import android.os.StrictMode;

import androidx.multidex.MultiDex;


import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.vgroyalchemist.BuildConfig;
import com.vgroyalchemist.R;
import com.vgroyalchemist.utils.Tags;

import org.acra.ACRA;
import org.acra.config.CoreConfigurationBuilder;
import org.acra.config.MailSenderConfigurationBuilder;
import org.acra.data.StringFormat;


//@AcraMailSender(
//        mailTo = "vastnesstechnology@gmail.com", customReportContent = {
//        ReportField.APP_VERSION_CODE, ReportField.APP_VERSION_NAME,
//        ReportField.ANDROID_VERSION, ReportField.PHONE_MODEL,
//        ReportField.CUSTOM_DATA, ReportField.STACK_TRACE, ReportField.LOGCAT}, mode =
//        ReportingInteractionMode.TOAST, resToastText = R.string.app_name)

//@AcraCore(buildConfigClass = BuildConfig.class,
//        reportFormat = StringFormat.JSON)

public class AppController extends Application {

    public static final String TAG = AppController.class.getSimpleName();
    private Tracker mTracker;
    private static AppController mInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);

        CoreConfigurationBuilder builder = new CoreConfigurationBuilder(this);
        builder.setBuildConfigClass(BuildConfig.class).setReportFormat(StringFormat.JSON);
//        builder.setReportField(ReportField.LOGCAT, true);
//        builder.getPluginConfigurationBuilder(ToastConfigurationBuilder.class).setResText(R.string.errorText).setEnabled(true);
        builder.getPluginConfigurationBuilder(MailSenderConfigurationBuilder.class)
                .setMailTo("vastnesstec@gmail.com")
                .setResSubject(R.string.ACRASubject)
                .setResBody(R.string.ErrorBody)
                .setReportFileName(Tags.PACKAGENAME + "_Crash_Report")
                .setReportAsFile(true)
                .setEnabled(true);
        ACRA.init(this, builder);

//        ACRA.init(this);
    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }


}