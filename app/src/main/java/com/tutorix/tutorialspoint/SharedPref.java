package com.tutorix.tutorialspoint;

import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.tutorix.tutorialspoint.utility.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SharedPref {

    /*FCM DEVICE TOKEN*/

    private static SharedPreferences getPref(Context ctx) {
        return ctx.getSharedPreferences(Constants.userInfo, Context.MODE_PRIVATE);
    }

    public static void addToken(Context ctx, String token) {
        SharedPreferences.Editor editor = ctx.getSharedPreferences(Constants.firebaseToken, Context.MODE_PRIVATE).edit();
        editor.putString(Constants.firebaseToken, token);
        editor.apply();
    }

    public static String getToken(Context ctx) {
        return ctx.getSharedPreferences(Constants.firebaseToken, Context.MODE_PRIVATE).getString(Constants.firebaseToken, null);
    }

    public static  boolean isTokenAdded(Context ctx)
    {
        getPref(ctx).getBoolean("serverRegister", false);
        return false;
    }

    public static void registerToken(String token, final Context ctx) {
        final JSONObject fjson = new JSONObject();
        try {
            String android_id = Settings.Secure.getString(ctx.getContentResolver(), Settings.Secure.ANDROID_ID);

            fjson.put("device_id", android_id);
            fjson.put("firebase_token", token);
            fjson.put("user_id", getPref(ctx).getString(Constants.userId, ""));
        } catch (JSONException ex) {
            ex.printStackTrace();
        }

        String url = Constants.FIREBASE;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

               // Log.v("Response","Response "+response);
                //CommonUtils.showToast(ctx, response);
                getPref(ctx).edit().putBoolean("serverRegister", true).apply();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }

        ) {
            final String Key = AppConfig.ENC_KEY;
            final String message = fjson.toString();
            final String encryption = Security.encrypt(message, Key);

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                params.put(AppConfig.JSON_DATA, encryption);
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }


    public void setButtenEffect(Context ctx,boolean value)
    {
        getPref(ctx).edit().putBoolean("btn_effects", value).apply();
    }
     public void setQuizEffect(Context ctx,boolean value)
    {
        getPref(ctx).edit().putBoolean("quiz_effects", value).apply();
    }


    public boolean isButtonEffects(Context ctx)
    {
       return getPref(ctx).getBoolean("btn_effects", true);

    }
    public boolean isQuizEffects(Context ctx)
    {
       return getPref(ctx).getBoolean("quiz_effects", true);

    }
    public boolean isExpired(Context ctx)
    {
        return getPref(ctx).getBoolean("is_expired", false);

    }

    public void setExpired(Context ctx,boolean value)
    {

        getPref(ctx).edit().putBoolean("is_expired", value).apply();


    }

    public String getActivationStatus(Context ctx)
    {

        return getPref(ctx).getString("activationStatus","D" );

    }
    public void setActiveStatus(Context ctx,String activationStatus)
    {

        getPref(ctx).edit().putString("activationStatus", activationStatus).apply();

    }


    public void setCurrentDate(Context ctx,String currentDate)
    {

        getPref(ctx).edit().putString("curretn_date", currentDate).apply();

    }

    public String getCurrentDate(Context ctx,String currentDate)
    {

       return getPref(ctx).getString("curretn_date","");

    }

    public void setIsFirstime(Context ctx,boolean value)
    {
        getPref(ctx).edit().putBoolean("is_date_check", value).apply();
    }


    public boolean getIsDateCheckFirsttime(Context ctx)
    {

        return getPref(ctx).getBoolean("is_date_check",true );

    }


    public static long getDefaultsLong(Context ctx,String string)
    {
        return getPref(ctx).getLong(string,-1 );
    }

    public static void setDefaultsLong(Context ctx,String string,long time)
    {
        getPref(ctx).edit().putLong(string, time).apply();
    }


    //Called for Check User Expiery

    public static String expeiry_called="expeiry_called";
    public static String last_time="last_time";
    public static void setExpiery(Context ctx,boolean value)
    {
        getPref(ctx).edit().putBoolean(expeiry_called, value).apply();
    }

    public static boolean getExpeiryCalled(Context ctx)
    {
        return getPref(ctx).getBoolean(expeiry_called,false );
    }

    public static void setLastTime(Context ctx,long time)
    {
        getPref(ctx).edit().putLong(last_time, time).apply();
    }

    public static long getLastTime(Context ctx)
    {
        return getPref(ctx).getLong(last_time,0);
    }


    public static int getLastFilterSyncDate(Context ctx)
    {
         return getPref(ctx).getInt("filter_sync_date",-1 );
    }

    public static void setFilterSyncDate(Context ctx,int versions)
    {
        getPref(ctx).edit().putInt("filter_sync_date", versions).apply();
    }

    public static String getDoubtFilterData(Context ctx)
    {
        return getPref(ctx).getString("doubt_filter_data","" );
    }

    public static void setDoubtFilterData(Context ctx,String json)
    {
        getPref(ctx).edit().putString("doubt_filter_data", json).apply();
    }
}
