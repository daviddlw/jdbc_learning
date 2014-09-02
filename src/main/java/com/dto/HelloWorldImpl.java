package com.dto;

public class HelloWorldImpl implements IHelloWorld
{

	@Override
	public void sayHello(String message)
	{
		System.out.println("message=> " + message);
	}

}
