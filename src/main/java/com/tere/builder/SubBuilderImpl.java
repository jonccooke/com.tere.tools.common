package com.tere.builder;

import com.tere.TereException;

public abstract class SubBuilderImpl<C, P, B extends Builder<P, E>, E extends TereException> extends BuilderImpl<C, E> implements SubBuilder<C, P, B, E>
{

	private B parentBuilder;
	private P parentValue;
	
	protected SubBuilderImpl(B parentBuilder, P parentValue) throws E
	{
		super();
		this.parentBuilder = parentBuilder;
		this.parentValue = parentValue;
	}

	protected B getParentBuilder()
	{
		return parentBuilder;
	}
	
	protected P getParentValue()
	{
		return parentValue;
	}
	
	public B end() throws BuilderException
	{
//		check();

		return parentBuilder;
	}

}
