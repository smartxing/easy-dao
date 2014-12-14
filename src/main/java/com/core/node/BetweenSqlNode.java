package com.core.node;

public class BetweenSqlNode implements SqlNode{
	
	private Object[] params;
	private String column;
	
	public BetweenSqlNode(String column,Object from,Object to)
	{
		this.column = column;
		params = new Object[]{from,to};
	}

	@Override
	public String getSql() {
		return String.format("%s betweenn ? and ?", column);
	}

	@Override
	public Object[] getParam() {
		return params;
	}

	
	
}
