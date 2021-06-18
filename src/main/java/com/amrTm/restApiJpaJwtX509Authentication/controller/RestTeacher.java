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

import com.amrTm.restApiJpaJwtX509Authentication.entity.ArrivalTeacher;
import com.amrTm.restApiJpaJwtX509Authentication.entity.Lesson;
import com.amrTm.restApiJpaJwtX509Authentication.entity.Teacher;
import com.amrTm.restApiJpaJwtX509Authentication.exception.AttributeNotFoundException;
import com.amrTm.restApiJpaJwtX509Authentication.exception.SaveAttributeException;
import com.amrTm.restApiJpaJwtX509Authentication.services.AccessModification;
import com.amrTm.restApiJpaJwtX509Authentication.services.TeacherService;

@RestController
@RequestMapping("/teachers")
public class RestTeacher {
	@Autowired
	private TeacherService teacherService;
	
	@GetMapping("/all")
	@PreAuthorize("hasAuthority('ADMIN')")
	public List<Teacher> findAllTeachers(@RequestParam(required=false) List<String> codeTeacher){
		if(codeTeacher != null)
			return teacherService.getAll(codeTeacher);
		return teacherService.getAll();
	}
	
	@GetMapping("/{nim}")
	@PreAuthorize("hasAnyAuthority('USER','ADMIN')")
	public Teacher findTeacher(@PathVariable String nim) throws AttributeNotFoundException {
		return teacherService.get(nim);
	}
	
	@GetMapping("/Lessons")
	@PreAuthorize("hasAnyAuthority('USER','ADMIN')")
	public List<Lesson> findAllLesson(@RequestParam(required=false) String type){
		if (type != null)
			return teacherService.getLessonByType(type);
		return teacherService.getAllLesson();
	}
	
	@GetMapping("/Lesson")
	@PreAuthorize("hasAnyAuthority('USER','ADMIN')")
	public Lesson findLesson(@RequestParam String code) throws AttributeNotFoundException {
		return teacherService.getLesson(code);
	}
	
//	@GetMapping("/{nim}/Lessons")
//	@PreAuthorize("hasAnyAuthority('USER','ADMIN')")
//	public List<TeacherLesson> findAllLessonTeacher(@PathVariable String nim){
//		return teacherService.getLessonOnTeacher(nim);
//	}
	
	@GetMapping("/arrive/{start}/{end}")
	@PreAuthorize("hasAuthority('ADMIN')")
	public List<ArrivalTeacher> getArriveTeacher(@PathVariable String start, @PathVariable String end){
		LocalDateTime st = LocalDateTime.of(LocalDate.parse(start),LocalTime.MIDNIGHT);
		LocalDateTime ed = LocalDateTime.of(LocalDate.parse(end), LocalTime.MIDNIGHT);
		return teacherService.getArrive(st, ed);
	}
	
	@GetMapping("/arrive")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ArrivalTeacher getArriveTeacher(@RequestParam Long id) {
		return teacherService.getArrive(id,false);
	}
	
	@PostMapping("/save")
	public ResponseEntity<Teacher> save(@RequestBody Teacher teacher) throws SaveAttributeException{
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(teacher.getCodeTeacher()).toUri();
		teacherService.save(teacher);
		return ResponseEntity.created(uri).build();
	}
	
	@PostMapping("/save/all")
	public ResponseEntity<Teacher> saveAll(@RequestBody List<Teacher> teachers) throws SaveAttributeException{
		teacherService.saveAll(teachers);
		return ResponseEntity.ok(null);
	}
	
	@PutMapping("/modify")
	public ResponseEntity<Teacher> modifyTeacher(@RequestBody Teacher teacher, @RequestParam String id) throws SaveAttributeException{
		Teacher st = teacherService.modify(teacher, id);
		return ResponseEntity.ok(st);
	}
	
	@PutMapping("/modify/all")
	public ResponseEntity<Teacher> modifyAllTeacher(@RequestBody List<Teacher> teachers) throws SaveAttributeException{
		teacherService.modifyAll(teachers);
		return ResponseEntity.ok(null);
	}
	
	@PostMapping("/modify/lesson/{access}")
	public ResponseEntity<Teacher> modifyLesson(@RequestParam String codeTeacher, @RequestParam String lesson,
			@PathVariable AccessModification access) throws SaveAttributeException, AttributeNotFoundException{
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(codeTeacher).toUri();
		teacherService.modifyTeacherLesson(codeTeacher, lesson, access);
		return ResponseEntity.created(uri).build();
	}
																		
	@PostMapping("/modify/add/arrive")
	public ResponseEntity<Teacher> addArrive(@RequestParam String codeTeacher) throws SaveAttributeException, AttributeNotFoundException{
		ArrivalTeacher arrive = new ArrivalTeacher();
		arrive.setArrive(LocalDateTime.now());
		teacherService.modifyTeacherArrive(codeTeacher, arrive, AccessModification.ADD);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(codeTeacher).toUri();
		return ResponseEntity.created(uri).build();
	}
	
	@PostMapping("/modify/remove/arrive")
	public ResponseEntity<Teacher> removeArrive(@RequestParam Long arrive) throws SaveAttributeException{
		ArrivalTeacher arrives = teacherService.getArrive(arrive, false);
		teacherService.modifyTeacherArrive(null, arrives, AccessModification.DELETE);
		return ResponseEntity.ok(null);
	}
	
	@DeleteMapping("/delete")
	public String delete(@RequestBody String codeteacher) throws AttributeNotFoundException {
		teacherService.delete(codeteacher);
		return "Teacher has been deleted";
	}
}
