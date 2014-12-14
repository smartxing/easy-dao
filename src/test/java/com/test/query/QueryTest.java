package com.test.query;

import org.junit.BeforeClass;
import org.junit.Test;

import com.core.context.DaoContext;
import com.core.query.$;
import com.core.query.Query;
import com.test.entity.Person;
import com.test.entity.Student;

public class QueryTest {
	
	@Test
	public void testQuery() throws Exception {
		String username = "gota";
		String password = "kota";
		Query<Person> builder = $.query(Person.class).where( 
			$.and(
				$.eq("username", username), 
				$.eq("password", password),
				$.or(
					$.gt("age", 23),
					$.between("length", 175, 180),
					$.in("sex", "男", "女")
				)
			)
		);
		
		System.out.println(builder.toString());
	}
	
	@Test
	public void test2() {
		String username = "gota";
		String password = "kota";
		Query<Person> builder = $.query(Person.class).tables(Student.class).where( 
			$.and(
				$.eq("username", username), 
				$.eq("password", password),
				$.or(
					$.gt("age", 23),
					$.between("length", 175, 180),
					$.in("sex", "男", "女")
				)
			)
		);
		System.out.println(builder.toString());
	}
	
	@Test
	public void test3() {
		String username = "tt";
		
		Query<Person> builder = $.query(Person.class).where(
			$.and(
				$.eq("mid", 
					$.query(Person.class).columns("member.id").where($.eq("username", username))
				),
				$.in("id", 
					$.query(Person.class).columns("person_id").where(
						$.and(
							$.and(
								$.gt("sdate", "2003-1-1"),
								$.lt("edate", "2003-1-5")
							),
							$.eq("rid", $.node("select room.id from room inner join category on category.name = ?", username))
						)
					)
				)
			)
		);
		
		String sql = builder.toString();
		System.out.println(sql);
	}
	
	@BeforeClass
	public static void beforeClass() {
		DaoContext.register(Person.class);
		DaoContext.register(Student.class);
	}
	
}
