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
package com.bfemmer.dtrdatecodelib;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by bfemmer on 6/7/2016.
 */
public class AirDateCodeBuilder implements DateCodeBuilder {
    private final String[] hourCodes = {"A", "B", "C", "D",
            "E", "F", "G", "H", "J", "K", "L", "M", "N", "P",
            "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

    @Override
    public String getCode() {
        // Setup the calendar using the GMT timezone.
        return getCode(Calendar.getInstance(TimeZone.getTimeZone("GMT")));
    }

    @Override
    public String getCode(Calendar calendar) {
        return generateAirConveyanceCode(calendar);
    }

    @Override
    public List<Date> getCalendarDatesForCode(String dateCode) {
        return getCalendarDatesForDateCode(dateCode);
    }

    @Override
    public boolean isValidFormat(String dateCode) {
        boolean isValid = true;

        // Validate length
        if (dateCode.length() != 3) isValid = false;

        // Validate first character as hour code
        if (Arrays.asList(hourCodes).contains(dateCode.substring(0, 1).toUpperCase()))
            isValid = false;

        // Validate last two characters as numeric
        // Left trim dateCode parameter to just the last two characters
        String code = dateCode.substring(dateCode.length() - 2);

        // If not a number, will throw a NumberFormatException
        try {
            Integer.parseInt(code);
        } catch (NumberFormatException numberFormatException) {
            isValid = false;
        }

        return isValid;
    }

    /**
     * Generates a Julian date code
     *
     * The Julian date is a 3-digit string consisting of the 3 digit day of the year.
     *
     * @return Date code in the format DDD
     */
    private String generateJulianDateCode(Calendar calendar) {
        int day = calendar.get(Calendar.DAY_OF_YEAR);

        // Pad the date with leading zeros
        return String.format(Locale.getDefault(), "%03d", day);
    }

    /**
     * Generates a DTR date code for air conveyance
     *
     * The air conveyance date code is a 1-digit hour code (consisting of an alphabetic character)
     * and the last 2-digits of the Julian date.
     *
     * @return Date code in the format HDD
     */
    private String generateAirConveyanceCode(Calendar calendar) {
        String code = generateJulianDateCode(calendar);

        // Left-trim Julian date (only the last 2 digits are needed)
        if (code.length() > 2) {
            code = code.substring(code.length() - 2);
        }

        // Extract hour component (this is our index into the hour code array)
        int hour = calendar.get(Calendar.HOUR_OF_DAY);

        // Concatenate hour code to Julian code
        code = hourCodes[hour] + code;
        return code;
    }

    /**
     * Gets the numerical hour corresponding to the date code
     *
     * The numerical hour corresponds to the index of the array element containing
     * the hour code in the parameter. The method just iterates through the array
     * and returns the index when a hit is found.
     *
     * @param hourCode the alpha character code to convert to hours
     * @return array index containing the specified code
     */
    private int getArrayIndexContainingHourCode(String hourCode) {
        for (int i = 0; i < hourCodes.length; i++) {
            if (hourCode.toUpperCase().equals(hourCodes[i])) {
                return i;
            }
        }

        return 0;
    }

    private List<Date> getCalendarDatesForDateCode(String dateCode) {
        String code;                     // Stores the "day" component of parameter
        int hour;                        // Stores the "hour" component of parameter
        String tempCode;                 // Temporary date code variable
        List values = new ArrayList<>(); // List of dates that will get returned

        // Use TimeZone.getTimeZone("GMT") versus Locale.getDefault() for the calendar instance
        // as we care about the date AND the time. The hour code is based on GMT time, and this
        // must be incorporated when calculating dates.
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));

        // Extract hour component (this is our index into the hour code array)
        tempCode = dateCode.substring(0, 1);
        hour = getArrayIndexContainingHourCode(tempCode);

        // Left trim dateCode parameter to just the last two characters
        code = dateCode.substring(dateCode.length() - 2);

        // If not a number, will throw a NumberFormatException
        Integer.parseInt(code);

        // Store current date
        Date currentDate = calendar.getTime();

        // Reset calendar to minus 1 year
        calendar.add(Calendar.YEAR, -1);

        // Set the hour and reset minutes and seconds (which is done for display purposes)
        calendar.set(Calendar.HOUR, hour);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        // using "not ... after" allows for the current date to be included in the result set
        while (!calendar.getTime().after(currentDate)) {
            tempCode = generateJulianDateCode(calendar);

            // Left trim dateCode parameter to just the last two characters
            tempCode = tempCode.substring(tempCode.length() - 2);

            // Add to list if there's a match
            if (tempCode.equals(code)) {
                values.add(calendar.getTime());
            }
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }

        return values;
    }
}
