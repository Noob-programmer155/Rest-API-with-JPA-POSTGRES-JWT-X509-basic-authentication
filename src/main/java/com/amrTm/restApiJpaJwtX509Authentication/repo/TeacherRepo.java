package com.amrTm.restApiJpaJwtX509Authentication.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.amrTm.restApiJpaJwtX509Authentication.entity.Teacher;

@Repository
public interface TeacherRepo extends JpaRepository<Teacher,Long>{
	public Optional<Teacher> findByCodeTeacher(String codeTeacher);
}
