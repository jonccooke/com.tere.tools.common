package com.tere.utils.io;

import java.io.InputStream;

import com.tere.TereException;

public interface TBatchReader<T> extends TReader<T>
{
	public T read(InputStream inputStream) throws TereException;
	public long readBatch(InputStream inputStream, T[] array, int batchSize) throws TereException;
	public void readRaw(InputStream inputStream) throws TereException;
}
