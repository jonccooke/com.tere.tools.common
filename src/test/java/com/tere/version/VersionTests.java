package com.tere.version;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

import junit.framework.Assert;

public class VersionTests
{

	@Test
	public void testRegex() throws VersionException
	{

		String input = "java version 1.8";
		Pattern pattern = Pattern.compile("(\\d+)([.]\\d+)?([.]\\d+)");
		Matcher matcher = pattern.matcher(input);
		String v = "";
		String major = "";
		String minor = "";
		String subMinor = "";
		if (matcher.find())
		{
			int no = matcher.groupCount();
			System.out.println(no);
			v = matcher.group(0); // 1.7
			major = matcher.group(1); // 1.7
			minor = matcher.group(2); // 17
			subMinor = matcher.group(3); // 17
		}
		System.out.println(major);
		System.out.println(minor);
		System.out.println(subMinor);
	}

	@Test
	public void testVersionFromString() throws VersionException
	{
		Assert.assertTrue(Version.of("1.0.0").equals(Version.of(1, 0, 0)));
	}
}
