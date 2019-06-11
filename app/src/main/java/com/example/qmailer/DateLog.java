package com.example.qmailer;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class DateLog extends AppCompatActivity {
    static ListView datelist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_log);
        datelist=findViewById(R.id.datelistview);
       DbInteract db=new DbInteract(this);
       db.getdates();
    }
    public static void setDates(ArrayList<String> dates,Context context)
    {
        ArrayAdapter adapter = new ArrayAdapter<>(context,R.layout.list_element_layout,dates);
        datelist.setAdapter(adapter);
    }
}
