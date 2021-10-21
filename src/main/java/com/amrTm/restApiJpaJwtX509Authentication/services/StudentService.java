package com.amrTm.restApiJpaJwtX509Authentication.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.amrTm.restApiJpaJwtX509Authentication.entity.ArrivalStudent;
import com.amrTm.restApiJpaJwtX509Authentication.entity.Student;
import com.amrTm.restApiJpaJwtX509Authentication.entity.Lesson;
import com.amrTm.restApiJpaJwtX509Authentication.entity.Teacher;
import com.amrTm.restApiJpaJwtX509Authentication.exception.AttributeNotFoundException;
import com.amrTm.restApiJpaJwtX509Authentication.exception.SaveAttributeException;
import com.amrTm.restApiJpaJwtX509Authentication.repository.ArriveStudentRepo;
import com.amrTm.restApiJpaJwtX509Authentication.repository.LessonRepo;
import com.amrTm.restApiJpaJwtX509Authentication.repository.StudentRepo;
import com.amrTm.restApiJpaJwtX509Authentication.repository.TeacherRepo;
import com.amrTm.restApiJpaJwtX509Authentication.security.TokenProvider;

@Service
public class StudentService {
	
	private StudentRepo studentRepo;
	private TeacherRepo teacherRepo; 
	private ArriveStudentRepo arriveStudentRepo;
	private LessonRepo lessonRepo; 
	private TokenProvider tokenProvider;
	
	public StudentService(StudentRepo studentRepo, ArriveStudentRepo arriveStudentRepo,
			LessonRepo lessonRepo, TeacherRepo teacherRepo, TokenProvider tokenProvider) {
		super();
		this.studentRepo = studentRepo;
		this.arriveStudentRepo = arriveStudentRepo;
		this.lessonRepo = lessonRepo;
		this.teacherRepo = teacherRepo;
		this.tokenProvider = tokenProvider;
	}
	
	@PreAuthorize("hasAnyAuthority('USER','ADMIN')")
	public List<Student> getAll(HttpServletRequest req) {
		tokenProvider.validateToken(req);
		return studentRepo.findAll(Sort.by(Order.asc("first")));
	}
	
	@PreAuthorize("hasAnyAuthority('USER','ADMIN')")
	public List<Student> getAll(List<String> studentCode, HttpServletRequest req){
		tokenProvider.validateToken(req);
		return studentRepo.findAllById(studentCode);
	}

	@PreAuthorize("hasAnyAuthority('USER','ADMIN')")
	public Student get(String code, HttpServletRequest req) throws AttributeNotFoundException{
		tokenProvider.validateToken(req);
		return studentRepo.findById(code).orElseThrow(()->new AttributeNotFoundException("Student cannot found !"));
	}
	
	@PreAuthorize("hasAnyAuthority('USER','ADMIN')")
	public Lesson getLesson(String codeLesson, HttpServletRequest req) throws AttributeNotFoundException{
		tokenProvider.validateToken(req);
		return lessonRepo.findById(codeLesson).orElseThrow(()->new AttributeNotFoundException("Lesson cannot found !"));
	}
	
	@PreAuthorize("hasAnyAuthority('USER','ADMIN')")
	public List<Lesson> getLessonByType(String typeLesson, HttpServletRequest req){
		tokenProvider.validateToken(req);
		return lessonRepo.findAllByTypeLesson(typeLesson);
	}
	
	@PreAuthorize("hasAnyAuthority('USER','ADMIN')")
	public List<Lesson> getAllLesson(HttpServletRequest req){
		tokenProvider.validateToken(req);
		return lessonRepo.findAll(Sort.by(Order.asc("typeLesson"),Order.asc("lesson")));
	}

	@PreAuthorize("hasAnyAuthority('USER','ADMIN')")
	public List<ArrivalStudent> getArrive(LocalDateTime start, LocalDateTime end, HttpServletRequest req){
		tokenProvider.validateToken(req);
		return arriveStudentRepo.findArriveBeetween(start, end);
	}
	
	@PreAuthorize("hasAnyAuthority('USER','ADMIN')")
	public ArrivalStudent getArrive(Long id, boolean jpa, HttpServletRequest req) throws EntityNotFoundException, NoSuchElementException{
		tokenProvider.validateToken(req);
		if (jpa)
			return arriveStudentRepo.getById(id);
		else
			return arriveStudentRepo.findById(id).get();
	}
	
//	========================================================================================
	
	@PreAuthorize("hasAuthority('ADMIN')")
	public void save(Student student, HttpServletRequest req) throws SaveAttributeException{
		tokenProvider.validateToken(req);
		try {
			studentRepo.saveAndFlush(student);
		}
		catch(Exception w) {throw new SaveAttributeException("Cannot saving this student, please check your input /or maybe this student has been saved");}
	}
	
