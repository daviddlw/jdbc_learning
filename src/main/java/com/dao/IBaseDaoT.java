package com.dao;

import java.io.Serializable;
import java.util.List;

/**
 * 基础公共类
 * 
 * @author pc
 * 
 */
public interface IBaseDaoT<T>
{
	public Serializable save(final T obj);

	public Serializable delete(final int id);

	public Serializable update(final T obj);

	public T get(final int id);

	public List<T> findAll();
}
