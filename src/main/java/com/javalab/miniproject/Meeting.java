package com.javalab.miniproject;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "meeting")
public class Meeting {
	
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	
	public long meeting_id;
	
	private int host_id;
	
	@ElementCollection(targetClass=Integer.class)
	private List<Integer> participants_id;
	
	private String meetingPassword;
	
	private String unencrpass;
	
	
	public String getUnencrpass() {
		return unencrpass;
	}

	public void setUnencrpass(String unencrpass) {
		this.unencrpass = unencrpass;
	}

	public String getMeetingPassword() {
		return meetingPassword;
	}

	public void setMeetingPassword(String meetingPassword) {
		this.meetingPassword = meetingPassword;
	}
	public Meeting() {
		
	}
	public Meeting(long meetingid,int host_id) {
		this.meeting_id=meetingid;
		this.host_id=host_id;
		participants_id=new ArrayList<Integer>();
	}
	
	public Meeting(long meetingid,int host_id,List<Integer> participants_id) {
		this.meeting_id=meetingid;
		this.host_id=host_id;
		this.participants_id=participants_id;
	}
	
	public long getId1() {
		return id;
	}
	public void setId1(long id) {
		this.id=id;
	}
	public long getId() {
		return meeting_id;
	}
	public void setId(long meetingid) {
		this.meeting_id = meetingid;
	}
	public int getHost_id() {
		return host_id;
	}
	public void setHost_id(int host_id) {
		this.host_id = host_id;
	}
	public List<Integer> getParticipants_id() {
		return participants_id;
	}
	public void setParticipants_id(List<Integer> participants_id) {
		this.participants_id = participants_id;
	}
	public void addParticipant(int participant_id) {
		participants_id.add(new Integer(participant_id));
	}
	public void removeParticipant(int participant_id) {
		participants_id.remove(new Integer(participant_id));
	}
	public void removeAllParticipants() {
		participants_id=new ArrayList<Integer>();
	}
}
