package com.tere.utils;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.tere.utils.StringUtils;

public class StringUtilsTest
{

	interface TestClass1
	{
		public String getString();
	}

	@Test
	public void test1()
	{
//		StringMatcher<TestClass1> matcher = new StringMatcher<TestClass1>();
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

		int result = StringUtils.match("a*:b*:c*", collection, (String patternRegex, TestClass1 matchingObject) ->
			{
				boolean r = matchingObject.getString().matches(patternRegex);
				return r;
			});
		Assert.assertEquals(1, result);
	}

	@Test
	public void stringRemoveIfPresentTestEOL()
	{
		Assert.assertEquals("test", StringUtils.removeIfPressent("testEnd", "End", StringUtils.EOL, false));
	}

	@Test
	public void stringRemoveIfPresentTestEOLIgnoreCase()
	{
		Assert.assertEquals("test", StringUtils.removeIfPressent("testEnd", "eND", StringUtils.EOL, true));
	}


	@Test
	public void stringRemoveIfPresentTestSOL()
	{
		Assert.assertEquals("End", StringUtils.removeIfPressent("testEnd", "test", StringUtils.SOL, false));
	}

	@Test
	public void stringRemoveIfPresentTestSOLIgnoreCase()
	{
		Assert.assertEquals("End", StringUtils.removeIfPressent("testEnd", "TeSt", StringUtils.SOL, true));
	}

	@Test
	public void stringRemoveIfPresentTestPos()
	{
		Assert.assertEquals("tesd", StringUtils.removeIfPressent("testEnd", "tEn", 3, false));
	}

	@Test
	public void stringRemoveIfPresentTestPosIgnoreCase()
	{
		Assert.assertEquals("tesd", StringUtils.removeIfPressent("testEnd", "TeN", 3, false));
	}

}
