package com.javalab.miniproject;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


//Repository interface for DB
@Repository
public interface MeetingRepository extends JpaRepository<Meeting, Long>{
	List<Meeting> findByIdAndMeetingPassword(long id, String meetingPassword);
	List<Meeting> findByMeeting_id(long meeting_id);
}
