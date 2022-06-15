package com.panduroscompany;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.panduroscompany.entity.User;
import com.panduroscompany.repositories.UsersRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class UsersRepositoryTest {
	
	@Autowired
	private TestEntityManager entityManager;
	
	@Autowired
	private UsersRepository userRepo;
	
	@Test
	public void testCreateUser() {
		User user = new User();
		user.setEmail("elizabeth.p@ibm.com");
		user.setFirstname("Elizabeth");
		user.setLastname("Panduro");
		user.setPassword("thisismypassword-eli.");
		
		User savedUser = userRepo.save(user);
		User existUser = entityManager.find(User.class, savedUser.getId());
		
		assertThat(existUser.getEmail()).isEqualTo(user.getEmail());
	}
	
	@Test
	public void testFindUserByEmail() {
		String email = "elizabeth.panduro@ibm.com";
		User user = userRepo.findByEmail(email);
		
		assertThat(user).isNotNull();
	}
}
