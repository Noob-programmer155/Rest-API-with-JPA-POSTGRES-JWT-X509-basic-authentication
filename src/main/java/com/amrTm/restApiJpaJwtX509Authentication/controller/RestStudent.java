package com.amrTm.restApiJpaJwtX509Authentication.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amrTm.restApiJpaJwtX509Authentication.entity.ArrivalStudent;
import com.amrTm.restApiJpaJwtX509Authentication.entity.Student;
import com.amrTm.restApiJpaJwtX509Authentication.entity.StudentLesson;
import com.amrTm.restApiJpaJwtX509Authentication.exception.AttributeNotFoundException;
import com.amrTm.restApiJpaJwtX509Authentication.services.StudentService;

@RestController
@RequestMapping("/students")
public class RestStudent {
	
	@Autowired
	private StudentService studentService; 
	
	@GetMapping("/all")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public List<Student> findAllStudents(@RequestParam(required=false) List<String> codeStudent){
		if(codeStudent != null)
			return studentService.getAll(codeStudent);
		return studentService.getAll();
	}
	
	@GetMapping("/{nim}")
	@PreAuthorize("hasRole('ROLE_USER')")
	public Student findStudent(@PathVariable String nim) throws AttributeNotFoundException {
		return studentService.get(nim);
	}
	
	@GetMapping("/Lessons")
	@PreAuthorize("hasRole('ROLE_USER')")
	public List<StudentLesson> findAllLesson(@RequestAttribute(required=false) String type){
		if (type != null)
			return studentService.getLessonByType(type);
		return studentService.getAllLesson();
	}
	
	@GetMapping("/Lesson")
	@PreAuthorize("hasRole('ROLE_USER')")
	public StudentLesson findLesson(@RequestAttribute String code) throws AttributeNotFoundException {
		return studentService.getLesson(code);
	}
	
	@GetMapping("/{nim}/Lessons")
	@PreAuthorize("hasRole('ROLE_USER')")
	public List<StudentLesson> findAllLessonStudent(@PathVariable String nim){
		return studentService.getLessonOnStudent(nim);
	}
	
	@GetMapping("/arrive/{start}/{end}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public List<ArrivalStudent> getArriveStudent(@PathVariable LocalDate start, @PathVariable LocalDate end){
		LocalDateTime st = LocalDateTime.of(start,LocalTime.MIDNIGHT);
		LocalDateTime ed = LocalDateTime.of(end, LocalTime.MIDNIGHT);
		return studentService.getArrive(st, ed);
	}
}
