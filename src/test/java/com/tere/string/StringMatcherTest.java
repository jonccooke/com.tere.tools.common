package com.tere.string;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class StringMatcherTest
{

	interface TestClass1
	{
		public String getString();
	}

	@Test
	public void test1()
	{
		StringMatcher<TestClass1> matcher = new StringMatcher<TestClass1>();
		List<TestClass1> collection = new ArrayList<TestClass1>();

		collection.add(new TestClass1()
		{

			@Override
			public String getString()
			{
				return "anvil:binary:create";
			}
		});

		collection.add(new TestClass1()
		{

			@Override
			public String getString()
			{
				return "ammer:zbent:create";
			}
		});

		collection.add(new TestClass1()
		{

			@Override
			public String getString()
			{
				return "pes:tff:oip";
			}
		});

		int result = matcher.match("a*:b*:c*", collection, (String patternRegex, TestClass1 matchingObject) ->
			{
				boolean r = matchingObject.getString().matches(patternRegex);
				return r;
			});
		Assert.assertEquals(1, result);
	}

}
