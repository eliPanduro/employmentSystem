package com.panduroscompany.service;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.panduroscompany.entity.Employee;
import com.panduroscompany.repositories.EmployeeRepository;

@Service
public class EmployeeService {
	
	@Autowired
	private EmployeeRepository employeeRepo;
	
	public List<Employee> allEmployees(){
		return employeeRepo.findAll();
	}
	
	public void save(Employee employee) {
		employeeRepo.save(employee);
	}
	
	public Employee getInfoById(Long id) {
		return employeeRepo.findById(id).get();
	}
	
	//Functions to search an employee and return the matches in a list
	public List<Employee> findEmployee(String fname, String lname, String pos) {
		List<Employee> employees = employeeRepo.findEmployee(fname, lname, pos);
		if(employees != null && employees.isEmpty()) {
			System.out.println("0 results");
		}
		return employees;
	}
	
	/*functions to validate*/
	
	//validate that the employee doesn't exist yet
	public Boolean validationExistingEmp(Employee employee) {
		if(employeeRepo.findExistingEmployee(employee.getFirstname(), employee.getMiddlename(), employee.getLastname(), employee.getBirthdate()) != null) {
			return true;//the employee already exists
		}
		return false;
	}
	
	//validate that there is approximately 18 years difference between the entered date and the current date
	public Boolean validationBirthdate(Date birthd) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");  
		String strBirth = dateFormat.format(birthd);
		
		LocalDate birthdate = LocalDate.parse(strBirth);
	    LocalDate currentDate = LocalDate.now();
	    
	    long numberOfDays = ChronoUnit.DAYS.between(birthdate, currentDate);
	    if (numberOfDays < 6570) {//natural year (365) x 18
	    	return false;
	    }
	    return true;
	}
}
