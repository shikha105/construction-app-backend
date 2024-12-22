package com.citb.app.entities;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name ="users")
@NoArgsConstructor 
@Getter
@Setter
public class User implements UserDetails{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private String name;
	
	@Column(name="email_address", nullable = false, length = 100)
	private String email;
	
	private String password;
	
	private String about;
	
	@ManyToOne
	@JoinColumn(name ="role_id")
	private Role role;

	@ManyToMany
	@JoinTable(name = "user_meeting", joinColumns = @JoinColumn(name="user_id", referencedColumnName ="id"),
	inverseJoinColumns = @JoinColumn(name="meeting_id", referencedColumnName="id")
			)
	@JsonBackReference
	Set<Meeting> meetings = new HashSet<>();
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		if(role !=null)
		return List.of(new SimpleGrantedAuthority(role.getName()));

		return Collections.emptyList();
	}

	@Override
	public String getUsername() {
		return this.email;
	}

	
	
	
	//extra methods I implemented from the source
	
	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return UserDetails.super.isAccountNonExpired();
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return UserDetails.super.isAccountNonLocked();
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return UserDetails.super.isCredentialsNonExpired();
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return UserDetails.super.isEnabled();
	}
	
}
