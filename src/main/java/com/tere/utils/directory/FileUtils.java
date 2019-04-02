package com.tere.utils.directory;

import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.StringTokenizer;

import com.tere.TereException;
import com.tere.logging.LogManager;
import com.tere.logging.Logger;

public final class FileUtils
{
	private static Logger log = LogManager.getLogger(FileUtils.class);
	public static final int JAVA_NAMESPACE = 0;
	public static final int CPLUSPLUS_NAMESPACE = 1;
	public static final int CSHARP_NAMESPACE = 2;

	public static final char JAVA_NS_TOKEN = '.';
	public static final char CPLUSPLUS_NS_TOKEN = '_';
	public static final char CSHARP_NS_TOKEN = '.';
	public static final char DIR_SEPARATOR = File.separatorChar;
	public static final String INVALID_CHARS_REGEX = "[!\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~]";
	public static final String EXT_SEPARATOR = ".";

	public FileUtils()
	{
		super();
	}

	static public char getNameSpaceToken(int nsType)
	{
		switch (nsType)
		{
		case JAVA_NAMESPACE:
			return JAVA_NS_TOKEN;
		case CPLUSPLUS_NAMESPACE:
			return CPLUSPLUS_NS_TOKEN;
		case CSHARP_NAMESPACE:
			return CSHARP_NS_TOKEN;
		}

		return JAVA_NS_TOKEN;
	}

	public static String toAbsoluteFilePath(String filePath) throws IOException
	{
		String newPath = filePath.trim();

		if (newPath.toLowerCase().startsWith("classpath:"))
		{
			newPath = newPath.substring("classPath:".length());
			URL url = ClassLoader.getSystemResource(".");
//			url = ClassLoader.getSystemResource(newPath);
//			if (null == url)
//			{
//				return null;
//			}
//			
			newPath = url.getPath() + newPath;
			return newPath;
		}
		else
		{
			File file = new File(newPath);
		
			return file.getAbsoluteFile().getAbsolutePath();
		}
	}
	
	public static StringBuffer readTextFile(InputStream inputStream)
			throws IOException
	{

		StringBuffer buf = new StringBuffer();

		InputStreamReader reader = new InputStreamReader(inputStream);

		char[] cBuf = new char[1000];
		int read = 0;
		while (-1 != (read = reader.read(cBuf)))
		{
			String str = new String(cBuf, 0, read);

			buf.append(str);
		}

		return buf;
	}

	public static InputStream getInputStream(String path) throws IOException
	{
		String newPath = path.trim();
		
		InputStream inputStream = null;
		if (newPath.toLowerCase().startsWith("classpath:"))
		{
			newPath = newPath.substring("classPath:".length());
			URL url = ClassLoader.getSystemResource(".");
//			log.debug("readTextFile:root:'%s'", url.getPath());
//			log.debug("readTextFile:reading file:'%s'", newPath);
			url = ClassLoader.getSystemResource(newPath);
			if (null == url)
			{
				return null;
			}
			log.debug("readTextFile:with abs path:'%s'", url.getPath());
			inputStream = url.openStream();
		}
		else
		{
			inputStream = new FileInputStream(toAbsoluteFilePath(path));
		}

//		inputStream = new FileInputStream(absPath);
		return inputStream;
	}


	public static StringBuffer readTextFile(String path) throws IOException
	{
//		String newPath = path.trim();
//		
		log.debug("Getting stream for path " + path);
		InputStream inputStream = getInputStream(path);
		log.debug("Got Stream " + inputStream);
		
		if (null == inputStream)
		{
			return null;
		}
//		if (newPath.toLowerCase().startsWith("classpath:"))
//		{
//			newPath = newPath.substring("classPath:".length());
//			URL url = ClassLoader.getSystemResource(".");
//			log.debug("readTextFile:root:'%s'", url.getPath());
//			log.debug("readTextFile:reading file:'%s'", newPath);
//			url = ClassLoader.getSystemResource(newPath);
//			log.debug("readTextFile:with abs path:'%s'", url.getPath());
//			inputStream = ClassLoader.getSystemResourceAsStream(newPath);
//		}
//		else
//		{
//			inputStream = new FileInputStream(path);
//		}
		
		StringBuffer buf = readTextFile(inputStream);

		inputStream.close();

		return buf;
	}

	public static byte[] readSmallBinaryFile(String path) throws IOException
	{
//		String newPath = path.trim();
//		
		String resolvedPath = toAbsoluteFilePath(path);
		log.debug("Getting path " + resolvedPath);

//		int bytesToReadNo =1024;
		byte[] buf = null;//new byte[bytesToReadNo];
//		int numRead =-1;
		try (FileInputStream inputStream = new FileInputStream(resolvedPath))
		{
			buf = inputStream.readAllBytes();
		}
		return buf;
	}


	public interface ReadBytes
	{
		public void read(long pos, int read, byte[] readBuf);
	}
	
	public static void readLargeBinaryFile(String path, int maxSize, ReadBytes readBytes) throws IOException
	{
//		String newPath = path.trim();
//		
		String resolvedPath = toAbsoluteFilePath(path);
		log.debug("Getting path " + resolvedPath);

		int bytesToReadNo =maxSize;//2^10;
		byte[] buf = new byte[bytesToReadNo];
		int numRead =-1;
		long pos=0;
		try (FileInputStream inputStream = new FileInputStream(resolvedPath))
		{
			while (-1 != (numRead = inputStream.read(buf)))
			{
				readBytes.read(pos, numRead, buf);
				
				pos+=numRead;
			}
		}
	}


