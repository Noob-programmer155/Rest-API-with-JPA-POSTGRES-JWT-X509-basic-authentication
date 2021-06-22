package com.amrTm.restApiJpaJwtX509Authentication.controller;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;

@RestController
@RequestMapping("/teachers")
@Api(tags="Teacher")
public class RestTeacher {
	@Autowired
	private TeacherService teacherService;
	
	@GetMapping("/all")
	@ApiOperation(value="${Rest.Teacher.findAllTeachers.value}", notes="${Rest.Teacher.findAllTeachers.note}", response=List.class
			, authorizations= {@Authorization(value="apiKey")})
	@ApiResponses({
		@ApiResponse(code = 401, message = "authority not allowed or not found"),
		@ApiResponse(code = 403, message = "expired or invalid JWT token")
	})
	public List<Teacher> findAllTeachers(@ApiParam("teachers code (Optional)") @RequestParam(required=false) List<String> codeTeacher, HttpServletRequest req){
		if(codeTeacher != null)
			return teacherService.getAll(codeTeacher, req);
		return teacherService.getAll(req);
	}
	
	@GetMapping("/{codeTeacher}")
	@ApiOperation(value="${Rest.Teacher.findTeacher.value}", notes="${Rest.Teacher.findTeacher.note}", response=Teacher.class
			, authorizations= {@Authorization(value="apiKey")})
	@ApiResponses({
		@ApiResponse(code = 401, message = "authority not allowed or not found"),
		@ApiResponse(code = 403, message = "expired or invalid JWT token"),
		@ApiResponse(code = 404, message = "Teacher cannot found !")
	})
	public Teacher findTeacher(@ApiParam("teacher code") @PathVariable String codeTeacher, HttpServletRequest req) throws AttributeNotFoundException {
		return teacherService.get(codeTeacher, req);
	}
	
	@GetMapping("/Lessons")
	@ApiOperation(value="${Rest.Teacher.findAllLesson.value}", notes="${Rest.Teacher.findAllLesson.note}", response=List.class
			, authorizations= {@Authorization(value="apiKey")})
	@ApiResponses({
		@ApiResponse(code = 401, message = "authority not allowed or not found"),
		@ApiResponse(code = 403, message = "expired or invalid JWT token")
	})
	public List<Lesson> findAllLesson(@ApiParam("lesson type (Optional)") @RequestParam(required=false) String type, HttpServletRequest req){
		if (type != null)
			return teacherService.getLessonByType(type, req);
		return teacherService.getAllLesson(req);
	}
	
	@GetMapping("/Lesson")
	@ApiOperation(value="${Rest.Teacher.findLesson.value}", notes="${Rest.Teacher.findLesson.note}", response=Lesson.class
			, authorizations= {@Authorization(value="apiKey")})
	@ApiResponses({
		@ApiResponse(code = 401, message = "authority not allowed or not found"),
		@ApiResponse(code = 403, message = "expired or invalid JWT token"),
		@ApiResponse(code = 404, message = "Lesson cannot found !")
	})
	public Lesson findLesson(@ApiParam("lesson code") @RequestParam String code, HttpServletRequest req) throws AttributeNotFoundException {
		return teacherService.getLesson(code, req);
	}
	
//	@GetMapping("/{nim}/Lessons")
//	@PreAuthorize("hasAnyAuthority('USER','ADMIN')")
//	public List<TeacherLesson> findAllLessonTeacher(@PathVariable String nim){
//		return teacherService.getLessonOnTeacher(nim);
//	}
	
	@GetMapping("/arrive/{start}/{end}")
	@ApiOperation(value="${Rest.Teacher.getArriveTeacherBetween.value}", notes="${Rest.Teacher.getArriveTeacherBetween.note}", response=List.class
			, authorizations= {@Authorization(value="apiKey")})
	@ApiResponses({
		@ApiResponse(code = 401, message = "authority not allowed or not found"),
		@ApiResponse(code = 403, message = "expired or invalid JWT token")
	})
	public List<ArrivalTeacher> getArriveTeacher(@ApiParam("start date time") @PathVariable String start, 
												@ApiParam("end date time") @PathVariable String end, HttpServletRequest req){
		LocalDateTime st = LocalDateTime.of(LocalDate.parse(start),LocalTime.MIDNIGHT);
		LocalDateTime ed = LocalDateTime.of(LocalDate.parse(end), LocalTime.MIDNIGHT);
		return teacherService.getArrive(st, ed, req);
	}
	
