package com.vgroyalchemist;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.widget.RemoteViews;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.vgroyalchemist.controller.MainActivity;
import com.vgroyalchemist.controller.ReminderActivity;
import com.vgroyalchemist.utils.Tags;
import com.vgroyalchemist.utils.Utility;
import com.vgroyalchemist.vos.GetLoginData;
import com.vgroyalchemist.webservice.PostParseGet;

import java.util.List;
import java.util.Random;

/* developed by krishna 24-03-2021
 *  Set notification data */
public class MyFirebaseMessagingService extends FirebaseMessagingService {


    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();
    static String packagename = Tags.PACKAGENAME;
    private static ActivityManager mActivityManager;
    static Boolean Status = false;
    private PostParseGet mPostParseGet;
    private GetLoginData mGetLoginData;
//    private GetNotificationData mGetNotificationData;

    SharedPreferences mSharedPreferences;

    String mStringPushNotificationFlag = "";

    SharedPreferences mPreferences;
    SharedPreferences.Editor mEditor;
    int NOTIFICATION_ID = 1111;

    String mStringData = "";
    String mStringTitle = "";
    String mStringMsg = "";
    String mStringReminderId = "";
    String mStringReminder = "";
    String mStringMedicineName;
    String mStringTime;
    String mStringDosage;


    @Override
    public void onCreate() {
        super.onCreate();

        mPostParseGet = new PostParseGet(this);
        mGetLoginData = new GetLoginData();
//        mGetNotificationData = new GetNotificationData();

        mSharedPreferences = getSharedPreferences(Tags.TAG_SHARED_PREFERENCE_PUSH_NOTIFICATION, MODE_PRIVATE);
        mStringPushNotificationFlag = mSharedPreferences.getString(Tags.PUSH_NOTIFICATION_KEY, "");

        mPreferences = getSharedPreferences(Tags.TAG_SHARED_PREFERENCE_PUSH_NOTIFICATION_DATA, 0);
        mEditor = mPreferences.edit();

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {


        if (remoteMessage.getNotification() != null) {

        }

        if (remoteMessage == null) {
            return;
        }

        Utility.debugger("VT data body3..: " + remoteMessage.getData().toString());

        mStringData = remoteMessage.getData().toString();
        mStringTitle = remoteMessage.getData().get("title");
        mStringMsg = remoteMessage.getData().get("message");
        mStringReminderId = remoteMessage.getData().get("ScheduleReminderId");
        mStringReminder = remoteMessage.getData().get("reminder");
        mStringMedicineName = remoteMessage.getData().get("medicinename");
        mStringDosage = remoteMessage.getData().get("dosage");
        mStringTime = remoteMessage.getData().get("time");


//        mEditor.putString("notification_compalinId", remoteMessage.getData().get("ComplainId"));
//        mEditor.putString("notification_OrderId", remoteMessage.getData().get("OrderId"));
//        mEditor.putString("notification_MedicineId", remoteMessage.getData().get("MedicineId"));
//        mEditor.commit();


        Utility.debugger("VT noti flag...." + mStringPushNotificationFlag);
        if (!mStringPushNotificationFlag.equalsIgnoreCase("")) {
            if (mStringPushNotificationFlag.equalsIgnoreCase("Y")) {
                generateNotification(this, mStringTitle, mStringMsg);
            }
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void generateNotification(Context context, String mStringtitle, String message) {

        Utility.debugger("VT noti title firebase..." + mStringtitle);
        Utility.debugger("VT noti message firebase..." + message);


        if (mStringtitle != null && mStringtitle.equalsIgnoreCase("")) {
            mStringtitle = getString(R.string.app_name);
        }

        if (mStringtitle == null || mStringtitle.equalsIgnoreCase("null")) {
            mStringtitle = getString(R.string.app_name);
        }

        if (message == null) {
            message = "null";
        }


        String title = context.getString(R.string.app_name);
        long when = System.currentTimeMillis();
        Random random = new Random();
        int m = random.nextInt(9999 - 1000) + 1000;
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.row_remoteview);
        remoteViews.setImageViewResource(R.id.row_chat_relative_image, getNotificationIcon());
        remoteViews.setTextViewText(R.id.row_chat_textview_title, mStringtitle);
        remoteViews.setTextViewText(R.id.row_chat_textview_desc, message);

        if (getRunningPackage(context)) {
            Utility.debugger("VT noti 1st...");

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                SendNotification_GreaterVersion(context, mStringtitle, message);
            } else {
                Utility.debugger("VT noti 3rd...");
                //App in background
                SendNotification_OLDVersion(context, when, notificationManager, mStringtitle, message);
            }
        } else {
            Utility.debugger("VT noti 2nd...");

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                SendNotification_GreaterVersion(context, mStringtitle, message);
            } else {

                Utility.debugger("VT noti 4st...");
                SendNotification_OLDVersion(context, when, notificationManager, mStringtitle, message);
            }
        }

    }

