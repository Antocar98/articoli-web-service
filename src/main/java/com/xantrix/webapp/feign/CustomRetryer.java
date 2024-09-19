package com.xantrix.webapp.feign;

import feign.RetryableException;
import feign.Retryer;
import lombok.extern.java.Log;

@Log
public class CustomRetryer implements Retryer 
{
	private final int maxAttemts;
	private final long backoff;
	int attempt;
	
	public CustomRetryer() 
	{
		this(2000, 5);
	}
	
	public CustomRetryer(long backoff, int maxAttemts)
	{
		this.backoff = backoff;
		this.maxAttemts = maxAttemts;
		this.attempt = 1;
	}
	
	public void continueOrPropagate(RetryableException e)
	{
		if (attempt++ >= maxAttemts)
		{
			throw e;
		}
		else
		{
			log.warning(String.format("Tentativo di connessione %s", attempt));
		}
		
		try
		{
			Thread.sleep(backoff);
		}
		catch(InterruptedException ignored)
		{
			Thread.currentThread().interrupt();
		}
	}
	
	public Retryer clone() 
	{
		return new CustomRetryer(backoff,maxAttemts);
	}
}
