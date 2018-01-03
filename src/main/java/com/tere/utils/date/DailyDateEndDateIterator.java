package com.tere.utils.date;

import java.util.Calendar;
import java.util.Date;

public class DailyDateEndDateIterator extends DateEndDateIterator
{
//	private Date endDate;
//	private int dailyInterval;
	private boolean weekDays;

	protected DailyDateEndDateIterator(Date startDate, Date endDate,
			int dailyInterval, boolean weekDays)
	{
		super(startDate, endDate, dailyInterval, Calendar.DAY_OF_WEEK);
		this.weekDays = weekDays;
	}

//	@Override
//	protected boolean checkNextDate()
//	{
//		boolean ret = super.checkNextDate();
//		
//		Date preNextDate = getPreNextDate();
//		if (weekDays && ret)
//		{
//			if (weekDays)
//			{
//				if (DateUtils.isWeekDay(preNextDate))
//				{
//					ret = true;
//				}
//			}
//		}
//		return ret;
//	}
	
	@Override
	public boolean hasNext()
	{
		while (super.hasNext())
		{
			if (weekDays)
			{
				if (DateUtils.isWeekDay(getPreNextDate()))
				{
					return true;
				}
			}
			else
			{
				return true;
			}
		}
		return false;
	}


}