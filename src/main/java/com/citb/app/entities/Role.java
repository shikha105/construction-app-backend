package com.citb.app.entities;

import java.util.HashSet;

import java.util.List;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
@Entity
@AllArgsConstructor
public class Role {

	
	@Id
	private String id;
	
	private String name;
	
	@OneToMany(mappedBy = "role", fetch= FetchType.LAZY, cascade = CascadeType.ALL)
	Set<User> users = new HashSet<>();
}
