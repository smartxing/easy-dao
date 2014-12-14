package com.test.query;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.sql.Types;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Test;

import com.core.context.DaoContext;
import com.core.dao.EasyDao;
import com.core.exception.DaoException;
import com.core.query.$;
import com.core.transaction.Transaction;
import com.test.entity.Student;

public class EasyDaoTest {

	private static Logger logger = LogManager.getLogger();
	
	@Test
	public void testGet() throws Exception {
		Student student = EasyDao.get(Student.class, 5);
		logger.debug(student);
	}

	@Test
	public void testListAll() throws Exception {
		List<Student> students = EasyDao.listAll(Student.class);
		for (Student student : students) {
			System.out.println(student);
		}
	}

	@Test
	public void testQuery() throws Exception {
		List<Student> students = EasyDao.query($.query(Student.class).where(
				$.between("score", 60, 80)));
		for (Student student : students) {
			System.out.println(student);
		}
	}

	@Test
	public void testCount() throws Exception {
		int num = EasyDao.count($.query(Student.class).where(
				$.between("score", 60, 80)));
		System.out.println(num);
	}

	@Test
	public void testLimit() throws Exception {
		List<Student> students = EasyDao.query($.query(Student.class)
				.where($.between("score", 60, 80)).limit(2, 5));
		for (Student student : students) {
			System.out.println(student);
		}
	}

	@Test
	public void testPage() throws Exception {
		int limit = 10;
		int maxPage = (int) Math.ceil(EasyDao.count(Student.class) / 10f);

		for (int i = 1; i <= maxPage; i++) {
			List<Student> students = EasyDao.query($.query(Student.class)
					.page(i, limit));
			System.out.println("----------第" + i + "页----------");
			for (Student student : students) {
				System.out.println(student);
			}
		}
	}

	@Test
	public void testUpdate() throws Exception {
		int num = EasyDao.update($.update(Student.class).set("name", "student")
				.where($.eq("id", 1)));
		System.out.println(num);
		Student student = EasyDao.get(Student.class, 1);
		System.out.println(student);
	}

	@Test
	public void testUpdateObject() throws Exception {
		Student student = EasyDao.get(Student.class, 1);
		student.setName("student no.1");
		EasyDao.update(student);
		System.out.println(EasyDao.get(Student.class, 1));
	}

	@Test
	public void testDelete() throws Exception {
		int num = EasyDao.update($.delete(Student.class).where($.eq("id", 47)));
		System.out.println(num);
	}

	@Test
	public void testInsert() throws Exception {
		Student[] students = new Student[] { new Student(0, "stu 1", 33, 2),
				new Student(0, "stu 2", 22, 1) };
		int num = EasyDao.save(students);
		// int num = EasyDao.update($.insert(Student.class).add(students));
		System.out.println(num);
		System.out.println(Arrays.deepToString(students));
	}

	@Test
	public void testInsertObject() throws Exception {
		int num = EasyDao.save(new Student(0, "stu 11", 33, 2), new Student(0,
				"stu 22", 22, 1));
		System.out.println(num);
	}

	@Test
	public void testTransaction() throws Exception {
		Student[] students = new Student[5];
		for (int i = 0; i < students.length; i++) {
			students[i] = new Student(0, "stu " + i * 5, 555, 1);
		}

		Transaction.begin();
		try {
			EasyDao.save(students[0], students[1]);
			EasyDao.update($.update(Student.class).set("xxx", "xxx"));
			EasyDao.save(students[2], students[3]);
			Transaction.commit();
		} catch (DaoException e) {
			Transaction.rollback();
			System.err.println(e.getMessage());
		}
	}

	@Test
	public void testAutoGenerate() throws Exception {
		String sql = "insert into `test_table` (`id`, `time`, `number`, `info`) values (?,?,?,?)";
		Connection conn = DaoContext.getConnection();
		PreparedStatement stmt = conn.prepareStatement(sql,
				Statement.RETURN_GENERATED_KEYS);

		for (int i = 0; i < 2; i++) {
			stmt.setNull(1, Types.INTEGER);
			stmt.setNull(2, Types.TIMESTAMP);
			stmt.setInt(3, 0);
			stmt.setString(4, "This is info");
			stmt.addBatch();
		}
		stmt.executeBatch();
		// int rows = stmt.executeUpdate();
		// System.out.println("effect rows: " + rows);

		ResultSet rs = stmt.getGeneratedKeys();
		ResultSetMetaData meta = rs.getMetaData();

		for (int index = 0; rs.next(); index++) {
			System.out.printf("========= row: %s =========\n", index);
			for (int i = 1; i <= meta.getColumnCount(); i++) {
				System.out.printf("-----------column %s----------\n", i);
				System.out.printf("column name: %s\n", meta.getColumnName(i));
				System.out.printf("column class name: %s\n", meta.getColumnClassName(i));
				System.out.printf("column label: %s\n", meta.getColumnLabel(i));
				System.out.printf("column type: %s\n", meta.getColumnType(i));
				System.out.printf("column type name: %s\n", meta.getColumnTypeName(i));
				System.out.printf("value: %s\n", rs.getObject(i));
			}
		}
	}

	@Test
	public void testInsertSingle() throws Exception {
		Student student = new Student(0, "new_stu", 33.3f, 1);
		int row = EasyDao.save(student);
		System.out.println("row: " + row);
		System.out.println(student);
	}

	@Test
	public void testDefaultValue() throws Exception {
		Connection conn = DaoContext.getConnection();
		String TableName = "test_table";
		String ColumnName = "time";

		String columnDefaultVal = "";
		DatabaseMetaData md = conn.getMetaData();
		ResultSet rs = md.getColumns(conn.getCatalog(), md.getUserName(), TableName, ColumnName);

		if (rs.next()) {
			columnDefaultVal = rs.getString("COLUMN_DEF");
		}

		System.out.println("Default Value of Column is " + columnDefaultVal);
	}
	
	@Test
	public void testSaveAll() throws Exception {
		Student[] students = new Student[5];
		for(int i = 0; i < students.length; i++) {
			students[i] = new Student(0, "new_test_stu_" + i, 44f, 1);
		}
//		students[4].setClass_id(100); 
		
		int rows = 0;
		
		Transaction.begin();
		try {
			rows = EasyDao.save(students);
			Transaction.commit();
		} catch (Exception e) {
			Transaction.rollback();
			e.printStackTrace();
		}
		
		System.out.println("effected rows: " + rows);
		System.out.println(Arrays.deepToString(students));
	}

	@BeforeClass
	public static void beforeClass() {
		BasicDataSource ds = new BasicDataSource();
		ds.setDriverClassName("com.mysql.jdbc.Driver");
		ds.setUsername("root");
		ds.setPassword("kota");
		ds.setUrl("jdbc:mysql://127.0.0.1:3306/test");
		DaoContext.init(ds);
		DaoContext.register(Student.class);
	}
}
