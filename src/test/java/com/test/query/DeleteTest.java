package com.test.query;

import org.junit.BeforeClass;
import org.junit.Test;

import com.core.context.DaoContext;
import com.core.query.$;
import com.core.query.Delete;
import com.test.entity.Student;

public class DeleteTest {

	@Test
	public void testDeleteBuilder() throws Exception {
		Delete<Student> builder = $.delete(Student.class).where($.eq("id", 47));
		System.out.println(builder);
	}
	
	@BeforeClass
	public static void beforeClass() {
		DaoContext.register(Student.class);
	}
}
