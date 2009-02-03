//
//  WKTimestampUtilities.java
//  cheetah
//
//  Created by Kieran Kelleher on 3/15/06.
//  Copyright (c) 2006 Kieran Kelleher. All rights reserved.
//
package wk.foundation;


import java.util.Calendar;
import java.util.GregorianCalendar;

import org.apache.log4j.Logger;

import com.webobjects.foundation.NSComparator;
import com.webobjects.foundation.NSMutableArray;
import com.webobjects.foundation.NSTimeZone;
import com.webobjects.foundation.NSTimestamp;
import com.webobjects.foundation.NSTimestampFormatter;

public class WKTimestampUtilities {
    private static Logger log = Logger.getLogger( WKTimestampUtilities.class );

    protected final static String shortDateFormat = System.getProperty( "shortDateFormat", "%m/%d/%Y" );
    protected final static String shortDateTimeFormat = System.getProperty( "shortDateTimeFormat", "%m/%d/%Y %I:%M %p" );

    private WKTimestampUtilities() { }

    /** Adds absolute seconds to a timestamp. */
    public static NSTimestamp timestampByAddingSeconds( NSTimestamp ts, long intervalSeconds ) {
        return new NSTimestamp( ( intervalSeconds * 1000l ) + ts.getTime(), ts.getNanos() );
    }

    public static NSTimestamp timestampByAddingHours( NSTimestamp ts, long intervalHours) {
        return timestampByAddingSeconds(ts, intervalHours * 3600l);
    }

    /**
     * Null-safe comparison.
     * compare (null, null )	return {@link NSComparator#OrderedSame}
     * compare (null, <a non-null timestamp>)	return {@link NSComparator#OrderedAscending}
     * compare (<a non-null timestamp>, null )	return {@link NSComparator#OrderedDescending}
     * @param t1
     * @param t2
     * @return result of t1.compare(t2)
     */
    public static int compare(NSTimestamp t1, NSTimestamp t2) {
        if (t1 != null && t2 != null) {
            return t1.compare(t2);
        } else if (t1 == null && t2 == null) {
            return NSComparator.OrderedSame;
        } else if (t1 == null){
            return NSComparator.OrderedAscending;
        } else if (t2 == null){
            return NSComparator.OrderedDescending;
        }
        // This line is never called. Necessary for compile warning avoidance only.
        return 0;
    }

    /**
     * @param t1
     * @param t2
     * @return the later of two non-null timestamp objects.
     */
    public static NSTimestamp max(NSTimestamp t1, NSTimestamp t2) {
    	if (t2.after(t1)) {
			return t2;
    	} else {
    		return t1;
		} //~ if (t2.after(t1))
    }

    /**
     * @param t1
     * @param t2
     * @return the earlier of two non-null timestamp objects.
     */
    public static NSTimestamp min(NSTimestamp t1, NSTimestamp t2) {
    	if (t2.after(t1)) {
			return t1;
    	} else {
    		return t2;
		} //~ if (t2.after(t1))
    }

    /** Rolls a date on by a certain number of months and automatically increments the year
        if we are rolling from December thru January as we roll in a loop. The day of the month
        in the final result is handled by setting it to the closest date in the valid range
        of days for the resulting month. For example if we start in January 31 and roll one month, we
        will result in February 28 or 29, depending on whether we have a leap year of not. If we roll
        two months from Jan 31, we will get March 31. If we roll 3 months from Jan 31, we will result in April 30th.
        @see java.util.GregorianCalendar */
    public static NSTimestamp timestampByRollingMonths( NSTimestamp ts, int intervalMonths ) {
        GregorianCalendar calendar = gregorianCalendarFromTimestamp( ts );

        // Remember the rule for "roll" is that the larger fields will not change
        // So...
        for (int i = 0; i < intervalMonths; i++) {
            if (calendar.get( Calendar.MONTH ) == Calendar.DECEMBER ) {
                // Push the year forward one before rolling the month
                calendar.roll( Calendar.YEAR, 1 );
            }
            calendar.roll( Calendar.MONTH, 1 );
        }

        return timestampFromGregorianCalendar( calendar );
    }

