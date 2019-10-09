package com.tere;


public interface Adapter<A, T>
{
	public T adapt(A valueObject) throws TereException;
	
}
