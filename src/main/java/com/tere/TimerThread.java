package com.tere;

public abstract class TimerThread implements Runnable
{
	private long timeOut = 100;
	private boolean running = true;

	public TimerThread(long timeOut)
	{
		this.timeOut = timeOut;
	}

	public boolean isRunning()
	{
		return running;
	}

	public void close()
	{
		this.running = false;
	}

	@Override
	public void run()
	{
		while (running)
		{
			try
			{
				Thread.sleep(timeOut);
				
				onTimeOut();
			}
			catch (InterruptedException e)
			{
				running = false;
			}
		}
	}

	protected abstract void onTimeOut();	
}