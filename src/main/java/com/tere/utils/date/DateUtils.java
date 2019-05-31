package com.tere.utils.date;

import java.text.DateFormatSymbols;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import com.tere.logging.LogManager;
import com.tere.logging.Logger;

public class DateUtils
{
	private static Logger log = LogManager.getLogger(DateUtils.class);
	private static String[] months;
	private static String[] weekdays;

	public static DateTimeFormatter ddMMyyyy = DateTimeFormatter
			.ofPattern("dd/MM/yyyy");
	public static DateTimeFormatter yyyyMMdd = DateTimeFormatter
			.ofPattern("yyyy/MM/dd");
	public static DateTimeFormatter HHmmss = DateTimeFormatter.ofPattern("HH:mm:ss");

	public static java.sql.Date convert(Date fromDate)

	{
		if (null == fromDate)
		{
			return null;
		}
		return new java.sql.Date(fromDate.getTime());
	}

	public static Date getTimeOnly(Date date)

	{
		if (null == date)
		{
			return null;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 0);  
		cal.set(Calendar.MINUTE, 0);  
		cal.set(Calendar.SECOND, 0);  
		cal.set(Calendar.MILLISECOND, 0);  
		return cal.getTime();
	}


	public static Date convert(java.sql.Date fromDate)
	{
		if (null == fromDate)
		{
			return null;
		}
		return new Date(fromDate.getTime());
	}

	public static Date create(int day, int month, int year)
	{
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(year, month - 1, day);
		return calendar.getTime();
	}

	public static Date create(int hour, int min, int sec, int millisec)
	{
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(Calendar.HOUR_OF_DAY, hour);  
		calendar.set(Calendar.MINUTE, min);  
		calendar.set(Calendar.SECOND, sec);  
		calendar.set(Calendar.MILLISECOND, millisec);  
		return calendar.getTime();
	}


	public static int get(Date date, int field)
	{
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(field);
	}

