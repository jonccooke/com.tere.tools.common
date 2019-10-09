package com.tere.builder;

import com.tere.TereException;
import com.tere.utils.io.json.FromJSON;

public interface Builder<C, E extends TereException> extends FromJSON<Builder<C, E>>
{
	
	public C build() throws E, BuilderException;
	
}
