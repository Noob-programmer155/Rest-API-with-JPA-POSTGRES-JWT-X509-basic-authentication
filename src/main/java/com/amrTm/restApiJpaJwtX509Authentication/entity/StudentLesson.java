package com.amrTm.restApiJpaJwtX509Authentication.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
public class StudentLesson {
	@Id
	@NotBlank(message="code lesson must be included")
	@Column(unique=true)
	private String codeLesson;
	@NotBlank(message="type lesson must be included")
	private String typeLesson;
	@NotBlank(message="lesson must be included")
	private String lesson;
	@ManyToOne
	@JoinTable(name="Lesson_Student", joinColumns= {@JoinColumn(name="Student_Id")}, inverseJoinColumns = {@JoinColumn(name="Lesson_Id")})
	private Student studentLesson;
	public StudentLesson() {
		super();
	}
	public String getCodeLesson() {
		return codeLesson;
	}
	public void setCodeLesson(String codeLesson) {
		this.codeLesson = codeLesson;
	}
	public String getLesson() {
		return lesson;
	}
	public void setLesson(String lesson) {
		this.lesson = lesson;
	}
	public String getTypeLesson() {
		return typeLesson;
	}
	public void setTypeLesson(String typeLesson) {
		this.typeLesson = typeLesson;
	}
	public Student getStudents() {
		return studentLesson;
	}
	public void setStudents(Student students) {
		this.studentLesson = students;
	}
//	public void addLesson(Student student) {
//		if(this.studentLesson.contains(student)) {return ;}
//		this.studentLesson.add(student);
//		student.addLesson(this);
//	}
//	public void removeLesson(Student student) {
//		if(!this.studentLesson.contains(student)) {return ;}
//		this.studentLesson.remove(student);
//		student.removeLesson(this);
//	}
//	public void addLesson(Teacher teacher) {
//		if(this.tLesson.contains(teacher)) {return ;}
//		this.tLesson.add(teacher);
//		teacher.addLesson(this);
//	}
//	public void removeLesson(Teacher teacher) {
//		if(!this.tLesson.contains(teacher)) {return ;}
//		this.tLesson.remove(teacher);
//		teacher.removeLesson(this);
//	}
}
