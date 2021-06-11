package com.amrTm.restApiJpaJwtX509Authentication.controller;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.amrTm.restApiJpaJwtX509Authentication.entity.ArrivalStudent;
import com.amrTm.restApiJpaJwtX509Authentication.entity.Student;
import com.amrTm.restApiJpaJwtX509Authentication.entity.StudentLesson;
import com.amrTm.restApiJpaJwtX509Authentication.exception.AttributeNotFoundException;
import com.amrTm.restApiJpaJwtX509Authentication.exception.SaveAttributeException;
import com.amrTm.restApiJpaJwtX509Authentication.services.StudentService;

@RestController
@RequestMapping("/students")
public class RestStudent {
	
	@Autowired
	private StudentService studentService; 
	
	@GetMapping("/all")
	@PreAuthorize("hasRole('ADMIN')")
	public List<Student> findAllStudents(@RequestParam(required=false) List<String> codeStudent){
		if(codeStudent != null)
			return studentService.getAll(codeStudent);
		return studentService.getAll();
	}
	
	@GetMapping("/{nim}")
	@PreAuthorize("hasRole('ADMIN')")
	public Student findStudent(@PathVariable String nim) throws AttributeNotFoundException {
		return studentService.get(nim);
	}
	
	@GetMapping("/Lessons")
	@PreAuthorize("hasAnyRole('USER','ADMIN')")
	public List<StudentLesson> findAllLesson(@RequestAttribute(required=false) String type){
		if (type != null)
			return studentService.getLessonByType(type);
		return studentService.getAllLesson();
	}
	
	@GetMapping("/Lesson")
	@PreAuthorize("hasAnyRole('USER','ADMIN')")
	public EntityModel<StudentLesson> findLesson(@RequestAttribute String code) throws AttributeNotFoundException {
		EntityModel<StudentLesson> lesson = EntityModel.of(studentService.getLesson(code));
		WebMvcLinkBuilder build = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(RestStudent.class).findAllLesson(null));
		lesson.add(build.withRel("list-lesson"));
		return lesson;
	}
	
	@GetMapping("/{nim}/Lessons")
	@PreAuthorize("hasAnyRole('USER','ADMIN')")
	public List<StudentLesson> findAllLessonStudent(@PathVariable String nim){
		return studentService.getLessonOnStudent(nim);
	}
	
	@GetMapping("/arrive/{start}/{end}")
	@PreAuthorize("hasRole('ADMIN')")
	public List<ArrivalStudent> getArriveStudent(@PathVariable LocalDate start, @PathVariable LocalDate end){
		LocalDateTime st = LocalDateTime.of(start,LocalTime.MIDNIGHT);
		LocalDateTime ed = LocalDateTime.of(end, LocalTime.MIDNIGHT);
		return studentService.getArrive(st, ed);
	}
	
	@GetMapping("/arrive")
	@PreAuthorize("hasRole('ADMIN')")
	public ArrivalStudent getArriveStudent(@RequestAttribute Long id) {
		return studentService.getArrive(id);
	}
	
	@PostMapping("/save")
	@PreAuthorize("hasAnyRole('USER','ADMIN')")
	public ResponseEntity<Student> save(@RequestBody Student student) throws SaveAttributeException{
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(student.getStudentCode()).toUri();
		studentService.save(student);
		return ResponseEntity.created(uri).build();
	}
	
	@PostMapping("/saveAll")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Student> saveAll(@RequestBody List<Student> students) throws SaveAttributeException{
		studentService.saveAll(students);
		return ResponseEntity.ok(null);
	}
	
	@PostMapping("/save/lesson")
	@PreAuthorize("hasAnyRole('USER','ADMIN')")
	public ResponseEntity<StudentLesson> saveLesson(@RequestBody StudentLesson lesson) throws SaveAttributeException{
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(lesson.getCodeLesson()).toUri();
		studentService.saveLesson(lesson);
		return ResponseEntity.created(uri).build();
	}
	
	@PostMapping("/saveall/lesson")
	@PreAuthorize("hasAnyRole('USER','ADMIN')")
	public ResponseEntity<StudentLesson> saveAllLesson(@RequestBody List<StudentLesson> lesson) throws SaveAttributeException{
		studentService.saveAllLesson(lesson);
		return ResponseEntity.ok(null);
	}
	
	
}
