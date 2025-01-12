package com.citb.app.entities;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
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
	
	@Column(length = 50, nullable = false)
	private String title;
	
	@Column(length = 100, nullable = false)
	private String description;
	
	@Column(name="image_urls", nullable = false )
	@ElementCollection
    private List<String> imageUrls;
    
    
	@ManyToOne
	@JoinColumn(name ="user_id")
	private User user;

	
}
