package com.amrTm.restApiJpaJwtX509Authentication.repo;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.amrTm.restApiJpaJwtX509Authentication.entity.Admin;
import com.amrTm.restApiJpaJwtX509Authentication.entity.Role;

@Repository
public interface AdminRepo extends JpaRepository<Admin,Long>{
	public Admin findByUsername(String username);
	@Modifying
	@Transactional
	@Query("select a from Admin a join fetch a.role t where a.username = :username")
	public List<Role> findAllRole(@Param("username") String username);
}
