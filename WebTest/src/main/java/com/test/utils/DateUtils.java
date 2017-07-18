/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package com.test.utils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public final class DateUtils {
    private static Log log = LogFactory.getLog(DateUtils.class);
    private static final String DATEFORMAT_DATE = "yyyy-MM-dd";

    public static boolean isDateConflicting(Calendar start1, Calendar end1, Calendar start2, Calendar end2, String week) {
        start1.set(14, 0);
        end1.set(14, 0);
        start2.set(14, 0);
        end2.set(14, 0);
        boolean flag = false;
        if ((start1.getTime().compareTo(start2.getTime()) > 0) && (start1.getTime().compareTo(end2.getTime()) <= 0)
                && (end1.getTime().compareTo(end2.getTime()) >= 0)) {
            flag = compareCal(start1, end2, week);
        } else if ((end1.getTime().compareTo(start2.getTime()) >= 0) && (end1.getTime().compareTo(end2.getTime()) < 0)
                && (start1.getTime().compareTo(start2.getTime()) <= 0)) {
            flag = compareCal(start2, end1, week);
        } else if ((start1.getTime().compareTo(start2.getTime()) > 0)
                && (start1.getTime().compareTo(end2.getTime()) < 0) && (end1.getTime().compareTo(start2.getTime()) > 0)
                && (end1.getTime().compareTo(end2.getTime()) < 0)) {
            flag = compareCal(start1, end1, week);
        } else if ((start1.getTime().compareTo(start2.getTime()) <= 0)
                && (end1.getTime().compareTo(end2.getTime()) >= 0)) {
            flag = compareCal(start2, end2, week);
        }

        return flag;
    }

    private static boolean compareCal(Calendar cal1, Calendar cal2, String week) {
        if (cal1.getTime().compareTo(cal2.getTime()) == 0) {
            int day = cal1.get(7);
            if (day == 1)
                day = 7;
            else {
                --day;
            }
            if (week.indexOf(String.valueOf(day)) != -1)
                return true;
        } else {
            Calendar temp = (Calendar) cal1.clone();
            temp.add(5, 6);
            if (temp.getTime().compareTo(cal2.getTime()) <= 0) {
                return true;
            }
            int day1 = cal1.get(7);
            int day2 = cal2.get(7);

            if (day1 == 1)
                day1 = 7;
            else {
                --day1;
            }
            if (day2 == 1)
                day2 = 7;
            else {
                --day2;
            }
            if (day1 < day2) {
                for (int i = day1; i <= day2; ++i)
                    if (week.indexOf(String.valueOf(i)) != -1)
                        return true;
            } else {
                for (int i = day1; i <= 7; ++i) {
                    if (week.indexOf(String.valueOf(i)) != -1) {
                        return true;
                    }
                }
                for (int i = 1; i <= day2; ++i) {
                    if (week.indexOf(String.valueOf(i)) != -1) {
                        return true;
                    }
                }
            }

        }

        return false;
    }

    public static Date getNextDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(5, 1);
        return calendar.getTime();
    }

    public static String getDateForward(Date USASDate, int fieldType, int amount) {
        Date currentDate = new Date();
        Calendar cal = Calendar.getInstance();
        if (null != USASDate) {
            currentDate = USASDate;
        }
        cal.setTime(currentDate);
        cal.add(fieldType, amount);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        return sdf.format(cal.getTime());
    }

    public static Timestamp getCurOpTime() {
        return new Timestamp(System.currentTimeMillis());
    }

    public static String getCurrentTimeStamp() {
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String str = sdf.format(d);
        str = str.replaceFirst(" ", "T") + "Z";
        return str;
    }

    public static String formatDate(Date date, String pattern, Locale locale) {
        if ((null == date) || (null == pattern)) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(pattern, locale);
        return sdf.format(date);
    }

    public static String formatDate(Date date, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }

    public static Date parseDate(String date, String pattern, Locale locale) {
        if ((StringUtils.isBlank(date)) || (StringUtils.isBlank(pattern))) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(pattern, locale);
        try {
            return sdf.parse(date);
        } catch (ParseException e) {
        }
        return null;
    }

    public static boolean isDate(String date, String pattern, Locale locale) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern, locale);
        try {
            String temp = sdf.format(sdf.parse(date));
            return temp.equalsIgnoreCase(date);
        } catch (ParseException e) {
        }
        return false;
    }

    public static Date addDays(Date date, int days) {
        Calendar cd = Calendar.getInstance();
        cd.setTime(date);
        cd.add(5, days);
        return cd.getTime();
    }

    public static String addDays(String dateddMMMyy, int days) {
        try {
            Date date = stringUS2Date(dateddMMMyy);
            Date newdate = addDays(date, days);
            return date2StringUS(newdate);
        } catch (ParseException e) {
        }
        return null;
    }

    public static Date addYears(Date date, int years) {
        Calendar cd = Calendar.getInstance();
        cd.setTime(date);
        cd.add(1, years);
        return cd.getTime();
    }

    public static String addYears(String dateStr, int years) {
        try {
            Date date = stringUS2Date(dateStr);
            Date newDate = addYears(date, years);
            return date2StringUS(newDate);
        } catch (ParseException e) {
        }
        return null;
    }

    public static long subDays(String datestr1, String datestr2) throws ParseException {
        Date date1 = stringUS2Date(datestr1);
        Date date2 = stringUS2Date(datestr2);
        long milliseconds = date1.getTime() - date2.getTime();
        return (milliseconds / 86400000L);
    }

    public static long subDays(Date date1, Date date2) {
        long milliseconds = date1.getTime() - date2.getTime();
        return (milliseconds / 86400000L);
    }

    public static String getLastDayofWeek(String dateddMMMyy, int firstdayofweek) throws ParseException {
        Date date = stringUS2Date(dateddMMMyy);
        int weekday = dayofWeek(date);
        int offset = weekday - firstdayofweek;
        if (offset < 0) {
            offset += 7;
        }
        Date lastdate = addDays(date, -1 * offset + 6);
        return date2StringUS(lastdate);
    }

    public static String getFirstDayofWeek(String dateddMMMyy, int firstdayofweek) throws ParseException {
        Date date = stringUS2Date(dateddMMMyy);
        int weekday = dayofWeek(date);
        int offset = weekday - firstdayofweek;
        if (offset < 0) {
            offset += 7;
        }
        Date lastdate = addDays(date, -1 * offset);
        return date2StringUS(lastdate);
    }

    public static int dayofWeek(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int d = c.get(7) - 1;
        d = (d == 0) ? 7 : d;
        return d;
    }

    public static String date2StringUS(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("ddMMMyy", Locale.US);
        sdf.setTimeZone(TimeZone.getDefault());
        String s = sdf.format(date);
        return s.toUpperCase();
    }

    public static String date2String(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setTimeZone(TimeZone.getDefault());
        String s = sdf.format(date);
        return s.toUpperCase();
    }

    public static Date stringUS2Date(String ddMMMyy) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("ddMMMyy", Locale.US);
        sdf.setTimeZone(TimeZone.getDefault());
        return sdf.parse(ddMMMyy);
    }

    public static Date string2Date(String yyyyMMdd) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setTimeZone(TimeZone.getDefault());
        return sdf.parse(yyyyMMdd);
    }

    public static String getLastDayOfMonth(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(2, 1);
        calendar.set(5, 0);
        return df.format(calendar.getTime());
    }

    public static Date now() throws ParseException {
        Calendar now = Calendar.getInstance(TimeZone.getDefault());
        return now.getTime();
    }

    public static String getFrequence(String day) {
        if (day.length() == 7) {
            return "D";
        }
        if (day.length() < 5) {
            return day;
        }
        StringBuffer result = new StringBuffer("X");
        for (int i = 1; i < 8; ++i) {
            if (day.indexOf(String.valueOf(i)) < 0) {
                result.append(i);
            }
        }
        return result.toString();
    }

    public static String getFrequence(String day, int add) {
        if (day.length() == 7) {
            return "D";
        }
        StringBuffer sbtmp = new StringBuffer();
        if (day.length() < 5) {
            if (add == 0) {
                return day;
            }
            int temp = Integer.parseInt(day);
            while (temp > 0) {
                int m = (temp % 10 + add) % 7;
                temp /= 10;
                if (m == 0)
                    sbtmp.append(7);
                else {
                    sbtmp.insert(0, m);
                }
            }
            return sbtmp.toString();
        }
        if (add != 0) {
            int temp = Integer.parseInt(day);
            while (temp > 0) {
                int m = (temp % 10 + add) % 7;
                if (m == 0) {
                    m = 7;
                }
                temp /= 10;
                sbtmp.append(m);
            }
        } else {
            sbtmp.append(day);
        }
        StringBuffer result = new StringBuffer("X");
        for (int i = 1; i < 8; ++i) {
            if (sbtmp.indexOf(String.valueOf(i)) < 0) {
                result.append(i);
            }
        }
        return result.toString();
    }

    public static boolean isDateStringValid(String date, String dateFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);

        if ((date != null) && (!(date.equals("")))) {
            try {
                sdf.format(sdf.parse(date));
                return true;
            } catch (ParseException e) {
                return false;
            }
        }
        return false;
    }

    public static Date getRightDate(Date bDate, Date eDate, String frequece) {
        if ((frequece == null) || (frequece.length() < 1)) {
            return null;
        }

        Date now = new Date();
        try {
            now = string2Date(date2String(now()));
        } catch (ParseException e) {
            log.warn(e.getMessage());
        }
        Date standDate = (subDays(bDate, now) > 3L) ? bDate : addDays(now, -3);
        int dayInWeek = dayofWeek(standDate);

        char[] arrFreq = frequece.toCharArray();
        int length = arrFreq.length;
        int[] iFreq = new int[length];
        for (int i = 0; i < length; ++i) {
            iFreq[i] = ((Integer.parseInt(String.valueOf(arrFreq[i])) + 7 - dayInWeek) % 7);
        }
        int chday = min(iFreq);
        Date rDate = addDays(standDate, chday);
        if ((eDate != null) && (eDate.before(rDate))) {
            return null;
        }
        return rDate;
    }

    public static int min(int[] data) {
        int length = data.length;
        if (length > 1) {
            int tmin = data[0];
            for (int i = 1; i < length; ++i) {
                tmin = (tmin < data[i]) ? tmin : data[i];
            }
            return tmin;
        }
        if (length < 1) {
            return -1;
        }
        return data[0];
    }

    public static String[] getDateRange(String USASString) throws ParseException {
        Date currentDate = new Date();
        if ((null != USASString) && (!(USASString.trim().equals("")))) {
            currentDate = string2Date(USASString);
        }
        return getDateRange(currentDate);
    }

    public static String[] getDateRange(Date USASDate) {
        Date currentDate = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(currentDate);
        String currentDateStr = formatDate(cal.getTime(), "yyyy-MM-dd", Locale.ENGLISH);
        String[] result = { currentDateStr, currentDateStr };
        if (null != USASDate) {
            currentDate = USASDate;
        }

        cal.setTime(currentDate);
        cal.add(3, -1);
        cal.set(7, 2);
        result[0] = formatDate(cal.getTime(), "yyyy-MM-dd", Locale.ENGLISH);

        cal.setTime(currentDate);
        cal.add(5, 591);
        result[1] = formatDate(cal.getTime(), "yyyy-MM-dd", Locale.ENGLISH);
        return result;
    }

    public static Date sectionString2Date(String ddMMM) {
        if ((ddMMM == null) || ("".equals(ddMMM))) {
            return null;
        }

        String tempDdMMM = ddMMM;
        if (ddMMM.length() == 5) {
            tempDdMMM = ddMMM + "72";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("ddMMMyy", Locale.US);
        sdf.setTimeZone(TimeZone.getDefault());
        try {
            return sdf.parse(tempDdMMM);
        } catch (ParseException e) {
        }
        return null;
    }

    public static String date2SectionString(Date d) {
        if (null == d) {
            return "---";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
        sdf.setTimeZone(TimeZone.getDefault());
        String s = sdf.format(d);
        return s.toUpperCase();
    }

    public static String time2String(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getDefault());
        String s = sdf.format(date);
        return s.toUpperCase();
    }

    public static boolean isDateMatches(String str) {
        return (str.matches("\\d{4}/\\d+/\\d+;-;-"));
    }

    public static Date getCurrentDay() {
        return new Date();
    }

    public static Date getCurrentDay(int ch) {
        Date curDate = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(curDate);
        cal.add(5, ch);
        return cal.getTime();
    }

    public static boolean isTime(String time) {
        if (StringUtils.isBlank(time)) {
            return false;
        }
        if (time.length() != 4) {
            return false;
        }
        if (!(StringUtils.isNumeric(time))) {
            return false;
        }
        String hour = time.substring(0, 2);
        String min = time.substring(2);
        int hourInt = Integer.parseInt(hour);
        int minInt = Integer.parseInt(min);

        return ((hourInt >= 0) && (hourInt < 24) && (minInt >= 0) && (minInt < 60));
    }

    public static long diffDays(Date one, Date two) {
        return ((one.getTime() - two.getTime()) / 86400000L);
    }

    public static Date changeStringToDate(String date, String form) {
        try {
            SimpleDateFormat df = new SimpleDateFormat(form);
            return df.parse(date);
        } catch (ParseException e) {
            log.warn(e.getMessage());
        }
        return null;
    }

    public static String changeDateToString(Date date, String form) {
        SimpleDateFormat df = new SimpleDateFormat(form);
        return df.format(date);
    }

    public static String calculateDate(String originalDate, int i) {
        Date date = changeStringToDate(originalDate, "yyyy-MM-dd");
        String resultDate = "";
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(5, i);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        resultDate = sdf.format(cal.getTime());
        return resultDate;
    }
}