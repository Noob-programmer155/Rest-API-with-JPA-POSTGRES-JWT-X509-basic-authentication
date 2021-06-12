package com.amrTm.restApiJpaJwtX509Authentication.services;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;

import com.amrTm.restApiJpaJwtX509Authentication.entity.ArrivalTeacher;
import com.amrTm.restApiJpaJwtX509Authentication.entity.Student;
import com.amrTm.restApiJpaJwtX509Authentication.entity.TeacherLesson;
import com.amrTm.restApiJpaJwtX509Authentication.exception.AttributeNotFoundException;
import com.amrTm.restApiJpaJwtX509Authentication.exception.SaveAttributeException;
import com.amrTm.restApiJpaJwtX509Authentication.entity.Teacher;
import com.amrTm.restApiJpaJwtX509Authentication.repo.ArriveTeacherRepo;
import com.amrTm.restApiJpaJwtX509Authentication.repo.LessonTeacherRepo;
import com.amrTm.restApiJpaJwtX509Authentication.repo.TeacherRepo;
import com.amrTm.restApiJpaJwtX509Authentication.repo.TeacherRepoEntity;

@Service
public class TeacherService implements TeacherRepoEntity{
	private TeacherRepo teacherRepo; 
	private ArriveTeacherRepo arriveTeacherRepo;
	private LessonTeacherRepo lessonTeacherRepo;
	
	@PersistenceContext
	private EntityManager entityManager;
	
	public TeacherService(TeacherRepo teacherRepo, ArriveTeacherRepo arriveTeacherRepo,
			LessonTeacherRepo lessonTeacherRepo) {
		super();
		this.teacherRepo = teacherRepo;
		this.arriveTeacherRepo = arriveTeacherRepo;
		this.lessonTeacherRepo = lessonTeacherRepo;
	}

	public List<Teacher> getAll(){
		return teacherRepo.findAll(Sort.by(Order.asc("first")));
	}
	
	public List<Teacher> getAll(List<String> TeacherCode){
		return teacherRepo.findAllById(TeacherCode);
	}
	
	public Teacher get(String code) throws AttributeNotFoundException{
		return teacherRepo.findById(code).orElseThrow(()-> new AttributeNotFoundException("Teacher not found !"));
	}
	
	public TeacherLesson getLesson(String codeLesson) throws AttributeNotFoundException{
		return lessonTeacherRepo.findById(codeLesson).orElseThrow(()-> new AttributeNotFoundException("Study not found !"));
	}
	
	public List<TeacherLesson> getLessonOnTeacher(String code){
		return teacherRepo.findAllLessonOnTeacherId(code).getTeacherLesson();
	}
	
	public List<TeacherLesson> getLessonByType(String typeLesson){
		return lessonTeacherRepo.findAllByTypeLesson(typeLesson);
	}
	
	public List<TeacherLesson> getAllLesson(){
		return lessonTeacherRepo.findAll(Sort.by(Order.asc("typeLesson"),Order.asc("lesson")));
	}

	public List<ArrivalTeacher> getArrive(LocalDateTime start, LocalDateTime end){
		return arriveTeacherRepo.findArriveBeetween(start, end);
	}
	
	public ArrivalTeacher getArrive(Long id) throws EntityNotFoundException {
		return arriveTeacherRepo.getById(id);
	}
	
	public void save(Teacher Teacher) throws SaveAttributeException {
//		teacherRepo.save(Teacher);
		try {
			teacherRepo.saveAndFlush(Teacher);
		}
		catch(Exception w) {
			throw new SaveAttributeException("Cannot saving this teacher, please check your input /or maybe this teacher has been saved");
		}
	}
	
	public void saveAll(List<Teacher> Teachers) throws SaveAttributeException {
		try {
			teacherRepo.saveAllAndFlush(Teachers);
		}
		catch(Exception e) {
			throw new SaveAttributeException("Cannot saving teachers, please check your input /or maybe there are teachers who have been saved");
		}
	}
	
	public void saveLesson(TeacherLesson lesson) throws SaveAttributeException {
		try {
			lessonTeacherRepo.saveAndFlush(lesson);}
		catch(Exception e) {
			throw new SaveAttributeException("Cannot saving this study, maybe study with the same code and name has been saved");
		}
	}
	
	public void saveAllLesson(List<TeacherLesson> lessons) throws SaveAttributeException {
		try {
			lessonTeacherRepo.saveAllAndFlush(lessons);}
		catch(Exception e) {
			throw new SaveAttributeException("Cannot saving this studies, maybe study with the same code and name has been saved");
		}
	}
	
//	public void saveArrive(ArrivalTeacher arrive) throws SaveAttributeException {
//		try {
//			arriveTeacherRepo.save(arrive);}
//		catch(Exception e) {
//			throw new SaveAttributeException("Cannot saving this arrive, please retry to input");
//		}
//	}
	
	public void modify(Teacher Teacher, String code) throws SaveAttributeException{
		try {
			teacherRepo.update(Teacher.getUsername(), Teacher.getGender(), Teacher.getEmail(), code);
		}
		catch(Exception e) {
			throw new SaveAttributeException("Cannot modify this teacher, please check your input /or maybe email has been same with the other teacher");
		}
	}
	
	public void modifyAll(Iterable<Teacher> Teacher) throws SaveAttributeException{
		try {
			for (Teacher g : Teacher) {
				teacherRepo.update(g.getUsername(), g.getGender(), g.getEmail(), g.getCodeTeacher());
			}
		}
		catch(Exception e) {
			throw new SaveAttributeException("Cannot modify teachers, please check your input, /or maybe email has been same with the other teacher");
		}
	}
	
	@Transactional
	public void modifyTeacherLesson(String codeTeacher, TeacherLesson lesson, AccessModification access) throws SaveAttributeException {
		try {
			if(access.equals(AccessModification.ADD)) {
				Teacher mn = teacherRepo.getById(codeTeacher);
				TeacherLesson ls = lessonTeacherRepo.getById(lesson.getCodeLesson());
				
				mn.getTeacherLesson().add(ls);
				ls.setTeacherLesson(mn);
				
				lessonTeacherRepo.saveAndFlush(ls);
			}
			else if(access.equals(AccessModification.MODIFY)) {
				lessonTeacherRepo.saveAndFlush(lesson);
			}
			else {
				lessonTeacherRepo.delete(lesson);
			}}
		catch(Exception e) {
			throw new SaveAttributeException("Cannot modify this study, maybe study code has been already /or study is not found");
		}
	}
	
	@Transactional
	public void modifyTeacherArrive(String codeTeacher, ArrivalTeacher arrive, AccessModification access) throws SaveAttributeException {
		try {	
			if(access.equals(AccessModification.ADD)) {
				Teacher mn = teacherRepo.getById(codeTeacher);
				
				arrive.setTeacherArrive(mn);
				mn.getTeachersArrive().add(arrive);
				
				arriveTeacherRepo.saveAndFlush(arrive);
			}
			else {
				arriveTeacherRepo.delete(arrive);
			}
		}catch(Exception e) {
			throw new SaveAttributeException("Cannot modify this arrive, maybe teacher is not found");
		}
	}
	
	@Transactional
	public void delete(String codeTeacher) throws AttributeNotFoundException {
		try {
			Teacher teacher = entityManager.find(Teacher.class, codeTeacher);
			for(Student s  : teacher.getStudents()) {
				s.removeTeacher(teacher);
			}
			entityManager.remove(teacher);
		}
		catch(Exception e) {
			throw new AttributeNotFoundException("Teacher not found !");
		}
	}
}
