package com.tere.utils.date;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.time.DateUtils;

public class MonthlyDateOccurrenceIterator extends DateOccurrenceIterator
{
	private int dayOfMonth;
	
	public MonthlyDateOccurrenceIterator(Date startDate, int occurrences,
			int monthInterval, int dayOfMonth)
	{
		super(startDate, occurrences, monthInterval, Calendar.MONTH);
		this.dayOfMonth = dayOfMonth;
	}

	@Override
	protected Date calcNextDate()
	{
		Date nextDate = super.calcNextDate();
		return DateUtils.setDays(nextDate, dayOfMonth);
	}

}
