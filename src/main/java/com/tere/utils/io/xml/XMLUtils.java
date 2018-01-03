package com.tere.utils.io.xml;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.csvreader.CsvReader;
import com.tere.logging.LogManager;
import com.tere.logging.Logger;
import com.tere.utils.directory.FileUtils;
import com.tere.utils.reflection.ClassReflect;

public class XMLUtils
{
	private static Logger log = LogManager.getLogger(XMLUtils.class);
	private static String XML_HEADER_STR = "<?xml version='1.0' encoding='ISO-8859-1'?>";

	private static String specialCharsRegex = "[/,:<>!~@#$%^&()+=?()\"|!\\[#$-]";  
    

	public static String replaceSpecialCharactors(String inputString)
	{
		int n=0;
		String newStr = "";
		for (char c : inputString.toCharArray())
		{
			if (' ' == c)
			{
				
			}
			else if (-1 != specialCharsRegex.indexOf(c))
			{
				newStr+="_";
			}
			else
			{
				newStr+=c;
			}
		}
		return newStr;
	}
	
	public static void CsvToXML(String csvInputFile, String xmlOutputFile,
			boolean readHeaders)
	{

		FileReader inputFileReader = null;
		FileWriter outputFileWriter = null;
		CsvReader csvReader = null;
		String[] values = null;

		String[] header = null;

		try
		{
			String csvFileName = FileUtils.toAbsoluteFilePath(csvInputFile);
			inputFileReader = new FileReader(csvFileName);
			csvReader = new CsvReader(inputFileReader);
			outputFileWriter = new FileWriter(FileUtils.toAbsoluteFilePath(xmlOutputFile), false);
			outputFileWriter.write(XML_HEADER_STR);
			outputFileWriter.write("\n");
			outputFileWriter.write("<elements>");
			if (readHeaders)
			{
				csvReader.readHeaders();
				header = csvReader.getHeaders();
				while (csvReader.readRecord())
				{
					outputFileWriter.write("\t<element ");
					values = csvReader.getValues();
					for (int i = 0; i < values.length; i++)
					{
						outputFileWriter.write(replaceSpecialCharactors(header[i]));
						outputFileWriter.write("=\'");
						outputFileWriter.write(values[i]);
						outputFileWriter.write("' ");
					}
					outputFileWriter.write(" />\n");
					outputFileWriter.flush();
				}
			}
			else
			{
				while (csvReader.readRecord())
				{
					outputFileWriter.write("\t<element ");
					values = csvReader.getValues();
					for (int i = 0; i < header.length; i++)
					{
						outputFileWriter.write("value" + i);
						outputFileWriter.write("=\'");
						outputFileWriter.write(values[i]);
						outputFileWriter.write("' ");
					}
					outputFileWriter.write(" />\n");
					outputFileWriter.flush();
				}
			}
			outputFileWriter.write("</elements>");

		}
		catch (Exception e)
		{
			log.error(e.getMessage());
		}
		finally
		{
			try
			{
				FileUtils.close(inputFileReader);
				FileUtils.close(outputFileWriter);
			}
			catch (IOException e)
			{
				log.error(e.getMessage(), e);
			}
			if (null != csvReader)
			{
				csvReader.close();
			}

		}
	}
}
