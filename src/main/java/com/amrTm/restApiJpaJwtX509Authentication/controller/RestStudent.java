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

import com.amrTm.restApiJpaJwtX509Authentication.entity.ArrivalStudent;
import com.amrTm.restApiJpaJwtX509Authentication.entity.Lesson;
import com.amrTm.restApiJpaJwtX509Authentication.entity.Student;
import com.amrTm.restApiJpaJwtX509Authentication.exception.AttributeNotFoundException;
import com.amrTm.restApiJpaJwtX509Authentication.exception.SaveAttributeException;
import com.amrTm.restApiJpaJwtX509Authentication.services.AccessModification;
import com.amrTm.restApiJpaJwtX509Authentication.services.StudentService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;

@RestController
@RequestMapping("/students")
@Api(tags="Student")
public class RestStudent {
	
	@Autowired
	private StudentService studentService;
	
	@GetMapping("/all")
	@ApiOperation(value="${Rest.Student.findAllStudents.value}", notes="${Rest.Student.findAllStudents.note}", response=List.class
			, authorizations= {@Authorization(value="apiKey")})
	@ApiResponses({
		@ApiResponse(code = 401, message = "authority not allowed or not found"),
		@ApiResponse(code = 403, message = "expired or invalid JWT token")
	})
	public List<Student> findAllStudents(@ApiParam("students code (Optional)") @RequestParam(required=false) List<String> codeStudent, HttpServletRequest req){
		if(codeStudent != null)
			return studentService.getAll(codeStudent, req);
		return studentService.getAll(req);
	}
	
	@GetMapping("/{codeStudent}")
	@ApiOperation(value="${Rest.Student.findStudent.value}", notes="${Rest.Student.findStudent.note}", response=Student.class
			, authorizations= {@Authorization(value="apiKey")})
	@ApiResponses({
		@ApiResponse(code = 401, message = "authority not allowed or not found"),
		@ApiResponse(code = 403, message = "expired or invalid JWT token"),
		@ApiResponse(code = 404, message = "Student cannot found !")
	})
	public Student findStudent(@ApiParam("student code") @PathVariable String codeStudent, HttpServletRequest req) throws AttributeNotFoundException {
		return studentService.get(codeStudent, req);
	}
	
	@GetMapping("/Lessons")
	@ApiOperation(value="${Rest.Student.findAllLesson.value}", notes="${Rest.Student.findAllLesson.note}", response=List.class
			, authorizations= {@Authorization(value="apiKey")})
	@ApiResponses({
		@ApiResponse(code = 401, message = "authority not allowed or not found"),
		@ApiResponse(code = 403, message = "expired or invalid JWT token")
	})
	public List<Lesson> findAllLesson(@ApiParam("lesson type (Optional)") @RequestParam(required=false) String type, HttpServletRequest req){
		if (type != null)
			return studentService.getLessonByType(type, req);
		return studentService.getAllLesson(req);
	}
	
	@GetMapping("/Lesson")
	@ApiOperation(value="${Rest.Student.findLesson.value}", notes="${Rest.Student.findLesson.note}", response=Lesson.class
			, authorizations= {@Authorization(value="apiKey")})
	@ApiResponses({
		@ApiResponse(code = 401, message = "authority not allowed or not found"),
		@ApiResponse(code = 403, message = "expired or invalid JWT token"),
		@ApiResponse(code = 404, message = "Lesson cannot found !")
	})
	public Lesson findLesson(@ApiParam("lesson code") @RequestParam String code, HttpServletRequest req) throws AttributeNotFoundException {
		return studentService.getLesson(code, req);
	}
	
//	@GetMapping("/{nim}/Lessons")
//	@PreAuthorize("hasAnyAuthority('USER','ADMIN')")
//	public List<StudentLesson> findAllLessonStudent(@PathVariable String nim){
//		return studentService.getLessonOnStudent(nim);
//	}
	
	@GetMapping("/arrive/{start}/{end}")
	@ApiOperation(value="${Rest.Student.getArriveStudentBetween.value}", notes="${Rest.Student.getArriveStudentBetween.note}", response=List.class
			, authorizations= {@Authorization(value="apiKey")})
	@ApiResponses({
		@ApiResponse(code = 401, message = "authority not allowed or not found"),
		@ApiResponse(code = 403, message = "expired or invalid JWT token")
	})
	public List<ArrivalStudent> getArriveStudentBetween(@ApiParam("start date time") @PathVariable String start, 
												@ApiParam("end date time") @PathVariable String end, HttpServletRequest req){
		LocalDateTime st = LocalDateTime.of(LocalDate.parse(start),LocalTime.MIDNIGHT);
		LocalDateTime ed = LocalDateTime.of(LocalDate.parse(end), LocalTime.MIDNIGHT);
		return studentService.getArrive(st, ed, req);
	}
	
