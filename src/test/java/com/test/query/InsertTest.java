package com.test.query;

import org.junit.BeforeClass;
import org.junit.Test;

import com.core.context.DaoContext;
import com.core.query.$;
import com.core.query.Insert;
import com.test.entity.Student;

public class InsertTest {

	@Test
	public void testInsertBuilder() throws Exception {
		Insert<Student> builder = $.insert(Student.class).add(
			new Student(0, "stu 1", 44, 2),
			new Student(0, "stu 3", 64, 3)
		);
		System.out.println(builder);
	}
	
	@BeforeClass
	public static void beforeClass() {
		DaoContext.register(Student.class);
	}
	
}
