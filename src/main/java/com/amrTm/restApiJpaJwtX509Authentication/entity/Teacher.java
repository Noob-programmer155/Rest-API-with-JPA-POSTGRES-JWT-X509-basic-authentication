package com.amrTm.restApiJpaJwtX509Authentication.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="Teacher")
public class Teacher {
	@Id
	@NotNull
	@GeneratedValue
	private Long id;
	@NotBlank(message="username must be included")
	private String username;
	@Enumerated
	@NotBlank(message="gender must be included")
	private GenderType gender;
	@NotBlank(message="teacher code must be included")
	private String codeTeacher;
	@NotBlank(message="email must be included")
//	@Email(message="Must be a valid email", regexp="{email}")
	private String email;
	@ManyToMany(mappedBy="teacherLesson")
	private Set<TeacherLesson> teacherLes;
	@ManyToMany(mappedBy="teacherArrive")
	private Set<ArrivalTeacher> teacherArr;
	@ManyToMany(cascade= {CascadeType.MERGE})
	@JoinTable(name="Scholl_Members", joinColumns= {@JoinColumn(name="Teacher_Id")}, inverseJoinColumns= {@JoinColumn(name="Student_Id")})
	@JsonIgnore
	private Set<Student> students;
	public Teacher() {
		super();
		this.teacherLes = new HashSet<>();
		this.teacherArr = new HashSet<>();
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
	public Set<TeacherLesson> getLesson() {
		return teacherLes;
	}
	public Set<Student> getStudents() {
		return students;
	}
//	public void addStudents(Student students) {
//		if(this.students.contains(students)) {return ;}
//		this.students.add(students);
//		students.addTeacher(this);
//	}
//	public void removeStudents(Student students) {
//		if(!this.students.contains(students)) {return ;}
//		this.students.remove(students);
//		students.removeTeacher(this);
//	}
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
	public Set<ArrivalTeacher> getArrival() {
		return teacherArr;
	}
	public Set<TeacherLesson> getTeacherLesson() {
		return teacherLes;
	}
//	public void addLesson(Lesson lesson) {
//		if(this.teacherLes.contains(lesson)) {return ;}
//		this.teacherLes.add(lesson);
//		lesson.addLesson(this);
//	}
//	public void removeLesson(Lesson lesson) {
//		if(!this.teacherLes.contains(lesson)) {return ;}
//		this.teacherLes.remove(lesson);
//		lesson.removeLesson(this);
//	}
	public Set<ArrivalTeacher> getTeachersArrive() {
		return teacherArr;
	}
//	public void addArrive(Arrival arrive) {
//		if(this.teacherArr.contains(arrive)) {return ;}
//		this.teacherArr.add(arrive);
//		arrive.addArrive(this);
//	}
//	public void removeArrive(Arrival arrive) {
//		if(!this.teacherArr.contains(arrive)) {return ;}
//		this.teacherArr.remove(arrive);
//		arrive.removeArrive(this);
//	}
	public void setTeacherLesson(Set<TeacherLesson> teacherLes) {
		this.teacherLes = teacherLes;
	}
	public void setTeachersArrive(Set<ArrivalTeacher> teacherArr) {
		this.teacherArr = teacherArr;
	}
	public void setStudents(Set<Student> students) {
		this.students = students;
	}
}
