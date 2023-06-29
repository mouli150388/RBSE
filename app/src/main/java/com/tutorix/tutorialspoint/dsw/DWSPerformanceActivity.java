package com.tutorix.tutorialspoint.dsw;

import android.Manifest;
import android.app.DownloadManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.Html;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.tutorix.tutorialspoint.R;
import com.tutorix.tutorialspoint.views.CustomTextview;

import java.io.File;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DWSPerformanceActivity extends AppCompatActivity {


    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.txt_dsw_desc)
    CustomTextview txtDswDesc;
    @BindView(R.id.txt_selecteddiscount)
    CustomTextview txt_selecteddiscount;
    @BindView(R.id.txt_dsw_desc1)
    CustomTextview txtDswDesc1;
    @BindView(R.id.txt_dsw_desc2)
    CustomTextview txtDswDesc2;
    @BindView(R.id.txt_score)
    CustomTextview txtScore;
    @BindView(R.id.btn_download)
    Button btnDownload;
    @BindView(R.id.txt_call)
    CustomTextview txt_call;
    @BindString(R.string.youareselected)
    String youareselected;
    @BindString(R.string.youscored)
    String youaScore;
    @BindString(R.string.discountsctring)
    String discountsctring;
    @BindString(R.string.call_on)
    String call_on;
    private float degree = -225;
    DownloadManager downloadManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dwsperformance);
        ButterKnife.bind(this);
        if (Build.VERSION.SDK_INT >= 24) {
            txtDswDesc.setText(Html.fromHtml(youareselected,Html.FROM_HTML_OPTION_USE_CSS_COLORS));

        } else {
            txtDswDesc.setText(Html.fromHtml(youareselected));

        }
        downloadManager  = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);

           txtDswDesc2.setText("Chandu");
        txtScore.setText(String.format(youaScore,"80%"));
        txt_selecteddiscount.setText(String.format(discountsctring,"50%"));
        txt_call.setText(String.format(call_on,"9701418234"));
    }

    @OnClick({R.id.img_back, R.id.btn_download, R.id.txt_selecteddiscount,R.id.txt_call})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.txt_selecteddiscount:
            case R.id.txt_call:
                if(Build.VERSION.SDK_INT > 22) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 101);
                        return;
                    }
                }
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:9701418234"));
                startActivity(callIntent);
                break;
            case R.id.img_back:
                finish();
                break;
            case R.id.btn_download:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if(checkPermissionForStorage())
                    {
                        downLoadFile();
                        return;
                    }
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 300);

                    return;
                }
                downLoadFile();
                break;
        }
    }

    private void downLoadFile()
    {

        registerReceiver(onComplete,
                new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        String url="https://www.tutorialspoint.com/videotutorials/assets/videos/courses/510/images/course_510_image.png";

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        request.setAllowedOverRoaming(false);
        request.setTitle("GadgetSaint Downloading " + url.substring(url.lastIndexOf(("/"))));
        request.setDescription("Downloading " + url.substring(url.lastIndexOf(("/"))));
        request.setVisibleInDownloadsUi(true);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "/Tutorix/"  +url.substring(url.lastIndexOf(("/"))));


        long refid = downloadManager.enqueue(request);
    }
    private boolean checkPermissionForStorage() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    BroadcastReceiver onComplete = new BroadcastReceiver() {

        public void onReceive(Context ctxt, Intent intent) {


            long referenceId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);

            openDownloadedAttachment(ctxt,referenceId);




           /* NotificationCompat.Builder mBuilder;
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.P)
            {
                mBuilder = new NotificationCompat.Builder(DWSPerformanceActivity.this, CHANNEL_ID)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Certificate")
                        .setContentText("Download completed")

                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setContentText("Download completed")
                        .setContentIntent(snoozePendingIntent);

            }else
            {
                 mBuilder =
                        new NotificationCompat.Builder(DWSPerformanceActivity.this)
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .setContentTitle("Certificate")
                                .setContentText("Download completed")
                                .setContentIntent(snoozePendingIntent);

            }



                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(455, mBuilder.build());*/




        }
    };
    String CHANNEL_ID;
    private void createNotificationChannel() {

        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Tutorix";
            String description = "Download DSW Certificate";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public static Intent getOpenFileIntent(String path) {
        File file = new File(path);
        Uri uri = Uri.fromFile(file);
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        String extension = path.substring(path.lastIndexOf(".")+1);
        String type = mime.getMimeTypeFromExtension(extension);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, type);
        return intent;
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            unregisterReceiver(onComplete);
        }catch (Exception e)
        {
            e.printStackTrace();
        }



    }

    /**
     * Used to open the downloaded attachment.
     *
     * @param context    Content.
     * @param downloadId Id of the downloaded file to open.
     */
    private void openDownloadedAttachment(final Context context, final long downloadId) {
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(downloadId);
        Cursor cursor = downloadManager.query(query);
        if (cursor.moveToFirst()) {
            int downloadStatus = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
            String downloadLocalUri = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
            String downloadMimeType = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_MEDIA_TYPE));
            if ((downloadStatus == DownloadManager.STATUS_SUCCESSFUL) && downloadLocalUri != null) {
                openDownloadedAttachment(context, Uri.parse(downloadLocalUri), downloadMimeType);
            }
        }
        cursor.close();
    }

    /**
     * Used to open the downloaded attachment.
     * <p/>
     * 1. Fire intent to open download file using external application.
     *
     * 2. Note:
     * 2.a. We can't share fileUri directly to other application (because we will get FileUriExposedException from Android7.0).
     * 2.b. Hence we can only share content uri with other application.
     * 2.c. We must have declared FileProvider in manifest.
     * 2.c. Refer - https://developer.android.com/reference/android/support/v4/content/FileProvider.html
     *
     * @param context            Context.
     * @param attachmentUri      Uri of the downloaded attachment to be opened.
     * @param attachmentMimeType MimeType of the downloaded attachment.
     */
    private void openDownloadedAttachment(final Context context, Uri attachmentUri, final String attachmentMimeType) {
        if(attachmentUri!=null) {
            // Get Content Uri.
           /* if (ContentResolver.SCHEME_FILE.equals(attachmentUri.getScheme())) {
                // FileUri - Convert it to contentUri.
                File file = new File(attachmentUri.getPath());
                attachmentUri = FileProvider.getUriForFile(activity, "com.freshdesk.helpdesk.provider", file);;
            }*/

            Intent openAttachmentIntent = new Intent(Intent.ACTION_VIEW);
            openAttachmentIntent.setDataAndType(attachmentUri, attachmentMimeType);
            openAttachmentIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            try {
                context.startActivity(openAttachmentIntent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(context, "Open Failed", Toast.LENGTH_LONG).show();
            }
        }
    }
}
