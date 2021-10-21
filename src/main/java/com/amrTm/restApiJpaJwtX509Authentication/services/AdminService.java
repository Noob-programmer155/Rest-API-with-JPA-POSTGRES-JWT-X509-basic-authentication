package com.amrTm.restApiJpaJwtX509Authentication.services;

import java.util.ArrayList;
import java.util.List;

import javax.mail.MessagingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.amrTm.restApiJpaJwtX509Authentication.entity.Admin;
import com.amrTm.restApiJpaJwtX509Authentication.entity.Lesson;
import com.amrTm.restApiJpaJwtX509Authentication.entity.Role;
import com.amrTm.restApiJpaJwtX509Authentication.entity.Student;
import com.amrTm.restApiJpaJwtX509Authentication.entity.Teacher;
import com.amrTm.restApiJpaJwtX509Authentication.exception.AttributeNotFoundException;
import com.amrTm.restApiJpaJwtX509Authentication.exception.SaveAttributeException;
import com.amrTm.restApiJpaJwtX509Authentication.repository.AdminRepo;
import com.amrTm.restApiJpaJwtX509Authentication.repository.LessonEntity;
import com.amrTm.restApiJpaJwtX509Authentication.repository.LessonRepo;
import com.amrTm.restApiJpaJwtX509Authentication.security.TokenProvider;

@Service
public class AdminService implements LessonEntity{
	private TokenProvider tokenProvider;
	private AuthenticationManager authenticationManager;
	private AdminRepo adminRepo;
	private LessonRepo lessonRepo;
	private MailService mailService;
	
	@PersistenceContext
	private EntityManager entityManager;
	
	public AdminService(TokenProvider tokenProvider, AuthenticationManager authenticationManager, AdminRepo adminRepo, MailService mailService, LessonRepo lessonRepo) {
		super();
		this.tokenProvider = tokenProvider;
		this.authenticationManager = authenticationManager;
		this.adminRepo = adminRepo;
		this.lessonRepo = lessonRepo;
		this.mailService = mailService;
	}
	
	@PreAuthorize("hasAuthority('ADMIN')")
	public Authentication getAdminActive(HttpServletRequest req) {
		tokenProvider.validateToken(req);
		Authentication details = SecurityContextHolder.getContext().getAuthentication();
		return details;
	}

	public String signInAdmin(String username, String password, HttpServletResponse res, HttpServletRequest req) throws AuthenticationException {
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username,password));
		tokenProvider.createToken(username, adminRepo.findByUsername(username).getEmail(), adminRepo.findByUsername(username).getRole(), res,req);
		return "Signin Success";
	}
	
	public String signUpAdmin(Admin admin) throws MessagingException, SaveAttributeException {
		if(!adminRepo.existsByUsernameAndEmail(admin.getUsername(),admin.getEmail())) {
			List<Role> jh = new ArrayList<>();
			jh.add(Role.ADMIN);
			jh.add(Role.USER);
			admin.setRole(jh);
			admin.setPassword(new BCryptPasswordEncoder().encode(admin.getPassword()));
			admin.setValidation(false);
			adminRepo.saveAndFlush(admin);
			String token = tokenProvider.setToken(admin.getUsername(),admin.getEmail(), admin.getRole());
			String s = mailService.sendValidationMessage(admin.getEmail(), admin.getUsername(), token);
			return s;
		}
		throw new SaveAttributeException("cannot save, maybe username or email already exist, please use other username or email");
	}
	
	@PreAuthorize("hasAuthority('ADMIN')")
	public boolean refresh(String username,HttpServletResponse res, HttpServletRequest req) {
		return tokenProvider.createToken(username, adminRepo.findByUsername(username).getEmail(), adminRepo.findByUsername(username).getRole(),res,req);
	}
	
	public String validateLink(String token, String name, HttpServletResponse res, HttpServletRequest req) throws MessagingException {
		if(tokenProvider.validateToken(token)) {
			Admin admin = adminRepo.findByUsername(tokenProvider.getAuth(token).getName());
			admin.setValidation(true);
			adminRepo.save(admin);
			tokenProvider.createToken(admin.getUsername(), admin.getEmail(), admin.getRole(), res, req);
			return "validating success";
		}
		else {
			Admin admin = adminRepo.findByUsername(name);
			String t = tokenProvider.setToken(admin.getUsername(), admin.getEmail(), admin.getRole());
			mailService.sendValidationMessage(admin.getEmail(), admin.getUsername(), t);
			return "validating failed, we got a new ones for you, check your email";
		}
	}
	
	@PreAuthorize("hasAuthority('ADMIN')")
	public String sendMessage(String to, String from, String subject, String text, HttpServletRequest req) throws MessagingException {
		tokenProvider.validateToken(req);
		return mailService.sendMessage(to, from, subject, text);
	}
	
	@PreAuthorize("hasAuthority('ADMIN')")
	public boolean modifyLesson(Lesson lesson, HttpServletRequest req) throws SaveAttributeException {
		tokenProvider.validateToken(req);
		try {
			lessonRepo.saveAndFlush(lesson);
			return true;
		}
		catch(Exception e) {
			throw new SaveAttributeException("Cannot saving this study, maybe study with the same code and name has been saved");
		}
	}
	
	@PreAuthorize("hasAuthority('ADMIN')")
	public void saveLesson(Lesson lesson, HttpServletRequest req) throws SaveAttributeException {
		tokenProvider.validateToken(req);
		try {
			lessonRepo.saveAndFlush(lesson);
		}
		catch(Exception e) {
			throw new SaveAttributeException("Cannot saving this study, maybe study with the same code and name has been saved");
		}
	}
	
	@PreAuthorize("hasAuthority('ADMIN')")
	public void saveAllLesson(List<Lesson> lessons, HttpServletRequest req) throws SaveAttributeException {
		tokenProvider.validateToken(req);
		try {
			lessonRepo.saveAllAndFlush(lessons);
		}
		catch(Exception e) {throw new SaveAttributeException("Cannot saving this studies, maybe study with the same code and name has been saved");}
	}
	
	@PreAuthorize("hasAuthority('MANAGER')")
	public boolean modify(Admin admin, HttpServletRequest req) throws SaveAttributeException{
		tokenProvider.validateToken(req);
		try {
			adminRepo.saveAndFlush(admin);
			return true;
		}
		catch(Exception e) {
			throw new SaveAttributeException("cannot modify, maybe username or email already exist, please use other username or email");
		}
	}
	
	@Transactional
	@PreAuthorize("hasAuthority('ADMIN')")
	public void delete(String codeLesson, HttpServletRequest req) throws AttributeNotFoundException {
		tokenProvider.validateToken(req);
		try {
			Lesson lesson = entityManager.find(Lesson.class, codeLesson);
			for(Student s  : lesson.getStudentLesson()) {
				s.removeLesson(lesson);
			}
			for(Teacher t : lesson.getTeacherLesson()) {
				t.removeLesson(lesson);
			}
			entityManager.remove(lesson);
		}
		catch(Exception e) {
			throw new AttributeNotFoundException("Lesson not found !");
		}
	}

	@PreAuthorize("hasAuthority('MANAGER')")
	public void deletes(Admin admin, HttpServletRequest req) throws IllegalArgumentException{
		tokenProvider.validateToken(req);
		adminRepo.delete(admin);
	}
}
