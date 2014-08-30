package com.dao;

import java.io.Serializable;
import java.util.List;

import com.dto.Student;

public interface IStudentDao
{
	/**
	 * * 新增记录 * @param student * @return 学生对象
	 */
	Serializable save(Student student) throws Exception;

	/**
	 * * 更新记录 * @param student 学生对象
	 * @return 
	 */
	Serializable update(Student student) throws Exception;

	/**
	 * * 根据主键ID删除 记录 * @param id 逐渐ID
	 */
	Serializable delete(Serializable id) throws Exception;

	/**
	 * * 获取记录 * @param id 主键ID* @return 记录
	 */
	Student get(Serializable id) throws Exception;

	/**
	 * *
	 *  获取记录列表
	 * * @return 记录列表
	 */
	List<Student> findAll() throws Exception;

}
