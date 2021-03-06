package com.panduroscompany.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.panduroscompany.entity.User;

public interface UsersRepository extends JpaRepository<User, Integer> {
	//To validate the email user in the database
	 @Query("SELECT us FROM User us WHERE us.email = ?1")
	 public User findByEmail(String email);
}
