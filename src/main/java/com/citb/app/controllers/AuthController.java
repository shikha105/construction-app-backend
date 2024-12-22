package com.citb.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.citb.app.exceptions.ApiException;
import com.citb.app.payloads.JwtRequest;
import com.citb.app.payloads.JwtResponse;
import com.citb.app.payloads.UserDTO;
import com.citb.app.security.JwtHelper;
import com.citb.app.services.UserService;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

	@Autowired
	private JwtHelper jwtHelper;

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserService userService;
	
	@PostMapping("/login")
	public ResponseEntity<JwtResponse> loginUser(@RequestBody JwtRequest request) {

		this.authenticate(request.getUsername(), request.getPassword());

		UserDetails userDetails = this.userDetailsService.loadUserByUsername(request.getUsername());

		String token = this.jwtHelper.generateToken(userDetails);

		JwtResponse response = JwtResponse.builder().jwtToken(token).username(userDetails.getUsername()).build();

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	private void authenticate(String username, String password) {

		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,
				password);

		try {
			authenticationManager.authenticate(authenticationToken);
		} catch (BadCredentialsException e) {
			throw new ApiException("incorrect username or password");
		}

	}
	
	
	@PostMapping("/register")
	public ResponseEntity<UserDTO> registerUser(@RequestBody UserDTO userDTO) {

		UserDTO registeredUser = userService.registerUser(userDTO);
		return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
	}
}
