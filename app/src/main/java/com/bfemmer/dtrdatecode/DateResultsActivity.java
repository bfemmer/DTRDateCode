package com.bfemmer.dtrdatecode;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.ListView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DateResultsActivity extends AppCompatActivity {
    private ListView resultsListView;
    private CalendarView calendarView;
    private ArrayAdapter<String> arrayAdapter;
    private DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_results);

        // Initialize view variables
        calendarView = (CalendarView) findViewById(R.id.calendarView);
        resultsListView = (ListView) findViewById(R.id.resultsListView);

        // Update title to display code being converted
        String dateCode = getIntent().getStringExtra("DateCode");
        setTitle("Date Results For " + dateCode);

        // Extract dates from bundle
        ArrayList<String> dates = getIntent().getStringArrayListExtra("DateList");

        // Assign dates to new array adapter and apply to listview
        arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                dates );

        resultsListView.setAdapter(arrayAdapter);

        // Update calendar with most recent date (last item in list)
        if (dates.size() > 0) {
            try {
                Date date = dateFormat.parse(dates.get(dates.size() - 1));
                calendarView.setDate(date.getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        // Set item click listener to update calendar to selected date
        resultsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedDate =(resultsListView.getItemAtPosition(position).toString());
                try {
                    calendarView.setDate(dateFormat.parse(selectedDate).getTime());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}
