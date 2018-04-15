package com.tere.memory;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Test;

public class UnsafeTests
{

	@Test
	public void testByteArrayAddress()
	{
	
		byte[] array = {'0', '1','2','3'};
		
		long address = Unsafe.toAddress(array);
		
		byte[] found = Unsafe.fromAddress(address);
		
		Assert.assertArrayEquals(array, found);
		
	}

}
