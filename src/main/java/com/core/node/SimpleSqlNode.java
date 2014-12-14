package com.core.node;

/**
 * 保留简单的节点
 * 
 * @author Administrator
 * 
 */
public class SimpleSqlNode implements SqlNode {

	// = > < like not like !=
	private SqlNode node;
	private String column;
	private String operation;
	private Object[] params;

	public SimpleSqlNode(String operation,String column,Object param)
	{	//子查询
		if(param instanceof SqlNode)
		{
			node = (SqlNode)param;
			params = node.getParam();
		}else
		{
			this.params = new Object[]{param};
		}
		this.column = column;
		this.operation = operation;
		
	}

	@Override
	public String getSql() {
		if(node != null)
		{ //产生子查询 (A = (select * from ...))
			return String.format("%s %s (%s)", column,operation,node.getSql());
		}
		//产生 A = ? 参数 B
		return String.format("%s %s ?", column,operation);
	}

	@Override
	public Object[] getParam() {
		return params;
	}

}
