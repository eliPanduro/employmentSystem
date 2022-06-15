package com.panduroscompany.controller;

import java.sql.Date;
import java.text.ParseException;
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
			if(employeeService.validationBirthdate(birthd)) {//If the birth date is valid save employee
				employeeService.save(employee);
				attribute.addFlashAttribute("success", "Successfully saved");
				return "redirect:/home";
			}
			else {//Return to home page and shows an error message for the birth date
				attribute.addFlashAttribute("error", "Error! Birth date entered is invalid, you must be almost or over 18 years old");
				return "redirect:/home";
			}
		}
		//Return to home page and shows an error message - employee exists
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
		
		//If the variables are empty there is no data to search
		if(firstn.isEmpty() && lastn.isEmpty() && position.isEmpty()) {
			attribute.addFlashAttribute("error", "Error! add at least one data to filter employees");
			return "redirect:/home";
		}
		
		List<Employee> listSearch = employeeService.findEmployee(firstn, lastn, position);
		if (listSearch.isEmpty() && listSearch != null) {//if no matches found
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
	public String formEditEmployee(@PathVariable(name = "id") Long id, Model model) {
		Employee employee = employeeService.getInfoById(id);
		model.addAttribute("employee", employee);
		return "editEmployee";
	}
	
	@PostMapping(value = "/update")
	public String updateEmployee(@Valid @ModelAttribute("employee") Employee employee, BindingResult result, RedirectAttributes attribute) {
		if(result.hasErrors()) {
			return "addEmployee";
		}//if there are no errors it will continue validating
		
		Employee actualInfo = employeeService.getInfoById(employee.getId());//I get all data employee
		Date birthd = employee.getBirthdate();
		
		if(!employeeService.validationExistingEmp(employee)) {//It's different birth date and name
			if(employeeService.validationBirthdate(birthd)) {//Valid date of birth
				employeeService.save(employee);
				attribute.addFlashAttribute("success", "Successfully saved");
				return "redirect:/home";
			}
			else {//if returns false it's because the date of birth isn't valid
				attribute.addFlashAttribute("error", "Error! Birth date entered is invalid, you must be almost or over 18 years old");
				return "redirect:/home";
			}
		}
		if(actualInfo.getId().equals(employee.getId())){//Is the same employee
			if(actualInfo.getPosition().equals(employee.getPosition())) {//Position doesn't change
				attribute.addFlashAttribute("error", "Error! No changes were made");
				return "redirect:/home";
			}
			else {//There was a change in position
				employeeService.save(employee);
				attribute.addFlashAttribute("success", "Successfully saved");
				return "redirect:/home";
			}
		}
		attribute.addFlashAttribute("error", "Error! Employee already exists");
		return "redirect:/home";	
	}
	
	/*-------------Compensation-------------*/
	
	//Add compensation
	@GetMapping("/addCompensation/{id}")
	public String addCompensationForm(Model model, @PathVariable(name="id") Long id ) {
		Compensation comp = new Compensation();
		comp.setId_employee(id);
		model.addAttribute("compensation", comp);
		
		return "addCompensation";
	}
	
	//Save compensation
	@PostMapping("/saveCompensation")
	public String saveCompensation(@ModelAttribute("compensation") Compensation compensation, Model model, RedirectAttributes attribute) {		
		String type = compensation.getType();
		
		if(type.equals("Salary")) {
			if(compService.isValidDateSalary(compensation)) {//if return true is valid, the employee doesn't have a salary in the same moth
				compService.save(compensation);
				attribute.addFlashAttribute("success", "Successfully saved");
				return "redirect:/home";
			}
			attribute.addFlashAttribute("error", "Only one salary entry per employee per month can be added");
			return "redirect:/home";
		}
		if(compService.validateTypeAndAmount(compensation)) {//if return true
			compService.save(compensation);
			attribute.addFlashAttribute("success", "Successfully saved");
			return "redirect:/home";
		}
		else {//The amount is incorrect for the type of compensation
			attribute.addFlashAttribute("error", "Amount must be different from zero or greater than zero");
			return "redirect:/home";
		}
	}
	
	//View compensation history
	@GetMapping("/compensation/{id}")
	public String viewCompensation(@PathVariable Long id, Model model, RedirectAttributes attribute) {
		List<Compensation> compList = compService.findCompensationById(id); //get all compensations
		
		if(compList.isEmpty()) {//If compensations aren't found for the employee - shows message
			attribute.addFlashAttribute("warning", "Employee doesn't have compensations");
			return "redirect:/home";
		}
		//If compensations are found for the employee, they are displayed
		model.addAttribute("employee", employeeService.getInfoById(id));
		model.addAttribute("compList", compList);
		return "compensationHistory";
	}
	
	//View compensation history in a date range
	@GetMapping("/compensationHistory/{id}/range")
	public String viewCompensationHistory(Model model, @PathVariable Long id, String startD, String endD, RedirectAttributes attribute) throws ParseException {
		
		//If the variables are empty there is no date range
		if(startD.isEmpty() && endD.isEmpty()) {
			attribute.addFlashAttribute("error", "To filter by date you must enter start date and end date");
			return "redirect:/home";
		}
		List<Compensation> compList = compService.findCompensationByDateRange(startD, endD, id); //get all compensations
		if(compList == null) {//If the list returns null is because end date occurs before start date
			attribute.addFlashAttribute("error", "End date that occurs before start date");
			return "redirect:/home";
		}
		if(compList.isEmpty()) {//if the list is empty means that the employee doesn't have compensation in this date range
			attribute.addFlashAttribute("warning", "Employee doesn't have compensations in this range");
			return "redirect:/home";
		}
		model.addAttribute("employee", employeeService.getInfoById(id));
		model.addAttribute("compList", compList);
		return "compensationHistory";
	}
	
	//View compensation history details for a specific month
	@GetMapping("/compensationHistory/{id}/details/{month}/{year}")
	public String viewCompensationDetails(@PathVariable Long id, @PathVariable String month, @PathVariable int year, Model model) {
		List <Compensation> compList = compService.findCompensationByMonth(id, month, year);
		model.addAttribute("employee", employeeService.getInfoById(id));
		model.addAttribute("compList", compList);
		return "compensationDetails";
	}
	
	//Form to edit compensation
	@RequestMapping("/compensationEdit/{id}")
	public String formEditCompensation(@PathVariable(name = "id") Long id, Model model) {
		Compensation compensation = compService.getInfoCompById(id);
		model.addAttribute("compensation", compensation);
		return "editCompensation";
	}
	
	//Edit compensation
	@PostMapping(value = "/updateCompensation/{id}")
	public String updateCompensation(@ModelAttribute("compensation") Compensation compensation, BindingResult result, RedirectAttributes attribute) {
		String type = compensation.getType();
		String desc = compensation.getDescription();
		
		if(!type.equals("Salary") && desc.isEmpty()) {//If compensation isn't salary, description is required
			attribute.addFlashAttribute("error", "The description is required");
			return "redirect:/home";
		}else {
			Compensation actualComp = compService.getInfoCompById(compensation.getId());
			if(type.equals("Salary")) {//we update changed data and save them
				actualComp.setAmount(compensation.getAmount());
				actualComp.setDescription(compensation.getDescription());
				compService.save(actualComp);
				attribute.addFlashAttribute("success", "Successfully modified");
				return "redirect:/home";
			}
			//If compensation isn't salary
			if(compService.validateTypeAndAmount(compensation)) {//validate amount for the compensation type
				//we update changed data and save them
				actualComp.setAmount(compensation.getAmount());
				actualComp.setDescription(compensation.getDescription());
				compService.save(actualComp);
				attribute.addFlashAttribute("success", "Successfully modified");
				return "redirect:/home";
			}
			else {//If return false the amount is wrong for this compensation type
				attribute.addFlashAttribute("error", "Amount must be different from zero or greater than zero");
				return "redirect:/home";
			}
		}
	}

}
