package com.amrTm.restApiJpaJwtX509Authentication.entity;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Lesson {
	@Column(name="CODE_STUDIES")
	private String code_studies;
	private String studies;
	public Lesson() {
		super();
		this.code_studies = UUID.randomUUID().toString();
	}
	public String getCode_studies() {
		return code_studies;
	}
	public void setCode_studies(String code_studies) {
		this.code_studies = code_studies;
	}
	public String getStudies() {
		return studies;
	}
	public void setStudies(String studies) {
		this.studies = studies;
	}
}
