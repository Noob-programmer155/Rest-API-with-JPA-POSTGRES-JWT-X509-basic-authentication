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
public class TeacherLesson {
	@Id
	@NotNull
	@GeneratedValue
	private Long id;
	@NotBlank(message="code lesson must be included")
	@Column(unique=true)
	private String codeLesson;
	@NotBlank(message="type lesson must be included")
	private String typeLesson;
	@NotBlank(message="lesson must be included")
	private String lesson;
	@ManyToOne
	@JoinTable(name="Lesson_Teacher", joinColumns= {@JoinColumn(name="Lesson_Id")}, inverseJoinColumns = {@JoinColumn(name="Teacher_Id")})
	private Teacher teacherLesson;
	public TeacherLesson() {
		super();
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
	public String getTypeLesson() {
		return typeLesson;
	}
	public void setTypeLesson(String typeLesson) {
		this.typeLesson = typeLesson;
	}
	public String getLesson() {
		return lesson;
	}
	public void setLesson(String lesson) {
		this.lesson = lesson;
	}
	public Teacher getTeacherLesson() {
		return teacherLesson;
	}
	public void setTeacherLesson(Teacher teacherLesson) {
		this.teacherLesson = teacherLesson;
	}
}
