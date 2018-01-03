package com.tere.utils.date;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.time.DateUtils;

public class MonthlyDateEndDateIterator extends DateEndDateIterator
{
	private int dayOfMonth;
	
	public MonthlyDateEndDateIterator(Date startDate, Date endDate,
			int interval, int dayOfMonth)
	{
		super(startDate, endDate, interval, Calendar.MONTH);
		this.dayOfMonth = dayOfMonth;
	}

	@Override
	protected Date calcNextDate()
	{
		Date  date = super.calcNextDate();
		return DateUtils.setDays(date, dayOfMonth);
	}

	
}
