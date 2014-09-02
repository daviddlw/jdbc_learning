package com.dto;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class LogHandler implements InvocationHandler
{
	private Object obj;

	public LogHandler(Object obj)
	{
		super();
		this.obj = obj;
	}
	
	private void doBefore()
	{
		System.err.println("log before");
	}
	
	private void doAfter()
	{
		System.err.println("log after");
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
	{
		doBefore();
		Object result = method.invoke(obj, args);
		doAfter();
		return result;
	}

}
