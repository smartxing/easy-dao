package com.core.query;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.core.context.DaoContext;
import com.core.entity.Entity;
import com.core.node.LimitSqlNode;
import com.core.node.SqlNode;
import com.core.util.StringUtils;

/**
 * 查询对象
 * 
 * @author Administrator
 * 
 */
public class Query<T> implements SqlNode {

	private String sql;
	private List<String> tables = new ArrayList<String>();
	private List<String> columns = new ArrayList<String>();
	private Object[] params;

	private SqlNode where;
	private SqlNode limit;
	private boolean count;

	private Class<T> type;
	private Entity entity;

	public Query(Class<T> type) {
		this.entity = DaoContext.getEntity(type);
		this.type = type;

	}

	public Class<T> getType() {
		return this.type;
	}

	public Query<T> tables(Class<?>... types) {
		for (Class<?> type : types) {
			Entity _entity = DaoContext.getEntity(type);
			tables.add(_entity.table);
		}
		return this;
	}

	public Query<T> columns(String... columns) {
		Collections.addAll(this.columns, columns);
		return this;
	}

	public Query<T> where(SqlNode root) {
		this.where = root;
		return this;
	}

	public Query<T> count() {
		this.count = true;
		return this;
	}

	public Query<T> limit(int offset, int number) {
		this.limit = new LimitSqlNode(offset, number);
		return this;
	}

	public Query<T> page(int page, int number) {
		int offset = (page - 1) * number;
		limit = new LimitSqlNode(offset, number);
		return this;
	}

	@Override
	public String getSql() {
		if(sql == null)
		{
			sql = String.format("select %s from %s %s %s",
					getColumns(),
					getTableSql(),
					getWhere(),
					limit != null ? "limit ?,?" :""
					);
		}
		return sql;
	}

	private String getColumns() {
		if (count)
			return " count(1) ";
		if (columns.isEmpty())
			return "*";
		StringBuilder sb = new StringBuilder();
		int count = 0;
		for (String col : columns) {
			if (count++ > 0)
				sb.append(",");
			sb.append(col);
		}
		return sb.toString();
	}

	private String getTableSql() {
		if (tables.isEmpty()) {
			return entity.table;
		} else {
			tables.add(entity.table);
			return StringUtils.join(tables.toArray(), ",");
		}
	}

	private String getWhere() {
		if (where == null)
			return "";
		if (where.getSql().isEmpty())
			return "";
		return "where ".concat(where.getSql());
	}

	@Override
	public Object[] getParam() {
		if (params == null) {
			ArrayList<Object> tmp = new ArrayList<Object>();
			if (where != null)
				Collections.addAll(tmp, where.getParam());
			if (limit != null)
				Collections.addAll(tmp, limit.getParam());
			params = tmp.toArray();
		}
		return params;
	}

	
	@Override
	public String toString() {
		return String.format("%s [%s]", getSql(), StringUtils.join(getParam(), ", "));
	}
	
}
