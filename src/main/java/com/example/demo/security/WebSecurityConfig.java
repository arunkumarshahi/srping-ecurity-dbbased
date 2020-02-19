package com.example.demo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity

public class WebSecurityConfig {

	@Autowired
	private UserDetailsService customUserDetailsService;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	// http://localhost:8092/webjars/springfox-swagger-ui/springfox.css?v=2.9.2
	// http://localhost:8092/swagger-resources/configuration/ui

	@Configuration
	@Order(1)
	public static class ApiWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {
		protected void configure(HttpSecurity http) throws Exception {
			http.antMatcher("/api/**")
					.authorizeRequests().anyRequest().authenticated()
					.and().httpBasic()
			;
			http.csrf().disable();
			http.headers().frameOptions().disable();

		}
	}

	@Configuration

	public static class FormLoginWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

		@Override
		protected void configure(HttpSecurity httpSecurity) throws Exception {
			httpSecurity.authorizeRequests()
					.antMatchers("/", "/home", "/h2-console", "/h2-console/*", "/webjars/**")
					.permitAll()
					.requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
					//adding below section to enable basic authentication for api call
					//.antMatchers("/v3/api-docs", "/swagger-ui.html").hasAuthority("USER").anyRequest().authenticated().and().httpBasic()
					//end of basic authentication for api.
					.antMatchers("/v3/api-docs", "/swagger-ui.html").hasAuthority("USER").anyRequest().authenticated().and()
					.formLogin().loginPage("/login").permitAll().and().logout().permitAll()
			;

			httpSecurity.csrf().disable();
			httpSecurity.headers().frameOptions().disable();
		}

		@Override
		public void configure(WebSecurity web) throws Exception {
			web.ignoring().antMatchers(
					"/v2/api-docs/**",
					"/configuration/ui", "/swagger-resources/**", "/configuration/**", "/css/**", "/images/**");

		}
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		System.out.print("is it configureGlobal" + auth);
		auth.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder());
	}
}
