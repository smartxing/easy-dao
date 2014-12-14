package com.core.transaction;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbutils.DbUtils;

import com.core.context.DaoContext;

public class Transaction {

	private static ThreadLocal<Transaction> transaction = new ThreadLocal<>();
	private Connection connection;
	
	public Transaction(Connection connection) {
		this.connection = connection;
	}

	/**
	 * 开启事务处理
	 */
	public static void begin() {
		Connection connection = DaoContext.getConnection();
		Transaction transaction = new Transaction(connection);
		transaction.beginTransaction();
	}
	
	/**
	 * 提交事务
	 */
	public static void commit() {
		Transaction transaction = Transaction.transaction.get();
		transaction.commitTransaction();
	}
	
	/**
	 * 回滚事务
	 */
	public static void rollback() {
		Transaction transaction = Transaction.transaction.get();
		transaction.rollbackTransaction();
	}
	
	/**
	 * 当前是否有事务
	 * @return 是否在事务处理中
	 */
	public static boolean hasTransaction() {
		return Transaction.transaction.get() != null;
	}
	
	public void beginTransaction() {
		try {
			connection.setAutoCommit(false);
			transaction.set(this);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void commitTransaction() {
		try {
			DbUtils.commitAndClose(connection);
			transaction.remove();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void rollbackTransaction() {
		try {
			DbUtils.rollbackAndClose(connection);
			transaction.remove();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
