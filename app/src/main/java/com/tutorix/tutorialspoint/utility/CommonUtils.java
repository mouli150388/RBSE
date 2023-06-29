package com.tutorix.tutorialspoint.utility;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.UiModeManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.tutorix.tutorialspoint.AppController;
import com.tutorix.tutorialspoint.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;

import static android.content.Context.UI_MODE_SERVICE;


public class CommonUtils {

    private static final int NOT_CONNECTED = 0;      // if device is not connected to internet
    private static final int WIFI = 1;               // if device is connected to wi-fi
    private static final int MOBILE = 2;             // if device is connected to mobile data
    public static SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");             // 2018-11-12 13:44:56
    public static SimpleDateFormat format_month=new SimpleDateFormat("dd-MMM-yyyy");
    public static SimpleDateFormat format_month_time=new SimpleDateFormat("dd-MMM-yyyy HH:mm aa");
    SimpleDateFormat dateFormat = new SimpleDateFormat(
            "dd-MM-yyyy HH:mm:ss", Locale.getDefault());// 2018-11-12 13:44:56

    public static String userPhone;
    public static int[]colors=new int[]{R.color.phy_background, R.color.che_background, R.color.math_background, R.color.bio_background};
    static Random random=new Random(colors.length-1);

    //public static String PASSWORD_REG="[a-zA-Z0-9@.#$%^&*_&]+$";
    public static String PASSWORD_REG="^[a-zA-Z0-9]{4,}$";

    static int previous=-1;
    static int current=-1;
    public static int doubt_filter_version=0;
    public static int getRandomColor()
    {

        if(previous<0)
        {
            previous= getCurrent();
         return    getRandomColor();

        }else if(previous==current) {

            previous=getCurrent();
            return getRandomColor();
        }
            else{
            current=previous;
            return colors[previous];
        }
    }
    private static int getCurrent()
    {
        return random.nextInt(3)+0;

    }

