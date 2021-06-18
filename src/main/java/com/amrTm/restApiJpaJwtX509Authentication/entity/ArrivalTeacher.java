package com.amrTm.restApiJpaJwtX509Authentication.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(name="ArrivalTeacher")
@JsonIdentityInfo(
		generator = ObjectIdGenerators.PropertyGenerator.class
		,property="idArrive")
public class ArrivalTeacher {
	@Id
	@GeneratedValue
	private long idArrive;
	@Column(columnDefinition="TIMESTAMP")
	private LocalDateTime arrive;
	@ManyToOne
	@JoinTable(name="Arrive_Teacher", joinColumns= {@JoinColumn(name="Arrive_Id")}, inverseJoinColumns = {@JoinColumn(name="Teacher_Id")})
	private Teacher teacherArrive;
	public ArrivalTeacher() {
		super();
	}
	public Long getIdArrive() {
		return idArrive;
	}
	public void setIdArrive(Long idArrive) {
		this.idArrive = idArrive;
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