    public int getNotificationIcon() {
        boolean whiteIcon = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP);
        return whiteIcon ? R.mipmap.ic_launcher : R.mipmap.ic_launcher;
    }

    public static Boolean getRunningPackage(Context context) {

        mActivityManager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);

        List<ActivityManager.RunningTaskInfo> infos = mActivityManager.getRunningTasks(1);
        for (int i = 0; i < infos.size(); i++) {
            ActivityManager.RunningTaskInfo info = infos.get(i);
            Status = !packagename.equalsIgnoreCase(info.topActivity.getPackageName());
        }
        return Status;
    }

    public Bitmap getNotificationIcon(Context mContext) {
        boolean whiteIcon = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP);
        if (whiteIcon) {
            return BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_launcher);
        } else {
            return BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_launcher);
        }
    }

    private void SendNotification_GreaterVersion(Context context, String Title, String Msg) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            //App in background
            if (mStringReminder.equalsIgnoreCase("true")) {
                Intent notificationIntent = new Intent(context, ReminderActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("ReminderId", mStringReminderId);
                bundle.putString("medicinename", mStringMedicineName);
                bundle.putString("dosage", mStringDosage);
                bundle.putString("time", mStringTime);
                notificationIntent.putExtras(bundle);
                notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                notificationIntent.putExtra(Tags.NOTIFICATION_KEY, "action");
                PendingIntent intent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);

                int notifyID = 1;
//                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bg_splash);
                String CHANNEL_ID = "VG Pharma";  // The id of the channel.
                int importance = NotificationManager.IMPORTANCE_HIGH;
                NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, Title, importance);
                mChannel.setDescription(Msg);
                mChannel.setShowBadge(true);
                mChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
                NotificationCompat.Builder notification;


                notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                        .setContentTitle(Title)
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(Msg))
                        .setContentText(Msg)
                        .setSmallIcon(getNotificationIcon())
                        .setChannelId(CHANNEL_ID)
                        .setAutoCancel(true)
                        .setDefaults(Notification.DEFAULT_ALL)
                        // .setCustomHeadsUpContentView(remoteViews)
//                        .setLargeIcon(lrg_Bitmap)
//                       .addAction(android.R.drawable.ic_delete, "VIEW", intent)
                        .setContentIntent(intent);


                notification.setSmallIcon(R.mipmap.ic_launcher);
                notification.setColor(getResources().getColor(R.color.parrot_green));

                NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                mNotificationManager.createNotificationChannel(mChannel);

                mNotificationManager.notify(notifyID, notification.build());
            } else {
                Intent notificationIntent = new Intent(context, MainActivity.class);
                notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                notificationIntent.putExtra(Tags.NOTIFICATION_KEY, "action");
                PendingIntent intent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);

                int notifyID = 1;
//                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bg_splash);
                String CHANNEL_ID = "VG Pharma";  // The id of the channel.
                int importance = NotificationManager.IMPORTANCE_HIGH;
                NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, Title, importance);
                mChannel.setDescription(Msg);
                mChannel.setShowBadge(true);
                mChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
                NotificationCompat.Builder notification;


                notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                        .setContentTitle(Title)
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(Msg))
                        .setContentText(Msg)
                        .setSmallIcon(getNotificationIcon())
                        .setChannelId(CHANNEL_ID)
                        .setAutoCancel(true)
                        .setDefaults(Notification.DEFAULT_ALL)
                        // .setCustomHeadsUpContentView(remoteViews)
//                        .setLargeIcon(lrg_Bitmap)
                        .setContentIntent(intent);


                notification.setSmallIcon(R.mipmap.ic_launcher);
                notification.setColor(getResources().getColor(R.color.parrot_green));

                NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                mNotificationManager.createNotificationChannel(mChannel);

                mNotificationManager.notify(notifyID, notification.build());
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void SendNotification_OLDVersion(Context context, long when, NotificationManager notificationManager, String Title, String Message) {

        if (mStringReminder.equalsIgnoreCase("true")) {
            Intent notificationIntent = new Intent(context, ReminderActivity.class);
            // set intent so it does not start a new activity
            Bundle bundle = new Bundle();
            bundle.putString("ReminderId", mStringReminderId);
            bundle.putString("medicinename", mStringMedicineName);
            bundle.putString("dosage", mStringDosage);
            bundle.putString("time", mStringTime);
            notificationIntent.putExtras(bundle);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            notificationIntent.putExtra(Tags.NOTIFICATION_KEY, "action");
//                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bg_splash);
            PendingIntent intent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(getNotificationIcon())
                    .setContentTitle(Title)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(Message))
                    .setContentText(Message)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setPriority(NotificationCompat.PRIORITY_MAX)
                    .setAutoCancel(true)
                    .setContentIntent(intent);

            notificationBuilder.setSmallIcon(R.mipmap.ic_launcher);
            notificationBuilder.setColor(getResources().getColor(R.color.parrot_green));



            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0, notificationBuilder.build());
//            notificationManager.notify(NOTIFICATION_ID++, notification);
        } else {
            Intent notificationIntent = new Intent(context, MainActivity.class);
            // set intent so it does not start a new activity
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            notificationIntent.putExtra(Tags.NOTIFICATION_KEY, "action");

//                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bg_splash);
            PendingIntent intent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//
            Notification notification;

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(getNotificationIcon())
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setContentTitle(Title)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(Message))
                    .setContentText(Message)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setAutoCancel(true)

                    .setContentIntent(intent);

            notificationBuilder.setSmallIcon(R.mipmap.ic_launcher);
            notificationBuilder.setColor(getResources().getColor(R.color.parrot_green));



            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0, notificationBuilder.build());
        }
    }
}
