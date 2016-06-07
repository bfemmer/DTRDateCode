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

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bfemmer.dtrdatecode.model.DateCodeBuilderFactory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity
        implements DateCodeInputDialogFragment.DateCodeInputDialogListener {
    private Button dateCodeButton;
    private TextView dateCodeTextView;
    private SharedPreferences sharedPreferences;
    private DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss", Locale.getDefault());

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
        dateCodeTextView.setText(DateCodeBuilderFactory.getDateCodeBuilder((manifestType)).getCode());
    }

    public void showDateCodeInputDialogFragment() {
        // Create an instance of the dialog fragment and show it
        DialogFragment dialog = new DateCodeInputDialogFragment();
        dialog.show(getSupportFragmentManager(), "DateCodeInputDialogFragment");
    }

    private void showExceptionMessage(String dateCode) {
        String message;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        message = "The entered value (" + dateCode + ") is not valid. ";
        message += "Check the format of the date code and try again.";

        // Build dialog
        builder.setTitle(R.string.title_invalid_input);
        builder.setMessage(message)
                .setPositiveButton(R.string.label_ok_button, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // No code
                    }
                });

        // Show AlertDialog
        builder.create().show();
    }

    /**
     * Verifies length of paramter
     *
     * DTR codes are either 3 characters or 4 characters in length.
     *
     * @param dateCode String to evaluate for length
     */
    private void validateDateCodeLength(String dateCode) {
        if (dateCode.length() < 3)
            throw new IllegalArgumentException("length insufficient");
        if (dateCode.length() > 4)
            throw new IllegalArgumentException("length insufficient");
    }

    /**
     * Infers conveyance type (Air, Ocean, or Surface) from format of parameter
     *
     * @param dateCode value to inspect
     * @return string value Air, Ocean, or Surface
     */
    private String getConveyanceTypeFromDateCode(String dateCode) {
        String conveyanceType = "Air";

        if (dateCode.length() == 4) conveyanceType = "Ocean";
        if (dateCode.length() == 3) {
            if (dateCode.substring(0, 1).toUpperCase().matches("\\d")) conveyanceType = "Surface";
        }

        return conveyanceType;
    }

    private void launchDateResultsActivityWithCode(String dateCode) {
        List<Date> dates;

        try {
            // Verify length is either 3 or 4
            validateDateCodeLength(dateCode);

            // Infer conveyance type based on format of entered value
            String conveyanceType = getConveyanceTypeFromDateCode(dateCode);

            // Get list of dates corresponding with date code
            dates = DateCode.getInstance().getCalendarDatesForDateCode(dateCode);

            // Convert date list to string list
            List<String> values = new ArrayList<>();
            for (Date date : dates) {
                values.add(dateFormat.format(date));
            }

            // Prepare bundle to pass to intent
            Bundle bundle = new Bundle();
            bundle.putString("DateCode", dateCode);
            bundle.putString("ConveyanceType", conveyanceType);
            bundle.putStringArrayList("DateList", (ArrayList)values);

            // Prepare intent and start
            Intent intent = new Intent(this, DateResultsActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        } catch (NumberFormatException numberFormatException) {
            showExceptionMessage(dateCode);
        } catch (IllegalArgumentException illegalArgumentException) {
            showExceptionMessage(dateCode);
        }
    }

    @Override
    public void onPositiveClick(DialogFragment dialog) {
        String dateCode = ((DateCodeInputDialogFragment) dialog).getDateCode();
        launchDateResultsActivityWithCode(dateCode);
    }

    @Override
    public void onNegativeClick(DialogFragment dialog) {

    }
}
