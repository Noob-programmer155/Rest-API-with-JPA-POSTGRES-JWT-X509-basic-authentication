package com.amrTm.restApiJpaJwtX509Authentication.services;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;

import com.amrTm.restApiJpaJwtX509Authentication.entity.ArrivalStudent;
import com.amrTm.restApiJpaJwtX509Authentication.entity.Student;
import com.amrTm.restApiJpaJwtX509Authentication.entity.StudentLesson;
import com.amrTm.restApiJpaJwtX509Authentication.entity.Teacher;
import com.amrTm.restApiJpaJwtX509Authentication.exception.AttributeNotFoundException;
import com.amrTm.restApiJpaJwtX509Authentication.exception.SaveAttributeException;
import com.amrTm.restApiJpaJwtX509Authentication.repo.ArriveStudentRepo;
import com.amrTm.restApiJpaJwtX509Authentication.repo.LessonStudentRepo;
import com.amrTm.restApiJpaJwtX509Authentication.repo.StudentRepo;
import com.amrTm.restApiJpaJwtX509Authentication.repo.TeacherRepo;

@Service
public class StudentService {
	
	private StudentRepo studentRepo;
	private TeacherRepo teacherRepo; 
	private ArriveStudentRepo arriveStudentRepo;
	private LessonStudentRepo lessonStudentRepo; 
	
	public StudentService(StudentRepo studentRepo, ArriveStudentRepo arriveStudentRepo,
			LessonStudentRepo lessonStudentRepo) {
		super();
		this.studentRepo = studentRepo;
		this.arriveStudentRepo = arriveStudentRepo;
		this.lessonStudentRepo = lessonStudentRepo;
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
	
	public StudentLesson getLesson(String codeLesson) throws AttributeNotFoundException{
		return lessonStudentRepo.findById(codeLesson).orElseThrow(()->new AttributeNotFoundException("Study cannot found !"));
	}
	
	public List<StudentLesson> getLessonOnStudent(String codeStudent){
		return studentRepo.findAllLessonOnStudentId(codeStudent).getStudentsLesson();
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
	
	public ArrivalStudent getArrive(Long id) throws EntityNotFoundException{
		return arriveStudentRepo.getById(id);
	}
	
//	========================================================================================
	
	public void save(Student student) throws SaveAttributeException{
		try {
			studentRepo.save(student);
		}
		catch(Exception w) {throw new SaveAttributeException("Cannot saving this student, please check your input /or maybe this student has been saved");}
	}
	
	public void saveAll(List<Student> students) throws SaveAttributeException {
		try {
			studentRepo.saveAllAndFlush(students);}
		catch(Exception e) {
			throw new SaveAttributeException("Cannot saving students, please check your input /or maybe there are students who have been saved");}
	}
	
	public void saveLesson(StudentLesson lesson) throws SaveAttributeException {
		try {
			lessonStudentRepo.save(lesson);}
		catch(Exception e) {
			throw new SaveAttributeException("Cannot saving this study, maybe study with the same code and name has been saved");
		}
	}
	
	public void saveAllLesson(List<StudentLesson> lessons) throws SaveAttributeException {
		try {
			lessonStudentRepo.saveAllAndFlush(lessons);}
		catch(Exception e) {throw new SaveAttributeException("Cannot saving this studies, maybe study with the same code and name has been saved");}
	}
	
//	public void saveArrive(ArrivalStudent arrive) throws SaveAttributeException {
//		try {
//			arriveStudentRepo.save(arrive);}
//		catch(Exception e) {
//			throw new SaveAttributeException("Cannot saving this arrive, please retry to input");
//		}
//	}
	
	public void modify(Student student, String code) throws SaveAttributeException{
		try {
			studentRepo.update(student.getFirst(), student.getLast(), student.getGender(), student.getEmail(), code);
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
	public void modifyTeacher(Teacher teacher, Student student, AccessModification access) throws SaveAttributeException {	
		try {
			if(access.equals(AccessModification.ADD)) {
				Student hg = new Student();
				hg.setStudentCode(student.getStudentCode());
				hg.setFirst(student.getFirst());
				hg.setLast(student.getLast());
				hg.setEmail(student.getEmail());
				hg.setGender(student.getGender());
				
				Teacher mn = new Teacher();
				mn.setCodeTeacher(teacher.getCodeTeacher());
				mn.setUsername(teacher.getUsername());
				mn.setGender(teacher.getGender());
				mn.setEmail(teacher.getEmail());
				teacherRepo.save(mn);
				
				mn.getStudents().add(hg);
				hg.getTeachers().add(mn);
				
				studentRepo.save(hg);
			}
			else {
				studentRepo.delete(student);
			}
		}catch(Exception e) {
			throw new SaveAttributeException("Cannot modify this teacher, maybe student /or teacher is not found");
		}
	}
	
	@Transactional
	public void modifyStudentLesson(Student student, StudentLesson codelesson, AccessModification access) throws SaveAttributeException {
		try {
			if(access.equals(AccessModification.ADD)) {
				StudentLesson mn = new StudentLesson();
				mn.setCodeLesson(codelesson.getCodeLesson());
				mn.setLesson(codelesson.getLesson());
				mn.setTypeLesson(codelesson.getTypeLesson());
				
				Student hg = new Student();
				hg.setStudentCode(student.getStudentCode());
				hg.setFirst(student.getFirst());
				hg.setLast(student.getLast());
				hg.setEmail(student.getEmail());
				hg.setGender(student.getGender());
				studentRepo.save(hg);
				
				codelesson.setStudents(hg);
				hg.getStudentsLesson().add(codelesson);
				
				lessonStudentRepo.save(codelesson);
			}
			else if(access.equals(AccessModification.MODIFY)) {
				lessonStudentRepo.save(codelesson);
			}
			else {
				lessonStudentRepo.delete(codelesson);
			}
		}catch(Exception e) {
			throw new SaveAttributeException("Cannot modify this study, maybe study code has been already /or study is not found");
		}
	}
	
	@Transactional
	public void modifyStudentArrive(Student student, ArrivalStudent arrive, AccessModification access) throws SaveAttributeException {
		try {
			if(access.equals(AccessModification.ADD)) {
	//			ArrivalStudent mn = new ArrivalStudent();
	//			mn.setId(arrive.getId());
	//			mn.setArrive(arrive.getArrive());	
				Student hg = new Student();
				hg.setStudentCode(student.getStudentCode());
				hg.setFirst(student.getFirst());
				hg.setLast(student.getLast());
				hg.setEmail(student.getEmail());
				hg.setGender(student.getGender());
				studentRepo.save(hg);	
				
				arrive.setStudentArrive(hg);
				hg.getStudentsArrive().add(arrive);
	
				arriveStudentRepo.save(arrive);
			}
			else {
				arriveStudentRepo.delete(arrive);
			}
		}catch(Exception e) {
			throw new SaveAttributeException("Cannot modify this arrive, maybe student is not found");
		}
	}
	
	@Transactional
	public void delete(Student student) throws AttributeNotFoundException {
		try {
			studentRepo.delete(student);
		}
		catch(Exception e) {
			throw new AttributeNotFoundException("Student not found !");
		}
	}
}
