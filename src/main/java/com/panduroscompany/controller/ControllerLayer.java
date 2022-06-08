package com.panduroscompany.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
	
	// Search form
	/*@GetMapping("/searchForm")
	public String searchForm(Model model) {
		model.addAttribute("employee", new Employee());
		return "search";
	}*/
	
	/*@GetMapping("/searchEmployee")
	public String searchEmployee(@RequestParam String firstName, @RequestParam String lastName, @RequestParam String position, Model model, @ModelAttribute("employees") Employee employees ) {
			List<Employee> employee = employeeService.buscarByFirstNameAndLastNameAndPosition(firstName, lastName, position);*/

			/*List<Employee> employees1 = service.buscarFirstName(firstName);
			
			List<Employee> employees2 = service.buscarLastName(lastName);
			
			List<Employee> employees3 = service.buscarPosition(position);
			
			List<Employee> resultList2 = new ArrayList<Employee>(employee);
			
			resultList2.addAll(employees1);
			resultList2.addAll(employees2);
			resultList2.addAll(employees3);
			
			
			resultList2 = resultList2.stream().distinct().collect(Collectors.toList());
			
			
			results=resultList2.size();*/
			//model.addAttribute("employeest",resultList2);
			
		/*return "search";
		
	}*/
	

		// _______search the employee___________
	@RequestMapping("/search/form")
	public String searchForm() {
		return "search";
	}

	// _______search the employee___________
	@GetMapping("/search")
	public String searchEmpl(Model model, String fname, String lname, String pos, RedirectAttributes redirAttrs) {
		List<Employee> list = employeeService.findEmployee(fname, lname, pos);
		if (list != null && list.isEmpty()) {
			redirAttrs.addFlashAttribute("empty", "0 results found.");
			return "redirect:/home";
		} else {
			model.addAttribute("list", list);
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
}
