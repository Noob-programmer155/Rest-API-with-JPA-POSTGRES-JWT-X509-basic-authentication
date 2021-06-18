package com.amrTm.restApiJpaJwtX509Authentication.controller;

import java.net.URI;
import java.util.List;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.User;
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

import com.amrTm.restApiJpaJwtX509Authentication.DTO.AdminDTO;
import com.amrTm.restApiJpaJwtX509Authentication.entity.Admin;
import com.amrTm.restApiJpaJwtX509Authentication.entity.Lesson;
import com.amrTm.restApiJpaJwtX509Authentication.entity.Student;
import com.amrTm.restApiJpaJwtX509Authentication.entity.Teacher;
import com.amrTm.restApiJpaJwtX509Authentication.exception.AttributeNotFoundException;
import com.amrTm.restApiJpaJwtX509Authentication.exception.SaveAttributeException;
import com.amrTm.restApiJpaJwtX509Authentication.mailConfig.MessageObject;
import com.amrTm.restApiJpaJwtX509Authentication.services.AccessModification;
import com.amrTm.restApiJpaJwtX509Authentication.services.AdminService;

@RestController
@RequestMapping("/firns")
public class RestAdmin {
	
	@Autowired
	private AdminService adminService;
	
	@Autowired
	private ModelMapper modelMapper; 
	
	@GetMapping("/info-admin")
	@PreAuthorize("hasAuthority('ADMIN')")
	public String getAuthAdmin() {
		return (String) adminService.getAdminActive().getPrincipal();
	}
	
	@PostMapping("/signin")
	public String signin(@RequestParam String username, @RequestParam String password, HttpServletResponse res, HttpServletRequest req){
		return adminService.signInAdmin(username, password, res, req);
	}
	
	@PostMapping("/signup")
	public String signup(@RequestBody AdminDTO admin) throws MessagingException{
		return adminService.signUpAdmin(modelMapper.map(admin, Admin.class));
	}
	
	@GetMapping("/signup/confirm")
	public String validation(@RequestParam String sa,@RequestParam String nm , HttpServletResponse res, HttpServletRequest req) {
		if(adminService.validateLink(sa, nm, res, req))
			return "Success to validating";
		return "Validating failed";
	}
	
	@GetMapping("/refresh")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<User> refresh(HttpServletResponse res, HttpServletRequest req) {
		String oi = (String) adminService.getAdminActive().getPrincipal();
		adminService.refresh(oi, res, req);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().buildAndExpand(oi).toUri();
		return ResponseEntity.created(uri).build();
	}
	
	@PostMapping("/send-message")
	public String send(@RequestBody MessageObject msg) throws MessagingException {
		return adminService.sendMessage(msg.getTo(), msg.getFrom(), msg.getSubject(), msg.getText());
	}
	
	@PostMapping("/save/lesson")
	public ResponseEntity<Lesson> saveLesson(@RequestBody Lesson lesson) throws SaveAttributeException{
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(lesson.getCodeLesson()).toUri();
		adminService.saveLesson(lesson);
		return ResponseEntity.created(uri).build();
	}
	
	@PostMapping("/modify/lesson")
	public ResponseEntity<Lesson> modifyLesson(@RequestParam String codeStudent, @RequestParam Lesson lesson,
			@PathVariable AccessModification access) throws SaveAttributeException, AttributeNotFoundException{
		Lesson jh = adminService.modifyLesson(lesson);
		return ResponseEntity.ok(jh);
	}
	
	@PostMapping("/save/all/lesson")
	public ResponseEntity<Lesson> saveAllLesson(@RequestBody List<Lesson> lesson) throws SaveAttributeException{
		adminService.saveAllLesson(lesson);
		return ResponseEntity.ok(null);
	}
	
	@PutMapping("/modify")
	public ResponseEntity<Teacher> modifyAdmin(@RequestBody Admin admin) throws SaveAttributeException{
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(admin.getId()).toUri();
		adminService.modify(admin);
		return ResponseEntity.created(uri).build();
	}
	
	@DeleteMapping("/deleteLesson")
	public boolean delete(String codeLesson) throws AttributeNotFoundException {
		adminService.delete(codeLesson);
		return true;
	}
	
	@Bean
	public ModelMapper modelMaper() {
		return new ModelMapper();
	}
}
