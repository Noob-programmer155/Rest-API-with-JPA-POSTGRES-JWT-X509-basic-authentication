package com.amrTm.restApiJpaJwtX509Authentication.security;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled=true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserService user;
	
	@Autowired
	private TokenProvider tokenProvider;
	
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(true);
		config.setAllowedOrigins(Arrays.asList("link to frontend services"));
		config.addAllowedHeader("*");
		config.addAllowedMethod("*");
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);
		return source;
	}  

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors()
				.and()
			.csrf()
				.ignoringAntMatchers("/firns/signin","/firns/signup/**","/firns/delete/**"
						,"/firns/send-message","/firns/modify/**","/firns/save/**",
						"/firns/refresh")
				.ignoringAntMatchers("/students/save/**","/students/modify/**","/students/delete"
							,"/teachers/save/**","/teachers/modify/**","/teachers/delete")
				.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
			.and()
//		# because we are using based csrf token, then we must make session automatically 
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
			.and()
				.authorizeRequests()
//					.antMatchers("/students/**", "/teachers/**").hasAnyRole("ADMIN","USER")		because using preAuthorize now it's useless
					.antMatchers("/students/save/**","/students/modify/**","/students/delete"
							,"/teachers/save/**","/teachers/modify/**","/teachers/delete").permitAll()
					.antMatchers("/actuator/**").hasAuthority("ADMIN")
					.antMatchers("/firns/signin","/firns/signup/**","/firns/delete/**"
							,"/firns/send-message","/firns/modify/**","/firns/save/**"
							,"/firns/refresh").permitAll()
					.anyRequest().authenticated()
			.and()
				.logout()
					.logoutUrl("/logout")
					.clearAuthentication(true)
					.invalidateHttpSession(true)
					.deleteCookies("JSESSIONID","JWT_BEARER","XSRF-TOKEN");
//			.and()
//			.httpBasic();
		
//		# because it will using cookie based authentication, this is still needed
//			http.csrf().disable()	
		
		http.apply(new TokenFilterConfig(tokenProvider));
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/v2/api-docs","/swagger-ui/**","/swagger-resources/**");
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(user).passwordEncoder(new BCryptPasswordEncoder());
	}

	@Override
	@Bean
	protected AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager();
	}
}
