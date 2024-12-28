package com.citb.app.services.impl;

import java.io.Console;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.citb.app.config.AppConstants;
import com.citb.app.entities.Meeting;
import com.citb.app.entities.MeetingStatus;
import com.citb.app.entities.Portfolio;
import com.citb.app.entities.Role;
import com.citb.app.entities.User;
import com.citb.app.exceptions.ApiException;
import com.citb.app.exceptions.ResourceNotFoundException;
import com.citb.app.payloads.MeetingDTO;
import com.citb.app.payloads.PortfolioDTO;
import com.citb.app.payloads.RoleDTO;
import com.citb.app.payloads.UserDTO;
import com.citb.app.repositories.MeetingRepo;
import com.citb.app.repositories.UserRepo;
import com.citb.app.services.MeetingService;
import com.citb.app.services.UserService;
import com.citb.app.utils.RandomUtil;

import jakarta.transaction.Transactional;

@Service
public class MeetingServiceImpl implements MeetingService {

	@Autowired
	private ModelMapper modelMapper;

	@Autowired

	private MeetingRepo meetingRepo;

	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserRepo userRepo;
	

	@Override
	@Transactional
	public MeetingDTO createMeeting(MeetingDTO meetingDTO) {

		
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = userDetails.getUsername();
		
		User userInfo = (User) userDetailsService.loadUserByUsername(username);
		
		Role role = userInfo.getRole();
		
		if (!role.getId().equals(AppConstants.ROLE_OFFICER_ID) ) {
			
			throw new ApiException("Create Meeting feature is only for Apprentice Officer");
		}

		Meeting meeting = modelMapper.map(meetingDTO, Meeting.class);
		meeting.setCreatorId(userInfo.getId());

		if (meeting.getStatus() == null) {
			meeting.setStatus(MeetingStatus.UPCOMING);
		}

		Set<User> validGuests = new HashSet<>();
		for (UserDTO guestDTO : meetingDTO.getGuests()) {
			
			User user = userRepo.findById(guestDTO.getId()).orElseThrow(() -> new ResourceNotFoundException("user", "id", guestDTO.getId()));

			if (user != null) {
				validGuests.add(user);
			}
		}
		
		meeting.setGuests(validGuests);
		meeting.setId(RandomUtil.randomIdentity("Meeting"));
		Meeting CreatedMeeting = meetingRepo.save(meeting);
		return modelMapper.map(CreatedMeeting, MeetingDTO.class);
	}

	@Override
	public MeetingDTO updateMeeting(MeetingDTO meetingDTO, String meetingId) {
		Meeting meeting = meetingRepo.findById(meetingId)
				.orElseThrow(() -> new ResourceNotFoundException("meeting", "id", meetingId));

		// added new because error de raha tha
		Set<User> guests = meetingDTO.getGuests().stream().map(userDTO -> modelMapper.map(userDTO, User.class))
				.collect(Collectors.toSet());

		meeting.setDescription(meetingDTO.getDescription());
		meeting.setEndDate(meetingDTO.getEndDate());
		meeting.setEndTime(meetingDTO.getEndTime());
		meeting.setStartDate(meetingDTO.getStartDate());
		meeting.setStartTime(meetingDTO.getStartTime());
		meeting.setGuests(guests);
		meeting.setLocation(meetingDTO.getLocation());
		meeting.setTitle(meetingDTO.getTitle());

		Meeting updatedMeeting = meetingRepo.save(meeting);
		return modelMapper.map(updatedMeeting, MeetingDTO.class);
	}

	@Override
	public MeetingDTO getMeetingDetailsById(String meetingId) {
		Meeting meeting = meetingRepo.findById(meetingId)
				.orElseThrow(() -> new ResourceNotFoundException("meeting", "id", meetingId));

		return modelMapper.map(meeting, MeetingDTO.class);
	}

	@Override
	public List<MeetingDTO> getAllMeetingsByUserId(String userId) {

		Set<Meeting> meetings = meetingRepo.findDistinctByCreatorIdOrGuests_Id(userId, userId);

	    List<MeetingDTO> meetingDTOs = meetings.stream()
	            .map(meeting -> this.modelMapper.map(meeting, MeetingDTO.class))
	            .collect(Collectors.toList());

	    return meetingDTOs;
	}

	@Override
	public void cancelMeeting(String meetingId) {
		Meeting meeting = this.meetingRepo.findById(meetingId)
				.orElseThrow(() -> new ResourceNotFoundException("meeting", "id", meetingId));

		meeting.setStatus(MeetingStatus.CANCELLED);
		meetingRepo.save(meeting);
	}

}
