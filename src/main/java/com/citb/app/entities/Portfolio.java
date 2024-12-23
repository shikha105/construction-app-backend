package com.citb.app.entities;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Portfolio {

	@Id
	@Column(length = 50, nullable = false, unique = true)
	private String id;
	
	@Column(length = 20, nullable = false)
	private String title;
	
	@Column(length = 60, nullable = false)
	private String description;
	

	
}