	@GetMapping("/arrive")
	@ApiOperation(value="${Rest.Teacher.getArriveTeacher.value}", notes="${Rest.Teacher.getArriveTeacher.note}", response=ArrivalTeacher.class
			, authorizations= {@Authorization(value="apiKey")})
	@ApiResponses({
		@ApiResponse(code = 401, message = "authority not allowed or not found"),
		@ApiResponse(code = 403, message = "expired or invalid JWT token"),
		@ApiResponse(code = 404, message = "teacher arrival cannot found !")
	})
	public ArrivalTeacher getArriveTeacher(@ApiParam("arrival teacher id") @RequestParam Long id, HttpServletRequest req) {
		return teacherService.getArrive(id,false, req);
	}
	
	@PostMapping("/save")
	@ApiOperation(value="${Rest.Teacher.save.value}", notes="${Rest.Teacher.save.note}", response=ResponseEntity.class
			, authorizations= {@Authorization(value="apiKey")})
	@ApiResponses({
		@ApiResponse(code = 401, message = "authority not allowed or not found"),
		@ApiResponse(code = 403, message = "expired or invalid JWT token"),
		@ApiResponse(code = 304, message = "Cannot saving this teacher, please check your input /or maybe this teacher has been saved")
	})
	public ResponseEntity<Teacher> save(@ApiParam("new teacher") @RequestBody Teacher teacher, HttpServletRequest req) throws SaveAttributeException{
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(teacher.getCodeTeacher()).toUri();
		teacherService.save(teacher, req);
		return ResponseEntity.created(uri).build();
	}
	
	@PostMapping("/save/all")
	@ApiOperation(value="${Rest.Teacher.saveAll.value}", notes="${Rest.Teacher.saveAll.note}", response=ResponseEntity.class
			, authorizations= {@Authorization(value="apiKey")})
	@ApiResponses({
		@ApiResponse(code = 401, message = "authority not allowed or not found"),
		@ApiResponse(code = 403, message = "expired or invalid JWT token"),
		@ApiResponse(code = 304, message = "Cannot saving teachers, please check your input /or maybe there are teachers who have been saved")
	})
	public ResponseEntity<Teacher> saveAll(@ApiParam("new teachers") @RequestBody List<Teacher> teachers, HttpServletRequest req) throws SaveAttributeException{
		teacherService.saveAll(teachers, req);
		return ResponseEntity.ok(null);
	}
	
	@PutMapping("/modify")
	@ApiOperation(value="${Rest.Teacher.modify.value}", notes="${Rest.Teacher.modify.note}", response=ResponseEntity.class
			, authorizations= {@Authorization(value="apiKey")})
	@ApiResponses({
		@ApiResponse(code = 401, message = "authority not allowed or not found"),
		@ApiResponse(code = 403, message = "expired or invalid JWT token"),
		@ApiResponse(code = 304, message = "Cannot modify this teacher, please check your input /or maybe email has been same with the other teacher")
	})
	public ResponseEntity<Teacher> modifyTeacher(@ApiParam("modified teacher") @RequestBody Teacher teacher, 
												@ApiParam("teacher code") @RequestParam String id, HttpServletRequest req) throws SaveAttributeException{
		Teacher st = teacherService.modify(teacher, id, req);
		return ResponseEntity.ok(st);
	}
	
	@PutMapping("/modify/all")
	@ApiOperation(value="${Rest.Teacher.modifyAll.value}", notes="${Rest.Teacher.modifyAll.note}", response=ResponseEntity.class
			, authorizations= {@Authorization(value="apiKey")})
	@ApiResponses({
		@ApiResponse(code = 401, message = "authority not allowed or not found"),
		@ApiResponse(code = 403, message = "expired or invalid JWT token"),
		@ApiResponse(code = 304, message = "Cannot modify teachers, please check your input, /or maybe email has been same with the other teacher")
	})
	public ResponseEntity<Teacher> modifyAllTeacher(@ApiParam("modified teachers") @RequestBody List<Teacher> teachers, HttpServletRequest req) throws SaveAttributeException{
		teacherService.modifyAll(teachers, req);
		return ResponseEntity.ok(null);
	}
	
