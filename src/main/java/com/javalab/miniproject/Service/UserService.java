package com.javalab.miniproject.Service;

import com.javalab.miniproject.Login.User;

public interface UserService {
	public void saveUser(User user);
	public boolean isUserAlreadyPresent(User user);
	public User findUserByEmail(String email);
	public User findUserById(int id);
}
