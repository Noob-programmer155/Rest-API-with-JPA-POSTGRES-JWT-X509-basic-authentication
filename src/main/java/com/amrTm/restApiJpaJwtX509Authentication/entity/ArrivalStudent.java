package com.amrTm.restApiJpaJwtX509Authentication.entity;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="ArrivalStudent")
public class ArrivalStudent {
	@Id
	@NotNull
	@GeneratedValue
	private Long id;
	@Column(columnDefinition="TIMESTAMP")
	private LocalDateTime arrive;
	@ManyToMany(cascade= {CascadeType.MERGE})
	@JoinTable(name="Arrive_Student", joinColumns= {@JoinColumn(name="Arrive_Id")}, inverseJoinColumns = {@JoinColumn(name="Student_Id")})
	private Set<Student> studentArrive;
	public ArrivalStudent() {
		super();
		this.studentArrive = new HashSet<>();
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public LocalDateTime getArrive() {
		return arrive;
	}
	public void setArrive(LocalDateTime arrive) {
		this.arrive = arrive;
	}
	public Set<Student> getStudents() {
		return studentArrive;
	}
//	public void addArrive(Student student) {
//		if(this.studentArrive.contains(student)) {return ;}
//		this.studentArrive.add(student);
//		student.addArrive(this);
//	}
//	public void removeArrive(Student student) {
//		if(!this.studentArrive.contains(student)) {return ;}
//		this.studentArrive.remove(student);
//		student.removeArrive(this);
//	}
//	public void addArrive(Teacher teacher) {
//		if(this.tArrive.contains(teacher)) {return ;}
//		this.tArrive.add(teacher);
//		teacher.addArrive(this);
//	}
//	public void removeArrive(Teacher teacher) {
//		if(!this.tArrive.contains(teacher)) {return ;}
//		this.tArrive.remove(teacher);
//		teacher.removeArrive(this);
//	}
}
