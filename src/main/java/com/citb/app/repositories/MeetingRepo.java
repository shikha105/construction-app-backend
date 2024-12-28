package com.citb.app.repositories;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;


import com.citb.app.entities.Meeting;

public interface MeetingRepo extends JpaRepository<Meeting, String>{
    
	Set<Meeting> findDistinctByCreatorIdOrGuests_Id(String creatorId, String guestId);
}
