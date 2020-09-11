package com.javalab.miniproject;

public class Query {
	private long id;
	private String meetingPassword;
public Query() {
		
	}
	public Query(long id,String meetingPassword) {
		this.id=id;
		this.meetingPassword=meetingPassword;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getMeetingPassword() {
		return meetingPassword;
	}
	public void setMeetingPassword(String meetingPassword) {
		this.meetingPassword = meetingPassword;
	}
	
	
}
