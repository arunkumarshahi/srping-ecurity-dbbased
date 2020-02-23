package com.example.demo.model;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Table(name = "USERS")
@Entity
@Data
@Slf4j
//@Accessors(chain = true)
public class User implements UserDetails {
    /**
     * added default serialization version
     */
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String email;
    private String password;
    @ManyToMany
    @JoinTable(name = "user_role", joinColumns = {
            @JoinColumn(name = "user_id", referencedColumnName = "id")}, inverseJoinColumns = {
            @JoinColumn(name = "role_id", referencedColumnName = "id")})
    private Set<Role> roles;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<SimpleGrantedAuthority> grantAuthoritySet = new HashSet<SimpleGrantedAuthority>();
        if (roles != null && roles.size() > 0) {
            grantAuthoritySet = roles.stream().map(role -> {
                SimpleGrantedAuthority grantedAuthority = new SimpleGrantedAuthority(role.getName());
                return grantedAuthority;
            }).collect(Collectors.toSet());
        }
        return grantAuthoritySet;
    }
//	public void setPassword(String password) {
//		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//		String hashedPassword = passwordEncoder.encode(password);
//		log.info("password in setter "+hashedPassword);
//		password=hashedPassword;
//	}

    public String getPassword() {
        // TODO Auto-generated method stub
        log.info("password in getter " + password);
        return password;
    }

    @Override
    public String getUsername() {
        // TODO Auto-generated method stub
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isEnabled() {
        // TODO Auto-generated method stub
        return true;
    }
}
