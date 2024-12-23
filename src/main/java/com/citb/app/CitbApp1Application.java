package com.citb.app;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.citb.app.config.AppConstants;
import com.citb.app.entities.Role;
import com.citb.app.repositories.RoleRepo;
import com.citb.app.utils.RandomUtil;

@EntityScan(basePackages = "com.citb.app.entities")
@SpringBootApplication
public class CitbApp1Application implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(CitbApp1Application.class, args);
	}

	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private RoleRepo roleRepo;

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	@Override
	public void run(String... args) throws Exception {

		try {
			Role role = new Role();
			role.setId(AppConstants.ROLE_APPRENTICE_ID);
			role.setName("APPRENTICE");

			Role role1 = new Role();
			role1.setId( AppConstants.ROLE_OFFICER_ID);
			role1.setName("OFFICER");

			// to save in db

			List<Role> roles = List.of(role, role1);
			List<Role> result = roleRepo.saveAll(roles);

			result.forEach(res -> System.out.println(res.getName()));
		} catch (Exception e) {

		}
	}

}
