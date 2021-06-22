package com.amrTm.restApiJpaJwtX509Authentication.entity;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(name="Student")
@JsonIdentityInfo(
		generator = ObjectIdGenerators.PropertyGenerator.class,
		property = "studentCode")
public class Student {
	@Id
	@Column(name="STUDENT_CODE",unique=true,nullable=false)
	private String studentCode;
	@NotBlank(message="first name must be included")
	private String first;
	@NotBlank(message="last name must be included")
	private String last;
	@Enumerated
	@NotNull(message="gender must be included")
	private GenderType gender;
	@NotBlank(message="email must be included")
	@Email(message="Must be a valid email", regexp="(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])")
	@Column(name="EMAIL",unique=true)
	private String email;
	@ManyToMany(cascade= {CascadeType.MERGE})
	@JoinTable(name="Lesson_Student", joinColumns= {@JoinColumn(name="Student_Id")}, inverseJoinColumns = {@JoinColumn(name="Lesson_Id")})
	private Set<Lesson> studentLes;
	@OneToMany(mappedBy="studentArrive", cascade= {CascadeType.MERGE,CascadeType.PERSIST})
	private List<ArrivalStudent> studentArr;
	@ManyToMany(cascade= {CascadeType.MERGE})
	@JoinTable(name="Scholl_Members", joinColumns= {@JoinColumn(name="Student_Id")}, inverseJoinColumns= {@JoinColumn(name="Teacher_Id")})
	private Set<Teacher> teachers;
	public Student() {
		super();
		this.studentLes = new HashSet<>();
		this.studentArr = new LinkedList<>();
		this.teachers = new HashSet<>();
		// TODO Auto-generated constructor stub
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
	public void addTeacher(Teacher teachers) {
		if(this.teachers.contains(teachers)) {return ;}
		this.teachers.add(teachers);
		teachers.addStudents(this);
	}
	public void removeTeacher(Teacher teachers) {
		if(!this.teachers.contains(teachers)) {return ;}
		this.teachers.remove(teachers);
		teachers.removeStudents(this);
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Set<Lesson> getLesson() {
		return studentLes;
	}
	public void addLesson(Lesson lesson) {
		if(this.studentLes.contains(lesson)) {return ;}
		this.studentLes.add(lesson);
		lesson.addLesson(this);
	}
	public void removeLesson(Lesson lesson) {
		if(!this.studentLes.contains(lesson)) {return ;}
		this.studentLes.remove(lesson);
		lesson.removeLesson(this);
	}
	public List<ArrivalStudent> getStudentsArrive() {
		return studentArr;
	}
//	public void addArrive(ArrivalStudent arrive) {
//		if(this.studentArr.contains(arrive)) {return ;}
//		this.studentArr.add(arrive);
//		arrive.addArrive(this);
//	}
//	public void removeArrive(ArrivalStudent arrive) {
//		if(!this.studentArr.contains(arrive)) {return ;}
//		this.studentArr.remove(arrive);
//		arrive.removeArrive(this);
//	}
	public void setLesson(Set<Lesson> studentsLesson) {
		this.studentLes = studentsLesson;
	}
	public void setStudentsArrive(List<ArrivalStudent> studentsArrive) {
		this.studentArr = studentsArrive;
	}
	public void setTeachers(Set<Teacher> teachers) {
		this.teachers = teachers;
	}
}
