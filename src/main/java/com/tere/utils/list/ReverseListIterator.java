package com.tere.utils.list;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ReverseListIterator<T> implements Iterator<T>
{
	private ListIterator<T> listIterator;

	public ReverseListIterator(List<T> wrappedList)
	{
		this.listIterator = wrappedList.listIterator(wrappedList.size());
	}

	public boolean hasNext()
	{
		return listIterator.hasPrevious();
	}

	public T next()
	{
		return listIterator.previous();
	}

	public void remove()
	{
		listIterator.remove();
	}

}
