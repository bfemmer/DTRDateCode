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

import com.bfemmer.dtrdatecode.model.DateCodeBuilder;
import com.bfemmer.dtrdatecode.model.DateCodeBuilderFactory;

import org.junit.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class DateCodeBuilderUnitTest {
    private DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss", Locale.getDefault());

    @Test
    public void testSurfaceConveyanceNormalYear() throws Exception {
        Calendar calendar = Calendar.getInstance(Locale.getDefault());

        // Seed date to March 1, 2015 00:01 (one minute after midnight)
        calendar.clear();
        calendar.set(2015, 2, 1, 0, 1);

        // Setup test conditions
        String expected = "060";

        DateCodeBuilder dateCodeBuilder = DateCodeBuilderFactory.getDateCodeBuilder("Surface");
        String actual = dateCodeBuilder.getCode(calendar);

        // Evaluate
        assertEquals("Surface conveyance date code generation failed for non leap year",
                expected, actual);
    }

    @Test
    public void testSurfaceConveyanceLeapYear() throws Exception {
        Calendar calendar = Calendar.getInstance(Locale.getDefault());

        // Seed date to March 1, 2016 00:01 (one minute after midnight)
        calendar.clear();
        calendar.set(2016, 2, 1, 0, 1);

        // Setup test conditions
        String expected = "061";

        DateCodeBuilder dateCodeBuilder = DateCodeBuilderFactory.getDateCodeBuilder("Surface");
        String actual = dateCodeBuilder.getCode(calendar);

        // Evaluate
        assertEquals("Surface conveyance date code generation failed for leap year",
                expected, actual);
    }

    @Test
    public void testOceanConveyanceNormalYear() throws Exception {
        Calendar calendar = Calendar.getInstance(Locale.getDefault());

        // Seed date to March 1, 2015 00:01 (one minute after midnight)
        calendar.clear();
        calendar.set(2015, 2, 1, 0, 1);

        // Setup test conditions
        String expected = "5060";

        DateCodeBuilder dateCodeBuilder = DateCodeBuilderFactory.getDateCodeBuilder("Ocean");
        String actual = dateCodeBuilder.getCode(calendar);

        // Evaluate
        assertEquals("Surface conveyance date code generation failed for non leap year",
                expected, actual);
    }

    @Test
    public void testOceanConveyanceLeapYear() throws Exception {
        Calendar calendar = Calendar.getInstance(Locale.getDefault());

        // Seed date to March 1, 2016 00:01 (one minute after midnight)
        calendar.clear();
        calendar.set(2016, 2, 1, 0, 1);

        // Setup test conditions
        String expected = "6061";

        DateCodeBuilder dateCodeBuilder = DateCodeBuilderFactory.getDateCodeBuilder("Ocean");
        String actual = dateCodeBuilder.getCode(calendar);

        // Evaluate
        assertEquals("Surface conveyance date code generation failed for leap year",
                expected, actual);
    }

    @Test
    public void testAirConveyanceNormalYear() throws Exception {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));

        // Seed date to March 1, 2015 00:01 (one minute after midnight)
        calendar.clear();
        calendar.set(2015, 2, 1, 0, 1);

        // Setup test conditions
        String expected = "A60";

        DateCodeBuilder dateCodeBuilder = DateCodeBuilderFactory.getDateCodeBuilder("Air");
        String actual = dateCodeBuilder.getCode(calendar);

        // Evaluate
        assertEquals("Surface conveyance date code generation failed for non leap year",
                expected, actual);
    }

    @Test
    public void testAirConveyanceLeapYear() throws Exception {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));

        // Seed date to March 1, 2016 00:01 (one minute after midnight)
        calendar.clear();
        calendar.set(2016, 2, 1, 0, 1);

        // Setup test conditions
        String expected = "A61";

        DateCodeBuilder dateCodeBuilder = DateCodeBuilderFactory.getDateCodeBuilder("Air");
        String actual = dateCodeBuilder.getCode(calendar);

        // Evaluate
        assertEquals("Surface conveyance date code generation failed for leap year",
                expected, actual);
    }

    @Test
    public void testAirConveyanceNormalYearAt0100() throws Exception {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));

        // Seed date to March 1, 2015 01:00 (one minute after midnight)
        calendar.clear();
        calendar.set(2015, 2, 1, 1, 0);

        // Setup test conditions
        String expected = "B60";

        DateCodeBuilder dateCodeBuilder = DateCodeBuilderFactory.getDateCodeBuilder("Air");
        String actual = dateCodeBuilder.getCode(calendar);

        // Evaluate
        assertEquals("Surface conveyance date code generation failed for hour code at 0100",
                expected, actual);
    }

    @Test
    public void testAirConveyanceNormalYearAt0200() throws Exception {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));

        // Seed date to March 1, 2015 02:00 (one minute after midnight)
        calendar.clear();
        calendar.set(2015, 2, 1, 2, 0);

        // Setup test conditions
        String expected = "C60";

        DateCodeBuilder dateCodeBuilder = DateCodeBuilderFactory.getDateCodeBuilder("Air");
        String actual = dateCodeBuilder.getCode(calendar);

        // Evaluate
        assertEquals("Surface conveyance date code generation failed for hour code at 0200",
                expected, actual);
    }

    @Test
    public void testAirConveyanceNormalYearAt0300() throws Exception {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));

        // Seed date to March 1, 2015 03:00 GMT
        calendar.clear();
        calendar.set(2015, 2, 1, 3, 0);

        // Setup test conditions
        String expected = "D60";

        DateCodeBuilder dateCodeBuilder = DateCodeBuilderFactory.getDateCodeBuilder("Air");
        String actual = dateCodeBuilder.getCode(calendar);

        // Evaluate
        assertEquals("Surface conveyance date code generation failed for hour code at 0300",
                expected, actual);
    }

    @Test
    public void testAirConveyanceNormalYearAt0400() throws Exception {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));

        // Seed date to March 1, 2015 04:00 GMT
        calendar.clear();
        calendar.set(2015, 2, 1, 4, 0);

        // Setup test conditions
        String expected = "E60";

        DateCodeBuilder dateCodeBuilder = DateCodeBuilderFactory.getDateCodeBuilder("Air");
        String actual = dateCodeBuilder.getCode(calendar);

        // Evaluate
        assertEquals("Surface conveyance date code generation failed for hour code at 0400",
                expected, actual);
    }

    @Test
    public void testAirConveyanceNormalYearAt0500() throws Exception {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));

        // Seed date to March 1, 2015 05:00 GMT
        calendar.clear();
        calendar.set(2015, 2, 1, 5, 0);

        // Setup test conditions
        String expected = "F60";

        DateCodeBuilder dateCodeBuilder = DateCodeBuilderFactory.getDateCodeBuilder("Air");
        String actual = dateCodeBuilder.getCode(calendar);

        // Evaluate
        assertEquals("Surface conveyance date code generation failed for hour code at 0500",
                expected, actual);
    }

    @Test
    public void testAirConveyanceNormalYearAt0600() throws Exception {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));

        // Seed date to March 1, 2015 06:00 GMT
        calendar.clear();
        calendar.set(2015, 2, 1, 6, 0);

        // Setup test conditions
        String expected = "G60";

        DateCodeBuilder dateCodeBuilder = DateCodeBuilderFactory.getDateCodeBuilder("Air");
        String actual = dateCodeBuilder.getCode(calendar);

        // Evaluate
        assertEquals("Surface conveyance date code generation failed for hour code at 0600",
                expected, actual);
    }

    @Test
    public void testAirConveyanceNormalYearAt0700() throws Exception {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));

        // Seed date to March 1, 2015 07:00 GMT
        calendar.clear();
        calendar.set(2015, 2, 1, 7, 0);

        // Setup test conditions
        String expected = "H60";

        DateCodeBuilder dateCodeBuilder = DateCodeBuilderFactory.getDateCodeBuilder("Air");
        String actual = dateCodeBuilder.getCode(calendar);

        // Evaluate
        assertEquals("Surface conveyance date code generation failed for hour code at 0700",
                expected, actual);
    }

    @Test
    public void testAirConveyanceNormalYearAt0800() throws Exception {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));

        // Seed date to March 1, 2015 08:00 GMT
        calendar.clear();
        calendar.set(2015, 2, 1, 8, 0);

        // Setup test conditions
        String expected = "J60";

        DateCodeBuilder dateCodeBuilder = DateCodeBuilderFactory.getDateCodeBuilder("Air");
        String actual = dateCodeBuilder.getCode(calendar);

        // Evaluate
        assertEquals("Surface conveyance date code generation failed for hour code at 0800",
                expected, actual);
    }

    @Test
    public void testAirConveyanceNormalYearAt0900() throws Exception {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));

        // Seed date to March 1, 2015 09:00 GMT
        calendar.clear();
        calendar.set(2015, 2, 1, 9, 0);

        // Setup test conditions
        String expected = "K60";

        DateCodeBuilder dateCodeBuilder = DateCodeBuilderFactory.getDateCodeBuilder("Air");
        String actual = dateCodeBuilder.getCode(calendar);

        // Evaluate
        assertEquals("Surface conveyance date code generation failed for hour code at 0900",
                expected, actual);
    }

    @Test
    public void testAirConveyanceNormalYearAt1000() throws Exception {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));

        // Seed date to March 1, 2015 10:00 GMT
        calendar.clear();
        calendar.set(2015, 2, 1, 10, 0);

        // Setup test conditions
        String expected = "L60";

        DateCodeBuilder dateCodeBuilder = DateCodeBuilderFactory.getDateCodeBuilder("Air");
        String actual = dateCodeBuilder.getCode(calendar);

        // Evaluate
        assertEquals("Surface conveyance date code generation failed for hour code at 1000",
                expected, actual);
    }

    @Test
    public void testAirConveyanceNormalYearAt1100() throws Exception {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));

        // Seed date to March 1, 2015 11:00 GMT
        calendar.clear();
        calendar.set(2015, 2, 1, 11, 0);

        // Setup test conditions
        String expected = "M60";

        DateCodeBuilder dateCodeBuilder = DateCodeBuilderFactory.getDateCodeBuilder("Air");
        String actual = dateCodeBuilder.getCode(calendar);

        // Evaluate
        assertEquals("Surface conveyance date code generation failed for hour code at 1100",
                expected, actual);
    }

    @Test
    public void testAirConveyanceNormalYearAt1200() throws Exception {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));

        // Seed date to March 1, 2015 12:00 GMT
        calendar.clear();
        calendar.set(2015, 2, 1, 12, 0);

        // Setup test conditions
        String expected = "N60";

        DateCodeBuilder dateCodeBuilder = DateCodeBuilderFactory.getDateCodeBuilder("Air");
        String actual = dateCodeBuilder.getCode(calendar);

        // Evaluate
        assertEquals("Surface conveyance date code generation failed for hour code at 1200",
                expected, actual);
    }

    @Test
    public void testAirConveyanceNormalYearAt1300() throws Exception {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));

        // Seed date to March 1, 2015 13:00 GMT
        calendar.clear();
        calendar.set(2015, 2, 1, 13, 0);

        // Setup test conditions
        String expected = "P60";

        DateCodeBuilder dateCodeBuilder = DateCodeBuilderFactory.getDateCodeBuilder("Air");
        String actual = dateCodeBuilder.getCode(calendar);

        // Evaluate
        assertEquals("Surface conveyance date code generation failed for hour code at 1300",
                expected, actual);
    }

    @Test
    public void testAirConveyanceNormalYearAt1400() throws Exception {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));

        // Seed date to March 1, 2015 14:00 GMT
        calendar.clear();
        calendar.set(2015, 2, 1, 14, 0);

        // Setup test conditions
        String expected = "Q60";

        DateCodeBuilder dateCodeBuilder = DateCodeBuilderFactory.getDateCodeBuilder("Air");
        String actual = dateCodeBuilder.getCode(calendar);

        // Evaluate
        assertEquals("Surface conveyance date code generation failed for hour code at 1400",
                expected, actual);
    }

    @Test
    public void testAirConveyanceNormalYearAt1500() throws Exception {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));

        // Seed date to March 1, 2015 15:00 GMT
        calendar.clear();
        calendar.set(2015, 2, 1, 15, 0);

        // Setup test conditions
        String expected = "R60";

        DateCodeBuilder dateCodeBuilder = DateCodeBuilderFactory.getDateCodeBuilder("Air");
        String actual = dateCodeBuilder.getCode(calendar);

        // Evaluate
        assertEquals("Surface conveyance date code generation failed for hour code at 1500",
                expected, actual);
    }

    @Test
    public void testAirConveyanceNormalYearAt1600() throws Exception {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));

        // Seed date to March 1, 2015 16:00 GMT
        calendar.clear();
        calendar.set(2015, 2, 1, 16, 0);

        // Setup test conditions
        String expected = "S60";

        DateCodeBuilder dateCodeBuilder = DateCodeBuilderFactory.getDateCodeBuilder("Air");
        String actual = dateCodeBuilder.getCode(calendar);

        // Evaluate
        assertEquals("Surface conveyance date code generation failed for hour code at 1600",
                expected, actual);
    }

    @Test
    public void testAirConveyanceNormalYearAt1700() throws Exception {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));

        // Seed date to March 1, 2015 17:00 GMT
        calendar.clear();
        calendar.set(2015, 2, 1, 17, 0);

        // Setup test conditions
        String expected = "T60";

        DateCodeBuilder dateCodeBuilder = DateCodeBuilderFactory.getDateCodeBuilder("Air");
        String actual = dateCodeBuilder.getCode(calendar);

        // Evaluate
        assertEquals("Surface conveyance date code generation failed for hour code at 1700",
                expected, actual);
    }

    @Test
    public void testAirConveyanceNormalYearAt1800() throws Exception {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));

        // Seed date to March 1, 2015 18:00 GMT
        calendar.clear();
        calendar.set(2015, 2, 1, 18, 0);

        // Setup test conditions
        String expected = "U60";

        DateCodeBuilder dateCodeBuilder = DateCodeBuilderFactory.getDateCodeBuilder("Air");
        String actual = dateCodeBuilder.getCode(calendar);

        // Evaluate
        assertEquals("Surface conveyance date code generation failed for hour code at 1800",
                expected, actual);
    }

    @Test
    public void testAirConveyanceNormalYearAt1900() throws Exception {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));

        // Seed date to March 1, 2015 19:00 GMT
        calendar.clear();
        calendar.set(2015, 2, 1, 19, 0);

        // Setup test conditions
        String expected = "V60";

        DateCodeBuilder dateCodeBuilder = DateCodeBuilderFactory.getDateCodeBuilder("Air");
        String actual = dateCodeBuilder.getCode(calendar);

        // Evaluate
        assertEquals("Surface conveyance date code generation failed for hour code at 1900",
                expected, actual);
    }

    @Test
    public void testAirConveyanceNormalYearAt2000() throws Exception {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));

        // Seed date to March 1, 2015 20:00 GMT
        calendar.clear();
        calendar.set(2015, 2, 1, 20, 0);

        // Setup test conditions
        String expected = "W60";

        DateCodeBuilder dateCodeBuilder = DateCodeBuilderFactory.getDateCodeBuilder("Air");
        String actual = dateCodeBuilder.getCode(calendar);

        // Evaluate
        assertEquals("Surface conveyance date code generation failed for hour code at 2000",
                expected, actual);
    }

    @Test
    public void testAirConveyanceNormalYearAt2100() throws Exception {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));

        // Seed date to March 1, 2015 21:00 GMT
        calendar.clear();
        calendar.set(2015, 2, 1, 21, 0);

        // Setup test conditions
        String expected = "X60";

        DateCodeBuilder dateCodeBuilder = DateCodeBuilderFactory.getDateCodeBuilder("Air");
        String actual = dateCodeBuilder.getCode(calendar);

        // Evaluate
        assertEquals("Surface conveyance date code generation failed for hour code at 2100",
                expected, actual);
    }

    @Test
    public void testAirConveyanceNormalYearAt2200() throws Exception {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));

        // Seed date to March 1, 2015 22:00 GMT
        calendar.clear();
        calendar.set(2015, 2, 1, 22, 0);

        // Setup test conditions
        String expected = "Y60";

        DateCodeBuilder dateCodeBuilder = DateCodeBuilderFactory.getDateCodeBuilder("Air");
        String actual = dateCodeBuilder.getCode(calendar);

        // Evaluate
        assertEquals("Surface conveyance date code generation failed for hour code at 2200",
                expected, actual);
    }

    @Test
    public void testAirConveyanceNormalYearAt2300() throws Exception {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));

        // Seed date to March 1, 2015 23:00 GMT
        calendar.clear();
        calendar.set(2015, 2, 1, 23, 0);

        // Setup test conditions
        String expected = "Z60";

        DateCodeBuilder dateCodeBuilder = DateCodeBuilderFactory.getDateCodeBuilder("Air");
        String actual = dateCodeBuilder.getCode(calendar);

        // Evaluate
        assertEquals("Surface conveyance date code generation failed for hour code at 2300",
                expected, actual);
    }

    @Test
    public void testOceanConveyanceCalendarCodeNormal() throws Exception {
        DateCodeBuilder dateCodeBuilder = DateCodeBuilderFactory.getDateCodeBuilder("Ocean");
        Calendar calendar = Calendar.getInstance(Locale.getDefault());

        // Seed date to January 1, 2016 00:00 local time
        calendar.clear();
        calendar.set(2016, 0, 1, 0, 0);

        // Establish expected results
        String expected = dateFormat.format(calendar.getTime());

        // Generate actual results
        List<Date> dates = dateCodeBuilder.getCalendarDatesForCode("6001");
        String actual = dateFormat.format(dates.get(0).getTime());

        // Evaluate
        assertEquals("Ocean conveyance calendar date code generation failed", expected,
                actual);
    }

    @Test(expected = NumberFormatException.class)
    public void testOceanConveyanceCalendarCodeNumberFormat() throws Exception {
        DateCodeBuilder dateCodeBuilder = DateCodeBuilderFactory.getDateCodeBuilder("Ocean");
        // Expect method to throw exception
        List<Date> dates = dateCodeBuilder.getCalendarDatesForCode("A001");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOceanConveyanceCalendarCodeLengthTooShort() throws Exception {
        DateCodeBuilder dateCodeBuilder = DateCodeBuilderFactory.getDateCodeBuilder("Ocean");
        // Expect method to throw exception
        List<Date> dates = dateCodeBuilder.getCalendarDatesForCode("1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOceanConveyanceCalendarCodeLengthTooLong() throws Exception {
        DateCodeBuilder dateCodeBuilder = DateCodeBuilderFactory.getDateCodeBuilder("Ocean");
        // Expect method to throw exception
        List<Date> dates = dateCodeBuilder.getCalendarDatesForCode("10000");
    }

    @Test
    public void testSurfaceConveyanceCalendarCodeNormal() throws Exception {
        DateCodeBuilder dateCodeBuilder = DateCodeBuilderFactory.getDateCodeBuilder("Surface");
        Calendar calendar = Calendar.getInstance(Locale.getDefault());

        // Seed date to January 1, 2016 00:00 local time
        calendar.clear();
        calendar.set(2016, 0, 1, 0, 0);

        // Establish expected results
        String expected = dateFormat.format(calendar.getTime());

        // Generate actual results
        List<Date> dates = dateCodeBuilder.getCalendarDatesForCode("001");
        String actual = dateFormat.format(dates.get(0).getTime());

        // Evaluate
        assertEquals("Surface conveyance calendar date code generation failed", expected,
                actual);
    }

    @Test(expected = NumberFormatException.class)
    public void testSurfaceConveyanceCalendarCodeNumberFormat() throws Exception {
        DateCodeBuilder dateCodeBuilder = DateCodeBuilderFactory.getDateCodeBuilder("Surface");

        // Expect method to throw exception
        List<Date> dates = dateCodeBuilder.getCalendarDatesForCode("A01");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSurfaceConveyanceCalendarCodeLengthTooShort() throws Exception {
        DateCodeBuilder dateCodeBuilder = DateCodeBuilderFactory.getDateCodeBuilder("Surface");

        // Expect method to throw exception
        List<Date> dates = dateCodeBuilder.getCalendarDatesForCode("1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSurfaceConveyanceCalendarCodeLengthTooLong() throws Exception {
        DateCodeBuilder dateCodeBuilder = DateCodeBuilderFactory.getDateCodeBuilder("Surface");

        // Expect method to throw exception
        List<Date> dates = dateCodeBuilder.getCalendarDatesForCode("10000");
    }
}