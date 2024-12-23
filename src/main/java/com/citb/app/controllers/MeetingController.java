package com.citb.app.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.citb.app.payloads.ApiResponse;
import com.citb.app.payloads.MeetingDTO;
import com.citb.app.services.MeetingService;
import com.citb.app.services.PortfolioService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/meetings")
public class MeetingController {

	@Autowired
	private MeetingService meetingService;
	
	//@PreAuthorize("hasRole('ROLE_OFFICER')")
	@PostMapping
	public ResponseEntity<MeetingDTO> createMeeting(@Valid @RequestBody MeetingDTO meetingDTO){
		MeetingDTO createdMeetingDTO =	this.meetingService.createMeeting(meetingDTO);
		
		return new ResponseEntity<>(createdMeetingDTO, HttpStatus.CREATED);
	}
	
	//@PreAuthorize("hasRole('ROLE_OFFICER')")
	@PutMapping("/{meetingId}")
	public ResponseEntity<MeetingDTO> updateMeeting(@Valid @RequestBody MeetingDTO meetingDTO, @PathVariable String meetingId){
		MeetingDTO updatedMeetingDTO =	this.meetingService.updateMeeting(meetingDTO, meetingId);
		
		
		return new ResponseEntity<>(updatedMeetingDTO, HttpStatus.OK);
	}
	
	@GetMapping("/{meetingId}")
	public ResponseEntity<MeetingDTO> getMeetingById(@PathVariable  String meetingId){
		MeetingDTO meetingDTO =	this.meetingService.getMeetingDetailsById(meetingId);
		
		
		return new ResponseEntity<>(meetingDTO, HttpStatus.OK);
	}
	
	@GetMapping("/getAll/{userId}")
	public ResponseEntity<List<MeetingDTO>> getAllMeetingsByUserId(@PathVariable  String userId){
		List<MeetingDTO> meetingDTOs =	this.meetingService.getAllMeetingsByUserId(userId);
		
		
		return new ResponseEntity<>(meetingDTOs, HttpStatus.OK);
	}
	
	@PutMapping("/cancel/{meetingId}")
	public ResponseEntity<ApiResponse> cancelMeeting(@PathVariable String meetingId){
			this.meetingService.cancelMeeting(meetingId);
		return new ResponseEntity<ApiResponse>(new ApiResponse("cancelled successfully", true), HttpStatus.OK);
		
	}
	
}