	@PostMapping("/modify/lesson/{access}")
	@ApiOperation(value="${Rest.Teacher.modifyLesson.value}", notes="${Rest.Teacher.modifyLesson.note}", response=ResponseEntity.class
			, authorizations= {@Authorization(value="apiKey")})
	@ApiResponses({
		@ApiResponse(code = 401, message = "authority not allowed or not found"),
		@ApiResponse(code = 403, message = "expired or invalid JWT token"),
		@ApiResponse(code = 404, message = "Cannot modify this study, maybe study code has been already /or study is not found")
	})
	public ResponseEntity<Teacher> modifyLesson(@ApiParam("teacher code") @RequestParam String codeTeacher, 
												@ApiParam("study code") @RequestParam String lesson,
												@ApiParam("type modification") @PathVariable AccessModification access, HttpServletRequest req) throws AttributeNotFoundException{
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(codeTeacher).toUri();
		teacherService.modifyTeacherLesson(codeTeacher, lesson, access, req);
		return ResponseEntity.created(uri).build();
	}
																		
	@PostMapping("/modify/add/arrive")
	@ApiOperation(value="${Rest.Teacher.addArrive.value}", notes="${Rest.Teacher.addArrive.note}", response=ResponseEntity.class
			, authorizations= {@Authorization(value="apiKey")})
	@ApiResponses({
		@ApiResponse(code = 401, message = "authority not allowed or not found"),
		@ApiResponse(code = 403, message = "expired or invalid JWT token"),
		@ApiResponse(code = 404, message = "Cannot modify this arrive, maybe teacher is not found")
	})
	public ResponseEntity<Teacher> addArrive(@ApiParam("teacher code") @RequestParam String codeTeacher, HttpServletRequest req) throws AttributeNotFoundException{
		ArrivalTeacher arrive = new ArrivalTeacher();
		arrive.setArrive(LocalDateTime.now());
		teacherService.modifyTeacherArrive(codeTeacher, arrive, AccessModification.ADD, req);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(codeTeacher).toUri();
		return ResponseEntity.created(uri).build();
	}
	
	@PostMapping("/modify/remove/arrive")
	@ApiOperation(value="${Rest.Teacher.removeArrive.value}", notes="${Rest.Teacher.removeArrive.note}", response=ResponseEntity.class
			, authorizations= {@Authorization(value="apiKey")})
	@ApiResponses({
		@ApiResponse(code = 401, message = "authority not allowed or not found"),
		@ApiResponse(code = 403, message = "expired or invalid JWT token"),
		@ApiResponse(code = 404, message = "Cannot modify this arrive, maybe teacher is not found")
	})
	public ResponseEntity<Teacher> removeArrive(@ApiParam("arrival teacher id") @RequestParam Long arrive, HttpServletRequest req) throws AttributeNotFoundException{
		ArrivalTeacher arrives = teacherService.getArrive(arrive, false, req);
		teacherService.modifyTeacherArrive(null, arrives, AccessModification.DELETE, req);
		return ResponseEntity.ok(null);
	}
	
	@DeleteMapping("/delete")
	@ApiOperation(value="${Rest.Teacher.delete.value}", notes="${Rest.Teacher.delete.note}", response=String.class
			, authorizations= {@Authorization(value="apiKey")})
	@ApiResponses({
		@ApiResponse(code = 401, message = "authority not allowed or not found"),
		@ApiResponse(code = 403, message = "expired or invalid JWT token"),
		@ApiResponse(code = 404, message = "Teacher not found !")
	})
	public String delete(@ApiParam("teacher code") @RequestParam String codeteacher, HttpServletRequest req) throws AttributeNotFoundException {
		teacherService.delete(codeteacher, req);
		return "Teacher has been deleted";
	}
}
