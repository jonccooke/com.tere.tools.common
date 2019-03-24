package com.tere.version;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.tere.TereException;
import com.tere.builder.Builder;

public class Version implements Comparable<Version>
{
	/*
	 * Major - range 0 - 255, minor rang 0 - 255, sub minor 0 - 65565
	 */
	private long versionLong;
	private static Pattern splitPattern = Pattern.compile("(\\d+)([.]\\d+)?([.]\\d+)");;
	private static Pattern versionPattern = Pattern.compile("(?!\\.)(\\d+(\\.\\d+)+)$");

	public Version(long versionNumber)
	{
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
		Matcher matcher = splitPattern.matcher(versionString);
		int major = -1;
		int minor = -1;
		int subMinor = 1;
		if (matcher.find())
		{
				int grpNo = matcher.groupCount();
				subMinor = Integer.parseInt(matcher.group(3).substring(1));
				minor = Integer.parseInt(matcher.group(2) == null ? matcher.group(3).substring(1) : matcher.group(2).substring(1));
				major = Integer.parseInt(matcher.group(1));
				return of(major, minor, subMinor);
		}
		throw new VersionException("Invalid version string '" + versionString + "' should be <major> . <minor> . <sub minor>");
////		matcher.g
//		String[] parts = splitPattern.split(versionString);
//		return of(Byte.parseByte(parts[0]), parts.length >= 2 ? Byte.parseByte(parts[1]) : 0,
//				parts.length >= 3 ? Byte.parseByte(parts[2]) : 0);
	}

	public static Version of(int major, int minor, int subMinor) throws VersionException
	{
		long n = ((major & 0xff) << 24) | ((0xff & minor) << 16) | (0xffff & subMinor);
		return new Version(n);
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (versionLong ^ (versionLong >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Version other = (Version) obj;
		if (versionLong != other.versionLong)
			return false;
		return true;
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
		return String.format("%d.%d.%d", versionLong >> 24, 0xff & (versionLong >> 16), versionLong & 0xffff);
	}

}
