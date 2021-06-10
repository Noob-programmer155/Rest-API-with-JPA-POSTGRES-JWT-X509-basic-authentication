package com.amrTm.restApiJpaJwtX509Authentication.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.amrTm.restApiJpaJwtX509Authentication.entity.StudentLesson;

@Repository
public interface LessonStudentRepo extends JpaRepository<StudentLesson,String>{
	public List<StudentLesson> findAllByTypeLesson(String typeLesson);
}
