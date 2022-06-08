package com.panduroscompany.service;

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
	
	public List<Employee> findEmployee(String fname, String lname, String pos) {
		List<Employee> employees = employeeRepo.findEmployee(fname, lname, pos);
		if(employees != null && employees.isEmpty()) {
			System.out.println("0 results");
		}
		return employees;
	}
	
	/*functions to validate*/
	
	//validate that the employee doesn't exist yet
	/*public Boolean existence(Employee emp) {
		String firstname = emp.getFirstname();
		String middlename = emp.getMiddlename();
		String lastname = emp.getLastname();
		Date birthdate = emp.getBirthdate();
		
		Employee existingEmployee = employeeRepo.findExistingEmployee(firstname, middlename, lastname, birthdate);
		if(existingEmployee == null) {
			return true;
		}
		return false;	
	}*/
	
	}
