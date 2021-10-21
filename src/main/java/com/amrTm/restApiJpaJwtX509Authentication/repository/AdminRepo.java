package com.amrTm.restApiJpaJwtX509Authentication.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.amrTm.restApiJpaJwtX509Authentication.entity.Admin;
import com.amrTm.restApiJpaJwtX509Authentication.entity.GenderType;

@Repository
public interface AdminRepo extends JpaRepository<Admin,Long>{
	public boolean existsByUsernameAndEmail(String username, String email);
//	@Modifying
	@Transactional
	@Query("select a from Admin a join fetch a.roles t where a.username = :username")
	public Admin findByUsername(@Param("username") String username);
}
