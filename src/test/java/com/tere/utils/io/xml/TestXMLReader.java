package com.tere.utils.io.xml;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.xml.xpath.XPathExpressionException;

import org.junit.Test;
import org.w3c.dom.DOMException;

import com.tere.TereException;
import com.tere.utils.directory.FileUtils;

public class TestXMLReader
{

	@Test
	public void testQuotedString() throws TereException, XPathExpressionException, DOMException, IOException
	{
		String testXML = FileUtils.readTextFile("src/test/resources/testdata/xml/quotedstring.xml").toString();		
		XmlReader xmlReader = new XmlReader(new ByteArrayInputStream(testXML.getBytes()));
		
		String result = xmlReader.one("test/@sql");
		
		
	}

}
