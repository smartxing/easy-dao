# easy-dao
轻量级jdbc实现，增删查改，

```java
@Test
    public void testQuery() throws Exception {
        String username = "test";
        String password = "test";
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
    }
```
    
    @Test
    public void test2() {
        String username = "test";
        String password = "test";
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
    }
2 添加操作 

    @Test
    public void testInsertBuilder() throws Exception {
        Insert<Student> builder = $.insert(Student.class).add(
            new Student(0, "stu 1", 44, 2),
            new Student(0, "stu 3", 64, 3)
        );
    }
3 删除操作

    @Test
    public void testDeleteBuilder() throws Exception {
        Delete<Student> builder = $.delete(Student.class).where($.eq("id", 47));
        System.out.println(builder);
    }
4 更新操作

    @Test
    public void testUpdate() throws Exception {
        Update<Student> builder = 
                $.update(Student.class)
                    .set("name", "new name")
                    .set("score", 77)
                    .where($.eq("id", 5));
        System.out.println(builder);
    }


