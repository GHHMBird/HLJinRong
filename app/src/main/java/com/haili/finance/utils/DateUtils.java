package com.haili.finance.utils;

import android.annotation.SuppressLint;
import android.content.Context;

import com.haili.finance.modle.CustomDate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import hirondelle.date4j.DateTime;


public class DateUtils {

    public static int getYear() {
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    public static int getMonth() {
        return Calendar.getInstance().get(Calendar.MONTH) + 1;
    }

    public static final String FORMAT_YMD = "YY年M月D日";
    public static final String FORMAT_YYMMDD = "YYYY-MM-DD";
    public static final String FORMAT_YYMMDD_C = "YYYY年MM月DD日";
    public static final String FORMAT_MMDD = "MM月DD日";
    public static final String FORMAT_MD = "M月D日";
    public static final String FORMAT_MMDDHHMM_C = "M月D日 hh:mm";
    public static final String FORMAT_MMDD1 = "MM-DD";
    public static final String FORMAT_YMD1 = "YY-M-D";
    public static final String FORMAT_DM = "D/M";
    public static final String FORMAT_DMY = "D/M/YYYY";


    private static SimpleDateFormat dateFormatYYMMDD = new SimpleDateFormat(
            "yyyy-MM-dd");

    private static SimpleDateFormat dateFormat2 = new SimpleDateFormat(
            "yyyy年MM月dd日");

    private static SimpleDateFormat dateFormat22 = new SimpleDateFormat(
            "yyyy年MM月dd日 HH:mm");


    private static SimpleDateFormat dateFormat4 = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");

    private static SimpleDateFormat MMddFormat = new SimpleDateFormat("MM月dd日");

    private static SimpleDateFormat MMddFormatX = new SimpleDateFormat("MM-dd");

    private static SimpleDateFormat dateFormat5 = new SimpleDateFormat(
            "yyyy年MM月dd日 HH:mm:ss");

    private static SimpleDateFormat dateFormat6 = new SimpleDateFormat(
            "MM月dd日 HH:mm");

    private static SimpleDateFormat dateFormat66 = new SimpleDateFormat(
            "MM-dd HH:mm");


    private static SimpleDateFormat dateFormat7 = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm");

    private static SimpleDateFormat dateFormat8 = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");

    private static SimpleDateFormat dateFormatYMDHM = new SimpleDateFormat(
            "yyyy年MM月dd日 HH:mm");

    private static SimpleDateFormat dateFormatHHMM = new SimpleDateFormat(
            "HH:mm");

    private static SimpleDateFormat dateFormatDM = new SimpleDateFormat(
            "D/M");

    private static SimpleDateFormat dateFormatDDMM = new SimpleDateFormat(
            "MM月dd日");
//    private static SimpleDateFormat dateFormatDMY = new SimpleDateFormat(
//            "D/M/YYYY");

    public static String getProcessTime() {
        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        return dateFormat4.format(date);
    }

    public static DateTime getCurrentDate() {
        return new DateTime(formatDate(new Date()));
    }

    public static String getDateFromLong(long time) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        return dateFormat2.format(cal.getTime());
    }

