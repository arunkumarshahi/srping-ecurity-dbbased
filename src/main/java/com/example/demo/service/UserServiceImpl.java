package com.example.demo.service;

import com.example.demo.dto.UserDTO;
import com.example.demo.mapper.UserMapper;
import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public User createUser(UserDTO userDTO) {
        Set<Role> roles=new HashSet<Role>();
        roles=userDTO.getRoleName().stream().map(roleName->{Role role=roleRepository.findByName(roleName).get();
        return role;
        }).collect(Collectors.toSet());
        log.info("User :::"+userDTO);
        User user = UserMapper.INSTANCE.toUser(userDTO);
        user.setRoles(roles);
        log.info("converted User :::"+user);
        user.setPassword(getEncodedPassword(userDTO.getPassword()));
        user=userRepository.save(user);

        log.info("convesaved  User :::"+user);
        return user;
    }

    public String getEncodedPassword(String password){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode(password);
       return hashedPassword;
    }
}
