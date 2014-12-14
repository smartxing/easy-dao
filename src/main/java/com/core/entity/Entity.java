package com.core.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.core.util.BeanUtils;
import com.mchange.v2.beans.BeansUtils;

/**
 * 实体映射
 * @author Administrator
 *
 */
public class Entity {

	public String table;
	public String id;
	
	private ArrayList<String> columns = new ArrayList<String>();
	private ArrayList<String> fields = new ArrayList<String>();
	
	//保留表字段和实体字段的映射
	private final Map<String,String> mapper = new HashMap<String,String>();
	//反转实体字段和表字段
	private final Map<String,String> reverseMapper = new HashMap<String,String>();
	
	//保留字段和类型的映射
	private final Map<String,Class<?>> typeMapper = new HashMap<String,Class<?>>();
	
	public Entity(String table,String id)
	{
		this.table = table;
		this.id = id;
	}
	
	public String toString()
	{
		return "Entity [table=" + table + ", \nprimaryKey=" + id
				+ ", \nmapper=" + mapper + "]";
	}
	
	public void addMapper(String column,String field,Class<?> fieldType)
	{
		columns.add(column);
		fields.add(field);
		
		mapper.put(column, field);
		reverseMapper.put(field, column);
		typeMapper.put(field,fieldType);
	}
	
	public Map<String,String> getMapper()
	{
		return mapper;
	}
	
	public String getColumn(String filed)
	{
		return reverseMapper.get(filed);
	}
	
	public String getField(String column)
	{
		return mapper.get(column);
	}
	
	public Class<?> getFiledType(String filed)
	{
		return typeMapper.get(filed);
	}

	public Object getId(Object obj) {
		String field = reverseMapper.get(id);
		//id 必须不是boolean类型
		return BeanUtils.getProperty(obj, field, false);
	}
	
	public String[] getColumns() {
		return columns.toArray(new String[columns.size()]);
	}
	
	public String[] getFields() {
		return fields.toArray(new String[fields.size()]);
	}
}

