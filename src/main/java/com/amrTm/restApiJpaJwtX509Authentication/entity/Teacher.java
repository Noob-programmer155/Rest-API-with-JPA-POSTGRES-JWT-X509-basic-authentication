package com.amrTm.restApiJpaJwtX509Authentication.entity;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
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
	@Column(unique=true)
	private String codeTeacher;
	@NotBlank(message="email must be included")
//	@Email(message="Must be a valid email", regexp="${email}")
	@Column(unique=true)
	private String email;
	@OneToMany(mappedBy="teacherLesson", cascade= {CascadeType.MERGE,CascadeType.PERSIST})
	private List<TeacherLesson> teacherLes;
	@OneToMany(mappedBy="teacherArrive", cascade= {CascadeType.MERGE, CascadeType.PERSIST})
	private List<ArrivalTeacher> teacherArr;
	@ManyToMany(mappedBy="teachers")
	@JsonIgnore
	private Set<Student> students;
	public Teacher() {
		super();
		this.teacherLes = new LinkedList<>();
		this.teacherArr = new LinkedList<>();
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
	public List<TeacherLesson> getTeacherLesson() {
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
	public List<ArrivalTeacher> getTeachersArrive() {
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
	public void setTeacherLesson(List<TeacherLesson> teacherLes) {
		this.teacherLes = teacherLes;
	}
	public void setTeachersArrive(List<ArrivalTeacher> teacherArr) {
		this.teacherArr = teacherArr;
	}
	public void setStudents(Set<Student> students) {
		this.students = students;
	}
}
