package com.tere.utils.date;

import java.util.Calendar;
import java.util.Date;

public class DateOccurrenceIterator extends DateIterator
{
	private int occurrences;
	int interval;
	int curOccurrence;
	int intervalType;

	protected DateOccurrenceIterator(Date startDate, int occurrences,
			int interval, int intervalType)
	{
		super(startDate);
		this.occurrences = occurrences;
		this.interval = interval;
		this.intervalType = intervalType;
		curOccurrence = 0;
	}

	@Override
	protected boolean checkNextDate()
	{
		boolean ret = true;
		if (curOccurrence < occurrences)
		{
			ret = true;
		}
		else
		{
			ret = false;
		}
		return ret;
	}

	@Override
	protected Date calcNextDate()
	{
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(getCurrentDate().getTime());
		calendar.add(intervalType, interval);
		return calendar.getTime();
	}

	protected Object getOccurrences()
	{
		return occurrences;
	}

	protected Object getCurOccurrence()
	{
		return curOccurrence;
	}

	@Override
	public Date next()
	{
		Date ret = super.next();
		curOccurrence++;
		return ret;
	}

}
