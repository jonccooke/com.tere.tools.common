package com.tere.utils.date;

import java.util.Calendar;
import java.util.Date;

public class DateEndDateIterator extends DateIterator
{
	private Date endDate;
	int interval;
	int curOccurrence;
	int intervalType;

	protected DateEndDateIterator(Date startDate, Date endDate,
			int interval, int intervalType)
	{
		super(startDate);
		this.endDate = endDate;
		this.interval = interval;
		this.intervalType = intervalType;
		curOccurrence = 0;
	}

	@Override
	protected boolean checkNextDate()
	{
		Date date = getPreNextDate();
		return endDate.after(date);
	}

	@Override
	protected Date calcNextDate()
	{
		Calendar calendar = Calendar.getInstance();
		if (null == getPreNextDate())
		{
			calendar.setTime(getCurrentDate().getTime());
		}
		else
		{
			calendar.setTime(getPreNextDate());
		}
		calendar.add(intervalType, interval);
		return calendar.getTime();
	}

	protected Date getEndDate()
	{
		return endDate;
	}

	@Override
	public Date next()
	{
		Date ret = super.next();
		curOccurrence++;
		return ret;
	}

}
