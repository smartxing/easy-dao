package com.core.context;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import com.core.annotition.Column;
import com.core.annotition.Id;
import com.core.entity.Entity;
import com.core.util.StringUtils;

public class DaoContext {

	private static DataSource dataSource;
	private static ThreadLocal<Connection> connection;
	private static Map<Class<?>, Entity> mapper = new HashMap<Class<?>, Entity>();

	public static void init(final DataSource dataSource) {
		DaoContext.dataSource = dataSource;
		connection = new LocalConnection(dataSource);

	}

	public static Connection getConnection() {
		check();
		boolean isColose;
		try {
			isColose = connection.get().isClosed();
			if (isColose) {
				connection.remove();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return connection.get();

	}

	public static DataSource getDataSource() {
		check();
		return dataSource;
	}

	public static void register(Class<?> type) {

		com.core.annotition.Entity _entity = type
				.getAnnotation(com.core.annotition.Entity.class);
		if (_entity == null) {
			throw new IllegalArgumentException(type.getName() + "是否标识@Entity");
		}
		String table = _entity.value();
		if (StringUtils.isEmpty(table)) {
			String tmp = StringUtils.firstLower(type.getSimpleName());
			table = StringUtils.convertField(tmp);
		}
		// 初始化属性名以及对应的列名称

		Field[] _fields = type.getDeclaredFields();
		int len = _fields.length;

		// field 字段 列 类型
		String[] fields = new String[len];
		String[] columns = new String[len];
		Class<?>[] types = new Class<?>[len];
		String id = null;
		for (int i = 0; i < len; i++) {
			Field field = _fields[i];
			if (isNeedSkip(field))
				continue;
			Column aColumn = field.getAnnotation(Column.class);
			fields[i] = field.getName();
			columns[i] = aColumn == null ? StringUtils.convertField(field
					.getName()) : aColumn.value();
			types[i] = field.getType();

			Id aId = field.getAnnotation(Id.class);
			if (aId != null)
				id = columns[i];

		}

		Entity entity = new Entity(table, id);
		for (int i = 0; i < len; i++) {
			if (columns[i] == null)
				continue;
			entity.addMapper(columns[i], fields[i], types[i]);
		}
		mapper.put(type, entity);
	}

	public static final void check() {
		if (dataSource == null) {
			throw new IllegalStateException("DataSource 未初始化");
		}
	}

	private static boolean isNeedSkip(Field field) {
		int modifiers = field.getModifiers();
		return Modifier.isStatic(modifiers) || Modifier.isTransient(modifiers);
	}

	public static Entity getEntity(Class<?> type) {
		Entity entity = mapper.get(type);
		if (null == entity) {
			throw new IllegalArgumentException("未注册的类型: " + type.getName());
		}
		return entity;
	}

	public static class LocalConnection extends ThreadLocal<Connection> {

		private DataSource dataSource;

		public LocalConnection(DataSource dataSource) {
			this.dataSource = dataSource;
		}

		@Override
		protected Connection initialValue() {
			try {
				return dataSource.getConnection();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return null;
		}

	}
}
