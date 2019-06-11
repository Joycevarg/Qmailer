package com.example.qmailer;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

public class NotificationHelper {


     static NotificationChannel channel;
     static Context context;
    String channelId = "Emailer";




    static void createNotificationChannel(Context cont, Integer importance, boolean showBadge, String name, String description)
    {
        context=cont;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            String channelId = "Emailer";
            channel =new NotificationChannel(channelId, name, importance);
            channel.setDescription(description);
            channel.setShowBadge(showBadge);
            channel.setSound(null,null);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

        }
    }

    void noInternetNotification(int id)
    {

        Intent i = new Intent(context, MyAlarm.class);

        //creating a pending intent using the intent
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);


        NotificationCompat.Builder notificationBuilder =new NotificationCompat.Builder(context, channelId)
        .setSmallIcon(R.drawable.notif)
                .setColor(R.drawable.ic_launcher_background)
        .setContentTitle("No Internet")
        .setContentText("Dont forget to open app and send mail")
         .addAction(R.drawable.notification_icon,"Sent mails",pi)
         .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setAutoCancel(true)
                .setOngoing(true); // 8
        sendNotification(notificationBuilder,id);
    }

 void notificationcancel(int id)
 {
     NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

     notificationManager.cancel(id);

 }


    void sendingNotification(int id,int progress,int max)
    {

        NotificationCompat.Builder notificationBuilder =new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.notif)
                .setColor(R.drawable.ic_launcher_background)
                .setContentTitle("We're sending out mails")
                .setContentText("Do not turn off internet")
//                .setStyle(new NotificationCompat.BigTextStyle()
//                        .bigText("Much longer text that cannot fit one line..."))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(false)
                .setOngoing(true)
                .setOnlyAlertOnce(true)
                .setProgress(max,progress,false);
        sendNotification(notificationBuilder,id);
    }
    void sendedNotification(int id)
    {
        NotificationCompat.Builder notificationBuilder =new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.notif)
                .setColor(R.drawable.ic_launcher_background)
                .setContentTitle("We've send out mails")
                .setContentText("You can turn off internet")
//                .setStyle(new NotificationCompat.BigTextStyle()
//                        .bigText("Much longer text that cannot fit one line..."))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setOnlyAlertOnce(true)
                .setTimeoutAfter(60*1000)
                .setAutoCancel(true); // 8
        sendNotification(notificationBuilder,id);
    }

    void questionNotification(int id)
    {

        NotificationCompat.Builder notificationBuilder =new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.notif)
                .setColor(R.drawable.ic_launcher_background)
                .setContentTitle("We're getting questions")
                .setContentText("Do not turn off internet")
//                .setStyle(new NotificationCompat.BigTextStyle()
//                        .bigText("Much longer text that cannot fit one line..."))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(false)
                .setOngoing(true)
                .setOnlyAlertOnce(true)
                .setProgress(0,0,true);
        sendNotification(notificationBuilder,id);
    }


    private void sendNotification(NotificationCompat.Builder notificationBuilder,int id)
    {
        Intent intent =new Intent(context,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        notificationBuilder.setContentIntent(pendingIntent);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        notificationManager.notify(id, notificationBuilder.build());


    }
    }


