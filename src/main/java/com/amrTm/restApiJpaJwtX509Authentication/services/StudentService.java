package com.amrTm.restApiJpaJwtX509Authentication.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.hibernate.Hibernate;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;

import com.amrTm.restApiJpaJwtX509Authentication.entity.ArrivalStudent;
import com.amrTm.restApiJpaJwtX509Authentication.entity.StudentLesson;
import com.amrTm.restApiJpaJwtX509Authentication.entity.Student;
import com.amrTm.restApiJpaJwtX509Authentication.entity.Teacher;
import com.amrTm.restApiJpaJwtX509Authentication.repo.ArriveStudentRepo;
import com.amrTm.restApiJpaJwtX509Authentication.repo.LessonStudentRepo;
import com.amrTm.restApiJpaJwtX509Authentication.repo.StudentRepo;
import com.amrTm.restApiJpaJwtX509Authentication.repo.TeacherRepo;

@Service
public class StudentService {
	
	private StudentRepo studentRepo;
	private ArriveStudentRepo arriveStudentRepo;
	private LessonStudentRepo lessonStudentRepo; 
	
	public StudentService(StudentRepo studentRepo, ArriveStudentRepo arriveStudentRepo,
			LessonStudentRepo lessonStudentRepo) {
		super();
		this.studentRepo = studentRepo;
		this.arriveStudentRepo = arriveStudentRepo;
		this.lessonStudentRepo = lessonStudentRepo;
	}

	public List<Student> getAll(){
		return studentRepo.findAll(Sort.by(Order.asc("first")));
	}
	
	public List<Student> getAll(List<String> studentCode){
		return studentRepo.findAllByStudentCode(studentCode);
	}

	public Optional<Student> get(String code){
		return studentRepo.findByStudentCode(code);
	}
	
	public Optional<StudentLesson> getLesson(String codeLesson){
		return lessonStudentRepo.findByCodeLesson(codeLesson);
	}
	
	public List<StudentLesson> getLessonByType(String typeLesson){
		return lessonStudentRepo.findAllByTypeLesson(typeLesson);
	}
	
	public List<StudentLesson> getAllLesson(){
		return lessonStudentRepo.findAll(Sort.by(Order.asc("typeLesson"),Order.asc("lesson")));
	}

	public List<ArrivalStudent> getArrive(LocalDateTime start, LocalDateTime end){
		return arriveStudentRepo.findArriveBeetween(start, end);
	}
	
	public ArrivalStudent getArrive(Long id) {
		return arriveStudentRepo.getById(id);
	}
	
//	========================================================================================
	
	public boolean save(Student student){
		try {
			studentRepo.save(student);
			return true;
		}
		catch(Exception w) {return false;}
	}
	
	public boolean saveAll(List<Student> students) {
		try {
			studentRepo.saveAllAndFlush(students);
			return true;
		}
		catch(Exception e) {return false;}
	}
	
	public void saveLesson(StudentLesson lesson) {
		lessonStudentRepo.save(lesson);
	}
	
	public void saveAllLesson(List<StudentLesson> lessons) {
		lessonStudentRepo.saveAllAndFlush(lessons);
	}
	
	public void saveArrive(ArrivalStudent arrive) {
		arriveStudentRepo.save(arrive);
	}
	
	public boolean modify(Student student, String code){
		try {
			studentRepo.update(student.getFirst(), student.getLast(), student.getGender(), student.getEmail(), code);
			return true;
		}
		catch(Exception e) {
			return false;
		}
	}
	
	public boolean modifyAll(Iterable<Student> student){
		try {
			for (Student g : student) {
				studentRepo.update(g.getFirst(), g.getLast(), g.getGender(), g.getEmail(), g.getStudentCode());
			}
			return true;
		}
		catch(Exception e) {
			return false;
		}
	}
	
	@Transactional
	public boolean modifyStudentLesson(Student student, StudentLesson codelesson, AccessModification access) {
		if(access.equals(AccessModification.ADD)) {
//			StudentLesson mn = new StudentLesson();
//			mn.setId(codelesson.getId());
//			mn.setCodeLesson(codelesson.getCodeLesson());
//			mn.setLesson(codelesson.getLesson());
//			mn.setTypeLesson(codelesson.getTypeLesson());
			Student hg = new Student();
			hg.setId(student.getId());
			hg.setFirst(student.getFirst());
			hg.setLast(student.getLast());
			hg.setEmail(student.getEmail());
			hg.setGender(student.getGender());
			hg.setStudentCode(student.getStudentCode());
			studentRepo.save(hg);
			
			codelesson.getStudents().add(student);
			student.getStudentsLesson().add(codelesson);
			
			lessonStudentRepo.save(codelesson);
			return true;
		}
		else if(access.equals(AccessModification.MODIFY)) {
			lessonStudentRepo.save(codelesson);
			return true;
		}
		else {
			lessonStudentRepo.delete(codelesson);
			return true;
		}
	}
	
	@Transactional
	public boolean modifyStudentArrive(Student student, ArrivalStudent arrive, AccessModification access) {
		if(access.equals(AccessModification.ADD)) {
//			ArrivalStudent mn = new ArrivalStudent();
//			mn.setId(arrive.getId());
//			mn.setArrive(arrive.getArrive());	
			Student hg = new Student();
			hg.setId(student.getId());
			hg.setFirst(student.getFirst());
			hg.setLast(student.getLast());
			hg.setEmail(student.getEmail());
			hg.setGender(student.getGender());
			hg.setStudentCode(student.getStudentCode());
			studentRepo.save(hg);	
			
			arrive.getStudents().add(hg);
			hg.getStudentsArrive().add(arrive);

			arriveStudentRepo.save(arrive);
			return true;
		}
		else {
			arriveStudentRepo.delete(arrive);
			return true;
		}
	}
	
	public boolean delete(Student student) {
		try {
			studentRepo.delete(student);
			return true;
		}
		catch(Exception e) {
			return false;
		}
	}
}
