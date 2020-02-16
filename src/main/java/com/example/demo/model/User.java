package com.example.demo.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Table(name = "USERS")
@Entity
@Data
@Slf4j
public class User implements UserDetails{
	/**
	 * added default serialization version
	 */
	private static final long serialVersionUID = 1L;
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	private String name;
	private String email;
	private String password;
	@OneToMany
	private Set<Role> roles;
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Set<SimpleGrantedAuthority> grantAuthoritySet=new HashSet<SimpleGrantedAuthority>();
		roles.stream().map(role->{SimpleGrantedAuthority grantedAuthority=new SimpleGrantedAuthority(role.getName());
		return grantedAuthority;
		}).collect(Collectors.toSet());
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
		log.info("password in getter "+password);
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
