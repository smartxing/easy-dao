package com.core.query;

import java.util.ArrayList;
import java.util.Collections;

import com.core.util.BeanUtils;
import com.core.util.StringUtils;

public class Insert<T> extends SqlBuilder<T> {

	private ArrayList<T> objs = new ArrayList<T>();
	private String[] columns;
	private String[] fields;

	protected Insert(Class<T> type) {
		super(type);
		this.fields = entity.getFields();
		String[] _columns = entity.getColumns();
		this.columns = new String[_columns.length];
		for (int i = 0; i < _columns.length; i++) {
			this.columns[i] = String.format("%s", _columns[i]);
		}
	}
	
	public Insert<T> add(T...obj){
		Collections.addAll(objs, obj);
		return this;
	}

	@Override
	protected String onGetSql() {
		String sqlColumns = StringUtils.join(columns, ",");
		String sqlValues = StringUtils.repeat("?", ",", columns.length);
		return String.format("insert into %s (%s) values (%s)", 
				entity.table, sqlColumns, sqlValues); 
	}

	@Override
	protected Object[] onGetParams() {
		int rows = objs.size();
		int cols = fields.length;
		Object[][] params = new Object[rows][cols];
		for(int i = 0; i < rows; i++) {
			for(int j = 0; j < cols; j++) {
				Object obj = objs.get(i);
				String field = fields[j];
				params[i][j] = BeanUtils.getProperty(obj, field,false);
			}
		}
		return params;
	}
	
}
