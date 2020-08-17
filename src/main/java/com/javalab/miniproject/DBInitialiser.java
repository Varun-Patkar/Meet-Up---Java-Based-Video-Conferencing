package com.javalab.miniproject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

//Initialiser class
@Component
public class DBInitialiser implements CommandLineRunner{
	private MeetingRepository meetingRepository;
	
	//Initialise DB on startup
	@Autowired
	public DBInitialiser(MeetingRepository patientsRepository) {
		// TODO Auto-generated constructor stub
		this.meetingRepository=patientsRepository;
	}
	
	//Adding Default values to local DB
	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
	}

}

