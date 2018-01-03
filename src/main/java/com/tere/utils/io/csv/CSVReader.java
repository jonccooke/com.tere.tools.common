package com.tere.utils.io.csv;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Properties;

import com.csvreader.CsvReader;
import com.tere.TereException;
import com.tere.exceptions.NotInitialisedException;
import com.tere.utils.io.TBatchReader;

public abstract class CSVReader<T> implements TBatchReader<T>
{
	public static int MAX_COLS = 1000;
	private CsvReader reader = null;
	private boolean read = false;
	private char delimeter = ',';
	private int relativeArray[];
	private Format[] colFormats;
	private boolean intialised = false;
	private boolean hasHeaders = true;
	private DateFormat defaultDateFormat = new SimpleDateFormat("dd/MM/yyyy");
	private DecimalFormat defaultDecimalFormat = new DecimalFormat();

	public CSVReader()
	{
		super();
		relativeArray = new int[MAX_COLS];
		colFormats = new Format[MAX_COLS];
		Arrays.fill(relativeArray, -1);
	}

	public void initialise() throws TereException
	{
		intialised = true;
	}

	protected void checkIntialised() throws NotInitialisedException
	{
		if (!intialised)
		{
			throw new NotInitialisedException("CSVReader");
		}
	}

	public Integer getIntegerValue(String value, long rowNum, int colNum)
			throws CSVParseException
	{
		try
		{
			Format format = getFormat(colNum);

			if (null != format)
			{
				return ((DecimalFormat) format).parse(value).intValue();
			}
			return Integer.valueOf(value);
		}
		catch (NumberFormatException e)
		{
			throw new CSVParseException(rowNum, colNum, e);
		}
		catch (ParseException e)
		{
			throw new CSVParseException(rowNum, colNum, e);
		}
	}

	public Byte getByteValue(String value, long rowNum, int colNum)
			throws CSVParseException
	{
		try
		{
			Format format = getFormat(colNum);

			if (null != format)
			{
				return ((DecimalFormat) format).parse(value).byteValue();
			}
			return Byte.valueOf(value);
		}
		catch (NumberFormatException e)
		{
			throw new CSVParseException(rowNum, colNum, e);
		}
		catch (ParseException e)
		{
			throw new CSVParseException(rowNum, colNum, e);
		}
	}

	public Long getLongValue(String value, long rowNum, int colNum)
			throws CSVParseException
	{
		try
		{
			Format format = getFormat(colNum);

			if (null != format)
			{
				return ((DecimalFormat) format).parse(value).longValue();
			}
			return Long.valueOf(value);
		}
		catch (ParseException e)
		{
			throw new CSVParseException(rowNum, colNum, e);
		}
	}

	public Boolean getBooleanValue(String value, long rowNum, int colNum)
			throws TereException
	{
		try
		{
			return Boolean.parseBoolean(value);
		}
		catch (Exception e)
		{
			throw new CSVParseException(rowNum, colNum, e);
		}
	}

	public Double getDoubleValue(String value, long rowNum, int colNum)
			throws TereException
	{
		try
		{
			Format format = getFormat(colNum);

			if (null != format)
			{
				return ((DecimalFormat) format).parse(value).doubleValue();
			}
			return Double.valueOf(value);
		}
		catch (ParseException e)
		{
			throw new CSVParseException(rowNum, colNum, e);
		}
	}

	public BigDecimal getBigDecimalValue(String value, long rowNum, int colNum)
			throws TereException
	{
		try
		{
			Format format = getFormat(colNum);

			if (null != format)
			{
				return new BigDecimal(((DecimalFormat) format).parse(value)
						.doubleValue());
			}
			return new BigDecimal(value);
		}
		catch (ParseException e)
		{
			throw new CSVParseException(rowNum, colNum, e);
		}
	}

	public Date getDateValue(String value, long rowNum, int colNum)
			throws TereException
	{
		try
		{
			Format format = getFormat(colNum);

			if (null != format)
			{
				return ((DateFormat) format).parse(value);
			}
			return defaultDateFormat.parse(value);
		}
		catch (ParseException e)
		{
			throw new CSVParseException(rowNum, colNum, e);
		}
	}

	public void setDelimeter(char delim)
	{
		delimeter = delim;
	}

	protected CsvReader getCsvReader()
	{
		return reader;
	}

	protected CsvReader getCsvReader(InputStream inputStream)
	{
		if (null == reader)
		{
			reader = new CsvReader(inputStream, Charset.defaultCharset());
			reader.setDelimiter(delimeter);
		}

		return reader;
	}

	public void setFormat(Format format, int col) throws FormatException
	{
		if (col >= MAX_COLS)
		{
			throw new FormatException(col, "Format column out of range");
		}
		if (-1 != col)
		{
			colFormats[col] = format;
		}
	}

	public Format getFormat(int col)
	{
		if (col >= MAX_COLS)
		{
			return null;
		}
		return colFormats[col];
	}

	protected abstract T createEntry(String[] values) throws TereException;

	protected void onReadHeaders(String[] values) throws TereException
	{

	}

	protected void onReadValues(String[] values) throws TereException
	{

	}

	public boolean checkValue(String value)
	{
		if ((null != value) && (0 != value.length()))
		{
			return true;
		}
		return false;
	}

	public int getRelativePosition(int pos)
	{
		if (pos < relativeArray.length)
		{
			return relativeArray[pos];
		}
		return -1;
	}

	public void setRelativePosition(int fileColumn, int parseColumn)
	{
		if (fileColumn < relativeArray.length)
		{
			relativeArray[parseColumn] = fileColumn;
		}
	}

	@Override
	public void readRaw(InputStream inputStream) throws TereException
	{
		checkIntialised();

		try
		{
			CsvReader reader = getCsvReader(inputStream);
			if (!read)
			{
				reader.readHeaders();
				onReadHeaders(reader.getHeaders());
				read = true;
			}
			if (reader.readRecord())
			{
				onReadValues(reader.getValues());
			}
		}
		catch (IOException e)
		{
			throw new TereException(e);
		}
	}

	@Override
	public T create(Object readObject) throws TereException
	{
		String[] values = (String[]) readObject;
		return createEntry(values);
	}

	@Override
	public T read(InputStream inputStream) throws TereException

	{
		checkIntialised();
		CsvReader reader = getCsvReader(inputStream);
		try
		{
			if (hasHeaders && (!read))
			{
				reader.readHeaders();
				onReadHeaders(reader.getHeaders());
				read = true;
			}
//			else
			{
				if (reader.readRecord())
				{
					String[] values = reader.getValues();

					onReadValues(values);

					return create(values);
				}
			}

		}
		catch (IOException e)
		{
			throw new TereException(e);
		}
		return null;
	}

	@Override
	public long readBatch(InputStream inputStream, T[] array, int batchSize)
			throws TereException
	{
		checkIntialised();
		long ret = 0;
		for (int batchPos = 0; batchPos < batchSize; batchPos++)
		{
			T entry = read(inputStream);
			if (null == entry)
			{
				return ret;
			}
			array[batchPos] = entry;
			ret++;
		}

		return ret;

	}

	public Format getDefaultDateFormat()
	{
		return defaultDateFormat;
	}

	public void setDefaultDateFormat(DateFormat defaultDateFormat)
	{
		this.defaultDateFormat = defaultDateFormat;
	}

	@Override
	public Collection<T> read(InputStream inputStream, Properties properties)
			throws TereException
	{
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isHasHeaders()
	{
		return hasHeaders;
	}

	public void setHasHeaders(boolean hasHeaders)
	{
		this.hasHeaders = hasHeaders;
	}

}
