package com.javalab.miniproject;

import java.security.Principal;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
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
	
	
	@GetMapping("/")
	@ResponseBody
	public ModelAndView HomePage(Model model, Principal principal) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("home.html");
        return mav;
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