	@GetMapping("/arrive")
	@ApiOperation(value="${Rest.Student.getArriveStudent.value}", notes="${Rest.Student.getArriveStudent.note}", response=ArrivalStudent.class
			, authorizations= {@Authorization(value="apiKey")})
	@ApiResponses({
		@ApiResponse(code = 401, message = "authority not allowed or not found"),
		@ApiResponse(code = 403, message = "expired or invalid JWT token"),
		@ApiResponse(code = 404, message = "student arrival cannot found !")
	})
	public ArrivalStudent getArriveStudent(@ApiParam("arrival student id") @RequestParam Long id, HttpServletRequest req) {
		return studentService.getArrive(id,false, req);
	}
	
	@PostMapping("/save")
	@ApiOperation(value="${Rest.Student.save.value}", notes="${Rest.Student.save.note}", response=ResponseEntity.class
			, authorizations= {@Authorization(value="apiKey")})
	@ApiResponses({
		@ApiResponse(code = 401, message = "authority not allowed or not found"),
		@ApiResponse(code = 403, message = "expired or invalid JWT token"),
		@ApiResponse(code = 304, message = "Cannot saving this student, please check your input /or maybe this student has been saved")
	})
	public ResponseEntity<Student> save(@ApiParam("new student") @RequestBody Student student, HttpServletRequest req) throws SaveAttributeException{
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(student.getStudentCode()).toUri();
		studentService.save(student, req);
		return ResponseEntity.created(uri).build();
	}
	
	@PostMapping("/save/all")
	@ApiOperation(value="${Rest.Student.saveAll.value}", notes="${Rest.Student.saveAll.note}", response=ResponseEntity.class
			, authorizations= {@Authorization(value="apiKey")})
	@ApiResponses({
		@ApiResponse(code = 401, message = "authority not allowed or not found"),
		@ApiResponse(code = 403, message = "expired or invalid JWT token"),
		@ApiResponse(code = 304, message = "Cannot saving students, please check your input /or maybe there are students who have been saved")
	})
	public ResponseEntity<Student> saveAll(@ApiParam("new students") @RequestBody List<Student> students, HttpServletRequest req) throws SaveAttributeException{
		studentService.saveAll(students, req);
		return ResponseEntity.ok(null);
	}
	
	@PutMapping("/modify")
	@ApiOperation(value="${Rest.Student.modify.value}", notes="${Rest.Student.modify.note}", response=ResponseEntity.class
			, authorizations= {@Authorization(value="apiKey")})
	@ApiResponses({
		@ApiResponse(code = 401, message = "authority not allowed or not found"),
		@ApiResponse(code = 403, message = "expired or invalid JWT token"),
		@ApiResponse(code = 304, message = "Cannot modify this student, please check your input /or maybe email has been same with the other student")
	})
	public ResponseEntity<Student> modifyStudent(@ApiParam("modified student") @RequestBody Student student, 
												@ApiParam("student code") @RequestParam String id, HttpServletRequest req) throws SaveAttributeException{
		Student st = studentService.modify(student, id, req);
		return ResponseEntity.ok(st);
	}
	
	@PutMapping("/modify/all")
	@ApiOperation(value="${Rest.Student.modifyAll.value}", notes="${Rest.Student.modifyAll.note}", response=ResponseEntity.class
			, authorizations= {@Authorization(value="apiKey")})
	@ApiResponses({
		@ApiResponse(code = 401, message = "authority not allowed or not found"),
		@ApiResponse(code = 403, message = "expired or invalid JWT token"),
		@ApiResponse(code = 304, message = "Cannot modify students, please check your input, /or maybe email has been same with the other student")
	})
	public ResponseEntity<Student> modifyAllStudent(@ApiParam("modified students") @RequestBody List<Student> students, HttpServletRequest req) throws SaveAttributeException{
		studentService.modifyAll(students, req);
		return ResponseEntity.ok(null);
	}
	
