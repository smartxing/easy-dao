package com.core.query;

import com.core.node.BetweenSqlNode;
import com.core.node.ComplexSqlNode;
import com.core.node.InSqlNode;
import com.core.node.NotSqlNode;
import com.core.node.PlainSqlNode;
import com.core.node.SimpleSqlNode;
import com.core.node.SqlNode;

public class $ {

	public static <T> Query<T> query(Class<T> type) {
		return new Query<T>(type);
	}

	public static <T> Update<T> update(Class<T> type) {
		return new Update<T>(type);
	}

	public static <T> Delete<T> delete(Class<T> type) {
		return new Delete<T>(type);
	}

	public static <T> Insert<T> insert(Class<T> type) {
		return new Insert<T>(type);
	}

	public static SqlNode eq(String column, Object value) {
		return new SimpleSqlNode(" = ", column, value);
	}

	public static SqlNode neq(String column, Object value) {
		return new SimpleSqlNode(" != ", column, value);
	}

	public static SqlNode gt(String column, Object value) {
		return new SimpleSqlNode(" > ", column, value);
	}

	public static SqlNode lt(String column, Object value) {
		return new SimpleSqlNode(" < ", column, value);
	}

	public static SqlNode nlt(String column, Object value) {
		return new SimpleSqlNode(" >= ", column, value);
	}

	public static SqlNode ngt(String column, Object value) {
		return new SimpleSqlNode(" <= ", column, value);
	}

	public static SqlNode between(String column, Object from, Object to) {
		return new BetweenSqlNode(column, from, to);
	}

	public static SqlNode in(String column, Object... values) {
		return new InSqlNode(column, values);
	}

	public static SqlNode in(String column, Query<?> builder) {
		return new InSqlNode(column, builder);
	}

	public static SqlNode and(SqlNode... nodes) {
		return new ComplexSqlNode(" and ", nodes);
	}

	public static SqlNode or(SqlNode... nodes) {
		return new ComplexSqlNode(" or ", nodes);
	}

	public static SqlNode not(SqlNode node) {
		return new NotSqlNode(node);
	}

	public static SqlNode node(String sql, Object... params) {
		return new PlainSqlNode(sql, params);
	}

}
