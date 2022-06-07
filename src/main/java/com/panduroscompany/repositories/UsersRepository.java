package com.panduroscompany.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.panduroscompany.entity.User;

@Repository
public interface UsersRepository extends JpaRepository<User, Integer> {
	 @Query("SELECT us FROM User us WHERE us.email = ?1")
	 public User findByEmail(String email);
}
