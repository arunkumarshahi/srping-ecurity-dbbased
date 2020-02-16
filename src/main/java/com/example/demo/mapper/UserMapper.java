package com.example.demo.mapper;

import java.util.HashSet;
import java.util.Set;

import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.demo.dto.UserDTO;
import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.repository.RoleRepository;

@Mapper
public interface UserMapper {
	public UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
	//@Autowired
	//RoleRepository roleRepository;
//	@Mapping(source = "name", target = "categoryName")
//	UserDTO categoryToCategoryDTO(User user);
//
//	@Mapping(source = "categoryName", target = "name")
//	User categoryDTOToCategory(UserDTO categoryDTO);

//	@BeforeMapping
//	protected void enrichUser(UserDTO userDto, @MappingTarget User user) {
//		Role role = roleRepository.findByName(userDto.getRoleName())
//				.orElseThrow(() -> new RuntimeException("role not found"));
//		Set roleSet = new HashSet<Role>();
//		roleSet.add(role);
//		user.setRoles(roleSet);
//
//	}

//    @AfterMapping
//    protected void convertNameToUpperCase(@MappingTarget CarDTO carDto) {
//        carDto.setName(carDto.getName().toUpperCase());
//    }
	@Mapping(source = "roleName", target = "role")
	  User toUser(UserDTO userDTO);

}
