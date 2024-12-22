package com.citb.app.payloads;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

import com.citb.app.entities.MeetingStatus;
import com.citb.app.entities.User;
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class MeetingDTO {
	
	private int id;
	
	@NotEmpty
	private String title;
	
	@NotEmpty
	private String description;
	
	@NotNull
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate startDate;
	
	@NotNull
	@JsonFormat(pattern = "HH:mm")
	private LocalTime startTime;
	
	@NotNull
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate endDate;
	
	@NotNull
	@JsonFormat(pattern = "HH:mm")
	private LocalTime endTime;
	
	@NotEmpty
	private String location;
	
	@NotEmpty
	Set<UserDTO> guests = new HashSet<>();
	
	
	private MeetingStatus status;
	
	private int creatorId;
	
	
}
