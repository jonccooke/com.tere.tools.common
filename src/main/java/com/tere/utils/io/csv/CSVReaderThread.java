package com.tere.utils.io.csv;

import java.util.Queue;

import com.tere.TereException;
import com.tere.logging.LogManager;
import com.tere.logging.Logger;

public abstract class CSVReaderThread<T> implements Runnable
{
	private static Logger log = LogManager.getLogger(CSVReaderThread.class);
	private Queue<CommandMessage> queue;
	private boolean running = true;
	private int threadNo;

	public CSVReaderThread(int threadNo, Queue<CommandMessage> queue)
	{
		this.queue = queue;
		this.threadNo = threadNo;
	}

	@Override
	public void run()
	{
		log.info("Starting thread %d...", threadNo);
		try
		{
			onStart();
		}
		catch (TereException e1)
		{
			log.error(e1.getMessage());
			return;
		}
		
		while (running)
		{
			if (!queue.isEmpty())
			{
				CommandMessage commandMessage = queue.poll();

				if (null != commandMessage)
				{
					switch (commandMessage.getCommand())
					{
					case EXIT:
						log.info("Got exit command, thread %d...", threadNo);
						running = false;
						break;
					case FLUSH:
						log.info("Got flush command, thread %d...", threadNo);
						try
						{
							onFlush();
						}
						catch (TereException e)
						{
							log.error(e.getMessage());
						}
						break;
					case MESSAGE:
						try
						{
							onRead(commandMessage.getValues(),
									commandMessage.getRowNum());
						}
						catch (TereException e)
						{
							log.error(e.getMessage());
						}
						break;
					}
				}
			}
			else
			{
				try
				{
					Thread.sleep(100);
				}
				catch (InterruptedException e)
				{
					log.info("Thread %d Interrupted.", threadNo);
					running = false;
				}
			}
		}
		try
		{
			onExit();
		}
		catch (TereException e)
		{
			log.error(e.getMessage());
		}
	}

	protected abstract void onRead(String[] values, long rowNum)
			throws TereException;

	protected void onStart() throws TereException
	{

	}

	protected void onExit() throws TereException
	{

	}

	protected void onFlush() throws TereException
	{

	}

}
