package com.test.query;

import org.junit.BeforeClass;
import org.junit.Test;

import com.core.context.DaoContext;
import com.core.query.$;
import com.core.query.Update;
import com.test.entity.Student;

public class UpdateTest {

	@Test
	public void testUpdate() throws Exception {
		Update<Student> builder = 
				$.update(Student.class)
					.set("name", "new name")
					.set("score", 77)
					.where($.eq("id", 5));
		System.out.println(builder);
	}
	
	@BeforeClass
	public static void beforeClass() {
		DaoContext.register(Student.class);
	}
}
