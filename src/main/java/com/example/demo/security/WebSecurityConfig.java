package com.example.demo.security;

import com.example.demo.config.ProfileManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Slf4j
public class WebSecurityConfig {


    private final UserDetailsService customUserDetailsService;
    private final ProfileManager profileManager;

    private final Environment env;

    public WebSecurityConfig(UserDetailsService customUserDetailsService, ProfileManager profileManager, Environment env) {
        this.customUserDetailsService = customUserDetailsService;
        this.profileManager = profileManager;
        this.env = env;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    // http://localhost:8092/webjars/springfox-swagger-ui/springfox.css?v=2.9.2
    // http://localhost:8092/swagger-resources/configuration/ui

    @Configuration
    @Order(1)
    public static class ApiWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {

        @Bean
        @Override
        public AuthenticationManager authenticationManagerBean() throws Exception {
            return super.authenticationManagerBean();
        }

        protected void configure(HttpSecurity http) throws Exception {
            http.antMatcher("/api/**").authorizeRequests().anyRequest().authenticated().and().httpBasic();
            http.csrf().disable();
            http.headers().frameOptions().disable();

        }
    }

    @Configuration
    @Order(2)
    public static class JWTWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {
        @Autowired
        private JwtRequestFilter jwtRequestFilter;
        @Autowired
        UserDetailsService customUserDetailsService;
        @Autowired
        private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
        @Autowired
        PasswordEncoder passwordEncoder;

        protected void configure(HttpSecurity httpSecurity) throws Exception {

            httpSecurity.
                    antMatcher("/auth/**").authorizeRequests().anyRequest().authenticated().and().
                    exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint).and().sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

            httpSecurity.csrf().disable();
            httpSecurity.headers().frameOptions().disable();
            httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        }

    }

    // @Autowired
    // @Profile("LDAP")
    public void configureLDAP(AuthenticationManagerBuilder auth) throws Exception {
        log.info("configureLDAP invoked ");
        auth
                .ldapAuthentication()
                .userDnPatterns("uid={0},ou=people")
                //.userDnPatterns("cn={0}")
                // .groupSearchBase("ou=groups")
                .contextSource()
                //.url("ldap://localhost:389/dc=example,dc=com")
                .url(env.getRequiredProperty("spring.ldap.urls") + "/" + env.getRequiredProperty("spring.ldap.base"))
                .managerDn(env.getRequiredProperty("spring.ldap.username"))
                .managerPassword(env.getRequiredProperty("spring.ldap.password"))
                .and()
                .passwordCompare()
                .passwordEncoder(passwordEncoder())
                .passwordAttribute("userPassword");
    }

    @Autowired
    // @Profile("DB")
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        log.info("Active profile in security ::" + profileManager.getActiveProfiles());
        if (profileManager.getActiveProfiles().equalsIgnoreCase("DB")) {
            log.info("Active profile in configureDB ::");
            configureDB(auth);
        } else {
            log.info("Active profile in configureLDAP ::");
            configureLDAP(auth);

        }
    }

    private void configureDB(AuthenticationManagerBuilder auth) throws Exception {
        log.info("configureDB invoked ");
        auth.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder());
    }

    @Configuration

    public static class FormLoginWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {
        @Autowired
        UserDetailsService customUserDetailsService;

        @Autowired
        PasswordEncoder passwordEncoder;

        @Override
        protected void configure(HttpSecurity httpSecurity) throws Exception {
            httpSecurity.authorizeRequests().antMatchers("/", "/home", "/h2-console", "/h2-console/*",
                    "/webjars/**", "/authenticate", "/ldapauthenticate", "/users", "/people/*")
                    .permitAll().requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                    .antMatchers("/v3/api-docs", "/swagger-ui.html").hasAuthority("USER").anyRequest().authenticated()
                    .and().formLogin().loginPage("/login").permitAll().and().logout().permitAll();

            httpSecurity.csrf().disable();
            httpSecurity.headers().frameOptions().disable();
        }

        @Override
        public void configure(WebSecurity web) throws Exception {
            web.ignoring().antMatchers("/v2/api-docs/**", "/configuration/ui", "/swagger-resources/**",
                    "/configuration/**", "/css/**", "/images/**");

        }


    }


}
