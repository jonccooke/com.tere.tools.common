package com.tere.utils.io;

import java.io.OutputStream;
import java.util.List;

import com.tere.TereException;

public interface TBatchWriter<T> extends TWriter<T>
{
	public void writeList(List<T> t, OutputStream outputStream, int offset, int batchSize) throws TereException;
	public void writeArray(T[] t, OutputStream outputStream, int offset, int batchSize) throws TereException;
}
