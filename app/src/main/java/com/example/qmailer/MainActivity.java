package com.example.qmailer;

import android.os.AsyncTask;
import android.support.v4.app.INotificationSideChannel;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.PasswordAuthentication;
import javax.mail.Session;

import static java.lang.Thread.sleep;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    Subscriber subs;
    question qu;
    ArrayList<question> questions;
    FirebaseDatabase database;
    DatabaseReference DBRef;
    DatabaseReference subRef,qRef;









    private Button buttonSend;
    ValueEventListener subscriberListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {



            for (final DataSnapshot s : dataSnapshot.getChildren()) {
//

                        subs = s.getValue(Subscriber.class);
                        String key = s.getKey();
                        sendEmail(subs);
                        subRef.child(key).child("question").setValue(subs.getQuestion()+1);
                    }


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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        questions=new ArrayList<>();
        buttonSend = findViewById(R.id.buttonSend);


        buttonSend.setOnClickListener(this);
        database = FirebaseDatabase.getInstance();
        DBRef = database.getReference("/");

        subRef=DBRef.child("/subscribers");
        qRef=DBRef.child("/questions");
        qRef.addValueEventListener(questionListener);



    }


    private void sendEmail(Subscriber s) {

        String email = s.getEmail();
        String subject = "Qmailer";
        question q=questions.get(s.getQuestion());
        String message = "HI "+ s.getName()+",<br>Here is your question for today<br> <a href=\""+q.getLink()+"\">"+q.getTitle()+"</a>";
        Log.d("Hey",message);

        SendMail sm = new SendMail(this, email, subject, message);

        sm.execute();
    }

    @Override
    public void onClick(View v) {
        subRef.addListenerForSingleValueEvent(subscriberListener);

    }
}