package com.core.query;

import java.util.Arrays;

import com.core.context.DaoContext;
import com.core.entity.Entity;
import com.core.node.ComplexSqlNode;
import com.core.node.SqlNode;

public abstract class SqlBuilder<T> implements SqlNode {
	
	protected ComplexSqlNode root = new ComplexSqlNode("");
	protected Entity entity;

	private String sql;
	private Object[] params;
	
	protected SqlBuilder(Class<T> type)
	{
		this.entity = DaoContext.getEntity(type);
	}
	
	@Override
	public String toString() {
		return String.format("%s %s", getSql(), Arrays.deepToString(getParam()));
	}
	
	@Override
	public String getSql() {
		if(sql == null)
		{
			sql = onGetSql();
		}
		return sql;
	}

	@Override
	public Object[] getParam() {
		if(params == null)
		 params = onGetParams();
		return params;
	}

	protected abstract String onGetSql();
	protected abstract Object[] onGetParams();
}
