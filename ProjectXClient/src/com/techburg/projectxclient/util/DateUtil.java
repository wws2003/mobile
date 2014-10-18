package com.techburg.projectxclient.util;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

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
}
