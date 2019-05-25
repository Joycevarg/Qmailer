package com.example.qmailer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;


//class extending the Broadcast Receiver
public class MyAlarm extends BroadcastReceiver {

    DbInteract db;
    ConnectivityManager cm;

    //the method will be fired when the alarm is triggerred
//    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d("Lets", "start sending emails");
        SendMail.count=0;
        NotificationHelper.createNotificationChannel(context,
                NotificationManagerCompat.IMPORTANCE_DEFAULT, false,
                context.getString(R.string.app_name), "App notification channel.");

        NotificationHelper nh=new NotificationHelper();

            cm=(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if(cm.getActiveNetworkInfo()==null)
            {nh.noInternetNotification(1002);

                Log.d("No internet", "Emails not sent");}
            else{
            db = new DbInteract(context);
            db.sendMails();
            cm=null;
            Log.d("Successfully", "Emails sent");
            }

    }


}