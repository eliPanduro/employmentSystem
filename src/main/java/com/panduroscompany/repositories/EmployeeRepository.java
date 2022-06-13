package com.panduroscompany.repositories;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.panduroscompany.entity.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
	//To find if an employee with the same first name, middle name, last name and birth date already exists
	@Query("SELECT emp FROM Employee emp WHERE emp.firstname = ?1 AND emp.middlename = ?2 AND emp.lastname = ?3 AND emp.birthdate = ?4")
	public Employee findExistingEmployee(String firstname, String middlename, String lastname, Date birthdate);
	
	/*@Query(value = "SELECT * FROM Employee "
			+ "WHERE ( firstname = :firstname OR :firstname = '')"
			+ "OR ( lastname = :lastname OR :lastname = '' ) "
			+ "OR ( position = :position OR :position = '' ) ", nativeQuery = true)*/
	
	//Using named parameters - to search an employee by first name, last name and/or position
	@Query(value = "SELECT * FROM Employee "
			+ "WHERE ( firstname = :firstname OR :firstname = '' ) "
			+ "AND ( lastname = :lastname OR :lastname = '' ) "
			+ "AND ( position = :position OR :position = '' ) ", nativeQuery = true)
	List<Employee> findEmployee(@Param("firstname") String fname, @Param("lastname") String lname, @Param("position") String pos); 
}
