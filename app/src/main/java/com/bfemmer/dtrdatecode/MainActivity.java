/*
The MIT License (MIT)

Copyright (c) 2016 Bill Femmer

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */
package com.bfemmer.dtrdatecode;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements DateCodeInputDialogFragment.DateCodeInputDialogListener {
    private Button dateCodeButton;
    private TextView dateCodeTextView;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize class variables
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        dateCodeTextView = (TextView) findViewById(R.id.datecode_value);
        dateCodeButton = (Button) findViewById(R.id.datecode_button);

        dateCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateCodeInputDialogFragment();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        updateCodeDisplay();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }

        return true;
    }

    private void updateCodeDisplay() {
        String manifestType = sharedPreferences.getString("conveyance_list", ConveyanceType.Air.toString());
        dateCodeTextView.setText(DateCode.getInstance().getCode(manifestType));
    }

    public void showDateCodeInputDialogFragment() {
        // Create an instance of the dialog fragment and show it
        DialogFragment dialog = new DateCodeInputDialogFragment();
        dialog.show(getSupportFragmentManager(), "DateCodeInputDialogFragment");
    }

    @Override
    public void onPositiveClick(DialogFragment dialog) {
        String dateCode = ((DateCodeInputDialogFragment) dialog).getDateCode();
        List<Date> dates = DateCode.getInstance().getCalendarDatesForDateCode(dateCode);
        Toast.makeText(this, dateCode, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNegativeClick(DialogFragment dialog) {

    }
}
