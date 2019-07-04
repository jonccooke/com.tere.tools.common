package com.tere.builder;

import com.tere.TereException;

public interface SubBuilder<C, P, B extends Builder<P, E>, E extends TereException>
{
	
	public B end() throws E, BuilderException;
	
}
