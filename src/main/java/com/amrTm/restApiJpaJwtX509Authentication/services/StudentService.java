package com.amrTm.restApiJpaJwtX509Authentication.services;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;

import com.amrTm.restApiJpaJwtX509Authentication.entity.Student;
import com.amrTm.restApiJpaJwtX509Authentication.repo.StudentRepo;

@Service
public class StudentService {
	@Autowired
	private StudentRepo studentRepo;
	
	public List<Student> getAll(){
		return studentRepo.findAll(Sort.by(Order.asc("first")));
	}
	
	public List<Student> getAll(List<String> studentCode){
		return studentRepo.findAllByStudentCode(studentCode);
	}
	
	public Optional<Student> get(String code){
		return studentRepo.findByStudentCode(code);
	}
	
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
