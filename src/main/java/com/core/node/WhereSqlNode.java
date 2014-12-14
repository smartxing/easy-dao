package com.core.node;

public class WhereSqlNode extends ComplexSqlNode {

	public WhereSqlNode() {
		super("", false);
	}
	
	public WhereSqlNode(SqlNode node) {
		super("", false, node);
	}
	
	@Override
	public void append(SqlNode node)
	{
		if(size()>0)
		{
			throw new IllegalStateException("where 子句中最多只能有一个节点!");
		}
		super.append(node);
	}
	
	public String getSql()
	{
		return size() == 0 ? "":"where ".concat(super.getSql());
	}
}
