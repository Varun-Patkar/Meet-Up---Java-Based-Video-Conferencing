package com.javalab.miniproject;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "meeting")
public class Meeting {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	public long id;
	
	public int host_id;
	
	@ElementCollection(targetClass=Integer.class)
	private Set<Integer> participants_id;
	
	public Meeting() {
		host_id=0;
		participants_id=new HashSet<Integer>();
	}
	
	public Meeting(int host_id,Set<Integer> participants_id) {
		this.host_id=host_id;
		this.participants_id=participants_id;
	}
	
	public Meeting(int host_id,int participant_id) {
		this.host_id=host_id;
		addParticipant(participant_id);
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public int getHost_id() {
		return host_id;
	}
	public void setHost_id(int host_id) {
		this.host_id = host_id;
	}
	public Set<Integer> getParticipants_id() {
		return participants_id;
	}
	public void setParticipants_id(Set<Integer> participants_id) {
		this.participants_id = participants_id;
	}
	public void addParticipant(int participant_id) {
		participants_id.add(participant_id);
	}
}