	@PreAuthorize("hasAuthority('ADMIN')")
	public void saveAll(List<Student> students, HttpServletRequest req) throws SaveAttributeException {
		tokenProvider.validateToken(req);
		try {
			studentRepo.saveAllAndFlush(students);}
		catch(Exception e) {
			throw new SaveAttributeException("Cannot saving students, please check your input /or maybe there are students who have been saved");}
	}
	
//	public void saveLesson(Lesson lesson) throws SaveAttributeException {
//		try {
//			lessonStudentRepo.saveAndFlush(lesson);}
//		catch(Exception e) {
//			throw new SaveAttributeException("Cannot saving this study, maybe study with the same code and name has been saved");
//		}
//	}
	
//	public void saveAllLesson(List<Lesson> lessons) throws SaveAttributeException {
//		try {
//			lessonStudentRepo.saveAllAndFlush(lessons);}
//		catch(Exception e) {throw new SaveAttributeException("Cannot saving this studies, maybe study with the same code and name has been saved");}
//	}
	
//	public void saveArrive(ArrivalStudent arrive) throws SaveAttributeException {
//		try {
//			arriveStudentRepo.save(arrive);}
//		catch(Exception e) {
//			throw new SaveAttributeException("Cannot saving this arrive, please retry to input");
//		}
//	}
	
	@PreAuthorize("hasAnyAuthority('USER','ADMIN')")
	public Student modify(Student student, String code, HttpServletRequest req) throws SaveAttributeException{
		tokenProvider.validateToken(req);
		try {
			studentRepo.update(student.getFirst(), student.getLast(), student.getGender(), student.getEmail(), code);
			return studentRepo.findById(code).get();
		}
		catch(Exception e) {
			throw new SaveAttributeException("Cannot modify this student, please check your input /or maybe email has been same with the other student");
		}
	}
	
	@PreAuthorize("hasAnyAuthority('USER','ADMIN')")
	public void modifyAll(Iterable<Student> student, HttpServletRequest req) throws SaveAttributeException{
		tokenProvider.validateToken(req);
		try {
			for (Student g : student) {
				studentRepo.update(g.getFirst(), g.getLast(), g.getGender(), g.getEmail(), g.getStudentCode());
			}
		}
		catch(Exception e) {
			throw new SaveAttributeException("Cannot modify students, please check your input, /or maybe email has been same with the other student");
		}
	}
	
	@Transactional
	@PreAuthorize("hasAnyAuthority('USER','ADMIN')")
	public boolean modifyTeacher(String codeteacher, String codeStudent,
			AccessModification access, HttpServletRequest req) throws AttributeNotFoundException {	
		tokenProvider.validateToken(req);
		try {
			if(access.equals(AccessModification.ADD)) {
				Teacher teacher = teacherRepo.getById(codeteacher);
				Student hg = studentRepo.getById(codeStudent);
				
				teacher.addStudents(hg);
				
				studentRepo.saveAndFlush(hg);
				return true;
			}
			else {
				Teacher teacher = teacherRepo.getById(codeteacher);
				Student hg = studentRepo.getById(codeStudent);
				
				hg.removeTeacher(teacher);
				
				teacherRepo.saveAndFlush(teacher);
				return true;
			}
		}catch(Exception e) {
			throw new AttributeNotFoundException("Cannot modify this teacher, maybe student /or teacher is not found");
		}
	}
	
	@Transactional
	@PreAuthorize("hasAnyAuthority('USER','ADMIN')")
	public boolean modifyStudentLesson(String codeStudent, String codelesson,
			AccessModification access, HttpServletRequest req) throws AttributeNotFoundException {
		tokenProvider.validateToken(req);
		try {
			if(access.equals(AccessModification.ADD)) {
				Student hg = studentRepo.getById(codeStudent);
				Lesson mn = lessonRepo.getById(codelesson);
				
				mn.addLesson(hg);
				
				studentRepo.saveAndFlush(hg);
				return true;
			}
//			else if(access.equals(AccessModification.MODIFY)) {
//				lessonStudentRepo.saveAndFlush(lesson);
//				return true;
//			}
			else {
				Student hg = studentRepo.getById(codeStudent);
				Lesson mn = lessonRepo.getById(codelesson);
				
				hg.removeLesson(mn);
				
				lessonRepo.saveAndFlush(mn);
				return true;
			}
		}catch(Exception e) {
			throw new AttributeNotFoundException("Cannot modify this study, maybe study code has been already /or study is not found");
		}
	}
	
	@Transactional
	@PreAuthorize("hasAnyAuthority('USER','ADMIN')")
	public boolean modifyStudentArrive(String codeStudent, ArrivalStudent arrive, AccessModification access, HttpServletRequest req) throws AttributeNotFoundException {
		tokenProvider.validateToken(req);
		try {
			if(access.equals(AccessModification.ADD)) {
				Student hg = studentRepo.getById(codeStudent);
	
				arrive.setStudentArrive(hg);
				hg.getStudentsArrive().add(arrive);
	
				arriveStudentRepo.saveAndFlush(arrive);
				return true;
			}
			else {
				arriveStudentRepo.deleteByIdArrive(arrive.getIdArrive());
				return true;
			}
		}catch(Exception e) {
			throw new AttributeNotFoundException("Cannot modify this arrive, maybe student is not found");
		}
	}
	
	@Transactional
	@PreAuthorize("hasAuthority('ADMIN')")
	public void delete(Student student, HttpServletRequest req) throws AttributeNotFoundException {
		tokenProvider.validateToken(req);
		try {
			studentRepo.delete(student);
		}
		catch(Exception e) {
			throw new AttributeNotFoundException("Student not found !");
		}
	}
}
