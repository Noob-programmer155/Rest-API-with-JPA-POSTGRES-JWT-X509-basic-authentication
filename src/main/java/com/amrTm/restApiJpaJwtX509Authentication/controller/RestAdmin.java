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
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.amrTm.restApiJpaJwtX509Authentication.DTO.AdminDTO;
import com.amrTm.restApiJpaJwtX509Authentication.entity.Admin;
import com.amrTm.restApiJpaJwtX509Authentication.entity.Lesson;
import com.amrTm.restApiJpaJwtX509Authentication.entity.Teacher;
import com.amrTm.restApiJpaJwtX509Authentication.exception.AttributeNotFoundException;
import com.amrTm.restApiJpaJwtX509Authentication.exception.SaveAttributeException;
import com.amrTm.restApiJpaJwtX509Authentication.services.AdminService;
import com.amrTm.restApiJpaJwtX509Authentication.services.MessageObject;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;

@RestController
@CrossOrigin
@RequestMapping("/firns")
@Api(tags="Admin")
public class RestAdmin {
	
	@Autowired
	private AdminService adminService;
	
	@Autowired
	private ModelMapper modelMapper; 
	
	@GetMapping("/info-admin")
	@ApiOperation(value="${Rest.Admin.getadmin.value}", notes="${Rest.Admin.getadmin.note}", response=String.class
			, authorizations= {@Authorization(value="apiKey")})
	@ApiResponses({
		@ApiResponse(code = 401, message = "authority not allowed or not found"),
		@ApiResponse(code = 403, message = "expired or invalid JWT token")
	})
	public String getAuthAdmin(HttpServletRequest req) {
		return (String) adminService.getAdminActive(req).getPrincipal();
	}
	
	@PostMapping("/signin")
	@ApiOperation(value="${Rest.Admin.signin.value}", notes="${Rest.Admin.signin.note}", response=String.class)
	@ApiResponses({
		@ApiResponse(code = 401, message = "authority not allowed or not found")
	})
	public String signin(@ApiParam("username") @RequestParam String username, 
						 @ApiParam("password") @RequestParam String password, HttpServletResponse res, HttpServletRequest req){
		return adminService.signInAdmin(username, password, res, req);
	}
	
	@PostMapping("/signup")
	@ApiOperation(value="${Rest.Admin.signup.value}", notes="${Rest.Admin.signup.note}", response=String.class)
	@ApiResponses({
		@ApiResponse(code = 500, message = "message can`t send"),
		@ApiResponse(code = 304, message = "cannot save, maybe username or email already exist, please use other username or email")
	})
	public String signup(@ApiParam("admin model") @RequestBody AdminDTO admin) throws MessagingException, SaveAttributeException{
		return adminService.signUpAdmin(modelMapper.map(admin, Admin.class));
	}
	
	@GetMapping("/signup/confirm")
	@ApiOperation(value="${Rest.Admin.signupresponse.value}", notes="${Rest.Admin.signupresponse.note}", response=String.class)
	@ApiResponses({
		@ApiResponse(code = 500, message = "message can`t send")
	})
	public String validation(@ApiParam("") @RequestParam String sa,
							@ApiParam("") @RequestParam String nm , HttpServletResponse res, HttpServletRequest req) throws MessagingException {
		return adminService.validateLink(sa, nm, res, req);
	}
	
	@PostMapping("/refresh")
	@ApiOperation(value="${Rest.Admin.refresh.value}", notes="${Rest.Admin.refresh.note}", response=ResponseEntity.class
			, authorizations= {@Authorization(value="apiKey")})
	@ApiResponses({
		@ApiResponse(code = 401, message = "authority not allowed or not found")
	})
	public ResponseEntity<User> refresh(HttpServletResponse res, HttpServletRequest req) {
		adminService.refresh(req.getRemoteUser(), res, req);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().buildAndExpand(req.getRemoteUser()).toUri();
		return ResponseEntity.created(uri).build();
	}
	
	@PostMapping("/send-message")
	@ApiOperation(value="${Rest.Admin.send.value}", notes="${Rest.Admin.send.note}", response=String.class
			, authorizations= {@Authorization(value="apiKey")})
	@ApiResponses({
		@ApiResponse(code = 401, message = "authority not allowed or not found"),
		@ApiResponse(code = 403, message = "expired or invalid JWT token"),
		@ApiResponse(code = 500, message = "message can`t send")
	})
	public String send(@ApiParam("message") @RequestBody MessageObject msg, HttpServletRequest req) throws MessagingException {
		return adminService.sendMessage(msg.getTo(), msg.getFrom(), msg.getSubject(), msg.getText(), req);
	}
	
