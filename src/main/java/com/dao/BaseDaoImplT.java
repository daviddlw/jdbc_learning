package com.dao;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;

public class BaseDaoImplT<T> implements IBaseDaoT<T>
{
	private JdbcTemplateT<T> jdbcTemplate = new JdbcTemplateT<T>();
	protected Class<T> entityClass = null;

	public BaseDaoImplT(Class<T> classType)
	{
		super();
		try
		{
			entityClass = classType;
		} catch (Exception e)
		{
			e.printStackTrace();
			// TODO: handle exception
		}

		// TODO Auto-generated constructor stub
	}

	public Serializable save(final T obj)
	{
		return jdbcTemplate.save(obj);
	}

	public Serializable delete(final int id)
	{
		return jdbcTemplate.delete(entityClass, id);

	}

	public Serializable update(final T obj)
	{
		return jdbcTemplate.update(obj);
	}

	public T get(final int id)
	{
		return jdbcTemplate.get(entityClass, id);
	}

	public List<T> findAll()
	{
		return jdbcTemplate.findAll(entityClass);
	}

}
