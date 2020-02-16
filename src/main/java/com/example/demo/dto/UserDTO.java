package com.example.demo.dto;

import lombok.Data;

import java.util.Set;

@Data
public class UserDTO {
	private String name;
	private String email;
	private String password;
	private Set<String> roleName;

}
