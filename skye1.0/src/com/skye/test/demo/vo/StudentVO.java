package com.skye.test.demo.vo;

public class StudentVO {
	private String id;
	private String username;
	private String sex;
	private String age;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	@Override
	public String toString() {
		return "StudentVO [id=" + id + ", username=" + username + ", sex=" + sex + ", age=" + age + "]";
	}
	
}
