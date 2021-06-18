package com.amrTm.restApiJpaJwtX509Authentication;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.amrTm.restApiJpaJwtX509Authentication.entity.Admin;
import com.amrTm.restApiJpaJwtX509Authentication.entity.ArrivalStudent;
import com.amrTm.restApiJpaJwtX509Authentication.entity.GenderType;
import com.amrTm.restApiJpaJwtX509Authentication.entity.Role;
import com.amrTm.restApiJpaJwtX509Authentication.entity.Student;
import com.amrTm.restApiJpaJwtX509Authentication.entity.Lesson;
import com.amrTm.restApiJpaJwtX509Authentication.entity.Teacher;
import com.amrTm.restApiJpaJwtX509Authentication.repo.AdminRepo;
import com.amrTm.restApiJpaJwtX509Authentication.security.TokenProvider;
import com.amrTm.restApiJpaJwtX509Authentication.services.TeacherService;
import com.amrTm.restApiJpaJwtX509Authentication.services.AccessModification;
import com.amrTm.restApiJpaJwtX509Authentication.services.AdminService;
import com.amrTm.restApiJpaJwtX509Authentication.services.StudentService;

@SpringBootApplication
@EnableJpaRepositories
//@EnableTransactionManagement
public class RestApiWithJpaOracleJwtX509BasicAuthenticationApplication implements CommandLineRunner {
	
	@Autowired
	private StudentService studentService;
	@Autowired
	private TeacherService teacherService; 
	@Autowired
	private AdminRepo adminRepo;
	@Autowired
	private TokenProvider tokenProvider;
	@Autowired
	private AdminService adminService; 
	
	public static void main(String[] args) {
		SpringApplication.run(RestApiWithJpaOracleJwtX509BasicAuthenticationApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		List<Student> d = new ArrayList<>();
		Student a = new Student();
		a.setFirst("Amar");
		a.setLast("rijal");
		a.setEmail("sssada");
		a.setGender(GenderType.MALE);
		a.setStudentCode("L200190149");
		Student b = new Student();
		b.setFirst("Doni");
		b.setLast("john");
		b.setEmail("sssasaa");
		b.setGender(GenderType.MALE);
		b.setStudentCode("L200190150");
		Student c = new Student();
		c.setFirst("Tiara");
		c.setLast("Khadijah");
		c.setEmail("ssddfs");
		c.setGender(GenderType.FEMALE);
		c.setStudentCode("L200190151");
		d.add(a);d.add(b);d.add(c);
		
		studentService.saveAll(d);
		Lesson hgs = new Lesson();
		hgs.setLesson("Java Learn");
		hgs.setCodeLesson("J12");
		hgs.setTypeLesson("Informatics");
		adminService.saveLesson(hgs);
		Student e = new Student();
		e.setFirst("Donni");
		e.setLast("johnsen");
		e.setEmail("sssasaa");
		e.setGender(GenderType.MALE);
		e.setStudentCode("L200190150");
		studentService.modify(e, e.getStudentCode());
		Student gf = studentService.get("L200190150");
		ArrivalStudent bf = new ArrivalStudent();
		bf.setArrive(LocalDateTime.now());
		studentService.modifyStudentArrive(gf.getStudentCode(), bf, AccessModification.ADD);
		ArrivalStudent bf1 = new ArrivalStudent();
		bf1.setArrive(LocalDateTime.now());
		studentService.modifyStudentArrive(gf.getStudentCode(), bf1, AccessModification.ADD);
		ArrivalStudent jh = studentService.getArrive(4l,true);
		studentService.modifyStudentArrive(gf.getStudentCode(), jh, AccessModification.DELETE);
		Teacher f = new Teacher();
		f.setCodeTeacher("NIS4672912");
		f.setUsername("Arrijal");
		f.setEmail("dfjhdsjkfn");
		f.setGender(GenderType.MALE);
		teacherService.save(f);
		studentService.modifyTeacher("NIS4672912", "L200190150", AccessModification.ADD);
//		teacherService.delete(f.getCodeTeacher());
		Lesson bhe = studentService.getLesson("J12");
		studentService.modifyStudentLesson(gf.getStudentCode(), bhe.getCodeLesson(), AccessModification.ADD);
		studentService.modifyTeacher("NIS4672912", "L200190150", AccessModification.DELETE);
		teacherService.modifyTeacherLesson(f.getCodeTeacher(), bhe.getCodeLesson(), AccessModification.ADD);
		studentService.modifyStudentLesson(gf.getStudentCode(), bhe.getCodeLesson(), AccessModification.DELETE);
		bhe.setLesson("Learn With Java");
		adminService.modifyLesson(bhe);
		
		Admin hg = new Admin();
		hg.setUsername("Amar");
		hg.setPassword(new BCryptPasswordEncoder().encode("Amar"));
		hg.setEmail("rijalamar29@gmail.com");
		List<Role> kj = new ArrayList<>();
		kj.add(Role.ADMIN);
		kj.add(Role.USER);
		hg.setRole(kj);
		hg.setValidation(true);
		adminRepo.saveAndFlush(hg);
		System.out.println(tokenProvider.setToken(hg.getUsername(), hg.getEmail(), hg.getRole()));
	}
}
