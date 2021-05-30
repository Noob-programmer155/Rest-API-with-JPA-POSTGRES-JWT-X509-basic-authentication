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
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.amrTm.restApiJpaJwtX509Authentication.entity.ArrivalStudent;
import com.amrTm.restApiJpaJwtX509Authentication.entity.GenderType;
import com.amrTm.restApiJpaJwtX509Authentication.entity.Student;
import com.amrTm.restApiJpaJwtX509Authentication.entity.Teacher;
import com.amrTm.restApiJpaJwtX509Authentication.services.TeacherService;
import com.amrTm.restApiJpaJwtX509Authentication.services.AccessModification;
import com.amrTm.restApiJpaJwtX509Authentication.services.StudentService;

@SpringBootApplication
@EnableJpaRepositories
//@EnableTransactionManagement
public class RestApiWithJpaOracleJwtX509BasicAuthenticationApplication implements CommandLineRunner {
	
	@Autowired
	private StudentService studentService;
	@Autowired
	private TeacherService teacherService; 
	
	public static void main(String[] args) {
		SpringApplication.run(RestApiWithJpaOracleJwtX509BasicAuthenticationApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		List<Student> d = new ArrayList<>();
		Student a = new Student();
		a.setId(1l);
		a.setFirst("Amar");
		a.setLast("rijal");
		a.setEmail("sssada");
		a.setGender(GenderType.MALE);
		a.setStudentCode("L200190149");
		Student b = new Student();
		b.setId(2l);
		b.setFirst("Doni");
		b.setLast("john");
		b.setEmail("sssasaa");
		b.setGender(GenderType.MALE);
		b.setStudentCode("L200190150");
		Student c = new Student();
		c.setId(3l);
		c.setFirst("Tiara");
		c.setLast("Khadijah");
		c.setEmail("ssddfs");
		c.setGender(GenderType.FEMALE);
		c.setStudentCode("L200190151");
		d.add(a);d.add(b);d.add(c);
		
		studentService.saveAll(d);
		Student e = new Student();
		e.setFirst("Donni");
		e.setLast("johnsen");
		e.setEmail("sssasaa");
		e.setGender(GenderType.MALE);
		e.setStudentCode("L200190150");
		studentService.modify(e, e.getStudentCode());
		Student gf = studentService.get("L200190150").get();
		ArrivalStudent bf = new ArrivalStudent();
		bf.setArrive(LocalDateTime.now());
		if(studentService.modifyStudentArrive(gf, bf, AccessModification.ADD)) {
			ArrivalStudent bf1 = new ArrivalStudent();
			bf.setArrive(LocalDateTime.now());
			studentService.modifyStudentArrive(gf, bf1, AccessModification.ADD);
//			ArrivalStudent jh = studentService.getArrive(4l);
//			studentService.modifyStudentArrive(gf, jh, AccessModification.DELETE);
		}
		Teacher f = new Teacher();
		f.setUsername("Arrijal");
		f.setEmail("dfjhdsjkfn");
		f.setGender(GenderType.MALE);
		f.setCodeTeacher("NIS4672912");
//		teacherService.save(f);
//		studentService.modifyTeacher(gf, f, AccessModification.ADD);
	}

}
