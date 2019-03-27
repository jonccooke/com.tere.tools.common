package com.tere.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Assert;
import org.junit.Test;

import com.tere.logging.LogManager;
import com.tere.logging.Logger;

public class StringUtilsTest
{
	private static Logger log = LogManager.getLogger(StringUtilsTest.class);

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

	@Test
	public void expand()
	{
		List<String> ports = new ArrayList<String>();
		
		StringUtils.expand(new ArrayList<String>(), (f,l,p) -> p);
		
	}
	
	@Test
	public void regexTest()
	{
		Pattern EXPAND_PATTERN = Pattern.compile("\\{.*?\\}");

		String str = "select role_id, permission_id from {role_permission} where role_id = ? and permission_id = ?";
		
		Matcher  matcher = EXPAND_PATTERN.matcher(str);
		if (matcher.find())
		{
			String found = matcher.group(0);
			log.debug(found);
			
		}
		
		
	}
}
