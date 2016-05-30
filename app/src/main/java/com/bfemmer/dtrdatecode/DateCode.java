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

import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by bfemmer on 5/30/2016.
 */
public class DateCode {
    private static DateCode instance = null;
    private final String[] hourCodes = {"A", "B", "C", "D",
            "E", "F", "G","H", "J", "K", "L", "M", "N", "P",
            "Q", "R", "S", "T", "U","V", "W", "X", "Y", "Z"};

    /**
     * Empty constructor, not used publicly.
     */
    protected DateCode() {

    }

    /**
     * Primary method for instantiating objects of this class
     * @return static instance of this class
     */
    public static DateCode getInstance() {
        // Instantiate new instance if one doesn't exist
        if(instance == null) {
            instance = new DateCode();
        }

        return instance;
    }

    /**
     *
     * @param conveyance String value of conveyance (Air, Surface, or Ocean)
     * @return ConveyanceType object representation of string parameter
     */
    private ConveyanceType getConveyanceType(String conveyance) {
        ConveyanceType conveyanceType = ConveyanceType.Air;
        if (conveyance.equals("Surface")) conveyanceType = ConveyanceType.Surface;
        if (conveyance.equals("Ocean")) conveyanceType = ConveyanceType.Ocean;

        return conveyanceType;
    }

    /**
     * Generates date code.
     *
     * Generates a DTR date code for the conveyance type defined in the parameter.
     *
     * @param conveyance Air, Surface, or Ocean
     * @return Date code formatted for the given conveyance type
     */
    public String getCode(String conveyance) {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));

        // Convert string parameter into ConveyanceType object
        ConveyanceType conveyanceType = getConveyanceType(conveyance);

        // The Julian date code is used for Ocean conveyance types
        String code = getJulianDateCode();

        if (conveyanceType.equals(ConveyanceType.Air)) {
            // Truncate to 2 digits
            if (code.length() > 2) {
                code = code.substring(code.length() - 2);
            }

            // Prepend hour code to Julian code
            code = hourCodes[calendar.get(Calendar.HOUR_OF_DAY)] + code;
        } else if (conveyanceType.equals(ConveyanceType.Surface)) {
            // Truncate to 3 digits
            if (code.length() > 3) {
                code = code.substring(code.length() - 3);
            }
        }

        return code;
    }

    /**
     * Returns a Julian date code
     *
     * The Julian date is a 4-digit string with the format consisting of the last digit of the
     * current year followed by a 3 digit day of the year.
     *
     * @return Date code in the format YDDD
     */
    private String getJulianDateCode() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        int day = calendar.get(Calendar.DAY_OF_YEAR);
        int year = calendar.get(Calendar.YEAR);

        // Get year and day-of-year into string variables
        String yearCode = String.format("%04d", year);
        String dayCode = String.format("%03d", day);

        // Return concatenation of last digit of year and 3-digit day-of-year code
        return yearCode.substring(yearCode.length() - 1) + dayCode;
    }

//    public List<Date> getDatesDayCode(String dayCode) {
//        String code;
//        List values = new ArrayList<Date>();
//        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
//
//        if (conveyanceType.equals(ConveyanceType.Air)) {
//            // Two-digit day code
//            if (dayCode.length() > 2) {
//                dayCode = dayCode.substring(dayCode.length() - 2);
//            }
//
//            for (int dayOfYear = 1; dayOfYear <= 366; dayOfYear++) {
//                code = getJulianDateCode(dayOfYear);
//
//                if (code.equals(dayCode)) {
//                    calendar.set(Calendar.DAY_OF_YEAR, dayOfYear);
//                    values.add(calendar.getTime());
//                }
//            }
//        } else {
//            // Three-digit day code
//            if (dayCode.length() > 3) {
//                dayCode = dayCode.substring(dayCode.length() - 3);
//                calendar.set(Calendar.DAY_OF_YEAR, Integer.valueOf(dayCode));
//                values.add(calendar.getTime());
//            }
//        }
//
//        return values;
//    }
}
