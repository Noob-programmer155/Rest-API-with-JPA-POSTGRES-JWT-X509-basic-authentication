package com.amrTm.restApiJpaJwtX509Authentication.swagger_config;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.apis(RequestHandlerSelectors.any())
				.paths(Predicate.not(PathSelectors.ant("/actuator/**")))
				.build()
				.apiInfo(info())
				.tags(new Tag("Admin","Rest Operation for Admin"),
						new Tag("Student","Rest Operation for Student"),
						new Tag("Teacher","Rest Operation for Teacher"))
				.directModelSubstitute(LocalDateTime.class, java.util.Date.class)
				.directModelSubstitute(LocalDate.class, java.sql.Date.class)
				.ignoredParameterTypes(HttpServletRequest.class,HttpServletResponse.class)
				.genericModelSubstitutes(ResponseEntity.class,EntityModel.class,List.class)
				.securitySchemes(apikey())
				.securityContexts(Collections.singletonList(secure()));
	}
	
	private ApiInfo info() {
		return new ApiInfoBuilder()
				.title("Rest API with JPA ORACLE JWT X509 Basic Authentication")
				.description("this Rest api using Jpa with oracle to store data and implement basic and advance security level in range JWT and x509 Authentication certificate")
				.version("1.0")
				.license("GNU Affero General Public License v3.0").licenseUrl("https://www.gnu.org/licenses/agpl-3.0.en.html")
				.contact(new Contact("Arrijal Amar M",null,"AmrrrR572@gmail.com"))
				.build();
	}
	
	private List<SecurityScheme> apikey() {
		List<SecurityScheme> api = new ArrayList<>();
		api.add(new ApiKey("AuthKey1","JSESSIONID","Cookie"));
		api.add(new ApiKey("AuthKey2","XSRF-TOKEN","Cookie"));
		api.add(new ApiKey("AuthKey3","JWT_BEARER","Cookie"));
		return api;
	}
	
	private SecurityContext secure() {
		AuthorizationScope[] auth = new AuthorizationScope[1];
		auth[0] = new AuthorizationScope("global","access everything");
		return SecurityContext.builder()
				.securityReferences(Arrays.asList(new SecurityReference("Authentication",auth)))
				.build();
	}
}
