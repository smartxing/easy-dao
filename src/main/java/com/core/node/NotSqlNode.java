package com.core.node;

public class NotSqlNode implements SqlNode {

	private SqlNode node;
	
	public NotSqlNode(SqlNode node) {
		this.node = node;
	}
	
	@Override
	public String getSql() {
		return String.format("!(%s)", node.getSql());
	}

	@Override
	public Object[] getParam() {
		return node.getParam();
	}

}
