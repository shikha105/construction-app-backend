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
import com.citb.app.services.MeetingService;
import com.citb.app.services.UserService;

@Service
public class MeetingServiceImpl implements MeetingService{

	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	
	private MeetingRepo meetingRepo;
	
    @Autowired
	private UserDetailsService userDetailsService;
    @Autowired
	private UserService userService;
	
	@Override
	public MeetingDTO createMeeting(MeetingDTO meetingDTO) {

        
        
        //fetching user details and setting the user id as creator id
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        User userInfo = (User) userDetailsService.loadUserByUsername(username);
        Role role = userInfo.getRole();
        
        if(role.getId()!=501) {
        	throw new ApiException("Create Meeting feature is only for Apprentice Officer");
        }
        
        
        Meeting meeting = modelMapper.map(meetingDTO, Meeting.class);
        meeting.setCreatorId(userInfo.getId());
        
        
        
        if (meeting.getStatus() == null) {
            meeting.setStatus(MeetingStatus.UPCOMING);
        }
        
        
        //validating the guest list
        Set<User> validGuests = new HashSet<>();
        for(UserDTO guestDTO: meetingDTO.getGuests()) {
        	
        	UserDTO userDTO =  userService.getUserById(guestDTO.getId());
          
        	User user =  modelMapper.map(userDTO, User.class);
        	
        	if(user!=null) {
        		validGuests.add(user);
        	}
        }
        
        
        
        meeting.setGuests(validGuests);
     
        
        Meeting CreatedMeeting = meetingRepo.save(meeting);
       
        for(User user1: CreatedMeeting.getGuests()) {
        	System.out.println(user1.getRole() +  "get role from the final added guests");
        }
        
		return modelMapper.map(CreatedMeeting, MeetingDTO.class);
	}

	@Override
	public MeetingDTO updateMeeting(MeetingDTO meetingDTO, Integer meetingId) {
		 Meeting meeting = meetingRepo.findById(meetingId).orElseThrow(()-> new ResourceNotFoundException("meeting", "id", meetingId));
	     
		 //added new because error de raha tha 
		 Set<User> guests = meetingDTO.getGuests().stream()
				 .map(userDTO -> modelMapper.map(userDTO, User.class))
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
	public MeetingDTO getMeetingDetailsById(Integer meetingId) {
		Meeting meeting = meetingRepo.findById(meetingId).orElseThrow(()-> new ResourceNotFoundException("meeting", "id", meetingId));
		
		return modelMapper.map(meeting, MeetingDTO.class);
	}

	@Override
	public List<MeetingDTO> getAllMeetingsByUserId(Integer userId) {
		List<Meeting> meetings = this.meetingRepo.findAll();
		
		  //fetching user role and using it
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        User userInfo = (User) userDetailsService.loadUserByUsername(username);
        
        Role role = userInfo.getRole();
        
        Set<Meeting> validMeetings = new HashSet<>();
        if(role.getId() == 501) {
        	
        	for(Meeting mee: meetings) {
        		if(mee.getCreatorId() == userId) {
        			validMeetings.add(mee);
        		}
        	}
        	
        }else if(role.getId() == 502) {
        	
        	for(Meeting mee: meetings) {
        		for(User user: mee.getGuests()) {
        			
        			if(user.getId() == userId) {
        				validMeetings.add(mee);
        			}
        			
        		}
        	}
        }
       
		
		
        List<MeetingDTO> meetingDTOs = validMeetings.stream()
        		.map(meeting -> this.modelMapper.map(meeting, MeetingDTO.class))
        		.collect(Collectors.toList());
		
		return meetingDTOs;
        
		
	}

	@Override
	public void cancelMeeting(Integer meetingId) {
		Meeting meeting = this.meetingRepo.findById(meetingId).orElseThrow(() -> new ResourceNotFoundException("meeting", "id", meetingId));
		
		meeting.setStatus(MeetingStatus.CANCELLED);
		meetingRepo.save(meeting);
	}

}
