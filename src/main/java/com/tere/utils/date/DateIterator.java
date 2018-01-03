package com.tere.utils.date;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

public abstract class DateIterator implements Iterator<Date>, Iterable<Date>
{
	private Calendar currentDate;
	private Calendar startDate;
	private Date preNextDate = null;
	private boolean preNext = false;
	
	public DateIterator(Date startDate)
	{
		currentDate = Calendar.getInstance();
		currentDate.setTime(startDate);
		this.startDate = Calendar.getInstance();
		this.startDate.setTime(startDate);
	}
	
	protected Calendar getCurrentDate()
	{
		return currentDate;
	}

	protected void setCurrentDate(Calendar date)
	{
		currentDate = date;
	}

	protected Calendar getStartDate()
	{
		return startDate;
	}

	protected Date getPreNextDate()
	{
		return preNextDate;
	}

	protected void setPreNextDate(Date date)
	{
		preNextDate = date;
	}

	protected void setStartDate(Calendar date)
	{
		startDate = date;
	}
	
	protected abstract Date calcNextDate();

	protected abstract boolean checkNextDate();

	@Override
	public Iterator<Date> iterator()
	{
		return this;
	}
	
	@Override
	public Date next()
	{
		if (!preNext)
		{
			hasNext();
		}
		preNext = false;
		currentDate = Calendar.getInstance();
		currentDate.setTime(preNextDate);
		return preNextDate;
	}

	@Override
	public boolean hasNext()
	{
		preNextDate = calcNextDate();
		preNext = true;
		return checkNextDate();
	}
	@Override
	public void remove()
	{
		// TODO Auto-generated method stub

	}

	
	
}
