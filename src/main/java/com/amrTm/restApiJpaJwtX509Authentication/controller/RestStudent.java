package com.amrTm.restApiJpaJwtX509Authentication.controller;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.amrTm.restApiJpaJwtX509Authentication.entity.ArrivalStudent;
import com.amrTm.restApiJpaJwtX509Authentication.entity.Lesson;
import com.amrTm.restApiJpaJwtX509Authentication.entity.Student;
import com.amrTm.restApiJpaJwtX509Authentication.exception.AttributeNotFoundException;
import com.amrTm.restApiJpaJwtX509Authentication.exception.SaveAttributeException;
import com.amrTm.restApiJpaJwtX509Authentication.services.AccessModification;
import com.amrTm.restApiJpaJwtX509Authentication.services.StudentService;

@RestController
@RequestMapping("/students")
public class RestStudent {
	
	@Autowired
	private StudentService studentService;
	
	@GetMapping("/all")
	@PreAuthorize("hasAnyAuthority('USER','ADMIN')")
	public List<Student> findAllStudents(@RequestParam(required=false) List<String> codeStudent){
		if(codeStudent != null)
			return studentService.getAll(codeStudent);
		return studentService.getAll();
	}
	
	@GetMapping("/{nim}")
	@PreAuthorize("hasAnyAuthority('USER','ADMIN')")
	public Student findStudent(@PathVariable String nim) throws AttributeNotFoundException {
		return studentService.get(nim);
	}
	
	@GetMapping("/Lessons")
	@PreAuthorize("hasAnyAuthority('USER','ADMIN')")
	public List<Lesson> findAllLesson(@RequestParam(required=false) String type){
		if (type != null)
			return studentService.getLessonByType(type);
		return studentService.getAllLesson();
	}
	
	@GetMapping("/Lesson")
	@PreAuthorize("hasAnyAuthority('USER','ADMIN')")
	public Lesson findLesson(@RequestParam String code) throws AttributeNotFoundException {
		return studentService.getLesson(code);
	}
	
//	@GetMapping("/{nim}/Lessons")
//	@PreAuthorize("hasAnyAuthority('USER','ADMIN')")
//	public List<StudentLesson> findAllLessonStudent(@PathVariable String nim){
//		return studentService.getLessonOnStudent(nim);
//	}
	
	@GetMapping("/arrive/{start}/{end}")
	@PreAuthorize("hasAnyAuthority('USER','ADMIN')")
	public List<ArrivalStudent> getArriveStudent(@PathVariable String start, @PathVariable String end){
		LocalDateTime st = LocalDateTime.of(LocalDate.parse(start),LocalTime.MIDNIGHT);
		LocalDateTime ed = LocalDateTime.of(LocalDate.parse(end), LocalTime.MIDNIGHT);
		return studentService.getArrive(st, ed);
	}
	
	@GetMapping("/arrive")
	@PreAuthorize("hasAnyAuthority('USER','ADMIN')")
	public ArrivalStudent getArriveStudent(@RequestParam Long id) {
		return studentService.getArrive(id,false);
	}
	
	@PostMapping("/save")
	public ResponseEntity<Student> save(@RequestBody Student student) throws SaveAttributeException{
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(student.getStudentCode()).toUri();
		studentService.save(student);
		return ResponseEntity.created(uri).build();
	}
	
	@PostMapping("/save/all")
	public ResponseEntity<Student> saveAll(@RequestBody List<Student> students) throws SaveAttributeException{
		studentService.saveAll(students);
		return ResponseEntity.ok(null);
	}
	
	@PutMapping("/modify")
	public ResponseEntity<Student> modifyStudent(@RequestBody Student student, @RequestParam String id) throws SaveAttributeException{
		Student st = studentService.modify(student, id);
		return ResponseEntity.ok(st);
	}
	
	@PutMapping("/modify/all")
	public ResponseEntity<Student> modifyAllStudent(@RequestBody List<Student> students) throws SaveAttributeException{
		studentService.modifyAll(students);
		return ResponseEntity.ok(null);
	}
	
	@PostMapping("/modify/teacher/{access}")
	public ResponseEntity<Student> modifyTeacher(@RequestParam String codeTeacher, @RequestParam String codeStudent, 
			@PathVariable AccessModification access) throws AttributeNotFoundException, SaveAttributeException{
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(codeStudent).toUri();
		studentService.modifyTeacher(codeTeacher, codeStudent, access);
		return ResponseEntity.created(uri).build();
	}
	
	@PostMapping("/modify/lesson/{access}")
	public ResponseEntity<Student> modifyLesson(@RequestParam String codeStudent, @RequestParam String lesson,
			@PathVariable AccessModification access) throws SaveAttributeException, AttributeNotFoundException{
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(codeStudent).toUri();
		studentService.modifyStudentLesson(codeStudent, lesson, access);
		return ResponseEntity.created(uri).build();
	}
	
	@PostMapping("/modify/add/arrive")
	public ResponseEntity<Student> addArrive(@RequestParam String codeStudent) throws SaveAttributeException, AttributeNotFoundException{
		ArrivalStudent arrive = new ArrivalStudent();
		arrive.setArrive(LocalDateTime.now());
		studentService.modifyStudentArrive(codeStudent, arrive, AccessModification.ADD);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(codeStudent).toUri();
		return ResponseEntity.created(uri).build();
	}
	
	@PostMapping("/modify/remove/arrive")
	public ResponseEntity<Student> removeArrive(@RequestParam Long arrive) throws SaveAttributeException{
		ArrivalStudent arrives = studentService.getArrive(arrive, false);
		studentService.modifyStudentArrive(null, arrives, AccessModification.DELETE);
		return ResponseEntity.ok(null);
	}
	
	@DeleteMapping("/delete")
	public String delete(@RequestBody Student student) throws AttributeNotFoundException {
		studentService.delete(student);
		return "Student has been deleted";
	}
}
