package com.citb.app.services;

import java.util.List;

import com.citb.app.payloads.MeetingDTO;

public interface MeetingService {
	

	MeetingDTO createMeeting(MeetingDTO meetingDTO);

	MeetingDTO updateMeeting(MeetingDTO meetingDTO, String meetingId);

	MeetingDTO getMeetingDetailsById(String meetingId);

	List<MeetingDTO> getAllMeetingsByUserId(String userId);

	void cancelMeeting(String meetingId);
}
