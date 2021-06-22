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
@Table(name="Teacher")
@JsonIdentityInfo(
		generator = ObjectIdGenerators.PropertyGenerator.class,
		property = "codeTeacher")
public class Teacher {
	@Id
	@Column(name="CODE_TEACHER",unique=true,nullable=false)
	private String codeTeacher;
	@NotBlank(message="username must be included")
	private String username;
	@NotNull(message="gender must be included")
	@Enumerated
	private GenderType gender;
	@NotBlank(message="email must be included")
	@Email(message="Must be a valid email", regexp="(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])")
	@Column(name="EMAIL",unique=true)
	private String email;
	@ManyToMany(cascade= {CascadeType.MERGE})
	@JoinTable(name="Lesson_Teacher", joinColumns= {@JoinColumn(name="Teacher_Id")}, inverseJoinColumns = {@JoinColumn(name="Lesson_Id")})
	private Set<Lesson> teacherLes;
	@OneToMany(mappedBy="teacherArrive", cascade= {CascadeType.MERGE, CascadeType.PERSIST})
	private List<ArrivalTeacher> teacherArr;
	@ManyToMany(mappedBy="teachers")
	private Set<Student> students;
	public Teacher() {
		super();
		this.teacherLes = new HashSet<>();
		this.teacherArr = new LinkedList<>();
		this.students = new HashSet<>();
		// TODO Auto-generated constructor stub
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
	public void addStudents(Student students) {
		if(this.students.contains(students)) {return ;}
		this.students.add(students);
		students.addTeacher(this);
	}
	public void removeStudents(Student students) {
		if(!this.students.contains(students)) {return ;}
		this.students.remove(students);
		students.removeTeacher(this);
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
	public Set<Lesson> getLesson() {
		return teacherLes;
	}
	public void addLesson(Lesson lesson) {
		if(this.teacherLes.contains(lesson)) {return ;}
		this.teacherLes.add(lesson);
		lesson.addLesson(this);
	}
	public void removeLesson(Lesson lesson) {
		if(!this.teacherLes.contains(lesson)) {return ;}
		this.teacherLes.remove(lesson);
		lesson.removeLesson(this);
	}
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
	public void setLesson(Set<Lesson> teacherLes) {
		this.teacherLes = teacherLes;
	}
	public void setTeachersArrive(List<ArrivalTeacher> teacherArr) {
		this.teacherArr = teacherArr;
	}
	public void setStudents(Set<Student> students) {
		this.students = students;
	}
}
