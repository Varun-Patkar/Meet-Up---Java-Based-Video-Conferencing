package com.javalab.miniproject.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.javalab.miniproject.Login.Role;
import com.javalab.miniproject.Login.User;
import com.javalab.miniproject.Repo.RoleRepository;
import com.javalab.miniproject.Repo.UserRepository;


@Service
public class UserServiceImpl implements UserService {

	@Autowired
	BCryptPasswordEncoder encoder;
	@Autowired
	RoleRepository roleRepository;
	@Autowired
	UserRepository userRepository;
	
//	@Override
	public void saveUser(User user) {
		// TODO Auto-generated method stub
		user.setPassword(encoder.encode(user.getPassword()));
		user.setStatus("VERIFIED");
		Role userRole = roleRepository.findByRole("SITE_USER");
		user.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
		userRepository.save(user);
	}
	@Override
	public boolean isUserAlreadyPresent(User user) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public User findUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}


}