    /** @return a timestamp by adding months according to Calendar.add algorithm. */
    public static NSTimestamp timestampByAddingMonths( NSTimestamp ts, int intervalMonths ) {
        GregorianCalendar calendar = gregorianCalendarFromTimestamp( ts );
        calendar.add( Calendar.MONTH, intervalMonths );
        return timestampFromGregorianCalendar( calendar );
    }

    /** @return a timestamp by adding months according to Calendar.add algorithm. */
    public static NSTimestamp timestampByAddingDays( NSTimestamp ts, int intervalDays ) {
        GregorianCalendar calendar = gregorianCalendarFromTimestamp( ts );
        calendar.add( Calendar.DATE, intervalDays );
        return timestampFromGregorianCalendar( calendar );
    }

    /** @return a timestamp by moving to the next Monday after the passed in timestamp. */
    public static NSTimestamp theNextMonday( NSTimestamp ts ) {
        return theNextSpecificWeekday( ts, Calendar.MONDAY );
    }


    /** @return a timestamp by moving to the next Monday after the passed in timestamp. */
    public static NSTimestamp theNextWednesday( NSTimestamp ts ) {
        return theNextSpecificWeekday( ts, Calendar.WEDNESDAY );
    }

    /** @return a timestamp by moving to the next Monday after the passed in timestamp.
        @param weekDay see Calendar class for constants Calendar.MONDAY, Calendar.TUESDAY, etc. */
    public static NSTimestamp theNextSpecificWeekday( NSTimestamp ts, int weekDay ) {
        GregorianCalendar calendar = gregorianCalendarFromTimestamp( ts );
        calendar.set( Calendar.DAY_OF_WEEK, weekDay );
        // If less than input date, then add 7 days since GregorianCalendar "rounds"
        // to the nearest weekDay
        if ( timestampFromGregorianCalendar( calendar ).before( ts ) ) {
            calendar.add( Calendar.DATE, 7 );
        }

        NSTimestamp result = timestampFromGregorianCalendar( calendar );
        if ( log.isDebugEnabled() ) log.debug("Input: weekDay = "
                                              + weekDay + "; Timestamp = "
                                              + WKTimestampUtilities.toDateTimeString( ts )
                                              + "; Output: result = "
                                              + WKTimestampUtilities.toDateTimeString( result ) );
        return timestampFromGregorianCalendar( calendar );
    }

    /** @return an NSTimestamp representing the gregorian calendar date argument */
    public static NSTimestamp timestampFromGregorianCalendar( GregorianCalendar calendar ) {
        return new NSTimestamp( calendar.getTime() );
    }

