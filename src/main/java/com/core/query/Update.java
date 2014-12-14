package com.core.query;

import com.core.node.SetSqlNode;
import com.core.node.SimpleSqlNode;
import com.core.node.SqlNode;
import com.core.node.WhereSqlNode;
import com.core.util.StringUtils;

public class Update<T> extends SqlBuilder<T>{

	private SetSqlNode setNode = new SetSqlNode();
	private WhereSqlNode whereNode = new WhereSqlNode();
	
	protected Update(Class<T> type) {
		super(type);
		root.append(setNode);
		root.append(whereNode);
	
	}
	
	public Update<T> set(String field,Object value)
	{
		String column = entity.getColumn(field);
		SimpleSqlNode node =new SimpleSqlNode("=", column, value);
		setNode.append(node);
		return this;
	}
	public Update<T> where(SqlNode root)
	{
		whereNode.append(root);
		return this;
	}

	@Override
	protected String onGetSql() {
		return String.format("update `%s` ", entity.table).concat(root.getSql());
	}

	@Override
	protected Object[] onGetParams() {
		return root.getParam();
	}

	public String toString() {
		return String.format("%s [%s]", getSql(), StringUtils.join(getParam(), ", "));
	}
}
