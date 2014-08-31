package com.dao;

import java.io.Serializable;
import java.util.List;

/**
 * 基础公共类
 * @author pc
 * 
 */
public interface IBaseDao
{
	public Serializable save(final Object obj);
	
	public Serializable delete(final Class type, final int id);
	
	public Serializable update(final Object obj);
	
	public Object get(final Class type, final int id);
	
	public List<? extends Object> findAll(final Class type);
}
