package com.tere.utils.date;

import java.util.Date;

public class DateIteratorFactory
{
	public static DateIterator createDailyDateIterator(Date startDate,
			int occurrences, int dailyInterval, boolean weekDays)
	{
		return new DailyDateOccurrenceIterator(startDate, occurrences, dailyInterval, weekDays);
	}

	public static DateIterator createDailyDateIterator(Date startDate,
			Date endDate, int dailyInterval, boolean weekDays)
	{
		return new DailyDateEndDateIterator(startDate, endDate, dailyInterval, weekDays);
	}

	public static DateIterator createMonthlyDateIterator(Date startDate,
			int occurrences, int monthlyInterval, int dayOfMonth)
	{
		return new MonthlyDateOccurrenceIterator(startDate, occurrences, monthlyInterval, dayOfMonth);
	}

	public static DateIterator createMonthlyDateIterator(Date startDate,
			Date endDate, int monthlyInterval, int dayOfMonth)
	{
		return new MonthlyDateEndDateIterator(startDate, endDate, monthlyInterval, dayOfMonth);
	}

	
}
