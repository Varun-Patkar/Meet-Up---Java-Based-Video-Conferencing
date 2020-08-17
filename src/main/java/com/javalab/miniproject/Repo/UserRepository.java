package com.javalab.miniproject.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.javalab.miniproject.Login.User;


@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
}
