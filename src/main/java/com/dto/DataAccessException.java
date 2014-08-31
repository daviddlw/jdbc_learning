package com.dto;

public class DataAccessException extends RuntimeException
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DataAccessException()
	{
		// TODO Auto-generated constructor stub
		super();
	}

	public DataAccessException(Throwable throwable, String message)
	{
		super(message, throwable);
	}
	
	public DataAccessException(Throwable throwable)
	{
		super(throwable);
	}

}
