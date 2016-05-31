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

import org.junit.Test;

import java.util.Calendar;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class DateCodeUnitTest {

    @Test
    public void testSurfaceConveyanceCode() throws Exception {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));

        // Seed date to January 1, 2016 00:01 (one minute after midnight)
        calendar.clear();
        calendar.set(2016, 0, 1, 0, 1);

        // Setup test conditions
        String expected = "001";
        String actual = DateCode.getInstance().getCode(ConveyanceType.Surface.toString(), calendar);

        // Evaluate
        assertEquals("Surface conveyance date code generation failed", expected,
                actual);
    }

    @Test
    public void testOceanConveyanceCode() throws Exception {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));

        // Seed date to January 1, 2016 00:01 (one minute after midnight)
        calendar.clear();
        calendar.set(2016, 0, 1, 0, 1);

        // Setup test conditions
        String expected = "6001";
        String actual = DateCode.getInstance().getCode(ConveyanceType.Ocean.toString(), calendar);

        // Evaluate
        assertEquals("Ocean conveyance date code generation failed", expected,
                actual);
    }

    @Test
    public void testAirConveyanceCodeAtMidnight() throws Exception {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));

        // Seed date to January 1, 2016 00:00 (midnight)
        calendar.clear();
        calendar.set(2016, 0, 1, 0, 0);

        // Setup test conditions
        String expected = "A01";
        String actual = DateCode.getInstance().getCode(ConveyanceType.Air.toString(), calendar);

        // Evaluate
        assertEquals("Air conveyance date code generation failed", expected,
                actual);
    }

    @Test
    public void testAirConveyanceCodeAtOneAM() throws Exception {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));

        // Seed date to January 1, 2016 00:00 (midnight)
        calendar.clear();
        calendar.set(2016, 0, 1, 1, 0);

        // Setup test conditions
        String expected = "B01";
        String actual = DateCode.getInstance().getCode(ConveyanceType.Air.toString(), calendar);

        // Evaluate
        assertEquals("Air conveyance date code generation failed", expected,
                actual);
    }

    @Test
    public void testAirConveyanceCodeAtTwoAM() throws Exception {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));

        // Seed date to January 1, 2016 00:00 (midnight)
        calendar.clear();
        calendar.set(2016, 0, 1, 2, 0);

        // Setup test conditions
        String expected = "C01";
        String actual = DateCode.getInstance().getCode(ConveyanceType.Air.toString(), calendar);

        // Evaluate
        assertEquals("Air conveyance date code generation failed", expected,
                actual);
    }
}