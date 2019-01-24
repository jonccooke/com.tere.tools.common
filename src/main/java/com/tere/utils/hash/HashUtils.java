package com.tere.utils.hash;

import static java.lang.Long.rotateLeft;

import java.math.BigInteger;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;

public class HashUtils
{

//	static final int PR1 = -1640531535;
//	static final int PR2 = -2048144777;
//	static final int PR3 = -1028477379;
//	static final int PR4 = 668265263;
//	static final int PR5 = 374761393;
//
//	static final long PR64_1 = -7046029288634856825L; // 11400714785074694791
//	static final long PR64_2 = -4417276706812531889L; // 14029467366897019727
//	static final long PR64_3 = 1609587929392839161L;
//	static final long PR64_4 = -8796714831421723037L; // 9650029242287828579
//	static final long PR64_5 = 2870177450012600261L;
//
//	public static long hash(byte[] inBuffer, int offset, int length, long sd)
//	{
//
//		final int end = offset + length;
//		long hashCode;
//
//		if (length >= 32)
//		{
//			final int limit = end - 32;
//			long version1 = sd + PR64_1 + PR64_2;
//			long version2 = sd + PR64_2;
//			long version3 = sd + 0;
//			long version4 = sd - PR64_1;
//			do
//			{
//				version1 += inBuffer[offset] * PR64_2;
//				version1 = rotateLeft(version1, 31);
//				version1 *= PR64_1;
//				offset += 8;
//
//				version2 += inBuffer[offset] * PR64_2;
//				version2 = rotateLeft(version2, 31);
//				version2 *= PR64_1;
//				offset += 8;
//
//				version3 += inBuffer[offset] * PR64_2;
//				version3 = rotateLeft(version3, 31);
//				version3 *= PR64_1;
//				offset += 8;
//
//				version4 += inBuffer[offset] * PR64_2;
//				version4 = rotateLeft(version4, 31);
//				version4 *= PR64_1;
//				offset += 8;
//			}
//			while (offset <= limit);
//
//			hashCode = rotateLeft(version1, 1) + rotateLeft(version2, 7)
//					+ rotateLeft(version3, 12) + rotateLeft(version4, 18);
//
//			version1 *= PR64_2;
//			version1 = rotateLeft(version1, 31);
//			version1 *= PR64_1;
//			hashCode ^= version1;
//			hashCode = hashCode * PR64_1 + PR64_4;
//
//			version2 *= PR64_2;
//			version2 = rotateLeft(version2, 31);
//			version2 *= PR64_1;
//			hashCode ^= version2;
//			hashCode = hashCode * PR64_1 + PR64_4;
//
//			version3 *= PR64_2;
//			version3 = rotateLeft(version3, 31);
//			version3 *= PR64_1;
//			hashCode ^= version3;
//			hashCode = hashCode * PR64_1 + PR64_4;
//
//			version4 *= PR64_2;
//			version4 = rotateLeft(version4, 31);
//			version4 *= PR64_1;
//			hashCode ^= version4;
//			hashCode = hashCode * PR64_1 + PR64_4;
//		}
//		else
//		{
//			hashCode = sd + PR64_5;
//		}
//
//		hashCode += length;
//
//		while (offset <= end - 8)
//		{
//			long k1 = inBuffer[offset];
//			k1 *= PR64_2;
//			k1 = rotateLeft(k1, 31);
//			k1 *= PR64_1;
//			hashCode ^= k1;
//			hashCode = rotateLeft(hashCode, 27) * PR64_1 + PR64_4;
//			offset += 8;
//		}
//
//		if (offset <= end - 4)
//		{
//			hashCode ^= ((int) inBuffer[offset] & 0xFFFFFFFFL) * PR64_1;
//			hashCode = rotateLeft(hashCode, 23) * PR64_2 + PR64_3;
//			offset += 4;
//		}
//
//		while (offset < end)
//		{
//			hashCode ^= ((byte) inBuffer[offset] & 0xFF) * PR64_5;
//			hashCode = rotateLeft(hashCode, 11) * PR64_1;
//			++offset;
//		}
//
//		hashCode ^= hashCode >>> 33;
//		hashCode *= PR64_2;
//		hashCode ^= hashCode >>> 29;
//		hashCode *= PR64_3;
//		hashCode ^= hashCode >>> 32;
//
//		return hashCode;
//	}
////
////	public static int hashCode(int len, byte[]... values)
////	{
////		int result = 1;
////		for (int pos = 0; pos < len; pos++)
////		{
////			if (values[pos] == null)
////				continue;
////
////			for (int valuePos = 0; valuePos < len; valuePos++)
////			{
////				result = 31 * result + values[pos][valuePos];
////			}
////		}
////		return result;
////	}
////
	public static int hashCode(byte[][] values)
	{
		int result = 1;
		for (byte[] value : values)
		{
			if (value == null)
				continue;

			for (int valuePos = 0; valuePos < value.length; valuePos++)
			{
				result = 31 * result + value[valuePos];
			}
		}
		return result;
	}

	public static int hashCode(byte[] value)
	{
		int result = 1;
		if (value == null)
			return result;

		for (int pos = 0; pos < value.length; pos++)
			result = 31 * result + value[pos];
		return result;
	}

	public static int hashCode(int value)
	{
		return Integer.hashCode(value);
	}

	public static int hashCode(float value)
	{
		return Float.hashCode(value);
	}

	public static int hashCode(long value)
	{
		return Long.hashCode(value);
	}

	public static int hashCode(double value)
	{
		return Double.hashCode(value);
	}

	public static int hashCode(short value)
	{
		return Short.hashCode(value);
	}

	public static int hashCode(byte value)
	{
		return Byte.hashCode(value);
	}

	public static int hashCode(Date value)
	{
		return value.hashCode();
	}

	public static int hashCode(Time value)
	{
		return value.hashCode();
	}

	public static int hashCode(Timestamp value)
	{
		return value.hashCode();
	}

	public static int hashCode(BigInteger value)
	{
		return value.hashCode();
	}

	public static int hashCode(int a[])
	{
		if (a == null)
			return 0;

		int result = 1;
		for (int element : a)
			result = 31 * result + element;

		return result;
	}

	public static int hashCode(Object... object)
	{
		if (object == null)
			return 0;

		int result = 1;
		for (Object element : object)
			result = 31 * result + element.hashCode();

		return result;
	}
}
