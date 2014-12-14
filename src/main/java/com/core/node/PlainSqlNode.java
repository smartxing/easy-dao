package com.core.node;

public class PlainSqlNode implements SqlNode{
	
	private String sql;
	private Object[] params;
	
	public PlainSqlNode(String sql, Object... params) {
		this.sql = sql;
		this.params = params;
	}
	
	@Override
	public String getSql() {
		return sql;
	}

	@Override
	public Object[] getParam() {
		return params;
	}

}
