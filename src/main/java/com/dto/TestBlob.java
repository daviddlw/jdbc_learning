package com.dto;

import java.util.Arrays;

public class TestBlob
{
	private int id;
	private byte[] picBytes;
	private byte[] txtBytes;

	public TestBlob()
	{
		super(); // TODO Auto-generated constructor stub
	}

	public TestBlob(int id, byte[] picBytes, byte[] txtBytes)
	{
		super();
		this.id = id;
		this.picBytes = picBytes;
		this.txtBytes = txtBytes;
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public byte[] getPicBytes()
	{
		return picBytes;
	}

	public void setPicBytes(byte[] picBytes)
	{
		this.picBytes = picBytes;
	}

	public byte[] getTxtBytes()
	{
		return txtBytes;
	}

	public void setTxtBytes(byte[] txtBytes)
	{
		this.txtBytes = txtBytes;
	}

	@Override
	public String toString()
	{
		return "TestBlob [id=" + id + ", picBytes=" + picBytes.length + ", txtBytes=" + txtBytes.length + "]";
	}

}
