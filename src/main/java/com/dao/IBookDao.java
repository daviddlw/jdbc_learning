package com.dao;

import java.io.Serializable;
import java.util.List;

import com.dto.Book;
import com.dto.Student;

public interface IBookDao
{
	/**
	 * * 新增记录 * @param student * @return 学生对象
	 */
	Serializable save(Book student);

	/**
	 * * 更新记录 * @param student 学生对象
	 * @return 
	 */
	void update(Book student);

	/**
	 * * 根据主键ID删除 记录 * @param id 逐渐ID
	 */
	void delete(Serializable id);

	/**
	 * * 获取记录 * @param id 主键ID* @return 记录
	 */
	Book get(Serializable id);

	/**
	 * *
	 *  获取记录列表
	 * * @return 记录列表
	 */
	List<Book> findAll();

}
