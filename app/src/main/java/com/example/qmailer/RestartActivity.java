package com.example.qmailer;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

public class RestartActivity extends BroadcastReceiver {

    private static final String LOG_TAG = "RestartAppReceiver";


    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent != null) {
            String action = intent.getAction();

            switch (action) {
                case Intent.ACTION_BOOT_COMPLETED:
                    Log.i(LOG_TAG, "Start resetting alarms after reboot");

                    SharedPreferences sharedPref = context.getSharedPreferences("Qmailer",Context.MODE_PRIVATE);
                    boolean isOnline =sharedPref.getBoolean("Online", false);
                    if(isOnline) {
                        long time=sharedPref.getLong("Time",0);
                        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

                        //creating a new intent specifying the broadcast receiver
                        Intent i = new Intent(context, MyAlarm.class);

                        //creating a pending intent using the intent
                        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);

                        //setting the repeating alarm that will be fired every day
                        am.setRepeating(AlarmManager.RTC, time, AlarmManager.INTERVAL_DAY/3, pi);
                        Toast.makeText(context, "Qmailer running", Toast.LENGTH_SHORT).show();
                    }
                    Log.i(LOG_TAG, "Finish resetting alarms after reboot");
                    break;
                default:
                    break;
            }
        }
    }


}