	public static int get(long dateVal, int field)
	{
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date(dateVal));
		return calendar.get(field);
	}


	public static Date add(Date srcDate, Date dateToAdd)
	{
		long timeInMs = srcDate.getTime() + dateToAdd.getTime();
		return new Date(timeInMs);
	}

	public static Date subtract(Date srcDate, Date dateToSubstract)
	{
		long timeInMs = srcDate.getTime() - dateToSubstract.getTime();
		return new Date(timeInMs);
	}

	public static final int getMonthsDifference(Date start, Date end)
	{
		Calendar startCalendar = Calendar.getInstance();
		Calendar endCalendar = Calendar.getInstance();
		startCalendar.setTime(start);
		endCalendar.setTime(end);
		int m1 = startCalendar.get(Calendar.YEAR) * 12
				+ startCalendar.get(Calendar.MONTH);
		int m2 = endCalendar.get(Calendar.YEAR) * 12
				+ endCalendar.get(Calendar.MONTH);
		return m2 - m1;
	}

	public static final int getDaysDifference(Date start, Date end)
	{
		Calendar startCalendar = Calendar.getInstance();
		Calendar endCalendar = Calendar.getInstance();
		startCalendar.setTime(start);
		endCalendar.setTime(end);
		int m1 = startCalendar.get(Calendar.YEAR) * 356
				+ startCalendar.get(Calendar.MONTH) * 12 + startCalendar.get(Calendar.DAY_OF_MONTH);
		int m2 = endCalendar.get(Calendar.YEAR) * 356
				+ endCalendar.get(Calendar.MONTH) * 12 + + endCalendar.get(Calendar.DAY_OF_MONTH);
		return m2 - m1;
	}

	public static boolean afterOrEqualMonth(Date srcDate, Date compareDate)
	{
		Calendar srcCalendar = Calendar.getInstance();
		Calendar compCalendar = Calendar.getInstance();
		srcCalendar.setTime(srcDate);
		compCalendar.setTime(compareDate);

		if (srcCalendar.after(compCalendar))
		{
			return true;
		}
		if ((srcCalendar.get(Calendar.YEAR) == compCalendar.get(Calendar.YEAR))
				&& (srcCalendar.get(Calendar.MONTH) == compCalendar
						.get(Calendar.MONTH)))
		{
			return true;
		}
		return false;
	}

	public static boolean beforeOrEqualMonth(Date srcDate, Date compareDate)
	{
		Calendar srcCalendar = Calendar.getInstance();
		Calendar compCalendar = Calendar.getInstance();
		srcCalendar.setTime(srcDate);
		compCalendar.setTime(compareDate);

		if (srcCalendar.before(compCalendar))
		{
			return true;
		}
		if ((srcCalendar.get(Calendar.YEAR) == compCalendar.get(Calendar.YEAR))
				&& (srcCalendar.get(Calendar.MONTH) == compCalendar
						.get(Calendar.MONTH)))
		{
			return true;
		}
		return false;
	}

	public static boolean isWeekDay(Date currentDate)
	{
		Calendar cal = Calendar.getInstance();
		cal.setTime(currentDate);
		return isWeekDay(cal);
	}

	public static boolean isWeekDay(Calendar currentDate)
	{
		int dayOfWeek = currentDate.get(Calendar.DAY_OF_WEEK);
		switch (dayOfWeek)
		{
		case Calendar.SATURDAY:
		case Calendar.SUNDAY:
			return false;
		}
		return true;
	}

	public static List<Date> createDailyForwardDates(Date currentDate,
			int occurrences, int dailyInterval, boolean weekDays)
	{
		List<Date> dateList = new Vector<Date>();
		Calendar cal = Calendar.getInstance();
		cal.setTime(currentDate);
		for (int curDateNo = 0; curDateNo < occurrences; curDateNo++)
		{
			cal.add(Calendar.DAY_OF_WEEK, dailyInterval);
			if (weekDays)
			{
				if (isWeekDay(cal))
				{
					dateList.add(cal.getTime());
				}
			}
			else
			{
				dateList.add(cal.getTime());
			}
		}
		return dateList;
	}

	public static List<Date> createWeeklyForwardDates(Date currentDate,
			int occurrences, int reccurence, int[] days)
	{
		List<Date> dateList = new Vector<Date>();
		Calendar cal = Calendar.getInstance();
		cal.setTime(currentDate);
		for (int curDateNo = 0; curDateNo < occurrences; curDateNo++)
		{
			cal.add(Calendar.WEEK_OF_YEAR, reccurence);

			for (int daysPos = 0; daysPos < days.length; daysPos++)
			{
				Calendar dayCal = (Calendar) cal.clone();
				dayCal.set(Calendar.DAY_OF_WEEK, days[daysPos]);
				Date newDate = dayCal.getTime();
				dateList.add(newDate);
			}
		}

		return dateList;
	}

	public static List<Date> createMonthlyForwardDates(Date currentDate,
			int occurrences, int dayOfMonth)
	{
		List<Date> dateList = new Vector<Date>();
		Calendar cal = Calendar.getInstance();
		cal.setTime(currentDate);
		for (int curDateNo = 0; curDateNo < occurrences; curDateNo++)
		{
			cal.add(Calendar.MONTH, 1);
			cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
			dateList.add(cal.getTime());
		}
		return dateList;
	}

	public static List<Date> createMonthlyForwardDates(Date currentDate,
			int occurrences, int dayPosition, int dayOfWeek)
	{
		List<Date> dateList = new Vector<Date>();
		Calendar cal = Calendar.getInstance();
		for (int curDateNo = 0; curDateNo < occurrences; curDateNo++)
		{
			cal.setTime(currentDate);
			cal.add(Calendar.MONTH, curDateNo + 1);
			cal.add(Calendar.WEEK_OF_MONTH, dayPosition);
			cal.set(Calendar.DAY_OF_WEEK, dayOfWeek);
			dateList.add(cal.getTime());
		}
		return dateList;
	}

	public static List<Date> createMonthlyForwardDates(Date currentDate,
			int occurrences, int dayPosition, int[] days)
	{
		List<Date> dateList = new Vector<Date>();
		Calendar cal = Calendar.getInstance();
		for (int curDateNo = 0; curDateNo < occurrences; curDateNo++)
		{
			cal.setTime(currentDate);
			cal.add(Calendar.MONTH, curDateNo + 1);
			cal.add(Calendar.WEEK_OF_MONTH, dayPosition);
			for (int daysPos = 0; daysPos < days.length; daysPos++)
			{
				Calendar dayCal = (Calendar) cal.clone();
				dayCal.set(Calendar.DAY_OF_WEEK, days[daysPos]);
				Date newDate = dayCal.getTime();
				dateList.add(newDate);
			}
		}
		return dateList;
	}

	public static List<Date> createYearlyForwardDates(Date currentDate,
			int occurrences, int dayOfMonth, int monthOfYear)
	{
		List<Date> dateList = new Vector<Date>();
		Calendar cal = Calendar.getInstance();
		for (int curDateNo = 0; curDateNo < occurrences; curDateNo++)
		{
			cal.setTime(currentDate);
			cal.add(Calendar.YEAR, curDateNo + 1);
			// cal.add(Calendar.WEEK_OF_MONTH, dayPosition);
			cal.set(Calendar.MONTH, monthOfYear);
			cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
			dateList.add(cal.getTime());
		}
		return dateList;
	}

	public static List<Date> createYearlyForwardDates(Date currentDate,
			int occurrences, int dayPosition, int dayOfWeek, int month)
	{
		List<Date> dateList = new Vector<Date>();
		Calendar cal = Calendar.getInstance();
		for (int curDateNo = 0; curDateNo < occurrences; curDateNo++)
		{
			cal.setTime(currentDate);
			cal.add(Calendar.YEAR, curDateNo + 1);
			cal.set(Calendar.MONTH, month);
			cal.add(Calendar.WEEK_OF_MONTH, dayPosition);
			cal.set(Calendar.DAY_OF_WEEK, dayOfWeek);
			dateList.add(cal.getTime());
		}
		return dateList;
	}

	public static List<Date> createDailyForwardDates(Date currentDate,
			Date endDate, int dailyInterval, boolean weekDays)
	{
		List<Date> dateList = new Vector<Date>();
		Calendar cal = Calendar.getInstance();
		cal.setTime(currentDate);
		Calendar endCal = Calendar.getInstance();
		endCal.setTime(endDate);

		while (cal.before(endCal))
		{
			cal.add(Calendar.DAY_OF_WEEK, dailyInterval);
			if (weekDays)
			{
				if (isWeekDay(cal))
				{
					dateList.add(cal.getTime());
				}
			}
			else
			{
				dateList.add(cal.getTime());
			}
		}
		return dateList;
	}

	public static List<Date> createWeeklyForwardDates(Date currentDate,
			Date endDate, int reccurence, int[] days)
	{
		List<Date> dateList = new Vector<Date>();
		Calendar cal = Calendar.getInstance();
		cal.setTime(currentDate);
		Calendar endCal = Calendar.getInstance();
		endCal.setTime(endDate);

		while (cal.before(endCal))
		{
			cal.add(Calendar.WEEK_OF_YEAR, reccurence);

			for (int daysPos = 0; daysPos < days.length; daysPos++)
			{
				Calendar dayCal = (Calendar) cal.clone();
				dayCal.set(Calendar.DAY_OF_WEEK, days[daysPos]);
				Date newDate = dayCal.getTime();
				dateList.add(newDate);
			}
		}

		return dateList;
	}

	public static List<Date> createMonthlyForwardDates(Date currentDate,
			Date endDate, int dayOfMonth)
	{
		List<Date> dateList = new Vector<Date>();
		Calendar cal = Calendar.getInstance();
		cal.setTime(currentDate);
		Calendar endCal = Calendar.getInstance();
		endCal.setTime(endDate);

		while (cal.before(endCal))
		{
			cal.add(Calendar.MONTH, 1);
			cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
			dateList.add(cal.getTime());
		}
		return dateList;
	}

	public static List<Date> createMonthlyForwardDates(Date currentDate,
			Date endDate, int dayPosition, int dayOfWeek)
	{
		List<Date> dateList = new Vector<Date>();
		Calendar cal = Calendar.getInstance();
		cal.setTime(currentDate);
		Calendar endCal = Calendar.getInstance();
		endCal.setTime(endDate);
		int curDateNo = 0;
		while (cal.before(endCal))
		{
			cal.setTime(currentDate);
			cal.add(Calendar.MONTH, curDateNo + 1);
			cal.add(Calendar.WEEK_OF_MONTH, dayPosition);
			cal.set(Calendar.DAY_OF_WEEK, dayOfWeek);
			dateList.add(cal.getTime());
			curDateNo++;
		}
		return dateList;
	}

	public static List<Date> createMonthlyForwardDates(Date currentDate,
			Date endDate, int dayPosition, int[] days)
	{
		List<Date> dateList = new Vector<Date>();
		Calendar cal = Calendar.getInstance();
		cal.setTime(currentDate);
		Calendar endCal = Calendar.getInstance();
		endCal.setTime(endDate);
		int curDateNo = 0;

		while (cal.before(endCal))
		{
			cal.setTime(currentDate);
			cal.add(Calendar.MONTH, curDateNo + 1);
			cal.add(Calendar.WEEK_OF_MONTH, dayPosition);
			for (int daysPos = 0; daysPos < days.length; daysPos++)
			{
				Calendar dayCal = (Calendar) cal.clone();
				dayCal.set(Calendar.DAY_OF_WEEK, days[daysPos]);
				Date newDate = dayCal.getTime();
				dateList.add(newDate);
			}
			curDateNo++;
		}
		return dateList;
	}

	public static List<Date> createYearlyForwardDates(Date currentDate,
			Date endDate, int dayOfMonth, int monthOfYear)
	{
		List<Date> dateList = new Vector<Date>();
		Calendar cal = Calendar.getInstance();
		cal.setTime(currentDate);
		Calendar endCal = Calendar.getInstance();
		endCal.setTime(endDate);
		int curDateNo = 0;

		while (cal.before(endCal))
		{
			cal.setTime(currentDate);
			cal.add(Calendar.YEAR, curDateNo + 1);
			// cal.add(Calendar.WEEK_OF_MONTH, dayPosition);
			cal.set(Calendar.MONTH, monthOfYear);
			cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
			dateList.add(cal.getTime());
			curDateNo++;
		}
		return dateList;
	}

	public static List<Date> createYearlyForwardDates(Date currentDate,
			Date endDate, int dayPosition, int dayOfWeek, int month)
	{
		log.debug("Ceating dates...");
		List<Date> dateList = new Vector<Date>();
		Calendar cal = Calendar.getInstance();
		cal.setFirstDayOfWeek(Calendar.MONDAY);
		cal.clear();
		cal.setTime(currentDate);
		Calendar endCal = Calendar.getInstance();
		endCal.setTime(endDate);
		int curDateNo = 0;
		log.debug("Start Date: %1$te, %1$tm %1$tY", cal);
		log.debug("End Date: %1$te, %1$tm %1$tY", endCal);
		while (cal.before(endCal))
		{
			log.debug("Ceating date...");
			cal.setTime(currentDate);
			cal.add(Calendar.YEAR, curDateNo + 1);
			log.debug("Cal: %1$te, %1$tm %1$tY", cal);
			cal.set(Calendar.MONTH, month);
			log.debug("Cal: %1$te, %1$tm %1$tY", cal);
			cal.add(Calendar.WEEK_OF_MONTH, dayPosition);
			log.debug("Cal: %1$te, %1$tm %1$tY", cal);
			cal.set(Calendar.DAY_OF_WEEK, dayOfWeek);
			log.debug("End Cal: %1$te, %1$tm %1$tY", cal);
			if (!cal.before(endCal))
			{
				break;
			}
			dateList.add(cal.getTime());
			curDateNo++;
		}
		return dateList;
	}

	public static String[] getWeekdayNames()
	{
		if (null == weekdays)
		{
			Locale usersLocale = Locale.getDefault();
	
			DateFormatSymbols dfs = new DateFormatSymbols(usersLocale);
			weekdays = dfs.getWeekdays();
		}
		return weekdays;
	}

	public static String[] getMonthNames()
	{
		if (null == months)
		{
			Locale usersLocale = Locale.getDefault();
	
			DateFormatSymbols dfs = new DateFormatSymbols(usersLocale);
			months = dfs.getMonths();
		}
		return months;
	}
	
}
