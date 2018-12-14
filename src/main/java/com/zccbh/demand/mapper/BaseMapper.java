package com.zccbh.demand.mapper;

import java.util.List;
import java.util.Map;

public interface BaseMapper<T> {

	/**
	 * @Comments: 添加单条数据
	 * @throws Exception
	 */
	int saveSingle(Map<String, Object> map) throws Exception;
	int saveEntitySingle(T t) throws Exception;

	/**
	 * @Comments: 添加多条数据
	 * @throws Exception
	 */
	int saveMore(List<Map<String, Object>> listMap) throws Exception;
	int saveEntityMore(List<T> list) throws Exception;

	/**
	 * @Comments: 删除
	 */
	int deleteModel(Integer id) throws Exception;

	/**
	 * @Comments: 修改
	 * @throws Exception
	 */
	int updateModel(Map<String, Object> map) throws Exception;
	int updateEntityModel(T t) throws Exception;

	/**
	 * @Comments: 查询单条数据
	 */
	Map<String,Object> findSingle(Map<String, Object> map) throws Exception;
	T findEntitySingle(Map<String, Object> map) throws Exception;

	/**
	 * @Comments: 查询多条数据
	 * @throws Exception
	 */
	List<Map<String, Object>> findMore(Map<String, Object> map) throws Exception;
	List<T> findEntityMore(Map<String, Object> map) throws Exception;
}
