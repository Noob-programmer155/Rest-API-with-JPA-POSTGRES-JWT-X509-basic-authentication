package com.amrTm.restApiJpaJwtX509Authentication.repo;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.amrTm.restApiJpaJwtX509Authentication.entity.GenderType;
import com.amrTm.restApiJpaJwtX509Authentication.entity.Teacher;

@Repository
public interface TeacherRepo extends JpaRepository<Teacher,String>, TeacherRepoEntity{
//	@Modifying
//	@Transactional
//	@Query("select u from Teacher u where u.codeTeacher in :codeTeacher")
//	public List<Teacher> findAllByTeacherCode(@Param("codeTeacher") List<String> studentCodes);
	@Modifying
	@Transactional
	@Query("update Teacher u set u.username = :username, u.gender = :gender, u.email = :email where u.codeTeacher = :code")
	public void update(@Param("username") String username,
						@Param("gender") GenderType gender,
						@Param("email") String email,
						@Param("code") String kode);
}
