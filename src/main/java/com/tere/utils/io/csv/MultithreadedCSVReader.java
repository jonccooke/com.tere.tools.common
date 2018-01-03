package com.tere.utils.io.csv;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Queue;
import java.util.Vector;
import java.util.concurrent.LinkedBlockingQueue;

import com.csvreader.CsvReader;
import com.tere.TereException;
import com.tere.logging.LogManager;
import com.tere.logging.Logger;

public abstract class MultithreadedCSVReader<T>
{
	private static Logger log = LogManager
			.getLogger(MultithreadedCSVReader.class);
	private int numThreads;
	private int queueSize;
	private List<Queue<CommandMessage>> queues;
	private List<Thread> threads;
	private boolean running = false;
	private boolean first = true;
	private boolean readHeaders = true;

	public MultithreadedCSVReader(int numThreads, int queueSize)
	{
		this(numThreads, queueSize, true);
	}
	
	public MultithreadedCSVReader(int numThreads, int queueSize, boolean readHeaders)
	{
		this.numThreads = numThreads;
		this.queueSize = queueSize;
		this.readHeaders = readHeaders;
		queues = java.util.Collections
				.synchronizedList(new Vector<Queue<CommandMessage>>(numThreads));
		threads = new Vector<Thread>(numThreads);
	}

	public void initialise() throws TereException
	{
		if (running)
		{
			throw new AlreadyRunningException();
		}
		for (int threadNo = 0; threadNo < numThreads; threadNo++)
		{
			queues.add(new LinkedBlockingQueue<CommandMessage>());
			Queue<CommandMessage> queue = queues.get(threadNo);
			Thread newThread = new Thread(createThread(threadNo, queue));
			threads.add(newThread);
			newThread.start();
		}
		running = true;
	}

	public void flushAll()
	{
		if (running)
		{
			log.info("Flushing all threads...");
			for (int queueNo = 0; queueNo < numThreads; queueNo++)
			{
				Thread thread = threads.get(queueNo);
				log.debug("Got thread %d...", thread.getId());
				Queue<CommandMessage> queue = queues.get(queueNo);
				CommandMessage commandMessage = new CommandMessage(
						ReaderCommand.FLUSH);
				log.info("Putting flush message...");
				queue.add(commandMessage);
				log.debug("Done.");
			}
		}
	}

	public void close()
	{
		if (running)
		{
			log.info("Stopping all threads...");
			for (int queueNo = 0; queueNo < numThreads; queueNo++)
			{
				log.info("Stopping thread %d...", queueNo);
				Thread thread = threads.get(queueNo);
				log.info("Got thread %d...", thread.getId());
				Queue<CommandMessage> queue = queues.get(queueNo);
				CommandMessage commandMessage = new CommandMessage(
						ReaderCommand.EXIT);
				log.info("Putting exit message...");
				queue.add(commandMessage);
				log.info("Joining thread %d...", thread.getId());
				try
				{
					thread.join();
				}
				catch (InterruptedException e)
				{
					log.info("Thread %d Interrupted.", thread.getId());
				}
				log.info("Thread %d finished.", thread.getId());
			}
		}
		running = false;
	}

	protected abstract CSVReaderThread createThread(int threadNum,
			Queue<CommandMessage> queue) throws TereException;

	public long readFile(InputStream inputStream) throws TereException,
			IOException
	{
		long ret = 0;
		int readThread = 0;
		boolean running = true;

		if (!running)
		{
			throw new NotRunningException();
		}

		CsvReader reader = new CsvReader(inputStream, Charset.defaultCharset());

		while (running)
		{
			boolean read = false;
			
			if (first)
			{
				if (readHeaders)
				{
					read = reader.readHeaders();
				}
				first = false;
			}
			read = reader.readRecord();

			if (read)
			{
				int readQueues = 0;
				while (queueSize <= queues.get(readThread).size())
				{
					readThread = (readThread + 1) % numThreads;
					readQueues++;
					if (numThreads == readQueues)
					{
						try
						{
							Thread.sleep(100);
						}
						catch (InterruptedException e)
						{
							log.info("Thread Interrupted.");
							running = false;
							break;
						}
						readQueues = 0;
					}
				}
				if (running)
				{
					Queue<CommandMessage> queue = queues.get(readThread);
					CommandMessage commandMessage = new CommandMessage(
							reader.getValues(), ReaderCommand.MESSAGE, ret);
					queue.add(commandMessage);
					ret++;
				}
				readThread = (readThread + 1) % numThreads;
			}
			else
			{
				log.info("End of file");
				running = false;
			}
		}

		return ret;
	}
}
