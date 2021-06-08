package com.amrTm.restApiJpaJwtX509Authentication.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;

import com.amrTm.restApiJpaJwtX509Authentication.entity.ArrivalTeacher;
import com.amrTm.restApiJpaJwtX509Authentication.entity.TeacherLesson;
import com.amrTm.restApiJpaJwtX509Authentication.exception.AttributeNotFoundException;
import com.amrTm.restApiJpaJwtX509Authentication.exception.SaveAttributeException;
import com.amrTm.restApiJpaJwtX509Authentication.entity.Student;
import com.amrTm.restApiJpaJwtX509Authentication.entity.Teacher;
import com.amrTm.restApiJpaJwtX509Authentication.repo.ArriveTeacherRepo;
import com.amrTm.restApiJpaJwtX509Authentication.repo.LessonTeacherRepo;
import com.amrTm.restApiJpaJwtX509Authentication.repo.StudentRepo;
import com.amrTm.restApiJpaJwtX509Authentication.repo.TeacherRepo;

@Service
public class TeacherService {
	private TeacherRepo teacherRepo; 
	private ArriveTeacherRepo arriveTeacherRepo;
	private LessonTeacherRepo lessonTeacherRepo;
	
	public List<Teacher> getAll(){
		return teacherRepo.findAll(Sort.by(Order.asc("first")));
	}
	
	public List<Teacher> getAll(List<String> TeacherCode){
		return teacherRepo.findAllByTeacherCode(TeacherCode);
	}
	
	public Teacher get(String code) throws AttributeNotFoundException{
		return teacherRepo.findByCodeTeacher(code).orElseThrow(()-> new AttributeNotFoundException("Teacher not found !"));
	}
	
	public TeacherLesson getLesson(String codeLesson) throws AttributeNotFoundException{
		return lessonTeacherRepo.findByCodeLesson(codeLesson).orElseThrow(()-> new AttributeNotFoundException("Study not found !"));
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
	
	public void save(Teacher Teacher) throws SaveAttributeException{
		try {
			teacherRepo.save(Teacher);
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
			lessonTeacherRepo.save(lesson);}
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
	
	public void saveArrive(ArrivalTeacher arrive) throws SaveAttributeException {
		try {
			arriveTeacherRepo.save(arrive);}
		catch(Exception e) {
			throw new SaveAttributeException("Cannot saving this arrive, please retry to input");
		}
	}
	
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
	public void modifyTeacherLesson(Teacher teacher, TeacherLesson lesson, AccessModification access) throws SaveAttributeException {
		try {
			if(access.equals(AccessModification.ADD)) {
				Teacher mn = new Teacher();
				mn.setId(teacher.getId());
				mn.setUsername(teacher.getUsername());
				mn.setGender(teacher.getGender());
				mn.setEmail(teacher.getEmail());
				mn.setCodeTeacher(teacher.getCodeTeacher());
				teacherRepo.save(mn);
				
				lesson.setTeacherLesson(mn);
				mn.getTeacherLesson().add(lesson);
				
				lessonTeacherRepo.save(lesson);
			}
			else if(access.equals(AccessModification.MODIFY)) {
				lessonTeacherRepo.save(lesson);
			}
			else {
				lessonTeacherRepo.delete(lesson);
			}}
		catch(Exception e) {
			throw new SaveAttributeException("Cannot modify this study, maybe study code has been already /or study is not found");
		}
	}
	
	@Transactional
	public void modifyStudentArrive(Teacher teacher, ArrivalTeacher arrive, AccessModification access) throws SaveAttributeException {
		try {	
			if(access.equals(AccessModification.ADD)) {
				Teacher mn = new Teacher();
				mn.setId(teacher.getId());
				mn.setUsername(teacher.getUsername());
				mn.setGender(teacher.getGender());
				mn.setEmail(teacher.getEmail());
				mn.setCodeTeacher(teacher.getCodeTeacher());
				teacherRepo.save(mn);
				
				arrive.setTeacherArrive(mn);
				mn.getTeachersArrive().add(arrive);
				
				arriveTeacherRepo.save(arrive);
			}
			else {
				arriveTeacherRepo.delete(arrive);
			}
		}catch(Exception e) {
			throw new SaveAttributeException("Cannot modify this arrive, maybe teacher is not found");
		}
	}
	
	@Transactional
	public void delete(Teacher Teacher) throws AttributeNotFoundException {
		try {
			teacherRepo.delete(Teacher);
		}
		catch(Exception e) {
			throw new AttributeNotFoundException("Teacher not found !");
		}
	}
}
