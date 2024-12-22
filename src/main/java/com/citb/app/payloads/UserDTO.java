package com.citb.app.payloads;


import lombok.NoArgsConstructor;

import com.citb.app.entities.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UserDTO {
	
	private int id;
	
	@NotEmpty
	@Size(min = 3, message ="name length should be atleast 3 characters")
	private String name;
	
	@Email(message ="email address is not valid ")
	private String email;
	
	@NotEmpty
	@Size(min = 5, max = 10, message= "Password must be between 3 to 10 (inclusive) letters")
	@JsonIgnore
    private String password;
	
	@NotEmpty
	private String about;
	
	private RoleDTO roleDTO;
}
