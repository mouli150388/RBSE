package com.tutorix.tutorialspoint;

import android.content.Context;
import android.content.SharedPreferences;

import com.tutorix.tutorialspoint.utility.Constants;

public class SessionManager {
    private static final String PREF_NAME = "Tutorix";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_ACCEES_TOKEN = Constants.accessToken;
    private static final String KEY_DEVICE_ID = Constants.deviceId;
    private static final String KEY_ACTIVATION_TYPE = Constants.activationKey;
    private static final String KEY_CLASS_ID = Constants.classId;
    private static final String KEY_DATA_URL = Constants.DATA_URL;
    private static final String TUTORIX_SCHOOL_FLAG = Constants.TUTORIX_SCHOOL_FLAG;
    private static final String KEY_SECURE_DATA_URL = Constants.SECURE_DATA_URL;
    private static final String TAG = SessionManager.class.getSimpleName();

    public static void setLogin(Context context,String user_id,String accessToken,String deviceId,String activationType,String class_id,String secure_url,String data_url,String tutorix_school_flag) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit();
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.putString(KEY_USER_ID, user_id);
        editor.putString(KEY_ACCEES_TOKEN, accessToken);
        editor.putString(KEY_ACTIVATION_TYPE, activationType);
        editor.putString(KEY_DEVICE_ID, deviceId);
        editor.putString(KEY_CLASS_ID, class_id);
        editor.putString(KEY_SECURE_DATA_URL, secure_url);
        editor.putString(KEY_DATA_URL, data_url);
        editor.putString(TUTORIX_SCHOOL_FLAG, tutorix_school_flag);

        editor.apply();
        if(secure_url!=null&&!secure_url.isEmpty())
            AppConfig.ACTIVATION_DATA_URL=secure_url;
        if(data_url!=null&&!data_url.isEmpty())
            AppConfig.NORMAL_DATA_URL=data_url;
        //Log.d(TAG, "User Sign-up Activity session modified!");
    }

    public static void logoutUser(Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit();
        editor.putString(KEY_USER_ID, "");
        editor.putBoolean(KEY_IS_LOGGED_IN, false);
        editor.apply();
    }

    public static boolean isLoggedIn(Context context) {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return pref.getBoolean(KEY_IS_LOGGED_IN, false);
    }
    public static String[] getUserInfo(Context context) {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String []userInfo=new String[8];
        userInfo[0]=pref.getString(KEY_USER_ID,"");
        userInfo[1]=pref.getString(KEY_ACCEES_TOKEN,"");
        userInfo[2]=pref.getString(KEY_ACTIVATION_TYPE,"");
        userInfo[3]=pref.getString(KEY_DEVICE_ID,"");
        userInfo[4]=pref.getString(KEY_CLASS_ID,"");
        userInfo[5]=pref.getString(KEY_SECURE_DATA_URL,"");
        userInfo[6]=pref.getString(KEY_DATA_URL,"");
        userInfo[7]=pref.getString(TUTORIX_SCHOOL_FLAG,"");
        return userInfo;
    }

    public static void setSchoolSetUp(Context context,String status) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit();
        editor.putString(TUTORIX_SCHOOL_FLAG, status);

        editor.apply();
    }

        public static void setUserAccessToken(Context context,String accessToken)
    {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit();
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.putString(KEY_ACCEES_TOKEN, accessToken);
        editor.apply();
    }



    public static void setSecureURL(String secure_url,Context context)
    {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit();
        editor.putString(KEY_SECURE_DATA_URL, secure_url);
        editor.apply();
        if(secure_url!=null&&!secure_url.isEmpty())
            AppConfig.ACTIVATION_DATA_URL=secure_url;

    }
    public static boolean isViewedWelcomPages(Context context)
    {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return  pref.getBoolean("welcomscreens",false);
    }
    public static void viewedWelcomPages(Context context)
    {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=pref.edit();
        editor.putBoolean("welcomscreens",true);
        editor.commit();

    }

    public static void setAudioLanguage(Context context,String language)
    {

        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=pref.edit();
        editor.putString("audio_language",language);
        editor.commit();
    }

    public static String getAudioLanguage(Context context)
    {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return  pref.getString("audio_language","english");
    }

    public static void setListView(Context context,boolean value)
    {

        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=pref.edit();
        editor.putBoolean("isListview",value);
        editor.commit();
    }

    public static boolean isViewList(Context context)
    {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return  pref.getBoolean("isListview",true);
    }


    public static int getVideoQuality(Context context)
    {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return  pref.getInt("video_quality",0);
    }

    public static void setVideoQuality(Context context,int quality)
    {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=pref.edit();
        editor.putInt("video_quality",quality).commit();
    }


    public static int getAudio(Context context)
    {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return  pref.getInt("audio_quality",1);
    }

    public static void setAudio(Context context,int quality)
    {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=pref.edit();
        editor.putInt("audio_quality",quality).commit();
    }

    public static int getAudioVideo(Context context)
    {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return  pref.getInt("audio_quality_video",1);
    }
    public static void setAudioVideo(Context context,int quality)
    {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=pref.edit();
        editor.putInt("audio_quality_video",quality).commit();
    }

    public static int getAudioText(Context context)
    {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return  pref.getInt("audio_text",-1);
    }

    public static void setAudioText(Context context,int quality)
    {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=pref.edit();
        editor.putInt("audio_text",quality).commit();
    }
}