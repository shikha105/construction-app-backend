package com.citb.app.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.citb.app.config.AppConstants;
import com.citb.app.entities.Role;
import com.citb.app.entities.User;
import com.citb.app.exceptions.ResourceNotFoundException;
import com.citb.app.payloads.RoleDTO;
import com.citb.app.payloads.UserDTO;
import com.citb.app.repositories.RoleRepo;
import com.citb.app.repositories.UserRepo;
import com.citb.app.services.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private RoleRepo roleRepo;

	@Override
	public UserDTO registerUser(UserDTO userDTO) {

		User user = this.modelMapper.map(userDTO, User.class);
		user.setPassword(passwordEncoder.encode(userDTO.getPassword()));

		Role role = roleRepo.findById(AppConstants.ROLE_APPRENTICE_ID)
				.orElseThrow(() -> new IllegalArgumentException("Role not found"));

		user.setRole(role);
		User registeredUser = userRepo.save(user);

		UserDTO registeredUserDTO = this.modelMapper.map(registeredUser, UserDTO.class);

		registeredUserDTO.setRoleDTO(modelMapper.map(registeredUser.getRole(), RoleDTO.class));
		return registeredUserDTO;
	}

	@Override
	public UserDTO createUser(UserDTO userDTO) {
		User user = this.dtoToUser(userDTO);
		User createdUser = this.userRepo.save(user);

		return this.userToDto(createdUser);
	}

	@Override
	public UserDTO updateUser(UserDTO userDTO, Integer userId) {
		User user = this.userRepo.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
		user.setName(userDTO.getName());
		user.setEmail(userDTO.getEmail());
		user.setPassword(userDTO.getPassword());
		user.setAbout(userDTO.getAbout());
		;

		User updatedUser = this.userRepo.save(user);

		// converting this to DTO
		UserDTO updatedUserDTO = this.userToDto(updatedUser);

		return updatedUserDTO;
	}

	@Override
	public UserDTO getUserById(Integer userId) {

		User user = this.userRepo.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("user", "id", userId));
		return this.userToDto(user);
	}

	@Override
	public List<UserDTO> getAllUsers() {

		List<User> users = this.userRepo.findAll();

		List<UserDTO> userDTOs = users.stream().map(user -> this.userToDto(user)).collect(Collectors.toList());

		return userDTOs;
	}

	@Override
	public void deleteUser(Integer userId) {
		User user = this.userRepo.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
		this.userRepo.delete(user);

	}

	public User dtoToUser(UserDTO userDTO) {

		User user = this.modelMapper.map(userDTO, User.class);

		return user;
	}

	public UserDTO userToDto(User user) {

		UserDTO userDTO = this.modelMapper.map(user, UserDTO.class);

		return userDTO;
	}

}
