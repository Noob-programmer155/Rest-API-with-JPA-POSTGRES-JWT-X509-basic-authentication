package com.amrTm.restApiJpaJwtX509Authentication.services;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.amrTm.restApiJpaJwtX509Authentication.entity.ArrivalTeacher;
import com.amrTm.restApiJpaJwtX509Authentication.entity.Lesson;
import com.amrTm.restApiJpaJwtX509Authentication.entity.Student;
import com.amrTm.restApiJpaJwtX509Authentication.exception.AttributeNotFoundException;
import com.amrTm.restApiJpaJwtX509Authentication.exception.SaveAttributeException;
import com.amrTm.restApiJpaJwtX509Authentication.entity.Teacher;
import com.amrTm.restApiJpaJwtX509Authentication.repo.ArriveTeacherRepo;
import com.amrTm.restApiJpaJwtX509Authentication.repo.LessonRepo;
import com.amrTm.restApiJpaJwtX509Authentication.repo.TeacherRepo;
import com.amrTm.restApiJpaJwtX509Authentication.repo.TeacherRepoEntity;

@Service
public class TeacherService implements TeacherRepoEntity{
	private TeacherRepo teacherRepo; 
	private ArriveTeacherRepo arriveTeacherRepo;
	private LessonRepo lessonRepo;
	
	@PersistenceContext
	private EntityManager entityManager;
	
	public TeacherService(TeacherRepo teacherRepo, ArriveTeacherRepo arriveTeacherRepo,
			LessonRepo lessonRepo) {
		super();
		this.teacherRepo = teacherRepo;
		this.arriveTeacherRepo = arriveTeacherRepo;
		this.lessonRepo = lessonRepo;
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
	
	public Lesson getLesson(String codeLesson) throws AttributeNotFoundException{
		return lessonRepo.findById(codeLesson).orElseThrow(()-> new AttributeNotFoundException("Study not found !"));
	}
	
//	public List<TeacherLesson> getLessonOnTeacher(String code){
//		return teacherRepo.findAllLessonOnTeacherId(code).getTeacherLesson();
//	}
	
	public List<Lesson> getLessonByType(String typeLesson){
		return lessonRepo.findAllByTypeLesson(typeLesson);
	}
	
	public List<Lesson> getAllLesson(){
		return lessonRepo.findAll(Sort.by(Order.asc("typeLesson"),Order.asc("lesson")));
	}

	public List<ArrivalTeacher> getArrive(LocalDateTime start, LocalDateTime end){
		return arriveTeacherRepo.findArriveBeetween(start, end);
	}
	
	public ArrivalTeacher getArrive(Long id, boolean jpa) throws EntityNotFoundException {
		if(jpa)
			return arriveTeacherRepo.getById(id);
		else
			return arriveTeacherRepo.findById(id).get();
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
	
//	public void saveLesson(Lesson lesson) throws SaveAttributeException {
//		try {
//			lessonRepo.saveAndFlush(lesson);}
//		catch(Exception e) {
//			throw new SaveAttributeException("Cannot saving this study, maybe study with the same code and name has been saved");
//		}
//	}
//	
//	public void saveAllLesson(List<TeacherLesson> lessons) throws SaveAttributeException {
//		try {
//			lessonTeacherRepo.saveAllAndFlush(lessons);}
//		catch(Exception e) {
//			throw new SaveAttributeException("Cannot saving this studies, maybe study with the same code and name has been saved");
//		}
//	}
	
//	public void saveArrive(ArrivalTeacher arrive) throws SaveAttributeException {
//		try {
//			arriveTeacherRepo.save(arrive);}
//		catch(Exception e) {
//			throw new SaveAttributeException("Cannot saving this arrive, please retry to input");
//		}
//	}
	
	public Teacher modify(Teacher Teacher, String code) throws SaveAttributeException{
		try {
			teacherRepo.update(Teacher.getUsername(), Teacher.getGender(), Teacher.getEmail(), code);
			return teacherRepo.findById(code).get();
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
	public boolean modifyTeacherLesson(String codeTeacher, String codelesson, AccessModification access) throws SaveAttributeException {
		try {
			if(access.equals(AccessModification.ADD)) {
				Teacher mn = teacherRepo.getById(codeTeacher);
				Lesson ls = lessonRepo.getById(codelesson);
				
				ls.addLesson(mn);
				
				teacherRepo.saveAndFlush(mn);
				return true;
			}
//			else if(access.equals(AccessModification.MODIFY)) {
//				lessonTeacherRepo.saveAndFlush(lesson);
//				return true;
//			}
			else {
				Teacher mn = teacherRepo.getById(codeTeacher);
				Lesson ls = lessonRepo.getById(codelesson);
				
				mn.removeLesson(ls);
				
				lessonRepo.saveAndFlush(ls);
				return true;
			}}
		catch(Exception e) {
			throw new SaveAttributeException("Cannot modify this study, maybe study code has been already /or study is not found");
		}
	}
	
	@Transactional
	public boolean modifyTeacherArrive(String codeTeacher, ArrivalTeacher arrive, AccessModification access) throws SaveAttributeException {
		try {	
			if(access.equals(AccessModification.ADD)) {
				Teacher mn = teacherRepo.getById(codeTeacher);
				
				arrive.setTeacherArrive(mn);
				mn.getTeachersArrive().add(arrive);
				
				arriveTeacherRepo.saveAndFlush(arrive);
				return true;
			}
			else {
				arriveTeacherRepo.delete(arrive);
				return true;
			}
		}catch(Exception e) {
			throw new SaveAttributeException("Cannot modify this arrive, maybe teacher is not found");
		}
	}
	
	@Transactional
	@PreAuthorize("hasAuthority('ADMIN')")
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
