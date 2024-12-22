package com.citb.app.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.citb.app.payloads.ApiResponse;
import com.citb.app.payloads.UserDTO;
import com.citb.app.services.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
	
	@Autowired
     private UserService userService;
	
	
	@PostMapping
	public ResponseEntity<UserDTO> createUser( @Valid @RequestBody UserDTO userDTO) {
		UserDTO createdUserDTO = this.userService.createUser(userDTO);
		return new ResponseEntity<>(createdUserDTO, HttpStatus.CREATED);
	}

	
	@PutMapping("/{userId}")
	public ResponseEntity<UserDTO> updateUser(@Valid @RequestBody UserDTO userDTO, @PathVariable Integer userId){
		
	UserDTO updatedUserDTO =	this.userService.updateUser(userDTO, userId);
	return new ResponseEntity<>(updatedUserDTO, HttpStatus.OK);
	}
	

//	@DeleteMapping("/{userId}")
//	public ResponseEntity<?> deleteUser(@PathVariable Integer userId){
//		
//	this.userService.deleteUser(userId);
//	return new ResponseEntity<Object>(Map.of("message", "deleted successfully"), HttpStatus.OK);
//	}
	
	@DeleteMapping("/{userId}")
	public ResponseEntity<ApiResponse> deleteUser(@PathVariable Integer userId){
		
	this.userService.deleteUser(userId);
	return new ResponseEntity(new ApiResponse("user deleted successfully", true), HttpStatus.OK);
	}
	
	
	
	@GetMapping
	public ResponseEntity<List<UserDTO>> getAllUsers(){
		
		return  ResponseEntity.ok(this.userService.getAllUsers());
	}

	@GetMapping("/{userId}")
	public ResponseEntity<UserDTO> getUserById(@PathVariable Integer userId){
		
		return  ResponseEntity.ok(this.userService.getUserById(userId));
	}
}
