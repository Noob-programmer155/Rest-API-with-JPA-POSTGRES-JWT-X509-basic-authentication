package com.amrTm.restApiJpaJwtX509Authentication.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
public class TeacherLesson {
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
	@JoinTable(name="Lesson_Teacher", joinColumns= {@JoinColumn(name="Lesson_Id")}, inverseJoinColumns = {@JoinColumn(name="Teacher_Id")})
	private Set<Teacher> teacherLesson;
	public TeacherLesson() {
		super();
		this.teacherLesson = new HashSet<>();
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
	public Set<Teacher> getTeacherLesson() {
		return teacherLesson;
	}
	public void setTeacherLesson(Set<Teacher> teacherLesson) {
		this.teacherLesson = teacherLesson;
	}
}
