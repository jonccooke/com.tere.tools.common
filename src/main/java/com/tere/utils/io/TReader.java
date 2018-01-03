package com.tere.utils.io;

import java.io.InputStream;
import java.util.Collection;
import java.util.Properties;

import com.tere.TereException;

public interface TReader<T>
{
    public T create(Object readObject) throws TereException;

	public Collection<T> read(InputStream inputStream, Properties properties) throws TereException;

}
