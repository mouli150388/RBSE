package com.tutorix.tutorialspoint;

import android.app.Activity;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.tutorix.tutorialspoint.activities.WelcomeActivity;
import com.tutorix.tutorialspoint.alarmm.MyAlarmReciever;
import com.tutorix.tutorialspoint.database.MyDatabase;
import com.tutorix.tutorialspoint.home.HomeTabActivity;
import com.tutorix.tutorialspoint.login.LoginActivity;
import com.tutorix.tutorialspoint.models.UserProfile;
import com.tutorix.tutorialspoint.registration.SchoolInfoActivity;
import com.tutorix.tutorialspoint.utility.Constants;
import com.zoho.salesiqembed.ZohoSalesIQ;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

//import com.crashlytics.android.Crashlytics;


public class SplashScreen extends AppCompatActivity {

    Activity _this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


      Configuration config= AppConfig.setLanguages(getApplicationContext());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            createConfigurationContext(config);

        getResources().updateConfiguration(config, getResources().getDisplayMetrics());

        setContentView(R.layout.activity_splash);
        Constants.isOpened=true;
        MyAlarmReciever receiver=new MyAlarmReciever(getApplicationContext());
        receiver.setAlarm();
        try{
            TextView txt_bottom=findViewById(R.id.txt_bottom);
            txt_bottom.setText(String.format(getString(R.string.copyright_update), Calendar.getInstance().get(Calendar.YEAR)+""));

        }catch (Exception e)
        {
            e.printStackTrace();
        }


        /*if(!checkpackageinstaller())
        {
            showAlert("App installed from non authorized application");

            return;
        }

        if(EmulatorDetector.isEmulator(getApplicationContext()))
        {
            showAlert("Your device doesn't support");
            return;
        }
        try {
            WifiManager wm = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
            String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
            if (ip != null && ip.startsWith("10.0")) {
                showAlert("Your device doesn't support in this network");

                return;
            }
            ip = getLocalIpAddress();
            if (ip != null && ip.startsWith("10.0")) {
                CommonUtils.showToast(getApplicationContext(), "Your device doesn't support");
                return;
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }*/
       // FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true);
       NotificationManager manager= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
         manager.cancel(234);
        if(new SessionManager().isLoggedIn(getApplicationContext())){
            String user_id=new SessionManager().getUserInfo(getApplicationContext())[0];
            UserProfile profile= MyDatabase.getDatabase(getApplicationContext()).userDAO().getUserProfile(user_id);
            if(profile!=null)
            {
                Map<String, String> userMeta = new HashMap<String, String>();
                userMeta.put("user_id", user_id);
                userMeta.put("Name", profile.full_name);
                userMeta.put("mobile_number", profile.mobile_number);
                try {
                    ZohoSalesIQ.registerVisitor(user_id);
                    ZohoSalesIQ.Visitor.setName(profile.full_name);
                    ZohoSalesIQ.Visitor.setContactNumber(profile.mobile_number);
                    ZohoSalesIQ.Visitor.setEmail(profile.email_id);
                    /*FirebaseCrashlytics crashlytics = FirebaseCrashlytics.getInstance();
                    crashlytics.setUserId(profile.mobile_number);
                    crashlytics.setCustomKey("Email_id",profile.email_id);
                    crashlytics.setCustomKey("User_Name",profile.full_name);
*/

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else
            {
               /* FirebaseCrashlytics crashlytics = FirebaseCrashlytics.getInstance();
                crashlytics.setUserId("New User");
                crashlytics.setCustomKey("Email_id","");
                crashlytics.setCustomKey("User_Name","");
*/


                //Sets a user property to a given value.
                AppController.getInstance().addNewUserEvents();

            }



            //int k=1/0;
            //Freshchat.getInstance(getApplicationContext()).setUserProperties(userMeta);
        }


        callHindiAvailability();

        _this=this;
        //Fabric.with(this, new Crashlytics.Builder().core(new CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build()).build());

    }

    private void timeout() {
        int splashTimeout = 500;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (new SessionManager().isLoggedIn(_this)) {
                    String user_session[]=new SessionManager().getUserInfo(getApplicationContext());
                    String user_id=user_session[0];
                    UserProfile profile= MyDatabase.getDatabase(getApplicationContext()).userDAO().getUserProfile(user_id);
                    if(profile!=null)
                    {
                        String user_school_status=user_session[7];
;                        if(user_school_status!=null&&user_school_status.equalsIgnoreCase("Y"))
                    {
                        Intent intent = new Intent(_this, HomeTabActivity.class);
                        startActivity(intent);
                    }else {
                        Intent intent = new Intent(_this, SchoolInfoActivity.class);
                        startActivity(intent);
                    }
                        overridePendingTransition(R.anim.right_in, R.anim.left_out);
                        finish();
                    }else
                    {
                        Intent i = new Intent(_this, LoginActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                        overridePendingTransition(R.anim.right_in, R.anim.left_out);
                        finish();
                    }

                } else {
                    Intent i = new Intent(_this, LoginActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    overridePendingTransition(R.anim.right_in, R.anim.left_out);
                    finish();
                }
            }
        }, splashTimeout);
    }


