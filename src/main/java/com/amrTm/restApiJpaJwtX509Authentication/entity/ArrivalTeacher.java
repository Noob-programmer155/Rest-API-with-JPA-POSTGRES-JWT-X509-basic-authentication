package com.amrTm.restApiJpaJwtX509Authentication.entity;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="ArrivalTeacher")
public class ArrivalTeacher {
	@Id
	@NotNull
	@GeneratedValue
	private Long id;
	@Column(columnDefinition="TIMESTAMP")
	private LocalDateTime arrive;
	@ManyToOne
	@JoinTable(name="Arrive_Teacher", joinColumns= {@JoinColumn(name="Arrive_Id")}, inverseJoinColumns = {@JoinColumn(name="Teacher_Id")})
	private Teacher teacherArrive;
	public ArrivalTeacher() {
		super();
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
	public Teacher getTeacherArrive() {
		return teacherArrive;
	}
	public void setTeacherArrive(Teacher teacherArrive) {
		this.teacherArrive = teacherArrive;
	}
}
