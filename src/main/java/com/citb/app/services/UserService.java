package com.citb.app.services;

import java.util.List;

import com.citb.app.payloads.UserDTO;

public interface UserService {

	UserDTO registerUser(UserDTO userDTO);

	UserDTO createUser(UserDTO userDTO);

	UserDTO updateUser(UserDTO userDTO, String userId);

	UserDTO getUserById(String userId);
	
	UserDTO getUserByUsername(String username);

	List<UserDTO> getAllUsers();

	void deleteUser(String userId);
}
