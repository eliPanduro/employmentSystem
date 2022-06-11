package com.panduroscompany.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.panduroscompany.entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
	// to search an employee by firstname, lastname and/or position
	
	/*@Query(value = "SELECT * FROM Employee "
			+ "WHERE ( firstname = :firstname OR :firstname = '')"
			+ "OR ( lastname = :lastname OR :lastname = '' ) "
			+ "OR ( position = :position OR :position = '' ) ", nativeQuery = true)*/
	
	@Query(value = "SELECT * FROM Employee "
			+ "WHERE ( firstname = :firstname OR :firstname = '' ) "
			+ "AND ( lastname = :lastname OR :lastname = '' ) "
			+ "AND ( position = :position OR :position = '' ) ", nativeQuery = true)
	List<Employee> findEmployee(@Param("firstname") String fname, @Param("lastname") String lname, @Param("position") String pos);

	//Employee findExistingEmployee(String firstname, String middlename, String lastname, Date birthdate);
}