    /** @return a GregorianCalendar representing the timestamp */
    public static GregorianCalendar gregorianCalendarFromTimestamp( NSTimestamp ts ) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime( ts );
        return calendar;
    }

    /** @return NSTimestamp representing 11:59:59 pm on the last day represented by the year and month arguments */
    public static NSTimestamp endOfTheMonth( int year, int month ) {
        // Since months have different numbers of days, this requires a few steps
        // and we will use GregorianCalendar ot make this easier

        // Create GregorianCalendar for the first day of the month midnight
        GregorianCalendar calendar = new GregorianCalendar( year, month, 1, 0, 0, 0 );

        // Add one month to the calendar to get the first day of the following month
        calendar.add( GregorianCalendar.MONTH, 1 );

        // Subtract one day from the calendar to get the last day of the original month
        calendar.add( GregorianCalendar.SECOND, -1 );

        if ( log.isDebugEnabled() ) log.debug("args: year = " + year + "; month = " + month
                                              + "; result = " + calendar );

        // Convert it to a NSTimestamp and return
        return timestampFromGregorianCalendar( calendar );
    }

    /** Timestamp representing date at 12:00 AM. Implementation
        * wise this method subtracts the current hours, minutes and
        * seconds from the timestamp.
        @return the date without the time portion. */
    public static NSTimestamp dateFromNSTimestamp( NSTimestamp ts ) {
        return (new NSTimestamp()).timestampByAddingGregorianUnits(0, 0, 0, -ts.hourOfDay(), -ts.minuteOfHour(), -ts.secondOfMinute());
    }

    /** @return the date representation of the timestamp as a displayable string. */
    public static String toDateString( NSTimestamp timeStamp ) {
        NSTimestampFormatter formatter = WKTimestampFormatter.dateFormatterForPattern( shortDateFormat );
        return formatter.format( timeStamp );
    }

    /** @return the date representation of the timestamp as a displayable string. */
    public static String toDateTimeString( NSTimestamp timeStamp ) {
        NSTimestampFormatter formatter = WKTimestampFormatter.dateFormatterForPattern( shortDateTimeFormat );
        return formatter.format( timeStamp );
    }

    /** @return the NSTimestamp representation of the string */
    public static NSTimestamp timestampFromString( String timestampString ) throws java.text.ParseException {
        NSTimestampFormatter formatter = WKTimestampFormatter.dateFormatterForPattern( shortDateTimeFormat );
        return (NSTimestamp)formatter.parseObject( timestampString );
    }

    /**
     * @param ts
     * @return month year formatted timestamp. For example "July 2008"
     */
    public static String toMonthYearString( NSTimestamp ts ) {
    	NSTimestampFormatter formatter = WKTimestampFormatter.dateFormatterForPattern("%B %Y");
    	return formatter.format(ts);
    }

    /**
     * @param ts
     * @param days. Can be positive (forward in time) or negative (back in time)
     * @return a timestamp offset by numWeekdays. Saturdays and Sundays are skipped during the offset calculation.
     */
    public static NSTimestamp timestampByAddingWeekdays(NSTimestamp ts, int numWeekdays) {
        if (numWeekdays == 0) {
            return ts;
        } //~ if (numWeekdays == 0)

        int direction = (numWeekdays > 0 ? 1 : -1);

        GregorianCalendar calendar = gregorianCalendarFromTimestamp( ts );
        for (int i = 1; i <= Math.abs(numWeekdays); i++) {
            calendar.add(Calendar.DATE, direction);
            // While in a weekend, keep moving
            while ((calendar.get(Calendar.DAY_OF_WEEK) == calendar.SATURDAY)
                    || (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)) {
                calendar.add(Calendar.DATE, direction);
            }
        }
        return timestampFromGregorianCalendar(calendar);
    }


    //******** From Simon McLean on mailing list ********************
    public static String calcHM(int timeInSeconds) {
        int hours, minutes;
        hours = timeInSeconds / 3600;
        timeInSeconds = timeInSeconds - (hours * 3600);
        minutes = timeInSeconds / 60;
        return hours + "h " + minutes + "m";
    }


    public static String calcHMS(int timeInSeconds) {
        int hours, minutes, seconds;
        hours = timeInSeconds / 3600;
        timeInSeconds = timeInSeconds - (hours * 3600);
        minutes = timeInSeconds / 60;
        timeInSeconds = timeInSeconds - (minutes * 60);
        seconds = timeInSeconds;
        return hours + "h " + minutes + "m " + seconds + "s";
    }


    public static NSTimestamp createTimestamp(NSTimestamp date, NSTimestamp time) {
        int year = year(date);
        int month = month(date);
        int day = dayOfMonth(date);
        int hours = hourOfDay(time);
        int minutes = minute(time);
        int seconds = second(time);
        NSTimeZone tz = NSTimeZone.systemTimeZone();
        return new NSTimestamp(year, month, day, hours, minutes, seconds, tz);
    }


    public static NSMutableArray dateArray(NSTimestamp from, NSTimestamp to) {
        NSMutableArray dateList = new NSMutableArray();
        NSTimestamp d = normalisedDate(from);
        while (d.compare(normalisedDate(to)) <= 0) {
            dateList.insertObjectAtIndex(d, dateList.count());
            d = d.timestampByAddingGregorianUnits(0, 0, 1, 0, 0, 0);
        }
        return dateList;
    }


    public static NSMutableArray dateArrayFromDateWithLength(NSTimestamp date, int length) {
        NSMutableArray dateList = new NSMutableArray();
        NSTimestamp d = normalisedDate(date);
        while (dateList.count() < length) {
            dateList.insertObjectAtIndex(d, dateList.count());
            d = d.timestampByAddingGregorianUnits(0, 0, 1, 0, 0, 0);
        }
        return dateList;
    }


    public static int dayOfMonth(NSTimestamp date) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        return gc.get(GregorianCalendar.DAY_OF_MONTH);
    }


    public static int dayOfWeek(NSTimestamp aDate) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(aDate);
        return gc.get(GregorianCalendar.DAY_OF_WEEK);
    }


    public static String dayOfWeek(NSTimestamp aDate, boolean longName) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(aDate);
        int d = gc.get(GregorianCalendar.DAY_OF_WEEK);
        if (d == 1) {
            return longName ? "Sunday" : "Sun";
        }
        if (d == 2) {
            return longName ? "Monday" : "Mon";
        }
        if (d == 3) {
            return longName ? "Tuesday" : "Tue";
        }
        if (d == 4) {
            return longName ? "Wednesday" : "Wed";
        }
        if (d == 5) {
            return longName ? "Thursday" : "Thur";
        }
        if (d == 6) {
            return longName ? "Friday" : "Fri";
        }
        if (d == 7) {
            return longName ? "Saturday" : "Sat";
        }
        return null;
    }


    public static String dayOfWeekFromInteger(String aDay, boolean longName) {
        if (aDay == "1") {
            return longName ? "Sunday" : "Sun";
        }
        if (aDay == "2") {
            return longName ? "Monday" : "Mon";
        }
        if (aDay == "3") {
            return longName ? "Tuesday" : "Tue";
        }
        if (aDay == "4") {
            return longName ? "Wednesday" : "Wed";
        }
        if (aDay == "5") {
            return longName ? "Thursday" : "Thur";
        }
        if (aDay == "6") {
            return longName ? "Friday" : "Fri";
        }
        if (aDay == "7") {
            return longName ? "Saturday" : "Sat";
        }
        return null;
    }


    public static int dayOfYear(NSTimestamp date) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        return gc.get(GregorianCalendar.DAY_OF_YEAR);
    }


    public static int daysBetweenDates(NSTimestamp t1, NSTimestamp t2) {
        if (t1 == null || t2 == null) {
            return 0;
        }
        GregorianCalendar gc1 = new GregorianCalendar();
        gc1.setTime(WKTimestampUtilities.normalisedDateGMT(t1));
        GregorianCalendar gc2 = new GregorianCalendar();
        gc2.setTime(WKTimestampUtilities.normalisedDateGMT(t2));
        int diff = (int) ((gc1.getTime().getTime() - gc2.getTime().getTime()) / (1000 * 60 * 60 * 24));
        return diff >= 0 ? diff : -1 * diff;
    }


    public static int daysInMonth(NSTimestamp date) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        return gc.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);
    }


    public static NSTimestamp endDate(NSTimestamp aDate) {
        return (WKTimestampUtilities.startDate(aDate)).timestampByAddingGregorianUnits(0, 1, -1, 0, 0, 0);
    }


    public static NSTimestamp gmtDate(NSTimestamp date) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        int d = gc.get(GregorianCalendar.DAY_OF_MONTH);
        int m = gc.get(GregorianCalendar.MONTH) + 1;
        int y = gc.get(GregorianCalendar.YEAR);
        int h = gc.get(GregorianCalendar.HOUR_OF_DAY);
        int n = gc.get(GregorianCalendar.MINUTE);
        int s = gc.get(GregorianCalendar.SECOND);
        NSTimeZone tz = NSTimeZone.timeZoneWithName("Etc/GMT", true);
        return new NSTimestamp(y, m, d, h, n, s, tz);
    }


    public static int hourOfDay(NSTimestamp date) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        return gc.get(GregorianCalendar.HOUR_OF_DAY);
    }


    public static int intOfWeek(NSTimestamp aDate) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(aDate);
        return gc.get(GregorianCalendar.DAY_OF_WEEK);
    }


    public static int minute(NSTimestamp date) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        return gc.get(GregorianCalendar.MINUTE);
    }


    public static int month(NSTimestamp date) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        return gc.get(GregorianCalendar.MONTH) + 1;
    }


    public static NSTimestamp normalisedDate(NSTimestamp date) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        int d = gc.get(GregorianCalendar.DAY_OF_MONTH);
        int m = gc.get(GregorianCalendar.MONTH) + 1;
        int y = gc.get(GregorianCalendar.YEAR);
        NSTimeZone tz = NSTimeZone.systemTimeZone();
        return new NSTimestamp(y, m, d, 12, 0, 0, tz);
    }


    public static NSTimestamp normalisedDateGMT(NSTimestamp date) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        int d = gc.get(GregorianCalendar.DAY_OF_MONTH);
        int m = gc.get(GregorianCalendar.MONTH) + 1;
        int y = gc.get(GregorianCalendar.YEAR);
        NSTimeZone tz = NSTimeZone.timeZoneWithName("Etc/GMT", true);
        return new NSTimestamp(y, m, d, 12, 0, 0, tz);
    }


    public static NSTimestamp normalisedTime(NSTimestamp date) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        int h = gc.get(GregorianCalendar.HOUR_OF_DAY);
        int m = gc.get(GregorianCalendar.MINUTE);
        NSTimeZone tz = NSTimeZone.systemTimeZone();
        return new NSTimestamp(2000, 1, 1, h, m, 0, tz);
    }


    public static int second(NSTimestamp date) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        return gc.get(GregorianCalendar.SECOND);
    }


    public static int secondsBetweenDates(NSTimestamp t1, NSTimestamp t2) {
        GregorianCalendar gc1 = new GregorianCalendar();
        gc1.setTime(t1);
        GregorianCalendar gc2 = new GregorianCalendar();
        gc2.setTime(t2);
        int diff = (int) ((gc1.getTime().getTime() - gc2.getTime().getTime()) / 1000);
        return diff >= 0 ? diff : -1 * diff;
    }


    public static NSTimestamp startDate(NSTimestamp aDate) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(aDate);
        int y = gc.get(GregorianCalendar.YEAR);
        int m = gc.get(GregorianCalendar.MONTH) + 1;
        NSTimeZone tz = NSTimeZone.timeZoneWithName("Etc/GMT", true);
        return new NSTimestamp(y, m, 1, 0, 0, 0, tz);
    }


    public static int workingDaysBetweenDates(NSTimestamp start, NSTimestamp end) {

        NSTimestamp day = WKTimestampUtilities.normalisedDate(start);
        int workingDays = 0;
        for (int i = 0; i < WKTimestampUtilities.daysBetweenDates(WKTimestampUtilities.normalisedDate(start), WKTimestampUtilities
                .normalisedDate(end)); i++) {
            if (WKTimestampUtilities.dayOfWeek(day, false).equals("Mon") || WKTimestampUtilities.dayOfWeek(day, false).equals("Tue")
                    || WKTimestampUtilities.dayOfWeek(day, false).equals("Wed")
                    || WKTimestampUtilities.dayOfWeek(day, false).equals("Thur")
                    || WKTimestampUtilities.dayOfWeek(day, false).equals("Fri")) {
                workingDays++;

            }
            day = day.timestampByAddingGregorianUnits(0, 0, 1, 0, 0, 0);
        }
        return workingDays;

    }


    public static int year(NSTimestamp date) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        return gc.get(GregorianCalendar.YEAR);
    }


}
