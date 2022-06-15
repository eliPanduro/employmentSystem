package com.panduroscompany.service;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.panduroscompany.entity.Compensation;
import com.panduroscompany.repositories.CompensationRepository;

@Service
public class CompensationService {

	@Autowired
	private CompensationRepository compRepo;
	
	public List<Compensation> allCompensations(){
		return compRepo.findAll();
	}
	
	public void save(Compensation comp) {
		compRepo.save(comp);
	}
	
	public Compensation getInfoCompById(Long id) {
		return compRepo.findById(id).get();
	}
	
	//returns the compensation of the employee with that id
	public List<Compensation> findCompensationById(Long id_employee) {
		List<Compensation> compList = compRepo.findCompensationsById(id_employee);
		if(compList != null && compList.isEmpty()) {
			System.out.println("0 results");
		}
		return compList;
	}
	
	//returns the compensations in the date range for the employee
	public List<Compensation> findCompensationByDateRange(String startD, String endD, Long id) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");

		java.util.Date date1 = sdf.parse(startD);
		java.util.Date date2 = sdf.parse(endD);
		java.sql.Date startDate = new Date(date1.getTime());
		java.sql.Date endDate = new Date(date2.getTime());
		
		if(startDate.after(endDate)) {//validates that the start date doesn't occur after the end date
			return null;
		}
		return compRepo.findCompensationByDate(startDate, endDate, id);
	}

	public List<Compensation> findCompensationByMonth(Long id, String month, int year) {
		return compRepo.findDetailsByMonth(id, month, year);
	}
	
	/*functions to validate*/
	
	//Validate the correct amount for the compensation type
	public Boolean validateTypeAndAmount(Compensation comp) {
		String type = comp.getType();
		double amount = comp.getAmount();
		if(type.equals("Bonus") || type.equals("Commission") || type.equals("Allowance")) {
			if(amount > 0) {
				return true;
			}
		}else if(type.equals("Adjustment")){
			if(amount != 0) {
				return true;
			}
		}
		return false;
	}
	
	//validate that an employee already has a salary or not in the same month
	public Boolean isValidDateSalary(Compensation comp) {
		Compensation cm = compRepo.findExistingSalary(comp.getType(), comp.getId_employee());
		if(cm == null) {
			return true;
		}
		
		Date dateMatch = cm.getDatec();
		Date dateAdd = comp.getDatec();

        SimpleDateFormat getFormat = new SimpleDateFormat("yyyy-MM");
        
        String dMatch = getFormat.format(dateMatch);
        String dAdd = getFormat.format(dateAdd);
		
        if(dMatch.equals(dAdd)) {
        	return false;
        }
        return true;
	}
}
