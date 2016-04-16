package com.tbg.simplestvallet.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by wws2003 on 10/29/15.
 */
public class DateUtil {
    public static String getYMDString(Date date) {
        if(date == null) {
            return "N/I";
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        StringBuilder ymdStringBuilder = new StringBuilder();
        ymdStringBuilder.append(calendar.get(Calendar.YEAR))
                .append("/")
                .append(String.format(Locale.US, "%02d", 1 + calendar.get(Calendar.MONTH)))
                .append("/")
                .append(String.format(Locale.US, "%02d", calendar.get(Calendar.DATE)));
        return ymdStringBuilder.toString();
    }

    public static Date getDateFromString(String date, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        try {
            return dateFormat.parse(date);
        } catch (Exception e) {
            e.printStackTrace();
            return new Date(0);
        }
    }

    public static long getCurrentTime() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTimeInMillis();
    }

}
