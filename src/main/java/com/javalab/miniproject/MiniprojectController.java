package com.javalab.miniproject;

import java.security.Principal;
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



@Controller
public class MiniprojectController {
	@Autowired
	UserService userService;
	
	Meeting currentMeeting=new Meeting();
	
	@Autowired
	private MeetingRepository meetingRepository;
	
	
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
		if (query.getId()==0L ||query.getMeetingPassword()==null) {
			ModelAndView mav=new ModelAndView();
			mav.addObject("error","Please reenter ID and password");
			mav.setViewName("error.html");
			mav.addObject("loggedin",principal!=null);
			return mav;
		}
		List<Meeting> byID=meetingRepository.findById(query.getId());
		ModelAndView mav=new ModelAndView();
		if (byID==null){
			mav.addObject("error","No such Meeting exists");
			mav.setViewName("error.html");
			mav.addObject("loggedin",principal!=null);
			return mav;
		}
		else if (!passwordEncoder1().matches(query.getMeetingPassword(),byID.get(0).getMeetingPassword())){
			mav.addObject("error","Wrong Password for the meeting");
			mav.setViewName("error.html");
			mav.addObject("loggedin",principal!=null);
			return mav;
		}
		else {
			Meeting meeting =byID.get(0);
			meeting.addParticipant(userService.findUserByEmail(principal.getName()).getId());
			currentMeeting=meeting;
			meetingRepository.delete(byID.get(0));
			meetingRepository.save(meeting);
			mav=new ModelAndView("redirect:/meeting");
			mav.addObject("loggedin",principal!=null);
			return mav;
		}
	}
	
	
	
	
	
	@RequestMapping(value = { "/create-meeting" }, method = RequestMethod.GET)
	public ModelAndView create_meeting(Principal principal) {
		ModelAndView mav=new ModelAndView();
		Random rnd = new Random();
	    int number = rnd.nextInt(999999);
	    String pass=String.format("%06d", number);
	    Meeting meeting = new Meeting(userService.findUserByEmail(principal.getName()).getId());
	    meeting.setUnencrpass(pass);
		meeting.setMeetingPassword(passwordEncoder1().encode(pass));
		currentMeeting=meeting;
		meetingRepository.save(meeting);
		mav=new ModelAndView("redirect:/meeting");
		mav.addObject("loggedin",principal!=null);
		return mav;
	}
	
	
	
	
	@GetMapping("/meeting")
	@ResponseBody
	public ModelAndView meeting(Model model,Principal principal){
		ModelAndView mav=new ModelAndView();
		mav.addObject("title","Meeting-"+currentMeeting.getId());
		mav.addObject("meeting_id",currentMeeting.getId());
		mav.addObject("meeting_password", currentMeeting.getUnencrpass());
		mav.setViewName("meeting.html");
		mav.addObject("loggedin",principal!=null);
		return mav;
	}
	
	
	@GetMapping("/exit-meeting")
	@ResponseBody
	public ModelAndView exit_meeting(Model model,Principal principal){
		if(currentMeeting.getParticipants_id().contains(userService.findUserByEmail(principal.getName()).getId())) {
			currentMeeting.removeParticipant(userService.findUserByEmail(principal.getName()).getId());
			return new ModelAndView("redirect:/");
		}
		else if(currentMeeting.getHost_id()==userService.findUserByEmail(principal.getName()).getId()){
			meetingRepository.delete(currentMeeting);
			currentMeeting=new Meeting();
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
		modelAndView.setViewName("login"); // resources/template/login.html
		return modelAndView;
	}
	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public ModelAndView register(Principal principal) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("loggedin",principal!=null);
		User user = new User();
		modelAndView.addObject("user", user); 
		modelAndView.setViewName("register"); // resources/template/register.html
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
