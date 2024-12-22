package com.citb.app.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.citb.app.entities.User;

public interface UserRepo extends JpaRepository<User, Integer>{

	Optional<User> findByEmail(String email);
}
