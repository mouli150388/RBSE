package com.tutorix.tutorialspoint.alarmm;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;

import com.tutorix.tutorialspoint.R;
import com.tutorix.tutorialspoint.SplashScreen;
import com.tutorix.tutorialspoint.utility.Constants;

import java.util.Objects;
import java.util.Random;

import androidx.core.app.NotificationCompat;

public class AlarmReceiverNotification extends MyBroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        runNotification(context);
       // this.mySP = null;
       // new MyAlarmReciever(context).setAlarm();
    }

    public void runNotification(Context context) {
       // if (this.mySP.getDefaults(context.getString(C0263R.string.preference_cb_notification)) != 0) {
            System.out.println("OnNotification");
        String title="Tutorix";
        String message = "Dear Student, Looks like you stopped your learning with Tutorix. We are really missing you, come back and start your Simply Easy Learning";

        try{
                if(Constants.isOpened)
                {
                    MyAlarmReciever myalaram=new MyAlarmReciever(context);
                    myalaram.setAlarm();
                    return;
                }

               /* int NOTIFICATION_ID = 234;
                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    String CHANNEL_ID = "Reminder_Chanel";
                    CharSequence name = "Tutorix";
                    String Description = "Dear Student, Looks like you stopped your learning with Tutorix. We are really missing you, come back and start your Simply Easy Learning";
                    int importance = NotificationManager.IMPORTANCE_HIGH;
                    NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
                    mChannel.setDescription(Description);
                    mChannel.enableLights(true);
                    mChannel.setLightColor(Color.RED);
                    mChannel.enableVibration(true);
                    mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                    mChannel.setShowBadge(false);
                    notificationManager.createNotificationChannel(mChannel);
                }

                RemoteViews mContentView = new RemoteViews(context.getPackageName(), R.layout.notification_custom_layout);
                String channelId = "Reminder_Chanel";

                String contentText = "Dear Student, Looks like you stopped your learning with Tutorix. We are really missing you, come back and start your Simply Easy Learning";
                NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setAutoCancel(false)
                        .setSound(alarmSound)
                        .setOngoing(true)
                        .setContentTitle(context.getString(R.string.app_name))
                        .setContentText(contentText)

                        .setCustomContentView(mContentView);


                Notification notification;
                builder.setContentIntent(PendingIntent.getActivity(context, 0, new Intent(context, SplashScreen.class), 0));

                if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                    builder.setSmallIcon(R.mipmap.ic_launcher);
                    notification = builder.build();

                } else {
                    // Lollipop specific setColor method goes here.
                    builder.setSmallIcon(R.drawable.ic_stat_gif_phy);
                    notification = builder.build();

                    notification.color=(R.color.colorPrimary);

                }
                if(android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.O)
                    notification.priority=Notification.PRIORITY_HIGH;

                notificationManager.notify(NOTIFICATION_ID,notification);
*/


                int notificationId = new Random().nextInt(60000);
                NotificationCompat.Builder notificationBuilder;
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationChannel channel=new NotificationChannel("TUTORIX","TUTORIX",NotificationManager.IMPORTANCE_HIGH);

                    notificationBuilder = new NotificationCompat.Builder(context,"TUTORIX")
                            //a resource for your custom small icon
                            .setContentTitle(title) //the "title" value you sent in your notification
                            .setContentText(message) //ditto
                            .setAutoCancel(false)
                            .setSmallIcon(R.mipmap.ic_launcher);


                    notificationBuilder.setChannelId("TUTORIX");

                } else {
                    notificationBuilder = new NotificationCompat.Builder(context)
                            //a resource for your custom small icon
                            .setContentTitle(title) //the "title" value you sent in your notification
                            .setContentText(message) //ditto
                            .setAutoCancel(true)
                            .setSmallIcon(R.mipmap.ic_launcher);

                }
                Intent notificationIntent;

                    notificationIntent = new Intent(context, SplashScreen.class);
                    notificationIntent.putExtra(Constants.subjectId, "0");


                PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent,
                        0);

                notificationBuilder.setContentIntent(contentIntent);
                notificationBuilder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(),
                        R.mipmap.ic_launcher));

                    notificationBuilder.setStyle(new NotificationCompat.BigPictureStyle()
                            .bigPicture(BitmapFactory.decodeResource(context.getResources(),
                                    R.drawable.expertise)));
                Notification notification=notificationBuilder.build();

                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    notification.icon=(R.mipmap.ic_launcher_round);
                    notification.color=R.color.colorPrimary;

                } else {
                    notification.icon=(R.mipmap.ic_launcher);
                }


                NotificationManager notificationManager =
                        (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

                Objects.requireNonNull(notificationManager).notify(notificationId /* ID of notification */, notificationBuilder.build());



            }catch ( Exception e)
            {

            }



    }


}
