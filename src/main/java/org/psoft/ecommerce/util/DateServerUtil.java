package org.psoft.ecommerce.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateServerUtil {

	public static GregorianCalendar currentDate() {
		return new GregorianCalendar();
	}

	public static int currentMonth() {
		return DateServerUtil.currentDate().get(Calendar.MONTH);
	}

	public static int currentYear() {
		return DateServerUtil.currentDate().get(Calendar.YEAR);
	}
	
	public static String format(Date date, String pattern){
		SimpleDateFormat formater = new SimpleDateFormat(pattern);
		return formater.format(date);
	}
}
