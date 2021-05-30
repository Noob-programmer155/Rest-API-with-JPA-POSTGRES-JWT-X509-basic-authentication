package com.amrTm.restApiJpaJwtX509Authentication.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.amrTm.restApiJpaJwtX509Authentication.entity.StudentLesson;

@Repository
public interface LessonStudentRepo extends JpaRepository<StudentLesson,Long>{
	public Optional<StudentLesson> findByCodeLesson(String codeLesson);
	public List<StudentLesson> findAllByTypeLesson(String typeLesson);
}
