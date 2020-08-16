package com.javalab.miniproject;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class MiniprojectController {
	@GetMapping("/")
	@ResponseBody
	public ModelAndView HomePage(Model model, Principal principal) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("home.html");
        return mav;
	}
}
