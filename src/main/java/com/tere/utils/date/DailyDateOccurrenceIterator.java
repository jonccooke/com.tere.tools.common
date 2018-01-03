package com.tere.utils.date;

import java.util.Calendar;
import java.util.Date;

public class DailyDateOccurrenceIterator extends DateOccurrenceIterator
{
	boolean weekDays;
	
	protected DailyDateOccurrenceIterator(Date startDate, int occurrences, int dailyInterval, boolean weekDays)
	{
		super(startDate, occurrences, dailyInterval, Calendar.DAY_OF_WEEK);
		this.weekDays = weekDays;
	}


	@Override
	protected boolean checkNextDate()
	{
		boolean ret = super.checkNextDate();
		
		Date preNextDate = getPreNextDate();
		if (weekDays && ret)
		{
			if (weekDays)
			{
				if (DateUtils.isWeekDay(preNextDate))
				{
					ret = true;
				}
				else
				{
					if (getCurOccurrence() == getOccurrences())
					{
						ret = false;
					}
				}
			}
		}
		return ret;
	}

}
