package com.panduroscompany;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.panduroscompany.entity.Compensation;
import com.panduroscompany.repositories.CompensationRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class CompensationRepositoryTest {

	@Autowired
	private CompensationRepository compRepo;

	@Test
	public void testValidateReturnSalary() {
		String type = "Salary";
		Long id_employee = (long) 1;
		//Long id_employee = (long) 10;should return null

		Compensation existingSalary = compRepo.findExistingSalary(type, id_employee);
		assertNotNull(existingSalary);
	}
	
	@Test
	public void testFindCompensationForEmployeeById() {
		Long id_employee = (long) 1;
		//Long id_employee = (long) 10;should return null
		
		List<Compensation> compensationsById = compRepo.findCompensationsById(id_employee);
		assertNotNull(compensationsById);
	}
}
