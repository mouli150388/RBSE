package com.tutorix.tutorialspoint.alarmm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.tutorix.tutorialspoint.R;
import com.tutorix.tutorialspoint.SharedPref;

import java.util.Calendar;

public class MyAlarmReciever {
    static final int RQS_1 = 1;
    Context context;


    public MyAlarmReciever(Context context2) {
        this.context = context2;

       // this.mySP = new MySharedPreferences(context2);
    }

    public void setAlarm() {
        //long time = SharedPref.getDefaultsLong(context,context.getString(R.string.preference_days_notification));

        Calendar calNow = Calendar.getInstance();
        Calendar calSet = (Calendar) calNow.clone();

            calSet.add(Calendar.HOUR, 23);
            SharedPref.setDefaultsLong(context,context.getString(R.string.preference_days_notification), calSet.getTimeInMillis());

        setAlarm(new Intent(context, AlarmReceiverNotification.class), calSet);
    }

    public void setAlarm(Intent intent, Calendar calSet) {
        //((AlarmManager) this.context.getSystemService(Context.ALARM_SERVICE)).cancel(PendingIntent.getBroadcast(context, 1, intent, 0));
        if(Build.VERSION.SDK_INT>=31)
        ((AlarmManager) this.context.getSystemService(Context.ALARM_SERVICE)).set(AlarmManager.RTC_WAKEUP, calSet.getTimeInMillis(), PendingIntent.getBroadcast(context, 1, intent, PendingIntent.FLAG_IMMUTABLE));
       else  ((AlarmManager) this.context.getSystemService(Context.ALARM_SERVICE)).set(AlarmManager.RTC_WAKEUP, calSet.getTimeInMillis(), PendingIntent.getBroadcast(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT));
    }


}
