package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.ldap.repository.config.EnableLdapRepositories;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;

@Configuration
@EnableLdapRepositories
public class ApacheDSConfiguration {
//    @Bean
//    LdapTemplate ldapTemplate(ContextSource contextSource) {
//        return new LdapTemplate(contextSource);
//    }

    @Bean
    public LdapContextSource contextSource() {
        LdapContextSource contextSource = new LdapContextSource();
        contextSource.setUrl("ldap://localhost:8389");
        contextSource.setUserDn("uid=TestL,ou=people,dc=springframework,dc=org");
        contextSource.setPassword("$2a$10$R/lx6BfHGwxmKfTD/emz..MLJ9JhXOxqqjOlFM5l3/QkZwy573nFu");
        return contextSource;
    }

    @Bean
    public LdapTemplate ldapTemplate() {
        LdapTemplate template = new LdapTemplate(contextSource());
        return template;
    }
}


