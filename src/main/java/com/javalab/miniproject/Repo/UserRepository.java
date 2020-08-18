package com.javalab.miniproject.Repo;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.javalab.miniproject.Login.User;


@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
	User findByEmail(String email);
}
