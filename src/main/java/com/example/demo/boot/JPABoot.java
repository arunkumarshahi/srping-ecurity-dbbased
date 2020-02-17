package com.example.demo.boot;

import java.util.HashSet;
import java.util.Set;

import com.example.demo.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.demo.dto.UserDTO;
import com.example.demo.mapper.UserMapper;
import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JPABoot implements CommandLineRunner {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final UserService userService;
//	@Autowired
//	private  UserMapper userMapper;

    public JPABoot(RoleRepository roleRepository, UserRepository userRepository, UserService userService) {
        // TODO Auto-generated constructor stub
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        //this.userMapper = userMapper;
        this.userService = userService;
    }

    @Override
    public void run(String... args) throws Exception {

        init();
    }

    public void init() {
        Role role = new Role();
        role.setName("ADMIN");
        role = roleRepository.save(role);

        Role role1 = new Role();
        role1.setName("USER");
        role1 = roleRepository.save(role1);
        log.info("saved daya ::" + role1);

        User user = new User();
        user.setEmail("arun@gmail.com");
        user.setName("arun kumar");
        user.setPassword(getEncodedPassword("Welcome1"));
        Set<Role> roleSet = new HashSet<Role>();
        roleSet.add(role);
        roleSet.add(role1);
        user.setRoles(roleSet);
        userRepository.save(user);

        UserDTO dto = new UserDTO();
        dto.setEmail("Test");
        dto.setPassword("Welcome1");
        Set<String> rolesName = new HashSet<String>();
        rolesName.add("ADMIN");
        rolesName.add("USER");
        dto.setRoleName(rolesName);
        User convertedUser = UserMapper.INSTANCE.toUser(dto);
        Role userrole = roleRepository.findById(1L).get();
        Set<Role> roles = new HashSet<Role>();
        roles.add(userrole);
        //convertedUser.setRoles(roles);
        convertedUser.setRoles(roleSet);

        convertedUser = userRepository.save(convertedUser);
        //convertedUser=userService.createUser(dto);
        log.info("user after conversion ::" + convertedUser);
    }

    public static String getEncodedPassword(String password) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode(password);
        log.info("password in setter " + hashedPassword);
        return hashedPassword;
    }
}
