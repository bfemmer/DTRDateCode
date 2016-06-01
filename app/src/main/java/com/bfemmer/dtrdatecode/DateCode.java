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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by bfemmer on 5/30/2016.
 */
public class DateCode {
    private static DateCode instance = null;
    private final String[] hourCodes = {"A", "B", "C", "D",
            "E", "F", "G", "H", "J", "K", "L", "M", "N", "P",
            "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

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
     * Generates ConveyanceType object for string parameters
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
        return getCode(conveyance, calendar);
    }

    /**
     * Generates a DTR code
     *
     * @param conveyance Type of conveyance (Ocean, Surface, or Air)
     * @param calendar Date/time of ship/receive
     * @return DTR date code in format YDDD, DDD, or HDD (depending on conveyance)
     */
    public String getCode(String conveyance, Calendar calendar) {
        String code;

        // Convert string parameter into ConveyanceType object
        ConveyanceType conveyanceType = getConveyanceType(conveyance);

        // Generate code for conveyance types
        if (conveyanceType.equals(ConveyanceType.Ocean)) {
            code = generateOceanConveyanceCode(calendar);
        } else if (conveyanceType.equals(ConveyanceType.Surface)) {
            code = generateSurfaceConveyanceCode(calendar);
        } else {
            code = generateAirConveyanceCode(calendar);
        }

        return code;
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
     * Generates a DTR date code for surface conveyance
     *
     * The surface conveyance date code is the 3-digit Julian date.
     *
     * @return Date code in the format DDD
     */
    private String generateSurfaceConveyanceCode(Calendar calendar) {
        return generateJulianDateCode(calendar);
    }

    /**
     * Generates a DTR date code for ocean conveyance
     *
     * The ocean conveyance date code is a 4-digit string with the format consisting of the
     * 3-digit Julian date prepended by the last digit of the current year.
     *
     * @return Date code in the format YDDD
     */
    private String generateOceanConveyanceCode(Calendar calendar) {
        String code = generateJulianDateCode(calendar);

        int year = calendar.get(Calendar.YEAR);
        String yearCode = String.format(Locale.getDefault(), "%04d", year);

        // Return concatenation of last digit of year and 3-digit day-of-year code
        return yearCode.substring(yearCode.length() - 1) + code;
    }

    /**
     * Returns a Julian date code
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

    private boolean validateDateCode(String dateCode) {
        if (dateCode.length() < 3) throw new IllegalArgumentException("string length of argument insufficient");
        if (dateCode.length() > 4) throw new IllegalArgumentException("string length of argument excessive");
        return true;
    }

    public List<Date>getCalendarDatesForDateCode (String dateCode){
        List values = new ArrayList<Date>(); // List of dates that will get returned

        if (dateCode.length() == 4) values = getCalendarDatesForOceanDateCode(dateCode);
        if (dateCode.length() == 3) {
            if (Arrays.asList(hourCodes).contains(dateCode.substring(0, 1).toUpperCase())) {
                values = getCalendarDatesForAirDateCode(dateCode);
            }
            else values = getCalendarDatesForSurfaceDateCode(dateCode);
        }

        return values;
    }

    public List<Date> getCalendarDatesForOceanDateCode(String dateCode) {
        List values = new ArrayList<Date>(); // List of dates that will get returned

        // Evaluate leading character
        String year = dateCode.substring(0, 1);

        // Strip off leading character
        String code = dateCode.substring(dateCode.length() - 3);

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        calendar.set(Calendar.DAY_OF_YEAR, Integer.valueOf(code));

        // Incorporate year
        //String.valueOf(calendar.get(Calendar.YEAR));

        values.add(calendar.getTime());

        return values;
    }

    public List<Date> getCalendarDatesForSurfaceDateCode(String dateCode) {
        List values = new ArrayList<Date>(); // List of dates that will get returned

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        calendar.set(Calendar.DAY_OF_YEAR, Integer.valueOf(dateCode));
        values.add(calendar.getTime());

        return values;
    }

    public List<Date> getCalendarDatesForAirDateCode(String dateCode) {
        String code;                         // Stores the "day" component of parameter
        String tempCode;                     // Temporary date code variable
        List values = new ArrayList<>(); // List of dates that will get returned
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));

        // Left trim dateCode parameter to just the last two characters
        code = dateCode.substring(dateCode.length() - 2);

        // Store current date
        Date currentDate = calendar.getTime();

        // Reset calendar to minus 1 year
        calendar.add(Calendar.YEAR, -1);

        // using "not ... after" allows for the current date to be included in the result set
        while (!calendar.getTime().after(currentDate)) {
            tempCode = generateJulianDateCode(calendar);

            // Left trim dateCode parameter to just the last two characters
            tempCode = tempCode.substring(tempCode.length() - 2);

            if (tempCode.equals(code)) {
                values.add(calendar.getTime());
            }
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }

        return values;
    }
}