	@PostMapping("/modify/teacher/{access}")
	@ApiOperation(value="${Rest.Student.modifyTeacher.value}", notes="${Rest.Student.modifyTeacher.note}", response=ResponseEntity.class
			, authorizations= {@Authorization(value="apiKey")})
	@ApiResponses({
		@ApiResponse(code = 401, message = "authority not allowed or not found"),
		@ApiResponse(code = 403, message = "expired or invalid JWT token"),
		@ApiResponse(code = 404, message = "Cannot modify this teacher, maybe student /or teacher is not found")
	})
	public ResponseEntity<Student> modifyTeacher(@ApiParam("teacher code") @RequestParam String codeTeacher, 
												@ApiParam("student code") @RequestParam String codeStudent, 
												@ApiParam("type modification") @PathVariable AccessModification access, HttpServletRequest req) throws AttributeNotFoundException{
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(codeStudent).toUri();
		studentService.modifyTeacher(codeTeacher, codeStudent, access, req);
		return ResponseEntity.created(uri).build();
	}
	
	@PostMapping("/modify/lesson/{access}")
	@ApiOperation(value="${Rest.Student.modifyLesson.value}", notes="${Rest.Student.modifyLesson.note}", response=ResponseEntity.class
			, authorizations= {@Authorization(value="apiKey")})
	@ApiResponses({
		@ApiResponse(code = 401, message = "authority not allowed or not found"),
		@ApiResponse(code = 403, message = "expired or invalid JWT token"),
		@ApiResponse(code = 404, message = "Cannot modify this study, maybe study code has been already /or study is not found")
	})
	public ResponseEntity<Student> modifyLesson(@ApiParam("student code") @RequestParam String codeStudent, 
												@ApiParam("study code") @RequestParam String lesson,
												@ApiParam("type modification") @PathVariable AccessModification access, HttpServletRequest req) throws AttributeNotFoundException{
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(codeStudent).toUri();
		studentService.modifyStudentLesson(codeStudent, lesson, access, req);
		return ResponseEntity.created(uri).build();
	}
	
	@PostMapping("/modify/add/arrive")
	@ApiOperation(value="${Rest.Student.addArrive.value}", notes="${Rest.Student.addArrive.note}", response=ResponseEntity.class
			, authorizations= {@Authorization(value="apiKey")})
	@ApiResponses({
		@ApiResponse(code = 401, message = "authority not allowed or not found"),
		@ApiResponse(code = 403, message = "expired or invalid JWT token"),
		@ApiResponse(code = 404, message = "Cannot modify this arrive, maybe student is not found")
	})
	public ResponseEntity<Student> addArrive(@ApiParam("student code") @RequestParam String codeStudent, HttpServletRequest req) throws AttributeNotFoundException{
		ArrivalStudent arrive = new ArrivalStudent();
		arrive.setArrive(LocalDateTime.now());
		studentService.modifyStudentArrive(codeStudent, arrive, AccessModification.ADD, req);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(codeStudent).toUri();
		return ResponseEntity.created(uri).build();
	}
	
	@PostMapping("/modify/remove/arrive")
	@ApiOperation(value="${Rest.Student.removeArrive.value}", notes="${Rest.Student.removeArrive.note}", response=ResponseEntity.class
			, authorizations= {@Authorization(value="apiKey")})
	@ApiResponses({
		@ApiResponse(code = 401, message = "authority not allowed or not found"),
		@ApiResponse(code = 403, message = "expired or invalid JWT token"),
		@ApiResponse(code = 404, message = "Cannot modify this arrive, maybe student is not found")
	})
	public ResponseEntity<Student> removeArrive(@ApiParam("arrival student  id") @RequestParam Long arrive, HttpServletRequest req) throws AttributeNotFoundException{
		ArrivalStudent arrives = studentService.getArrive(arrive, false, req);
		studentService.modifyStudentArrive(null, arrives, AccessModification.DELETE, req);
		return ResponseEntity.ok(null);
	}
	
	@DeleteMapping("/delete")
	@ApiOperation(value="${Rest.Student.delete.value}", notes="${Rest.Student.delete.note}", response=String.class
			, authorizations= {@Authorization(value="apiKey")})
	@ApiResponses({
		@ApiResponse(code = 401, message = "authority not allowed or not found"),
		@ApiResponse(code = 403, message = "expired or invalid JWT token"),
		@ApiResponse(code = 404, message = "Student not found !")
	})
	public String delete(@ApiParam("deleted student") @RequestBody Student student, HttpServletRequest req) throws AttributeNotFoundException {
		studentService.delete(student, req);
		return "Student has been deleted";
	}
}
