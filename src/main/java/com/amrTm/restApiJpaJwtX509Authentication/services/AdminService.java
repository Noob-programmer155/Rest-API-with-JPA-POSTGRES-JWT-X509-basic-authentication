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
import com.amrTm.restApiJpaJwtX509Authentication.mailConfig.MailService;
import com.amrTm.restApiJpaJwtX509Authentication.repo.AdminRepo;
import com.amrTm.restApiJpaJwtX509Authentication.repo.LessonEntity;
import com.amrTm.restApiJpaJwtX509Authentication.repo.LessonRepo;
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
	
	public Authentication getAdminActive() {
		Authentication details = SecurityContextHolder.getContext().getAuthentication();
		return details;
	}

	public String signInAdmin(String username, String password, HttpServletResponse res, HttpServletRequest req) throws AuthenticationException {
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username,password));
		tokenProvider.createToken(username, adminRepo.findByUsername(username).getEmail(), adminRepo.findByUsername(username).getRole(), res,req);
		return "Signin Success";
	}
	
	public String signUpAdmin(Admin admin) throws MessagingException {
		if(!adminRepo.existsByUsername(admin.getUsername())) {
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
		return null;
	}
	
	public boolean refresh(String username,HttpServletResponse res, HttpServletRequest req) {
		return tokenProvider.createToken(username, adminRepo.findByUsername(username).getEmail(), adminRepo.findByUsername(username).getRole(),res,req);
	}
	
	public boolean validateLink(String token, String name, HttpServletResponse res, HttpServletRequest req) {
		if(tokenProvider.validateToken(token)) {
			Admin admin = adminRepo.findByUsername(tokenProvider.getAuth(token).getName());
			admin.setValidation(true);
			adminRepo.save(admin);
			tokenProvider.createToken(admin.getUsername(), admin.getEmail(), admin.getRole(), res, req);
			return true;
		}
		else {
			Admin admin = adminRepo.findByUsername(name);
			admin.setValidation(true);
			adminRepo.save(admin);
			tokenProvider.createToken(admin.getUsername(), admin.getEmail(), admin.getRole(), res, req);
			return true;
		}
	}
	
	public String sendMessage(String to, String from, String subject, String text) throws MessagingException {
		return mailService.sendMessage(to, from, subject, text);
	}
	
	public Lesson modifyLesson(Lesson lesson) {
		return lessonRepo.saveAndFlush(lesson);
	}
	
	public void saveLesson(Lesson lesson) throws SaveAttributeException {
		try {
			lessonRepo.saveAndFlush(lesson);
		}
		catch(Exception e) {
			throw new SaveAttributeException("Cannot saving this study, maybe study with the same code and name has been saved");
		}
	}
	
	public void saveAllLesson(List<Lesson> lessons) throws SaveAttributeException {
		try {
			lessonRepo.saveAllAndFlush(lessons);
		}
		catch(Exception e) {throw new SaveAttributeException("Cannot saving this studies, maybe study with the same code and name has been saved");}
	}
	
	@PreAuthorize("hasAuthority('ADMIN')")
	public boolean modify(Admin admin) throws SaveAttributeException{
		try {
			adminRepo.saveAndFlush(admin);
			return true;
		}
		catch(Exception e) {
			throw new SaveAttributeException("Cannot modify this student, please check your input /or maybe email has been same with the other student");
		}
	}
	
	@Transactional
	@PreAuthorize("hasAuthority('ADMIN')")
	public void delete(String codeLesson) throws AttributeNotFoundException {
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
}
