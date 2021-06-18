package com.amrTm.restApiJpaJwtX509Authentication.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.amrTm.restApiJpaJwtX509Authentication.entity.Lesson;

@Repository
public interface LessonRepo extends JpaRepository<Lesson,String>, LessonEntity{
	public List<Lesson> findAllByTypeLesson(String typeLesson);
}