    // to check the type of connection and return respective values
    public static int getConnectionStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if(cm==null)
            return 0;
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                return WIFI;
            if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                return MOBILE;
        }
        return NOT_CONNECTED;
    }

    /**
     * Method checks if the app is in background or not
     */
    public static boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningProcesses = null;
        if (am != null) {
            runningProcesses = am.getRunningAppProcesses();
        }
        if (runningProcesses != null) {
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        }
        return !isInBackground;
    }

    public static void setFullScreen(Activity activity) {
        activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    public static void secureScreen(Activity activity) {
        activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);
    }


    /*public static AgeModel calculateAge(Date birthDate) {
        int years;
        int months;
        int days;

        //create calendar object for birth day
        Calendar birthDay = Calendar.getInstance();
        birthDay.setTimeInMillis(birthDate.getTime());

        //create calendar object for current day
        long currentTime = System.currentTimeMillis();
        Calendar now = Calendar.getInstance();
        now.setTimeInMillis(currentTime);

        //Get difference between years
        years = now.get(Calendar.YEAR) - birthDay.get(Calendar.YEAR);
        int currMonth = now.get(Calendar.MONTH) + 1;
        int birthMonth = birthDay.get(Calendar.MONTH) + 1;

        //Get difference between months
        months = currMonth - birthMonth;

        //if month difference is in negative then reduce years by one
        //and calculate the number of months.
        if (months < 0) {
            years--;
            months = 12 - birthMonth + currMonth;
            if (now.get(Calendar.DATE) < birthDay.get(Calendar.DATE))
                months--;
        } else if (months == 0 && now.get(Calendar.DATE) < birthDay.get(Calendar.DATE)) {
            years--;
            months = 11;
        }

        //Calculate the days
        if (now.get(Calendar.DATE) > birthDay.get(Calendar.DATE))
            days = now.get(Calendar.DATE) - birthDay.get(Calendar.DATE);
        else if (now.get(Calendar.DATE) < birthDay.get(Calendar.DATE)) {
            int today = now.get(Calendar.DAY_OF_MONTH);
            now.add(Calendar.MONTH, -1);
            days = now.getActualMaximum(Calendar.DAY_OF_MONTH) - birthDay.get(Calendar.DAY_OF_MONTH) + today;
        } else {
            days = 0;
            if (months == 12) {
                years++;
                months = 0;
            }
        }
        //Create new Age object
        return new AgeModel(days, months, years);
    }*/

    public static void showToast(Context ctx, String msg) {

        if(ctx==null)
            return;
        View layoutValue = LayoutInflater.from(ctx).inflate(R.layout.layout_customtoast, null);
        TextView text = layoutValue.findViewById(R.id.custom_toast);

        text.setText(msg);
        //Creating the Toast object
        text.setTypeface(AppController.getInstance().tf);
        Toast toast = new Toast(ctx);
        toast.setDuration(Toast.LENGTH_SHORT);
        text.setAnimation(AnimationUtils.loadAnimation(ctx,
                R.anim.right_in));
        // gravity, xOffset, yOffset
        toast.setGravity(Gravity.BOTTOM, 0, 100);
        toast.setView(layoutValue);//setting the view of custom toast layout
        toast.show();

    }
    public static void showToastLong(Context ctx, String msg) {

        if(ctx==null)
            return;
        View layoutValue = LayoutInflater.from(ctx).inflate(R.layout.layout_customtoast, null);
        TextView text = layoutValue.findViewById(R.id.custom_toast);

        text.setText(msg);
        //Creating the Toast object
        text.setTypeface(AppController.getInstance().tf);
        Toast toast = new Toast(ctx);
        toast.setDuration(Toast.LENGTH_LONG);
        text.setAnimation(AnimationUtils.loadAnimation(ctx,
                R.anim.right_in));
        // gravity, xOffset, yOffset
        toast.setGravity(Gravity.BOTTOM, 0, 100);
        toast.setView(layoutValue);//setting the view of custom toast layout
        toast.show();

    }
    public boolean isPackageInstalled(Context context, String packageName) {
        try {
            PackageManager packageManager = context.getPackageManager();
            packageManager.getPackageInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            //Log.d("firing ", "Fire in the hole");
            return false;
        }
    }

    public static long daysBetween(String date1, String date2) {
        String pattern = new String("yyyy-mm-dd");

        SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.ENGLISH);
        Date Date1 = null, Date2 = null;
        try {
            Date1 = sdf.parse(date1);
            Date2 = sdf.parse(date2);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return (Date2.getTime() - Date1.getTime()) / (24 * 60 * 60 * 1000);
    }

    public static boolean  checkActivation(String date1, String date2) {
        //String pattern = new String("yyyy-mm-dd");

        //SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.ENGLISH);


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date d = new Date();
        String cDate = sdf.format(d);
        Date d1, d2;
        try {
            d1 = sdf.parse(cDate);
            d2 = sdf.parse(date2);



            if (d1.getTime()>=d2.getTime() ) {


                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return true;
        }
       return true;
    }

    public static boolean  checkEqualdates(String date1, String date2) {
        String pattern = new String("yyyy-mm-dd");

        SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.ENGLISH);
        Date Date1 = null, Date2 = null;
        try {
            Date1 = sdf.parse(date1);
            Date2 = sdf.parse(date2);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return (Date2.getTime()> Date1.getTime());
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null&&activity.getCurrentFocus()!=null&&activity.getCurrentFocus().getWindowToken()!=null) {
            inputMethodManager.hideSoftInputFromWindow(Objects.requireNonNull(activity.getCurrentFocus()).getWindowToken(), 0);
        }
    }

    public static String getstringDate(String question_asked_time)
    {

        try {
           return format_month.format(format.parse(question_asked_time));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return question_asked_time;
    }

    public static String getstringDateAndTime(String question_asked_time)
    {

        try {
            return format_month_time.format(format.parse(question_asked_time));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return question_asked_time;
    }


    public static String getTimeInAMPM(String time)
    {
        try {
            return ampmformat.format(format.parse(time));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return "";
    }

    static String mntsScnd = new String("MM-SS");
    static DateFormat ampmformat = new SimpleDateFormat("hh:mm a");
    static SimpleDateFormat MntsScndsdf = new SimpleDateFormat("mm:ss");
    static SimpleDateFormat HrsMntsScndsdf = new SimpleDateFormat("hh:mm:s");
    public static SimpleDateFormat monthth = new SimpleDateFormat("dd MMM yyyy");
    public static SimpleDateFormat ddmmyyyy = new SimpleDateFormat("yyyy-MM-dd");

    public static String mntsScndFormat(String time)
    {
        try {
            if(time==null)
                return "";
            return  MntsScndsdf.format(HrsMntsScndsdf.parse(time));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }

  public static long  getSeconds(String time)
  {
      long duration=0;
      try {
         String[] units = time.split(":"); //will break the string up into an array
         int hours = Integer.parseInt(units[0]); //first element
         int minutes = Integer.parseInt(units[1]); //first element
         int seconds = Integer.parseInt(units[2]); //second element
          duration = 60 * 60 * hours + 60 * minutes + seconds;
     }catch (Exception e)
     {
         e.printStackTrace();
     }
      return duration;
  }
    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param dp A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent px equivalent to dp depending on device density
     */
    public static float convertDpToPixel(float dp, Context context){
        return dp * (context.getResources().getDisplayMetrics().densityDpi/ DisplayMetrics.DENSITY_DEFAULT);
    }


    public static boolean isTVAPP(Context ctx)
    {
        UiModeManager uiModeManager = (UiModeManager)ctx. getSystemService(UI_MODE_SERVICE);
        if (uiModeManager.getCurrentModeType() == Configuration.UI_MODE_TYPE_TELEVISION) {
            return true;
        }
        return false;
    }

    public static String daysBetween( String date2) {

        if(date2==null)
            return "";
        try{
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM d, yyyy : hh:mm a");


            return simpleDateFormat.format(Long.parseLong(date2));
        }catch (Exception e)
        {
            e.printStackTrace();
            return date2;
        }

    }
}
