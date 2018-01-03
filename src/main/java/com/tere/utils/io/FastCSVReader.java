package com.tere.utils.io;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;

import com.tere.logging.LogManager;
import com.tere.logging.Logger;

public class FastCSVReader
{
	private static Logger log = LogManager.getLogger(FastCSVReader.class);
	public static String newline = System.getProperty("line.separator");
	private byte[] buffer;
	private int bufLen;
	private char delimeter;
	private char stringsDelimeter;
	private int column = 0;
	private long rows = 0l;

	public FastCSVReader(int bufferLength, char delimeter)
	{
		this.bufLen = bufferLength;
		buffer = new byte[bufLen * 2];
		this.delimeter = delimeter;
		this.stringsDelimeter = stringsDelimeter;
	}

	protected long getRows()
	{
		return rows;
	}

	protected void onTokenReceived(byte[] buf, int startPos, int length,
			int column) throws Exception
	{
		log.info("Token '%s', column %d", new String(buf, startPos, length),
				column);

	}

	protected void onNewLineReceived() throws Exception
	{
		log.info("Row %d", rows);
	}

	protected int decodeBuffer(byte[] buffer, int length) throws Exception
	{
		int tokenStartPos = 0;
		int pos = 0;
		int len;
		boolean newLineRec = false;
		for (int bufPos = 0; bufPos < length; bufPos++)
		{
			byte ch = buffer[bufPos];
			switch ((char) ch)
			{
			case '\n':
			case '\r':
				if (!newLineRec)
				{
					len = pos - tokenStartPos;
					onTokenReceived(buffer, tokenStartPos, len, column);
					column = 0;
					onNewLineReceived();
					rows++;
				}
				tokenStartPos = pos + 1;
				newLineRec = true;
				break;
			default:
				newLineRec = false;
				if (ch == delimeter)
				{
					len = pos - tokenStartPos;
					onTokenReceived(buffer, tokenStartPos, len, column);
					column++;
					tokenStartPos = pos + 1;
				}
				break;
			}
			pos++;
		}
		return tokenStartPos;
	}

	public long readFile(String path) throws Exception
	{
		File file = new File(path);
		FileInputStream fileInputStream = null;
		BufferedInputStream inputStream = null;
		column = 0;
		try
		{
			fileInputStream = new FileInputStream(file);

			inputStream = new BufferedInputStream(fileInputStream);
			int read;
			int offset = 0;
			int len = bufLen;
			int oldRead = -1;
			String str = null;
			int remainderPos = 0;
			boolean running = true;
			while (running)
			{
				read = fileInputStream.read(buffer, offset, bufLen);
				if (-1 == read)
				{
					break;
				}
				remainderPos = decodeBuffer(buffer, read + offset);
				// str = new String(buffer, remainderPos, bufLen-remainderPos);
				if (-1 == oldRead)
				{
					len = read - remainderPos + offset;
					System.arraycopy(buffer, remainderPos, buffer, 0, len);
				}
				else
				{
					len = oldRead - remainderPos + offset;
					System.arraycopy(buffer, remainderPos, buffer, 0, len);
				}
				// str = new String(buffer, 0, bufLen-remainderPos);
				offset = read + offset - remainderPos;
				// len=bufLen-offset;
				oldRead = read;

			}
			// remainder = decodeBuffer(buffer, oldRead);
			str = new String(buffer);
			String str2 = new String(buffer, remainderPos, bufLen
					- remainderPos);

			return rows;
		}
		finally
		{
			if (null != inputStream)
			{
				inputStream.close();
			}
			if (null != fileInputStream)
			{
				fileInputStream.close();
			}
		}
	}
}
