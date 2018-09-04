package com.tere.memory;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Assert;
import org.junit.Test;

import com.tere.logging.LogManager;
import com.tere.logging.Logger;

public class UnsafeTests
{
	private static Logger log = LogManager.getLogger(UnsafeTests.class);

	@Test
	public void testByteArrayAddress()
	{
	
		byte[] array = {'0', '1','2','3'};
		
		long address = Unsafe.toAddress(array);
		
//		byte[] found = Unsafe.fromAddress(address);
		
//		Assert.assertArrayEquals(array, found);
		
	}

	@Test
	public void testPointerToPrimativeArray()
	{
		Integer int1 = new Integer(10);
		Integer int2 = (Integer) Unsafe.fromAddress(Unsafe.toAddress(int1));
		
		int[] iint1 = {1,2,3,4};
		int[] iint2 = (int[]) Unsafe.fromAddress(Unsafe.toAddress(iint1));

		long arrayUnsafe1 = Unsafe.getUnsafe().allocateMemory(4 *4);
		int[] iint3 = (int[]) Unsafe.fromAddress(arrayUnsafe1);
		iint3[0] =1;
		Unsafe.getUnsafe().freeMemory(arrayUnsafe1);
//		int pow = 20;
//		int size = 1 << pow;
//		int bound = 1000000;
//		log.debug("Generating %d values", size);
//		int[] array1 = new Random().ints(0, bound).limit(size).toArray();
//
//		long arrayUnsafe1 = Unsafe.getUnsafe().allocateMemory(size *4);
//		long arrayUnsafe2 =  Unsafe.toAddress(array1);
//		long address = Unsafe.getUnsafe().getLong(array1, (long) Unsafe.getUnsafe().ARRAY_INT_BASE_OFFSET);
//		int[] array2 = Unsafe.intArrayfromAddress(arrayUnsafe2);
//		array2[0] = 0;
//		log.debug("Done...");
//		long tm1 = System.currentTimeMillis();
//
//		long tm2 = System.currentTimeMillis() - tm1;
//		log.debug("Done in %d(ms)...", tm2);
//		Unsafe.getUnsafe().freeMemory(arrayUnsafe1);
	}

	@Test
	public void testPrimativeArrayPointerTest()
	{
		final long[] ar = new long[ 1000 ];
		System.out.println( "long[] header: size = " + Unsafe.getUnsafe().ARRAY_LONG_BASE_OFFSET );
		System.out.println( "First 8 bytes = " + Long.toHexString( Unsafe.getUnsafe().getLong( ar, 0L ) ) );
		//check what's in the array header
		for ( int i = 8; i < Unsafe.getUnsafe().ARRAY_LONG_BASE_OFFSET; i+=2 )
		{
		    System.out.print( ( Unsafe.getUnsafe().getShort(ar, (long) i) & 0xFFFF ) + " ");
		}
		System.out.println();
		 
		final int[] ar1 = new int[ 1000 ];
		System.out.println( "int[] header: size = " + Unsafe.getUnsafe().ARRAY_INT_BASE_OFFSET );
		System.out.println( "First 8 bytes = " + Long.toHexString( Unsafe.getUnsafe().getLong( ar1, 0L ) ) );
		//check what's in the array header
		for ( int i = 8; i < Unsafe.getUnsafe().ARRAY_INT_BASE_OFFSET; i+=2 )
		{
		    System.out.print( ( Unsafe.getUnsafe().getShort(ar1, (long) i) & 0xFFFF ) + " ");
		}
		System.out.println();

	}
}
