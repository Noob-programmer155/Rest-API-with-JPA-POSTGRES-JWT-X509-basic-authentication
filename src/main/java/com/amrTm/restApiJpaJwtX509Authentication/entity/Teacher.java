package com.amrTm.restApiJpaJwtX509Authentication.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.MapKeyJoinColumn;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Entity
public class Teacher {
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	private Long id;
	@NotBlank(message="username must be included")
	private String username;
	@Enumerated
	@NotBlank(message="gender must be included")
	private GenderType gender;
	@NotBlank(message="teacher code must be included")
	private String codeTeacher;
	@NotBlank(message="email must be included")
	@Email(message="Must be a valid email", regexp="{email}")
	private String email;
	@ElementCollection
	@CollectionTable(name="LESSONS",joinColumns= {@JoinColumn(name="TEACHER_ID")})
	@MapKeyJoinColumn(name="LESSON_CODE")
	private Map<String,Lesson> lesson;
	@ElementCollection
	@Column(name="HOUR_OF_ARRIVALS", columnDefinition="DATE")
	private List<LocalDateTime> arrival;
	@ManyToMany
	@JoinTable(name="SCHOOL_MEMBERS", joinColumns= {@JoinColumn(name="TEACHER_ID")}, inverseJoinColumns= {@JoinColumn(name="STUDENT_ID")})
	private Set<Student> students;
	public Teacher() {
		super();
		this.lesson = new HashMap<>();
		this.arrival = new ArrayList<>();
		this.students = new HashSet<>();
		// TODO Auto-generated constructor stub
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getCodeTeacher() {
		return codeTeacher;
	}
	public void setCodeTeacher(String codeTeacher) {
		this.codeTeacher = codeTeacher;
	}
	public Map<String, Lesson> getLesson() {
		return lesson;
	}
	public void setLesson(String code, Lesson lesson) {
		this.lesson.put(code, lesson);
	}
	public Set<Student> getStudents() {
		return students;
	}
	public void setStudents(Student students) {
		this.students.add(students);
	}
	public GenderType getGender() {
		return gender;
	}
	public void setGender(GenderType gender) {
		this.gender = gender;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public List<LocalDateTime> getArrival() {
		return arrival;
	}
	public void setArrival(LocalDateTime arrival) {
		this.arrival.add(arrival);
	}
}
