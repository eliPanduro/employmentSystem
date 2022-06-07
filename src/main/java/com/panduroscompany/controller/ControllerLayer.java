package com.panduroscompany.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.panduroscompany.entity.Employee;
import com.panduroscompany.entity.User;
import com.panduroscompany.repositories.UsersRepository;
import com.panduroscompany.service.EmployeeService;

@Controller
public class ControllerLayer {
	@Autowired
	private UsersRepository userRepo;
	
	@Autowired
	private EmployeeService employeeService;

	// select to register or login
	@GetMapping("")
    public String indexPage() {
        return "index";
    }
	
	// Form to register
	@GetMapping("/register")
	public String showRegistrationForm(Model model) {
	    model.addAttribute("user", new User());
	    return "signUpForm";
	}
	
	//If registration is successful, encode the user's password and save
	@PostMapping("/registrationProcess")
	public String processRegister(User user) {
	    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	    String encodedPassword = passwordEncoder.encode(user.getPassword());
	    user.setPassword(encodedPassword);	     
	    userRepo.save(user);
	    return "registrationCompleted";
	}
	
	// Home page
	@RequestMapping("/home")
	public String homePage() {
		return "home";
	}

	// Form to add an employee
	@RequestMapping("/addEmployee")
	public String formToAddEmployee(Model model) {
		model.addAttribute("employee", new Employee());
		return "addEmployee";
	}

	// Save the employee
	@PostMapping(value = "/save")
	public String saveEmployee(@ModelAttribute("employee") Employee employee) {
		employeeService.save(employee);
		return "redirect:/home";
	}
	
	// Form to search employee
	@RequestMapping("/searchEmployee")
	public String formSearchEmployee() {
		return "search";
	}
		
	// Search an employee or employees
	@GetMapping("/search")
	public String searchEmployees(Model model, @Param("firstname") String firstname, @Param("lastname") String lastname, @Param("position") String position) {
		System.out.println(firstname);
		System.out.println(lastname);
		System.out.println(position);
		List<Employee> list = employeeService.findEmployee(firstname, lastname, position);
		System.out.println("This is the list in search: "+list);
		model.addAttribute("list", list);
		return "search";
	}

	// List all the employees
	@GetMapping("/viewEmployees")
	public String viewEmployees(Model model) {
		List<Employee> listOfEmployees = employeeService.allEmployees();
		model.addAttribute("listOfEmployees", listOfEmployees);
		return "viewEmployees";
	}

	// Edit an existing employee
	@RequestMapping("/edit/{id}")
	public ModelAndView formEditEmployee(@PathVariable(name = "id") Long id) {
		ModelAndView modelView = new ModelAndView("editEmployee");
		Employee employee = employeeService.getInfoById(id);
		modelView.addObject("employee", employee);
		return modelView;
	}
}
