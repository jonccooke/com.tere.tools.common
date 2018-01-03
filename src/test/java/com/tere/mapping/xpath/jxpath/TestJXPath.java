package com.tere.mapping.xpath.jxpath;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.xml.DOMParser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;

import com.tere.TereException;
import com.tere.logging.LogManager;
import com.tere.logging.Logger;
import com.tere.mapping.TMapper;
import com.tere.utils.directory.FileUtils;

public class TestJXPath
{
	private static final String TIME_SERIES_CSV_FILE_PATH = "src/test/resources/testdata/finance/TimeSeries.csv";
	private static final String TIME_SERIES_JSON_FILE_PATH = "src/test/resources/testdata/finance/TimeSeries.json";
	private static Logger log = LogManager.getLogger(TestJXPath.class);

	@Before
	public void setUp() throws Exception
	{
	}

	@After
	public void tearDown() throws Exception
	{
	}


	@Test
	public void loadEquityOptionXPathTMapper() throws TereException, IOException
	{
		
		FileInputStream fileInputStream = null;
		BufferedInputStream bufferedInputStream = null;
		
		try
		{
			fileInputStream = new FileInputStream(FileUtils.toAbsoluteFilePath("src/test/resources/testdata/finance/fpml/4.1/eqd-ex01-american-call-stock-long-form.xml"));
			bufferedInputStream = new BufferedInputStream(fileInputStream);

			DOMParser domParser = new DOMParser();
			TMapper mapper = new JXPathMapper();
			mapper.init(null);

			long no = 0;
			long ms = System.currentTimeMillis();
			bufferedInputStream.mark(fileInputStream.available());
			Document doc = (Document)domParser.parseXML(bufferedInputStream);
			Object srcContext = mapper.createContext(doc); 
			EuropeanOption europeanOption = new EuropeanOption();
			Object destContext = mapper.createContext(europeanOption); 
			mapper.map(srcContext, destContext, "/fpml:FpML/fpml:header/fpml:creationTimestamp/text()", "tradeDate", Date.class);
			log.info("%d, %d", 1, System.currentTimeMillis() - ms);
			
		}
		finally
		{
			try
			{
				if (null != bufferedInputStream)
				{
					bufferedInputStream.close();
				}
				if (null != fileInputStream)
				{
					fileInputStream.close();
				}
			}
			catch (IOException e)
			{
				log .error(e);
			}
		}
	}

	@Test
	public void loadEquityOptionXPathTMapperBulk() throws TereException, IOException
	{
		
		FileInputStream fileInputStream = null;
		BufferedInputStream bufferedInputStream = null;
		
		try
		{
			fileInputStream = new FileInputStream(FileUtils.toAbsoluteFilePath("src/test/resources/testdata/finance/fpml/4.1/eqd-ex01-american-call-stock-long-form-bulk.xml"));
			bufferedInputStream = new BufferedInputStream(fileInputStream);

			DOMParser domParser = new DOMParser();
			TMapper mapper = new JXPathMapper();
			mapper.init(null);

			long no = 0;
			long ms = System.currentTimeMillis();
			bufferedInputStream.mark(fileInputStream.available());
			Document doc = (Document)domParser.parseXML(bufferedInputStream);
			Object srcContext = mapper.createContext(doc); 
//			EuropeanOption europeanOption = new EuropeanOption();
//			Object destContext = mapper.createContext(europeanOption); 
//			mapper.map(srcContext, destContext, "/fpml:messages/fpml:FpML/fpml:header/fpml:creationTimestamp/text()", "tradeDate", Date.class);
			log.info("%d, %d", 1, System.currentTimeMillis() - ms);
			
		}
		finally
		{
			try
			{
				if (null != bufferedInputStream)
				{
					bufferedInputStream.close();
				}
				if (null != fileInputStream)
				{
					fileInputStream.close();
				}
			}
			catch (IOException e)
			{
				log .error(e);
			}
		}
	}
	
	@Test
	public void loadFPMLEquityOption() throws TereException, IOException
	{
		
		FileInputStream fileInputStream = null;
		BufferedInputStream bufferedInputStream = null;
		
		try
		{
			fileInputStream = new FileInputStream(FileUtils.toAbsoluteFilePath("src/test/resources/testdata/finance/fpml/4.1/eqd-ex01-american-call-stock-long-form.xml"));
			bufferedInputStream = new BufferedInputStream(fileInputStream);
			DOMParser domParser = new DOMParser();
			long no = 0;
			long ms = System.currentTimeMillis();
			Document doc = (Document)domParser.parseXML(bufferedInputStream);
			JXPathContext context = JXPathContext.newContext(doc);
			Object object = context.getValue("/fpml:FpML/fpml:header/fpml:creationTimestamp/text()");
			
			log.info("%s", object.toString(), System.currentTimeMillis() - ms);
		}
		finally
		{
			try
			{
				if (null != bufferedInputStream)
				{
					bufferedInputStream.close();
				}
				if (null != fileInputStream)
				{
					fileInputStream.close();
				}
			}
			catch (IOException e)
			{
				log .error(e);
			}
		}
	}

}
