package com.tere.memory;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class Unsafe
{
	private static final sun.misc.Unsafe UNSAFE;
	private static long BYTE_ARRAY_OFFSET;

	static
	{
		sun.misc.Unsafe unsafe;
		try
		{
			Field unsafeField = sun.misc.Unsafe.class
					.getDeclaredField("theUnsafe");
			unsafeField.setAccessible(true);
			unsafe = (sun.misc.Unsafe) unsafeField.get(null);
			BYTE_ARRAY_OFFSET = unsafe.arrayBaseOffset(byte[].class);

		}
		catch (Exception e)
		{
			unsafe = null;
		}

		UNSAFE = unsafe;
	}

	private Unsafe()
	{
	}

	public static sun.misc.Unsafe getUnsafe()
	{
		return UNSAFE;
	}

	public static long sizeOf(Class<?> clazz)
	{
		long maximumOffset = 0;
		do
		{
			for (Field f : clazz.getDeclaredFields())
			{
				if (!Modifier.isStatic(f.getModifiers()))
				{
					maximumOffset = Math.max(maximumOffset,
							UNSAFE.objectFieldOffset(f));
				}
			}
		}
		while ((clazz = clazz.getSuperclass()) != null);
		return maximumOffset + 8;
	}

	public static void copy(byte[] src, int srcPos, byte[] dest, int destPos,
			int length)
	{
		assert srcPos >= 0 : srcPos;
		assert srcPos <= src.length - length : srcPos;
		assert destPos >= 0 : destPos;
		assert destPos <= dest.length - length : destPos;
		UNSAFE.copyMemory(src, BYTE_ARRAY_OFFSET + srcPos, dest,
				BYTE_ARRAY_OFFSET + destPos, length);
	}

	// public static void copy(long srcPointer, byte[] dest, int length)
	// {
	//
	// UNSAFE.copyMemory(srcPointer, toAddress(dest), length);
	// }
	//
	// public static void copy(byte[] src, long destPointer, int length)
	// {
	//
	// UNSAFE.copyMemory(toAddress(src), destPointer, length);
	// }
	//
	// public static long toAddress(byte[] array)
	// {
	// return normalize(UNSAFE.getInt(array, BYTE_ARRAY_OFFSET));
	// }
	//
	// private static long normalize(int value)
	// {
	// if (value >= 0)
	// return value;
	// return (~0L >>> 32) & value;
	// }

	// public static byte[] fromAddress(long address) {
	// // byte[] array = new byte[] {(byte)0};
	// Object[]
	// UNSAFE.putLong(array, BYTE_ARRAY_OFFSET, address);
	// return array;
	// }

	// public static byte[] fromAddress(long address)
	// {
	// Object[] array = new Object[] { new byte[] { (byte) 0 } };
	// getUnsafe().putLong(array, BYTE_ARRAY_OFFSET, address);
	// return (byte[]) array[0];
	// }

	public static void copy(long src, byte[] dest, long length)
	{
		UNSAFE.copyMemory(null, src, dest, BYTE_ARRAY_OFFSET, length);
	}

	public static void copy(byte[] src, long dest, long length)
	{
		UNSAFE.copyMemory(src, BYTE_ARRAY_OFFSET, null, dest, length);
	}

	public static void copy(byte[] src, long dest, long offset, long length)
	{
		UNSAFE.copyMemory(src, BYTE_ARRAY_OFFSET, null, dest + offset, length);
	}

	public static void copy(byte[] src, int offset, long dest, int length)
	{
		UNSAFE.copyMemory(src, BYTE_ARRAY_OFFSET + offset, null, dest, length);
	}

	public static void copy(byte[] src, byte[] dest)
	{
		UNSAFE.copyMemory(src, BYTE_ARRAY_OFFSET, dest, 0, dest.length);
	}

	public static void copy(byte[] src, byte[] dest, int length)
	{
		UNSAFE.copyMemory(src, BYTE_ARRAY_OFFSET, dest, 0, length);
	}

	public static void fill(long start, long length, byte value)
	{
		UNSAFE.setMemory(start, length, value);
	}

	public static void fill(byte[] dest, long length, byte value)
	{
		UNSAFE.setMemory(dest, BYTE_ARRAY_OFFSET, length, value);
	}

	public static void copy(long src, byte[] dest)
	{
		UNSAFE.copyMemory(null, src, dest, BYTE_ARRAY_OFFSET, dest.length);
	}

}