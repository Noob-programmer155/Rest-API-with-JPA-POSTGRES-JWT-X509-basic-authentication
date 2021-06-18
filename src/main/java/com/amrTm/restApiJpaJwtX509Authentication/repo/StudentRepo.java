package com.amrTm.restApiJpaJwtX509Authentication.repo;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.amrTm.restApiJpaJwtX509Authentication.entity.GenderType;
import com.amrTm.restApiJpaJwtX509Authentication.entity.Student;

@Repository
public interface StudentRepo extends JpaRepository<Student,String> {
//	@Modifying
//	@Transactional
//	@Query("select u from Student u where u.studentCode in :studentCodes")
//	public List<Student> findAllByStudentCode(@Param("studentCodes") List<String> studentCodes);
	@Modifying
	@Transactional
	@Query("update Student u set u.first = :first, u.last = :last, u.gender = :gender, u.email = :email where u.studentCode = :code")
	public void update(@Param("first") String first,
						@Param("last") String last,
						@Param("gender") GenderType gender,
						@Param("email") String email,
						@Param("code") String kode);
}
