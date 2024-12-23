package com.citb.app.payloads;

import java.util.HashSet;
import java.util.Set;

import com.citb.app.entities.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;


@Data
public class RoleDTO {


	
	private String id;
	
	private String name;
	
	
	//removing bcz we dont need the users list in the response
	//Set<UserDTO> users = new HashSet<>();
}
