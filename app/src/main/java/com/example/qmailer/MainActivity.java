package com.example.qmailer;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    DbInteract db=new DbInteract(this);
    Button send;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        send=findViewById(R.id.buttonSend);
        db.addQuestions();
        send.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        db.sendMails();
    }
}