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
        return null;
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
}