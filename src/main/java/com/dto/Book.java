package com.dto;

public class Book
{
	private int id;
	private String name;
	private String author;
	private int amount;
	private double price;

	public Book(int id, String name, String author, int amount, double price)
	{
		super();
		this.id = id;
		this.name = name;
		this.author = author;
		this.amount = amount;
		this.price = price;
	}

	public Book()
	{
		super(); // TODO Auto-generated constructor stub
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getAuthor()
	{
		return author;
	}

	public void setAuthor(String author)
	{
		this.author = author;
	}

	public int getAmount()
	{
		return amount;
	}

	public void setAmount(int amount)
	{
		this.amount = amount;
	}

	public double getPrice()
	{
		return price;
	}

	public void setPrice(double price)
	{
		this.price = price;
	}

	@Override
	public String toString()
	{
		return "Book [id=" + id + ", name=" + name + ", author=" + author + ", amount=" + amount + ", price=" + price + "]";
	}

}
