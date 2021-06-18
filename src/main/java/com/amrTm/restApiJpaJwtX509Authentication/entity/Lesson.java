package com.amrTm.restApiJpaJwtX509Authentication.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@JsonIdentityInfo(
		generator = ObjectIdGenerators.PropertyGenerator.class,
		property = "codeLesson")
public class Lesson {
	@Id
	@Column(name="CODE_LESSON",unique=true,nullable=false)
	private String codeLesson;
	@NotBlank(message="type lesson must be included")
	private String typeLesson;
	@NotBlank(message="lesson must be included")
	private String lesson;
	@ManyToMany(mappedBy="studentLes")
	private Set<Student> studentLesson;
	@ManyToMany(mappedBy="teacherLes")
	private Set<Teacher> teacherLesson;
	public Lesson() {
		super();
		this.studentLesson = new HashSet<>();
		this.teacherLesson = new HashSet<>();
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
	public void addLesson(Student student) {
		if(this.studentLesson.contains(student)) {return ;}
		this.studentLesson.add(student);
		student.addLesson(this);
	}
	public void removeLesson(Student student) {
		if(!this.studentLesson.contains(student)) {return ;}
		this.studentLesson.remove(student);
		student.removeLesson(this);
	}
	public void addLesson(Teacher teacher) {
		if(this.teacherLesson.contains(teacher)) {return ;}
		this.teacherLesson.add(teacher);
		teacher.addLesson(this);
	}
	public void removeLesson(Teacher teacher) {
		if(!this.teacherLesson.contains(teacher)) {return ;}
		this.teacherLesson.remove(teacher);
		teacher.removeLesson(this);
	}
	public Set<Student> getStudentLesson() {
		return studentLesson;
	}
	public void setStudentLesson(Set<Student> studentLesson) {
		this.studentLesson = studentLesson;
	}
	public Set<Teacher> getTeacherLesson() {
		return teacherLesson;
	}
	public void setTeacherLesson(Set<Teacher> teacherLesson) {
		this.teacherLesson = teacherLesson;
	}
}
