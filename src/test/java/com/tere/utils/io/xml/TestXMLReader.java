package com.tere.utils.io.xml;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.xpath.XPathExpressionException;

import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.DOMException;

import com.tere.TereException;
import com.tere.utils.directory.FileUtils;

public class TestXMLReader
{

	@Test
	public void testXPath() throws TereException, XPathExpressionException, DOMException, IOException
	{
		try (InputStream is= FileUtils.getInputStream("classpath:testdata/xml/Catalog.xml"))
		{
			
			Assert.assertEquals( "Empire Burlesque", XmlReader.reader(is).one("/CATALOG/CD/TITLE/text()"));
		}		
	}

}
