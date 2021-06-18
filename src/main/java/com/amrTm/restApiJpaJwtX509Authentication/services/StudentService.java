package com.amrTm.restApiJpaJwtX509Authentication.services;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.EntityNotFoundException;
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
import com.amrTm.restApiJpaJwtX509Authentication.repo.ArriveStudentRepo;
import com.amrTm.restApiJpaJwtX509Authentication.repo.LessonRepo;
import com.amrTm.restApiJpaJwtX509Authentication.repo.StudentRepo;
import com.amrTm.restApiJpaJwtX509Authentication.repo.TeacherRepo;

@Service
public class StudentService {
	
	private StudentRepo studentRepo;
	private TeacherRepo teacherRepo; 
	private ArriveStudentRepo arriveStudentRepo;
	private LessonRepo lessonRepo; 
	
	public StudentService(StudentRepo studentRepo, ArriveStudentRepo arriveStudentRepo,
			LessonRepo lessonRepo, TeacherRepo teacherRepo) {
		super();
		this.studentRepo = studentRepo;
		this.arriveStudentRepo = arriveStudentRepo;
		this.lessonRepo = lessonRepo;
		this.teacherRepo = teacherRepo;
	}

	public List<Student> getAll() {
		return studentRepo.findAll(Sort.by(Order.asc("first")));
	}
	
	public List<Student> getAll(List<String> studentCode){
		return studentRepo.findAllById(studentCode);
	}

	public Student get(String code) throws AttributeNotFoundException{
		return studentRepo.findById(code).orElseThrow(()->new AttributeNotFoundException("Student cannot found !"));
	}
	
	public Lesson getLesson(String codeLesson) throws AttributeNotFoundException{
		return lessonRepo.findById(codeLesson).orElseThrow(()->new AttributeNotFoundException("Lesson cannot found !"));
	}
	
	public List<Lesson> getLessonByType(String typeLesson){
		return lessonRepo.findAllByTypeLesson(typeLesson);
	}
	
	public List<Lesson> getAllLesson(){
		return lessonRepo.findAll(Sort.by(Order.asc("typeLesson"),Order.asc("lesson")));
	}

	public List<ArrivalStudent> getArrive(LocalDateTime start, LocalDateTime end){
		return arriveStudentRepo.findArriveBeetween(start, end);
	}
	
	public ArrivalStudent getArrive(Long id, boolean jpa) throws EntityNotFoundException{
		if (jpa)
			return arriveStudentRepo.getById(id);
		else
			return arriveStudentRepo.findById(id).get();
	}
	
//	========================================================================================
	
	public void save(Student student) throws SaveAttributeException{
		try {
			studentRepo.saveAndFlush(student);
		}
		catch(Exception w) {throw new SaveAttributeException("Cannot saving this student, please check your input /or maybe this student has been saved");}
	}
	
	public void saveAll(List<Student> students) throws SaveAttributeException {
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
	
	public Student modify(Student student, String code) throws SaveAttributeException{
		try {
			studentRepo.update(student.getFirst(), student.getLast(), student.getGender(), student.getEmail(), code);
			return studentRepo.findById(code).get();
		}
		catch(Exception e) {
			throw new SaveAttributeException("Cannot modify this student, please check your input /or maybe email has been same with the other student");
		}
	}
	
	public void modifyAll(Iterable<Student> student) throws SaveAttributeException{
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
	public boolean modifyTeacher(String codeteacher, String codeStudent,
			AccessModification access)/* throws SaveAttributeException */ {	
//		try {
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
//		}catch(Exception e) {
//			throw new SaveAttributeException("Cannot modify this teacher, maybe student /or teacher is not found");
//		}
	}
	
	@Transactional
	public boolean modifyStudentLesson(String codeStudent, String codelesson,
			AccessModification access) throws SaveAttributeException {
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
			throw new SaveAttributeException("Cannot modify this study, maybe study code has been already /or study is not found");
		}
	}
	
	@Transactional
	public boolean modifyStudentArrive(String codeStudent, ArrivalStudent arrive, AccessModification access) throws SaveAttributeException {
		try {
			if(access.equals(AccessModification.ADD)) {
				Student hg = studentRepo.getById(codeStudent);
	
				arrive.setStudentArrive(hg);
				hg.getStudentsArrive().add(arrive);
	
				arriveStudentRepo.saveAndFlush(arrive);
				return true;
			}
			else {
				arriveStudentRepo.delete(arrive);
				return true;
			}
		}catch(Exception e) {
			throw new SaveAttributeException("Cannot modify this arrive, maybe student is not found");
		}
	}
	
	@Transactional
	@PreAuthorize("hasAuthority('ADMIN')")
	public void delete(Student student) throws AttributeNotFoundException {
		try {
			studentRepo.delete(student);
		}
		catch(Exception e) {
			throw new AttributeNotFoundException("Student not found !");
		}
	}
}
