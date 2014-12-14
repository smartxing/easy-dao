package com.core.node;

import com.core.query.Query;
import com.core.util.StringUtils;

public class InSqlNode implements SqlNode{
	
	private String column;
	private Object[] params;
	private Query<?> query;
	
	public InSqlNode(String column,Object ...params)
	{
		this.column = column;
		this.params = params;
	}
	
	public InSqlNode(String column,Query<?> query)
	{
		this.column = column;
		this.query = query;
		this.params = query.getParam();
	}
	
	
	
	@Override
	public String getSql() {
		if(null == query)
		{
			String str = StringUtils.repeat("?",",",params.length);
			return String.format("%s in (%s)",column,str );
		}
		else
		{
			return String.format("%s in (%s)", column,query.getSql());
		}
	}
	
	
	@Override
	public Object[] getParam() {
		return params;
	}
	
	

}
