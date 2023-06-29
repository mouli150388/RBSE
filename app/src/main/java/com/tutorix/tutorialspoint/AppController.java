package com.tutorix.tutorialspoint;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Process;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.Volley;
import com.facebook.FacebookSdk;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.tutorix.tutorialspoint.utility.Constants;
import com.zoho.commons.Color;
import com.zoho.salesiqembed.ZohoSalesIQ;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class AppController extends Application {

    private static final String TAG = AppController.class.getSimpleName();
    private static AppController mInstance;
    private RequestQueue mRequestQueue;
    public Typeface tf;
    public FirebaseAnalytics firebaseAnalytics ;
    public static int QUESTION_ASKED=0;
    //public DefaultTrackSelector.ParametersBuilder builder;
    public static int childQaulity=0;
    public static int childQaulityAudio=-1;
    public static int childQaulityAudioVideo=-1;
    public static int childQaulityText=-1;
    public int SpeedControll=1;
    public static synchronized AppController getInstance() {
        return mInstance;
    }

    private Thread.UncaughtExceptionHandler androidDefaultUEH;

    private Thread.UncaughtExceptionHandler handler = new Thread.UncaughtExceptionHandler() {
        public void uncaughtException(Thread thread, Throwable ex) {
            Log.e("TestApplication", "Uncaught exception is: ", ex);
            // log it & phone home.
            ActivityManager am = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
            if(am != null) {
                List<ActivityManager.AppTask> tasks = am.getAppTasks();
                if (tasks != null) {
                    tasks.get(0).finishAndRemoveTask();
                }
            }
            Intent intent = getBaseContext().getPackageManager()
                    .getLaunchIntentForPackage( getBaseContext().getPackageName());
                              intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                    startActivity(intent);

            Process.killProcess(Process.myPid());

            System.exit(0);
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();

        VolleyLog.setTag("Called : ");
        mInstance = this;
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);
       tf= Typeface.createFromAsset(getAssets(),"opensans_regular.ttf");
        //ZohoSalesIQ.init(this,"gQc2Dsmn512zhuEb4JxybKpYSoo8A6YfVOFbn%2FGu4IFvSVPGAAnt2tCYtaYurnpTOJCvHVOjjvzgP%2Fk6AuFmxP3GaITqJDG5_in","TlUFb%2BRBg1F8e0zp7FG3bnxyiEfT41ty9MNsKe2ZdOjkWJ%2BBDsJliv6HgVjm327uo82%2FIGzrE%2F5w3jgP4UNrU8eL5kjMdU2vxrdIiDkhIq70%2Fv8mcByPtjYFM%2Fk3olklSfoR24gbq9ZTQCHALvYrMmxNjbk7m0OoXAN5dhvc21i02YYV6rJF3idHIw4mrnEfAUmC5fO7c2W1jadLuJTDdg%3D%3D");
        ZohoSalesIQ.init(this,"vRyCnzkE6zWKiu4dwyZ7sjI37kpOL2%2BKJTFHWEnh3Mbeh3%2F0hvSx8CyTm%2BwHDCPz_in",
                "TlUFb%2BRBg1F8e0zp7FG3bnxyiEfT41tyLh1JfVKyhhm49PmbLIh%2FcuvpiLJPTazrccEuo6vPcDVahnfuhkh%2BkZJgtr1GKHY0STdK8VkOK%2FxCcrBnOUWiRa%2FKYbdONinG84AlRkp6PuuSDarS%2BRSSOzhsQGczMnQs3Qrmg9buEZU%3D");
        ZohoSalesIQ.Chat.setThemeColor("colorPrimary", new Color(57,51,155));

        try {
            FacebookSdk.setApplicationId(BuildConfig.facebook_app_id);
            FacebookSdk.sdkInitialize(getApplicationContext());
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        Configuration config= AppConfig.setLanguages(getApplicationContext());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            createConfigurationContext(config);

        getResources().updateConfiguration(config, getResources().getDisplayMetrics());

        //AppEventsLogger.activateApp(this);
        // Thread.setDefaultUncaughtExceptionHandler(handler);
    }

    private RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext(),new CurlHttpStack());
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        req.setRetryPolicy(new DefaultRetryPolicy(
                100000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        req.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

     MediaPlayer mp;
    public  void playAudio(int rawId)
    {
        if(!new SharedPref().isButtonEffects(getApplicationContext()))
        {
            return;
        }
        try {
            //if(mp==null)
            mp = MediaPlayer.create(this, rawId);

            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                public void onCompletion(MediaPlayer mp) {
                    mp.release();
                    mp = null;
                }
            });
            mp.start();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public  void playQuizAudio(int rawId)
    {
        if(!new SharedPref().isQuizEffects(getApplicationContext()))
        {
            return;
        }

       // if(mp==null)


                try {
                    mp = MediaPlayer.create(this, rawId);
                    mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        public void onCompletion(MediaPlayer mp) {
                            mp.release();
                            mp = null;
                        }
                    });
                    //mp.prepare();
                    mp.start();
                }catch (Exception e)
                {
                    e.printStackTrace();
                }



    }
    public void startLayoutAnimation(View container)
    {

        final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.layout_scale);
        //MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
        //  myAnim.setInterpolator(interpolator);
        container.startAnimation(myAnim);

    }
    public void startLayoutRoatateAnimation(View container)
    {

        final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.rotate_animation);
        //MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
        //  myAnim.setInterpolator(interpolator);
        container.startAnimation(myAnim);

    }
    public void startItemAnimation(View container)
    {
        final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.item_scale_center);
        //MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
        //  myAnim.setInterpolator(interpolator);
        container.startAnimation(myAnim);

    }
    public void startButtonAnimation(View container)
    {
        final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.button_scale);
        //MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
        //  myAnim.setInterpolator(interpolator);
        container.startAnimation(myAnim);

    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        Constants.isOpened=false;
        //System.out.println("OnClosed");
    }

    public void addNewUserEvents()
    {
        //Sets a user property to a given value.
        Bundle b=new Bundle();
        b .putString("fb_screen_class","SplashScreen");
        b .putString("App_started_by","NewUser");
        AppController.getInstance().firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SIGN_UP,b);
    }
    public void addSignupEvents(String mobile)
    {
        Bundle b=new Bundle();
        b .putString("fb_screen_class","Registration");
        b .putString("App_started_by","New User");
        b .putString("App_started_by_mobile",mobile);
        //Sets a user property to a given value.
        AppController.getInstance().firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SIGN_UP,b);
    }
    public void addLoginEvents(String mobile)
    {
        Bundle b=new Bundle();
        b .putString("fb_screen_class","LoginScreen");
        b .putString("App_started_by_mobile",mobile);
        //Sets a user property to a given value.
        AppController.getInstance().firebaseAnalytics.logEvent(FirebaseAnalytics.Event.LOGIN,b);
    }
    public void addBuyActivationEvents(String email,String id)
    {
        Bundle b=new Bundle();
        b .putString("fb_screen_class","BuyActivation");
        b .putString("App_started_by_email",email);
        b .putString("App_user_id",id);
        //Sets a user property to a given value.
        AppController.getInstance().firebaseAnalytics.logEvent(FirebaseAnalytics.Event.BEGIN_CHECKOUT,b);
    }
    public void addSubScribeEvents(String deviceid,String id)
    {
        Bundle b=new Bundle();
        b .putString("fb_screen_class","SubscribeActivation");
        b .putString("App_started_by_device_id",deviceid);
        b .putString("App_user_id",id);
        //Sets a user property to a given value.
        AppController.getInstance().firebaseAnalytics.logEvent("Subscribed",b);
    }
    public void addRenewEvents(String email,String id)
    {
        Bundle b=new Bundle();
        b .putString("fb_screen_class","BuyActivation");
        b .putString("App_started_by_email",email);
        b .putString("App_user_id",id);
        //Sets a user property to a given value.
        AppController.getInstance().firebaseAnalytics.logEvent("Renewal",b);
    }
    public void addDoubtsEvents(String accessToken,String id)
    {
        Bundle b=new Bundle();
        b .putString("fb_screen_class","Doubts");
        b .putString("App_user_accesstoken",accessToken);
        b .putString("App_user_id",id);
        //Sets a user property to a given value.
        AppController.getInstance().firebaseAnalytics.logEvent("Doubts",b);
    }
    public void addSearchEvents(String accessToken,String id,String search)
    {
        Bundle b=new Bundle();
        b .putString("fb_screen_class","SearchVideos");
        b .putString("App_user_accesstoken",accessToken);
        b .putString("App_user_search_text",search);
        b .putString("App_user_id",id);
        AppController.getInstance().firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SEARCH,b);
    }
    public void addSwitchClassEvents(String deviceid,String id,String currentCls,String swtchCls)
    {
        Bundle b=new Bundle();
        b .putString("fb_screen_class","SwitchClass");
        b .putString("App_device_id",deviceid);
        b .putString("App_user_id",id);
        b .putString("current_class",currentCls);
        b .putString("switch_class",swtchCls);
        //Sets a user property to a given value.
        AppController.getInstance().firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SEARCH,b);
    }
    private static class CurlHttpStack extends HurlStack {
        @Override
        public com.android.volley.toolbox.HttpResponse executeRequest(Request<?> request, Map<String, String> additionalHeaders) throws IOException, AuthFailureError {
            // Print cURL command here
            printCurlCommand(request, additionalHeaders);

            // Proceed with the request as usual
            return super.executeRequest(request, additionalHeaders);
        }

        private void printCurlCommand(Request<?> request, Map<String, String> additionalHeaders) throws AuthFailureError {
            String url = request.getUrl();
            String method = String.valueOf(request.getMethod());
            Map<String, String> headers = request.getHeaders();

            StringBuilder curlCommand = new StringBuilder();
            curlCommand.append("curl");
            curlCommand.append(" -X ").append(method);
            curlCommand.append(" \"").append(url).append("\"");

            for (Map.Entry<String, String> entry : headers.entrySet()) {
                curlCommand.append(" -H \"").append(entry.getKey()).append(": ").append(entry.getValue()).append("\"");
            }

            for (Map.Entry<String, String> entry : additionalHeaders.entrySet()) {
                curlCommand.append(" -H \"").append(entry.getKey()).append(": ").append(entry.getValue()).append("\"");
            }

            System.out.println(curlCommand.toString());
        }
    }
}
