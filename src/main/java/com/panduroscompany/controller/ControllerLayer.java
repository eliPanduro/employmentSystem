package com.panduroscompany.controller;

import java.sql.Date;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.panduroscompany.entity.Compensation;
import com.panduroscompany.entity.Employee;
import com.panduroscompany.entity.User;
import com.panduroscompany.repositories.UsersRepository;
import com.panduroscompany.service.CompensationService;
import com.panduroscompany.service.EmployeeService;

@Controller
public class ControllerLayer {
	@Autowired
	private UsersRepository userRepo;
	
	@Autowired
	private EmployeeService employeeService;
	
	@Autowired
	private CompensationService compService;
	
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

	// Save the employee if it passes all validations
	@PostMapping(value = "/save")
	public String saveEmployee(@Valid @ModelAttribute("employee") Employee employee, BindingResult result, RedirectAttributes attribute) {
		if(result.hasErrors()) {
			return "addEmployee";
		}//if there are no errors it will continue validating
		
		Date birthd = employee.getBirthdate();
		
		if(!employeeService.validationExistingEmp(employee)) {//If the employee doesn't exits
			if(employeeService.validationBirthdate(birthd)) {
				employeeService.save(employee);
				attribute.addFlashAttribute("success", "Successfully saved");
				return "redirect:/home";
			}
			else {
				attribute.addFlashAttribute("error", "Error! Birth date entered is invalid, you must be almost or over 18 years old");
				return "redirect:/home";
			}
		}
		attribute.addFlashAttribute("error", "Error! Employee already exists");
		return "redirect:/home";	
	}
	
	//Search employee form
	@RequestMapping("/searchForm")
	public String searchForm() {
		return "search";
	}

	//Call function to search the employee with the data and show it
	@GetMapping("/search")
	public String searchEmpl(Model model, String firstn, String lastn, String position, RedirectAttributes attribute) {
		if(firstn.isEmpty() && lastn.isEmpty() && position.isEmpty()) {
			attribute.addFlashAttribute("error", "Error! add at least one data to filter employees");
			return "redirect:/home";
		}
		List<Employee> listSearch = employeeService.findEmployee(firstn, lastn, position);
		if (listSearch.isEmpty() && listSearch != null) {
			attribute.addFlashAttribute("warning", "0 results found");
			return "redirect:/home";
		} else {
			model.addAttribute("listSearch", listSearch);
			return "search";
		}
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
	
	/*Compensation*/
	
	@GetMapping("/addCompensation/{id}")
	public String addCompensationForm(Model model, @PathVariable(name="id") Long id ) {
		Compensation comp = new Compensation();
		comp.setId_employee(id);
		model.addAttribute("compensation", comp);
		
		return "addCompensation";
	}
	
	@PostMapping("/saveCompensation")
	public String saveCompensation(@ModelAttribute("compensation") Compensation compensation, Model model, RedirectAttributes attribute) {		
		String type = compensation.getType();
		
		if(type.equals("Salary")) {
			System.out.println("Es salario");
			if(compService.isValidDateSalary(compensation)) {//if return true is valid
				compService.save(compensation);
				attribute.addFlashAttribute("success", "Successfully saved");
				return "redirect:/home";
			}
			attribute.addFlashAttribute("error", "Only one salary entry per employee per month can be added");
			return "redirect:/home";
		}
		if(compService.validateTypeAndAmount(compensation)) {//if return true
			System.out.println("Es diferente a salario");
			compService.save(compensation);
			attribute.addFlashAttribute("success", "Successfully saved");
			return "redirect:/home";
		}
		else {
			attribute.addFlashAttribute("error", "Amount must be different from zero or greater than zero");
			return "redirect:/home";
		}
	}
	
	@GetMapping("/compensation/{id}")
	public String viewCompensation(@PathVariable Long id, Model model) {
		List<Compensation> compList = compService.findCompensationById(id); //get all compensations
		model.addAttribute("compList", compList);
		return "compensationHistory";
	}
	
}
