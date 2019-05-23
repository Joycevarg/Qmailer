package com.example.qmailer;

import android.content.Context;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DbInteract {

    Subscriber subs;
    question qu;
    ArrayList<question> questions;
    FirebaseDatabase database;
    DatabaseReference DBRef;
    DatabaseReference subRef,qRef;
    private Context context;


    public DbInteract(Context context)
    {
        this.context=context;
        this.addQuestions();
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

                NotificationHelper nh=new NotificationHelper();
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
    public void addQuestions(){

        questions=new ArrayList<>();

        database = FirebaseDatabase.getInstance();
        DBRef = database.getReference("/");

        subRef=DBRef.child("/subscribers");
        qRef=DBRef.child("/questions");
        qRef.addValueEventListener(questionListener);

    }

    public void sendMails()
    {
        subRef.addListenerForSingleValueEvent(subscriberListener);
    }


}
