package com.citb.app;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.citb.app.repositories.UserRepo;

@SpringBootTest
class CitbApp1ApplicationTests {

	@Test
	void contextLoads() {
	}

	@Autowired
	private UserRepo userRepo;
	
	@Test
	public void userRepoTest() {
		
		System.out.println("repo name " + this.userRepo.getClass().getName());
		System.out.println("repo package " + this.userRepo.getClass().getPackageName());
	}
}
