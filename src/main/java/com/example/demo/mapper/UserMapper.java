package com.example.demo.mapper;

import java.util.HashSet;
import java.util.Set;

import lombok.extern.slf4j.Slf4j;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.demo.dto.UserDTO;
import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.repository.RoleRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper
@Slf4j
public abstract class UserMapper {
//	public UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
	public static final UserMapper INSTANCE = Mappers.getMapper( UserMapper.class );
	@Autowired
	public PasswordUtils passwordUtils;
//	@Autowired
//	RoleRepository roleRepository;
//	@Mapping(source = "name", target = "categoryName")
//	UserDTO categoryToCategoryDTO(User user);
//
//	@Mapping(source = "categoryName", target = "name")
//	User categoryDTOToCategory(UserDTO categoryDTO);

	@AfterMapping
	protected void enrichUser(UserDTO userDto, @MappingTarget User user) {
//		log.info("Total result "+roleRepository.findAll());
//		Role role = roleRepository.findByName(userDto.getRoleName())
//				.orElseThrow(() -> new RuntimeException("role not found"));
//		Set roleSet = new HashSet<Role>();
//		roleSet.add(role);


	}

//    @AfterMapping
//    protected void convertNameToUpperCase(@MappingTarget UserDTO userDto) {
//		//user.setPassword(getEncodedPassword(userDto.getPassword()));
//	}

//    }
	//@Mapping(source = "roleName", target = "role")
	  public  abstract User toUser(UserDTO userDTO);
//	  {
//	  	User user = new User();
//	  	return user;
//	  }

}
