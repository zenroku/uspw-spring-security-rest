package com.zahir.fathurrahman.springsecurity;

import com.zahir.fathurrahman.springsecurity.domain.Role;
import com.zahir.fathurrahman.springsecurity.domain.User;
import com.zahir.fathurrahman.springsecurity.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;

@SpringBootApplication
public class SpringSecurityDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringSecurityDemoApplication.class, args);
	}

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder(){
		return new BCryptPasswordEncoder();
	}

	@Bean
	CommandLineRunner run(UserService userService){
		return args -> {
			userService.saveRole(new Role(null,"ROLE_USER"));
			userService.saveRole(new Role(null,"ROLE_MANAGER"));
			userService.saveRole(new Role(null,"ROLE_ADMIN"));
			userService.saveRole(new Role(null,"ROLE_SUPER_ADMIN"));

			userService.saveUser(new User(null,"Yukinoshita Yukino","yukino","123",new ArrayList<>()));
			userService.saveUser(new User(null,"Hikigaya Hachiman","hachiman","123",new ArrayList<>()));
			userService.saveUser(new User(null,"Yuigahaman Yui","yui","123",new ArrayList<>()));
			userService.saveUser(new User(null,"Hiratsuka Shizuka","shizuka","123",new ArrayList<>()));

			userService.addRoleToUser("yukino","ROLE_USER");
			userService.addRoleToUser("yukino","ROLE_MANAGER");
			userService.addRoleToUser("hachiman","ROLE_MANAGER");
			userService.addRoleToUser("yui","ROLE_ADMIN");
			userService.addRoleToUser("shizuka","ROLE_USER");
			userService.addRoleToUser("shizuka","ROLE_ADMIN");
			userService.addRoleToUser("shizuka","ROLE_SUPER_ADMIN");
		};
	}
}
