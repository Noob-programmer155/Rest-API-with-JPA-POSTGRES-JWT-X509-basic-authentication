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
import javax.persistence.ManyToMany;
import javax.persistence.JoinColumn;
import javax.persistence.MapKeyJoinColumn;
import javax.persistence.OrderColumn;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
public class Student {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;
	@NotBlank(message="first name must be included")
	private String first;
	@NotBlank(message="last name must be included")
	private String last;
	@Enumerated
	@NotNull(message="gender must be included")
	private GenderType gender;
	@NotBlank(message="email must be included")
	@Email(message="Must be a valid email", regexp="{email}")
	private String email;
	@NotBlank(message="student code must be included")
	private String studentCode;
	@ElementCollection
	@CollectionTable(name="LESSONS_STUDENT",joinColumns= {@JoinColumn(name="STUDENT_ID")})
	@MapKeyJoinColumn(name="LESSON_CODE")
	@OrderColumn
	private Map<String,Lesson> lesson;
	@ElementCollection
	@Column(name="HOUR_OF_ARRIVALS", columnDefinition="DATE")
	@OrderColumn
	private List<LocalDateTime> arrival;
	@ManyToMany(mappedBy="students")
	private Set<Teacher> teachers;
	public Student() {
		super();
		this.lesson = new HashMap<>();
		this.arrival = new ArrayList<>();
		this.teachers = new HashSet<>();
		// TODO Auto-generated constructor stub
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getFirst() {
		return first;
	}
	public void setFirst(String first) {
		this.first = first;
	}
	public String getLast() {
		return last;
	}
	public void setLast(String last) {
		this.last = last;
	}
	public GenderType getGender() {
		return gender;
	}
	public void setGender(GenderType gender) {
		this.gender = gender;
	}
	public String getStudentCode() {
		return studentCode;
	}
	public void setStudentCode(String studentCode) {
		this.studentCode = studentCode;
	}
	public Set<Teacher> getTeachers() {
		return teachers;
	}
	public void setTeachers(Teacher teachers) {
		this.teachers.add(teachers);
	}
	public Map<String, Lesson> getLesson() {
		return lesson;
	}
	public void setLesson(String code,Lesson lesson) {
		this.lesson.put(code, lesson);
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
