package com.amrTm.restApiJpaJwtX509Authentication.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;

import com.amrTm.restApiJpaJwtX509Authentication.entity.ArrivalTeacher;
import com.amrTm.restApiJpaJwtX509Authentication.entity.TeacherLesson;
import com.amrTm.restApiJpaJwtX509Authentication.entity.Student;
import com.amrTm.restApiJpaJwtX509Authentication.entity.Teacher;
import com.amrTm.restApiJpaJwtX509Authentication.repo.ArriveTeacherRepo;
import com.amrTm.restApiJpaJwtX509Authentication.repo.LessonTeacherRepo;
import com.amrTm.restApiJpaJwtX509Authentication.repo.StudentRepo;
import com.amrTm.restApiJpaJwtX509Authentication.repo.TeacherRepo;

@Service
public class TeacherService {
	private TeacherRepo teacherRepo; 
	private StudentRepo studentRepo;
	private ArriveTeacherRepo arriveTeacherRepo;
	private LessonTeacherRepo lessonTeacherRepo;
	
	public List<Teacher> getAll(){
		return teacherRepo.findAll(Sort.by(Order.asc("first")));
	}
	
	public List<Teacher> getAll(List<String> TeacherCode){
		return teacherRepo.findAllByTeacherCode(TeacherCode);
	}
	
	public Optional<Teacher> get(String code){
		return teacherRepo.findByCodeTeacher(code);
	}
	
	public Optional<TeacherLesson> getLesson(String codeLesson){
		return lessonTeacherRepo.findByCodeLesson(codeLesson);
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
	
	public ArrivalTeacher getArrive(Long id) {
		return arriveTeacherRepo.getById(id);
	}
	
	public boolean save(Teacher Teacher){
		try {
			teacherRepo.save(Teacher);
			return true;
		}
		catch(Exception w) {return false;}
	}
	
	public boolean saveAll(List<Teacher> Teachers) {
		try {
			teacherRepo.saveAllAndFlush(Teachers);
			return true;
		}
		catch(Exception e) {return false;}
	}
	
	public void saveLesson(TeacherLesson lesson) {
		lessonTeacherRepo.save(lesson);
	}
	
	public void saveAllLesson(List<TeacherLesson> lessons) {
		lessonTeacherRepo.saveAllAndFlush(lessons);
	}
	
	public void saveArrive(ArrivalTeacher arrive) {
		arriveTeacherRepo.save(arrive);
	}
	
	public boolean modify(Teacher Teacher, String code){
		try {
			teacherRepo.update(Teacher.getUsername(), Teacher.getGender(), Teacher.getEmail(), code);
			return true;
		}
		catch(Exception e) {
			return false;
		}
	}
	
	public boolean modifyAll(Iterable<Teacher> Teacher){
		try {
			for (Teacher g : Teacher) {
				teacherRepo.update(g.getUsername(), g.getGender(), g.getEmail(), g.getCodeTeacher());
			}
			return true;
		}
		catch(Exception e) {
			return false;
		}
	}
	
	public boolean modifyTeacher(Teacher teacher, Student student, AccessModification access) {	
		if(access.equals(AccessModification.ADD)) {
			Teacher mn = new Teacher();
			mn.setId(teacher.getId());
			mn.setUsername(teacher.getUsername());
			mn.setGender(teacher.getGender());
			mn.setEmail(teacher.getEmail());
			mn.setCodeTeacher(teacher.getCodeTeacher());
			
			Student hg = new Student();
			hg.setId(student.getId());
			hg.setFirst(student.getFirst());
			hg.setLast(student.getLast());
			hg.setEmail(student.getEmail());
			hg.setGender(student.getGender());
			hg.setStudentCode(student.getStudentCode());
			studentRepo.save(hg);
			
			mn.getStudents().add(hg);
			hg.getTeachers().add(mn);
			
			teacherRepo.save(mn);
			return true;
		}
		else {
			teacherRepo.delete(teacher);
			return true;
		}
	}
	
	public boolean modifyTeacherLesson(Teacher teacher, TeacherLesson lesson, AccessModification access) {
		if(access.equals(AccessModification.ADD)) {
			
			lesson.getTeacherLesson().add(teacher);
			teacher.getTeacherLesson().add(lesson);
			
			lessonTeacherRepo.save(lesson);
			return true;
		}
		else if(access.equals(AccessModification.MODIFY)) {
			lessonTeacherRepo.save(lesson);
			return true;
		}
		else {
			lessonTeacherRepo.delete(lesson);
			return true;
		}
	}
	
	public boolean modifyStudentArrive(Teacher teacher, ArrivalTeacher arrive, AccessModification access) {
		if(access.equals(AccessModification.ADD)) {
			
			arrive.getTeacherArrive().add(teacher);
			teacher.getTeachersArrive().add(arrive);
			
			arriveTeacherRepo.save(arrive);
			return true;
		}
		else {
			arriveTeacherRepo.delete(arrive);
			return true;
		}
	}
	
	public boolean delete(Teacher Teacher) {
		try {
			teacherRepo.delete(Teacher);
			return true;
		}
		catch(Exception e) {
			return false;
		}
	}
}
