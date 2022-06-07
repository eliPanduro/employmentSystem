package com.panduroscompany.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.panduroscompany.details.userDetails;
import com.panduroscompany.entity.User;
import com.panduroscompany.repositories.UsersRepository;

public class userDetailsService implements UserDetailsService {

	@Autowired
	private UsersRepository userRepo;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepo.findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return new userDetails(user);
	}

}
