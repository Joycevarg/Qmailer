package com.example.qmailer;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    TimePicker timePicker;
    Context context;
    TextView Online;
    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        context=this;


        sharedPref = MainActivity.this.getSharedPreferences("Qmailer",Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPref.edit();
        boolean isOnline =sharedPref.getBoolean("Online", false);
        Online=findViewById(R.id.online);
        timePicker =  findViewById(R.id.timePicker);
        if(isOnline)
        {
            int hour=sharedPref.getInt("Hour", 0);
            int minute=sharedPref.getInt("Minute", 0);
            Online.setText("Active");
            Online.setTextColor(Color.GREEN);
            if (android.os.Build.VERSION.SDK_INT >= 23) {
               timePicker.setHour(hour);
               timePicker.setMinute(minute);
            } else {
               timePicker.setCurrentHour(minute);
               timePicker.setCurrentMinute(minute);

            }
        }
        else
        {
            Online.setText("Inactive");
            Online.setTextColor(Color.RED);
        }

        //attaching clicklistener on button
        findViewById(R.id.buttonAlarm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //We need a calendar object to get the specified time in millis

                Calendar calendar = Calendar.getInstance();
                if (android.os.Build.VERSION.SDK_INT >= 23) {
                    calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
                            timePicker.getHour(), timePicker.getMinute(), 0);
                    editor.putInt("Hour", timePicker.getHour());
                    editor.putInt("Minute", timePicker.getMinute());
                } else {
                    calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
                            timePicker.getCurrentHour(), timePicker.getCurrentMinute(), 0);
                    editor.putInt("Hour", timePicker.getCurrentHour());
                    editor.putInt("Minute", timePicker.getCurrentMinute());
                }
                editor.putBoolean("Online",true);
                editor.putLong("Time",calendar.getTimeInMillis());
                Online.setText("Active");
                Online.setTextColor(Color.GREEN);
                editor.commit();
                setTime(calendar.getTimeInMillis());
            }
        });


        findViewById(R.id.Log).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConnectivityManager cm=(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                if(cm.getActiveNetworkInfo()!=null) {
                    Intent intent = new Intent(MainActivity.this, DateLog.class);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(context, "No Internet", Toast.LENGTH_SHORT).show();
                }
            }
        });



        findViewById(R.id.buttonSend).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //We need a calendar object to get the specified time in millis
                //as the alarm manager method takes time in millis to setup the alarm

                Toast.makeText(context, "Qmailer starting", Toast.LENGTH_SHORT).show();
                Log.d("Lets", "start sending emails");
                SendMail.count=0;
                NotificationHelper.createNotificationChannel(context,
                        NotificationManagerCompat.IMPORTANCE_DEFAULT, false,
                        context.getString(R.string.app_name), "App notification channel.");

                NotificationHelper nh=new NotificationHelper();

                ConnectivityManager cm=(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                if(cm.getActiveNetworkInfo()==null)
                {nh.noInternetNotification(1002);

                    Log.d("No internet", "Emails not sent");}
                else{
                    nh.notificationcancel(1002);
                    DbInteract db;
                    db = new DbInteract(context);
                    db.sendMails();
                }
            }
        });

        findViewById(R.id.buttonStop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                suspendEmailServices();
                editor.putBoolean("Online",false);
                Online.setText("Inactive");
                Online.setTextColor(Color.RED);
                editor.commit();
            }
        });

    }


    private void suspendEmailServices()
    {
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        //creating a new intent specifying the broadcast receiver
        Intent i = new Intent(this, MyAlarm.class);

        //creating a pending intent using the intent
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
        am.cancel(pi);
        Log.d("Down","Services Cancelled");
        Toast.makeText(this, "Qmailer suspended", Toast.LENGTH_SHORT).show();
    }



    private void setTime(long time) {
        //getting the alarm manager
        NotificationHelper nh=new NotificationHelper();
        nh.notificationcancel(1002);
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        //creating a new intent specifying the broadcast receiver
        Intent i = new Intent(this, MyAlarm.class);

        //creating a pending intent using the intent
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);

        //setting the repeating alarm that will be fired every day
        am.setRepeating(AlarmManager.RTC, time, AlarmManager.INTERVAL_DAY/3, pi);
        Toast.makeText(this, "Qmailer running", Toast.LENGTH_SHORT).show();
    }
}