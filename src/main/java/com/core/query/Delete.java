package com.core.query;

import com.core.node.SqlNode;
import com.core.node.WhereSqlNode;

public class Delete<T> extends SqlBuilder<T> {

	protected Delete(Class<T> type) {
		super(type);
	}

	
	public Delete<T> where(SqlNode node)
	{
		root.append(new WhereSqlNode(node));
		return this;
	}
	
	@Override
	protected String onGetSql() {
		return String.format("delete from %s ", entity.table).concat(root.getSql());
	}

	@Override
	protected Object[] onGetParams() {
		return root.getParam();
	}
}
