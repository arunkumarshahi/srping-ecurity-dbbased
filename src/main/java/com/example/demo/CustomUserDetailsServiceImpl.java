package com.example.demo;

import javax.transaction.Transactional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional
public class CustomUserDetailsServiceImpl implements UserDetailsService {
	private final UserRepository userRepository;
	public CustomUserDetailsServiceImpl(UserRepository userRepository) {
		this.userRepository=userRepository;
	}
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		User user=userRepository.findByEmail(username).orElseThrow(()->new RuntimeException("User not found"));
		log.info("user in user detail service ::"+user);
		return user;
	}

}
