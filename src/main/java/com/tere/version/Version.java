package com.tere.version;

import java.util.regex.Pattern;

import com.tere.TereException;
import com.tere.builder.Builder;

public class Version implements Comparable<Version>
{
	/*
	 * Major - range 0 - 255, minor rang 0 - 255, sub minor 0 - 65565
	 */
	private long versionLong;
	private static Pattern splitPattern = Pattern.compile("(?:(\\d+\\.(?:\\d+\\.)*\\d+))");
	private static Pattern versionPattern = Pattern.compile("(?!\\.)(\\d+(\\.\\d+)+)$");

	public Version(long versionNumber) {
		this.versionLong = versionNumber;
	}

//	@Override
	public byte getMajor()
	{
		return (byte) (versionLong >> 24);
	}

	public byte getMinor()
	{
		return (byte) ((versionLong >> 16) & 0xff);
	}

//	@Override
	public short getSubMinor()
	{
		return (short) (versionLong & 0xffff);
	}


	public static Version of(String versionString) throws VersionException
	{
		String[] parts = splitPattern.split(versionString);
		return of(Byte.parseByte(parts[0]), parts.length >= 2 ? Byte.parseByte(parts[1]) : 0,
				parts.length >= 3 ? Byte.parseByte(parts[2]) : 0);
	}

	public static Version of(int major, int minor, int subMinor) throws VersionException
	{
		long n= ((major & 0xff) << 24) | ((0xff & minor) << 16) | (0xffff & subMinor);
		return new Version( n);
	}

	@Override
	public int compareTo(Version o)
	{
		if (getMajor() < o.getMajor())
		{
			return -1;
		}
		if (getMajor() > o.getMajor())
		{
			return 1;
		}
		if (getMinor() < o.getMinor())
		{
			return -1;
		}
		if (getMinor() > o.getMinor())
		{
			return 1;
		}
		if (getSubMinor() < o.getSubMinor())
		{
			return -1;
		}
		if (getSubMinor() > o.getSubMinor())
		{
			return 1;
		}
		return 0;
	}

	@Override
	public String toString()
	{
		return String.format("%d.%d.%d",versionLong>> 24, 0xff & (versionLong >> 16),  versionLong & 0xffff);
	}

}
