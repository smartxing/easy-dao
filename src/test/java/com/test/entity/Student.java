package com.test.entity;

import java.io.Serializable;

import com.core.annotition.Entity;
import com.core.annotition.Id;

@Entity
public class Student implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private int id;
	private String name;
	private float score;
	private int class_id;

	public Student() {
	}

	public Student(int id, String name, float score, int class_id) {
		this.id = id;
		this.name = name;
		this.score = score;
		this.class_id = class_id;
	}

	@Override
	public String toString() {
		return "Student [id=" + id + ", name=" + name + ", score=" + score
				+ ", class_id=" + class_id + "]";
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

	public float getScore() {
		return score;
	}

	public void setScore(float score) {
		this.score = score;
	}

	public int getClass_id() {
		return class_id;
	}

	public void setClass_id(int class_id) {
		this.class_id = class_id;
	}

}
