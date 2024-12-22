package com.citb.app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.citb.app.entities.Meeting;

public interface MeetingRepo extends JpaRepository<Meeting, Integer>{

}
