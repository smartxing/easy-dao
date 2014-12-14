package com.test.entity;

import java.io.Serializable;

import com.core.annotition.Column;
import com.core.annotition.Entity;
import com.core.annotition.Id;

@Entity("_person")
public class Person implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column("_id")
	private int id;
	@Column("_name")
	private String name;
	private int age;

	public Person() {
	}

	public Person(int id, String name, int age) {
		this.id = id;
		this.name = name;
		this.age = age;
	}

	@Override
	public String toString() {
		return "Person [id=" + id + ", name=" + name + ", age=" + age + "]";
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

}
