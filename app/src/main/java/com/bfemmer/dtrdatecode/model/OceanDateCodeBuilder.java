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
package com.bfemmer.dtrdatecode.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by bfemmer on 6/7/2016.
 */
public class OceanDateCodeBuilder implements DateCodeBuilder {
    @Override
    public String getCode() {
        return getCode(Calendar.getInstance(Locale.getDefault()));
    }

    @Override
    public String getCode(Calendar calendar) {
        return generateConveyanceCode(calendar);
    }

    @Override
    public List<Date> getCalendarDatesForCode(String dateCode) {
        return getCalendarDatesForDateCode(dateCode);
    }

    @Override
    public boolean isValidFormat(String dateCode) {
        return false;
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
     * Generates a DTR date code for ocean conveyance
     *
     * The ocean conveyance date code is a 4-digit string with the format consisting of the
     * 3-digit Julian date prepended by the last digit of the current year.
     *
     * @return Date code in the format YDDD
     */
    private String generateConveyanceCode(Calendar calendar) {
        String code = generateJulianDateCode(calendar);

        int year = calendar.get(Calendar.YEAR);
        String yearCode = String.format(Locale.getDefault(), "%04d", year);

        // Return concatenation of last digit of year and 3-digit day-of-year code
        return yearCode.substring(yearCode.length() - 1) + code;
    }

    private List<Date> getCalendarDatesForDateCode(String dateCode) {
        List values = new ArrayList<>(); // List of dates that will get returned

        // Use Locale.getDefault() versus TimeZone.getTimeZone("GMT") for the calendar instance
        // as we only care about the date. See the getCalendarDatesForAirDateCode method
        // to see why the GMT timezone is used there.
        Calendar calendar = Calendar.getInstance(Locale.getDefault());

        // Validate length
        if (dateCode.length() != 4) throw new IllegalArgumentException("insufficient length in date code");

        // If not a number, will throw a NumberFormatException
        Integer.parseInt(dateCode);

        // Get last digit of current year
        String temp = String.valueOf(calendar.get(Calendar.YEAR));
        temp = temp.substring(temp.length() - 1);
        int lastDigitOfCurrentYear = Integer.parseInt(temp);

        // Get last digit of date code
        int lastDigitOfYearInDateCode = Integer.parseInt(dateCode.substring(0, 1));

        // Calculate year offset
        int yearOffset = (lastDigitOfCurrentYear - lastDigitOfYearInDateCode) * -1;

        // Apply offset to year (must be done prior to setting the day of year)
        calendar.add(Calendar.YEAR, yearOffset);

        // Strip off leading character and set calendar to day of year
        String dayOfYear = dateCode.substring(dateCode.length() - 3);
        calendar.set(Calendar.DAY_OF_YEAR, Integer.valueOf(dayOfYear));

        // Reset hours, minutes, and seconds
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR, 0);

        // Add date to list and return
        values.add(calendar.getTime());
        return values;
    }
}
