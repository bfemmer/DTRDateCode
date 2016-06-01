package com.bfemmer.dtrdatecode;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Date;

public class DateResultsActivity extends AppCompatActivity {
    private ListView resultsListView;
    private CalendarView calendarView;
    ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_results);

        // Extract dates from bundle
        ArrayList<String> dates = getIntent().getStringArrayListExtra("DateList");

        resultsListView = (ListView) findViewById(R.id.resultsListView);
        calendarView = (CalendarView) findViewById(R.id.calendarView);


        //calendarView.setDate();
        arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                dates );

        resultsListView.setAdapter(arrayAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}
