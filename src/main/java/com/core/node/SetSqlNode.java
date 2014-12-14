package com.core.node;

public class SetSqlNode extends ComplexSqlNode{

	public SetSqlNode(SqlNode... nodes) {
		super(", ", false, nodes);
	}
	
	@Override
	public String getSql() {
		if(size() == 0) return "";
		return "set ".concat(super.getSql());
	}
}
