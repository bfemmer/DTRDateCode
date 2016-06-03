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
     * Gets the numerical hour corresponding to the date code
     *
     * The numerical hour corresponds to the index of the array element containing
     * the hour code in the parameter. The method just iterates through the array
     * and returns the index when a hit is found.
     *
     * @param hourCode
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
     * Generates a DTR date code.
     *
     * Generates a DTR date code for the conveyance type defined in the parameter.
     *
     * @param conveyance Air, Surface, or Ocean
     * @return Date code formatted for the given conveyance type
     */
    public String getCode(String conveyance) {
        String code;
        Calendar calendar;

        // Convert string parameter into ConveyanceType object
        ConveyanceType conveyanceType = getConveyanceType(conveyance);

        // Setup the calendar using the default locale (will apply to Surface and Ocean
        // conveyances). If conveyance is Air, then the calendar will be set to use
        // the GMT timezone.
        calendar = Calendar.getInstance(Locale.getDefault());

        // Generate code for conveyance types
        if (conveyanceType.equals(ConveyanceType.Ocean)) {
            code = generateOceanConveyanceCode(calendar);
        } else if (conveyanceType.equals(ConveyanceType.Surface)) {
            code = generateSurfaceConveyanceCode(calendar);
        } else {
            // Use the GMT timezone versus the default locale
            calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
            code = generateAirConveyanceCode(calendar);
        }

        return code;
    }

    /**
     * Generates a DTR date code
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
     * Verifies length of paramter
     *
     * DTR codes are either 3 characters or 4 characters in length. If the length falls
     * outside of this range, than an IllegalArgumentException is thrown.
     *
     * @param dateCode String to evaluate for length
     */
    private void validateDateCodeLength(String dateCode) {
        if (dateCode.length() < 3)
            throw new IllegalArgumentException("string length of argument insufficient");
        if (dateCode.length() > 4)
            throw new IllegalArgumentException("string length of argument excessive");
    }

    /**
     * Generates a list of date codes corresponding to the provided date code
     *
     * @param dateCode user-entered value to lookup
     * @return List of dates matching date code
     */
    public List<Date>getCalendarDatesForDateCode (String dateCode){
        List values = new ArrayList<>(); // List of dates that will get returned

        validateDateCodeLength(dateCode);

        if (dateCode.length() == 4) values = getCalendarDatesForOceanDateCode(dateCode);
        if (dateCode.length() == 3) {
            if (Arrays.asList(hourCodes).contains(dateCode.substring(0, 1).toUpperCase())) {
                values = getCalendarDatesForAirDateCode(dateCode);
            }
            else values = getCalendarDatesForSurfaceDateCode(dateCode);
        }

        return values;
    }

    private List<Date> getCalendarDatesForOceanDateCode(String dateCode) {
        List values = new ArrayList<>(); // List of dates that will get returned

        // Use Locale.getDefault() versus TimeZone.getTimeZone("GMT") for the calendar instance
        // as we only care about the date. See the getCalendarDatesForAirDateCode method
        // to see why the GMT timezone is used there.
        Calendar calendar = Calendar.getInstance(Locale.getDefault());

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
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        // Add date to list and return
        values.add(calendar.getTime());
        return values;
    }

    private List<Date> getCalendarDatesForSurfaceDateCode(String dateCode) {
        List values = new ArrayList<>(); // List of dates that will get returned

        // If not a number, will throw a NumberFormatException
        Integer.parseInt(dateCode);

        // Use Locale.getDefault() versus TimeZone.getTimeZone("GMT") for the calendar instance
        // as we only care about the date. See the getCalendarDatesForAirDateCode method
        // to see why the GMT timezone is used there.
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.set(Calendar.DAY_OF_YEAR, Integer.valueOf(dateCode));

        // Reset hours, minutes, and seconds
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        values.add(calendar.getTime());

        return values;
    }

    private List<Date> getCalendarDatesForAirDateCode(String dateCode) {
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
