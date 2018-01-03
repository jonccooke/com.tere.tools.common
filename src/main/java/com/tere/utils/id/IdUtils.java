package com.tere.utils.id;

import java.util.concurrent.atomic.AtomicLong;

public class IdUtils
{

	private static AtomicLong currentId = new AtomicLong(0);;
	
	public static long nextLongId()
	{
		return currentId.incrementAndGet();
	}
}
