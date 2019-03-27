package com.tere.builder;

import com.tere.TereException;

public interface Builder<C, E extends TereException>
{
	
	public C build() throws E, BuilderException;
	
}
