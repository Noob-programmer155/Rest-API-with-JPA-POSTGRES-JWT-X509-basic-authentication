package com.amrTm.restApiJpaJwtX509Authentication.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.amrTm.restApiJpaJwtX509Authentication.entity.TeacherLesson;

@Repository
public interface LessonTeacherRepo extends JpaRepository<TeacherLesson, Long>{
	public Optional<TeacherLesson> findByCodeLesson(String codeLesson);
	public List<TeacherLesson> findAllByTypeLesson(String typeLesson);
}
