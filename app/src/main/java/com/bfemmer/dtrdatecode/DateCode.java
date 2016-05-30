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
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by bfemmer on 5/30/2016.
 */
public class DateCode {
    private static DateCode instance = null;
    private ConveyanceType conveyanceType;
    private String[] hourCodes = {"A", "B", "C", "D",
            "E", "F", "G","H", "J", "K", "L", "M", "N", "P",
            "Q", "R", "S", "T", "U","V", "W", "X", "Y", "Z"};

    protected DateCode(ConveyanceType conveyanceType) {
        this.conveyanceType = conveyanceType;
    }

    public static DateCode getInstance(String manifest) {
        if(instance == null) {
            ConveyanceType conveyanceType = ConveyanceType.Air;

            if (manifest.equals("Surface")) conveyanceType = ConveyanceType.Surface;
            if (manifest.equals("Ocean")) conveyanceType = ConveyanceType.Ocean;

            instance = new DateCode(conveyanceType);
        }
        return instance;
    }

    /**
     * Gets the current date code in DTR format
     *
     * @return Date code in HDD format
     */
    public String getCode() {
        return getCode(Calendar.getInstance(
                TimeZone.getTimeZone("GMT")).getTime());
    }

    /**
     * Gets a specified date in mil format
     *
     * @param date The date to calculate the SET for
     * @return System Entry Time in HDD format
     */
    public String getCode(Date date) {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        calendar.setTime(date);

        String setTime = hourCodes[calendar.get(Calendar.HOUR_OF_DAY)];
        setTime += getJulianCode(calendar.get(Calendar.DAY_OF_YEAR));
        return setTime;
    }

    /**
     * Gets the current hour code
     *
     * @return The hour code component of the SET
     */
    public String getCurrentHourCode() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        int hour = calendar.get(Calendar.HOUR_OF_DAY);

        // Because the hour codes are specified to begin with minute #1 of the hour,
        // subtract 1 hour if the time is exactly on the hour.
        //if (calendar.get(Calendar.MINUTE) == 0) hour--;

        return hourCodes[hour];
    }

    /**
     * Gets the current hour code
     *
     * @param hour Number between 1 and 23
     * @return The hour code component of the SET
     */
    public String getAlphaHourCode(int hour) {
        return hourCodes[hour];
    }

    public String getJulianCode() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        return getJulianCode(calendar.get(Calendar.DAY_OF_YEAR));
    }

    public String getJulianCode(int dayOfYear) {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        calendar.set(Calendar.DAY_OF_YEAR, dayOfYear);
        int day = calendar.get(Calendar.DAY_OF_YEAR);

        String dayCode = String.format("%02d", day);

        if (dayCode.length() > 2) {
            dayCode = dayCode.substring(dayCode.length() - 2);
        }
        return dayCode;
    }

    public List<Date> getDatesDayCode(String dayCode) {
        String code;
        List values = new ArrayList<Date>();
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));

        if (conveyanceType.equals(ConveyanceType.Air)) {
            // Two-digit day code
            if (dayCode.length() > 2) {
                dayCode = dayCode.substring(dayCode.length() - 2);
            }

            for (int dayOfYear = 1; dayOfYear <= 366; dayOfYear++) {
                code = getJulianCode(dayOfYear);

                if (code.equals(dayCode)) {
                    calendar.set(Calendar.DAY_OF_YEAR, dayOfYear);
                    values.add(calendar.getTime());
                }
            }
        } else {
            // Three-digit day code
            if (dayCode.length() > 3) {
                dayCode = dayCode.substring(dayCode.length() - 3);
                calendar.set(Calendar.DAY_OF_YEAR, Integer.valueOf(dayCode));
                values.add(calendar.getTime());
            }
        }

        return values;
    }
}
