package com.amrTm.restApiJpaJwtX509Authentication.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
public class StudentLesson {
	@Id
	@NotNull
	@GeneratedValue
	private Long id;
	@NotBlank(message="code lesson must be included")
	private String codeLesson;
	@NotBlank(message="type lesson must be included")
	private String typeLesson;
	@NotBlank(message="lesson must be included")
	private String lesson;
	@ManyToMany(cascade= {CascadeType.MERGE})
	@JoinTable(name="Lesson_Student", joinColumns= {@JoinColumn(name="Student_Id")}, inverseJoinColumns = {@JoinColumn(name="Lesson_Id")})
	private Set<Student> studentLesson;
	public StudentLesson() {
		super();
		this.studentLesson = new HashSet<>();
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
	public Set<Student> getStudents() {
		return studentLesson;
	}
	public void setStudents(Set<Student> students) {
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
