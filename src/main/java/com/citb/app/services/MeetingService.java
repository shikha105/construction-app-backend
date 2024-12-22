package com.citb.app.services;

import java.util.List;

import com.citb.app.payloads.MeetingDTO;

public interface MeetingService {
	

	MeetingDTO createMeeting(MeetingDTO meetingDTO);

	MeetingDTO updateMeeting(MeetingDTO meetingDTO, Integer meetingId);

	MeetingDTO getMeetingDetailsById(Integer meetingId);

	List<MeetingDTO> getAllMeetingsByUserId(Integer userId);

	void cancelMeeting(Integer meetingId);
}
