package com.citb.app.entities;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
public class Meeting {

	@Id
	@Column(length = 50, nullable = false, unique = true)
	private String id;
	
	@Column(length = 20, nullable = false)
	private String title;
	@Column(length = 60, nullable = false)
	private String description;
	
	private LocalDate startDate;
	private LocalTime startTime;
	private LocalDate endDate;
	private LocalTime endTime;
	
	private String location;
	
	@ManyToMany(mappedBy = "meetings")
    @JsonManagedReference
	Set<User> guests = new HashSet<>();
	
	
	private MeetingStatus status;
	
	@Column(name="creator_id", nullable = false, length = 100)
	private String creatorId;

}


