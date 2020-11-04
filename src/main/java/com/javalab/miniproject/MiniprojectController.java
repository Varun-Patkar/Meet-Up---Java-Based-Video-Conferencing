package com.javalab.miniproject;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.javalab.miniproject.Login.User;
import com.javalab.miniproject.Service.UserService;
import org.springframework.jdbc.core.JdbcTemplate;
@Controller
public class MiniprojectController {
	@Autowired
	UserService userService;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	long currentMeetingID;
	
	@Autowired
	private MeetingRepository meetingRepository;
	

	boolean host=false;

	@GetMapping("/")
	@ResponseBody
	public ModelAndView HomePage(Model model, Principal principal) {
		ModelAndView mav = new ModelAndView();
		mav.addObject("loggedin",principal!=null);
		Query query=new Query();
		mav.addObject("query",query);
		mav.setViewName("home.html");
        return mav;
	}
	@Bean
	public PasswordEncoder passwordEncoder1() {
	    return new BCryptPasswordEncoder();
	}
	

	@PostMapping("/join-meeting")
	public ModelAndView meeting(@ModelAttribute("query") Query query,Principal principal) {
		host=false;
		if (query.getId()==0L ||query.getMeetingPassword()==null) {
			ModelAndView mav=new ModelAndView();
			mav.addObject("error","Please reenter ID and password");
			mav.setViewName("error.html");
			return mav;
		}
		List<Meeting> byID=meetingRepository.findByMeetingid(query.getId());
		ModelAndView mav=new ModelAndView();
		if (byID==null){
			mav.addObject("error","No such Meeting exists");
			mav.setViewName("error.html");
			return mav;
		}
		else if (!passwordEncoder1().matches(query.getMeetingPassword(),byID.get(0).getMeetingPassword())){
			mav.addObject("error","Wrong Password for the meeting");
			mav.setViewName("error.html");
			return mav;
		}
		else {
			Meeting meeting =byID.get(0);
			meeting.addParticipant(userService.findUserByEmail(principal.getName()).getId());
			meetingRepository.delete(byID.get(0));
			meetingRepository.save(meeting);
			mav=new ModelAndView("redirect:/meeting");
			host=false;
			return mav;
		}
	}
	
	
	
	
	
	@RequestMapping(value = { "/create-meeting" }, method = RequestMethod.GET)
	public ModelAndView create_meeting(Principal principal) {
		ModelAndView mav=new ModelAndView();
		Random rnd = new Random();
	    int number = rnd.nextInt(999999);
	    String pass=String.format("%06d", number);
	    long meeting_id=123456;
	    do {
	    meeting_id=rnd.nextInt(900000)+100000;
	    }while(meetingRepository.findByMeetingid(meeting_id).size()!=0);
	    Meeting meeting = new Meeting(meeting_id,userService.findUserByEmail(principal.getName()).getId());
	    meeting.setUnencrpass(pass);
		meeting.setMeetingPassword(passwordEncoder1().encode(pass));
		currentMeetingID=meeting_id;
		meetingRepository.save(meeting);
		mav=new ModelAndView("redirect:/meeting");
		host=true;
		return mav;
	}
	
	@GetMapping("/about-us")
	@ResponseBody
	public ModelAndView aboutus(Principal principal) {
		ModelAndView mav=new ModelAndView();
		mav.addObject("loggedin",principal!=null);
		mav.setViewName("aboutus.html");
		return mav;
	}
	
	
	@GetMapping("/meeting")
	@ResponseBody
	public ModelAndView meeting(Model model,Principal principal){
		ModelAndView mav=new ModelAndView();
		Meeting meeting=meetingRepository.findByMeetingid(currentMeetingID).get(0);
		mav.addObject("title","Meeting-"+meeting.getId());
		mav.addObject("meeting_id",meeting.getId());
		mav.addObject("meeting_password", meeting.getUnencrpass());
		List<Integer> participants_id=meeting.getParticipants_id();
		List<String> participants_name=new ArrayList<String>();
		for(Integer participant_id:participants_id) {
			participants_name.add(userService.findUserById(participant_id).getName()+" "+userService.findUserById(participant_id).getLastName());
		}
		mav.addObject("host", host);
		mav.addObject("participants_name",participants_name);
		mav.setViewName("meeting.html");
		return mav;
	}
	
	
	@GetMapping("/exit-meeting")
	@ResponseBody
	public ModelAndView exit_meeting(Model model,Principal principal){
		host=false;
		Meeting meeting=meetingRepository.findByMeetingid(currentMeetingID).get(0);
		if(meeting.getParticipants_id().contains(userService.findUserByEmail(principal.getName()).getId())) {
			jdbcTemplate.update("DELETE FROM `miniproject_db`.`meeting_participants_id` WHERE (`meeting_id` = '"+meeting.getId1()+"');");
			return new ModelAndView("redirect:/");
		}
		else if(meeting.getHost_id()==userService.findUserByEmail(principal.getName()).getId()){
			jdbcTemplate.update("DELETE FROM `miniproject_db`.`meeting` WHERE (`id` = '"+meeting.getId1()+"');");
			return new ModelAndView("redirect:/");
		}
		else {
			return new ModelAndView("redirect:/");
		}
	}
	

	@RequestMapping(value = { "/login" }, method = RequestMethod.GET)
	public ModelAndView login(Principal principal) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("loggedin",principal!=null);
		modelAndView.setViewName("login.html"); // resources/template/login.html
		return modelAndView;
	}
	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public ModelAndView register(Principal principal) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("loggedin",principal!=null);
		User user = new User();
		modelAndView.addObject("user", user); 
		modelAndView.setViewName("register.html"); // resources/template/register.html
		return modelAndView;
	}
	
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public ModelAndView registerUser(@Valid User user,BindingResult bindingResult,ModelMap modelMap,Principal principal) {
		ModelAndView mav = new ModelAndView();
		//Check for validations
		mav.addObject("loggedin",principal!=null);
		if (bindingResult.hasErrors()){
			mav.addObject("message", "Please correct errors in form!!");
			modelMap.addAttribute("bindingResult", bindingResult);
		}
		else if(userService.isUserAlreadyPresent(user)){
			//if user already exists display that
			mav.addObject("message", "User already exists!!");
		}
		else {
			//if not than save user
			userService.saveUser(user);
			mav.addObject("message", "User has been registered successfully!!");
		}
		mav.addObject("user", new User());
		mav.setViewName("register");
		return mav;
	}
}
