package com.example.qmailer;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class DbInteract {

    Subscriber subs;
    question qu;
    ArrayList<question> questions;
    ArrayList<String> dates=new ArrayList<>();
    FirebaseDatabase database;
    DatabaseReference DBRef;
    DatabaseReference subRef,qRef,dRef;
    private Context context;
    String date;


    public DbInteract(Context context)
    {
        this.context=context;

        database = FirebaseDatabase.getInstance();
        DBRef = database.getReference("/");
        subRef=DBRef.child("/subscribers");
        qRef=DBRef.child("/questions");
        dRef=DBRef.child("/senddates");
    }
    private void sendEmail(Subscriber s) {
        if(s.getQuestion()<200)
        {
        question q=questions.get(s.getQuestion());
        String email = s.getEmail();
        String subject = "Qmailer";
        String message = "Hi "+ s.getName()+",<br>Here is your question for today<br> <a href=\""+q.getLink()+"\">"+q.getTitle()+"</a>";
        Log.d("Hey",message);

        SendMail sm = new SendMail(context, email, subject, message,false);

        sm.execute();}
    }
    ValueEventListener subscriberListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {


                int max= (int) dataSnapshot.getChildrenCount();
                SendMail.max=max;
                int count=0;
            for (final DataSnapshot s : dataSnapshot.getChildren()) {
//
//                nh.sendingNotification(1002,count,max);
                count++;
                subs = s.getValue(Subscriber.class);
                String key = s.getKey();
                sendEmail(subs);
                subRef.child(key).child("question").setValue(subs.getQuestion()+1);
            }
//            nh.sendedNotification(1002);

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

            Log.w("Hello", "loadPost:onCancelled", databaseError.toException());
            // ...
        }
    };
    ValueEventListener questionListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            // Get Post object and use the values to update the UI

            for (DataSnapshot q:dataSnapshot.getChildren()) {


                qu=q.getValue(question.class);
                questions.add(qu);
                //get question
            }
            // ...
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

            Log.w("Hello", "loadPost:onCancelled", databaseError.toException());
            // ...
        }
    };


    ValueEventListener alldateListener=new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            // This method is called once with the initial value and again
            // whenever data at this location is updated.
            if(dataSnapshot.exists())
            {
                for (DataSnapshot year:dataSnapshot.getChildren())
                      {
                        for(DataSnapshot month:year.getChildren())
                        {
                            for(DataSnapshot day:month.getChildren())
                            {
                                if((Boolean)day.getValue())
                                dates.add(year.getKey()+"/"+month.getKey()+"/"+day.getKey());

                            }
                        }
                }
            }
            else
            {
                dates.add("No dates found");

            }
            DateLog.setDates(dates,context);
        }

        @Override
        public void onCancelled(DatabaseError error) {
            Log.d("Date",error.toString())   ;
        }

    };









    ValueEventListener dateListener=new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            // This method is called once with the initial value and again
            // whenever data at this location is updated.
            boolean value;
            if(dataSnapshot.exists())
                value = dataSnapshot.getValue(boolean.class);

            else
                value=false;
            NotificationHelper nh=new NotificationHelper();
            if(!value){

                nh.questionNotification(1002);
                setDate(date,false);
                addQuestions();
                subRef.addListenerForSingleValueEvent(subscriberListener);
            }
            else {
                Toast.makeText(context, "Emails for today were already sent", Toast.LENGTH_SHORT).show();
                nh.notificationcancel(1002);
            }
        }

        @Override
        public void onCancelled(DatabaseError error) {
            Log.d("Date",error.toString())   ;
        }

    };
    public void addQuestions(){

        questions=new ArrayList<>();

        qRef.addValueEventListener(questionListener);

    }

    public void setDate(String date,boolean val)
    {

        dRef.child(date).setValue(val);
    }


    public void sendMails()
    {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("yyyy/MM/dd");
        date=mdformat.format(calendar.getTime());
       dRef.child(date).addListenerForSingleValueEvent(dateListener);
    }
    public void getdates()
    {

        dRef.addListenerForSingleValueEvent(alldateListener);
    }


}