	@PostMapping("/save/lesson")
	@ApiOperation(value="${Rest.Admin.savelesson.value}", notes="${Rest.Admin.savelesson.note}", response=ResponseEntity.class
			, authorizations= {@Authorization(value="apiKey")})
	@ApiResponses({
		@ApiResponse(code = 401, message = "authority not allowed or not found"),
		@ApiResponse(code = 403, message = "expired or invalid JWT token"),
		@ApiResponse(code = 304, message = "Cannot saving this study, maybe study with the same code and name has been saved")
	})
	public ResponseEntity<Lesson> saveLesson(@ApiParam("lesson study") @RequestBody Lesson lesson, HttpServletRequest req) throws SaveAttributeException{
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(lesson.getCodeLesson()).toUri();
		adminService.saveLesson(lesson, req);
		return ResponseEntity.created(uri).build();
	}
	
	@PostMapping("/modify/lesson")
	@ApiOperation(value="${Rest.Admin.savelesson.value}", notes="${Rest.Admin.savelesson.note}", response=ResponseEntity.class
			, authorizations= {@Authorization(value="apiKey")})
	@ApiResponses({
		@ApiResponse(code = 401, message = "authority not allowed or not found"),
		@ApiResponse(code = 403, message = "expired or invalid JWT token"),
		@ApiResponse(code = 304, message = "Cannot saving this study, maybe study with the same code and name has been saved")
	})
	public ResponseEntity<Lesson> modifyLesson(@ApiParam("modified lesson") @RequestBody Lesson lesson, HttpServletRequest req) throws SaveAttributeException {
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(lesson.getCodeLesson()).toUri();
		adminService.modifyLesson(lesson, req);
		return ResponseEntity.created(uri).build();
	}
	
	@PostMapping("/save/all/lesson")
	@ApiOperation(value="${Rest.Admin.savealllesson.value}", notes="${Rest.Admin.savealllesson.note}", response=ResponseEntity.class
			, authorizations= {@Authorization(value="apiKey")})
	@ApiResponses({
		@ApiResponse(code = 401, message = "authority not allowed or not found"),
		@ApiResponse(code = 403, message = "expired or invalid JWT token"),
		@ApiResponse(code = 304, message = "Cannot saving this study, maybe study with the same code and name has been saved")
	})
	public ResponseEntity<Lesson> saveAllLesson(@ApiParam("lesson studies") @RequestBody List<Lesson> lesson, HttpServletRequest req) throws SaveAttributeException{
		adminService.saveAllLesson(lesson, req);
		return ResponseEntity.ok(null);
	}
	
	@DeleteMapping("/delete/Lesson")
	@ApiOperation(value="${Rest.Admin.deletelesson.value}", notes="${Rest.Admin.deletelesson.note}", response=Boolean.class
			, authorizations= {@Authorization(value="apiKey")})
	@ApiResponses({
		@ApiResponse(code = 401, message = "authority not allowed or not found"),
		@ApiResponse(code = 403, message = "expired or invalid JWT token"),
		@ApiResponse(code = 404, message = "Lesson not found !")
	})
	public boolean delete(@ApiParam("lesson code") @RequestParam String codeLesson, HttpServletRequest req) throws AttributeNotFoundException {
		adminService.delete(codeLesson, req);
		return true;
	}
	
	@PostMapping("/modify")
	@ApiOperation(value="${Rest.Admin.modifylesson.value}", notes="${Rest.Admin.modifylesson.note}", response=ResponseEntity.class
			, authorizations= {@Authorization(value="apiKey")})
	@ApiResponses({
		@ApiResponse(code = 401, message = "authority not allowed or not found"),
		@ApiResponse(code = 403, message = "expired or invalid JWT token"),
		@ApiResponse(code = 304, message = "cannot modify, maybe username or email already exist, please use other username or email")
	})
	public ResponseEntity<Teacher> modifyAdmin(@ApiParam("admin") @RequestBody Admin admin, HttpServletRequest req) throws SaveAttributeException{
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(admin.getId()).toUri();
		adminService.modify(admin, req);
		return ResponseEntity.created(uri).build();
	}
	
	@DeleteMapping("/delete")
	@ApiOperation(value="${Rest.Admin.deleteadmin.value}", notes="${Rest.Admin.deleteadmin.note}", response=Boolean.class
			, authorizations= {@Authorization(value="apiKey")})
	@ApiResponses({
		@ApiResponse(code = 401, message = "authority not allowed or not found"),
		@ApiResponse(code = 403, message = "expired or invalid JWT token"),
		@ApiResponse(code = 404, message = "admin not found !")
	})
	public boolean delete(@ApiParam("admin") @RequestBody Admin admin, HttpServletRequest req) throws AttributeNotFoundException {
		adminService.deletes(admin, req);
		return true;
	}
	
	@Bean
	public ModelMapper modelMaper() {
		return new ModelMapper();
	}
}
