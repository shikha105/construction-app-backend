package com.citb.app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.citb.app.entities.Role;

public interface RoleRepo extends JpaRepository<Role, String>{

}
