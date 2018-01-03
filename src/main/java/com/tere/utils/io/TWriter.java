package com.tere.utils.io;

import java.io.OutputStream;
import java.util.Collection;

import com.tere.TereException;

public interface TWriter<T>
{
    public Object create(T value) throws TereException;
	public void write(T t, OutputStream outputStream) throws TereException;
	public void write(Collection<T> t, OutputStream outputStream) throws TereException;
}
