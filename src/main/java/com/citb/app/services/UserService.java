package com.citb.app.services;

import java.util.List;

import com.citb.app.payloads.UserDTO;

public interface UserService {

	UserDTO registerUser(UserDTO userDTO);

	UserDTO createUser(UserDTO userDTO);

	UserDTO updateUser(UserDTO userDTO, Integer userId);

	UserDTO getUserById(Integer userId);

	List<UserDTO> getAllUsers();

	void deleteUser(Integer userId);
}