    public static String getDateFromLong2(long time) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        return dateFormat4.format(cal.getTime());
    }

    public static String getDateFromLong3(long time) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        return dateFormatYYMMDD.format(cal.getTime());
    }

    public static String getDateFromLong7(long time) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        return dateFormat7.format(cal.getTime());
    }

    public static String getDateFromLong8(long time) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        return MMddFormat.format(cal.getTime());
    }

    public static String getDateFromLongX(long time) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        return MMddFormatX.format(cal.getTime());
    }

    public static String getDateFromLongTravel(long time) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        return dateFormat6.format(cal.getTime());
    }


    public static String getDateFromLongMMHH(long time) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        return dateFormatHHMM.format(cal.getTime());
    }

    public static long disposeDate(DateTime mDateTime){
        Calendar c = Calendar.getInstance();
        if(mDateTime==null){
            return c.getTimeInMillis();
        }
        String dateTime = "" +mDateTime;
        try {
            c.setTime(dateFormatYYMMDD.parse(dateTime));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return c.getTimeInMillis();
    }

    public static long getDateTimeFromLong(DateTime mDateTime){
        Calendar c = Calendar.getInstance();
        if(mDateTime==null){
            return c.getTimeInMillis();
        }
        String dateTime = "" +mDateTime;
        try {
            c.setTime(dateFormat4.parse(dateTime));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return c.getTimeInMillis();
    }

    public static String formatDateWithTime(Date time) {
        if (time == null) {
            return "";
        }
        return dateFormat4.format(time);
    }

    public static String formatDateWithTime7(Date time) {
        if (time == null) {
            return "";
        }
        return dateFormat7.format(time);
    }

    public static String formatDate(Date date) {
        if (date == null) {
            return "";
        }
        return dateFormatYYMMDD.format(date);
    }

    public static String formatDateHHMM(Date date) {
        if (date == null) {
            return "";
        }
        return dateFormatHHMM.format(date);
    }

    public static String dateFormatDDMM(Date date) {
        if (date == null) {
            return "";
        }
        return dateFormatDDMM.format(date);
    }


    public static String shortDate(Date date) {
        Calendar cal = Calendar.getInstance();
        int currentYear = cal.get(Calendar.YEAR);
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);
        if (year == currentYear) {
            return MMddFormat.format(date);
        } else {
            return dateFormat2.format(date);
        }
    }

    public static String shortDate2(Date date) {
        Calendar cal = Calendar.getInstance();
        int currentYear = cal.get(Calendar.YEAR);
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);
        if (year == currentYear) {
            return dateFormat6.format(date);
        } else {
            return dateFormat22.format(date);
        }
    }


    public static String formatDateYMD(Date date) {
        if (date == null) {
            return "";
        }
        return dateFormat2.format(date);
    }

    public static Date dateFromString(String string) {
        if (string == null) {
            return new Date();
        }

        Date date;
        try {
            date = dateFormatYYMMDD.parse(string);
        } catch (Exception e) {
            e.printStackTrace();
            date = new Date();
        }
        return date;
    }

    public static Date dateFromStringHHMM(String string) {
        if (string == null) {
            return new Date();
        }

        Date date;
        try {
            date = dateFormatHHMM.parse(string);
        } catch (Exception e) {
            e.printStackTrace();
            date = new Date();
        }
        return date;
    }

    public static Date dateFromYYYYHHMMSS(String string) {
        if (string == null) {
            return new Date();
        }

        Date date;
        try {
            date = dateFormat4.parse(string);
        } catch (Exception e) {
            e.printStackTrace();
            date = new Date();
        }
        return date;
    }

    public static Date dateFromString4(String string) {
        if (string == null) {
            return new Date();
        }

        Date date;
        try {
            date = dateFormat4.parse(string);
        } catch (Exception e) {
            e.printStackTrace();
            date = new Date();
        }
        return date;
    }

    public static Date dateFromString7(String string) {
        if (string == null) {
            return new Date();
        }

        Date date;
        try {
            date = dateFormat7.parse(string);
        } catch (Exception e) {
            e.printStackTrace();
            date = new Date();
        }
        return date;
    }


    public static Date dateFromString8(String string) {
        if (string == null) {
            return new Date();
        }

        Date date;
        try {
            date = dateFormat8.parse(string);
        } catch (Exception e) {
            e.printStackTrace();
            date = new Date();
        }
        return date;
    }

    public static boolean isPassBookingTime(Date bookingDate, long advance) {
        long bookingDateTime = bookingDate.getTime();
        long processTime = dateFromString4(getProcessTime()).getTime();
        return (bookingDateTime - advance) > processTime;
    }


    public static String convertDuration(int duration) {

        String text;
        if (duration < 0) {
            int h = 23 + duration / 60;
            text = h + "时";
            text += 60 + duration % 60 + "分";
        } else {
            int h = duration / 60;
            text = h + "时";
            text += duration % 60 + "分";
        }
        return text;
    }

    //为0不显示
    public static String zeroGoneConvertDuration(int duration) {

        String text;
        if (duration < 0) {
            int h = 23 + duration / 60;
            int m = 60 + duration % 60;
            if (duration / 60 == 0){
                text = "";
            }else {
                text = h + "小时";
            }
            if (duration % 60 != 0){
                text += 60 + duration % 60 + "分";
            }
        } else {
            int h = duration / 60;
            int m = duration % 60;
            if (h == 0){
                text = "";
            }else {
                text = h + "小时";
            }
            if (m != 0){
                text += duration % 60 + "分";
            }
        }
        return text;
    }


    /**
     * 格式化时间   天小时分钟
     * @return
     */
    public static String formatRemainingTime(long remainingTime) {
        remainingTime   =   remainingTime/60000;
        StringBuilder buffer = new StringBuilder();
        //大于1天
        if (remainingTime >= 24 * 60) {
            long days = remainingTime / (24 * 60);
            long hours = (remainingTime % (24 * 60)) / 60;
            buffer.append(days);
            buffer.append("天");
            if (hours != 0) {
                buffer.append(hours);
                buffer.append("小时");
            }
            return buffer.toString();
        }
        //大于1小时
        if (remainingTime >= 60) {
            long hours = remainingTime / 60;
            long minutes = remainingTime % 60;
            buffer.append(hours);
            buffer.append("小时");
            if (minutes != 0) {
                buffer.append(minutes);
                buffer.append("分钟");
            }
            return buffer.toString();
        }
        buffer.append(remainingTime);
        buffer.append("分钟");
        return buffer.toString();
    }

     //是否超过限制的时间
    public static boolean isBehindTimeLimit(){
        if(System.currentTimeMillis()> 1487606400000L){
             return  true;
        }
        return false;
    }

    public static String[] weekName = { "周日", "周一", "周二", "周三", "周四", "周五","周六" };

    public static int getMonthDays(int year, int month) {
        if (month > 12) {
            month = 1;
            year += 1;
        } else if (month < 1) {
            month = 12;
            year -= 1;
        }
        int[] arr = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
        int days = 0;

        if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
            arr[1] = 29; // 闰年2月29天
        }

        try {
            days = arr[month - 1];
        } catch (Exception e) {
            e.getStackTrace();
        }

        return days;
    }


    public static int getCurrentMonthDay() {
        return Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
    }

    public static int getWeekDay() {
        return Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
    }

    public static int getHour() {
        return Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
    }
    public static int getMinute() {
        return Calendar.getInstance().get(Calendar.MINUTE);
    }
    public static CustomDate getNextSunday() {

        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, 7 - getWeekDay()+1);
        CustomDate date = new CustomDate(c.get(Calendar.YEAR),
                c.get(Calendar.MONTH)+1, c.get(Calendar.DAY_OF_MONTH));
        return date;
    }

    public static int[] getWeekSunday(int year, int month, int day, int pervious) {
        int[] time = new int[3];
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, day);
        c.add(Calendar.DAY_OF_MONTH, pervious);
        time[0] = c.get(Calendar.YEAR);
        time[1] = c.get(Calendar.MONTH )+1;
        time[2] = c.get(Calendar.DAY_OF_MONTH);
        return time;

    }

    public static int getWeekDayFromDate(int year, int month) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getDateFromString(year, month));
        int week_index = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (week_index < 0) {
            week_index = 0;
        }
        return week_index;
    }

    @SuppressLint("SimpleDateFormat")
    public static Date getDateFromString(int year, int month) {
        String dateString = year + "-" + (month > 9 ? month : ("0" + month))
                + "-01";
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            date = sdf.parse(dateString);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
        }
        return date;
    }
    public static boolean isToday(CustomDate date){
        return(date.year == DateUtils.getYear() &&
                date.month == DateUtils.getMonth()
                && date.day == DateUtils.getCurrentMonthDay());
    }

    public static boolean isCurrentMonth(CustomDate date){
        return(date.year == DateUtils.getYear() &&
                date.month == DateUtils.getMonth());
    }

}