	public static final void createTextFile(String path, String fileContentsStr)
			throws TereException
	{
		try
		{
			File textFile = new File(path);

			if (!textFile.exists())
			{
				if (false == textFile.createNewFile())
				{
					throw new TereException("Cannot create file:" + path);
				}
			}
			FileWriter writer = new FileWriter(textFile);

			writer.write(fileContentsStr.toCharArray());
			writer.flush();
			writer.close();
		}
		catch (IOException e)
		{
			throw new TereException("Cannot create file:" + path, e);
		}
	}

	public static final String toNamespace(String namespace, int nsType)
	{
		String ns = new String(namespace);

		switch (nsType)
		{
		case JAVA_NAMESPACE:
			ns = ns.toLowerCase();
			ns.replaceAll(INVALID_CHARS_REGEX, Character
					.toString(JAVA_NS_TOKEN));
			break;
		case CPLUSPLUS_NAMESPACE:
			ns = ns.toUpperCase();
			ns.replaceAll(INVALID_CHARS_REGEX, Character
					.toString(CPLUSPLUS_NS_TOKEN));
			break;
		case CSHARP_NAMESPACE:
			ns = ns.toLowerCase();
			ns.replaceAll(INVALID_CHARS_REGEX, Character
					.toString(CSHARP_NS_TOKEN));
			break;
		}
		return ns;
	}

	public static final String toNamespace(String namespace,
			String namespaceToken)
	{
		String ns = new String(namespace);

		ns = ns.toLowerCase();
		ns.replaceAll(INVALID_CHARS_REGEX, namespaceToken);

		return ns;
	}

	public static final String namespaceToPath(String namespace, int nsType)
	{
		char token = (char) 0;

		switch (nsType)
		{
		case JAVA_NAMESPACE:
			token = JAVA_NS_TOKEN;
			break;
		case CPLUSPLUS_NAMESPACE:
			token = CPLUSPLUS_NS_TOKEN;
			break;
		case CSHARP_NAMESPACE:
			token = CSHARP_NS_TOKEN;
			break;
		}

		return namespace.replace(token, DIR_SEPARATOR);
	}

	public static final String namespaceToPath(String namespace, String nsToken)
	{
		StringBuilder result = new StringBuilder();
		String delimiters = nsToken;
		StringTokenizer st = new StringTokenizer(namespace, delimiters, true);
		while (st.hasMoreTokens())
		{
			String w = st.nextToken();
			if (!w.equals(delimiters))
			{
				result.append(w);
				result.append(DIR_SEPARATOR);
			}
		}
		return result.toString();
	}

	/*
	 * Removes a directory and all subdirectories and files beneath it.
	 * 
	 * @param directory The name of the root directory to be deleted. @return
	 * boolean If all went successful, returns true, otherwise false.
	 * 
	 */
	public static final boolean deleteDirectory(String dirPath)
			throws DirectoryDeletionException
	{
		if (null == dirPath)
		{
			throw new NullPointerException("createDirectories");
		}
		try
		{
			File dir = new File(dirPath);
			if (!dir.isDirectory())
			{
				dir.delete();
				return true;
			}

			deleteChildren(dirPath);

			dir.delete();
			return true;

		}
		catch (Exception e)
		{
			throw new DirectoryDeletionException(e);
		}
	}

	private static final void deleteChildren(String path)
			throws DirectoryDeletionException
	{
		if (null == path)
		{
			throw new NullPointerException("createDirectories");
		}

		File file = new File(path);

		if (file.isFile())
		{
			try
			{
				file.delete();
			}
			catch (Exception e)
			{
				throw new DirectoryDeletionException("Failed to Delete file: "
						+ path + " : ", e);
			}
		}
		else if (file.isDirectory())
		{
			if (!path.endsWith(File.separator))
			{
				path += File.separator;
			}
			String list[] = file.list();

			// Delete all files recursively
			for (int dirLoop = 0; list != null && dirLoop < list.length; dirLoop++)
			{
				deleteChildren(path + list[dirLoop]);
			}
			// Delete the root dir
			try
			{
				file.delete();
			}
			catch (Exception e)
			{
				throw new DirectoryDeletionException(
						"Failed to Delete directory: " + path + " : ", e);
			}
		}
	}

	public static void createDirectories(String rootPath, String path, boolean errorOnFail)
		throws DirectoryCreationException
	{
//		logger.debug("Creating directory " + path + " from " + rootPath);
		
		if ((null == rootPath) || (null == path))
		{
			throw new NullPointerException("createDirectories");
		}
		StringBuffer rootPathBuf = new StringBuffer(rootPath.trim());
		StringBuffer pathBuf = new StringBuffer(path.trim());

		//logger.debug("Checking if separator exists as suffix in root path...");

		if (File.separatorChar == rootPathBuf.charAt(rootPathBuf.length() - 1))
		{
			rootPathBuf.deleteCharAt(pathBuf.length() - 1);
		}

		//logger.debug("Checking if separator exists as suffix in path...");

		if (File.separatorChar == pathBuf.charAt(pathBuf.length() - 1))
		{
			pathBuf.deleteCharAt(pathBuf.length() - 1);
		}

		//logger.debug("Adding separator...");

		String newDir = rootPath + File.separatorChar + path;

		boolean success = true; 
		
//		logger.debug("Creating dir object:" + newDir);
		
		File dirFile = new File(newDir);
		
//		logger.debug("Checking if dir exists");
		if (!dirFile.exists())
		{
//			logger.debug("Yes. creating dir...");
			success = dirFile.mkdirs();
		}
		if (errorOnFail && !success)
		{
			throw new DirectoryCreationException(newDir);
		}
	}

	public static void close(Closeable closeable) throws IOException
	{
		if (null != closeable)
		{
			closeable.close();
		}
	}
}
