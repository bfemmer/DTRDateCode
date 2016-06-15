# DTRDateCode
Android app for calculating TCMD date shipped and received codes

## Purpose

DTRDateCode is a simple Android mobile app that calculates the date shipped and received codes used on advanced Transportation Control and Movement Documents as specified in USTRANSCOM Defense Transportation Regulation (DTR) - Part II (Cargo Movement) Appendix RR (Date Shipped and Received Codes) dated May 2014 which can be downloaded from http://www.ustranscom.mil/dtr/part-ii/dtr_part_ii_app_rr.pdf. 

![ScreenShot](dtrdatecode_screenshot.png)

The app can be configured (via settings) to calculate surface conveyance date codes in accordance with paragraph B (including the 4-digit codes used for ocean conveyance), and air conveyance hour/day codes in accordance with paragraph C. For air conveyance hour codes, the program takes into account the local timezone. In addition, the app can reverse-calculate dates from an existing date code.

*Note: the author is an Air Transportation Craftsman (2T271) with the 38th APS; hence, the squadron patch you see displayed on the app and in the screenshot above. Feel free to replace.*

## Usage

### Current Date Code

The app displays the current date code (at the top of the main screen) in the format of the conveyance type selected in *Settings*.

### Changing Date Code Format

The three date code formats are *Air*, *Surface*, and *Ocean*. The default format is *Air*, and can be changed via *Settings*.

### Looking Up a Date Code

Clicking on the *Calculate Calendar Date From Code* button at the bottom of the screen brings up a dialog for entering a date code. If the date code is valid with the *OK* button is clicked, then a calendar will display showing the date corresponding with the date code. If *Air* conveyance is selected, then the calendar dates corresponding with the date code for the previous year will be listed.

## Development

The project is built with Android Studio 2.1.1.
