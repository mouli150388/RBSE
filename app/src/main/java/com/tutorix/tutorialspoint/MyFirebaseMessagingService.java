package com.tutorix.tutorialspoint;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.tutorix.tutorialspoint.activities.AllNotificationActivity;
import com.tutorix.tutorialspoint.database.MyDatabase;
import com.tutorix.tutorialspoint.home.HomeTabActivity;
import com.tutorix.tutorialspoint.models.Notifications;
import com.tutorix.tutorialspoint.utility.Constants;
import com.zoho.salesiqembed.ZohoSalesIQ;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

import androidx.core.app.NotificationCompat;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "FCM Service";
    private String message;
    private String title="Tutorix";
    private String ntype="";
    private String image="";


    @Override
    public void onNewToken(String token) {
        if (new SessionManager().isLoggedIn(getApplicationContext())) {
            SharedPref.registerToken(token, getApplicationContext());
        } else {
            SharedPref.addToken(getApplicationContext(), token);
        }
        //Freshchat.getInstance(this).setPushRegistrationToken(token);
        ZohoSalesIQ.Notification.enablePush(token, false);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        /*if (Freshchat.isFreshchatNotification(remoteMessage)) {
            Freshchat.getInstance(this).handleFcmMessage(remoteMessage);
            return;
        }*/



        Map<String, String> params = remoteMessage.getData();
        remoteMessage.getNotification();

        JSONObject object = new JSONObject(params);
      //  remoteMessage.getNotification().toString();

        if(object==null||!object.has("ntype"))
        {
            ZohoSalesIQ.Notification.handle(this.getApplicationContext(),params, R.mipmap.ic_launcher);
            return;
        }
        try {
            message = object.getString("text");
            title = object.getString("title");
            ntype = object.getString("ntype");
            if(object.has("image"))
            image = object.getString("image");

        } catch (JSONException e) {
            e.printStackTrace();
        }


        if(ntype.equals("N"))
        {
            new SendNotification(this).execute();
            return;
        }

        createNotification(BitmapFactory.decodeResource(getApplicationContext().getResources(),
                R.mipmap.ic_launcher),0);
    }


    private class SendNotification extends AsyncTask<String, Void, Bitmap> {

        Context ctx;


        public SendNotification(Context context) {
            super();
            this.ctx = context;
        }

        @Override
        protected Bitmap doInBackground(String... params) {

            InputStream in;

            try {

                URL url = new URL(image);
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                in = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(in);
                return myBitmap;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {

            super.onPostExecute(result);
            try {


                    createNotification(result,1);


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private void createNotification(Bitmap bitmap,int what)
    {

        int notificationId = new Random().nextInt(60000);
        NotificationCompat.Builder notificationBuilder;
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel=new NotificationChannel("TUTORIX","TUTORIX",NotificationManager.IMPORTANCE_HIGH);

            notificationBuilder = new NotificationCompat.Builder(getApplicationContext(),"TUTORIX")
                    //a resource for your custom small icon
                    .setContentTitle(title) //the "title" value you sent in your notification
                    .setContentText(message) //ditto
                    .setAutoCancel(true)
                    .setSmallIcon(R.mipmap.ic_launcher);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

            notificationBuilder.setChannelId("TUTORIX");

        } else {
            notificationBuilder = new NotificationCompat.Builder(getApplicationContext())
                    //a resource for your custom small icon
                    .setContentTitle(title) //the "title" value you sent in your notification
                    .setContentText(message) //ditto
                    .setAutoCancel(true)
                    .setSmallIcon(R.mipmap.ic_launcher);

        }
        Intent notificationIntent;
        if(ntype!=null&&ntype.equalsIgnoreCase("Q")) {
            notificationIntent = new Intent(getApplicationContext(), HomeTabActivity.class);
            notificationIntent.putExtra(Constants.subjectId, "0");
        }
        else {
            notificationIntent = new Intent(getApplicationContext(), HomeTabActivity.class);
            Notifications notifications=new Notifications();
            notifications.title=title;
            notifications.message=message;
            notifications.image=image;

            notifications.time=Calendar.getInstance().getTimeInMillis()+"";
            MyDatabase.getDatabase(getApplicationContext()).notificationsDAO().insert(notifications);
        }
        PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        notificationBuilder.setContentIntent(contentIntent);
        notificationBuilder.setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(),
                R.mipmap.ic_launcher));
                if(what==1)
        notificationBuilder.setStyle(new NotificationCompat.BigPictureStyle()
                .bigPicture(bitmap));
        Notification notification=notificationBuilder.build();

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notification.icon=(R.drawable.default_notification);
            notification.color=getResources().getColor(R.color.ColorPrimary);

        } else {
            notification.icon=(R.mipmap.ic_launcher);
        }


        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Objects.requireNonNull(notificationManager).notify(notificationId /* ID of notification */, notification);



    }

}