    public String getLocalIpAddress() {
        try {

            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        String ip = Formatter.formatIpAddress(inetAddress.hashCode());

                        return ip;
                    }
                }
            }
        } catch (SocketException ex) {

        }
        return "";
    }
    public static boolean isEmulator() {
        return Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
                || "google_sdk".equals(Build.PRODUCT);
    }

    private boolean checkpackageinstaller()
    {
        try {
            String installer = getPackageManager()
                    .getInstallerPackageName(getPackageName());

            if(installer!=null && installer.contains("com.android.vending"))
                return true;
        } catch (Throwable e) {
        }
        return false;
    }

    Dialog dialog;

    public void showAlert(String msg) {

        dialog=new Dialog(SplashScreen.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.dialog_alert);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        TextView txt_msg=dialog.findViewById(R.id.txt_msg);
        txt_msg.setText(msg);
        TextView txt_cancel=dialog.findViewById(R.id.txt_cancel);
        TextView txt_ok=dialog.findViewById(R.id.txt_ok);
        txt_ok.setText("OK");
        txt_cancel.setText("X");
        txt_cancel.setVisibility(View.GONE);
        txt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        txt_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                dialog.dismiss();



            }
        });
        dialog.show();
    }

     private void crashlistics()
    {
        // Log the onCreate event, this will also be printed in logcat
        //Crashlytics.log(Log.VERBOSE, "", "onCreate");

        // Add some custom values and identifiers to be included in crash reports
        String user_id="";
        if(!new SessionManager().isLoggedIn(_this))
            user_id="Not loged in";
        else
        {
            user_id=new SessionManager().getUserInfo(getApplicationContext())[0];
        }
       // Crashlytics.setString("user_id", user_id);
       // Crashlytics.setString("Screen", "Splash");



        // Report a non-fatal exception, for demonstration purposes
        //Crashlytics.logException(new Exception("Non-fatal exception: something went wrong!"));

    }

    public void callHindiAvailability()
    {
        StringRequest request=new StringRequest(Request.Method.GET,"https://d2vgb5tug4mj1f.cloudfront.net/classes_hindi.json", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject obj=new JSONObject(response);
                    if(obj.has("error_flag"))
                    {
                        AppConfig.HINDI_AVAILABLE_CLASSES=obj.getString("hindi");
                    }else
                    {

                    }

                    Log.v(" AppConfigS","  AppConfig.HINDI_AVAILABLE_CLASSES"+  AppConfig.HINDI_AVAILABLE_CLASSES);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if(!new SessionManager().isViewedWelcomPages(getApplicationContext()))
                {
                    startActivity(new Intent(getApplicationContext(), WelcomeActivity.class));
                    finish();
                    return;
                }
                //  crashlistics();
                timeout();


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if(!new SessionManager().isViewedWelcomPages(getApplicationContext()))
                {
                    startActivity(new Intent(getApplicationContext(), WelcomeActivity.class));
                    finish();
                    return;
                }
                //  crashlistics();
                timeout();
            }
        });
        AppController.getInstance().addToRequestQueue(request, "tag_string_req");
    }
}
