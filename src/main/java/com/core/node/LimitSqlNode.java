package com.core.node;

public class LimitSqlNode  implements SqlNode{

	private Object[] params;
	
	public LimitSqlNode(int offset, int number)
	{
		params = new Object[]{offset,number};
	}

	@Override
	public String getSql() {
		return "limit ?,?";
	}

	@Override
	public Object[] getParam() {
		return params;
	}
}
