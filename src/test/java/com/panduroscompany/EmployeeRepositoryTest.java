package com.panduroscompany;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.panduroscompany.entity.Employee;
import com.panduroscompany.repositories.EmployeeRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class EmployeeRepositoryTest {

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private EmployeeRepository employeeRepo;

	public Date StringToDate(String s) {

		java.util.Date result = null;
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			result = dateFormat.parse(s);
		}

		catch (ParseException e) {
			e.printStackTrace();

		}
		return (Date) result;
	}

	@Test
	public void testCreateEmployee() {
		Employee emp = new Employee();
		emp.setFirstname("Jorge");
		emp.setMiddlename("Lorenzo");
		emp.setLastname("Ozuna");
		emp.setBirthdate(StringToDate("1995-10-02"));
		emp.setPosition("Student");

		Employee savedEmployee = employeeRepo.save(emp);
		Employee existEmp = entityManager.find(Employee.class, savedEmployee.getId());

		assertThat(existEmp.getFirstname()).isEqualTo(emp.getFirstname());
		assertThat(existEmp.getLastname()).isEqualTo(emp.getLastname());
	}

	@Test
	public void testFindUserByFirstname() {// Test by firstname
		String fistname = "Melanie";
		String lastname = "";
		String position = "";

		List<Employee> emp = employeeRepo.findEmployee(fistname, lastname, position);
		System.out.println(emp);
		assertNotNull(emp);
		assertTrue(emp.size() == 2);
	}

	@Test
	public void testFindUserBylastname() {// Test by lastname
		String fistname = "";
		String lastname = "Cardenas";
		String position = "";

		List<Employee> emp = employeeRepo.findEmployee(fistname, lastname, position);
		System.out.println(emp);
		assertNotNull(emp);
		assertTrue(emp.size() == 1);
	}

	@Test
	public void testFindUserByPosition() {// Test by position
		String fistname = "";
		String lastname = "";
		String position = "Manager";

		List<Employee> emp = employeeRepo.findEmployee(fistname, lastname, position);
		System.out.println(emp);
		assertNotNull(emp);
		assertTrue(emp.size() == 3);
	}

	@Test
	public void testFindUserByFirstnameAndLastname() {// Test by firstname and lastname
		String fistname = "Abraham";
		String lastname = "Mederos";
		String position = "";

		List<Employee> emp = employeeRepo.findEmployee(fistname, lastname, position);
		System.out.println(emp);
		assertNotNull(emp);
		assertTrue(emp.size() == 1);
	}

	@Test
	public void testFindUserByFirstnameAndLastnameAndPosition() {// Test by firstname, lastname and position
		String fistname = "Kevin";
		String lastname = "Cardenas";
		String position = "Student";

		List<Employee> emp = employeeRepo.findEmployee(fistname, lastname, position);
		System.out.println(emp);
		assertNotNull(emp);
		assertTrue(emp.size() == 1);
	}

}
