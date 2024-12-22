package com.citb.app.entities;

public enum MeetingStatus {
	
	UPCOMING("upcoming"),
	COMPLETED("completed"),
	CANCELLED("cancelled");
	
	
	private final String value;
	
	MeetingStatus(String value) {
        this.value = value;
    }
	
	public String getValue() {
	        return value;
	}
	
	
	
}
