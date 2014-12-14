package com.core.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.ArrayHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.core.builder.BeanHandlerManager;
import com.core.context.DaoContext;
import com.core.entity.Entity;
import com.core.exception.DaoException;
import com.core.query.$;
import com.core.query.Insert;
import com.core.query.Query;
import com.core.query.SqlBuilder;
import com.core.query.Update;
import com.core.transaction.Transaction;
import com.core.util.BeanUtils;



public class EasyDao {
	
	private static Logger logger = LogManager.getLogger();
	
	/**
	 * 列出所有条目
	 * @param type
	 * @return
	 */
	public static final <T> List<T> listAll(Class<T> type) {
		return query($.query(type));
	}
	
	/**
	 * 通过主键查找对象
	 * @param type
	 * @param id
	 * @return
	 */
	public static final <T> T get(Class<T> type, Object id) {
		Entity entity = DaoContext.getEntity(type);
		return querySingle($.query(type).where($.eq(entity.id, id)));
	}
	
	/**
	 * 查询条目总数
	 * @param type
	 * @return
	 */
	public static final int count(Class<?> type) {
		return count($.query(type));
	}
	
	/**
	 * 分页, 从第一页开始
	 * @param type
	 * @param page
	 * @param number
	 * @return
	 */
	public static final <T> List<T> page(Class<T> type, int page, int number) {
		return query($.query(type).page(page, number));
	}
	
	/**
	 * 多结果查询
	 * @param builder 查询条件
	 * @return 查询结果列表
	 */
	public static final <T> List<T> query(Query<T> builder) {
		BeanListHandler<T> handler = BeanHandlerManager.getBeanListHandler(builder.getType());
		List<T> list = query(builder, handler);
		if(list == null) list = Collections.emptyList();
		return list;
	}
	
	/**
	 * 单结果查询
	 * @param builder 查询条件
	 * @return 第一个符合要求的查询结果
	 */
	public static final <T> T querySingle(Query<T> builder) {
		BeanHandler<T> handler = BeanHandlerManager.getBeanHandler(builder.getType());
		return query(builder, handler);
	}
	
	/**
	 * 查询项目条数
	 * @param builder 查询条件
	 * @return 项目条数
	 */
	public static final int count(Query<?> builder) {
		Object result = query(builder.count(), new ArrayHandler())[0];
		return convertInteger(result);
	}
	
	private static final int convertInteger(Object obj) {
		if(obj instanceof Long) {
			return (int)(long)obj;
		} else if(obj instanceof Integer) {
			return (int)obj;
		} else {
			return Integer.parseInt(obj.toString());
		}
	}
	
	private static final <T, E> E query(Query<T> builder, ResultSetHandler<E> handler) {
		logger.info(builder);
		String sql = builder.getSql();
		Object[] params = builder.getParam();

		try {
			// 有事务时取消自动关闭, 没有事务时自动关闭连接
			if(Transaction.hasTransaction()) {
				QueryRunner runner = new QueryRunner();
				return runner.query(DaoContext.getConnection(), sql, handler, params);
			} else {
				QueryRunner runner = new QueryRunner(DaoContext.getDataSource());
				return runner.query(sql, handler, params);
			}
		} catch (SQLException e) {
			throw new DaoException(e);
		}
	}
	
	
	/**
	 * 根据对象更新
	 * @param obj 要更新的对象
	 * @return 更新影响的行数
	 */
	public static int update(Object obj) {
		Entity entity = DaoContext.getEntity(obj.getClass());
		Update<?> builder = $.update(obj.getClass());
		Map<String, String> mapper = entity.getMapper();
		
		for(String field : mapper.values()) {
			Object value = BeanUtils.getProperty(obj, field,false);
			boolean isPrimaryKey = entity.getColumn(field).equals(entity.id);
			if(isPrimaryKey) {
				builder.where($.eq(entity.id, value));
			} else {
				builder.set(field, value);
			}
		}
		return update(builder);
	}
	
	/**
	 * 自定义更新(包括 delete, update)
	 * @param builder 更新条件
	 * @return 更新影响的行数
	 */
	public static final <T> int update(SqlBuilder<T> builder) {
		if(Query.class.isInstance(builder) || 
			Insert.class.isInstance(builder)) {
			throw new IllegalArgumentException("只能执行更新和删除操作!");
		}
		
		logger.info(builder);
		String sql = builder.getSql();
		Object[] params = builder.getParam();
		
		int rows = 0;
		
		try {
			// 有事务时取消自动关闭, 没有事务时自动关闭连接
			if(Transaction.hasTransaction()) {
				QueryRunner runner = new QueryRunner();
				rows = runner.update(DaoContext.getConnection(), sql, params);
			} else {
				QueryRunner runner = new QueryRunner(DaoContext.getDataSource());
				rows = runner.update(sql, params);
			}
		} catch (SQLException e) {
			throw new DaoException(e);
		}
		
		return rows;
	}
	
	/**
	 * 根据 ID 删除指定条目
	 * @param type
	 * @param id
	 * @return
	 */
	public static final <T> int delete(Class<T> type, Object id) {
		Entity entity = DaoContext.getEntity(type);
		return update($.delete(type).where($.eq(entity.id, id)));
	}
	
	/**
	 * 根据 ID 批量删除条目
	 * @param type
	 * @param ids
	 * @return
	 */
	public static final <T> int delete(Class<T> type, Object... ids) {
		Entity entity = DaoContext.getEntity(type);
		return update($.delete(type).where($.in(entity.id, ids)));
	}
	
	/**
	 * 保存指定对象, 保存后会自动更新对象的主键, 但默认值不会被自动填充
	 * @param objs 要保存的对象
	 * @return 受影响的行数
	 */
	@SuppressWarnings("unchecked")
	public static final <T> int save(T... objs) {
		// 对象列表为空时直接返回
		if(objs.length == 0) return 0;
		
		// 得到类描述对象
		Class<T> type = (Class<T>) objs[0].getClass();
		
		// 构建 SQL 语句, 及参数
		Insert<T> builder = $.insert(type).add(objs);
		String sql = builder.getSql();
		Object[] params = builder.getParam();
		
		logger.info(builder);
		
		// 开始执行插入操作
		int rows = 0;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			conn = DaoContext.getConnection();
			stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			QueryRunner runner = new QueryRunner();

			// 批量插入
			for(int i = 0; i < params.length; i++) {
				runner.fillStatement(stmt, (Object[])params[i]);
				stmt.addBatch();
			}
			
			// 执行并统计受影响的行数
			int[] effectedRows = stmt.executeBatch();
			for(int row : effectedRows) { rows += row; }
			
			// 获得自动生成的主键
			rs = stmt.getGeneratedKeys();
			
			// 获得类描述相关对象
			Entity entity = DaoContext.getEntity(type);
			String idField = entity.getField(entity.id);
			Class<?> idType = entity.getFiledType(idField);
			
			// 将主键赋值给每个对象
			for(int i = 0; rs.next(); i++) {
				// 可能为 Long 也可能为 Integer, 推荐使用 Long 做主键
				Object id = rs.getLong(1);
				if(!idType.isInstance(id)) {
					id = rs.getInt(1);
				}
				BeanUtils.setProperty(objs[i], idField, id, idType);
			}
		} catch (SQLException e) {
			throw new DaoException(e);
		} finally {
			// 存在事务时仅保留连接对象, 不存在事务时关闭所有对象
			if(Transaction.hasTransaction()) {
				DbUtils.closeQuietly(null, stmt, rs);
			} else {
				DbUtils.closeQuietly(conn, stmt, rs);
			}
		}
		return rows;
	}
}
