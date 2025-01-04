package com.citb.app;

import java.util.HashSet;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;

import com.citb.app.config.AppConstants;
import com.citb.app.entities.Role;
import com.citb.app.repositories.RoleRepo;

@EntityScan(basePackages = "com.citb.app.entities")
@SpringBootApplication
public class CitbApp1Application implements CommandLineRunner {
	
	private static final Logger logger = LoggerFactory.getLogger(CitbApp1Application.class);
	
	public static void main(String[] args) {
		SpringApplication.run(CitbApp1Application.class, args);
	}
	
	@Autowired
	private RoleRepo roleRepo;

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	@Override
	public void run(String... args) throws Exception {
		try {
            createRolesIfNotExist();
        } catch (Exception e) {
            logger.error("An error occurred while initializing roles", e);
        }
	}

	private void createRolesIfNotExist() {
		 List<Role> roles = List.of(
		            new Role(AppConstants.ROLE_APPRENTICE_ID, "APPRENTICE", new HashSet<>()),
		            new Role(AppConstants.ROLE_OFFICER_ID, "OFFICER", new HashSet<>()),
		            new Role(AppConstants.ROLE_ADMIN_ID, "ADMIN", new HashSet<>())
		        );
		 
		 roles.forEach(role -> {
		        if (!roleRepo.existsById(role.getId())) {
		            roleRepo.save(role);
		            logger.info("Role '{}' has been added to the database.", role.getName());
		        } else {
		            logger.info("Role '{}' already exists in the database.", role.getName());
		        }
		    });
		
	}

}
